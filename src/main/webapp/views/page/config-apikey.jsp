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
    <script src=""></script>
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

        <table id="apikey-table">

        </table>

    </div>
</div>
</body>
</html>
