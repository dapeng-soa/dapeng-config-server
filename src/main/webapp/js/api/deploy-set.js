$(document).ready(function () {
    InitDeploySets();
});
var deploy = new api.Deploy();

function InitDeploySets() {
    //记录页面bootstrap-table全局变量$table，方便应用
    var queryUrl = basePath + '/api/deploy-sets';
    var table = new BSTable("deploy-set-table", queryUrl, setColumns());
    table.onDblClickRow = function (row) {
        viewDeploySetEditByID(row.id, VIEW)
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
        title: '环境集',
        sortable: true
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
        formatter: deploySetActionFormatter
    }]
};

/**
 * @return {string}
 */
deploySetActionFormatter = function (value, row, index) {
    return deploy.exportDeploySetActionContext(value, row);
};

openAddDeploySetModle = function () {
    // 导出弹窗内容模版
    var context = deploy.exportAddDeploySetContext("add");
    console.log(context);
    // 初始化弹窗
    initModelContext(context, refresh);
};

/**
 * 保存
 */
saveDeploySet = function () {
    var url = basePath + "/api/deploy-set/add";
    var settings = {
        type: "post",
        url: url,
        data: JSON.stringify(processDeploySetData()),
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
clearDeploySetInput = function () {
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

processDeploySetData = function () {
    var name = $("#name").val();
    var env = $("#env-area").val();
    var remark = $("#remark-area").val();
    return {
        name: name,
        env: env,
        remark: remark
    }
};

/**
 * 修改
 * @param id
 * @param op
 */
viewDeploySetEditByID = function (id, op) {
    var url = basePath + "/api/deploy-set/" + id;
    $.get(url, function (res) {
        // 导出弹窗内容模版
        var context = deploy.exportAddDeploySetContext(op, "", res.context);
        // 初始化弹窗
        initModelContext(context, refresh);
    }, "json");
};

/**
 * 修改apikey
 * @param id
 */
editedDeploySet = function (id) {
    var url = basePath + "/api/deploy-set/edit/" + id;

    var settings = {
        type: "post",
        url: url,
        data: JSON.stringify(processDeploySetData()),
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

delDeploySet = function () {
    layer.msg("暂无权限")
};


