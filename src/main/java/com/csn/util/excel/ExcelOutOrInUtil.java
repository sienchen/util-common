
package com.csn.util.excel;

import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.ZipUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tongtu.cyber.common.api.vo.Result;
import com.tongtu.cyber.common.exception.JeecgBootException;
import com.tongtu.cyber.common.util.CommonUtils;
import com.tongtu.cyber.modules.system.entity.SysFile;
import com.tongtu.cyber.modules.system.service.ISysFileService;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.io.IOUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.jeecgframework.poi.excel.ExcelExportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.enmus.ExcelType;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * excel导入导出工具类
 *
 * @author : 陈世恩
 * @date : 2023/5/12 15:18
 */
@Component
public class ExcelOutOrInUtil {
    //临时文件夹
    @Value("${basic.tempPath:/data/tongtu}")
    private String baseDir;
    @Value("${jeecg.uploadType}")
    private String uploadType;
    @Autowired
    private ISysFileService sysFileService;

    /**
     * 【autoPoi方式】单excel导出(实体类方式)
     *
     * @param response
     * @param dataList  数据集合
     * @param dataClass 数据类
     * @param filename  文件名称
     * @return
     */
    public ModelAndView exportExcel(HttpServletResponse response, Integer type, String filename, List dataList, Class<?> dataClass) {
        response.setContentType("application/vnd.ms-excel;charset=UTF-8");
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        ExportParams exportParams = new ExportParams();
        exportParams.setStyle(AutoExcelStyleHandler.class);
        if (type == 1) {
            exportParams.setSheetName("数据");
        } else {
            exportParams.setSheetName("模板");
        }
        mv.addObject(NormalExcelConstants.PARAMS, exportParams);
        mv.addObject(NormalExcelConstants.FILE_NAME, filename);
        mv.addObject(NormalExcelConstants.CLASS, dataClass);
        mv.addObject(NormalExcelConstants.DATA_LIST, dataList);
        return mv;

    }

    /**
     * 【autoPoi方式】多excel导出(实体类方式)
     *
     * @param response
     * @param filename 文件名
     * @param list     导出的多个sheet页配置
     */
    public ModelAndView exportExcel(HttpServletResponse response, String filename, List<Map<String, Object>> list) {
        // 多个sheet配置参数
        final List<Map<String, Object>> sheetsList = Lists.newArrayList();
        for (Map<String, Object> map : list) {
            Map<String, Object> exportMap = Maps.newHashMap();
            final ExportParams exportParams = new ExportParams(null, (String) map.get("sheetName"), ExcelType.HSSF);
            exportParams.setStyle(AutoExcelStyleHandler.class);
            // 以下3个参数为API中写死的参数名 分别是sheet配置/导出类（注解定义）/数据集
            exportMap.put("title", exportParams);
            exportMap.put("entity", map.get("entity"));
            exportMap.put("data", map.get("data"));
            // 加入多sheet配置列表
            sheetsList.add(exportMap);
        }
        try {
            // 核心方法：导出含多个sheet的excel文件 【注意，该方法第二个参数必须与上述的ExportParams对象指定的导出类型一致，默认ExcelType.HSSF格式，否则执行此方法时会报错!!!】
            final Workbook workbook = ExcelExportUtil.exportExcel(sheetsList, ExcelType.HSSF);
            setHeadResponse(response, filename);
            OutputStream outputStream = response.getOutputStream();
            response.flushBuffer();
            workbook.write(outputStream);
            // 写完数据关闭流
            outputStream.close();
        } catch (IOException e) {
            setReturnResponse(response, e.getMessage());
        }
        return null;
    }

    /**
     * 【autoPoi方式】excel导出(自定义模板)
     *
     * @param dataList     数据集合(每个Map代表一个sheet页数据，map中的key表示数据对象，其中key必为name 文件名)
     * @param templatePath excel模板路径
     */
    public void exportExcelByAutoPoi(HttpServletResponse response, List<Map<String, Object>> dataList, String templatePath) {
        this.downloadExcel(response, this.createExcelByAutoPoi(dataList, templatePath));
    }


