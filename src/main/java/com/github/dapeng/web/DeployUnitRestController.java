package com.github.dapeng.web;

import com.github.dapeng.common.Commons;
import com.github.dapeng.common.Resp;
import com.github.dapeng.dto.UnitDto;
import com.github.dapeng.entity.deploy.TDeployUnit;
import com.github.dapeng.entity.deploy.THost;
import com.github.dapeng.entity.deploy.TService;
import com.github.dapeng.entity.deploy.TSet;
import com.github.dapeng.repository.deploy.DeployUnitRepository;
import com.github.dapeng.repository.deploy.HostRepository;
import com.github.dapeng.repository.deploy.ServiceRepository;
import com.github.dapeng.repository.deploy.SetRepository;
import com.github.dapeng.util.DateUtil;
import com.github.dapeng.util.UnitUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author with struy.
 * Create by 2018/7/19 16:14
 * email :yq1724555319@gmail.com
 */
@RestController
@RequestMapping("/api")
@Transactional(rollbackFor = Throwable.class)
public class DeployUnitRestController {

    @Autowired
    SetRepository setRepository;
    @Autowired
    HostRepository hostRepository;
    @Autowired
    ServiceRepository serviceRepository;
    @Autowired
    DeployUnitRepository unitRepository;


    /**
     * @return 部署单元
     */
    @GetMapping("/deploy-units")
    public ResponseEntity<?> deployUnits() {
        List<TDeployUnit> units = unitRepository.findAll();
        return ResponseEntity
                .ok(Resp.of(Commons.SUCCESS_CODE, Commons.LOADED_DATA, units));
    }

    /**
     * 根据Id获取当前部署单元信息
     *
     * @return
     */
    @GetMapping("/deploy-unit/{id}")
    public ResponseEntity<?> deployUnitById(@PathVariable long id) {
        TDeployUnit unit = unitRepository.findOne(id);
        return ResponseEntity
                .ok(Resp.of(Commons.SUCCESS_CODE, Commons.LOADED_DATA, unit));
    }

    /**
     * 获取所有的发布tag
     *
     * @return
     */
    @GetMapping("/deploy-tags")
    public ResponseEntity<?> deployTags() {
        List<TDeployUnit> units = unitRepository.findAll();
        List<String> gitTags = units
                .stream()
                .map(TDeployUnit::getGitTag)
                .distinct()
                .collect(Collectors.toList());
        return ResponseEntity
                .ok(Resp.of(Commons.SUCCESS_CODE, Commons.LOADED_DATA, gitTags));
    }

    /**
     * 添加部署单元
     *
     * @param unitDto
     * @return
     */
    @PostMapping("/deploy-unit/add")
    public ResponseEntity<?> addUnit(@RequestBody UnitDto unitDto) {
        TDeployUnit unit = new TDeployUnit();
        unit.setGitTag(unitDto.getGitTag());
        unit.setImageTag(unitDto.getImageTag());
        unit.setHostId(unitDto.getHostId());
        unit.setServiceId(unitDto.getServiceId());
        unit.setSetId(unitDto.getSetId());
        unit.setEnv(unitDto.getEnv());
        unit.setPorts(unitDto.getPorts());
        unit.setVolumes(unitDto.getVolumes());
        unit.setDockerExtras(unitDto.getDockerExtras());
        unit.setCreatedAt(DateUtil.now());
        unit.setUpdatedAt(DateUtil.now());

        unitRepository.save(unit);
        return ResponseEntity
                .ok(Resp.of(Commons.SUCCESS_CODE, Commons.SAVE_SUCCESS_MSG));
    }

    /**
     * 按照优先级为t_set<t_host<t_service将数据
     *
     * @param setId
     * @param hostId
     * @param serviceId
     * @return
     */
    @GetMapping("/deploy-unit/process-envs")
    public ResponseEntity<?> processEnvs(@RequestParam long setId,
                                         @RequestParam long hostId,
                                         @RequestParam long serviceId) {
        // 优先级为t_set<t_host<t_service

        TSet set = setRepository.getOne(setId);
        THost host = hostRepository.getOne(hostId);
        TService service = serviceRepository.getOne(serviceId);
        Map<String, String> setEnvs = UnitUtil.ofEnv(set.getEnv());
        Map<String, String>  hostEnvs = UnitUtil.ofEnv(host.getEnv());
        Map<String, String>  serviceEnv = UnitUtil.ofEnv(service.getEnv());


        return ResponseEntity
                .ok(Resp.of(Commons.SUCCESS_CODE, Commons.SAVE_SUCCESS_MSG));
    }


}
