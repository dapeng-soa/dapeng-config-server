$(document).ready(function () {
    InitWhiteList();
});

/**
 * 初始白名单
 * @constructor
 */
InitWhiteList = function () {
    var url = basePath + "/api/sysWhiteList";
    var whiteContext = "";
    $.get(url, function (res) {
        for (var i = 0; i < res.context.length; i++) {
            whiteContext += '<li ondblclick=delWhiteItem("' + res.context[i] + '") style="cursor: pointer" class="list-group-item">' + res.context[i] + '</li>'
        }
        $("#white-list-group").html(whiteContext);
    }, "json")
};

/**
 * 添加白名单
 */
addWhiteItem = function () {
    var text = $("#white-list-text").val();
    var url = basePath + "/api/white/add";
    $.post(url, {
        service: text
    }, function (res) {
        layer.msg(res.msg);
        InitWhiteList();
        $("#white-list-text").val("");
    }, "json")
};

/**
 * 删除白名单
 */
delWhiteItem = function (service) {
    bodyAbs();
    layer.confirm('删除白名单\n[' + service + ']?', {
        btn: ['确认', '取消']
    }, function () {
        url = basePath + "/api/white/del";
        $.post(url, {
            path: service
        }, function (res) {
            layer.msg(res.msg);
            rmBodyAbs();
        }, "json")
    }, function () {
        layer.msg("未做任何变更");
        rmBodyAbs();
    });
};

