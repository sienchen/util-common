package ${package.Controller};

import ${package.Entity}.${entity};
import ${package.Service}.${table.serviceName};
import ${package.Entity}.dto.${entity}Dto;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import com.tongtu.cyber.common.api.vo.Result;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @description: ${table.comment!} Controller 接口
 * @author: ${author}
 * @since: ${date}
 **/
<#if restControllerStyle>
@RestController
<#else>
@Controller
</#if>
<#if swagger2>
@Api(value="${table.controllerName}", tags = "${table.comment!}")
</#if>
@RequestMapping("/<#if controllerMappingHyphenStyle??>${controllerMappingHyphen}<#else>${table.entityPath}</#if>")
<#if kotlin>
class ${table.controllerName}<#if superControllerClass??> : ${superControllerClass}()</#if>
<#else>
<#if superControllerClass??>
public class ${table.controllerName} extends ${superControllerClass} {
<#else>
public class ${table.controllerName} {
</#if>
    @Autowired
    private ${table.serviceName} service;

    @ApiOperation(value = "模板下载")
    @RequestMapping(value = "/exportTemplate", method = RequestMethod.GET)
    public void downLoad(HttpServletResponse response) throws Exception {
         service.exportExcel(response, 0, null);
    }

    @ApiOperation(value = "导入excel")
    @RequestMapping(value = "/importExcel",consumes = "multipart/*", headers = "content-type=multipart/form-data", method = RequestMethod.POST)
    public Result importExcel(@RequestParam("file") MultipartFile res, HttpServletRequest request) throws Exception {
        return service.importExcel(res, request);
    }

    @ApiOperation(value = "导出excel")
    @RequestMapping(value = "/exportExcel", method = RequestMethod.GET)
    public void exportExcel(HttpServletResponse response, @Validated @QueryBody ${entity}Dto param) throws IOException {
         service.exportExcel(response, 1, param);
    }


    @ApiOperation(value = "分页查询")
    @RequestMapping(value = "/getPageList", method = RequestMethod.GET)
    public Result<IPage<${entity}>> getPageList(@Validated @QueryBody ${entity}Dto param) {
        Result<IPage<${entity}>> result = new Result<>();
        result.setResult(service.getPageList(param));
        return result;
    }

    @ApiOperation(value = "全部查询")
    @RequestMapping(value = "/getList", method = RequestMethod.GET)
    public Result<List<${entity}>> getList(@Validated @QueryBody ${entity}Dto param) {
        Result<List<${entity}>> result = new Result<>();
        result.setResult(service.getList(param));
        return result;
    }

    @ApiOperation(value = "详情查看")
    @RequestMapping(value = "/getItem", method = RequestMethod.GET)
    public Result<${entity}> getItem(@Validated @QueryBody CommonIdParam param) {
        Result<${entity}> result = new Result<>();
        result.setResult(service.getItem(param.getId()));
        return result;
    }

    @ApiOperation(value = "添加")
    @RequestMapping(value = "/saveRecord", method = RequestMethod.POST)
    public Result saveRecord(@Validated @RequestBody ${entity} entity) {
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
    public Result updateRecord(@Validated @RequestBody ${entity} entity) {
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
    @ApiImplicitParam(name = "ids", value = "记录ids(逗号拼接)", paramType = "String", required = true)
    public Result removeRecordBatch(String ids) {
        Result<String> result = new Result<>();
        Boolean b = service.removeRecordBatch(Arrays.asList(ids.split(",")));
        result.setSuccess(b);
        if (b) {
            result.setMessage("删除成功");
        } else {
            result.setMessage("删除失败");
        }
            return result;
   }
}
</#if>