    /**
     * 【easyExcel方式】单excel导出(实体类方式)
     *
     * @param response  响应链接
     * @param filename  excel文件名
     * @param sheetName sheet页名称
     * @param data      导出数据集合
     * @throws IOException
     */
    public void exportExcel(HttpServletResponse response, String filename, String sheetName, List data) {
        try {
            if (CollUtil.isEmpty(data)) {
                throw new RuntimeException("导出数据不存在");
            }
            setHeadResponse(response, filename);
            EasyExcel.write(response.getOutputStream())
                    .head(data.get(0).getClass())
                    .registerWriteHandler(EasyExcelStyleHandler.initStyle())
                    .registerWriteHandler(new EasyExcelCustomCellWriteHandler())
                    .sheet(sheetName)
                    .doWrite(data);
        } catch (Exception e) {
            setReturnResponse(response, e.getMessage());
        }
    }

    /**
     * 【easyExcel方式】多excel导出(实体类方式)
     *
     * @param response   响应链接
     * @param filename   excel文件名
     * @param sheetNames 各sheet名称集合
     * @param data       各sheet数据集合
     */
    public void exportExcel(HttpServletResponse response, String filename, List<String> sheetNames, List<List> data) {
        ExcelWriter excelWriter = null;
        try {
            if (CollUtil.isEmpty(data)) {
                throw new RuntimeException("导出数据不存在");
            }
            if (CollUtil.isNotEmpty(sheetNames) && data.size() != sheetNames.size()) {
                throw new RuntimeException("sheet数据数量和sheet名称数量不相等");
            }
            setHeadResponse(response, filename);
            excelWriter = EasyExcel.write(response.getOutputStream()).build();
            for (int i = 0; i < data.size(); i++) {
                List d = data.get(i);
                if (d != null && d.size() > 0) {
                    WriteSheet ws = EasyExcel.writerSheet().sheetNo(i)
                            .sheetName(sheetNames == null ? "sheet" + i : sheetNames.get(i))
                            .head(d.get(0).getClass()).registerWriteHandler(EasyExcelStyleHandler.initStyle()).registerWriteHandler(new EasyExcelCustomCellWriteHandler()).build();
                    excelWriter.write(d, ws);
                }
            }
            excelWriter.finish();
        } catch (Exception e) {
            if (excelWriter != null) {
                excelWriter.finish();
            }
            this.setReturnResponse(response, e.getMessage());
        }
    }

    /**
     * 【EasyExcel方式】excel导出(自定义模板)
     *
     * @param dataList     数据集合(每个Map代表一个sheet页数据，map中的key表示数据对象，其中key必为name 文件名)
     * @param templatePath excel模板路径
     */
    public void exportExcelByEasyExcel(HttpServletResponse response, String templatePath, List<Map<String, Object>> dataList) {
        this.downloadExcel(response, this.createExcelByAutoPoi(dataList, templatePath));
    }

    /**
     * 导入后错误信息下载
     *
     * @param type         0 远程服务器生成 1本地生成
     * @param errorLines
     * @param successLines
     * @param errorMessage
     * @return
     * @throws IOException
     */
    public Result imporExcelError(Integer type, int errorLines, int successLines, List<String> errorMessage) {
        Result res = new Result();
        if (errorLines == 0) {
            res.setCode(200);
            res.setSuccess(true);
            res.setMessage("共" + successLines + "行数据格式全部正确可导入！！！");
        } else {
            //上传信息
            String excelErrorName = DateUtil.date().toString("yyyyMMdd") + Math.round(Math.random() * 10000.0D) + ".txt";
            String fileUrl;
            if (type == null) {
                fileUrl = uploadErrorLog(errorMessage, excelErrorName);
            } else {
                fileUrl = saveErrorTxtByList(errorMessage, excelErrorName);
            }
            //包装信息
            int totalCount = successLines + errorLines;
            Map<String, Object> result = new HashMap<>();
            result.put("totalCount", totalCount);
            result.put("errorCount", errorLines);
            result.put("successCount", successLines);
            result.put("msg", "总上传行数：" + totalCount + "，已导入行数：" + successLines + "，错误行数：" + errorLines);
            result.put("fileName", excelErrorName);
            result.put("errorFilePath", fileUrl);
            res.setSuccess(false);
            res.setCode(201);
            res.setResult(result);
            res.setMessage("文件导入成功，但有错误!!!");
        }
        return res;
    }

