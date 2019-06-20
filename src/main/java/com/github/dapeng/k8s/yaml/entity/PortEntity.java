package com.github.dapeng.k8s.yaml.entity;

/**
 * @author huyj
 * @Created 2019-06-04 21:38
 */
public class PortEntity {
    private String containerPort;
    private String serverPort;

    public PortEntity() {
    }

    public PortEntity(String serverPort, String containerPort) {
        this.containerPort = containerPort;
        this.serverPort = serverPort;
    }

    public String getContainerPort() {
        return containerPort;
    }

    public void setContainerPort(String containerPort) {
        this.containerPort = containerPort;
    }

    public String getServerPort() {
        return serverPort;
    }

    public void setServerPort(String serverPort) {
        this.serverPort = serverPort;
    }

    @Override
    public String toString() {
        return "PortEntity{" +
                "containerPort='" + containerPort + '\'' +
                ", serverPort='" + serverPort + '\'' +
                '}';
    }
}
