package com.github.dapeng.web;

import com.github.dapeng.common.CommonRepose;
import com.github.dapeng.dto.ConfigInfoDto;
import com.github.dapeng.entity.ConfigInfo;
import com.github.dapeng.openapi.cache.ZookeeperClient;
import com.github.dapeng.openapi.utils.Constants;
import com.github.dapeng.repository.ConfigInfoRepository;
import com.github.dapeng.util.CheckConfigUtil;
import com.github.dapeng.util.ZkUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

import com.github.dapeng.common.ConfigStatusEnum;
import com.github.dapeng.common.Commons;

/**
 * @author with struy.
 * Create by 2018/5/31 00:17
 * email :yq1724555319@gmail.com
 */
@RestController
@RequestMapping("/api")
@Transactional(rollbackFor = Throwable.class)
public class ConfigRestController {
    private static Logger LOGGER = LoggerFactory.getLogger(ConfigRestController.class);

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
        try {
            saveConfig(configInfoDto, ConfigStatusEnum.PASS.key());
            return ResponseEntity
                    .ok(CommonRepose.of(Commons.SUCCESS_CODE, Commons.SAVE_SUCCESS_MSG));
        } catch (Exception e) {
            LOGGER.error("添加服务配置出错:", e);
            return ResponseEntity
                    .ok(CommonRepose.of(Commons.ERROR_CODE, e.getMessage()));
        }

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
     * 修改配置信息
     * 1.修改配置信息,将新的修改保存为新的记录
     * 2.上一条记录将不做任何改动,相当记录流水信息
     *
     * @param id
     * @return
     */
    @PostMapping(value = "/config/edit/{id}")
    public ResponseEntity<?> editConfig(@PathVariable Long id, @RequestBody ConfigInfoDto configInfoDto) {
        try {
            saveConfig(configInfoDto, ConfigStatusEnum.PASS.key());
            return ResponseEntity
                    .ok(CommonRepose.of(Commons.SUCCESS_CODE, Commons.SAVE_SUCCESS_MSG));
        } catch (Exception e) {
            LOGGER.error("保存新改配置出错:", e);
            return ResponseEntity
                    .ok(CommonRepose.of(Commons.ERROR_CODE, e.getMessage()));
        }
    }

    /**
     * 修改配置并发布
     * 0.修改历史配置为审核通过状态
     * 1.保存修改配置为新配置
     * 2.发布配置(当前提交新增的配置)
     * 3.发布的配置是当前配置
     *
     * @param id
     * @return
     */
    @PostMapping(value = "/config/editAndPublish/{id}")
    public ResponseEntity<?> editAndPublish(@PathVariable Long id, @RequestBody ConfigInfoDto configInfoDto) {
        try {

            // 0.修改历史配置为审核通过状态
            List<ConfigInfo> configInfos = repository.findByServiceName(configInfoDto.getServiceName());
            configInfos.forEach(c -> {
                c.setStatus(ConfigStatusEnum.PASS.key());
            });
            // 1.保存修改配置为新配置,并且状态为已发布

            saveConfigAndPublish(configInfoDto, ConfigStatusEnum.PUBLISHED.key());
            // 2.发布当前提交修改的配置
            processPublish(configInfoDto);

            return ResponseEntity
                    .ok(CommonRepose.of(Commons.SUCCESS_CODE, Commons.EDITED_PUBLISH_SUCCESS_MSG));
        } catch (Exception e) {
            LOGGER.error("保存新改配置出错:", e);
            return ResponseEntity
                    .ok(CommonRepose.of(Commons.ERROR_CODE, e.getMessage()));
        }
    }

