<%--
  Created by IntelliJ IDEA.
  User: struy
  Date: 2018/9/18
  Time: 16:23
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <jsp:include page="../core/resource.jsp"/>
    <script src="${basePath}/js/ts/Build.js"></script>
    <script src="${basePath}/js/api/build-exec.js"></script>
</head>
<body>
<jsp:include page="../core/sidebar.jsp"/>
<div class="container-right">

    <div class="container-right-context">
        <div class="panel-header">
            <div class="input-group">
                <p class="left-panel-title">构建任务</p>
                <span class="input-group-btn panel-button-group">
                    <button type="button" class="btn btn-primary" onclick="openAddBuildHostModle()">新增</button>
                </span>
            </div>
        </div>
    </div>
</div>
</body>
</html>
