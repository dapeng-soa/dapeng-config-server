package com.github.dapeng.web;

import com.github.dapeng.common.Commons;
import com.github.dapeng.common.Resp;
import com.github.dapeng.dto.SetDto;
import com.github.dapeng.entity.deploy.TSet;
import com.github.dapeng.repository.deploy.SetRepository;
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
public class DeploySetRestController {

    @Autowired
    SetRepository setRepository;

    /**
     * @return
     * 环境集
     */
    @GetMapping("/deploy-sets")
    public ResponseEntity<?> deploySets() {
        List<TSet> sets = setRepository.findAll();
        return ResponseEntity
                .ok(Resp.of(Commons.SUCCESS_CODE, Commons.LOADED_DATA, sets));
    }

    /**
     * 根据Id获取当前环境集信息
     * @return
     */
    @GetMapping("/deploy-set/{id}")
    public ResponseEntity<?> deploySetById(@PathVariable long id) {
        TSet set = setRepository.findOne(id);
        return ResponseEntity
                .ok(Resp.of(Commons.SUCCESS_CODE, Commons.LOADED_DATA,set));
    }

    /**
     * 添加环境集
     * @param setDto
     * @return
     */
    @PostMapping("/deploy-set/add")
    public ResponseEntity<?> addSet(@RequestBody SetDto setDto) {
        TSet set = new TSet();
        set.setName(setDto.getName());
        set.setRemark(setDto.getRemark());
        set.setEnv(setDto.getEnv());
        set.setUpdatedAt(DateUtil.now());
        set.setCreatedAt(DateUtil.now());
        setRepository.save(set);

        return ResponseEntity
                .ok(Resp.of(Commons.SUCCESS_CODE, Commons.SAVE_SUCCESS_MSG));
    }

}
