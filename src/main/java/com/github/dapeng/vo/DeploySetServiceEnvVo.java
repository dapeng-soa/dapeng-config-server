package com.github.dapeng.vo;

import com.github.dapeng.entity.deploy.TSetServiceEnv;

import java.util.List;

/**
 * @author with struy.
 * Create by 2018/9/5 14:44
 * email :yq1724555319@gmail.com
 */

public class DeploySetServiceEnvVo {


    private List<TSetServiceEnv> subEnv;

    public List<TSetServiceEnv> getSubEnv() {
        return subEnv;
    }

    public void setSubEnv(List<TSetServiceEnv> subEnv) {
        this.subEnv = subEnv;
    }

}
