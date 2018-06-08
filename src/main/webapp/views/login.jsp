<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <jsp:include page="core/resource.jsp"/>
</head>
<body>
<div class="container animated fadeIn">
    <form id="login-from" name='login-from' action='${basePath}/login' method='post'>
        <h2 class="text-center" style="margin-bottom: 30px">大鹏配置中心</h2>
        <div class="form-group">
            <label for="username">用户名:</label>
            <input type="text" class="form-control" id="username" name="username" placeholder="Username">
        </div>
        <div class="form-group">
            <label for="password">密码:</label>
            <input type="password" class="form-control" id="password" name="password" placeholder="Password">
        </div>
        <button style="width: 100%" type="submit" class="btn btn-success">登录</button>
        <%--<input name="_csrf" type="hidden" value="2108c289-a721-4927-b555-2a089aefe58f" />--%>
    </form>

</div>
</body>
</html>
