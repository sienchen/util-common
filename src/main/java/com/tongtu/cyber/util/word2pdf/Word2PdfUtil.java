package com.tongtu.cyber.util.word2pdf;

import com.aspose.words.Document;
import com.aspose.words.FontSettings;
import com.aspose.words.License;
import com.aspose.words.SaveFormat;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.tongtu.cyber.common.exception.JeecgBootException;
import com.tongtu.cyber.util.upload.UploadUtil;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * word模板编辑生成工具
 */
public class Word2PdfUtil {

    private static final String ENCODING = "UTF-8";
    private static Configuration cfg = new Configuration();

    static {
        cfg.setClassForTemplateLoading(Word2PdfUtil.class, "/templates");
        cfg.setEncoding(Locale.getDefault(), ENCODING);
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.IGNORE_HANDLER);

    }

    /**
     * 根据模板下载pdf
     *
     * @param data         模板中数据
     * @param templateName 模板名
     * @param wordPath     word路径
     * @param response
     * @throws Exception
     */
    public static void downLoadPDF(Map<String, Object> data, String templateName, String wordPath, HttpServletResponse response) throws Exception {
        //生成word
        generateWord(data, templateName, wordPath);
        //word转pdf
        String pdfPath = generatePDF(wordPath);
        //下载
        UploadUtil.downLoad(response, pdfPath);
    }

    /**
     * 根据模板下载word
     *
     * @param data         模板中数据
     * @param templateName 模板名
     * @param wordPath     word路径
     * @param response
     * @throws Exception
     */
    public static void downLoadWord(Map<String, Object> data, String templateName, String wordPath, HttpServletResponse response) throws Exception {
        //生成word
        generateWord(data, templateName, wordPath);
        //下载
        UploadUtil.downLoad(response, wordPath);

    }

    /**
     * 根据模板名称获取模板对象
     *
     * @param templateName 模板名称
     * @return
     * @throws IOException
     */
    public static Template getTemplate(String templateName) throws IOException {
        return cfg.getTemplate(templateName, ENCODING);
    }


    /**
     * 根据模板生成word
     *
     * @param data         模板中数据
     * @param templateName 模板名
     * @param wordPath     word路径
     * @throws Exception
     */
    public static void generateWord(Map<String, Object> data, String templateName, String wordPath) throws Exception {
        Writer out = null;
        File outFile = new File(wordPath);
        try {
            Template template = getTemplate(templateName);
            if (!outFile.getParentFile().exists()) {
                outFile.getParentFile().mkdirs();
            }
            out = new OutputStreamWriter(new FileOutputStream(outFile), ENCODING);
            template.process(data, out);
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.close();
        }

    }

    /**
     * 根据word生成pdf
     *
     * @param wordPath word文件路径
     * @return
     */
    public static String generatePDF(String wordPath) {
        File wordFile = new File(wordPath);
        if (!wordFile.exists()) {
            new JeecgBootException("文件不存在");
        }
        FileOutputStream os = null;
        try {
            String pdfPath = convertFilePathToPdfPath(wordPath);
            String s = "<License><Data><Products><Product>Aspose.Total for Java</Product><Product>Aspose.Words for Java</Product></Products><EditionType>Enterprise</EditionType><SubscriptionExpiry>20991231</SubscriptionExpiry><LicenseExpiry>20991231</LicenseExpiry><SerialNumber>8bfe198c-7f0c-4ef8-8ff0-acc3237bf0d7</SerialNumber></Data><Signature>sNLLKGMUdF0r8O1kKilWAGdgfs2BvJb/2Xp8p5iuDVfZXmhppo+d0Ran1P9TKdjV4ABwAgKXxJ3jcQTqE/2IRfqwnPf8itN8aFZlV3TJPYeD3yWE7IT55Gz6EijUpC7aKeoohTb4w2fpox58wWoF3SNp6sK6jDfiAUGEHYJ9pjU=</Signature></License>";
            ByteArrayInputStream is = new ByteArrayInputStream(s.getBytes());
            License license = new License();
            license.setLicense(is);
            // 新建pdf文件
            File pdfFile = new File(pdfPath);
            os = new FileOutputStream(pdfFile);
            //将要被转化的word文档
            Document doc = new Document(wordPath);
            //引入字体
            FontSettings.setFontsFolder("/usr/share/fonts/chinese/", true);
            doc.save(os, SaveFormat.PDF);
            return pdfPath;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * word名称转为pdf名称
     *
     * @param filePath
     * @return
     */
    private static String convertFilePathToPdfPath(String filePath) {
        return filePath.replaceFirst("\\.[^\\.]+$", ".pdf");
    }

    public static void main(String[] args) {
        //生成word名称
        String wordName = "督办函-安全生产事故隐患整改督办通知书" + IdWorker.getTimeId() + ".docx";
        //生成模板名称
        String templateName = "scdb.ftl";
        //word路径
        String wordPath = "D:\\data\\tongtu\\sc" + File.separator + wordName;
        try {
            generateWord(new HashMap<>(), templateName, wordPath);
            generatePDF(wordPath);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
