package com.github.dapeng.util;

import com.github.dapeng.vo.YamlService;
import com.github.dapeng.entity.deploy.THost;
import com.github.dapeng.entity.deploy.TService;
import com.github.dapeng.entity.deploy.TSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author with struy.
 * Create by 2018/7/25 14:42
 * email :yq1724555319@gmail.com
 */

public class Composeutil {
    private static Logger LOGGER = LoggerFactory.getLogger(Composeutil.class);

    /**
     * 获得yaml格式实体
     * environment
     * soa_monitor_enable: "true"
     * <p>
     * extra_hosts
     * db-master: 192.168.20.100
     * <p>
     * ports
     * - 9083:9083
     * <p>
     * volumes
     * - /data/logs/dapeng/biz-service:/dapeng-container/logs
     * <p>
     * labels
     * project.depends: mysql,zookeeper
     *
     * @return YamlService
     */
    public static YamlService processService(TSet set, THost host, TService service) {

        YamlService yService = new YamlService();

        //==================service
        yService.setName(service.getName());

        //==================image
        yService.setImage(service.getImage());

        //==================environment

        yService.setEnv(processEnv(set, host, service));

        //==================volumes
        yService.setVolumes(service.getVolumes());

        //==================ports
        yService.setPorts(service.getPorts());

        //==================dockerExtras
        yService.setDockerExtras(service.getDockerExtras());

        //==================composeLabels
        yService.setComposeLabels(service.getComposeLabels());

        return yService;
    }

    /**
     * 按照优先级排除相同的环境变量
     * // 优先级为t_set<t_host<t_service
     *
     * @param set
     * @param host
     * @param service
     * @return
     */
    public static String processEnv(TSet set, THost host, TService service) {
        Map<String, String> setEnvs = UnitUtil.ofEnv(set.getEnv());
        Map<String, String> hostEnvs = UnitUtil.ofEnv(host.getEnv());
        Map<String, String> serviceEnvs = UnitUtil.ofEnv(service.getEnv());

        Map<String, String> rmEnvs = new HashMap<>(16);
        serviceEnvs.forEach((k, v) -> hostEnvs.forEach((k1, v1) -> {
            if (k1.equals(k)) {
                rmEnvs.put(k1, v1);
            }
        }));
        LOGGER.info("去除host中与服务[{}]重复的环境变量:[{}]", service.getName(), rmEnvs);
        rmEnvs.forEach(hostEnvs::remove);
        rmEnvs.clear();
        Map<String, String> realEnvs = new HashMap<>(serviceEnvs);
        realEnvs.putAll(hostEnvs);
        realEnvs.forEach((k, v) -> setEnvs.forEach((k1, v1) -> {
            if (k1.equals(k)) {
                rmEnvs.put(k1, v1);
            }
        }));
        LOGGER.info("去除set中与节点[{}]服务[{}]重复的环境变量:[{}]",host.getName(), service.getName(), rmEnvs);
        rmEnvs.forEach(setEnvs::remove);
        rmEnvs.clear();
        realEnvs.putAll(setEnvs);
        StringBuilder sb = new StringBuilder();
        // k: v
        realEnvs.forEach((k, v) -> {
            sb.append(k).append(": ").append(v).append("\n");
        });
        return sb.toString();
    }
}
