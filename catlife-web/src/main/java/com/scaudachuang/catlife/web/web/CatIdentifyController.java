package com.scaudachuang.catlife.web.web;


import com.scaudachuang.catlife.commons.model.RtMessage;
import com.scaudachuang.catlife.commons.model.TopHotDetection;
import com.scaudachuang.catlife.commons.model.UserSession;
import com.scaudachuang.catlife.commons.utils.HttpHelper;
import com.scaudachuang.catlife.commons.utils.LimitProcessing;
import com.scaudachuang.catlife.web.dao.RedisDao;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @author hiluyx
 * @since 2021/8/26 21:50
 **/
@RestController
@RequestMapping("/public")
public class CatIdentifyController {
    @Resource
    private RedisDao redisDao;

    @GetMapping("/topIdentity")
    @LimitProcessing(name = "topHot", ratePerSec = 1)
    public RtMessage<Map<String, Object>> getTopDetect(@RequestParam(value = "top", required = false) int top) {
        if (top <= 0)
            top = 10;
        List<TopHotDetection> topHotZSetN = redisDao.getTopHotZSetN(top);
        HashMap<String, Object> map = new HashMap<>();
        map.put("tops", topHotZSetN);
        map.put("nums", topHotZSetN.size());
        return RtMessage.OK(map);
    }

    @GetMapping("/detectClass")
    public RtMessage<String> getDetectClass(HttpServletRequest request) throws Exception {
        UserSession userSessionValue = HttpHelper.getUserSessionValue(request);
        return RtMessage.OK(userSessionValue.getNowTaskUUID());
    }
}
