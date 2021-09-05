package com.scaudachuang.catlife.web.web;

import com.scaudachuang.catlife.commons.model.UserSession;
import com.scaudachuang.catlife.web.dao.RedisDao;
import com.scaudachuang.catlife.web.entity.CatOwner;
import com.scaudachuang.catlife.commons.model.RtMessage;
import com.scaudachuang.catlife.web.model.wx.LoginParams;
import com.scaudachuang.catlife.web.model.wx.WxSessionResponse;
import com.scaudachuang.catlife.web.model.wx.WxUserDecryptedInfo;
import com.scaudachuang.catlife.web.service.CatOwnerService;
import com.scaudachuang.catlife.commons.utils.HttpHelper;
import com.scaudachuang.catlife.web.utils.WxCodedInfoServerHelper;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.net.ConnectException;

/**
 * @author hiluyx
 * @since 2021/7/11 21:12
 **/
@RestController
@RequestMapping("/self")
public class LoginController {

    @Resource
    private WxCodedInfoServerHelper wxCodedInfoServerHelper;

    @Resource
    private CatOwnerService catOwnerService;

    @Resource
    private RedisDao redisDao;

    @GetMapping("/myselfInfo")
    public RtMessage<CatOwner> myself(HttpServletRequest request) throws Exception {
        UserSession userSessionValue = HttpHelper.getUserSessionValue(request);
        if (userSessionValue.getDefineOnlineStatus() <= 0)
            return RtMessage.ERROR(404, "尚未登录", null);
        return RtMessage.OK(catOwnerService.getMyselfInfo(userSessionValue.getDefineOnlineStatus()));
    }

    @PostMapping(path = "/login")
    public RtMessage<CatOwner> wxLogin(@RequestBody LoginParams params,
                                       HttpServletRequest request) throws ConnectException {
        /* 登录参数 */
        final String code = params.getCode();
        final String encryptedData = params.getEncryptedData();
        final String iv = params.getIv();
        /* 与wx server建立session */
        WxSessionResponse wxSessionResponse = wxCodedInfoServerHelper.code2Session(code);
        if (wxSessionResponse.getErrCode() != 0) {
            return RtMessage.ERROR(wxSessionResponse.getErrCode(), wxSessionResponse.getErrMsg(), null);
        }
        /* ******************** */

        String openId = wxSessionResponse.getOpenId();
        String sessionKey = wxSessionResponse.getSessionKey();
        try {
            /* 解压用户信息 */
            WxUserDecryptedInfo wxUserDecryptedInfo = WxCodedInfoServerHelper.decryptUserInfo(encryptedData, sessionKey, iv);
            /* 获取或覆盖数据库用户数据 */
            CatOwner catOwner = catOwnerService.existThenGetOtherwiseInsert(openId, wxUserDecryptedInfo, sessionKey);

            if (catOwner == null)
                return RtMessage.ERROR(500, "login fail", null);

            HttpHelper.setUserSessionId(request, catOwner.getOwnerId());
            return RtMessage.OK(catOwner);
        } catch (Exception e) {
            return RtMessage.ERROR(500, "decrypt uer info fail", null);
        }
    }
}
