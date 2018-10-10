package com.github.dapeng.entity.config;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

/**
 * ZK 节点
 *
 * @author huyj
 * @Created 2018/6/14 15:53
 */
@Entity
@Table(name = "zk_node")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class ZkNode {
    @Id
    private long id ;

    @Column(name = "zk_host")
    private String zkHost;

    @Column(name = "influxdb_host")
    private String influxdbHost;

    @Column(name = "influxdb_user")
    private String influxdbUser;

    @Column(name = "influxdb_pass")
    private String influxdbPass;

    @Column(name = "remark")
    private String remark;


    /**
     * 添加人
     */
    @Column(name = "created_by")
    private long createdBy;
    /**
     * 最后更新人
     */
    @Column(name = "updated_by")
    private long updatedBy;
    /**
     * 添加时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    @Column(name = "created_at")
    private java.sql.Timestamp createdAt;
    /**
     * 最后更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    @Column(name = "updated_at")
    private java.sql.Timestamp updatedAt;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

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

    public long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(long createdBy) {
        this.createdBy = createdBy;
    }

    public long getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(long updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "ZkNode{" +
                "id=" + id +
                ", zkHost='" + zkHost + '\'' +
                ", influxdbHost='" + influxdbHost + '\'' +
                ", influxdbUser='" + influxdbUser + '\'' +
                ", influxdbPass='" + influxdbPass + '\'' +
                ", remark='" + remark + '\'' +
                ", createdBy=" + createdBy +
                ", updatedBy=" + updatedBy +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
