$(document).ready(function () {
    initBuildHosts();
});

var build = new api.Build();
var $$ = new api.Api();
var bsTable = {};

function viewBuildHostOrEditByID(id, op) {
    var url = basePath + "/api/build-host/" + id;
    $.get(url, function (res) {
        var context = build.exportAddBuildHostContext(op, "", res.context);
        initModelContext(context, function () {
            bsTable.refresh();
        });
    }, "json");
}

function delBuildHost() {

}

function initBuildHosts() {
    var queryUrl = basePath + '/api/build-hosts';
    var table = new BSTable("build-host-table", queryUrl, setColumns());
    table.onDblClickRow = function (row) {
        viewBuildHostOrEditByID(row.id, VIEW)
    };
    table.responseHandler = function (res) {
        return {
            total: res.context == null ? 0 : res.context.totalElements,
            rows: res.context.content
        };
    };
    table.params = function (ps) {
        ps.sort = "name";
        return ps;
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
        field: 'name',
        title: '构建环境',
        sortable: true
    }, {
        field: 'host',
        title: 'host地址'
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
        formatter: buildHostActionFormatter
    }]
};
openAddBuildHostModle = function () {
    // 导出弹窗内容模版
    var context = build.exportAddBuildHostContext(ADD);
    // 初始化弹窗
    initModelContext(context, function () {
        bsTable.refresh();
    });
};

var buildHostActionFormatter = function (value, row) {
    return build.buildHostActionContext(value);
};

saveBuildHost = function () {
    var url = basePath + "/api/build-host/add";
    $$.post(url, JSON.stringify(processBuildHostData()), function (res) {
        layer.msg(res.msg);
        if (res.code === SUCCESS_CODE) {
            closeModel();
        }
    }, "application/json");
};

processBuildHostData = function () {
    var name = $("#name").val();
    var remark = $("#remark-area").val();
    var host = $("#ip").val();
    return {
        name: name,
        remark: remark,
        host: host
    }
};

clearBuildHostInput = function () {

};

editedBuildHost = function (id) {
    var url = basePath + "/api/build-host/edit/" + id;
    console.log(id);
    $$.post(url, JSON.stringify(processBuildHostData()), function (res) {
        layer.msg(res.msg);
        if (res.code === SUCCESS_CODE) {
            closeModel();
        }
    }, "application/json");
};