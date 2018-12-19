var $$ = new api.Api();
var build = new api.Build();
var socket = {};
var hostTimer = -1;
var taskTimer = -1;


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

    initBuildSetList();
    initBuildServiceList();
    getBuildTasks();
    initTask();
});

initTask = function () {
    window.clearInterval(hostTimer);
    window.clearInterval(taskTimer);
    getBuildListReq();
    hostTimer = setInterval(function () {
        getBuildListReq();
    }, 4000);
};

/**
 * 初始化set
 * @constructor
 */
initBuildSetList = function () {
    var curl = basePath + "/api/deploy-sets?sort=name&order=asc";
    var ss = new BzSelect(curl, "setSelect", "id", "name");
    ss.refresh = true;
    ss.responseHandler = function (res) {
        return res.context.content
    };
    ss.init();
};

/**
 * 初始化服务
 * @constructor
 */
initBuildServiceList = function () {
    var curl = basePath + "/api/deploy-services?sort=name&order=asc";
    var filterUrl = basePath + "/api/deploy-unit/filter-services?";
    var setSelected = $("#setSelect").find("option:selected").val();
    if (setSelected !== undefined && Number(setSelected) !== 0) {
        curl = filterUrl + "setId=" + setSelected;
    }
    var ss = new BzSelect(curl, "serviceSelect", "id", "name");
    ss.responseHandler = function (res) {
        if (setSelected !== undefined && Number(setSelected) !== 0) {
            return res.context
        }
        return res.context.content
    };
    ss.refresh = true;
    ss.init();
};

/**
 * 环境集改变
 */
execBuildSetChanged = function () {
    initBuildServiceList();
    getBuildTasks();
};
// 服务视图服务选择
execBuildServiceChanged = function () {
    getBuildTasks();
};

getBuildTasks = function () {
    var url = basePath + "/api/build/build-tasks";
    var setSelected = $("#setSelect").find("option:selected").val();
    var serviceId = $("#serviceSelect").find("option:selected").val();
    $$.get(url, {
        setId: setSelected,
        serviceId: serviceId
    }, function (res) {
        if (res.code === SUCCESS_CODE) {
            var context = build.buildTasksContext(res.context);
            $("#buildTaskTable").html(context);
        }
    });
    initTask();
};


var execBuildService = function (taskId) {
    var url = basePath + "/api/build/exec-build/" + taskId;
    $$.post(url, {}, function (res) {
        if (res.code === SUCCESS_CODE) {
            socket.emit(BUILD, JSON.stringify(res.context));
            layer.msg("正在构建");
            getBuildListReq();
        } else {
            layer.msg(res.msg);
        }
    })
};

var getBuildListReq = function () {
    var url = basePath + "/api/build/get-building-list";
    var setSelected = $("#setSelect").find("option:selected").val();
    var serviceId = $("#serviceSelect").find("option:selected").val();
    $.get(url, {setId: setSelected, serviceId: serviceId}, function (res) {
        getBuildingListHtml(res.context);
    }, "json")
};
var getTaskBuildListReq = function (taskId) {
    var url = basePath + "/api/build/building-history-byTask/" + taskId;
    $.get(url, {}, function (res) {
        getBuildingListHtml(res.context);
    }, "json")
};


var getBuildingListHtml = function (records) {
    var context = build.buildingListContext(records);
    $("#buildingList").html(context);
};

var getTaskBuildList = function (taskId) {
    window.clearInterval(hostTimer);
    window.clearInterval(taskTimer);
    getTaskBuildListReq(taskId);
    taskTimer = setInterval(function () {
        getTaskBuildListReq(taskId);
    }, 4000)
};