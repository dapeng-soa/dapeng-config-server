package com.github.dapeng.socket;

/**
 * @author with struy.
 * Create by 2018/7/24 16:44
 * email :yq1724555319@gmail.com
 */

public class HostAgent {
    private String name ;
    private String ip;
    private String sessionId;

    public HostAgent(String name, String ip, String sessionId) {
        this.name = name;
        this.ip = ip;
        this.sessionId = sessionId;
    }

    public String getName() {
        return name;
    }

    public void setName(String namssse) {
        this.name = name;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}
