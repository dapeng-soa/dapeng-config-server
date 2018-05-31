<%--
  Created by IntelliJ IDEA.
  User: struy
  Date: 2018/5/30
  Time: 09:49
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <jsp:include page="../core/resource.jsp"/>
</head>
<body>
<jsp:include page="../core/header.jsp"/>
<jsp:include page="../core/sidebar.jsp"/>
<div class="container-right">
    <div class="container-right-context">

        <div class="panel-header">
            <div class="input-group">
                <p class="left-panel-title">服务配置管理</p>
                <span class="input-group-btn ">
                    <button type="button" class="btn btn-primary">新增</button>
                    <button type="button" class="btn btn-success">刷新</button>
                    <button type="button" class="btn btn-default">发布历史</button>
                </span>
            </div>
        </div>

        <div class="panel-context">
            <table class="table">
                <thead>
                <tr>
                    <td>序号</td>
                    <td>服务名</td>
                    <td>版本号</td>
                    <td>发布状态</td>
                    <td>发布时间</td>
                    <td>发布人</td>
                    <td>最后修改时间</td>
                    <td>操作</td>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <td>1</td>
                    <td>goodsService</td>
                    <td>12312312</td>
                    <td>
                        <%--0:无效,1:新建,2:审核通过,3:已发布--%>
                        <%--0:danger,1:default,2:primary,3:success--%>
                        <span class="label label-success">已发布</span>
                    </td>
                    <td>现在</td>
                    <td>大佬</td>
                    <td>现在</td>
                    <td>
                        <span class="link-button-table">
                            <%--如果未发布--%>
                            <a href="#">发布</a>
                            <%--如果已发布--%>
                            <a href="#">回滚</a>
                            <a href="#">详情</a>
                        </span>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
</div>
</body>
</html>
