package testData;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang.StringUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.storm.utils.Utils;
import org.junit.Test;
import redis.clients.jedis.Jedis;
import redisWis.MyJedisPool;

import java.io.*;
import java.util.*;

/**
 * @Description: traffic_fan  将经纬度进行入库
 * @author: fan
 * @Date: Created in 2018/12/5 10:13
 * @Modified By:
 */
public class RedisUtiles {

    @Test
    public void te111st() {
        Properties props = new Properties();
        props.put("bootstrap.servers", "10.14.83.150:9092");
        props.put("group.id", "ruiku");
        props.put("enable.auto.commit", "true");
        props.put("auto.commit.interval.ms", "1000");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Arrays.asList("gld-link"));
        int i = 0;
        Jedis jedis = new Jedis("10.14.83.150", 6379);
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(100);
            for (ConsumerRecord<String, String> record : records) {
                jedis.hset(record.key().substring(0, record.key().length() - 2), "00", record.value());
            }
        }
    }

    /**
     * @descrption 将经纬度表导入redis数据库
     */
    @Test
    public void inputDBLatlon() {
        BufferedReader bufferedReader = null;
        // Jedis jedis = MyJedisPool.getWriteJedisObject();
        Jedis jedis = new Jedis("10.14.83.150", 6379);
        jedis.select(3);
        Map mapTxt = new HashMap();
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(new File("D:/data/insert_road6.csv")), "utf8"));
            String string;
            while ((string = bufferedReader.readLine()) != null) {
                String[] strings = string.split(",", -1);
                mapTxt.put(strings[1], new String[]{strings[2], strings[3]});
            }
            jedis.set("latlon", JSON.toJSONString(mapTxt));
            System.out.println("----------------");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (bufferedReader != null)
                    bufferedReader.close();
                MyJedisPool.returnJedisOjbect(jedis);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @description pan码入库
     */
    @Test
    public void inputDBPAC() {
        BufferedReader bufferedReader = null;
        // Jedis jedis = MyJedisPool.getWriteJedisObject();
        Jedis jedis = new Jedis("10.14.83.150", 6379);
        jedis.select(3);
        Map mapPAC = new HashMap();
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(new File("D:/data/insert_road6.csv")), "utf8"));
            String string;
            while ((string = bufferedReader.readLine()) != null) {
                String[] strings = string.split(",", -1);
                mapPAC.put(strings[1], strings[9]);
            }
            jedis.hmset("pac", mapPAC);
            System.out.println("--------over--------");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (bufferedReader != null)
                    bufferedReader.close();
                MyJedisPool.returnJedisOjbect(jedis);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @description 指标卡值入库
     */
    @Test
    public void inputKaZhi() {
        BufferedReader bufferedReader = null;
        // Jedis jedis = MyJedisPool.getWriteJedisObject();
        Jedis jedis = new Jedis("10.14.83.150", 6379);
        jedis.select(3);
        Map mapKaZhi = new HashMap();
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(new File("D:/data/kazhi.csv")), "utf8"));
            String string;
            while ((string = bufferedReader.readLine()) != null) {
                String[] strings = string.split(",", -1);
                mapKaZhi.put(strings[0], strings[1]);
            }
            jedis.hmset("riskKaZhi", mapKaZhi);
            System.out.println("--------over--------");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (bufferedReader != null)
                    bufferedReader.close();
                MyJedisPool.returnJedisOjbect(jedis);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @description 过滤数据的入库
     */
    @Test
    public void inputFilter() {
        BufferedReader bufferedReader = null;
        // Jedis jedis = MyJedisPool.getWriteJedisObject();
        Jedis jedis = new Jedis("10.14.83.150", 6379);
        jedis.select(3);

        List list = new LinkedList();
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(new File("D:/data/5kmpoint_fan.txt")), "utf8"));
            String string;
            while ((string = bufferedReader.readLine()) != null) {
                if (StringUtils.isNotBlank(string)) {
                    jedis.lpush("filter", string);
                }
            }
            System.out.println("--------over--------");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (bufferedReader != null)
                    bufferedReader.close();
                MyJedisPool.returnJedisOjbect(jedis);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @descripion 经纬度对应桩点入库
     */
    @Test
    public void latLonToDian() throws IOException {
        Map pointMap = new HashMap();
        BufferedReader bufferedReader = null;
        Jedis jedis = new Jedis("10.14.83.150", 6379);
        jedis.select(3);
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(new File("D:/data/insert_road6.csv")), "utf8"));
            String line = new String();
            while ((line = bufferedReader.readLine()) != null) {
                String[] ss = line.trim().split(",");
                pointMap.put(new String[]{ss[2], ss[3]}, ss[1]);
                // pointMap.put(ss[2] + "," + ss[3], ss[1]);
            }
            jedis.set("latlonToDian", JSON.toJSONString(pointMap));
            System.out.println("--------over--------");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (bufferedReader != null)
                bufferedReader.close();
        }
    }

    /**
     * @throws IOException
     * @description 桩点对应其他信息 --- 风险告警信息中所需要使用的
     */
    @Test
    public void con() throws IOException {
        Map content_map = new HashMap();
        BufferedReader bufferedReader = null;
        Jedis jedis = new Jedis("10.14.83.150", 6379);
        jedis.select(3);
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(new File("D:/data/insert_road6.csv")), "utf8"));
            String line = new String();
            while ((line = bufferedReader.readLine()) != null) {
                String[] ss = line.trim().split(",");
                content_map.put(ss[1], new String[]{ss[5], ss[6], ss[7], ss[8], ss[10]});
            }
            jedis.set("dianToAll", JSON.toJSONString(content_map));
            System.out.println("--------over--------");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (bufferedReader != null)
                bufferedReader.close();
        }
    }

    /**
     * @description 根据桩点生成切片id --- 线的
     */
    @Test
    public void test() throws IOException {
        Map map = new HashMap();
        BufferedReader bufferedReader = null;
        Jedis jedis = new Jedis("10.14.83.150", 6379);
        jedis.select(3);
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(new File("D:/data/insert_road6.csv")), "utf8"));
            String line = new String();
            SlicingCalculation slicingCalculation = new SlicingCalculation();
            while ((line = bufferedReader.readLine()) != null) {
                String[] ss = line.trim().split(",", -1);
                map.put(ss[1], slicingCalculation.getSliceId(2, Double.parseDouble(ss[2]), Double.parseDouble(ss[3])));
            }
            jedis.hmset("dianToLineSclie", map);
            System.out.println("--------over--------");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (bufferedReader != null)
                bufferedReader.close();
        }
    }

    /**
     * @description 根据桩点生成切片id --- 点的
     */
    @Test
    public void tes1t() throws IOException {
        Map map = new HashMap();
        BufferedReader bufferedReader = null;
        Jedis jedis = new Jedis("10.14.83.150", 6379);
        jedis.select(3);
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(new File("D:/data/insert_road6.csv")), "utf8"));
            String line = new String();
            SlicingCalculation slicingCalculation = new SlicingCalculation();
            while ((line = bufferedReader.readLine()) != null) {
                String[] ss = line.trim().split(",", -1);
                map.put(ss[1], slicingCalculation.getSliceId(1, Double.parseDouble(ss[2]), Double.parseDouble(ss[3])));
            }
            jedis.hmset("dianToDianSclie", map);
            System.out.println("--------over--------");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (bufferedReader != null)
                bufferedReader.close();
        }
    }


    /**
     * @description 从数据库获取所有的key和value测试
     */
    @Test
    public void huoQuHash() throws Exception {
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("d:/data/test.txt")), "utf-8"));
        Jedis jedis = null;
        try {
            jedis = new Jedis("10.14.83.150", 6379);
            jedis.select(0);
            Map<String, String> latlon2 = jedis.hgetAll("0231405242019010717402011839");
            /*for (Map.Entry m : latlon2.entrySet()) {
                bufferedWriter.write(m.getKey() + "：" + m.getValue());
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }*/
            System.out.println(latlon2);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            jedis.close();
            if (jedis != null)
                MyJedisPool.returnJedisOjbect(jedis);
            if (bufferedWriter != null)
                bufferedWriter.close();
        }
    }

    /**
     * @description 测试数据库是否有值
     */
    @Test
    public void hah() {
        Jedis jedis = null;
        try {
            jedis = MyJedisPool.getReadJedisObject();
            jedis.select(3);
            String zhibiao;
            Map map;
            if (!jedis.exists("zhibiao")) {
                zhibiao = jedis.set("zhibiao", "1");
                System.out.println("redis数据库中标志创建成功：" + zhibiao);
            } else {
                zhibiao = jedis.get("zhibiao");
                map = jedis.hgetAll("latlon");
                System.out.println("redis数据库查询指标值成功：" + JSON.toJSONString(map));
            }
            Utils.sleep(3000);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (jedis != null)
                MyJedisPool.returnJedisOjbect(jedis);
        }
    }

    @Test
    public void riskZhiBiao() {
        Jedis jedis = null;
        try {
            jedis = MyJedisPool.getReadJedisObject();
            jedis.select(3);
            jedis.hset("riskKaZhi", "vb1", "50");
            jedis.hset("riskKaZhi", "vb2", "100");
            jedis.hset("riskKaZhi", "vb3", "200");
            jedis.hset("riskKaZhi", "vb4", "500");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (jedis != null)
                MyJedisPool.returnJedisOjbect(jedis);
            System.out.println("---------------over------------------");
        }
    }

    @Test
    public void test01() {
        Jedis jedis = null;
        try {
            jedis = MyJedisPool.getReadJedisObject();
            jedis.select(3);
            List<String> lrange = jedis.lrange("filter", 0, jedis.llen("filter"));
            System.out.println(lrange.size());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (jedis != null)
                MyJedisPool.returnJedisOjbect(jedis);
            System.out.println("---------------over------------------");
        }

    }

    @Test
    public void ceshi() {
        Jedis jedis = new Jedis("10.14.83.150", 6379);
        jedis.select(3);
        // List<String> lrange;
        List<String> lrange = jedis.lrange("filter", 0, jedis.llen("filter"));
        System.out.println(lrange.toString());
        if (!lrange.toString().contains("S83_197"))
            System.out.println("------------------------");
        System.out.println("+++++++++++++++++");
        jedis.close();
    }
}
