package com.xigua.constant;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xigua.model.SysConfig;
import com.xigua.util.HttpRequestUtil;

public class TestHttpMethod {

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        Map<String,String> map = new HashMap<String,String>();
        map.put("A", "网关A");
        map.put("B", "网关B");
        map.put("C", "云");
        String result = map.get("B");
        System.out.println(result);
    }

}
