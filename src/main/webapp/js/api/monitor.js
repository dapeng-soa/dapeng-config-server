/**
 * @author huyj
 * @Created  2018/6/9 19:04
 */
document.write("<script language=javascript src='./js/api/base-common.js'></script>");

$(document).ready(function () {
    loadNodeList();
    setInterval('$($table).bootstrapTable("refresh")', 1000 * 60 * 5);
    $('#nodeSelect.selectpicker').on('change', function () {
        $($table).bootstrapTable("refresh");
    });

});

function InitMonitorTable() {
    //记录页面bootstrap-table全局变量$table，方便应用
    var queryUrl = basePath + '/api/serviceList';
    var rows = 10;
    $table = $('#monitor-table').bootstrapTable({
        url: queryUrl,                      //请求后台的URL（*）
        method: 'GET',                      //请求方式（*）
        responseHandler: function (res) {     //格式化返回数据
            //console.info(res)
            if (res.code === '4004') {
                //layer.msg(res.msg);
                showMessage("Error", res.msg, "加载失败");
                return {data: []};
            }
            return {data: res.context};
        },
        toolbar: '#toolbar',              //工具按钮用哪个容器
        striped: true,                      //是否显示行间隔色
        cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
        pagination: true,                   //是否显示分页（*）
        sortable: true,                     //是否启用排序
        sortOrder: "desc",                   //排序方式
        sidePagination: "client",           //分页方式：client客户端分页，server服务端分页（*）
        pageNumber: 1,                      //初始化加载第一页，默认第一页,并记录
        pageSize: rows,                     //每页的记录行数（*）
        pageList: [5, 10, 15, 20],        //可供选择的每页的行数（*）
        search: true,                      //是否显示表格搜索
        strictSearch: false,                 //设置为 true启用全匹配搜索，否则为模糊搜索。
        showColumns: true,                  //是否显示所有的列（选择显示的列）
        showRefresh: true,                  //是否显示刷新按钮
        minimumCountColumns: 2,             //最少允许的列数
        clickToSelect: true,                //是否启用点击选中行
        height: 700,                      //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
        uniqueId: "id",                     //每一行的唯一标识，一般为主键列
        showToggle: false,                   //是否显示详细视图和列表视图的切换按钮
        cardView: false,                    //是否显示详细视图
        detailView: true,                  //是否显示父子表
        //得到查询的参数
        queryParams: function (params) {
            //这里的键的名字和控制器的变量名必须一致，这边改动，控制器也需要改成一样的
            return {
                nodeHost: $('#nodeSelect.selectpicker').val(),
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
            title: '序号',
            formatter: indexFormatter
        }, {
            field: 'serviceName',
            title: '服务名',
            sortable: true
        }, {
            field: 'runStatus',
            /*events: runStatusOperateEvents,*/
            title: '服务状态',
            align: 'center',
            valign: 'middle',
            formatter: serviceRunStatusFormatter
        }, {
            field: 'healthStatus',
            title: '健康度',
            sortable: true,
            align: 'center',
            valign: 'middle',
            formatter: healthStatusFormatter
        }/*, {
            field: 'notes',
            title: '备注',
            sortable: true
        }*/],
        onLoadSuccess: function () {
            console.info("onLoadSuccess")
            $("[data-toggle='tooltip']").tooltip();
        },
        onPageChange: function (number, size) {
            //表格页面切换事件
            console.info("onPageChange")
            $("[data-toggle='tooltip']").tooltip();
        },
        onLoadError: function () {
            layer.msg("数据加载失败");
        },
        // 双击行事件
        onDblClickRow: function (row) {
            console.info("onDblClickRow")
            //var id = row.serviceName;
            //viewOrEditByID(id, 'view');
            //$table.bootstrapTable('expandRow', 1);
        },
        //注册加载子表的事件。你可以理解为点击父表中+号时触发的事件-- 实例信息表
        onExpandRow: function (index, row, $detail) {
            var cur_table = $detail.html('<table></table>').find('table');
            //console.info(row.instanceList)
            $(cur_table).bootstrapTable({
                clickToSelect: true,
                detailView: true,//父子表
                columns: [{
                    field: 'instance',
                    title: '服务实例'
                }, {
                    field: 'callCount',
                    title: '调用次数',
                    sortable: true,
                    align: 'center',
                    valign: 'middle'
                }, {
                    field: 'averageTime',
                    title: '平均耗时[ms]',
                    sortable: true,
                    align: 'center',
                    valign: 'middle'
                }, {
                    field: 'failCount',
                    title: '失败次数',
                    sortable: true,
                    align: 'center',
                    valign: 'middle'
                }, {
                    field: 'containerPool',
                    title: '线程池[activeCount/poolSize]',
                    align: 'center',
                    valign: 'middle'
                }, {
                    field: 'containerTask',
                    title: '任务队列[waiting/complete/total]',
                    align: 'center',
                    valign: 'middle'
                }, {
                    field: 'runStatus',
                    title: '状态',
                    align: 'center',
                    valign: 'middle',
                    formatter: instanceStatusFormatter
                }, {
                    field: 'operate',
                    title: '操作',
                    align: 'center',
                    valign: 'middle',
                    formatter: instanceOperateFormatter,
                    events: instanceOperateEvents
                }],
                //注册加载子表的事件。你可以理解为点击父表中+号时触发的事件-- 方法信息
                onExpandRow: function (index, row, $detail) {
                    var cur_table = $detail.html('<table></table>').find('table');
                    $(cur_table).bootstrapTable({
                        url: basePath + '/api/loadMethodInfo',   //请求后台的URL（*）
                        method: 'GET',                      //请求方式（*）
                        responseHandler: function (res) {     //格式化返回数据
                            //console.info(res)
                            if (res.code == '4004') {
                                //layer.msg(res.msg);
                                showMessage("Error", res.msg, "加载失败");
                                return {data: []};
                            }
                            return {data: res.context};
                        },
                        //得到查询的参数
                        queryParams: function (params) {
                            //这里的键的名字和控制器的变量名必须一致，这边改动，控制器也需要改成一样的
                            /*console.info(row.instance)
                            console.info($('#nodeSelect.selectpicker').val())
                            console.info($($detail.parents("tr.detail-view")[1]).prev().find("td").eq(2).html())*/
                            //console.info($($detail.parents("tr.detail-view")[1]).prev().find("td").eq(2).html())
                            return {
                                zkNode: $('#nodeSelect.selectpicker').val(),
                                instance: row.instance,
                                serviceName: $($detail.parents("tr.detail-view")[1]).prev().find("td").eq(2).html()
                            };
                        },
                        columns: [{
                            field: 'methodName',
                            sortable: true,
                            title: '方法名称'
                        }, {
                            field: 'maxTime',
                            title: '最大耗时[ms]',
                            sortable: true,
                            align: 'center',
                            valign: 'middle'
                        }, {
                            field: 'averageTime',
                            title: '平均耗时[ms]',
                            sortable: true,
                            align: 'center',
                            valign: 'middle'
                        }, {
                            field: 'callCount',
                            title: '调用次数',
                            sortable: true,
                            align: 'center',
                            valign: 'middle'
                        }, {
                            field: 'failCount',
                            title: '失败次数',
                            sortable: true,
                            align: 'center',
                            valign: 'middle'
                        }]
                    });
                    //console.info(row.methodList)
                    //$(cur_table).bootstrapTable("load", row.methodList);
                }
            });
            $(cur_table).bootstrapTable("load", row.instanceList);
        }
    });
}


