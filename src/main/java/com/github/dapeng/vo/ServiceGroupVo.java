package com.github.dapeng.vo;

import java.util.List;

/**
 * @Author: zhup
 * @Date: 2018/8/1 20:51
 */

public class ServiceGroupVo {

    private String service;

    private List<MonitorHosts> hosts;

    private List<Subservices> subservices;

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public List<MonitorHosts> getHosts() {
        return hosts;
    }

    public void setHosts(List<MonitorHosts> hosts) {
        this.hosts = hosts;
    }

    public List<Subservices> getSubservices() {
        return subservices;
    }

    public void setSubervices(List<Subservices> subservices) {
        this.subservices = subservices;
    }
}