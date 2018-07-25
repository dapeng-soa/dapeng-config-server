package com.github.dapeng.web;

import com.github.dapeng.common.Commons;
import com.github.dapeng.common.Resp;
import com.github.dapeng.dto.SetDto;
import com.github.dapeng.entity.deploy.THost;
import com.github.dapeng.entity.deploy.TSet;
import com.github.dapeng.repository.deploy.HostRepository;
import com.github.dapeng.repository.deploy.SetRepository;
import com.github.dapeng.util.DateUtil;
import com.github.dapeng.util.NullUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import java.util.List;

import static com.github.dapeng.util.NullUtil.isEmpty;

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

    @Autowired
    HostRepository hostRepository;

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
        if (isEmpty(setDto.getName())){
            return ResponseEntity
                    .ok(Resp.of(Commons.ERROR_CODE, Commons.SAVE_ERROR_MSG));
        }
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

    @PostMapping("/deploy-set/del/{id}")
    public ResponseEntity delSet(@PathVariable long id){
        List<THost> hosts = hostRepository.findBySetId(id);
        if (isEmpty(hosts)){
            setRepository.delete(id);
            return ResponseEntity
                    .ok(Resp.of(Commons.SUCCESS_CODE, Commons.DEL_SUCCESS_MSG));
        }else {
            return ResponseEntity
                    .ok(Resp.of(Commons.SUCCESS_CODE, "不能删除,此环境集下仍有绑定主机"));
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
    public ResponseEntity<?> updateSet(@PathVariable Long id, @RequestBody SetDto setDto) {
        try {
            if (isEmpty(setDto.getName())){
                return ResponseEntity
                        .ok(Resp.of(Commons.ERROR_CODE, Commons.SAVE_ERROR_MSG));
            }
            TSet set = setRepository.getOne(id);
            set.setName(setDto.getName());
            set.setRemark(setDto.getRemark());
            set.setEnv(setDto.getEnv());
            set.setUpdatedAt(DateUtil.now());
            setRepository.save(set);
            return ResponseEntity
                    .ok(Resp.of(Commons.SUCCESS_CODE, Commons.COMMON_SUCCESS_MSG));
        } catch (Exception e) {
            return ResponseEntity
                    .ok(Resp.of(Commons.ERROR_CODE, Commons.COMMON_ERRO_MSG));
        }
    }
}
