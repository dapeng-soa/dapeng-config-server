package com.github.dapeng.config;

import com.github.dapeng.common.Commons;
import com.github.dapeng.datasource.ConfigServerDataSource;
import com.github.dapeng.entity.TRole;
import com.github.dapeng.entity.Tmenu;
import com.github.dapeng.query.SystemQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.*;

/**
 * @author with struy.
 * Create by 2019-01-24 21:31
 * email :yq1724555319@gmail.com
 */

public class CustomerInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

    private static final Logger logger = LoggerFactory.getLogger(CustomerInvocationSecurityMetadataSource.class);
    private static Map<String, Collection<ConfigAttribute>> resourceMap = null;

    /**
     * 优先初始化数据源bean
     *
     * @param configServerDataSource
     */
    public CustomerInvocationSecurityMetadataSource(ConfigServerDataSource configServerDataSource) {
        loadResourceDefine();
    }

    public static void loadResourceDefine() {
        resourceMap = new HashMap<>(32);

       /* Map<String, List<String>> map = SystemQuery.loadResourceRoleDefine();
        map.forEach((k, v) -> {
            ArrayList<ConfigAttribute> list = new ArrayList<>();
            v.forEach(y -> list.add(new SecurityConfig(Commons.ROLE_PREFIX + y)));
            resourceMap.put(k, list);
        });*/

        List<Tmenu> menuList = SystemQuery.getMenuList();
        menuList.forEach(menu -> {
            ArrayList<ConfigAttribute> roleStrList = new ArrayList<ConfigAttribute>(32);
            List<TRole> roleList = SystemQuery.getRoleListByMenuID(menu.id());
            if (roleList != null && !roleList.isEmpty()) {
                //  roleStrList = roleList.stream().map(role -> new SecurityConfig(Commons.ROLE_PREFIX + role.role())).collect(Collectors.toList());
                roleList.forEach(y -> roleStrList.add(new SecurityConfig(Commons.ROLE_PREFIX + y.role())));
            }

            //添加超级权限
            roleStrList.add(new SecurityConfig(Commons.SUPER_ROLE));
            resourceMap.put(menu.url(), roleStrList);
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
                /*if(logger.isWarnEnabled()){
                    logger.warn("url地址:" + url+",需要权限:"+ resourceMap.get(resURL));
                }*/
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
