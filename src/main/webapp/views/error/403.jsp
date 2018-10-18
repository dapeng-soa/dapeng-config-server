<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="true" %>
<html lang="zh-CN">
<head>
    <%
        request.setAttribute("DEFAULT_TITLE", "403页面");
    %>
    <jsp:include page="../core/resource.jsp"/>
</head>
<body>
<jsp:include page="../core/sidebar.jsp"/>
<div class="container">
    <div class="jumbotron">
        <h1>操作权限不足: ${error}</h1>
        <p><p>${message},http status: ${status},request path: ${path}</p></p>
        <p><a class="btn btn-primary btn-lg" href="/me" role="button">Learn more</a></p>
    </div>
</div>


<jsp:include page="../core/footer.jsp"/>
</body>
</html>
