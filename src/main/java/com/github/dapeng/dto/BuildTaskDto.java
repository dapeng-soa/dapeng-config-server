package com.github.dapeng.dto;

import java.util.List;

/**
 * @author with struy.
 * Create by 2018/10/16 14:23
 * email :yq1724555319@gmail.com
 */

public class BuildTaskDto {
    private Long hostId;
    private Long serviceId;
    private String taskName;
    private List<DependsServiceDto> buildDepends;

    public Long getHostId() {
        return hostId;
    }

    public void setHostId(Long hostId) {
        this.hostId = hostId;
    }

    public Long getServiceId() {
        return serviceId;
    }

    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
    }

    public List<DependsServiceDto> getBuildDepends() {
        return buildDepends;
    }

    public void setBuildDepends(List<DependsServiceDto> buildDepends) {
        this.buildDepends = buildDepends;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }
}
