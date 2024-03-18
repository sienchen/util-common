package com.tongtu.cyber.modules.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tongtu.cyber.modules.domain.GhBaseUnit;
import com.tongtu.cyber.modules.domain.dto.GhBaseUnitDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @description: 港航段-基础信息-站信息管理 Mapper 接口
 * @author: 陈世恩
 * @since: 2024-03-18
 **/
public interface GhBaseUnitMapper extends BaseMapper<GhBaseUnit> {

    IPage<GhBaseUnit> getList(@Param("page") Page<GhBaseUnit> page, @Param("param") GhBaseUnitDto param);

    List<GhBaseUnit> getList(@Param("param") GhBaseUnitDto param);
}
