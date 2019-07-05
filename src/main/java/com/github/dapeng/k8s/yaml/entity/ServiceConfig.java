package com.github.dapeng.k8s.yaml.entity;

import java.util.List;
import java.util.Map;

/**
 * @author huyj
 * @Created 2019-06-04 21:49
 */
public class ServiceConfig {
    private List<NameSpaceEntity> nameSpaceEntities;
    private String k8sTemplate;
    private String serviceName;
    private String image;
    private String ip;
    private List<PortEntity> portEntities;
    private List<EnvironmentEntity> environmentEntities;
    private List<VolumnEntity> volumnEntities;

    private Map<String, String> k8sAttrMap;

    public List<NameSpaceEntity> getNameSpaceEntities() {
        return nameSpaceEntities;
    }

    public void setNameSpaceEntities(List<NameSpaceEntity> nameSpaceEntities) {
        this.nameSpaceEntities = nameSpaceEntities;
    }

    public String getK8sTemplate() {
        return k8sTemplate;
    }

    public void setK8sTemplate(String k8sTemplate) {
        this.k8sTemplate = k8sTemplate;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public List<PortEntity> getPortEntities() {
        return portEntities;
    }

    public void setPortEntities(List<PortEntity> portEntities) {
        this.portEntities = portEntities;
    }

    public List<EnvironmentEntity> getEnvironmentEntities() {
        return environmentEntities;
    }

    public void setEnvironmentEntities(List<EnvironmentEntity> environmentEntities) {
        this.environmentEntities = environmentEntities;
    }

    public List<VolumnEntity> getVolumnEntities() {
        return volumnEntities;
    }

    public void setVolumnEntities(List<VolumnEntity> volumnEntities) {
        this.volumnEntities = volumnEntities;
    }

    public Map<String, String> getK8sAttrMap() {
        return k8sAttrMap;
    }

    public void setK8sAttrMap(Map<String, String> k8sAttrMap) {
        this.k8sAttrMap = k8sAttrMap;
    }

    @Override
    public String toString() {
        return "ServiceConfig{" +
                "nameSpaceEntities=" + nameSpaceEntities +
                ", serviceName='" + serviceName + '\'' +
                ", image='" + image + '\'' +
                ", ip='" + ip + '\'' +
                ", portEntities=" + portEntities +
                ", environmentEntities=" + environmentEntities +
                ", volumnEntities=" + volumnEntities +
                ", k8sAttrMap=" + k8sAttrMap +
                '}';
    }
}
