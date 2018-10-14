package com.github.dapeng.web.deploy;

import com.github.dapeng.common.Resp;
import com.github.dapeng.dto.ServiceDto;
import com.github.dapeng.entity.deploy.TService;
import com.github.dapeng.repository.deploy.ServiceRepository;
import com.github.dapeng.util.DateUtil;
import com.github.dapeng.util.Check;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
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

    private static Logger LOGGER = LoggerFactory.getLogger(DeployServiceRestController.class);

    @Autowired
    ServiceRepository serviceRepository;

    /**
     * @return
     */
    @GetMapping("/deploy-services")
    public ResponseEntity<?> deployServices(@RequestParam(required = false, defaultValue = "0") int offset,
                                            @RequestParam(required = false, defaultValue = "100000") int limit,
                                            @RequestParam(required = false) String sort,
                                            @RequestParam(required = false, defaultValue = "desc") String order,
                                            @RequestParam(required = false, defaultValue = "") String search) {

        PageRequest pageRequest = new PageRequest
                (offset / limit, limit,
                        new Sort("desc".toUpperCase().equals(order.toUpperCase()) ? Sort.Direction.DESC : Sort.Direction.ASC,
                                null == sort ? "updatedAt" : sort));
        Page<TService> page = serviceRepository.findAll((root, query, cb) -> {
            Path<String> name = root.get("name");
            Path<String> image = root.get("image");
            Path<String> remark = root.get("remark");
            Path<String> labels = root.get("labels");
            List<Predicate> ps = new ArrayList<>();
            ps.add(cb.or(cb.like(name, "%" + search + "%"), cb.like(remark, "%" + search + "%"), cb.like(image, "%" + search + "%"), cb.like(labels, "%" + search + "%")));
            query.where(ps.toArray(new Predicate[ps.size()]));
            return null;
        }, pageRequest);
        return ResponseEntity
                .ok(Resp.of(SUCCESS_CODE, LOADED_DATA, page));
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
    public ResponseEntity<?> addDeployService(@RequestBody ServiceDto serviceDto) {
        if (isEmpty(serviceDto.getName()) || isEmpty(serviceDto.getImage())) {
            return ResponseEntity
                    .ok(Resp.of(ERROR_CODE, "服务名，镜像名不能为空"));
        }
        try {
            Check.hasChinese(serviceDto.getName(), "服务名");
            Check.hasChinese(serviceDto.getImage(), "镜像名");
            List<TService> tServices = serviceRepository.findByName(serviceDto.getName());
            if (!isEmpty(tServices)){
                throw new Exception("已存在同名服务");
            }
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
            LOGGER.info("add deploy-service name [{}]", serviceDto.getName());
            return ResponseEntity
                    .ok(Resp.of(SUCCESS_CODE, SAVE_SUCCESS_MSG));

        } catch (Exception e) {
            LOGGER.error("add deploy-service error [{}]", serviceDto.getName(), e);
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
    public ResponseEntity delDeployService(@PathVariable long id) {
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
    public ResponseEntity<?> editDeploySet(@PathVariable Long id, @RequestBody ServiceDto serviceDto) {
        try {
            if (isEmpty(serviceDto.getName()) || isEmpty(serviceDto.getImage())) {
                return ResponseEntity
                        .ok(Resp.of(ERROR_CODE, SAVE_ERROR_MSG));
            }
            Check.hasChinese(serviceDto.getName(), "服务名");
            Check.hasChinese(serviceDto.getImage(), "镜像名");
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
            LOGGER.info("update deploy-service name [{}]", serviceDto.getName());
            return ResponseEntity
                    .ok(Resp.of(SUCCESS_CODE, COMMON_SUCCESS_MSG));
        } catch (Exception e) {
            LOGGER.error("update deploy-service error [{}]", serviceDto.getName(), e);
            return ResponseEntity
                    .ok(Resp.of(ERROR_CODE, e.getMessage()));
        }
    }


}
