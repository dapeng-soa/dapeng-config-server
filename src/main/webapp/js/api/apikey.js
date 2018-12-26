$(document).ready(function () {
    InitApiTable();
});
var config1 = new api.Config();

function InitApiTable() {
    var queryUrl = basePath + '/api/authkeys';
    var table = new BSTable("apikey-table", queryUrl, setColumns());
    table.onDblClickRow = function (row) {
        viewApiKeyOrEditByID(row.id, VIEW)
    };
    table.init();
}

setColumns = function () {
    return [{
        checkbox: false,
        visible: false//是否显示复选框
    }, {
        field: 'id',
        title: '#',
        formatter: indexFormatter

    }, {
        field: 'apiKey',
        title: 'ApiKey',
        sortable: true
    }, {
        field: 'password',
        title: '密码'
    }, {
        field: 'timeout',
        title: '超时时间',
        sortable: true
    }, {
        field: 'validated',
        title: '验证超时',
        sortable: true,
        formatter: validatedFormatter
    }, {
        field: 'status',
        title: '状态',
        sortable: true,
        formatter: dataStatusFormatter
    }, {
        field: 'biz',
        title: '所属业务'
    }, {
        field: 'ips',
        title: 'IP规则'
    }, {
        field: 'notes',
        title: '备注',
        sortable: true
    }, {
        field: 'id',
        title: '操作',
        width: 160,
        align: 'center',
        valign: 'middle',
        formatter: ApiActionFormatter
    }]
};

/**
 * @return {string}
 */
ApiActionFormatter = function (value, row, index) {
    return config1.exportApiKeyTableActionContext(value, row);
};

genKey = function () {
    $.get(basePath + "/api/apikey/genkey", function (res) {
        result(res) ? $("#authApikey").val(result(res)) : {};
    }, "json")
};

openSendApiKey = function (id) {
    layer.open({
        type: 1,
        title: '下发ApiKey至',
        area: ['500px', 'auto'],
        content: config1.exportApiKeySend(),
        btn: ['下发', '取消'],
        yes: function (index, layero) {
            var mail = $("#sendKeyEmail").val();
            if (mail !== undefined && mail !== "") {
                var url = basePath + "/api/apikey/send/" + id;
                $.post(url, {email: mail}, function (res) {
                    showMessage(SUCCESS, res.msg);
                    layer.close(index);
                }, "json")
            } else {
                showMessage(WARN, "请填写邮箱！");
            }
        }, btn2: function (index, layero) {
            showMessage(INFO, "操作取消");
        }, cancel: function () {
            showMessage(INFO, "操作取消");
        }
    });
};

/**
 * 超时验证状态
 * @param value
 * @returns {string}
 */
validatedFormatter = function (value) {
    //0:默认验证,1:不验证超时
    //0:default,1:danger
    switch (value) {
        case 0:
            return '<span class="label label-default">验证</span>';
        case 1:
            return '<span class="label label-danger">不验证</span>';
        default:
            return '<span class="label label-default">验证</span>';
    }
};

/**
 * 超时状态
 * @param value
 * @returns {string}
 */
dataStatusFormatter = function (value) {
    //0:有效,1:禁用
    //0:success,1:danger
    switch (value) {
        case 0:
            return '<span class="label label-success">有效</span>';
        case 1:
            return '<span class="label label-danger">禁用</span>';
        default:
            return '<span class="label label-success">有效</span>';
    }
};

openAddApiKeyModle = function () {
    // 导出弹窗内容模版
    var context = config1.exportAddApiKeyContext("add");
    // 初始化弹窗
    initModelContext(context, refresh);
    setTextareaFull();
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
        showMessage(INFO, res.msg);
        if (res.code === SUCCESS_CODE) {
            refresh();
        }
    });
};

/**
 * 清空配置
 */
clearApiKeyInput = function () {
    layer.confirm('将清空当前所有输入？', {
        btn: ['确认', '取消']
    }, function (index) {
        $("textarea.form-control").val("");
        showMessage(SUCCESS, "已清空");
        layer.close(index);
    }, function () {
        showMessage(INFO, "取消清空");
    });
};

processApiKeyData = function () {
    var authApikey = $("#authApikey").val();
    var authPassWord = $("#authPassWord").val();
    var authBiz = $("#authBiz").val();
    var authIps = $("#authIps").val();
    var notes = $("#notes").val();
    var timeout = $("#authTimeout").val();
    var validated = $("#authValidated").find("option:selected").val();
    return {
        apiKey: authApikey,
        password: authPassWord,
        biz: authBiz,
        ips: authIps,
        notes: notes,
        timeout: timeout,
        validated: validated
    }
};

/**
 * 修改
 * @param id
 * @param op
 */
viewApiKeyOrEditByID = function (id, op) {
    var url = basePath + "/api/apikey/" + id;
    $.get(url, function (res) {
        // 导出弹窗内容模版
        var context = config1.exportAddApiKeyContext(op, "", res.context);
        // 初始化弹窗
        initModelContext(context, refresh);
        setTextareaFull();
    }, "json");
};

/**
 * 修改apikey
 * @param id
 */
editedApiKey = function (id) {
    var url = basePath + "/api/apikey/edit/" + id;

    var settings = {
        type: "post",
        url: url,
        data: JSON.stringify(processApiKeyData()),
        dataType: "json",
        contentType: "application/json"
    };
    $.ajax(settings).done(function (res) {
        showMessage(INFO, res.msg);
        if (res.code === SUCCESS_CODE) {
            refresh();
        }
    });
};

delApiKey = function () {
    showMessage(ERROR, "暂无权限")
};


