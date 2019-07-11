package com.monkey.finder.find.service;

import org.springframework.data.geo.Circle;
import org.springframework.data.geo.GeoResult;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisGeoCommands;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author: 王志伟
 * @date: 2018/3/8 18:16
 * @desc:
 */
public interface IRedisService {

    void set(String key, Long value, int time, TimeUnit timeUnit);

    void set(String key, String value, int time, TimeUnit timeUnit);

    void set(String key, Long value);

    void set(String key, Object value, int time, TimeUnit timeUnit);

    Long getUid(String token);

    Long getLong(String key);

    Object get(String key);

    String getStr(String key);

    List<Object> multiGet(List<String> keys);

    void delete(String key);

    void increment(String key);

    void increment(String key, long num);

    Integer hashGetInteger(String hKey, String hSubKey);

    Long hashGetLong(String hKey, String hSubKey);

    String hashGet(String hKey, String hSubKey);

    List<Integer> hashMultiGetInteger(String hKey, List<String> hSubKeys);

    List<Long> hashMultiGetLong(String hKey, List<String> hSubKeys);

    List<String> hashMultiGet(String hKey, List<String> hSubKeys);

    Map<String, Long> hashGetAllLong(String hKey);

    Map<String, String> hashGetAll(String hKey);

    boolean hashHasKey(String hKey, String hSubKey);

    void hashPut(String hKey, String hSubKey, String hSubV);

    void hashPutInteger(String hKey, String hSubKey, Integer hSubV);

    void hashPutLong(String hKey, String hSubKey, Long hSubV);

    void hashdelete(String hKey, String hSubKey);

    void hashIncrement(String hKey, String hSubKey, long num);

    void leftPush(String key, String v);

    String rightPop(String key);

    Long listSize(String key);

    void zAdd(String key, String v, double score);

    void zRemove(String key, String v);

    Set<Object> zRangeByScore(String key, double min, double max);

    /**
     * 返回有序集 key 中成员 member 的排名。其中有序集成员按 score 值递增(从小到大)顺序排列。
     * 排名以 0 为底，也就是说， score 值最小的成员排名为 0 。
     * @param key
     * @param member
     * @return
     */
    Long zRank(String key, String member);

    /**
     * 返回有序集 key 的基数
     * @param key
     * @return
     */
    Long zCard(String key);

    Long removeRangeByScore(String key, double min, double max);

    Set<Object> sMembers(String k);

    boolean sIsMember(String k, String o);

    void sAdd(String k, String o);

    void sDelete(String k, String o);

    //set end  -----------------------
    void gAdd(String key, Point point, Long shopId);

    void gDel(String key, Long shopId);

    List<GeoResult<RedisGeoCommands.GeoLocation<Object>>> gRadius(String key, Circle circle);
}
