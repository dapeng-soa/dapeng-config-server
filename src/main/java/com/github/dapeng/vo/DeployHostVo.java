package com.github.dapeng.vo;


import java.util.List;

/**
 * @author with struy.
 * Create by 2018/7/25 20:41
 * email :yq1724555319@gmail.com
 * 主机视图
 */

public class DeployHostVo {
    private String hostName;
    private String hostIp;
    private long hostId;
    private List<DeploySubServiceVo> deploySubServiceVos;

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

    public List<DeploySubServiceVo> getDeploySubServiceVos() {
        return deploySubServiceVos;
    }

    public void setDeploySubServiceVos(List<DeploySubServiceVo> deploySubServiceVos) {
        this.deploySubServiceVos = deploySubServiceVos;
    }

    public String getHostIp() {
        return hostIp;
    }

    public void setHostIp(String hostIp) {
        this.hostIp = hostIp;
    }
}
