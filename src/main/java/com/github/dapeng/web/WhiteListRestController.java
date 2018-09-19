package com.github.dapeng.web;

import com.github.dapeng.common.Resp;
import com.github.dapeng.common.Commons;
import com.github.dapeng.entity.config.ZkNode;
import com.github.dapeng.openapi.utils.Constants;
import com.github.dapeng.openapi.utils.ZkUtil;
import com.github.dapeng.repository.WhiteListHistoryRepository;
import com.github.dapeng.repository.WhiteListInfoRepository;
import com.github.dapeng.repository.ZkNodeRepository;
import com.github.dapeng.util.NullUtil;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author with struy.
 * Create by 2018/6/6 09:54
 * email :yq1724555319@gmail.com
 */

@RestController
@Transactional(rollbackFor = Throwable.class)
@RequestMapping("/api")
public class WhiteListRestController {

    private static Logger LOGGER = LoggerFactory.getLogger(WhiteListRestController.class);

    @Autowired
    WhiteListInfoRepository listInfoRepository;

    @Autowired
    WhiteListHistoryRepository historyRepository;

    @Autowired
    ZkNodeRepository nodeRepository;

    /**
     * 同步线上白名单
     */
    @GetMapping(value = "/sysWhiteList")
    public ResponseEntity<?> sysWhiteList(@RequestParam Long cid) {
        ZooKeeper zk = null;
        try {
            ZkNode node = nodeRepository.findOne(cid);
            if (!NullUtil.isEmpty(node)) {
                zk = ZkUtil.createZkByHost(node.getZkHost());
                List<String> children = ZkUtil
                        .getNodeChildren(zk, Constants.SERVICE_WITHELIST_PATH, false);
                if (null != children) {
                    return ResponseEntity
                            .ok(Resp.of(Commons.SUCCESS_CODE, Commons.LOADED_DATA, children));
                    // 同步至数据库进行备份，增量备份
                } else {
                    return ResponseEntity
                            .ok(Resp.of(Commons.ERROR_CODE, Commons.DATA_NOTFOUND_MSG, new ArrayList<>()));
                }
            } else {
                return ResponseEntity
                        .ok(Resp.of(Commons.ERROR_CODE, Commons.DATA_NOTFOUND_MSG, new ArrayList<>()));
            }
        } catch (Exception e) {
            return ResponseEntity
                    .ok(Resp.of(Commons.ERROR_CODE, Commons.DATA_NOTFOUND_MSG, new ArrayList<>()));
        } finally {
            ZkUtil.closeZk(zk);
        }
    }

    /**
     * 添加单个白名单
     *
     * @return
     */
    @PostMapping(value = "/white/add")
    public ResponseEntity<?> addWhiteItem(@RequestParam Long cid, @RequestParam String service) {
        if (service.isEmpty()) {
            return ResponseEntity
                    .ok(Resp.of(Commons.ERROR_CODE, Commons.SERVICE_ISEMPTY_MSG));
        } else if (!service.contains(".")) {
            return ResponseEntity
                    .ok(Resp.of(Commons.ERROR_CODE, Commons.SERVICE_FORMAT_EROR_MSG));
        }
        try {
            String[] services = service.split("\n|\r|\r\n");
            ZkNode node = nodeRepository.findOne(cid);
            if (!NullUtil.isEmpty(node)) {
                processAddWhiteList(node.getZkHost(), services);
                return ResponseEntity
                        .ok(Resp.of(Commons.SUCCESS_CODE, Commons.ADD_WHITELIST_SUCCESS_MSG));
            } else if (cid == -1) {
                List<ZkNode> list = nodeRepository.findAll();
                for (ZkNode zkNode : list) {
                    processAddWhiteList(zkNode.getZkHost(), services);
                }
                return ResponseEntity
                        .ok(Resp.of(Commons.SUCCESS_CODE, Commons.ADD_WHITELIST_SUCCESS_MSG));
            }
        } catch (Exception e) {
            LOGGER.error("add whitelist error", e);
        }
        return ResponseEntity
                .ok(Resp.of(Commons.ERROR_CODE, Commons.COMMON_ERRO_MSG));
    }

    /**
     * 执行添加
     *
     * @param host
     * @param services
     */
    private void processAddWhiteList(String host, String[] services) throws Exception {
        ZooKeeper zk = ZkUtil.createZkByHost(host);
        for (String service1 : services) {
            ZkUtil.createNode(zk, Constants.SERVICE_WITHELIST_PATH + "/" + service1, false);
        }
        ZkUtil.closeZk(zk);
    }

    /**
     * 删除白名单|单个
     *
     * @param path
     * @return
     */
    @PostMapping(value = "/white/del")
    public ResponseEntity<?> delWhiteItem(@RequestParam Long cid, @RequestParam String path) {
        ZooKeeper zk = null;
        try {
            ZkNode node = nodeRepository.findOne(cid);
            if (!NullUtil.isEmpty(node)) {
                zk = ZkUtil.createZkByHost(node.getZkHost());
                ZkUtil.delNode(zk, Constants.SERVICE_WITHELIST_PATH + "/" + path);
                return ResponseEntity
                        .ok(Resp.of(Commons.SUCCESS_CODE, Commons.DEL_SUCCESS_MSG));
            }
        } catch (Exception e) {
            LOGGER.error("删除白名单出错::", e);
        } finally {
            ZkUtil.closeZk(zk);
        }
        return ResponseEntity
                .ok(Resp.of(Commons.ERROR_CODE, Commons.DEL_ERROR_MSG));
    }
}
