package com.github.dapeng.socket.entity;

/**
 * @author with struy.
 * Create by 2018/7/26 18:11
 * email :yq1724555319@gmail.com
 */

public class DockerVo {

    private Long lastDeployTime;
    private DockerYaml dockerYaml;

    public Long getLastDeployTime() {
        return lastDeployTime;
    }

    public void setLastDeployTime(Long lastDeployTime) {
        this.lastDeployTime = lastDeployTime;
    }

    public DockerYaml getDockerYaml() {
        return dockerYaml;
    }

    public void setDockerYaml(DockerYaml dockerYaml) {
        this.dockerYaml = dockerYaml;
    }
}
