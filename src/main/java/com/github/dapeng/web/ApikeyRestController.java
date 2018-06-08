package com.github.dapeng.web;

import com.github.dapeng.common.CommonRepose;
import com.github.dapeng.common.Commons;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author with struy.
 * Create by 2018/6/7 20:39
 * email :yq1724555319@gmail.com
 */
@RestController
@Transactional(rollbackFor = Throwable.class)
@RequestMapping("/api")
public class ApikeyRestController {

    @GetMapping(value = "/apikey")
    public ResponseEntity getApiKeys() {

        List datas = new ArrayList();
        return ResponseEntity
                .ok(CommonRepose.of(Commons.SUCCESS_CODE, datas));
    }
}
