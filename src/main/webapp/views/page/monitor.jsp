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
    <script src="${basePath}/js/api/monitor.js"></script>
</head>
<body>
<jsp:include page="../core/header.jsp"/>
<jsp:include page="../core/sidebar.jsp"/>
<div class="container-right">
    <div class="container-right-context animated fadeIn">
        <div class="panel-header">
            <div class="input-group">
                <p class="left-panel-title">服务监控</p>
                <span class="input-group-btn panel-button-group">
                    <%--<button type="button" class="btn btn-success" onclick="InitWhiteList()">刷新</button>--%>
                </span>
            </div>
        </div>

        <table id="monitor-table"></table>

        <%--<div id="toolbar">
            <select id="slpk" class="selectpicker" data-live-search="true" multiple></select>
        </div>--%>
        <div id="toolbar" class="col-sm-12">
            <div class="row">
                <label class="col-sm-5 control-label">注册中心:</label>
                <div class="col-sm-7">
                    <select id="nodeSelect" name="select" class="selectpicker show-tick form-control">
                        　　<%--<option value="127.0.0.1">127.0.0.1</option>
                        　　<option value="10.10.10.45">10.10.10.45</option>--%>
                    </select>
                </div>
            </div>
        </div>

        <jsp:include page="../core/footer.jsp"/>
    </div>
</div>
</body>
</html>
