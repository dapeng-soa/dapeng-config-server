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
    <div class="container-right-context animated fadeIn">
        <div class="panel-header">
            <div class="input-group">
                <p class="left-panel-title">集群管理</p>
                <span class="input-group-btn panel-button-group">
                    <%--<button type="button" class="btn btn-success" onclick="InitWhiteList()">刷新</button>--%>
                </span>
            </div>
        </div>

        <div class="row" style="margin-top: 40px" >
            <div class="col-xs-12">
                <code>暂时没有新内容</code>
            </div>
        </div>
        <jsp:include page="../core/footer.jsp"/>
    </div>
</div>
</body>
</html>
