package com.xigua.model;

import java.util.List;

public class vxlanTunnel {
    private String tunnelDestinationIp;
    private String vxlanTunnelId;
    private String vxlanTunnelName;
    private String tunnelSourceIpPrefixLen;
    private String tunnelSourceIp;
    private List<vxlanId> bindVxlanId;
    public String getTunnelDestinationIp() {
        return tunnelDestinationIp;
    }
    public void setTunnelDestinationIp(String tunnelDestinationIp) {
        this.tunnelDestinationIp = tunnelDestinationIp;
    }
    public String getVxlanTunnelId() {
        return vxlanTunnelId;
    }
    public void setVxlanTunnelId(String vxlanTunnelId) {
        this.vxlanTunnelId = vxlanTunnelId;
    }
    public String getVxlanTunnelName() {
        return vxlanTunnelName;
    }
    public void setVxlanTunnelName(String vxlanTunnelName) {
        this.vxlanTunnelName = vxlanTunnelName;
    }
    public String getTunnelSourceIpPrefixLen() {
        return tunnelSourceIpPrefixLen;
    }
    public void setTunnelSourceIpPrefixLen(String tunnelSourceIpPrefixLen) {
        this.tunnelSourceIpPrefixLen = tunnelSourceIpPrefixLen;
    }
    public String getTunnelSourceIp() {
        return tunnelSourceIp;
    }
    public void setTunnelSourceIp(String tunnelSourceIp) {
        this.tunnelSourceIp = tunnelSourceIp;
    }
    public List<vxlanId> getBindVxlanId() {
        return bindVxlanId;
    }
    public void setBindVxlanId(List<vxlanId> bindVxlanId) {
        this.bindVxlanId = bindVxlanId;
    }
    
}
