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
                    <%--<button type="button" class="btn btn-primary" onclick="openAddBuildHostModle()">新增</button>--%>
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
                        <div class="advance-format-item">
                            <p class="advance-format-title" onclick="toggleBlock(this)">新增</p>
                            <div class="advance-format-content">
                                <div class="form-inline">
                                    <div class="form-group">
                                        <label for="buildTaskName">taskName</label>
                                        <input type="text" class="form-control" id="buildTaskName" placeholder="">
                                    </div>
                                    <div class="form-group">
                                        <label for="buildService">service</label>
                                        <select data-live-search="true"  class="form-control selectpicker" id="buildService">
                                            <c:forEach var="task" items="${view.value}" varStatus="vs">
                                                <option <c:choose><c:when test="${vs.first}">selected</c:when></c:choose> value="${task.id}">${task.name}</option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                    <div class="form-group">
                                        <div class="advance-format-item">
                                            <p class="advance-format-title" onclick="toggleBlock(this)">点击修改构建分支</p>
                                            <div class="advance-format-content">
                                                <ul>
                                                    <li><span>orderService</span>:<input class="form-control" type="text" value="master"></li>
                                                    <li><span>orderService</span>:<input class="form-control" type="text" value="master"></li>
                                                    <li><span>orderService</span>:<input class="form-control" type="text" value="master"></li>
                                                    <li><span>orderService</span>:<input class="form-control" type="text" value="master"></li>
                                                </ul>
                                            </div>
                                        </div>
                                    </div>
                                    <button type="button" onclick="saveBuildTask(${view.key.id})" class="btn btn-success">保存</button>
                                </div>
                            </div>
                        </div>
                        <table class="table table-hover table-striped">
                            <thead>
                            <tr>
                                <th>#</th>
                                <th>任务名</th>
                                <th>服务</th>
                                <th>构建分支</th>
                                <th>最后构建</th>
                                <th>操作</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach var="task" items="${view.value}" varStatus="vs">
                                <tr>
                                    <th scope="row">${vs.index+1}</th>
                                    <td>build-${task.name}-${view.key.name}</td>
                                    <td>${task.name}</td>
                                    <td>
                                        <div class="advance-format-item">
                                            <p class="advance-format-title" onclick="toggleBlock(this)">master</p>
                                            <div class="advance-format-content">
                                                <ul>
                                                    <li><span>orderService</span>:<input type="text" value=""></li>
                                                    <li><span>orderService</span>:<input type="text" value=""></li>
                                                    <li><span>orderService</span>:<input type="text" value=""></li>
                                                    <li><span>orderService</span>:<input type="text" value=""></li>
                                                </ul>
                                            </div>
                                        </div>
                                    </td>
                                    <td>2018-09-20</td>
                                    <td><a>构建</a></td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </div>
            </c:forEach>
        </div>
    </div>
</div>
</body>
</html>
