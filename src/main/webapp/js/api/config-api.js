$(document).ready(function () {

    InitMainTable();
});

function InitMainTable() {
    //记录页面bootstrap-table全局变量$table，方便应用
    var queryUrl = '/api/configs';
    var rows = 10;
    $table = $('#table').bootstrapTable({
        url: queryUrl,                      //请求后台的URL（*）
        method: 'GET',                      //请求方式（*）
        responseHandler: function (res) {     //格式化返回数据
            return {
                total: res.context.totalElements,
                rows: res.context.content
            };
        },
        //toolbar: '#toolbar',              //工具按钮用哪个容器
        striped: true,                      //是否显示行间隔色
        cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
        pagination: true,                   //是否显示分页（*）
        sortable: true,                     //是否启用排序
        sortOrder: "DESC",                   //排序方式
        sidePagination: "server",           //分页方式：client客户端分页，server服务端分页（*）
        pageNumber: 1,                      //初始化加载第一页，默认第一页,并记录
        pageSize: rows,                     //每页的记录行数（*）
        pageList: [10, 25, 50, 100],        //可供选择的每页的行数（*）
        search: true,                      //是否显示表格搜索
        strictSearch: false,                 //设置为 true启用全匹配搜索，否则为模糊搜索。
        showColumns: false,                  //是否显示所有的列（选择显示的列）
        showRefresh: false,                  //是否显示刷新按钮
        minimumCountColumns: 2,             //最少允许的列数
        clickToSelect: false,                //是否启用点击选中行
        height: 900,                      //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
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
            visible: false                  //是否显示复选框
        }, {
            field: 'serviceName',
            title: '服务名',
            sortable: true
        }, {
            field: 'version',
            title: '版本号'
        }, {
            field: 'status',
            title: '状态',
            sortable: true,
            align: 'center',
            valign: 'middle',
            formatter: statusFormatter
        }, {
            field: 'publishedBy',
            title: '发布人',
            //formatter: linkFormatter
        }, {
            field: 'publishedAt',
            title: '发布时间',
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
            width: 140,
            align: 'center',
            valign: 'middle',
            formatter: actionFormatter
        }],
        onLoadSuccess: function () {
        },
        onLoadError: function () {
            showTips("数据加载失败！");
        },
        // 双击行事件
        onDblClickRow: function (row, $element) {
            var id = row.id;
            console.log(row);
            $viewOrEditByID(id, 'view');
        }
    });
}

// 格式化状态
function statusFormatter(value) {
    //0:无效,1:新建,2:审核通过,3:已发布
    //0:danger,1:default,2:primary,3:success
    switch (value) {
        case 0:
            return '<span class="label label-danger">无效</span>';
        case 1:
            return '<span class="label label-default">新建</span>';
        case 2:
            return '<span class="label label-primary">审核通过</span>';
        case 3:
            return '<span class="label label-success">已发布</span>';
        default:
            return '<span class="label label-danger">无效</span>';
    }
}

// 操作格式化
function actionFormatter(id) {
    return '<span class="link-button-table">' +
        //如果未发布
        '<a href="javascript:void(0)" onclick="$publishConfig('+id+')">发布</a>' +
        //如果已发布
        '<a href="javascript:void(0)" onclick="$rollback('+id+')">回滚</a>' +
        '<a href="javascript:void(0)" onclick="$dateilView('+id+')">详情</a>' +
        '</span>';
}

// 发布
$publishConfig = function(id){
    var url = "/api/config/publish/"+id;
    $.post(url,function (res) {
        console.log(res);
    },"json")
};

// 详情
$viewOrEditByID= function(id,viewOrEdit){
    var url = "/api/config/"+id;
    $.get(url,function (res) {
        console.log(res);
    },"json")
};

// 回滚
$rollback = function(id){
    var url = "/api/config/rollback/"+id;
    $.post(url,function (res) {
        console.log(res);
    },"json")
};
