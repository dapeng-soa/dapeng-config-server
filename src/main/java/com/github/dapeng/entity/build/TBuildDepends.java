package com.github.dapeng.entity.build;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

@Entity
@Table(name = "t_build_depends")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class TBuildDepends {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "task_id")
    private long taskId;
    @Column(name = "service_name")
    private String serviceName;
    @Column(name = "git_name")
    private String gitName;
    @Column(name = "git_url")
    private String gitUrl;
    @Column(name = "branch_name")
    private String branchName;
    @Column(name = "build_operation")
    private String buildOperation;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public long getTaskId() {
        return taskId;
    }

    public void setTaskId(long taskId) {
        this.taskId = taskId;
    }


    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }


    public String getGitName() {
        return gitName;
    }

    public void setGitName(String gitName) {
        this.gitName = gitName;
    }


    public String getGitUrl() {
        return gitUrl;
    }

    public void setGitUrl(String gitUrl) {
        this.gitUrl = gitUrl;
    }


    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }


    public String getBuildOperation() {
        return buildOperation;
    }

    public void setBuildOperation(String buildOperation) {
        this.buildOperation = buildOperation;
    }

}
