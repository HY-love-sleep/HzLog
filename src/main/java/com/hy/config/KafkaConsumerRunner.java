package com.hy.config;
import com.hy.service.KafkaPollMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Description: 配置kafka自动拉取消息

 * 当Spring Boot应用启动并完成所有Spring容器的初始化之后；
 * 它会自动调用所有实现了CommandLineRunner或ApplicationRunner接口的Bean的run方法。
 * Author: yhong
 * Date: 2024/1/11
 */
@Component
public class KafkaConsumerRunner implements CommandLineRunner {

    private final KafkaPollMessageService kafkaMessageService;

    @Autowired
    public KafkaConsumerRunner(KafkaPollMessageService kafkaMessageService) {
        this.kafkaMessageService = kafkaMessageService;
    }

    @Override
    public void run(String... args) throws Exception {
        kafkaMessageService.startConsuming();
    }
}
