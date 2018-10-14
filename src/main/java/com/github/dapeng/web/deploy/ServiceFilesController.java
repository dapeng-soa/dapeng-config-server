package com.github.dapeng.web.deploy;

import com.github.dapeng.common.Resp;
import com.github.dapeng.dto.FileUnitDto;
import com.github.dapeng.entity.deploy.TDeployUnit;
import com.github.dapeng.entity.deploy.TFilesUnit;
import com.github.dapeng.entity.deploy.TServiceFiles;
import com.github.dapeng.repository.deploy.*;
import com.github.dapeng.util.DateUtil;
import com.github.dapeng.vo.DeployUnitVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.github.dapeng.common.Commons.*;
import static com.github.dapeng.util.Md5.encrypt16;
import static com.github.dapeng.util.NullUtil.isEmpty;

/**
 * @author with struy.
 * Create by 2018/9/27 10:12
 * email :yq1724555319@gmail.com
 */
@RestController
@RequestMapping("/api")
public class ServiceFilesController {

    @Autowired
    ServiceFilesRepository serviceFilesRepository;
    @Autowired
    FilesUnitRepository filesUnitRepository;
    @Autowired
    DeployUnitRepository unitRepository;

    @Autowired
    SetRepository setRepository;
    @Autowired
    HostRepository hostRepository;
    @Autowired
    ServiceRepository serviceRepository;

    @GetMapping("/deploy-files")
    public ResponseEntity serviceFiles(@RequestParam(required = false, defaultValue = "0") int offset,
                                       @RequestParam(required = false, defaultValue = "100000") int limit,
                                       @RequestParam(required = false) String sort,
                                       @RequestParam(required = false, defaultValue = "desc") String order,
                                       @RequestParam(required = false, defaultValue = "") String search) {

        PageRequest pageRequest = new PageRequest
                (offset / limit, limit,
                        new Sort("desc".toUpperCase().equals(order.toUpperCase()) ? Sort.Direction.DESC : Sort.Direction.ASC,
                                null == sort ? "updatedAt" : sort));
        Page<TServiceFiles> page = serviceFilesRepository.findAll((root, query, cb) -> {
            Path<String> fileName = root.get("fileName");
            Path<String> fileExtName = root.get("fileExtName");
            Path<String> remark = root.get("remark");
            List<Predicate> ps = new ArrayList<>();
            ps.add(cb.or(cb.like(fileName, "%" + search + "%"), cb.like(remark, "%" + search + "%"), cb.like(fileExtName, "%" + search + "%")));
            query.where(ps.toArray(new Predicate[ps.size()]));
            return null;
        }, pageRequest);
        return ResponseEntity
                .ok(Resp.of(SUCCESS_CODE, LOADED_DATA, page));

    }


    @PostMapping("/deploy-file/add")
    @Transactional(rollbackFor = Throwable.class)
    public ResponseEntity saveServiceFile(@RequestBody TServiceFiles file) {
        try {
            List<TServiceFiles> files = serviceFilesRepository.findByFileNameAndFileExtName(file.getFileName(), file.getFileExtName());
            if (!isEmpty(files)) {
                throw new Exception("已存在相同文件配置");
            }
            file.setCreatedAt(DateUtil.now());
            file.setUpdatedAt(DateUtil.now());
            file.setFileTag(encrypt16(file.getFileContext()));
            serviceFilesRepository.save(file);
            return ResponseEntity
                    .ok(Resp.of(SUCCESS_CODE, COMMON_SUCCESS_MSG));
        } catch (Exception e) {
            return ResponseEntity
                    .ok(Resp.of(ERROR_CODE, e.getMessage()));
        }
    }

    @GetMapping("/deploy-file/{id}")
    public ResponseEntity serviceFile(@PathVariable Long id) {
        try {
            TServiceFiles one = serviceFilesRepository.getOne(id);
            return ResponseEntity
                    .ok(Resp.of(SUCCESS_CODE, LOADED_DATA, one));
        } catch (Exception e) {
            return ResponseEntity
                    .ok(Resp.of(ERROR_CODE, LOAD_DATA_ERROR));
        }
    }

    @PostMapping("/deploy-file/edit/{id}")
    @Transactional(rollbackFor = Throwable.class)
    public ResponseEntity editServiceFile(@PathVariable Long id,
                                          @RequestBody TServiceFiles file) {
        try {
            TServiceFiles one = serviceFilesRepository.getOne(id);
            if (!one.getFileExtName().equals(file.getFileExtName()) ||
                    !one.getFileContext().equals(file.getFileContext()) ||
                    !one.getFileName().equals(file.getFileName())) {
                one.setUpdatedAt(DateUtil.now());
            }
            one.setFileContext(file.getFileContext());
            one.setFileExtName(file.getFileExtName());
            one.setFileName(file.getFileName());
            one.setRemark(file.getRemark());
            one.setFileTag(encrypt16(one.getFileContext()));
            serviceFilesRepository.save(one);
            return ResponseEntity
                    .ok(Resp.of(SUCCESS_CODE, COMMON_SUCCESS_MSG));
        } catch (Exception e) {
            return ResponseEntity
                    .ok(Resp.of(ERROR_CODE, e.getMessage()));
        }
    }

