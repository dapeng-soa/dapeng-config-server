package com.github.dapeng.dto;

/**
 * @author with struy.
 * Create by 2018/6/1 10:28
 * email :yq1724555319@gmail.com
 */

public class ConfigInfoDto {
    /**
     * 服务全限定名
     */
    private String serviceName = "";
    /**
     * 超时配置
     */
    private String timeoutConfig = "";
    /**
     * 负载均衡配置
     */
    private String loadbalanceConfig = "";
    /**
     * 路由配置
     */
    private String routerConfig = "";
    /**
     * 限流配置
     */
    private String freqConfig = "";

    private String remark = "";

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getTimeoutConfig() {
        return timeoutConfig;
    }

    public void setTimeoutConfig(String timeoutConfig) {
        this.timeoutConfig = timeoutConfig;
    }

    public String getLoadbalanceConfig() {
        return loadbalanceConfig;
    }

    public void setLoadbalanceConfig(String loadbalanceConfig) {
        this.loadbalanceConfig = loadbalanceConfig;
    }

    public String getRouterConfig() {
        return routerConfig;
    }

    public void setRouterConfig(String routerConfig) {
        this.routerConfig = routerConfig;
    }

    public String getFreqConfig() {
        return freqConfig;
    }

    public void setFreqConfig(String freqConfig) {
        this.freqConfig = freqConfig;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
