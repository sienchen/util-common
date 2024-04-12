package com.tongtu.cyber.util.minio;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Sets;
import com.tongtu.cyber.common.constant.enums.ViewContentType;
import com.tongtu.cyber.common.exception.JeecgBootException;
import com.tongtu.cyber.util.characters.StrRegFilterUtil;
import com.tongtu.cyber.util.upload.UpLoadUtil;
import io.minio.*;
import io.minio.messages.DeleteError;
import io.minio.messages.DeleteObject;
import io.minio.messages.Item;
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
public class MinioTool {
    private static String minioUrl;
    private static String minioName;
    private static String minioPass;
    private static String bucketName;
    private static MinioClient minioClient = null;

    public static void setMinioUrl(String minioUrl) {
        MinioTool.minioUrl = minioUrl;
    }

    public static void setMinioName(String minioName) {
        MinioTool.minioName = minioName;
    }

    public static void setMinioPass(String minioPass) {
        MinioTool.minioPass = minioPass;
    }

    public static void setBucketName(String bucketName) {
        MinioTool.bucketName = bucketName;
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
     * 获取minio连接 对象
     *
     * @param newBucketName 使用默认桶名
     * @throws Exception
     */
    public static MinioClient connection(String newBucketName) throws Exception {
        //默认使用桶名
        newBucketName = StrUtil.isNotEmpty(newBucketName) ? newBucketName : bucketName;
        //连接minio服务
        if (minioClient == null) {
            minioClient = MinioClient.builder().endpoint(minioUrl).credentials(minioName, minioPass).build();
        }
        //判断桶名是否存在或者新建
        if (minioClient.bucketExists((BucketExistsArgs) ((BucketExistsArgs.Builder) BucketExistsArgs.builder().bucket(newBucketName)).build())) {
        } else {
            minioClient.makeBucket((MakeBucketArgs) ((MakeBucketArgs.Builder) MakeBucketArgs.builder().bucket(newBucketName)).build());
        }
        return minioClient;
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
        connection(newBucketName);
        //默认文件类型
        fileType = StrUtil.isEmpty(fileType) ? "temporary" : fileType;
        //构建文件输入流
        InputStream stream = file.getInputStream();
        //获取文件名称
        String orgName = file.getOriginalFilename();
        orgName = StrRegFilterUtil.formatFileName(StrUtil.isEmpty(orgName) ? file.getName() : orgName);
        //构建文件存储路径
        String today = DateUtil.today().replace("-", "");
        String objectName = fileType + "/" + today + "/" + orgName.substring(0, orgName.lastIndexOf(".")) + "_" + System.currentTimeMillis() + orgName.substring(orgName.indexOf("."));
        objectName = objectName.startsWith("/") ? objectName.substring(1) : objectName;
        PutObjectArgs objectArgs = (PutObjectArgs) ((PutObjectArgs.Builder) ((PutObjectArgs.Builder) PutObjectArgs.builder().object(objectName)).bucket(newBucketName)).contentType(ViewContentType.getContentType(objectName)).stream(stream, (long) stream.available(), -1L).build();
        minioClient.putObject(objectArgs);
        stream.close();
        return objectName;
    }

    /**
     * @param stream     输入文件流
     * @param objectName 文件存储路径
     * @return 文件绝对路径
     * @throws Exception
     */
    public static String upload(InputStream stream, String objectName) throws Exception {
        connection(null);
        PutObjectArgs objectArgs = (PutObjectArgs) ((PutObjectArgs.Builder) ((PutObjectArgs.Builder) PutObjectArgs.builder().object(objectName)).bucket(bucketName)).contentType("application/octet-stream").stream(stream, (long) stream.available(), -1L).build();
        minioClient.putObject(objectArgs);
        stream.close();
        return objectName;
    }


    /**
     * 获取minio文件流
     *
     * @param newBucketName 桶名
     * @param objectName    文件路径
     * @return
     */
    public static InputStream getMinioFile(String newBucketName, String objectName) throws Exception {
        connection(newBucketName);
        GetObjectArgs objectArgs = (GetObjectArgs) ((GetObjectArgs.Builder) ((GetObjectArgs.Builder) GetObjectArgs.builder().object(objectName)).bucket(newBucketName)).build();
        return minioClient.getObject(objectArgs);
    }

    /**
     * 删除minio文件
     *
     * @param newBucketName 桶名
     * @param objectName    文件路径
     */
    public static void removeMinioFile(String newBucketName, String objectName) throws Exception {
        connection(newBucketName);
        RemoveObjectArgs objectArgs = (RemoveObjectArgs) ((RemoveObjectArgs.Builder) ((RemoveObjectArgs.Builder) RemoveObjectArgs.builder().object(objectName)).bucket(newBucketName)).build();
        minioClient.removeObject(objectArgs);
    }

    /**
     * 下载minio文件
     *
     * @param response
     * @param bucketName
     * @param filePath
     * @throws IOException
     */
    public static void download(HttpServletResponse response, String bucketName, String filePath) throws Exception {
        InputStream inputStream = getMinioFile(bucketName, filePath);
        String fileName = new String(StrRegFilterUtil.formatFileName(filePath).getBytes("UTF-8"), "iso-8859-1");
        new UpLoadUtil().downLoad(response, fileName, inputStream);
    }

    /**
     * 判断文件分片上传是否完成
     *
     * @param md5         件md5唯一标识
     * @param totalPieces 文件总片数
     * @return
     * @throws Exception
     */
    public static boolean isUploadComplete(String md5, Integer totalPieces) throws Exception {
        connection(null);
        ListObjectsArgs objectsArgs = ListObjectsArgs.builder().bucket(bucketName).prefix(md5.concat("/")).build();
        Iterable<Result<Item>> list = minioClient.listObjects(objectsArgs);
        Set<String> objectNames = Sets.newHashSet();
        for (Result<Item> item : list) {
            objectNames.add(item.get().objectName());
        }
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
    public static void mergeMinioFile(String md5, int totalPieces, String fileName, String fileType) throws Exception {
        connection(null);
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
    public static void removeMinioFile(String md5) throws Exception {
        connection(null);
        //获取所有md5分片文件
        Iterable<Result<Item>> list = minioClient.listObjects(
                ListObjectsArgs.builder().bucket(bucketName)
                        .prefix(md5.concat("/")).build());
        //需要删除的md5文件
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
            throw new JeecgBootException("minio文件删除失败!!!md5=" + md5 + ":" + error.objectName() + "; " + error.message());
        }
    }
}

