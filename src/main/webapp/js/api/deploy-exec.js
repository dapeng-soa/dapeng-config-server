var socket = {};
var yaml = "";
$(document).ready(function () {
    initSetList();
    setTimeout(function () {
        execViewTypeChanged(1);
    }, 200);
    socket = io(socketUrl);
    socket.on(SOC_CONNECT, function () {
        if (socket.connected) {
            socket.emit(WEB_REG, SOCKET_REG_INFO);
            showMessage(SUCCESS, "已建立与服务器的连接", "连接成功");
            emitGetTimeEvent(socket);
        }
    });
    socket.on(NODE_EVENT, function (data) {
        openConloseView();
        deploy.consoleView(data);
        setTimeout(function () {
            closeConloseView();
        }, 5000);
    });
    socket.on(GET_SERVER_TIME_RESP, function (data) {
        deploy.consoleView(data);

        checkService(data);
    });

    socket.on(GET_YAML_FILE_RESP, function (data) {
        yaml += data + "\n";
    });
    socket.on(SOC_CDISCONNECT, function () {
        showMessage(ERROR, "已断开与服务器的连接", "断开连接");
    });

    socket.on(CONNECT_ERROR, function () {
        showMessage(ERROR, "与服务器建立连接失败", "连接失败");
    });

    socket.on(CONNECT_TIMEOUT, function () {
        showMessage(ERROR, "与服务器建立连接失败", "连接超时");
    });
});
var deploy = new api.Deploy();
var util = new api.Api();
/**
 * 同步每个agent上服务时间
 * @param soc
 */
emitGetTimeEvent = function (soc) {
    var url = basePath + "/api/deploy-services";
    $.get(url, function (res) {
        if (res.code === SUCCESS_CODE) {
            $.each(res.context, function (idx, ement) {
                soc.emit(GET_SERVER_TIME, ement.name);
            })
        }
    }, "json");
};

toggleConloseView = function () {
    var ob = $("#consoleView");
    if (!ob.hasClass("opened")) {
        openConloseView();
    } else {
        closeConloseView();
    }
};
openConloseView = function () {
    var ob = $("#consoleView");
    ob.removeClass("closed");
    ob.animateCss("fadeInRight").addClass("opened");
};

closeConloseView = function () {
    var ob = $("#consoleView");
    if (ob.hasClass("opened")) {
        ob.removeClass("opened");
        ob.animateCss("fadeOutRight").addClass("closed");
    }
};


/**
 * 初始化set
 * @constructor
 */
initSetList = function (id) {
    var curl = basePath + "/api/deploy-sets";
    util.$get(curl, function (res) {
        if (res.code === SUCCESS_CODE) {
            var html = "";
            for (var i = 0; i < res.context.length; i++) {
                var seled = "";
                if (i === 0) {
                    seled = "selected"
                }
                if (id !== undefined && id !== "") {
                    seled = res.context[i].id === id ? "selected" : "";
                }
                html += '<option ' + seled + ' value="' + res.context[i].id + '">' + res.context[i].name + '</option>';
            }
            $("#setSelect").html(html).selectpicker('refresh');
        }
    });
};

/**
 * 初始化服务
 * @constructor
 */
initServiceList = function (id) {
    var curl = basePath + "/api/deploy-services";
    $.get(curl, function (res) {
        if (res.code === SUCCESS_CODE) {
            var html = "";
            for (var i = 0; i < res.context.length; i++) {
                var seled = "";
                if (id !== undefined && id !== "") {
                    seled = res.context[i].id === id ? "selected" : "";
                }
                html += '<option ' + seled + ' value="' + res.context[i].id + '">' + res.context[i].name + '</option>';
            }
            $("#serviceSelect").html(html).selectpicker('refresh');
        }
    }, "json");
};

initHostList = function (id) {
    var setSelected = $("#setSelect").find("option:selected").val();
    var curl = basePath + "/api/deploy-hosts/" + setSelected;
    $.get(curl, function (res) {
        if (res.code === SUCCESS_CODE) {
            var html = "";
            for (var i = 0; i < res.context.length; i++) {
                var seled = "";
                if (id !== undefined && id !== "") {
                    seled = res.context[i].id === id ? "selected" : "";
                }
                html += '<option seled value="' + res.context[i].id + '">' + res.context[i].name + '</option>';
            }
            $("#hostSelect").html(html).selectpicker('refresh');
        }
    }, "json");
};

