package com.xigua.demo;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xigua.model.ClusterInfo;
import com.xigua.model.MonitorCluster;
import com.xigua.service.ClusterMonitorService;

@Controller
public class RestClusterController {
    @Autowired
    private ClusterMonitorService service;
    
    private static final Logger LOG = LoggerFactory.getLogger(RestClusterController.class);
    
    /*
     * 返回集群基本信息
     */
    @RequestMapping(value = "/rest/monitorClusterInfo.json", method = RequestMethod.GET)
    @ResponseBody
    public List<MonitorCluster> getClusterInfo(){
        List<String> list = new ArrayList<String>();
        list = service.getAllNameList();
        List<MonitorCluster> info = new ArrayList<MonitorCluster>();
        for(int i=0; i<list.size() ; i++) {
            MonitorCluster monitor = new MonitorCluster();
            monitor.setMemberName(list.get(i));
            monitor.setStatus("正常");
            info.add(monitor);
        }
        return info;
    }
    /*
     * 返回随机一个信息
     */
    @RequestMapping(value = "/rest/getSingleCluster.json", method = RequestMethod.GET)
    @ResponseBody
    public MonitorCluster getSingleClusterInfo(){
        MonitorCluster cluster = service.getSingleInfo();
        return cluster;
    }
    /*
     * 返回一个集群的详细信息
     */
    @RequestMapping(value = "/rest/getClusterDetails/{member_name}.json", method = RequestMethod.GET)
    @ResponseBody
    public ClusterInfo getClusterDetails(@PathVariable String member_name){
        ClusterInfo info = service.getInfo(member_name);
        return info;
    }
}
