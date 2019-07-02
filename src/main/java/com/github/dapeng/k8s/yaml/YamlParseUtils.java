package com.github.dapeng.k8s.yaml;

import com.github.dapeng.k8s.yaml.entity.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * @author huyj
 * @Created 2019-06-04 21:00
 */
public class YamlParseUtils {
    private static final Logger logger = LoggerFactory.getLogger(YamlParseUtils.class);
    private static String K8S_ATTR_PREFIX = "k8s-";

    public static List<ServiceConfig> parseYaml(Map<String, Object> yamlData) {
        final List<ServiceConfig> serviceConfigList = new ArrayList<>();

        Map<String, Map<String, Object>> serviceMap = (Map<String, Map<String, Object>>) yamlData.get("services");

        serviceMap.forEach((key, value) -> {
            ServiceConfig serviceConfig = new ServiceConfig();
            Map<String, Object> environmentMap = ((Map<String, Object>) value.get("environment"));
            serviceConfig.setNameSpaceEntities(parseNamespace((List<String>) value.get("extra_hosts")));
            serviceConfig.setServiceName((String) value.get("container_name"));

            String hostIp = environmentMap.get("host_ip") != null ? (String) environmentMap.get("host_ip") : (String) environmentMap.get("soa_container_ip");
            serviceConfig.setIp(hostIp == null ? "127.0.0.1" : hostIp);
            serviceConfig.setPortEntities(parsePort((List<String>) value.get("ports")));
            serviceConfig.setImage((String) value.get("image"));
            serviceConfig.setVolumnEntities(parseVolumns((List<String>) value.get("volumes")));

            //筛选 k8s-前缀变量
            /*Map<String, String> k8sAttrMap = new HashMap<>();
            if (environmentMap != null && !environmentMap.isEmpty()) {
                environmentMap.forEach((envKey, envValue) -> {
                    if (envKey.contains(K8S_ATTR_PREFIX)) {
                        k8sAttrMap.put(envKey.replaceAll(K8S_ATTR_PREFIX, "").toUpperCase(), (String) envValue);
                    }
                });
            }
            serviceConfig.setEnvironmentEntities(parseEnvironments(environmentMap));
            serviceConfig.setK8sAttrMap(k8sAttrMap);*/

            serviceConfig = parseEnvironments(serviceConfig, environmentMap);

            // System.out.println(serviceConfig);
            serviceConfigList.add(serviceConfig);
        });
        return serviceConfigList;
    }

    public static List<NameSpaceEntity> parseNamespace(List<String> hostList) {
        List<NameSpaceEntity> nameSpaceEntities = null;
        if (hostList != null && !hostList.isEmpty()) {
            nameSpaceEntities = new ArrayList<NameSpaceEntity>();
            for (String hostItem : hostList) {
                String[] hostArr = hostItem.split(":");
                nameSpaceEntities.add(new NameSpaceEntity(hostArr[0], hostArr[1]));
            }
        }
        return nameSpaceEntities;
    }

    public static List<PortEntity> parsePort(List<String> portsList) {
        List<PortEntity> portEntityList = null;
        if (portsList != null && !portsList.isEmpty()) {
            portEntityList = new ArrayList<PortEntity>();
            for (String portItem : portsList) {
                String[] portArr = portItem.split(":");
                portEntityList.add(new PortEntity(portArr[0], portArr[1]));
            }
        }
        return portEntityList;
    }

    public static List<VolumnEntity> parseVolumns(List<String> volumesList) {
        List<VolumnEntity> volumnEntityList = null;
        if (volumesList != null && !volumesList.isEmpty()) {
            volumnEntityList = new ArrayList<VolumnEntity>();
            for (String volumnItem : volumesList) {
                String[] volumnArr = volumnItem.split(":");
                volumnEntityList.add(new VolumnEntity(volumnArr[0], volumnArr[1]));
            }
        }
        return volumnEntityList;
    }

    public static ServiceConfig parseEnvironments(ServiceConfig serviceConfig, Map<String, Object> environmentMap) {
        List<EnvironmentEntity> environmentEntityList = null;
        Map<String, String> k8sAttrMap = new HashMap<>();
        if (environmentMap != null && !environmentMap.isEmpty()) {
            environmentEntityList = new ArrayList<EnvironmentEntity>();
            for (String key : environmentMap.keySet()) {
                if (key.contains(K8S_ATTR_PREFIX)) {
                    k8sAttrMap.put(key.replaceAll(K8S_ATTR_PREFIX, "").toUpperCase(), (String) environmentMap.get(key));
                } else {
                    environmentEntityList.add(new EnvironmentEntity(key, (String) environmentMap.get(key)));
                }
            }
        }

        serviceConfig.setEnvironmentEntities(environmentEntityList);
        serviceConfig.setK8sAttrMap(k8sAttrMap);
        return serviceConfig;
    }


