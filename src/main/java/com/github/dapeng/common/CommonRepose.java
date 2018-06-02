package com.github.dapeng.common;

/**
 * @author with struy.
 * Create by 2018/6/1 14:53
 * email :yq1724555319@gmail.com
 */

public class CommonRepose {
    private int code;
    private Object context;

    private CommonRepose(int code, Object context) {
        this.code = code;
        this.context = context;
    }

    public static CommonRepose of(int code, Object context){
        return  new CommonRepose(code,context);
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
}
