package com.github.dapeng.dto;

/**
 * @author with struy.
 * Create by 2018/6/22 16:09
 * email :yq1724555319@gmail.com
 */

public class ZkNodeDto {

    private String zkHost = "127.0.0.1:2181";

    private String influxdbHost = "";

    private String influxdbUser = "";

    private String influxdbPass = "";

    private String remark = "";

    public String getZkHost() {
        return zkHost;
    }

    public void setZkHost(String zkHost) {
        this.zkHost = zkHost;
    }

    public String getInfluxdbHost() {
        return influxdbHost;
    }

    public void setInfluxdbHost(String influxdbHost) {
        this.influxdbHost = influxdbHost;
    }

    public String getInfluxdbUser() {
        return influxdbUser;
    }

    public void setInfluxdbUser(String influxdbUser) {
        this.influxdbUser = influxdbUser;
    }

    public String getInfluxdbPass() {
        return influxdbPass;
    }

    public void setInfluxdbPass(String influxdbPass) {
        this.influxdbPass = influxdbPass;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "ZkNodeDto{" +
                "zkHost='" + zkHost + '\'' +
                ", influxdbHost='" + influxdbHost + '\'' +
                ", influxdbUser='" + influxdbUser + '\'' +
                ", influxdbPass='" + influxdbPass + '\'' +
                ", remark='" + remark + '\'' +
                '}';
    }
}
