package com.github.dapeng.monitor.entity;

import javax.persistence.Entity;

/**
 * @Author: zhup
 * @Date: 2018/8/1 15:07
 */
@Entity
public class ServiceMonitorVo {

    private long serviceId;

    private String ipList;

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    private String serviceName;
}
