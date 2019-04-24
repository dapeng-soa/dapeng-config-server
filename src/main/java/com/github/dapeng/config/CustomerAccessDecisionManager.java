package com.github.dapeng.config;

import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.FilterInvocation;

import java.util.Collection;

/**
 * @author with struy.
 * Create by 2019-01-24 21:26
 * email :yq1724555319@gmail.com
 */

public class CustomerAccessDecisionManager implements AccessDecisionManager {

    /**
     * 全部放行
     */
    private String PERMIT_ALL = "permitAll";
    private String LOGIN_URL = "/login";
    private String ANONYMOUS_USER = "anonymousUser";

    @Override
    public void decide(Authentication authentication, Object o, Collection<ConfigAttribute> collection) throws AccessDeniedException, InsufficientAuthenticationException {
        FilterInvocation filterInvocation = (FilterInvocation) o;
        if (filterInvocation.getRequestUrl().trim().equals(LOGIN_URL)) {
            return;
        }

        // 访客登陆失效
        if (authentication.getPrincipal().toString().equals(ANONYMOUS_USER)) {
            authentication.setAuthenticated(false);
            return;
        }

        // 没有指定的话默认不允许访问
        if (collection == null) {
            throw new AccessDeniedException("没有权限访问！");
        }
        for (ConfigAttribute ca : collection) {
            //访问所请求资源所需要的权限
            String needRole = ca.getAttribute();
            if (null == needRole) {
                return;
            }
            for (GrantedAuthority ga : authentication.getAuthorities()) {
                if (needRole.trim().equals(ga.getAuthority().trim())) {
                    return;
                }
            }
        }
        throw new AccessDeniedException("没有权限访问！");
    }

    @Override
    public boolean supports(ConfigAttribute configAttribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}
