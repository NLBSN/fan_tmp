package com.fan.demo01;

import com.mysql.cj.jdbc.Driver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @Description: fan_tmp
 * @author: z1042
 * @Date: Created in 2019/3/13 21:40
 * @Modified By:
 */
public class Demo01 {
    public static void main(String[] args) {
        try {
            // Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/test", "root", "example");
            Statement statement = conn.createStatement();
            String sql = "";
            statement.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
