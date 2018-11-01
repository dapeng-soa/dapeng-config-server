package com.github.dapeng.web.config;

import com.github.dapeng.common.Commons;
import com.github.dapeng.common.Resp;
import com.github.dapeng.dto.ZkNodeDto;
import com.github.dapeng.entity.config.ZkNode;
import com.github.dapeng.repository.config.ZkNodeRepository;
import com.github.dapeng.util.DateUtil;
import com.github.dapeng.util.NullUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author with struy.
 * Create by 2018/6/19 23:23
 * email :yq1724555319@gmail.com
 */

@RestController
@RequestMapping(value = "/api")
@Transactional(rollbackFor = Exception.class)
public class ClustersRestController {

    @Autowired
    ZkNodeRepository nodeRepository;

    private static Logger LOGGER = LoggerFactory.getLogger(ClustersRestController.class);

    /**
     * 添加集群
     *
     * @return
     */
    @PostMapping(value = "/cluster/add")
    public ResponseEntity<?> addCluster(@RequestBody ZkNodeDto dto) {
        if (NullUtil.isEmpty(dto.getZkHost())) {
            return ResponseEntity
                    .ok(Resp.of(Commons.ERROR_CODE, Commons.ZKHOST_NOTNULL));
        }
        ZkNode node = new ZkNode();
        node.setZkHost(dto.getZkHost());
        node.setRemark(dto.getRemark());
        node.setInfluxdbHost(dto.getInfluxdbHost());
        node.setInfluxdbPass(dto.getInfluxdbPass());
        node.setInfluxdbUser(dto.getInfluxdbUser());
        node.setCreatedAt(DateUtil.now());
        node.setUpdatedAt(DateUtil.now());
        node.setCreatedBy(0);
        node.setUpdatedBy(0);
        nodeRepository.save(node);

        return ResponseEntity
                .ok(Resp.of(Commons.SUCCESS_CODE, Commons.SAVE_SUCCESS_MSG));
    }

    /**
     * 删除单个集群
     *
     * @return
     */
    @PostMapping(value = "/cluster/del/{cid}")
    public ResponseEntity<?> delCluster(@PathVariable Long cid) {
        try {
            nodeRepository.delete(cid);
            return ResponseEntity
                    .ok(Resp.of(Commons.SUCCESS_CODE, Commons.COMMON_SUCCESS_MSG));
        } catch (Exception e) {
            LOGGER.error("del cluster error::", e);
            return ResponseEntity
                    .ok(Resp.of(Commons.ERROR_CODE, Commons.COMMON_ERRO_MSG));
        }
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
