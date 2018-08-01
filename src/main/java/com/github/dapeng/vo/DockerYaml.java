package com.github.dapeng.vo;

import java.util.Map;

public class DockerYaml {
    private String version;
    private Map<String, DockerService> services;

    public void setVersion(String version) {
        this.version = version;
    }

    public String getVersion() {
        return version;
    }

    public Map<String, DockerService> getServices() {
        return services;
    }

    public void setServices(Map<String, DockerService> services) {
        this.services = services;
    }
}
