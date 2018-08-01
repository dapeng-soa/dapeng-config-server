package com.github.test;

import java.util.List;
import java.util.Map;

public class DockerYaml {
    private String version;
    private Map<String, Service> services;

    public void setVersion(String version) {
        this.version = version;
    }

    public String getVersion() {
        return version;
    }

    public Map<String, Service> getServices() {
        return services;
    }

    public void setServices(Map<String, Service> services) {
        this.services = services;
    }
}
