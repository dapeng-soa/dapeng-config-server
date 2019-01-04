package com.github.dapeng.vo;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.sql.Timestamp;

/**
 * @author with struy.
 * Create by 2019/1/4 11:35
 * email :yq1724555319@gmail.com
 */

public class DiffYamlVo {
    private String unit1;
    private String unit2;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    private java.sql.Timestamp update1;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    private java.sql.Timestamp update2;
    private String yaml1;
    private String yaml2;

    public String getUnit1() {
        return unit1;
    }

    public void setUnit1(String unit1) {
        this.unit1 = unit1;
    }

    public String getUnit2() {
        return unit2;
    }

    public void setUnit2(String unit2) {
        this.unit2 = unit2;
    }

    public Timestamp getUpdate1() {
        return update1;
    }

    public void setUpdate1(Timestamp update1) {
        this.update1 = update1;
    }

    public Timestamp getUpdate2() {
        return update2;
    }

    public void setUpdate2(Timestamp update2) {
        this.update2 = update2;
    }

    public String getYaml1() {
        return yaml1;
    }

    public void setYaml1(String yaml1) {
        this.yaml1 = yaml1;
    }

    public String getYaml2() {
        return yaml2;
    }

    public void setYaml2(String yaml2) {
        this.yaml2 = yaml2;
    }
}
