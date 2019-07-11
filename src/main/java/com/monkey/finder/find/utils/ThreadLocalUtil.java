package com.monkey.finder.find.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: monkey
 * @date:
 * @desc:
 */
public class ThreadLocalUtil {

    private static final ThreadLocal<Map<String, Object>> session = new ThreadLocal<>();


    public static Long getUid(){
        Object o = ThreadLocalUtil.get("uid");
        if(o==null){
            return null;
        }
        return Long.parseLong(o.toString());
    }

    public static void setUid(Long uid){
        ThreadLocalUtil.set("uid", uid+"");
    }


    public static Object get(String key) {
        Map<String, Object> map = session.get();
        if (map != null) {
            return map.get(key);
        } else {
            return null;
        }
    }

    public static void set(String key, Object value) {
        Map<String, Object> map = session.get();
        if (map != null) {
            map.put(key, value);
        } else {
            map = new HashMap<String, Object>();
            map.put(key, value);
            session.set(map);
        }
    }

    public static void clear(){
        Map<String, Object> map = session.get();
        if(map!=null){
            map.clear();
        }
    }
}
