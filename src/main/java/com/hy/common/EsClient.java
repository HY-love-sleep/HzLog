package com.hy.common;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.elasticsearch.client.ElasticsearchClient;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;

/**
 * Description: ES客户端
 * Author: yhong
 * Date: 2024/1/10
 */
@Slf4j
@Component
public class EsClient {
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

    private RestHighLevelClient client;

    public EsClient() {
        this.client = null;
    }

    @PostConstruct
    public void init() {
        // 鉴权
        BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY,
                new UsernamePasswordCredentials(username, passwd));

        RestClientBuilder builder = RestClient.builder(new HttpHost(hostname, port))
                .setHttpClientConfigCallback(new RestClientBuilder.HttpClientConfigCallback() {
                    @Override
                    public HttpAsyncClientBuilder customizeHttpClient(HttpAsyncClientBuilder httpClientBuilder) {
                        return httpClientBuilder
                                .setDefaultCredentialsProvider(credentialsProvider)
                                .setDefaultIOReactorConfig(IOReactorConfig.custom()
                                        .setIoThreadCount(1)
                                        .build());
                    }
                });
        client = new RestHighLevelClient(builder);
    }

    public RestHighLevelClient getClient() {
        return this.client;
    }

    public void close() {
        try{
            this.client.close();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

}
