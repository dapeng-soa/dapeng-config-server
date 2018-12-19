package com.github.dapeng.dto;

import java.util.List;

/**
 * @author with struy.
 * Create by 2018/9/13 17:29
 * email :yq1724555319@gmail.com
 */

public class ModifyBatchBranchDto {
    private List<Long> ids;
    private String branch;

    public List<Long> getIds() {
        return ids;
    }

    public void setIds(List<Long> ids) {
        this.ids = ids;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }
}
