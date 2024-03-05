package com.hy;

import com.hy.entity.common.OriginLogMessage;
import com.hy.service.KafkaProducerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Description: 随机生成日志消息， 并发送消息到kafka
 * Author: yhong
 * Date: 2024/1/15
 */
@SpringBootTest(classes = HyLogApplication.class)
public class SendToKafka {
    @Autowired
    private KafkaProducerService kafkaProducerService;

    @Test
    public void sendDBLogTest() {
        // 随机生成50000条原始日志消息发送至kafka
        List<String> logList = generateRandomLogs(50000);

        List<OriginLogMessage> list = logList.stream()
                .map(log -> new OriginLogMessage(log, "database"))
                .collect(Collectors.toList());

        kafkaProducerService.AsyncSendLogsToKafka(list);
    }

    @Test
    public void sendHostLogTest() {
        List<String> logList = new ArrayList<>();
        logList.add("{\"@version\":\"1\",\"fields\":{\"log_topic\":\"sys_command\"},\"hostipv6\":\"fe80::f816:3eff:fef2:2f41\",\"ecs\":{\"version\":\"1.0.0\"},\"agent\":{\"id\":\"d029fc38-474e-48aa-9722-dd1bea76a8a1\",\"version\":\"7.2.0\",\"ephemeral_id\":\"bb7f9f00-fba5-4860-9daa-45a5e8a47984\",\"type\":\"filebeat\",\"hostname\":\"caiji-165\"},\"message\":\"2023-10-09 11:14:02 ### root@/dev/pts/0 ### 172.20.205.154 53530 192.168.179.165 22 ### /data/logstash-7.14.0/1009 ### 1696820264 ###  ps aux | grep logstash \",\"input\":{\"type\":\"log\"},\"log\":{\"offset\":9406,\"file\":{\"path\":\"/.cmdlog/cmdlog.2023-10-09\"}},\"host\":{\"name\":\"caiji-165\",\"mac\":[\"fa:16:3e:f2:2f:41\"],\"architecture\":\"x86_64\",\"os\":{\"name\":\"CentOS Linux\",\"kernel\":\"3.10.0-957.el7.x86_64\",\"version\":\"7 (Core)\",\"family\":\"redhat\",\"platform\":\"centos\",\"codename\":\"Core\"},\"ip\":[\"192.168.179.165\",\"fe80::f816:3eff:fef2:2f41\"],\"containerized\":false,\"hostname\":\"caiji-165\",\"id\":\"ecd2091ebeb549d1a98835f858a296e9\"},\"@timestamp\":\"2023-10-09T03:14:03.970Z\",\"hostipv4\":\"192.168.179.165\",\"tags\":[\"bashhistory\"]}");
        logList.add("{\"@version\":\"1\",\"fields\":{\"log_topic\":\"sys_command\"},\"hostipv6\":\"fe80::f816:3eff:fef2:2f41\",\"ecs\":{\"version\":\"1.0.0\"},\"agent\":{\"id\":\"d029fc38-474e-48aa-9722-dd1bea76a8a1\",\"version\":\"7.2.0\",\"ephemeral_id\":\"bb7f9f00-fba5-4860-9daa-45a5e8a47984\",\"type\":\"filebeat\",\"hostname\":\"caiji-165\"},\"message\":\"2023-10-09 11:14:25 ### root@/dev/pts/0 ### 172.20.205.154 53530 192.168.179.165 22 ### /data/logstash-7.14.0/1009 ### 1696820264 ###  nohup ../bin/logstash -f host-log-filter.conf -r --path.data=./data/host > host.log & \",\"input\":{\"type\":\"log\"},\"log\":{\"offset\":10880,\"file\":{\"path\":\"/.cmdlog/cmdlog.2023-10-09\"}},\"host\":{\"name\":\"caiji-165\",\"mac\":[\"fa:16:3e:f2:2f:41\"],\"architecture\":\"x86_64\",\"os\":{\"name\":\"CentOS Linux\",\"kernel\":\"3.10.0-957.el7.x86_64\",\"version\":\"7 (Core)\",\"platform\":\"centos\",\"family\":\"redhat\",\"codename\":\"Core\"},\"ip\":[\"192.168.179.165\",\"fe80::f816:3eff:fef2:2f41\"],\"containerized\":false,\"hostname\":\"caiji-165\",\"id\":\"ecd2091ebeb549d1a98835f858a296e9\"},\"@timestamp\":\"2023-10-09T03:14:25.975Z\",\"tags\":[\"bashhistory\"],\"hostipv4\":\"192.168.179.165\"}");
        logList.add("{\"@version\":\"1\",\"hostipv6\":\"fe80::f816:3eff:fef2:2f41\",\"fields\":{\"log_topic\":\"sys_command\"},\"ecs\":{\"version\":\"1.0.0\"},\"agent\":{\"id\":\"d029fc38-474e-48aa-9722-dd1bea76a8a1\",\"ephemeral_id\":\"bb7f9f00-fba5-4860-9daa-45a5e8a47984\",\"version\":\"7.2.0\",\"type\":\"filebeat\",\"hostname\":\"caiji-165\"},\"message\":\"2023-10-09 11:14:21 ### root@/dev/pts/0 ### 172.20.205.154 53530 192.168.179.165 22 ### /data/logstash-7.14.0/1009 ### 1696820264 ###  history \",\"input\":{\"type\":\"log\"},\"log\":{\"offset\":10514,\"file\":{\"path\":\"/.cmdlog/cmdlog.2023-10-09\"}},\"host\":{\"name\":\"caiji-165\",\"mac\":[\"fa:16:3e:f2:2f:41\"],\"architecture\":\"x86_64\",\"os\":{\"name\":\"CentOS Linux\",\"kernel\":\"3.10.0-957.el7.x86_64\",\"version\":\"7 (Core)\",\"platform\":\"centos\",\"family\":\"redhat\",\"codename\":\"Core\"},\"ip\":[\"192.168.179.165\",\"fe80::f816:3eff:fef2:2f41\"],\"containerized\":false,\"hostname\":\"caiji-165\",\"id\":\"ecd2091ebeb549d1a98835f858a296e9\"},\"@timestamp\":\"2023-10-09T03:14:21.973Z\",\"tags\":[\"bashhistory\"],\"hostipv4\":\"192.168.179.165\"}");

        List<OriginLogMessage> list = logList.stream()
                .map(log -> new OriginLogMessage(log, "host"))
                .collect(Collectors.toList());

        kafkaProducerService.AsyncSendLogsToKafka(list);
    }


