package com.github.dapeng.dto;

/**
 * @author with struy.
 * Create by 2018/6/12 14:59
 * email :yq1724555319@gmail.com
 */

public class RealConfig {
    /**
     * 超时/负载均衡配置
     */
    private String timeoutBalanceConfig = "";
    /**
     * 路由配置
     */
    private String routerConfig = "";
    /**
     * 限流配置
     */
    private String freqConfig = "";

    public String getTimeoutBalanceConfig() {
        return timeoutBalanceConfig;
    }

    public void setTimeoutBalanceConfig(String timeoutBalanceConfig) {
        this.timeoutBalanceConfig = timeoutBalanceConfig;
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

    @Override
    public String toString() {
        return "RealConfig{" +
                "timeoutBalanceConfig='" + timeoutBalanceConfig + '\'' +
                ", routerConfig='" + routerConfig + '\'' +
                ", freqConfig='" + freqConfig + '\'' +
                '}';
    }
}
