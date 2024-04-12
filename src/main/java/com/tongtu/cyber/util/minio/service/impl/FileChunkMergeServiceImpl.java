package com.tongtu.cyber.util.minio.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tongtu.cyber.common.system.util.JwtUtil;
import com.tongtu.cyber.util.characters.PingYingUtil;
import com.tongtu.cyber.util.characters.StrRegFilterUtil;
import com.tongtu.cyber.util.minio.MinioTool;
import com.tongtu.cyber.util.minio.domain.FileChunk;
import com.tongtu.cyber.util.minio.domain.FileChunkMerge;
import com.tongtu.cyber.util.minio.domain.vo.FileChunkVo;
import com.tongtu.cyber.util.minio.mapper.FileChunkMergeMapper;
import com.tongtu.cyber.util.minio.service.FileChunkMergeService;
import com.tongtu.cyber.util.minio.service.FileChunkService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 文件分片合并记录 服务实现类
 * </p>
 *
 * @author 陈世恩
 * @since 2024-03-21
 */
@Log4j2
@Service
@DS("business")
public class FileChunkMergeServiceImpl extends ServiceImpl<FileChunkMergeMapper, FileChunkMerge> implements FileChunkMergeService {
    @Autowired
    private FileChunkMergeMapper mapper;
    @Autowired
    private FileChunkService fileChunkService;

    @Override
    public FileChunkMerge findByMd5(String md5) {
        QueryWrapper<FileChunkMerge> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(StrUtil.isNotBlank(md5), "md5", md5);
        return this.mapper.selectOne(queryWrapper);
    }

    /**
     * minio方式切片上传
     *
     * @param param
     * @throws Exception
     */
    @Override
    public FileChunkVo uploadByMinio(FileChunk param) throws Exception {
        //判断文件不为空
        if (ObjUtil.isEmpty(param) || ObjUtil.isEmpty(param.getFile())) {
            throw new RuntimeException("上传文件未空!!!");
        }
        FileChunkMerge chunkMerge = this.findByMd5(param.getMd5());
        FileChunkVo vo = new FileChunkVo();
        vo.setFlag(false);
        if (ObjUtil.isNotEmpty(chunkMerge)) {
            //文件已上传完成
            vo.setId(chunkMerge.getId());
            vo.setFilePath(chunkMerge.getFilePath());
            vo.setFlag(true);
        } else {
            //设置默认文件类型
            if (StrUtil.isBlank(param.getFileType())) {
                param.setFileType("temporary");
            } else {
                param.setFileType(PingYingUtil.getPingYin(param.getFileType()) + (int) ((Math.random() * 9 + 1) * 1000));
            }
            if (param.getTotalChunk() == 1) {
                String objectName = param.getFileType().concat("/").concat(param.getFileName());
                MinioTool.upload(param.getFile().getInputStream(), objectName);
                param.setFilePath(objectName);
                this.saveRecourd(param);
            } else {
                vo.setFlag(upload(param));
            }
        }
        return vo;

    }

    /**
     * minio分片上传大文件
     *
     * @param param
     * @return
     * @throws Exception
     */
    public boolean upload(FileChunk param) throws Exception {
        //文件唯一标识
        String md5 = param.getMd5();
        //总分片数
        int totalPieces = param.getTotalChunk();
        //当前分片
        int sliceIndex = param.getCurrentChunkIndex();
        //上传文件
        MultipartFile file = param.getFile();
        //上传分片
        String objectName = md5.concat("/").concat(Integer.toString(sliceIndex));
        MinioTool.upload(file.getInputStream(), objectName);
        //判断是否上传完成
        if (MinioTool.isUploadComplete(md5, totalPieces)) {
            //合并分片
            MinioTool.mergeMinioFile(md5, totalPieces, param.getFileName(), param.getFileType());
            // 删除分片文件
            MinioTool.removeMinioFile(md5);
            //删除数据库分片记录
            fileChunkService.deleteByMd5(md5);
            //保存上传记录进数据库正式表
            return saveRecourd(param);
        }
        return false;
    }

    @Override
    public boolean saveRecourd(FileChunk param) {
        FileChunkMerge chunkMerge = findByMd5(param.getMd5());
        if (ObjUtil.isEmpty(chunkMerge)) {
            chunkMerge = new FileChunkMerge();
        }
        String userName = JwtUtil.getUserName();
        Date date = new Date();
        String realName = param.getFileName();
        String name = StrRegFilterUtil.getFileName(realName);
        String suffix = StrRegFilterUtil.getFileExtension(realName);
        chunkMerge.setFileRealName(realName);
        chunkMerge.setFileName(name);
        chunkMerge.setSuffix(suffix);
        chunkMerge.setTotalSize(StrRegFilterUtil.convertFileSize(param.getTotalSize().longValue()));
        chunkMerge.setMd5(param.getMd5());
        chunkMerge.setFileType(param.getFileType());
        chunkMerge.setFilePath(param.getFileType().concat("/").concat(param.getFileName()));
        //后缀pdf，插入页数
        if ("pdf".equals(suffix.trim())) {
            chunkMerge.setMaterialDuration(getFilePage(param.getFile()));
        }
        chunkMerge.setUpdateBy(userName);
        chunkMerge.setCreatedBy(userName);
        chunkMerge.setUpdateTime(date);
        chunkMerge.setCreatedTime(date);
        return saveOrUpdate(chunkMerge);

    }

    @Override
    public FileChunkVo isExit(FileChunk param) {
        FileChunkMerge chunkMerge = this.findByMd5(param.getMd5());
        FileChunkVo vo = new FileChunkVo();
        if (ObjUtil.isNotEmpty(chunkMerge)) {
            //文件已上传完成
            vo.setId(chunkMerge.getId());
            vo.setFilePath(chunkMerge.getFilePath());
            vo.setFlag(true);
        } else {
            //文件未上传完成
            List<FileChunk> list = fileChunkService.findByMd5(param.getMd5());
            vo.setFlag(false);
            if (CollUtil.isNotEmpty(list)) {
                // 已上传分片下标
                Integer[] indexArr = new Integer[list.size()];
                int index = 0;
                for (FileChunk fileChunk : list) {
                    indexArr[index] = fileChunk.getCurrentChunkIndex();
                    index++;
                }
                vo.setIndexArr(indexArr);
            }
        }
        return vo;
    }

    /**
     * 获取pdf文件页数
     * MultipartFile:pdf文件
     */
    public int getFilePage(MultipartFile multipartFile) {
        /* try {
            PdfReader pdfReader = new PdfReader(multipartFile.getInputStream());
            //pdf页数
            int pdfPage = pdfReader.getNumberOfPages();
            if (pdfPage != 0) {
                QueryWrapper<GhTrainConfig> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("type", "train_pdf");
                List<GhTrainConfig> list = this.configService.list(queryWrapper);
                if (CollUtil.isNotEmpty(list)) {
                    Integer value = list.get(0).getValue();
                    return value * pdfPage;
                } else {
                    return pdfPage;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } */
        return 0;
    }
}
