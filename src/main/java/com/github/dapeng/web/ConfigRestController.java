package com.github.dapeng.web;

import com.github.dapeng.common.CommonRepose;
import com.github.dapeng.dto.ConfigInfoDto;
import com.github.dapeng.entity.ConfigInfo;
import com.github.dapeng.openapi.cache.ZookeeperClient;
import com.github.dapeng.openapi.utils.Constants;
import com.github.dapeng.openapi.utils.EnvUtil;
import com.github.dapeng.repository.ConfigInfoRepository;
import com.github.dapeng.util.ZkUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.Optional;
import java.util.UUID;

import com.github.dapeng.constant.ConfigStatusEnum;
import com.github.dapeng.constant.Commons;

/**
 * @author with struy.
 * Create by 2018/5/31 00:17
 * email :yq1724555319@gmail.com
 */
@RestController
@RequestMapping("/api")
@Transactional(rollbackFor = Throwable.class)
public class ConfigRestController {

    @Autowired
    ConfigInfoRepository repository;

    /**
     * 添加配置
     */
    @PostMapping(value = "/config/add")
    public ResponseEntity<?> addConfig(@RequestBody ConfigInfoDto configInfoDto) {
        if (configInfoDto.getServiceName().isEmpty()) return ResponseEntity
                .ok(CommonRepose.of(Commons.SUCCESS_CODE, Commons.SERVICE_ISEMPTY_MSG));
        // 校验呢配置规则？

        ConfigInfo configInfo = new ConfigInfo();
        configInfo.setServiceName(configInfoDto.getServiceName());
        configInfo.setStatus(ConfigStatusEnum.PASS.key());
        configInfo.setVersion(UUID.randomUUID().toString());
        configInfo.setFreqConfig(configInfoDto.getFreqConfig());
        configInfo.setRouterConfig(configInfoDto.getRouterConfig());
        configInfo.setTimeoutConfig(configInfoDto.getTimeoutConfig());
        configInfo.setLoadbalanceConfig(configInfoDto.getLoadbalanceConfig());

        repository.save(configInfo);
        return ResponseEntity
                .ok(CommonRepose.of(Commons.SUCCESS_CODE, Commons.SAVE_SUCCESS_MSG));
    }

    /**
     * 删除配置信息
     *
     * @param id
     * @return
     */
    @PostMapping(value = "/config/delete/{id}")
    public ResponseEntity<?> delConfig(@PathVariable Long id) {
        ConfigInfo configInfo = repository.getOne(id);
        // 失效
        configInfo.setStatus(ConfigStatusEnum.FAILURE.key());
        return ResponseEntity
                .ok(CommonRepose.of(Commons.SUCCESS_CODE, Commons.DEL_SUCCESS_MSG));
    }

    /**
     * 获取单个详情配置
     */
    @GetMapping(value = "/config/{id}")
    public ResponseEntity<?> findOne(@PathVariable Long id) {
        Optional<ConfigInfo> configInfo = repository.findById(id);
        return ResponseEntity
                .ok(CommonRepose.of(Commons.SUCCESS_CODE, configInfo));
    }

    /**
     * 分页获取配置信息
     *
     * @param
     * @return
     */
    @GetMapping(value = "/configs")
    public ResponseEntity<?> getConfig(@RequestParam int page,
                                       @RequestParam int rows,
                                       @RequestParam(required = false) String sort,
                                       @RequestParam(required = false) String sortOrder,
                                       @RequestParam(required = false) String keyword) {
        if (page <= 0) return ResponseEntity
                .ok(CommonRepose.of(Commons.SUCCESS_CODE, Commons.PAGENO_ERROR_MSG));
        PageRequest pageRequest = PageRequest
                .of(page - 1, rows,
                        new Sort("desc".toUpperCase().equals(sortOrder.toUpperCase()) ? Sort.Direction.DESC : Sort.Direction.ASC,
                                null == sort ? "updatedAt" : sort));

        Page<ConfigInfo> infos = repository.findAllByServiceNameLikeOrVersionLike('%' + keyword + '%', '%' + keyword + '%', pageRequest);
        return ResponseEntity
                .ok(CommonRepose.of(Commons.SUCCESS_CODE, infos));
    }

    /**
     * 发布配置信息
     */
    @PostMapping(value = "/config/publish/{id}")
    public ResponseEntity<?> publishConfig(@PathVariable Long id) {
        ZookeeperClient zk = ZkUtil.getCurrInstance();
        // 将配置发布到对应的zk节点data或者node
        Optional<ConfigInfo> config = repository.findById(id);
        if (config.isPresent()){
            ConfigInfo ci = config.get();
            if (ci.getStatus()==ConfigStatusEnum.PUBLISHED.key()){
                return ResponseEntity
                        .ok(CommonRepose.of(Commons.SUCCESS_CODE, Commons.CONFIG_PUBLISHED_MSG));
            }
            String service = ci.getServiceName();
            // 超时，负载均衡
            zk.createData(Constants.SERVICE_RUNTIME_PATH+"/"+service,ci.getTimeoutConfig()+ci.getLoadbalanceConfig());
            // 路由
            zk.createData(Constants.CONFIG_ROUTER_PATH+"/"+service,ci.getRouterConfig());
            // 限流
            zk.createData(Constants.CONFIG_FREQ_PATH+"/"+service,ci.getFreqConfig());
            // 已发布
            Long now = System.currentTimeMillis();
            ci.setStatus(ConfigStatusEnum.PUBLISHED.key());
            ci.setUpdatedAt(new Timestamp(now));
            ci.setPublishedAt(new Timestamp(now));
            ci.setUpdatedBy(0);
            ci.setPublishedBy(0);
            return ResponseEntity
                    .ok(CommonRepose.of(Commons.SUCCESS_CODE, Commons.PUBLISH_SUCCESS_MSG));
        }else {
            return ResponseEntity
                    .ok(CommonRepose.of(Commons.SUCCESS_CODE, Commons.DATA_NOTFOUND_MSG));
        }
    }

    /**
     * 回滚配置
     */
    @PostMapping(value = "/config/rollback/{id}")
    public ResponseEntity<?> rollbackConfig(@PathVariable Long id) {
        // 回滚：：==> 重置为发布状态 ==> 更新
        return ResponseEntity.ok(CommonRepose.of(Commons.SUCCESS_CODE, Commons.ROLLBACK_SUCCESS_MSG));
    }


}
