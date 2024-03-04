package com.hy.service;

import com.hy.disruptor.OriginLogDisruptor;
import com.hy.entity.OriginLogMessage;
import com.lmax.disruptor.RingBuffer;
import com.sun.org.apache.bcel.internal.generic.FSUB;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.graylog2.syslog4j.Syslog;
import org.graylog2.syslog4j.SyslogConstants;
import org.graylog2.syslog4j.SyslogIF;
import org.graylog2.syslog4j.server.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;

/**
 * Description: 通过SysLog接收日志
 * todo: 可以与KafkaPollMessageService通过策略模式合并成一个接收器
 * Author: yhong
 * Date: 2024/3/4
 */

@Service
@RequiredArgsConstructor
@Slf4j
public class SysLogReceivedMessageService {
    @Value("${syslog.consumer.host}")
    private String host;
    @Value("${syslog.consumer.port}")
    private Integer port;

    private final OriginLogDisruptor disruptor;
    private final ExecutorService executorService;

    public void receiveSyslogMessage() throws InterruptedException {

        SyslogServerIF server = SyslogServer.getInstance(SyslogConstants.UDP);
        SyslogServerConfigIF config = server.getConfig();
        config.setHost(host);
        config.setPort(port);
        log.info("receiveSyslogMessage start listen at {}!", port);
        config.addEventHandler(new SyslogServerSessionEventHandlerIF() {
            @Override
            public Object sessionOpened(SyslogServerIF syslogServerIF, SocketAddress socketAddress) {
                return null;
            }

            @Override
            public void event(Object o, SyslogServerIF syslogServerIF, SocketAddress socketAddress,
                              SyslogServerEventIF syslogServerEventIF) {
                executorService.submit(() -> processEvent(syslogServerEventIF));
            }

            private void processEvent(SyslogServerEventIF syslogServerEventIF) {
                String originMessage = syslogServerEventIF.getMessage().substring(syslogServerEventIF.getMessage().indexOf("DBSEC"));
                log.info("sysLog:{}", originMessage);
                disruptor.getDisruptor().publishEvent((event, sequence) -> {
                    event.setOriginLogMessage(originMessage);
                    event.setLogType("database");
                });
            }

            @Override
            public void exception(Object o, SyslogServerIF syslogServerIF, SocketAddress socketAddress, Exception e) {
                log.error("Exception occurred while processing Syslog event", e);
            }

            @Override
            public void sessionClosed(Object o, SyslogServerIF syslogServerIF, SocketAddress socketAddress, boolean b) {
                // 可选实现
            }

            @Override
            public void initialize(SyslogServerIF syslogServerIF) {
                // 可选实现
            }

            @Override
            public void destroy(SyslogServerIF syslogServerIF) {
                SyslogServer.shutdown();
                executorService.shutdown();
            }
        });
        SyslogServer.getThreadedInstance(SyslogConstants.UDP);
        Thread.sleep(Long.MAX_VALUE);
    }
}

