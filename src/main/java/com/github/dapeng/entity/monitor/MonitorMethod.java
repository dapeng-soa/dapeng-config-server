package com.github.dapeng.entity.monitor;

/**
 * @author huyj
 * @Created 2018/6/14 10:00
 */
public class MonitorMethod {

    private String methodName;
    private long maxTime;
    private double averageTime;
    private long callCount;
    private long failCount;

    public MonitorMethod(String methodName, long maxTime, double averageTime, long callCount, long failCount) {
        this.methodName = methodName;
        this.maxTime = maxTime;
        this.averageTime = averageTime;
        this.callCount = callCount;
        this.failCount = failCount;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public long getMaxTime() {
        return maxTime;
    }

    public void setMaxTime(long maxTime) {
        this.maxTime = maxTime;
    }

    public double getAverageTime() {
        return averageTime;
    }

    public void setAverageTime(double averageTime) {
        this.averageTime = averageTime;
    }

    public long getCallCount() {
        return callCount;
    }

    public void setCallCount(long callCount) {
        this.callCount = callCount;
    }

    public long getFailCount() {
        return failCount;
    }

    public void setFailCount(long failCount) {
        this.failCount = failCount;
    }
}
