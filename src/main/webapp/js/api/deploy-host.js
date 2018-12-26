$(document).ready(function () {
    InitDeployHosts();
    viewInitSetList();
    initHostSelectTags();
});
var deploy = new api.Deploy();
var bsTable = {};
var $$ = new api.Api();

function InitDeployHosts() {
    //记录页面bootstrap-table全局变量$table，方便应用
    var queryUrl = basePath + '/api/deploy-hosts';
    var table = new BSTable("deploy-host-table", queryUrl, setColumns());
    table.onDblClickRow = function (row) {
        viewDeployHostOrEditByID(row.id, VIEW)
    };
    table.responseHandler = function (res) {
        return {
            total: res.context == null ? 0 : res.context.totalElements,
            rows: res.context.content
        };
    };
    table.params = function (ps) {
        //ps.sort = "name";
        ps.setId = $("#setSelectView").find("option:selected").val();
        ps.tag = $("#deployHostTags").find("option:selected").val();
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
        formatter: deployHostActionFormatter
    }]
};

var execHostTagChanged = function () {
    bsTable.refresh();
};

var initHostSelectTags = function () {
    var url = basePath + "/api/deploy-host/host-tags";
    $$.$get(url, function (res) {
        if (res.code === SUCCESS_CODE) {
            var tags = res.context;
            var ops = "<option value=''>请选择</option>";
            for (var i in tags) {
                ops += "<option value='" + tags[i] + "'>" + tags[i] + "</option>"
            }
            $("#deployHostTags").html(ops).selectpicker('refresh');
        }
    });
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
    initModelContext(context, function () {
        bsTable.refresh();
        initHostSelectTags();
    });
    setTextareaFull();
    initSetList();
    initTags();
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
        showMessage(INFO, res.msg);
        if (res.code === SUCCESS_CODE) {
            closeModel();
        }
    });
};

viewSetChanged = function () {
    bsTable.refresh();
};

/**
 * 初始化set
 * @constructor
 */
viewInitSetList = function () {
    var curl = basePath + "/api/deploy-sets?sort=name&order=asc";
    var ss = new BzSelect(curl, "setSelectView", "id", "name");
    ss.refresh = true;
    ss.responseHandler = function (res) {
        return res.context.content
    };
    ss.init();
};

/**
 * 清空配置
 */
clearDeployHostInput = function () {
    layer.confirm('将清空当前所有输入？', {
        btn: ['确认', '取消']
    }, function (index) {
        $("textarea.form-control").val("");
        showMessage(INFO, "已清空");
        layer.close(index);
    }, function () {
        showMessage(WARN, "取消清空");
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
        remark: remark
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
        initModelContext(context, function () {
            bsTable.refresh();
            initHostSelectTags();
        });
        setTextareaFull();
        initSetList(res.context.setId);
        initTags(res.context.labels);
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
        showMessage(INFO, res.msg);
        if (res.code === SUCCESS_CODE) {
            closeModel();
        }
    });
};

delDeployHost = function (id, name) {
    layer.confirm('删除节点' + name + '？', {
        btn: ['删除', '取消']
    }, function (index) {
        var url = basePath + "/api/deploy-host/del/" + id;
        var settings = {
            type: "post",
            url: url,
            dataType: "json",
            contentType: "application/json"
        };
        $.ajax(settings).done(function (res) {
            showMessage(INFO, res.msg);
            if (res.code === SUCCESS_CODE) {
                bsTable.refresh();
                layer.close(index);
            }
        });
    }, function () {
        showMessage(WARN, "未做任何变更");
    });
};

/**
 * 初始化set
 * @constructor
 */
initSetList = function (id) {
    var curl = basePath + "/api/deploy-sets?sort=name&order=asc";
    var ss = new BzSelect(curl, "setSelect", "id", "name");
    ss.v_selected = id;
    ss.responseHandler = function (res) {
        return res.context.content
    };
    ss.refresh = true;
    ss.init();
};


