package com.immotor.util;

import java.util.UUID;


public class StringUtils {

    public static String UUID(){
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static boolean isEmpty(String str){
        return org.springframework.util.StringUtils.isEmpty(str);
    }

    public static boolean is6len(String str){ return str.length()==6; }

    public  static  boolean is8len(String str){ return str.length()==8;}

    public static final String trim(Object obj) {
        return obj == null ? "" : String.valueOf(obj).trim();
    }
}
