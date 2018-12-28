package com.github.dapeng.web.build;

import com.github.dapeng.common.Resp;
import com.github.dapeng.core.helper.IPUtils;
import com.github.dapeng.entity.build.TServiceBuildRecords;
import com.github.dapeng.entity.deploy.TDeployUnit;
import com.github.dapeng.entity.deploy.THost;
import com.github.dapeng.entity.deploy.TService;
import com.github.dapeng.entity.deploy.TSet;
import com.github.dapeng.repository.build.ServiceBuildRecordsRepository;
import com.github.dapeng.repository.deploy.DeployUnitRepository;
import com.github.dapeng.repository.deploy.HostRepository;
import com.github.dapeng.repository.deploy.ServiceRepository;
import com.github.dapeng.repository.deploy.SetRepository;
import com.github.dapeng.socket.entity.BuildVo;
import com.github.dapeng.socket.entity.DependServiceVo;
import com.github.dapeng.util.BuildServerUtil;
import com.github.dapeng.util.DateUtil;
import com.github.dapeng.vo.BuildTaskVo;
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
@Transactional(rollbackFor = Exception.class)
public class BuildExecRestController {

    @Autowired
    ServiceRepository serviceRepository;

    @Autowired
    ServiceBuildRecordsRepository buildRecordsRepository;

    @Autowired
    SetRepository setRepository;

    @Autowired
    DeployUnitRepository unitRepository;

    @Autowired
    HostRepository hostRepository;

    /**
     * 构建主机来自环境集
     * 部署节点来自部署单元
     *
     * @param unitId
     * @return
     */
    @PostMapping("/build/exec-build/{unitId}")
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity execBuild(@PathVariable Long unitId) {
        try {
            BuildVo buildVo = new BuildVo();
            TDeployUnit unit = unitRepository.findOne(unitId);
            if (isEmpty(unit)) {
                throw new Exception("找不到构建任务,请刷新重试");
            }
            TSet set = setRepository.findOne(unit.getSetId());
            THost deployHost = hostRepository.findOne(unit.getHostId());
            TService service = serviceRepository.findOne(unit.getServiceId());
            if (isEmpty(set)) {
                throw new Exception("找不到这个环境集(部署单元所属环境集找不到)");
            }
            THost buildHost = hostRepository.findOne(set.getBuildHost());
            // TODO 没有指定默认使用本机？
            if (isEmpty(buildHost)) {
                throw new Exception("找不到构建主机(在所属环境中指定)");
            }
            if (isEmpty(deployHost)) {
                throw new Exception("找不到部署节点(部署单元部署节点不存在)");
            }
            if (isEmpty(service)) {
                throw new Exception("找不到当前构建的服务");
            }

            // 当前构建主机是否存在构建？
            List<Long> list = new ArrayList<>();
            list.add(BUILD_INIT);
            list.add(BUILD_ING);
            List<TServiceBuildRecords> buildRecords = buildRecordsRepository.findByAgentHostAndStatusIn(IPUtils.transferIp(buildHost.getIp()), list);
            if (!isEmpty(buildRecords)) {
                throw new Exception("存在正在构建的服务，请等待构建完成");
            }
            // 解析依赖，验证是否可以构建
            List<DependServiceVo> depends = getServiceBuildDepends(set, service);
            if (isEmpty(depends)) {
                throw new Exception("项目不支持构建");
            }
            buildVo.setBuildServices(depends);

            // 存储一条记录
            TServiceBuildRecords records = new TServiceBuildRecords();
            records.setAgentHost(IPUtils.transferIp(buildHost.getIp()));
            records.setBuildService(service.getName());
            records.setTaskId(unitId);
            records.setCreatedAt(DateUtil.now());
            records.setUpdatedAt(DateUtil.now());
            records.setBuildLog("");
            buildRecordsRepository.save(records);

            buildVo.setAgentHost(records.getAgentHost());
            buildVo.setBuildService(records.getBuildService());
            buildVo.setTaskId(records.getTaskId());
            buildVo.setId(records.getId());
            buildVo.setImageName(service.getImage());
            buildVo.setDeployHost(IPUtils.transferIp(deployHost.getIp()));

            return ResponseEntity
                    .ok(Resp.of(SUCCESS_CODE, LOADED_DATA, buildVo));
        } catch (Exception e) {
            return ResponseEntity
                    .ok(Resp.of(ERROR_CODE, e.getMessage()));
        }
    }


