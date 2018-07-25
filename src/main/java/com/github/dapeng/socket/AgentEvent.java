package com.github.dapeng.socket;

import java.io.Serializable;
import java.util.List;

/**
 * @author with struy.
 * Create by 2018/7/24 16:46
 * email :yq1724555319@gmail.com
 */

public class AgentEvent implements Serializable {
    private List<String> clientSessionIds;
    private String cmd;
    private String serviceName;
    private String content;

    public AgentEvent(List<String> sessionIds, String cmd, String serviceName, String content) {
        this.clientSessionIds = sessionIds;
        this.cmd = cmd;
        this.serviceName = serviceName;
        this.content = content;
    }

    public List<String> getClientSessionIds() {
        return clientSessionIds;
    }

    public void setClientSessionIds(List<String> clientSessionIds) {
        this.clientSessionIds = clientSessionIds;
    }

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(128);
        sb.append(clientSessionIds.get(0)).append(" ").append(cmd).append(" ").append(serviceName).append(" ").append(content);
        return sb.toString();
    }
}
