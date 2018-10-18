<%--
  Created by IntelliJ IDEA.
  User: craneding
  Date: 15/9/29
  Time: 下午2:10
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="true" %>
<html lang="zh-CN">
<head>
    <%
        request.setAttribute("DEFAULT_TITLE", "未找到页面");
    %>
    <jsp:include page="../core/resource.jsp"/>
</head>
<body>
<div class="container" style="margin-top: 40px">
    <div class="jumbotron">
        <h1>请求出现未知错误: ${error}</h1>
        <p>${message},http status: ${status},request path: ${path}</p>
        <p><a class="btn btn-primary btn-lg" href="/" role="button">Learn more</a></p>
    </div>
</div>
<jsp:include page="../core/footer.jsp"/>
</body>
</html>
