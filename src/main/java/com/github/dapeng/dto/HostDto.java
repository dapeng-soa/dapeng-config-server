package com.github.dapeng.dto;

public class HostDto {

  private String name;
  private long setId;
  private long ip;
  private String labels;
  private long extra;
  private String env;
  private String remark;

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


  public long getIp() {
    return ip;
  }

  public void setIp(long ip) {
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

}
