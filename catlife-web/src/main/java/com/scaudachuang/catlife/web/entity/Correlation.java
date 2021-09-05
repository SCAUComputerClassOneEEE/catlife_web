package com.scaudachuang.catlife.web.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import org.joda.time.DateTime;

/**
 * @author hiluyx
 * @since 2021/8/25 23:03
 **/
@Data
public class Correlation {
    @TableField("nId")
    private Long nId;
    @TableId("beNid")
    private Long beNid;
    @TableField("bfDatetime")
    private DateTime bfDatetime;
    private boolean bf;
}
