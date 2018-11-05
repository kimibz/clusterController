package com.xigua.serviceImp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xigua.dao.vxlanDao;
import com.xigua.model.VxlanServiceModel;
import com.xigua.model.VxlanServicePage;
import com.xigua.model.vxlanAddModel;
import com.xigua.service.VxlanService;
@Service
public class VxlanServiceImpl implements VxlanService{
    @Autowired
    private vxlanDao dao;
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
        VxlanServiceModel model = dao.getServiceInfo(Integer.parseInt(index));
        String vxlanA = model.getVxlanA();
        String vxlanB = model.getVxlanB();
        deleteConfigVxlan(vxlanA,vxlanB);
        dao.deleteServiceInfo(Integer.parseInt(index));
    }
    @Override
    public void addSingleModel(vxlanAddModel model) {
        // TODO Auto-generated method stub
        VxlanServiceModel Amodel = new VxlanServiceModel();
        Amodel.setSource(model.getSource());
        Amodel.setDestination(model.getDestination());
        Amodel.setVlan(model.getVlan());
        Amodel.setVxlanA("12345");
        Amodel.setVxlanB("4568");
        dao.insertServiceInfo(Amodel);
    }
    //vxlan的控制器配置
    public static void configVxlan() {
        
    }
    //删除vxlan的控制器配置
    public static void deleteConfigVxlan(String vxlanA,String vxlanB) {
        
    }
    @Override
    public VxlanServiceModel findModel(String source, String vlan) {
        // TODO Auto-generated method stub
        VxlanServiceModel model = dao.find(source, vlan);
        return model;
    }
}
