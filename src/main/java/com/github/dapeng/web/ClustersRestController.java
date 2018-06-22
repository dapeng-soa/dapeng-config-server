package com.github.dapeng.web;

import com.github.dapeng.common.Commons;
import com.github.dapeng.common.Resp;
import com.github.dapeng.entity.ZkNode;
import com.github.dapeng.repository.ZkNodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @author with struy.
 * Create by 2018/6/19 23:23
 * email :yq1724555319@gmail.com
 */

@RestController
@RequestMapping(value = "/api")
@Transactional(rollbackFor = Throwable.class)
public class ClustersRestController {

    @Autowired
    ZkNodeRepository nodeRepository;

    /**
     * 添加集群
     *
     * @return
     */
    @PostMapping(value = "/cluster/add/")
    public ResponseEntity<?> addKey() {
        return ResponseEntity
                .ok(Resp.of(Commons.SUCCESS_CODE, Commons.SAVE_SUCCESS_MSG));
    }

    /**
     * 获取集群列表
     *
     * @return
     */
    @GetMapping(value = "/clusters")
    public ResponseEntity<?> clusters() {
        try {
            List<ZkNode> clusters = nodeRepository.findAll();
            return ResponseEntity
                    .ok(Resp.of(Commons.SUCCESS_CODE, Commons.LOADED_DATA, clusters));
        } catch (Exception e) {
            return ResponseEntity
                    .ok(Resp.of(Commons.ERROR_CODE, Commons.LOAD_DATA_ERROR));
        }
    }

}