    /**
     * 查找构建任务列表，包含正在运行的，和历史的
     * 1.可根据环境集筛选(一个环境集只有一个构建主机)
     * 2.可根据服务进行筛选,和其他条件并
     *
     * @param serviceId
     * @param setId
     * @return
     */
    @GetMapping("/build/get-building-list")
    public ResponseEntity getBuildingList(@RequestParam(required = false) Long serviceId,
                                          @RequestParam(required = false) Long setId) {
        try {
            List<TServiceBuildRecords> records = new ArrayList<>();
            if (!isEmpty(setId)) {
                TSet set = setRepository.findOne(setId);
                if (isEmpty(set)) {
                    throw new Exception("找不到构建环境");
                }
                THost buildHost = hostRepository.findOne(set.getBuildHost());
                if (isEmpty(buildHost)) {
                    throw new Exception("找不到构建主机,请在环境集中设置");
                }
                if (!isEmpty(serviceId)) {
                    TService service = serviceRepository.findOne(serviceId);
                    if (!isEmpty(service)) {
                        records = buildRecordsRepository.findByBuildServiceAndAgentHostOrderByCreatedAtDesc(service.getName(), IPUtils.transferIp(buildHost.getIp()));
                    }
                } else {
                    records = buildRecordsRepository.findByAgentHostOrderByCreatedAtDesc(IPUtils.transferIp(buildHost.getIp()));
                }
            } else {
                records = buildRecordsRepository.findByOrderByCreatedAtDesc();
            }
            return ResponseEntity
                    .ok(Resp.of(SUCCESS_CODE, LOADED_DATA, records));
        } catch (Exception e) {
            return ResponseEntity
                    .ok(Resp.of(ERROR_CODE, e.getMessage()));
        }
    }

    @GetMapping("/build/building-history-byTask/{taskId}")
    public ResponseEntity getBuildingHistoryByTask(@PathVariable Long taskId) {
        try {
            List<TServiceBuildRecords> records = buildRecordsRepository.findByTaskIdOrderByCreatedAtDesc(taskId);
            return ResponseEntity
                    .ok(Resp.of(SUCCESS_CODE, LOADED_DATA, records));
        } catch (Exception e) {
            return ResponseEntity
                    .ok(Resp.of(ERROR_CODE, e.getMessage()));
        }
    }

    /**
     * 构建任务从部署单元中来
     * 1.默认查询全部
     * 2.[1]服务视图，根据环境集和服务id进行查询
     * 3.[2]主机视图，根据环境集和主机id进行查询
     *
     * @return
     */
    @GetMapping("/build/build-tasks")
    public ResponseEntity buildTasks(@RequestParam(required = false) Long setId,
                                     @RequestParam(required = false) Long serviceId) {
        try {
            List<BuildTaskVo> buildTaskVos;
            // 主机视图
            if (!isEmpty(setId)) {
                if (!isEmpty(serviceId)) {
                    List<TDeployUnit> units = unitRepository.findAllBySetIdAndServiceId(setId, serviceId);
                    buildTaskVos = toTaskList(units);
                } else {
                    List<TDeployUnit> units = unitRepository.findAllBySetId(setId);
                    buildTaskVos = toTaskList(units);
                }
            } else {
                List<TDeployUnit> units = unitRepository.findAll();
                buildTaskVos = toTaskList(units);
            }
            return ResponseEntity.ok(Resp.of(SUCCESS_CODE, LOADED_DATA, buildTaskVos));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity
                    .ok(Resp.of(ERROR_CODE, e.getMessage()));
        }
    }

    private List<BuildTaskVo> toTaskList(List<TDeployUnit> units) {
        List<BuildTaskVo> buildTaskVos = new ArrayList<>();
        units.forEach(u -> {
            BuildTaskVo vo = new BuildTaskVo();
            // 环境集内包含了构建的主机节点
            TSet set = setRepository.findOne(u.getSetId());
            // host是服务的部署节点
            THost host = hostRepository.findOne(u.getHostId());
            // 服务用来分析构建的依赖
            TService service = serviceRepository.findOne(u.getServiceId());
            if (!isEmpty(set) && !isEmpty(host) && !isEmpty(service)) {
                THost buildHost = hostRepository.findOne(set.getBuildHost());
                vo.setId(u.getId());
                vo.setSetId(set.getId());
                vo.setSetName(set.getName());
                vo.setHostId(set.getBuildHost());
                if (!isEmpty(buildHost)) {
                    vo.setHostName(buildHost.getName());
                } else {
                    vo.setHostName("[未设置]");
                }
                vo.setServiceId(u.getServiceId());
                vo.setServiceName(service.getName());
                vo.setBranch(isEmpty(u.getBranch()) ? "master" : u.getBranch());
                vo.setUpdatedAt(u.getUpdatedAt());
                vo.setDeployHostName(host.getName());
                vo.setDepends(getServiceBuildDepends(set, service));
                // 没有任何依赖则不支持构建，直接过滤掉
                if (!isEmpty(vo.getDepends())) {
                    buildTaskVos.add(vo);
                }
            }
        });
        return buildTaskVos;
    }

    private List<DependServiceVo> getServiceBuildDepends(TSet set, TService service) {
        List<DependServiceVo> buildServices = BuildServerUtil.getSortedBuildServices(service.getComposeLabels(), service.getImage(), new ArrayList());
        buildServices.forEach(s -> {
            //1.查找对应的服务在部署单元填写的分支
            // 找到则更改依赖服务的分支
            // 找不到则默认master
            List<TService> services = serviceRepository.findByName(s.getServiceName());
            if (!isEmpty(services)) {
                List<TDeployUnit> units1 = unitRepository.findAllBySetIdAndServiceId(set.getId(), services.get(0).getId());
                if (!isEmpty(units1)) {
                    String branch = units1.get(0).getBranch();
                    s.setBranchName(isEmpty(branch) ? "master" : branch);
                }
            }
        });
        return buildServices;
    }
}
