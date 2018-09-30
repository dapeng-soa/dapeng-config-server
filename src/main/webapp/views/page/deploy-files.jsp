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
    <script src="${basePath}/js/api/deploy-files.js"></script>
</head>
<body>
<jsp:include page="../core/model.jsp"/>
<jsp:include page="../core/sidebar.jsp"/>
<div class="container-right">
    <div class="container-right-context animated fadeIn">
        <div class="panel-header">
            <div class="input-group">
                <p class="left-panel-title">文件管理</p>
                <span class="input-group-btn panel-button-group">
                    <button type="button" class="btn btn-primary" onclick="openAddServiceFileModle()">新增</button>
                </span>
            </div>
        </div>
        <div id="deploy-files-tableToolbar">
            <code>
                文件描述了一个或多个容器的VOLUMES信息,当添加一个文件后，都需要与指定的部署单元(容器)进行关联,关联的文件或文件夹最终将作为容器的VOLUMES,点击关联部署单元按钮查看或关联/取消关联部署单元。
            </code>
        </div>
        <table id="deploy-files-table">

        </table>
    </div>
    <jsp:include page="../core/footer.jsp"/>
</div>
</div>
</body>
</html>
