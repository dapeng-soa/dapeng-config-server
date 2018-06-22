$(document).ready(function () {
    InitApiTable();
});
var config1 = new api.Config();

function InitApiTable() {
    //记录页面bootstrap-table全局变量$table，方便应用
    var queryUrl = basePath + '/api/authkeys';
    var rows = 20;
    $table = $('#apikey-table').bootstrapTable({
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
        showRefresh: false,                  //是否显示刷新按钮
        minimumCountColumns: 2,             //最少允许的列数
        clickToSelect: false,                //是否启用点击选中行
        //height: 900,                      //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
        uniqueId: "id",                     //每一行的唯一标识，一般为主键列
        showToggle: false,                   //是否显示详细视图和列表视图的切换按钮
        cardView: false,                    //是否显示详细视图
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
            field: 'apiKey',
            title: 'ApiKey',
            sortable: true
        }, {
            field: 'password',
            title: '密码'
        }, {
            field: 'biz',
            title: '所属业务',
            sortable: true,
            align: 'center',
            valign: 'middle'
        }, {
            field: 'ips',
            title: 'IP规则'
        }, {
            field: 'notes',
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
            formatter: ApiActionFormatter
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

/**
 * @return {string}
 */
ApiActionFormatter = function (value, row, index) {
    return config1.exportApiKeyTableActionContext(value, row);
};

numberFormatter = function (value, row, index) {
    return index + 1;
};

openAddApiKeyModle = function () {
    // 导出弹窗内容模版
    var context = config1.exportAddApiKeyContext("add");
    // 初始化弹窗
    initModelContext(context, refresh);
};

/**
 * 保存
 */
saveApiKey = function () {
    var url = basePath + "/api/apikey/add";
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

/**
 * 清空配置
 */
clearApiKeyInput = function () {
    bodyAbs();
    layer.confirm('将清空当前所有输入？', {
        btn: ['确认', '取消']
    }, function () {
        $("textarea.form-control").val("");
        layer.msg("已清空");
        rmBodyAbs();
    }, function () {
        layer.msg("取消清空");
        rmBodyAbs();
    });
};

processApiKeyData = function () {
    var authApikey = $("#authApikey").val();
    var authPassWord = $("#authPassWord").val();
    var authBiz = $("#authBiz").val();
    var authIps = $("#authIps").val();
    var notes = $("#notes").val();

    return {
        apiKey: authApikey,
        password: authPassWord,
        biz: authBiz,
        ips: authIps,
        notes: notes
    }
};

viewApiKeyOrEditByID = function () {
    layer.msg("暂无权限")
};

delApiKey = function () {
    layer.msg("暂无权限")
};


