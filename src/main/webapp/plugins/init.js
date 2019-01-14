// 后台返回状态码
window.SUCCESS_CODE = 200;
window.ERROR_CODE = 4004;
// socket 注册信息
window.SOCKET_REG_INFO = "web:192.168.0.666";
// socket 系统事件消息类型
window.SOC_CONNECT = "connect";
window.SOC_CDISCONNECT = "disconnect";
window.CONNECT_ERROR = "connect_error";
window.CONNECT_TIMEOUT = "connect_timeout";
// socket 自定义事件消息类型
window.NODE_REG = "nodeReg";
window.NODE_EVENT = "nodeEvent";
window.WEB_REG = "webReg";
window.WEB_EVENT = "webEvent";
window.GET_SERVER_INFO_RESP = "getServerInfoResp";
window.GET_SERVER_INFO = "getServerInfo";
window.DEPLOY = "deploy";
window.DEPLOY_RESP = "deployResp";
window.STOP = "stop";
window.STOP_RESP = "stopResp";
window.RESTART = "restart";
window.RESTART_RESP = "restartResp";
window.RM_CONTAINER = "rmContainer";
window.RM_CONTAINER_RESP = "rmContainerResp";
window.GET_YAML_FILE = "getYamlFile";
window.GET_YAML_FILE_RESP = "getYamlFileResp";
window.GET_REGED_AGENTS = "getRegedAgents";
window.BUILD = "build";
window.BUILD_RESP = "buildResp";
window.BUILDING = "building"; // 构建中的
window.BUILDING_LIST = "buildingList";
window.GET_BUILD_PROGRESSIVE = "getBuildProgressive";
window.GET_BUILD_PROGRESSIVE_RESP = "getBuildProgressiveResp";
window.GET_REGED_AGENTS_RESP = "getRegedAgentsResp";
window.SYNC_NETWORK = "syncNetwork";
window.SYNC_NETWORK_RESP = "syncNetworkResp";
window.CMD_EVENT = "cmd";
window.CMD_RESP = "cmdResult";
window.CMD_EXITED = "cmdExited";
window.ERROR_EVENT = "errorEvent";
// 常用状态
window.SUCCESS = "success";
window.WARN = "warn";
window.WARN = "warn";
window.ERROR = "error";
window.INFO = "info";

// 常用标签颜色
window.LABEL_COLORS = ["label label-default", "label label-primary", "label label-success", "label label-info", "label label-warning", "label label-danger"];
/**
 * @return {string}
 */
window.RANDOM_LABEL = function () {
    return LABEL_COLORS[Math.floor(Math.random() * (LABEL_COLORS.length))]
};

window.PRIMARY_LABEL = "label label-primary";
window.SUCCESS_LABEL = "label label-success";

// 常用视图
window.VIEW = "view";
window.ADD = "add";
window.EDIT = "edit";

window.setTextareaFull = function () {
    setTimeout(function () {
        $('textarea.form-control').each(function () {
            this.setAttribute('style', 'height:' + (this.scrollHeight) + 'px;overflow-y:hidden;');
        }).on('input', function () {
            this.style.height = 'auto';
            this.style.height = (this.scrollHeight) + 'px';
        })
    }, 200);
};

window.clearTextareaFull = function () {

};

window.layer = {};

layui.use('layer', function () {
    window.layui = layui.layer;
});

// 刷新页面
window.refresh = function () {
    window.location.reload();
};
window.backPrePage = function () {
    window.history.back();
};

// 自定义手风琴
window.toggleBlock = function (a) {
    $(a).next(".advance-format-content").toggle();
};

window.result = function (res) {
    if (res.code === SUCCESS_CODE) {
        return res.context;
    } else {
        showMessage(ERROR, res.msg);
        return false;
    }
};


// 文本差异对比
window.diffTxt = function (current, old) {
    var mergely = $('#mergely');
    mergely.mergely({
        license: 'gpl',
        autoresize: true,
        sidebar: false,
        viewport: false,
        cmsettings: {
            readOnly: true
        },
        lhs: function (setValue) {
            setValue(old);
        },
        rhs: function (setValue) {
            setValue(current);
        }
    });
    mergely.resize();
    return mergely;
};

