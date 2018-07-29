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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

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
public class DeployHostRestController {

    @Autowired
    HostRepository hostRepository;

    @Autowired
    SetRepository setRepository;

    /**
     * @return
     */
    @GetMapping("/deploy-hosts")
    public ResponseEntity<?> deployHosts() {
        List<THost> hosts = hostRepository.findAll();
        List<HostVo> hostVos = hosts.stream().map(x -> {
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
        }).collect(Collectors.toList());
        return ResponseEntity
                .ok(Resp.of(SUCCESS_CODE, LOADED_DATA, hostVos));
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
                        .ok(Resp.of(ERROR_CODE, SAVE_ERROR_MSG));
            }
            DeployCheck.hasChinese(hostDto.getName(), "节点名");
            DeployCheck.isboolIp(hostDto.getIp());
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

            return ResponseEntity
                    .ok(Resp.of(SUCCESS_CODE, SAVE_SUCCESS_MSG));
        } catch (Exception e) {
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
            return ResponseEntity
                    .ok(Resp.of(SUCCESS_CODE, COMMON_SUCCESS_MSG));
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
    @PostMapping("/deploy-host/del/{id}")
    public ResponseEntity delHost(@PathVariable long id) {
        hostRepository.delete(id);
        return ResponseEntity
                .ok(Resp.of(SUCCESS_CODE, DEL_SUCCESS_MSG));
    }

}
