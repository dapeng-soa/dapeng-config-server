package com.github.dapeng.entity.deploy;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

@Entity
@Table(name = "t_deploy_unit_status")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class TDeployUnitStatus {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;
  @Column(name = "current_timestamp")
  private String currentTimestamp;
  @Column(name = "current_yml")
  private String currentYml;
  @Column(name = "expired")
  private String expired;


  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }


  public String getCurrentTimestamp() {
    return currentTimestamp;
  }

  public void setCurrentTimestamp(String currentTimestamp) {
    this.currentTimestamp = currentTimestamp;
  }


  public String getCurrentYml() {
    return currentYml;
  }

  public void setCurrentYml(String currentYml) {
    this.currentYml = currentYml;
  }


  public String getExpired() {
    return expired;
  }

  public void setExpired(String expired) {
    this.expired = expired;
  }

  @Override
  public String toString() {
    return "TDeployUnitStatus{" +
            "id=" + id +
            ", currentTimestamp='" + currentTimestamp + '\'' +
            ", currentYml='" + currentYml + '\'' +
            ", expired='" + expired + '\'' +
            '}';
  }
}
