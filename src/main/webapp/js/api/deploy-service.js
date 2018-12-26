$(document).ready(function () {
    InitDeployServices();
    initDeploySelectTags();
    initUploader();
});
var deploy = new api.Deploy();
var bsTable = {};
var $$ = new api.Api();

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
        ps.tag = $("#deployServiceTags").find("option:selected").val();
        return ps;
    };
    table.init();
    bsTable = table;
}

initUploader = function () {
    var uploader = WebUploader.create({

        // swf文件路径
        swf: '/plugins/web-uploader/Uploader.swf',

        // 文件接收服务端。
        server: basePath + '/api/deploy-service/import',

        // 选择文件的按钮。可选。
        // 内部根据当前运行是创建，可能是input元素，也可能是flash.
        pick: '#picker',

        // 不压缩image, 默认如果是jpeg，文件上传前会压缩一把再上传！
        resize: false
    });
    uploader.on('fileQueued', function (file) {
        uploader.upload();
    });

    uploader.on('uploadSuccess', function (file, response) {
        showMessage(SUCCESS, file.name + response.msg);
        bsTable.refresh();
    });

    uploader.on('uploadError', function (file, reason) {
        showMessage(ERROR, file.name + "导入失败,错误码" + reason);
        bsTable.refresh();
    });

    uploader.on('uploadComplete', function (file) {
        $('#' + file.id).find('.progress').fadeOut();
    });

    uploader.on('uploadProgress', function (file, percentage) {

    });


};

setColumns = function () {
    return [{
        checkbox: true,
        visible: true//是否显示复选框
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
        sortable: true,
        align: 'center',
        valign: 'middle',
        formatter: tagsFormatter
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

/**
 * 批量导出服务
 */
exportBatchService = function () {
    var selected = bsTable.getAllSelections();
    var ids = [];
    if (selected.length !== 0) {
        $.each(selected, function (index, em) {
            ids.push(em.id);
        });
        window.open(basePath + "/api/deploy-service/export/" + JSON.stringify(ids));
    } else {
        showMessage(ERROR, "未选中任何数据", "警告")
    }
};


openAddDeployServiceModle = function () {
    // 导出弹窗内容模版
    var context = deploy.exportAddDeployServiceContext(ADD);
    // 初始化弹窗
    initModelContext(context, function () {
        bsTable.refresh();
        initDeploySelectTags();
    });
    addCopyServiceSelectInit();
    initTags();
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
        showMessage(INFO, res.msg);
        if (res.code === SUCCESS_CODE) {
            closeModel();
        }
    });
};

/**
 * 清空配置
 */
clearDeployServiceInput = function () {
    layer.confirm('将清空当前所有输入？', {
        btn: ['确认', '取消']
    }, function (index) {
        $("textarea.form-control").val("");
        showMessage(SUCCESS, "已清空");
        layer.close(index);
    }, function () {
        showMessage(WARN, "取消清空");
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
            initDeploySelectTags();
        });
        initTags(res.context.labels);
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
        showMessage(INFO, res.msg);
        if (res.code === SUCCESS_CODE) {
            closeModel();
        }
    });
};

delDeployService = function (id, name) {
    layer.confirm('删除服务' + name + '？', {
        btn: ['删除', '取消']
    }, function (index) {
        var url = basePath + "/api/deploy-service/del/" + id;
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
        showMessage(INFO, "未做任何变更");
    });
};

var initDeploySelectTags = function () {
    var url = basePath + "/api/deploy-service/service-tags";
    $$.$get(url, function (res) {
        if (res.code === SUCCESS_CODE) {
            var tags = res.context;
            var ops = "<option value=''>请选择</option>";
            for (var i in tags) {
                ops += "<option value='" + tags[i] + "'>" + tags[i] + "</option>"
            }
            $("#deployServiceTags").html(ops).selectpicker('refresh');
        }
    });
};

var execServiceTagChanged = function () {
    bsTable.refresh();
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


