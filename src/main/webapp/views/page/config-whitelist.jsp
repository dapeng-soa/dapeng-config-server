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
    <script src="${basePath}/js/api/white-list.js"></script>
</head>
<body>
<jsp:include page="../core/model.jsp"/>
<jsp:include page="../core/header.jsp"/>
<jsp:include page="../core/sidebar.jsp"/>
<div class="container-right">
    <div class="container-right-context animated fadeIn">
        <div class="panel-header">
            <div class="input-group">
                <p class="left-panel-title">网关服务白名单管理</p>
                <span class="input-group-btn panel-button-group">
                    <%--<button type="button" class="btn btn-success" onclick="InitWhiteList()">刷新</button>--%>
                </span>
            </div>
        </div>

        <div class="row" style="margin-top: 40px" >

            <div class="col-xs-5">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <h3 class="panel-title">白名单列表</h3>
                        <div class="panel-btn-group-left">
                            <i class="fa fa-refresh" style="cursor: pointer" onclick="InitWhiteList()" aria-hidden="true"></i>
                        </div>
                    </div>
                    <div class="panel-body" >
                        <ul class="list-group"  id="white-list-group">
                        </ul>
                    </div>
                </div>
            </div>

            <div class="col-xs-7">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <h3 class="panel-title">新增白名单</h3>
                    </div>
                    <div class="panel-body">
                        <div class="form-group">
                            <textarea class="form-control" rows="15" id="white-list-text"></textarea>
                            <br>
                            <code>tip:使用服务全限定名,回车换行即可一次配置多个!</code>
                        </div>
                        <span class="input-group-btn panel-button-group">
                            <button class="btn btn-primary" onclick="addWhiteItem()">添加</button>
                        </span>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</body>