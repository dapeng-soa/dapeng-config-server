package com.github.dapeng.dto;

import java.util.List;

/**
 * @author with struy.
 * Create by 2018/9/27 23:52
 * email :yq1724555319@gmail.com
 * 文件部署单元关联
 */

public class FileUnitDto {
    /**
     * 部署单元id
     */
    private List<Long> uids;
    /**
     * 文件id
     */
    private Long fid;

    public List<Long> getUids() {
        return uids;
    }

    public void setUids(List<Long> uids) {
        this.uids = uids;
    }

    public Long getFid() {
        return fid;
    }

    public void setFid(Long fid) {
        this.fid = fid;
    }

    @Override
    public String toString() {
        return "FileUnitDto{" +
                "uids=" + uids +
                ", fid=" + fid +
                '}';
    }
}
