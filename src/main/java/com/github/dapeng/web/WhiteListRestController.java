package com.github.dapeng.web;

import com.github.dapeng.common.Resp;
import com.github.dapeng.common.Commons;
import com.github.dapeng.openapi.utils.Constants;
import com.github.dapeng.openapi.utils.ZkUtil;
import com.github.dapeng.repository.WhiteListHistoryRepository;
import com.github.dapeng.repository.WhiteListInfoRepository;
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

    /**
     * 同步线上白名单
     */
    @GetMapping(value = "/sysWhiteList")
    public ResponseEntity<?> sysWhiteList(@RequestParam(required = false) String host) {
        host = "127.0.0.1:2181";
        ZooKeeper zk = null;
        try {
            zk = ZkUtil.createZkByHost(host);

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
    public ResponseEntity<?> addWhiteItem(@RequestParam(required = false) String host, @RequestParam String service) {
        host = "127.0.0.1:2181";
        if (service.isEmpty()) {
            return ResponseEntity
                    .ok(Resp.of(Commons.ERROR_CODE, Commons.SERVICE_ISEMPTY_MSG));
        } else if (!service.contains(".")) {
            return ResponseEntity
                    .ok(Resp.of(Commons.ERROR_CODE, Commons.SERVICE_FORMAT_EROR_MSG));
        }
        ZooKeeper zk = null;
        try {
            String[] services = service.split("\n|\r|\r\n");
            zk = ZkUtil.createZkByHost(host);
            for (String service1 : services) {
                ZkUtil.createNode(zk, Constants.SERVICE_WITHELIST_PATH + "/" + service1, false);
            }
            return ResponseEntity
                    .ok(Resp.of(Commons.SUCCESS_CODE, Commons.ADD_WHITELIST_SUCCESS_MSG));
        } catch (Exception e) {
            LOGGER.error("add whitelist error", e);
            return ResponseEntity
                    .ok(Resp.of(Commons.ERROR_CODE, Commons.COMMON_ERRO_MSG));
        } finally {
            ZkUtil.closeZk(zk);
        }
    }

    /**
     * 删除白名单|单个
     *
     * @param path
     * @return
     */
    @PostMapping(value = "/white/del")
    public ResponseEntity<?> delWhiteItem(@RequestParam(required = false) String host, @RequestParam String path) {
        host = "127.0.0.1:2181";
        ZooKeeper zk = null;
        try {
            zk = ZkUtil.createZkByHost(host);
            ZkUtil.delNode(zk, Constants.SERVICE_WITHELIST_PATH + "/" + path);
            return ResponseEntity
                    .ok(Resp.of(Commons.SUCCESS_CODE, Commons.DEL_SUCCESS_MSG));
        } catch (Exception e) {
            LOGGER.error("删除白名单出错::", e);
            return ResponseEntity
                    .ok(Resp.of(Commons.ERROR_CODE, Commons.DEL_ERROR_MSG));
        } finally {
            ZkUtil.closeZk(zk);
        }
    }
}
