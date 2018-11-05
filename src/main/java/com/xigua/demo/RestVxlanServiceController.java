package com.xigua.demo;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xigua.model.SpawnNewVirDevice;
import com.xigua.model.VxlanServiceModel;
import com.xigua.model.VxlanServicePage;
import com.xigua.model.vxlanAddModel;
import com.xigua.serviceImp.VxlanServiceImpl;

@Controller
public class RestVxlanServiceController {
    @Autowired
    private VxlanServiceImpl service;
    
    private static final Logger LOG = LoggerFactory.getLogger(RestVxlanServiceController.class);
    
    /*
     * 返回所有业务信息
     */
    @RequestMapping(value = "/rest/vxlan/serviceInfo.json", method = RequestMethod.GET)
    @ResponseBody
    public List<VxlanServicePage> getServiceInfo(){
        return service.getAll();
    }
    /*
     * 返回单个业务信息
     */
    @RequestMapping(value = "/rest/vxlan/serviceInfo/{index}.json", method = RequestMethod.GET)
    @ResponseBody
    public VxlanServiceModel getSingleServiceInfo(@PathVariable String index){
        return service.getSingleModel(index);
    }
    /*
     * 通过源和VLAN查询业务流
     */
    @RequestMapping(value = "/rest/vxlan/findService/{source}/{vlan}.json", method = RequestMethod.GET)
    @ResponseBody
    public VxlanServiceModel findService(@PathVariable String source,@PathVariable String vlan){
        VxlanServiceModel model = service.findModel(source, vlan);
        return model;
    }
    /*
     * 删除单个业务信息
     */
    @RequestMapping(value = "/rest/vxlan/serviceInfo/{index}.json", method = RequestMethod.DELETE)
    @ResponseBody
    public void deleteSingleServiceInfo(@PathVariable String index){
        service.deleteSingleModel(index);
    }
    /*
     * 添加新的业务流
     */
    @RequestMapping(value="/rest/newVxlanService.json", method=RequestMethod.POST)
    public void spawnNewVirslice(@RequestBody vxlanAddModel model) {
        service.addSingleModel(model);
    }
    
}
