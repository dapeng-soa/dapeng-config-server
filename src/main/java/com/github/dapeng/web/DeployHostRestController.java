package com.github.dapeng.web;

import com.github.dapeng.common.Resp;
import com.github.dapeng.core.helper.IPUtils;
import com.github.dapeng.dto.HostDto;
import com.github.dapeng.entity.deploy.THost;
import com.github.dapeng.entity.deploy.TSet;
import com.github.dapeng.repository.deploy.HostRepository;
import com.github.dapeng.repository.deploy.SetRepository;
import com.github.dapeng.util.DateUtil;
import com.github.dapeng.util.DeployCheck;
import com.github.dapeng.vo.HostVo;
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
public class DeployHostRestController {

    private static Logger LOGGER = LoggerFactory.getLogger(DeployHostRestController.class);

    @Autowired
    HostRepository hostRepository;

    @Autowired
    SetRepository setRepository;

    /**
     * @return
     */
    @GetMapping("/deploy-hosts")
    public ResponseEntity<?> deployHosts(@RequestParam(required = false, defaultValue = "0") int offset,
                                         @RequestParam(required = false, defaultValue = "100000") int limit,
                                         @RequestParam(required = false) String sort,
                                         @RequestParam(required = false, defaultValue = "desc") String order,
                                         @RequestParam(required = false, defaultValue = "") String search) {
        PageRequest pageRequest = new PageRequest
                (offset / limit, limit,
                        new Sort("desc".toUpperCase().equals(order.toUpperCase()) ? Sort.Direction.DESC : Sort.Direction.ASC,
                                null == sort ? "updatedAt" : sort));
        Page<THost> page = hostRepository.findAll((root, query, cb) -> {
            Path<String> name = root.get("name");
            Path<String> remark = root.get("remark");
            List<Predicate> ps = new ArrayList<>();
            ps.add(cb.or(cb.like(name, "%" + search + "%"), cb.like(remark, "%" + search + "%")));
            query.where(ps.toArray(new Predicate[ps.size()]));
            return null;
        }, pageRequest);
        Page<HostVo> voPage = page.map(x -> toVo(x));
        return ResponseEntity
                .ok(Resp.of(SUCCESS_CODE, LOADED_DATA, voPage));
    }

    private HostVo toVo(THost x) {
        HostVo hostVo = new HostVo();
        hostVo.setId(x.getId());
        hostVo.setIp(IPUtils.transferIp(x.getIp()));
        hostVo.setEnv(x.getEnv());
        hostVo.setName(x.getName());
        hostVo.setCreatedAt(x.getCreatedAt());
        hostVo.setUpdatedAt(x.getUpdatedAt());
        hostVo.setSetId(x.getSetId());
        TSet tSet = setRepository.getOne(x.getSetId());
        hostVo.setSetName(tSet.getName());
        hostVo.setExtra(x.getExtra());
        hostVo.setLabels(x.getLabels());
        hostVo.setRemark(x.getRemark());
        return hostVo;
    }

    /**
     * 根据setId获取该节点
     *
     * @return
     */
    @GetMapping("/deploy-hosts/{setId}")
    public ResponseEntity<?> deployHostsBySetId(@PathVariable long setId) {
        List<THost> hosts = hostRepository.findBySetId(setId);
        return ResponseEntity
                .ok(Resp.of(SUCCESS_CODE, LOADED_DATA, hosts));
    }


    /**
     * 根据Id获取当前节点信息
     *
     * @return
     */
    @GetMapping("/deploy-host/{id}")
    public ResponseEntity<?> deployHostById(@PathVariable long id) {
        THost x = hostRepository.findOne(id);
        HostVo hostVo = new HostVo();
        hostVo.setId(x.getId());
        hostVo.setIp(IPUtils.transferIp(x.getIp()));
        hostVo.setEnv(x.getEnv());
        hostVo.setName(x.getName());
        hostVo.setCreatedAt(x.getCreatedAt());
        hostVo.setUpdatedAt(x.getUpdatedAt());
        hostVo.setSetId(x.getSetId());
        TSet tSet = setRepository.getOne(x.getSetId());
        hostVo.setSetName(tSet.getName());
        hostVo.setExtra(x.getExtra());
        hostVo.setLabels(x.getLabels());
        hostVo.setRemark(x.getRemark());
        return ResponseEntity
                .ok(Resp.of(SUCCESS_CODE, LOADED_DATA, hostVo));
    }


