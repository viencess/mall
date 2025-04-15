package com.hmall.item.service;

import com.alibaba.fastjson.JSON;
import com.hmall.item.domain.dto.ItemDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ItemRedisService {
    public static final String REDIS_KEY_ITEM_LIST_CACHE = "item:list";
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public void saveItemListCache(List<ItemDTO> itemDTOList) {
        try {
            stringRedisTemplate.opsForList().leftPushAll(
                    REDIS_KEY_ITEM_LIST_CACHE,
                    itemDTOList.stream().map(JSON::toJSONString).collect(Collectors.toList()));
            stringRedisTemplate.expire(REDIS_KEY_ITEM_LIST_CACHE, 5, TimeUnit.MINUTES);
        } catch (Exception e) {
            log.error("Error save Item list to Redis", e);
        }
    }

    public List<ItemDTO> getItemListCache(Collection<Long> ids) {
        try {
            List<String> strings = stringRedisTemplate.opsForList().range(REDIS_KEY_ITEM_LIST_CACHE, 0, -1);
            if (ObjectUtils.isEmpty(strings)) {
                return null;
            }
            log.info("Get item list from Redis, size:{}", strings.size());
            return strings.stream().map(t -> JSON.parseObject(t, ItemDTO.class))
                    .filter(t -> ids.contains(t.getId()))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error retrieving item list from Redis", e);
            return null;
        }
    }

    public void removeItemListCache() {
        try {
            stringRedisTemplate.delete(REDIS_KEY_ITEM_LIST_CACHE);
        } catch (Exception e) {
            log.error("Error delete item list from Redis", e);
        }
    }


}
