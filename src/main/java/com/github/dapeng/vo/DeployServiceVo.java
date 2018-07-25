package com.github.dapeng.vo;


import java.util.List;

/**
 * @author with struy.
 * Create by 2018/7/25 20:41
 * email :yq1724555319@gmail.com
 */

public class DeployServiceVo {
    private long serviceId;
    private String serviceName;
    private List<DeploySubHostVo> deploySubHostVos;

    public long getServiceId() {
        return serviceId;
    }

    public void setServiceId(long serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public List<DeploySubHostVo> getDeploySubHostVos() {
        return deploySubHostVos;
    }

    public void setDeploySubHostVos(List<DeploySubHostVo> deploySubHostVos) {
        this.deploySubHostVos = deploySubHostVos;
    }
}
