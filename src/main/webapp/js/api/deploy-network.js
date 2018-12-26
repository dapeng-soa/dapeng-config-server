var socket = {};
var net = new api.Network();
var $$ = new api.Api();
var bsTable = {};
var netHostTable = {};
var ansi = new AnsiUp;

$(document).ready(function () {
    initNetwork();
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

    socket.on(SYNC_NETWORK_RESP, function (data) {
        if (data !== "") {
            var html = ansi.ansi_to_html(data);
            $$.consoleView(html);
        }
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
 * 查看网络
 * @param id
 * @param op
 */
function viewNetworkOrEditByID(id, op) {
    var url = basePath + "/api/deploy-network/" + id;
    $.get(url, function (res) {
        var context = net.exportAddNetworkContext(op, '', res.context);
        initModelContext(context, function () {
            bsTable.refresh();
        });
    }, "json");
}

/**
 * 删除网络
 * @param id
 */
function delNetwork(id) {
    layer.confirm('确定删除？', {
        btn: ['确认', '取消']
    }, function (index) {
        var url = basePath + "/api/deploy-network/del/" + id;
        $.post(url, function (res) {
            showMessage(INFO, res.msg);
            layer.close(index);
            bsTable.refresh();
        }, "json");
    }, function () {
        showMessage(WARN, "未做任何改动");
    });
}

/**
 * 打开关联节点
 */
function openLinkDeployHosts(netId, name) {
    var context = net.exportNetLinkHostContext(netId, name);
    initModelContext(context, function () {
        bsTable.refresh();
    });
    initDeployHosts(netId);
}

function initDeployHosts(id) {
    var queryUrl = basePath + '/api/deploy-hosts';
    var table = new BSTable("net-host-table", queryUrl, setHostColumns());
    table.responseHandler = function (res) {
        return {
            total: res.context == null ? 0 : res.context.totalElements,
            rows: res.context.content
        };
    };

    table.onCheck = function (row) {
        linkingHost(id, [row.id])
    };
    table.onUncheck = function (row) {
        unLinkingHost(id, [row.id])
    };
    table.onCheckAll = function (rows) {
        linkingHost(id, getHostIds(rows))
    };
    table.onUncheckAll = function (rows) {
        unLinkingHost(id, getHostIds(rows))
    };
    table.onLoadSuccess = function () {
        var url = basePath + "/api/deploy-network-hosts/" + id;
        $.get(url, function (res) {
            if (res.code === SUCCESS_CODE) {
                var rids2 = [];
                for (var i in res.context) {
                    rids2.push(res.context[i].hostId);
                }
                table.checkBy("id", rids2);
            }
        }, "json");
    };

    table.params = function (ps) {
        ps.setId = $("#setSelectView").find("option:selected").val();
        ps.tag = $("#deployHostTags").find("option:selected").val();
        return ps;
    };
    table.init();
    netHostTable = table;
}

getHostIds = function (rows) {
    var rids = [];
    for (var i in rows) {
        rids.push(rows[i].id);
    }
    return rids;
};


setHostColumns = function () {
    return [{
        checkbox: true,
        visible: true//是否显示复选框
    }, {
        field: 'id',
        title: '#',
        formatter: indexFormatter

    }, {
        field: 'name',
        title: '节点名',
        sortable: true
    }, {
        field: 'ip',
        title: '节点host'
    }, {
        field: 'setName',
        title: '所属环境集'
    }, {
        field: 'labels',
        title: '标签',
        sortable: true,
        align: 'center',
        valign: 'middle',
        formatter: tagsFormatter
    }, {
        field: 'extra',
        title: '外部机器',
        formatter: extraFormatter,
        align: 'center',
        valign: 'middle'
    }]
};

extraFormatter = function (value, row, index) {
    switch (value) {
        case 0:
            return '<span class="label label-success">是</span>';
        case 1:
            return '<span class="label label-danger">否</span>';
        default:
            return '<span class="label label-success">是</span>';
    }
};


function initNetwork() {
    var queryUrl = basePath + '/api/deploy-networks';
    var table = new BSTable("deploy-network-table", queryUrl, setColumns());
    table.onDblClickRow = function (row) {
        viewNetworkOrEditByID(row.id, VIEW)
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

var setColumns = function () {
    return [{
        checkbox: false,
        visible: false//是否显示复选框
    }, {
        field: 'id',
        title: '#',
        formatter: indexFormatter

    }, {
        field: 'networkName',
        title: '网络名称',
        sortable: true
    }, {
        field: 'driver',
        title: '网桥(默认bridge)',
        sortable: true
    }, {
        field: 'subnet',
        title: '网段'
    }, {
        field: 'opt',
        title: 'opt'
    }, {
        field: 'createdAt',
        title: '添加时间',
        sortable: true
    }, {
        field: 'updatedAt',
        title: '修改时间',
        sortable: true
    }, {
        field: 'id',
        title: '操作',
        width: 160,
        align: 'center',
        valign: 'middle',
        formatter: netWorkActionFormatter
    }]
};

linkingHost = function (netId, hostIds) {
    var url = basePath + "/api/deploy-network/link-host";
    $$.post(url, JSON.stringify({netId: netId, hostIds: hostIds}), function (res) {
        showMessage(INFO, res.msg);
    }, "application/json");
};

unLinkingHost = function (netId, hostIds) {
    var url = basePath + "/api/deploy-network/unlink-host";
    $$.post(url, JSON.stringify({netId: netId, hostIds: hostIds}), function (res) {
        showMessage(INFO, res.msg);
    }, "application/json");
};


openAddNetWorkModle = function () {
    var context = net.exportAddNetworkContext(ADD);
    initModelContext(context, function () {
        bsTable.refresh();
    });
};

var netWorkActionFormatter = function (value, row) {
    return net.exportNetWorkAction(value, "network:" + row.networkName);
};

saveNetWork = function () {
    var url = basePath + "/api/deploy-network/add";
    $$.post(url, JSON.stringify(processNetWorkData()), function (res) {
        showMessage(INFO, res.msg);
        if (res.code === SUCCESS_CODE) {
            closeModel();
        }
    }, "application/json");
};

processNetWorkData = function () {
    var networkName = $("#networkName").val();
    var driver = $("#driver").val();
    var subnet = $("#subnet").val();
    var opt = $("#opt").val();
    return {
        networkName: networkName,
        driver: driver,
        subnet: subnet,
        opt: opt
    }
};

clearNetWorkInput = function () {

};

/**
 * 同步网络至各个绑定的节点
 */
sysNetworkToHost = function (netId, name) {
    layer.confirm('确定将网络配置:' + name + '同步至绑定节点?', {
        btn: ['确认', '取消']
    }, function (index) {
        var url = basePath + "/api/deploy-network/sync-network/" + netId;
        $.get(url, function (res) {
            socket.emit(SYNC_NETWORK, JSON.stringify(res.context));
        }, "json");
        openConloseView();
        showMessage(SUCCESS, "操作已发送");
        layer.close(index);
    }, function () {
        showMessage(WARN, "未做任何改动");
    });
};


editedNetWork = function (id) {
    var url = basePath + "/api/deploy-network/edit/" + id;
    $$.post(url, JSON.stringify(processNetWorkData()), function (res) {
        showMessage(INFO, res.msg);
        if (res.code === SUCCESS_CODE) {
            closeModel();
        }
    }, "application/json");
};