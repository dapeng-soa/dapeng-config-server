package com.github.dapeng.web;

import com.github.dapeng.common.Resp;
import com.github.dapeng.common.Commons;
import com.github.dapeng.entity.WhiteListInfo;
import com.github.dapeng.openapi.utils.Constants;
import com.github.dapeng.repository.WhiteListHistoryRepository;
import com.github.dapeng.repository.WhiteListInfoRepository;
import com.github.dapeng.util.ZkUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    public ResponseEntity<?> sysWhiteList() {
        Optional<List<String>> children = ZkUtil
                .getCurrInstance()
                .getNodeChildren(Constants.SERVICE_WITHELIST_PATH);
        if (children.isPresent()) {
            return ResponseEntity
                    .ok(Resp.of(Commons.SUCCESS_CODE, Commons.LOADED_DATA, children.get()));
            // 同步至数据库进行备份，增量备份
        } else {
            return ResponseEntity
                    .ok(Resp.of(Commons.ERROR_CODE, Commons.DATA_NOTFOUND_MSG, new ArrayList<>()));
        }
    }

    /**
     * 添加单个白名单
     *
     * @return
     */
    @PostMapping(value = "/white/add")
    public ResponseEntity<?> addWhiteItem(String service) {
        if (service.isEmpty()) {
            return ResponseEntity
                    .ok(Resp.of(Commons.ERROR_CODE, Commons.SERVICE_ISEMPTY_MSG));
        } else if (!service.contains(".")) {
            return ResponseEntity
                    .ok(Resp.of(Commons.ERROR_CODE, Commons.SERVICE_FORMAT_EROR_MSG));
        }
        String[] services = service.split("\n|\r|\r\n");
        for (int i = 0; i < services.length; i++) {
            ZkUtil.getCurrInstance().createNode(Constants.SERVICE_WITHELIST_PATH + "/" + services[i], false);
        }
        return ResponseEntity
                .ok(Resp.of(Commons.SUCCESS_CODE, Commons.ADD_WHITELIST_SUCCESS_MSG));
    }

    /**
     * 删除白名单|单个
     *
     * @param path
     * @return
     */
    @PostMapping(value = "/white/del")
    public ResponseEntity<?> delWhiteItem(@RequestParam String path) {
        try {
            ZkUtil.getCurrInstance().delNode(Constants.SERVICE_WITHELIST_PATH + "/" + path);
            return ResponseEntity
                    .ok(Resp.of(Commons.SUCCESS_CODE, Commons.DEL_SUCCESS_MSG));
        } catch (Exception e) {
            LOGGER.error("删除白名单出错::", e);
            return ResponseEntity
                    .ok(Resp.of(Commons.ERROR_CODE, Commons.DEL_ERROR_MSG));
        }
    }
}
