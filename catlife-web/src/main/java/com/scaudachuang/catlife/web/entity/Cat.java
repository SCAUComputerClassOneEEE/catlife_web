package com.scaudachuang.catlife.web.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class Cat {
    @TableId
    private String catClass;
    private String introduction;
    private String headPhoto;
}
