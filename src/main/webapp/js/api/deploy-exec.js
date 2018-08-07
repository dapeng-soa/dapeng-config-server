var socket = {};
var yaml = "";
var serviceNum = 0; // 当前的服务数量
var realService = []; //缓存服务时间事件问询的数据
$(document).ready(function () {
    openConloseView();
    socket = io(socketUrl);
    socket.on(SOC_CONNECT, function () {
        if (socket.connected) {
            socket.emit(WEB_REG, SOCKET_REG_INFO);
            showMessage(SUCCESS, "已建立与服务器的连接", "连接成功");
        }
    });
    initSetList();
    socket.on(NODE_EVENT, function (data) {
        deploy.consoleView(data);
        setTimeout(function () {
            closeConloseView();
        }, 10000);
    });
    socket.on(GET_SERVER_TIME_RESP, function (data) {
        deploy.consoleView(data);
        realService.push(JSON.parse(data)[0]);
        if (realService.length === serviceNum) {
            setTimeout(function () {
                checkService(realService);
            }, 100);
        }
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
emitGetTimeEvent = function () {
    var url = basePath + "/api/deploy-services";
    $.get(url, function (res) {
        if (res.code === SUCCESS_CODE) {
            realService = [];
            serviceNum = res.context.length;
            $.each(res.context, function (idx, ement) {
                socket.emit(GET_SERVER_TIME, ement.name);
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
        ob.addClass("closed");
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
            execViewTypeChanged(1);
        }
    });
};

/**
 * 初始化服务
 * @constructor
 */
initServiceList = function () {
    var curl = basePath + "/api/deploy-services";
    $.get(curl, function (res) {
        if (res.code === SUCCESS_CODE) {
            var html = "<option selected value='0'>请选择</option>";
            for (var i = 0; i < res.context.length; i++) {
                html += '<option  value="' + res.context[i].id + '">' + res.context[i].name + '</option>';
            }
            $("#serviceSelect").html(html).selectpicker('refresh');
        }
    }, "json");
};

initHostList = function () {
    var setSelected = $("#setSelect").find("option:selected").val();
    var curl = basePath + "/api/deploy-hosts/" + setSelected;
    $.get(curl, function (res) {
        if (res.code === SUCCESS_CODE) {
            var html = "<option selected value='0'>请选择</option>";
            for (var i = 0; i < res.context.length; i++) {
                html += '<option  value="' + res.context[i].id + '">' + res.context[i].name + '</option>';
            }
            $("#hostSelect").html(html).selectpicker('refresh');
        }
    }, "json");
};

/**
 * 环境集改变
 */
execSetChanged = function () {
    execViewTypeChanged();
};
/**
 * 视图类型变更
 * @param obj
 */
execViewTypeChanged = function (obj) {
    // 发送获取各个节点的服务信息(时间信息)的事件
    emitGetTimeEvent();
    var selected = obj === 1 ? 1 : Number($("#viewType").find("option:selected").val());
    if (selected === 1) {
        $("#viewTypeLabel").html("服务：");
        $("#viewTypeSelect").html(
            '<select id="serviceSelect" data-live-search="true" class="selectpicker form-control" onchange="execServiceChanged()" >' +
            '</select>'
        );
        // 初始化服务
        initServiceList();
    } else {
        $("#viewTypeLabel").html("主机：");
        $("#viewTypeSelect").html(
            '<select id="hostSelect" data-live-search="true" class="selectpicker form-control" onchange="execHostChanged()" >' +
            '</select>'
        );
        // 初始化主机
        initHostList();
    }

};


// 服务视图服务选择
execServiceChanged = function () {
    emitGetTimeEvent();
};

// 主机视图主机选择
execHostChanged = function () {
    emitGetTimeEvent();
};

/**
 * 升级前预览yaml
 * @param unitId
 * @param viewType
 */
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


/**
 * 比较节点服务配置文件时间与数据库配置更新时间,校验是否需要更新
 * @param realTime
 */
checkService = function (realTime) {
    var serviceTimes = {};
    if (realTime !== undefined && realTime !== "") {
        serviceTimes = realTime
    }
    var viewType = Number($("#viewType").find("option:selected").val());
    var setId = $("#setSelect").find("option:selected").val();
    var serviceId = $("#serviceSelect").find("option:selected").val();
    var hostId = $("#hostSelect").find("option:selected").val();

    var url = basePath + "/api/deploy/checkRealService?setId=" + setId + "&serviceId=" + (serviceId === undefined ? 0 : serviceId) + "&hostId=" + (hostId === undefined ? 0 : hostId) + "&viewType=" + viewType;
    util.$get(url, function (res) {
        if (viewType === 1) {
            if (serviceTimes !== {}) {
                $.each(serviceTimes, function (i, em) {
                    $.each(res.context, function (i2, em2) {
                        if (em2.serviceName === em.serviceName) {
                            $.each(em2.deploySubHostVos, function (i3, em3) {
                                if (em3.hostIp === em.ip) {
                                    var log = "compare service:" + em2.serviceName + "==ip:" + em3.hostIp + "==unitId:" + em3.unitId + "==configUpdateBy:" + em3.configUpdateBy + "==realDeployTime:" + em.time;
                                    deploy.consoleView(log);
                                    em3.deployTime = em.time;
                                    em3.needUpdate = em3.deployTime < em3.configUpdateBy
                                    em3.deployTime = unix2Time(em3.deployTime);
                                    em3.configUpdateBy = unix2Time(em3.configUpdateBy);
                                }
                            })
                        }
                    })
                })
            }
        } else {
            if (serviceTimes !== {}) {
                $.each(serviceTimes, function (i, em) {
                    $.each(res.context, function (i2, em2) {
                        if (em2.hostIp === em.ip) {
                            $.each(em2.deploySubServiceVos, function (i3, em3) {
                                if (em3.serviceName === em.serviceName) {
                                    var log = "compare service:" + em3.serviceName + "==ip:" + em2.hostIp + "==unitId:" + em3.unitId + "==configUpdateBy:" + em3.configUpdateBy + "==realDeployTime:" + em.time;
                                    deploy.consoleView(log);
                                    em3.deployTime = em.time;
                                    em3.needUpdate = em3.deployTime < em3.configUpdateBy
                                    em3.deployTime = unix2Time(em3.deployTime);
                                    em3.configUpdateBy = unix2Time(em3.configUpdateBy);
                                }
                            })
                        }
                    })
                })
            }
        }
        // 展示视图（默认服务视图）
        var context = deploy.deployViewChange(viewType, res.context);
        $("#deployMain").html(context);
    })
};
/**
 * 停止服务
 * stopService
 * @param unitId
 */
stopService = function (unitId) {
    var url = basePath + "/api/deploy/stopRealService";
    util.post(url, {unitId: unitId}, function (res) {
        if (res.code === SUCCESS_CODE) {
            socket.emit(STOP, JSON.stringify(res.context));
        }
    })
};

/**
 * 重启服务
 * restartService
 * @param unitId
 */
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
        if (res.code === SUCCESS_CODE) {
            socket.emit(DEPLOY, JSON.stringify(res.context));
            closeModel();
        }
    });
};
/**
 * 取消升级
 */
cancelServiceUpdate = function () {
    layer.msg("取消升级");
    closeModel();
};

/**
 * 下载yaml
 */
downloadYaml = function () {
    layer.msg("下载成功");
};


