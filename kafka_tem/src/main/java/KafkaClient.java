 
import com.google.common.collect.ImmutableList;
import org.apache.kafka.clients.admin.*;
import org.apache.kafka.common.KafkaFuture;
import org.apache.kafka.common.config.TopicConfig;
import org.apache.log4j.Logger;
 
import java.util.*;
import java.util.concurrent.ExecutionException;
 
public class KafkaClient implements AutoCloseable{
 
    private KafkaAdminClient client;
 
    private int timeout=6000;
 
    private final static Logger logger=Logger.getLogger(KafkaClient.class);
 
    // public KafkaClient(KafkaSettings settings){
    //     Map<String,Object> map=new HashMap<>();
    //     String[] hosts=settings.getArray(KafkaSettings.PREFIX_ALL,AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG,",");
    //     map.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, Arrays.asList(hosts));
    //     client=(KafkaAdminClient)KafkaAdminClient.create(map);
    //     if (logger.isInfoEnabled()){
    //         logger.info("Kafka集群已成功连接");
    //     }
    // }
 
    public KafkaClient(String[] hosts){
        Map<String,Object> map=new HashMap<>();
        map.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, Arrays.asList(hosts));
        client=(KafkaAdminClient)KafkaAdminClient.create(map);
        if (logger.isInfoEnabled()){
            logger.info("Kafka集群已成功连接");
        }
    }
 
    /**
     * 创建一个新的topic
     * @param name topic名
     * @param numPartitions 分区数
     * @param replicationFactor 副本数
     * @return 是否创建成功
     * @throws
     */
    public boolean createTopic(String name, int numPartitions, short replicationFactor) throws ExecutionException, InterruptedException {
 
        Map<String,String> topicConfig=new HashMap<>();
        /** 旧日志段的保留测率，删除或压缩，此时选择删除 */
        topicConfig.put(TopicConfig.CLEANUP_POLICY_CONFIG,TopicConfig.CLEANUP_POLICY_DELETE);
        /** 过期数据的压缩方式，如果上面选项为压缩的话才有效 */
        //topicConfig.put(TopicConfig.COMPRESSION_TYPE_CONFIG,"snappy");
        /**
         * The amount of time to retain delete tombstone markers for log compacted topics.
         * This setting also gives a bound on the time in which a consumer must complete a
         * read if they begin from offset 0 to ensure that they get a valid snapshot of the
         * final stage (otherwise delete tombstones may be collected before they complete their scan).
         * 默认1天
         * */
        topicConfig.put(TopicConfig.DELETE_RETENTION_MS_CONFIG,"86400000");
        /** 文件在文件系统上被删除前的保留时间，默认为60秒 */
        topicConfig.put(TopicConfig.FILE_DELETE_DELAY_MS_CONFIG,"60000");
        /** 将数据强制刷入日志的条数间隔 */
        //topicConfig.put(TopicConfig.FLUSH_MESSAGES_INTERVAL_CONFIG,"9223372036854775807");
        /** 将数据强制刷入日志的时间间隔 */
        //topicConfig.put(TopicConfig.FLUSH_MS_CONFIG,"9223372036854775807");
        /** offset设置 */
        //topicConfig.put(TopicConfig.INDEX_INTERVAL_BYTES_CONFIG,"4096");
        /** 每个批量消息最大字节数 */
        //topicConfig.put(TopicConfig.MAX_MESSAGE_BYTES_CONFIG,"1000012");
        /** 记录标记时间与kafka本机时间允许的最大间隔，超过此值的将被拒绝 */
        //topicConfig.put(TopicConfig.MESSAGE_TIMESTAMP_DIFFERENCE_MAX_MS_CONFIG,"9223372036854775807");
        /** 标记时间类型，是创建时间还是日志时间 CreateTime/LogAppendTime */
        //topicConfig.put(TopicConfig.MESSAGE_TIMESTAMP_TYPE_CONFIG,"CreateTime");
        /** 如果日志压缩设置为可用的话，设置日志压缩器清理日志的频率。默认情况下，压缩比率超过50%时会避免清理日志。
        此比率限制重复日志浪费的最大空间，设置为50%，意味着最多50%的日志是重复的。更高的比率设置意味着更少、更高效
        的清理，但会浪费更多的磁盘空间。*/
        //topicConfig.put(TopicConfig.MIN_CLEANABLE_DIRTY_RATIO_CONFIG,"0.5");
        /** 消息在日志中保持未压缩状态的最短时间，只对已压缩的日志有效 */
        //topicConfig.put(TopicConfig.MIN_COMPACTION_LAG_MS_CONFIG,"0");
        /** 当一个producer的ack设置为all（或者-1）时，此项设置的意思是认为新记录写入成功时需要的最少副本写入成功数量。
        如果此最小数量没有达到，则producer抛出一个异常（NotEnoughReplicas 或者NotEnoughReplicasAfterAppend）。
        你可以同时使用min.insync.replicas 和ack来加强数据持久话的保障。一个典型的情况是把一个topic的副本数量设置为3,
        min.insync.replicas的数量设置为2,producer的ack模式设置为all，这样当没有足够的副本没有写入数据时，producer会抛出一个异常。*/
        topicConfig.put(TopicConfig.MIN_IN_SYNC_REPLICAS_CONFIG,"1");
        /** 如果设置为true，会在新日志段创建时预分配磁盘空间 */
        topicConfig.put(TopicConfig.PREALLOCATE_CONFIG,"true");
        /** 当保留策略为删除（delete）时，此设置控制在删除就日志段来清理磁盘空间前，保存日志段的partition能增长到的最大尺寸。
         * 默认情况下没有尺寸大小限制，只有时间限制。。由于此项指定的是partition层次的限制，它的数量乘以分区数才是topic层面保留的数量。 */
       // topicConfig.put(TopicConfig.RETENTION_BYTES_CONFIG,"-1");
        /**
         * 当保留策略为删除（delete）时，此设置用于控制删除旧日志段以清理磁盘空间前，日志保留的最长时间。默认为7天。
         * 这是consumer在多久内必须读取数据的一个服务等级协议（SLA）。
         * */
        topicConfig.put(TopicConfig.RETENTION_MS_CONFIG,"604800000");
        /**
         * 此项用于控制日志段的大小，日志的清理和持久话总是同时发生，所以大的日志段代表更少的文件数量和更小的操作粒度。
         * */
        topicConfig.put(TopicConfig.SEGMENT_BYTES_CONFIG,"1073741824");
        /**
         * 此项用于控制映射数据记录offsets到文件位置的索引的大小。我们会给索引文件预先分配空间，然后在日志滚动时收缩它。
         * 一般情况下你不需要改动这个设置。
         * */
        //topicConfig.put(TopicConfig.SEGMENT_INDEX_BYTES_CONFIG,"10485760");
 
        /**  从预订的段滚动时间中减去最大的随机抖动，避免段滚动时的惊群（thundering herds）  */
        //topicConfig.put(TopicConfig.SEGMENT_JITTER_MS_CONFIG,"0");
 
        /** 此项用户控制kafka强制日志滚动时间，在此时间后，即使段文件没有满，也会强制滚动，以保证持久化操作能删除或压缩就数据。默认7天 */
        topicConfig.put(TopicConfig.SEGMENT_MS_CONFIG,"604800000");
        /**
         * 是否把一个不在isr中的副本被选举为leader作为最后手段，即使这样做会带来数据损失
         * */
        topicConfig.put(TopicConfig.UNCLEAN_LEADER_ELECTION_ENABLE_CONFIG,"false");
 
        NewTopic newTopic=new NewTopic(name,numPartitions,replicationFactor);
        newTopic.configs(topicConfig);
        CreateTopicsOptions options=new CreateTopicsOptions();
        options.timeoutMs(timeout);
        CreateTopicsResult result=client.createTopics(ImmutableList.of(newTopic),options);
        for(Map.Entry<String,KafkaFuture<Void>> e : result.values().entrySet()){
            KafkaFuture<Void> future= e.getValue();
            future.get();
            boolean success=!future.isCompletedExceptionally();
            if(logger.isInfoEnabled()&&success){
                logger.info("已成功创建Kafka topic "+name+" ,分区 "+numPartitions+" ,副本 "+replicationFactor);
            }
            return success;
        }
 
        return false;
    }
 
    /**
     * 当topic不存在时，主动创建
     * @param name topic名
     * @param numPartitions 分区数
     * @param replicationFactor 副本数
     * @return 是否可以使用此topic，如果为true，可能是新创建或已存在
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public boolean createTopicIfNotExists(String name, int numPartitions, short replicationFactor) throws ExecutionException, InterruptedException {
        if(!listTopics().contains(name)){
            return createTopic(name,numPartitions,replicationFactor);
        }
        if(logger.isInfoEnabled()){
            logger.info("Kafka topic "+name+" 已存在");
        }
        return true;
    }
 
    /**
     * 列出所有非内部topic
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public Set<String> listTopics() throws ExecutionException, InterruptedException {
        ListTopicsOptions options=new ListTopicsOptions();
        //设置超时时间
        options.timeoutMs(timeout);
        //不列出kafka内部topic
        options.listInternal(false);
        ListTopicsResult result=client.listTopics(options);
        Set<String> topics= result.names().get();
        if(logger.isDebugEnabled()){
            logger.debug("发现"+topics.size()+"个Kafka topic ： "+topics.toString());
        }
        return topics;
    }
 
    /**
     * 检查topic是否存在
     * @param topics 待检查的topic
     * @return topic是否存在
     * @throws ExecutionException
     * @throws InterruptedException
     */
    // public List<PairObject<String,Boolean>> checkExists(String[] topics) throws ExecutionException, InterruptedException {
    //     Set<String> topicSet= listTopics();
    //     List<PairObject<String,Boolean>> exists=new ArrayList<>();
    //     for (String topic :topics){
    //         exists.add(new PairObject<>(topic,topicSet.contains(topic)));
    //     }
    //     return exists;
    // }
 
    /**
     * 删除指定topic(如果broker那没有设置允许删除topic的话，此调用会持续等待最终超时返回)
     * @param topics 待删除的topic
     * @return 删除是否成功
     * @throws ExecutionException
     * @throws InterruptedException
     */
    // public List<PairObject<String,Boolean>> deleteTopic(String[] topics) throws ExecutionException, InterruptedException {
    //     DeleteTopicsOptions options=new DeleteTopicsOptions();
    //     options.timeoutMs(timeout);
    //     DeleteTopicsResult deleteTopicsResult=client.deleteTopics(Arrays.asList(topics),options);
    //
    //     List<PairObject<String,Boolean>> result=new ArrayList<>();
    //
    //     for(Map.Entry<String,KafkaFuture<Void>> e : deleteTopicsResult.values().entrySet()){
    //         String topic=e.getKey();
    //         KafkaFuture<Void> future=e.getValue();
    //         future.get();
    //         result.add(new PairObject<>(topic,!future.isCompletedExceptionally()));
    //     }
    //     return result;
    // }
 
 
    @Override
    public void close() throws Exception {
        if (client!=null){
            client.close();
        }
    }
 
    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }
}
