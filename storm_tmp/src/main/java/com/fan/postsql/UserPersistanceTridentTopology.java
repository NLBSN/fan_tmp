package com.fan.postsql;

import org.apache.storm.generated.StormTopology;
import org.apache.storm.jdbc.common.Column;
import org.apache.storm.jdbc.mapper.SimpleJdbcLookupMapper;
import org.apache.storm.jdbc.trident.state.JdbcQuery;
import org.apache.storm.jdbc.trident.state.JdbcState;
import org.apache.storm.jdbc.trident.state.JdbcStateFactory;
import org.apache.storm.jdbc.trident.state.JdbcUpdater;
import org.apache.storm.shade.com.google.common.collect.Lists;
import org.apache.storm.trident.Stream;
import org.apache.storm.trident.TridentState;
import org.apache.storm.trident.TridentTopology;
import org.apache.storm.tuple.Fields;

import java.sql.Types;

/**
 * @description 高级的事物api
 */
public class UserPersistanceTridentTopology extends AbstractUserTopology {

    public static void main(String[] args) throws Exception {
        new UserPersistanceTridentTopology().execute(args);
    }

    @Override
    public StormTopology getTopology() {
        TridentTopology topology = new TridentTopology();

        // 为了创建一个jdbc持久化的trident state我们需要提供一个表名或者一个插入查询，
        JdbcState.Options options = new JdbcState.Options()
                .withConnectionProvider(connectionProvider)// 拿到未初始化的连接
                .withMapper(this.jdbcMapper)// 通过表名完成映射
                // 因为sql语句中时通过user_id查询出dept_name的 所以将user_id传入值为上个流中发射user_id的值
                .withJdbcLookupMapper(new SimpleJdbcLookupMapper(new Fields("dept_name"), Lists.newArrayList(new Column("user_id", Types.INTEGER))))
                .withTableName(TABLE_NAME)// 表名  通过表名将数据写入表中 注释这句话则不会写入
                //.withInsertQuery("")     也可以通过sql语句来代替上面的表名
                .withSelectQuery(SELECT_QUERY);// 查询语句

        JdbcStateFactory jdbcStateFactory = new JdbcStateFactory(options);

        Stream stream = topology.newStream("userSpout", new UserSpout());
        TridentState state = topology.newStaticState(jdbcStateFactory);
        // 查询 将stream 的字段改变 由三个字段变为四个  ("user_id", "user_name", "dept_name", "create_date")
        stream = stream.stateQuery(state, new Fields("user_id","user_name","create_date"), new JdbcQuery(), new Fields("dept_name"));
        // 将数据更新插入数据库  jdbcStateFactory 根据设置的表名更新到对应的数据库 批处理 一批一批的插入
        stream.partitionPersist(jdbcStateFactory, new Fields("user_id","user_name","dept_name","create_date"),  new JdbcUpdater(), new Fields());
        return topology.build();
    }
}