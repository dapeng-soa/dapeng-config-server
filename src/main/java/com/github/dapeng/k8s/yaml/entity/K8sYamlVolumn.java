package com.github.dapeng.k8s.yaml.entity;

/**
 * @author huyj
 * @Created 2019-06-18 17:14
 */
public class K8sYamlVolumn {

    private String volumeMounts;
    private String volumes;
    private String volumePvpcs;

    public K8sYamlVolumn(){

    }

    public K8sYamlVolumn(String volumeMounts, String volumes, String volumePvpcs) {
        this.volumeMounts = volumeMounts;
        this.volumes = volumes;
        this.volumePvpcs = volumePvpcs;
    }

    public String getVolumeMounts() {
        return volumeMounts;
    }

    public void setVolumeMounts(String volumeMounts) {
        this.volumeMounts = volumeMounts;
    }

    public String getVolumes() {
        return volumes;
    }

    public void setVolumes(String volumes) {
        this.volumes = volumes;
    }

    public String getVolumePvpcs() {
        return volumePvpcs;
    }

    public void setVolumePvpcs(String volumePvpcs) {
        this.volumePvpcs = volumePvpcs;
    }
}
