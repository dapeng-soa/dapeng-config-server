package com.github.dapeng.entity.deploy;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

@Entity
@Table(name = "t_host")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class THost {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;
  @Column(name = "name")
  private String name;
  @Column(name = "set_id")
  private long setId;
  @Column(name = "ip")
  private int ip;
  @Column(name = "labels")
  private String labels;
  @Column(name = "extra")
  private long extra;
  @Column(name = "env")
  private String env;
  @Column(name = "created_at")
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
  private java.sql.Timestamp createdAt;
  @Column(name = "updated_at")
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
  private java.sql.Timestamp updatedAt;
  @Column(name = "remark")
  private String remark;
  @Column(name = "deleted")
  private int deleted;

  public int getDeleted() {
    return deleted;
  }

  public void setDeleted(int deleted) {
    this.deleted = deleted;
  }


  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }


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

  public int getIp() {
    return ip;
  }

  public void setIp(int ip) {
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

  @Override
  public String toString() {
    return "THost{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", setId=" + setId +
            ", ip=" + ip +
            ", labels='" + labels + '\'' +
            ", extra=" + extra +
            ", env='" + env + '\'' +
            ", createdAt=" + createdAt +
            ", updatedAt=" + updatedAt +
            ", remark='" + remark + '\'' +
            '}';
  }
}
