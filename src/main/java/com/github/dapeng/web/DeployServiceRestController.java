package com.github.dapeng.web;

import com.github.dapeng.common.Commons;
import com.github.dapeng.common.Resp;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author with struy.
 * Create by 2018/7/19 16:14
 * email :yq1724555319@gmail.com
 */
@RestController
@RequestMapping("/api")
@Transactional(rollbackFor = Throwable.class)
public class DeployServiceRestController {
    /**
     *
     * @return
     */
    @GetMapping("/deploy-services")
    public ResponseEntity<?> deployServices() {
        return ResponseEntity
                .ok(Resp.of(Commons.SUCCESS_CODE, Commons.LOADED_DATA));
    }
}
