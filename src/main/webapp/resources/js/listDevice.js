+$(function () {
	// 严格模式
    "use strict";
	
    // =================================== 变量声明 =================================== //
    // 取得ContextPath	
	var nodeId;
    var sContextPath = util.getContextPath();
	// =================================== 方法执行 =================================== //
    //table初始化
    function setDeviceTbl(oData) {
        var hHtml = template("device-template", {list: oData});
        $("#device-placeholder").html(hHtml);
        var key;
        for(var item in oData) {
            if(oData[item].state == "离线"){
                    key =oData[item].node_id +"Info";
                    $("#"+key).attr("class","btn btn-outline btn-sm disabled");
                }
            }
        $("#device a[data-click='edit']").bind("click", changeNameBind);
        $("#device a[data-click='delete']").bind("click", bindDeleteId);
        $("#device a[data-click='get']").bind("click", getSysConfig);
        $("#device a[data-click='getInfo']").bind("click", goToShowVirtual);
        $("#deleteDevice").bind("click", deleteDeviceBind);
    }
    $("#addNewDevice").click(function() {
        ClearForm();
//        $("#spawnModal").modal();
    });
    //清楚新建设备表单
    function ClearForm(){
        $("#node_id").val("");
        $("#host_id").val("");
        $("#username").val("");
        $("#password").val("");
        $("#tcp").attr("checked",false);
        $("#address").val("");
        $("#port").val("");
        $("#aliveTime").val("");
    }
    
    //设置模态框sysConfig
    function setSysInfo(oData){
            $("#hostname").content(oData.hostname);
            $("#contact").content(oData.contact);
            $("#cpu-isolate").content(oData.cpuIisolate);
            $("#location").content(oData.location);
            $("#load-mode").content(oData.loadMmode);
    }
    //模态框 修改设备名称 确定按钮按下
    $("#myModal a[data-click='add']").click(function(){
    	var newId=$("#new_device_id").val();//获取输入框的值
    	var oAjaxOption = {
                type: "put",
                url: sContextPath + "/rest/deviceData/" + nodeId + ".json",
                contentType: "application/json",
                dataType: "text",
                data:newId,
                success: function(oData, oStatus) {
                	initPage();
                },
                error: function(oData, oStatus, eErrorThrow) {
                    util.handleAjaxError(oData, oStatus, eErrorThrow);
                },
                complete: function (oXmlHttpRequest, oStatus) {
                }
        };
        $.ajax(oAjaxOption);
    });
    //新增OLT设备
    $("#spawnModal a[data-click='add']").click(function(){
        var bSuccess = false;
        var oAjaxOption = {
                type: "post",
                url: sContextPath + "/rest/spawnNewDevice.json",
                data: $("form").form2json(),
                contentType: "application/json",
                dataType: "json",
                success: function(oData, oStatus) {
                    initPage();
                    bSuccess = true;
                },
                error: function(oData, oStatus, eErrorThrow) {
                    util.handleAjaxError(oData, oStatus, eErrorThrow);
                },
                complete: function(oXmlHttpRequest, oStatus) {
                    $.unblockUI();
                    initPage();
                }
            };
            $.blockUI(util.getBlockOption());
            $.ajax(oAjaxOption);
    });
    
    //修改设备Id功能
    function changeNameBind(){
    	nodeId = $(this).attr("data-click-data");//获取设备的node_id
    	$("#new_device_id").val("");
    }
    function goToShowVirtual(){
        nodeId = $(this).attr("data-click-data");
        var url = sContextPath +"/showVirtualDevice/"+nodeId;
        window.location.href = url;
    }
    //给table绑值
    function bindDeleteId(){
        nodeId = $(this).attr("data-click-data");
        $("#deleteDevice").attr("data-click-data", nodeId); 
        var ii = $("#deleteDevice").attr("data-click-data");
    }
    //删除设备
    function deleteDeviceBind(){
        nodeId = $(this).attr("data-click-data");//获取设备的node_id
        var oAjaxOption = {
                type: "delete",
                url: sContextPath + "/rest/deleteOneDevice.json",
                contentType: "application/json",
                dataType: "text",
                data:nodeId,
                success: function(oData, oStatus) {
                	initPage();
                },
                error: function(oData, oStatus, eErrorThrow) {
                    util.handleAjaxError(oData, oStatus, eErrorThrow);
                },
                complete: function (oXmlHttpRequest, oStatus) {
                    $.unblockUI();
                    initPage();
                }
        };
        $.blockUI(util.getBlockOption());
        $.ajax(oAjaxOption);
    }
    //设置模态框SysConfig信息
    function getSysConfig(){
        nodeId = $(this).attr("data-click-data");//获取设备的node_id
        var oAjaxOption = {
                type: "get",
                url: sContextPath + "/rest/deviceSysInfo/"+nodeId+".json",
                contentType: "application/json",
                dataType: "text",
                data:nodeId,
                success: function(oData, oStatus) {
                    setSysInfo(JSON.parse(oData));
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
    //设置图表
    function setChart(){
        var randomScalingFactor = function(){ return Math.round(Math.random()*100)};
        var lineChartData = {
        labels : ['January','February','March','April','May','June','July'],
        datasets : [
            {
                label: 'My First dataset',
                backgroundColor : 'rgba(220,220,220,0.2)',
                borderColor : 'rgba(220,220,220,1)',
                pointBackgroundColor : 'rgba(220,220,220,1)',
                pointBorderColor : '#fff',
                data : [randomScalingFactor(),randomScalingFactor(),randomScalingFactor(),randomScalingFactor(),randomScalingFactor(),randomScalingFactor(),randomScalingFactor()]
            },
            {
                label: 'My Second dataset',
                backgroundColor : 'rgba(151,187,205,0.2)',
                borderColor : 'rgba(151,187,205,1)',
                pointBackgroundColor : 'rgba(151,187,205,1)',
                pointBorderColor : '#fff',
                data : [randomScalingFactor(),randomScalingFactor(),randomScalingFactor(),randomScalingFactor(),randomScalingFactor(),randomScalingFactor(),randomScalingFactor()]
            }
            ]
        }

        var ctx = document.getElementById('myChart');
        var chart = new Chart(ctx, {
            type: 'line',
            data: lineChartData,
            options: {
                responsive: true
            }
        });
    }
    function setChart2(){
        var pieData = {
                labels: [
                    'Red',
                    'Green',
                    'Yellow'
                    ],
                    datasets: [{
                        data: [166, 66, 33],
                        backgroundColor: [
                            '#FF6384',
                            '#36A2EB',
                            '#FFCE56'
                            ],
                            hoverBackgroundColor: [
                                '#FF6384',
                                '#36A2EB',
                                '#FFCE56'
                                ]
                    }]
        };
          var ctx = document.getElementById('myChart2');
            var chart = new Chart(ctx, {
                    type: 'pie',
                        data: pieData,
                            options: {
                                      responsive: true
                                          }
             });
    }
    function setChart3(){
        var radarChartData = {
                labels: ['Eating', 'Drinking', 'Sleeping', 'Designing', 'Coding', 'Cycling', 'Running'],
                datasets: [
                    {
                        label: 'My First dataset',
                        backgroundColor: 'rgba(220,220,220,0.2)',
                        borderColor: 'rgba(220,220,220,1)',
                        pointBackgroundColor: 'rgba(220,220,220,1)',
                        pointBorderColor: '#fff',
                        pointHighlightFill: '#fff',
                        pointHighlightStroke: 'rgba(220,220,220,1)',
                        data: [65,59,90,81,56,55,40]
                    },
                    {
                        label: 'My Second dataset',
                        backgroundColor: 'rgba(151,187,205,0.2)',
                        borderColor: 'rgba(151,187,205,1)',
                        pointBackgroundColor: 'rgba(151,187,205,1)',
                        pointBorderColor: '#fff',
                        pointHighlightFill: '#fff',
                        pointHighlightStroke: 'rgba(151,187,205,1)',
                        data: [28,48,40,19,96,27,100]
                    }
                    ]
        };
        var ctx = document.getElementById('myChart3');
        var chart = new Chart(ctx, {
            type: 'radar',
            data: radarChartData,
            options: {
                responsive: true
            }
        });
    }
    // 画面初期化
    function setPage(){
        initPage();
//        setChart();
//        setChart2();
//        setChart3();
    }
    function initPage() {
//        // 画面事件绑定及JS插件渲染
//        initPageComponent();
        // 画面数据取得并设定
        var oAjaxOption = {
                type: "get",
                url: sContextPath + "/rest/AlldeviceInfo.json",
                contentType: "application/json",
                dataType: "json",
                success: function(oData, oStatus) {
                        setDeviceTbl(oData);
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
    // 每隔2s刷新页面
    setInterval(initPage,2000);
});