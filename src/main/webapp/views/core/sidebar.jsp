<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="sidebar-left">
    <ul class="nav nav-pills nav-stacked" style="margin-top: 15px">
        <li class="${sideName == 'config-service' ? 'active' : ''}">
            <a href="${basePath}/config/service">服务配置管理</a>
        </li>
        <li class="${sideName == 'config-whitelist' ? 'active' : ''}">
            <a href="${basePath}/config/whitelist">服务白名单管理</a>
        </li>
        <li class="${sideName == 'config-apikey' ? 'active' : ''}">
            <a href="${basePath}/config/apikey">网关ApiKey管理</a>
        </li>
    </ul>
</div>
