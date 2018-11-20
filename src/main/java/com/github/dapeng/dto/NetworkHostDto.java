package com.github.dapeng.dto;

import java.util.List;

/**
 * @author with struy.
 * Create by 2018/11/15 13:31
 * email :yq1724555319@gmail.com
 */

public class NetworkHostDto {
    private List<Long> hostIds;
    private Long netId;

    public List<Long> getHostIds() {
        return hostIds;
    }

    public void setHostIds(List<Long> hostIds) {
        this.hostIds = hostIds;
    }

    public Long getNetId() {
        return netId;
    }

    public void setNetId(Long netId) {
        this.netId = netId;
    }
}
