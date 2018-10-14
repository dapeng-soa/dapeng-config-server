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
    <script src="${basePath}/js/api/system-me.js"></script>
</head>
<body>
<jsp:include page="../core/sidebar.jsp"/>
<div class="container-right">
    <div class="container-right-context animated fadeIn">
        <div class="panel-header">
            <div class="input-group">
                <p class="left-panel-title">账户信息</p>
                <span class="input-group-btn panel-button-group">
                </span>
            </div>
        </div>
        <div class="row">
            <div class="col-sm-5" style="border-right:1px solid #ccc">
                <h4 style="text-align: center;margin-top: 20px">基本信息</h4>
                <div class="form-horizontal" style="margin-top: 20px">
                    <div class="form-group">
                        <label class="col-sm-3 control-label">用户名:</label>
                        <div class="col-sm-9 ">
                            <p class="form-control-static">${me.username}</p>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label">昵称:</label>
                        <div class="col-sm-9">
                            <p class="form-control-static">${me.nickname}</p>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label">邮箱:</label>
                        <div class="col-sm-9">
                            <p class="form-control-static">${me.email}</p>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label">电话:</label>
                        <div class="col-sm-9">
                            <p class="form-control-static">${me.tel}</p>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label">备注:</label>
                        <div class="col-sm-9">
                            <p class="form-control-static">${me.remark}</p>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-sm-7">
                <h4 style="text-align: center;margin-top: 20px">修改密码</h4>
                <div class="form-horizontal" style="margin-top: 20px">
                    <div class="form-group">
                        <label class="col-sm-2 control-label">旧密码:</label>
                        <div class="col-sm-9">
                            <input type="hidden" id="userName" value="${me.username}">
                            <input type="password" id="oldPwd" class="form-control">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">新密码:</label>
                        <div class="col-sm-9">
                            <input type="password" id="newPwd" class="form-control">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">确认密码:</label>
                        <div class="col-sm-9">
                            <input type="password" id="confirmNewPwd" class=" form-control">
                        </div>
                    </div>
                    <span class="input-group-btn panel-button-group text-center">
                    <button type="button" class="btn btn-success" onclick="changePwd()">确认修改</button>
                    </span>
                </div>
            </div>
        </div>
        <jsp:include page="../core/footer.jsp"/>
    </div>
</div>
</body>
</html>
