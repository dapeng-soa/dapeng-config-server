package com.github.dapeng.vo;

import java.util.List;

/**
 * @author with struy.
 * Create by 2019-01-22 18:07
 * email :yq1724555319@gmail.com
 */
public class JMenuVo {
    private Long id;
    private String name;
    private String text;
    private String url;
    private String status;
    private String remark;
    private Long parentId;
    private List<JMenuVo> nodes;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public List<JMenuVo> getNodes() {
        return nodes;
    }

    public void setNodes(List<JMenuVo> nodes) {
        this.nodes = nodes;
    }
}
