$(document).ready(function () {
    initClusters();
});
var config1 = new api.Config();

function initClusters() {
    //记录页面bootstrap-table全局变量$table，方便应用
    var queryUrl = basePath + '/api/clusters';
    var rows = 20;
    $table = $('#clusters-table').bootstrapTable({
        url: queryUrl,                      //请求后台的URL（*）
        method: 'GET',                      //请求方式（*）
        responseHandler: function (res) {     //格式化返回数据
            return {
                total: res.context.length,
                rows: res.context
            };
        },
        //toolbar: '#toolbar',              //工具按钮用哪个容器
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
        cardView: ($(window).width()<1024),                    //是否显示详细视图
        detailView: false,                  //是否显示父子表
        //得到查询的参数
        queryParams: function (params) {
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
        }],
        onLoadSuccess: function () {
        },
        onLoadError: function () {
            layer.msg("数据加载失败");
        },
        // 双击行事件
        onDblClickRow: function (row) {
            var id = row.id;
            viewOrEditByID(id, 'view');
        }
    });
}

/**
 * @return {string}
 */
clustersActionFormatter = function (value, row, index) {
    return config1.exportClustersTableActionContext(value, row);
};

numberFormatter = function (value, row, index) {
    return index + 1;
};

/**
 * 查看或者删除集群
 */
viewClusterOrEditByID = function (cid, op) {
    layer.msg("暂无权限");
};

/**
 * 删除单个集群
 */
delCluster = function (cid) {
    bodyAbs();
    layer.confirm('确定删除？', {
        btn: ['确认', '取消']
    }, function () {
        var url = basePath + "/api/cluster/del/" + cid
        $.post(url, function (res) {
            layer.msg(res.msg);
            refresh();
        }, "json");
        rmBodyAbs();
    }, function () {
        layer.msg("未做任何改动");
        rmBodyAbs();
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
        layer.msg(res.msg);
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
    bodyAbs();
    layer.confirm('将清空当前所有输入？', {
        btn: ['确认', '取消']
    }, function () {
        $("textarea.form-control,input.form-control").val("");
        layer.msg("已清空");
    }, function () {
        layer.msg("取消清空");
    });
};


openAddCluster = function () {
    // 导出弹窗内容模版
    var context = config1.exportAddClusterContext("add");
    // 初始化弹窗
    initModelContext(context, refresh);
};