    /**
     * 【easyExcel】 生成excel
     *
     * @param dataList     数据集合和excel名称(map包含name文件名)
     * @param templatePath excel模板路径
     * @return 返回生成后文件路径
     */
    private String createExcelByEasyExcel(List<Map<String, Object>> dataList, String templatePath) {
        //定义生成文件夹的输出目录
        String timeId = IdWorker.getTimeId();
        String excelPath = this.baseDir + "/" + timeId;
        File file = new File(excelPath);
        if (!file.exists()) {
            file.mkdir();
        }
        //生成多个excel
        InputStream is = null;
        String excelName = "";
        for (int i = 0; i < dataList.size(); i++) {
            Map<String, Object> dataMap = dataList.get(i);
            if (dataMap.get("name") != null) {
                try {
                    is = ResourceUtil.getStream("classpath:" + templatePath);
                    //创建easyExcel写入对象
                    excelName = excelPath + "/" + dataMap.get("name") + "_" + IdUtil.simpleUUID().substring(0, 8) + ".xlsx";
                    ExcelWriter excelWriter = EasyExcel.write(excelName).withTemplate(is).excelType(ExcelTypeEnum.XLSX).build();
                    //默认写入第0页
                    WriteSheet writeSheet = EasyExcel.writerSheet().build();
                    //构建写入数据
                    excelWriter.fill(dataMap.get("one"), writeSheet);
                    // 这个 data 是官方约定就这么写， 传进去的 list 就是要遍历的表格数据，详见我定义的模板中图片位置右侧那部分
                    excelWriter.fill(dataMap.get("list"), writeSheet);
                    excelWriter.finish();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                } finally {
                    if (is != null) {
                        try {
                            is.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        if (dataList.size() > 1) {
            excelName = this.baseDir + "/" + timeId + ".zip";
        }
        return excelName;
    }


    /**
     * 【AutoPoi】 生成excel
     *
     * @param dataList     数据集合和excel名称(map包含name文件名)
     * @param templatePath excel模板路径
     * @return 返回生成后文件路径
     */
    public String createExcelByAutoPoi(List<Map<String, Object>> dataList, String templatePath) {
        if (CollUtil.isEmpty(dataList)) {
            throw new JeecgBootException("导出数据不存在！！！");
        }
        if (StrUtil.isBlank(templatePath)) {
            throw new JeecgBootException("导出模板路径不为空！！！");
        }
        String timeId = IdWorker.getTimeId();
        String excelPath = this.baseDir + "/" + timeId;
        File file = new File(excelPath);
        if (!file.exists()) {
            file.mkdir();
        }
        String filePath = "";
        for (int i = 0; i < dataList.size(); i++) {
            Map<String, Object> dataMap = dataList.get(i);
            if (dataMap.get("name") != null) {
                FileOutputStream outputStream = null;
                try {
                    cn.afterturn.easypoi.excel.entity.TemplateExportParams params;
                    Workbook workbook;
                    if (dataMap.get("sheets") != null) {
                        Map<Integer, Map<String, Object>> sheets = (Map<Integer, Map<String, Object>>) dataMap.get("sheets");
                        params = new TemplateExportParams(templatePath, sheets.keySet().toArray(new Integer[]{}));
                        workbook = cn.afterturn.easypoi.excel.ExcelExportUtil.exportExcel(sheets, params);
                    } else {
                        params = new TemplateExportParams(templatePath, true);
                        workbook = cn.afterturn.easypoi.excel.ExcelExportUtil.exportExcel(params, dataMap);
                    }
                    String excelName = excelPath + "/" + dataMap.get("name") + "_" + IdUtil.simpleUUID().substring(0, 8) + ".xlsx";
                    if (i == 0) {
                        filePath = excelName;
                    }
                    outputStream = new FileOutputStream(excelName);
                    workbook.write(outputStream);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    IoUtil.close(outputStream);
                }
            }
        }
        if (dataList.size() > 1) {
            filePath = this.baseDir + "/" + timeId + ".zip";
        }
        return filePath;
    }


    /**
     * excel和excel的zip压缩包下载
     *
     * @param response 输出流
     * @param filePath 需下载文件路径
     */
    public void downloadExcel(HttpServletResponse response, String filePath) {
        if (StrUtil.isBlank(filePath)) {
            throw new JeecgBootException("导出文件名不为空！！！");
        }
        OutputStream outputStream = null;
        InputStream fileInputStream = null;
        File file = null;
        String excelPath = this.baseDir;
        try {
            String[] split = filePath.split("/");
            String fileName = split[split.length - 1];
            if (filePath.endsWith(".zip")) {
                int dotIndex = fileName.lastIndexOf('.');
                if (dotIndex != -1) {
                    excelPath = excelPath + "/" + fileName.substring(0, dotIndex);
                    file = ZipUtil.zip(excelPath, filePath);
                    fileInputStream = new FileInputStream(file);
                }
            } else {
                if (filePath.indexOf("template") != -1) {
                    //绝对路径(模板)
                    fileInputStream = this.getClass().getClassLoader().getResourceAsStream(filePath);
                } else {
                    //相对路径
                    fileInputStream = new FileInputStream(new File(filePath));
                }
            }
            if (fileInputStream != null) {
                byte[] buf = new byte[1024];
                response.setContentType("application/force-download");
                response.addHeader("Content-Disposition", "attachment;fileName=" + new String(fileName.getBytes("UTF-8"), "iso-8859-1"));
                outputStream = response.getOutputStream();
                int len;
                while ((len = fileInputStream.read(buf)) > 0) {
                    outputStream.write(buf, 0, len);
                }
                response.flushBuffer();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IoUtil.close(fileInputStream);
            IoUtil.close(outputStream);
            // 删除临时文件夹和临时压缩文件
            FileUtil.del(file);
            if (filePath.endsWith(".zip")) {
                FileUtil.del(excelPath);
            }
        }
    }

    /**
     * 本地导入错误excel上传信息
     *
     * @param msg
     * @param excelErrorName
     * @return
     */
    private String uploadErrorLog(List<String> msg, String excelErrorName) {
        File file = null;
        //服务器本地先生成文件
        String fileName = saveErrorTxtByList(msg, excelErrorName);
        //将生成的文件上传至文件服务器
        MultipartFile multipartFile = getMultipartFile(new File(fileName));
        SysFile sysFile = new SysFile();
        sysFile.setType("excelError");
        sysFile.setFileName(fileName);
        String path = CommonUtils.upload(multipartFile, sysFile.getType(), this.uploadType, (String) null);
        String id = IdWorker.get32UUID();
        sysFile.setFilePath(path);
        sysFile.setId(id);
        if (this.sysFileService.save(sysFile)) {
            return path;
        } else {
            throw new RuntimeException("错误");
        }
    }

    /**
     * 将普通文件转为媒体文件
     *
     * @param file
     * @return
     */
    private MultipartFile getMultipartFile(File file) {
        FileItem item = new DiskFileItemFactory().createItem("file"
                , MediaType.MULTIPART_FORM_DATA_VALUE
                , true
                , file.getName());
        try (InputStream input = new FileInputStream(file);
             OutputStream os = item.getOutputStream()) {
            // 流转移
            IOUtils.copy(input, os);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid file: " + e, e);
        }

        return new CommonsMultipartFile(item);
    }


    /**
     * 本地生成错误文件
     *
     * @param msg
     * @param name
     * @return
     */
    private String saveErrorTxtByList(List<String> msg, String name) {
        //文件夹路径
        String saveFullDir = baseDir + File.separator + "excelError" + File.separator + DateUtil.date().toString("yyyyMMdd") + File.separator;
        File saveFile = new File(saveFullDir);
        if (!saveFile.exists()) {
            saveFile.mkdirs();
        }
        //文件全路径
        String saveFilePath = saveFullDir + name;
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(saveFilePath));
            for (Iterator var8 = msg.iterator(); var8.hasNext(); bw.write("\r\n")) {
                String s = (String) var8.next();
                if (s.indexOf("_") > 0) {
                    String[] arr = s.split("_");
                    bw.write("第" + arr[0] + "行:" + arr[1]);
                } else {
                    bw.write(s);
                }
            }
            bw.flush();
            bw.close();
        } catch (Exception var11) {
            throw new RuntimeException(var11);
        }
        return saveFilePath;
    }

    /**
     * 设置请求头
     *
     * @param response
     * @param filename
     * @throws UnsupportedEncodingException
     */
    private static void setHeadResponse(HttpServletResponse response, String filename) throws UnsupportedEncodingException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName = URLEncoder.encode(filename, "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");

    }

    /**
     * 设置响应头
     *
     * @param response
     * @param msg
     */
    private static void setReturnResponse(HttpServletResponse response, String msg) {
        response.reset();
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        Result<Object> result = new Result<>();
        result.setCode(500);
        result.setSuccess(false);
        result.setMessage(msg);
        try {
            response.getWriter().println(JSON.toJSONString(result));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }


}


