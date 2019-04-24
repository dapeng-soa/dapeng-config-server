package com.github.dapeng.vo;

import com.github.dapeng.dto.HostDto;

import java.util.List;
import java.util.Set;

/**
 * @author huyj
 * @Created 2019-03-20 11:30
 */
public class RouteInitData {

    private List<ServiceInfo> serviceInfoList = null;
    private Set<String> versionList = null;
    private List<HostDto> hostList = null;
    private List<Store> storeList = null;


    public List<ServiceInfo> getServiceInfoList() {
        return serviceInfoList;
    }

    public void setServiceInfoList(List<ServiceInfo> serviceInfoList) {
        this.serviceInfoList = serviceInfoList;
    }

    public Set<String> getVersionList() {
        return versionList;
    }

    public void setVersionList(Set<String> versionList) {
        this.versionList = versionList;
    }

    public List<HostDto> getHostList() {
        return hostList;
    }

    public void setHostList(List<HostDto> hostList) {
        this.hostList = hostList;
    }

    public List<Store> getStoreList() {
        return storeList;
    }

    public void setStoreList(List<Store> storeList) {
        this.storeList = storeList;
    }
}
