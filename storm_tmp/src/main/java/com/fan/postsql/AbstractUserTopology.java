package com.fan.postsql;

import org.apache.storm.Config;
import org.apache.storm.StormSubmitter;
import org.apache.storm.generated.StormTopology;
import org.apache.storm.jdbc.common.Column;
import org.apache.storm.jdbc.common.ConnectionProvider;
import org.apache.storm.jdbc.common.HikariCPConnectionProvider;
import org.apache.storm.jdbc.common.JdbcClient;
import org.apache.storm.jdbc.mapper.JdbcLookupMapper;
import org.apache.storm.jdbc.mapper.JdbcMapper;
import org.apache.storm.jdbc.mapper.SimpleJdbcLookupMapper;
import org.apache.storm.jdbc.mapper.SimpleJdbcMapper;
import org.apache.storm.shade.com.google.common.collect.Lists;
import org.apache.storm.shade.com.google.common.collect.Maps;
import org.apache.storm.tuple.Fields;

import java.sql.Types;
import java.util.List;
import java.util.Map;

/**
 * @description 抽象类：将一些公用的方法封装
 * 饭补充：storm将数据写入postgis数据库的官方示例（应该是）
 */
public abstract class AbstractUserTopology {
    // sql语句 可以自己现在数据库中创建好
    private static final List<String> setupSqls = Lists.newArrayList(
            "drop table if exists user",
            "drop table if exists department",
            "drop table if exists user_department",
            "create table if not exists user (user_id integer, user_name varchar(100), dept_name varchar(100), create_date date)",
            "create table if not exists department (dept_id integer, dept_name varchar(100))",
            "create table if not exists user_department (user_id integer, dept_id integer)",
            "insert into department values (1, 'R&D')",
            "insert into department values (2, 'Finance')",
            "insert into department values (3, 'HR')",
            "insert into department values (4, 'Sales')",
            "insert into user_department values (1, 1)",
            "insert into user_department values (2, 2)",
            "insert into user_department values (3, 3)",
            "insert into user_department values (4, 4)"
    );
    protected UserSpout userSpout;
    protected JdbcMapper jdbcMapper;
    protected JdbcLookupMapper jdbcLookupMapper;
    // 线程安全的 实现了ConnectionProvider接口  有三个方法 prepare(),getConnection()  获取连接,cleanUp()
    protected ConnectionProvider connectionProvider;

    protected static final String TABLE_NAME = "user";
    protected static final String JDBC_CONF = "jdbc.conf";
    protected static final String SELECT_QUERY = "select dept_name from department, user_department where department.dept_id = user_department.dept_id" +
            " and user_department.user_id = ?";

    public void execute(String[] args) throws Exception {
        if (args.length != 4 && args.length != 5) {
            System.out.println("Usage: " + this.getClass().getSimpleName() + " <dataSourceClassName> <dataSource.url> "
                    + "<user> <password> [topology name]");
            System.exit(-1);
        }
        Map<String, Object> map = Maps.newHashMap();
        map.put("dataSourceClassName", args[0]);//com.mysql.jdbc.jdbc2.optional.MysqlDataSource
        map.put("dataSource.url", args[1]);//jdbc:mysql://localhost/test
        map.put("dataSource.user", args[2]);//root

        if (args.length == 4) {
            map.put("dataSource.password", args[3]);//password
        }

        Config config = new Config();
        config.put(JDBC_CONF, map);

        ConnectionProvider connectionProvider = new HikariCPConnectionProvider(map);
        // 对数据库连接池进行初始化
        connectionProvider.prepare();
        // 数据查找超时时间
        int queryTimeoutSecs = 60;
        // 获得数据库连接
        JdbcClient jdbcClient = new JdbcClient(connectionProvider, queryTimeoutSecs);
        for (String sql : setupSqls) {
            // 执行sql语句
            jdbcClient.executeSql(sql);
        }

        // List<Column> list = new ArrayList<>();
        // 创建一列将值传入   列名  值    值的类型
        // list.add(new Column("dept_id", 1, Types.INTEGER));
        // //查询
        // List<List<Column>> lists = jdbcClient.select("select dept_id,dept_name from department where  dept_id=?", list);
        // //计算出查询的条数
        // Long count = lists.stream().count();
        // System.err.println(count);

        this.userSpout = new UserSpout();
        // 通过connectionProvider和table自己去获取数据表的metadata(元数据)表字段的类型，名称，初始化schemaColumns
        // 使用tableName进行插入数据，需要指定表中的所有字段
        this.jdbcMapper = new SimpleJdbcMapper(TABLE_NAME, connectionProvider);
        // 关闭数据库连接池
        connectionProvider.cleanup();
        // 上面的代码可以独立运行
        // 指定bolt的输出字段
        // declarer.declare(new Fields("user_id", "user_name", "create_date"));  spout中发射的字段
        // dept_name 通过查询出的字段
        Fields outputFields = new Fields("user_id", "user_name", "dept_name", "create_date");
        // 指定查询条件字段  user_id的值是spout中发射出user_id的值
        List<Column> queryParamColumns = Lists.newArrayList(new Column("user_id", Types.INTEGER));
        // 通过查询为outputFields中的 dept_name赋值   其他三个字段是原始spout中的
        this.jdbcLookupMapper = new SimpleJdbcLookupMapper(outputFields, queryParamColumns);
        // 拿到还未初始化的连接
        this.connectionProvider = new HikariCPConnectionProvider(map);
        String topoName = "test";
        if (args.length > 4) {
            topoName = args[4];
        }

        StormSubmitter.submitTopology(topoName, config, getTopology());
    }

    public abstract StormTopology getTopology();

}