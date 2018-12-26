package com.github.dapeng.web.deploy;

import com.github.dapeng.common.Resp;
import com.github.dapeng.dto.SetDto;
import com.github.dapeng.entity.deploy.*;
import com.github.dapeng.repository.deploy.*;
import com.github.dapeng.util.DateUtil;
import com.github.dapeng.util.DownloadUtil;
import com.github.dapeng.util.Tools;
import com.github.dapeng.util.VersionUtil;
import com.github.dapeng.vo.DeploySetServiceEnvVo;
import com.github.dapeng.vo.SetSubEnvVo;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
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
import java.util.concurrent.atomic.AtomicBoolean;
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
public class DeploySetRestController {

    private static Logger LOGGER = LoggerFactory.getLogger(DeploySetRestController.class);

    @Autowired
    SetRepository setRepository;

    @Autowired
    HostRepository hostRepository;

    @Autowired
    SetServiceEnvRepository envRepository;

    @Autowired
    ServiceRepository serviceRepository;

    @Autowired
    DeployUnitRepository unitRepository;

    @Autowired
    Gson gson;

    /**
     * @return 环境集
     */
    @GetMapping("/deploy-sets")
    public ResponseEntity<?> deploySets(@RequestParam(required = false, defaultValue = "0") int offset,
                                        @RequestParam(required = false, defaultValue = "100000") int limit,
                                        @RequestParam(required = false) String sort,
                                        @RequestParam(required = false, defaultValue = "desc") String order,
                                        @RequestParam(required = false, defaultValue = "") String search) {
        PageRequest pageRequest = new PageRequest
                (offset / limit, limit,
                        new Sort("desc".toUpperCase().equals(order.toUpperCase()) ? Sort.Direction.DESC : Sort.Direction.ASC,
                                null == sort ? "updatedAt" : sort));
        Page<TSet> page = setRepository.findAll((root, query, cb) -> {
            Path<String> name = root.get("name");
            Path<String> remark = root.get("remark");
            Path<Integer> deleted = root.get("deleted");
            List<Predicate> ps = new ArrayList<>();
            ps.add(cb.or(cb.like(name, "%" + search + "%"), cb.like(remark, "%" + search + "%")));
            ps.add(cb.equal(deleted, NORMAL_STATUS));
            query.where(ps.toArray(new Predicate[ps.size()]));
            return null;
        }, pageRequest);
        return ResponseEntity
                .ok(Resp.of(SUCCESS_CODE, LOADED_DATA, page));
    }

    /**
     * 根据Id获取当前环境集信息
     *
     * @return
     */
    @GetMapping("/deploy-set/{id}")
    public ResponseEntity<?> deploySetById(@PathVariable long id) {
        TSet set = setRepository.findOne(id);
        return ResponseEntity
                .ok(Resp.of(SUCCESS_CODE, LOADED_DATA, set));
    }

    @PostMapping("/deploy-set/importSubEnv/{setId}")
    public ResponseEntity importSubEnv(@RequestParam MultipartFile file, @PathVariable Long setId) {
        String fileName;
        if (!file.isEmpty()) {
            try {
                AtomicInteger failCounter = new AtomicInteger(0);
                fileName = file.getOriginalFilename();
                LOGGER.info("import subEnv by file:::" + fileName);
                String json = new String(file.getBytes());
                List<SetSubEnvVo> vos = gson.fromJson(json, new TypeToken<List<SetSubEnvVo>>() {
                }.getType());

                for (SetSubEnvVo vo : vos) {
                    if (!isEmpty(vo.getServiceName())) {
                        // 按照服务名查找是否有匹配服务
                        List<TService> services = serviceRepository.findByName(vo.getServiceName());
                        if (!isEmpty(services) && !isEmpty(vo.getSubEnv())) {
                            long serviceId = services.get(0).getId();
                            // 已经添加的就不要添加了
                            Boolean exists = envRepository.existsBySetIdAndServiceId(setId, serviceId);
                            if (!exists) {
                                TSetServiceEnv se = new TSetServiceEnv();
                                se.setEnv(vo.getSubEnv());
                                se.setServiceId(serviceId);
                                se.setCreatedAt(DateUtil.now());
                                se.setSetId(setId);
                                se.setUpdatedAt(DateUtil.now());
                                envRepository.save(se);
                            } else {
                                failCounter.incrementAndGet();
                            }
                        } else {
                            failCounter.incrementAndGet();
                        }
                    } else {
                        failCounter.incrementAndGet();
                    }
                }
                return ResponseEntity
                        .ok(Resp.of(SUCCESS_CODE, "导入成功" + (vos.size() - failCounter.get()) + "个，失败" + failCounter.get() + "个"));
            } catch (Exception e) {
                return ResponseEntity
                        .ok(Resp.of(ERROR_CODE, "导入异常,解析文件异常"));
            }
        } else {
            return ResponseEntity
                    .ok(Resp.of(ERROR_CODE, "文件为空,导入出错"));
        }
    }

