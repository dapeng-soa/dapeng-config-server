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
import com.github.dapeng.socket.SocketUtil;
import com.github.dapeng.socket.enums.EventType;
import com.github.dapeng.util.Composeutil;
import com.github.dapeng.util.DateUtil;
import com.github.dapeng.vo.DeployServiceVo;
import com.github.dapeng.vo.DeploySubHostVo;
import com.github.dapeng.vo.YamlService;
import com.github.dapeng.vo.YamlVo;
import io.socket.client.Socket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
        System.out.println(" step into check real service................");
        socketClient.emit(EventType.GET_SERVER_TIME().name(), serviceNames.get(0));

        // 问询后的返回 Map<HostIP, List<(serviceName, serverTime)>>()
//        socketClient.on(EventType.GET_SERVER_TIME().name(), objects -> {
//            Map<String, List<Map<String, Long>>> serverDeployTimes = (Map<String, List<Map<String, Long>>>) objects[0];
//
//        });
        // 获取四个配置表最后的更新时间，用于对比是否需要更新

        socketClient.on(EventType.SERVER_TIME().name(), objects -> {
            Map<String, Long> serverDeployTimes = (Map<String, Long>) objects[0];
            System.out.println(" serverDeployTimes...............");
        });
        // 过滤-

        // 根据视图类型返回对应的数据结构(test)
        List<DeployServiceVo> voList = new ArrayList<>();
        List<TDeployUnit> units = unitRepository.findAllBySetId(setId);
        if (viewType == 1) {
            LOGGER.info("服务视图");
            units.forEach(unit -> {
                List<DeploySubHostVo> subHostVos = new ArrayList<>();
                DeploySubHostVo subHostVo = new DeploySubHostVo();
                subHostVo.setSetId(unit.getSetId());
                subHostVo.setHostId(unit.getHostId());
                THost tHost = hostRepository.getOne(unit.getHostId());
                subHostVo.setHostIp(IPUtils.transferIp(tHost.getIp()));
                subHostVo.setHostName(tHost.getName());
                subHostVo.setServiceStatus(1);
                subHostVo.setNeedUpdate(true);
                subHostVo.setConfigUpdateBy(DateUtil.now());
                subHostVo.setDeployTime(DateUtil.now());
                subHostVos.add(subHostVo);
                DeployServiceVo deployServiceVo = new DeployServiceVo();
                TService tService = serviceRepository.getOne(unit.getServiceId());
                deployServiceVo.setServiceName(tService.getName());
                deployServiceVo.setServiceId(unit.getServiceId());
                deployServiceVo.setDeploySubHostVos(subHostVos);
                voList.add(deployServiceVo);
            });
        } else {
            LOGGER.info("主机视图");
        }

        return ResponseEntity
                .ok(Resp.of(Commons.SUCCESS_CODE, Commons.LOADED_DATA, voList));
    }

    /**
     * 升级
     */
    @RequestMapping("/deploy/updateRealService")
    public ResponseEntity updateRealService() {
        // 发送升级指令+yaml数据
        return ResponseEntity
                .ok(Resp.of(Commons.SUCCESS_CODE, Commons.COMMON_SUCCESS_MSG));
    }

    /**
     * 停止
     */
    @RequestMapping("/deploy/stopRealService")
    public ResponseEntity stopRealService() {
        // 发送停止指令
        return ResponseEntity
                .ok(Resp.of(Commons.SUCCESS_CODE, Commons.COMMON_SUCCESS_MSG));
    }

    /**
     * 重启
     */
    @RequestMapping("/deploy/restartRealService")
    public ResponseEntity restartRealService() {
        // 发送重启指令
        return ResponseEntity
                .ok(Resp.of(Commons.SUCCESS_CODE, Commons.COMMON_SUCCESS_MSG));
    }

    /**
     * 获取对应的yaml服务实体
     *
     * @param setId
     * @param hostId
     * @param serviceId
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

        //socketClient.emit(EventType.GET_SERVER_TIME().name(),yamlVo.getYamlService().getName());

        //TODO: 过滤

        return ResponseEntity
                .ok(Resp.of(Commons.SUCCESS_CODE, Commons.SAVE_SUCCESS_MSG, yamlVo));
    }
}
