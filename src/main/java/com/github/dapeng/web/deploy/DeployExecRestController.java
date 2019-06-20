package com.github.dapeng.web.deploy;

import com.github.dapeng.common.Resp;
import com.github.dapeng.core.helper.IPUtils;
import com.github.dapeng.entity.deploy.*;
import com.github.dapeng.k8s.yaml.YamlParseUtils;
import com.github.dapeng.repository.deploy.*;
import com.github.dapeng.socket.entity.DeployRequest;
import com.github.dapeng.socket.entity.DeployVo;
import com.github.dapeng.socket.entity.VolumesFile;
import com.github.dapeng.util.*;
import com.github.dapeng.vo.*;
import com.github.dapeng.vo.compose.DockerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

import static com.github.dapeng.common.Commons.*;
import static com.github.dapeng.util.Composeutil.ofList;
import static com.github.dapeng.util.NullUtil.isEmpty;

/**
 * @author with struy.
 * Create by 2018/7/25 12:07
 * email :yq1724555319@gmail.com
 */
@RestController
@RequestMapping("/api")
@Transactional(rollbackFor = Exception.class)
public class DeployExecRestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeployExecRestController.class);


    @Autowired
    SetRepository setRepository;
    @Autowired
    HostRepository hostRepository;
    @Autowired
    ServiceRepository serviceRepository;
    @Autowired
    DeployUnitRepository unitRepository;
    @Autowired
    DeployOpJournalRepository journalRepository;
    @Autowired
    SetServiceEnvRepository subEnvRepository;
    @Autowired
    ServiceFilesRepository serviceFilesRepository;
    @Autowired
    FilesUnitRepository filesUnitRepository;
    @Autowired
    NetworkHostRepository networkHostRepository;
    @Autowired
    NetworkRepository networkRepository;

    /**
     * 向agent发送问询指令
     *
     * @param setId     环境集id,根据环境筛选主机和服务
     * @param serviceId 服务id,如果有则只问询单个服务
     * @param viewType  视图类型 [1:服务视图] [2:主机视图] 默认主机视图
     * @return
     */
    @RequestMapping("/deploy/checkRealService")
    public ResponseEntity checkRealService(@RequestParam(defaultValue = "0") Long setId,
                                           @RequestParam(defaultValue = "0") Long serviceId,
                                           @RequestParam(defaultValue = "0") Long hostId,
                                           @RequestParam(defaultValue = "1") Integer viewType) {

        // 根据视图类型返回对应的视图数据结构
        List<DeployServiceVo> serviceVos = new ArrayList<>();
        List<DeployHostVo> hostVos = new ArrayList<>();

        List<TDeployUnit> units = unitRepository.findAll((root, query, cb) -> {
            Path<Long> setId1 = root.get("setId");
            Path<Long> hostId1 = root.get("hostId");
            Path<Long> serviceId1 = root.get("serviceId");
            Path<Integer> deleted = root.get("deleted");
            List<Predicate> ps = new ArrayList<>();
            if (!isEmpty(setId)) {
                ps.add(cb.equal(setId1, setId));
            }
            if (!isEmpty(hostId)) {
                ps.add(cb.equal(hostId1, hostId));
            }
            if (!isEmpty(serviceId)) {
                ps.add(cb.equal(serviceId1, serviceId));
            }
            ps.add(cb.equal(deleted, NORMAL_STATUS));
            query.where(ps.toArray(new Predicate[ps.size()]));
            return null;
        }, new Sort(Sort.Direction.DESC, "updatedAt"));

        if (viewType == 1) {
            // (serviceId->unit)
            Map<Long, List<TDeployUnit>> map = new HashMap<>(16);

            units.forEach(x -> {
                List<TDeployUnit> list = map.getOrDefault(x.getServiceId(), new ArrayList<>());
                if (isEmpty(list)) {
                    list.add(x);
                    map.put(x.getServiceId(), list);
                } else {
                    list.add(x);
                }
            });
            map.forEach((Long k, List<TDeployUnit> v) -> {
                DeployServiceVo deployServiceVo = new DeployServiceVo();
                TService tService = serviceRepository.getOne(k);
                deployServiceVo.setServiceName(tService.getName());
                deployServiceVo.setServiceId(k);
                List<DeploySubHostVo> subHostVos = new ArrayList<>();
                v.forEach((TDeployUnit u) -> {
                    DeploySubHostVo subHostVo = new DeploySubHostVo();
                    subHostVo.setSetId(u.getSetId());
                    subHostVo.setUnitId(u.getId());
                    subHostVo.setHostId(u.getHostId());
                    THost tHost = hostRepository.getOne(u.getHostId());
                    subHostVo.setHostIp(IPUtils.transferIp(tHost.getIp()));
                    subHostVo.setHostName(tHost.getName());
                    subHostVo.setServiceStatus(3);
                    subHostVo.setNeedUpdate(true);
                    subHostVo.setConfigUpdateBy(lastUpdateAt(u) / 1000);
                    subHostVo.setDeployTime(0L);
                    subHostVo.setContainerName(isEmpty(u.getContainerName()) ? tService.getName() : u.getContainerName());
                    subHostVos.add(subHostVo);
                });
                subHostVos.sort((Comparator.comparingLong(DeploySubHostVo::getConfigUpdateBy).reversed()));
                deployServiceVo.setDeploySubHostVos(subHostVos);
                serviceVos.add(deployServiceVo);
                serviceVos.sort((o1, o2) -> {
                    List<Long> list = new ArrayList<>();
                    for (DeploySubHostVo x : o1.getDeploySubHostVos()) {
                        Long configUpdateBy = x.getConfigUpdateBy();
                        list.add(configUpdateBy);
                    }
                    Long o1Max = Collections.max(list);
                    List<Long> result = new ArrayList<>();
                    for (DeploySubHostVo x : o2.getDeploySubHostVos()) {
                        Long configUpdateBy = x.getConfigUpdateBy();
                        result.add(configUpdateBy);
                    }
                    Long o2Max = Collections.max(result);
                    if (o1Max > o2Max) {
                        return -1;
                    }
                    if (o1Max.equals(o2Max)) {
                        return 0;
                    }
                    return 1;
                });
            });
            return ResponseEntity
                    .ok(Resp.of(SUCCESS_CODE, LOADED_DATA, serviceVos));
        } else {
            // (hostId->unit)
            Map<Long, List<TDeployUnit>> map = new HashMap<>(16);

            units.forEach(x -> {
                List<TDeployUnit> list = map.getOrDefault(x.getHostId(), new ArrayList<>());
                if (isEmpty(list)) {
                    list.add(x);
                    map.put(x.getHostId(), list);
                } else {
                    list.add(x);
                }
            });
            map.forEach((Long k, List<TDeployUnit> v) -> {
                DeployHostVo deployHostVo = new DeployHostVo();
                THost host = hostRepository.getOne(k);
                deployHostVo.setHostId(k);
                deployHostVo.setHostIp(IPUtils.transferIp(host.getIp()));
                deployHostVo.setHostName(host.getName());
                List<DeploySubServiceVo> subServiceVos = new ArrayList<>();
                v.forEach((TDeployUnit u) -> {
                    DeploySubServiceVo subServiceVo = new DeploySubServiceVo();
                    TService tService = serviceRepository.getOne(u.getServiceId());
                    subServiceVo.setServiceName(tService.getName());
                    subServiceVo.setSetId(u.getSetId());
                    subServiceVo.setUnitId(u.getId());
                    subServiceVo.setServiceId(u.getServiceId());
                    subServiceVo.setNeedUpdate(true);
                    subServiceVo.setConfigUpdateBy(lastUpdateAt(u) / 1000);
                    subServiceVo.setDeployTime(0L);
                    subServiceVo.setServiceStatus(3);
                    subServiceVo.setContainerName(isEmpty(u.getContainerName()) ? tService.getName() : u.getContainerName());
                    subServiceVos.add(subServiceVo);
                });
                subServiceVos.sort((Comparator.comparingLong(DeploySubServiceVo::getConfigUpdateBy).reversed()));
                deployHostVo.setDeploySubServiceVos(subServiceVos);
                hostVos.add(deployHostVo);
                hostVos.sort((o1, o2) -> {
                    List<Long> list = new ArrayList<>();
                    for (DeploySubServiceVo x : o1.getDeploySubServiceVos()) {
                        Long configUpdateBy = x.getConfigUpdateBy();
                        list.add(configUpdateBy);
                    }
                    Long o1Max = Collections.max(list);
                    List<Long> result = new ArrayList<>();
                    for (DeploySubServiceVo x : o2.getDeploySubServiceVos()) {
                        Long configUpdateBy = x.getConfigUpdateBy();
                        result.add(configUpdateBy);
                    }
                    Long o2Max = Collections.max(result);
                    if (o1Max > o2Max) {
                        return -1;
                    }
                    if (o1Max.equals(o2Max)) {
                        return 0;
                    }
                    return 1;
                });
            });
            return ResponseEntity
                    .ok(Resp.of(SUCCESS_CODE, LOADED_DATA, hostVos));
        }
    }

    /**
     * 获取配置文件的最新关联和修改时间戳
     *
     * @return
     */
    private Long getFilesUpdateAt(TDeployUnit u) {
        long uId = u.getId();
        List<TFilesUnit> byUnitId = filesUnitRepository.findByUnitId(uId);
        List<Long> latest = new ArrayList<>();
        byUnitId.forEach(x -> {
            latest.add(x.getCreateAt().getTime());
            TServiceFiles one = serviceFilesRepository.getOne(x.getFileId());
            if (!isEmpty(one)) {
                latest.add(one.getUpdatedAt().getTime());
            }
        });
        return latest.stream().max(Comparator.naturalOrder()).orElse(0L);
    }

    /**
     * 获取网络配置的最新时间戳
     *
     * @param u
     * @return
     */
    private Long getNetworkUpdateAt(TDeployUnit u) {
        long hid = u.getHostId();
        List<TNetworkHost> byHostId = networkHostRepository.findByHostId(hid);
        List<Long> latest = new ArrayList<>();
        byHostId.forEach(x -> {
            latest.add(x.getCreatedAt().getTime());
            TNetwork network = networkRepository.findOne(x.getNetId());
            if (!isEmpty(network)) {
                latest.add(network.getUpdatedAt().getTime());
            }
        });
        return latest.stream().max(Comparator.naturalOrder()).orElse(0L);
    }

    /**
     * 最后的更新时间
     * // 获取四个配置表最后的更新时间，用于对比是否需要更新
     *
     * @param u
     * @return
     */
    public long lastUpdateAt(TDeployUnit u) {
        // 检查时间
        List<TSet> setList = setRepository.findTop1ByIdOrderByUpdatedAtDesc(u.getSetId());
        List<THost> hosts = hostRepository.findTop1ByIdOrderByUpdatedAtDesc(u.getHostId());
        List<THost> hosts1 = hostRepository.findTop1ByOrderByUpdatedAtDesc();
        List<TService> serviceList = serviceRepository.findTop1ByIdOrderByUpdatedAtDesc(u.getServiceId());
        List<TSetServiceEnv> subEnvs = subEnvRepository.findTop1BySetIdAndServiceIdOrderByUpdatedAtDesc(u.getSetId(), u.getServiceId());
        Long setUpdateAt = !isEmpty(setList) ? setList.get(0).getUpdatedAt().getTime() : 0;
        Long hostUpdateAt = !isEmpty(hosts) ? hosts.get(0).getUpdatedAt().getTime() : 0;
        Long hostsUpdateAt = !isEmpty(hosts1) ? hosts1.get(0).getUpdatedAt().getTime() : 0;
        Long serviceUpdateAt = !isEmpty(serviceList) ? serviceList.get(0).getUpdatedAt().getTime() : 0;
        Long subEnvUpdateAt = !isEmpty(subEnvs) ? subEnvs.get(0).getUpdatedAt().getTime() : 0;
        Long unitUpdateAt = unitRepository.getOne(u.getId()).getUpdatedAt().getTime();
        Long filesUpdateAt = getFilesUpdateAt(u);
        Long netUpdateAt = getNetworkUpdateAt(u);
        Long[] times = {setUpdateAt, hostUpdateAt, hostsUpdateAt, serviceUpdateAt, serviceUpdateAt, subEnvUpdateAt, filesUpdateAt, netUpdateAt, unitUpdateAt};
        return Arrays.stream(times).max(Comparator.naturalOrder()).get();
    }


    /**
     * 升级
     */
    @RequestMapping("/deploy/updateRealService")
    public ResponseEntity updateRealService(@RequestParam Long unitId) {
        TDeployUnit unit = unitRepository.getOne(unitId);
        TSet set = setRepository.getOne(unit.getSetId());
        THost host = hostRepository.getOne(unit.getHostId());
        TService service = serviceRepository.getOne(unit.getServiceId());
        String composeContext = genComposeContext(set, host, service, unit);
        String ip = IPUtils.transferIp(host.getIp());
        DeployVo dockerVo = new DeployVo();
        dockerVo.setLastModifyTime(lastUpdateAt(unit));
        dockerVo.setServiceName(isEmpty(unit.getContainerName()) ? service.getName() : unit.getContainerName());
        dockerVo.setFileContent(composeContext);
        //生成k8sYaml 内容
        dockerVo.setK8sYamlContent(YamlParseUtils.buildK8sYamlByContext(composeContext));
        dockerVo.setNameSpace(host.getName());

        dockerVo.setIp(ip);
        dockerVo.setVolumesFiles(processVolumesFile(unit));
        journalRepository.saveAndFlush(toOperationJournal(unit, 1, composeContext));
        return ResponseEntity
                .ok(Resp.of(SUCCESS_CODE, COMMON_SUCCESS_MSG, dockerVo));
    }

    @GetMapping("/deploy/diffYaml/{id1}/{id2}")
    public ResponseEntity diffYaml(@PathVariable Long id1, @PathVariable Long id2) {
        TDeployUnit unit = unitRepository.getOne(id1);
        TSet set = setRepository.getOne(unit.getSetId());
        THost host = hostRepository.getOne(unit.getHostId());
        TService service = serviceRepository.getOne(unit.getServiceId());
        TDeployUnit unit1 = unitRepository.getOne(id2);
        TSet set1 = setRepository.getOne(unit1.getSetId());
        THost host1 = hostRepository.getOne(unit1.getHostId());
        TService service1 = serviceRepository.getOne(unit1.getServiceId());
        String composeContext = genComposeContext(set, host, service, unit);
        String composeContext1 = genComposeContext(set1, host1, service1, unit1);
        DiffYamlVo vo = new DiffYamlVo();
        vo.setUnit1(host.getName() + "_" + service.getName());
        vo.setUnit2(host1.getName() + "_" + service1.getName());
        vo.setUpdate1(unit.getUpdatedAt());
        vo.setUpdate2(unit1.getUpdatedAt());
        vo.setYaml1(composeContext);
        vo.setYaml2(composeContext1);
        return ResponseEntity
                .ok(Resp.of(SUCCESS_CODE, LOADED_DATA, vo));
    }

    /**
     * 新的文件挂载处理方式
     * 将部署单元关联的文件信息处理为Volumes参数
     * 外部目录添加固定绝对路径的文件夹，并用文件内容生成的tag作为文件名的一部分,一般为在agent端的固定目录
     *
     * @return
     */
    private List<String> processFiles2Volumes(TDeployUnit unit) {
        long unitId = unit.getId();
        List<TFilesUnit> filesUnits = filesUnitRepository.findByUnitId(unitId);
        List<TServiceFiles> filesList = new ArrayList<>();
        filesUnits.forEach(x -> {
            TServiceFiles file = serviceFilesRepository.findOne(x.getFileId());
            if (!isEmpty(file)) {
                filesList.add(file);
            }
        });
        StringBuilder sb = new StringBuilder();
        filesList.forEach(f -> {
            // 在所有路径前加固定路径
            String rmPath = Tools.rmSuffix(f.getFileExtName());
            if (rmPath.startsWith("/")) {
                rmPath = rmPath.replaceFirst("/", "");
            }
            sb.append("./configs/")
                    .append(rmPath)
                    .append("-")
                    .append(f.getFileTag())
                    .append(Tools.suffix(f.getFileExtName()))
                    .append(":").append(f.getFileName())
                    .append("\n");
        });
        return ofList(sb.toString());
    }

    /**
     * 处理文件内容
     *
     * @return
     */
    private List<VolumesFile> processVolumesFile(TDeployUnit unit) {
        long unitId = unit.getId();
        List<TFilesUnit> filesUnits = filesUnitRepository.findByUnitId(unitId);
        List<VolumesFile> filesList = new ArrayList<>();
        filesUnits.forEach(x -> {
            TServiceFiles file = serviceFilesRepository.findOne(x.getFileId());
            if (!isEmpty(file)) {
                StringBuilder sb = new StringBuilder();
                /// 在所有路径前加固定路径
                String rmPath = Tools.rmSuffix(file.getFileExtName());
                if (rmPath.startsWith("/")) {
                    rmPath = rmPath.replaceFirst("/", "");
                }
                sb.append("./configs/")
                        .append(rmPath)
                        .append("-")
                        .append(file.getFileTag())
                        .append(Tools.suffix(file.getFileExtName()));
                VolumesFile vf = new VolumesFile();
                vf.setFileName(sb.toString());
                vf.setFileContext(file.getFileContext());
                filesList.add(vf);
            }
        });
        return filesList;
    }

    /**
     * 停止
     */
    @RequestMapping("/deploy/stopRealService")
    public ResponseEntity stopRealService(@RequestParam Long unitId) {
        DeployRequest request = toDeployRequest(unitId);
        TDeployUnit unit = unitRepository.getOne(unitId);
        // 写流水，停止没有yml
        journalRepository.saveAndFlush(toOperationJournal(unit, 3, ""));
        return ResponseEntity
                .ok(Resp.of(SUCCESS_CODE, COMMON_SUCCESS_MSG, request));
    }

    /**
     * 重启
     */
    @RequestMapping("/deploy/restartRealService")
    public ResponseEntity restartRealService(@RequestParam Long unitId) {
        DeployRequest request = toDeployRequest(unitId);
        // 写流水，重启没有yml
        TDeployUnit unit = unitRepository.getOne(unitId);

        journalRepository.saveAndFlush(toOperationJournal(unit, 2, ""));
        return ResponseEntity
                .ok(Resp.of(SUCCESS_CODE, COMMON_SUCCESS_MSG, request));
    }

    /**
     * 回滚
     *
     * @param jid
     * @return
     */
    @RequestMapping("/deploy/rollbackRealService")
    public ResponseEntity rollbackRealService(@RequestParam Long jid) {
        TOperationJournal journal = journalRepository.findOne(jid);
        TDeployUnit unit = unitRepository.findOne(journal.getUnitId());
        THost host = hostRepository.findOne(journal.getHostId());
        TService service = serviceRepository.findOne(journal.getServiceId());
        String ip = IPUtils.transferIp(host.getIp());
        DeployVo dockerVo = new DeployVo();
        dockerVo.setLastModifyTime(journal.getCreatedAt().getTime());
        dockerVo.setServiceName(isEmpty(unit) || isEmpty(unit.getContainerName()) ? service.getName() : unit.getContainerName());
        dockerVo.setFileContent(journal.getYml());
        dockerVo.setK8sYamlContent(YamlParseUtils.buildK8sYamlByContext(journal.getYml()));
        dockerVo.setIp(ip);
        dockerVo.setNameSpace(host.getName());

        TOperationJournal newJournal = new TOperationJournal();
        if (isEmpty(unit)) {
            newJournal.setUnitId(0);
        } else {
            newJournal.setUnitId(unit.getId());
        }
        newJournal.setOpFlag(4);
        newJournal.setCreatedAt(DateUtil.now());
        newJournal.setCreatedBy(SecurityUtil.me());
        newJournal.setDiff("");
        newJournal.setYml(journal.getYml());
        newJournal.setServiceId(journal.getServiceId());
        newJournal.setHostId(journal.getHostId());
        newJournal.setSetId(journal.getSetId());
        newJournal.setImageTag(journal.getImageTag());
        newJournal.setGitTag(journal.getGitTag());

        journalRepository.saveAndFlush(newJournal);
        LOGGER.info(":::rollbackRealService []");
        return ResponseEntity
                .ok(Resp.of(SUCCESS_CODE, COMMON_SUCCESS_MSG, dockerVo));
    }

    /**
     * @param unit
     * @return
     */
    private TOperationJournal toOperationJournal(TDeployUnit unit, int opFlag, String yml) {
        // 写流水
        TOperationJournal journal = new TOperationJournal();
        journal.setUnitId(unit.getId());
        journal.setGitTag(unit.getGitTag());
        journal.setImageTag(unit.getImageTag());
        journal.setSetId(unit.getSetId());
        journal.setHostId(unit.getHostId());
        journal.setServiceId(unit.getServiceId());
        journal.setYml(yml);
        journal.setCreatedAt(DateUtil.now());
        journal.setCreatedBy(SecurityUtil.me());
        // 升级
        journal.setOpFlag(opFlag);
        journal.setDiff("");
        return journal;
    }


    /**
     * 获取对应的yaml服务实体
     *
     * @return
     */
    @GetMapping("/deploy-unit/process-envs/{unitId}")
    public ResponseEntity<?> processService(@PathVariable Long unitId) {
        TDeployUnit unit = unitRepository.getOne(unitId);
        TSet set = setRepository.getOne(unit.getSetId());
        THost host = hostRepository.getOne(unit.getHostId());
        TService service = serviceRepository.getOne(unit.getServiceId());
        DeployVo dockerVo = new DeployVo();
        dockerVo.setServiceName(isEmpty(unit.getContainerName()) ? service.getName() : unit.getContainerName());


        //生成K8s Yaml
        dockerVo.setFileContent(genComposeContext(set, host, service, unit));
//        dockerVo.setFileContent(YamlParseUtils.buildK8sYamlByContext(genComposeContext(set, host, service, unit)));

        return ResponseEntity.ok(Resp.of(SUCCESS_CODE, LOADED_DATA, dockerVo));
    }

    /**
     * 下载yaml
     *
     * @param unitId
     * @param response
     * @return
     */
    @GetMapping("/deploy-unit/download-yml/{unitId}")
    public ResponseEntity downloadYml(@PathVariable Long unitId, HttpServletResponse response) {
        TDeployUnit unit = unitRepository.getOne(unitId);
        TSet set = setRepository.getOne(unit.getSetId());
        THost host = hostRepository.getOne(unit.getHostId());
        TService service = serviceRepository.getOne(unit.getServiceId());
        String path = System.getProperty("java.io.tmpdir") + "/" + host.getName() + "_" + (isEmpty(unit.getContainerName()) ? service.getName() : unit.getContainerName()) + VersionUtil.version() + ".yml";
        // 将内容写入文件
        // Tools.writeStringToFile(path, genComposeContext(set, host, service, unit));
        Tools.writeStringToFile(path, YamlParseUtils.buildK8sYamlByContext(genComposeContext(set, host, service, unit)));
        // 下载
        try {
            DownloadUtil.downLoad(path, response, false);
        } catch (Exception e) {
            LOGGER.error("下载出错了");
        }
        return ResponseEntity
                .ok(Resp.of(SUCCESS_CODE, COMMON_SUCCESS_MSG));
    }

    /**
     * 事件请求通用结构体
     *
     * @param unitId
     * @return
     */
    @GetMapping("/deploy-unit/event_rep/{unitId}")
    public ResponseEntity eventRep(@PathVariable Long unitId) {
        return ResponseEntity
                .ok(Resp.of(SUCCESS_CODE, LOADED_DATA, toDeployRequest(unitId)));
    }

    @GetMapping("/deploy-unit/event_rep")
    public ResponseEntity eventRep2(@RequestParam Long hostId,
                                    @RequestParam Long serviceId) {
        THost host = hostRepository.findOne(hostId);
        TService service = serviceRepository.findOne(serviceId);
        DeployRequest request = new DeployRequest();
        String ip = isEmpty(host) ? "-" : IPUtils.transferIp(host.getIp());
        request.setIp(ip);
        request.setNamespace(host.getName());
        request.setServiceName(isEmpty(service) ? "-" : service.getName());
        return ResponseEntity
                .ok(Resp.of(SUCCESS_CODE, LOADED_DATA, request));
    }


    private DeployRequest toDeployRequest(Long unitId) {
        TDeployUnit unit = unitRepository.getOne(unitId);
        THost host = hostRepository.getOne(unit.getHostId());
        TService service = serviceRepository.getOne(unit.getServiceId());
        DeployRequest request = new DeployRequest();
        String ip = IPUtils.transferIp(host.getIp());
        request.setIp(ip);
        request.setNamespace(host.getName());
        request.setServiceName(isEmpty(unit.getContainerName()) ? service.getName() : unit.getContainerName());
        return request;
    }

    /**
     * 通过unit获取yaml配置
     *
     * @param set
     * @param host
     * @param service
     * @param unit
     * @return
     */
    private String genComposeContext(TSet set, THost host, TService service, TDeployUnit unit) {
        List<THost> hosts = hostRepository.findBySetId(unit.getSetId());
        List<TSetServiceEnv> envList = subEnvRepository.findTop1BySetIdAndServiceIdOrderByUpdatedAtDesc(unit.getSetId(), unit.getServiceId());
        TSetServiceEnv setSubEnv = new TSetServiceEnv();
        if (!isEmpty(envList)) {
            setSubEnv = envList.get(0);
        }
        DockerService dockerService1 = Composeutil.processServiceOfUnit(set, host, service, unit, setSubEnv);
        List<String> files2Volumes = processFiles2Volumes(unit);
        dockerService1.setVolumes(Composeutil.mergeList(files2Volumes, dockerService1.getVolumes()));
        dockerService1.setExtra_hosts(Composeutil.processExtraHosts(hosts));
        List<TNetworkHost> byHostId = networkHostRepository.findByHostId(host.getId());
        String netName = "";
        if (!isEmpty(byHostId)) {
            TNetwork network = networkRepository.findOne(byHostId.get(0).getNetId());
            if (!isEmpty(network)) {
                netName = network.getNetworkName();
            }
        }
        return Composeutil.processComposeContext(dockerService1, netName);
    }
}
