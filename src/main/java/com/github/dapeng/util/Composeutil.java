package com.github.dapeng.util;

import com.github.dapeng.core.helper.IPUtils;
import com.github.dapeng.entity.deploy.TDeployUnit;
import com.github.dapeng.entity.deploy.THost;
import com.github.dapeng.entity.deploy.TService;
import com.github.dapeng.entity.deploy.TSet;
import com.github.dapeng.vo.DockerYaml;
import com.github.dapeng.vo.DockerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Field;
import java.util.*;

import static com.github.dapeng.util.NullUtil.isEmpty;

/**
 * @author with struy.
 * Create by 2018/7/25 14:42
 * email :yq1724555319@gmail.com
 */

public class Composeutil {
    private static Logger LOGGER = LoggerFactory.getLogger(Composeutil.class);
    private static final String REGEX = "\n|\r|\r\n";

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
    public static DockerService processServiceOfUnit(TSet set, THost host, TService service, TDeployUnit unit) {

        DockerService dockerService1 = new DockerService();

        //==================service
        dockerService1.setContainer_name(service.getName());

        //==================image /tag default=>latest
        String imageTag = isEmpty(unit.getImageTag()) ? "latest" : unit.getImageTag();
        dockerService1.setImage(service.getImage() + ":" + imageTag);

        //==================environment

        dockerService1.setEnvironment(processEnv(set, host, service, unit));

        //==================volumes
        dockerService1.setVolumes(processVolume(unit.getVolumes(), service.getVolumes()));

        //==================ports
        dockerService1.setPorts(processPorts(service, unit));

        processDockerExtras(dockerService1, process2String(processVolume(unit.getDockerExtras(), service.getDockerExtras())));

        //==================composeLabels
        dockerService1.setLabels(ofList(service.getComposeLabels()));

        return dockerService1;
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
    public static Map<String, String> processEnv(TSet set, THost host, TService service, TDeployUnit unit) {
        Map<String, String> setEnvs = ofEnv(set.getEnv());
        Map<String, String> hostEnvs = ofEnv(host.getEnv());
        Map<String, String> serviceEnvs = ofEnv(service.getEnv());
        Map<String, String> unitEnvs = ofEnv(unit.getEnv());

        Map<String, String> realEnvs = mergeEnvs(mergeEnvs(mergeEnvs(unitEnvs, serviceEnvs), hostEnvs), setEnvs);

        StringBuilder sb = new StringBuilder();
        if (realEnvs.size() > 0) {
            realEnvs.forEach((k, v) -> {
                sb.append(k).append(":").append(v).append("\n");
            });
            sb.deleteCharAt(sb.lastIndexOf("\n"));
        }
        return realEnvs;
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
    public static List<String> processVolume(String priority, String attach) {
        List<String> prioritys = ofList(priority);
        List<String> attachs = ofList(attach);
        StringBuilder sb = new StringBuilder();
        // /data/logs:/data/logs
        List<String> list = mergeList(prioritys, attachs);
        return list;
    }


    public static String process2String(List list) {
        StringBuilder sb = new StringBuilder();
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
    public static List<String> processPorts(TService service, TDeployUnit unit) {
        Map<String, String> servicePorts = ofEnv(service.getPorts());
        Map<String, String> unitPorts = ofEnv(unit.getPorts());
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

        List<String> list = new ArrayList<>();

        // 9999:9999
        if (realPosts.size() > 0) {
            realPosts.forEach((k, v) -> {
                list.add(k + ":" + v);
            });
        }
        return list;
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

    public static DockerService processService(TSet set, THost host, TService service) {
        DockerService dockerService1 = new DockerService();

        //==================service
        dockerService1.setContainer_name(service.getName());

        //==================image /tag default=>latest
        dockerService1.setImage(service.getImage());

        //==================environment
        dockerService1.setEnvironment(processEnv(set, host, service, new TDeployUnit()));

        //==================volumes
        dockerService1.setVolumes(ofList(service.getVolumes()));

        //==================ports
        dockerService1.setPorts(ofList(service.getPorts()));

        //==================composeLabels
        dockerService1.setLabels(ofList(service.getComposeLabels()));

        processDockerExtras(dockerService1, service.getDockerExtras());

        return dockerService1;
    }

    /**
     * 处理extraHosts
     *
     * @param hosts
     * @return
     */
    public static List<String> processExtraHosts(List<THost> hosts) {
        List<String> list = new ArrayList<>();
        if (!isEmpty(hosts)) {
            hosts.forEach(h -> {
                list.add(h.getName() + ":" + IPUtils.transferIp(h.getIp()));
            });
        }
        return list;
    }

    /**
     * @return
     */
    public static DockerService processDockerExtras(DockerService dockerService, String dockerExtras) {
        Class<? extends DockerService> clazz = dockerService.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            String name = field.getName().toUpperCase();
            ofEnv(dockerExtras).forEach((k, v) -> {
                if (k.toUpperCase().equals(name)) {
                    String method = "set" + name.substring(0, 1).toUpperCase().concat(name.substring(1).toLowerCase());
                    try {
                        clazz.getMethod(method, String.class).invoke(dockerService, v);
                    } catch (Exception e) {
                        LOGGER.error("not found method [{}]", method);
                    }
                }
            });
        }
        return dockerService;
    }

    /**
     * 返回字符串的
     *
     * @param
     * @return
     */
    public static String processComposeContext(DockerService dockerService) {
        // 时间应当查询一个最后更新时间发送
        DockerYaml dockerYaml = new DockerYaml();
        dockerYaml.setVersion("2");
        Map<String, DockerService> serviceMap = new HashMap<>(1);
        serviceMap.put(dockerService.getContainer_name(), dockerService);
        dockerYaml.setServices(serviceMap);
        DumperOptions dumperOptions = new DumperOptions();
        dumperOptions.setPrettyFlow(true);
        String yaml = new Yaml(dumperOptions).dump(dockerYaml);
        BufferedReader br = new BufferedReader(new StringReader(yaml));
        StringBuilder sb = new StringBuilder();
        String str;
        try {
            while ((str = br.readLine()) != null) {
                if (!str.startsWith("!!") && !str.contains("null")) {
                    sb.append(str).append("\n");
                }
            }
            br.close();
        } catch (IOException e) {
            LOGGER.error("生成yaml失败", e);
        }
        return sb.toString();
    }


    /**
     * 将换行文本转换为Map
     *
     * @return
     */
    public static Map<String, String> ofEnv(String s) {
        Map<String, String> envMap = new HashMap<>(16);
        ofList(s).forEach(s1 -> {
            if (!isEmpty(s1) && s1.contains(":") || s1.contains("=")) {
                String[] s2 = s1.split("[:=]", 2);
                envMap.put(s2[0].trim(), s2[1].trim());
            }
        });
        return envMap;
    }


    /**
     * 将换行文本转换为集合
     *
     * @return
     */
    public static List<String> ofList(String s) {
        if (isEmpty(s)) {
            return new ArrayList<>();
        }
        return Arrays.asList(s.split(REGEX));
    }

    public static void main(String[] args) {
        String sss = "LANG: zh_CN.UTF-8\n" +
                "      TZ: CST-8\n" +
                "      fluent_bit_enable: \"true\"\n" +
                "      redis_host_ip: redis_host\n" +
                "      redis_host_port: '6379'\n" +
                "      serviceName: idGenService\n" +
                "      slow_service_check_enable: \"true\"\n" +
                "      soa_container_port: '9081'\n" +
                "      soa_core_pool_size: '100'\n" +
                "      soa_jmxrmi_enable: \"false\"\n" +
                "      soa_monitor_enable: \"true\"";
        ofEnv(sss).forEach((k, v) -> {
            System.out.println(k + " ->" + v);
        });
    }
}
