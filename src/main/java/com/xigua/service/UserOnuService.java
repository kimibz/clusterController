package com.xigua.service;

import java.util.List;

import com.xigua.model.OnuSpawnModel;
import com.xigua.model.serviceVlanModel;
import com.xigua.model.Topo.Onu;

public interface UserOnuService {
    
    //添加onu
    void addOnu(String vndName , OnuSpawnModel OnuModel);
    //查看端口下ONU
    List<Onu> getOnu(String vndName,String interfaceName);
    //删除端口下ONU
    void deleteOnu(String vndName, String interfaceName);
    //新建cvlan/svlan
    void setServiceVlan(String vndName,String interfaceName,serviceVlanModel model);
    //配置cvlan/svlan
    void editServiceVlan(String vndName,String interfaceName,serviceVlanModel model);
    //获取用户vlan和网络vlan
    serviceVlanModel getVlan(String vndName,String interfaceName);
    //刷新ONU
    List<Onu> refreshOnu(String vndName,String ifIndex);
}
