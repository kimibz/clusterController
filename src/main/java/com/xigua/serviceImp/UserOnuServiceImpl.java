package com.xigua.serviceImp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.xigua.JSONTemplate.ControllerSettings;
import com.xigua.JSONTemplate.JSONTemplate;
import com.xigua.model.OnuSpawnModel;
import com.xigua.model.serviceVlanModel;
import com.xigua.model.Topo.Onu;
import com.xigua.service.UserOnuService;
import com.xigua.service.vlanService;
import com.xigua.util.HttpRequestUtil;
import com.xigua.util.Util;
@Service
public class UserOnuServiceImpl implements UserOnuService{
    @Autowired 
    private vlanService service; 
    private String Ipaddress = ControllerSettings.ip;
    
    private static final Logger LOG = LoggerFactory.getLogger(UserOnuServiceImpl.class); 
    
    @Override
    public void addOnu(String vndName, OnuSpawnModel OnuModel) {
        // TODO Auto-generated method stub
        Util util = new Util();
        int ifIndex = util.getIndex(OnuModel.getIfIndex());
        String url = Ipaddress+"/restconf/config/"
                + "network-topology:network-topology/"
                + "topology/topology-netconf/node/"+vndName
                + "/yang-ext:mount/zxr10-test:configuration";
        JSONObject onu = JSON.parseObject(JSONTemplate.addOnu);
        JSONObject onuList = onu.getJSONArray("zxr10-test:onulist")
                .getJSONObject(0);
        onuList.put("zxr10-test:zxAnGponOnuMgmtTypeName",OnuModel.getOnuType());
        onuList.put("zxr10-test:zxAnGponOnuMgmtRegMode", "regModeSn");
        onuList.put("zxr10-test:ifIndex", ""+ifIndex);
        onuList.put("zxr10-test:zxAnPonOnuIndex", OnuModel.getOnuId());
        onuList.put("zxr10-test:zxAnGponOnuMgmtSn",OnuModel.getOnuMac());
        HttpRequestUtil.Post(url, onu.toString());
        //绑定业务和线路模板
        //线路模板
        String serviceBind = JSONTemplate.servicebind;
        String type = "onuLinePrf";
        String profileName = "test";
        JSONObject object = JSON.parseObject(serviceBind);
        JSONObject lineBindObj = object.getJSONArray("zxr10-test:onuBindList").getJSONObject(0);
        lineBindObj.put("zxr10-test:onuProfileType", type);
        lineBindObj.put("zxr10-test:onuProfileName", profileName);
        lineBindObj.put("zxr10-test:onuIndex", OnuModel.getOnuId());
        lineBindObj.put("zxr10-test:ifIndex", ifIndex);
        String LineUrl = Ipaddress+"/restconf/config/network-topology:network-topology"
               + "/topology/topology-netconf/node/vDevice_zte_vnd001/yang-ext:mount/"
               + "zxr10-test:configuration/onuBindList/"+ifIndex+"/"+OnuModel.getOnuId()+"/"+type;
        HttpRequestUtil.Put(LineUrl, object.toJSONString());
        //业务模板
        String profileName_Service = "onuServicePrf" ; 
        JSONObject serviceBindObj = JSON.parseObject(serviceBind);
        JSONObject serviceBindData = serviceBindObj.getJSONArray("zxr10-test:onuBindList").getJSONObject(0);
        serviceBindData.put("zxr10-test:onuProfileType", profileName_Service);
        serviceBindData.put("zxr10-test:onuProfileName", profileName);
        serviceBindData.put("zxr10-test:onuIndex", OnuModel.getOnuId());
        serviceBindData.put("zxr10-test:ifIndex", ifIndex);
        String ServiceUrl = Ipaddress+"/restconf/config/network-topology:network-topology"
                + "/topology/topology-netconf/node/vDevice_zte_vnd001/yang-ext:mount/"
                + "zxr10-test:configuration/onuBindList/"+ifIndex+"/"+OnuModel.getOnuId()+"/"+profileName_Service;
        HttpRequestUtil.Put(ServiceUrl,serviceBindObj.toJSONString());
    }

