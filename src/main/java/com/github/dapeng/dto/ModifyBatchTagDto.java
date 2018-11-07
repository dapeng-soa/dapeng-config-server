package com.github.dapeng.dto;

import java.util.List;

/**
 * @author with struy.
 * Create by 2018/9/13 17:29
 * email :yq1724555319@gmail.com
 */

public class ModifyBatchTagDto {
    private List<Long> ids;
    private String tag;
    private String publishTag;

    public List<Long> getIds() {
        return ids;
    }

    public void setIds(List<Long> ids) {
        this.ids = ids;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getPublishTag() {
        return publishTag;
    }

    public void setPublishTag(String publishTag) {
        this.publishTag = publishTag;
    }
}
