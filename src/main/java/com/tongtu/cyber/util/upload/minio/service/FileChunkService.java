package com.tongtu.cyber.util.upload.minio.service;

import com.tongtu.cyber.util.upload.minio.domain.FileChunk;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 文件分块记录 服务类
 * </p>
 *
 * @author 陈世恩
 * @since 2024-03-21
 */
public interface FileChunkService extends IService<FileChunk> {

    List<FileChunk> findByMd5(String md5);

    void deleteByMd5(String md5);
}
