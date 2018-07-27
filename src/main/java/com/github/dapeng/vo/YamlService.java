package com.github.dapeng.vo;

/**
 * @author with struy.
 * Create by 2018/6/22 16:09
 * email :yq1724555319@gmail.com
 * docker-compose .yaml文件映射关系bean
 */
public class YamlService {

  private String name;
  private String image;
  private String env = "";
  private String volumes = "";
  private String ports = "";
  private String extraHosts = "";
  private String composeLabels = "";
  private String dockerExtras = "";



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

  public String getExtraHosts() {
    return extraHosts;
  }

  public void setExtraHosts(String extraHosts) {
    this.extraHosts = extraHosts;
  }
}
