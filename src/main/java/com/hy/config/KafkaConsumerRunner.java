package com.hy.config;
import com.hy.service.KafkaPollMessageService;
import com.hy.service.SysLogReceivedMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Description: 配置kafka和syslog自动拉取消息

 * 当Spring Boot应用启动并完成所有Spring容器的初始化之后；
 * 它会自动调用所有实现了CommandLineRunner或ApplicationRunner接口的Bean的run方法。
 * Author: yhong
 * Date: 2024/1/11
 */
@Component
public class KafkaConsumerRunner implements CommandLineRunner {

    @Value("${spring.inner.consumer}")
    private boolean consumerMode;

    private final KafkaPollMessageService kafkaMessageService;
    private final SysLogReceivedMessageService sysLogReceivedMessageService;

    @Autowired
    public KafkaConsumerRunner(KafkaPollMessageService kafkaMessageService, SysLogReceivedMessageService sysLogReceivedMessageService) {
        this.kafkaMessageService = kafkaMessageService;
        this.sysLogReceivedMessageService = sysLogReceivedMessageService;
    }

    @Override
    public void run(String... args) throws Exception {
        if (consumerMode) {
            kafkaMessageService.startConsuming();
            sysLogReceivedMessageService.receiveSyslogMessage();
        }
    }
}

