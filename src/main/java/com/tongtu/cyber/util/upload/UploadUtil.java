package com.tongtu.cyber.util.upload;

import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.log4j.Log4j2;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;

/**
 * 文件上传工具类
 *
 * @author : 陈世恩
 * @date : 2024/3/20 14:21
 */
@Log4j2
public class UploadUtil {
    /**
     * 根据文件路径下载文件
     *
     * @param filePath 文件路径
     * @param response
     */
    public static void downLoad(HttpServletResponse response, String filePath) throws Exception {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new RuntimeException("文件路径不存在!!!");
        }
        downLoad(response, file.getName(), new FileInputStream(file));
    }

    public static void downLoad(HttpServletResponse response, String fileName, InputStream inputStream) {
        if (ObjUtil.isEmpty(inputStream)) {
            throw new RuntimeException("inputStream 为空!!!");
        }
        BufferedInputStream bufferedInputStream = null;
        BufferedOutputStream bufferedOutputStream = null;
        try {
            //设置响应头
            setResponse(fileName, response);
            bufferedInputStream = new BufferedInputStream(inputStream);
            OutputStream outputStream = response.getOutputStream();
            bufferedOutputStream = new BufferedOutputStream(outputStream);
            int n;
            //输出流
            while ((n = bufferedInputStream.read()) != -1) {
                bufferedOutputStream.write(n);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                //关闭流
                bufferedOutputStream.close();
                bufferedInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 设置响应头
     * @param fileName
     * @param response
     * @throws UnsupportedEncodingException
     */
    public static void setResponse(String fileName, HttpServletResponse response) throws UnsupportedEncodingException {
        fileName = StrUtil.isEmpty(fileName) ? RandomUtil.randomNumbers(8) : fileName;
        response.reset();
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
        response.setContentType("application/vnd.ms-excel;charset=UTF-8");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
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
}
