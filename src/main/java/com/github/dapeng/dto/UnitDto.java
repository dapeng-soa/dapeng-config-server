package com.github.dapeng.dto;

/**
 * @author with struy.
 * Create by 2018/6/22 16:09
 * email :yq1724555319@gmail.com
 * 部署单元
 */
public class UnitDto {

    private long setId;
    private long hostId;
    private long serviceId;
    private String gitTag;
    private String imageTag;
    private String env = "";
    private String ports = "";
    private String volumes = "";
    private String dockerExtras = "";
    private String containerName = "";


    public long getSetId() {
        return setId;
    }

    public void setSetId(long setId) {
        this.setId = setId;
    }


    public long getHostId() {
        return hostId;
    }

    public void setHostId(long hostId) {
        this.hostId = hostId;
    }


    public long getServiceId() {
        return serviceId;
    }

    public void setServiceId(long serviceId) {
        this.serviceId = serviceId;
    }


    public String getGitTag() {
        return gitTag;
    }

    public void setGitTag(String gitTag) {
        this.gitTag = gitTag;
    }


    public String getImageTag() {
        return imageTag;
    }

    public void setImageTag(String imageTag) {
        this.imageTag = imageTag;
    }


    public String getEnv() {
        return env;
    }

    public void setEnv(String env) {
        this.env = env;
    }


    public String getPorts() {
        return ports;
    }

    public void setPorts(String ports) {
        this.ports = ports;
    }


    public String getVolumes() {
        return volumes;
    }

    public void setVolumes(String volumes) {
        this.volumes = volumes;
    }


    public String getDockerExtras() {
        return dockerExtras;
    }

    public void setDockerExtras(String dockerExtras) {
        this.dockerExtras = dockerExtras;
    }

    public String getContainerName() {
        return containerName;
    }

    public void setContainerName(String containerName) {
        this.containerName = containerName;
    }

    @Override
    public String toString() {
        return "UnitDto{" +
                "setId=" + setId +
                ", hostId=" + hostId +
                ", serviceId=" + serviceId +
                ", gitTag='" + gitTag + '\'' +
                ", imageTag='" + imageTag + '\'' +
                ", env='" + env + '\'' +
                ", ports='" + ports + '\'' +
                ", volumes='" + volumes + '\'' +
                ", dockerExtras='" + dockerExtras + '\'' +
                '}';
    }
}
