package com.github.dapeng.entity.config;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;


/**
 * @author struy
 */
@Entity
@Table(name = "config_info")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class ConfigInfo {

    @Id
    private long id ;
    /**
     * 服务全限定名
     */
    @Column(name = "service_name")
    private String serviceName;
    /**
     * 发布状态,0:无效,1:新建,2:审核通过,3:已发布
     */
    private Integer status;
    /**
     * 超时配置
     */
    @Column(name = "timeout_config")
    private String timeoutConfig;
    /**
     * 负载均衡配置
     */
    @Column(name = "loadbalance_config")
    private String loadbalanceConfig;
    /**
     * 路由配置
     */
    @Column(name = "router_config")
    private String routerConfig;
    /**
     * 限流配置
     */
    @Column(name = "freq_config")
    private String freqConfig;
    /**
     * 添加人
     */
    @Column(name = "created_by")
    private Long createdBy;
    /**
     * 最后更新人
     */
    @Column(name = "updated_by")
    private Long updatedBy;
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

    @Column(name = "remark")
    private String remark;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getTimeoutConfig() {
        return timeoutConfig;
    }

    public void setTimeoutConfig(String timeoutConfig) {
        this.timeoutConfig = timeoutConfig;
    }

    public String getLoadbalanceConfig() {
        return loadbalanceConfig;
    }

    public void setLoadbalanceConfig(String loadbalanceConfig) {
        this.loadbalanceConfig = loadbalanceConfig;
    }

    public String getRouterConfig() {
        return routerConfig;
    }

    public void setRouterConfig(String routerConfig) {
        this.routerConfig = routerConfig;
    }

    public String getFreqConfig() {
        return freqConfig;
    }

    public void setFreqConfig(String freqConfig) {
        this.freqConfig = freqConfig;
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

    public java.sql.Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(java.sql.Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public java.sql.Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(java.sql.Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
