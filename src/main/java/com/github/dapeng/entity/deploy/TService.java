package com.github.dapeng.entity.deploy;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "t_service")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class TService {

  @Id
  private long id;
  @Column(name = "name")
  private String name;
  @Column(name = "image")
  private String image;
  @Column(name = "labels")
  private String labels;
  @Column(name = "env")
  private String env;
  @Column(name = "volumes")
  private String volumes;
  @Column(name = "ports")
  private String ports;
  @Column(name = "compose_labels")
  private String composeLabels;
  @Column(name = "docker_extras")
  private String dockerExtras;
  @Column(name = "created_at")
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
  private java.sql.Timestamp createdAt;
  @Column(name = "updated_at")
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
  private java.sql.Timestamp updatedAt;
  @Column(name = "remark")
  private String remark;


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


  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
  }


  public String getLabels() {
    return labels;
  }

  public void setLabels(String labels) {
    this.labels = labels;
  }


  public String getEnv() {
    return env;
  }

  public void setEnv(String env) {
    this.env = env;
  }


  public String getVolumes() {
    return volumes;
  }

  public void setVolumes(String volumes) {
    this.volumes = volumes;
  }


  public String getPorts() {
    return ports;
  }

  public void setPorts(String ports) {
    this.ports = ports;
  }


  public String getComposeLabels() {
    return composeLabels;
  }

  public void setComposeLabels(String composeLabels) {
    this.composeLabels = composeLabels;
  }


  public String getDockerExtras() {
    return dockerExtras;
  }

  public void setDockerExtras(String dockerExtras) {
    this.dockerExtras = dockerExtras;
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