    private static List<String> generateRandomLogs(int count) {
        List<String> logs = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < count; i++) {
            String log = generateRandomLog(random);
            logs.add(log);
        }

        return logs;
    }

    private static String generateRandomLog(Random random) {
        String[] riskTypes = {"无", "类型1", "类型2", "类型3"};
        String[] riskLevels = {"无", "低", "中", "高"};
        String[] engineActions = {"放行", "阻止", "监控"};
        String[] actionTypes = {"SELECT", "UPDATE", "INSERT", "DELETE"};

        return String.format("DBSEC CEF:%s:%d|发生时间:2024-01-05 08:%02d:%02d|服务器IP:%s|服务器端口:%d|数据库实例名:mysql|数据库名:godzilla|客户端IP:%s|客户端端口:%d|应用用户:无|数据库用户:GODZILLA|风险类型:%s|风险级别:%s|引擎动作:%s|规则名称:无|操作类型:%s|响应时间:%d(us)|执行结果:成功|影响行数:%d|SQL语句:SELECT*FROM `SYNC_INFO` WHERE(UID=? AND PTS>=?)ORDER BY PTS ASC LIMIT 1000/*Y*/\n\u0000",
                getRandomIpAddress(), getRandomPort(), random.nextInt(60), random.nextInt(60),
                getRandomIpAddress(), getRandomPort(), getRandomIpAddress(), getRandomPort(),
                getRandomElement(riskTypes), getRandomElement(riskLevels), getRandomElement(engineActions),
                getRandomElement(actionTypes), random.nextInt(1000), random.nextInt(100));
    }

    private static String getRandomIpAddress() {
        Random random = new Random();
        return String.format("%d.%d.%d.%d", random.nextInt(256), random.nextInt(256), random.nextInt(256), random.nextInt(256));
    }

    private static int getRandomPort() {
        Random random = new Random();
        return random.nextInt(65536);
    }

    private static String getRandomElement(String[] array) {
        Random random = new Random();
        return array[random.nextInt(array.length)];
    }
}
