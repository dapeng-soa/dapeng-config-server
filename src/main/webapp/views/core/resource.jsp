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

<%--selectpicker--%>
<link rel="stylesheet" href="${basePath}/plugins/selectpicker/css/bootstrap-select.css">
<script src="${basePath}/plugins/selectpicker/js/bootstrap-select.js"></script>

<%--toastr--%>
<script src="${basePath}/plugins/toastr/js/toastr.min.js"></script>
<link rel="stylesheet" href="${basePath}/plugins/toastr/css/toastr.css">


<script src="${basePath}/js/api/Config.js"></script>
<script src="${basePath}/plugins/model.js"></script>

<script>

    window.basePath = "${basePath}";

    layui.use('element', function(){
        var element = layui.element;

        //一些事件监听
        element.on('nav(filter)', function(elem){
            console.log(elem); //得到当前点击的DOM对象
        });

        element.on('collapse(filter)', function(data){
            console.log(data.show); //得到当前面板的展开状态，true或者false
            console.log(data.title); //得到当前点击面板的标题区域DOM对象
            console.log(data.content); //得到当前点击面板的内容区域DOM对象
        });
    });

    window.layer = {};

    layui.use('layer', function(){
        window.layui = layui.layer;
    });

    window.refresh = function(){
        window.location.reload();
    };

    window.toggleBlock = function (a) {
        $(a).next(".advance-format-content").toggle();
    };
    window.SUCCESS_CODE = 200;
    window.ERROR_CODE = 4004;
</script>