<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <jsp:include page="../core/resource.jsp"/>
    <script src="${basePath}/js/api/config-route.js"></script>
    <link type="text/css" rel="stylesheet" href="${basePath}/css/bootstrap/checkbox/checkbox.css"/>
<%--    <link rel="stylesheet" type="text/css" href="http://www.jq22.com/jquery/bootstrap-3.3.4.css">
    <link rel="stylesheet" type="text/css" href="http://www.jq22.com/jquery/font-awesome.4.6.0.css">--%>
</head>
<body>
<%--<jsp:include page="../core/model.jsp"/>--%>
<%--<jsp:include page="../core/sidebar.jsp"/>--%>
<div class="main-content">
    <div class="container-right-context animated fadeIn">

        <div class="panel-header">
            <div class="input-group">
                <p class="left-panel-title">灰度路由规则</p>
                <span class="input-group-btn panel-button-group">
                    <button type="button" class="btn btn-primary" onclick="buildRule()" >生成规则</button>

                </span>
            </div>
        </div>

        <div class="main-content-inner">
            <div class="page-content main-content">
                <div class="row" >

                    <div  class="col-md-12">
                        <div class="panel-body">
                            <div class="alert alert-success fade in">
                                <button type="button" class="close close-sm" data-dismiss="alert"> <i class="fa fa-times"></i> </button>
                                <strong>注意!</strong>&nbsp;配置路由规则请仔细核对，一定要慎重，慎重，再慎重！</div>
                        </div>
                    </div>


                    <%--路由规则配置--%>
                    <div class="col-md-12">
                        <div class="panel panel-primary">
                            <div class="panel-heading">[路由规则] 配置</div>
                            <div class="row">
                                <div class="col-md-12 col-lg-12 panel-group ">
                                    <!--collapse start-->
                                    <div class="panel-body" id="route_rule_panel">

                                        <div class="row">
                                            <div class="col-md-1">
                                                <select id="route_key_1" name="route_key_1" onchange="javascript:routeKeyChange(this,'1');" class="selectpicker show-tick form-control" data-live-search="true" require="规则类型" placeholder="规则类型">
                                                    <option value="">规则类型</option>
                                                    <option value="method">method</option>
                                                    <option value="storeId">storeId</option>
                                                    <option value="serviceName">serviceName</option>
                                                    <option value="rule_version">version</option>
                                                </select>
                                            </div>

                                            <div class="col-md-1">
                                                <select id="route_rule_1" name="route_rule_1" onchange="javascript:routeRuleChange(this,'1');" class="selectpicker show-tick form-control" data-live-search="true" require="匹配模式" placeholder="匹配模式">
                                                    <%--<option value="">匹配规则</option>--%>
                                                    <option value="equal">equal</option>
                                                    <option value="match">match</option>
                                                </select>
                                            </div>

                                            <%-- <div class="col-md-2">
                                                <select id="select_service" class="selectpicker show-tick form-control" data-live-search="true" require="请选择服务" placeholder="请选择服务">
                                                    <option value="">请选择服务</option>
                                                </select>
                                            </div>
                                            <div class="col-md-2">
                                                <select id="select_method" class="selectpicker show-tick form-control" data-live-search="true" require="请选择方法" placeholder="请选择方法">
                                                    <option value="">请选择方法</option>
                                                </select>
                                            </div>

                                            <div class="col-md-2">
                                                <div class="form-group fl">
                                                    <input type="text" id="match_rule" class="form-control" placeholder="请输入匹配规则">
                                                </div>
                                            </div>--%>

                                          <%--  <div class="col-md-1">
                                                <select id="importOpt_select" class="selectpicker show-tick form-control" data-live-search="true" require="请选择导入类型(更新、插入)" placeholder="请选择导入类型(更新、插入)">
                                                    <option value="AND"></option>
                                                    <option value="DIST">修改</option>
                                                </select>
                                            </div>--%>

                                            <div class="col-md-1">
                                                <button type="button" class="btn btn-primary" onclick="addRule(this,1)">添加</button>
                                            </div>
                                        </div>

                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <%--路由指向配置--%>
                    <div class="col-md-12">
                        <div class="panel panel-primary">
                            <div class="panel-heading">[路由指向] 配置</div>
                            <div class="row">
                                <div class="col-md-12 col-lg-12 panel-group ">
                                    <!--collapse start-->
                                    <div class="panel-body" id="route_dist_panel">

                                        <div class="row">
                                            <div class="col-md-1">
                                                <select id="dist_key_1"  name="dist_key_1" onchange="javascript:distKeyChange(this,'1');" class="selectpicker show-tick form-control" data-live-search="true" require="路由匹配项" placeholder="路由匹配项">
                                                    <option value="">指向类型</option>
                                                    <option value="dist_ip">ip</option>
                                                    <option value="dist_version">version</option>
                                                </select>
                                            </div>

                                            <%--<div class="col-md-1">
                                                <select id="route_dist_vlaue" class="selectpicker show-tick form-control" data-live-search="true" require="匹配模式" placeholder="匹配模式">
                                                    <option value="">请选择</option>
                                                    <option value="equal">equal</option>
                                                    <option value="match">match</option>
                                                </select>
                                            </div>--%>

                                            <div class="col-md-1">
                                                <div class="checkbox checkbox-success checkbox-inline">
                                                    <input type="checkbox" class="styled" id="inlineCheckbox1" name="inlineCheckbox1" value="option1">
                                                    <label for="inlineCheckbox1"> 取非 </label>
                                                </div>
                                            </div>

                                            <div class="col-md-1">
                                                <button type="button" class="btn btn-primary" onclick="addDist(this,1)">添加</button>
                                            </div>
                                        </div>

                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>


                    <%--otherwise 配置--%>
                    <div class="col-md-12">
                        <div class="panel panel-primary">
                            <div class="panel-heading">[otherwise] 配置</div>
                            <div class="row">
                                <div class="col-md-12 col-lg-12 panel-group ">
                                    <!--collapse start-->
                                    <div class="panel-body" id="route_otherwise_panel">

                                        <div class="row">

                                            <div class="col-md-1">
                                                <div class="checkbox checkbox-success checkbox-inline">
                                                    <input type="checkbox" class="styled" id="otherwiseCheckbox1" name="otherwiseCheckbox1" value="option1">
                                                    <label for="otherwiseCheckbox1"> otherwise  </label>
                                                </div>
                                            </div>


                                            <div class="col-md-1">
                                                <select id="otherwise_key_1" name="otherwise_key_1" onchange="javascript:otherwiseKeyChange(this,'1');" class="selectpicker show-tick form-control" data-live-search="true" require="otherwise" placeholder="otherwise">
                                                    <option value="">指向类型</option>
                                                    <option value="otherwise_ip">ip</option>
                                                    <option value="otherwise_version">version</option>
                                                </select>
                                            </div>

                                            <div class="col-md-1">
                                                <div class="checkbox checkbox-success checkbox-inline">
                                                    <input type="checkbox" class="styled" id="otherwiseElse" name="otherwiseElse" value="otherwiseElse">
                                                    <label for="otherwiseElse"> 取非 </label>
                                                </div>
                                            </div>

                                            <%--<div class="col-md-1">
                                                <select id="route_dist_vlaue" class="selectpicker show-tick form-control" data-live-search="true" require="匹配模式" placeholder="匹配模式">
                                                    <option value="">请选择</option>
                                                    <option value="equal">equal</option>
                                                    <option value="match">match</option>
                                                </select>
                                            </div>--%>
                                        </div>

                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                </div>
            </div>
        </div>
        <jsp:include page="../core/footer.jsp"/>
    </div>
</div>


<%--生成信息展示--%>
<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog" style="width:80%">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                    &times;
                </button>
                <h4 class="modal-title" id="myModalLabel"> 生城路由规则信息</h4>
            </div>
            <div class="modal-body">
                <%--在这里添加一些文本--%>
                <div class="panel-body">
                    <div class="alert alert-success fade in" id="showRuleDiv">
                        <strong>注意!</strong>&nbsp;配置路由规则请仔细核对，一定要慎重，慎重，再慎重！
                    </div>
                </div>

            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-primary" data-dismiss="modal">确定 </button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal -->
</div>

</body>
</html>