function loadNodeList() {
    $('#nodeSelect.selectpicker').selectpicker({
        liveSearch: true,
        maxOptions: 1,
        width: '180'
    });
    $.ajax({
        url: "/api/loadNodes",
        type: "get",
        dataType: "json",
        data: 'data',
        success: function (data) {
            //console.info(data.context)
            $.each(data.context, function (i) {
                $('#nodeSelect.selectpicker').append("<option value=" + data.context[i].id + ">" + data.context[i].zkHost + "</option>");
            });
            $('#nodeSelect').selectpicker('refresh');
        },
        error: function (data) {
            console.info("load node data failed ...")
        }
    }).done(function (value) {
        InitMonitorTable();
    });
}


/-----------------------------------------------------------------------------------------------------------------/
serviceRunStatusFormatter = function (value, row, index) {
    if (value) {
        //var instanceInfo = row.instanceList.join("</br>");
        var instanceInfo = "";
        row.instanceList.forEach(function (node, index) {
            // console.info(node.instance)
            instanceInfo += node.instance + "  </br>"
        });
        return "<span class='label label-success'>AVAILABLE</span><a href='javascript:void(0)' data-toggle='tooltip' data-placement='right' data-html='true' title='" + instanceInfo + "'> <span class='badge'>" + row.instanceSize + "</span></a>";
    } else {
        return "<span class='label label-warning'>UN-AVAILABLE</span>";
    }
}

healthStatusFormatter = function (value, row, index) {
    if (value == 1) {//没有可用实力
        return "<span class='label label-warning'>UNAVAILABLE</span></a>";
    } else if (value == 2) {//所有实例都不可用
        return "<span class='label label-danger'>DANGER</span>";
    } else if (value == 3) {//部分实例可用
        return "<span class='label label-warning'>WARNING</span></a>";
    } else if (value == 4) {//所有实例 都可用
        return "<span class='label label-success'>STRONG</span></a>";
    } else {//未知
        return "<span class='label label-info'>UNKNOWN</span>";
    }
}


instanceStatusFormatter = function (value, row, index) {
    if (value) {
        return "<span class='label label-warning'>ON-LINE</span>";
    } else {
        return "<span class='label label-warning'>OFF-LINE</span>";
    }
}

instanceOperateFormatter = function (value, row, index) {
    return "<button type='button' id ='inst_down' class='btn btn-info btn-xs' style='text-shadow: black 4px 2px 2px;'> <span class='glyphicon glyphicon-sort-by-attributes-alt'></span>&nbsp;降级</button>&nbsp;" +
        "<button type='button' id ='inst_close' class='btn btn-danger btn-xs' style='text-shadow: black 4px 2px 2px;'> <span class='glyphicon glyphicon-trash'></span>&nbsp;屏蔽</button>";
}

window.instanceOperateEvents = {
    "click #inst_close": function (e, value, row, index) {
        showMessage("Warn", row, "此功能正在努力开发中...");
    },
    "click #inst_down": function (e, value, row, index) {
        showMessage("Warn", row, "此功能正在努力开发中...");
    }
}


