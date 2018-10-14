package com.github.dapeng.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import static com.github.dapeng.common.Commons.SUPER_ROLE;

/**
 * @author with struy.
 * Create by 2018/10/14 19:29
 * email :yq1724555319@gmail.com
 */

public class SecurityUtil {
    /**
     * 获取当前登陆用户名
     *
     * @return
     */
    public static String me() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    /**
     * 检查是不是管理员角色
     *
     * @throws Exception
     */
    public static void checkAdmin() throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = authentication.getAuthorities().contains(new SimpleGrantedAuthority(SUPER_ROLE));
        if (!isAdmin) {
            throw new Exception("操作权限不足");
        }
    }
}
