package com.github.dapeng.web;

import com.github.dapeng.common.Commons;
import com.github.dapeng.common.Resp;
import com.github.dapeng.core.helper.DapengUtil;
import com.github.dapeng.core.helper.IPUtils;
import com.github.dapeng.entity.deploy.TDeployUnit;
import com.github.dapeng.entity.deploy.THost;
import com.github.dapeng.entity.deploy.TService;
import com.github.dapeng.entity.deploy.TSet;
import com.github.dapeng.repository.deploy.DeployUnitRepository;
import com.github.dapeng.repository.deploy.HostRepository;
import com.github.dapeng.repository.deploy.ServiceRepository;
import com.github.dapeng.repository.deploy.SetRepository;
import com.github.dapeng.socket.AgentEvent;
import com.github.dapeng.socket.SocketUtil;
import com.github.dapeng.socket.entity.DockerVo;
import com.github.dapeng.socket.entity.DockerYaml;
import com.github.dapeng.socket.entity.Service;
import com.github.dapeng.socket.enums.EventType;
import com.github.dapeng.util.Composeutil;
import com.github.dapeng.util.DateUtil;
import com.github.dapeng.util.UnitUtil;
import com.github.dapeng.vo.*;
import com.google.gson.Gson;
import io.socket.client.Socket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

import static com.github.dapeng.util.NullUtil.isEmpty;

/**
 * @author with struy.
 * Create by 2018/7/25 12:07
 * email :yq1724555319@gmail.com
 */
