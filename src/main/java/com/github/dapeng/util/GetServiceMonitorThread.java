package com.github.dapeng.util;

import com.github.dapeng.client.netty.RequestUtils;

import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: zhup
 * @Date: 2018/8/2 20:12
 */

public class GetServiceMonitorThread implements Callable<String> {

    private String romoteIp;
    private Integer remotePort;
    private String serviceName;
    private String version;

    public String getRomoteIp() {
        return romoteIp;
    }

    public void setRomoteIp(String romoteIp) {
        this.romoteIp = romoteIp;
    }

    public Integer getRemotePort() {
        return remotePort;
    }

    public void setRemotePort(Integer remotePort) {
        this.remotePort = remotePort;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public GetServiceMonitorThread(String romoteIp, Integer remotePort, String serviceName, String version) {
        this.romoteIp = romoteIp;
        this.remotePort = remotePort;
        this.serviceName = serviceName;
        this.version = version;
    }

    @Override
    public String call() throws Exception {
        String romoteServiceEcho = RequestUtils.getRomoteServiceEcho(romoteIp, remotePort, serviceName, version);
        return romoteServiceEcho;
    }
}
