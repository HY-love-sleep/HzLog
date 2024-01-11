package com.hy.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

/**
 * Description: es客户端配置类
 * Author: yhong
 * Date: 2024/1/11
 */
@Slf4j
@Configuration
public class ElasticsearchConfig {
    @Value("${spring.es.hostname}")
    private String hostname;
    @Value("${spring.es.port}")
    private Integer port;
    @Value("${spring.es.schema}")
    private String schema;
    @Value("${spring.es.username}")
    private String username;
    @Value("${spring.es.passwd}")
    private String passwd;

    @Bean(destroyMethod = "close")
    public RestHighLevelClient restHighLevelClient() {
        try {
            final RestClientBuilder builder = RestClient.builder(
                            new HttpHost(hostname, port, schema))
                    .setHttpClientConfigCallback(httpClientBuilder -> httpClientBuilder
                            .setDefaultCredentialsProvider(credentialsProvider()));
            RestHighLevelClient client = new RestHighLevelClient(builder);
            // 测试连接
            testClientConnection(client);
            // RestHighLevelClient内部维护了连接池，在初始化时只会创建一个RestHighLevelClient实例， 复用其中的http长连接；
            return client;
        } catch (Exception e) {
            log.error("创建ES客户端失败，error:{}", e.getMessage());
            throw e;
        }

    }

    private BasicCredentialsProvider credentialsProvider() {
        BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY,
            new UsernamePasswordCredentials(username, passwd));
        return credentialsProvider;
    }

    private void testClientConnection(RestHighLevelClient client) {
        // 例如，获取集群健康状态
        try {
            client.cluster().health(new ClusterHealthRequest(), RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.error("测试到Es的连接失败 : ", e);
            throw new RuntimeException("连接ES失败", e);
        }
    }
}
