package com.scaudachuang.catlife.web.web;

import com.scaudachuang.catlife.commons.model.UserSession;
import com.scaudachuang.catlife.web.entity.HaveCat;
import com.scaudachuang.catlife.commons.model.RtMessage;
import com.scaudachuang.catlife.web.model.CorrelationInfoBar;
import com.scaudachuang.catlife.web.model.SimpleHaveCatInfoBar;
import com.scaudachuang.catlife.web.service.CatOwnerService;
import com.scaudachuang.catlife.web.service.HaveCatService;
import com.scaudachuang.catlife.commons.utils.HttpHelper;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户的私人信息
 * - 粉丝 1
 * - 黑名单 1
 * - 有哪些猫 1
 * - 猫日记
 */
@RestController
@RequestMapping("/self/catLife")
public class CatOwnerSelfInfoController {
    @Resource
    private CatOwnerService catOwnerService;
    @Resource
    private HaveCatService haveCatService;

    /**
     * bf
     * 粉丝 -- 0
     * 黑名单 -- 1
     */
    @GetMapping(value = "/correlationList")
    public RtMessage<Map<String, Object>> getMyCorrelationList(
            @RequestParam(value = "ownerId", required = false) long ownerId,
            @RequestParam("bf") int bf,
            @RequestParam("limit") int limit,
            @RequestParam("page") int page,
            HttpServletRequest request
        ) throws Exception {
        /*
        * ownerId 不提供说明是自己，在缓存中获取
        * */
        if (ownerId == 0) {
            ownerId = HttpHelper.getUserSessionValue(request).getDefineOnlineStatus();
        } else if (ownerId < 0) {
            return RtMessage.ERROR(303, "error ownerId", null);
        }
        List<CorrelationInfoBar> correlationList = catOwnerService.getCorrelationList(page, limit, bf == 1, ownerId);
        HashMap<String, Object> map = new HashMap<>();
        map.put("correlationList", correlationList);
        map.put("nums", correlationList.size());
        return RtMessage.OK(map);
    }

    /**
     * 插入一条关系
     * @param nId 操作的对方
     * @param bf 操作的类型，拉黑/关注 1/0
     */
    @PostMapping(value = "/correlationList")
    public RtMessage<Object> insertMyCorrelationList(@RequestParam("nId") long nId,
                                                     @RequestParam("bf") int bf,
                                                     @RequestParam(value = "beNid", required = false) long beNid,
                                                     HttpServletRequest request) throws Exception {
        UserSession sessionValue = HttpHelper.getUserSessionValue(request);
        long beNid_redis = sessionValue.getDefineOnlineStatus();
        if (beNid <= 0 && beNid_redis <= 0)
            return RtMessage.ERROR(404, "用户错误", null);
        if (beNid_redis > 0)
            beNid = beNid_redis;
        boolean b = catOwnerService.newCorrelation(nId, beNid, bf == 1);
        return RtMessage.INSERT_BOOL(b, "插入结果");
    }

    /**
     * haveCat 简单界面的haveCat
     *
     */
    @GetMapping("/myAllCats")
    public RtMessage<Map<String, Object>> getMyAllCat_simple(
            @RequestParam("ownerId") long ownerId,
            @RequestParam("page") int page,
            @RequestParam("limit") int limit,
            @RequestParam(value = "catClass", required = false) String catClass,
            HttpServletRequest request
        ) throws Exception {
        if (ownerId == 0) {
            ownerId = HttpHelper.getUserSessionValue(request).getDefineOnlineStatus();
        } else if (ownerId < 0) {
            return RtMessage.ERROR(303, "error ownerId", null);
        }
        List<SimpleHaveCatInfoBar> catInfoBars = haveCatService.myAllCats_simple(ownerId, catClass, page, limit);
        HashMap<String, Object> map = new HashMap<>();
        map.put("cats", catInfoBars);
        map.put("nums", catInfoBars.size());
        return RtMessage.OK(map);
    }

    /**
     * 根据上面的接口/myAllCats的结果获取参数
     * @param catClass
     * @param haveCatId
     * @return 一个详细的cat
     */
    @GetMapping("/myOneHaveCat")
    public RtMessage<HaveCat> getMyHaveCat(@RequestParam("catClass") String catClass,
                                           @RequestParam("haveCatId") int haveCatId,
                                           HttpServletRequest request) throws Exception {
        long ownerId = HttpHelper.getUserSessionValue(request).getDefineOnlineStatus();
        if (ownerId <= 0) return RtMessage.ERROR(500, "错误用户缓存", null);

        HaveCat haveCat = haveCatService.myOneHaveCat(ownerId, catClass, haveCatId);
        if (haveCat != null)
            return RtMessage.OK(haveCat);
        else
            return RtMessage.ERROR(303, "no such your cat", null);
    }

    /**
     * 向数据库传输一个新的cat
     * cat.haveCatId 后端生成
     * @param cat 前端包装好这个cat
     */
    @PostMapping("/myOneHaveCat")
    public RtMessage<Object> newHaveCat(@RequestBody HaveCat cat, HttpServletRequest request) {
        boolean b = haveCatService.newMyCat(cat);
        return RtMessage.INSERT_BOOL(b, "插入结果");
    }

}