    /**
     * 添加host
     *
     * @param hostDto
     * @return
     */
    @PostMapping("/deploy-host/add")
    public ResponseEntity<?> addHost(@RequestBody HostDto hostDto) {
        try {
            if (isEmpty(hostDto.getName()) || isEmpty(hostDto.getIp()) || isEmpty(hostDto.getSetId())) {
                return ResponseEntity
                        .ok(Resp.of(ERROR_CODE, "服务名,IP,所属环境集不能为空"));
            }
            DeployCheck.hasChinese(hostDto.getName(), "节点名");
            DeployCheck.isboolIp(hostDto.getIp());
            List<THost> tHosts = hostRepository.findByName(hostDto.getName());
            if (!isEmpty(tHosts)) {
                throw new Exception("已存在同名节点");
            }
            THost host = new THost();
            host.setIp(IPUtils.transferIp(hostDto.getIp()));
            host.setName(hostDto.getName());
            host.setEnv(hostDto.getEnv());
            host.setRemark(hostDto.getRemark());
            host.setLabels(hostDto.getLabels());
            host.setExtra(hostDto.getExtra());
            host.setSetId(hostDto.getSetId());
            host.setCreatedAt(DateUtil.now());
            host.setUpdatedAt(DateUtil.now());
            hostRepository.save(host);
            LOGGER.info("add deploy-host name [{}]", hostDto.getName());
            return ResponseEntity
                    .ok(Resp.of(SUCCESS_CODE, SAVE_SUCCESS_MSG));
        } catch (Exception e) {
            LOGGER.error("add deploy-host error [{}]", hostDto.getName(), e);
            return ResponseEntity
                    .ok(Resp.of(ERROR_CODE, e.getMessage()));
        }
    }

    /**
     * 修改
     *
     * @param id
     * @param hostDto
     * @return
     */
    @PostMapping(value = "/deploy-host/edit/{id}")
    public ResponseEntity<?> updateSet(@PathVariable Long id, @RequestBody HostDto hostDto) {
        try {
            if (isEmpty(hostDto.getName()) || isEmpty(hostDto.getIp()) || isEmpty(hostDto.getSetId())) {
                return ResponseEntity
                        .ok(Resp.of(ERROR_CODE, SAVE_ERROR_MSG));
            }
            DeployCheck.hasChinese(hostDto.getName(), "节点名");
            DeployCheck.isboolIp(hostDto.getIp());
            THost host = hostRepository.findOne(id);
            host.setIp(IPUtils.transferIp(hostDto.getIp()));
            host.setName(hostDto.getName());
            host.setEnv(hostDto.getEnv());
            host.setRemark(hostDto.getRemark());
            host.setLabels(hostDto.getLabels());
            host.setExtra(hostDto.getExtra());
            host.setSetId(hostDto.getSetId());
            host.setUpdatedAt(DateUtil.now());
            hostRepository.save(host);
            LOGGER.info("update deploy-host name [{}]", hostDto.getName());
            return ResponseEntity
                    .ok(Resp.of(SUCCESS_CODE, COMMON_SUCCESS_MSG));
        } catch (Exception e) {
            LOGGER.error("update deploy-host error [{}]", hostDto.getName(), e);
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
    @PostMapping("/deploy-host/del/{id}")
    public ResponseEntity delHost(@PathVariable long id) {
        hostRepository.delete(id);
        return ResponseEntity
                .ok(Resp.of(SUCCESS_CODE, DEL_SUCCESS_MSG));
    }

}
