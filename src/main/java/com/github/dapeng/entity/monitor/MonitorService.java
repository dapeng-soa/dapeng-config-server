package com.github.dapeng.entity.monitor;

import java.util.List;
import java.util.Objects;

/**
 * @author huyj
 * @Created 2018/6/14 9:59
 */
public class MonitorService {

    private String serviceName;
    private boolean runStatus;
    private int healthStatus;
    private int instanceSize;
    List<MonitorInstance> instanceList;

    public MonitorService(String serviceName) {
        this.serviceName = serviceName;
    }

    public List<MonitorInstance> getInstanceList() {
        return instanceList;
    }

    public void setInstanceList(List<MonitorInstance> instanceList) {
        this.instanceList = instanceList;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }


    public boolean isRunStatus() {
        return Objects.nonNull(instanceList) && !instanceList.isEmpty();
    }

    public int getHealthStatus() {
        if (Objects.isNull(instanceList) || instanceList.isEmpty()) return 1;
        //int healthInstanceNum = instanceList.stream().filter(MonitorInstance::isHealthStatus).collect(Collectors.toList()).size();
        long healthInstanceNum = instanceList.stream().filter(MonitorInstance::isRunStatus).count();
        if (healthInstanceNum == 0) {
            return 2;
        } else if (healthInstanceNum < instanceList.size()) {
            return 3;
        } else if (healthInstanceNum == instanceList.size()) {
            return 4;
        }
        return 0;
    }

    public int getInstanceSize() {
        return Objects.nonNull(instanceList) ? instanceList.size() : 0;
    }
}
