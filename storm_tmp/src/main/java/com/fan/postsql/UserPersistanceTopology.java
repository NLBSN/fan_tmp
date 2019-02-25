package com.fan.postsql;

import com.google.common.collect.Lists;
import org.apache.storm.generated.StormTopology;
import org.apache.storm.jdbc.bolt.JdbcInsertBolt;
import org.apache.storm.jdbc.bolt.JdbcLookupBolt;
import org.apache.storm.jdbc.common.Column;
import org.apache.storm.jdbc.mapper.JdbcMapper;
import org.apache.storm.jdbc.mapper.SimpleJdbcMapper;
import org.apache.storm.topology.TopologyBuilder;

import java.sql.Types;
import java.util.List;

/**
 * @description 常用的api
 */
public class UserPersistanceTopology extends AbstractUserTopology {
    private static final String USER_SPOUT = "USER_SPOUT";
    private static final String LOOKUP_BOLT = "LOOKUP_BOLT";
    private static final String PERSISTANCE_BOLT = "PERSISTANCE_BOLT";

    public static void main(String[] args) throws Exception {
        new UserPersistanceTopology().execute(args);
    }

    @Override
    public StormTopology getTopology() {
        // 将outputFields的四个字段输出
        JdbcLookupBolt departmentLookupBolt = new JdbcLookupBolt(connectionProvider, SELECT_QUERY, this.jdbcLookupMapper);

        //must specify column schema when providing custom query.
        // 自己将表中的字段类型，名称映射
        List<Column> schemaColumns = Lists.newArrayList(new Column("create_date", Types.DATE),
                new Column("dept_name", Types.VARCHAR), new Column("user_id", Types.INTEGER), new Column("user_name", Types.VARCHAR));
        JdbcMapper mapper = new SimpleJdbcMapper(schemaColumns);
        // 将映射好的mapper与JdbcInsertBolt组合 形成一个完整的执行插入操作的bolt  使用schemaColumns，可以指定字段要插入的字段
        JdbcInsertBolt userPersistanceBolt = new JdbcInsertBolt(connectionProvider, mapper)
                .withInsertQuery("insert into user (create_date, dept_name, user_id, user_name) values (?,?,?,?)");

        // userSpout ==> jdbcBolt
        TopologyBuilder builder = new TopologyBuilder();

        builder.setSpout(USER_SPOUT, this.userSpout, 1);
        builder.setBolt(LOOKUP_BOLT, departmentLookupBolt, 1).shuffleGrouping(USER_SPOUT);
        builder.setBolt(PERSISTANCE_BOLT, userPersistanceBolt, 1).shuffleGrouping(LOOKUP_BOLT);
        return builder.createTopology();
    }
}