package com.github.dapeng.web.deploy;

import com.github.dapeng.common.Resp;
import com.github.dapeng.dto.ModifyBatchTagDto;
import com.github.dapeng.dto.UnitDto;
import com.github.dapeng.entity.deploy.TDeployUnit;
import com.github.dapeng.repository.deploy.*;
import com.github.dapeng.util.DateUtil;
import com.github.dapeng.util.Check;
import com.github.dapeng.vo.DeployUnitVo;
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
import java.util.stream.Collectors;

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
public class DeployUnitRestController {

    private static Logger LOGGER = LoggerFactory.getLogger(DeployUnitRestController.class);

    @Autowired
    SetRepository setRepository;
    @Autowired
    HostRepository hostRepository;
    @Autowired
    ServiceRepository serviceRepository;
    @Autowired
    DeployUnitRepository unitRepository;
    @Autowired
    FilesUnitRepository filesUnitRepository;

    /**
     * @return 部署单元
     */
    @GetMapping("/deploy-units")
    public ResponseEntity<?> deployUnits(@RequestParam(required = false, defaultValue = "0") int offset,
                                         @RequestParam(required = false, defaultValue = "100000") int limit,
                                         @RequestParam(required = false) String sort,
                                         @RequestParam(required = false, defaultValue = "desc") String order,
                                         @RequestParam(required = false, defaultValue = "") String search,
                                         @RequestParam(required = false, defaultValue = "0") Long setId,
                                         @RequestParam(required = false, defaultValue = "0") Long hostId,
                                         @RequestParam(required = false, defaultValue = "0") Long serviceId,
                                         @RequestParam(required = false) String gitTag) {
        PageRequest pageRequest = new PageRequest
                (offset / limit, limit,
                        new Sort("desc".toUpperCase().equals(order.toUpperCase()) ? Sort.Direction.DESC : Sort.Direction.ASC,
                                null == sort ? "updatedAt" : sort));


        Page<TDeployUnit> page = unitRepository.findAll((root, query, cb) -> {
            Path<Long> setId1 = root.get("setId");
            Path<Long> hostId1 = root.get("hostId");
            Path<Long> serviceId1 = root.get("serviceId");
            Path<String> gitTag1 = root.get("gitTag");
            Path<String> imageTag = root.get("imageTag");
            List<Predicate> ps = new ArrayList<>();
            ps.add(cb.or(cb.like(gitTag1, "%" + search + "%"), cb.like(imageTag, "%" + search + "%")));
            //这里可以设置任意条查询条件
            if (!isEmpty(setId)) {
                ps.add(cb.equal(setId1, setId));
            }
            if (!isEmpty(hostId)) {
                ps.add(cb.equal(hostId1, hostId));
            }
            if (!isEmpty(serviceId)) {
                ps.add(cb.equal(serviceId1, serviceId));
            }
            query.where(ps.toArray(new Predicate[ps.size()]));
            //这种方式使用JPA的API设置了查询条件，所以不需要再返回查询条件Predicate给Spring Data Jpa，故最后return null;即可。
            return null;
        }, pageRequest);
        Page<DeployUnitVo> voPage = page.map(u -> toVo(u));
        return ResponseEntity
                .ok(Resp.of(SUCCESS_CODE, LOADED_DATA, voPage));
    }

    private DeployUnitVo toVo(TDeployUnit u) {
        DeployUnitVo vo = new DeployUnitVo();
        vo.setId(u.getId());
        vo.setSetId(u.getSetId());
        vo.setSetName(setRepository.getOne(u.getSetId()).getName());
        vo.setHostId(u.getHostId());
        vo.setHostName(hostRepository.getOne(u.getHostId()).getName());
        vo.setServiceId(u.getServiceId());
        vo.setServiceName(serviceRepository.getOne(u.getServiceId()).getName());
        vo.setCreatedAt(u.getCreatedAt());
        vo.setDockerExtras(u.getDockerExtras());
        vo.setEnv(u.getEnv());
        vo.setGitTag(u.getGitTag());
        vo.setImageTag(u.getImageTag());
        vo.setPorts(u.getPorts());
        vo.setUpdatedAt(u.getUpdatedAt());
        vo.setVolumes(u.getVolumes());
        return vo;
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
                .ok(Resp.of(SUCCESS_CODE, LOADED_DATA, unit));
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
                .ok(Resp.of(SUCCESS_CODE, LOADED_DATA, gitTags));
    }

