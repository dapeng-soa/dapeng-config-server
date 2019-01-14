$(document).ready(function () {
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
    term.fit();
    term.attach(socket);
    term.focus();
    term.on('data', function (data) {
        var request = new api.CmdRequest();
        request.sourceClientId = socket.id;
        request.ip = getRequest().host;
        request.containerId = getRequest().containerId;
        request.data = data;
        socket.emit(CMD_EVENT, JSON.stringify(request));
    });
    socket.on(CMD_RESP, function (data) {
        term.write(data);
    });
    socket.on(CMD_EXITED, function (data) {
        console.log(data);
        layer.confirm('已退出终端，是否重连？', {
            btn: ['刷新重连', '关闭终端']
        }, function (index) {
            layer.close(index);
            window.refresh();
        }, function () {
            open("/deploy/exec", '_self');
        });
    })
});

function getRequest () {
    var url = location.search;
    var theRequest = {};
    if (url.indexOf("?") !== -1) {
        var str = url.substr(1);
        var strs = str.split("&");
        for(var i = 0; i < strs.length; i ++) {
            theRequest[strs[i].split("=")[0]] = unescape(strs[i].split("=")[1]);
        }
    }
    return theRequest;
}
