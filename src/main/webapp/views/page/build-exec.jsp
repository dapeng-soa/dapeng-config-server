<%--
  Created by IntelliJ IDEA.
  User: struy
  Date: 2018/9/18
  Time: 16:23
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<html>
<head>
    <jsp:include page="../core/resource.jsp"/>
    <script src="${basePath}/js/ts/Build.js"></script>
    <script src="${basePath}/js/api/build-exec.js"></script>
</head>
<body>
<jsp:include page="../core/sidebar.jsp"/>
<div class="container-right">

    <div class="container-right-context">
        <div class="panel-header">
            <div class="input-group">
                <p class="left-panel-title">构建任务</p>
                <span class="input-group-btn panel-button-group">
                </span>
            </div>
        </div>
    </div>
    <div style="margin-top: 20px;padding:0 15px">
        <div class="row" style="padding:0 15px">
            <span>环境： <span style="display: inline-block;width: 120px">
                <select id="setSelect" data-live-search="true" onchange="execBuildSetChanged(this)"
                        class="selectpicker form-control">

                </select>
            </span></span>
            <span style="line-height: 250%" id="viewTypeLabel">服务：</span>
            <div id="viewTypeSelect" style="display: inline-block;width: 120px">
                <select id="serviceSelect" data-live-search="true" class="selectpicker form-control"
                        onchange="execBuildServiceChanged()">
                </select>
            </div>
        </div>

        <div class="row" style="margin-top: 20px">
            <div class="col-sm-9">
                <table class="table table-hover table-striped">
                    <thead>
                    <tr>
                        <th>#</th>
                        <th>环境</th>
                        <th>构建节点</th>
                        <th>服务</th>
                        <th>部署节点</th>
                        <th>构建分支</th>
                        <th>修改时间</th>
                        <th>操作</th>
                    </tr>
                    </thead>
                    <tbody id="buildTaskTable">

                    </tbody>
                </table>
            </div>
            <div class="col-sm-3">
                <p class="text-center">构建历史</p>
                <ul class="list-group" style="max-height: 600px;overflow-y: auto;" id="buildingList">

                </ul>
            </div>
        </div>
    </div>
</div>
</body>
</html>
