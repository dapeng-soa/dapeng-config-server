$(document).ready(function () {
    initSetList();
    setTimeout(function () {
        execViewTypeChanged(1);
    }, 200);
});
var deploy = new api.Deploy();
var util = new api.Api();

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
        console.log(res);
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
execSetChanged = function (obj) {
    var viewType = Number($("#viewType").find("option:selected").val());
    checkService(viewType);
};
// 视图类型变更
execViewTypeChanged = function (obj) {
    var selected = obj===1?1:Number($(obj).find("option:selected").val());
    if (selected === 1) {
        $("#viewTypeLabel").html("服务：");
        $("#viewTypeSelect").html(
            '<select id="serviceSelect" data-live-search="true" class="selectpicker form-control" onchange="execServiceChanged()" >' +
            '</select>'
        );
        // 初始化服务
        initServiceList();
        // 切换至服务视图
        checkService(selected);
    } else {
        $("#viewTypeLabel").html("主机：");
        $("#viewTypeSelect").html(
            '<select id="hostSelect" data-live-search="true" class="selectpicker form-control" onchange="execHostChanged()" >' +
            '</select>'
        );
        // 初始化主机
        initHostList();
        // 切换至主机视图
        checkService(selected);
    }

};


// 服务视图服务选择
execServiceChanged = function () {
    var viewType = Number($("#viewType").find("option:selected").val());
    checkService(viewType);
};

// 主机视图主机选择
execHostChanged = function () {
    var viewType = Number($("#viewType").find("option:selected").val());
    checkService(viewType);
};

// 升级前
updateService = function (unitId, viewType) {
    var url = basePath + "/api/deploy-unit/process-envs/" + unitId;
    util.$get(url, function (res) {
        if (res.code === SUCCESS_CODE) {
            // 导出弹窗内容模版
            var context = deploy.viewDeployYamlContext(unitId, viewType);
            initModelContext(context, function () {
                refresh()
            });
            diffTxt(res.context.fileContent,res.context.fileContent)
        }
    });
};
// checkService
checkService = function (viewType) {
    var setId = $("#setSelect").find("option:selected").val();
    var serviceId = $("#serviceSelect").find("option:selected").val();
    var hostId = $("#hostSelect").find("option:selected").val();

    var url = basePath + "/api/deploy/checkRealService?setId=" + setId + "&serviceId=" + (serviceId===undefined?0:serviceId) + "&hostId=" + (hostId===undefined?0:hostId) + "&viewType=" + viewType;
    util.$get(url, function (res) {
        // 展示视图（默认服务视图）
        var context = deploy.deployViewChange(viewType, res.context);
        $("#deployMain").html(context);
    })
};

// stopService
stopService = function (unitId) {
    var url = basePath + "/api/deploy/stopRealService";
    util.$get(url, function (res) {
        layer.msg(res.msg);
    })
};

// restartService
restartService = function (unitId) {
    var url = basePath + "/api/deploy/restartRealService";
    util.$get(url, function (res) {
        layer.msg(res.msg);
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


