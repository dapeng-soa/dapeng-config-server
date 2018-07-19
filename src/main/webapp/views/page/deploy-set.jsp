<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
    <script src="${basePath}/js/api/deploy-set.js"></script>
</head>
<body>
<jsp:include page="../core/model.jsp"/>
<jsp:include page="../core/sidebar.jsp"/>
<div class="container-right">
    <div class="container-right-context animated fadeIn">
        <div class="panel-header">
            <div class="input-group">
                <p class="left-panel-title">环境集管理</p>
                <span class="input-group-btn panel-button-group">
                    <%--<button type="button" class="btn btn-success" onclick="InitWhiteList()">刷新</button>--%>
                    <button type="button" class="btn btn-primary" onclick="openAddDeploySetModle()">新增</button>
                </span>
            </div>
        </div>

        <table id="deploy-set-table">
        </table>
        <jsp:include page="../core/footer.jsp"/>
    </div>
</div>
</body>
</html>
