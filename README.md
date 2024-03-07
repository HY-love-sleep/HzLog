# springboot + disruptor 处理日志Demo

### 1、流程

以kafka接收为例：

1. kafka消费端接收消息， 将其直接扔进OriginLogDisruptor的环形数组里；
2. OriginLogDisruptor的消费者工作池消费ringbuffer中的原始日志， 进行清洗的操作；
3. LogFilterHandler是OriginLogDisruptor中的消费者， 他对于获取到的每个原始日志， 通过策略模式调用合适的日志清洗业务方法去清洗， 然后将清洗好的日志put到CleanedLogDisruptor中；
4. LogSenderHandler是CleanedLogDisruptor中的消费者，对于获取到的每个LogEvent， 将其发送到Es指定索引中；



- 对于新接入的日志， 接收和发送日志流程是通用的， 只需要更改配置文件中kafka、syslog、es的相关配置；

- 对于日志清洗部分，对于每一种新接入的日志， 需要继承LogCleanTemplate以及实现LogCleanStrategy，LogCleanTemplate固定了清洗的流程：打印-解析json-转实体类；LogCleanStrategy为LogFilterHandler封装了一个统一的调用接口；

  不同类型的日志，如果不是json格式的， 在解析json的过程中需要自定义正则表达式；是json格式的， 需要对字段进行映射， 这段逻辑也需要根据映射Map进行重写；

### 2、功能点

1. 模版方法+策略模式+泛型 定义日志清洗流程，添加不同类型日志更加灵活；

   - 日志清洗的流程， 基本是固定的， 所以定义了一个模版方法， 所有的日志清洗都经历以下步骤：

     - 打印原始日志【可选】；

     - 将原始日志转为JSON格式；

     - 将JSON格式的原始日志转为对应的实体类；【实体类是根据标准日志模版定死的】

       ![image-20240304193551439](C:/Users/%E6%B4%AA%E5%B2%A9/AppData/Roaming/Typora/typora-user-images/image-20240304193551439.png)

       

   - 每种日志清洗方法都继承模版方法，自我实现转换json和转换实体类的方法；

   - 对于日志来说， 只有一个clean方法来清洗他们， 所以对于不同的日志类型，上层只提供一个统一的clean接口，通过策略模式根据日志类型来调用合适的strategy实现类来进行清洗， 而strategy实现类里的clean方法持有了template里的cleanLog；

   - 每种类型的日志对应的实体类是不一样的， 所以采用泛型来接收不同的实体类；

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

经测试， 每秒发送8000条syslog消息时， UDP模式下能保证基本不丢消息；





