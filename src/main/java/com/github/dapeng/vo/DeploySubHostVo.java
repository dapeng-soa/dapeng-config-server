package com.github.dapeng.vo;

/**
 * @author with struy.
 * Create by 2018/7/25 20:41
 * email :yq1724555319@gmail.com
 * 服务示例内host
 */

public class DeploySubHostVo {
    private long setId;
    private long hostId;
    private long unitId;
    private String hostName;
    private String hostIp;
    private boolean needUpdate = true;
    private int serviceStatus = 0;
    private Long deployTime;
    private Long configUpdateBy;
    private String containerName;

    public long getSetId() {
        return setId;
    }

    public void setSetId(long setId) {
        this.setId = setId;
    }

    public long getHostId() {
        return hostId;
    }

    public void setHostId(long hostId) {
        this.hostId = hostId;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getHostIp() {
        return hostIp;
    }

    public void setHostIp(String hostIp) {
        this.hostIp = hostIp;
    }

    public boolean isNeedUpdate() {
        return needUpdate;
    }

    public void setNeedUpdate(boolean needUpdate) {
        this.needUpdate = needUpdate;
    }

    public int getServiceStatus() {
        return serviceStatus;
    }

    public void setServiceStatus(int serviceStatus) {
        this.serviceStatus = serviceStatus;
    }

    public Long getDeployTime() {
        return deployTime;
    }

    public void setDeployTime(Long deployTime) {
        this.deployTime = deployTime;
    }

    public Long getConfigUpdateBy() {
        return configUpdateBy;
    }

    public void setConfigUpdateBy(Long configUpdateBy) {
        this.configUpdateBy = configUpdateBy;
    }

    public long getUnitId() {
        return unitId;
    }

    public void setUnitId(long unitId) {
        this.unitId = unitId;
    }

    public String getContainerName() {
        return containerName;
    }

    public void setContainerName(String containerName) {
        this.containerName = containerName;
    }
}
