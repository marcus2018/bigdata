package com.example.storm.benchmark.test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TestList {


    public static void main(String[] args) {
        List list1 =new ArrayList();
        list1.add("1111");
        list1.add("2222");
        list1.add("3333");

        List list2 =new ArrayList();
        list2.add("3333");
        list2.add("4444");
        list2.add("5555");

        //并集
        //list1.addAll(list2);
        //交集
        //list1.retainAll(list2);
        //差集
        //list1.removeAll(list2);
        //无重复并集
        list2.removeAll(list1);
        //list1.addAll(list2);
        String s54="12";
        System.out.println(s54+" "+(8-s54.length()));
        for(int m=0;m<8-s54.length();m++){
            s54=s54+"0";
            System.out.println(m+" "+s54);
        }
        System.out.println(s54);
        String str="05,06,B5,07,00,00,00,00,00,A9,07,C0,00,00,00,00,00,00,00,7B,07,C0,00,00,00,00,00,00,00,9E,07,00,00,00,00,00,00,00,00,92,07,C0,00,00,00,00,00,00,00,93,07,C0,00,00,00,00,00,00,00,00,00,00,00,00,00,00,00,00,00,CA,07,00,00,00,00,00,00,00,00,99,07,00,00,00,00,00,00,00,00,C6,07,00,00,00,00,00,00,00,00,CE,07,C0,00,00,00,00,00,00,00,7C,07,C0,00,00,00,00,00,00,00,52,07,C0,00,00,00,00,00,00,00,B2,07,00,00,00,00,00,00,00,00";
        System.out.println("length="+str.split(",").length);
        //System.out.println("-----------------------------------\n");
        //printStr(list1);

    }

    public static void printStr(List list1){
        String str="05,06,B5,07,00,00,00,00,00,A9,07,C0,00,00,00,00,00,00,00,7B,07,C0,00,00,00,00,00,00,00,9E,07,00,00,00,00,00,00,00,00,92,07,C0,00,00,00,00,00,00,00,93,07,C0,00,00,00,00,00,00,00,00,00,00,00,00,00,00,00,00,00,CA,07,00,00,00,00,00,00,00,00,99,07,00,00,00,00,00,00,00,00,C6,07,00,00,00,00,00,00,00,00,CE,07,C0,00,00,00,00,00,00,00,7C,07,C0,00,00,00,00,00,00,00,52,07,C0,00,00,00,00,00,00,00,B2,07,00,00,00,00,00,00,00,00";
        System.out.println("length="+str.split(",").length);
        for (int i = 0; i < list1.size(); i++) {
            System.out.println(list1.get(i));
        }
    }
}

