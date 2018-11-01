$(document).ready(function () {
    InitMainTable();
    initSelectTags();
});
var config = new api.Config();
var bsTable = {};
var $$ = new api.Api();

function InitMainTable() {
    var queryUrl = basePath + '/api/configs';
    var table = new BSTable("config-table", queryUrl, setColumns());
    table.onDblClickRow = function (row) {
        viewOrEditByID(row.id, VIEW)
    };
    table.params = function (ps) {
        ps.tag = $("#configServiceTags").find("option:selected").val();
        return ps;
    };
    table.responseHandler = function (res) {     //格式化返回数据
        return {
            total: res.context.totalElements,
            rows: res.context.content
        };
    };
    table.init();
    bsTable = table;
}

setColumns = function () {
    return [{
        checkbox: false,
        visible: false                  //是否显示复选框
    }, {
        field: 'id',
        title: '#',
        formatter: indexFormatter
    }, {
        field: 'serviceName',
        title: '服务名',
        sortable: true
    }, {
        field: 'tags',
        title: 'tags',
        formatter: tagsFormatter
    }, {
        field: 'status',
        title: '状态',
        sortable: true,
        align: 'center',
        valign: 'middle',
        formatter: statusFormatter
    }, {
        field: 'remark',
        title: '备注'
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
        formatter: actionFormatter
    }];
};

var initSelectTags = function () {
    var url = basePath + "/api/config/service-tags";
    $$.$get(url, function (res) {
        if (res.code === SUCCESS_CODE) {
            var tags = res.context;
            var ops = "<option value=''>请选择</option>";
            for (var i in tags) {
                ops += "<option value='" + tags[i] + "'>" + tags[i] + "</option>"
            }
            $("#configServiceTags").html(ops).selectpicker('refresh');
        }
    });

};

execTagChanged = function (obj) {
    bsTable.refresh();
};

tagsFormatter = function (value) {
    var tags = value.split(",");
    var res = "";
    for (var i in tags) {
        var lable = RANDOM_LABEL();
        res += '<span class="' + lable + '">' + tags[i] + '</span> ';
    }
    return res;
};


// 格式化状态
statusFormatter = function (value) {
    //0:无效,1:新建,2:审核通过,3:已发布
    //0:danger,1:default,2:primary,3:success
    switch (value) {
        case 0:
            return '<span class="label label-danger">无效</span>';
        case 1:
            return '<span class="label label-default">新建>待审核</span>';
        case 2:
            return '<span class="label label-primary">已审>待发布</span>';
        case 3:
            return '<span class="label label-success">已发布</span>';
        default:
            return '<span class="label label-danger">无效</span>';
    }
};

// 操作格式化
actionFormatter = function (id, row) {
    return config.exportConfigTableActionContext(id, row);
};


/**
 * 保存配置
 */
