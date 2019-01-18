var oldTime = new Date().getTime();
var newTime = new Date().getTime();
var outTime = 8 * 60 * 1000;

$(document).ready(function () {
    /* 鼠标移动事件 */
    $(document).mouseover(function () {
        oldTime = new Date().getTime();
    });
    function OutTime() {
        newTime = new Date().getTime();
        if (newTime - oldTime > outTime) {
            showReconnection('因长时间未操作已断开连接，是否重连？');
        }
    }
    window.setInterval(OutTime, 5000);

    var socket = io(socketUrl);
    // 打开终端
    socket.on(SOC_CONNECT, function () {
        if (socket.connected) {
            /**
             * 注册
             */
            socket.emit(WEB_REG, SOCKET_REG_INFO);
            // 初始化
            var request = new api.CmdRequest();
            request.sourceClientId = socket.id;
            request.ip = getRequest().host;
            request.containerId = getRequest().containerId;
            request.width = $("#Terminal").width();
            request.height = $("#Terminal").height();
            request.data = "";
            socket.emit(CMD_EVENT, JSON.stringify(request));
            showMessage(SUCCESS, "容器终端会话连接成功");
        }
    });
    var term = new Terminal({
        useStyle: true,
        convertEol: true,
        screenKeys: true,
        cursorBlink: false,
        visualBell: true,
        colors: Terminal.xtermColors
    });
    term.open(document.getElementById('Terminal'), true);
    term.attach(socket);
    term.focus();
    term.fit();
    term.on('data', function (data) {
        var request = new api.CmdRequest();
        request.sourceClientId = socket.id;
        request.ip = getRequest().host;
        request.containerId = getRequest().containerId;
        request.width = $("#Terminal").width();
        request.height = $("#Terminal").height();
        request.data = data;
        socket.emit(CMD_EVENT, JSON.stringify(request));
    });
    socket.on(CMD_RESP, function (data) {
        term.write(data);
    });
    socket.on(CMD_EXITED, function (data) {
        showReconnection('已退出终端，是否重连？');
    })
});

/**
 * 重连
 */
function showReconnection(msg) {
    layer.confirm(msg, {
        btn: ['刷新重连', '关闭终端'],
        closeBtn: false
    }, function (index) {
        layer.close(index);
        window.refresh();
    }, function () {
        // 自己结束自己的生命
        var index = parent.layer.getFrameIndex(window.name);
        parent.layer.close(index);
    });
}

function getRequest() {
    var url = location.search;
    var theRequest = {};
    if (url.indexOf("?") !== -1) {
        var str = url.substr(1);
        var strs = str.split("&");
        for (var i = 0; i < strs.length; i++) {
            theRequest[strs[i].split("=")[0]] = unescape(strs[i].split("=")[1]);
        }
    }
    return theRequest;
}
