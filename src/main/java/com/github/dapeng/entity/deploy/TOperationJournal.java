package com.github.dapeng.entity.deploy;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "t_operation_journal")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class TOperationJournal {

  @Id
  private long id;
  @Column(name = "set_id")
  private long setId;
  @Column(name = "host_id")
  private long hostId;
  @Column(name = "service_id")
  private long serviceId;
  @Column(name = "git_tag")
  private String gitTag;
  @Column(name = "image_tag")
  private String imageTag;
  @Column(name = "yml")
  private String yml;
  @Column(name = "diff")
  private String diff;
  @Column(name = "op_flag")
  private long opFlag;
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
  @Column(name = "created_at")
  private java.sql.Timestamp createdAt;
  @Column(name = "created_by")
  private String createdBy;


  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }


  public long getSetId() {
    return setId;
  }

  public void setSetId(long setId) {
    this.setId = setId;
  }


  public long getHostId() {
    return hostId;
  }

  public void setHostId(long hostId) {
    this.hostId = hostId;
  }


  public long getServiceId() {
    return serviceId;
  }

  public void setServiceId(long serviceId) {
    this.serviceId = serviceId;
  }


  public String getGitTag() {
    return gitTag;
  }

  public void setGitTag(String gitTag) {
    this.gitTag = gitTag;
  }


  public String getImageTag() {
    return imageTag;
  }

  public void setImageTag(String imageTag) {
    this.imageTag = imageTag;
  }


  public String getYml() {
    return yml;
  }

  public void setYml(String yml) {
    this.yml = yml;
  }


  public String getDiff() {
    return diff;
  }

  public void setDiff(String diff) {
    this.diff = diff;
  }


  public long getOpFlag() {
    return opFlag;
  }

  public void setOpFlag(long opFlag) {
    this.opFlag = opFlag;
  }


  public java.sql.Timestamp getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(java.sql.Timestamp createdAt) {
    this.createdAt = createdAt;
  }


  public String getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(String createdBy) {
    this.createdBy = createdBy;
  }

  @Override
  public String toString() {
    return "TOperationJournal{" +
            "id=" + id +
            ", setId=" + setId +
            ", hostId=" + hostId +
            ", serviceId=" + serviceId +
            ", gitTag='" + gitTag + '\'' +
            ", imageTag='" + imageTag + '\'' +
            ", yml='" + yml + '\'' +
            ", diff='" + diff + '\'' +
            ", opFlag=" + opFlag +
            ", createdAt=" + createdAt +
            ", createdBy='" + createdBy + '\'' +
            '}';
  }
}
