package com.github.dapeng.web;

import com.github.dapeng.common.Resp;
import com.github.dapeng.core.helper.IPUtils;
import com.github.dapeng.entity.deploy.TDeployUnit;
import com.github.dapeng.entity.deploy.THost;
import com.github.dapeng.entity.deploy.TService;
import com.github.dapeng.entity.deploy.TSet;
import com.github.dapeng.repository.deploy.DeployUnitRepository;
import com.github.dapeng.repository.deploy.HostRepository;
import com.github.dapeng.repository.deploy.ServiceRepository;
import com.github.dapeng.repository.deploy.SetRepository;
import com.github.dapeng.socket.entity.DeployRequest;
import com.github.dapeng.socket.entity.DeployVo;
import com.github.dapeng.util.Composeutil;
import com.github.dapeng.util.DownloadUtil;
import com.github.dapeng.util.Tools;
import com.github.dapeng.vo.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.*;

import static com.github.dapeng.common.Commons.*;
import static com.github.dapeng.util.NullUtil.isEmpty;

/**
 * @author with struy.
 * Create by 2018/7/25 12:07
 * email :yq1724555319@gmail.com
 */
@RestController
@RequestMapping("/api")
@Transactional(rollbackFor = Throwable.class)
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

    /**
     * 向agent发送问询指令
     *
     * @param setId     环境集id,根据环境筛选主机和服务
     * @param serviceId 服务id,如果有则只问询单个服务
     * @param viewType  视图类型 [1:服务视图] [2:主机视图] 默认主机视图
     * @return
     */
    @RequestMapping("/deploy/checkRealService")
    public ResponseEntity checkRealService(@RequestParam() Long setId,
                                           @RequestParam(defaultValue = "0") Long serviceId,
                                           @RequestParam(defaultValue = "0") Long hostId,
                                           @RequestParam(defaultValue = "1") Integer viewType) {

        // 根据视图类型返回对应的视图数据结构
        List<DeployServiceVo> serviceVos = new ArrayList<>();
        List<DeployHostVo> hostVos = new ArrayList<>();
        List<TDeployUnit> units = !isEmpty(serviceId) ?
                unitRepository.findAllBySetIdAndServiceId(setId, serviceId) :
                !isEmpty(hostId) ? unitRepository.findAllBySetIdAndHostId(setId, hostId) :
                        unitRepository.findAllBySetId(setId);

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
            LOGGER.info("服务视图");
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
                    subHostVos.add(subHostVo);
                });
                deployServiceVo.setDeploySubHostVos(subHostVos);
                serviceVos.add(deployServiceVo);
            });
            return ResponseEntity
                    .ok(Resp.of(SUCCESS_CODE, LOADED_DATA, serviceVos));
        } else {
            LOGGER.info("主机视图");
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
                    subServiceVos.add(subServiceVo);
                });
                deployHostVo.setDeploySubServiceVos(subServiceVos);
                hostVos.add(deployHostVo);
            });
            return ResponseEntity
                    .ok(Resp.of(SUCCESS_CODE, LOADED_DATA, hostVos));
        }
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
        List<TService> serviceList = serviceRepository.findTop1ByIdOrderByUpdatedAtDesc(u.getServiceId());
        Long setUpdateAt = !isEmpty(setList) ? setList.get(0).getUpdatedAt().getTime() : 0;
        Long hostUpdateAt = !isEmpty(hosts) ? hosts.get(0).getUpdatedAt().getTime() : 0;
        Long serviceUpdateAt = !isEmpty(serviceList) ? serviceList.get(0).getUpdatedAt().getTime() : 0;
        Long unitUpdatAt = unitRepository.getOne(u.getId()).getUpdatedAt().getTime();
        Long[] times = {setUpdateAt, hostUpdateAt, serviceUpdateAt, serviceUpdateAt, unitUpdatAt};
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
        List<THost> hosts = hostRepository.findBySetId(unit.getSetId());

        DockerService dockerService1 = Composeutil.processServiceOfUnit(set, host, service, unit);
        dockerService1.setExtra_hosts(Composeutil.processExtraHosts(hosts));
        String composeContext = Composeutil.processComposeContext(dockerService1);

        String ip = IPUtils.transferIp(host.getIp());
        DeployVo dockerVo = new DeployVo();
        dockerVo.setLastModifyTime(lastUpdateAt(unit));
        dockerVo.setServiceName(service.getName());
        dockerVo.setFileContent(composeContext);
        dockerVo.setIp(ip);

        return ResponseEntity
                .ok(Resp.of(SUCCESS_CODE, COMMON_SUCCESS_MSG, dockerVo));
    }

    /**
     * 停止
     */
    @RequestMapping("/deploy/stopRealService")
    public ResponseEntity stopRealService(@RequestParam Long unitId) {
        DeployRequest request = toDeployRequest(unitId);
        LOGGER.info("::send stop service:{}", request);
        return ResponseEntity
                .ok(Resp.of(SUCCESS_CODE, COMMON_SUCCESS_MSG, request));
    }

    /**
     * 重启
     */
    @RequestMapping("/deploy/restartRealService")
    public ResponseEntity restartRealService(@RequestParam Long unitId) {
        DeployRequest request = toDeployRequest(unitId);
        return ResponseEntity
                .ok(Resp.of(SUCCESS_CODE, COMMON_SUCCESS_MSG, request));
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
        List<THost> hosts = hostRepository.findBySetId(unit.getSetId());

        DockerService dockerService1 = Composeutil.processServiceOfUnit(set, host, service, unit);
        dockerService1.setExtra_hosts(Composeutil.processExtraHosts(hosts));
        String composeContext = Composeutil.processComposeContext(dockerService1);

        DeployVo dockerVo = new DeployVo();
        dockerVo.setServiceName(service.getName());
        dockerVo.setFileContent(composeContext);

        return ResponseEntity
                .ok(Resp.of(SUCCESS_CODE, LOADED_DATA, dockerVo));
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
        List<THost> hosts = hostRepository.findBySetId(unit.getSetId());

        DockerService dockerService1 = Composeutil.processServiceOfUnit(set, host, service, unit);
        dockerService1.setExtra_hosts(Composeutil.processExtraHosts(hosts));
        String composeContext = Composeutil.processComposeContext(dockerService1);
        String path = System.getProperty("java.io.tmpdir") + "/" + host.getName() + "_" + service.getName() + ".yml";
        // 将内容写入文件
        Tools.writeStringToFile(path, composeContext);
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


    private DeployRequest toDeployRequest(long unitId) {
        TDeployUnit unit = unitRepository.getOne(unitId);
        THost host = hostRepository.getOne(unit.getHostId());
        TService service = serviceRepository.getOne(unit.getServiceId());

        DeployRequest request = new DeployRequest();
        String ip = IPUtils.transferIp(host.getIp());
        request.setIp(ip);
        request.setServiceName(service.getName());
        return request;
    }
}
