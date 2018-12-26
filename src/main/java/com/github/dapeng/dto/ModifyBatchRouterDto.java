package com.github.dapeng.dto;

import java.util.List;

/**
 * @author with struy.
 * Create by 2018/9/13 17:29
 * email :yq1724555319@gmail.com
 */

public class ModifyBatchRouterDto {
    private List<Long> ids;
    private String router;

    public List<Long> getIds() {
        return ids;
    }

    public void setIds(List<Long> ids) {
        this.ids = ids;
    }

    public String getRouter() {
        return router;
    }

    public void setRouter(String router) {
        this.router = router;
    }
}
