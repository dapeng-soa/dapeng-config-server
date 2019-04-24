package com.github.dapeng.vo;

import java.util.List;

/**
 * @author huyj
 * @Created 2019-03-19 21:47
 */
public class ServiceInfo {
    private String serviceName;
    private String serviceFullName;
    private String version;
    private List<String> methodList;

    public ServiceInfo(String serviceName, String serviceFullName, String version, List<String> methodList) {
        this.serviceName = serviceName;
        this.serviceFullName = serviceFullName;
        this.methodList = methodList;
        this.version = version;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceFullName() {
        return serviceFullName;
    }

    public void setServiceFullName(String serviceFullName) {
        this.serviceFullName = serviceFullName;
    }

    public List<String> getMethodList() {
        return methodList;
    }

    public void setMethodList(List<String> methodList) {
        this.methodList = methodList;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}