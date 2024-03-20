package com.tongtu.cyber.util.upload;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Sets;
import com.tongtu.cyber.common.constant.enums.ViewContentType;
import io.minio.*;
import io.minio.messages.DeleteError;
import io.minio.messages.DeleteObject;
import io.minio.messages.Item;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * minio服务，文件上传管理工具类
 *
 * @author : 陈世恩
 * @date : 2024/3/20 11:24
 */
@Log4j2
public class MinioUtil {
    private static String minioUrl;
    private static String minioName;
    private static String minioPass;
    private static String bucketName;
    private static MinioClient minioClient = null;

    public static void setMinioUrl(String minioUrl) {
        MinioUtil.minioUrl = minioUrl;
    }

    public static void setMinioName(String minioName) {
        MinioUtil.minioName = minioName;
    }

    public static void setMinioPass(String minioPass) {
        MinioUtil.minioPass = minioPass;
    }

    public static void setBucketName(String bucketName) {
        MinioUtil.bucketName = bucketName;
    }

    /**
     * 获取minio连接地址
     *
     * @return
     */
    public static String getMinioUrl() {
        return minioUrl;
    }

    /**
     * 获取minio桶名
     *
     * @return
     */
    public static String getBucketName() {
        return bucketName;
    }

    /**
     * 初始化minio
     *
     * @param minioUrl
     * @param minioName
     * @param minioPass
     * @return
     */
    private static MinioClient initMinio(String minioUrl, String minioName, String minioPass) {
        if (minioClient == null) {
            try {
                minioClient = MinioClient.builder().endpoint(minioUrl).credentials(minioName, minioPass).build();
            } catch (Exception e) {
                log.error("minio连接失败", e);
                e.printStackTrace();
            }
        }
        return minioClient;
    }

    /**
     * 获取minio连接
     *
     * @param newBucketName
     * @throws Exception
     */
    private static void connection(String newBucketName) throws Exception {
        //默认使用桶名
        newBucketName = StrUtil.isNotEmpty(newBucketName) ? newBucketName : bucketName;
        //获取minio连接对象
        initMinio(minioUrl, minioName, minioPass);
        //判断桶名是否存在或者新建
        if (minioClient.bucketExists((BucketExistsArgs) ((BucketExistsArgs.Builder) BucketExistsArgs.builder().bucket(newBucketName)).build())) {
            log.info("Bucket already exists.");
        } else {
            minioClient.makeBucket((MakeBucketArgs) ((MakeBucketArgs.Builder) MakeBucketArgs.builder().bucket(newBucketName)).build());
            log.info("create a new bucket.");
        }
    }

    /**
     * minio文件上传
     *
     * @param file          上传文件
     * @param fileType      文件类型
     * @param newBucketName 文件桶名
     * @return 文件相对路径
     */
    public static String upload(MultipartFile file, String fileType, String newBucketName) throws Exception {
        //默认文件类型
        fileType = StrUtil.isEmpty(fileType) ? "temporary" : fileType;
        connection(newBucketName);
        //构建文件输入流
        InputStream stream = file.getInputStream();
        //获取文件名称
        String orgName = file.getOriginalFilename();
        orgName = UploadUtil.formatFileName(StrUtil.isEmpty(orgName) ? file.getName() : orgName);
        //构建文件存储路径
        String today = DateUtil.today().replace("-", "");
        String filePath = fileType + "/" + today + "/" + orgName.substring(0, orgName.lastIndexOf(".")) + "_" + System.currentTimeMillis() + orgName.substring(orgName.indexOf("."));
        filePath = filePath.startsWith("/") ? filePath.substring(1) : filePath;
        PutObjectArgs objectArgs = (PutObjectArgs) ((PutObjectArgs.Builder) ((PutObjectArgs.Builder) PutObjectArgs.builder().object(filePath)).bucket(newBucketName)).contentType(ViewContentType.getContentType(filePath)).stream(stream, (long) stream.available(), -1L).build();
        minioClient.putObject(objectArgs);
        stream.close();
        return filePath;
    }

    /**
     * @param stream       输入文件流
     * @param relativePath 文件存储路径
     * @return 文件绝对路径
     * @throws Exception
     */
    public static String upload(InputStream stream, String relativePath) throws Exception {
        connection(null);
        PutObjectArgs objectArgs = (PutObjectArgs) ((PutObjectArgs.Builder) ((PutObjectArgs.Builder) PutObjectArgs.builder().object(relativePath)).bucket(bucketName)).contentType("application/octet-stream").stream(stream, (long) stream.available(), -1L).build();
        minioClient.putObject(objectArgs);
        stream.close();
        return minioUrl + bucketName + "/" + relativePath;
    }

