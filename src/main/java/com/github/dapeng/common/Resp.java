package com.github.dapeng.common;

/**
 * @author with struy.
 * Create by 2018/6/1 14:53
 * email :yq1724555319@gmail.com
 */

public class Resp {
    private int code;
    private String msg;
    private Object context;

    private Resp(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    private Resp(int code, String msg, Object context) {
        this.code = code;
        this.context = context;
        this.msg = msg;
    }

    public static Resp of(int code, String msg) {
        return new Resp(code, msg);
    }

    public static Resp of(int code, String msg, Object context) {
        return new Resp(code, msg, context);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Object getContext() {
        return context;
    }

    public void setContext(Object context) {
        this.context = context;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
