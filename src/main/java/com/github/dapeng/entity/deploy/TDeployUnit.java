package com.github.dapeng.entity.deploy;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

@Entity
@Table(name = "t_deploy_unit")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class TDeployUnit {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
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
  @Column(name = "created_at")
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
  private java.sql.Timestamp createdAt;
  @Column(name = "updated_at")
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
  private java.sql.Timestamp updatedAt;
  @Column(name = "env")
  private String env;
  @Column(name = "ports")
  private String ports;
  @Column(name = "volumes")
  private String volumes;
  @Column(name = "docker_extras")
  private String dockerExtras;


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


  public String getEnv() {
    return env;
  }

  public void setEnv(String env) {
    this.env = env;
  }


  public String getPorts() {
    return ports;
  }

  public void setPorts(String ports) {
    this.ports = ports;
  }


  public String getVolumes() {
    return volumes;
  }

  public void setVolumes(String volumes) {
    this.volumes = volumes;
  }


  public String getDockerExtras() {
    return dockerExtras;
  }

  public void setDockerExtras(String dockerExtras) {
    this.dockerExtras = dockerExtras;
  }

  @Override
  public String toString() {
    return "TDeployUnit{" +
            "id=" + id +
            ", setId=" + setId +
            ", hostId=" + hostId +
            ", serviceId=" + serviceId +
            ", gitTag='" + gitTag + '\'' +
            ", imageTag='" + imageTag + '\'' +
            ", createdAt=" + createdAt +
            ", updatedAt=" + updatedAt +
            ", env='" + env + '\'' +
            ", ports='" + ports + '\'' +
            ", volumes='" + volumes + '\'' +
            ", dockerExtras='" + dockerExtras + '\'' +
            '}';
  }
}
