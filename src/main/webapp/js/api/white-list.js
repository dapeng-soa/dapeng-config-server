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
            whiteContext += '<li class="list-group-item">' + res.context[i] + '</li>'
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