package com.github.dapeng.entity.monitor;

import java.util.List;

/**
 * @author huyj
 * @Created 2018/6/14 9:59
 */
public class MonitorInstance {
    private String instance;
    private long callCount;
    private double averageTime;
    private long failCount;
    private String containerPool;
    private String containerTask;
    private boolean runStatus;
    List<MonitorMethod> methodList;

    public MonitorInstance(String instance, long callCount, double averageTime, long failCount, String containerPool, String containerTask, boolean runStatus) {
        this.instance = instance;
        this.callCount = callCount;
        this.averageTime = averageTime;
        this.failCount = failCount;
        this.containerPool = containerPool;
        this.containerTask = containerTask;
        this.runStatus = runStatus;
    }

    public List<MonitorMethod> getMethodList() {
        return methodList;
    }

    public void setMethodList(List<MonitorMethod> methodList) {
        this.methodList = methodList;
    }

    public String getInstance() {
        return instance;
    }

    public void setInstance(String instance) {
        this.instance = instance;
    }

    public long getCallCount() {
        return callCount;
    }

    public void setCallCount(long callCount) {
        this.callCount = callCount;
    }

    public double getAverageTime() {
        return averageTime;
    }

    public void setAverageTime(double averageTime) {
        this.averageTime = averageTime;
    }

    public long getFailCount() {
        return failCount;
    }

    public void setFailCount(long failCount) {
        this.failCount = failCount;
    }

    public String getContainerPool() {
        return containerPool;
    }

    public void setContainerPool(String containerPool) {
        this.containerPool = containerPool;
    }

    public String getContainerTask() {
        return containerTask;
    }

    public void setContainerTask(String containerTask) {
        this.containerTask = containerTask;
    }

    public boolean isRunStatus() {
        return runStatus;
    }

    public void setRunStatus(boolean runStatus) {
        this.runStatus = runStatus;
    }
}
