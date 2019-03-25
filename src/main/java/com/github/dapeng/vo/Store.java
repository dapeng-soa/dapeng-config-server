package com.github.dapeng.vo;

/**
 * @author huyj
 * @Created 2019-03-19 21:49
 */
public class Store {
    private String storeCode;
    private String storeName;

    public Store(String storeCode, String storeName) {
        this.storeCode = storeCode;
        this.storeName = storeName;
    }

    public String getStoreCode() {
        return storeCode;
    }

    public void setStoreCode(String storeCode) {
        this.storeCode = storeCode;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }
}
