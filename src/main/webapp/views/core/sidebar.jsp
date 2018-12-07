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
            <security:authentication property="principal.username"/>
        </p>
        <a class="header-icon-item" href="${basePath}/logout">
            <span>登出</span><i class="fa fa-sign-out" aria-hidden="true"></i>
        </a>
    </div>
    <ul class="layui-nav layui-nav-tree menu-left" lay-filter="test">
        <security:authorize access="hasAnyRole('ADMIN','OPS')">
            <li class="layui-nav-item layui-nav-itemed">
                <a href="javascript:void(0);"><i class="fa fa-cogs" aria-hidden="true"></i>配置管理</a>
                <dl class="layui-nav-child">
                    <dd class="${sideName == 'clusters' ? 'layui-this' : ''}">
                        <a href="${basePath}/config/clusters"><i class="fa fa-server" aria-hidden="true"></i>集群管理</a>
                    </dd>
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
        </security:authorize>
        <c:if test="${build_enable}">
        <security:authorize access="hasAnyRole('ADMIN','OPS','DEV')">
            <li class="layui-nav-item layui-nav-itemed">
                <a href="javascript:void(0);"><i class="fa fa-recycle" aria-hidden="true"></i>持续集成</a>
                <dl class="layui-nav-child">
                    <dd class="${sideName == 'build-host' ? 'layui-this' : ''}">
                        <a href="${basePath}/build/host"><i class="fa fa-sitemap" aria-hidden="true"></i>构建主机</a>
                    </dd>
                    <dd class="${sideName == 'build-exec' ? 'layui-this' : ''}">
                        <a href="${basePath}/build/exec"><i class="fa fa-bug" aria-hidden="true"></i>构建任务</a>
                    </dd>
                </dl>
            </li>
        </security:authorize>
        </c:if>
        <security:authorize access="hasAnyRole('ADMIN','OPS','')">
            <li class="layui-nav-item layui-nav-itemed">
                <a href="javascript:void(0);"><i class="fa fa-paper-plane" aria-hidden="true"></i>发布部署</a>
                <dl class="layui-nav-child">
                    <dd class="${sideName == 'deploy-files' ? 'layui-this' : ''}">
                        <a href="${basePath}/deploy/files"><i class="fa fa-file" aria-hidden="true"></i>文件管理</a>
                    </dd>
                    <dd class="${sideName == 'deploy-network' ? 'layui-this' : ''}">
                        <a href="${basePath}/deploy/network"><i class="fa fa-signal" aria-hidden="true"></i>网络管理</a>
                    </dd>
                    <dd class="${sideName == 'deploy-service' ? 'layui-this' : ''}">
                        <a href="${basePath}/deploy/service"><i class="fa fa-wifi" aria-hidden="true"></i>服务管理</a>
                    </dd>
                    <dd class="${sideName == 'deploy-set' ? 'layui-this' : ''}">
                        <a href="${basePath}/deploy/set"><i class="fa fa-archive" aria-hidden="true"></i>环境管理</a>
                    </dd>
                    <dd class="${sideName == 'deploy-host' ? 'layui-this' : ''}">
                        <a href="${basePath}/deploy/host"><i class="fa fa-linux" aria-hidden="true"></i>节点管理</a>
                    </dd>
                    <dd class="${sideName == 'deploy-unit' ? 'layui-this' : ''}">
                        <a href="${basePath}/deploy/unit"><i class="fa fa-microchip" aria-hidden="true"></i> 部署单元</a>
                    </dd>
                    <dd class="${sideName == 'deploy-exec' ? 'layui-this' : ''}">
                        <a href="${basePath}/deploy/exec"><i class="fa fa-rocket" aria-hidden="true"></i>上线部署</a>
                    </dd>
                    <dd class="${sideName == 'deploy-journal' ? 'layui-this' : ''}">
                        <a href="${basePath}/deploy/journal"><i class="fa fa-history" aria-hidden="true"></i></i>
                            部署记录</a>
                    </dd>
                </dl>
            </li>
        </security:authorize>
        <security:authorize access="hasAnyRole('ADMIN','OPS')">
            <li class="layui-nav-item layui-nav-itemed">
                <a href="javascript:void(0);"><i class="fa fa-wrench" aria-hidden="true"></i>系统管理</a>
                <dl class="layui-nav-child">
                    <security:authorize access="hasRole('ADMIN')">
                        <dd class="${sideName == 'system-account' ? 'layui-this' : ''}">
                            <a href="${basePath}/system/account"><i class="fa fa-users" aria-hidden="true"></i>账号管理</a>
                        </dd>
                    </security:authorize>
                    <security:authorize access="hasAnyRole('ADMIN','OPS')">
                        <dd class="${sideName == 'system-log' ? 'layui-this' : ''}">
                            <a href="${basePath}/system/log"><i class="fa fa-file-text-o"
                                                                aria-hidden="true"></i>操作日志</a>
                        </dd>
                    </security:authorize>
                </dl>
            </li>
        </security:authorize>
        <%--<li class="layui-nav-item"><a href="${basePath}/monitor"><i class="fa fa-tachometer" aria-hidden="true"></i>服务监控</a>
        </li>--%>
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