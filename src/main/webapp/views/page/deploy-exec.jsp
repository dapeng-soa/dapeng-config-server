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
    <script src="${basePath}/js/api/deploy-unit.js"></script>
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
                    <button type="button" class="btn btn-success" onclick="addDeployUnit()">新增部署单元</button>
                </span>
            </div>
        </div>

        <div class="">
            <span>选择环境： <span>
                <select class="form-control " style="display: inline-block;width: 120px">
                    <option>生产环境</option>
                    <option>sandbox</option>
                    <option>demo</option>
                </select>
            </span></span>

            <span>选择视图： <span>
                <select class="form-control" style="display: inline-block;width: 120px">
                    <option>服务视图</option>
                    <option>主机视图</option>
                </select>
            </span></span>
        </div>

        <div class="row" style="margin-top: 40px">
            <div class="col-sm-12 col-xs-12">
                <div class="panel panel-default panel-box">
                    <div class="panel-heading">bizService1
                        <span class="panel-box-menu">
                        <!-- Split button -->
                        <div class="btn-group">
                          <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown"
                                  aria-haspopup="true" aria-expanded="false">
                            <span class="caret"></span>
                            <span class="sr-only">Toggle Dropdown</span>
                          </button>
                          <ul class="dropdown-menu">
                            <li><a href="#">添加配置</a></li>
                            <li><a href="#">查看配置</a></li>
                            <li role="separator" class="divider"></li>
                            <li><a href="#">删除</a></li>
                            <li><a href="#">修改</a></li>
                          </ul>
                        </div>
                    </span>
                    </div>
                    <div class="panel-body" style="overflow-y: auto;max-height: 450px">
                        <table class="table">
                            <thead>
                            <tr>
                                <th>#</th>
                                <th>部署tag</th>
                                <th>镜像tag</th>
                                <th>环境变量</th>
                                <th>端口</th>
                                <th>卷配置</th>
                                <th>其他配置</th>
                                <th>操作</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr>
                                <th scope="row">1</th>
                                <td>t_20180707_01_bugfix</td>
                                <td>43862f9</td>
                                <td><pre>
soa_zookeeper_host=soa_zookeeper:2181
soa_core_pool_size=100
soa_monitor_enable=false
soa_transactional_enable=false
soa_jmxrmi_enable=false
slow_service_check_enable=false
                                </pre></td>
                                <td><pre>9090:9090</pre></td>
                                <td><pre>/data/logs/openApi:/usr/local/tomcat/logs</pre></td>
                                <td><pre>restart: on-failure:3</pre></td>
                                <td><span class="link-button-table">
                                    <a>详情</a>
                                    <a>yml</a>
                                    <a>删除</a>
                                </span></td>
                            </tr>
                            <tr>
                                <th scope="row">2</th>
                                <td>t_20180707_01_bugfix</td>
                                <td>43862f9</td>
                                <td><pre>
soa_zookeeper_host=soa_zookeeper:2181
soa_core_pool_size=100
soa_monitor_enable=false
soa_transactional_enable=false
soa_jmxrmi_enable=false
slow_service_check_enable=false
                                </pre></td>
                                <td><pre>9090:9090</pre></td>
                                <td><pre>/data/logs/openApi:/usr/local/tomcat/logs</pre></td>
                                <td><pre>restart: on-failure:3</pre></td>
                                <td><span class="link-button-table">
                                    <a>详情</a>
                                    <a>yml</a>
                                    <a>删除</a>
                                </span></td>
                            </tr>
                            <tr>
                                <th scope="row">3</th>
                                <td>t_20180707_01_bugfix</td>
                                <td>43862f9</td>
                                <td><pre>
soa_zookeeper_host=soa_zookeeper:2181
soa_core_pool_size=100
soa_monitor_enable=false
soa_transactional_enable=false
soa_jmxrmi_enable=false
slow_service_check_enable=false
                                </pre></td>
                                <td><pre>9090:9090</pre></td>
                                <td><pre>/data/logs/openApi:/usr/local/tomcat/logs</pre></td>
                                <td><pre>restart: on-failure:3</pre></td>
                                <td><span class="link-button-table">
                                    <a>详情</a>
                                    <a>yml</a>
                                    <a>删除</a>
                                </span></td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
        <jsp:include page="../core/footer.jsp"/>
    </div>
</div>
</body>
</html>
