package com.xigua.constant;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.xigua.model.vlanId;

public class TestHttpMethod {

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        vlanId id = new vlanId();
        id.setVlanId("123");
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_DASHES);
        Gson gson = gsonBuilder.create();
        String usersJson = gson.toJson(id);
        System.out.println(usersJson);
    }
}
