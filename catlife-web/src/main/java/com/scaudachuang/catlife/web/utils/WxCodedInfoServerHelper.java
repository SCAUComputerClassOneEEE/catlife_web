package com.scaudachuang.catlife.web.utils;

import com.scaudachuang.catlife.commons.utils.JSONUtil;
import com.scaudachuang.catlife.web.model.wx.*;
import com.scaudachuang.catlife.web.config.WxAppDevInfo;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Base64;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.net.ConnectException;
import java.nio.charset.StandardCharsets;
import java.security.AlgorithmParameters;
import java.security.Security;
import java.util.Arrays;

@Component
public class WxCodedInfoServerHelper {
    @Resource
    private RestTemplate restTemplate;

    @Resource
    private WxAppDevInfo wxAppDevInfo;

    public WxSessionResponse code2Session(String code) throws IOException {
        ResponseEntity<String> forEntity =
                restTemplate.getForEntity(
                        wxAppDevInfo.getUrl(),
                        String.class,
                        wxAppDevInfo.mapParams(code)
                );
        if (forEntity.getStatusCode().isError()) {
            throw new ConnectException("wx server connect fail.");
        }
        String body = forEntity.getBody();
        if (body == null) throw new NullPointerException("wx code2Session response body is null");
        return JSONUtil.readValue(body, WxSessionResponse.class);
    }

    public static WxUserDecryptedInfo decryptUserInfo(
            String encryptedData, String sessionKey, String iv) throws Exception {
        // 被加密的数据
        byte[] dataByte = Base64.decode(encryptedData);
        // 加密秘钥
        byte[] keyByte = Base64.decode(sessionKey);
        // 偏移量
        byte[] ivByte = Base64.decode(iv);
        // 如果密钥不足16位，那么就补足.  这个if 中的内容很重要
        int base = 16;
        if (keyByte.length % base != 0) {
            int groups = keyByte.length / base + 1;
            byte[] temp = new byte[groups * base];
            Arrays.fill(temp, (byte) 0);
            System.arraycopy(keyByte, 0, temp, 0, keyByte.length);
            keyByte = temp;
        }
        // 初始化
        Security.addProvider(new BouncyCastleProvider());
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding","BC");
        SecretKeySpec spec = new SecretKeySpec(keyByte, "AES");
        AlgorithmParameters parameters = AlgorithmParameters.getInstance("AES");
        parameters.init(new IvParameterSpec(ivByte));
        cipher.init(Cipher.DECRYPT_MODE, spec, parameters);// 初始化
        byte[] resultByte = cipher.doFinal(dataByte);
        if (null != resultByte && resultByte.length > 0) {
            String result = new String(resultByte, StandardCharsets.UTF_8);
            return JSONUtil.readValue(result, WxUserDecryptedInfo.class);
        }
        throw new NullPointerException("decryptUserInfo null");
    }

}
