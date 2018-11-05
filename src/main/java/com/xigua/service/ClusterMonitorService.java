package com.xigua.service;

import java.util.List;

import com.xigua.model.ClusterInfo;
import com.xigua.model.MonitorCluster;

public interface ClusterMonitorService {
    //获取集群中单个控制器的信息
    MonitorCluster getSingleInfo();
    //获取集群中所有控制器的名字列表
    List<String> getAllNameList();
    //获取某个集群的状态信息
    ClusterInfo getInfo(String member_name);
}
