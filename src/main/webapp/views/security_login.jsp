<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <jsp:include page="core/resource.jsp"/>
</head>
<style>
html,body{
    padding: 0;
    margin: 0;
    color: #FFF;
}
html,body{
    background: url(${basePath}/images/bg.jpg) no-repeat center center fixed;
    -webkit-background-size: cover;
    -moz-background-size: cover;
    -o-background-size: cover;
    background-size: cover;
}
</style>
<body>
<div class="container animated fadeIn">
    <form id="login-from" name='login' action='${basePath}/login' method='POST'>
        <%--<div class="sidebar-cover" style="margin-top: 10px">--%>
            <%--<span>--%>
                <%--<img src="${basePath}/images/favicon.png">--%>
            <%--</span>--%>
        <%--</div>--%>
        <h2 class="text-center" style="margin-bottom: 30px;color: #222">大鹏配置中心</h2>
        <div class="form-group">
            <label style="color: #222">用户名:</label>
            <input type="text" class="form-control" name="username" value="" placeholder="Username">
        </div>
        <div class="form-group">
            <label style="color: #222">密码:</label>
            <input type="password" class="form-control" name="password" value="" placeholder="Password">
        </div>
        <input style="width: 100%" type="submit" class="btn btn-success" value="登录"/>
    </form>
    <div style="position: absolute;bottom: 0;left: 0;right: 0">
        <jsp:include page="core/footer.jsp"/>
    </div>
</div>
</body>
</html>
