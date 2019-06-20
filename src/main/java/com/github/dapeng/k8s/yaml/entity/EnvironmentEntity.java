package com.github.dapeng.k8s.yaml.entity;

/**
 * @author huyj
 * @Created 2019-06-04 21:30
 */
public class EnvironmentEntity {
    private String key;
    private String value;

    public EnvironmentEntity() {
    }

    public EnvironmentEntity(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "AttributeEntity{" +
                "key='" + key + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
