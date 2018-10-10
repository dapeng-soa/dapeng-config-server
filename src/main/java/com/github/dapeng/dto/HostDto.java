package com.github.dapeng.dto;

/**
 * @author with struy.
 * Create by 2018/6/22 16:09
 * email :yq1724555319@gmail.com
 * 节点信息
 */
public class HostDto {

    private String name;
    private long setId;
    private String ip;
    private String labels = "";
    private long extra;
    private String env = "";
    private String remark = "";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public long getSetId() {
        return setId;
    }

    public void setSetId(long setId) {
        this.setId = setId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getLabels() {
        return labels;
    }

    public void setLabels(String labels) {
        this.labels = labels;
    }


    public long getExtra() {
        return extra;
    }

    public void setExtra(long extra) {
        this.extra = extra;
    }


    public String getEnv() {
        return env;
    }

    public void setEnv(String env) {
        this.env = env;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "HostDto{" +
                "name='" + name + '\'' +
                ", setId=" + setId +
                ", ip='" + ip + '\'' +
                ", labels='" + labels + '\'' +
                ", extra=" + extra +
                ", env='" + env + '\'' +
                ", remark='" + remark + '\'' +
                '}';
    }
}
