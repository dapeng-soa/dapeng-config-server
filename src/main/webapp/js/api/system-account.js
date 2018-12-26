var bsTable = {};
var roleTable = {};
var $$ = new api.Api();
var system = new api.System();
$(document).ready(function () {
    initSystemAccounts();
});

function initSystemAccounts() {
    var queryUrl = basePath + '/api/system-users';
    var table = new BSTable("system-account-table", queryUrl, setColumns());
    table.params = function (ps) {
        ps.sort = "username";
        return ps;
    };
    table.responseHandler = function (res) {
        return {
            total: res.context == null ? 0 : res.context.totalElements,
            rows: res.context.content
        };
    };
    table.init();
    bsTable = table;
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
        field: 'username',
        title: '用户名',
        sortable: true
    }, {
        field: 'enabled',
        title: '状态',
        align: 'center',
        valign: 'middle',
        formatter: statusFormatter
    }, {
        field: 'nickname',
        title: '昵称',
        sortable: true
    }, {
        field: 'email',
        title: 'email'
    }, {
        field: 'tel',
        title: '电话'
    }, {
        field: 'remark',
        title: '备注'
    }, {
        field: 'id',
        title: '操作',
        width: 160,
        align: 'center',
        valign: 'middle',
        formatter: systemUserActionFormatter
    }]
};


statusFormatter = function (value) {
    switch (Number(value)) {
        case 0:
            return '<span class="label label-success" title="' + value + '">启用</span>';
        case -1:
            return '<span class="label label-danger" title="' + value + '">禁用</span>';
        default:
            return '<span class="label label-danger" title="' + value + '">禁用</span>';
    }
};

systemUserActionFormatter = function (value, row, index) {
    return system.exportSystemUsersActionContext(value, row);
};

resetUserPwd = function (id, username) {
    layer.confirm('重置用户' + username + '密码为默认(当前用户名)？', {
        btn: ['重置', '取消']
    }, function (index) {
        var url = basePath + "/api/system-user/reset-password/" + id;
        $$.post(url, {}, function (res) {
            showMessage(INFO, res.msg);
            if (res.code === SUCCESS_CODE) {
                layer.close(index);
                bsTable.refresh();
            }
        });
    }, function () {
        showMessage(WARN, "未做任何改动");
    });
};

viewSystemUserOrEditByID = function (id, op) {
    var url = basePath + "/api/system-user/" + id;
    $.get(url, function (res) {
        var context = system.exportAddSystemUserContext(op, "", res.context);
        initModelContext(context, function () {
            bsTable.refresh();
        });
    }, "json");
};

openLinkRole = function (id, name) {
    var context = system.exportUserLinkRoleContext(id, name);
    initModelContext(context, function () {
        bsTable.refresh();
    });
    initSystemRole(id);
};

/**
 * 弹窗里的角色
 */
function initSystemRole(id) {
    var queryUrl = basePath + '/api/system-roles';
    var table = new BSTable("system-role-table", queryUrl, setRoleColumns());
    table.params = function (ps) {
        return ps;
    };
    table.responseHandler = function (res) {
        return {
            total: res.context.length,
            rows: res.context
        };
    };
    table.onCheck = function (row) {
        linkingRole(id, [row.id])
    };
    table.onUncheck = function (row) {
        unLinkingRole(id, [row.id])
    };
    table.onCheckAll = function (rows) {
        linkingRole(id, getRoleIds(rows))
    };
    table.onUncheckAll = function (rows) {
        unLinkingRole(id, getRoleIds(rows))
    };
    table.onLoadSuccess = function () {
        var url = basePath + "/api/system-role/" + id;
        $.get(url, function (res) {
            if (res.code === SUCCESS_CODE) {
                var rids2 = [];
                for (var i in res.context) {
                    rids2.push(res.context[i].roleId);
                }
                table.checkBy("id", rids2);
            }
        }, "json");
    };
    table.init();
    roleTable = table;
}

getRoleIds = function (rows) {
    var rids = [];
    for (var i in rows) {
        rids.push(rows[i].id);
    }
    return rids;
};

setRoleColumns = function () {
    return [{
        checkbox: true,
        visible: true
    }, {
        field: 'id',
        title: '#',
        formatter: indexFormatter

    }, {
        field: 'role',
        title: '角色名',
        sortable: true
    }, {
        field: 'remark',
        title: '备注',
        sortable: true
    }]
};


linkingRole = function (userId, roles) {
    var url = basePath + "/api/system-user/link-role";
    $$.post(url, JSON.stringify({userId: userId, roleIds: roles}), function (res) {
        showMessage(INFO, res.msg);
    }, "application/json");
};

unLinkingRole = function (userId, roles) {
    var url = basePath + "/api/system-user/unlink-role";
    $$.post(url, JSON.stringify({userId: userId, roleIds: roles}), function (res) {
        showMessage(INFO, res.msg);
    }, "application/json");
};

disabledSystemUser = function (id, username, status) {
    var title = "";
    switch (status) {
        case 0:
            title = "禁用用户" + username + "？";
            break;
        case -1:
            title = "启用用户" + username + "？";
            break;
        default:
            title = "禁用用户" + username + "？";
    }
    layer.confirm(title, {
        btn: ['执行', '取消']
    }, function (index) {
        var url = basePath + "/api/system-user/switch-status/" + id;
        $$.post(url, {}, function (res) {
            showMessage(INFO, res.msg);
            if (res.code === SUCCESS_CODE) {
                layer.close(index);
                bsTable.refresh();
            }
        });
    }, function () {
        showMessage(WARN, "未做任何改动");
    });
};

addSystemUser = function () {
    var context = system.exportAddSystemUserContext(ADD);
    initModelContext(context, function () {
        bsTable.refresh();
    });
};

saveSystemUser = function () {
    var url = basePath + "/api/system-user/add";
    $$.post(url, JSON.stringify(processSystemUserData()), function (res) {
        showMessage(INFO, res.msg);
        if (res.code === SUCCESS_CODE) {
            closeModel();
        }
    }, "application/json");
};

processSystemUserData = function () {
    var username = $("#username").val();
    var nickname = $("#nickname").val();
    var email = $("#email").val();
    var tel = $("#tel").val();
    var remark = $("#remark-area").val();
    return {
        username: username,
        nickname: nickname,
        email: email,
        tel: tel,
        remark: remark
    }
};

clearSystemUserInput = function () {

};
/**
 * 修改账号
 * @param id
 */
editedSystemUser = function (id) {
    var url = basePath + "/api/system-user/edit/" + id;
    $$.post(url, JSON.stringify(processSystemUserData()), function (res) {
        showMessage(INFO, res.msg);
        if (res.code === SUCCESS_CODE) {
            closeModel();
        }
    }, "application/json");
};


reloadTable = function () {
    bsTable.refresh();
};

