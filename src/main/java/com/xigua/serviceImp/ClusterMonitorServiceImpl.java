package com.xigua.serviceImp;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xigua.JSONTemplate.ControllerSettings;
import com.xigua.model.ClusterFollower;
import com.xigua.model.ClusterInfo;
import com.xigua.model.MonitorCluster;
import com.xigua.service.ClusterMonitorService;
import com.xigua.util.HttpRequestUtil;
import com.xigua.util.Util;
@Service
public class ClusterMonitorServiceImpl implements ClusterMonitorService{
    private String Ipaddress = ControllerSettings.ip;
    /*
     * 随机获取集群中单个控制器的信息--->表明负载分担
     * @see com.xigua.service.ClusterMonitorService#getSingleInfo()
     */
    @Override
    public MonitorCluster getSingleInfo() {
        // TODO Auto-generated method stub
        String url = Ipaddress+"/jolokia/read/org.opendaylight.controller:"
                + "type=DistributedConfigDatastore,Category=ShardManager,name=shard-manager-config";
        String result = HttpRequestUtil.Get(url);
        JSONObject object = JSON.parseObject(result);
        MonitorCluster cluster = new MonitorCluster();
        if(object.getIntValue("status")==200) {
            cluster.setStatus("正常");
        }else {
            cluster.setStatus("宕机");
        }
        cluster.setTime(Util.TimestampToString(object.getString("timestamp")));
        cluster.setMemberName(object.getJSONObject("value").getString("MemberName"));
        return cluster;
    }
    @Override
    public List<String> getAllNameList() {
        // TODO Auto-generated method stub
        String url = Ipaddress+"/jolokia/read/org.opendaylight.controller:"
                + "type=DistributedConfigDatastore,Category=ShardManager,name=shard-manager-config";
        List<String> memberList = new ArrayList<String>();
        for(int i=0;i<100;i++) {
            String result = HttpRequestUtil.Get(url);
            JSONObject object = JSON.parseObject(result);
            String memberName = object.getJSONObject("value").getString("MemberName");
            if(memberList.contains(memberName)) {
                break;
            }else {
                memberList.add(memberName);
            }
        }
        return memberList;
    }
    @Override
    public ClusterInfo getInfo(String member_name) {
        // TODO Auto-generated method stub
        String url = Ipaddress + "/jolokia/read/org.opendaylight.controller:Category=Shards,"
                + "name="+member_name+"-shard-default-operational,type=DistributedOperationalDatastore";
        ClusterInfo info = new ClusterInfo();
        while(true) {
            JSON.parseObject(HttpRequestUtil.Get(url)).getString("status").equals("200");
            String result = HttpRequestUtil.Get(url);
            JSONObject object = JSON.parseObject(result);
            if(object.getString("status").equals("200")) {
                info.setRaftState(object.getJSONObject("value").getString("RaftState"));
                info.setShardName(Util.cutShardName(object.getJSONObject("value").getString("ShardName")));
                info.setLastLeadershipChangeTime(object.getJSONObject("value").getString("LastLeadershipChangeTime"));
                info.setLeader(Util.cutShardName(object.getJSONObject("value").getString("Leader")));
                info.setStatRetrievalTime(object.getJSONObject("value").getString("StatRetrievalTime"));
                info.setTime(Util.TimestampToString(object.getString("timestamp")));
                //如果RaftState为Leader
                if(info.getRaftState().equals("Leader")) {
                    JSONArray arr = object.getJSONObject("value").getJSONArray("FollowerInfo");
                    List <String> list = new ArrayList<String>();
                    for(int i=0; i<arr.size() ; i++) {
                        JSONObject obj = arr.getJSONObject(i);
                       // ClusterFollower follower = new ClusterFollower();
                       // follower.setActive(obj.getString("active"));
                        //follower.setId(Util.cutShardName(obj.getString("id")));
                        list.add(Util.cutShardName(obj.getString("id")));
                    }
                    info.setFollowerInfo(list);
                }else {
                    info.setFollowerInfo(null);
                }
                break;
            }
        }
        return info;
    }

}
