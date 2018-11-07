$(document).ready(function () {
    initDeployUnits();
    initViewSetSelect();
    initViewHostSelect();
    initViewServiceSelect();
});
var deploy = new api.Deploy();
var bsTable = {};

function initDeployUnits() {
    var queryUrl = basePath + '/api/deploy-units';
    var table = new BSTable("deploy-unit-table", queryUrl, setColumns());
    table.onDblClickRow = function (row) {
        viewDeployUnitOrEditByID(row.id, VIEW)
    };
    table.params = function (ps) {
        ps.setId = $("#setSelectView").find("option:selected").val();
        ps.hostId = $("#hostSelectView").find("option:selected").val();
        ps.serviceId = $("#serviceSelectView").find("option:selected").val();
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
        checkbox: true,
        visible: true//是否显示复选框
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

/**
 * 批量修改tag
 */
modifyBatchTag = function () {
    var selected = bsTable.getAllSelections();
    var ids = [];
    if (selected.length !== 0) {
        $.each(selected, function (index, em) {
            ids.push(em.id);
        });
        bodyAbs();
        var modifyBatchTagInputId = "modifyBatchTagInputId";
        var modifyBatchPublishTagInputId = "modifyBatchPublishTagInputId";
        layer.open({
            type: 1,
            title: '填写新的发布/镜像tag',
            area: ['300px', 'auto'],
            content: deploy.exportModifyBatchTagContent(modifyBatchPublishTagInputId, modifyBatchTagInputId),
            btn: ['修改', '取消'],
            yes: function (index, layero) {
                var tag = $("#" + modifyBatchTagInputId).val();
                var publishTag = $("#" + modifyBatchPublishTagInputId).val();
                if (tag !== undefined && tag !== "") {
                    var url = basePath + "/api/deploy-unit/modify-batch";
                    var p = JSON.stringify({tag: tag, publishTag: publishTag, ids: ids});

                    var settings = {
                        type: "post",
                        url: url,
                        data: p,
                        dataType: "json",
                        contentType: "application/json"
                    };
                    $.ajax(settings).done(function (res) {
                        if (res.code === SUCCESS_CODE) {
                            showMessage(SUCCESS, res.msg, "修改成功");
                            bsTable.refresh();
                            layer.close(index);
                        } else {
                            showMessage(ERROR, res.msg, "修改失败");
                        }
                    });
                } else {
                    layer.msg("请填写tag！");
                }
            }, btn2: function (index, layero) {
                layer.msg("操作取消");
            }, cancel: function () {
                layer.msg("操作取消");
            }
        });
        rmBodyAbs();
    } else {
        showMessage(ERROR, "未选中任何数据", "警告")
    }
};


openAddDeployUnitModle = function () {
    // 导出弹窗内容模版
    var context = deploy.exportAddDeployUnitContext("add");
    // 初始化弹窗
    initModelContext(context, function () {
        bsTable.refresh();
    });
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
            closeModel();
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
        initModelContext(context, function () {
            bsTable.refresh();
        });
        initSetList(res.context.setId);
        initHostList(res.context.hostId);
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
            closeModel();
        }
    });
};

delDeployUnit = function (id) {
    bodyAbs();
    layer.confirm('确定删除？', {
        btn: ['确认', '取消']
    }, function () {
        var url = basePath + "/api/deploy-unit/del/" + id;
        $.post(url, function (res) {
            layer.msg(res.msg);
            bsTable.refresh();
        }, "json");
        rmBodyAbs();
    }, function () {
        layer.msg("未做任何改动");
        rmBodyAbs();
    });
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
    var curl = basePath + "/api/deploy-hosts?sort=name&order=asc&extra=1";
    var ss = new BzSelect(curl, "hostSelectView", "id", "name");
    ss.responseHandler = function (res) {
        return res.context.content
    };
    ss.refresh = true;
    ss.init();
};

var initViewServiceSelect = function () {
    var curl = basePath + "/api/deploy-services?sort=name&order=asc";
    var ss = new BzSelect(curl, "serviceSelectView", "id", "name");
    ss.responseHandler = function (res) {
        return res.context.content
    };
    ss.refresh = true;
    ss.init();
};

var viewUnitSetChanged = function () {
    //initViewHostSelect();
    bsTable.refresh();
};
var viewUnitHostChanged = function () {
    bsTable.refresh();
};
var viewUnitServiceChanged = function () {
    bsTable.refresh();
};


/**
 * 初始化set
 * @constructor
 */

initSetList = function (id) {
    var curl = basePath + "/api/deploy-sets?sort=name&order=asc";
    var ss = new BzSelect(curl, "setSelect", "id", "name");
    ss.v_selected = id;
    ss.after = function () {
        if (id === undefined || id === "") {
            initHostList();
            addUnitSetChanged();
        } else {
            addUnitSetEnvInit(id);
        }
    };
    ss.responseHandler = function (res) {
        return res.context.content
    };
    ss.init();
};

/**
 * 初始化host/根据选择的set
 * @constructor
 */
initHostList = function (id) {
    var setSelected = $("#setSelect").find("option:selected").val();
    var curl = basePath + "/api/deploy-hosts/" + setSelected;
    if (id !== undefined && id !== "") {
        curl = basePath + "/api/deploy-hosts?sort=name&order=asc";
    }
    var ss = new BzSelect(curl, "hostSelect", "id", "name");
    ss.v_selected = id;
    ss.after = function () {
        addUnitHostChanged();
    };
    ss.responseHandler = function (res) {
        return res.context
    };
    if (id !== undefined && id !== "") {
        ss.responseHandler = function (res) {
            return res.context.content
        };
    }
    ss.init();
};

/**
 * 初始化服务
 * @constructor
 */
initServiceList = function (id) {
    var curl = basePath + "/api/deploy-services?sort=name&order=asc";
    var ss = new BzSelect(curl, "serviceSelect", "id", "name");
    ss.v_selected = id;
    ss.after = function () {
        addUnitServiceChanged();
    };
    ss.responseHandler = function (res) {
        return res.context.content
    };
    ss.init();
};

/**
 * 当环境集选择变更时
 * @param obj
 */
setChanged = function () {
    initHostList();
};


/**
 * 增加环境集变更时，重新获取变量信息
 * @param obj
 */
addUnitSetChanged = function () {
    var setId = $("#setSelect").find("option:selected").val();
    if (setId !== '0') {
        initHostList();
        addUnitSetEnvInit(setId);
    } else {
        $("#setEnvView").html("无");
    }
};

addUnitSetEnvInit = function (setId) {
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
addUnitHostChanged = function () {
    var hostId = $("#hostSelect").find("option:selected").val();
    if (hostId !== '0') {
        var url = basePath + "/api/deploy-host/" + hostId;
        $.get(url, function (res) {
            if (res.code === SUCCESS_CODE) {
                $("#hostEnvView").html(deploy.hostCnfigView(res.context));
            }
        }, "json");
    } else {
        $("#hostEnvView").html("无");
    }
};

/**
 * 服务变化，重新获取变量信息
 */
addUnitServiceChanged = function (obj) {
    var serviceId = $("#serviceSelect").find("option:selected").val();
    if (serviceId !== '0') {
        var url = basePath + "/api/deploy-service/" + serviceId;
        $.get(url, function (res) {
            if (res.code === SUCCESS_CODE) {
                $("#serviceEnvView").html(deploy.serviceCnfigView(res.context));
            }
        }, "json");
    } else {
        $("#serviceEnvView").html("无");
    }
};

