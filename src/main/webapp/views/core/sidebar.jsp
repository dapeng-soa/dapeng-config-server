<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%--<div class="sidebar-left">
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
</div>--%>
<div class="sidebar-left">
    <ul class="layui-nav layui-nav-tree" lay-filter="test">
        <!-- 侧边导航: <ul class="layui-nav layui-nav-tree layui-nav-side"> -->
        <li class="layui-nav-item layui-nav-itemed">
            <a href="javascript:void(0);"><i class="fa fa-cogs" aria-hidden="true"></i>配置管理</a>
            <dl class="layui-nav-child">
                <dd class="${sideName == 'config-service' ? 'layui-this' : ''}">
                    <a href="${basePath}/config/service"><i class="fa fa-wrench" aria-hidden="true"></i>服务配置管理</a>
                </dd>
                <dd class="${sideName == 'config-whitelist' ? 'layui-this' : ''}">
                    <a href="${basePath}/config/whitelist"><i class="fa fa-book" aria-hidden="true"></i>服务白名单管理</a>
                </dd>
                <dd class="${sideName == 'config-apikey' ? 'layui-this' : ''}">
                    <a href="${basePath}/config/apikey"><i class="fa fa-unlock" aria-hidden="true"></i>网关ApiKey管理</a>
                </dd>
            </dl>
        </li>
        <li class="layui-nav-item"><a href="${basePath}/deploy"><i class="fa fa-paper-plane" aria-hidden="true"></i>发布部署</a></li>
        <li class="layui-nav-item"><a href="${basePath}/monitor"><i class="fa fa-tachometer" aria-hidden="true"></i>服务监控</a></li>
    </ul>
</div>