    /**
     * 获取单个详情配置
     */
    @GetMapping(value = "/config/{id}")
    public ResponseEntity<?> findOne(@PathVariable Long id) {
        ConfigInfo configInfo = repository.getOne(id);
        if (null != configInfo) {
            return ResponseEntity
                    .ok(CommonRepose.of(Commons.SUCCESS_CODE, configInfo));
        } else {
            return ResponseEntity
                    .ok(CommonRepose.of(Commons.ERROR_CODE, Commons.DATA_NOTFOUND_MSG));
        }
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
        PageRequest pageRequest = new PageRequest
                (page - 1, rows,
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
        return publish(id);
    }


    /**
     * 回滚配置
     */
    @PostMapping(value = "/config/rollback/{id}")
    public ResponseEntity<?> rollbackConfig(@PathVariable Long id) {
        // 回滚：：==> 重置为发布状态 ==> 更新
        return ResponseEntity.ok(CommonRepose.of(Commons.SUCCESS_CODE, Commons.ROLLBACK_SUCCESS_MSG));
    }


    /**
     * 发布逻辑
     */
    private ResponseEntity<?> publish(Long id) {
        // 将配置发布到对应的zk节点data或者node
        ConfigInfo config = repository.getOne(id);
        if (null != config) {
            if (config.getStatus() == ConfigStatusEnum.PUBLISHED.key()) {
                return ResponseEntity
                        .ok(CommonRepose.of(Commons.SUCCESS_CODE, Commons.CONFIG_PUBLISHED_MSG));
            }

            // 更新历史发布为初始状态
            List<ConfigInfo> configInfos = repository.findByServiceName(config.getServiceName());
            configInfos.forEach(c -> {
                if (c.getId() != config.getId()) {
                    c.setStatus(ConfigStatusEnum.PASS.key());
                }
            });

            ConfigInfoDto cid = new ConfigInfoDto();
            cid.setServiceName(config.getServiceName());
            cid.setFreqConfig(config.getFreqConfig());
            cid.setLoadbalanceConfig(config.getLoadbalanceConfig());
            cid.setTimeoutConfig(config.getTimeoutConfig());
            cid.setRouterConfig(config.getRouterConfig());

            try {
                processPublish(cid);
            } catch (Exception e) {
                return ResponseEntity
                        .ok(CommonRepose.of(Commons.SUCCESS_CODE, e.getMessage()));
            }
            // 已发布
            config.setStatus(ConfigStatusEnum.PUBLISHED.key());
            Long now = System.currentTimeMillis();
            config.setUpdatedAt(new Timestamp(now));
            config.setPublishedAt(new Timestamp(now));
            config.setUpdatedBy(0);
            config.setPublishedBy(0);
            return ResponseEntity
                    .ok(CommonRepose.of(Commons.SUCCESS_CODE, Commons.PUBLISH_SUCCESS_MSG));
        } else {
            return ResponseEntity
                    .ok(CommonRepose.of(Commons.SUCCESS_CODE, Commons.DATA_NOTFOUND_MSG));
        }
    }


    /**
     * 通过dto信息保存配置信息
     *
     * @param configInfoDto
     */
    private ConfigInfo saveConfig(ConfigInfoDto configInfoDto, int status) throws Exception {
        CheckGrammar(configInfoDto);
        ConfigInfo configInfo = new ConfigInfo();
        configInfo.setServiceName(configInfoDto.getServiceName());
        Long now = System.currentTimeMillis();
        configInfo.setCreatedAt(new Timestamp(now));
        configInfo.setUpdatedAt(new Timestamp(now));
        configInfo.setStatus(status);
        configInfo.setVersion(UUID.randomUUID().toString());
        configInfo.setFreqConfig(configInfoDto.getFreqConfig());
        configInfo.setRouterConfig(configInfoDto.getRouterConfig());
        configInfo.setTimeoutConfig(configInfoDto.getTimeoutConfig());
        configInfo.setLoadbalanceConfig(configInfoDto.getLoadbalanceConfig());

        return repository.saveAndFlush(configInfo);
    }


    /**
     * 通过dto信息保存配置信息
     *
     * @param configInfoDto
     */
    private ConfigInfo saveConfigAndPublish(ConfigInfoDto configInfoDto, int status) throws Exception {
        CheckGrammar(configInfoDto);
        ConfigInfo configInfo = new ConfigInfo();
        configInfo.setServiceName(configInfoDto.getServiceName());
        configInfo.setStatus(status);
        configInfo.setVersion(UUID.randomUUID().toString());
        configInfo.setFreqConfig(configInfoDto.getFreqConfig());
        configInfo.setRouterConfig(configInfoDto.getRouterConfig());
        configInfo.setTimeoutConfig(configInfoDto.getTimeoutConfig());
        configInfo.setLoadbalanceConfig(configInfoDto.getLoadbalanceConfig());
        Long now = System.currentTimeMillis();
        configInfo.setCreatedAt(new Timestamp(now));
        configInfo.setUpdatedAt(new Timestamp(now));
        configInfo.setPublishedAt(new Timestamp(now));
        configInfo.setUpdatedBy(0);
        configInfo.setPublishedBy(0);

        return repository.saveAndFlush(configInfo);
    }

    private void CheckGrammar(ConfigInfoDto cofig) throws Exception {
        // 语法检查
        boolean freqCheckStatus = CheckConfigUtil.doCheckFreq(cofig.getFreqConfig());
        if (!freqCheckStatus) {
            throw new Exception("限流配置格式错误，请检查！");
        }
        boolean routerCheckStatus = CheckConfigUtil.doCheckRouter(cofig.getRouterConfig());
        if (!routerCheckStatus) {
            throw new Exception("路由配置格式错误，请检查！");
        }
        boolean timeoutCheckStatus = CheckConfigUtil.doCheckTimeOut(cofig.getTimeoutConfig());
        if (!timeoutCheckStatus) {
            throw new Exception("超时配置格式错误，请检查！");
        }
        boolean loadbalanceCheckStatus = CheckConfigUtil.doCheckLoadbalance(cofig.getLoadbalanceConfig());
        if (!loadbalanceCheckStatus) {
            throw new Exception("负载均衡配置格式错误，请检查！");
        }
    }

    private void processPublish(ConfigInfoDto cid) throws Exception {
        ZookeeperClient zk = ZkUtil.getCurrInstance();
        String service = cid.getServiceName();
        // 超时，负载均衡
        zk.createData(Constants.SERVICE_RUNTIME_PATH + "/" + service, cid.getTimeoutConfig() + cid.getLoadbalanceConfig());
        // 路由
        zk.createData(Constants.CONFIG_ROUTER_PATH + "/" + service, cid.getRouterConfig());
        // 限流
        zk.createData(Constants.CONFIG_FREQ_PATH + "/" + service, cid.getFreqConfig());
    }
}
