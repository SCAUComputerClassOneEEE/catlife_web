package com.scaudachuang.catlife.web.dao;

import com.scaudachuang.catlife.web.entity.CatOwner;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @author hiluyx
 * @since 2021/7/11 21:32
 **/
@Repository
public interface CatOwnerMapper {

    CatOwner getByOpenId(@Param("openId") String openId);

    CatOwner getSelf(@Param("ownerId") long ownerId);

    int replaceInsertOwner(@Param("catOwner") CatOwner catOwner);
}
