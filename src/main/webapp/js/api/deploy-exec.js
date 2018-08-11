var socket = {};
var yaml = "";
var serviceNum = 0; // 当前的服务数量
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
    initSetList();
    socket.on(NODE_EVENT, function (data) {
        deploy.consoleView(data);
    });
    /**
     * 获取服务更新时间返回
     */
    socket.on(GET_SERVER_INFO_RESP, function (data) {
        // 将匹配页面元素的状态变化
        deploy.processServiceStatus(JSON.parse(data));
    });

    /**
     * 获取yaml配置返回
     */
    socket.on(GET_YAML_FILE_RESP, function (data) {
        yaml += data + "\n";
    });

    /**
     * 升级返回
     */
    socket.on(DEPLOY_RESP, function (data) {
        deploy.consoleView(data);
    });

    /**
     * 停止返回
     */
    socket.on(STOP_RESP, function (data) {
        deploy.consoleView(data);
    });

    /**
     * 重启返回
     */
    socket.on(RESTART_RESP, function (data) {
        deploy.consoleView(data);
    });

    /**
     * 错误返回
     */
    socket.on(ERROR_EVENT, function (data) {
        layer.msg(data);
        openConloseView();
        deploy.consoleView(data,ERROR);
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
 * 同步每个agent上服务时间和服务状态
 */
emitGetInfoEvent = function () {
    var url = basePath + "/api/deploy-units";
    $.get(url, function (res) {
        if (res.code === SUCCESS_CODE) {
            serviceNum = res.context.length;
            var eventReps = [];
            $.each(res.context, function (idx, ement) {
                var event_url = basePath + "/api/deploy-unit/event_rep/" + ement.id;
                util.$get(event_url, function (res2) {
                    if (res.code === SUCCESS_CODE) {
                        eventReps.push(res2.context);
                        if (serviceNum === eventReps.length) {
                            socket.emit(GET_SERVER_INFO, JSON.stringify(eventReps));
                        }
                    }
                });
            });
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
    var line = $("#line");
    if (ob.hasClass("opened")) {return}
    ob.animateCss("fadeInRight").addClass("opened");
    ob.css({top: "0", right: 0 + "px"});
    line.css({position: "fixed"});

};

closeConloseView = function () {
    var ob = $("#consoleView");
    var line = $("#line");
    if (ob.hasClass("opened")) {
        ob.removeClass("opened");
        ob.css({
            top: "5px",
            right: -(ob.width() + Number(ob.css("padding-left").replace("px", "")) + Number(ob.css("padding-right").replace("px", ""))) + "px",
        });
        line.css({position: "absolute"});
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
 * 视图类型变更
 * @param obj
 */
execViewTypeChanged = function (obj) {
    // 发送获取各个节点的服务信息(时间信息)的事件
    emitGetInfoEvent();
    var selected = obj === 1 ? 1 : Number($("#viewType").find("option:selected").val());
    if (selected === 1) {
        $("#viewTypeLabel").html("服务：");
        $("#viewTypeSelect").html(
            '<select id="serviceSelect" data-live-search="true" class="selectpicker form-control" onchange="execServiceChanged()" >' +
            '</select>'
        );
        // 初始化服务
        initServiceList();
        checkService();
    } else {
        $("#viewTypeLabel").html("主机：");
        $("#viewTypeSelect").html(
            '<select id="hostSelect" data-live-search="true" class="selectpicker form-control" onchange="execHostChanged()" >' +
            '</select>'
        );
        // 初始化主机
        initHostList();
        checkService();
    }

};

/**
 * 环境集改变
 */
execSetChanged = function () {
    execViewTypeChanged();
};
// 服务视图服务选择
execServiceChanged = function () {
    checkService();
    emitGetInfoEvent();
};

// 主机视图主机选择
execHostChanged = function () {
    checkService();
    emitGetInfoEvent();
};

/**
 * 升级前预览yaml
 * @param deployTime
 * @param updateTime
 * @param unitId
 * @param viewType
 */
serviceYamlPreview = function (deployTime, updateTime, unitId, viewType) {
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
                    var context = deploy.viewDeployYamlContext(deployTime, updateTime, unitId, viewType);
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
checkService = function () {
    var viewType = Number($("#viewType").find("option:selected").val());
    var setId = $("#setSelect").find("option:selected").val();
    var serviceId = $("#serviceSelect").find("option:selected").val();
    var hostId = $("#hostSelect").find("option:selected").val();

    var url = basePath + "/api/deploy/checkRealService?setId=" + setId + "&serviceId=" + (serviceId === undefined ? 0 : serviceId) + "&hostId=" + (hostId === undefined ? 0 : hostId) + "&viewType=" + viewType;
    util.$get(url, function (res) {
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
            //bodyAbs();
            layer.confirm('停止服务[' + res.context.ip + ':' + res.context.serviceName + ']?', {
                btn: ['停止', '取消']
            }, function () {
                socket.emit(STOP, JSON.stringify(res.context));
                layer.msg("操作已发送");
            }, function () {
                layer.msg("操作取消");
            });
        }
    });
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
            //bodyAbs();
            layer.confirm('重启服务[' + res.context.ip + ':' + res.context.serviceName + ']?', {
                btn: ['重启', '取消']
            }, function () {
                socket.emit(RESTART, JSON.stringify(res.context));
                layer.msg("操作已发送");
            }, function () {
                layer.msg("操作取消");
            });
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
downloadYaml = function (unitId) {
    window.open(basePath + "/api/deploy-unit/download-yml/"+unitId);
    layer.msg("下载成功");
};

slideLine = function (e) {
    var body = document.body, oconsoleView = document.getElementById("consoleView"),
        oLine = document.getElementById("line");
    var disX = (e || event).clientX;
    oLine.width = oLine.offsetWidth;
    document.onmousemove = function (e) {
        var iT = oLine.width + ((e || event).clientX - disX);
        var e = e || window.event, tarnameb = e.target || e.srcElement;
        var maxT = body.clientWidth - oLine.offsetWidth;
        var base = (maxT - disX);
        if (iT > maxT) {
            iT = maxT
        }
        oLine.style.right = (base - oLine.width - iT) < (300 - oLine.width) ? 300 - oLine.width : (base - oLine.width - iT) + "px";
        oconsoleView.style.width = base - iT + "px";
        return false
    };
    document.onmouseup = function () {
        document.onmousemove = null;
        document.onmouseup = null;
        oLine.releaseCapture && oLine.releaseCapture()
    };
    oLine.setCapture && oLine.setCapture();
    return false
};


