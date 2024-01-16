# springboot + disruptor 处理日志Demo

### 1、流程

原始日志----->kafka----->poll----->放入OriginLogDisruptor由LogFilterHandler清洗
----->清洗好后放入CleanedLogDisruptor由logSenderHandler发送至es指定索引；

### 2、功能点

1. 模版方法+策略模式+泛型 定义日志清洗流程，添加不同类型日志更加灵活；
2. kafka 多线程异步poll消息 + disruptor无锁处理日志 提升日志处理性能；
3. todo: 考虑结合规则引擎如Aviator或URule等，将日志解析的正则表达式脱离coding；