$(document).ready(function () {
    InitMainTable();
});
var config = new api.Config();

function InitMainTable() {
    //记录页面bootstrap-table全局变量$table，方便应用
    var queryUrl = basePath + '/api/configs';
    var rows = 10;
    $table = $('#config-table').bootstrapTable({
        url: queryUrl,                      //请求后台的URL（*）
        method: 'GET',                      //请求方式（*）
        responseHandler: function (res) {     //格式化返回数据
            return {
                total: res.context.totalElements,
                rows: res.context.content
            };
        },
        toolbar: '#config-toolbar',              //工具按钮用哪个容器
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
            field: 'id',
            title: '#',
            formatter: numberFormatter
        }, {
            field: 'serviceName',
            title: '服务名',
            sortable: true
        }, {
            field: 'status',
            title: '状态',
            sortable: true,
            align: 'center',
            valign: 'middle',
            formatter: statusFormatter
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
            formatter: actionFormatter
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

numberFormatter = function (value, row, index) {
    return index + 1;
};

// 格式化状态
statusFormatter = function (value) {
    //0:无效,1:新建,2:审核通过,3:已发布
    //0:danger,1:default,2:primary,3:success
    switch (value) {
        case 0:
            return '<span class="label label-danger">无效</span>';
        case 1:
            return '<span class="label label-default">新建>待审核</span>';
        case 2:
            return '<span class="label label-primary">已审>待发布</span>';
        case 3:
            return '<span class="label label-success">已发布</span>';
        default:
            return '<span class="label label-danger">无效</span>';
    }
};

// 操作格式化
actionFormatter = function (id, row) {
    return config.exportTableActionContext(id, row);
};


/**
 * 保存配置
 */
saveconfig = function () {
    var url = basePath + "/api/config/add";
    var settings = {
        type: "post",
        url: url,
        data: JSON.stringify(processConfigData()),
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
 * 删除配置
 * @param id
 */
delConfig = function (id) {

    bodyAbs();
    layer.confirm('删除此当前配置？', {
        btn: ['删除', '取消']
    }, function () {
        var url = basePath + "/api/config/delete/" + id;
        var settings = {
            type: "post",
            url: url,
            dataType: "json",
            contentType: "application/json"
        };
        $.ajax(settings).done(function (res) {
            layer.msg(res.msg);
            if (res.code === SUCCESS_CODE) {
                refresh();
            }
        });
    }, function () {
        layer.msg("未做任何变更");
        rmBodyAbs();
    });
};

/**
 * 修改配置
 * @param id
 */
editedConfig = function (id) {
    var url = basePath + "/api/config/edit/" + id;
    var settings = {
        type: "post",
        url: url,
        data: JSON.stringify(processConfigData()),
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
 * 发布历史
 * @param id
 */
viewHistory = function (id, serviceName) {
    var url = basePath + "/api/config/publish-history/" + id;
    $.get(url, function (res) {
        layer.msg(res.msg);
        if (res.code === SUCCESS_CODE) {
            openPublishHistory(serviceName);
            processHistoryData(res.context);
        }
    }, "json")
};

processHistoryData = function (data) {

    var historyHtml = "";
    for (var i = 0; i < data.length; i++) {

        historyHtml += '<div class="layui-colla-item">';
        historyHtml += '<h2 class="layui-colla-title" onclick="toggleBlock(this)">v-' + data[i].version + '</h2>';
        historyHtml += '<div class="advance-format-content">' +
            '<h4>超时配置:<h4/>' +
            '<pre>' + data[i].timeoutConfig + '<pre/>' +
            '<h4>负载均衡:<h4/>' +
            '<pre>' + data[i].loadbalanceConfig + '<pre/>' +
            '<h4>路由配置:<h4/>' +
            '<pre>' + data[i].routerConfig + '<pre/>' +
            '<h4>限流配置:<h4/>' +
            '<pre>' + data[i].freqConfig + '<pre/>' +
            '</div>';
        historyHtml += '</div>'
    }
    $("#publishHistory").html(historyHtml);
};

/**
 * 发布配置
 * @param id
 */
publishConfig = function (id) {

    var curl = basePath + "/api/clusters";
    $.get(curl, function (res) {
        console.log(res);
        if (res.code === SUCCESS_CODE) {
            var html = "<select style='width: 80%;margin: 0 auto' id='nodeSelect' class='form-control'>" +
                "<option value='-1'>全部</option>";
            for (var i = 0; i < res.context.length; i++) {
                html += '<option value="' + res.context[i].id + '">' + res.context[i].zkHost + '</option>'
            }
            html += '</select>';
            bodyAbs();
            // 选择集群发布
            layer.open({
                type: 1,
                title: '请选择需要发布的集群',
                content: html,
                btn: ['确认发布', '取消发布'],
                yes: function (index, layero) {

                    var url = basePath + "/api/config/publish/" + id;
                    $.post(url, {
                        cid: $("#nodeSelect").find("option:selected").val()
                    }, function (res) {
                        layer.msg(res.msg);
                        if (res.code === SUCCESS_CODE) {
                            refresh();
                        }
                    }, "json")

                },
                btn2: function (index, layero) {
                    layer.msg("取消发布");
                }, cancel: function () {
                    layer.msg("取消发布");
                    //return false 开启该代码可禁止点击该按钮关闭
                }
            });
            rmBodyAbs();
        }
    }, "json");


    /*var url = basePath + "/api/config/publish/" + id;
    $.post(url, function (res) {
        layer.msg(res.msg);
        if (res.code === SUCCESS_CODE) {
            refresh();
        }
    }, "json")*/
};

/**
 * 配置详情
 * @param id
 * @param viewOrEdit
 */
viewOrEditByID = function (id, viewOrEdit) {
    var url = basePath + "/api/config/" + id;
    $.get(url, function (res) {
        // 导出弹窗内容模版
        var context = config.exportAddConfigContext(viewOrEdit, biz = res.context.serviceName, data = res.context);
        initModelContext(context, viewOrEdit === "view" ? refresh : function () {
        });
    }, "json")
};

/**
 * 同步服务配置
 * @param service
 */
viewRealConfig = function (service) {
    var url = basePath + "/api/config/sysRealConfig/";
    $.get(url, {
        serviceName: service
    }, function (res) {
        var realdata = res.context;
        realdata.serviceName = service;
        var context = config.exportAddConfigContext("real", biz = "", data = realdata);
        initModelContext(context, function () {
        });
    }, "json");
};

/**
 * 回滚配置
 * @param id
 */
rollback = function (id) {
    var url = basePath + "/api/config/rollback/" + id;
    $.post(url, function (res) {
        layer.msg(res.msg);
        refresh();
    }, "json")
};

/**
 * 获取配置关键信息
 * @returns
 */
processConfigData = function () {
    var serviceName = $("#service-name").val();
    var timeoutConfig = $("#timeout-config-area").val();
    var loadbalanceConfig = $("#loadbalance-config-area").val();
    var routerConfig = $("#router-config-area").val();
    var freqConfig = $("#freq-config-area").val();
    var remark = $("#remark-area").val();
    return {
        serviceName: serviceName,
        timeoutConfig: timeoutConfig,
        loadbalanceConfig: loadbalanceConfig,
        routerConfig: routerConfig,
        freqConfig: freqConfig,
        remark: remark
    };
};

/**
 * 清空配置
 */
clearConfigInput = function () {
    layer.confirm('清空已填写配置？', {
        btn: ['确认', '取消']
    }, function () {
        $("textarea.form-control").val("");
        layer.msg("已清空");
    }, function () {
        layer.msg("取消清空");
    });
};

/**
 * 打开添加配置弹窗
 */
openAddConfig = function () {
    // 导出弹窗内容模版
    var context = config.exportAddConfigContext("add");
    // 初始化弹窗
    initModelContext(context, refresh);
};


openPublishHistory = function (serviceName) {

    // 导出弹窗内容模版
    var context = config.exportPublishHistoryContext(serviceName);
    // 初始化弹窗
    initModelContext(context, refresh);
};

toggleBlock = function (a) {
    $(a).next(".advance-format-content").toggle();
};