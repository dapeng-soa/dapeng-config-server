$(document).ready(function () {
    initSetList();
    initServiceList();
    setTimeout(function () {
        InitDeployUnits();
    }, 100);
});
var deploy = new api.Deploy();
var sets = {};
var hosts = {};
var services = {};

function InitDeployUnits() {
    var queryUrl = basePath + '/api/deploy-units';
    var table = new BSTable("deploy-unit-table", queryUrl, setColumns());
    table.onDblClickRow = function (row) {
        viewDeployUnitOrEditByID(row.id, VIEW)
    };
    table.init();
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
        field: 'setName',
        title: 'set',
        sortable: true
    }, {
        field: 'hostName',
        title: 'host'
    }, {
        field: 'serviceName',
        title: 'service',
        sortable: true
    }, {
        field: 'gitTag',
        title: '发布tag',
        sortable: true
    }, {
        field: 'imageTag',
        title: '镜像tag',
        sortable: true
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
        formatter: deployUnitActionFormatter
    }]
};
/**
 * @return {string}
 */
deployUnitActionFormatter = function (value, row, index) {
    return deploy.exportDeployUnitActionContext(value, row);
};


openAddDeployUnitModle = function () {
    // 导出弹窗内容模版
    var context = deploy.exportAddDeployUnitContext("add");
    // 初始化弹窗
    initModelContext(context, refresh);
    initSetList();
    initServiceList();
};

/**
 * 保存
 */
saveDeployUnit = function () {
    var url = basePath + "/api/deploy-unit/add";
    var settings = {
        type: "post",
        url: url,
        data: JSON.stringify(processDeployUnitData()),
        dataType: "json",
        contentType: "application/json"
    };
    $.ajax(settings).done(function (res) {
        layer.msg(res.msg);
        if (res.code === SUCCESS_CODE) {
            refresh();
        }
    });
};

/**
 * 清空配置
 */
clearDeployUnitInput = function () {
    bodyAbs();
    layer.confirm('将清空当前所有输入？', {
        btn: ['确认', '取消']
    }, function () {
        $("textarea.form-control").val("");
        layer.msg("已清空");
    }, function () {
        layer.msg("取消清空");
    });
};

processDeployUnitData = function () {
    var gitTag = $("#gitTag").val();
    var setSelect = $("#setSelect").find("option:selected").val();
    var hostSelect = $("#hostSelect").find("option:selected").val();
    var serviceSelect = $("#serviceSelect").find("option:selected").val();
    var imageTag = $("#imageTag").val();
    var env = $("#env-area").val();
    var volumes = $("#volumes-area").val();
    var ports = $("#ports-area").val();
    var dockerExtras = $("#dockerExtras-area").val();
    return {
        gitTag: gitTag,
        setId: setSelect,
        hostId: hostSelect,
        serviceId: serviceSelect,
        imageTag: imageTag,
        env: env,
        volumes: volumes,
        ports: ports,
        dockerExtras: dockerExtras
    }
};

/**
 * 修改
 * @param id
 * @param op
 */
viewDeployUnitOrEditByID = function (id, op) {
    var url = basePath + "/api/deploy-unit/" + id;
    $.get(url, function (res) {
        // 导出弹窗内容模版
        var context = deploy.exportAddDeployUnitContext(op, "", res.context);
        // 初始化弹窗
        initModelContext(context, refresh);
        initSetList(res.context.setId);
        setTimeout(function () {
            initHostList(res.context.hostId)
        }, 100);
        initServiceList(res.context.serviceId)
    }, "json");
};

/**
 * 修改apikey
 * @param id
 */
editedDeployUnit = function (id) {
    var url = basePath + "/api/deploy-unit/edit/" + id;

    var settings = {
        type: "post",
        url: url,
        data: JSON.stringify(processDeployUnitData()),
        dataType: "json",
        contentType: "application/json"
    };
    $.ajax(settings).done(function (res) {
        layer.msg(res.msg);
        if (res.code === SUCCESS_CODE) {
            refresh();
        }
    });
};

delDeployUnit = function () {
    layer.msg("暂无权限")
};


/**
 * 初始化set
 * @constructor
 */
initSetList = function (id) {
    var curl = basePath + "/api/deploy-sets";
    $.get(curl, function (res) {
        if (res.code === SUCCESS_CODE) {
            sets = res.context;
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
            $("#setSelect").html(html);
            if (id === undefined || id === "") {
                initHostList()
            }
            addUnitSetChanged();
        }
    }, "json");
};

/**
 * 当环境集选择变更时
 * @param obj
 */
setChanged = function (obj) {
    var setId = $(obj).find("option:selected").val();
    initHostList(setId);
};

/**
 * 初始化host/根据选择的set
 * @constructor
 */
initHostList = function (id) {
    var setSelected = $("#setSelect").find("option:selected").val();
    var curl = basePath + "/api/deploy-hosts/" + setSelected;
    $.get(curl, function (res) {
        if (res.code === SUCCESS_CODE) {
            hosts = res.context;
            var html = "";
            for (var i = 0; i < res.context.length; i++) {
                var seled = "";
                if (id !== undefined && id !== "") {
                    seled = res.context[i].id === id ? "selected" : "";
                }
                html += '<option ' + seled + ' value="' + res.context[i].id + '">' + res.context[i].name + '</option>';
            }
            $("#hostSelect").html(html);
            addUnitHostChanged();
        }
    }, "json");
};

/**
 * 初始化服务
 * @constructor
 */
initServiceList = function (id) {
    var curl = basePath + "/api/deploy-services";
    $.get(curl, function (res) {
        if (res.code === SUCCESS_CODE) {
            services = res.context;
            var html = "";
            for (var i = 0; i < res.context.length; i++) {
                var seled = "";
                if (id !== undefined && id !== "") {
                    seled = res.context[i].id === id ? "selected" : "";
                }
                html += '<option ' + seled + ' value="' + res.context[i].id + '">' + res.context[i].name + '</option>';
            }
            $("#serviceSelect").html(html);
            addUnitServiceChanged();
        }
    }, "json");
};


/**
 * 增加环境集变更时，重新获取变量信息
 * @param obj
 */
addUnitSetChanged = function (obj) {
    var setId = $("#setSelect").find("option:selected").val();
    initHostList(setId);
    var url = basePath + "/api/deploy-set/" + setId;
    $.get(url, function (res) {
        if (res.code === SUCCESS_CODE) {
            $("#setEnvView").html(deploy.setCnfigView(res.context));
        }
    }, "json");
};
/**
 * host 变化，重新获取变量信息
 */
addUnitHostChanged = function (obj) {
    var hostId = $("#hostSelect").find("option:selected").val();
    var url = basePath + "/api/deploy-host/" + hostId;
    $.get(url, function (res) {
        if (res.code === SUCCESS_CODE) {
            $("#hostEnvView").html(deploy.hostCnfigView(res.context));
        }
    }, "json");
};

/**
 * 服务变化，重新获取变量信息
 */
addUnitServiceChanged = function (obj) {
    var serviceId = $("#serviceSelect").find("option:selected").val();
    var url = basePath + "/api/deploy-service/" + serviceId;
    $.get(url, function (res) {
        if (res.code === SUCCESS_CODE) {
            $("#serviceEnvView").html(deploy.serviceCnfigView(res.context));
        }
    }, "json");
};

