package com.scaudachuang.catlife.web.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class CatOwner {
    @TableId
    private Long ownerId;
    private String openId;
    private String sessionKey;
    private String nickName;
    private String avatar;
}
