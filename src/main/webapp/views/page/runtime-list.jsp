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
    <script src="${basePath}/plugins/tree/bootstrap-treeview.min.js"></script>
    <script src="${basePath}/js/api/runtime-list.js"></script>
    <%--<meta http-equiv="refresh" content="10">--%>
</head>
<body>
<jsp:include page="../core/sidebar.jsp"/>
<div class="container-right">
    <div class="container-right-context animated fadeIn">
        <div class="panel-header">
            <div class="input-group">
                <p class="left-panel-title">服务实例</p>
                <span class="input-group-btn panel-button-group">
                    <button type="button" class="btn btn-warning" onclick="javascript:location.reload();">刷新</button>
                </span>
            </div>
        </div>
        <div class="row">
            <div class="col-sm-12" style="border-right:1px solid #ccc;height: 100%;">
                <%--tree--%>
                <div style="max-height: 800px;min-height: 400px;margin-top: 20px;">
                    <ul id="menu-tree">
                    </ul>
                </div>
            </div>
        </div>
        <jsp:include page="../core/footer.jsp"/>
    </div>
</div>
</body>
</html>
