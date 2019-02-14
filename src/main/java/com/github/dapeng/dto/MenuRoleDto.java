package com.github.dapeng.dto;

import java.util.List;

/**
 * 角色菜单关联
 */

public class MenuRoleDto {
    /**
     * 角色id
     */
    private List<Long> roleIds;
    /**
     * 菜单id
     */
    private Long menuId;

    public List<Long> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(List<Long> roleIds) {
        this.roleIds = roleIds;
    }

    public Long getMenuId() {
        return menuId;
    }

    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }
}
