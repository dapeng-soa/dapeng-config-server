<%--
  Created by IntelliJ IDEA.
  User: struy
  Date: 2018/9/18
  Time: 16:23
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <jsp:include page="../core/resource.jsp"/>
    <script src="${basePath}/js/ts/Build.js"></script>
</head>
<script>
    var socket = {};
    var progressiveDto = {id:${currentId}, start: 0};
    var ansi = new AnsiUp;
    $(document).ready(function () {
        socket = io(socketUrl);
        socket.on(SOC_CONNECT, function () {
            if (socket.connected) {
                /**
                 * 注册
                 */
                socket.emit(WEB_REG, SOCKET_REG_INFO);
                showMessage(SUCCESS, "已建立与服务器的连接", "连接成功");
                socket.emit(GET_BUILD_PROGRESSIVE, JSON.stringify(progressiveDto));
            }
        });

        setInterval(function () {
            socket.emit(GET_BUILD_PROGRESSIVE, JSON.stringify(progressiveDto));
        }, 3000);

        socket.on(GET_BUILD_PROGRESSIVE_RESP, function (data) {
            $("#console-Box").append(ansi.ansi_to_html(data));
            progressiveDto.start = progressiveDto.start + data.length;
        });
    });

    // 获取返回

</script>
<body>
<jsp:include page="../core/sidebar.jsp"/>
<jsp:include page="../core/model.jsp"/>
<div class="container-right">
    <div class="container-right-context">
        <div class="panel-header">
            <div class="input-group">
                <p class="left-panel-title">控制台输出xxx</p>
                <span class="input-group-btn panel-button-group">
                    <%--如果是某一个任务历史点击进入则返回上个页面，如果是直接进入则返回任务主页--%>
                   <button type="button" class="btn btn-primary" onclick="backPrePage()"><i class="fa fa-chevron-left"
                                                                                            aria-hidden="true"></i>返回</button>
                </span>
            </div>
        </div>
    </div>
    <pre id="console-Box" style="background: #FFF"></pre>
    <i class="fa fa-spinner icon-spin" aria-hidden="true"></i>
</div>
</body>
</html>
