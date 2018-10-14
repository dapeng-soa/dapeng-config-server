package com.github.dapeng.dto;

import java.util.List;

/**
 * 角色用户关联
 */

public class RoleUserDto {
    /**
     * 角色id
     */
    private List<Long> roleIds;
    /**
     * 用户id
     */
    private Long userId;

    public List<Long> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(List<Long> roleIds) {
        this.roleIds = roleIds;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
