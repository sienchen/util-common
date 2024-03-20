/*
package com.tongtu.cyber.util.upload;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import com.tongtu.cyber.common.api.vo.Result;
import com.tongtu.cyber.modules.train.domain.GhTrainMaterial;
import com.tongtu.cyber.modules.train.service.GhTrainMaterialService;
import com.tongtu.cyber.modules.train.upload.domain.GhTrainFileChunk;
import com.tongtu.cyber.modules.train.upload.service.FileService;
import com.tongtu.cyber.modules.train.upload.service.GhTrainFileChunkService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
*/

/**
 * @description: 分块上传记录 Controller 接口
 * @author: 陈世恩
 * @since: 2023-06-12
 **/
/*

@RestController
@Slf4j
@Api(value = "FileUploadController对象", tags = "行业培训-培训材料-大文件分块上传")
@RequestMapping("/fileUpload")
public class FileUploadController {

    @Autowired
    private FileService fileService;
    @Autowired
    private GhTrainFileChunkService fileChunkService;
    @Autowired
    private GhTrainMaterialService localStorageService;

    @ApiOperation(value = "验证当前文件块是否已上传(断点续传)")
    @RequestMapping(value = "/upload", method = RequestMethod.GET)
    public Result<Map<String, Object>> checkUpload(GhTrainFileChunk param) {
        Result result = new Result<>();
        Map<String, Object> data = new HashMap<>(1);
        log.info("文件MD5:" + param.getIdentifier());
        //先查询文件是否已经在系统中存在,则返回
        GhTrainMaterial serviceByMd5 = localStorageService.findByMd5(param.getIdentifier());
        if (ObjUtil.isNotEmpty(serviceByMd5)) {
            data.put("fileId", serviceByMd5.getId());
            data.put("url", serviceByMd5.getStyle() + "/" + serviceByMd5.getPath());
            data.put("uploaded", true);
        } else {
            List<GhTrainFileChunk> list = fileChunkService.findByMd5(param.getIdentifier());
            if (CollUtil.isEmpty(list)) {
                data.put("uploaded", false);
            } else {
                // 处理分片
                int[] uploadedFiles = new int[list.size()];
                int index = 0;
                for (GhTrainFileChunk fileChunkItem : list) {
                    uploadedFiles[index] = fileChunkItem.getChunkNumber();
                    index++;
                }
                data.put("uploadedChunks", uploadedFiles);
            }
        }
        result.setCode(200);
        result.setMessage("上传成功");
        result.setResult(data);
        return result;
    }


    @ApiOperation(value = "分块上传(单个分片上传)")
    @PostMapping("/upload")
    public Result chunkUpload(GhTrainFileChunk param, HttpServletRequest request) {
        boolean isMultipart = ServletFileUpload.isMultipartContent(request);
        if (isMultipart) {
            try {
                //先查询文件是否已经在系统中存在,则返回
                GhTrainMaterial serviceByMd5 = localStorageService.findByMd5(param.getIdentifier());
                if (ObjUtil.isNotEmpty(serviceByMd5)) {
                    Map<String, Object> data = new HashMap<>(1);
                    data.put("fileId", serviceByMd5.getId());
                    data.put("url", serviceByMd5.getStyle() + "/" + serviceByMd5.getPath());
                    data.put("uploaded", true);
                    return Result.ok(data);
                }
                //上传到本地
                //fileService.uploadFileToLocal(param);
                //上传到minio
                String filePath = fileService.uploadFileToMinio(param);
                return Result.OK(filePath);
            } catch (Exception e) {
                log.error("文件上传失败。{}", param.toString(), e);
                return Result.error("上传失败");
            }
        }
        return Result.ok("上传成功");
    }

    //@ApiOperation(value = "根据文件名和md5（下载文件）(适用本地分片上传)")
    @GetMapping(value = "/download/{md5}/{name}")
    public void downloadbyname(HttpServletRequest request, HttpServletResponse response, @PathVariable String name, @PathVariable String md5) throws IOException {
        fileService.downloadByName(name, md5, request, response);
    }


}
*/
