package com.xigua.serviceImp;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xigua.JSONTemplate.ControllerSettings;
import com.xigua.JSONTemplate.JSONTemplate;
import com.xigua.dao.DeviceNameDao;
import com.xigua.dao.ManageVirtualUsrDao;
import com.xigua.model.Port;
import com.xigua.model.SpawnInfo;
import com.xigua.model.SysConfig;
import com.xigua.service.SysInfoService;
import com.xigua.util.HttpRequestUtil;

@Service
public class SysInfoServiceImpl implements SysInfoService{
    @Autowired
    private DeviceNameDao dao ;
    private String Ipaddress = ControllerSettings.ip;
    private static final Logger LOG = LoggerFactory.getLogger(SysInfoServiceImpl.class);
    
    @Override
    public SysConfig getConfig(String device) {
        // TODO Auto-generated method stub
//        String url = Ipaddress+"/restconf/config/"
//                + "opendaylight-inventory:nodes/node/"
//                + device +"/yang-ext:mount/zxr10-pm-sys:configuration/sys/";
        String url = Ipaddress+"/restconf/config/network-topology:network-topology"
                + "/topology/topology-netconf/node/"
                + device +"/yang-ext:mount/zxr10-pm-sys:configuration/sys/";
        String result = HttpRequestUtil.Get(url);
        SysConfig config  = new SysConfig();
        if(result != null){
            JSONObject object = JSON.parseObject(result);
            JSONObject sys = (JSONObject) object.get("sys");
//            config.setContact(sys.getString("contact"));
//            config.setLocation(sys.getString("location"));
            config.setCpuIisolate(sys.getString("cpu-isolate"));
//            config.setHostname(sys.getString("hostname"));
            config.setLoadMmode(sys.getString("load-mode"));
            return config;
        }else{
            return config;
        }
    }

    @Override
    public void SpawnNewDevice(SpawnInfo info) {
        // TODO Auto-generated method stub
        String template = JSONTemplate.SpawnDeviceJSON;
        JSONObject object = JSON.parseObject(template);
        JSONObject device_obj = object.getJSONObject("input");
        device_obj.put("host", info.getAddress());
        device_obj.put("keepalive-delay", info.getAliveTime());
        device_obj.put("node-id", info.getNode_id());
        device_obj.put("password", info.getUsername());
        device_obj.put("username", info.getPassword());
        device_obj.put("tcp-only", info.getTcp());
        device_obj.put("port", info.getPort());
        device_obj.put("reconnect-on-changed-schema", true);
        String url = Ipaddress+"/restconf/operations/netconf-node-topology:create-device";
        HttpRequestUtil.Post(url, object.toJSONString());
        //hostId存入数据库
        dao.addDeviceById(info.getNode_id(), info.getHost_id());
    }

    @Override
    public List<Port> getPortList(String oltId) {
        // TODO Auto-generated method stub
        String url =Ipaddress+"/restconf/operational/network-"
                + "topology:network-topology/topology/topology-netconf/node/"
                +oltId+"/yang-ext:mount/zxr10-pm-sys:state/sys/ports";
        String jsonStr = HttpRequestUtil.Get(url);
        JSONObject object = JSON.parseObject(jsonStr);
        JSONObject jsonPort = object.getJSONObject("ports");
        List <Port> port = JSON.parseArray(jsonPort.get("port").
                toString(),Port.class);
        LOG.info(port.get(0).getPortname());
        return port;
    }

    @Override
    public Port getPort(String oltId, String shelf, String slot, String subslot, String portno) {
        // TODO Auto-generated method stub
        String url =Ipaddress+"/restconf/operational/network-topology:network-topology"
                + "/topology/topology-netconf/node/"
                + oltId +"/yang-ext:mount/zxr10-pm-sys:state/sys/ports/"
                        + "port/"+shelf+"/"+slot+"/"+subslot+"/"+portno;
        String jsonStr = HttpRequestUtil.Get(url);
        JSONObject object = JSON.parseObject(jsonStr);
        List <Port> portlist = JSON.parseArray(object.get("port").
                toString(),Port.class);
        Port port = portlist.get(0);
        return port;
    }

   

}
