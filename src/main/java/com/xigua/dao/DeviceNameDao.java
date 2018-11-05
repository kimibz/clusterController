package com.xigua.dao;

import org.apache.ibatis.annotations.Param;

import com.xigua.model.changeDeviceName;

public interface DeviceNameDao {
   //获取设备名字
    String getDeviceName(String nodeId);
    //修改设备名字
    void changeName(@Param("nodeId")String nodeId,@Param("deviceName")String deviceName);
    //删除设备名字
    void deleteDeviceById(String nodeId);
    //增加设备
    void addDeviceById(@Param("nodeId")String nodeId,@Param("deviceName")String deviceName);
}
