package com.github.dapeng.datasource;

/**
 * @author with struy.
 * Create by 2018/6/14 00:17
 * email :yq1724555319@gmail.com
 */

public class DynamicDataSourceHolder {
    private static final ThreadLocal<String> CONTEXT_HOLDER = new ThreadLocal<>();

    public static void setDataSource(String name) {
        CONTEXT_HOLDER.set(name);
    }

    public static String getDataSource() {
        return CONTEXT_HOLDER.get();
    }

   /* public static void reset() {
        CONTEXT_HOLDER.set("mainSource");
    }*/

    public static void clearDataSources() {
        CONTEXT_HOLDER.remove();
    }
}
