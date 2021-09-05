package com.scaudachuang.catlife.web.model.wx;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WxUserDecryptedInfo {
    private String openId;
    private String nickName;
    private int gender;
    private String city;
    private String province;
    private String country;
    private String avatarUrl;
    private String unionId;
    private WaterMark waterMark;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class WaterMark {
        String appId;
        long timeStamp;
    }
}
