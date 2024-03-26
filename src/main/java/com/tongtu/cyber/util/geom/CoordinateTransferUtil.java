package com.tongtu.cyber.util.geom;

import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CoordinateTransferUtil {
    public static double pi = 3.1415926535897932384626;
    public static double x_pi = 3.14159265358979324 * 3000.0 / 180.0;
    public static double a = 6378245.0;
    public static double ee = 0.00669342162296594323;

    public static DecimalFormat df = new DecimalFormat("0.0000000");

    /**
     * 将 WGS84 坐标系中的经纬度转换为 Web Mercator 坐标系中的经纬度
     * Web Mercator 坐标系常用于地图和地理信息系统中，以将地球的球面坐标转换为平面坐标，方便在屏幕上显示和处理地图数据
     * * 将经度值从度转换为弧度,弧度在数据计算更常用
     *
     * @param lon
     * @param lat
     * @return
     */
    public static double[] WGS84_To_WebMercator(double lon, double lat) {
        double x = wgs84LonToWebMercator(lon);
        double y = wgs84LatToWebMercator(lat);
        return new double[]{x, y};
    }

    /**
     * 将 WGS84 坐标系中的经纬度转换为 Web Mercator 坐标系中的经纬度
     *
     * @param wkt84 "POINT (120.123 40.456)"
     * @return
     */
    public static String WGS84_To_WebMercator(String wkt84) {
        Pattern pattern = Pattern.compile("([-\\+]?\\d+(\\.\\d+)?) ([-\\+]?\\d+(\\.\\d+)?)");
        String wktCopy = wkt84;

        String temp;
        double[] wgs900913XYArr;
        for (Matcher matcher = pattern.matcher(wkt84); matcher.find(); wktCopy = wktCopy.replaceFirst(temp, df.format(wgs900913XYArr[0]) + " " + df.format(wgs900913XYArr[1]))) {
            temp = wkt84.substring(matcher.start(), matcher.end());
            String[] xyArrTemp = temp.split(" ");
            double x_double = Double.parseDouble(xyArrTemp[0]);
            double y_double = Double.parseDouble(xyArrTemp[1]);
            wgs900913XYArr = WGS84_To_WebMercator(x_double, y_double);
        }

        return wktCopy;
    }

    /**
     * GPS84 全球定位系统的经纬度（lat和lon）转换为中国国家大地坐标系（GCJ02）中的经纬度
     * WGS84和GPS84是两种不同的坐标系，GPS84是WGS84的一个子集
     *
     * @param lat
     * @param lon
     * @return
     */
    public static double[] GPS84_To_GCJ02(double lat, double lon) {
        if (outOfChina(lat, lon)) {
            return new double[]{lat, lon};
        }
        double dLat = transformLat(lon - 105.0, lat - 35.0);
        double dLon = transformLon(lon - 105.0, lat - 35.0);
        double radLat = lat / 180.0 * pi;
        double magic = Math.sin(radLat);
        magic = 1 - ee * magic * magic;
        double sqrtMagic = Math.sqrt(magic);
        dLat = (dLat * 180.0) / ((a * (1 - ee)) / (magic * sqrtMagic) * pi);
        dLon = (dLon * 180.0) / (a / sqrtMagic * Math.cos(radLat) * pi);
        double mgLat = lat + dLat;
        double mgLon = lon + dLon;
        return new double[]{mgLat, mgLon};
    }

    /**
     * GPS84 全球定位系统的经纬度（lat和lon）转换为中国国家大地坐标系（GCJ02）中的经纬度
     * WGS84和GPS84是两种不同的坐标系，GPS84是WGS84的一个子集
     *
     * @param wkt900913
     * @return
     */
    public static String GPS84_To_GCJ02(String wkt900913) {
        Pattern pattern = Pattern.compile("([-\\+]?\\d+(\\.\\d+)?) ([-\\+]?\\d+(\\.\\d+)?)");
        String wktCopy = wkt900913;

        String temp;
        double[] wgs84XYArr;
        for (Matcher matcher = pattern.matcher(wkt900913); matcher.find(); wktCopy = wktCopy.replaceAll(temp, wgs84XYArr[0] + " " + wgs84XYArr[1])) {
            temp = wkt900913.substring(matcher.start(), matcher.end());
            String[] xyArrTemp = temp.split(" ");
            double x_double = Double.parseDouble(xyArrTemp[0]);
            double y_double = Double.parseDouble(xyArrTemp[1]);
            double[] bak = GPS84_To_GCJ02(y_double, x_double);
            wgs84XYArr = new double[]{bak[1], bak[0]};
        }

        return wktCopy;
    }

    /**
     * 将火星坐标系（GCJ-02）的经纬度转换为 GPS84 全球定位系统的经纬度
     * WGS84和GPS84是两种不同的坐标系，GPS84是WGS84的一个子集
     *
     * @param lon
     * @param lat
     * @return
     */
    public static double[] GCJ02_To_GPS84(double lon, double lat) {
        double[] gps = transform(lat, lon);
        double lontitude = lon * 2 - gps[1];
        double latitude = lat * 2 - gps[0];
        return new double[]{lontitude, latitude};
    }

    /**
     * 将火星坐标系（GCJ-02）的经纬度转换为 GPS84 全球定位系统的经纬度
     *
     * @param wkt900913
     * @return
     */
    public static String GCJ02_To_GPS84(String wkt900913) {
        Pattern pattern = Pattern.compile("([-\\+]?\\d+(\\.\\d+)?) ([-\\+]?\\d+(\\.\\d+)?)");
        String wktCopy = wkt900913;

        String temp;
        double[] wgs84XYArr;
        for (Matcher matcher = pattern.matcher(wkt900913); matcher.find(); wktCopy = wktCopy.replaceAll(temp, wgs84XYArr[0] + " " + wgs84XYArr[1])) {
            temp = wkt900913.substring(matcher.start(), matcher.end());
            String[] xyArrTemp = temp.split(" ");
            double x_double = Double.parseDouble(xyArrTemp[0]);
            double y_double = Double.parseDouble(xyArrTemp[1]);
            wgs84XYArr = GCJ02_To_GPS84(x_double, y_double);
        }

        return wktCopy;
    }

    /**
     * 将火星坐标系（GCJ-02）的经纬度转换为百度坐标系 (BD-09)经纬度
     *
     * @param lat
     * @param lon
     */
    public static double[] GCJ02_To_BD09(double lat, double lon) {
        double x = lon, y = lat;
        double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * x_pi);
        double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * x_pi);
        double tempLon = z * Math.cos(theta) + 0.0065;
        double tempLat = z * Math.sin(theta) + 0.006;
        return new double[]{tempLat, tempLon};
    }

    /**
     * 将百度坐标系 (BD-09)经纬度转换为星坐标系（GCJ-02）的经纬度
     *
     * @param lat
     * @param lon
     * @return
     */
    public static double[] BD09_To_GCJ02(double lat, double lon) {
        double x = lon - 0.0065, y = lat - 0.006;
        double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * x_pi);
        double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * x_pi);
        double tempLon = z * Math.cos(theta);
        double tempLat = z * Math.sin(theta);
        double[] gps = {tempLat, tempLon};
        return gps;
    }

    /**
     * 将GPS84全球定位系统的经纬度 转换为 百度坐标系 (BD-09)经纬度
     *
     * @param lat
     * @param lon
     * @return
     */
    public static double[] GPS84_To_BD09(double lat, double lon) {
        double[] gcj02 = GPS84_To_GCJ02(lat, lon);
        double[] bd09 = GCJ02_To_BD09(gcj02[0], gcj02[1]);
        return bd09;
    }

    /**
     * 将百度坐标系 (BD-09)经纬度 转换为 GPS84全球定位系统的经纬度
     *
     * @param lat
     * @param lon
     * @return
     */
    public static double[] BD09_To_GPS84(double lat, double lon) {
        double[] gcj02 = BD09_To_GCJ02(lat, lon);
        double[] gps84 = GCJ02_To_GPS84(gcj02[0], gcj02[1]);
        gps84[0] = retain7(gps84[0]);
        gps84[1] = retain7(gps84[1]);
        return gps84;
    }
    /**
     * 将 WGS84 坐标系中的经度值转换为 Web Mercator 坐标系中的经度值
     *
     * @param lon
     * @return
     */
    public static double wgs84LonToWebMercator(double lon) {
        return lon * 6378137.0D * 3.141592653589793D / 180.0D;
    }

    /**
     * Web Mercator 坐标系常用于地图和地理信息系统中，以将地球的球面坐标转换为平面坐标，方便在屏幕上显示和处理地图数据
     *
     * @param lat
     * @return
     */
    public static double wgs84LatToWebMercator(double lat) {
        double y = Math.log(Math.tan((90.0D + lat) * 3.141592653589793D / 360.0D)) / 0.017453292519943295D;
        return y * 6378137.0D * 3.141592653589793D / 180.0D;
    }


    /**
     * 判断是否在中国境外
     *
     * @param lat
     * @param lon
     * @return
     */
    public static boolean outOfChina(double lat, double lon) {
        if (lon < 72.004 || lon > 137.8347)
            return true;
        if (lat < 0.8293 || lat > 55.8271)
            return true;
        return false;
    }
    /**
     * 坐标系 保留小数点后六位
     *
     * @param num
     * @return
     */
    private static double retain7(double num) {
        String result = String.format("%.7f", num);
        return Double.valueOf(result);
    }

    private static double transformLat(double x, double y) {
        double ret = -100.0 + 2.0 * x + 3.0 * y + 0.2 * y * y + 0.1 * x * y
                + 0.2 * Math.sqrt(Math.abs(x));
        ret += (20.0 * Math.sin(6.0 * x * pi) + 20.0 * Math.sin(2.0 * x * pi)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(y * pi) + 40.0 * Math.sin(y / 3.0 * pi)) * 2.0 / 3.0;
        ret += (160.0 * Math.sin(y / 12.0 * pi) + 320 * Math.sin(y * pi / 30.0)) * 2.0 / 3.0;
        return ret;
    }

    private static double transformLon(double x, double y) {
        double ret = 300.0 + x + 2.0 * y + 0.1 * x * x + 0.1 * x * y + 0.1
                * Math.sqrt(Math.abs(x));
        ret += (20.0 * Math.sin(6.0 * x * pi) + 20.0 * Math.sin(2.0 * x * pi)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(x * pi) + 40.0 * Math.sin(x / 3.0 * pi)) * 2.0 / 3.0;
        ret += (150.0 * Math.sin(x / 12.0 * pi) + 300.0 * Math.sin(x / 30.0
                * pi)) * 2.0 / 3.0;
        return ret;
    }

    private static double[] transform(double lat, double lon) {
        if (outOfChina(lat, lon)) {
            return new double[]{lat, lon};
        }
        double dLat = transformLat(lon - 105.0, lat - 35.0);
        double dLon = transformLon(lon - 105.0, lat - 35.0);
        double radLat = lat / 180.0 * pi;
        double magic = Math.sin(radLat);
        magic = 1 - ee * magic * magic;
        double sqrtMagic = Math.sqrt(magic);
        dLat = (dLat * 180.0) / ((a * (1 - ee)) / (magic * sqrtMagic) * pi);
        dLon = (dLon * 180.0) / (a / sqrtMagic * Math.cos(radLat) * pi);
        double mgLat = lat + dLat;
        double mgLon = lon + dLon;
        return new double[]{mgLat, mgLon};
    }

    public static void main(String[] args) {
        System.out.println(GPS84_To_GCJ02("POLYGON((112.1607863 32.0335578,112.1537383 32.0397994,112.1540265 32.0400714,112.1612053 32.0339063,112.1607863 32.0335578))"));
    }


}
