package com.xigua.util;

import com.xigua.JSONTemplate.ControllerSettings;

/*
 * 与控制器解接口，未来有可能是多控制器所以需要可变化
 */
public class GetHttpUrl {
    private String Ipaddress = ControllerSettings.ip;
    
    public String getPortUrl(String oltId){
        String url =Ipaddress+"/restconf/operational/network-"
                + "topology:network-topology/topology/topology-netconf/node/"
                +oltId+"/yang-ext:mount/zxr10-pm-sys:state/sys/ports";
        return url;
    }
    
}
