package com.github.dapeng.common;

/**
 * @author with struy.
 * Create by 2018/6/1 10:46
 * email :yq1724555319@gmail.com
 */

public enum ConfigStatusEnum {
    /**
     * 配置状态默认审核通过
     */
    FAILURE(0, "无效"),

    NEW(1, "新建"),

    PASS(2, "审核通过"),

    PUBLISHED(3, "已发布");

    private int status;
    private String value;

    ConfigStatusEnum(int status, String value) {
        this.status = status;
        this.value = value;
    }

    public int key() {
        return status;
    }

    public String value() {
        return value;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
