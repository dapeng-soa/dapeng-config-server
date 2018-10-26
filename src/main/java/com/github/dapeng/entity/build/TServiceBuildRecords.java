package com.github.dapeng.entity.build;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

@Entity
@Table(name = "t_service_build_records")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class TServiceBuildRecords {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "agent_host")
    private String agentHost;
    @Column(name = "build_service")
    private String buildService;
    @Column(name = "task_id")
    private long taskId;
    @Column(name = "status")
    private long status;
    @Column(name = "build_log")
    private String buildLog;
    @Column(name = "created_at")
    private java.sql.Timestamp createdAt;
    @Column(name = "created_by")
    private long createdBy;
    @Column(name = "updated_at")
    private java.sql.Timestamp updatedAt;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public String getAgentHost() {
        return agentHost;
    }

    public void setAgentHost(String agentHost) {
        this.agentHost = agentHost;
    }


    public String getBuildService() {
        return buildService;
    }

    public void setBuildService(String buildService) {
        this.buildService = buildService;
    }


    public long getTaskId() {
        return taskId;
    }

    public void setTaskId(long taskId) {
        this.taskId = taskId;
    }


    public long getStatus() {
        return status;
    }

    public void setStatus(long status) {
        this.status = status;
    }


    public String getBuildLog() {
        return buildLog;
    }

    public void setBuildLog(String buildLog) {
        this.buildLog = buildLog;
    }


    public java.sql.Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(java.sql.Timestamp createdAt) {
        this.createdAt = createdAt;
    }


    public long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(long createdBy) {
        this.createdBy = createdBy;
    }


    public java.sql.Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(java.sql.Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

}
