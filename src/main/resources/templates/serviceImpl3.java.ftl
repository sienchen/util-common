package ${package.ServiceImpl};

import ${package.Entity}.${entity};
import ${package.Mapper}.${table.mapperName};
import ${package.Service}.${table.serviceName};
import ${package.Entity}.dto.${entity}Dto;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @description: ${table.comment!} Service 实现接口
 * @author: ${author}
 * @since: ${date}
 **/
@Service
<#if kotlin>
open class ${table.serviceImplName} : ${superServiceImplClass}<${table.mapperName}, ${entity}>(), ${table.serviceName} {
<#else>
public class ${table.serviceImplName} extends ${superServiceImplClass}<${table.mapperName}, ${entity}> implements ${table.serviceName} {

    @Autowired
    private ${table.mapperName} mapper;

    @Autowired
    private EasyExcelOutOrInUtil easyExcelOutOrInUtil;

    @Override
    public IPage<${entity}> getPageList(${entity}Dto param) {
        param.setPageNo(param.getPageNo() == null ? 1 : param.getPageNo());
        param.setPageSize(param.getPageSize() == null ? 10 : param.getPageSize());
        Page<${entity}> page = new Page<>(param.getPageNo(), param.getPageSize());
        return this.mapper.getList(page, param);
    }

    @Override
    public List<${entity}> getList(${entity}Dto param) {
         return this.mapper.getList(param);
    }

    @Override
    public ${entity} getItem(String id) {
        ${entity}Dto param = new ${entity}Dto();
        param.setIds(id);
        List<${entity}> list = this.getList(param);
        if (CollUtil.isEmpty(list)) {
            return null;
        } else {
            //处理附件详情
            ${entity} entity = list.get(0);
            return entity;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveRecord(${entity} entity) {
        if (queryList(entity, 1)) {
            return false;
        }
        return this.mapper.insert(entity) != 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateRecord(${entity} entity) {
        if (queryList(entity, 1)) {
            return false;
        }
        return this.mapper.updateById(entity) != 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeRecord(String id) {
       return this.mapper.deleteById(id)!= 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeRecordBatch(List<String> ids) {
       return this.baseMapper.deleteBatchIds(ids)!= 0;
    }

    @Override
    public void exportExcel(HttpServletResponse response, Integer type, ${entity}Dto param) {
         List<${entity}> list= new ArrayList<>();
        String fileName = "${table.comment!}";
        String sheetName;
        if (type == null || type == 0) {
            sheetName = "模板";
            list.add(new ${entity}());
        } else {
            sheetName = "导出数据";
             if (StrUtil.isNotBlank(param.getIds())) {
                ${entity}Dto par = new ${entity}Dto();
                 par.setIds(param.getIds());
                 param = par;
            }
            list = this.getList(param);
        }
         easyExcelOutOrInUtil.exportExcel(response, fileName, sheetName, list);
    }

    @Override
    public Result importExcel(MultipartFile res, HttpServletRequest request) {
        if (!(request instanceof MultipartHttpServletRequest)) {
            return Result.error(430, "文件不符合模板要求，请参照上传文件模板进行调整!");
        }
        try {
        List<${entity}> list = easyExcelOutOrInUtil.importExcel(res, ${entity}.class);
        if (CollUtil.isNotEmpty(list)) {
            int successLines = 0, errorLines = 0;
            //错误信息
            List<String> errorMessageList = new ArrayList<>();
            //正确数据
            List<${entity}> successArr = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                ${entity} entity = list.get(i);
                Boolean b = queryList(entity, 1);
                if (b) {
                    int lineNumber = i + 1;
                    errorMessageList.add("第 " + lineNumber + " 行：" + "数据重复");
                    continue;
                  }
                   successArr.add(entity);
             }
             errorLines += errorMessageList.size();
             successLines += (list.size() - errorLines);
             this.saveBatch(successArr);
             return ExcelOutOrInUtil.imporReturnRes(errorLines, successLines, errorMessageList);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.error("文件导入失败:" + e.getMessage());
        }
        return Result.ok("导入成功!");
    }

    /**
     * 添加/修改重复性校验
     *
     * @param entity
     * @return
     */
    private boolean queryList(${entity} entity, int flag) {
        QueryWrapper<${entity}> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(flag == 1 && StrUtil.isNotBlank(entity.getName()), "name", entity.getName())
        .ne(flag == 2 && StrUtil.isNotBlank(entity.getId()), "id", entity.getId())
        .eq(flag == 2 && StrUtil.isNotBlank(entity.getName()), "name", entity.getName());
        return this.baseMapper.selectCount(queryWrapper) > 0;
   }

}
</#if>