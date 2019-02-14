var $$ = new api.Api();
var opTypes = ["addSamelevelMenu", "addSublevelMenu", "modifyMenu"];
var roleTable = {};
$(document).ready(function () {
    handlerInitTree();
});

/**
 * 加载菜单
 */
var handlerInitTree = function () {
    $$.$get("/api/system-menu/getMenuTree", function (res) {
        $('#menu-tree').treeview({
            data: res.context,
            color: "#0e5eaa",
            showBorder: true,
            selectedIcon: "glyphicon glyphicon-check",
            multiSelect: false,
            highlightSelected: false,
            onNodeSelected: function (event, data) {
                menuClick(data);
            }
        });
        initSystemRole();
    });
};


/**
 * 获取父节点
 * @param data
 * @returns {*|jQuery}
 */
var getParentNode = function (data) {
    return $('#menu-tree').treeview('getParent', data)
};
/**
 * 获取当前选中节点
 * @returns {*|jQuery}
 */
var getSelectedTreeNode = function () {
    var nodes = $('#menu-tree').treeview('getSelected');
    if (nodes.length > 0) {
        return nodes[0]
    } else {
        return null
    }
};

/**
 * 菜单单击
 */
var menuClick = function (menu) {
    if (menu.parentId !== undefined) {
        menu.parentNode = getParentNode(menu)
    } else {
        // 没有父节点
        menu.parentNode = {
            name: "",
            id: ""
        }
    }
    $("#menuId").val(menu.id);
    $("#parentId").val(menu.parentNode.id);
    $("#parentMenu").val(menu.parentNode.name);
    $("#menuName").val(menu.name);
    $("#menuUrl").val(menu.url);
    $("#menuRemark").val(menu.remark);
    $("#menuStatus").find("option[value='" + menu.status + "']").prop("selected", true);
    $("#defaultBtns").show();
    isWriteMenuBox(false, false);
    $("#appendBtns").hide();
    roleTable.refresh();
};

/**
 * 输入框的状态操作，读写
 * @param iswrite
 * @param exclude 排除的元素 eg:xx,xx
 */
var isWriteMenuBox = function (iswrite, isclean, exclude) {
    var ems = ["parentMenu", "menuName", "menuUrl", "menuRemark", "menuStatus"];
    if (exclude === undefined) {
        exclude = ""
    }
    for (var i in ems) {
        if (exclude.indexOf(ems[i]) === -1) {
            if (isclean) {
                $("#" + ems[i]).val("");
            }
            if (iswrite) {
                $("#" + ems[i]).removeAttr("disabled");
            } else {
                $("#" + ems[i]).attr("disabled", true);
            }
        }
    }
};

/**
 * 可能需要传递操作类型
 */
var showSaveAndCancel = function (opType) {
    $("#defaultBtns").hide();
    $("#appendBtns").show();
    $("#opType").val(opType);
};

/**
 * 检查数据正确性
 * @param menu
 */
var checkMenuData = function (menu) {
    return menu.name !== "" && menu.url !== "" && menu.status !== "";
};

/**
 * 保存菜单
 */
var saveMenu = function () {
    var menu = handlerGetMenuData();
    if (checkMenuData(menu)) {
        var opType = $("#opType").val();
        switch (opType) {
            case opTypes[0]:
                $$.post("/api/system-menu/add", JSON.stringify(menu), function (res) {
                    showMessage(INFO, res.msg);
                    if (res.code === SUCCESS_CODE) {
                        cancelMenuOps();
                    }
                }, "application/json");
                break;
            case opTypes[1]:
                // 新建下级菜单
                var node = getSelectedTreeNode(); //当前选中
                if (node != null) {
                    menu.parentId = node.id
                } else {
                    showMessage(WARN, "没有选中任何菜单");
                    break;
                }
                $$.post("/api/system-menu/add", JSON.stringify(menu), function (res) {
                    showMessage(INFO, res.msg);
                    if (res.code === SUCCESS_CODE) {
                        cancelMenuOps();
                    }
                }, "application/json");
                break;
            case  opTypes[2]:
                $$.post("/api/system-menu/edit/" + menu.id, JSON.stringify(menu), function (res) {
                    showMessage(INFO, res.msg);
                    if (res.code === SUCCESS_CODE) {
                        $("#defaultBtns").show();
                        isWriteMenuBox(false, false);
                        $("#appendBtns").hide();
                    }
                }, "application/json")
                break;
            default :
                alert("未知操作")
        }
        setTimeout(function () {
            handlerInitTree();
        }, 200);
    } else {
        showMessage(WARN, "请校验输入");
    }
};

