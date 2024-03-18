package com.tongtu.cyber.modules.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tongtu.cyber.common.api.vo.Result;
import com.tongtu.cyber.modules.domain.GhBaseUnit;
import com.tongtu.cyber.modules.domain.dto.GhBaseUnitDto;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @description: 港航段-基础信息-站信息管理 Service 接口
 * @author: 陈世恩
 * @since: 2024-03-18
 **/
public interface GhBaseUnitService extends IService<GhBaseUnit> {

    IPage<GhBaseUnit> getPageList(GhBaseUnitDto param);

    List<GhBaseUnit> getList(GhBaseUnitDto param);

    GhBaseUnit getItem(String id);

    boolean saveRecord(GhBaseUnit entity);

    boolean updateRecord(GhBaseUnit entity);

    boolean removeRecord(String id);

    boolean removeRecordBatch(List<String> ids);

    Result importExcel(MultipartFile res, HttpServletRequest request, Integer type);

    ModelAndView exportExcel(HttpServletResponse response, Integer type, GhBaseUnitDto param);

}
