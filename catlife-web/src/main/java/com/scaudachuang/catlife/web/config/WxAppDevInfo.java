package com.scaudachuang.catlife.web.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@ConfigurationProperties(prefix = "wx.info")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WxAppDevInfo {
    private String url;

    private String appId;

    private String secret;

    private String grantType;

    public Map<String, String> mapParams(String code) {
        HashMap<String, String> map = new HashMap<>();
        if (code == null || "".equals(code) || appId == null || secret == null)
            throw new NullPointerException("mapParams cannot be null");
        map.put("appid", appId);
        map.put("secret", secret);
        map.put("js_code", code);
        if (grantType == null || "".equals(grantType))
            grantType = "authorization_code";
        map.put("grant_type", grantType);
        return map;
    }
}
