package com.github.dapeng.entity.config;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * @author struy
 */
@Entity
@Table(name = "config_publish_history")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class ConfigPublishHistory {

    @Id
    private long id;

    /**
     * 版本([年月日时分秒]20180611083030)
     */
    @Column(name = "version")
    private String version;

    /**
     * 服务名
     */
    @Column(name = "service_name")
    private String serviceName;

    @Column(name = "timeout_config")
    private String timeoutConfig;

    @Column(name = "loadbalance_config")
    private String loadbalanceConfig;

    @Column(name = "router_config")
    private String routerConfig;

    @Column(name = "freq_config")
    private String freqConfig;

    @Column(name = "cookie_config")
    private String cookieConfig;

    /**
     * 发布人
     */
    @Column(name = "published_by")
    private long publishedBy;

    /**
     * 发布时间
     */
    @Column(name = "published_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    private java.sql.Timestamp publishedAt;

    @Column(name = "remark")
    private String remark;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }


    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
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


    public long getPublishedBy() {
        return publishedBy;
    }

    public void setPublishedBy(long publishedBy) {
        this.publishedBy = publishedBy;
    }


    public java.sql.Timestamp getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(java.sql.Timestamp publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCookieConfig() {
        return cookieConfig;
    }

    public void setCookieConfig(String cookieConfig) {
        this.cookieConfig = cookieConfig;
    }

    @Override
    public String toString() {
        return "ConfigPublishHistory{" +
                "id=" + id +
                ", version='" + version + '\'' +
                ", serviceName='" + serviceName + '\'' +
                ", timeoutConfig='" + timeoutConfig + '\'' +
                ", loadbalanceConfig='" + loadbalanceConfig + '\'' +
                ", routerConfig='" + routerConfig + '\'' +
                ", freqConfig='" + freqConfig + '\'' +
                ", publishedBy=" + publishedBy +
                ", publishedAt=" + publishedAt +
                ", remark='" + remark + '\'' +
                '}';
    }
}
