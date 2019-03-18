package com.fan.postsql;

import com.wis.tools.TrafficUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description: traffic_fan
 * @author: fan
 * @Date: Created in 2018/10/16 16:01
 * @Modified By:
 */
public class SelectTable {
    private static final Logger LOGGER = LoggerFactory.getLogger(SelectTable.class);
    /**
     * @param tableName 表名
     * @Description: 获取数据
     */
    public Map<String, Map<String, String>> selectSQLData(String tableName) {
        Connection conn = null;
        PreparedStatement pStemt = null;
        Map<String, Map<String, String>> mapG = new HashMap<>();
        try {
            conn = TrafficUtils.getConnection();
            // logger.info(new Date(System.currentTimeMillis())+"：开始查询表数据："+tableName);
            pStemt = conn.prepareStatement("select * from " + tableName);
            // 结果集元数据
            ResultSetMetaData rsmd = pStemt.getMetaData();
            // 表列数
            int size = rsmd.getColumnCount();
            ResultSet result = pStemt.executeQuery();
            while (result.next()) {
                Map<String, String> map = new HashMap<>();
                for (int i = 2; i <= size; i++) {
                    map.put(rsmd.getColumnName(i).trim(), result.getString(i).trim());
                }
                mapG.put(result.getString(1).trim(), map);
            }
            TrafficUtils.release(result, pStemt, conn);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            return mapG;
        }
    }

}
