package com.xuecheng.search.config;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Administrator
 * @version 1.0
 **/
@Configuration
public class ElasticsearchConfig {

    @Value("${xuecheng.elasticsearch.hostlist}")
    private String hostlist;
    @Value("${xuecheng.elasticsearch.username}")
    private String username;
    @Value("${xuecheng.elasticsearch.password}")
    private String password;

    @Bean
    public CredentialsProvider credentialsProvider() {
        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY,
                new UsernamePasswordCredentials(username, password));  //es账号密码（默认用户名为elastic）
        return credentialsProvider;
    }

    @Bean
    public RestHighLevelClient restHighLevelClient() {
        //解析hostlist配置信息
        String[] split = hostlist.split(",");
        //创建HttpHost数组，其中存放es主机和端口的配置信息
        HttpHost[] httpHostArray = new HttpHost[split.length];
        for (int i = 0; i < split.length; i++) {
            String item = split[i];
            httpHostArray[i] = new HttpHost(item.split(":")[0], Integer.parseInt(item.split(":")[1]), "http");
        }
        //创建RestHighLevelClient客户端
        return new RestHighLevelClient(RestClient.builder(httpHostArray).setHttpClientConfigCallback(httpClientBuilder -> {
            httpClientBuilder.disableAuthCaching();
            return httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider());
        }));
    }

    //项目主要使用RestHighLevelClient，对于低级的客户端暂时不用
    @Bean
    public RestClient restClient() {
        //解析hostlist配置信息
        String[] split = hostlist.split(",");
        //创建HttpHost数组，其中存放es主机和端口的配置信息
        HttpHost[] httpHostArray = new HttpHost[split.length];
        for (int i = 0; i < split.length; i++) {
            String item = split[i];
            httpHostArray[i] = new HttpHost(item.split(":")[0], Integer.parseInt(item.split(":")[1]), "http");
        }
        RestClientBuilder restClientBuilder = RestClient.builder(httpHostArray).setHttpClientConfigCallback(httpAsyncClientBuilder -> {
            httpAsyncClientBuilder.disableAuthCaching();
            return httpAsyncClientBuilder.setDefaultCredentialsProvider(credentialsProvider());
        });
        return restClientBuilder.build();
        }

    }
