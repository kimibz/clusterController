+$(function () {
    // 严格模式
    "use strict";
    
    // =================================== 变量声明 =================================== //
    // 取得ContextPath    
    var nodeId;
    var sContextPath = util.getContextPath();
    // =================================== 方法执行 =================================== //
    //table初始化
    function setClusterTbl(oData) {
        var hHtml = template("cluster-template", {list: oData});
        setTableTitle();
        $("#cluster-placeholder").html(hHtml);
        /*var key;
        for(var item in oData) {
            if(oData[item].state == "离线"){
                    key =oData[item].node_id +"Info";
                    $("#"+key).attr("class","btn btn-outline btn-sm disabled");
                }
            }*/
        $("#cluster a[data-click='get']").bind("click", getDetails);
    }
    
    
    $("#addNewDevice").click(function() {
        ClearForm();
//        $("#spawnModal").modal();
    });
    //设置table标题
    function setTableTitle(){
        var oAjaxOption = {
                type: "get",
                url: sContextPath + "/rest/getSingleCluster.json",
                contentType: "application/json",
                dataType: "json",
                success: function(oData, oStatus) {
                        document.getElementById('clusterInfo').innerHTML="(来自"+oData.memberName+"的回复)";
                        util.showWarningDialog();
                },
                error: function(oData, oStatus, eErrorThrow) { 
                    util.handleAjaxError(oData, oStatus, eErrorThrow);
                },
                complete: function (oXmlHttpRequest, oStatus) {
                }
        };
        $.ajax(oAjaxOption);
    }
    //设置模态框的详细信息
    function setDetail(oData){
            document.getElementById("FollowerRow").style.display="";
            $("#memberName").content(oData.shardName);
            $("#RaftState").content(oData.raftState);
            $("#LastLeadershipChangeTime").content(oData.lastLeadershipChangeTime);
            $("#StatRetrievalTime").content(oData.statRetrievalTime);
            if(oData.followerInfo == null ){
                document.getElementById("FollowerRow").style.display="none";
            }else{
                $("#FollowerInfo").content(oData.followerInfo);
            }
            //$("#load-mode").content(oData.loadMmode);
    }
    //获取模态框的详细信息
    function getDetails(){
        nodeId = $(this).attr("data-click-data");//获取设备的node_id
        var oAjaxOption = {
                type: "get",
                url: sContextPath + "/rest/getClusterDetails/"+nodeId+".json",
                contentType: "application/json",
                dataType: "text",
                data:nodeId,
                success: function(oData, oStatus) {
                    setDetail(JSON.parse(oData));
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
    // 画面初期化
    function setPage(){
        initPage();
    }
    function initPage() {
//        // 画面事件绑定及JS插件渲染
//        initPageComponent();
        // 画面数据取得并设定
        var oAjaxOption = {
                type: "get",
                url: sContextPath + "/rest/monitorClusterInfo.json",
                contentType: "application/json",
                dataType: "json",
                success: function(oData, oStatus) {
                        setClusterTbl(oData);
                        util.showWarningDialog();
                },
                error: function(oData, oStatus, eErrorThrow) { 
                    util.handleAjaxError(oData, oStatus, eErrorThrow);
                },
                complete: function (oXmlHttpRequest, oStatus) {
                }
        };
        $.ajax(oAjaxOption);
    }
    // 画面初期化
    setPage();
//    util.showMessage(500);
    // 每隔8s刷新页面
    //setInterval(initPage,8000);
});