package com.github.dapeng.vo;

import java.util.List;
import java.util.Map;

public class DockerService {
    private String container_name;
    private String image;
    private String restart;
    private Map<String,String> environment;
    private List<String> ports;
    private String command;
    private String stop_grace_period;
    private List<String> volumes;
    private List<String> labels;
    private List<String> extra_hosts;
    public void setContainer_name(String container_name) {
        this.container_name = container_name;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getContainer_name() {
        return container_name;
    }

    public String getImage() {
        return image;
    }

    public String getRestart() {
        return restart;
    }

    public void setRestart(String restart) {
        this.restart = restart;
    }

    public Map<String, String> getEnvironment() {
        return environment;
    }

    public void setEnvironment(Map<String, String> environment) {
        this.environment = environment;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public List<String> getLabels() {
        return labels;
    }

    public void setLabels(List<String> labels) {
        this.labels = labels;
    }

    public List<String> getPorts() {
        return ports;
    }

    public void setPorts(List<String> ports) {
        this.ports = ports;
    }

    public List<String> getVolumes() {
        return volumes;
    }

    public void setVolumes(List<String> volumes) {
        this.volumes = volumes;
    }

    public List<String> getExtra_hosts() {
        return extra_hosts;
    }

    public String getStop_grace_period() {
        return stop_grace_period;
    }

    public void setStop_grace_period(String stop_grace_period) {
        this.stop_grace_period = stop_grace_period;
    }

    public void setExtra_hosts(List<String> extra_hosts) {
        this.extra_hosts = extra_hosts;
    }
}