/**
 * 编辑模式下的取消按钮
 */
var cancelMenuOps = function () {
    $("#defaultBtns").show();
    isWriteMenuBox(false, false);
    $("#appendBtns").hide();
    /*var node = getSelectedTreeNode();
    if (node != null) {
        menuClick(node);
    }*/
};

/**
 * 处理输入数据
 * @returns {{name: any | jQuery, remark: any | jQuery, id: any | jQuery, parentId: any | jQuery, url: any | jQuery, status: any | jQuery}}
 */
var handlerGetMenuData = function () {
    return {
        id: $("#menuId").val(),
        parentId: $("#parentId").val(),
        name: $("#menuName").val(),
        url: $("#menuUrl").val(),
        remark: $("#menuRemark").val(),
        status: $("#menuStatus").val()
    }
};

/**
 * 添加同级菜单
 * 1.没选中任何菜单则认为建立的外层菜单
 */
var addSamelevelMenu = function () {
    isWriteMenuBox(true, true, "parentMenu");
    showSaveAndCancel(opTypes[0]);
    if (getSelectedTreeNode() == null) {
        showMessage(WARN, "未选中任何节点,建立最外层菜单");
    }
};

/**
 * 添加下级菜单
 */
var addSublevelMenu = function () {
    var node = getSelectedTreeNode();
    if (node != null) {
        isWriteMenuBox(true, true, "parentMenu");
        showSaveAndCancel(opTypes[1]);
        $("#parentMenu").val(node.name);
    } else {
        alert("请先选中任意菜单");
    }
};

/**
 * 修改菜单
 */
var modifyMenu = function () {
    if (getSelectedTreeNode() != null) {
        isWriteMenuBox(true, false, "parentMenu");
        showSaveAndCancel(opTypes[2]);
    } else {
        alert("请先选中任意菜单");
    }
};


var modifyMenuRole = function () {
    roleTable.setShadeBox(false)
};

var cancelModifyMenuRole = function () {
    roleTable.setShadeBox(true)
};

/**
 * 角色列表
 */
function initSystemRole() {
    var queryUrl = basePath + '/api/system-roles';
    var table = new BSTable("roule-table", queryUrl, setRoleColumns());
    table.height = 300;
    table.search = false;
    table.showColumns = false;
    table.showRefresh = false;
    table.shade = true;
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
        linkMenuRole([row.id])
    };
    table.onUncheck = function (row) {
        unLinkMenuRole([row.id])
    };
    table.onCheckAll = function (rows) {
        linkMenuRole(getRoleIds(rows))
    };
    table.onUncheckAll = function (rows) {
        unLinkMenuRole(getRoleIds(rows))
    };
    table.onLoadSuccess = function () {
        // 加载已关联的
        loadMenuRoles(table);
    };
    table.init();
    roleTable = table;
}

/*
加载已经关联的
 */
var loadMenuRoles = function (table) {
    var node = getSelectedTreeNode();
    if (node !== null) {
        var url = basePath + "/api/system-menu-roles/" + node.id;
        $.get(url, function (res) {
            if (res.code === SUCCESS_CODE) {
                var rids2 = [];
                for (var i in res.context) {
                    rids2.push(res.context[i].roleId);
                }
                table.checkBy("id", rids2);
            }
        }, "json");
    } else {
        console.log("没有任何选中");
    }
};


var linkMenuRole = function (roles) {
    var node = getSelectedTreeNode();
    if (node !== null) {
        var url = basePath + "/api/system-menu-role/link-role";
        $$.post(url, JSON.stringify({menuId: node.id, roleIds: roles}), function (res) {
            showMessage(INFO, res.msg);
        }, "application/json");
    } else {
        showMessage(WARN, "未选定任何菜单")
    }
};

var unLinkMenuRole = function (roles) {
    var node = getSelectedTreeNode();
    if (node !== null) {
        var url = basePath + "/api/system-menu-role/unlink-role";
        $$.post(url, JSON.stringify({menuId: node.id, roleIds: roles}), function (res) {
            showMessage(INFO, res.msg);
        }, "application/json");
    } else {
        showMessage(WARN, "未选定任何菜单")
    }
};

var getRoleIds = function (rows) {
    var rids = [];
    for (var i in rows) {
        rids.push(rows[i].id);
    }
    return rids;
};


var setRoleColumns = function () {
    return [{
        checkbox: true,
        visible: true
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
