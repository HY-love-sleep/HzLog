package com.hy;

import com.hy.service.SysLogReceivedMessageService;
import org.graylog2.syslog4j.Syslog;
import org.graylog2.syslog4j.SyslogConstants;
import org.graylog2.syslog4j.SyslogIF;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Description: 发送SysLog消息
 * Author: yhong
 * Date: 2024/3/4
 */
@SpringBootTest(classes = HyLogApplication.class)
public class SysLogTest {
    @Value("${syslog.producer.host}")
    private String host;
    @Value("${syslog.producer.port}")
    private Integer port;

    @Autowired
    private SysLogReceivedMessageService sysLogReceivedMessageService;

    @Test
    public void sendSyslogTest() {
        generate();
    }
    @Test
    public void getSysLogTest() throws InterruptedException {
        sysLogReceivedMessageService.receiveSyslogMessage();
    }

    public void generate() {
        SyslogIF syslog = Syslog.getInstance(SyslogConstants.UDP);
        syslog.getConfig().setHost(host);
        syslog.getConfig().setPort(port);
        List<String> logList = generateRandomLogs(50000);
        for (String log : logList) {
            try {
                syslog.log(0, URLDecoder.decode(log, "utf-8"));
            } catch (UnsupportedEncodingException e) {
                System.out.println("generate log get exception " + e);
            }
        }
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
