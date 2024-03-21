package com.tongtu.cyber.util.upload.minio.controller;

import com.tongtu.cyber.common.api.vo.Result;
import com.tongtu.cyber.util.upload.minio.domain.FileChunk;
import com.tongtu.cyber.util.upload.minio.domain.vo.FileChunkVo;
import com.tongtu.cyber.util.upload.minio.service.FileChunkMergeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 文件分块记录 前端控制器
 * </p>
 *
 * @author 陈世恩
 * @since 2024-03-21
 */
@RestController
@RequestMapping("/file-chunk")
@Api(value = "FileUploadController对象", tags = "大文件分块上传")
public class FileChunkController {
    @Autowired
    private FileChunkMergeService fileChunkMergeService;

    @ApiOperation(value = "验证当前文件块是否已上传(断点续传)")
    @RequestMapping(value = "/upload", method = RequestMethod.GET)
    public Result<FileChunkVo> upload(FileChunk param) {
        Result<FileChunkVo> result = new Result<>();
        result.setResult(fileChunkMergeService.isExit(param));
        result.setSuccess(true);
        return result;
    }

    @ApiOperation(value = "分片上传")
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public Result<FileChunkVo> upload(FileChunk param, HttpServletRequest request) {
        boolean isMultipart = ServletFileUpload.isMultipartContent(request);
        Result result = new Result<>();
        if (!isMultipart) {
            result.setSuccess(false);
            result.setMessage("上传文件未空或类型错误");
        } else {
            try {
                FileChunkVo vo = fileChunkMergeService.uploadByMinio(param);
                result.setSuccess(vo.getFlag());
                result.setResult(vo);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return result;
    }

}
