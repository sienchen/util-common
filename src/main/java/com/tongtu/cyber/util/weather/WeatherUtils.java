package com.tongtu.cyber.util.weather;


import com.alibaba.fastjson.JSONObject;
import com.tongtu.cyber.common.exception.JeecgBootException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * 天气工具类
 *
 * @author : 陈世恩
 * @date : 2024/3/19 11:06
 */
public class WeatherUtils {
    //个人key
    private static final String API_KEY = "XVQGyXF6XZOPh3aSwKWmeY86r8TGbllZ";
    //天气地址
    private static final String WEATHER_URL = "https://api.map.baidu.com/weather/v1/?district_id=%s&data_type=all&ak=%s";
    //地图位置地址
    private static final String GEOCODING_URL = "https://api.map.baidu.com/geocoding/v3/?output=json&address=%s&coordtype=wgs84ll&ak=%s";
    //获取行政区信息地址
    private static final String REVERSE_GEOCODING_URL = "https://api.map.baidu.com/reverse_geocoding/v3/?output=json&location=%s,%s&coordtype=wgs84ll&ak=%s";

    /**
     * 根据地名获取对应区域天气
     *
     * @param addr
     * @return
     * @throws Exception
     */
    public static String getWeather(String addr) throws Exception {
        String[] coordinate = getReverseCoordinate(addr);
        if (coordinate == null) {
            throw new JeecgBootException("地名信息错误!!!");
        }
        // 构建请求URL
        String url = String.format(WEATHER_URL, coordinate[3], API_KEY);
        return getHttpInfo(url);
    }

    /**
     * 根据地名获取行政区信息
     *
     * @param addr 地名
     * @return
     * @throws Exception
     */
    public static String[] getReverseCoordinate(String addr) throws Exception {
        String[] coordinate = getCoordinate(addr);
        if (coordinate == null) {
            return null;
        }
        String url = String.format(REVERSE_GEOCODING_URL, coordinate[1], coordinate[0], API_KEY);
        String strInfo = getHttpInfo(url);
        JSONObject jsonInfo = JSONObject.parseObject(strInfo);
        //省
        String province = jsonInfo.getJSONObject("result").getJSONObject("addressComponent").getString("province");
        //市
        String city = jsonInfo.getJSONObject("result").getJSONObject("addressComponent").getString("city");
        //区县
        String qx = jsonInfo.getJSONObject("result").getJSONObject("addressComponent").getString("district");
        //行政区代码
        String code = jsonInfo.getJSONObject("result").getJSONObject("addressComponent").getString("adcode");
        return new String[]{province, city, qx, code};
    }

    /**
     * 根据地点获取经纬度
     *
     * @param addr 地名
     * @return
     * @throws Exception
     */
    public static String[] getCoordinate(String addr) throws Exception {
        String url = String.format(GEOCODING_URL, addr, API_KEY);
        String strInfo = getHttpInfo(url);
        JSONObject jsonInfo = JSONObject.parseObject(strInfo);
        String status = jsonInfo.get("status").toString();
        if (!"0".equals(status)) {
            return null;
        }
        String lng = jsonInfo.getJSONObject("result").getJSONObject("location").getString("lng");
        String lat = jsonInfo.getJSONObject("result").getJSONObject("location").getString("lat");
        return new String[]{lng, lat};
    }

    private static String getHttpInfo(String urlString) throws Exception {
        //构建请求
        URL url = new URL(urlString);
        // 发起请求
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept-Charset", StandardCharsets.UTF_8.name());
        // 读取响应
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        // 断开连接
        connection.disconnect();
        // 返回信息
        return response.toString();
    }

    public static void main(String[] args) {
        try {
            String city = "湖南"; // 要查询的城市名
            String[] coordinate = getCoordinate(city);
            System.out.println(coordinate);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
