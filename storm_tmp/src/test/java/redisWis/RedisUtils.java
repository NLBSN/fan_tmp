package redisWis;

import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class RedisUtils {

    /**
     * 获取hash表中所有key
     *
     * @param name
     * @return
     */
    public static Set<String> getHashAllKey(String name) {
        Jedis jedis = null;
        try {
            jedis = MyJedisPool.getReadJedisObject();
            return jedis.hkeys(name);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            MyJedisPool.returnJedisOjbect(jedis);
        }
        return null;
    }

    public static Map<String, String> getHashAllKeyValue(int numDB, String key) {
        Map<String, String> map;
        Jedis jedis = null;
        try {
            jedis = MyJedisPool.getReadJedisObject();
            jedis.select(numDB);
            return jedis.hgetAll(key);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (jedis != null)
                MyJedisPool.returnJedisOjbect(jedis);
        }
        return null;
    }

    /**
     * 从redis hash表中获取
     *
     * @param hashName
     * @param key
     * @return
     */
    public static String getHashKV(String hashName, String key) {
        Jedis jedis = null;
        try {
            jedis = MyJedisPool.getReadJedisObject();
            return jedis.hget(hashName, key);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            MyJedisPool.returnJedisOjbect(jedis);
        }
        return null;
    }

    /**
     * 删除hash表的键值对
     *
     * @param hashName
     * @param key
     */
    public static Long delHashKV(String hashName, String key) {
        Jedis jedis = null;
        try {
            jedis = MyJedisPool.getWriteJedisObject();
            return jedis.hdel(hashName, key);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            MyJedisPool.returnJedisOjbect(jedis);
        }
        return null;
    }

    /**
     * 存放hash表键值对
     *
     * @param hashName
     * @param key
     * @param value
     */
    public static Long setHashKV(String hashName, String key, String value) {
        Jedis jedis = null;
        try {
            jedis = MyJedisPool.getWriteJedisObject();
            return jedis.hset(hashName, key, value);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            MyJedisPool.returnJedisOjbect(jedis);
        }
        return null;
    }

    /**
     * 删除键值对
     *
     * @param k
     * @return
     */
    public static Long delKV(String k) {
        Jedis jedis = null;
        try {
            jedis = MyJedisPool.getWriteJedisObject();
            return jedis.del(k);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            MyJedisPool.returnJedisOjbect(jedis);
        }
        return null;
    }

    /**
     * 放键值对
     * 永久
     *
     * @param k
     * @param v
     */
    public static String setKV(String k, String v) {
        Jedis jedis = null;
        try {
            jedis = MyJedisPool.getWriteJedisObject();
            return jedis.set(k, v);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            MyJedisPool.returnJedisOjbect(jedis);
        }
        return null;
    }


    /**
     * 放键值对
     *
     * @param k
     * @param v
     */
    public static String setKV(String k, int second, String v) {
        Jedis jedis = null;
        try {
            jedis = MyJedisPool.getWriteJedisObject();
            return jedis.setex(k, second, v);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            MyJedisPool.returnJedisOjbect(jedis);
        }
        return null;
    }

    /**
     * 根据key取value
     *
     * @param k
     * @return
     */
    public static String getKV(String k) {
        Jedis jedis = null;
        try {
            jedis = MyJedisPool.getReadJedisObject();
            return jedis.get(k);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            MyJedisPool.returnJedisOjbect(jedis);
        }
        return null;
    }

    /**
     * 根据key取value
     *
     * @param k
     * @return
     */
    public static String getKV(int num, String k) {
        Jedis jedis = null;
        try {
            jedis = MyJedisPool.getReadJedisObject();
            jedis.select(num);
            return jedis.get(k);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // MyJedisPool.returnJedisOjbect(jedis);
        }
        return null;
    }

    /**
     * @param num
     * @param k
     * @return
     * @description 取到list的所有值
     */
    public static List getListAllValue(int num, String k) {
        Jedis jedis = null;
        try {
            jedis = MyJedisPool.getReadJedisObject();
            jedis.select(num);
            return jedis.lrange(k, 0, jedis.llen(k));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            MyJedisPool.returnJedisOjbect(jedis);
        }
        return null;
    }
}