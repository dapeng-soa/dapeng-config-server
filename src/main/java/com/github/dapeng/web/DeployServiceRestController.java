package com.github.dapeng.web;

import com.github.dapeng.common.Resp;
import com.github.dapeng.dto.ServiceDto;
import com.github.dapeng.entity.deploy.TService;
import com.github.dapeng.repository.deploy.ServiceRepository;
import com.github.dapeng.util.DateUtil;
import com.github.dapeng.util.DeployCheck;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.github.dapeng.common.Commons.*;
import static com.github.dapeng.util.NullUtil.isEmpty;

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
                .ok(Resp.of(SUCCESS_CODE, LOADED_DATA, services));
    }

    /**
     * 根据Id获取当前服务信息信息
     *
     * @return
     */
    @GetMapping("/deploy-service/{id}")
    public ResponseEntity<?> deployServiceById(@PathVariable long id) {
        TService service = serviceRepository.findOne(id);
        return ResponseEntity
                .ok(Resp.of(SUCCESS_CODE, LOADED_DATA, service));
    }

    @PostMapping("/deploy-service/add")
    public ResponseEntity<?> addService(@RequestBody ServiceDto serviceDto) {
        if (isEmpty(serviceDto.getName()) || isEmpty(serviceDto.getImage())) {
            return ResponseEntity
                    .ok(Resp.of(ERROR_CODE, SAVE_ERROR_MSG));
        }
        try {
            DeployCheck.hasChinese(serviceDto.getName(), "服务名");
            DeployCheck.hasChinese(serviceDto.getImage(), "镜像名");
            TService service = new TService();
            service.setName(serviceDto.getName());
            service.setRemark(serviceDto.getRemark());
            service.setImage(serviceDto.getImage());
            service.setEnv(serviceDto.getEnv());
            service.setComposeLabels(serviceDto.getComposeLabels());
            service.setVolumes(serviceDto.getVolumes());
            service.setPorts(serviceDto.getPorts());
            service.setDockerExtras(serviceDto.getDockerExtras());
            service.setLabels(serviceDto.getLabels());
            service.setCreatedAt(DateUtil.now());
            service.setUpdatedAt(DateUtil.now());
            serviceRepository.save(service);

            return ResponseEntity
                    .ok(Resp.of(SUCCESS_CODE, SAVE_SUCCESS_MSG));

        } catch (Exception e) {
            return ResponseEntity
                    .ok(Resp.of(ERROR_CODE, e.getMessage()));
        }
    }

    /**
     * 删除
     *
     * @param id
     * @return
     */
    @PostMapping("/deploy-service/del/{id}")
    public ResponseEntity delService(@PathVariable long id) {
        serviceRepository.delete(id);
        return ResponseEntity
                .ok(Resp.of(SUCCESS_CODE, DEL_SUCCESS_MSG));
    }

    /**
     * 修改
     *
     * @param id
     * @param serviceDto
     * @return
     */
    @PostMapping(value = "/deploy-service/edit/{id}")
    public ResponseEntity<?> updateSet(@PathVariable Long id, @RequestBody ServiceDto serviceDto) {
        try {
            if (isEmpty(serviceDto.getName()) || isEmpty(serviceDto.getImage())) {
                return ResponseEntity
                        .ok(Resp.of(ERROR_CODE, SAVE_ERROR_MSG));
            }
            DeployCheck.hasChinese(serviceDto.getName(), "服务名");
            DeployCheck.hasChinese(serviceDto.getImage(), "镜像名");
            TService service = serviceRepository.findOne(id);
            service.setName(serviceDto.getName());
            service.setRemark(serviceDto.getRemark());
            service.setImage(serviceDto.getImage());
            service.setEnv(serviceDto.getEnv());
            service.setComposeLabels(serviceDto.getComposeLabels());
            service.setVolumes(serviceDto.getVolumes());
            service.setPorts(serviceDto.getPorts());
            service.setDockerExtras(serviceDto.getDockerExtras());
            service.setLabels(serviceDto.getLabels());
            service.setUpdatedAt(DateUtil.now());
            serviceRepository.save(service);
            return ResponseEntity
                    .ok(Resp.of(SUCCESS_CODE, COMMON_SUCCESS_MSG));
        } catch (Exception e) {
            return ResponseEntity
                    .ok(Resp.of(ERROR_CODE, e.getMessage()));
        }
    }


}
