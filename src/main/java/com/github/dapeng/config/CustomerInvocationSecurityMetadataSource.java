package com.github.dapeng.config;

import com.github.dapeng.common.Commons;
import com.github.dapeng.datasource.ConfigServerDataSource;
import com.github.dapeng.query.SystemQuery;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.ServletException;
import java.util.*;

/**
 * @author with struy.
 * Create by 2019-01-24 21:31
 * email :yq1724555319@gmail.com
 */

public class CustomerInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

    private static Map<String, Collection<ConfigAttribute>> resourceMap = null;

    /**
     * 优先初始化数据源bean
     *
     * @param configServerDataSource
     */
    public CustomerInvocationSecurityMetadataSource(ConfigServerDataSource configServerDataSource) {
        loadResourceDefine();
    }

    private void loadResourceDefine() {
        Map<String, List<String>> map = SystemQuery.loadResourceRoleDefine();
        resourceMap = new HashMap<>(32);
        map.forEach((k, v) -> {
            ArrayList<ConfigAttribute> list = new ArrayList<>();
            v.forEach(y -> list.add(new SecurityConfig(Commons.ROLE_PREFIX + y)));
            resourceMap.put(k, list);
        });
    }

    @Override
    public Collection<ConfigAttribute> getAttributes(Object o) throws IllegalArgumentException {
        FilterInvocation filterInvocation = ((FilterInvocation) o);
        // 请求的url地址
        String url = ((FilterInvocation) o).getRequestUrl();
        // logger
        for (String resURL : resourceMap.keySet()) {
            RequestMatcher urlMatcher = new AntPathRequestMatcher(resURL);
            if (urlMatcher.matches(filterInvocation.getHttpRequest())) {
                return resourceMap.get(resURL);
            }
        }
        return null;
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}
