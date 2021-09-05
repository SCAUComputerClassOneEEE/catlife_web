package com.scaudachuang.catlife.web.service;

import com.scaudachuang.catlife.web.dao.CatOwnerMapper;
import com.scaudachuang.catlife.web.dao.CorrelationMapper;
import com.scaudachuang.catlife.web.entity.CatOwner;
import com.scaudachuang.catlife.web.entity.Correlation;
import com.scaudachuang.catlife.web.model.CorrelationInfoBar;
import com.scaudachuang.catlife.web.model.wx.WxUserDecryptedInfo;
import org.apache.ibatis.session.RowBounds;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CatOwnerService {
    @Resource
    private CatOwnerMapper catOwnerMapper;
    @Resource
    private CorrelationMapper correlationMapper;

    /**
     * 存在则更新获取
     * 不存在就插入
     * @param openId 微信的用户id
     * @param wxUserDecryptedInfo 微信的用户信息
     * @return 用户再服务器的表数据
     */
    public CatOwner existThenGetOtherwiseInsert(String openId,
                                                WxUserDecryptedInfo wxUserDecryptedInfo,
                                                String sessionKey) {
        CatOwner catOwner = new CatOwner();
        catOwner.setOpenId(openId);
        catOwner.setAvatar(wxUserDecryptedInfo.getAvatarUrl());
        catOwner.setNickname(wxUserDecryptedInfo.getNickName());
        catOwner.setSessionKey(sessionKey);
        int i = catOwnerMapper.replaceInsertOwner(catOwner);
        if (i <= 0)
            return null;
        return catOwnerMapper.getByOpenId(openId);
    }

    public CatOwner getMyselfInfo(long ownerId) {
        return catOwnerMapper.getSelf(ownerId);
    }

    public List<CorrelationInfoBar> getCorrelationList(int page, int limit, boolean bf, long ownerId) {
        RowBounds rowBounds = new RowBounds(page, limit);
        List<CorrelationInfoBar> userCorrelationInfoBar = correlationMapper.getUserCorrelationInfoBar(ownerId, bf, rowBounds);

        return userCorrelationInfoBar;
    }

    public boolean newCorrelation(long nId, long beNid, boolean bf) {
        /*
        * 严谨来说，nId需要检测是否存在
        * */
        DateTime dateTime = new DateTime();
        Correlation correlation = new Correlation();
        correlation.setBf(bf);
        correlation.setBeNid(beNid);
        correlation.setNId(nId);
        correlation.setBfDatetime(dateTime);

        /* 已存在 */
        Correlation dd = correlationMapper.checkIfPresent(nId, beNid);
        if (dd != null) {
            if (dd.isBf() != bf)
                /* 更新关系 */
                return correlationMapper.updateCorr(correlation) > 0;
            else return true;
        }

        int i = correlationMapper.insert(correlation);
        return i > 0;
    }
}
