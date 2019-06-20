package com.github.dapeng.k8s.yaml.entity;

/**
 * @author huyj
 * @Created 2019-06-04 21:17
 */
public class VolumnEntity {

    private String containerPath;
    private String serverPath;


    public VolumnEntity() {
    }

    public VolumnEntity(String serverPath, String containerPath) {
        this.containerPath = containerPath;
        this.serverPath = serverPath;
    }

    public String getContainerPath() {
        return containerPath;
    }

    public void setContainerPath(String containerPath) {
        this.containerPath = containerPath;
    }

    public String getServerPath() {
        return serverPath;
    }

    public void setServerPath(String serverPath) {
        this.serverPath = serverPath;
    }

    @Override
    public String toString() {
        return "VolumnEntity{" +
                "containerPath='" + containerPath + '\'' +
                ", serverPath='" + serverPath + '\'' +
                '}';
    }
}
