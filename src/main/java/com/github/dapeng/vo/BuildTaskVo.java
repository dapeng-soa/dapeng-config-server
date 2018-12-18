package com.github.dapeng.vo;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.github.dapeng.socket.entity.DependServiceVo;

import java.util.List;

public class BuildTaskVo {

    private long id;
    private long setId;
    private String setName;
    private long serviceId;
    private String serviceName;
    /**
     * 这是构建主机
     */
    private long hostId;
    private String hostName;
    private String deployHostName;
    private String branch;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    private java.sql.Timestamp updatedAt;
    private List<DependServiceVo> depends;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getServiceId() {
        return serviceId;
    }

    public void setServiceId(long serviceId) {
        this.serviceId = serviceId;
    }


    public long getHostId() {
        return hostId;
    }

    public void setHostId(long hostId) {
        this.hostId = hostId;
    }


    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public java.sql.Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(java.sql.Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }


    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public List<DependServiceVo> getDepends() {
        return depends;
    }

    public void setDepends(List<DependServiceVo> depends) {
        this.depends = depends;
    }

    public String getDeployHostName() {
        return deployHostName;
    }

    public void setDeployHostName(String deployHostName) {
        this.deployHostName = deployHostName;
    }

    public long getSetId() {
        return setId;
    }

    public void setSetId(long setId) {
        this.setId = setId;
    }

    public String getSetName() {
        return setName;
    }

    public void setSetName(String setName) {
        this.setName = setName;
    }
}
