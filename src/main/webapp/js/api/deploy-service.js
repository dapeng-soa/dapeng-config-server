$(document).ready(function () {
    InitDeployServices();
});
var deploy = new api.Deploy();
var bsTable = {};

function InitDeployServices() {
    //记录页面bootstrap-table全局变量$table，方便应用
    var queryUrl = basePath + '/api/deploy-services';
    var table = new BSTable("deploy-service-table", queryUrl, setColumns());
    table.onDblClickRow = function (row) {
        viewDeployServiceOrEditByID(row.id, VIEW)
    };
    table.responseHandler = function (res) {
        return {
            total: res.context == null ? 0 : res.context.totalElements,
            rows: res.context.content
        };
    };
    table.params = function (ps) {
        //ps.sort = "name";
        return ps;
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
        field: 'name',
        title: '服务名',
        sortable: true
    }, {
        field: 'image',
        title: '镜像'
    }, {
        field: 'labels',
        title: '标签',
        sortable: true
    }, {
        field: 'remark',
        title: '备注',
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
        formatter: deployServiceActionFormatter
    }]
};

/**
 * @return {string}
 */
deployServiceActionFormatter = function (value, row, index) {
    return deploy.exportDeployServiceActionContext(value, row);
};


openAddDeployServiceModle = function () {
    // 导出弹窗内容模版
    var context = deploy.exportAddDeployServiceContext(ADD);
    // 初始化弹窗
    initModelContext(context, function () {
        bsTable.refresh();
    });
    addCopyServiceSelectInit();
};

/**
 * 保存
 */
saveDeployService = function () {
    var url = basePath + "/api/deploy-service/add";
    var settings = {
        type: "post",
        url: url,
        data: JSON.stringify(processDeployServiceData()),
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
clearDeployServiceInput = function () {
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

processDeployServiceData = function () {
    var name = $("#name").val();
    var image = $("#image").val();
    var labels = $("#labels").val();
    var env = $("#env-area").val();
    var volumes = $("#volumes-area").val();
    var ports = $("#ports-area").val();
    var composeLabels = $("#composeLabels-area").val();
    var dockerExtras = $("#dockerExtras-area").val();
    var remark = $("#remark-area").val();
    return {
        name: name,
        image: image,
        labels: labels,
        env: env,
        volumes: volumes,
        ports: ports,
        composeLabels: composeLabels,
        dockerExtras: dockerExtras,
        remark: remark
    }
};

/**
 * 修改
 * @param id
 * @param op
 */
viewDeployServiceOrEditByID = function (id, op) {
    var url = basePath + "/api/deploy-service/" + id;
    $.get(url, function (res) {
        // 导出弹窗内容模版
        var context = deploy.exportAddDeployServiceContext(op, "", res.context);
        // 初始化弹窗
        initModelContext(context, function () {
            bsTable.refresh();
        });
    }, "json");
};

/**
 * 修改apikey
 * @param id
 */
editedDeployService = function (id) {
    var url = basePath + "/api/deploy-service/edit/" + id;

    var settings = {
        type: "post",
        url: url,
        data: JSON.stringify(processDeployServiceData()),
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

delDeployService = function () {
    layer.msg("暂无权限")
};

addCopyServiceSelectInit = function () {
    var curl = basePath + "/api/deploy-services?sort=name&order=asc";
    var ss = new BzSelect(curl, "addCopyServiceSelect", "id", "name");
    ss.responseHandler = function (res) {
        return res.context.content
    };
    ss.init();
};

copyServiceChange = function () {
    var ed = $("#addCopyServiceSelect").find("option:selected").val();
    if (Number(ed) !== undefined && Number(ed) !== 0) {
        var url = basePath + "/api/deploy-service/" + ed;
        $.get(url, function (res) {
            var s = res.context;
            $("#name").val(s.name);
            $("#image").val(s.image);
            $("#labels").val(s.labels);
            $("#env-area").val(s.env);
            $("#volumes-area").val(s.volumes);
            $("#ports-area").val(s.ports);
            $("#composeLabels-area").val(s.composeLabels);
            $("#dockerExtras-area").val(s.dockerExtras);
            $("#remark-area").val(s.remark);
        }, "json");
    }
};


