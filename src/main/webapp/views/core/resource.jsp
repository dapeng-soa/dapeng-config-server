<%--
  Created by IntelliJ IDEA.
  User: craneding
  Date: 15/9/29
  Time: 下午1:12
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    request.setAttribute("basePath", request.getContextPath());
    request.setAttribute("socketUrl", System.getenv("deploy_socket_url"));
%>
<title>大鹏配置中心</title>
<meta charset="utf-8"/>
<meta name="author" content="struy">
<meta name="description" content="dapeng 配置中心">
<meta name="keywords" content="dapeng,thrift,zookeeper,netty,redis,mysql">
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
<meta name="format-detection" content="telephone=no">
<meta name="format-detection" content="email=no">
<link rel="shortcut icon" href="${basePath}/images/favicon.ico" type="image/x-icon"/>
<link rel="stylesheet" href="${basePath}/css/default.css" type="text/css"/>
<!-- 新 Bootstrap 核心 CSS 文件 -->
<link rel="stylesheet" href="${basePath}/css/bootstrap/3.3.5/bootstrap.min.css">
<!-- 可选的Bootstrap主题文件（一般不用引bootstrap-theme.min.css入） -->
<link rel="stylesheet" href="${basePath}/css/bootstrap/3.3.5/bootstrap-theme.min.css">
<link rel="stylesheet" href="${basePath}/css/model/scroll-top.css" type="text/css"/>
<!-- jQuery文件。务必在bootstrap.min.js 之前引入 -->
<script src="${basePath}/js/jquery/1.11.3/jquery.min.js"></script>
<!-- 最新的 Bootstrap 核心 JavaScript 文件 -->
<script src="${basePath}/js/bootstrap/3.3.5/bootstrap.min.js"></script>
<%--layui--%>
<link href="${basePath}/plugins/layui/css/layui.css" rel="stylesheet">
<script src="${basePath}/plugins/layui/layui.js"></script>

<%--bootstrap-table--%>
<link href="${basePath}/css/table/bootstrap-table.min.css" rel="stylesheet">
<script src="${basePath}/js/table/bootstrap-table.min.js"></script>
<script src="${basePath}/js/table/bootstrap-table-zh-CN.min.js"></script>

<link href="${basePath}/plugins/font-awesome-4.7.0/css/font-awesome.min.css" rel="stylesheet">
<%--animate--%>
<link href="${basePath}/plugins/animate/animate.min.css" rel="stylesheet">

<%--selectpicker 选择框--%>
<link rel="stylesheet" href="${basePath}/plugins/selectpicker/css/bootstrap-select.css">
<script src="${basePath}/plugins/selectpicker/js/bootstrap-select.js"></script>

<%--toastr 通知--%>
<script src="${basePath}/plugins/toastr/js/toastr.min.js"></script>
<link rel="stylesheet" href="${basePath}/plugins/toastr/css/toastr.css">

<!-- Requires CodeMirror -->
<script type="text/javascript" src="${basePath}/plugins/codemirror/codemirror.min.js"></script>
<script type="text/javascript" src="${basePath}/plugins/codemirror/searchcursor.min.js"></script>
<link type="text/css" rel="stylesheet" href="${basePath}/plugins/codemirror/codemirror.min.css" />

<%--mergely diff--%>
<script src="${basePath}/plugins/mergely/mergely.js"></script>
<link rel="stylesheet" href="${basePath}/plugins/mergely/mergely.css">

<script src="${basePath}/js/ts/Config.js"></script>
<script src="${basePath}/js/ts/Deploy.js"></script>
<script src="${basePath}/js/ts/Api.js"></script>
<script src="${basePath}/plugins/socketIo/socket.io.js"></script>
<script src="${basePath}/plugins/model.js"></script>
<script src="${basePath}/plugins/init.js"></script>
<script>
    window.basePath = "${basePath}";
    window.socketUrl = "${socketUrl}"
</script>