saveconfig = function () {
    var url = basePath + "/api/config/add";
    var settings = {
        type: "post",
        url: url,
        data: JSON.stringify(processConfigData()),
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
 * 删除配置
 * @param id
 */
delConfig = function (id) {

    bodyAbs();
    layer.confirm('删除此当前配置？', {
        btn: ['删除', '取消']
    }, function () {
        var url = basePath + "/api/config/delete/" + id;
        var settings = {
            type: "post",
            url: url,
            dataType: "json",
            contentType: "application/json"
        };
        $.ajax(settings).done(function (res) {
            layer.msg(res.msg);
            if (res.code === SUCCESS_CODE) {
                closeModel();
            }
            rmBodyAbs();
        });
    }, function () {
        layer.msg("未做任何变更");
        rmBodyAbs();
    });
};

/**
 * 修改配置
 * @param id
 */
editedConfig = function (id) {
    var url = basePath + "/api/config/edit/" + id;
    var settings = {
        type: "post",
        url: url,
        data: JSON.stringify(processConfigData()),
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
 * 发布历史
 * @param id
 */
viewHistory = function (id, serviceName) {
    var url = basePath + "/api/config/publish-history/" + id;
    $.get(url, function (res) {
        if (res.code === SUCCESS_CODE) {
            openPublishHistory(serviceName);
            processHistoryData(res.context);
        }
    }, "json")
};

processHistoryData = function (data) {

    var historyHtml = "";
    for (var i = data.length - 1; i >= 0; i--) {

        historyHtml += '<li class="layui-timeline-item">' +
            '<i class="layui-icon layui-timeline-axis" onclick="toggleHistory(this)" >&#xe63f;</i>' +
            '<div class="layui-timeline-content layui-text">' +
            '<h3 class="layui-timeline-title advance-format-title" onclick="toggleBlock(this)">' + data[i].version + '</h3>' +
            '<div class="advance-format-content">' +
            '<p>发布日期：' + data[i].publishedAt + '</p>' +
            '<p>备注：' + (data[i].remark === "" ? "无" : data[i].remark) + '</p>' +
            '<p>超时配置</p>' +
            '<pre>' + (data[i].timeoutConfig === "" ? "无" : data[i].timeoutConfig) + '</pre>' +
            '<p>负载均衡</p>' +
            '<pre>' + (data[i].loadbalanceConfig === "" ? "无" : data[i].loadbalanceConfig) + '</pre>' +
            '<p>路由配置</p>' +
            '<pre>' + (data[i].routerConfig === "" ? "无" : data[i].routerConfig) + '</pre>' +
            '<p>限流配置</p>' +
            '<pre>' + (data[i].freqConfig === "" ? "无" : data[i].freqConfig) + '</pre>' +
            '</div>' +
            '</div>' +
            '</li>';
    }
    $("#publishHistory").html(historyHtml);


};

toggleHistory = function (obj) {
    $(obj).next().children(".advance-format-title").click();
};

/**
 * 发布配置
 * @param id
 */
publishConfig = function (id) {

    var curl = basePath + "/api/clusters";
    $.get(curl, function (res) {
        if (res.code === SUCCESS_CODE) {
            var html = "<select style='width: 80%;margin: 0 auto' id='nodeSelect' class='form-control'>" +
                "<option value='-1'>全部</option>";
            for (var i = 0; i < res.context.length; i++) {
                html += '<option value="' + res.context[i].id + '">' + res.context[i].zkHost + '</option>';
            }
            html += '</select>';
            bodyAbs();
            // 选择集群发布
            layer.open({
                type: 1,
                title: '请选择需要发布的集群',
                content: html,
                btn: ['确认发布', '取消发布'],
                yes: function (index, layero) {

                    processPublishConfig(id, $("#nodeSelect").find("option:selected").val());

                }, btn2: function (index, layero) {
                    layer.msg("取消发布");
                }, cancel: function () {
                    layer.msg("取消发布");
                    //return false 开启该代码可禁止点击该按钮关闭
                }
            });
            rmBodyAbs();
        }
    }, "json");
};

/**
 * 执行发布
 * @param cid
 */
processPublishConfig = function (id, cid) {
    var url = basePath + "/api/config/publish/" + id;
    $.post(url, {
        cid: cid
    }, function (res) {
        layer.msg(res.msg);
        if (res.code === SUCCESS_CODE) {
            closeModel();
        }
    }, "json")
};

/**
 * 配置详情
 * @param id
 * @param viewOrEdit
 */
viewOrEditByID = function (id, viewOrEdit) {
    var url = basePath + "/api/config/" + id;
    $.get(url, function (res) {
        // 导出弹窗内容模版
        var context = config.exportAddConfigContext(viewOrEdit, biz = res.context.serviceName, data = res.context);
        initModelContext(context, viewOrEdit === "view" ? refresh : function () {
        });
        initTags(data.tags);
    }, "json")
};

/**
 * 同步服务配置
 * @param service
 */
viewRealConfig = function (service) {

    var curl = basePath + "/api/clusters";
    $.get(curl, function (res) {
        if (res.code === SUCCESS_CODE) {
            var html = "<select style='width: 80%;margin: 0 auto' id='nodeSelect' class='form-control'>";
            for (var i = 0; i < res.context.length; i++) {
                html += '<option value="' + res.context[i].id + '">' + res.context[i].zkHost + '</option>';
            }
            html += '</select>';
            bodyAbs();
            // 选择集群发布
            layer.open({
                type: 1,
                title: '请选择需要查看的集群',
                content: html,
                btn: ['查看', '取消'],
                yes: function (index, layero) {
                    layer.close(index);
                    processSysRealConfig($("#nodeSelect").find("option:selected").val(), service);
                }, btn2: function (index, layero) {
                    layer.msg("操作取消");
                }, cancel: function () {
                    layer.msg("操作取消");
                    //return false 开启该代码可禁止点击该按钮关闭
                }
            });
            rmBodyAbs();
        }
    }, "json");

};
/**
 * 执行配置同步
 * @param cid
 */
processSysRealConfig = function (cid, service) {
    var url = basePath + "/api/config/sysRealConfig/";
    $.get(url, {
        cid: cid,
        serviceName: service
    }, function (res) {
        var realdata = res.context;
        realdata.serviceName = service;
        var context = config.exportAddConfigContext("real", biz = "", data = realdata);
        initModelContext(context, function () {
        });
    }, "json");
};

/**
 * 回滚配置
 * @param id
 */
rollback = function (id) {
    var url = basePath + "/api/config/rollback/" + id;
    $.post(url, function (res) {
        layer.msg(res.msg);
        closeModel();
    }, "json")
};

/**
 * 获取配置关键信息
 * @returns
 */
processConfigData = function () {
    var serviceName = $("#service-name").val();
    var timeoutConfig = $("#timeout-config-area").val();
    var loadbalanceConfig = $("#loadbalance-config-area").val();
    var routerConfig = $("#router-config-area").val();
    var freqConfig = $("#freq-config-area").val();
    var remark = $("#remark-area").val();
    var t = $("input[data-role='tagsinput']#serviceTags");
    var tags = t.val();
    return {
        serviceName: serviceName,
        tags: tags,
        timeoutConfig: timeoutConfig,
        loadbalanceConfig: loadbalanceConfig,
        routerConfig: routerConfig,
        freqConfig: freqConfig,
        remark: remark
    };
};

/**
 * 清空配置
 */
clearConfigInput = function () {
    layer.confirm('清空已填写配置？', {
        btn: ['确认', '取消']
    }, function () {
        $("textarea.form-control").val("");
        layer.msg("已清空");
    }, function () {
        layer.msg("取消清空");
    });
};

/**
 * 打开添加配置弹窗
 */
openAddConfig = function () {
    // 导出弹窗内容模版
    var context = config.exportAddConfigContext("add");
    // 初始化弹窗
    initModelContext(context, refresh);
    initTags();
};

var initTags = function (data) {
    var t = $("input[data-role='tagsinput']");
    t.tagsinput({
        tagClass: function () {
            return RANDOM_LABEL;
        }
    });
    t.tagsinput('refresh');
    t.tagsinput('add', data);
};


openPublishHistory = function (serviceName) {

    // 导出弹窗内容模版
    var context = config.exportPublishHistoryContext(serviceName);
    // 初始化弹窗
    initModelContext(context, refresh);
};