@RestController
@RequestMapping("/api")
@Transactional(rollbackFor = Throwable.class)
public class DeployExecRestController implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeployExecRestController.class);

    private Socket socketClient = null;

    @Autowired
    SetRepository setRepository;
    @Autowired
    HostRepository hostRepository;
    @Autowired
    ServiceRepository serviceRepository;
    @Autowired
    DeployUnitRepository unitRepository;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent applicationEvent) {
        socketClient = SocketUtil.registerWebSocketClient("127.0.0.1", 9095, "127.0.0.1", "DeployExecSocket");
    }

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
                                           @RequestParam(required = false) Long serviceId,
                                           @RequestParam(defaultValue = "1") Integer viewType) {

        List<String> serviceNames = new ArrayList<>();
        if (isEmpty(serviceId)) {
            List<TService> services = serviceRepository.findAll();
            serviceNames = services
                    .stream()
                    .map(TService::getName)
                    .collect(Collectors.toList());
        } else {
            serviceNames.add(serviceRepository.getOne(serviceId).getName());
        }
        // 发送服务名问询各个节点的【节点IP，(服务->服务时间)】
        LOGGER.info(" step into check real service................");
        socketClient.emit(EventType.GET_SERVER_TIME().name(), serviceNames.get(0));

        // 问询后的返回 Map<HostIP, List<(serviceName, serverTime)>>()
        socketClient.on(EventType.SERVER_TIME().name(), objects -> {
            //Map<String, List<Map<String, Long>>> serverDeployTimes = (Map<String, List<Map<String, Long>>>) objects[0];
            LOGGER.info(" serverDeployTimes...............{}", objects);
        });

        // 过滤-

        // 根据视图类型返回对应的数据结构(test)
        List<DeployServiceVo> serviceVos = new ArrayList<>();
        List<DeployHostVo> hostVos = new ArrayList<>();
        List<TDeployUnit> units = unitRepository.findAllBySetId(setId);

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
                    subHostVo.setServiceStatus(1);
                    subHostVo.setNeedUpdate(true);
                    subHostVo.setConfigUpdateBy(DateUtil.time(lastUpdateAt(u)));
                    subHostVo.setDeployTime(DateUtil.now());
                    subHostVos.add(subHostVo);
                });
                deployServiceVo.setDeploySubHostVos(subHostVos);
                serviceVos.add(deployServiceVo);
            });
            return ResponseEntity
                    .ok(Resp.of(Commons.SUCCESS_CODE, Commons.LOADED_DATA, serviceVos));
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
                    subServiceVo.setConfigUpdateBy(DateUtil.time(lastUpdateAt(u)));
                    subServiceVo.setDeployTime(DateUtil.now());
                    subServiceVo.setServiceStatus(1);
                    subServiceVos.add(subServiceVo);
                });
                deployHostVo.setDeploySubServiceVos(subServiceVos);
                hostVos.add(deployHostVo);
            });
            return ResponseEntity
                    .ok(Resp.of(Commons.SUCCESS_CODE, Commons.LOADED_DATA, hostVos));
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
        Long hostUpdateAt = !isEmpty(hosts) ? hosts.get(0).getCreatedAt().getTime() : 0;
        Long serviceUpdateAt = !isEmpty(serviceList) ? serviceList.get(0).getCreatedAt().getTime() : 0;
        Long unitUpdatAt = unitRepository.getOne(u.getId()).getUpdatedAt().getTime();
        Long[] times = {setUpdateAt, hostUpdateAt, serviceUpdateAt, serviceUpdateAt, unitUpdatAt};
        return Arrays.stream(times).max(Comparator.naturalOrder()).get();
    }


    /**
     * 升级
     */
    @RequestMapping("/deploy/updateRealService")
    public ResponseEntity updateRealService(@RequestParam long unitId) {
        TDeployUnit unit = unitRepository.getOne(unitId);
        TSet set = setRepository.getOne(unit.getSetId());
        THost host = hostRepository.getOne(unit.getHostId());
        TService service = serviceRepository.getOne(unit.getServiceId());
        List<THost> hosts = hostRepository.findBySetId(unit.getSetId());
        YamlService yamlService = Composeutil.processServiceOfUnit(set, host, service,unit);
        yamlService.setExtraHosts(Composeutil.processExtraHosts(hosts));
        DockerVo dockerVo = new DockerVo();
        // 时间应当查询一个最后更新时间发送
        dockerVo.setLastDeployTime(System.currentTimeMillis() / 1000);
        DockerYaml dockerYaml = new DockerYaml();
        dockerYaml.setVersion("2");
        Map<String, Service> serviceMap = new HashMap<>();
        Service service1 = new Service();
        service1.setContainer_name(yamlService.getName());
        service1.setEnvironment(UnitUtil.ofEnv(yamlService.getEnv()));
        service1.setImage(yamlService.getImage());
        service1.setLabels(UnitUtil.ofList(yamlService.getComposeLabels()));
        service1.setPorts(UnitUtil.ofList(yamlService.getPorts()));
        service1.setVolumes(UnitUtil.ofList(yamlService.getVolumes()));
        serviceMap.put(yamlService.getName(), service1);
        dockerYaml.setServices(serviceMap);
        dockerVo.setDockerYaml(dockerYaml);

        // 发送升级指令+yaml数据
        socketClient.emit(EventType.DEPLOY().name(), new Gson().toJson(dockerVo));

        return ResponseEntity
                .ok(Resp.of(Commons.SUCCESS_CODE, Commons.COMMON_SUCCESS_MSG, dockerVo));
    }

    /**
     * 停止
     */
    @RequestMapping("/deploy/stopRealService")
    public ResponseEntity stopRealService(@RequestParam long unitId) {
        // 发送停止指令
        return ResponseEntity
                .ok(Resp.of(Commons.SUCCESS_CODE, Commons.COMMON_SUCCESS_MSG));
    }

    /**
     * 重启
     */
    @RequestMapping("/deploy/restartRealService")
    public ResponseEntity restartRealService(@RequestParam long unitId) {

        // 发送重启指令
        return ResponseEntity
                .ok(Resp.of(Commons.SUCCESS_CODE, Commons.COMMON_SUCCESS_MSG));
    }

    /**
     * 获取对应的yaml服务实体
     *
     * @return
     */
    @GetMapping("/deploy-unit/process-envs")
    public ResponseEntity<?> processService(@RequestParam long setId,
                                            @RequestParam long hostId,
                                            @RequestParam long serviceId) {
        TSet set = setRepository.getOne(setId);
        THost host = hostRepository.getOne(hostId);
        TService service = serviceRepository.getOne(serviceId);
        YamlService yamlService = Composeutil.processService(set, host, service);
        YamlVo yamlVo = new YamlVo();
        yamlVo.setYamlService(yamlService);
        yamlVo.setLastDeployTime(System.currentTimeMillis() / 1000);

        return ResponseEntity
                .ok(Resp.of(Commons.SUCCESS_CODE, Commons.SAVE_SUCCESS_MSG, yamlVo));
    }

    /**
     * 获取对应的yaml服务实体
     *
     * @return
     */
    @GetMapping("/deploy-unit/process-envs/{unitId}")
    public ResponseEntity<?> processService(@PathVariable long unitId) {
        TDeployUnit unit = unitRepository.getOne(unitId);
        TSet set = setRepository.getOne(unit.getSetId());
        THost host = hostRepository.getOne(unit.getHostId());
        TService service = serviceRepository.getOne(unit.getServiceId());
        List<THost> hosts = hostRepository.findBySetId(unit.getSetId());
        YamlService yamlService = Composeutil.processServiceOfUnit(set, host, service,unit);
        yamlService.setExtraHosts(Composeutil.processExtraHosts(hosts));
        YamlVo yamlVo = new YamlVo();
        yamlVo.setYamlService(yamlService);
        yamlVo.setLastDeployTime(System.currentTimeMillis() / 1000);

        return ResponseEntity
                .ok(Resp.of(Commons.SUCCESS_CODE, Commons.SAVE_SUCCESS_MSG, yamlVo));
    }
}
