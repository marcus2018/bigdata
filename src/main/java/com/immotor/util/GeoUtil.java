package com.immotor.util;

import net.sf.json.JSONObject;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GeoUtil {
    public static final String KEY = "a500d687a0bee4d45847fa512f7d6790";
    private static final String OUTPUT = "JSON";
    public static final String GET_LNG_LAT_URL = "https://restapi.amap.com/v3/geocode/geo";
    public static final String GET_ADDR_FROM_LNG_LAT = "https://restapi.amap.com/v3/geocode/regeo";
    private static final String EXTENSIONS_ALL = "all";
    //31.543297 120.417532 江苏省无锡市新吴区满天星文化艺术培训中心(梅村基地)梅村二胡文化园
    public static void main(String[] args) throws MalformedURLException {
        System.out.println( getAddrFromLngLat("31.542197","120.430834"));
        System.out.println("============");
        System.out.println(getAddrInfo(120.430834,31.542197));
    }
    /**
     *
     * @description 根据经纬度查地址
     * @param lng：经度，lat：纬度
     * @return 地址
     * @author jxp
     * @date 2017年7月12日
     */
    public static String getAddrFromLngLat(String lng, String lat) throws MalformedURLException {

        BufferedReader in = null;
        URL tirc = new URL(""+GET_ADDR_FROM_LNG_LAT+"?location=" + lat + "," + lng + "&key="+KEY+"");
        //System.out.println("url="+tirc);
        try {
          //  System.out.println("sb0");
            System.out.println(tirc.openStream());
            System.out.println(new InputStreamReader(tirc.openStream(), "UTF-8"));
            in = new BufferedReader(new InputStreamReader(tirc.openStream(), "UTF-8"));
          //  System.out.println("sb00");

            String res;
            StringBuilder sb = new StringBuilder("");
            System.out.println("sb1");
            while ((res = in.readLine()) != null) {
              //  System.out.println(res.trim());
                sb.append(res.trim());
            }
          //  System.out.println("sb2");
            String result = sb.toString();
          //  System.out.println("result="+result);
            // String result = HttpclientUtil.post(params, GET_ADDR_FROM_LNG_LAT);
            JSONObject json = JSONObject.fromObject(result);
            System.out.println(json);
            String status = json.getString("status");
            String address = null;
            if ("1".equals(status)) {
                JSONObject regeocode = JSONObject.fromObject(json.get("regeocode"));
                address = regeocode.getString("formatted_address");
            }
            return address;

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public  static Map<String,String> getAddrInfo(double lat,double lng){
        Map<String,String> map=new HashMap<>();
        List<NameValuePair> list=new ArrayList<>();
        list.add(new BasicNameValuePair("key", GeoUtil.KEY));
        list.add(new BasicNameValuePair("location",lat+","+lng));

        String result= HttpClientUtil.get("utf-8",GeoUtil.GET_ADDR_FROM_LNG_LAT,list);
        JSONObject json = JSONObject.fromObject(result);
        String status = json.getString("status");
        String address = null;
        String district=null;
        if ("1".equals(status)) {
            JSONObject regeocode = JSONObject.fromObject(json.get("regeocode"));
            address = regeocode.getString("formatted_address");
            JSONObject addressComponent = JSONObject.fromObject(regeocode.get("addressComponent"));
            district = addressComponent.getString("district");
            map.put("address",address);
            map.put("district",district);
            map.put("cityCode",addressComponent.getString("adcode").substring(0,4)+"00");
            map.put("province",addressComponent.getString("province"));
        }
        return map;
    }
}
