package com.github.dapeng.web;

import com.github.dapeng.common.Commons;
import com.github.dapeng.common.Resp;
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

    /**
     * 添加集群
     * @return
     */
    @PostMapping(value = "/cluster/add/")
    @Deprecated
    public ResponseEntity<?> addKey() {
        return ResponseEntity
                .ok(Resp.of(Commons.SUCCESS_CODE,Commons.SAVE_SUCCESS_MSG));
    }

}
