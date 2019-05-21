package consumer.official;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashSet;
import java.util.Properties;
import java.util.Set;

public class MyJedisClusterPool {
    private static JedisCluster cluster = null;

    //静态代码初始化池配置
    static {
        Properties props = new Properties();
        InputStream in = null;
        try {
            // jar包打包之后执行
            in = MyJedisClusterPool.class.getClassLoader().getResourceAsStream("redis.properties");
            // in = new FileInputStream(new File("").getAbsoluteFile() + "/" + "src/main/resources/redis.properties");
            // in = new FileInputStream(new File("").getAbsoluteFile() + "/" + "risk.properties");
            // InputStream in = new ReaderInputStream(new FileReader(new File("").getAbsoluteFile() + "/" + "src/main/resources/redis.properties"), "utf8");
            //创建jedis池配置实例
            JedisPoolConfig config = new JedisPoolConfig();
            // GenericObjectPoolConfig config = new GenericObjectPoolConfig();
            props.load(in);
            //设置池配置项值
            config.setMaxTotal(Integer.valueOf(props.getProperty("jedis.pool.maxActive")));
            config.setMaxIdle(Integer.valueOf(props.getProperty("jedis.pool.maxIdle")));
            config.setMaxWaitMillis(Long.valueOf(props.getProperty("jedis.pool.maxWait")));
            config.setTestOnBorrow(Boolean.valueOf(props.getProperty("jedis.pool.testOnBorrow")));
            config.setTestOnReturn(Boolean.valueOf(props.getProperty("jedis.pool.testOnReturn")));
            config.setTestWhileIdle(true);
            config.setMinEvictableIdleTimeMillis(30000);
            config.setTimeBetweenEvictionRunsMillis(30000);

            Set<HostAndPort> hostAndPort = getHostAndPort(props.getProperty("redis.cluster.ip"));
            // cluster = new JedisCluster(hostAndPort, Integer.valueOf(props.getProperty("redis.timeout")), Integer.valueOf(props.getProperty("redis.timeout")), Integer.valueOf(props.getProperty("jedis.pool.maxAttempts")), props.getProperty("redis.passWord"), config);
            cluster = new JedisCluster(hostAndPort, config);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static Set<HostAndPort> getHostAndPort(String redisCluster) {
        Set<HostAndPort> nodes = new LinkedHashSet<>();
        for (String tmp : redisCluster.split(";", -1)) {
            String[] split = tmp.split(":", -1);
            nodes.add(new HostAndPort(split[0], Integer.parseInt(split[1])));
        }
        return nodes;
    }

    public static JedisCluster getRedisClusterObject() {
        return cluster;
    }

    public static void returnJedisCluterObject(JedisCluster jedisCluster) {
        try {
            if (jedisCluster != null) jedisCluster = null;
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}