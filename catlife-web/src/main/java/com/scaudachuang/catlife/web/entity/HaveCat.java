package com.scaudachuang.catlife.web.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import org.joda.time.DateTime;

@Data
public class HaveCat {
    @TableId
    private int id;
    @TableField
    private Integer ownerId;
    @TableField
    private String catClass;
    private DateTime birthday;
    private String catName;
    private boolean isSterilization;
    private boolean isBear;
    private String drugAllergy;
    private String likes;
}
