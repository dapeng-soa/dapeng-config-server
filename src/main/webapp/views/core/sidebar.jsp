<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="sidebar-left" style="overflow-y: auto">
    <div class="sidebar-top-box">
        <div class="sidebar-cover">
            <span>
                <img src="${basePath}/images/favicon.png">
            </span>
        </div>
        <a class="header-icon-item" href="${basePath}/logout">
            <span>登出</span><i class="fa fa-sign-out" aria-hidden="true"></i>
        </a>
    </div>
    <ul class="layui-nav layui-nav-tree menu-left" lay-filter="test">
        <li class="layui-nav-item"><a href="${basePath}/clusters"><i class="fa fa-server" aria-hidden="true"></i>集群管理</a>
        </li>
        <li class="layui-nav-item layui-nav-itemed">
            <a href="javascript:void(0);"><i class="fa fa-cogs" aria-hidden="true"></i>配置管理</a>
            <dl class="layui-nav-child">
                <dd class="${sideName == 'config-service' ? 'layui-this' : ''}">
                    <a href="${basePath}/config/service"><i class="fa fa-wrench" aria-hidden="true"></i>服务配置</a>
                </dd>
                <dd class="${sideName == 'config-whitelist' ? 'layui-this' : ''}">
                    <a href="${basePath}/config/whitelist"><i class="fa fa-book" aria-hidden="true"></i>* 白名单 *</a>
                </dd>
                <dd class="${sideName == 'config-apikey' ? 'layui-this' : ''}">
                    <a href="${basePath}/config/apikey"><i class="fa fa-unlock" aria-hidden="true"></i>*ApiKey*</a>
                </dd>
            </dl>
        </li>
        <li class="layui-nav-item layui-nav-itemed">
            <a href="javascript:void(0);"><i class="fa fa-paper-plane" aria-hidden="true"></i>发布部署</a>
            <dl class="layui-nav-child">
                <dd class="${sideName == 'deploy-set' ? 'layui-this' : ''}">
                    <a href="${basePath}/deploy/set"><i class="fa fa-archive" aria-hidden="true"></i>环境管理</a>
                </dd>
                <dd class="${sideName == 'deploy-host' ? 'layui-this' : ''}">
                    <a href="${basePath}/deploy/host"><i class="fa fa-linux" aria-hidden="true"></i>节点管理</a>
                </dd>
                <dd class="${sideName == 'deploy-service' ? 'layui-this' : ''}">
                    <a href="${basePath}/deploy/service"><i class="fa fa-wifi" aria-hidden="true"></i>服务管理</a>
                </dd>
                <dd class="${sideName == 'deploy-unit' ? 'layui-this' : ''}">
                    <a href="${basePath}/deploy/unit"><i class="fa fa-microchip" aria-hidden="true"></i> 部署单元</a>
                </dd>
                <dd class="${sideName == 'deploy-exec' ? 'layui-this' : ''}">
                    <a href="${basePath}/deploy/exec"><i class="fa fa-rocket" aria-hidden="true"></i>上线部署</a>
                </dd>
            </dl>
        </li>
        <li class="layui-nav-item"><a href="${basePath}/monitor"><i class="fa fa-tachometer" aria-hidden="true"></i>服务监控</a>
        </li>
    </ul>
    <a class="toggle-sidebar-btn" onclick="toggleSidebar()">
        <i class="fa fa-play-circle" aria-hidden="true"></i>
    </a>
</div>
<script>
    function toggleSidebar() {
        var sc = $(".sidebar-left,.container-right");
        var isclosed = sc.hasClass("closed");
        var isopened = sc.hasClass("opened");
        if (isclosed){
            sc.removeClass("closed");
            sc.addClass("opened");
        }
        if (isopened){
            sc.removeClass("opened");
            sc.addClass("closed");
        }else {
            sc.addClass("opened");
        }
    }
</script>