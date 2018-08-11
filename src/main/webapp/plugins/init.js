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
window.GET_YAML_FILE = "getYamlFile";
window.GET_YAML_FILE_RESP = "getYamlFileResp";
window.ERROR_EVENT = "errorEvent";
// 常用状态
window.SUCCESS = "success";
window.WARN = "warn";
window.WARN = "warn";
window.ERROR = "error";
window.INFO = "info";

// 常用视图
window.VIEW = "view";
window.ADD = "add";
window.EDIT = "edit";


layui.use('element', function () {
    var element = layui.element;

    //一些事件监听
    element.on('nav(filter)', function (elem) {
        console.log(elem); //得到当前点击的DOM对象
    });

    element.on('collapse(filter)', function (data) {
        console.log(data.show); //得到当前面板的展开状态，true或者false
        console.log(data.title); //得到当前点击面板的标题区域DOM对象
        console.log(data.content); //得到当前点击面板的内容区域DOM对象
    });
});

window.layer = {};

layui.use('layer', function () {
    window.layui = layui.layer;
});

// 刷新页面
window.refresh = function () {
    window.location.reload();
};

// 自定义手风琴
window.toggleBlock = function (a) {
    $(a).next(".advance-format-content").toggle();
};

window.result = function (res) {
    if (res.code === SUCCESS_CODE){
        return res.context;
    }else {
        layer.msg(res.msg);
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


// table
// 序号
window.indexFormatter = function (value, row, index) {
    return index + 1;
};

(function () {
    var BSTable = function (bstableId, url, columns) {
        this.btInstance = null;                 //jquery和BootStrapTable绑定的对象
        this.bstableId = bstableId;
        this.url = url;
        this.method = "get";
        this.paginationType = "server";         //默认分页方式是服务器分页,可选项"client"
        this.toolbarId = bstableId+"Toolbar";
        this.columns = columns;
        this.height = 665;                      //默认表格高度665
        this.data = {};
        this.params = {}; // 向后台传递的自定义参数
        this.onLoadSuccess = function () {
        };
        this.onLoadError = function () {
            layer.msg("数据加载失败");
        };
        this.onDblClickRow = function (row) {};
        // 可格式化处理返回参数
        this.responseHandler = function (res) {
            return {
                total: res.context == null ? 0 : res.context.length,
                rows: res.context
            };
        };

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
                    pageSize: 14,               //每页的记录行数（*）
                    pageList: [5, 10, 15, 20],    //可供选择的每页的行数（*）
                    search: true,              //是否显示表格搜索，此搜索是客户端搜索，不会进服务端
                    strictSearch: false,         //设置为 true启用 全匹配搜索，否则为模糊搜索
                    showColumns: true,          //是否显示所有的列
                    showRefresh: true,          //是否显示刷新按钮
                    minimumCountColumns: 2,     //最少允许的列数
                    clickToSelect: true,        //是否启用点击选中行
                    searchOnEnterKey: true,     //设置为 true时，按回车触发搜索方法，否则自动触发搜索方法
                    pagination: true,           //是否显示分页条
                    height: this.height,
                    uniqueId: "id",                     //每一行的唯一标识，一般为主键列
                    showToggle: true,                   //是否显示详细视图和列表视图的切换按钮
                    cardView: ($(window).width() < 1024),                    //是否显示详细视图
                    detailView: false,                  //是否显示父子表
                    queryParamsType: 'limit',   //默认值为 'limit' ,在默认情况下 传给服务端的参数为：offset,limit,sort
                    queryParams: function (param) {
                        return $.extend(this.params, param);
                    }, // 向后台传递的自定义参数
                    columns: this.columns,      //列数组
                    onLoadSuccess: this.onLoadSuccess, //载入成功
                    onLoadError: this.onLoadError, //载入失败
                    // 双击行事件
                    onDblClickRow: this.onDblClickRow
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
            console.log(parms);
            if (typeof parms != "undefined") {
                this.btInstance.bootstrapTable('refresh', parms);
            } else {
                this.btInstance.bootstrapTable('refresh');
            }
        }
    };

    window.BSTable = BSTable;
}());