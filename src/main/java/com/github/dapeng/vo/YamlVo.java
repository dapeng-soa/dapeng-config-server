package com.github.dapeng.vo;

/**
 * @author with struy.
 * Create by 2018/7/25 17:31
 * email :yq1724555319@gmail.com
 */

public class YamlVo {
    private Long lastDeployTime;
    private YamlService yamlService;

    public Long getLastDeployTime() {
        return lastDeployTime;
    }

    public void setLastDeployTime(Long lastDeployTime) {
        this.lastDeployTime = lastDeployTime;
    }

    public YamlService getYamlService() {
        return yamlService;
    }

    public void setYamlService(YamlService yamlService) {
        this.yamlService = yamlService;
    }
}