    /*----------------------------------  生成 Yaml 配置---------------------------------------*/
    public static String buildK8sYamlByFile(String ymlPath) {
        Yaml yaml = new Yaml();
        Map<String, Object> ymlMap = null;
        try {
            ymlMap = (Map<String, Object>) yaml.load(new FileInputStream(new File(ymlPath)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //System.out.println(ret);
        List<ServiceConfig> serviceConfigList = YamlParseUtils.parseYaml(ymlMap);
        return buildK8sYaml(serviceConfigList);
    }

    public static String buildK8sYamlByContext(String ymlContext) {
        Yaml yaml = new Yaml();
        Map<String, Object> ymlMap = null;
        ymlMap = (Map<String, Object>) yaml.load(ymlContext);
        List<ServiceConfig> serviceConfigList = YamlParseUtils.parseYaml(ymlMap);
        return buildK8sYaml(serviceConfigList);
    }


    public static String buildK8sYaml(List<ServiceConfig> serviceConfigList) {
//        String template_info = FileUtils.readFromeFile("E:\\workspace\\test\\src\\main\\java\\com\\today\\yaml\\k8sYaml\\template.yaml");
        //String template_info = FileUtils.readFromeFile("src\\main\\resources\\k8s-template.yaml");
        String template_info = FileUtils.getResourceFileContext("k8s-template.yaml");

        ServiceConfig serviceConfig = serviceConfigList.get(0);
//        HashMap<String, String> volumnsMap = YamlParseUtils.buildK8sVolums(serviceConfig);
        K8sYamlVolumn k8sYamlVolumn = YamlParseUtils.buildK8sCommonVolums(serviceConfig);
//        System.out.println(volumnsMap.get("volumeMounts"));
//        System.out.println(volumnsMap.get("volumes"));

        template_info = template_info.replaceAll("@NAMESPACE@", serviceConfig.getNameSpaceEntities().get(0).getName());

        template_info = template_info.replaceAll("@VOLUME_MOUNT@", k8sYamlVolumn.getVolumeMounts());
        template_info = template_info.replaceAll("@VOLUME@", k8sYamlVolumn.getVolumes());
        template_info = template_info.replaceAll("@PVPC_VOLUME@", k8sYamlVolumn.getVolumePvpcs());

        template_info = template_info.replaceAll("@SERVICE_NAME@", serviceConfig.getServiceName().toLowerCase());

        template_info = template_info.replaceAll("@IMAGE@", serviceConfig.getImage());
        template_info = template_info.replaceAll("@PORT@", serviceConfig.getPortEntities().get(0).getContainerPort());
        template_info = template_info.replaceAll("@IP@", serviceConfig.getIp());
        template_info = template_info.replaceAll("@ENVIRONMENTS@", YamlParseUtils.buildK8sEnvironments(serviceConfig));

        //替换K8S_Attr Map
        if (serviceConfig.getK8sAttrMap() != null && !serviceConfig.getK8sAttrMap().isEmpty()) {
            for (String key : serviceConfig.getK8sAttrMap().keySet()) {
                template_info = template_info.replaceAll("@" + key + "@", serviceConfig.getK8sAttrMap().get(key));
            }
        }

        //替换副本 (没有设置就 默认为1)
        template_info = template_info.replaceAll("@REPLICAS@", "1");

        /*String buildYamlPath = "E:\\workspace\\test\\src\\main\\java\\com\\today\\yaml\\k8sYaml\\" + serviceConfig.getServiceName() + ".yaml";
        FileUtils.saveFile(buildYamlPath, template_info);
        System.out.println(buildYamlPath);*/
        return template_info;
    }

    public static HashMap<String, String> buildK8sVolums(ServiceConfig serviceConfig) {
        String volumeMountTemplate = "         - name: @LABEL@\n" +
                "           mountPath: @CONTAINER_PATH@";

        String volumeTemplate = "       - name: @LABEL@\n" +
                "         nfs:\n" +
                "          server: @IP@\n" +
                "          path: @SERVER_PATH@";

        StringBuffer volumeMountBuffer = new StringBuffer();
        StringBuffer volumeBuffer = new StringBuffer();


        HashMap<String, String> volumnMap = new HashMap<String, String>();
        if (serviceConfig.getVolumnEntities() != null && !serviceConfig.getVolumnEntities().isEmpty()) {
            for (VolumnEntity volumnEntity : serviceConfig.getVolumnEntities()) {
                String _label = volumnEntity.getContainerPath().substring(1).replaceAll("/", "-");
                String label = serviceConfig.getServiceName().toLowerCase() + "-" + _label;

                volumeMountBuffer.append(volumeMountTemplate.replaceAll("@LABEL@", label).replaceAll("@CONTAINER_PATH@", volumnEntity.getContainerPath())).append("\n");
                volumeBuffer.append(volumeTemplate.replaceAll("@LABEL@", label).replaceAll("@SERVER_PATH@", volumnEntity.getServerPath()).replaceAll("@IP@", serviceConfig.getIp())).append("\n");
//                System.out.println("位置：YamlParseUtils.buildK8sVolums ==> " + "[label = " + label + "]");
//                System.out.println(volumnEntity.getServerPath() + "=>" + volumnEntity.getContainerPath());
            }
        }
        volumnMap.put("volumeMounts", volumeMountBuffer.toString());
        volumnMap.put("volumes", volumeBuffer.toString());
        return volumnMap;
    }

    public static K8sYamlVolumn buildK8sCommonVolums(ServiceConfig serviceConfig) {
        String volumeMountTemplate = "         - name: @LABEL@\n" +
                "           mountPath: @CONTAINER_PATH@";

        String volumeTemplate = "      - name: @LABEL@\n" +
                "        persistentVolumeClaim:\n" +
                "          claimName: @LABEL@";

        String pvpcTemplate = "---\n" +
                "apiVersion: v1\n" +
                "kind: PersistentVolume\n" +
                "metadata:\n" +
                "  name: @LABEL@\n" +
                "spec:\n" +
                "  capacity:\n" +
                "    storage: 100Mi\n" +
                "  accessModes:\n" +
                "    - ReadWriteMany\n" +
                "  nfs:\n" +
                "    server: @NFS-SERVER@\n" +
                "    path: @SERVER_PATH@\n" +
                "\n" +
                "---\n" +
                "apiVersion: v1\n" +
                "kind: PersistentVolumeClaim\n" +
                "metadata:\n" +
                "  name: @LABEL@\n" +
                "  namespace: @NAMESPACE@\n" +
                "spec:\n" +
                "  accessModes:\n" +
                "  - ReadWriteMany\n" +
                "  resources:\n" +
                "    requests:\n" +
                "      storage: 50Mi\n" +
                "  selector:\n" +
                "    matchLabels:\n" +
                "      alicloud-pvname: @LABEL@\n" +
                "  volumeName: @LABEL@";

        StringBuffer volumeMountBuffer = new StringBuffer();
        StringBuffer volumeBuffer = new StringBuffer();
        StringBuffer pvpcBuffer = new StringBuffer();

        if (serviceConfig.getVolumnEntities() != null && !serviceConfig.getVolumnEntities().isEmpty()) {
            for (VolumnEntity volumnEntity : serviceConfig.getVolumnEntities()) {
                String _label = volumnEntity.getContainerPath().substring(1).replaceAll("/", "-");
                String label = serviceConfig.getServiceName().toLowerCase() + "-" + _label;

                String nameSpace = serviceConfig.getNameSpaceEntities().get(0).getName();
                volumeMountBuffer.append(volumeMountTemplate.replaceAll("@NAMESPACE@", nameSpace).replaceAll("@LABEL@", label).replaceAll("@CONTAINER_PATH@", volumnEntity.getContainerPath())).append("\n");
                volumeBuffer.append(volumeTemplate.replaceAll("@NAMESPACE@", nameSpace).replaceAll("@LABEL@", label).replaceAll("@SERVER_PATH@", volumnEntity.getServerPath()).replaceAll("@IP@", serviceConfig.getIp())).append("\n");
                pvpcBuffer.append(pvpcTemplate.replaceAll("@NAMESPACE@", nameSpace).replaceAll("@LABEL@", label).replaceAll("@SERVER_PATH@", volumnEntity.getServerPath()).replaceAll("@IP@", serviceConfig.getIp())).append("\n");
//                System.out.println("位置：YamlParseUtils.buildK8sVolums ==> " + "[label = " + label + "]");
//                System.out.println(volumnEntity.getServerPath() + "=>" + volumnEntity.getContainerPath());
            }
        }

        volumeMountTemplate = volumeMountBuffer.toString();
        volumeTemplate = volumeBuffer.toString();
        pvpcTemplate = pvpcBuffer.toString();
        //替换K8S_Attr Map
        if (serviceConfig.getK8sAttrMap() != null && !serviceConfig.getK8sAttrMap().isEmpty()) {
            for (String key : serviceConfig.getK8sAttrMap().keySet()) {
                volumeMountTemplate = volumeMountTemplate.replaceAll("@" + key + "@", serviceConfig.getK8sAttrMap().get(key));
                volumeTemplate = volumeTemplate.replaceAll("@" + key + "@", serviceConfig.getK8sAttrMap().get(key));
                pvpcTemplate = pvpcTemplate.replaceAll("@" + key + "@", serviceConfig.getK8sAttrMap().get(key));
            }
        }
        return new K8sYamlVolumn(volumeMountTemplate, volumeTemplate, pvpcTemplate);
    }

    public static String buildK8sEnvironments(ServiceConfig serviceConfig) {
        String environmentsTemplate = "        - name: @KEY@\n" +
                "          value: \"@VALUE@\"";

        StringBuffer environmentsBuffer = new StringBuffer();
        if (serviceConfig.getEnvironmentEntities() != null && !serviceConfig.getEnvironmentEntities().isEmpty()) {
            for (EnvironmentEntity environmentEntity : serviceConfig.getEnvironmentEntities()) {
                environmentsBuffer.append(environmentsTemplate.replaceAll("@KEY@", environmentEntity.getKey()).replaceAll("@VALUE@", environmentEntity.getValue())).append("\n");
            }
        }
        return environmentsBuffer.toString();
    }
}
