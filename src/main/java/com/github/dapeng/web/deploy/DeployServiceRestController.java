package com.github.dapeng.web.deploy;

import com.github.dapeng.common.Resp;
import com.github.dapeng.dto.ServiceDto;
import com.github.dapeng.entity.deploy.TService;
import com.github.dapeng.repository.deploy.DeployUnitRepository;
import com.github.dapeng.repository.deploy.ServiceRepository;
import com.github.dapeng.util.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import static com.github.dapeng.common.Commons.*;
import static com.github.dapeng.util.NullUtil.isEmpty;

/**
 * @author with struy.
 * Create by 2018/7/19 16:14
 * email :yq1724555319@gmail.com
 */
@RestController
@RequestMapping("/api")
@Transactional(rollbackFor = Exception.class)
public class DeployServiceRestController {

    private static Logger LOGGER = LoggerFactory.getLogger(DeployServiceRestController.class);

    @Autowired
    ServiceRepository serviceRepository;

    @Autowired
    DeployUnitRepository unitRepository;

    @Autowired
    Gson gson;

    /**
     * @return
     */
    @GetMapping("/deploy-services")
    public ResponseEntity<?> deployServices(@RequestParam(required = false, defaultValue = "0") int offset,
                                            @RequestParam(required = false, defaultValue = "100000") int limit,
                                            @RequestParam(required = false) String sort,
                                            @RequestParam(required = false, defaultValue = "desc") String order,
                                            @RequestParam(required = false, defaultValue = "") String search,
                                            @RequestParam(required = false, defaultValue = "") String tag) {

        PageRequest pageRequest = new PageRequest
                (offset / limit, limit,
                        new Sort("desc".toUpperCase().equals(order.toUpperCase()) ? Sort.Direction.DESC : Sort.Direction.ASC,
                                null == sort ? "updatedAt" : sort));
        Page<TService> page = serviceRepository.findAll((root, query, cb) -> {
            Path<String> name = root.get("name");
            Path<String> image = root.get("image");
            Path<String> remark = root.get("remark");
            Path<String> labels = root.get("labels");
            Path<Integer> deleted = root.get("deleted");
            List<Predicate> ps = new ArrayList<>();
            ps.add(cb.or(cb.like(name, "%" + search + "%"), cb.like(remark, "%" + search + "%"), cb.like(image, "%" + search + "%")));
            ps.add(cb.like(labels, "%" + tag + "%"));
            ps.add(cb.equal(deleted, NORMAL_STATUS));
            query.where(ps.toArray(new Predicate[ps.size()]));
            return null;
        }, pageRequest);
        return ResponseEntity
                .ok(Resp.of(SUCCESS_CODE, LOADED_DATA, page));
    }

    /**
     * 导出服务
     *
     * @param ids
     * @param response
     * @return
     */
    @GetMapping("/deploy-service/export/{ids}")
    public ResponseEntity exportDeployService(@PathVariable String ids, HttpServletResponse response) {
        ArrayList<Long> list = gson.fromJson(ids, new TypeToken<List<Long>>() {
        }.getType());
        List<TService> services = serviceRepository.findAll(list);
        List<ServiceDto> dtos = new ArrayList<>();
        services.forEach(x -> {
            ServiceDto dto = new ServiceDto();
            dto.setName(x.getName());
            dto.setRemark(x.getRemark());
            dto.setEnv(x.getEnv());
            dto.setImage(x.getImage());
            dto.setComposeLabels(x.getComposeLabels());
            dto.setDockerExtras(x.getDockerExtras());
            dto.setPorts(x.getPorts());
            dto.setVolumes(x.getVolumes());
            dto.setLabels(x.getLabels());
            dtos.add(dto);
        });
        String json = gson.toJson(dtos);
        String path = System.getProperty("java.io.tmpdir") + "/" + "services_" + VersionUtil.version() + ".json";
        Tools.writeStringToFile(path, json);
        try {
            DownloadUtil.downLoad(path, response, false);
        } catch (Exception e) {
            LOGGER.error("下载出错了");
        }
        return ResponseEntity
                .ok(Resp.of(SUCCESS_CODE, LOADED_DATA, json));
    }

    /**
     * 批量导入服务
     *
     * @param file
     * @return
     */
    @PostMapping("/deploy-service/import")
    public ResponseEntity importService(@RequestParam MultipartFile file) {
        String fileName;
        if (!file.isEmpty()) {
            try {
                fileName = file.getOriginalFilename();
                LOGGER.info("import service by file:::" + fileName);
                String json = new String(file.getBytes());
                List<ServiceDto> dtos = gson.fromJson(json, new TypeToken<List<ServiceDto>>() {
                }.getType());
                AtomicInteger failCounter = new AtomicInteger(0);
                // 批量导入，过滤同名和不规范服务
                dtos.forEach(serviceDto -> {
                    try {
                        Check.hasChinese(serviceDto.getName(), "服务名");
                        Check.hasChinese(serviceDto.getImage(), "镜像名");
                        List<TService> tServices = serviceRepository.findByName(serviceDto.getName());
                        if (!isEmpty(tServices)) {
                            throw new Exception(serviceDto.getName() + "::已存在同名服务");
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
                    } catch (Exception e) {
                        LOGGER.warn("batch import service error:::{}", e.getMessage());
                        failCounter.incrementAndGet();
                    }
                });
                return ResponseEntity
                        .ok(Resp.of(SUCCESS_CODE, "导入成功" + (dtos.size() - failCounter.get()) + "个，失败" + failCounter.get() + "个"));
            } catch (Exception e) {
                return ResponseEntity
                        .ok(Resp.of(ERROR_CODE, "导入出错,解析文件异常"));
            }
        } else {
            return ResponseEntity
                    .ok(Resp.of(ERROR_CODE, "导入出错"));
        }
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
            if (!isEmpty(tServices)) {
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
        try {
            boolean b = unitRepository.existsAllByServiceId(id);
            if (b) {
                throw new Exception("不能删除,此服务存在部署单元");
            }
            TService service = serviceRepository.findOne(id);
            if (isEmpty(service)) {
                throw new Exception("找不到此服务");
            }
            service.setDeleted(DELETED_STATUS);
            return ResponseEntity
                    .ok(Resp.of(SUCCESS_CODE, DEL_SUCCESS_MSG));
        } catch (Exception e) {
            return ResponseEntity
                    .ok(Resp.of(ERROR_CODE, e.getMessage()));
        }
    }


    /**
     * 获取tags
     *
     * @return
     */
    @GetMapping(value = "/deploy-service/service-tags")
    public ResponseEntity deployServiceTags() {
        List<TService> services = serviceRepository.findAll();
        Set<String> tags = new HashSet<>();
        services.forEach(x -> {
            String[] ts = x.getLabels().split(",");
            for (String t : ts) {
                if (!NullUtil.isEmpty(t)) {
                    tags.add(t);
                }
            }
        });
        return ResponseEntity
                .ok(Resp.of(SUCCESS_CODE, LOADED_DATA, tags));
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
