package com.github.dapeng.dto;

/**
 * @author with struy.
 * Create by 2018/6/22 16:09
 * email :yq1724555319@gmail.com
 * 服务信息
 */
public class ServiceDto {

    private String name;
    private String image;
    private String labels = "";
    private String env = "";
    private String volumes = "";
    private String ports = "";
    private String composeLabels = "";
    private String dockerExtras = "";
    private String remark = "";


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


    public String getLabels() {
        return labels;
    }

    public void setLabels(String labels) {
        this.labels = labels;
    }


    public String getEnv() {
        return env;
    }

    public void setEnv(String env) {
        this.env = env;
    }


    public String getVolumes() {
        return volumes;
    }

    public void setVolumes(String volumes) {
        this.volumes = volumes;
    }


    public String getPorts() {
        return ports;
    }

    public void setPorts(String ports) {
        this.ports = ports;
    }


    public String getComposeLabels() {
        return composeLabels;
    }

    public void setComposeLabels(String composeLabels) {
        this.composeLabels = composeLabels;
    }


    public String getDockerExtras() {
        return dockerExtras;
    }

    public void setDockerExtras(String dockerExtras) {
        this.dockerExtras = dockerExtras;
    }


    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "ServiceDto{" +
                "name='" + name + '\'' +
                ", image='" + image + '\'' +
                ", labels='" + labels + '\'' +
                ", env='" + env + '\'' +
                ", volumes='" + volumes + '\'' +
                ", ports='" + ports + '\'' +
                ", composeLabels='" + composeLabels + '\'' +
                ", dockerExtras='" + dockerExtras + '\'' +
                ", remark='" + remark + '\'' +
                '}';
    }
}
