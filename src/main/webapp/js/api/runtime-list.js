var $$ = new api.Api();
$(document).ready(function () {
    console.info("runtime-list.js");
    initTree();

});

/**
 * 初始服务实例
 * @constructor
 */
initTree = function () {
    // $$.$get("/api/system-menu/getMenuTree", function (res) {
    $$.$get("/api/config/loadRuntimeService", function (res) {
        $('#menu-tree').treeview({
            data: res.context,
            color: "#0e5eaa",
            showBorder: true,
            selectedIcon: "glyphicon glyphicon-check",
            multiSelect: false,
            highlightSelected: true,
            onNodeSelected: function (event, data) {
                menuClick(data);
            }
        });
        $('#menu-tree').treeview('collapseAll', { silent:true });
    });
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
    // isWriteMenuBox(false, false);
    $("#appendBtns").hide();
};

/**
 * 获取父节点
 * @param data
 * @returns {*|jQuery}
 */
var getParentNode = function (data) {
    return $('#menu-tree').treeview('getParent', data)
};
