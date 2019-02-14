package com.github.dapeng.util;

import com.github.dapeng.dto.UserDetail;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

import static com.github.dapeng.common.Commons.SUPER_ROLE;

/**
 * @author with struy.
 * Create by 2018/10/14 19:29
 * email :yq1724555319@gmail.com
 */

public class SecurityUtil {

    private static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public static UserDetail getUserDetail() {
        return (UserDetail) getAuthentication().getPrincipal();
    }

    public static List<String> userMenus() {
        return getUserDetail().getMenus();
    }

    public static Long id(){
        return getUserDetail().getId();
    }

    /**
     * 获取当前登陆用户名
     *
     * @return
     */
    public static String me() {
        return getAuthentication().getName();
    }

    /**
     * 检查是不是管理员角色
     *
     * @throws Exception
     */
    public static void checkAdmin() throws Exception {
        boolean isAdmin = getAuthentication().getAuthorities().contains(new SimpleGrantedAuthority(SUPER_ROLE));
        if (!isAdmin) {
            throw new Exception("操作权限不足");
        }
    }
}
