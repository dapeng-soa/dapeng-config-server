package com.github.dapeng.socket;

import java.util.List;

/**
 * @author with struy.
 * Create by 2018/7/24 16:46
 * email :yq1724555319@gmail.com
 */

public class AgentEvent {
    private List<String> clientSessionIds;
    private String cmd;
    private String serviceName;
    private String content;

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
}
