+$(function () {
    // 严格模式
    "use strict";
    
    // =================================== 变量声明 =================================== //
    // 取得ContextPath    
    function getContextPath() {
        var sPathName = document.location.pathname;
        var iIndex = sPathName.substr(1).indexOf("/");
        var sResult = sPathName.substr(0, iIndex+1);
        return sResult;
    }
    var sContextPath = getContextPath();
    //获取olt的ID
    var oltId = "zte";
    var username = $("#username").content();
    var octetTx ; 
    // =================================== 方法执行 =================================== 
    //设置table标题
    function setTableTitle(){
        document.getElementById('oltId').innerHTML="用户:"+username+"的设备管理";
    }
    function setVndMangement(vndName){
        document.getElementById('vndMangement').innerHTML="虚拟切片:"+vndName;
    }
    //给table赋值
    function setTable(oData){
        var hHtml = template("device-template", {list: oData});
        $("#device-placeholder").html(hHtml);
        for(var x in oData){
            if(oData[x].status == "离线"){
                var index = oData[x].oltId + "_" + oData[x].virtualName;
                $("#"+index).attr("class","btn btn-outline-primary btn-sm disabled");
                $("#"+index+"M").attr("class","btn btn-outline-primary btn-sm disabled");
            }
        }
        $("#device a[data-click='get']").bind("click", getVirtualInfo);
        $("#device a[data-click='go']").bind("click", goToShowHistory);
        $("#device a[data-click='onu']").bind("click", goToOnuManagement);
    }
    //给vlan赋值
    function setVlan(oData){
        $("#vlanId").content(oData.vlan_info);
        $("#vlanMode").content(oData.mode);
    }
    //端口历史数据跳转
    function goToShowHistory(){
        var nodeId = $(this).attr("data-click-data");
        var index = nodeId.lastIndexOf("_");
        var oltId = nodeId.substring(0,index);
        var vndName = nodeId.substring(index+1,nodeId.length);
        var url = sContextPath +"/history/portStats/"+oltId+"/"+vndName;
        window.location.href = url;
    }
    //ONU管理跳转
    function goToOnuManagement(){
        var nodeId = $(this).attr("data-click-data");
        var index = nodeId.lastIndexOf("_");
        var oltId = nodeId.substring(0,index);
        var vndName = nodeId.substring(index+1,nodeId.length);
        var url = sContextPath +"/Management/onu/"+oltId+"/"+vndName;
        window.location.href = url;
    }
    //给PortStats赋值
    function setPortStats(oData){
        $("#octetTxPeak").content(oData.octetTxPeak);
        $("#octetRxPeak").content(oData.octetRxPeak);
        $("#pktRxRate").content(oData.pktRxRate);
        $("#pktTxRate").content(oData.pktTxRate);
        $("#octetRx").content(oData.octetRx);
        $("#octetTx").content(oData.octetTx);
    }
    //获取该用户下切片的ID,NAME和STATUS列表
    function getVirtualList(){
        var oAjaxOption = {
                type: "get",
                url: sContextPath + "/rest/showDeviceByUser/"+username+".json",
                contentType: "application/json",
                dataType: "text",
                success: function(oData, oStatus) {
                    setTable(JSON.parse(oData));
                },
                error: function(oData, oStatus, eErrorThrow) {
                    util.handleAjaxError(oData, oStatus, eErrorThrow);
                },
                complete: function (oXmlHttpRequest, oStatus) {
                    $.unblockUI();
                }
        };
        $.blockUI(util.getBlockOption());
        $.ajax(oAjaxOption);
    }
    //获取该虚拟切片的pon口信息等
    function getVirtualInfo(){
        var vnd_name = $(this).attr("data-click-data");
        var index = vnd_name.lastIndexOf("_");
        var nodeId = "vDevice_"+vnd_name.substring(0,index)+"_"+vnd_name.substring(index+1,vnd_name.length);
        var oAjaxOption = {
                type: "get",
                url: sContextPath + "/rest/oltUsrManagement/"+ nodeId +".json",
                contentType: "application/json",
                dataType: "text",
                success: function(oData, oStatus) {
                    setInterfaceTable(JSON.parse(oData),vnd_name);
                },
                error: function(oData, oStatus, eErrorThrow) {
                    util.handleAjaxError(oData, oStatus, eErrorThrow);
                },
                complete: function (oXmlHttpRequest, oStatus) {
                    $.unblockUI();
                }
        };
        $.blockUI(util.getBlockOption());
        $.ajax(oAjaxOption);
    }
    //刷新模态框数据
    function refreshVirtualInfo(){
        var title = document.getElementById('vndMangement').innerHTML;
        var vnd_name = title.substring(5);
        var oAjaxOption = {
                type: "get",
                url: sContextPath + "/rest/"+oltId+"/VirtualInfo/"+vnd_name+".json",
                contentType: "application/json",
                dataType: "text",
                success: function(oData, oStatus) {
                    setInterfaceTable(JSON.parse(oData));
                },
                error: function(oData, oStatus, eErrorThrow) {
                    util.handleAjaxError(oData, oStatus, eErrorThrow);
                },
                complete: function (oXmlHttpRequest, oStatus) {
                    $.unblockUI();
                }
        };
        $.blockUI(util.getBlockOption());
        $.ajax(oAjaxOption);
    }
    //修改VLAN确认按下功能
    $("#editVlanConfirm").click(function() {
        var vndName = $("#source").val();
        var interfaceName = $(this).attr("data-click-data");
        var vlan = $("#VLAN").val();
        var ifCleanVlan = $('#choose input[name="ifCleanVlan"]:checked').val();
        //ifCleanVlan为1是清空vlan
        if(ifCleanVlan != 1){
            var oAjaxOption = {
                    type: "put",
                    url: sContextPath + "/rest/"+vndName+"/"+ interfaceName +".json",
                    contentType: "application/json",
                    data:vlan,
                    dataType: "text",
                    success: function(oData, oStatus) {
                        setVlanConfirm();
                    },
                    error: function(oData, oStatus, eErrorThrow) {
                        util.handleAjaxError(oData, oStatus, eErrorThrow);
                    },
                    complete: function (oXmlHttpRequest, oStatus) {
                        $.unblockUI();
                    }
            };
            $.blockUI(util.getBlockOption());
            $.ajax(oAjaxOption);
        }else{
            var oAjaxOption = {
                    type: "delete",
                    url: sContextPath + "/rest/"+vndName+"/"+ interfaceName +".json",
                    contentType: "application/json",
                    dataType: "text",
                    success: function(oData, oStatus) {
                        setVlanConfirm();
                    },
                    error: function(oData, oStatus, eErrorThrow) {
                        util.handleAjaxError(oData, oStatus, eErrorThrow);
                    },
                    complete: function (oXmlHttpRequest, oStatus) {
                        $.unblockUI();
                    }
            };
            $.blockUI(util.getBlockOption());
            $.ajax(oAjaxOption);
        }
       
    });
    //清空VLAN
    $("#ifCleanVlan").click(function() {
        var ifCleanVlan = $('#choose input[name="ifCleanVlan"]:checked').val();
        if(ifCleanVlan == 1){
            $('#VLAN').attr("disabled",true); 
        }
        if(ifCleanVlan == null){
            $('#VLAN').removeAttr("disabled"); 
        }
    });
    //给模态框的interface表格赋值
    function setInterfaceTable(oData,vnd_name){
        var x;
        var portList = oData.portList;
        //修改TYPE 并且当type=1116时,
        for(x in portList){
            var type = portList[x].porttype;
            if(type=="1116"){
                portList[x].porttype = "PON口";
            }else if(type=="132"){
                portList[x].porttype = "上联口";
            }
        }
        //来源清空
        $("#source").val();
        var hHtml = template("vDevice-template", {list: oData.portList});
        $("#vDevice-placeholder").html(hHtml);
        var index = vnd_name.lastIndexOf("_");
        var title = vnd_name.substring(index+1,vnd_name.length);
        var source = "vDevice_"+vnd_name;
        setVndMangement(title);
        for(x in portList){
            //禁用性能监控按钮
            if(portList[x].porttype == "PON口"){
                var index = parseInt(x)+1 ;
                var portName = portList[x].portname;
                portName = portName.replaceAll(/\//g, '\\/');
                $("#"+index).attr("class","btn btn-outline-primary btn-sm disabled");
                $("#"+portName).attr("class","btn btn-outline-primary btn-sm disabled");
            }
        }
        $("#device_type").content(oData.device_type);
        $("#device_version").content(oData.device_version);
        $("#system_version").content(oData.system_version);
        $("#source").val(source);
        $("#vDevice a[data-click='edit']").bind("click", setVlanConfirm);
        $("#vDevice a[data-click='get']").bind("click", getPortStats);
        //console.log($("#source").val());
    }
    function setVndMangement(vndName){
        document.getElementById('vndMangement').innerHTML="设备:"+vndName;
    }
    //获取修改vlan的模态框的值
    function setVlanConfirm(){
        //清空vlan输入框
        $("#VLAN").val("");
        $("#ifCleanVlan").removeAttr('checked');
        $('#VLAN').removeAttr("disabled"); 
        var vndName = $("#source").val();
        var interfaceName = $(this).attr("data-click-data");
        var pon = interfaceName.replaceAll("/","_");
        var slot =interfaceName.substring(interfaceName.indexOf("/")+1, interfaceName.lastIndexOf("/"));
        //if(slot=="17"){
            //$("#ifCleanVlan").attr("disabled","disabled");
        //}
        //确认按钮赋值
        $("#editVlanConfirm").attr("data-click-data", pon);
        var oAjaxOption = {
                type: "get",
                url: sContextPath + "/rest/vlan/"+vndName+"/"+pon+".json",
                contentType: "application/json",
                dataType: "text",
                success: function(oData, oStatus) {
                    setVlan(JSON.parse(oData));
                },
                error: function(oData, oStatus, eErrorThrow) {
                    util.handleAjaxError(oData, oStatus, eErrorThrow);
                },
                complete: function (oXmlHttpRequest, oStatus) {
                    $.unblockUI();
                }
        };
        $.blockUI(util.getBlockOption());
        $.ajax(oAjaxOption);
    }
    //获取性能统计并赋值
    function getPortStats(){
        var vndName = $("#source").val();
        var interfaceName = $(this).attr("data-click-data");
        var pon = interfaceName.replaceAll("/","_");
        var oAjaxOption = {
                type: "get",
                url: sContextPath + "/rest/PortStats/"+vndName+"/"+pon+".json",
                contentType: "application/json",
                dataType: "text",
                success: function(oData, oStatus) {
                    setPortStats(JSON.parse(oData));
                    octetTx = oData.octetTx;
                },
                error: function(oData, oStatus, eErrorThrow) {
                    util.handleAjaxError(oData, oStatus, eErrorThrow);
                },
                complete: function (oXmlHttpRequest, oStatus) {
                    $.unblockUI();
                }
        };
        $.blockUI(util.getBlockOption());
        $.ajax(oAjaxOption);
        setChart(oAjaxOption);
    }
    String.prototype.replaceAll = function(s1,s2){ 
        return this.replace(new RegExp(s1,"gm"),s2); 
        }
    //画面初始化
    function initPage(){
        setTableTitle();
        getVirtualList();
    }
    
    //生成折线图
    function setChart(oAjaxOption) {                                                  
        Highcharts.setOptions({                                                     
            global: {                                                               
                useUTC: false                                                       
            }                                                                       
        });                                                                         
                                                                                    
        var chart;
        //GET数据
        $.ajax(oAjaxOption);
        var data =123;                                                               
        $('#container').highcharts({                                                
            chart: {                                                                
                type: 'spline',                                                     
                animation: Highcharts.svg, // don't animate in old IE               
                marginRight: 10,                                                    
                events: {                                                           
                    load: function() {                                              
                                                                                    
                        // set up the updating of the chart each second             
                        var series = this.series[0];                                
                        setInterval(function() {                                    
                            var x = (new Date()).getTime(), // current time         
                                y = octetTx;                               
                            series.addPoint([x, y], true, true);                    
                        }, 5000);                                                   
                    }                                                               
                }                                                                   
            },                                                                      
            title: {                                                                
                text: '上联口性能统计图'                                            
            },                                                                      
            xAxis: {                                                                
                type: 'datetime',                                                   
                tickPixelInterval: 100                                              
            },                                                                      
            yAxis: {                                                                
                title: {                                                            
                    text: '--单位(kb)'                                                   
                },                                                                  
                plotLines: [{                                                       
                    value: 0,                                                       
                    width: 1,                                                       
                    color: '#808080'                                                
                }]                                                                  
            },                                                                      
            tooltip: {                                                              
                formatter: function() {                                             
                        return '<b>'+ this.series.name +'</b><br/>'+                
                        Highcharts.dateFormat('%Y-%m-%d %H:%M:%S', this.x) +'<br/>'+
                        Highcharts.numberFormat(this.y, 2);                         
                }                                                                   
            },                                                                      
            legend: {                                                               
                enabled: false                                                      
            },                                                                      
            exporting: {                                                            
                enabled: false                                                      
            },
            //初始数据                                                                      
            series: [{                                                              
                name: '瞬时数据',                                                
                data: (function() {                                                 
                    // generate an array of random data                             
                    var data = [],                                                  
                        time = (new Date()).getTime(),                              
                        i;                                                          
                                                                                    
                    for (i = -10; i <= 0; i++) {                                    
                        data.push({                                                 
                            x: time + i * 5000,                                     
                            y: 1000+200*Math.random()                                       
                        });                                                         
                    }                                                               
                    return data;                                                    
                })()                                                                
            }]                                                                      
        });                                                                         
    }
    
    // =================================== 页面加载 =================================== 
    //$("#VLAN").val("222");
    initPage();

});