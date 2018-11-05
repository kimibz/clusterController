package com.xigua.constant;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class TestDate {
	public static void main(String[] args) throws ParseException {
		// TODO Auto-generated method stub
	    Long t = Long.parseLong("1540452984");
        Timestamp ts = new Timestamp(1540453766);
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //方法一:优势在于可以灵活的设置字符串的形式。
        String tsStr = sdf.format(ts);// 2017-01-15 21:17:04
        System.out.println(tsStr);
	}

}
