package com.xigua.model;

public class VxlanServiceModel {
    private int id;
    private String source;
    private String vlan;
    private String vxlanA;
    private String vxlanB;
    private String destination;
    public int getIndex() {
        return id;
    }
    public void setIndex(int id) {
        this.id = id;
    }
    public String getSource() {
        return source;
    }
    public void setSource(String source) {
        this.source = source;
    }
    public String getVlan() {
        return vlan;
    }
    public void setVlan(String vlan) {
        this.vlan = vlan;
    }
    public String getVxlanA() {
        return vxlanA;
    }
    public void setVxlanA(String vxlanA) {
        this.vxlanA = vxlanA;
    }
    public String getVxlanB() {
        return vxlanB;
    }
    public void setVxlanB(String vxlanB) {
        this.vxlanB = vxlanB;
    }
    public String getDestination() {
        return destination;
    }
    public void setDestination(String destination) {
        this.destination = destination;
    }
    
}
