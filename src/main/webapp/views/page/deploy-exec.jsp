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
    <script src="${basePath}/js/api/deploy-exec.js"></script>
</head>
<body>
<jsp:include page="../core/model.jsp"/>
<jsp:include page="../core/sidebar.jsp"/>
<div class="container-right">
    <div class="container-right-context animated fadeIn">
        <div class="panel-header">
            <div class="input-group">
                <p class="left-panel-title">执行发布</p>
                <span class="input-group-btn panel-button-group">

                </span>
            </div>
        </div>

        <div class="">
            <span>选择环境： <span>
                <select id="setSelect" class="form-control " style="display: inline-block;width: 120px">

                </select>
            </span></span>

            <span>选择视图： <span>
                <select class="form-control" style="display: inline-block;width: 120px">
                    <option>服务视图</option>
                    <option>主机视图</option>
                </select>
            </span></span>

            <span>服务： <span>
                <select class="form-control" style="display: inline-block;width: 120px">
                    <option>goodsService</option>
                    <option>categoryService</option>
                </select>
            </span></span>
        </div>

        <div class="row" style="margin-top: 40px">
            <div class="col-sm-6 col-xs-12">
                <div class="panel panel-default panel-box">
                    <div class="panel-heading"><p style="text-align: center">bizService</p>
                        <%--<span class="panel-box-menu">
                        <div class="btn-group">
                          <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown"
                                  aria-haspopup="true" aria-expanded="false">
                            <span class="caret"></span>
                            <span class="sr-only"></span>
                          </button>
                          <ul class="dropdown-menu">
                            <li><a href="#">全部升级</a></li>
                              <li><a href="#">全部启动</a></li>
                          </ul>
                        </div>
                    </span>--%>
                    </div>
                    <div class="panel-body" style="overflow-y: auto;max-height: 400px">
                        <div class="row" style="border-bottom: 1px solid gainsboro;padding: 10px 0;">
                            <div class="col-sm-3 col-xs-12">
                                <p >app1</p>
                                <p >192.168.0.11</p>
                                <p >需要更新：是</p>
                            </div>
                            <div class="col-sm-6 col-xs-12">
                                <p>配置更新时间:2018-07-24 09:09:30</p>
                                <p>主机服务时间:2018-07-23 10:09:30</p>
                                <p>服务状态:<spa style="color: #00AA00">运行中</spa></p>
                            </div>
                            <div class="col-sm-3 col-xs-12">
                                <p ><a href="#" style="color: #1E9FFF" onclick="">升级</a></p>
                                <p ><a href="#" style="color: #1E9FFF">停止</a></p>
                                <p ><a href="#" style="color: #1E9FFF">重启</a></p>
                            </div>
                        </div>
                        <div class="row" style="border-bottom: 1px solid gainsboro;padding: 10px 0;">
                            <div class="col-sm-3 col-xs-12">
                                <p >app2</p>
                                <p >192.168.0.12</p>
                                <p >需要更新：否</p>
                            </div>
                            <div class="col-sm-6 col-xs-12">
                                <p>配置更新时间:2018-07-24 09:09:30</p>
                                <p>主机服务时间:2018-07-23 10:09:30</p>
                                <p>服务状态:<spa style="color: #00AA00">运行中</spa></p>
                            </div>
                            <div class="col-sm-3 col-xs-12">
                                <p ><a href="#" style="color: #1E9FFF">升级</a></p>
                                <p ><a href="#" style="color: #1E9FFF">停止</a></p>
                                <p ><a href="#" style="color: #1E9FFF">重启</a></p>
                            </div>
                        </div>
                        <div class="row" style="border-bottom: 1px solid gainsboro;padding: 10px 0;">
                            <div class="col-sm-3 col-xs-12">
                                <p >app2</p>
                                <p >192.168.0.12</p>
                                <p >需要更新：是</p>
                            </div>
                            <div class="col-sm-6 col-xs-12">
                                <p>配置更新时间:2018-07-24 09:09:30</p>
                                <p>主机服务时间:2018-07-23 10:09:30</p>
                                <p>服务状态:<spa style="color: #00AA00">停止</spa></p>
                            </div>
                            <div class="col-sm-3 col-xs-12">
                                <p ><a href="#" style="color: #1E9FFF">升级</a></p>
                                <p ><a href="#" style="color: #1E9FFF">停止</a></p>
                                <p ><a href="#" style="color: #1E9FFF">重启</a></p>
                            </div>
                        </div>
                        <div class="row" style="border-bottom: 1px solid gainsboro;padding: 10px 0;">
                            <div class="col-sm-3 col-xs-12">
                                <p >app2</p>
                                <p >192.168.0.12</p>
                                <p >需要更新：否</p>
                            </div>
                            <div class="col-sm-6 col-xs-12">
                                <p>配置更新时间:2018-07-24 09:09:30</p>
                                <p>主机服务时间:2018-07-23 10:09:30</p>
                                <p>服务状态:<spa style="color: #00AA00">停止</spa></p>
                            </div>
                            <div class="col-sm-3 col-xs-12">
                                <p ><a href="#" style="color: #1E9FFF">升级</a></p>
                                <p ><a href="#" style="color: #1E9FFF">停止</a></p>
                                <p ><a href="#" style="color: #1E9FFF">重启</a></p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <jsp:include page="../core/footer.jsp"/>
    </div>
</div>
</body>
</html>
