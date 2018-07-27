package com.github.dapeng.util;

import com.github.dapeng.core.helper.IPUtils;
import com.github.dapeng.entity.deploy.TDeployUnit;
import com.github.dapeng.vo.YamlService;
import com.github.dapeng.entity.deploy.THost;
import com.github.dapeng.entity.deploy.TService;
import com.github.dapeng.entity.deploy.TSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.github.dapeng.util.NullUtil.isEmpty;

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
    public static YamlService processServiceOfUnit(TSet set, THost host, TService service, TDeployUnit unit) {

        YamlService yService = new YamlService();

        //==================service
        yService.setName(service.getName());

        //==================image /tag default=>latest
        String imageTag = isEmpty(unit.getImageTag()) ? "latest" : unit.getImageTag();
        yService.setImage(service.getImage() + ":" + imageTag);

        //==================environment

        yService.setEnv(processEnv(set, host, service, unit));

        //==================volumes
        yService.setVolumes(processVolume(unit.getVolumes(), service.getVolumes()));

        //==================ports
        yService.setPorts(processPorts(service, unit));

        //==================dockerExtras
        yService.setDockerExtras(processVolume(unit.getDockerExtras(), service.getDockerExtras()));

        //==================composeLabels
        yService.setComposeLabels(service.getComposeLabels());

        return yService;
    }

    /**
     * 按照优先级排除相同的环境变量
     * // 优先级为t_set<t_host<t_service<t_unit
     *
     * @param set
     * @param host
     * @param service
     * @return
     */
    public static String processEnv(TSet set, THost host, TService service, TDeployUnit unit) {
        Map<String, String> setEnvs = UnitUtil.ofEnv(set.getEnv());
        Map<String, String> hostEnvs = UnitUtil.ofEnv(host.getEnv());
        Map<String, String> serviceEnvs = UnitUtil.ofEnv(service.getEnv());
        Map<String, String> unitEnvs = UnitUtil.ofEnv(unit.getEnv());

        Map<String, String> realEnvs = mergeEnvs(mergeEnvs(mergeEnvs(unitEnvs, serviceEnvs), hostEnvs), setEnvs);

        StringBuilder sb = new StringBuilder();
        if (realEnvs.size() > 0) {
            realEnvs.forEach((k, v) -> {
                sb.append(k).append(":").append(v).append("\n");
            });
            sb.deleteCharAt(sb.lastIndexOf("\n"));
        }
        return sb.toString();
    }

    /**
     * @param priority 高优先级
     * @param attach   低优先级
     * @return 去除低优先级的返回并集
     * env的key不重复即可
     */
    public static Map<String, String> mergeEnvs(Map<String, String> priority, Map<String, String> attach) {
        Map<String, String> rmEnvs = new HashMap<>(16);
        Map<String, String> realEnvs = new HashMap<>(16);
        priority.forEach((k, v) -> attach.forEach((k1, v1) -> {
            if (k1.equals(k)) {
                rmEnvs.put(k1, v1);
            }
        }));
        LOGGER.info("mergeEnvs :[{}]", rmEnvs);
        rmEnvs.forEach(attach::remove);
        rmEnvs.clear();
        realEnvs.putAll(priority);
        realEnvs.putAll(attach);
        return realEnvs;
    }

    /**
     * 挂载卷只要不是完全一样的宿主机:容器挂载即可
     * 如:
     * /data/logs:/data/logs
     * /data/logs:/data/logs
     * 只会保留一个
     *
     * @param priority
     * @param attach
     * @return
     */
    public static String processVolume(String priority, String attach) {
        List<String> prioritys = UnitUtil.ofList(priority);
        List<String> attachs = UnitUtil.ofList(attach);
        StringBuilder sb = new StringBuilder();
        // /data/logs:/data/logs
        List<String> list = mergeList(prioritys, attachs);
        if (list.size() > 0) {
            list.forEach(s -> {
                sb.append(s).append("\n");
            });
            sb.deleteCharAt(sb.lastIndexOf("\n"));
        }
        return sb.toString();
    }

    /**
     * 端口只要绑定的宿主机端口不一致都可以
     * 如：
     * 9999:9999
     * 9999:9898
     * 是不通过的
     *
     * @param service
     * @param unit
     * @return
     */
    public static String processPorts(TService service, TDeployUnit unit) {
        Map<String, String> servicePorts = UnitUtil.ofEnv(service.getPorts());
        Map<String, String> unitPorts = UnitUtil.ofEnv(unit.getPorts());
        Map<String, String> rmPosts = new HashMap<>(16);
        Map<String, String> realPosts = new HashMap<>(16);
        unitPorts.forEach((k, v) -> servicePorts.forEach((k1, v1) -> {
            if (k1.equals(k)) {
                rmPosts.put(k1, v1);
            }
        }));
        LOGGER.info("mergePorts :[{}]", rmPosts);
        rmPosts.forEach(servicePorts::remove);
        rmPosts.clear();
        realPosts.putAll(unitPorts);
        realPosts.putAll(servicePorts);

        StringBuilder sb = new StringBuilder();

        // 9999:9999
        if (realPosts.size() > 0) {
            realPosts.forEach((k, v) -> {
                sb.append(k).append(":").append(v).append("\n");
            });
            sb.deleteCharAt(sb.lastIndexOf("\n"));
        }
        return sb.toString();
    }


    public static List<String> mergeList(List<String> priority, List<String> attach) {
        List<String> rmList = new ArrayList<>();
        List<String> realList = new ArrayList<>();
        priority.forEach(x -> attach.forEach(y -> {
            if (!isEmpty(x) && !isEmpty(y) || y.equals(x)) {
                rmList.add(y);
            }
        }));
        attach.removeAll(rmList);
        realList.addAll(priority);
        realList.addAll(attach);
        return realList;
    }

    public static YamlService processService(TSet set, THost host, TService service) {
        YamlService yService = new YamlService();

        //==================service
        yService.setName(service.getName());

        //==================image /tag default=>latest
        yService.setImage(service.getImage());

        //==================environment

        yService.setEnv(processEnv(set, host, service, new TDeployUnit()));

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
     * 处理extraHosts
     *
     * @param hosts
     * @return
     */
    public static String processExtraHosts(List<THost> hosts) {
        StringBuilder sb = new StringBuilder();
        if (!isEmpty(hosts)){
            hosts.forEach(h -> {
                sb.append(h.getName())
                        .append(":")
                        .append(IPUtils.transferIp(h.getIp()))
                        .append("\n");
            });
            sb.deleteCharAt(sb.lastIndexOf("\n"));
        }
        return sb.toString();
    }
}
