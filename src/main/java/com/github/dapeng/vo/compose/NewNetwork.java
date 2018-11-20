package com.github.dapeng.vo.compose;

/**
 * @author with struy.
 * Create by 2018/11/15 16:12
 * email :yq1724555319@gmail.com
 */

public class NewNetwork {

    private External external;

    public NewNetwork(String name) {
        External external = new External();
        external.setName(name);
        this.external = external;
    }

    public External getExternal() {
        return external;
    }

    public void setExternal(External external) {
        this.external = external;
    }
}
