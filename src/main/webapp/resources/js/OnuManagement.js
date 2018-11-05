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
    // 取得OLT
    function getOltId(){
        var sPathName = document.location.pathname;
        var iIndex = sPathName.substr(1).lastIndexOf("/");
        var sResult = sPathName.substr(0, iIndex+1);
        var index = sResult.substr(1).lastIndexOf("/");
        var result = sResult.substr(index+2, sResult.length);
        return result;
    }
    // 取得VND
    function getVndName(){
        var sPathName = document.location.pathname;
        var iIndex = sPathName.substr(1).lastIndexOf("/");
        var sResult = sPathName.substr(iIndex+2, sPathName.length);
        return sResult;
    }
    var sContextPath = getContextPath();
    //获取olt的ID
    var oltId = getOltId();
    var vndName = getVndName();
    var username = $("#username").content();
    // =================================== 方法执行 =================================== 
    //设置table标题
    function setTableTitle(){
        document.getElementById('oltId').innerHTML="切片:"+vndName+"的ONU管理";
    }
    //给table赋值
    function setTable(oData){
        var portList = oData.portList;
        var x;
        for(x in portList){
            var type = portList[x].porttype;
            if(type=="1116"){
                portList[x].porttype = "PON口";
            }else if(type=="132"){
                portList.splice(x);
            }
        }
        var hHtml = template("vDevice-template", {list: portList});
        $("#vDevice-placeholder").html(hHtml);
        $("#vDevice a[data-click='add']").bind("click", setAddOnuLabel);
        $("#vDevice a[data-click='get']").bind("click", getOnuStatus);
        $("#confirmDeleteOnu").bind("click", deleteOnu);
    }
    //给OnuModel table赋值
    function setModelTable(oData){
        var hHtml = template("vOnu-template", {list: oData});
        $("#vOnu-placeholder").html(hHtml);
        $("#vOnu a[data-click='deleteOnu']").bind("click", bindONUdelete);
        $("#vOnu a[data-click='editVlan']").bind("click", bindOnuVlan);
    }
    //清空form
    function cleanForm(){
        $("#onuId").val('');
        $("#OnuType").val('');
        $("#OnuMac").val('');
    }
    function setAddOnuLabel(){
        cleanForm();
        var port = $(this).attr("data-click-data");
        $("#confirmOnu").attr("data-click-data", port); 
        var slot =port.substring(port.indexOf("/")+1, port.lastIndexOf("/"));
        var portNo = port.substring(port.lastIndexOf("/")+1, port.length); 
        var label ="槽位:"+slot+",端口号:"+portNo;
        $("#ponId").content(label);
    }
    
    //绑定ONU确认删除框
    function bindONUdelete(){
        var interfaceName = $(this).attr("data-click-data");
        $("#confirmDeleteOnu").attr("data-click-data", interfaceName);
    }
    //绑定ONU VLAN框
    function bindOnuVlan(){
    	var interfaceName = $(this).attr("data-click-data");
    	$("#editVlan").attr("data-click-data", interfaceName);
    	getOnuVlan(interfaceName);
    }
    //给vlan输入框赋值
    function setVlan(oData){
    	var net_vlan = oData.netVlan;
    	$("#netVlan").val(net_vlan);
    	if(net_vlan == null){
    		$("#ifNull").val("0");
    	}else{
    		$("#ifNull").val("1");
    	}
    	//$("#usrVlan").val(user_vlan);
    }
    //获取ONU的vlan配置
    function getOnuVlan(interfaceName){
    	var onuId = interfaceName.split("_");
    	$("#onuVlan_Id").content(onuId[1]);
    	var nodeId = "vDevice_"+oltId+"_"+vndName;
        var url = sContextPath + "/rest/onuManagement/vlan/"+nodeId+"/"+interfaceName+".json";
        var oAjaxOption = {
                type: "get",
                url: url,
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
    //配置ONU VLAN
    $("#editVlan").click(function() {
    	var nodeId = "vDevice_"+oltId+"_"+vndName;
    	var interfaceName = $(this).attr("data-click-data");
    	var url = sContextPath + "/rest/onuManagement/vlan/"+nodeId+"/"+interfaceName+".json";
    	var netVlan = $("#netVlan").val().toString();
    	var ifNull = $("#ifNull").val().toString();
    	var usrVlan = "100";
    	var oData = new Object();
    	oData.netVlan = netVlan;
    	oData.usrVlan = usrVlan;
    	if(ifNull == "0"){
    		//新建VLAN
    		var oAjaxOption = {
                    type: "post",
                    url: url,
                    contentType: "application/json",
                    dataType: "text",
                    data: JSON.stringify(oData),
                    success: function(oData, oStatus) {
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
    		//修改VLAN
    		var oAjaxOption = {
                    type: "put",
                    url: url,
                    contentType: "application/json",
                    dataType: "text",
                    data: JSON.stringify(oData),
                    success: function(oData, oStatus) {
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
    //获取该虚拟切片的pon口信息等
    function getVirtualInfo(){
        var nodeId = "vDevice_"+oltId+"_"+vndName;
        var oAjaxOption = {
                type: "get",
                url: sContextPath + "/rest/oltUsrManagement/"+ nodeId +".json",
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
    //获取端口下的ONU信息
    function getOnuStatus(){
        var nodeId = "vDevice_"+oltId+"_"+vndName;
        var interfaceName = $(this).attr("data-click-data");
        var pon = interfaceName.replaceAll("/","_");
        var oAjaxOption = {
                type: "get",
                url: sContextPath + "/rest/onuManagement/"+ nodeId +"/"+pon+".json",
                contentType: "application/json",
                dataType: "text",
                success: function(oData, oStatus) {
                    setModelTable(JSON.parse(oData));
                    console.log("getONU"+interfaceName);
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
    function refreshOnuStatus(ifIndex){
    	var nodeId = "vDevice_"+oltId+"_"+vndName;
    	console.log("delete:"+ifIndex);
        var oAjaxOption = {
                type: "get",
                url: sContextPath + "/rest/onuManagementRefresh/"+ nodeId +"/"+ifIndex+".json",
                contentType: "application/json",
                dataType: "text",
                success: function(oData, oStatus) {
                    setModelTable(JSON.parse(oData));
//                    console.log(oData);
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
    //新增ONU确认键按下 OK 传值OK
    $("#confirmOnu").click(function() {
        var data = $("form").form2object();
        var ifIndex = $("#confirmOnu").attr("data-click-data").toString();
        data.ifIndex = ifIndex ;
        var nodeId = "vDevice_"+oltId+"_"+vndName;
        var oAjaxOption = {
                type: "post",
                url: sContextPath + "/rest/onuManagement/"+nodeId+".json",
                contentType: "application/json",
                dataType: "text",
                data: JSON.stringify(data),
                success: function(oData, oStatus) {
                    initPage();
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
    });
    //删除ONU
//    $("#confirmDeleteOnu").click(function() 
    function deleteOnu(){
        var ifIndex = $(this).attr("data-click-data");
        var nodeId = "vDevice_"+oltId+"_"+vndName;
        var oAjaxOption = {
                type: "delete",
                url: sContextPath + "/rest/onuManagement/"+nodeId+"/"+ ifIndex +".json",
                contentType: "application/json",
                success: function(oData, oStatus) {
                	refreshOnuStatus(ifIndex);
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
    $("#onuId").bind('keyup', function () {
        var v = $(this).val();
        if(v > 15){
            $(this).val(15);
        }
    });
    String.prototype.replaceAll = function(s1,s2){ 
        return this.replace(new RegExp(s1,"gm"),s2); 
        }
    //画面初始化
    function initPage(){
        setTableTitle();
        getVirtualInfo();
    }
    
    
    
    // =================================== 页面加载 =================================== 
    initPage();

});