package com.tongtu.cyber.util.upload;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.ZipUtil;
import com.tongtu.cyber.util.characters.StrRegFilterUtil;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;

/**
 * 文件上传工具类
 *
 * @author : 陈世恩
 * @date : 2024/3/20 14:21
 */
@Component
public class UpLoadUtil {
    /**
     * 下载文件
     *
     * @param filePath 文件路径
     * @param response
     */
    public  static  void downLoad(HttpServletResponse response, String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new RuntimeException("文件路径不存在!!!");
        }
        InputStream inputStream;
        try {
            inputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        downLoad(response, file.getName(), inputStream);
    }

    /**
     * 下载文件
     *
     * @param filePath 文件路径
     * @param response
     */
    public void downLoad(HttpServletResponse response, String filePath, String basePath) {
        String fileName = StrRegFilterUtil.getFileNameByPath(filePath);
        if (StrUtil.isEmpty(fileName)) {
            throw new RuntimeException("文件名称为空!!!");
        }
        InputStream inputStream = null;
        File outFile = null;
        String zipStrPath = "";
        try {
            if (filePath.endsWith(".zip")) {
                int dotIndex = fileName.lastIndexOf('.');
                if (dotIndex != -1) {
                    zipStrPath = basePath + File.separator + fileName.substring(0, dotIndex);
                    outFile = ZipUtil.zip(zipStrPath, filePath);
                    inputStream = new FileInputStream(outFile);
                }
            } else {
                if (filePath.indexOf("template") != -1) {
                    //绝对路径(模板)
                    inputStream = this.getClass().getClassLoader().getResourceAsStream(filePath);
                } else {
                    //相对路径
                    inputStream = new FileInputStream(new File(filePath));
                }
            }
            downLoad(response, fileName, inputStream);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            // 删除临时文件夹和临时压缩文件
            FileUtil.del(outFile);
            if (filePath.endsWith(".zip")) {
                FileUtil.del(zipStrPath);
            }
        }
    }

    /**
     * 下载文件
     *
     * @param response
     * @param fileName
     * @param inputStream
     */
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
     *
     * @param fileName
     * @param response
     * @throws UnsupportedEncodingException
     */
    private static void setResponse(String fileName, HttpServletResponse response) throws UnsupportedEncodingException {
        fileName = StrUtil.isEmpty(fileName) ? RandomUtil.randomNumbers(8) : fileName;
        response.reset();
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
        response.setContentType("application/vnd.ms-excel;charset=UTF-8");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
    }

}
