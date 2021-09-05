-- 微信用户授权登录
    -- 如果第一次授权
    select ownerid from catowner where openid = #{openId};

    insert into catowner(openid, sessionkey, nickname, avatar)
    values (#{catOwner.openId},
            #{catOwner.sessionKey},
            #{catOwner.nickname},
            #{catOwner.avatar});
    -- 如果不是第一次授权
    update catowner set nickname = #{nickname}, avatar = #{avatar}
    where ownerid = #{ownerId};

-- 查找用户的所有粉丝
select nId as id, fDatetime as fbDateTime, nickname, avatar
from follow join catowner on follow.nId = catowner.ownerId
where follow.beNId = #{id};

-- 查找用户的所有黑名单
select nId as id, bDatetime as fbDateTime, nickname, avatar
from blacklist join catowner on blacklist.nId = catowner.ownerId
where blacklist.beNId = #{id};

-- 查找备忘录
select * from memorandum where ownerid = #{ownerId} order by redatetime;

-- 查找某个用户所有动态，按时间排序
select * from moments where ownerid = #{ownerId} order by timeofmom;

-- 查找一条动态的所有评论，按时间排序
select * from comments where ownerid = #{ownerId} and timeofmom = #{timeOfMom} order by timeofcom;