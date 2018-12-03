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
    <script>

        var hostTimer = -1;
        var taskTimer = -1;
        var hostId = ${current.id};
        $(document).ready(function () {
            getBuildListReq(hostId);
            hostTimer = setInterval(function () {
                getBuildListReq(hostId);
            }, 4000);
        });


        var getTaskBuildList = function (taskId) {
            window.clearInterval(hostTimer);
            window.clearInterval(taskTimer);
            getTaskBuildListReq(hostId, taskId);
            taskTimer = setInterval(function () {
                getTaskBuildListReq(hostId, taskId);
            }, 4000)
        }

    </script>
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
    <div>
        <ul id="serviceGroupTabs" class="nav nav-tabs" role="tablist" style="height: 42px">
            <c:forEach var="view" items="${buildViews}" varStatus="vs">
                <li role="presentation"
                    class="<c:choose><c:when test="${current.id == view.key.id}">in active</c:when></c:choose>"
                    style="position: relative">
                    <a class="tab-link" href="${basePath}/build/exec?id=${view.key.id}">${view.key.name}</a>
                </li>
            </c:forEach>
        </ul>
        <div id="serviceGroupTabContent" class="tab-content">
            <c:forEach var="view" items="${buildViews}" varStatus="vs">
                <div role="tabpane1"
                     class="tab-pane fade <c:choose><c:when test="${current.id == view.key.id}">in active</c:when></c:choose>"
                     id="view${vs.index}"
                     aria-labelledby="view${vs.index}Data-tab">
                    <div>
                        <div class="advance-format-item" style="padding-bottom:15px;border-bottom: 1px solid #ccc;">
                            <p class="advance-format-title bg-primary "
                               style="height: 30px;line-height: 200%;padding-left: 10px;" onclick="toggleBlock(this)">
                                <i class="fa fa-plus" aria-hidden="true"></i>新增任务</p>
                            <div class="advance-format-content" style="padding-top: 15px">
                                <div style="float: left;width: 100%;">
                                    <div class="form-inline">
                                        <div class="form-group" style="float: left;margin-right: 10px">
                                            <label for="buildTaskName${view.key.id}">任务名</label>
                                            <input type="text" class="form-control" id="buildTaskName${view.key.id}"
                                                   placeholder="">
                                        </div>
                                        <div class="form-group" style="float: left">
                                            <label for="buildService${view.key.id}" style="line-height: 200%">服务</label>
                                        </div>

                                        <div class="form-group" style="float: left;margin-right: 10px">
                                            <select data-live-search="true" class="form-control selectpicker"
                                                    id="buildService${view.key.id}" data-index="${view.key.id}"
                                                    onchange="serviceSelectChange(this)">
                                                <c:forEach var="s" items="${services}" varStatus="vs">
                                                    <option
                                                            <c:choose>
                                                                <c:when test="${vs.first}">selected</c:when>
                                                            </c:choose> value="${s.id}">${s.name}</option>
                                                </c:forEach>
                                            </select>
                                        </div>
                                    </div>
                                </div>
                                <div>
                                    <div class="form-inline">
                                        <div class="form-group">
                                            <table class="table table-striped">
                                                <thead>
                                                <tr>
                                                    <th>#</th>
                                                    <th>服务</th>
                                                    <th>分支</th>
                                                </tr>
                                                </thead>
                                                <tbody id="dependsService${view.key.id}">

                                                </tbody>
                                            </table>
                                        </div>
                                    </div>
                                    <button type="button" onclick="saveBuildTask('${view.key.id}')"
                                            class="btn btn-success">保存
                                    </button>
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-sm-9">
                                <table class="table table-hover table-striped">
                                    <thead>
                                    <tr>
                                        <th>#</th>
                                        <th>任务名</th>
                                        <th>服务</th>
                                        <th>构建分支</th>
                                        <th>添加时间</th>
                                        <th>操作</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <c:forEach var="task" items="${view.value}" varStatus="vs">
                                        <tr>
                                            <th scope="row">${vs.index+1}</th>
                                            <td><a href="javascript:void(0)"
                                                   onclick="getTaskBuildList(${task.id})">${task.taskName}</a></td>
                                            <td>${task.serviceName}</td>
                                            <td>
                                                <div class="advance-format-item">
                                                    <p class="advance-format-title"
                                                       onclick="toggleBlock(this)">${task.branch}</p>
                                                    <div class="advance-format-content">
                                                        <ul>
                                                            <c:forEach var="depend" items="${task.depends}"
                                                                       varStatus="vs">
                                                                <li>
                                                                    <span>${depend.serviceName}</span> =>
                                                                    <span>${depend.branchName}</span>
                                                                </li>
                                                            </c:forEach>
                                                        </ul>
                                                    </div>
                                                </div>
                                            </td>
                                            <td>${task.updatedAt}</td>
                                            <td><a href="javascript:void(0)" title="开始构建"
                                                   onclick="execBuildService(${task.id})"><i
                                                    class="fa fa-play-circle" aria-hidden="true"></i></a>
                                                <a href="javascript:void(0)" title="删除任务"
                                                   onclick="delBuildTask(${task.id})"><span
                                                        class="glyphicon glyphicon-remove"
                                                        aria-hidden="true"></span></a>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                            <div class="col-sm-3">
                                <p class="text-center">构建历史</p>
                                    <%--构建历史查一下数据库,点击构建历史可以进入到控制台输出--%>
                                <div class="list-group" id="buildingList${current.id}">

                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>
    </div>
</div>
</body>
</html>
