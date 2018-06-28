package com.github.dapeng.web;

import com.github.dapeng.common.Resp;
import com.github.dapeng.dto.ConfigInfoDto;
import com.github.dapeng.dto.RealConfig;
import com.github.dapeng.entity.ConfigInfo;
import com.github.dapeng.entity.ConfigPublishHistory;
import com.github.dapeng.entity.ZkNode;
import com.github.dapeng.openapi.utils.Constants;
import com.github.dapeng.openapi.utils.ZkUtil;
import com.github.dapeng.repository.ConfigInfoRepository;
import com.github.dapeng.repository.ConfigPublishRepository;
import com.github.dapeng.repository.ZkNodeRepository;
import com.github.dapeng.util.*;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import com.github.dapeng.common.ConfigStatus;
import com.github.dapeng.common.Commons;

import java.util.List;

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

    @Autowired
    ConfigPublishRepository publishRepository;

    @Autowired
    ZkNodeRepository nodeRepository;

    /**
     * 添加配置
     * TODO 应当有一份服务名单用作校验服务名的正确性
     * 0.配置语法规则校验
     * 1.检查服务是否已经存在
     */
    @PostMapping(value = "/config/add")
    public ResponseEntity<?> addConfig(@RequestBody ConfigInfoDto configInfoDto) {

        if (configInfoDto.getServiceName().isEmpty()) return ResponseEntity
                .ok(Resp.of(Commons.ERROR_CODE, Commons.SERVICE_ISEMPTY_MSG));
        try {
            checkGrammar(configInfoDto);
            boolean hasService = repository.existsConfigInfoByServiceNameAndStatusIsNot(
                    configInfoDto.getServiceName(), ConfigStatus.FAILURE.key());
            // 如果存在此服务的配置
            if (hasService) {
                return ResponseEntity
                        .ok(Resp.of(Commons.ERROR_CODE, Commons.SERVICE_ISEXISTS_MSG));
            }
            // 状态应暂时默认为通过(后期添加审核流程)正常情况下应当是新建
            saveNewConfig(configInfoDto, ConfigStatus.PASS.key());
            return ResponseEntity
                    .ok(Resp.of(Commons.SUCCESS_CODE, Commons.SAVE_SUCCESS_MSG));
        } catch (Exception e) {
            LOGGER.error("添加服务配置出错::", e);
            return ResponseEntity
                    .ok(Resp.of(Commons.ERROR_CODE, e.getMessage()));
        }

    }


    /**
     * 检查服务配置是否已存在
     *
     * @param serviceName
     * @return
     */
    @GetMapping(value = "/config/exists/{serviceName}")
    public ResponseEntity<?> checkServiceExists(@PathVariable String serviceName) {
        boolean serviceIsExits = repository.existsConfigInfoByServiceNameAndStatusIsNot(serviceName, ConfigStatus.FAILURE.key());
        return ResponseEntity
                .ok(Resp.of(Commons.SUCCESS_CODE, Commons.LOADED_DATA, serviceIsExits));
    }

    /**
     * 删除配置信息
     *
     * @param id
     * @return
     */
    @PostMapping(value = "/config/delete/{id}")
    public ResponseEntity<?> delConfig(@PathVariable Long id) {
        try {
            ConfigInfo info = repository.getOne(id);
            if (null != info) {
                // 失效
                info.setUpdatedAt(DateUtil.now());
                info.setStatus(ConfigStatus.FAILURE.key());
                return ResponseEntity
                        .ok(Resp.of(Commons.SUCCESS_CODE, Commons.DEL_SUCCESS_MSG));
            } else {
                return ResponseEntity
                        .ok(Resp.of(Commons.ERROR_CODE, Commons.DATA_NOTFOUND_MSG));
            }
        } catch (Exception e) {
            LOGGER.error("删除配置失败::", e);
            return ResponseEntity
                    .ok(Resp.of(Commons.ERROR_CODE, Commons.DEL_ERROR_MSG));
        }
    }

    /**
     * 修改配置信息
     * 如果配置状态是未发布，则需要改变状态为初始状态==>[新建=>审核通过]
     *
     * @param id
     * @return
     */
    @PostMapping(value = "/config/edit/{id}")
    public ResponseEntity<?> editConfig(@PathVariable Long id, @RequestBody ConfigInfoDto infoDto) {
        try {
            checkGrammar(infoDto);
            ConfigInfo info = repository.getOne(id);
            if (null != info) {
                info.setStatus(ConfigStatus.PASS.key());
                info.setUpdatedBy(0);
                info.setUpdatedAt(DateUtil.now());
                info.setRemark(infoDto.getRemark());
                info.setTimeoutConfig(infoDto.getTimeoutConfig());
                info.setLoadbalanceConfig(infoDto.getLoadbalanceConfig());
                info.setRouterConfig(infoDto.getRouterConfig());
                info.setFreqConfig(infoDto.getFreqConfig());
                repository.save(info);
                return ResponseEntity
                        .ok(Resp.of(Commons.SUCCESS_CODE, Commons.SAVE_SUCCESS_MSG));
            } else {
                return ResponseEntity
                        .ok(Resp.of(Commons.ERROR_CODE, Commons.DATA_NOTFOUND_MSG));
            }
        } catch (Exception e) {
            LOGGER.error("修改配置出错:", e);
            return ResponseEntity
                    .ok(Resp.of(Commons.ERROR_CODE, e.getMessage()));
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
                    .ok(Resp.of(Commons.SUCCESS_CODE, Commons.LOADED_DATA, configInfo));
        } else {
            return ResponseEntity
                    .ok(Resp.of(Commons.ERROR_CODE, Commons.DATA_NOTFOUND_MSG));
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
                .ok(Resp.of(Commons.SUCCESS_CODE, Commons.PAGENO_ERROR_MSG));

        PageRequest pageRequest = new PageRequest
                (page - 1, rows,
                        new Sort("desc".toUpperCase().equals(sortOrder.toUpperCase()) ? Sort.Direction.DESC : Sort.Direction.ASC,
                                null == sort ? "updatedAt" : sort));

        Page<ConfigInfo> infos = repository.findAllByStatusIsNotAndServiceNameLike(ConfigStatus.FAILURE.key(), '%' + keyword + '%', pageRequest);
        return ResponseEntity
                .ok(Resp.of(Commons.SUCCESS_CODE, Commons.LOADED_DATA, infos));
    }

    /**
     * 发布配置信息
     */
    @PostMapping(value = "/config/publish/{id}")
    public ResponseEntity<?> publishConfig(@PathVariable Long id,
                                           @RequestParam Long cid) {
        ZkNode node = nodeRepository.findOne(cid);
        if (!NullUtil.isEmpty(node)) {
            String host = node.getZkHost();
            return publish(host, id);
        } else if (cid == -1) {
            List<ZkNode> list = nodeRepository.findAll();
            list.forEach(zkNode -> {
                publish(zkNode.getZkHost(), id);
            });
            return ResponseEntity.ok(Resp.of(Commons.SUCCESS_CODE, Commons.COMMON_ERRO_MSG));
        } else {
            return ResponseEntity.ok(Resp.of(Commons.ERROR_CODE, Commons.COMMON_ERRO_MSG));
        }
    }


    /**
     * 发布历史
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/config/publish-history/{id}")
    public ResponseEntity<?> publishHistory(@PathVariable Long id) {
        ConfigInfo info = repository.getOne(id);
        if (!NullUtil.isEmpty(info)) {
            List<ConfigPublishHistory> publishHistories = publishRepository.findAllByServiceName(info.getServiceName());
            return ResponseEntity
                    .ok(Resp.of(Commons.SUCCESS_CODE, Commons.LOADED_DATA, publishHistories));
        } else {
            return ResponseEntity
                    .ok(Resp.of(Commons.ERROR_CODE, Commons.DATA_NOTFOUND_MSG));
        }
    }

    /**
     * 回滚配置
     */
    @PostMapping(value = "/config/rollback/{id}")
    @Deprecated
    public ResponseEntity<?> rollbackConfig(@PathVariable Long id) {
        // 回滚：：==> 重置为发布状态 ==> 更新
        return ResponseEntity.ok(Resp.of(Commons.SUCCESS_CODE, Commons.ROLLBACK_SUCCESS_MSG));
    }

    /**
     * 同步线上配置
     *
     * @return
     */
    @GetMapping(value = "/config/sysRealConfig")
    public ResponseEntity<?> sysRealConfig(@RequestParam Long cid,
                                           @RequestParam String serviceName) {
        ZkNode node = nodeRepository.findOne(cid);
        if (!NullUtil.isEmpty(node)) {
            try {
                RealConfig realConfig = proccessSysConfig(node.getZkHost(), serviceName);
                return ResponseEntity
                        .ok(Resp.of(Commons.SUCCESS_CODE, Commons.LOADED_DATA, realConfig));
            } catch (Exception e) {
                LOGGER.error("同步服务配置出错::", e);
                return ResponseEntity
                        .ok(Resp.of(Commons.ERROR_CODE, Commons.SYS_CONFIG_ERROR));
            }
        } else {
            return ResponseEntity
                    .ok(Resp.of(Commons.ERROR_CODE, Commons.SYS_CONFIG_ERROR));
        }
    }


    /**
     * 发布逻辑
     * 1.修改配置的发布状态为已发布
     * 2.将本次的发布信息同步到发布历史表并且以==>年月日时分秒:20180611083030作为版本号
     * 3.执行具体的发布,发布出错则回滚
     */
    private ResponseEntity<?> publish(String host, Long id) {
        ConfigInfo config = repository.getOne(id);
        if (null != config) {
            if (config.getStatus() == ConfigStatus.PUBLISHED.key()) {
                return ResponseEntity
                        .ok(Resp.of(Commons.ERROR_CODE, Commons.CONFIG_PUBLISHED_MSG));
            } else if (config.getStatus() == ConfigStatus.FAILURE.key()) {
                return ResponseEntity
                        .ok(Resp.of(Commons.ERROR_CODE, Commons.DATA_NOTFOUND_MSG));
            }

            // 修改当前状态
            config.setStatus(ConfigStatus.PUBLISHED.key());
            repository.save(config);

            ConfigInfoDto cid = new ConfigInfoDto();
            cid.setServiceName(config.getServiceName());
            cid.setFreqConfig(config.getFreqConfig());
            cid.setLoadbalanceConfig(config.getLoadbalanceConfig());
            cid.setTimeoutConfig(config.getTimeoutConfig());
            cid.setRouterConfig(config.getRouterConfig());
            cid.setRemark(config.getRemark());

            try {
                processPublish(host, cid);
                return ResponseEntity
                        .ok(Resp.of(Commons.SUCCESS_CODE, Commons.PUBLISH_SUCCESS_MSG));
            } catch (Exception e) {
                return ResponseEntity
                        .ok(Resp.of(Commons.ERROR_CODE, e.getMessage()));
            }
        } else {
            return ResponseEntity
                    .ok(Resp.of(Commons.ERROR_CODE, Commons.DATA_NOTFOUND_MSG));
        }
    }


    /**
     * 通过dto信息保存新增配置信息
     *
     * @param configInfoDto
     */
    private void saveNewConfig(ConfigInfoDto configInfoDto, int status) {
        ConfigInfo configInfo = new ConfigInfo();
        configInfo.setServiceName(configInfoDto.getServiceName());
        configInfo.setCreatedAt(DateUtil.now());
        configInfo.setUpdatedAt(DateUtil.now());
        configInfo.setCreatedBy(0);
        configInfo.setUpdatedBy(0);
        configInfo.setStatus(status);
        configInfo.setRemark(configInfoDto.getRemark());
        configInfo.setFreqConfig(configInfoDto.getFreqConfig());
        configInfo.setRouterConfig(configInfoDto.getRouterConfig());
        configInfo.setTimeoutConfig(configInfoDto.getTimeoutConfig());
        configInfo.setLoadbalanceConfig(configInfoDto.getLoadbalanceConfig());
        repository.saveAndFlush(configInfo);
    }

    /**
     * 校验输入格式
     *
     * @param cofig
     * @throws Exception
     */
    private void checkGrammar(ConfigInfoDto cofig) throws Exception {

        if (!cofig.getServiceName().contains(".")) {
            throw new Exception("服务名格式有误，请检查！");
        }

        // 语法检查
        boolean freqIsOk = NullUtil.isEmpty(cofig.getFreqConfig()) ?
                true : CheckConfigUtil.doCheckFreq(cofig.getFreqConfig());
        if (!freqIsOk) {
            throw new Exception("限流配置格式错误，请检查！");
        }

        boolean routerIsOk = NullUtil.isEmpty(cofig.getRouterConfig()) ?
                true : CheckConfigUtil.doCheckRouter(cofig.getRouterConfig());
        if (!routerIsOk) {
            throw new Exception("路由配置格式错误，请检查！");
        }

        boolean timeoutIsOk = NullUtil.isEmpty(cofig.getTimeoutConfig()) ?
                true : CheckConfigUtil.doCheckConfig(cofig.getTimeoutConfig());
        if (!timeoutIsOk) {
            throw new Exception("超时配置格式错误，请检查！");
        }

        boolean loadbalanceIsOk = NullUtil.isEmpty(cofig.getLoadbalanceConfig()) ?
                true : CheckConfigUtil.doCheckConfig(cofig.getLoadbalanceConfig());
        if (!loadbalanceIsOk) {
            throw new Exception("负载均衡配置格式错误，请检查！");
        }
    }

    /**
     * 执行同步
     *
     * @param service
     */
    private RealConfig proccessSysConfig(String host, String service) throws Exception {
        ZooKeeper zk = ZkUtil.createZkByHost(host);
        RealConfig realConfig = new RealConfig();
        String timeoutBalanceConfig = ZkUtil.getNodeData(zk, Constants.CONFIG_SERVICE_PATH + "/" + service);
        String freqConfig = ZkUtil.getNodeData(zk, Constants.CONFIG_FREQ_PATH + "/" + service);
        String routerConfig = ZkUtil.getNodeData(zk, Constants.CONFIG_ROUTER_PATH + "/" + service);
        realConfig.setTimeoutBalanceConfig(timeoutBalanceConfig);
        realConfig.setFreqConfig(freqConfig);
        realConfig.setRouterConfig(routerConfig);
        ZkUtil.closeZk(zk);
        return realConfig;
    }

    /**
     * 执行发布（服务）
     *
     * @param cid
     * @throws Exception
     */
    private void processPublish(String host, ConfigInfoDto cid) throws Exception {
        ZooKeeper zk = ZkUtil.createZkByHost(host);
        ConfigPublishHistory history = new ConfigPublishHistory();
        history.setVersion(VersionUtil.version());
        history.setRemark("publish to [" + host + "]");
        history.setServiceName(cid.getServiceName());
        history.setTimeoutConfig(cid.getTimeoutConfig());
        history.setLoadbalanceConfig(cid.getLoadbalanceConfig());
        history.setRouterConfig(cid.getRouterConfig());
        history.setFreqConfig(cid.getFreqConfig());
        history.setPublishedAt(DateUtil.now());
        history.setPublishedBy(0);
        publishRepository.save(history);
        String service = cid.getServiceName();
        // 超时，负载均衡 针对服务的配置，全局的配置在 /soa/config/services 节点
        ZkUtil.createData(zk, Constants.CONFIG_SERVICE_PATH + "/" + service, cid.getTimeoutConfig() + cid.getLoadbalanceConfig());
        // 路由
        ZkUtil.createData(zk, Constants.CONFIG_ROUTER_PATH + "/" + service, cid.getRouterConfig());
        // 限流
        ZkUtil.createData(zk, Constants.CONFIG_FREQ_PATH + "/" + service, cid.getFreqConfig());
        ZkUtil.closeZk(zk);
    }
}
