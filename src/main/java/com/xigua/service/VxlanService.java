package com.xigua.service;

import java.util.List;

import com.xigua.model.VxlanServiceModel;
import com.xigua.model.VxlanServicePage;
import com.xigua.model.vxlanAddModel;

public interface VxlanService {
    //获取所有的业务信息
    List<VxlanServicePage> getAll();
    //获取单个业务信息
    VxlanServiceModel getSingleModel(String index);
    //删除单个业务
    void deleteSingleModel(String index);
    //添加一条业务
    void addSingleModel(vxlanAddModel model);
    //查找单个业务信息
    VxlanServiceModel findModel(String source,String vlan);
}
