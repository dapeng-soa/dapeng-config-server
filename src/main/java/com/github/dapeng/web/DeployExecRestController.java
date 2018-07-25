package com.github.dapeng.web;

import com.github.dapeng.common.Commons;
import com.github.dapeng.common.Resp;
import com.github.dapeng.socket.SocketUtil;
import com.github.dapeng.socket.client.CmdExecutor;
import com.github.dapeng.socket.enums.EventType;
import com.github.dapeng.socket.listener.DeployServerOperations;
import io.socket.client.IO;
import io.socket.client.Socket;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URISyntaxException;
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
}
