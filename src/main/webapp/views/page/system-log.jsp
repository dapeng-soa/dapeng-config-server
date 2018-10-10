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
    <link rel="stylesheet" href="${basePath}/plugins/json/json.format.css">
    <script src="${basePath}/js/api/system-log.js"></script>
    <script src="${basePath}/plugins/json/json.format.js"></script>
</head>
<body>
<jsp:include page="../core/sidebar.jsp"/>
<div class="container-right">
    <div class="container-right-context animated fadeIn">
        <div class="panel-header">
            <div class="input-group">
                <p class="left-panel-title">操作记录</p>
                <span class="input-group-btn panel-button-group">
                </span>
            </div>
        </div>
        <div id="system-log-tableToolbar">
            <span>方法名： <span style="display: inline-block;width: 120px">
                <select data-live-search="true" class="selectpicker form-control"
                        id="opNameSelect" onchange="reloadTable()">

                </select>
            </span></span>

            <span>操作人： <span style="display: inline-block;width: 120px">
                <select data-live-search="true" class="selectpicker form-control"
                        id="operatorSelect" onchange="reloadTable()">

                </select>
            </span></span>

            <span>返回类型： <span style="display: inline-block;width: 120px">
                <select data-live-search="true" class="selectpicker form-control"
                        id="resultTypeSelect" onchange="reloadTable()">
                    <option value="">请选择</option>
                    <option value="200">成功</option>
                    <option value="4004">失败</option>
                </select>
            </span></span>

            <span>操作类型： <span style="display: inline-block;width: 120px">
                <select data-live-search="true" class="selectpicker form-control"
                        id="opTypeSelect" onchange="reloadTable()">
                    <option value="">请选择</option>
                    <option value="add">增加</option>
                    <option value="edit">修改</option>
                    <option value="del">删除</option>
                </select>
            </span></span>
        </div>
        <table id="system-log-table">
        </table>
        <jsp:include page="../core/footer.jsp"/>
    </div>
</div>
</body>
</html>
