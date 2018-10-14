package com.github.dapeng.dto;

/**
 * @author with struy.
 * Create by 2018/10/14 21:12
 * email :yq1724555319@gmail.com
 */

public class ChangePwdDto {
    private String userName;
    private String oldPwd;
    private String confirmPwd;
    private String newPwd;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getOldPwd() {
        return oldPwd;
    }

    public void setOldPwd(String oldPwd) {
        this.oldPwd = oldPwd;
    }

    public String getConfirmPwd() {
        return confirmPwd;
    }

    public void setConfirmPwd(String confirmPwd) {
        this.confirmPwd = confirmPwd;
    }

    public String getNewPwd() {
        return newPwd;
    }

    public void setNewPwd(String newPwd) {
        this.newPwd = newPwd;
    }
}
