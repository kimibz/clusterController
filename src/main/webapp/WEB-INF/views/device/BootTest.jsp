<%@ page language="java" contentType="text/html; charset=utf-8"   pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>  
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%
    String url = request.getRequestURL().toString();
    url = url.substring(0, url.indexOf('/', url.indexOf("//") + 2));
    String context = request.getContextPath();
    url += context;
    application.setAttribute("ctx", url);
%>
<!DOCTYPE html>
<html lang="zh-CN">
  <head>

    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="keyword" content="Bootstrap,Admin,Template,Open,Source,AngularJS,Angular,Angular2,jQuery,CSS,HTML,RWD,Dashboard">

    <spring:url value="/resources/img/favicon.png" var="shortcut_icon"/>
    <link href="${shortcut_icon}" rel="icon"/>
    
    <title>createdBy-西瓜不甜</title>

    <!-- Icons -->
    <spring:url value="/resources/css/font-awesome.min.css" var="font_awesome"/>
    <link href="${font_awesome}" rel="stylesheet" type="text/css" media="screen, projection"/>
    
    <spring:url value="/resources/css/simple-line-icons.css" var="simple_line_icons"/>
    <link href="${simple_line_icons}" rel="stylesheet" type="text/css" media="screen, projection"/>


    <!-- Main styles for this application -->
    <spring:url value="/resources/css/style.css" var="style"/>
    <link href="${style}" rel="stylesheet" type="text/css" media="screen, projection"/>
</head>


