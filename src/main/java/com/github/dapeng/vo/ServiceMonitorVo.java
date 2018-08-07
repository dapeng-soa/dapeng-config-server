package com.github.dapeng.vo;

import javax.persistence.Entity;

/**
 * @Author: zhup
 * @Date: 2018/8/1 15:07
 */

public class ServiceMonitorVo {

    private long serviceId;

    private String ipList;

    public String getServiceName() {
        return serviceName;
    }

    public long getServiceId() {
        return serviceId;
    }

    public void setServiceId(long serviceId) {
        this.serviceId = serviceId;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    private String serviceName;

    public String getIpList() {
        return ipList;
    }

    public void setIpList(String ipList) {
        this.ipList = ipList;
    }

    public String getEnv() {
        return env;

    }

    public void setEnv(String env) {
        this.env = env;
    }

    private String env;

}