window.showMessage = function (type, mesage, title) {
    toastr.options = {
        positionClass: "toast-bottom-right",
        timeOut: "2000",
        closeButton: true
    }

    switch (type) {
        case SUCCESS:
            if (title) {
                toastr.success(mesage, title);
            } else {
                toastr.success(mesage);
            }
            break;
        case WARN:
            if (title) {
                toastr.warning(mesage, title);
            } else {
                toastr.warning(mesage);
            }
            break;
        case ERROR:
            if (title) {
                toastr.error(mesage, title);
            } else {
                toastr.error(mesage);
            }
            break;
        case INFO:
            if (title) {
                toastr.info(mesage, title);
            } else {
                toastr.info(mesage);
            }
            break;
        default:
            if (title) {
                toastr.info(mesage, title);
            } else {
                toastr.info(mesage);
            }
    }
};

window.clearMessage = function () {
    toastr.clear();
};

/**
 * @return {string}
 */
window.unix2Time = function (unix) {
    var unixTimestamp = new Date(unix * 1000);
    return unixTimestamp.toLocaleString();
};

$.fn.extend({
    animateCss: function (animationName, callback) {
        var animationEnd = (function (el) {
            var animations = {
                animation: 'animationend',
                OAnimation: 'oAnimationEnd',
                MozAnimation: 'mozAnimationEnd',
                WebkitAnimation: 'webkitAnimationEnd'
            };

            for (var t in animations) {
                if (el.style[t] !== undefined) {
                    return animations[t];
                }
            }
        })(document.createElement('div'));

        this.addClass('animated ' + animationName).one(animationEnd, function () {
            $(this).removeClass('animated ' + animationName);

            if (typeof callback === 'function') callback();
        });
        return this;
    }
});

window.selectRefresh = function () {
    $(".selectpicker").selectpicker('refresh');
};

// table
// 序号
window.indexFormatter = function (value, row, index) {
    return index + 1;
};
// table
//
window.tagsFormatter = function (value, row, index) {
    var tags = value.split(",");
    var res = "";
    for (var i in tags) {
        var lable = SUCCESS_LABEL;
        res += '<span class="' + lable + '">' + tags[i] + '</span> ';
    }
    return res;
};

window.initTags = function (data) {
    var t = $("input[data-role='tagsinput']");
    t.tagsinput({
        tagClass: function () {
            return SUCCESS_LABEL;
        }
    });
    t.tagsinput('refresh');
    t.tagsinput('add', data);
};

