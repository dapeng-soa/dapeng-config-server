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
    //记录页面bootstrap-table全局变量$table，方便应用
    var queryUrl = basePath + '/api/deploy-units';
    var rows = 20;
    $table = $('#deploy-unit-table').bootstrapTable({
        url: queryUrl,                      //请求后台的URL（*）
        method: 'GET',                      //请求方式（*）
        responseHandler: function (res) {     //格式化返回数据
            return {
                total: res.context == null ? 0 : res.context.length,
                rows: res.context
            };
        },
        toolbar: '#toolbar',              //工具按钮用哪个容器
        striped: true,                      //是否显示行间隔色
        cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
        pagination: true,                   //是否显示分页（*）
        sortable: true,                     //是否启用排序
        sortOrder: "desc",                   //排序方式
        sidePagination: "server",           //分页方式：client客户端分页，server服务端分页（*）
        pageNumber: 1,                      //初始化加载第一页，默认第一页,并记录
        pageSize: rows,                     //每页的记录行数（*）
        pageList: [5, 10, 15, 20],        //可供选择的每页的行数（*）
        search: true,                      //是否显示表格搜索
        strictSearch: false,                 //设置为 true启用全匹配搜索，否则为模糊搜索。
        showColumns: true,                  //是否显示所有的列（选择显示的列）
        showRefresh: true,                  //是否显示刷新按钮
        minimumCountColumns: 2,             //最少允许的列数
        clickToSelect: false,                //是否启用点击选中行
        //height: 900,                      //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
        uniqueId: "id",                     //每一行的唯一标识，一般为主键列
        showToggle: true,                   //是否显示详细视图和列表视图的切换按钮
        cardView: ($(window).width() < 1024),                    //是否显示详细视图
        detailView: false,                  //是否显示父子表
        //得到查询的参数
        queryParams: function (params) {
            //这里的键的名字和控制器的变量名必须一致，这边改动，控制器也需要改成一样的
            return {
                keyword: params.search,
                rows: params.limit,                         //页面大小
                page: (params.offset / params.limit) + 1,   //页码
                sort: params.sort,      //排序列名
                sortOrder: params.order //排位命令（desc，asc）
            };
        },
        columns: [{
            checkbox: false,
            visible: false//是否显示复选框
        }, {
            field: 'id',
            title: '#',
            formatter: numberFormatter

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
        }],
        onLoadSuccess: function () {
        },
        onLoadError: function () {
            layer.msg("数据加载失败");
        },
        // 双击行事件
        onDblClickRow: function (row) {
            var id = row.id;
        }
    });
}

/**
 * @return {string}
 */
deployUnitActionFormatter = function (value, row, index) {
    console.log(row.setId);
    return deploy.exportDeployUnitActionContext(value, row);
};

numberFormatter = function (value, row, index) {
    return index + 1;
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
                html += '<option seled value="' + res.context[i].id + '">' + res.context[i].name + '</option>';
            }
            $("#setSelect").html(html);
            if (id === undefined || id === "") {
                initHostList()
            }
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
                html += '<option seled value="' + res.context[i].id + '">' + res.context[i].name + '</option>';
            }
            $("#hostSelect").html(html);
            setTimeout(function () {
                processEnvsChanged();
            },100);
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
                html += '<option seled value="' + res.context[i].id + '">' + res.context[i].name + '</option>';
            }
            $("#serviceSelect").html(html);
        }
    }, "json");
};


/**
 * 增加环境集变更时，重新获取变量信息
 * @param obj
 */
addUnitSetChanged = function (obj) {
    var setId = $(obj).find("option:selected").val();
    initHostList(setId);
    processEnvsChanged();
};
/**
 * host 变化，重新获取变量信息
 */
addUnitHostChanged = function () {
    processEnvsChanged();
};

/**
 * 服务变化，重新获取变量信息
 */
addUnitServiceChanged = function () {
    processEnvsChanged();
};

/**
 * 处理env等变量等数据合并
 */
processEnvsChanged = function () {
    var setId = $("#setSelect").find("option:selected").val();
    var hostId = $("#hostSelect").find("option:selected").val();
    var serviceId = $("#serviceSelect").find("option:selected").val();

    var url = basePath + "/api/deploy-unit/process-envs";
    $.get(url, {
        setId: setId,
        hostId: hostId,
        serviceId: serviceId
    }, function (res) {
        console.log(res.context);
        if (res.code === SUCCESS_CODE) {
            $("#env-area").val();
            $("#volumes-area").val();
            $("#ports-area").val();
            $("#dockerExtras-area").val();
        }
    },"json");
};


