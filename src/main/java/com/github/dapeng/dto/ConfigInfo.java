package com.github.dapeng.dto;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author struy
 */
@Entity
@Table(name = "config_info")
public class ConfigInfo {

  @Id
  private long id;
  /**
   * 服务全限定名
   */
  private String serviceName;
  /**
   * 唯一的版本,用于回滚和历史查看(uuid?)
   */
  private String version;
  /**
   * 发布状态,0:无效,1:新建,2:审核通过,3:已发布
   */
  private long status;
  /**
   * 超时配置
   */
  private String timeoutConfig;
  /**
   * 负载均衡配置
   */
  private String loadbalanceConfig;
  /**
   * 路由配置
   */
  private String routerConfig;
  /**
   * 限流配置
   */
  private String freqConfig;
  /**
   * 发布人(发布人默认为0,实为未发布状态，发布更新此id)
   */
  private long publishedBy;
  /**
   * 发布时间
   */
  private java.sql.Timestamp publishedAt;
  /**
   * 添加人
   */
  private long createdBy;
  /**
   * 最后更新人
   */
  private long updatedBy;
  /**
   * 添加时间
   */
  private java.sql.Timestamp createdAt;
  /**
   * 最后更新时间
   */
  private java.sql.Timestamp updatedAt;


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


  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }


  public long getStatus() {
    return status;
  }

  public void setStatus(long status) {
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

}
