package com.github.dapeng.web;

import com.github.dapeng.common.Commons;
import com.github.dapeng.common.Resp;
import com.github.dapeng.entity.deploy.TConfigFiles;
import com.github.dapeng.repository.deploy.ConfigFilesRepository;
import com.github.dapeng.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.github.dapeng.common.Commons.*;
import static com.github.dapeng.util.NullUtil.*;

/**
 * @author with struy.
 * Create by 2018/9/14 14:37
 * email :yq1724555319@gmail.com
 */
@RestController
@RequestMapping("/api")
@Transactional(rollbackFor = Throwable.class)
public class ConfigFilesController {
    @Autowired
    ConfigFilesRepository configFilesRepository;

    @PostMapping("/config-file/add")
    public ResponseEntity addConfigFile(@RequestBody List<TConfigFiles> configFiles) {
        try {
            configFiles.forEach(x -> {
                if (isEmpty(x.getId())) {
                    x.setCreatedAt(DateUtil.now());
                    x.setRemark("");
                    x.setUpdatedAt(DateUtil.now());
                    configFilesRepository.save(x);
                } else {
                    TConfigFiles one = configFilesRepository.findOne(x.getId());
                    one.setUpdatedAt(DateUtil.now());
                    one.setFileContext(x.getFileContext());
                    one.setServiceId(x.getServiceId());
                    one.setFileName(x.getFileName());
                }
            });
            return ResponseEntity
                    .ok(Resp.of(SUCCESS_CODE, SAVE_SUCCESS_MSG));
        } catch (Exception e) {
            return ResponseEntity
                    .ok(Resp.of(Commons.ERROR_CODE, e.getMessage()));
        }
    }

    @GetMapping("/config-files/{setId}")
    public ResponseEntity configFiles(@PathVariable Long setId) {
        List<TConfigFiles> bySetId = configFilesRepository.findBySetId(setId);
        return ResponseEntity
                .ok(Resp.of(SUCCESS_CODE, LOADED_DATA, bySetId));

    }
}