(function () {
    var BSTable = function (bstableId, url, columns) {
        this.btInstance = null;                 //jquery和BootStrapTable绑定的对象
        this.bstableId = bstableId;
        this.url = url;
        this.method = "get";
        this.paginationType = "server";         //默认分页方式是服务器分页,可选项"client"
        this.toolbarId = bstableId + "Toolbar";
        this.columns = columns;
        this.height = 700;                      //默认表格高度700
        this.data = {};
        this.showFullscreen = false;
        this.onLoadSuccess = function () {
        };
        this.onLoadError = function () {
        };
        this.onDblClickRow = function (row) {
        };
        this.onClickRow = function (row) {
        };
        this.onCheck = function (row) {
        };
        this.onUncheck = function (row) {
        };
        this.onCheckAll = function (rows) {
        };
        this.onUncheckAll = function (rows) {
        };
        // 可格式化处理返回参数
        this.responseHandler = function (res) {
            return {
                total: res.context == null ? 0 : res.context.length,
                rows: res.context
            };
        };
        this.params = function (ps) {
            return ps;
        }; //向后台传递的自定义参数

    };

    BSTable.prototype = {
        /**
         * 初始化bootstrap table
         */
        init: function () {
            var tableId = this.bstableId;
            this.btInstance =
                $('#' + tableId).bootstrapTable({
                    contentType: "application/x-www-form-urlencoded",
                    url: this.url,              //请求地址
                    method: this.method,        //ajax方式,post还是get
                    responseHandler: this.responseHandler,
                    toolbar: "#" + this.toolbarId,//顶部工具条
                    striped: true,              //是否显示行间隔色
                    cache: false,               //是否使用缓存,默认为true
                    pagination: true,           //是否显示分页（*）
                    sortable: true,             //是否启用排序
                    sortOrder: "desc",          //排序方式
                    sidePagination: this.paginationType,   //分页方式：client客户端分页，server服务端分页（*）
                    pageNumber: 1,                  //初始化加载第一页，默认第一页
                    pageSize: 15,               //每页的记录行数（*）
                    pageList: [5, 10, 15, 20, 30, 40, 50],    //可供选择的每页的行数（*）
                    search: true,              //是否显示表格搜索，此搜索是客户端搜索，不会进服务端
                    strictSearch: false,         //设置为 true启用 全匹配搜索，否则为模糊搜索
                    showColumns: true,          //是否显示所有的列
                    showRefresh: true,          //是否显示刷新按钮
                    minimumCountColumns: 2,     //最少允许的列数
                    clickToSelect: true,        //是否启用点击选中行
                    searchOnEnterKey: false,     //设置为 true时，按回车触发搜索方法，否则自动触发搜索方法
                    height: this.height,
                    showFullscreen: this.showFullscreen,
                    maintainSelected: true,
                    uniqueId: "id",                     //每一行的唯一标识，一般为主键列
                    showToggle: false,                   //是否显示详细视图和列表视图的切换按钮
                    cardView: ($(window).width() < 1024),                    //是否显示详细视图
                    detailView: false,                  //是否显示父子表
                    queryParamsType: 'limit',   //默认值为 'limit' ,在默认情况下 传给服务端的参数为：offset,limit,sort
                    queryParams: this.params, // 向后台传递的自定义参数
                    columns: this.columns,      //列数组
                    onLoadSuccess: this.onLoadSuccess, //载入成功
                    onLoadError: this.onLoadError, //载入失败
                    onDblClickRow: this.onDblClickRow, // 双击行事件
                    onClickRow: this.onClickRow, // 单击事件
                    onCheck: this.onCheck, // 当选择某行时触发
                    onUncheck: this.onUncheck, // 当反选某行时触发
                    onCheckAll: this.onCheckAll, // 全选所有的行时触发
                    onUncheckAll: this.onUncheckAll // 反选所有的行时触发
                });
            return this;
        },
        /**
         * 向后台传递的自定义参数
         * @param param
         */
        setQueryParams: function (param) {
            this.params = param;
        },
        /**
         * 设置分页方式：server 或者 client
         */
        setPaginationType: function (type) {
            this.paginationType = type;
        },
        /**
         * 获取所有的选中参数
         * @returns {*}
         */
        getAllSelections: function () {
            return this.btInstance.bootstrapTable('getAllSelections');
        },

        /**
         * 按值或数组选中某行，参数包含：
         * @param field 用于查找记录的字段的名称
         * @param values 要检查的行的值数组
         */
        checkBy: function (field, values) {
            this.btInstance.bootstrapTable("checkBy", {field: field, values: values});
        },


        /**
         * 设置ajax post请求时候附带的参数
         */
        set: function (key, value) {
            if (typeof key == "object") {
                for (var i in key) {
                    if (typeof i == "function")
                        continue;
                    this.data[i] = key[i];
                }
            } else {
                this.data[key] = (typeof value == "undefined") ? $("#" + key).val() : value;
            }
            return this;
        },

        /**
         * 设置ajax post请求时候附带的参数
         */
        setData: function (data) {
            this.data = data;
            return this;
        },

        /**
         * 清空ajax post请求参数
         */
        clear: function () {
            this.data = {};
            return this;
        },

        /**
         * 刷新 bootstrap 表格
         * Refresh the remote server data,
         * you can set {silent: true} to refresh the data silently,
         * and set {url: newUrl} to change the url.
         * To supply query params specific to this request, set {query: {foo: 'bar'}}
         */
        refresh: function (parms) {
            if (typeof parms != "undefined") {
                this.btInstance.bootstrapTable('refresh', parms);
            } else {
                this.btInstance.bootstrapTable('refresh');
            }
        }
    };

    window.BSTable = BSTable;
}());

