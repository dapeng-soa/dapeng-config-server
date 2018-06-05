package com.github.dapeng.web;

import com.github.dapeng.common.CommonRepose;
import com.github.dapeng.common.Commons;
import com.github.dapeng.core.metadata.Service;
import com.github.dapeng.openapi.cache.ServiceCache;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author with struy.
 * Create by 2018/6/2 15:08
 * email :yq1724555319@gmail.com
 */
@RestController
@RequestMapping("/api")
@Transactional(rollbackFor = Throwable.class)
public class ServiceController {
    /**
     * 获取服务缓存列表
     * @return
     */
    @GetMapping(value = "/services")
    public ResponseEntity<?> serviceList(){
        Map<String, Service> services = ServiceCache.getServices();
        List<String> list = new ArrayList(16);
        services.forEach((k, v) -> {
            list.add(v.namespace + "." + v.name);
        });
        return ResponseEntity.ok(CommonRepose.of(Commons.SUCCESS_CODE,list));
    }
}
