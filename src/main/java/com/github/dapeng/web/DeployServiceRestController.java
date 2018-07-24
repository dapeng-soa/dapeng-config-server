package com.github.dapeng.web;

import com.github.dapeng.common.Commons;
import com.github.dapeng.common.Resp;
import com.github.dapeng.dto.ServiceDto;
import com.github.dapeng.entity.deploy.TService;
import com.github.dapeng.repository.deploy.ServiceRepository;
import com.github.dapeng.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author with struy.
 * Create by 2018/7/19 16:14
 * email :yq1724555319@gmail.com
 */
@RestController
@RequestMapping("/api")
@Transactional(rollbackFor = Throwable.class)
public class DeployServiceRestController {

    @Autowired
    ServiceRepository serviceRepository;

    /**
     * @return
     */
    @GetMapping("/deploy-services")
    public ResponseEntity<?> deployServices() {
        List<TService> services = serviceRepository.findAll();
        return ResponseEntity
                .ok(Resp.of(Commons.SUCCESS_CODE, Commons.LOADED_DATA,services));
    }

    /**
     * 根据Id获取当前服务信息信息
     * @return
     */
    @GetMapping("/deploy-service/{id}")
    public ResponseEntity<?> deployServiceById(@PathVariable long id) {
        TService service = serviceRepository.findOne(id);
        return ResponseEntity
                .ok(Resp.of(Commons.SUCCESS_CODE, Commons.LOADED_DATA,service));
    }

    @PostMapping("/deploy-service/add")
    public ResponseEntity<?> addService(@RequestBody ServiceDto serviceDto) {
        TService service = new TService();
        service.setName(serviceDto.getName());
        service.setRemark(serviceDto.getRemark());
        service.setImage(serviceDto.getImage());
        service.setEnv(serviceDto.getEnv());
        service.setComposeLabels(serviceDto.getEnv());
        service.setVolumes(serviceDto.getVolumes());
        service.setPorts(serviceDto.getPorts());
        service.setDockerExtras(serviceDto.getDockerExtras());
        service.setLabels(serviceDto.getLabels());
        service.setCreatedAt(DateUtil.now());
        service.setUpdatedAt(DateUtil.now());
        serviceRepository.save(service);

        return ResponseEntity
                .ok(Resp.of(Commons.SUCCESS_CODE, Commons.SAVE_SUCCESS_MSG));
    }
}
