package com.github.dapeng.web.build;

import com.github.dapeng.common.Resp;
import com.github.dapeng.entity.deploy.TService;
import com.github.dapeng.repository.deploy.ServiceRepository;
import com.github.dapeng.util.BuildServerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

import static com.github.dapeng.common.Commons.LOADED_DATA;
import static com.github.dapeng.common.Commons.SUCCESS_CODE;
import static com.github.dapeng.util.NullUtil.isEmpty;

/**
 * @author with struy.
 * Create by 2018/9/20 20:49
 * email :yq1724555319@gmail.com
 */

@RestController
@RequestMapping("/api")
@Transactional(rollbackFor = Throwable.class)
public class BuildExecRestController {

    @Autowired
    ServiceRepository serviceRepository;

    @RequestMapping("/build/depends/{serviceId}")
    public ResponseEntity getDepends(@PathVariable Long serviceId) {
        try {
            TService service = serviceRepository.getOne(serviceId);
            if (isEmpty(service)) {
                throw new Exception("没有该服务,无法获取依赖关系");
            }
            List buildServices = BuildServerUtil.getSortedBuildServices(service.getComposeLabels(), new ArrayList());
            return ResponseEntity
                    .ok(Resp.of(SUCCESS_CODE, LOADED_DATA, buildServices));
        } catch (Exception e) {
            return ResponseEntity
                    .ok(Resp.of(SUCCESS_CODE, LOADED_DATA, e.getMessage()));
        }
    }
}
