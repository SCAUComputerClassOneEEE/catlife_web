package com.scaudachuang.catlife.web.dao;

import com.scaudachuang.catlife.commons.model.TopHotDetection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author hiluyx
 * @since 2021/6/26 17:26
 **/
@Repository
public class RedisDao implements InitializingBean {
    private final Logger logger = LoggerFactory.getLogger(RedisDao.class);

    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Resource
    private CatMapper catMapper;

    public Set<TopHotDetection> getAllTopHotZSet() {
        Set<Object> range = redisTemplate.opsForZSet().range(TopHotDetection.redisZSetKey, 0, -1);
        if (range == null) return null;
        return range.stream()
                .map(topHotDetection -> (TopHotDetection) topHotDetection)
                .collect(Collectors.toSet());
    }

    public List<TopHotDetection> getTopHotZSetN(int n) {
        List<TopHotDetection> allTopHotZSet = new ArrayList<>(getAllTopHotZSet());
        int size = allTopHotZSet.size();
        return allTopHotZSet.subList(size - n >= size ? n : 0, size);
    }

    public void addScore(TopHotDetection val, double score) {
        redisTemplate.opsForZSet().add(TopHotDetection.redisZSetKey, val, score);
    }

    public int rankTopHot(TopHotDetection val) {
        Long rank = redisTemplate.opsForZSet().rank(TopHotDetection.redisZSetKey, val);
        if (rank == null) return -1;
        return rank.intValue();
    }

    /**
     * 把redis中没有的加进去
     */
    @Override
    public void afterPropertiesSet() {
        Set<String> cats = catMapper.catRepos();
        logger.info("初始化redis排行耪，mysql：cats " + cats.size());
        Set<TopHotDetection> allTopHotZSet = getAllTopHotZSet();
        logger.info("初始化redis排行耪，redis：cats " + allTopHotZSet.size());
        cats.removeAll(allTopHotZSet.stream()
                .map(TopHotDetection::getCatClass)
                .collect(Collectors.toSet()));
        logger.info("初始化redis排行耪，差异：cats " + cats);
        cats.forEach(c -> addScore(new TopHotDetection(c), 0));
    }
}
