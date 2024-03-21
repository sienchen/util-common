package com.tongtu.cyber.util.upload.minio.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.tongtu.cyber.util.upload.minio.domain.FileChunk;
import com.tongtu.cyber.util.upload.minio.mapper.FileChunkMapper;
import com.tongtu.cyber.util.upload.minio.service.FileChunkService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 文件分块记录 服务实现类
 * </p>
 *
 * @author 陈世恩
 * @since 2024-03-21
 */
@Service
@DS("business")
public class FileChunkServiceImpl extends ServiceImpl<FileChunkMapper, FileChunk> implements FileChunkService {
    @Autowired
    private FileChunkMapper mapper;

    @Override
    public List<FileChunk> findByMd5(String md5) {
        QueryWrapper<FileChunk> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(StrUtil.isNotBlank(md5), "md5", md5);
        queryWrapper.eq("complete_flag", 0);
        return this.mapper.selectList(queryWrapper);
    }

    @Override
    public void deleteByMd5(String md5) {
        UpdateWrapper<FileChunk> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("complete_flag", 1);
        updateWrapper.eq("md5", md5);
        this.mapper.update(null, updateWrapper);
    }
}
