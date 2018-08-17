package com.github.dapeng.web;

import com.github.dapeng.common.Commons;
import com.github.dapeng.common.Resp;
import com.github.dapeng.datasource.DataSource;
import com.github.dapeng.dto.ApiKeyInfoDto;
import com.github.dapeng.entity.ApiKeyInfo;
import com.github.dapeng.repository.ApiKeyInfoRepository;
import com.github.dapeng.util.DateUtil;
import com.github.dapeng.util.MailSend;
import com.github.dapeng.util.NullUtil;
import com.github.dapeng.util.PasswordUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

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

    private static Logger LOGGER = LoggerFactory.getLogger(ApikeyRestController.class);

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
        } else if (dto.getPassword().length() != Commons.DEF_PWD_LENGTH) {
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
        info.setStatus(0);
        info.setValidated(dto.getValidated());
        info.setNotes(dto.getNotes());
        info.setCreatedAt(DateUtil.now());
        info.setUpdatedAt(DateUtil.now());
        info.setCreatedBy(0);
        info.setUpdatedBy(0);
        repository.save(info);

        return ResponseEntity
                .ok(Resp.of(Commons.SUCCESS_CODE, Commons.SAVE_SUCCESS_MSG));
    }

    /**
     * @param id
     * @return
     */
    @GetMapping(value = "/apikey/{id}")
    @DataSource(EXTRA_DATASOURCE)
    public ResponseEntity<?> finOneKey(@PathVariable Long id) {
        ApiKeyInfo info = repository.getOne(id);
        return ResponseEntity
                .ok(Resp.of(Commons.SUCCESS_CODE, Commons.LOADED_DATA, info));
    }


    /**
     * 修改apikey
     *
     * @param id
     * @param dto
     * @return
     */
    @PostMapping(value = "/apikey/edit/{id}")
    @DataSource(EXTRA_DATASOURCE)
    public ResponseEntity<?> updateApiKey(@PathVariable Long id, @RequestBody ApiKeyInfoDto dto) {
        try {
            ApiKeyInfo info = repository.getOne(id);
            info.setPassword(dto.getPassword());
            info.setTimeout(dto.getTimeout());
            info.setBiz(dto.getBiz());
            info.setIps(dto.getIps());
            info.setNotes(dto.getNotes());
            info.setValidated(dto.getValidated());
            repository.save(info);
            return ResponseEntity
                    .ok(Resp.of(Commons.SUCCESS_CODE, Commons.COMMON_SUCCESS_MSG));
        } catch (Exception e) {
            LOGGER.error("save apikey error::", e);
            return ResponseEntity
                    .ok(Resp.of(Commons.ERROR_CODE, Commons.COMMON_ERRO_MSG));
        }
    }

    /**
     * 生成随机key
     *
     * @return
     */
    @GetMapping("/apikey/genkey")
    public ResponseEntity genKey() {
        String key = PasswordUtil.createPassword(UUID.randomUUID().toString(), UUID.randomUUID().toString());
        return ResponseEntity
                .ok(Resp.of(Commons.SUCCESS_CODE, Commons.COMMON_SUCCESS_MSG, key));
    }

    /**
     * 下发apikey
     *
     * @param id
     * @param email
     * @return
     */
    @PostMapping("/apikey/send/{id}")
    @DataSource(EXTRA_DATASOURCE)
    public ResponseEntity sendKey(@PathVariable Long id, @RequestParam String email) {
        try {
            ApiKeyInfo info = repository.getOne(id);
            MailSend.sendApiKeyMail(info, email);
            return ResponseEntity
                    .ok(Resp.of(Commons.SUCCESS_CODE, Commons.COMMON_SUCCESS_MSG));
        }catch (Exception e){
            return ResponseEntity
                    .ok(Resp.of(Commons.ERROR_CODE, Commons.COMMON_ERRO_MSG));
        }
    }
}
