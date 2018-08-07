// 后台返回状态码
window.SUCCESS_CODE = 200;
window.ERROR_CODE = 4004;
// socket 连接地址
window.SOCKET_REG_INFO = "web1:192.168.0.109";
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
window.GET_SERVER_TIME_RESP = "getServerTimeResp";
window.GET_SERVER_TIME = "getServerTime";
window.DEPLOY = "deploy";
window.STOP = "stop";
window.RESTART = "restart";
window.GET_YAML_FILE = "getYamlFile";
window.GET_YAML_FILE_RESP = "getYamlFileResp";
// 常用状态
window.SUCCESS = "success";
window.WARN = "warn";
window.WARN = "warn";
window.ERROR = "error";
window.INFO = "info";


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


// 文本差异对比
window.diffTxt = function (current, old) {
    var mergely = $('#mergely');
    mergely.mergely({
        license: 'gpl',
        autoresize: true,
        sidebar: false,
        cmsettings: {
            readOnly: false
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

window.indexFormatter = function (value, row, index) {
    return index + 1;
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

$.fn.extend({
    animateCss: function(animationName, callback) {
        var animationEnd = (function(el) {
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

        this.addClass('animated ' + animationName).one(animationEnd, function() {
            $(this).removeClass('animated ' + animationName);

            if (typeof callback === 'function') callback();
        });
        return this;
    }
});