    /**
     * minio分片上传大文件
     *
     * @param md5         文件md5唯一标识
     * @param totalPieces 文件总片数
     * @param sliceIndex  文件当前片数
     * @param file        分片文件
     * @return
     * @throws Exception
     */
    public static boolean upload(String md5, int totalPieces, int sliceIndex, MultipartFile file) throws Exception {
        connection(null);
        if (1 == sliceIndex) {
            log.info("开始上传:" + DateUtil.now() + "===================================================");
        }
        //上传文件
        InputStream stream = file.getInputStream();
        PutObjectArgs objectArgs = (PutObjectArgs) ((PutObjectArgs.Builder) ((PutObjectArgs.Builder) PutObjectArgs.builder().object(md5.concat("/").concat(Integer.toString(sliceIndex)))).bucket(bucketName)).contentType(file.getContentType()).stream(stream, (long) stream.available(), -1L).build();
        minioClient.putObject(objectArgs);
        stream.close();
        //上传分片记录存入数据库
        //fileChunkService.saveFileChunk(param);
        //判断是否全部上传完成
        if (isUploadComplete(md5, totalPieces)) {
            mergeMinioFile(md5, totalPieces, "", "");
            // 删除所有的分片文件
            removeMinioFile(md5);
            //删除数据库分片记录
            //fileChunkService.deleteByMd5(md5);
            //上传的合并记录存入数据库
            log.info("完成上传:" + DateUtil.now() + "===================================================");
            //保存上传记录进数据库正式表
            // return localStorageService.saveLocalStorage(param);
        }
        return false;
    }

    /**
     * 获取minio文件流
     *
     * @param newBucketName 桶名
     * @param filePath      文件路径
     * @return
     */
    public static InputStream getMinioFile(String newBucketName, String filePath) {
        InputStream inputStream = null;
        try {
            //使用默认桶名
            newBucketName = StrUtil.isNotEmpty(newBucketName) ? newBucketName : bucketName;
            initMinio(minioUrl, minioName, minioPass);
            GetObjectArgs objectArgs = (GetObjectArgs) ((GetObjectArgs.Builder) ((GetObjectArgs.Builder) GetObjectArgs.builder().object(filePath)).bucket(newBucketName)).build();
            inputStream = minioClient.getObject(objectArgs);
        } catch (Exception var4) {
            log.info("文件获取失败" + var4.getMessage());
        }
        return inputStream;
    }

    /**
     * 删除minio文件
     *
     * @param newBucketName 桶名
     * @param filePath      文件路径
     */
    public static void removeMinioFile(String newBucketName, String filePath) {
        try {
            //使用默认桶名
            newBucketName = StrUtil.isNotEmpty(newBucketName) ? newBucketName : bucketName;
            initMinio(minioUrl, minioName, minioPass);
            RemoveObjectArgs objectArgs = (RemoveObjectArgs) ((RemoveObjectArgs.Builder) ((RemoveObjectArgs.Builder) RemoveObjectArgs.builder().object(filePath)).bucket(newBucketName)).build();
            minioClient.removeObject(objectArgs);
        } catch (Exception var3) {
            log.info("文件删除失败" + var3.getMessage());
        }

    }


    public static void download(HttpServletResponse response, String bucketName, String filePath) throws IOException {
        InputStream inputStream = getMinioFile(bucketName, filePath);
        String fileName = new String(UploadUtil.formatFileName(filePath).getBytes("UTF-8"), "iso-8859-1");
        UploadUtil.downLoad(response, fileName, inputStream);
    }

    private static boolean isUploadComplete(String md5, Integer totalPieces) throws Exception {
        ListObjectsArgs objectsArgs = ListObjectsArgs.builder().bucket(bucketName).prefix(md5.concat("/")).build();
        Iterable<Result<Item>> list = minioClient.listObjects(objectsArgs);
        Set<String> objectNames = Sets.newHashSet();
        for (Result<Item> item : list) {
            objectNames.add(item.get().objectName());
        }
        //minio上总片数等于上传的总片数
        return objectNames.size() == totalPieces;
    }

    /**
     * 合并minio的分片文件
     *
     * @param md5         文件md5唯一标识
     * @param totalPieces 文件总片数
     * @param fileName    合并后文件名称
     * @param fileType    合并后文件类型
     * @throws Exception
     */
    private static void mergeMinioFile(String md5, int totalPieces, String fileName, String fileType) throws Exception {
        // 完成上传从缓存目录合并迁移到正式目录
        List<ComposeSource> sourceObjectList = Stream.iterate(1, i -> ++i)
                .limit(totalPieces)
                .map(i -> ComposeSource.builder()
                        .bucket(bucketName)
                        .object(md5.concat("/").concat(Integer.toString(i)))
                        .build())
                .collect(Collectors.toList());
        String objectName = fileType.concat("/").concat(fileName);
        minioClient.composeObject(
                ComposeObjectArgs.builder()
                        .bucket(bucketName)
                        .object(objectName)
                        .sources(sourceObjectList)
                        .build());
    }

    /**
     * 删除minio的分片文件
     *
     * @param md5 文件md5唯一标识
     * @throws Exception
     */
    private static void removeMinioFile(String md5) throws Exception {
        //获取所有md5分片文件
        Iterable<Result<Item>> list = minioClient.listObjects(
                ListObjectsArgs.builder().bucket(bucketName)
                        .prefix(md5.concat("/")).build());
        //收集需要删除的md5文件
        List<DeleteObject> delObjects = new ArrayList<>();
        int i = 1;
        for (Result<Item> item : list) {
            delObjects.add(new DeleteObject(md5.concat("/").concat(i + "")));
            i++;
        }
        //删除md5相关文件分片
        Iterable<Result<DeleteError>> deleteRes = minioClient.removeObjects(
                RemoveObjectsArgs.builder().bucket(bucketName)
                        .objects(delObjects).build());
        //打印删除失败信息
        for (Result<DeleteError> result : deleteRes) {
            DeleteError error = result.get();
            log.error("minio文件删除失败!!!md5=" + md5 + ":" + error.objectName() + "; " + error.message());
        }
    }
}

