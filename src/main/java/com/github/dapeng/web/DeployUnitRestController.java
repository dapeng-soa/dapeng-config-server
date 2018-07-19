package com.github.dapeng.web;

import com.github.dapeng.common.Commons;
import com.github.dapeng.common.Resp;
import com.github.dapeng.dto.UnitDto;
import com.github.dapeng.entity.deploy.TDeployUnit;
import com.github.dapeng.repository.deploy.DeployUnitRepository;
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
public class DeployUnitRestController {
    @Autowired
    DeployUnitRepository unitRepository;

    /**
     * @return
     */
    @GetMapping("/deploy-units")
    public ResponseEntity<?> deployUnits() {
        return ResponseEntity
                .ok(Resp.of(Commons.SUCCESS_CODE, Commons.LOADED_DATA));
    }

    @PostMapping("/deploy-unit/add")
    public ResponseEntity<?> addUnit(UnitDto unitDto) {
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
}
