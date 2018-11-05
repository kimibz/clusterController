package com.xigua.constant;

import java.util.ArrayList;
import java.util.List;

public class ListTest {

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        List<Integer> list = new ArrayList<Integer>();
        for (int i =0; i<11 ;i++) {
            list.add(i);
        }
        System.out.println(list);
        int page = 0;
        int number =2;
        int totalRows = list.size();
        if ((totalRows % number) == 0) {  
            page = totalRows / number;  
          } else {  
              page = totalRows / number + 1;  
          } 
        System.out.println(page);
        List<ListModel> result = new ArrayList<ListModel>();
        for(int i=0,len=page-1;i<=len;i++) {
            int fromIndex = i*number;
            int toIndex = ((i == len) ? totalRows : ((i + 1) * number));
            ListModel model = new ListModel();
            model.setCurrency(i+1);
            model.setList(list.subList(fromIndex, toIndex));
            result.add(model);
        }
        System.out.println(result);
    }

}
