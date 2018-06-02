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
                <p class="left-panel-title">API-KEY管理</p>
                <span class="input-group-btn panel-button-group">
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
                    <td>ApiKey</td>
                    <td>密码</td>
                    <td>所属业务</td>
                    <td>IP规则</td>
                    <td>备注</td>
                    <td>最后修改时间</td>
                    <td>操作</td>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <td>1</td>
                    <td>1223</td>
                    <td>12312312</td>
                    <td>wms</td>
                    <td>供应链</td>
                    <td>127.0.0.1</td>
                    <td>xx年</td>
                    <td>
                        <span class="link-button-table">
                            <%--如果未发布--%>
                            <a href="#">分配</a>
                            <%--如果已发布--%>
                            <a href="#">修改</a>
                            <a href="#">删除</a>
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