    @Override
    public List<Onu> getOnu(String vndName, String interfaceName) {
        // TODO Auto-generated method stub
        String url = Ipaddress+"/restconf/"
                + "operational/network-topology:network-topology/"
                + "topology/topology-netconf/node/" + vndName
                + "/yang-ext:mount/zxr10-test:state";
        String result = HttpRequestUtil.Get(url);
        ArrayList<Onu> onuOfPon = new ArrayList<Onu>();
        if(result != null) {
        	String JSON_ARRAY_STR = JSON.parseObject(result).
                    getJSONObject("state").getJSONArray("onuStatus").toJSONString();
//            JSONArray arr = JSON.parseObject(result).getJSONObject("state")
//                    .getJSONArray("onuStatus");
            ArrayList<Onu> onuList = JSON.parseObject(JSON_ARRAY_STR, new TypeReference<ArrayList<Onu>>() {});
            Util util = new Util();
            int ifIndex = util.getIndex(interfaceName);
            for(Onu onu : onuList) {
                if(onu.getZxAnGponSrvOnuPhaseStatus().equals("offline")) {
                    onu.setZxAnGponSrvOnuPhaseStatus("离线");
                }else {
                    onu.setZxAnGponSrvOnuPhaseStatus("在线");
                }
                if(onu.getIfIndex() == ifIndex) {
                    onuOfPon.add(onu);
                }
            }
            Collections.sort(onuOfPon,new Comparator<Onu>(){
                public int compare(Onu arg0, Onu arg1) {
                    int hits0 = arg0.getZxAnPonOnuIndex();  
                    int hits1 = arg1.getZxAnPonOnuIndex();  
                    if (hits1 > hits0) {  
                        return -1;  
                    } else if (hits1 == hits0) {  
                        return 0;  
                    } else {  
                        return 1;  
                    } 
                }
            });
        }
        return onuOfPon;
    }

    @Override
    public void deleteOnu(String vndName, String interfaceName) {
        // TODO Auto-generated method stub
        String []a = interfaceName.split("_");
        String ifIndex = a[0];
        String OnuIndex = a[1];
        String url = Ipaddress+"/restconf/config/network-topology:network-topology/"
                + "topology/topology-netconf/node/" + vndName
                + "/yang-ext:mount/zxr10-test:configuration/onulist/"+ifIndex+"/"+OnuIndex;
        HttpRequestUtil.Delete(url);
    }

	@Override
	public void setServiceVlan(String vndName,String interfaceName ,serviceVlanModel model) {
		// TODO Auto-generated method stub
		String url = Ipaddress+"/restconf/config/network-topology:network-topology/"
				+ "topology/topology-netconf/node/"+vndName+"/yang-ext:mount/"
				+ "zxr10-vlan-dev-c600:configuration/service-port";
		String []a = interfaceName.split("_");
        String ifIndex = a[0];
        String OnuIndex = a[1];
        String ifSubIndex = Util.OnuIndexToIfSubIndex(OnuIndex); 
		JSONObject vlan = JSON.parseObject(JSONTemplate.serviceVlanEdit);
        JSONObject servicePort = vlan.getJSONObject("zxr10-vlan-dev-c600:service-port").
        		getJSONArray("zxr10-vlan-dev-c600:service-port").getJSONObject(0);
        servicePort.put("zxr10-vlan-dev-c600:user-vlan-id", model.getUsrVlan());
        servicePort.put("zxr10-vlan-dev-c600:mode", "tag");
        servicePort.put("zxr10-vlan-dev-c600:if-sub-index", ifSubIndex);
        servicePort.put("zxr10-vlan-dev-c600:vport-id", "1");
        servicePort.put("zxr10-vlan-dev-c600:if-index", ifIndex);
        servicePort.put("zxr10-vlan-dev-c600:net-vlan-id", model.getNetVlan());
        HttpRequestUtil.Put(url, vlan.toJSONString());
		
	}

	@Override
	public serviceVlanModel getVlan(String vndName, String interfaceName) {
		// TODO Auto-generated method stub
		//String vndName = "vDevice_zte_vnd001";
		serviceVlanModel model = new serviceVlanModel();
		String []a = interfaceName.split("_");
        String ifIndex = a[0];
        String OnuIndex = a[1];
    	String ifSubIndex = Util.OnuIndexToIfSubIndex(OnuIndex);
    	String url = Ipaddress + "/restconf/"
    			+ "config/network-topology:network-topology/"
    			+ "topology/topology-netconf/node/"
    			+ vndName + "/yang-ext:mount/"
    			+ "zxr10-vlan-dev-c600:configuration/"
    			+ "service-port/service-port/"
    			+ ifIndex + "/" + ifSubIndex;
    	String result = HttpRequestUtil.Get(url);
    	if(result != null) {
    		JSONObject service_port = JSON.parseObject(result).
        			getJSONArray("service-port").getJSONObject(0);
        	String net_vlan = service_port.getString("net-vlan-id");
        	String user_vlan = service_port.getString("user-vlan-id");
    		model.setNetVlan(net_vlan);
    		model.setUsrVlan(user_vlan);
    	}
    	return model;
	}

