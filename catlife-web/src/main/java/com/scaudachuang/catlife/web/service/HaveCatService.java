package com.scaudachuang.catlife.web.service;

import com.scaudachuang.catlife.web.dao.CatMapper;
import com.scaudachuang.catlife.web.entity.HaveCat;
import com.scaudachuang.catlife.web.model.SimpleHaveCatInfoBar;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author hiluyx
 * @since 2021/8/25 22:16
 **/
@Service
public class HaveCatService {
    @Resource
    private CatMapper catMapper;

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
}
