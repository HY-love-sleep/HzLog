# springboot + disruptor 处理日志Demo

### 1、流程

原始日志----->kafka----->poll----->放入OriginLogDisruptor由LogFilterHandler清洗
----->清洗好后放入CleanedLogDisruptor由logSenderHandler发送至es指定索引；



### 2、功能点

1. 模版方法+策略模式+泛型 定义日志清洗流程，添加不同类型日志更加灵活；
   - 日志清洗的流程， 
2. 性能优化：
   - kafka 多线程异步poll消息 ；
   - disruptor无锁处理；
   - syslog接收消息交由线程池放入ringbuffer;



### 3、本地测试

1. 使用SendToKafka测试类像topic： log-info推送5W条数据；

   查看topic消息量：

   ```bash
   bin/kafka-run-class.sh kafka.tools.GetOffsetShell --broker-list localhost:9092 --time -1 --topic log-info
   ```

   ![image-20240304100455091](img/image-20240304100455091.png)

2. 启动程序进行消费

3. 查看es中的文档数

   ```bash
   curl -XGET -u elastic:971122 'http://192.168.0.128:9200/databaseaudit-hy/_count'
   ```



### 4、67测试

1. kafka新建topic

   ```
   kafka-topics.sh --bootstrap-server 172.16.21.67:9092 --create --topic log-info --partitions 5 --replication-factor 1
   
   ```

2. 查看topic列表

   ```
   kafka-topics.sh --bootstrap-server 172.16.21.67:9092 --list
   
   ```

3. 使用SendToKafka向log-info推送10W条数据；

   ![image-20240304104327026](img/image-20240304104327026.png)

4. 启动程序， 消费日志到es中；



### 5、集成syslog

通过SyslogServerIF接收syslog消息， 监听到消息直接扔给线程池， 由线程池提交给disruptor;

经测试， 每秒发送8000条syslog消息时， UDP模式下能保证不丢消息；

