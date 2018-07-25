$(document).ready(function () {
    InitDeployHosts();
});
var deploy = new api.Deploy();

function InitDeployHosts() {
    //记录页面bootstrap-table全局变量$table，方便应用
    var queryUrl = basePath + '/api/deploy-hosts';
    var rows = 20;
    $table = $('#deploy-host-table').bootstrapTable({
        url: queryUrl,                      //请求后台的URL（*）
        method: 'GET',                      //请求方式（*）
        responseHandler: function (res) {     //格式化返回数据
            return {
                total: res.context==null?0:res.context.length,
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
            title: '是否外部机器',
            sortable: true,
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

extraFormatter = function () {

};
/**
 * @return {string}
 */
deployHostActionFormatter = function (value, row, index) {
    return deploy.exportDeployHostActionContext(value, row);
};

numberFormatter = function (value, row, index) {
    return index + 1;
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
    console.log(processDeployHostData());
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
        data: JSON.stringify(processApiKeyData()),
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
                html += '<option seled value="' + res.context[i].id + '">' + res.context[i].name + '</option>';
            }
            $("#setSelect").html(html);
        }
    }, "json");
};


