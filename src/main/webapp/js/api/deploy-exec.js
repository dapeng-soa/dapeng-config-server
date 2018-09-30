var socket = {};
var yaml = "";
var serviceNum = 0; // 当前的服务数量
var deploy = new api.Deploy();
var $$ = new api.Api();
var diff = {};
var ansi = new AnsiUp;
$(document).ready(function () {
    window.onbeforeunload = function () {
        return "控制台输出将不被保存,确认要离开此页面吗？"
    };
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
        openConloseView();
        var html = ansi.ansi_to_html(data);
        deploy.consoleView(html);
    });

    /**
     * 停止返回
     */
    socket.on(STOP_RESP, function (data) {
        openConloseView();
        var html = ansi.ansi_to_html(data);
        deploy.consoleView(html);
    });

    /**
     * 重启返回
     */
    socket.on(RESTART_RESP, function (data) {
        openConloseView();
        var html = ansi.ansi_to_html(data);
        deploy.consoleView(html);
    });

    /**
     * 错误返回
     */
    socket.on(ERROR_EVENT, function (data) {
        openConloseView();
        var html = ansi.ansi_to_html(data);
        deploy.consoleView(html);
    });
    /**
     * 监听已注册agent列表
     */
    socket.on(GET_REGED_AGENTS_RESP, function (data) {
        var agents = JSON.parse(data);
        var agentList = deploy.exportAgentList(agents);
        $("#agentList").html(agentList);
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
/**
 * 获取已经注册agent列表
 */
var getRegAgents = function () {
    socket.emit(GET_REGED_AGENTS, "");
    var context = deploy.exportAgentsContext();
    initModelContext(context, function(){});
};
/**
 * 同步每个agent上服务时间和服务状态
 */
emitGetInfoEvent = function () {
    var url = basePath + "/api/deploy-units";
    $.get(url, function (res) {
        if (res.code === SUCCESS_CODE) {
            serviceNum = res.context.content.length;
            var eventReps = [];
            $.each(res.context.content, function (idx, ement) {
                var event_url = basePath + "/api/deploy-unit/event_rep/" + ement.id;
                $$.$get(event_url, function (res2) {
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


/**
 * 初始化set
 * @constructor
 */
initSetList = function () {
    var curl = basePath + "/api/deploy-sets?sort=name&order=asc";
    var ss = new BzSelect(curl, "setSelect", "id", "name");
    ss.refresh = true;
    ss.after = function () {
        execViewTypeChanged(1);
    };
    ss.responseHandler = function (res) {
        return res.context.content
    };
    ss.init();
};

/**
 * 初始化服务
 * @constructor
 */
initServiceList = function () {
    var curl = basePath + "/api/deploy-services?sort=name&order=asc";
    var ss = new BzSelect(curl, "serviceSelect", "id", "name");
    ss.responseHandler = function (res) {
        return res.context.content
    };
    ss.refresh = true;
    ss.init();
};

initHostList = function () {
    var setSelected = $("#setSelect").find("option:selected").val();
    var curl = basePath + "/api/deploy-hosts/" + setSelected;
    if (Number(setSelected) === 0) {
        curl = basePath + "/api/deploy-hosts?sort=name&order=asc"
    }
    var ss = new BzSelect(curl, "hostSelect", "id", "name");
    ss.responseHandler = function (res) {
        if (Number(setSelected) === 0) {
            return res.context.content
        }
        return res.context
    };
    ss.refresh = true;
    ss.init();
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
    execViewTypeChanged()
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
    $$.$get(event_url, function (res) {
        if (res.code === SUCCESS_CODE) {
            yaml = "";
            socket.emit(GET_YAML_FILE, JSON.stringify(res.context));

            var url = basePath + "/api/deploy-unit/process-envs/" + unitId;
            $$.$get(url, function (res2) {
                if (res2.code === SUCCESS_CODE) {
                    // 导出弹窗内容模版
                    var context = deploy.viewDeployYamlContext(deployTime, updateTime, unitId, viewType);
                    initModelContext(context, function () {
                        //refresh()
                    });
                    setTimeout(function () {
                        diff = diffTxt(res2.context.fileContent, yaml)
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
    $$.$get(url, function (res) {
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
    $$.post(url, {unitId: unitId}, function (res) {
        if (res.code === SUCCESS_CODE) {
            bodyAbs();
            layer.confirm('停止服务[' + res.context.ip + ':' + res.context.serviceName + ']?', {
                btn: ['停止', '取消']
            }, function () {
                socket.emit(STOP, JSON.stringify(res.context));
                layer.msg("操作已发送");
                rmBodyAbs();
            }, function () {
                layer.msg("操作取消");
                rmBodyAbs();
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
    $$.post(url, {unitId: unitId}, function (res) {
        if (res.code === SUCCESS_CODE) {
            bodyAbs();
            layer.confirm('重启服务[' + res.context.ip + ':' + res.context.serviceName + ']?', {
                btn: ['重启', '取消']
            }, function () {
                socket.emit(RESTART, JSON.stringify(res.context));
                layer.msg("操作已发送");
                rmBodyAbs();
            }, function () {
                layer.msg("操作取消");
                rmBodyAbs();
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
    $$.get(url, req, function (res) {
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
    window.open(basePath + "/api/deploy-unit/download-yml/" + unitId);
    layer.msg("下载成功");
};


