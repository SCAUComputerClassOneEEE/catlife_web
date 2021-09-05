-- 查找用户的猫
    -- 查找用户的所有入档猫（简单信息）
    select catclass, havecatid, catname
    from havecat
    where ownerid = #{ownerId}
    order by havecatid;
    -- 查找用的同一类的所有猫（简单信息）
    select catclass, havecatid, catname
    from havecat
    where ownerid = #{ownerId} and catclass = #{catClass}
    order by havecatid;

-- 查找一个猫的档案（详细信息）
select *
from havecat
where ownerid = #{ownerId} and catclass = #{catClass} and havecatid = #{haveCatId};

-- 查找猫的日记
    -- 该用户的所有猫日记
    select * from catliferecord where ownerid = #{ownerId};
    -- 该用户的同类猫的所有日记
    select * from catliferecord
    where ownerid = #{ownerId} and catclass = #{catClass};
    -- 该用户的同类猫的某一个猫的所有日记
    select * from catliferecord
    where ownerid = #{ownerId} and catclass = #{catClass} and havecatid = #{haveCatId};
