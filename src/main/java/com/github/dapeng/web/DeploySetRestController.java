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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
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
     */
    @GetMapping("/deploy-sets")
    public ResponseEntity<?> deploySets() {
        List<TSet> sets = setRepository.findAll();
        TSet set = new TSet();
        set.setId(1);
        set.setName("生产环境");
        set.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        set.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        set.setEnv("soz_zookeeper=192.168.2.66:2181\n" +
                "soa_core_pool_size=100\n" +
                "soa_monitor_enable=false");
        set.setRemark("测试一哈");
        sets.add(set);
        return ResponseEntity
                .ok(Resp.of(Commons.SUCCESS_CODE, Commons.LOADED_DATA, sets));
    }


    @PostMapping("/deploy-set/add")
    public ResponseEntity<?> addSet(SetDto setDto) {
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
