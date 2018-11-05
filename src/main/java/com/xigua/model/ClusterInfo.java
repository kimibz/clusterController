package com.xigua.model;

import java.util.List;

public class ClusterInfo {
    private String ShardName;
    private String RaftState;
    private String LastLeadershipChangeTime;
    private String Leader;
    private String StatRetrievalTime;
    private List<String> FollowerInfo;
    private String time;
    public String getShardName() {
        return ShardName;
    }
    public void setShardName(String shardName) {
        ShardName = shardName;
    }
    public String getRaftState() {
        return RaftState;
    }
    public void setRaftState(String raftState) {
        RaftState = raftState;
    }
    public String getLastLeadershipChangeTime() {
        return LastLeadershipChangeTime;
    }
    public void setLastLeadershipChangeTime(String lastLeadershipChangeTime) {
        LastLeadershipChangeTime = lastLeadershipChangeTime;
    }
    public String getLeader() {
        return Leader;
    }
    public void setLeader(String leader) {
        Leader = leader;
    }
    public String getStatRetrievalTime() {
        return StatRetrievalTime;
    }
    public void setStatRetrievalTime(String statRetrievalTime) {
        StatRetrievalTime = statRetrievalTime;
    }
    public List<String> getFollowerInfo() {
        return FollowerInfo;
    }
    public void setFollowerInfo(List<String> followerInfo) {
        FollowerInfo = followerInfo;
    }
    public String getTime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }
}
