var deploy = new api.Deploy();
var bsTable = {};
var socket = {};
var yaml = "";
var $$ = new api.Api();
$(document).ready(function () {
    initDeployJournals();
    initViewSetSelect();
    initViewHostSelect();
    initViewServiceSelect();
    socket = io(socketUrl);
    socket.on(SOC_CONNECT, function () {
        if (socket.connected) {
            /**
             * 注册
             */
            socket.emit(WEB_REG, SOCKET_REG_INFO);
            showMessage(SUCCESS, "已建立与socket服务器的连接", "连接成功");
        }
    });

    /**
     * 升级返回
     */
    socket.on(DEPLOY_RESP, function (data) {
        var html = ansi.ansi_to_html(data);
        $$.consoleView(html);
    });

    /**
     * 错误返回
     */
    socket.on(ERROR_EVENT, function (data) {
        openConloseView();
        var html = ansi.ansi_to_html(data);
        $$.consoleView(html);
    });

    /**
     * 获取yaml配置返回
     */
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

function initDeployJournals() {
    var queryUrl = basePath + '/api/deploy-journals';
    var table = new BSTable("deploy-journal-table", queryUrl, setColumns());
    table.params = function (ps) {
        ps.setId = $("#setSelectView").find("option:selected").val();
        ps.hostId = $("#hostSelectView").find("option:selected").val();
        ps.serviceId = $("#serviceSelectView").find("option:selected").val();
        ps.opType = $("#opTypeView").find("option:selected").val();
        return ps;
    };
    table.responseHandler = function (res) {
        return {
            total: res.context == null ? 0 : res.context.totalElements,
            rows: res.context.content
        };
    };
    table.init();
    bsTable = table;
}

setColumns = function () {
    return [{
        checkbox: false,
        visible: false//是否显示复选框
    }, {
        field: 'id',
        title: '#',
        formatter: indexFormatter

    }, {
        field: 'gitTag',
        title: '发布tag',
        sortable: true
    }, {
        field: 'serviceName',
        title: 'service'
    }, {
        field: 'imageTag',
        title: '镜像tag'
    }, {
        field: 'opFlag',
        title: '操作',
        align: 'center',
        formatter: flagFormatter
    }, {
        field: 'yml',
        title: 'yml',
        align: 'center',
        formatter: ymlFormatter
    }, {
        field: 'setName',
        title: 'set'
    }, {
        field: 'hostName',
        title: 'host'
    }, {
        field: 'createdAt',
        title: '添加时间',
        sortable: true
    }, {
        field: 'createdBy',
        title: '操作人'
    }, {
        field: 'id',
        title: '操作',
        width: 160,
        align: 'center',
        valign: 'middle',
        formatter: deployJournalActionFormatter
    }]
};

var flagFormatter = function (value, row, index) {
    return deploy.exportDeployJournalFlagContext(value, row);
};

var ymlFormatter = function (value, row, index) {
    return deploy.exportDeployJournalYmlContext(value, row);
};
/**
 * @return {string}
 */
var deployJournalActionFormatter = function (value, row, index) {
    return deploy.exportDeployJournalActionContext(value, row);
};


/**
 * 查看
 * @param id
 */
viewDeployJournal = function (id) {
    var url = basePath + "/api/deploy-journal/" + id;
    $.get(url, function (res) {
        // 导出弹窗内容模版
        var context = deploy.exportViewDeployJournalContext(res.context);
        // 初始化弹窗
        initModelContext(context);
    }, "json");
};

/**
 * 预览yml
 * @param id
 */
viewDeployJournalYml = function (id, hostId, serviceId) {

    var event_url = basePath + "/api/deploy-unit/event_rep";
    $$.get(event_url, {hostId: hostId, serviceId: serviceId}, function (res) {
        if (res.code === SUCCESS_CODE) {
            yaml = "";
            socket.emit(GET_YAML_FILE, JSON.stringify(res.context));

            var url = basePath + "/api/deploy-journal/" + id;
            $$.$get(url, function (res2) {
                if (res2.code === SUCCESS_CODE) {
                    // 导出弹窗内容模版
                    var context = deploy.exportViewDeployJournalContext(id);
                    initModelContext(context, function () {
                    });
                    setTimeout(function () {
                        diffTxt(res2.context.yml, yaml)
                    }, 300);
                }
            });
        }
    });
};

/**
 * 回滚
 * @param jid
 */
rollbackDeploy = function (jid, host, service) {
    var url = basePath + "/api/deploy/rollbackRealService";
    bodyAbs();
    layer.confirm('回滚服务[' + host + ':' + service + ']?', {
        btn: ['确认', '取消']
    }, function () {
        $$.post(url, {jid: jid}, function (res) {
            if (res.code === SUCCESS_CODE) {
                socket.emit(DEPLOY, JSON.stringify(res.context));
                layer.msg("操作已发送");
                rmBodyAbs();
            }
        })
    }, function () {
        layer.msg("操作取消");
    });
};


delDeployJournal = function () {
    layer.msg("暂无权限")
};

var initViewSetSelect = function () {
    var curl = basePath + "/api/deploy-sets?sort=name&order=asc";
    var s1 = new BzSelect(curl, "setSelectView", "id", "name");
    s1.after = function () {
        viewUnitSetChanged();
    };
    s1.responseHandler = function (res) {
        return res.context.content
    };
    s1.refresh = true;
    s1.init();
};

var initViewHostSelect = function () {
    var curl = basePath + "/api/deploy-hosts?sort=name&order=asc";
    var ss = new BzSelect(curl, "hostSelectView", "id", "name");
    ss.refresh = true;
    ss.responseHandler = function (res) {
        return res.context.content
    };
    ss.init();
};

var initViewServiceSelect = function () {
    var curl = basePath + "/api/deploy-services?sort=name&order=asc";
    var ss = new BzSelect(curl, "serviceSelectView", "id", "name");
    ss.refresh = true;
    ss.responseHandler = function (res) {
        return res.context.content
    };
    ss.init();
};


var viewUnitSetChanged = function () {
    bsTable.refresh();
};
var viewUnitHostChanged = function () {
    bsTable.refresh();
};
var viewUnitServiceChanged = function () {
    bsTable.refresh();
};
var viewOpTypeChanged = function () {
    bsTable.refresh();
};


