$(document).ready(function () {
    InitWhiteList();
});

/**
 * 初始白名单
 * @constructor
 */
InitWhiteList = function () {
    var curl = basePath + "/api/clusters";
    $.get(curl, function (res) {
        if (res.code === SUCCESS_CODE) {
            var html = "";
            for (var i = 0; i < res.context.length; i++) {
                html += '<option value="' + res.context[i].id + '">' + res.context[i].zkHost + '</option>';
            }
            $("#whiteNodeSelect").html(html);
            refreshWhiteList($("#whiteNodeSelect").find("option:selected").val());
        }
    }, "json");
};

/**
 * 刷新白名单
 */
refreshWhiteList = function (cid) {
    var url = basePath + "/api/sysWhiteList";
    var whiteContext = "";
    $.get(url, {
        cid: cid === undefined ? $("#whiteNodeSelect").find("option:selected").val() : cid
    }, function (res) {
        for (var i = 0; i < res.context.length; i++) {
            whiteContext += '<li ondblclick=delWhiteItem("' + res.context[i] + '") style="cursor: pointer;white-space: pre-line;word-wrap: break-word;" class="list-group-item">' + res.context[i] + '</li>'
        }
        $("#white-list-group").html(whiteContext);
    }, "json")
};

/**
 * 所选节点变更时
 * @param obj
 */
nodeChanged = function (obj) {
    refreshWhiteList($(obj).find("option:selected").val());
};


/**
 * 添加白名单
 */
addWhiteItem = function () {
    var text = $("#white-list-text").val();
    if (text === "" || text === undefined || text.trim() === ""){
        layer.msg("请填写白名单");
        return;
    }
    var curl = basePath + "/api/clusters";
    $.get(curl, function (res) {
        if (res.code === SUCCESS_CODE) {
            var html = "<select style='width: 80%;margin: 0 auto' id='nodeSelect' class='form-control'>" +
                "<option value='-1'>全部</option>";
            for (var i = 0; i < res.context.length; i++) {
                html += '<option value="' + res.context[i].id + '">' + res.context[i].zkHost + '</option>';
            }
            html += '</select>';
            bodyAbs();
            // 选择集群添加
            layer.open({
                type: 1,
                title: '添加白名单至',
                content: html,
                btn: ['添加', '取消'],
                yes: function (index, layero) {
                    layer.close(index);
                    processAddWhiteList($("#nodeSelect").find("option:selected").val());
                }, btn2: function (index, layero) {
                    layer.msg("操作取消");
                }, cancel: function () {
                    layer.msg("操作取消");
                    //return false 开启该代码可禁止点击该按钮关闭
                }
            });
            rmBodyAbs();
        }
    },"json");

};

/**
 * 执行添加白名单
 */
processAddWhiteList = function (cid) {
    var text = $("#white-list-text").val();
    var url = basePath + "/api/white/add";
    $.post(url, {
        cid: cid,
        service: text
    }, function (res) {
        layer.msg(res.msg);
        if (res.code === SUCCESS_CODE) {
            refreshWhiteList();
        }
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
            cid: $("#whiteNodeSelect").find("option:selected").val(),
            path: service
        }, function (res) {
            layer.msg(res.msg);
            if (res.code === SUCCESS_CODE) {
                refreshWhiteList();
            }
            rmBodyAbs();
        }, "json")
    }, function () {
        layer.msg("未做任何变更");
        rmBodyAbs();
    });
};

