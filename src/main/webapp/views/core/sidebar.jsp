<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="sidebar-left opened">
    <div class="sidebar-top-box">
        <div class="sidebar-cover">
            <span>
                <a href="${basePath}/me">
                <img src="${basePath}/images/favicon.png">
                </a>
            </span>
        </div>
        <p>
            <%--<security:authentication property="principal.username"/>--%>
        </p>
        <a class="header-icon-item" href="${basePath}/logout">
            <span>登出</span><i class="fa fa-sign-out" aria-hidden="true"></i>
        </a>
        <br>
        <%--拥有权限:<security:authentication property="principal.authorities" /><br>--%>
        <br>
        欢迎[<security:authentication property="name"/>]来到大鹏配置中心<br>
    </div>
    <ul class="layui-nav layui-nav-tree menu-left" lay-filter="test">

        <%--配置管理--%>
        <security:authorize url='/config'>
            <li class="layui-nav-item layui-nav-itemed">
                <a href="javascript:void(0);"><i class="fa fa-cogs" aria-hidden="true"></i>配置管理</a>
                <dl class="layui-nav-child">
                    <security:authorize url='/config/clusters'>
                        <dd class="${sideName == 'clusters' ? 'layui-this' : ''}">
                            <a href="${basePath}/config/clusters"><i class="fa fa-server"
                                                                     aria-hidden="true"></i>集群管理</a>
                        </dd>
                    </security:authorize>

                    <security:authorize url='/config/service'>
                        <dd class="${sideName == 'config-service' ? 'layui-this' : ''}">
                            <a href="${basePath}/config/service"><i class="fa fa-wrench" aria-hidden="true"></i>服务配置</a>
                        </dd>
                    </security:authorize>

                    <security:authorize url='/config/whitelist'>
                        <dd class="${sideName == 'config-whitelist' ? 'layui-this' : ''}">
                            <a href="${basePath}/config/whitelist"><i class="fa fa-book" aria-hidden="true"></i>* 白名单 *</a>
                        </dd>
                    </security:authorize>

                    <security:authorize url='/config/apikey'>
                        <dd class="${sideName == 'config-apikey' ? 'layui-this' : ''}">
                            <a href="${basePath}/config/apikey"><i class="fa fa-unlock" aria-hidden="true"></i>*ApiKey*</a>
                        </dd>
                    </security:authorize>

                    <security:authorize url='/config/route'>
                        <dd class="${sideName == 'config-route' ? 'layui-this' : ''}">
                            <a href="${basePath}/config/route"><i class="fa fa-unlock" aria-hidden="true"></i>*路由生成*</a>
                        </dd>
                    </security:authorize>

                    <security:authorize url='/config/runtime'>
                        <dd class="${sideName == 'config-runtime' ? 'layui-this' : ''}">
                            <a href="${basePath}/config/runtime"><i class="fa fa-unlock" aria-hidden="true"></i>*服务实例*</a>
                        </dd>
                    </security:authorize>

                </dl>
            </li>
        </security:authorize>

        <%--持续集成--%>
        <security:authorize url='/build'>
            <li class="layui-nav-item layui-nav-itemed">
                <a href="javascript:void(0);"><i class="fa fa-recycle" aria-hidden="true"></i>持续集成</a>
                <dl class="layui-nav-child">
                    <security:authorize url='/build/exec'>
                        <dd class="${sideName == 'build-exec' ? 'layui-this' : ''}">
                            <a href="${basePath}/build/exec"><i class="fa fa-bug" aria-hidden="true"></i>构建任务</a>
                        </dd>
                    </security:authorize>
                </dl>
            </li>
        </security:authorize>

        <%--发布部署--%>
        <security:authorize url="/deploy">
            <li class="layui-nav-item layui-nav-itemed">
                <a href="javascript:void(0);"><i class="fa fa-paper-plane" aria-hidden="true"></i>发布部署</a>
                <dl class="layui-nav-child">
                    <security:authorize url="/deploy/files">
                        <dd class="${sideName == 'deploy-files' ? 'layui-this' : ''}">
                            <a href="${basePath}/deploy/files"><i class="fa fa-file" aria-hidden="true"></i>文件管理</a>
                        </dd>
                    </security:authorize>

                    <security:authorize url="/deploy/network">
                        <dd class="${sideName == 'deploy-network' ? 'layui-this' : ''}">
                            <a href="${basePath}/deploy/network"><i class="fa fa-signal" aria-hidden="true"></i>网络管理</a>
                        </dd>
                    </security:authorize>

                    <security:authorize url="/deploy/service">
                        <dd class="${sideName == 'deploy-service' ? 'layui-this' : ''}">
                            <a href="${basePath}/deploy/service"><i class="fa fa-wifi" aria-hidden="true"></i>服务管理</a>
                        </dd>
                    </security:authorize>

                    <security:authorize url="/deploy/set">
                        <dd class="${sideName == 'deploy-set' ? 'layui-this' : ''}">
                            <a href="${basePath}/deploy/set"><i class="fa fa-archive" aria-hidden="true"></i>环境管理</a>
                        </dd>
                    </security:authorize>

                    <security:authorize url="/deploy/host">
                        <dd class="${sideName == 'deploy-host' ? 'layui-this' : ''}">
                            <a href="${basePath}/deploy/host"><i class="fa fa-linux" aria-hidden="true"></i>空间管理</a>
                        </dd>
                    </security:authorize>

                    <security:authorize url="/deploy/unit">
                        <dd class="${sideName == 'deploy-unit' ? 'layui-this' : ''}">
                            <a href="${basePath}/deploy/unit"><i class="fa fa-microchip" aria-hidden="true"></i>
                                部署单元</a>
                        </dd>
                    </security:authorize>

                    <security:authorize url="/deploy/exec">
                        <dd class="${sideName == 'deploy-exec' ? 'layui-this' : ''}">
                            <a href="${basePath}/deploy/exec"><i class="fa fa-rocket" aria-hidden="true"></i>上线部署</a>
                        </dd>
                    </security:authorize>

                    <security:authorize url="/deploy/journal">
                        <dd class="${sideName == 'deploy-journal' ? 'layui-this' : ''}">
                            <a href="${basePath}/deploy/journal"><i class="fa fa-history" aria-hidden="true"></i></i>
                                部署记录</a>
                        </dd>
                    </security:authorize>
                </dl>
            </li>
        </security:authorize>

        <%--系统管理--%>
            <security:authorize url="/system">
            <li class="layui-nav-item layui-nav-itemed">
                <a href="javascript:void(0);"><i class="fa fa-wrench" aria-hidden="true"></i>系统管理</a>
                <dl class="layui-nav-child">
                    <security:authorize access="hasRole('ADMIN')">
                        <dd class="${sideName == 'system-menus' ? 'layui-this' : ''}">
                            <a href="${basePath}/system/menus"><i class="fa fa-bars" aria-hidden="true"></i>菜单管理</a>
                        </dd>
                    </security:authorize>
                    <security:authorize access="hasRole('ADMIN')">
                        <dd class="${sideName == 'system-account' ? 'layui-this' : ''}">
                            <a href="${basePath}/system/account"><i class="fa fa-users" aria-hidden="true"></i>账号管理</a>
                        </dd>
                    </security:authorize>
                    <security:authorize url="/system/log">
                        <dd class="${sideName == 'system-log' ? 'layui-this' : ''}">
                            <a href="${basePath}/system/log"><i class="fa fa-file-text-o" aria-hidden="true"></i>操作日志</a>
                        </dd>
                    </security:authorize>
                </dl>
            </li>
        </security:authorize>
    </ul>
    <a class="toggle-sidebar-btn" onclick="toggleSidebar()">
        <i class="fa fa-outdent" aria-hidden="true"></i>
    </a>
</div>
<script>
    function toggleSidebar() {
        var sc = $(".sidebar-left");
        var isopened = sc.hasClass("opened");
        var isclosed = sc.hasClass("closed");
        if (!isopened) {
            $(".toggle-sidebar-btn").css({right: 0, color: '#ccc'});
            openSidebar();
        } else if (isopened && isclosed) {
            $(".toggle-sidebar-btn").css({right: -40 + 'px', color: '#333'});
            closeSidebar();
        } else {
            $(".toggle-sidebar-btn").css({right: -40 + 'px', color: '#333'});
            closeSidebar();
        }
    }

    function openSidebar() {
        var sc = $(".sidebar-left");
        var cn = $(".container-right");
        sc.removeClass("closed");
        sc.animateCss("fadeInLeft").addClass("opened");
        cn.removeClass("closed");
        cn.addClass("opened");
    }

    function closeSidebar() {
        var sc = $(".sidebar-left");
        var cn = $(".container-right");
        sc.removeClass("opened");
        sc.addClass("closed");
        cn.removeClass("opened");
        cn.addClass("closed");
    }
</script>