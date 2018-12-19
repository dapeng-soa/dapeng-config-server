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
    <script src="${basePath}/js/api/deploy-unit.js"></script>
</head>
<body>
<jsp:include page="../core/model.jsp"/>
<jsp:include page="../core/sidebar.jsp"/>
<div class="container-right">
    <div class="container-right-context animated fadeIn">
        <div class="panel-header">
            <div class="input-group">
                <p class="left-panel-title">部署单元管理</p>
                <span class="input-group-btn panel-button-group">
                    <button type="button" class="btn btn-info" onclick="modifyBatchTag()">批量修改tag</button>
                    <button type="button" class="btn btn-info" onclick="modifyBatchBranch()">批量修改分支</button>
                    <button type="button" class="btn btn-primary" onclick="openAddDeployUnitModle()">新增</button>
                </span>
            </div>
        </div>
        <div id="deploy-unit-tableToolbar">
            <span>环境集： <span style="display: inline-block;width: 120px">
                <select data-live-search="true" class="selectpicker form-control" onchange="viewUnitSetChanged()"
                        id="setSelectView">

                </select>
            </span></span>

            <span>节点： <span style="display: inline-block;width: 120px">
                <select data-live-search="true" class="selectpicker form-control" onchange="viewUnitHostChanged()"
                        id="hostSelectView">

                </select>
            </span></span>

            <span>服务： <span style="display: inline-block;width: 120px">
                <select data-live-search="true" class="selectpicker form-control" onchange="viewUnitServiceChanged()"
                        id="serviceSelectView">

                </select>
            </span></span>
        </div>
        <table id="deploy-unit-table">
        </table>
        <jsp:include page="../core/footer.jsp"/>
    </div>
</div>
</body>
</html>
