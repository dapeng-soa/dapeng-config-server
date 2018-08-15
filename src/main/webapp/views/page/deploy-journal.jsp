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
    <script src="${basePath}/js/api/deploy-journal.js"></script>
</head>
<body>
<jsp:include page="../core/console.jsp"/>
<jsp:include page="../core/model.jsp"/>
<jsp:include page="../core/sidebar.jsp"/>
<div class="container-right">
    <div class="container-right-context animated fadeIn">
        <div class="panel-header">
            <div class="input-group">
                <p class="left-panel-title">部署记录</p>
                <span class="input-group-btn panel-button-group">
                </span>
            </div>
        </div>
        <div id="deploy-journal-tableToolbar">
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
            <span>操作类型： <span style="display: inline-block;width: 120px">
                <select data-live-search="true" class="selectpicker form-control" onchange="viewOpTypeChanged()"
                        id="opTypeView">
                    <option value="0" selected>请选择</option>
                    <option value="1">升级</option>
                    <option value="2">重启</option>
                    <option value="3">停止</option>
                    <option value="4">回滚</option>
                </select>
            </span></span>
        </div>
        <table id="deploy-journal-table">
        </table>
        <jsp:include page="../core/footer.jsp"/>
    </div>
</div>
</body>
</html>
