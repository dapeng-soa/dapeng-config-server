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
    <link rel="stylesheet" href="${basePath}/plugins/xterm/xterm.css">
    <script src="${basePath}/plugins/xterm/xterm.js"></script>
    <script src="${basePath}/plugins/xterm/addons/fit/fit.js"></script>
    <script src="${basePath}/plugins/xterm/addons/attach/attach.js"></script>
    <script src="${basePath}/js/api/terminal.js"></script>
    <script src="${basePath}/js/ts/Mapper.js"></script>
</head>
<body>
<div style="height: 100%;" id="Terminal">

</div>
</body>
</html>
