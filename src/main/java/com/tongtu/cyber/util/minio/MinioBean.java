package com.tongtu.cyber.util.minio;

import cn.hutool.core.util.StrUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

/**
 * minio连接参数配置
 *
 * @author : 陈世恩
 * @date : 2024/3/20 14:55
 */
//@Configuration
public class MinioBean {
    @Value("${jeecg.minio.minio_url:null}")
    private String minioUrl;
    @Value("${jeecg.minio.minio_name:null}")
    private String minioName;
    @Value("${jeecg.minio.minio_pass:null}")
    private String minioPass;
    @Value("${jeecg.minio.bucketName:null}")
    private String bucketName;

    @Bean
    public void initMinio() {
        if (StrUtil.isNotBlank(this.minioUrl)) {
            if (!this.minioUrl.startsWith("http")) {
                this.minioUrl = "http://" + this.minioUrl;
            }

            if (!this.minioUrl.endsWith("/")) {
                this.minioUrl = this.minioUrl.concat("/");
            }
        }
        MinioTool.setMinioUrl(this.minioUrl);
        MinioTool.setMinioName(this.minioName);
        MinioTool.setMinioPass(this.minioPass);
        MinioTool.setBucketName(this.bucketName);
    }

}
