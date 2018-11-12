package com.xigua.serviceImp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.xigua.JSONTemplate.ControllerSettings;
import com.xigua.JSONTemplate.JSONTemplate;
import com.xigua.dao.vxlanDao;
import com.xigua.model.VxlanServiceModel;
import com.xigua.model.VxlanServicePage;
import com.xigua.model.vlanId;
import com.xigua.model.vxlanAddModel;
import com.xigua.service.VxlanService;
import com.xigua.util.HttpRequestUtil;
@Service
public class VxlanServiceImpl implements VxlanService{
    @Autowired
    private vxlanDao dao;
    
    private static String Ipaddress = ControllerSettings.ip;
    
    @Override
    public List<VxlanServicePage> getAll() {
        // TODO Auto-generated method stub
        List<VxlanServiceModel> modelList = dao.getAllInfo();
        //翻译用户名字
        for(int i=0;i<modelList.size();i++) {
            VxlanServiceModel model = new VxlanServiceModel();
            model = modelList.get(i);
            model.setSource(checkUsrName(model.getSource()));
            model.setDestination(checkUsrName(model.getDestination()));
        }
        //分页
        int page = 0;
        int number =2;
        int totalRows = modelList.size();
        if ((totalRows % number) == 0) {  
            page = totalRows / number;  
          } else {  
              page = totalRows / number + 1;  
          }
        List<VxlanServicePage> result = new ArrayList<VxlanServicePage>();
        for(int i=0,len=page-1;i<=len;i++) {
            int fromIndex = i*number;
            int toIndex = ((i == len) ? totalRows : ((i + 1) * number));
            VxlanServicePage model = new VxlanServicePage();
            model.setCurrency(i+1);
            model.setModel(modelList.subList(fromIndex, toIndex));
            result.add(model);
        }
        return result;
    }
    //翻译用户名字
    String checkUsrName(String usr) {
        Map<String,String> map = new HashMap<String,String>();
        map.put("A", "网关A");
        map.put("B", "网关B");
        map.put("C", "云");
        String result = map.get(usr);
        return result;
    }
    @Override
    public VxlanServiceModel getSingleModel(String index) {
        // TODO Auto-generated method stub
        VxlanServiceModel model = dao.getServiceInfo(Integer.parseInt(index));
        //model.setDestination(checkUsrName(model.getDestination()));
        //model.setSource(checkUsrName(model.getSource()));
        return model;
    }
    @Override
    public void deleteSingleModel(String index) {
        // TODO Auto-generated method stub
        String node = "17830";
;        VxlanServiceModel model = dao.getServiceInfo(Integer.parseInt(index));
        String vxlanA = model.getVxlanA();
        String vxlanB = model.getVxlanB();
        deleteConfigVxlan(node,vxlanA);
        dao.deleteServiceInfo(Integer.parseInt(index));
    }
    @Override
    public void addSingleModel(vxlanAddModel model) {
        // TODO Auto-generated method stub
        String node = "17830";
        VxlanServiceModel Amodel = new VxlanServiceModel();
        Amodel.setSource(model.getSource());
        Amodel.setDestination(model.getDestination());
        Amodel.setVlan(model.getVlan());
        String vxlanA = randomVxlan();
        Amodel.setVxlanA(vxlanA);
        if(model.getDestination().equals("B")) {
            Amodel.setVxlanB("4568");
        }else {
            Amodel.setVxlanB("6666");
        }
        //global-enable
        globalEnable(node);
        setVxlanTunnel(node);
        //创建vxlan实例
        setVxlanInstance(node,vxlanA);
        //vlan-ac绑定
        bindVlan(model.getVlan(),vxlanA,node);
        //隧道-VNI绑定
        bindVxlan(node ,vxlanA);
        dao.insertServiceInfo(Amodel);
    }
    //vxlan global enable
    public static void globalEnable(String nodeId) {
        String url = Ipaddress + "/restconf/config/network-topology:network-topology/topology/topology-netconf/"
                + "node/"+nodeId+"/yang-ext:mount/ctc-vxlan:vxlan";
        String entity = JSONTemplate.globalEnable;
        HttpRequestUtil.Put(url, entity);
    }
    //创建VXLAN实例
    public static void setVxlanInstance(String nodeId,String vxlanId) {
        String url = Ipaddress+"/restconf/config/network-topology:network-topology"
                + "/topology/topology-netconf/node/"+nodeId+"/yang-ext:mount/ctc-vxlan:vxlan/vxlan-instance/"+vxlanId;
        String entity = JSONTemplate.createVni;
        JSONObject OBJ = JSON.parseObject(entity);
        JSONObject result = OBJ.getJSONArray("ctc-vxlan:vxlan-instance").getJSONObject(0);
        result.put("ctc-vxlan:vxlan-id", vxlanId);
        HttpRequestUtil.Put(url, OBJ.toJSONString());
    }
    //vlan-ac绑定
    public static void bindVlan(String vlan ,String vxlan,String nodeId) {
        String url = "http://192.168.70.129:6500/restconf/config/network-topology:network-topology"
                + "/topology/topology-netconf/node/"+nodeId+"/yang-ext:mount/ctc-vxlan:vxlan/vxlan-instance/"+vxlan;
        String entity = JSONTemplate. vlan_ac;
        JSONObject obj = JSON.parseObject(entity);
        JSONObject result = obj.getJSONArray("vxlan-instance").getJSONObject(0);
        result.put("vxlan-id", vxlan);
        JSONArray vlanList = result.getJSONArray("access-vlan-list");
        JSONObject vlanObj = vlanList.getJSONObject(0);
        vlanObj.put("vlan-id", vlan);
        //若多个vlan则使用这步
        //vlanList.add(JSON.parse(vlanJson(vlan)));
        HttpRequestUtil.Put(url, obj.toJSONString());
    }
    //vlan json
    @SuppressWarnings("unused")
    private static String vlanJson(String vlan) {
        vlanId id = new vlanId();
        id.setVlanId(vlan);
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_DASHES);
        Gson gson = gsonBuilder.create();
        String vlanJson = gson.toJson(id);
        return vlanJson; 
    }
    //隧道-VNI绑定
    public static void bindVxlan(String node ,String vxlan) {
        String vxlanTunnelId = "100";
        String url = Ipaddress+"/restconf/config/network-topology:network-topology/"
                + "topology/topology-netconf/node/"+node+"/yang-ext:mount/ctc-vxlan:vxlan/static-vxlan-tunnel/"+vxlanTunnelId;
        String entity = JSONTemplate.bindVNI;
        JSONObject obj = JSON.parseObject(entity);
        JSONObject result = obj.getJSONArray("ctc-vxlan:static-vxlan-tunnel").getJSONObject(0);
        result.put("ctc-vxlan:vxlan-tunnel-id", "100");
        result.put("ctc-vxlan:vxlan-tunnel-name", "vxlan_tunnel100");
        JSONObject vlanList = result.getJSONArray("ctc-vxlan:bind-vxlan-id").getJSONObject(0);
        vlanList.put("ctc-vxlan:vxlan-id", vxlan);
        HttpRequestUtil.Put(url, obj.toJSONString());
    }
    //配置VXLAN隧道
    public static void setVxlanTunnel(String nodeId) {
        String vxlanTunnelId = "100";
        String url = Ipaddress+"/restconf/config/network-topology:network-topology/"
                + "topology/topology-netconf/node/"+nodeId+"/yang-ext:mount/ctc-vxlan:vxlan/"
                        + "static-vxlan-tunnel/"+vxlanTunnelId;
        String entity = null;
        if(nodeId.equals("zte")) {
            entity = JSONTemplate.zte_vxlanTunnel;
            JSONObject OBJ = JSON.parseObject(entity);
            JSONObject tunnel = OBJ.getJSONArray("ctc-vxlan:static-vxlan-tunnel").getJSONObject(0);
            tunnel.put("ctc-vxlan:tunnel-destination-ip", "20.1.1.12");
            tunnel.put("ctc-vxlan:vxlan-tunnel-id", vxlanTunnelId);
            tunnel.put("ctc-vxlan:vxlan-tunnel-name", "vxlan_tunnel100");
            tunnel.put("ctc-vxlan:tunnel-source-ip", "20.1.1.3");
            HttpRequestUtil.Put(url, OBJ.toJSONString());
            System.out.println(OBJ.toJSONString());
        }else if(nodeId.equals("fiberhome")){
            entity = JSONTemplate.fiberhome_vxlanTunnel;
            JSONObject OBJ = JSON.parseObject(entity);
            JSONObject tunnel = OBJ.getJSONArray("ctc-vxlan:static-vxlan-tunnel").getJSONObject(0);
            tunnel.put("ctc-vxlan:tunnel-destination-ip", "20.1.1.12");
            tunnel.put("ctc-vxlan:vxlan-tunnel-id", vxlanTunnelId);
            tunnel.put("ctc-vxlan:vxlan-tunnel-name", "vxlan_tunnel100");
            tunnel.put("ctc-vxlan:tunnel-source-ip", "20.1.1.2");
            tunnel.put("ctc-vxlan:tunnel-source-ip-prefix-len", "8");
            HttpRequestUtil.Put(url, OBJ.toJSONString());
        }
    }
    //删除控制器里VNI=VXLAN的配置
    public static void deleteConfigVxlan(String nodeId,String vxlan) {
        //删除实例
        String url = Ipaddress + "/restconf/config/network-topology:network-topology/"
                + "topology/topology-netconf/node/"+nodeId+"/yang-ext:mount/"
                + "ctc-vxlan:vxlan/vxlan-instance/"+vxlan;
        //删除绑定
        String url2 = Ipaddress + "/restconf/config/network-topology:network-topology/topology/"
                + "topology-netconf/node/17830/yang-ext:mount/ctc-vxlan:vxlan/"
                + "static-vxlan-tunnel/"+nodeId+"/bind-vxlan-id/"+vxlan;
        HttpRequestUtil.Delete(url);
        //HttpRequestUtil.Delete(url2);
    }
    @Override
    public VxlanServiceModel findModel(String source, String vlan) {
        // TODO Auto-generated method stub
        VxlanServiceModel model = dao.find(source, vlan);
        return model;
    }
    //随机vxlan 
    public String randomVxlan() {
        boolean ifVxlan = true;
        String result = null;
        while(ifVxlan) {
            Long vxlan = 1+(((long) (new Random().nextDouble()*(5000L))));
            result = vxlan + "";
            String id = dao.findVxlan(result);
            if(id == null) {
                ifVxlan = false;
            }
        }
        return result;
    }
}