// 环境集改变
execSetChanged = function () {
    checkService();
};
// 视图类型变更
execViewTypeChanged = function (obj) {
    var selected = obj === 1 ? 1 : Number($(obj).find("option:selected").val());
    if (selected === 1) {
        $("#viewTypeLabel").html("服务：");
        $("#viewTypeSelect").html(
            '<select id="serviceSelect" data-live-search="true" class="selectpicker form-control" onchange="execServiceChanged()" >' +
            '</select>'
        );
        // 初始化服务
        initServiceList();
        // 切换至服务视图
        checkService();
    } else {
        $("#viewTypeLabel").html("主机：");
        $("#viewTypeSelect").html(
            '<select id="hostSelect" data-live-search="true" class="selectpicker form-control" onchange="execHostChanged()" >' +
            '</select>'
        );
        // 初始化主机
        initHostList();
        // 切换至主机视图
        checkService();
    }

};


// 服务视图服务选择
execServiceChanged = function () {
    checkService();
};

// 主机视图主机选择
execHostChanged = function () {
    checkService();
};

// 升级前
serviceYamlPreview = function (unitId, viewType) {
    closeConloseView();

    var event_url = basePath + "/api/deploy-unit/event_rep/" + unitId;
    util.$get(event_url, function (res) {
        if (res.code === SUCCESS_CODE) {
            yaml = "";
            socket.emit(GET_YAML_FILE, JSON.stringify(res.context));

            var url = basePath + "/api/deploy-unit/process-envs/" + unitId;
            util.$get(url, function (res2) {
                if (res2.code === SUCCESS_CODE) {
                    // 导出弹窗内容模版
                    var context = deploy.viewDeployYamlContext(unitId, viewType);
                    initModelContext(context, function () {
                        //refresh()
                    });
                    setTimeout(function () {
                        diffTxt(res2.context.fileContent, yaml)
                    }, 300);
                }
            });
        }
    });
};


// checkService
checkService = function (realTime) {
    var serviceTimes = {};
    if (realTime !== undefined && realTime !== "") {
        serviceTimes = JSON.parse(realTime)
    }
    var viewType = Number($("#viewType").find("option:selected").val());
    var setId = $("#setSelect").find("option:selected").val();
    var serviceId = $("#serviceSelect").find("option:selected").val();
    var hostId = $("#hostSelect").find("option:selected").val();

    var url = basePath + "/api/deploy/checkRealService?setId=" + setId + "&serviceId=" + (serviceId === undefined ? 0 : serviceId) + "&hostId=" + (hostId === undefined ? 0 : hostId) + "&viewType=" + viewType;
    util.$get(url, function (res) {
        // 对比
        console.log(res.context);
        console.log(serviceTimes);
        if (viewType === 1) {
            if (serviceTimes !== {}) {
                $.each(serviceTimes, function (i, em) {
                    console.log("===>"+em.ip + "---" + em.serviceName);
                    $.each(res.context, function (i2, em2) {
                        if (em2.serviceName === em.serviceName) {
                            $.each(em2.deploySubHostVos, function (i3, em3) {
                                console.log("unitId " +em3.unitId+ "---" + em3.hostIp);
                                if (em3.hostIp === em.ip) {
                                    var log = "compare service:"+em2.serviceName+"==ip:"+em3.hostIp+"==unitId:"+em3.unitId+"==configUpdateBy:"+em3.configUpdateBy+"==realDeployTime:"+em.time;
                                    console.log(log);
                                    deploy.consoleView(log);
                                    em3.deployTime = em.time;
                                    em3.needUpdate = em3.deployTime < em3.configUpdateBy
                                }
                            })
                        }
                    })
                })
            }
        } else {

        }
        // 展示视图（默认服务视图）
        var context = deploy.deployViewChange(viewType, res.context);
        $("#deployMain").html(context);
    })
};

// stopService
stopService = function (unitId) {
    var url = basePath + "/api/deploy/stopRealService";
    util.post(url, {unitId: unitId}, function (res) {
        if (res.code === SUCCESS_CODE) {
            socket.emit(STOP, JSON.stringify(res.context));
        }
    })
};

// restartService
restartService = function (unitId) {
    var url = basePath + "/api/deploy/restartRealService";
    util.post(url, {unitId: unitId}, function (res) {
        if (res.code === SUCCESS_CODE) {
            socket.emit(RESTART, JSON.stringify(res.context));
        }
    })
};

/**
 * 执行升级
 */
execServiceUpdate = function (unitId) {
    var url = basePath + "/api/deploy/updateRealService";
    var req = {
        unitId: unitId
    };
    util.get(url, req, function (res) {
        layer.msg(res.msg);
        if (res.code === SUCCESS_CODE) {
            socket.emit(DEPLOY, JSON.stringify(res.context));
            closeModel();
        }
    });
};

cancelServiceUpdate = function () {
    layer.msg("取消升级");
    closeModel();
};

downloadYaml = function () {
    layer.msg("下载成功");
};