<!--顶部菜单  -->
<body class="app header-fixed sidebar-fixed aside-menu-fixed aside-menu-hidden">
    <header class="app-header navbar">
        <button class="navbar-toggler mobile-sidebar-toggler d-lg-none mr-auto" type="button">
      		<span class="navbar-toggler-icon">☰</span>
    	</button>
    	<a class="navbar-brand" href="#"></a>
    	<button class="navbar-toggler sidebar-toggler d-md-down-none" type="button">
      		<span class="navbar-toggler-icon">☰</span>
    	</button>
        
        <ul class="nav navbar-nav hidden-md-down">

            <%-- <li class="nav-item px-1">
                <a class="nav-link" href="/demo">主页</a>
            </li>
            <shiro:hasPermission name="delete"> 
            <li class="nav-item px-1">
                <a class="nav-link" href="/demo/listDevice">设备管理</a>
            </li>
            </shiro:hasPermission>
            <shiro:hasPermission name="service">
            <li class="nav-item px-1">
                <a class="nav-link" href="/demo/management/showDevice">设备管理</a>
            </li>
            </shiro:hasPermission>
            <shiro:hasPermission name="delete">  
            <li class="nav-item px-1">
                <a class="nav-link" href="#">其他</a>
            </li>
            </shiro:hasPermission> --%>
        </ul>
        <ul class="nav navbar-nav ml-auto">
            <!-- <li class="nav-item hidden-md-down">
                <a class="nav-link" href="#"><i class="icon-bell"></i><span class="badge badge-pill badge-danger">5</span></a>
            </li> -->
            <li class="nav-item hidden-md-down">
                <a class="nav-link" href="#"><i class="icon-list"></i></a>
            </li>
            <li class="nav-item hidden-md-down">
                <a class="nav-link" href="#"><i class="icon-location-pin"></i></a>
            </li>
            <li class="nav-item dropdown">
                <a class="nav-link dropdown-toggle nav-link" data-toggle="dropdown" href="#" role="button" aria-haspopup="true" aria-expanded="false">
                    <img src=<c:url value="/resources/img/avatars/7.jpg" /> class="img-avatar" alt="admin@bootstrapmaster.com">
                    <span id = "username" class="hidden-md-down"><shiro:principal/></span>
                </a>
                <div class="dropdown-menu dropdown-menu-right">

                    <div class="dropdown-header text-center">
                        <strong>Account</strong>
                    </div>

                    <a class="dropdown-item" href="${ctx}/logout"><i class="fa fa-lock"></i> Logout</a>
                </div>
            </li>

        </ul>
        
    </header>

    <div class="app-body">
        <!-- 侧边菜单栏 -->
        <div class="sidebar">
            <nav class="sidebar-nav">
                <ul class="nav">
                    <li class="nav-item">
                        <a class="nav-link" href="/demo"><i class="icon-speedometer"></i> 主页 </a>
                    </li>
                    <li class="nav-title">
                        Main Services
                    </li>
                    <li class="nav-item nav-dropdown">
                        <a class="nav-link nav-dropdown-toggle" href="#"><i class="icon-puzzle"></i> 服务</a>
                        <ul class="nav-dropdown-items">
                        	<shiro:hasPermission name="delete"> 
            				<li class="nav-item px-1">
                				<a class="nav-link" href="/demo/listDevice"><i class="nav-icon icon-speech"></i>设备管理</a>
            				</li>
            				<li class="nav-item px-1">
                                <a class="nav-link" href="/demo/showClusterMonitoring"><i class="nav-icon icon-speech"></i>集群服务</a>
                            </li>
            				</shiro:hasPermission>
            				<shiro:hasPermission name="service">
            				<li class="nav-item px-1">
                				<a class="nav-link" href="/demo/management/showDevice"><i class="nav-icon icon-speech"></i>设备管理</a>
            				</li>
            				</shiro:hasPermission>
                            <li class="nav-item">
                                <a class="nav-link" href="/demo/showTopo"><i class="nav-icon icon-speech"></i> 显示拓扑图</a>
                            </li>
                            <li class="nav-item">
                                <a class="nav-link" href="/demo/globalTopo"><i class="nav-icon icon-speech"></i> VXLAN业务配置</a>
                            </li>
                        </ul>
                    </li>
                    <li class="divider"></li>
                    <li class="nav-title">
                        Extras
                    </li>
                    <li class="nav-item nav-dropdown">
                        <a class="nav-link nav-dropdown-toggle" href="#"><i class="icon-star"></i> Pages</a>
                        <ul class="nav-dropdown-items">
                            <li class="nav-item">
                                <a class="nav-link" href="register" target="_top"><i class="icon-star"></i> Register</a>
                            </li>
                        </ul>
                    </li>

                </ul>
            </nav>
        </div>

        <!-- Main content 地址栏-->
        
        <main class="main">

            <!-- Breadcrumb -->
            <ol class="breadcrumb">
                <li class="breadcrumb-item">Home</li>
                <li class="breadcrumb-item"><a href="#">Admin</a>
                </li>
                <li class="breadcrumb-item active">Dashboard</li>

                <!-- Breadcrumb Menu-->
                <li class="breadcrumb-menu hidden-md-down">
                    <div class="btn-group" role="group" aria-label="Button group with nested dropdown">
                        <a class="btn btn-secondary" href="#"><i class="icon-speech"></i></a>
                        <a class="btn btn-secondary" href="./"><i class="icon-graph"></i> &nbsp;Dashboard</a>
                        <a class="btn btn-secondary" href="#"><i class="icon-settings"></i> &nbsp;Settings</a>
                    </div>
                </li>
            </ol>

            <!-- 网页主体 -->
            <div class="container-fluid">
                
            </div>
            <!-- /.conainer-fluid -->
        </main>

        <aside class="aside-menu">
            <ul class="nav nav-tabs" role="tablist">
                <li class="nav-item">
                    <a class="nav-link active" data-toggle="tab" href="#timeline" role="tab"><i class="icon-list"></i></a>
                </li>
            </ul>

            <!-- Tab panes -->
            <div class="tab-content">
                <div class="tab-pane active" id="timeline" role="tabpanel">
                    <div class="callout m-0 py-h text-muted text-center bg-faded text-uppercase">
                        <small><b>Today</b>
                        </small>
                    </div>
                    <hr class="transparent mx-1 my-0">
                    
                    <hr class="mx-1 my-0">
                  
                    <hr class="transparent mx-1 my-0">
                    <hr class="transparent mx-1 my-0">
                    <hr class="mx-1 my-0">
                    
                    <hr class="mx-1 my-0">
                </div>
            </div>
                
                
        </aside>


    </div>
    <!--页脚显示  -->
    <footer class="app-footer">
        <a href="#">naijgnat</a> © 2017 Xigua
        <span class="float-right">Created by-<a href="https://github.com/kimibz/ShowDemo" target="_blank" title="Xigua">Xigua</a>
        </span>
    </footer>

    <!-- Bootstrap and necessary plugins -->
    <script type="text/javascript" src="<c:url value="/resources/js/jquery.js" />"></script>
    <script type="text/javascript" src="<c:url value="/resources/plugin/bootstrap-growl/bootstrap-growl.js" />"></script>
    <script type="text/javascript" src="<c:url value="/resources/plugin/tether_js/js/tether.min.js" />"></script>
    <script type="text/javascript" src="<c:url value="/resources/plugin/bootstrap-3.2.0/js/bootstrap.min.js" />"></script>
    <script type="text/javascript" src="<c:url value="/resources/plugin/pace-0.5.6/pace.min.js" />"></script>
    <script type="text/javascript" src="<c:url value="/resources/plugin/blockui/jquery.blockUI.js" />"></script>

    <!-- GenesisUI main scripts -->
    <script type="text/javascript" src="<c:url value="/resources/js/app.js" />"></script>
    <script type="text/javascript" src="<c:url value="/resources/js/util.js" />"></script>
    <!-- Plugins and scripts required by this views -->

    <!-- Custom scripts required by this view -->

</body>

</html>