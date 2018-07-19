package com.github.dapeng.web;

import com.github.dapeng.common.Commons;
import com.github.dapeng.common.Resp;
import com.github.dapeng.dto.HostDto;
import com.github.dapeng.entity.deploy.THost;
import com.github.dapeng.repository.deploy.HostRepository;
import com.github.dapeng.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        return ResponseEntity
                .ok(Resp.of(Commons.SUCCESS_CODE, Commons.LOADED_DATA));
    }

    @PostMapping("/deploy-host/add")
    public ResponseEntity<?> addHost(HostDto hostDto) {
        THost host = new THost();
        host.setIp(hostDto.getIp());
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