    @GetMapping("/deploy-set/download-subenv/{setId}")
    public ResponseEntity downloadSubEnv(@PathVariable Long setId, HttpServletResponse response) {
        List<TSetServiceEnv> subEnvs = envRepository.findAllBySetId(setId);
        List<SetSubEnvVo> vos = new ArrayList<>();
        TSet set = setRepository.findOne(setId);
        subEnvs.forEach(s -> {
            SetSubEnvVo vo = new SetSubEnvVo();
            TService service = serviceRepository.findOne(s.getServiceId());
            if (!isEmpty(service)) {
                vo.setServiceName(service.getName());
                vo.setSubEnv(s.getEnv());
            }
            vos.add(vo);
        });
        String json = gson.toJson(vos);
        String path = System.getProperty("java.io.tmpdir") + "/" + set.getName() + "_subEnv" + VersionUtil.version() + ".json";
        Tools.writeStringToFile(path, json);
        try {
            DownloadUtil.downLoad(path, response, false);
        } catch (Exception e) {
            LOGGER.error("下载出错了");
        }
        return ResponseEntity
                .ok(Resp.of(SUCCESS_CODE, COMMON_SUCCESS_MSG));
    }

    /**
     * 添加环境集
     *
     * @param setDto
     * @return
     */
    @PostMapping("/deploy-set/add")
    public ResponseEntity<?> addDeploySet(@RequestBody SetDto setDto) {
        if (isEmpty(setDto.getName())) {
            return ResponseEntity
                    .ok(Resp.of(ERROR_CODE, SAVE_ERROR_MSG));
        }
        try {
            List<TSet> tSets = setRepository.findByName(setDto.getName());
            if (!isEmpty(tSets)) {
                throw new Exception("已存在同名环境集");
            }
            TSet set = new TSet();
            set.setName(setDto.getName());
            set.setRemark(setDto.getRemark());
            set.setEnv(setDto.getEnv());
            set.setNetworkMtu(isEmpty(setDto.getNetworkMtu()) ? "1500" : setDto.getNetworkMtu());
            set.setUpdatedAt(DateUtil.now());
            set.setCreatedAt(DateUtil.now());
            set.setBuildHost(isEmpty(setDto.getBuildHost()) ? 0L : setDto.getBuildHost());
            setRepository.save(set);
            LOGGER.info("add deploy-set name [{}]", setDto.getName());
            return ResponseEntity
                    .ok(Resp.of(SUCCESS_CODE, SAVE_SUCCESS_MSG));
        } catch (Exception e) {
            LOGGER.error("add deploy-set error [{}]", setDto.getName(), e);
            return ResponseEntity
                    .ok(Resp.of(ERROR_CODE, e.getMessage()));
        }
    }

    @PostMapping("/deploy-set/del/{id}")
    public ResponseEntity delDeploySet(@PathVariable long id) {
        List<THost> hosts = hostRepository.findBySetId(id);
        List<TSetServiceEnv> serviceEnvs = envRepository.findAllBySetId(id);
        List<TDeployUnit> deployUnits = unitRepository.findAllBySetId(id);
        try {
            if (!isEmpty(hosts)) {
                throw new Exception("不能删除,此环境集下仍有绑定主机");
            }
            if (!isEmpty(serviceEnvs)) {
                throw new Exception("不能删除,此环境集下仍有绑定的服务环境变量");
            }
            if (!isEmpty(deployUnits)) {
                throw new Exception("不能删除,此环境集下仍有绑定的部署单元");
            }
            TSet set = setRepository.findOne(id);
            if (isEmpty(set)) {
                throw new Exception("找不到此环境集");
            }
            set.setDeleted(DELETED_STATUS);
            return ResponseEntity
                    .ok(Resp.of(SUCCESS_CODE, DEL_SUCCESS_MSG));
        } catch (Exception e) {
            return ResponseEntity
                    .ok(Resp.of(ERROR_CODE, e.getMessage()));
        }
    }

