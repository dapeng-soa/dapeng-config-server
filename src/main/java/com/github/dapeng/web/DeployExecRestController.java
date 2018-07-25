package com.github.dapeng.web;

import com.github.dapeng.common.Commons;
import com.github.dapeng.common.Resp;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author with struy.
 * Create by 2018/7/25 12:07
 * email :yq1724555319@gmail.com
 */
@RestController
@RequestMapping("/api")
@Transactional(rollbackFor = Throwable.class)
public class DeployExecRestController {
    /**
     * 向agent发送问询指令
     *
     */
    @RequestMapping("/deploy/checkRealService")
    public ResponseEntity checkRealService(){
        // 问询->返回
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
