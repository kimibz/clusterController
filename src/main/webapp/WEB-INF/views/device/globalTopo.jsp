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
      <!-- 设置拓扑图大小像素 -->

    <!-- Icons -->
    <spring:url value="/resources/css/font-awesome.min.css" var="font_awesome"/>
    <link href="${font_awesome}" rel="stylesheet" type="text/css" media="screen, projection"/>
    
    <spring:url value="/resources/css/simple-line-icons.css" var="simple_line_icons"/>
    <link href="${simple_line_icons}" rel="stylesheet" type="text/css" media="screen, projection"/>


    <!-- Main styles for this application -->
    <spring:url value="/resources/css/style.css" var="style"/>
    <link href="${style}" rel="stylesheet" type="text/css" media="screen, projection"/>

    <spring:url value="/resources/css/globalTopo.css" var="global"/>
    <link href="${global}" rel="stylesheet" type="text/css" />
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
            <div class="container-fluid" id="globalTopoBackground">
                <div class="globalTopo">
                <span id="onu1vlan"></span>
                <span id="onu1vlan2"></span>
                <span id="vxlan1"></span>
                <span id="vxlan2"></span>
                <span id="vxlan3"></span>
                <span id="vxlan4"></span>
                <span id="onu2vlan"></span>
                <span id="cloudvlan"></span>
                <span id="gateway1">政企网关</span>
                <span id="gateway2">政企网关</span>
                <img src=<c:url value="/resources/img/globalTopo/红.png"/> id="topoRed" />
                <img src=<c:url value="/resources/img/globalTopo/拓扑.png"/> id="globalTopo" />
                <img src=<c:url value="/resources/img/globalTopo/绿.png"/> id="topoGreen" />
                </div>
            </div>
            <div class="row">
                    <div class="col-lg-12">
                        <div class="card">
                            <div class="card-header">
                                <i class="fa fa-align-justify"></i>业务流信息
                            </div>
                            <div class="card-block">
                                <button type="button" id="addNewDevice" name="addNewDevice" class="btn btn-outline-primary" data-toggle="modal" data-target="#spawnModal">新建业务流</button>
                                <div class="panel panel-default">
                                    <div class="panel-body">
                                        <div id="vxlan-placeholder">
                                        </div>
                                    </div>
                             </div>
                            </div>
                        </div>
                    </div>
                 </div>
            <!--Modal Begin --> 
            <div class="modal fade" id="spawnModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
                    <div class="modal-dialog " role="document">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h4 class="modal-title">新建业务流</h4>
                                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                    <span aria-hidden="true">X</span>
                                </button>
                            </div>
                            <div class="modal-body">
                                <!--  begin form -->
                                <form id="form" class="needs-validation" novalidate>
                                <div class="form-group row">
                                    <label class="col-md-3 form-control-label text-info" for="select"><strong>源用户:</strong></label>
                                      <div class="col-md-8">
                                           <select id="source" name="source" class="selectpicker show-tick form-control" required>
                                                <option value="" disabled selected>请选择源用户</option>
                                                <option value="A">A</option>
                                           </select>
                                      <div class="invalid-feedback">请选择用户</div>
                                      </div>
                                </div>
                                <div class="form-group row">
                                    <div class="col-md-3 ">
                                    <label class="text-info" for="validationCustom01"><strong>VLAN:</strong></label>
                                    </div>
                                    <div class="col-md-8">
                                    <input type="text" class="form-control" id="vlan" placeholder="VLAN" required>
                                    <div class="invalid-feedback">该VLAN已被使用或不能为空</div>
                                    </div>
                                </div>
                                <div class="form-group row">
                                     <label class="col-md-3 form-control-label text-info" for="select"><strong>目标用户:</strong></label>
                                     <div class="col-md-8">
                                           <select id="destination" name="destination" class="selectpicker show-tick form-control" required>
                                                <option value="" disabled selected>请选择目标用户</option>
                                                <option value="B">B</option>
                                                <option value="C">云</option>
                                           </select>
                                      <div class="invalid-feedback">请选择用户</div>
                                      </div>
                                </div>
                                <button class="btn btn-primary" type="submit" id="addService">添加</button>
                                </form>
                                <!-- end form -->
                            </div>
                            <!-- <div class="modal-footer">
                                <a href="javascript:;" class="btn btn-secondary" data-dismiss="modal">取消</a>
                                <a href="javascript:;" id="btn btn-primary" type="submit" class="btn btn-sm btn-primary" data-dismiss="modal" data-click="add" data-action-target="accountInfo">确定</a>
                            </div> -->
                        </div>
                        <!-- /.modal-content -->
                    </div>
                    <!-- /.modal-dialog -->
                </div>
            <!--Modal End -->
            <!-- 确认框 -->
            <div class="modal fade" id="deleteWarning" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
                    <div class="modal-dialog modal-danger" role="document">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h4 class="modal-title">是否删除</h4>
                                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                    <span aria-hidden="true">×</span>
                                </button>
                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-secondary" data-dismiss="modal">取消</button>
                                <button type="button" id="deleteService" data-click-data="" data-dismiss="modal" class="btn btn-primary">确认删除</button>
                            </div>
                        </div>
                        <!-- /.modal-content -->
                    </div>
                    <!-- /.modal-dialog -->
                </div>
                <!-- /.modal -->
                <!-- 确认框结束 -->
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
    <!-- ================== BEGIN TEMPLATE ================== -->
        <script id="vxlan-template" type="text/html" charset="UTF-8">
                                              <div class="tab-content">      
                                                  {{each list as item,index}}
                                                     <div id="tab-{{item.currency}}" {{if index == 0}}class="tab-pane fade active in"{{else}}class="tab-pane fade"{{/if}}">
                                                         <div class="table-responsive">
                                                         <table id="vxlan" class="table table-condensed" cellspacing="0" width="100%">
                                                             <thead>
                                                                 <tr>
                                                                     <td style="width: 10%;">#</td>
                                                                     <td style="width: 15%;">源用户</td>
                                                                     <td style="width: 15%;">VLAN</td>
                                                                     <td style="width: 15%;">VXLAN1</td>
                                                                     <td style="width: 15%;">VXLAN2</td>
                                                                     <td style="width: 15%;">目标用户</td>
                                                                     <td style="width: 15%;">操作</td>
                                                                 </tr>
                                                             </thead>
                                                             <tbody>
                                                                 {{each item.model as subItem,number}}
                                                                 <tr>
                                                                     <td class="email-subject text-ellipsis" title="{{number}}">{{number+1}}</td>
                                                                     <td class="email-subject text-ellipsis" title="{{subItem.source}}">{{subItem.source}}</td>
                                                                     <td class="email-subject text-ellipsis" title="{{subItem.vlan}}">{{subItem.vlan}}</td>
                                                                     <td class="email-subject text-ellipsis" title="{{subItem.vxlanA}}">{{subItem.vxlanA}}</td>
                                                                     <td class="email-subject text-ellipsis" title="{{subItem.vxlanB}}">{{subItem.vxlanB}}</td>
                                                                     <td class="email-subject text-ellipsis" title="{{subItem.destination}}">{{subItem.destination}}</td>
                                                                     <td class="email-select">
                                                                         <a href="javascript:;" button class="btn btn-success" data-toggle="modal" id="{{subItem.index}}" data-click="get"  data-click-data="{{subItem.index}}"><i class="fa fa-search-plus"></i></a>
                                                                         <a href="javascript:;" button class="btn btn-danger" data-toggle="modal" id="{{subItem.index}}" data-click="delete" data-target="#deleteWarning"  data-click-data="{{subItem.index}}"><i class="fa fa-trash-o"></i></a>
                                                                     </td>
                                                                 </tr>
                                                                 {{/each}}
                                                             </tbody>
                                                         </table>
                                                     </div>
                                                     </div>
                                                     {{/each}}
                                                     <ul class="pagination">
                                                        {{if list != null && list.length > 0}}
                                                            {{each list as item index}}
                                                            <li {{if index == 0}}class="active"{{/if}}><a href="#tab-{{item.currency}}" data-toggle="tab">{{item.currency}}</a></li>
                                                            {{/each}}
                                                        {{else}}
                                                            <li class="page-item"><a href="#tab-1" class="page-link">CNY</a></li>
                                                        {{/if}}
                                                     </ul>
        </script>
    <!-- Bootstrap and necessary plugins -->
    <script type="text/javascript" src="<c:url value="/resources/js/jquery.js" />"></script>
    <script type="text/javascript" src="<c:url value="/resources/plugin/bootstrap-growl/bootstrap-growl.js" />"></script>
    <script type="text/javascript" src="<c:url value="/resources/plugin/tether_js/js/tether.min.js" />"></script>
    <script type="text/javascript" src="<c:url value="/resources/plugin/bootstrap-3.2.0/js/bootstrap.min.js" />"></script>
    <script type="text/javascript" src="<c:url value="/resources/plugin/pace-0.5.6/pace.min.js" />"></script>
    <script type="text/javascript" src="<c:url value="/resources/plugin/blockui/jquery.blockUI.js" />"></script>
    
    
    <script type="text/javascript" src="<c:url value="/resources/plugin/artTemplate/template.js" />"></script>
    <!-- GenesisUI main scripts -->
    <script type="text/javascript" src="<c:url value="/resources/js/app.js" />"></script>
    <script type="text/javascript" src="<c:url value="/resources/js/xigua_local.js" />"></script>
    <script type="text/javascript" src="<c:url value="/resources/js/util.js" />"></script>
    <script type="text/javascript" src="<c:url value="/resources/js/globalTopo.js" />"></script>
    <!-- Plugins and scripts required by this views -->

    <!-- Custom scripts required by this view -->
</body>

</html>