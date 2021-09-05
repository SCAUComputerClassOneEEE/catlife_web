package com.scaudachuang.catlife.web.config;

import com.scaudachuang.catlife.commons.model.UserSession;
import com.scaudachuang.catlife.commons.utils.HttpHelper;
import com.scaudachuang.catlife.web.dao.RedisDao;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author hiluyx
 * @since 2021/9/1 11:20
 **/
public class SelfOnlineInterceptor implements HandlerInterceptor {

    @Resource
    private RedisDao redisDao;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 获得session
        UserSession sessionValue = HttpHelper.getUserSessionValue(request);
        if (sessionValue.getDefineOnlineStatus() <= 0) { // 用户没有登录
            HttpHelper.errMsgResponse(response, 404, "尚未登录", null);
            return false;
        }

        /*
         * 这里没必要再查mysql验证 DefineOnlineStatus 是否正确？
         */
        return true;
    }
}