/**
 * select 异步加载
 */
(function () {
    /**
     *
     * @param url 获取select的地址
     * @param selectId  DOM Id
     * @param v_column value 列
     * @param n_column name 列
     * @constructor
     */
    var BzSelect = function (url, selectId, v_column, n_column) {
        this.url = url; // 数据请求地址
        this.method = "get"; // 请求方法
        this.selectId = selectId; // selectId
        this.v_column = v_column; // val列
        this.n_column = n_column; // 内容列
        this.i_selected = 0; // 默认选中的index,第几个
        this.v_selected = undefined; // 默认选中的value
        this.defaultOption = true; // 默认的option
        this.refresh = false;
        this.responseHandler = function (res) {
            return res.context;
        }; //
        this.before = function () {
        }; // 初始化之前
        this.after = function () {
        }; // 初始化后
    };

    BzSelect.prototype = {
        init: function () {
            var ts = this;
            if (typeof ts.before === 'function') ts.before();
            var settings = {type: ts.method, url: ts.url, dataType: "json"};
            var el = $("#" + ts.selectId);
            $.ajax(settings).done(function (res) {
                if (res.code === SUCCESS_CODE) {
                    var contexts = ts.responseHandler(res);
                    var html = ts.defaultOption ? "<option value='0'>请选择</option>" : "";
                    $.each(contexts, function (i, em) {
                        var seled = "";
                        if (ts.v_selected !== undefined && ts.v_selected !== "") {
                            seled = em[ts.v_column] === ts.v_selected ? "selected" : "";
                        }
                        html += '<option ' + seled + ' value="' + em[ts.v_column] + '">' + em[ts.n_column] + '</option>';
                    });
                    $("#" + ts.selectId).html(html);
                    ts.refresh ? $("#" + ts.selectId).selectpicker('refresh') : {};
                    if (typeof ts.after === 'function') ts.after();
                }
            });
        },
        value: function () {
            var ts = this;
            return $("#" + ts.selectId).find("option:selected").val();
        },
        name: function () {
            var ts = this;
            return $("#" + ts.selectId).find("option:selected").label;
        }
    };

    window.BzSelect = BzSelect;

}());

(function () {
    var BzConslose = function () {
        this.cid = "ConsoleView";// 默认id
        this.drag = true; // 可拖动
    };
    BzConslose.prototype = {
        init: function () {
        },
        refreshConsole: function () {

        }

    }

}());


Date.prototype.pattern = function (fmt) {
    var o;
    o = {
        "M+": this.getMonth() + 1, //月份
        "d+": this.getDate(), //日
        "h+": this.getHours() % 24 === 0 ? 00 : this.getHours() % 24, //小时
        "H+": this.getHours(), //小时
        "m+": this.getMinutes(), //分
        "s+": this.getSeconds(), //秒
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度
        "S": this.getMilliseconds() //毫秒
    };
    var week = {
        "0": "/u65e5",
        "1": "/u4e00",
        "2": "/u4e8c",
        "3": "/u4e09",
        "4": "/u56db",
        "5": "/u4e94",
        "6": "/u516d"
    };
    if (/(y+)/.test(fmt)) {
        fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    }
    if (/(E+)/.test(fmt)) {
        fmt = fmt.replace(RegExp.$1, ((RegExp.$1.length > 1) ? (RegExp.$1.length > 2 ? "/u661f/u671f" : "/u5468") : "") + week[this.getDay() + ""]);
    }
    for (var k in o) {
        if (new RegExp("(" + k + ")").test(fmt)) {
            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
        }
    }
    return fmt;
}

/**
 *文档实际高度
 */
window.getScrollHeight = function () {
    return Math.max(document.body.scrollHeight, document.documentElement.scrollHeight);
};