package com.github.dapeng.web.build;

import com.github.dapeng.common.Resp;
import com.github.dapeng.core.helper.IPUtils;
import com.github.dapeng.dto.BuildTaskDto;
import com.github.dapeng.dto.DependsServiceDto;
import com.github.dapeng.entity.build.TBuildDepends;
import com.github.dapeng.entity.build.TBuildHost;
import com.github.dapeng.entity.build.TBuildTask;
import com.github.dapeng.entity.build.TServiceBuildRecords;
import com.github.dapeng.entity.deploy.TService;
import com.github.dapeng.repository.build.BuildDependsRepository;
import com.github.dapeng.repository.build.BuildHostRepository;
import com.github.dapeng.repository.build.BuildTaskRepository;
import com.github.dapeng.repository.build.ServiceBuildRecordsRepository;
import com.github.dapeng.repository.deploy.ServiceRepository;
import com.github.dapeng.socket.entity.BuildVo;
import com.github.dapeng.socket.entity.DependServiceVo;
import com.github.dapeng.util.BuildServerUtil;
import com.github.dapeng.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static com.github.dapeng.common.Commons.*;
import static com.github.dapeng.util.NullUtil.isEmpty;

/**
 * @author with struy.
 * Create by 2018/9/20 20:49
 * email :yq1724555319@gmail.com
 */

@RestController
@RequestMapping("/api")
public class BuildExecRestController {

    @Autowired
    ServiceRepository serviceRepository;

    @Autowired
    BuildTaskRepository buildTaskRepository;

    @Autowired
    BuildDependsRepository buildDependsRepository;

    @Autowired
    BuildHostRepository buildHostRepository;

    @Autowired
    ServiceBuildRecordsRepository buildRecordsRepository;

    @GetMapping("/build/depends/{serviceId}")
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
                    .ok(Resp.of(ERROR_CODE, e.getMessage()));
        }
    }

    @PostMapping("/build/exec-build/{taskId}")
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity execBuild(@PathVariable Long taskId) {
        try {
            BuildVo buildVo = new BuildVo();
            TBuildTask task = buildTaskRepository.findOne(taskId);
            if (isEmpty(task)) {
                throw new Exception("找不到构建任务");
            }
            TBuildHost host = buildHostRepository.findOne(task.getHostId());
            if (isEmpty(host)) {
                throw new Exception("找不到构建主机");
            }
            TService service = serviceRepository.findOne(task.getServiceId());
            if (isEmpty(service)) {
                throw new Exception("找不到这个服务");
            }
            List<Long> list = new ArrayList<>();
            list.add(0L);
            list.add(1L);
            List<TServiceBuildRecords> buildRecords = buildRecordsRepository.findByAgentHostAndStatusIn(IPUtils.transferIp(host.getHost()), list);
            if (!isEmpty(buildRecords)) {
                throw new Exception("存在正在构建的服务，请等待构建完成");
            }
            List<DependServiceVo> serviceVoList = new ArrayList<>();
            List<TBuildDepends> depends = buildDependsRepository.findByTaskId(taskId);
            if (isEmpty(depends)) {
                throw new Exception("项目不支持构建");
            }
            depends.forEach(x -> {
                DependServiceVo vo = new DependServiceVo();
                vo.setServiceName(x.getServiceName());
                vo.setBranchName(x.getBranchName());
                vo.setBuildOperation(x.getBuildOperation());
                vo.setGitName(x.getGitName());
                vo.setGitURL(x.getGitUrl());
                serviceVoList.add(vo);
            });
            buildVo.setBuildServices(serviceVoList);

            // 存储一条记录
            TServiceBuildRecords records = new TServiceBuildRecords();
            records.setAgentHost(IPUtils.transferIp(host.getHost()));
            records.setBuildService(service.getName());
            records.setTaskId(taskId);
            records.setCreatedAt(DateUtil.now());
            records.setUpdatedAt(DateUtil.now());
            records.setBuildLog("");
            buildRecordsRepository.save(records);

            buildVo.setAgentHost(records.getAgentHost());
            buildVo.setBuildService(records.getBuildService());
            buildVo.setTaskId(records.getTaskId());
            buildVo.setId(records.getId());

            return ResponseEntity
                    .ok(Resp.of(SUCCESS_CODE, LOADED_DATA, buildVo));
        } catch (Exception e) {
            return ResponseEntity
                    .ok(Resp.of(ERROR_CODE, e.getMessage()));
        }
    }

    @GetMapping("/build/get-building-list")
    public ResponseEntity getBuildingList(@RequestParam Long hostId) {
        try {
            TBuildHost one = buildHostRepository.findOne(hostId);
            if (isEmpty(one)) {
                throw new Exception("找不到该构建主机");
            }
            List<TServiceBuildRecords> records = buildRecordsRepository.findByAgentHostOrderByCreatedAtDesc(IPUtils.transferIp(one.getHost()));
            return ResponseEntity
                    .ok(Resp.of(SUCCESS_CODE, LOADED_DATA, records));
        } catch (Exception e) {
            return ResponseEntity
                    .ok(Resp.of(ERROR_CODE, e.getMessage()));
        }
    }

    @PostMapping("/build/add-build-task")
    public ResponseEntity addBuildTask(@RequestBody BuildTaskDto dto) {
        try {
            // 检查是否存在
            // 先存任务
            TService service1 = serviceRepository.findOne(dto.getServiceId());
            TBuildHost host = buildHostRepository.findOne(dto.getHostId());
            TBuildTask task = new TBuildTask();
            task.setHostId(dto.getHostId());
            task.setServiceId(dto.getServiceId());
            task.setCreatedAt(DateUtil.now());
            if (isEmpty(dto.getTaskName())) {
                dto.setTaskName("build-" + host.getName() + "-" + service1.getName());
            }
            task.setTaskName(dto.getTaskName());
            if (isEmpty(dto.getBuildDepends())) {
                throw new Exception("项目不支持构建");
            }
            task.setBranch(dto.getBuildDepends().get(dto.getBuildDepends().size() - 1).getServiceBranch());
            buildTaskRepository.save(task);
            TService service = serviceRepository.getOne(dto.getServiceId());
            List<DependServiceVo> buildServices = BuildServerUtil.getSortedBuildServices(service.getComposeLabels(), new ArrayList());
            dto.getBuildDepends().forEach((DependsServiceDto x) -> {
                for (DependServiceVo buildService : buildServices) {
                    if (buildService.getServiceName().equals(x.getServiceName())) {
                        buildService.setBranchName(x.getServiceBranch());
                    }
                }
            });
            List<TBuildDepends> depends = new ArrayList<>();
            buildServices.forEach(x -> {
                TBuildDepends t = new TBuildDepends();
                t.setTaskId(task.getId());
                t.setServiceName(x.getServiceName());
                t.setBranchName(x.getBranchName());
                t.setGitName(x.getGitName());
                t.setGitUrl(x.getGitURL());
                t.setBuildOperation(x.getBuildOperation());
                depends.add(t);
            });

            buildDependsRepository.save(depends);
            return ResponseEntity
                    .ok(Resp.of(SUCCESS_CODE, "添加成功"));
        } catch (Exception e) {
            return ResponseEntity
                    .ok(Resp.of(ERROR_CODE, e.getMessage()));
        }
    }
}
