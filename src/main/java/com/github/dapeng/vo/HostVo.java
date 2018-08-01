package com.github.dapeng.vo;

import com.fasterxml.jackson.annotation.JsonFormat;

public class HostVo {

  private long id;
  private String name;
  private long setId;
  private String setName;
  private String ip;
  private String labels;
  private long extra;
  private String env;
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
  private java.sql.Timestamp createdAt;
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
  private java.sql.Timestamp updatedAt;
  private String remark;

  public String getSetName() {
    return setName;
  }

  public void setSetName(String setName) {
    this.setName = setName;
  }

  public String getIp() {
    return ip;
  }

  public void setIp(String ip) {
    this.ip = ip;
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

}
