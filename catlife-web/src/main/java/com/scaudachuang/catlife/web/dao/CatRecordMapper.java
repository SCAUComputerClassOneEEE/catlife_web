package com.scaudachuang.catlife.web.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.scaudachuang.catlife.web.entity.CatLifeRecord;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @author hiluyx
 * @since 2021/10/20 16:54
 **/
@Repository
public interface CatRecordMapper extends BaseMapper<CatLifeRecord> {
    List<CatLifeRecord> getSomeRecordBetween(
            @Param("start") Date start,
            @Param("end") Date end,
            @Param("haveCatId") long haveCatId
    );
}