    /**
     * 添加部署单元
     *
     * @param unitDto
     * @return
     */
    @PostMapping("/deploy-unit/add")
    public ResponseEntity<?> addDeployUnit(@RequestBody UnitDto unitDto) {
        if (isEmpty(unitDto.getSetId())
                || isEmpty(unitDto.getHostId())
                || isEmpty(unitDto.getServiceId())
                || isEmpty(unitDto.getGitTag())
                || isEmpty(unitDto.getImageTag())) {
            return ResponseEntity
                    .ok(Resp.of(ERROR_CODE, SAVE_ERROR_MSG));
        }
        try {
            Check.hasChinese(unitDto.getGitTag(), "发布tag");
            Check.hasChinese(unitDto.getImageTag(), "镜像tag");
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
            LOGGER.info("add deploy-unit hostId [{}] serviceId [{}]", unit.getHostId(), unit.getServiceId());
            return ResponseEntity
                    .ok(Resp.of(SUCCESS_CODE, SAVE_SUCCESS_MSG));
        } catch (Exception e) {
            LOGGER.error("add deploy-unit error hostId [{}] serviceId [{}]", unitDto.getHostId(), unitDto.getServiceId(), e);
            return ResponseEntity
                    .ok(Resp.of(ERROR_CODE, e.getMessage()));
        }
    }

    /**
     * 修改
     *
     * @param id
     * @param unitDto
     * @return
     */
    @PostMapping(value = "/deploy-unit/edit/{id}")
    public ResponseEntity<?> editDeployUnit(@PathVariable Long id, @RequestBody UnitDto unitDto) {
        try {
            if (isEmpty(unitDto.getSetId())
                    || isEmpty(unitDto.getHostId())
                    || isEmpty(unitDto.getServiceId())
                    || isEmpty(unitDto.getGitTag())
                    || isEmpty(unitDto.getImageTag())) {
                return ResponseEntity
                        .ok(Resp.of(ERROR_CODE, SAVE_ERROR_MSG));
            }
            Check.hasChinese(unitDto.getGitTag(), "发布tag");
            Check.hasChinese(unitDto.getImageTag(), "镜像tag");
            TDeployUnit unit = unitRepository.getOne(id);
            unit.setGitTag(unitDto.getGitTag());
            unit.setImageTag(unitDto.getImageTag());
            unit.setHostId(unitDto.getHostId());
            unit.setServiceId(unitDto.getServiceId());
            unit.setSetId(unitDto.getSetId());
            unit.setEnv(unitDto.getEnv());
            unit.setPorts(unitDto.getPorts());
            unit.setVolumes(unitDto.getVolumes());
            unit.setDockerExtras(unitDto.getDockerExtras());
            unit.setUpdatedAt(DateUtil.now());
            LOGGER.info("update deploy-unit hostId [{}] serviceId [{}]", unit.getHostId(), unit.getServiceId());
            unitRepository.save(unit);
            return ResponseEntity
                    .ok(Resp.of(SUCCESS_CODE, COMMON_SUCCESS_MSG));
        } catch (Exception e) {
            LOGGER.error("update deploy-unit error hostId [{}] serviceId [{}]", unitDto.getHostId(), unitDto.getServiceId(), e);
            return ResponseEntity
                    .ok(Resp.of(ERROR_CODE, e.getMessage()));
        }
    }

    @PostMapping(value = "/deploy-unit/del/{id}")
    public ResponseEntity delDeployUnit(@PathVariable Long id) {
        TDeployUnit unit = unitRepository.getOne(id);
        LOGGER.info("del deploy-unit hostId [{}] serviceId [{}]", unit.getHostId(), unit.getServiceId());
        unitRepository.delete(unit.getId());
        return ResponseEntity
                .ok(Resp.of(SUCCESS_CODE, DEL_SUCCESS_MSG));
    }

    /**
     * 批量修改镜像tag
     *
     * @param tagDto
     * @return
     */
    @PostMapping(value = "deploy-unit/modify-batch")
    public ResponseEntity modifyDeployUnitTagBatch(@RequestBody ModifyBatchTagDto tagDto) {
        try {
            Check.hasChinese(tagDto.getTag(), "镜像tag");
            tagDto.getIds().forEach(x -> {
                TDeployUnit unit = unitRepository.getOne(x);
                unit.setImageTag(tagDto.getTag());
                unitRepository.save(unit);
            });
            return ResponseEntity
                    .ok(Resp.of(SUCCESS_CODE, "批量修改镜像tag成功"));
        } catch (Exception e) {
            return ResponseEntity
                    .ok(Resp.of(ERROR_CODE, e.getMessage()));
        }
    }
}
