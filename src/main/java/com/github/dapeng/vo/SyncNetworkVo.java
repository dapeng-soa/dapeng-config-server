package com.github.dapeng.vo;

import java.util.List;

/**
 * @author with struy.
 * Create by 2018/11/19 17:32
 * email :yq1724555319@gmail.com
 */

public class SyncNetworkVo {
    private List<String> hosts;
    private String networkName;
    private String driver = "bridge";
    private String subnet;
    private String opt;

    public List<String> getHosts() {
        return hosts;
    }

    public void setHosts(List<String> hosts) {
        this.hosts = hosts;
    }

    public String getNetworkName() {
        return networkName;
    }

    public void setNetworkName(String networkName) {
        this.networkName = networkName;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getSubnet() {
        return subnet;
    }

    public void setSubnet(String subnet) {
        this.subnet = subnet;
    }

    public String getOpt() {
        return opt;
    }

    public void setOpt(String opt) {
        this.opt = opt;
    }
}