    /**
     * 修改
     *
     * @param id
     * @param setDto
     * @return
     */
    @PostMapping(value = "/deploy-set/edit/{id}")
    public ResponseEntity<?> editDeploySet(@PathVariable Long id, @RequestBody SetDto setDto) {
        try {
            if (isEmpty(setDto.getName())) {
                return ResponseEntity
                        .ok(Resp.of(ERROR_CODE, SAVE_ERROR_MSG));
            }
            TSet set = setRepository.getOne(id);
            set.setName(setDto.getName());
            set.setRemark(setDto.getRemark());
            set.setEnv(setDto.getEnv());
            set.setNetworkMtu(isEmpty(setDto.getNetworkMtu()) ? "1500" : setDto.getNetworkMtu());
            set.setUpdatedAt(DateUtil.now());
            set.setBuildHost(isEmpty(setDto.getBuildHost()) ? 0L : setDto.getBuildHost());
            setRepository.save(set);
            LOGGER.info("update deploy-set name [{}]", setDto.getName());
            return ResponseEntity
                    .ok(Resp.of(SUCCESS_CODE, COMMON_SUCCESS_MSG));
        } catch (Exception e) {
            LOGGER.error("update deploy-set error [{}]", setDto.getName(), e);
            return ResponseEntity
                    .ok(Resp.of(ERROR_CODE, e.getMessage()));
        }
    }

    @GetMapping(value = "/deploy-set/sub-env/{sid}")
    public ResponseEntity getSubEnvBySet(@PathVariable Long sid) {
        List<TSetServiceEnv> subEnvs = envRepository.findAllBySetId(sid);
        return ResponseEntity
                .ok(Resp.of(SUCCESS_CODE, LOADED_DATA, subEnvs));
    }

    /**
     * 查找没有配置环境变量的其他服务
     *
     * @param setId
     * @return
     */
    @GetMapping(value = "/deploy-set/filter-services/{setId}")
    public ResponseEntity filterSubEnvServices(@PathVariable Long setId) {
        List<TSetServiceEnv> subEnvs = envRepository.findAllBySetId(setId);
        Set<Long> ids = new HashSet<>();
        subEnvs.forEach(x -> {
            ids.add(x.getServiceId());
        });
        List<TService> services = serviceRepository.findAllByIdNotIn(ids);
        return ResponseEntity
                .ok(Resp.of(SUCCESS_CODE, LOADED_DATA, services));
    }

    @PostMapping(value = "deploy-set/sub-env/add")
    public ResponseEntity addDeploySetSubEnv(@RequestBody DeploySetServiceEnvVo subEnv) {
        try {
            List<TSetServiceEnv> envList = new ArrayList<>();
            List<Long> serviceIds = new ArrayList<>();
            AtomicBoolean flag = new AtomicBoolean(true);
            AtomicBoolean flag2 = new AtomicBoolean(true);
            subEnv.getSubEnv().forEach(x -> {
                if (isEmpty(x.getServiceId())) {
                    flag2.set(false);
                }
                if (serviceIds.contains(x.getServiceId())) {
                    flag.set(false);
                }
                serviceIds.add(x.getServiceId());
                if (isEmpty(x.getId())) {
                    TSetServiceEnv se = new TSetServiceEnv();
                    se.setEnv(x.getEnv());
                    se.setServiceId(x.getServiceId());
                    se.setCreatedAt(DateUtil.now());
                    se.setSetId(x.getSetId());
                    se.setUpdatedAt(DateUtil.now());
                    envList.add(se);
                } else {
                    TSetServiceEnv se = envRepository.findOne(x.getId());
                    if (!isEmpty(se)) {
                        if (!se.getEnv().equals(x.getEnv())) {
                            se.setUpdatedAt(DateUtil.now());
                        }
                        se.setEnv(String.valueOf(x.getEnv()));
                        envList.add(se);
                    }
                }
            });
            if (!flag.get()) {
                throw new Exception("存在相同的服务配置，请检查！");
            }
            if (!flag2.get()) {
                throw new Exception("存在未选择服务的配置，请检查");
            }
            envRepository.save(envList);
            return ResponseEntity
                    .ok(Resp.of(SUCCESS_CODE, COMMON_SUCCESS_MSG));
        } catch (Exception e) {
            return ResponseEntity
                    .ok(Resp.of(ERROR_CODE, e.getMessage()));
        }
    }
}
