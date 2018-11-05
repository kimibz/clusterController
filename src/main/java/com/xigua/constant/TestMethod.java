package com.xigua.constant;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xigua.JSONTemplate.JSONTemplate;
import com.xigua.model.serviceVlanModel;
import com.xigua.util.HttpRequestUtil;

public class TestMethod {
    public static void main(String[] args) throws Exception {
        // TODO Auto-generated method stub
    	String vndName = "vDevice_zte_vnd001";
    	String ifIndex = "285280008";
    	String ifSubIndex = "402850048";
    	String url = "http://localhost:8181/restconf/"
    			+ "config/network-topology:network-topology/"
    			+ "topology/topology-netconf/node/"
    			+ vndName + "/yang-ext:mount/"
    			+ "zxr10-vlan-dev-c600:configuration/"
    			+ "service-port/service-port/"
    			+ ifIndex + "/" + ifSubIndex;
    	String result = HttpRequestUtil.Get(url);
    	JSONObject service_port = JSON.parseObject(result).
    			getJSONArray("service-port").getJSONObject(0);
    	String net_vlan = service_port.getString("net-vlan-id");
    	String user_vlan = service_port.getString("user-vlan-id");
    	System.out.println(net_vlan+user_vlan);
    }
} 
