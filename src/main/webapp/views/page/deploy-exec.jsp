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
    <script src="${basePath}/js/api/deploy-exec.js"></script>
</head>
<body>
<jsp:include page="../core/console.jsp"/>
<jsp:include page="../core/model.jsp"/>
<jsp:include page="../core/sidebar.jsp"/>
<div class="container-right">
    <div class="container-right-context animated fadeIn">
        <div class="panel-header">
            <div class="input-group">
                <p class="left-panel-title">发布部署</p>
                <span class="input-group-btn panel-button-group">
                    <button type="button" class="btn btn-default" onclick="">操作说明</button>
                    <button type="button" class="btn btn-info" onclick="">Agent列表</button>
                </span>
            </div>
        </div>

        <div class="row" style="margin-top: 20px;padding:0 15px">
            <span>环境： <span style="display: inline-block;width: 120px">
                <select id="setSelect" data-live-search="true" onchange="execSetChanged(this)"
                        class="selectpicker form-control">

                </select>
            </span></span>
            <span>视图： <span style="display: inline-block;width: 120px">
                <select id="viewType" data-live-search="true" class="selectpicker form-control"
                        onchange="execViewTypeChanged(this)">
                    <option value="1" selected>服务视图</option>
                    <option value="2">主机视图</option>
                </select>
            </span></span>
            <span style="line-height: 250%" id="viewTypeLabel">服务：</span>
            <div id="viewTypeSelect" style="display: inline-block;width: 120px">

            </div>
        </div>

        <div class="row" style="margin-top: 20px" id="deployMain">

        </div>
        <jsp:include page="../core/footer.jsp"/>
    </div>
</div>
</body>
</html>
