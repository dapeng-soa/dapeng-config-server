package com.github.dapeng.vo.compose;

import java.util.Map;

public class DockerYaml {
    private String version;
    private Map<String, DockerService> services;
    private Map<String, Network> networks;

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

    public Map<String, Network> getNetworks() {
        return networks;
    }

    public void setNetworks(Map<String, Network> networks) {
        this.networks = networks;
    }
}