    /**
     * 关联unit
     *
     * @return
     */
    @PostMapping("/deploy-file/link-unit")
    public ResponseEntity linkUnit(@RequestBody FileUnitDto dto) {

        try {
            dto.getUids().forEach(uid -> {
                List<TFilesUnit> list = filesUnitRepository.findByFileIdAndUnitId(dto.getFid(), uid);
                if (isEmpty(list)) {
                    TFilesUnit filesUnit = new TFilesUnit();
                    filesUnit.setFileId(dto.getFid());
                    filesUnit.setUnitId(uid);
                    filesUnit.setCreateAt(DateUtil.now());
                    filesUnitRepository.save(filesUnit);
                }
            });
            return ResponseEntity
                    .ok(Resp.of(SUCCESS_CODE, "批量关联部署单元成功"));
        } catch (Exception e) {
            return ResponseEntity
                    .ok(Resp.of(ERROR_CODE, e.getMessage()));
        }
    }

    /**
     * 取消关联unit
     *
     * @return
     */
    @PostMapping("/deploy-file/unlink-unit")
    @Transactional(rollbackFor = Throwable.class)
    public ResponseEntity unLinkUnit(@RequestBody FileUnitDto dto) {
        try {
            dto.getUids().forEach(uid -> {
                filesUnitRepository.deleteByFileIdAndUnitId(dto.getFid(), uid);
            });
            return ResponseEntity
                    .ok(Resp.of(SUCCESS_CODE, "批量取消关联成功"));
        } catch (Exception e) {
            return ResponseEntity
                    .ok(Resp.of(ERROR_CODE, e.getMessage()));
        }
    }

    /**
     * 删除文件
     * 1.需要先检查是不是有关联的部署单元
     *
     * @param id
     * @return
     */
    @PostMapping("/deploy-file/del/{id}")
    @Transactional(rollbackFor = Throwable.class)
    public ResponseEntity delServiceFile(@PathVariable Long id) {
        try {
            List<TFilesUnit> byFileId = filesUnitRepository.findByFileId(id);
            if (!isEmpty(byFileId)) {
                throw new Exception("还存在关联的部署单元，不能删除此文件");
            }
            serviceFilesRepository.delete(id);
            return ResponseEntity
                    .ok(Resp.of(SUCCESS_CODE, "删除文件成功"));
        } catch (Exception e) {
            return ResponseEntity
                    .ok(Resp.of(ERROR_CODE, e.getMessage()));
        }
    }

    /**
     * 筛选部署单元
     *
     * @param offset
     * @param limit
     * @param sort
     * @param order
     * @param search
     * @param setId
     * @param hostId
     * @param serviceId
     * @param linkType
     * @param fileId
     * @return
     */
    @GetMapping("/deploy-file/units")
    public ResponseEntity units(@RequestParam(required = false, defaultValue = "0") int offset,
                                @RequestParam(required = false, defaultValue = "100000") int limit,
                                @RequestParam(required = false) String sort,
                                @RequestParam(required = false, defaultValue = "desc") String order,
                                @RequestParam(required = false, defaultValue = "") String search,
                                @RequestParam(required = false, defaultValue = "0") Long setId,
                                @RequestParam(required = false, defaultValue = "0") Long hostId,
                                @RequestParam(required = false, defaultValue = "0") Long serviceId,
                                @RequestParam(required = false, defaultValue = "0") Integer linkType,
                                @RequestParam(required = false, defaultValue = "0") Long fileId) {
        try {
            // 根据fileId查询已经和此file绑定的部署单元，排除以外的是未绑定的
            List<TFilesUnit> byFileId = filesUnitRepository.findByFileId(fileId);
            // 获取已经绑定的部署单元
            List<Long> uids = byFileId.stream().map(x -> x.getUnitId()).collect(Collectors.toList());

            PageRequest pageRequest = new PageRequest
                    (offset / limit, limit,
                            new Sort("desc".toUpperCase().equals(order.toUpperCase()) ? Sort.Direction.DESC : Sort.Direction.ASC,
                                    null == sort ? "updatedAt" : sort));

            Page<TDeployUnit> page = unitRepository.findAll((root, query, cb) -> {
                Path<Long> uid = root.get("id");
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
                if (!isEmpty(linkType)) {
                    switch (linkType) {
                        case 1:
                            CriteriaBuilder.In<Long> in2 = cb.in(uid).value(0L);
                            uids.forEach(in2::value);
                            // 未绑定
                            ps.add(cb.not(in2));
                            break;
                        case 2:
                            CriteriaBuilder.In<Long> in = cb.in(uid).value(0L);
                            uids.forEach(in::value);
                            // 已绑定
                            ps.add(in);
                            break;
                        default:
                            break;
                    }
                }
                query.where(ps.toArray(new Predicate[ps.size()]));
                //这种方式使用JPA的API设置了查询条件，所以不需要再返回查询条件Predicate给Spring Data Jpa，故最后return null;即可。
                return null;
            }, pageRequest);
            Page<DeployUnitVo> voPage = page.map(u -> toVo(u));
            return ResponseEntity
                    .ok(Resp.of(SUCCESS_CODE, LOADED_DATA, voPage));
        } catch (Exception e) {
            return ResponseEntity
                    .ok(Resp.of(ERROR_CODE, e.getMessage()));
        }
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
}
