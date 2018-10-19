var $$ = new api.Api();
var build = new api.Build();
var socket = {};
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
        }
    });
    socket.on(NODE_EVENT, function (data) {
        if (data !== "") {
            openConloseView();
            var html = ansi.ansi_to_html(data);
            $$.consoleView(html);
        }
        if (data === "[end]"){
            setTimeout(function () {
                closeConloseView()
            },2000);
        }
    });
    /**
     * 错误返回
     */
    socket.on(ERROR_EVENT, function (data) {
        if (data !== "") {
            openConloseView();
            var html = ansi.ansi_to_html(data);
            $$.consoleView(html);
        }
        if (data === "[end]"){
            setTimeout(function () {
                closeConloseView()
            },2000);
        }
    });

    socket.on(SOC_CDISCONNECT, function () {
        showMessage(ERROR, "已断开与服务器的连接", "断开连接");
    });

    socket.on(CONNECT_ERROR, function () {
        showMessage(ERROR, "与服务器建立连接失败", "连接失败");
    });
});

// 保存构建任务
var saveBuildTask = function (hostId) {
    var serviceId = $("#buildService" + hostId).val();
    var serviceDoms = $("#dependsService" + hostId).find("input[type='hidden'].build-depend-name");
    var buildDependBranchs = $("#dependsService" + hostId).find("input.build-depend-branch");
    var buildService = {
        serviceId: serviceId,
        hostId: hostId,
        taskName: $("#buildTaskName" + hostId).val(),
        buildDepends: []
    };
    if (serviceDoms.length === buildDependBranchs.length) {
        $.each(serviceDoms, function (index, em) {
            var service = $(em).val();
            var branch = $(buildDependBranchs[index]).val();
            var depend = {
                serviceName: service,
                serviceBranch: branch
            };
            buildService.buildDepends.push(depend);
        });
    }

    var url = basePath + "/api/build/add-build-task";
    $$.post(url, JSON.stringify(buildService), function (res) {
        layer.msg(res.msg);
        if (res.code === SUCCESS_CODE) {
            refresh();
        }
    }, "application/json")

};

// 当所选服务变更
var serviceSelectChange = function (obj) {
    // test 获取依赖
    var serviceId = $(obj).val();
    var index = $(obj).data("index");
    var url = basePath + "/api/build/depends/" + serviceId;
    $$.$get(url, function (res) {
        var depends = res.context;
        var context = build.buildTaskDependsContext(depends);
        $("#dependsService" + index).html(context);
    })
};

var execBuildService = function (taskId) {
    var url = basePath + "/api/build/task/" + taskId;
    $$.$get(url, function (res) {
        if (res.code === SUCCESS_CODE) {
            socket.emit(BUILD, JSON.stringify(res.context));
            layer.msg("正在构建");
        } else {
            layer.msg(res.msg);
        }
    })
};