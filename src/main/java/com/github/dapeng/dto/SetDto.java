package com.github.dapeng.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * @author with struy.
 * Create by 2018/6/22 16:09
 * email :yq1724555319@gmail.com
 * 环境集信息
 */
public class SetDto {

    private String name;
    private String env = "";
    private String remark = "";
    private List<SubEnv>  subEnv = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getEnv() {
        return env;
    }

    public void setEnv(String env) {
        this.env = env;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List getSubEnv() {
        return subEnv;
    }

    public void setSubEnv(List subEnv) {
        this.subEnv = subEnv;
    }
}
