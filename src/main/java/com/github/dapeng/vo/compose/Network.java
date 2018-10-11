package com.github.dapeng.vo.compose;

import java.util.HashMap;
import java.util.Map;

import static com.github.dapeng.common.Commons.*;

/**
 * @author with struy.
 * Create by 2018/10/11 11:35
 * email :yq1724555319@gmail.com
 */

public class Network {
    private Map<String, String> driver_opts;
    private boolean external = false;

    public Network() {
        Map<String, String> map = new HashMap<>();
        map.put(NETWORK_MTU_KEY, NETWORK_MTU_VAL);
        this.driver_opts = map;
    }

    public Network(String mtu) {
        Map<String, String> map = new HashMap<>();
        map.put(NETWORK_MTU_KEY, mtu);
        this.driver_opts = map;
    }

    public Map<String, String> getDriver_opts() {
        return driver_opts;
    }


    public void setDriver_opts(Map<String, String> driver_opts) {
        this.driver_opts = driver_opts;
    }

    public boolean isExternal() {
        return external;
    }

    public void setExternal(boolean external) {
        this.external = external;
    }
}
