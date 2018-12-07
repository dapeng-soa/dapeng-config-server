var $$ = new api.Api();
var build = new api.Build();
var socket = {};

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
    var deployHostId = $("#deployHost" + hostId).val();
    var serviceDoms = $("#dependsService" + hostId).find("input[type='hidden'].build-depend-name");
    var buildDependBranchs = $("#dependsService" + hostId).find("input.build-depend-branch");
    var buildService = {
        serviceId: serviceId,
        deployHostId: deployHostId,
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

var execBuildService = function (taskId, hostId) {
    var url = basePath + "/api/build/exec-build/" + taskId;
    $$.post(url, {}, function (res) {
        if (res.code === SUCCESS_CODE) {
            socket.emit(BUILD, JSON.stringify(res.context));
            layer.msg("正在构建");
            getTaskBuildListReq(hostId, taskId)
        } else {
            layer.msg(res.msg);
        }
    })
};

var delBuildTask = function (taskId) {
    bodyAbs();
    layer.confirm('确定删除此任务？这将会将关联日志一并清空', {
        btn: ['确认', '取消']
    }, function () {
        var url = basePath + "/api/build/del-build/" + taskId;
        $$.post(url, {}, function (res) {
            if (res.code === SUCCESS_CODE) {
                layer.msg(res.msg);
                refresh();
            } else {
                layer.msg(res.msg);
            }
        });
        rmBodyAbs();
    }, function () {
        layer.msg("未做任何改动");
        rmBodyAbs();
    });
};

var hostSelectChange = function () {
    console.log("333")
};

var getBuildListReq = function (hostId) {
    var url = basePath + "/api/build/get-building-list";
    $.get(url, {hostId: hostId}, function (res) {
        getBuildingListHtml(hostId, res.context);
    }, "json")
};
var getTaskBuildListReq = function (hostId, taskId) {
    var url = basePath + "/api/build/building-history-byTask/" + taskId;
    $.get(url, {}, function (res) {
        getBuildingListHtml(hostId, res.context);
    }, "json")
};

var getBuildingListHtml = function (elId, records) {
    var context = build.buildingListContext(records);
    $("#buildingList" + elId).html(context);
};