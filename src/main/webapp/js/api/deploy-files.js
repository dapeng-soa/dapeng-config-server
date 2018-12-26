$(document).ready(function () {
    initServiceFiles();
});
var sf = new api.ServiceFiles();
var $$ = new api.Api();
var bsTable = {};
var unitTable = {};

/**
 * 查看服务配置文件
 * @param id
 * @param op
 */
function viewServiceFilesOrEditByID(id, op) {
    var url = basePath + "/api/deploy-file/" + id;
    $.get(url, function (res) {
        var context = sf.exportAddServiceFilesContext(op, '', res.context);
        initModelContext(context, function () {
            bsTable.refresh();
        });
    }, "json");
}

/**
 * 删除配置文件
 * @param id
 */
function delServiceFiles(id) {
    layer.confirm('确定删除？', {
        btn: ['确认', '取消']
    }, function (index) {
        var url = basePath + "/api/deploy-file/del/" + id;
        $.post(url, function (res) {
            showMessage(INFO, res.msg);
            layer.close(index);
            bsTable.refresh();
        }, "json");
    }, function () {
        showMessage(INFO, "未做任何改动");
    });
}

/**
 * 打开关联部署单元
 */
function openLinkDeployUnits(fid, name) {
    var context = sf.exportFileLinkUnitContext(fid, name);
    initModelContext(context, function () {
        bsTable.refresh();
    });
    initDeployUnits();
    initViewSetSelect();
    initViewServiceSelect();
    initViewHostSelect();
    selectRefresh();
    linkTypeChanged();
}

/**
 * 一键关联文件
 */
function linkDeployUnits(fileId) {
    var selections = unitTable.getAllSelections();
    var url = basePath + "/api/deploy-file/link-unit";
    var uids = [];
    if (selections.length !== 0) {
        $.each(selections, function (index, em) {
            uids.push(em.id);
        });
        var data = JSON.stringify({
            uids: uids,
            fid: fileId
        });
        $$.post(url, data, function (res) {
            showMessage(INFO, res.msg);
            unitTable.refresh();
        }, "application/json")
    } else {
        showMessage(WARN, "请勾选需要绑定的部署单元", "警告")
    }
}

/**
 * 解绑
 * @param fileId
 */
function unLinkDeployUnits(fileId) {
    var selections = unitTable.getAllSelections();
    var url = basePath + "/api/deploy-file/unlink-unit";
    var uids = [];
    if (selections.length !== 0) {
        $.each(selections, function (index, em) {
            uids.push(em.id);
        });
        var data = JSON.stringify({
            uids: uids,
            fid: fileId
        });
        $$.post(url, data, function (res) {
            showMessage(INFO, res.msg);
            unitTable.refresh();
        }, "application/json")
    } else {
        showMessage(WARN, "请选择需要解绑的部署单元", "警告")
    }
}


var viewUnitSetChanged = function () {
    unitTable.refresh();
};
var viewUnitHostChanged = function () {
    unitTable.refresh();
};
var viewUnitServiceChanged = function () {
    unitTable.refresh();
};

var linkTypeChanged = function () {
    var linkType = $("#linkTypeSelect").find("option:selected").val();
    switch (Number(linkType)) {
        case 1:
            $("#linkButton").show();
            $("#unLinkButton").hide();
            break;
        case 2:
            $("#linkButton").hide();
            $("#unLinkButton").show();
            break;
        default:
            break;
    }
    // 当是已关联应当显示解绑
    unitTable.refresh();
};

/**
 * 弹窗里的部署单元
 */
function initDeployUnits() {
    var queryUrl = basePath + '/api/deploy-file/units';
    var table = new BSTable("deploy-unit-table", queryUrl, setUnitColumns());
    table.params = function (ps) {
        ps.setId = $("#setSelectView").find("option:selected").val();
        ps.hostId = $("#hostSelectView").find("option:selected").val();
        ps.serviceId = $("#serviceSelectView").find("option:selected").val();
        ps.linkType = $("#linkTypeSelect").find("option:selected").val();
        ps.fileId = $("#currFileId").val();
        return ps;
    };
    table.responseHandler = function (res) {
        return {
            total: res.context == null ? 0 : res.context.totalElements,
            rows: res.context.content
        };
    };
    table.init();
    unitTable = table;
}


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

var setUnitColumns = function () {
    return [{
        checkbox: true,
        visible: true//是否显示复选框
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
        field: 'updatedAt',
        title: '修改时间',
        sortable: true
    }]
};

function initServiceFiles() {
    var queryUrl = basePath + '/api/deploy-files';
    var table = new BSTable("deploy-files-table", queryUrl, setColumns());
    table.onDblClickRow = function (row) {
        viewServiceFilesOrEditByID(row.id, VIEW)
    };
    table.responseHandler = function (res) {
        return {
            total: res.context == null ? 0 : res.context.totalElements,
            rows: res.context.content
        };
    };
    table.params = function (ps) {
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
        field: 'fileExtName',
        title: '外部挂载文件名',
        sortable: true
    }, {
        field: 'fileName',
        title: '容器内文件名',
        sortable: true
    }, {
        field: 'fileTag',
        title: 'fileTag'
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
        formatter: serviceFileActionFormatter
    }]
};
openAddServiceFileModle = function () {
    var context = sf.exportAddServiceFilesContext(ADD);
    initModelContext(context, function () {
        bsTable.refresh();
    });
};

var serviceFileActionFormatter = function (value, row) {
    return sf.exportServiceFileAction(value, row.fileExtName + ":" + row.fileName);
};

saveServiceFile = function () {
    var url = basePath + "/api/deploy-file/add";
    $$.post(url, JSON.stringify(processServiceFileData()), function (res) {
        showMessage(INFO, res.msg);
        if (res.code === SUCCESS_CODE) {
            closeModel();
        }
    }, "application/json");
};

processServiceFileData = function () {
    var fileName = $("#fileName").val();
    var fileExtName = $("#fileExtName").val();
    var fileContext = $("#fileContext").val();
    var remark = $("#remark-area").val();
    return {
        fileName: fileName,
        fileExtName: fileExtName,
        fileContext: fileContext,
        remark: remark
    }
};

clearServiceFileInput = function () {

};


editedServiceFile = function (id) {
    var url = basePath + "/api/deploy-file/edit/" + id;
    $$.post(url, JSON.stringify(processServiceFileData()), function (res) {
        showMessage(INFO, res.msg);
        if (res.code === SUCCESS_CODE) {
            closeModel();
        }
    }, "application/json");
};