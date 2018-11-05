package com.xigua.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.xigua.model.VxlanServiceModel;

public interface vxlanDao {
    //获取一条业务信息
    VxlanServiceModel getServiceInfo(int index);
    //获取所有信息
    List<VxlanServiceModel> getAllInfo();
    //插入一条业务信息
    void insertServiceInfo(VxlanServiceModel model);
    //删除一条业务信息
    void deleteServiceInfo(int index);
    //通过vlan和源 搜索数据库是否有重复
    VxlanServiceModel find(@Param("source") String source,@Param("vlan") String vlan);
}
