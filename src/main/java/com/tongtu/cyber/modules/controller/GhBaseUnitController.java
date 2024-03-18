package com.tongtu.cyber.modules.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tongtu.cyber.common.api.vo.Result;
import com.tongtu.cyber.modules.domain.GhBaseUnit;
import com.tongtu.cyber.modules.domain.dto.GhBaseUnitDto;
import com.tongtu.cyber.modules.service.GhBaseUnitService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

/**
 * @description: 港航段-基础信息-站信息管理 Controller 接口
 * @author: 陈世恩
 * @since: 2024-03-18
 **/
@RestController
@Api(value = "GhBaseUnitController", tags = "港航段-基础信息-站信息管理")
@RequestMapping("/gh-base-unit")
public class GhBaseUnitController {
    @Autowired
    private GhBaseUnitService service;

    @ApiOperation(value = "模板下载")
    @RequestMapping(value = "/exportTemplate", method = RequestMethod.GET)
    public ModelAndView downLoad(HttpServletResponse response) throws Exception {
        return service.exportExcel(response, 0, null);
    }

    @ApiOperation(value = "导入excel")
    @RequestMapping(value = "/importExcel", consumes = "multipart/*", headers = "content-type=multipart/form-data", method = RequestMethod.POST)
    @ApiImplicitParam(name = "type", value = "status导入方式 0:不导入，只校验数据正确性 1:正确行导入，错误行提示 2:导入-不覆盖  3:导入-覆盖 默认：1", defaultValue = "1")
    public Result importExcel(@RequestParam("file") MultipartFile res, HttpServletRequest request, Integer type) {
        return service.importExcel(res, request, type);
    }

    @ApiOperation(value = "导出excel")
    @RequestMapping(value = "/exportExcel", method = RequestMethod.GET)
    public ModelAndView exportExcel(HttpServletResponse response, @ModelAttribute GhBaseUnitDto param) {
        return service.exportExcel(response, 1, param);
    }

    @ApiOperation(value = "分页查询")
    @RequestMapping(value = "/getPageList", method = RequestMethod.GET)
    public Result<IPage<GhBaseUnit>> getPageList(@ModelAttribute GhBaseUnitDto param) {
        Result<IPage<GhBaseUnit>> result = new Result<>();
        result.setResult(service.getPageList(param));
        return result;
    }

    @ApiOperation(value = "全部查询")
    @RequestMapping(value = "/getList", method = RequestMethod.GET)
    public Result<List<GhBaseUnit>> getList(@ModelAttribute GhBaseUnitDto param) {
        Result<List<GhBaseUnit>> result = new Result<>();
        result.setResult(service.getList(param));
        return result;
    }

    @ApiOperation(value = "详情查看")
    @RequestMapping(value = "/getItem", method = RequestMethod.GET)
    public Result<GhBaseUnit> getItem(String id) {
        Result<GhBaseUnit> result = new Result<>();
        result.setResult(service.getItem(id));
        return result;
    }

    @ApiOperation(value = "添加")
    @RequestMapping(value = "/saveRecord", method = RequestMethod.POST)
    public Result saveRecord(@Validated @RequestBody GhBaseUnit entity) {
        Result<String> result = new Result<>();
        Boolean b = service.saveRecord(entity);
        result.setSuccess(b);
        if (b) {
            result.setMessage("添加成功");
        } else {
            result.setCode(251);
            result.setMessage("添加失败,添加的数据已存在");
        }
        return result;
    }

    @ApiOperation(value = "修改")
    @RequestMapping(value = "/updateRecord", method = RequestMethod.POST)
    public Result updateRecord(@Validated @RequestBody GhBaseUnit entity) {
        Result<String> result = new Result<>();
        Boolean b = service.updateRecord(entity);
        result.setSuccess(b);
        if (b) {
            result.setMessage("修改成功");
        } else {
            result.setCode(251);
            result.setMessage("修改失败,修改数据已经存在");
        }
        return result;
    }


    @ApiOperation(value = "删除")
    @RequestMapping(value = "/removeRecord", method = RequestMethod.POST)
    @ApiImplicitParam(name = "id", value = "记录id", paramType = "String", required = true)
    public Result removeRecord(String id) {
        Result<String> result = new Result<>();
        Boolean b = service.removeRecord(id);
        result.setSuccess(b);
        if (b) {
            result.setMessage("删除成功");
        } else {
            result.setMessage("删除失败");
        }
        return result;
    }

    @ApiOperation(value = "批量删除")
    @RequestMapping(value = "/removeRecordBatch", method = RequestMethod.POST)
    @ApiImplicitParam(name = "ids", value = "记录id(多条逗号分隔)", paramType = "String", required = true)
    public Result removeRecordBatch(String ids) {
        Result<String> result = new Result<>();
        Boolean b = service.removeRecordBatch(Arrays.asList(ids));
        result.setSuccess(b);
        if (b) {
            result.setMessage("删除成功");
        } else {
            result.setMessage("删除失败");
        }
        return result;
    }
}