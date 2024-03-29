package com.tongtu.cyber.util.characters;

import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * 字符串处理工具类
 *
 * @author : 陈世恩
 * @date : 2024/3/20 9:09
 */
public class StrRegFilterUtil {
    /**
     * 定义GB的计算常量
     */
    private static final int GB = 1024 * 1024 * 1024;
    /**
     * 定义MB的计算常量
     */
    private static final int MB = 1024 * 1024;
    /**
     * 定义KB的计算常量
     */
    private static final int KB = 1024;
    /**
     * 格式化小数
     */
    private static final DecimalFormat DF = new DecimalFormat("0.00");

    /**
     * 过滤掉特殊字符
     *
     * @param str
     * @return
     * @throws PatternSyntaxException
     */
    public static String filterSpecialStr(String str) throws PatternSyntaxException {
        String regEx = "[`_《》~!@#$%^&*()+=|{}':;',\\[\\].<>?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }

    /**
     * 过滤字符串多余0
     *
     * @param s
     * @return
     */
    public static String filterZero(String s) {
        if (s.indexOf(".") > 0) {
            s = s.replaceAll("0+?$", "");//去掉多余的0
            s = s.replaceAll("[.]$", "");//如最后一位是.则去掉
        }
        return s;
    }

    /**
     * 获取文件名(.前面)
     */
    public static String getFileName(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length() - 1))) {
                return filename.substring(dot + 1);
            }
        }
        return filename;
    }

    /**
     * 获取文件扩展名(.后面)
     */
    public static String getFileExtension(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length()))) {
                return filename.substring(0, dot);
            }
        }
        return filename;
    }
    /**
     * 格式化文件名称 去除特殊字符
     *
     * @param fileName
     * @return
     */
    public static String formatFileName(String fileName) {
        int unixSep = fileName.lastIndexOf(47);
        int winSep = fileName.lastIndexOf(92);
        int pos = winSep > unixSep ? winSep : unixSep;
        if (pos != -1) {
            fileName = fileName.substring(pos + 1);
        }
        fileName = fileName.replace("=", "").replace(",", "").replace("&", "").replace("#", "");
        fileName = fileName.replaceAll("\\s", "");
        return fileName;
    }
    /**
     * 文件大小转换
     */
    public static String convertFileSize(long size) {
        String resultSize;
        if (size / GB >= 1) {
            //如果当前Byte的值大于等于1GB
            resultSize = DF.format(size / (float) GB) + "GB   ";
        } else if (size / MB >= 1) {
            //如果当前Byte的值大于等于1MB
            resultSize = DF.format(size / (float) MB) + "MB   ";
        } else if (size / KB >= 1) {
            //如果当前Byte的值大于等于1KB
            resultSize = DF.format(size / (float) KB) + "KB   ";
        } else {
            resultSize = size + "B   ";
        }
        return resultSize;
    }
}
