package com.xigua.constant;

public class LamdaTest {

    public static void main(String[] args) {
        // TODO Auto-generated method stub
//        List features = Arrays.asList("Lambdas", "Default Method", "Stream API", "Date and Time API");
//        features.forEach(n -> System.out.println(n));
        String str="member-3-shard-default-operational";
        String year=str.substring(0,str.indexOf("-shard"));
        System.out.println(year);
    }

}
