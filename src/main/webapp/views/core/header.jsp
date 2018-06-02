
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<header class="navbar navbar-fixed-top bs-docs-nav" id="top" role="banner" style="background-color: #393D49">
  <div <%--class="container"--%>>
    <div class="navbar-header">
      <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
        <span class="sr-only">Toggle navigation</span>
        <span class="icon-bar"></span>
      </button>
      <a href="${basePath}/" class="navbar-brand">大鹏配置中心</a>
    </div>
    <%--<nav class="collapse navbar-collapse bs-navbar-collapse" id="navbar">
      <ul class="nav navbar-nav">
        <li class="${tagName == 'monitor' ? 'active' : ''}">
          <a href="${basePath}/monitor">服务监控</a>
        </li>
        <li class="${tagName == 'deploy' ? 'active' : ''}">
          <a href="${basePath}/deploy">发布部署</a>
        </li>
        <li class="${tagName == 'config' ? 'active' : ''}">
          <a href="${basePath}/config">配置管理</a>
        </li>
      </ul>

      <form action="${basePath}/search.htm" method="POST" class="navbar-form navbar-right" role="search">
        <div class="form-group">
          <input name="searchText" type="text" class="form-control" placeholder="">
        </div>
        <button type="submit" class="btn btn-default">搜索</button>
      </form>

    </nav>--%>
  </div>
</header>


