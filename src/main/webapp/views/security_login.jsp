<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <jsp:include page="core/resource.jsp"/>
</head>
<body>
<div class="container animated fadeIn">
    <form id="login-from" name='login' action='${basePath}/login' method='POST'>
        <h2 class="text-center" style="margin-bottom: 30px">大鹏配置中心</h2>
        <div class="form-group">
            <label >用户名:</label>
            <input type="text" class="form-control"  name="username" value="" placeholder="Username">
        </div>
        <div class="form-group">
            <label >密码:</label>
            <input type="password" class="form-control"  name="password" value="" placeholder="Password">
        </div>
        <input style="width: 100%" type="submit" class="btn btn-success" value="登录"/>
    </form>

</div>
</body>
</html>
