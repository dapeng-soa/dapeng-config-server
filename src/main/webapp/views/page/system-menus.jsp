<%--
  Created by IntelliJ IDEA.
  User: struy
  Date: 2018/5/30
  Time: 09:49
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <jsp:include page="../core/resource.jsp"/>
    <script src="${basePath}/plugins/tree/bootstrap-treeview.min.js"></script>
    <script src="${basePath}/js/api/system-menus.js"></script>
</head>
<body>
<jsp:include page="../core/sidebar.jsp"/>
<div class="container-right">
    <div class="container-right-context animated fadeIn">
        <div class="panel-header">
            <div class="input-group">
                <p class="left-panel-title">菜单管理</p>
                <span class="input-group-btn panel-button-group">
                </span>
            </div>
        </div>
        <div class="row">
            <div class="col-sm-5" style="border-right:1px solid #ccc;height: 100%;">
                <h4 style="text-align: center;margin-top: 20px">菜单列表</h4>
                <%--tree--%>
                <div style="max-height: 800px;min-height: 400px;margin-top: 20px;">
                    <ul id="menu-tree">
                    </ul>
                </div>
            </div>
            <div class="col-sm-7">
                <h4 style="text-align: center;margin-top: 20px">菜单管理<span id="subtitle"></span></h4>
                <div class="form-horizontal" style="margin-top: 20px">
                    <div class="form-group">
                        <label class="col-sm-2 control-label">父级菜单:</label>
                        <div class="col-sm-9">
                            <input type="hidden" id="menuId" value="">
                            <input type="hidden" id="parentId" value="">
                            <input disabled type="text" id="parentMenu" class="form-control">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">菜单名称:</label>
                        <div class="col-sm-9">
                            <input disabled type="text" id="menuName" class="form-control">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">菜单地址:</label>
                        <div class="col-sm-9">
                            <input disabled type="text" id="menuUrl" class=" form-control">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">备注:</label>
                        <div class="col-sm-9">
                            <textarea disabled id="menuRemark" class="form-control"></textarea>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">状态:</label>
                        <div class="col-sm-9">
                            <select id="menuStatus" disabled class="form-control" autocomplete="off">
                                <option value="valid">有效</option>
                                <option value="invalid">无效</option>
                            </select>
                        </div>
                    </div>
                    <span class="input-group-btn panel-button-group text-center">
                        <span id="defaultBtns">
                            <button type="button" class="btn btn-default" onclick="addSamelevelMenu()">新建同级菜单</button>
                            <button type="button" class="btn btn-default" onclick="addSublevelMenu()">新建下级菜单</button>
                            <button type="button" class="btn btn-default" onclick="modifyMenu()">修改</button>
                        </span>
                        <span id="appendBtns" style="display: none">
                            <input type="hidden" id="opType" value="">
                            <button type="button" class="btn btn-default" onclick="saveMenu()">保存</button>
                            <button type="button" class="btn btn-default" onclick="cancelMenuOps()">取消</button>
                        </span>
                    </span>
                </div>
                <hr/>
                <h4 style="text-align: center;margin-top: 20px">菜单角色管理</h4>
                <div class="form-horizontal" style="margin-top: 20px">
                    <table id="roule-table">

                    </table>
                    <span class="input-group-btn panel-button-group text-center">
                       <button type="button" class="btn btn-default" onclick="modifyMenuRole()">修改</button>
                        <button type="button" class="btn btn-default" onclick="cancelModifyMenuRole()">取消</button>
                    </span>
                </div>
            </div>
        </div>
        <jsp:include page="../core/footer.jsp"/>
    </div>
</div>
</body>
</html>
