package com.github.dapeng.entity.build;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "t_build_host")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class TBuildHost {

    @Id
    private long id;
    @Column(name = "name")
    private String name;
    @Column(name = "host")
    private int host;
    @Column(name = "remark")
    private String remark;
    @Column(name = "created_at")
    private java.sql.Timestamp createdAt;
    @Column(name = "updated_at")
    private java.sql.Timestamp updatedAt;

    public Long getId() {
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


    public int getHost() {
        return host;
    }

    public void setHost(int host) {
        this.host = host;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TBuildHost host1 = (TBuildHost) o;
        return id == host1.id &&
                host == host1.host &&
                Objects.equals(name, host1.name) &&
                Objects.equals(remark, host1.remark) &&
                Objects.equals(createdAt, host1.createdAt) &&
                Objects.equals(updatedAt, host1.updatedAt);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, name, host, remark, createdAt, updatedAt);
    }

    @Override
    public String toString() {
        return "TBuildHost{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", host=" + host +
                ", remark='" + remark + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