	@Override
	public List<Onu> refreshOnu(String vndName, String ifIndex) {
		// TODO Auto-generated method stub
		String url = Ipaddress+ "/restconf/"
                + "operational/network-topology:network-topology/"
                + "topology/topology-netconf/node/" + vndName
                + "/yang-ext:mount/zxr10-test:state";
        String result = HttpRequestUtil.Get(url);
        ArrayList<Onu> onuOfPon = new ArrayList<Onu>();
        if(result != null) {
        	String JSON_ARRAY_STR = JSON.parseObject(result).
                    getJSONObject("state").getJSONArray("onuStatus").toJSONString();
//            JSONArray arr = JSON.parseObject(result).getJSONObject("state")
//                    .getJSONArray("onuStatus");
            ArrayList<Onu> onuList = JSON.parseObject(JSON_ARRAY_STR, new TypeReference<ArrayList<Onu>>() {});
            
            Util util = new Util();
            int ifIndex2 = Integer.parseInt(ifIndex);
            for(Onu onu : onuList) {
                if(onu.getZxAnGponSrvOnuPhaseStatus().equals("offline")) {
                    onu.setZxAnGponSrvOnuPhaseStatus("离线");
                }else {
                    onu.setZxAnGponSrvOnuPhaseStatus("在线");
                }
                if(onu.getIfIndex() == ifIndex2) {
                    onuOfPon.add(onu);
                }
            }
            Collections.sort(onuOfPon,new Comparator<Onu>(){
                public int compare(Onu arg0, Onu arg1) {
                    int hits0 = arg0.getZxAnPonOnuIndex();  
                    int hits1 = arg1.getZxAnPonOnuIndex();  
                    if (hits1 > hits0) {  
                        return -1;  
                    } else if (hits1 == hits0) {  
                        return 0;  
                    } else {  
                        return 1;  
                    } 
                }
            });
        }
        return onuOfPon;
	}

	@Override
	public void editServiceVlan(String vndName, String interfaceName, serviceVlanModel model) {
		// TODO Auto-generated method stub
		String url = Ipaddress+"/restconf/config/network-topology:network-topology/"
				+ "topology/topology-netconf/node/"+vndName+"/yang-ext:mount/"
				+ "zxr10-vlan-dev-c600:configuration/service-port";
		String []a = interfaceName.split("_");
        String ifIndex = a[0];
        String OnuIndex = a[1];
        String ifSubIndex = Util.OnuIndexToIfSubIndex(OnuIndex); 
        //删除service port
      	String deleteUrl = Ipaddress+"/restconf/config/network-topology:"
      			+ "network-topology/topology/topology-netconf/node/"+ vndName +"/yang-ext:"
      			+ "mount/zxr10-vlan-dev-c600:configuration/service-port/"
      			+ "service-port/"+ ifIndex +"/" + ifSubIndex;
      	HttpRequestUtil.Delete(deleteUrl);
      	//将端口改成hybrid
      	service.changePort(vndName, interfaceName);
		JSONObject vlan = JSON.parseObject(JSONTemplate.serviceVlanEdit);
        JSONObject servicePort = vlan.getJSONObject("zxr10-vlan-dev-c600:service-port").
        		getJSONArray("zxr10-vlan-dev-c600:service-port").getJSONObject(0);
        servicePort.put("zxr10-vlan-dev-c600:user-vlan-id", "100");
        servicePort.put("zxr10-vlan-dev-c600:mode", "tag");
        servicePort.put("zxr10-vlan-dev-c600:if-sub-index", ifSubIndex);
        servicePort.put("zxr10-vlan-dev-c600:vport-id", "1");
        servicePort.put("zxr10-vlan-dev-c600:if-index", ifIndex);
        servicePort.put("zxr10-vlan-dev-c600:net-vlan-id", model.getNetVlan());
        HttpRequestUtil.Put(url, vlan.toJSONString());
	}
    
}
