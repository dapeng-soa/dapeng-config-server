$(document).ready(function () {
    initClusters();
});
var config1 = new api.Config();

function initClusters() {
    var queryUrl = basePath + '/api/clusters';
    var table = new BSTable("clusters-table", queryUrl, setColumns());
    table.onDblClickRow = function (row) {
        viewClusterOrEditByID(row.id, VIEW)
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
        field: 'zkHost',
        title: 'zookeeper集群地址'
    }, {
        field: 'remark',
        title: '描述'
    }, {
        field: 'influxdbHost',
        title: 'influxdb',
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
        formatter: clustersActionFormatter
    }]
};

/**
 * @return {string}
 */
clustersActionFormatter = function (value, row, index) {
    return config1.exportClustersTableActionContext(value, row);
};


/**
 * 查看或者删除集群
 */
viewClusterOrEditByID = function (cid, op) {
    showMessage(ERROR, "暂无权限");
};

/**
 * 删除单个集群
 */
delCluster = function (cid) {
    layer.confirm('确定删除？', {
        btn: ['确认', '取消']
    }, function () {
        var url = basePath + "/api/cluster/del/" + cid
        $.post(url, function (res) {
            showMessage(INFO, res.msg);
            layer.close(index);
            bsTable.refresh();
        }, "json");
    }, function () {
        showMessage(WARN, "未做任何改动");
    });
};

/**
 * 保存集群
 */
saveCluster = function () {

    var url = basePath + "/api/cluster/add";
    var settings = {
        type: "post",
        url: url,
        data: JSON.stringify(processClusterData()),
        dataType: "json",
        contentType: "application/json"
    };
    $.ajax(settings).done(function (res) {
        showMessage(INFO, res.msg);
        if (res.code === SUCCESS_CODE) {
            refresh();
        }
    });
};


/**
 *
 */
processClusterData = function () {
    var zkHost = $("#zookeeperHost").val();
    var remark = $("#remark").val();
    var influxdbHost = $("#influxdbHost").val();
    var influxdbUser = $("#influxdbUser").val();
    var influxdbPass = $("#influxdbPass").val();

    return {
        zkHost: zkHost,
        remark: remark,
        influxdbHost: influxdbHost,
        influxdbUser: influxdbUser,
        influxdbPass: influxdbPass
    }
};

/**
 * 清空输入
 */
clearClusterInput = function () {
    layer.confirm('将清空当前所有输入？', {
        btn: ['确认', '取消']
    }, function (index) {
        $("textarea.form-control,input.form-control").val("");
        showMessage(SUCCESS, "已清空");
        layer.close(index);
    }, function () {
        showMessage(WARN, "取消清空");
    });
};


openAddCluster = function () {
    // 导出弹窗内容模版
    var context = config1.exportAddClusterContext("add");
    // 初始化弹窗
    initModelContext(context, refresh);
};

