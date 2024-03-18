package ${package.ServiceImpl};

import ${package.Entity}.${entity};
import ${package.Mapper}.${table.mapperName};
import ${package.Service}.${table.serviceName};
import ${package.Entity}.dto.${entity}Dto;
import com.tongtu.cyber.common.api.vo.Result;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
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
    private ExcelOutOrInUtil excelOutOrInUtil;
    @Autowired
    private ${table.mapperName} mapper;

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
    param.setIds(Arrays.asList(id));
    List<${entity}> list = this.getList(param);
    if (CollUtil.isEmpty(list)) {
    return null;
    } else {
    return list.get(0);
    }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveRecord(${entity} entity) {
    return this.mapper.insert(entity) != 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateRecord(${entity} entity) {
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
    public ModelAndView exportExcel(HttpServletResponse response, Integer type, ${entity}Dto param) {
    List<${entity}> list= new ArrayList<>();
    String name = "${table.comment!}";
    if (type == null || type == 0) {
    list.add(new ${entity}());
    } else {
    if (CollUtil.isNotEmpty(param.getIds())) {
    ${entity}Dto par = new ${entity}Dto();
    par.setIds(param.getIds());
    param = par;
    }
    list = this.getList(param);
    }
    return excelOutOrInUtil.exportExcel(response, type, name, list, ${entity}.class);
    }

    @Override
    public Result importExcel(MultipartFile res, HttpServletRequest request, Integer type) {
    if (res == null) {
    return Result.error(430, "上传文件为空!!!");
    }
    if (!(request instanceof MultipartHttpServletRequest)) {
    return Result.error(430, "文件不符合模板要求，请参照上传文件模板进行调整!!!");
    }
    //列表参数定义
    int successLines = 0, errorLines = 0;
    List<String> errorMessageList = new ArrayList<>();
    List<${entity}> successArr = new ArrayList<>();
    List<${entity}> repeatArr = new ArrayList<>();
    //excel导入参数定义
    ImportParams params = new ImportParams();
    params.setTitleRows(0);
    params.setHeadRows(1);
    params.setNeedSave(true);
    //解析列表
    try {
    List<${entity}> list = ExcelImportUtil.importExcel(res.getInputStream(), ${entity}.class, params);
    if (CollUtil.isEmpty(list)) {
    return Result.error("上传文件内容为空!!!");
    }
    //校验列表内容
    for (int i = 0; i < list.size(); i++) {
    ${entity} entity = list.get(i);
    Boolean b = queryList(entity, 1);
    if (b) {
    int lineNumber = i + 1;
    errorMessageList.add("第 " + lineNumber + " 行：" + "数据重复");
    repeatArr.add(entity);
    continue;
    }
    successArr.add(entity);
    }
    //错误行数
    errorLines += errorMessageList.size();
    //正确行数
    successLines += (list.size() - errorLines);
    if (type == null) {
    type = 1;
    }
    if (0 == type) {
    return excelOutOrInUtil.imporExcelError(null,errorLines, successLines, errorMessageList);
    } else if (1 == type) {
    this.saveBatch(successArr);
    return excelOutOrInUtil.imporExcelError(null,errorLines, successLines, errorMessageList);
    } else { List<String> ids = repeatArr.stream().map(${entity}::getId).collect(Collectors.toList());
    this.removeByIds(ids);
    successArr.addAll(repeatArr);
    this.saveBatch(successArr);
    return Result.ok("导入成功,覆盖已存在数据!!!");
    }
    } catch (Exception e) {
    log.error(e.getMessage(), e);
    return Result.error("导入失败:" + e.getMessage());
    } finally {
    try {
    res.getInputStream().close();
    } catch (IOException e) {
    e.printStackTrace();
    }
    }
    }


        /**
        * 添加/修改重复性校验
        * @param entity
        * @return
        */
        private boolean queryList(${entity} entity, int flag) {
        QueryWrapper<${entity}> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(flag == 1 && StrUtil.isNotBlank(entity.getId()), "id", entity.getId())
        .ne(flag == 2 && StrUtil.isNotBlank(entity.getId()), "id", entity.getId())
        .eq(flag == 2 && StrUtil.isNotBlank(entity.getId()), "id", entity.getId());
        return this.baseMapper.selectCount(queryWrapper) > 0;
        }
        }
</#if>