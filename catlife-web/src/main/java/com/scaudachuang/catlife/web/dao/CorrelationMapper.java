package com.scaudachuang.catlife.web.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.scaudachuang.catlife.web.entity.Correlation;
import com.scaudachuang.catlife.web.model.CorrelationInfoBar;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

/**
 * @author hiluyx
 * @since 2021/8/25 23:00
 **/
public interface CorrelationMapper extends BaseMapper<Correlation> {

    /*分页，通过id查找自己的粉丝*/
    List<CorrelationInfoBar> getUserCorrelationInfoBar(
            @Param("id") long id, @Param("bf") boolean bf,
            RowBounds rowBounds);

    Correlation checkIfPresent(@Param("nid") long nid, @Param("beId") long beId);

    int updateCorr(@Param("corr") Correlation corr);
}
