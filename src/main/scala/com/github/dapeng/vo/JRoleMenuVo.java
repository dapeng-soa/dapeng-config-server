package com.github.dapeng.vo;

/**
 * @author with struy.
 * Create by 2019-01-24 11:37
 * email :yq1724555319@gmail.com
 */
public class JRoleMenuVo {
    private Long id;
    private Long roleId;
    private Long menuId;
    private String remark;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Long getMenuId() {
        return menuId;
    }

    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
