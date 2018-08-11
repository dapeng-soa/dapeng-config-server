$(document).ready(function () {
    InitDeployHosts();
});
var deploy = new api.Deploy();

function InitDeployHosts() {
    //记录页面bootstrap-table全局变量$table，方便应用
    var queryUrl = basePath + '/api/deploy-hosts';
    var table = new BSTable("deploy-host-table", queryUrl, setColumns());
    table.onDblClickRow = function (row) {
        viewDeployHostOrEditByID(row.id, VIEW)
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
        field: 'name',
        title: '节点名',
        sortable: true
    }, {
        field: 'ip',
        title: '节点host'
    }, {
        field: 'labels',
        title: '标签',
        sortable: true
    }, {
        field: 'extra',
        title: '外部机器',
        formatter: extraFormatter
    },{
        field: 'remark',
        title: '备注'
    },{
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
        formatter: deployHostActionFormatter
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
/**
 * @return {string}
 */
deployHostActionFormatter = function (value, row, index) {
    return deploy.exportDeployHostActionContext(value, row);
};


openAddDeployHostModle = function () {
    // 导出弹窗内容模版
    var context = deploy.exportAddDeployHostContext("add");
    // 初始化弹窗
    initModelContext(context, refresh);
    initSetList();
};

/**
 * 保存
 */
saveDeployHost = function () {
    var url = basePath + "/api/deploy-host/add";
    var settings = {
        type: "post",
        url: url,
        data: JSON.stringify(processDeployHostData()),
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
clearDeployHostInput = function () {
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

processDeployHostData = function () {
    var name = $("#name").val();
    var ip = $("#ip").val();
    var labels = $("#labels").val();
    var env = $("#env-area").val();
    var remark = $("#remark-area").val();
    var setSelect = $("#setSelect").find("option:selected").val();
    var extraSelect = $("#extraSelect").find("option:selected").val();
    return {
        name: name,
        ip: ip,
        labels: labels,
        env: env,
        setId: setSelect,
        extra: extraSelect,
        remark:remark
    }
};

/**
 * 修改
 * @param id
 * @param op
 */
viewDeployHostOrEditByID = function (id, op) {
    var url = basePath + "/api/deploy-host/" + id;
    $.get(url, function (res) {
        // 导出弹窗内容模版
        var context = deploy.exportAddDeployHostContext(op, "", res.context);
        // 初始化弹窗
        initModelContext(context, refresh);
        initSetList(res.context.setId);
    }, "json");
};

/**
 * 修改apikey
 * @param id
 */
editedDeployHost = function (id) {
    var url = basePath + "/api/deploy-host/edit/" + id;

    var settings = {
        type: "post",
        url: url,
        data: JSON.stringify(processDeployHostData()),
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

delDeployHost = function () {
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
            var html = "";
            for (var i = 0; i < res.context.length; i++) {
                var seled = "";
                if (id !== undefined && id !==""){
                    seled = res.context[i].id === id?"selected":"";
                }
                html += '<option '+seled+' value="' + res.context[i].id + '">' + res.context[i].name + '</option>';
            }
            $("#setSelect").html(html);
        }
    }, "json");
};


