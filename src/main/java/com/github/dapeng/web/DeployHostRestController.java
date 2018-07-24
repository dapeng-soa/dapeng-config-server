package com.github.dapeng.web;

import com.github.dapeng.common.Commons;
import com.github.dapeng.common.Resp;
import com.github.dapeng.core.helper.DapengUtil;
import com.github.dapeng.core.helper.IPUtils;
import com.github.dapeng.dto.HostDto;
import com.github.dapeng.entity.deploy.THost;
import com.github.dapeng.repository.deploy.HostRepository;
import com.github.dapeng.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    /**
     * @return
     */
    @GetMapping("/deploy-hosts")
    public ResponseEntity<?> deployHosts() {
        List<THost> hosts = hostRepository.findAll();
        return ResponseEntity
                .ok(Resp.of(Commons.SUCCESS_CODE, Commons.LOADED_DATA,hosts));
    }

    /**
     * 根据setId获取该节点
     * @return
     */
    @GetMapping("/deploy-hosts/{setId}")
    public ResponseEntity<?> deployHostsBySetId(@PathVariable long setId) {
        List<THost> hosts = hostRepository.findBySetId(setId);
        return ResponseEntity
                .ok(Resp.of(Commons.SUCCESS_CODE, Commons.LOADED_DATA,hosts));
    }


    /**
     * 根据Id获取当前节点信息
     * @return
     */
    @GetMapping("/deploy-host/{id}")
    public ResponseEntity<?> deployHostById(@PathVariable long id) {
        THost host = hostRepository.findOne(id);
        return ResponseEntity
                .ok(Resp.of(Commons.SUCCESS_CODE, Commons.LOADED_DATA,host));
    }


    /**
     * 添加host
     * @param hostDto
     * @return
     */
    @PostMapping("/deploy-host/add")
    public ResponseEntity<?> addHost(@RequestBody HostDto hostDto) {
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
                .ok(Resp.of(Commons.SUCCESS_CODE, Commons.SAVE_SUCCESS_MSG));
    }
}
