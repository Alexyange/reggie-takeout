package com.itheima.reggie.utils;


import net.sf.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Alex
 * @version 1.0.0
 * @ClassName MapTransfer.java
 * @Description TODO
 * @createTime 2022年02月18日 14:59:00
 */
public class MapTransfer {
    public static String AK = "X0x9g2GKXRh9tYyaQgjIU6UyO6GTQPWi"; // 百度地图密钥

    // 调用百度地图API根据地址，获取坐标
    public static Map getCoordinate(String address) {
        Map map = new HashMap<>();
        if (address != null && !"".equals(address)) {
            address = address.replaceAll("\\s*", "").replace("#", "栋");
            String url = "http://api.map.baidu.com/geocoding/v3/?address=" + address + "&output=json&ak=" + AK;
            String json = loadJSON(url);
            if (json != null && !"".equals(json)) {
                JSONObject obj = JSONObject.fromObject(json);
                if ("0".equals(obj.getString("status"))) {
                    double lng = obj.getJSONObject("result").getJSONObject("location").getDouble("lng"); // 经度
                    double lat = obj.getJSONObject("result").getJSONObject("location").getDouble("lat"); // 纬度
                    DecimalFormat df = new DecimalFormat("#.######");
                    map.put("lng", df.format(lng));
                    map.put("lat", df.format(lat));
                    //return df.format(lng) + "," + df.format(lat);
                    return map;
                }
            }
        }
        return null;
    }

    public static String loadJSON(String url) {
        StringBuilder json = new StringBuilder();
        try {
            URL oracle = new URL(url);
            URLConnection yc = oracle.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream(), "UTF-8"));
            String inputLine = null;
            while ((inputLine = in.readLine()) != null) {
                json.append(inputLine);
            }
            in.close();
        } catch (MalformedURLException e) {
        } catch (IOException e) {
        }
        return json.toString();
    }

    public static void main(String[] args) {
        String dom = "安徽省合肥市瑶海区闽商国贸";
        Map coordinate = getCoordinate(dom);
        System.out.println("'" + dom + "'的经纬度为：" + coordinate);
    }

}
