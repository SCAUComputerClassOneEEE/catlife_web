package com.scaudachuang.catlife.web.service;

import com.scaudachuang.catlife.web.dao.CatMapper;
import com.scaudachuang.catlife.web.dao.CatRecordMapper;
import com.scaudachuang.catlife.web.entity.CatLifeRecord;
import com.scaudachuang.catlife.web.entity.HaveCat;
import com.scaudachuang.catlife.web.model.SimpleHaveCatInfoBar;
import com.scaudachuang.catlife.web.utils.DateUtil;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author hiluyx
 * @since 2021/8/25 22:16
 **/
@Service
public class CatService {
    @Resource
    private CatMapper catMapper;

    @Resource
    private CatRecordMapper catRecordMapper;

    public List<SimpleHaveCatInfoBar> myAllCats_simple(long ownerId, String catClass, int page, int limit) {
        RowBounds rowBounds = new RowBounds(page, limit);
        List<SimpleHaveCatInfoBar> all = catMapper.getAllOwnerSimpleHaveCat(ownerId, catClass, rowBounds);
        return all;
    }

    public HaveCat myOneHaveCat(long ownerId, String catClass, int haveCatId) {
        return catMapper.getOneHaveCat(ownerId, catClass, haveCatId);
    }

    @Transactional
    public boolean newMyCat(HaveCat cat) {
        int insert = catMapper.insert(cat);
        return insert > 0;
    }

    public Map<String, CatLifeRecord> getSomeDateCatRecord(Date end, Date start, long haveCatId) throws ParseException {
        HashMap<String, CatLifeRecord> map = new HashMap<>();
        List<CatLifeRecord> someRecordBetween = catRecordMapper.getSomeRecordBetween(start, end, haveCatId);
        for (CatLifeRecord catLifeRecord : someRecordBetween) {
            Date date = catLifeRecord.getReDateTime();
            String formatDate = DateUtil.formatDate(date);
            map.put(formatDate, catLifeRecord);
        }

        return map;
    }

    @Transactional
    public boolean newCatRecord(CatLifeRecord catLifeRecord) {
        return catRecordMapper.insert(catLifeRecord) > 0;
    }
}
