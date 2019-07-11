package com.monkey.finder.find.service.Imp;


import com.monkey.finder.find.service.IRedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.geo.*;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author: 王志伟
 * @date: 2018/3/8 18:16
 * @desc:
 */
@Slf4j
@Service
public class RedisService implements IRedisService {
    @Resource
    private RedisTemplate<String,Object> redisTemplate;

    @Override
    public void set(String key, Long value, int time, TimeUnit timeUnit) {
        this.set(key,value+"",time,timeUnit);
    }

    @Override
    public void set(String key, String value, int time, TimeUnit timeUnit){
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(key,value, (long)time, timeUnit);
    }

    @Override
    public void set(String key, Long value){
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(key,value);
    }


    @Override
    public void set(String key, Object value, int time, TimeUnit timeUnit){
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(key,value, (long)time, timeUnit);
    }


    @Override
    public Long getUid(String key){
        return getLong(key);
    }

    @Override
    public Long getLong(String key){
        Long l = null;
        try{
            Object obj = this.get(key);
            if(obj==null){
                log.info("RedisService getLong key:{} is null", key);
                return l;
            }
            if (obj instanceof Long) {
                l = (Long)obj;
            } else if (obj instanceof Integer) {
                l = Long.parseLong(""+obj);
            } else {
                l = Long.parseLong((String) obj);
            }
        } catch (Exception e){
            log.error("RedisService getLong()", e);
        }
        return l;
    }

    @Override
    public Object get(String key){
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        return valueOperations.get(key);
    }

    @Override
    public String getStr(String key){
        String value = (String) this.get(key);
        return value;
    }

    @Override
    public List<Object> multiGet(List<String> keys){
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        return valueOperations.multiGet(keys);
    }

    @Override
    public void delete(String key){
        redisTemplate.delete(key);
    }

    @Override
    public void increment(String key){
        redisTemplate.opsForValue().increment(key, 1L);
    }

    @Override
    public void increment(String key, long num){
        redisTemplate.opsForValue().increment(key, num);
    }
    //hash begin-----------------------

    @Override
    public Integer hashGetInteger(String hKey, String hSubKey){
        HashOperations<String, String, Integer> operations = redisTemplate.opsForHash();
        return operations.get(hKey, hSubKey);
    }

    @Override
    public Long hashGetLong(String hKey, String hSubKey){
        HashOperations<String, String, Long> operations = redisTemplate.opsForHash();
        return operations.get(hKey, hSubKey);
    }

    @Override
    public String hashGet(String hKey, String hSubKey){
        HashOperations<String, String, String> operations = redisTemplate.opsForHash();
        return operations.get(hKey, hSubKey);
    }

    @Override
    public List<Integer> hashMultiGetInteger(String hKey, List<String> hSubKeys){
        HashOperations<String, String, Integer> operations = redisTemplate.opsForHash();
        return operations.multiGet(hKey, hSubKeys);
    }

    @Override
    public List<Long> hashMultiGetLong(String hKey, List<String> hSubKeys){
        HashOperations<String, String, Long> operations = redisTemplate.opsForHash();
        return operations.multiGet(hKey, hSubKeys);
    }

    @Override
    public List<String> hashMultiGet(String hKey, List<String> hSubKeys){
        HashOperations<String, String, String> operations = redisTemplate.opsForHash();
        return operations.multiGet(hKey, hSubKeys);
    }

    @Override
    public Map<String, Long> hashGetAllLong(String hKey){
        HashOperations<String, String, Long> operations = redisTemplate.opsForHash();
        return operations.entries(hKey);
    }

    @Override
    public Map<String, String> hashGetAll(String hKey){
        HashOperations<String, String, String> operations = redisTemplate.opsForHash();
        return operations.entries(hKey);
    }

    @Override
    public boolean hashHasKey(String hKey, String hSubKey) {
        return redisTemplate.opsForHash().hasKey(hKey, hSubKey);
    }

    @Override
    public void hashPut(String hKey, String hSubKey, String hSubV) {
        redisTemplate.opsForHash().put(hKey, hSubKey, hSubV);
    }

    @Override
    public void hashPutInteger(String hKey, String hSubKey, Integer hSubV) {
        HashOperations<String, String, Integer> operations = redisTemplate.opsForHash();
        operations.put(hKey, hSubKey, hSubV);
    }

