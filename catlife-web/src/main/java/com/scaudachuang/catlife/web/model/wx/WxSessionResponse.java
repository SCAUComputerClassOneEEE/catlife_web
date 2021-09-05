package com.scaudachuang.catlife.web.model.wx;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WxSessionResponse {
    private String openId;
    private String sessionKey;
    private String unionId;
    private int errCode;
    private String errMsg;
}
