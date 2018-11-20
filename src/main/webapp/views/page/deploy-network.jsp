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
    <script src="${basePath}/js/api/deploy-network.js"></script>
</head>
<body>
<jsp:include page="../core/console.jsp"/>
<jsp:include page="../core/model.jsp"/>
<jsp:include page="../core/sidebar.jsp"/>
<div class="container-right">
    <div class="container-right-context animated fadeIn">
        <div class="panel-header">
            <div class="input-group">
                <p class="left-panel-title">网络管理</p>
                <span class="input-group-btn panel-button-group">
                    <button type="button" class="btn btn-primary" onclick="openAddNetWorkModle()">新增</button>
                </span>
            </div>
        </div>
        <div id="deploy-network-tableToolbar">
            <p>
                在这里可以定义网络，并绑定到对应的节点之上,节点上的容器会使用对应的网络
            </p>
        </div>
        <table id="deploy-network-table">

        </table>
    </div>
    <jsp:include page="../core/footer.jsp"/>
</div>
</div>
</body>
</html>
