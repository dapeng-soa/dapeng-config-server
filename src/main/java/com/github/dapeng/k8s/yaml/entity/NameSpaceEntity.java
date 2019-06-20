package com.github.dapeng.k8s.yaml.entity;

/**
 * @author huyj
 * @Created 2019-06-05 11:26
 */
public class NameSpaceEntity {
    private String name;
    private String hostIp;

    public NameSpaceEntity() {
    }

    public NameSpaceEntity(String name, String hostIp) {
        this.name = name;
        this.hostIp = hostIp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHostIp() {
        return hostIp;
    }

    public void setHostIp(String hostIp) {
        this.hostIp = hostIp;
    }

    @Override
    public String toString() {
        return "NameSpaceEntity{" +
                "name='" + name + '\'' +
                ", hostIp='" + hostIp + '\'' +
                '}';
    }
}
