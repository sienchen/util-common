package ${package.Service};

import ${package.Entity}.${entity};
import ${package.Entity}.dto.${entity}Dto;
import com.tongtu.cyber.common.api.vo.Result;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
/**
 * @description: ${table.comment!} Service 接口
 * @author: ${author}
 * @since: ${date}
 **/
<#if kotlin>
interface ${table.serviceName} : ${superServiceClass}<${entity}>
<#else>
public interface ${table.serviceName} extends ${superServiceClass}<${entity}> {

    IPage<${entity}> getPageList(${entity}Dto param);

    List<${entity}> getList(${entity}Dto param);

    ${entity} getItem(String id);

    boolean saveRecord(${entity} entity);

    boolean updateRecord(${entity} entity);

    boolean removeRecord(String id);

    boolean removeRecordBatch(List<String> ids);

    Result importExcel(MultipartFile res, HttpServletRequest request);

    void exportExcel(HttpServletResponse response, Integer type, ${entity}Dto param);

}
</#if>