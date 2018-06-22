package com.github.dapeng.web;

import com.github.dapeng.common.Resp;
import com.github.dapeng.common.Commons;
import com.github.dapeng.datasource.DataSource;
import com.github.dapeng.dto.ApiKeyInfoDto;
import com.github.dapeng.entity.ApiKeyInfo;
import com.github.dapeng.repository.ApiKeyInfoRepository;
import com.github.dapeng.util.DateUtil;
import com.github.dapeng.util.NullUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.github.dapeng.common.Commons.EXTRA_DATASOURCE;

/**
 * @author with struy.
 * Create by 2018/6/7 20:39
 * email :yq1724555319@gmail.com
 */
@RestController
@Transactional(rollbackFor = Throwable.class)
@RequestMapping("/api")
public class ApikeyRestController {
    @Autowired
    ApiKeyInfoRepository repository;

    /**
     * 获取历史apikey配置信息
     *
     * @return
     */
    @GetMapping(value = "/authkeys")
    @DataSource(EXTRA_DATASOURCE)
    public ResponseEntity<?> getApiKeys() {
        List<ApiKeyInfo> apiKeyInfos = repository.findAll();
        return ResponseEntity
                .ok(Resp.of(Commons.SUCCESS_CODE, Commons.LOADED_DATA, apiKeyInfos));
    }

    /**
     * 添加
     *
     * @param dto
     * @return
     */
    @PostMapping(value = "/apikey/add")
    @DataSource(EXTRA_DATASOURCE)
    public ResponseEntity<?> addKey(@RequestBody ApiKeyInfoDto dto) {

        if (NullUtil.isEmpty(dto.getApiKey()) || NullUtil.isEmpty(dto.getPassword())) {
            return ResponseEntity
                    .ok(Resp.of(Commons.ERROR_CODE, Commons.APIKEY_PWD_NOTNULL));
        } else if (dto.getPassword().length() < Commons.DEF_PWD_LENGTH) {
            return ResponseEntity
                    .ok(Resp.of(Commons.ERROR_CODE, Commons.PWD_LENGTH_ERROR));
        }
        ApiKeyInfo info = new ApiKeyInfo();
        // 前置判断
        info.setApiKey(dto.getApiKey());
        info.setBiz(dto.getBiz());
        info.setIps(NullUtil.isEmpty(dto.getIps()) ? "*" : dto.getIps());
        info.setPassword(dto.getPassword());
        info.setTimeout((NullUtil.isEmpty(dto.getTimeout()) || Math.abs(dto.getTimeout()) < 60) ? 60 : Math.abs(dto.getTimeout()));
        info.setNotes(dto.getNotes());
        info.setCreatedAt(DateUtil.now());
        info.setUpdatedAt(DateUtil.now());
        info.setCreatedBy(0);
        info.setUpdatedBy(0);
        repository.save(info);

        return ResponseEntity
                .ok(Resp.of(Commons.SUCCESS_CODE, Commons.SAVE_SUCCESS_MSG));
    }
}
