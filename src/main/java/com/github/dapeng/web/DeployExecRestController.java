package com.github.dapeng.web;

import com.github.dapeng.common.Commons;
import com.github.dapeng.common.Resp;
import com.github.dapeng.entity.deploy.THost;
import com.github.dapeng.entity.deploy.TService;
import com.github.dapeng.entity.deploy.TSet;
import com.github.dapeng.repository.deploy.DeployUnitRepository;
import com.github.dapeng.repository.deploy.HostRepository;
import com.github.dapeng.repository.deploy.ServiceRepository;
import com.github.dapeng.repository.deploy.SetRepository;
import com.github.dapeng.socket.SocketUtil;
import com.github.dapeng.socket.client.CmdExecutor;
import com.github.dapeng.socket.enums.EventType;
import com.github.dapeng.socket.listener.DeployServerOperations;
import com.github.dapeng.util.Composeutil;
import com.github.dapeng.vo.YamlService;
import com.github.dapeng.vo.YamlVo;
import io.socket.client.IO;
import io.socket.client.Socket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author with struy.
 * Create by 2018/7/25 12:07
 * email :yq1724555319@gmail.com
 */
@RestController
@RequestMapping("/api")
@Transactional(rollbackFor = Throwable.class)
public class DeployExecRestController implements ApplicationListener<ContextRefreshedEvent> {

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
     */
    @RequestMapping("/deploy/checkRealService")
    public ResponseEntity checkRealService(){
        // 问询->返回
        socketClient.emit(EventType.WEB_EVENT().name(), "serverTime");

        // 过滤-
        // 返回unit

        return ResponseEntity
                .ok(Resp.of(Commons.SUCCESS_CODE, Commons.SAVE_SUCCESS_MSG));
    }

    /**
     * 升级
     *
     */
    @RequestMapping("/deploy/updateRealService")
    public ResponseEntity updateRealService(){
        // 发送升级指令+yaml数据
        return ResponseEntity
                .ok(Resp.of(Commons.SUCCESS_CODE, Commons.SAVE_SUCCESS_MSG));
    }

    /**
     * 停止
     *
     */
    @RequestMapping("/deploy/stopRealService")
    public ResponseEntity stopRealService(){
        // 发送停止指令
        return ResponseEntity
                .ok(Resp.of(Commons.SUCCESS_CODE, Commons.SAVE_SUCCESS_MSG));
    }

    /**
     * 重启
     *
     */
    @RequestMapping("/deploy/restartRealService")
    public ResponseEntity restartRealService(){
        // 发送重启指令
        return ResponseEntity
                .ok(Resp.of(Commons.SUCCESS_CODE, Commons.SAVE_SUCCESS_MSG));
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
        yamlVo.setLastDeployTime(System.currentTimeMillis());

        socketClient.emit(EventType.GET_SERVER_TIME().name(),"");

        socketClient.on(EventType.GET_SERVER_TIME().name(), objects -> {
            Map<String, Long> serverDeployTimes = (Map<String, Long>) objects[0];
            System.out.println(" serverDeployTimes...............");

        });

        //TODO: 过滤

        return ResponseEntity
                .ok(Resp.of(Commons.SUCCESS_CODE, Commons.SAVE_SUCCESS_MSG, yamlVo));
    }
}
