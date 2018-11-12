package com.xigua.constant;

import java.util.Random;

import com.xigua.util.HttpRequestUtil;

public class ListTest {

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        String url = "http://192.168.70.129:6500" + "/restconf/config/network-topology:network-topology/"
                + "topology/topology-netconf/node/"+"17830"+"/yang-ext:mount/"
                + "ctc-vxlan:vxlan/vxlan-instance/"+"4246317310";
        HttpRequestUtil.Delete(url);
    }

}
