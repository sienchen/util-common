package com.tongtu.cyber.util.upload.minio.service;

import com.tongtu.cyber.util.upload.minio.domain.FileChunk;
import com.tongtu.cyber.util.upload.minio.domain.FileChunkMerge;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tongtu.cyber.util.upload.minio.domain.vo.FileChunkVo;

/**
 * <p>
 * 文件分片合并记录 服务类
 * </p>
 *
 * @author 陈世恩
 * @since 2024-03-21
 */
public interface FileChunkMergeService extends IService<FileChunkMerge> {

    FileChunkMerge findByMd5(String md5);

    FileChunkVo uploadByMinio(FileChunk param) throws Exception;

    boolean saveRecourd(FileChunk param);

    FileChunkVo isExit(FileChunk param);
}
