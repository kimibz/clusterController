package com.xigua.demo;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.xigua.model.OnuSpawnModel;
import com.xigua.model.serviceVlanModel;
import com.xigua.model.Topo.Onu;
import com.xigua.service.UserOnuService;

@Controller
public class RestUserOnuController {
    
    @Autowired
    private UserOnuService service;
    private static final Logger LOG = LoggerFactory.getLogger(RestUserOnuController.class);
    
    /** spawn NewOnu*/
    @RequestMapping(value="/rest/onuManagement/{nodeId}.json", method=RequestMethod.POST)
    public void spawnNewOnu(@PathVariable String nodeId,@RequestBody String jsonString) {
//        System.out.println(info.getAddress());
        OnuSpawnModel onu = JSON.parseObject(jsonString, new TypeReference<OnuSpawnModel>() {});
        LOG.info(onu.getIfIndex());
        service.addOnu(nodeId, onu);
    }
    /*
     * 返回端口下ONU状态
     */
    @RequestMapping(value = "/rest/onuManagement/{nodeId}/{interfaceName}.json", method = RequestMethod.GET)
    @ResponseBody
    public List<Onu> getOnuStatus(@PathVariable String nodeId,@PathVariable String interfaceName){
        String interfaceFormat = interfaceName.replaceAll("_", "/");
        List<Onu> list = service.getOnu(nodeId, interfaceFormat);
        return list;
    }
    /*
     * 刷新端口下ONU状态
     */
    @RequestMapping(value = "/rest/onuManagementRefresh/{nodeId}/{interfaceName}.json", method = RequestMethod.GET)
    @ResponseBody
    public List<Onu> getOnuStatus2(@PathVariable String nodeId,@PathVariable String interfaceName){
        String []interfaceFormat = interfaceName.split("_");
        String ifIndex = interfaceFormat[0];
        List<Onu> list = service.refreshOnu(nodeId, ifIndex);
        System.out.println("-----refresh");
        return list;
    }
    /*
     * 删除ONU
     */
    @RequestMapping(value = "/rest/onuManagement/{nodeId}/{interfaceName}.json", method = RequestMethod.DELETE,produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public void deleteOnu(@PathVariable String nodeId,@PathVariable String interfaceName){
        service.deleteOnu(nodeId, interfaceName);
        LOG.info("--------删除ONU");
    }
    /*
     * 返回端口下ONU的VLAN配置
     */
    @RequestMapping(value = "/rest/onuManagement/vlan/{nodeId}/{interfaceName}.json", method = RequestMethod.GET)
    @ResponseBody
    public serviceVlanModel getOnuVlan(@PathVariable String nodeId,@PathVariable String interfaceName){
    	serviceVlanModel model = service.getVlan(nodeId, interfaceName);
        return model;
    }
    /*
     * 修改ONU的vlan配置
     */
    @RequestMapping(value = "/rest/onuManagement/vlan/{nodeId}/{interfaceName}.json", method = RequestMethod.PUT)
    public void editOnuVlan(@PathVariable String nodeId,@PathVariable String interfaceName,@RequestBody serviceVlanModel model){
    	service.editServiceVlan(nodeId, interfaceName, model);
    	System.out.println("------修改ONU VLAN");
    }
    /*
     * 新建ONU的vlan配置
     */
    @RequestMapping(value = "/rest/onuManagement/vlan/{nodeId}/{interfaceName}.json", method = RequestMethod.POST)
    public void editNewOnuVlan(@PathVariable String nodeId,@PathVariable String interfaceName,@RequestBody serviceVlanModel model){
    	service.setServiceVlan(nodeId, interfaceName, model);
    	System.out.println("------新建ONU VLAN");
    }
}