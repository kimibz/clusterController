+$(function () {
    // 严格模式
    "use strict";
    
    // =================================== 变量声明 =================================== //
    // 取得ContextPath    
    var nodeId;
    var sContextPath = util.getContextPath();
    var ifVlan;
    // =================================== 方法执行 =================================== //
    //table初始化
    function setServiceTbl(oData) {
        var hHtml = template("vxlan-template", {list: oData});
        $("#vxlan-placeholder").html(hHtml);
        /*var key;
        for(var item in oData) {
            if(oData[item].state == "离线"){
                    key =oData[item].node_id +"Info";
                    $("#"+key).attr("class","btn btn-outline btn-sm disabled");
                }
            }*/
        $("#vxlan a[data-click='get']").bind("click", getDetails);
        $("#vxlan a[data-click='delete']").bind("click", deleteBind);
        $("#deleteService").bind("click", deleteServiceBind);
    }
    $("#addNewDevice").click(function() {
        $("#form").removeClass("was-validated");
        $('#vlan').removeClass('is-invalid');
        $("#vlan").val("");
        document.getElementById("source").options.selectedIndex = 0; //回到空选择状态
        //$("#source").selectpicker('refresh');
        document.getElementById("destination").options.selectedIndex = 0; //回到空选择状态
        //$("#destination").selectpicker('refresh');
    });
    //获取单个业务流并显示
    function getDetails(){
        var index = $(this).attr("data-click-data");
        var oAjaxOption = {
                type: "get",
                url: sContextPath + "/rest/vxlan/serviceInfo/"+index+".json",
                contentType: "application/json",
                dataType: "json",
                success: function(oData, oStatus) {
                        setPicture(oData);
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
    //显示图片
    function setPicture(oData){
        if(oData.destination=="B"){
            $('#topoGreen').hide();
            $('#topoRed').show(500);
            $('#onu1vlan2').content("");
            $('#cloudvlan').content("");
            $('#vxlan3').content("");
            $('#vxlan4').content("");
            $('#onu1vlan').content("用户规划VLAN:"+oData.vlan)
            $('#onu2vlan').content("用户规划VLAN:"+oData.vlan)
            $('#vxlan1').content("VxLAN1:"+oData.vxlanA)
            $('#vxlan2').content("VxLAN2:"+oData.vxlanB)
        }else if(oData.destination=="C"){
            $('#topoRed').hide();
            $('#topoGreen').show(500);
            $('#onu1vlan').content("");
            $('#onu2vlan').content("");
            $('#vxlan1').content("");
            $('#vxlan2').content("");
            $('#onu1vlan2').content("用户规划VLAN:"+oData.vlan)
            $('#cloudvlan').content("用户规划VLAN:"+oData.vlan)
            $('#vxlan3').content("VxLAN1:"+oData.vxlanA)
            $('#vxlan4').content("VxLAN2:"+oData.vxlanB)
        }
    }
    //绑定ID
    function deleteBind(){
        nodeId = $(this).attr("data-click-data");
        $("#deleteService").attr("data-click-data", nodeId); 
    }
    //删除业务流
    function deleteServiceBind(){
        var index = $(this).attr("data-click-data");
        var oAjaxOption = {
                type: "delete",
                url: sContextPath + "/rest/vxlan/serviceInfo/"+index+".json",
                contentType: "application/json",
                dataType: "json",
                success: function(oData, oStatus) {
                    
                },
                error: function(oData, oStatus, eErrorThrow) { 
                    util.handleAjaxError(oData, oStatus, eErrorThrow);
                },
                complete: function (oXmlHttpRequest, oStatus) {
                    setPage();
                    $.unblockUI();
                }
        };
        $.blockUI(util.getBlockOption());
        $.ajax(oAjaxOption);
    }
    function checkIfVlan(){
        var data = $("form").form2object();
        data.vlan =$("#vlan").val();
        var source = data.source;
        var vlan = data.vlan;
        var oAjaxOption = {
                type: "get",
                url: sContextPath + "/rest/vxlan/findService/"+source+"/"+vlan+".json",
                contentType: "application/json",
                dataType: "json",
                success: function(oData, oStatus) {
                        if(oData!=null){
                            ifVlan = true;
                        }else{
                            ifVlan = false;
                        }
                },
                error: function(oData, oStatus, eErrorThrow) { 
                    util.handleAjaxError(oData, oStatus, eErrorThrow);
                },
                complete: function (oXmlHttpRequest, oStatus) {
                }
        };
        $.ajax(oAjaxOption);

    }
    //添加业务流
    function addService(){
        var data = $("form").form2object();
        data.vlan =$("#vlan").val();
        var oAjaxOption = {
                type: "post",
                url: sContextPath + "/rest/newVxlanService.json",
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
                    setPage();
                    $.unblockUI();
                }
        };
        $.blockUI(util.getBlockOption());
        $.ajax(oAjaxOption);
    }
    //校验添加流的表单
    function validateForm(){
        window.addEventListener('load', function() {
            // Fetch all the forms we want to apply custom Bootstrap validation styles to
        var forms = document.getElementsByClassName('needs-validation');
        // Loop over them and prevent submission
        var vlan = parseInt($('#vlan').val());
        var validation = Array.prototype.filter.call(forms, function(form) {
        form.addEventListener('submit', function(event) {
            var vlan = parseInt($('#vlan').val());
            var source = $("#source").val();
            if(!isNaN(vlan))
                if(source!=null){
                checkIfVlan();
            }
            //如果VLAN重复
            if(vlan=""||ifVlan||form.checkValidity() === false||vlan<0||vlan>4096){
                $('#vlan').addClass('is-invalid');
                event.preventDefault();
                event.stopPropagation();
            }else{
                addService();
            }
            form.classList.add('was-validated');
            }, false);
        });
        }, false);
    }
    
    // 画面初期化
    function setPage(){
        initPage();
        validateForm();
        $('#topoGreen').hide();
        $('#topoRed').hide();
    }
    function initPage() {
//        // 画面事件绑定及JS插件渲染
//        initPageComponent();
        // 画面数据取得并设定
        var oAjaxOption = {
                type: "get",
                url: sContextPath + "/rest/vxlan/serviceInfo.json",
                contentType: "application/json",
                dataType: "json",
                success: function(oData, oStatus) {
                        setServiceTbl(oData);
                        util.showWarningDialog();
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
    setPage();
    //    util.showMessage(500);
    // 每隔8s刷新页面
    //setInterval(initPage,8000);
});