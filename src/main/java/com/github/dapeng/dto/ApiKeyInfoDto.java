package com.github.dapeng.dto;

/**
 * @author with struy.
 * Create by 2018/6/13 00:28
 * email :yq1724555319@gmail.com
 */

public class ApiKeyInfoDto {
    private String apiKey;
    private String password;
    private String ips;
    private String notes;
    private String biz;

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getIps() {
        return ips;
    }

    public void setIps(String ips) {
        this.ips = ips;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getBiz() {
        return biz;
    }

    public void setBiz(String biz) {
        this.biz = biz;
    }
}
