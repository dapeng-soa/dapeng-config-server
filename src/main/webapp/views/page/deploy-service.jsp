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
    <script src="${basePath}/js/api/deploy-service.js"></script>
</head>
<body>
<jsp:include page="../core/model.jsp"/>
<jsp:include page="../core/sidebar.jsp"/>
<div class="container-right">
    <div class="container-right-context animated fadeIn">
        <div class="panel-header">
            <div class="input-group">
                <p class="left-panel-title">服务管理</p>
                <span class="input-group-btn panel-button-group">
                    <button type="button" class="btn btn-primary" onclick="openAddDeployServiceModle()">新增</button>
                </span>
            </div>
        </div>
        <div id="deploy-service-tableToolbar">
            <span>tags过滤： <span style="display: inline-block;width: 120px">
                <select class="form-control selectpicker" data-live-search="true"  onchange="execServiceTagChanged(this)" id="deployServiceTags" >

            </select>
            </span></span>
        </div>
        <table id="deploy-service-table">
        </table>
        <jsp:include page="../core/footer.jsp"/>
    </div>
</div>
</body>
</html>
