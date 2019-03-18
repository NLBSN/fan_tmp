package redisWis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class MyJedisPool {

    private final static Logger logger = LoggerFactory.getLogger(MyJedisPool.class);

    private static JedisPool readPool = null;
    private static JedisPool writePool = null;

    //静态代码初始化池配置
    static {
        Properties props = new Properties();
        InputStream in = null;
        try {
            // in = MyJedisPool.class.getResourceAsStream("redis.properties");
            in = new FileInputStream(new File("").getAbsoluteFile() + "/" + "src/test/resources/redis.properties");
            // InputStream in = new ReaderInputStream(new FileReader(new File("").getAbsoluteFile() + "/" + "src/main/resources/redis.properties"), "utf8");
            //创建jedis池配置实例
            JedisPoolConfig config = new JedisPoolConfig();
            props.load(in);
            //设置池配置项值
            config.setMaxTotal(Integer.valueOf(props.getProperty("jedis.pool.maxActive")));
            config.setMaxIdle(Integer.valueOf(props.getProperty("jedis.pool.maxIdle")));
            config.setMaxWaitMillis(Long.valueOf(props.getProperty("jedis.pool.maxWait")));
            config.setTestOnBorrow(Boolean.valueOf(props.getProperty("jedis.pool.testOnBorrow")));
            config.setTestOnReturn(Boolean.valueOf(props.getProperty("jedis.pool.testOnReturn")));

            //根据配置实例化jedis池
            readPool = new JedisPool(config, props.getProperty("redis.ip"), Integer.valueOf(props.getProperty("redis.port")));
            writePool = new JedisPool(config, props.getProperty("redis.ip"), Integer.valueOf(props.getProperty("redis.port")));
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 获得jedis对象
     */
    public static Jedis getReadJedisObject() {
        return readPool.getResource();
    }

    /**
     * 获得jedis对象
     */
    public static Jedis getWriteJedisObject() {
        return writePool.getResource();
    }

    /**
     * 归还jedis对象
     */
    public static void returnJedisOjbect(Jedis jedis) {
        if (jedis != null) {
            jedis.close();
        }
    }


}