    @Override
    public void hashPutLong(String hKey, String hSubKey, Long hSubV) {
        HashOperations<String, String, Long> operations = redisTemplate.opsForHash();
        operations.put(hKey, hSubKey, hSubV);
    }

    @Override
    public void hashdelete(String hKey, String hSubKey) {
        redisTemplate.opsForHash().delete(hKey, hSubKey);
    }

    @Override
    public void hashIncrement(String hKey, String hSubKey, long num) {
        redisTemplate.opsForHash().increment(hKey, hSubKey, num);
    }

    //hash end  -----------------------


    //list begin-----------------------

    /**
     *
     * @param key
     * @param v
     */
    @Override
    public void leftPush(String key, String v){
        redisTemplate.opsForList().leftPush(key, v);
    }

    /**
     *
     * @param key
     * @return
     */
    @Override
    public String rightPop(String key){
        return (String)redisTemplate.opsForList().rightPop(key);
    }

    /**
     *
     * @param key
     * @return
     */
    @Override
    public Long listSize(String key){
        return redisTemplate.opsForList().size(key);
    }
    //list end  -----------------------

    //zset begin-----------------------

    /**
     *
     * @param key
     * @param v
     * @param score
     */
    @Override
    public void zAdd(String key, String v, double score){
        redisTemplate.opsForZSet().add(key, v, score);
    }
    @Override
    public void zRemove(String key, String v){
        redisTemplate.opsForZSet().remove(key, v);
    }
    @Override
    public Set<Object> zRangeByScore(String key, double min, double max){
        return redisTemplate.opsForZSet().rangeByScore(key, min, max);
    }
    @Override
    public Long zRank(String key, String member){
        return redisTemplate.opsForZSet().rank(key, member);
    }

    @Override
    public Long zCard(String key){
        return redisTemplate.opsForZSet().zCard(key);
    }

    /**
     * 移除有序集 key 中，所有 score 值介于 min 和 max 之间(包括等于 min 或 max )的成员
     * @param key
     * @param min
     * @param max
     * @return
     */
    @Override
    public Long removeRangeByScore(String key, double min, double max){
        return redisTemplate.opsForZSet().removeRangeByScore(key, min, max);
    }
    //zset end  -----------------------

    //set begin-----------------------

    @Override
    public Set<Object> sMembers(String k){
        return redisTemplate.opsForSet().members(k);
    }

    @Override
    public boolean sIsMember(String k, String o) {
        return redisTemplate.opsForSet().isMember(k, o);
    }

    @Override
    public void sAdd(String k, String o) {
        redisTemplate.opsForSet().add(k, o);
    }

    @Override
    public void sDelete(String k, String o) {
        redisTemplate.opsForSet().remove(k, o);
    }

    //set end  -----------------------

    /**
     * geo 添加位置
     * @param key
     * @param point
     * @param shopId
     */
    @Override
    public void gAdd(String key, Point point, Long shopId){
        redisTemplate.opsForGeo().add(key,point,shopId);
    }

    /**
     * geo 删除位置
     * @param key
     * @param shopId
     */
    @Override
    public void gDel(String key, Long shopId){
        redisTemplate.opsForGeo().remove(key,shopId);
    }
    /**
     * geo 查看位置
     * @param key
     * @param shopId
     */
    public List<Point> gGet(String key, Long shopId){
        return redisTemplate.opsForGeo().position(key,shopId);
    }
    /**
     * geo 返回俩点位置
     * @param key
     * @param shopId
     * @return 返回距离 M
     */
    public Double gDist(String key, Long shopId, Long userId){
        Distance distance = redisTemplate.opsForGeo().distance(key,shopId,userId, RedisGeoCommands.DistanceUnit.METERS);
        return distance.getValue();
    }

    /**
     * 获取当前位置为圆心的店铺信息
     * @param key
     * @param circle
     */
    @Override
    public List<GeoResult<RedisGeoCommands.GeoLocation<Object>>> gRadius(String key, Circle circle){
        RedisGeoCommands.GeoRadiusCommandArgs args = RedisGeoCommands.GeoRadiusCommandArgs
                                                     .newGeoRadiusArgs()
                                                     .includeDistance()
                                                     .sortAscending();
        GeoResults<RedisGeoCommands.GeoLocation<Object>> results = redisTemplate.opsForGeo().radius(key,circle,args);
        List<GeoResult<RedisGeoCommands.GeoLocation<Object>>> list = results.getContent();
        return list;
    }


}
