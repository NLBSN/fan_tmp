package com.new_storm_single;

import org.apache.log4j.Logger;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @Description: traffic_fan
 * @author: fan
 * @Date: Created in 2018/10/10 10:12
 * @Modified By:
 */
public class Utils {
    private final static Logger log = Logger.getLogger(Utils.class);

    public Properties getProperties(String confPath) {
        InputStream in = Utils.class.getClassLoader().getResourceAsStream(confPath);
        // System.out.println("-----------路径为:"+in);
        Properties prop = new Properties();
        try {
            prop.load(in);//获取配置文件

            System.out.println(prop.getProperty("kafka.consumer.topic"));
            in.close();
        } catch (IOException e) {
            log.error("No config.properties defined error");
        }
        return prop;
    }

    @Test
    public void tet1(){
        getProperties("config.properties");
    }
}
