package com.xing.elasticsearch.util;


import com.alibaba.fastjson.JSON;
import com.xing.elasticsearch.entity.Book;
import com.xing.elasticsearch.entity.EsEntity;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.CountRequest;
import org.elasticsearch.client.core.CountResponse;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.*;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 类功能简述：
 * 类功能详述：
 *
 * @author xingguo
 * @date 2019/7/29 11:24
 */
@Component
public class EsUtil {


    @Value("${es.host}")
    public String host;
    @Value("${es.port}")
    public int port;
    @Value("${es.scheme}")
    public String scheme;

    public static final String INDEX_NAME = "book-index";

    public static RestHighLevelClient client = null;


    @PostConstruct
    public void init() {
        try {
            if (client != null) {
                client.close();
            }
            final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
            credentialsProvider.setCredentials(AuthScope.ANY,
                    new UsernamePasswordCredentials("elastic", "changeme"));  //es账号密码（默认用户名为elastic）
            client = new RestHighLevelClient(
                    RestClient.builder(
                            new HttpHost(host, port, scheme)
                    ).setHttpClientConfigCallback(httpClientBuilder -> {
                        httpClientBuilder.disableAuthCaching();
                        return httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
                    }));
            if (this.indexExist(INDEX_NAME)) {
                deleteIndex(INDEX_NAME);
            }
            CreateIndexRequest request = new CreateIndexRequest(INDEX_NAME);
//            request.settings(Settings.builder().put("index.number_of_shards", 3).put("index.number_of_replicas", 2));
            request.mapping(JSON.toJSONString(new Book()), XContentType.JSON);
            CreateIndexResponse res = client.indices().create(request, RequestOptions.DEFAULT);
            if (!res.isAcknowledged()) {
                throw new RuntimeException("初始化失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    /**
     * Description: 判断某个index是否存在
     *
     * @param index index名
     * @return boolean
     * @author xingguo
     * @date 2019/7/24 14:57
     */
    public boolean indexExist(String index) throws Exception {
        GetIndexRequest request = new GetIndexRequest(index);
        request.local(false);
        request.humanReadable(true);
        request.includeDefaults(false);
        return client.indices().exists(request, RequestOptions.DEFAULT);
    }

    /**
     * Description: 插入/更新一条记录
     *
     * @param index  index
     * @param entity 对象
     * @author xingguo
     * @date 2019/7/24 15:02
     */
    public void insertOrUpdateOne(String index, EsEntity entity) {
        IndexRequest request = new IndexRequest(index);
        request.id(entity.getId());
        request.source(JSON.toJSONString(entity.getData()), XContentType.JSON);
        try {
            client.index(request, RequestOptions.DEFAULT);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Description: 批量插入数据
     *
     * @param index index
     * @param list  带插入列表
     * @author xingguo
     * @date 2019/7/24 17:38
     */
    public void insertBatch(String index, List<EsEntity> list) {
        BulkRequest request = new BulkRequest();
        list.forEach(item -> request.add(new IndexRequest(index).setRefreshPolicy(WriteRequest.RefreshPolicy.NONE).id(item.getId())
                .source(JSON.toJSONString(item.getData()), XContentType.JSON)));
        request.setRefreshPolicy(WriteRequest.RefreshPolicy.WAIT_UNTIL);
        try {
            long l = System.currentTimeMillis();
            BulkResponse bulk = client.bulk(request, RequestOptions.DEFAULT);
            if(bulk.hasFailures()){
                throw new RuntimeException("error");
            }
            System.out.println(System.currentTimeMillis()-l+" ms");
            CountRequest countRequest = new CountRequest(index);
            countRequest.source(new SearchSourceBuilder().query(QueryBuilders.matchAllQuery()));
            CountResponse count = client.count(countRequest, RequestOptions.DEFAULT);
            System.out.println(count.getCount());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Description: 批量插入数据
     *
     * @param index index
     * @param list  带插入列表
     * @author xingguo
     * @date 2019/7/24 17:38
     */
    public void insertOrUpdateBatch(String index, List<EsEntity> list) {
        BulkRequest request = new BulkRequest();
        list.forEach(item -> request.add(new IndexRequest(index).id(item.getId()).id(item.getId())
                .source(JSON.toJSONString(item.getData()), XContentType.JSON)));
        try {
            client.bulk(request, RequestOptions.DEFAULT);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Description: 批量删除
     *
     * @param index  index
     * @param idList 待删除列表
     * @author xingguo
     * @date 2019/7/25 14:24
     */
    public <T> void deleteBatch(String index, Collection<T> idList) {
        BulkRequest request = new BulkRequest();
        idList.forEach(item -> request.add(new DeleteRequest(index, item.toString())));
        try {
            client.bulk(request, RequestOptions.DEFAULT);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Description: 搜索
     *
     * @param index   index
     * @param builder 查询参数
     * @param c       结果类对象
     * @return java.util.ArrayList
     * @author xingguo
     * @date 2019/7/25 13:46
     */
    public <T> List<T> search(String index, SearchSourceBuilder builder, Class<T> c) {
        SearchRequest request = new SearchRequest(index);
        request.source(builder);
        try {
            SearchResponse response = client.search(request, RequestOptions.DEFAULT);
            SearchHit[] hits = response.getHits().getHits();
            List<T> res = new ArrayList<>(hits.length);
            for (SearchHit hit : hits) {
                res.add(JSON.parseObject(hit.getSourceAsString(), c));
            }
            return res;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Description: 删除index
     *
     * @param index index
     * @return void
     * @author xingguo
     * @date 2019/7/26 11:30
     */
    public void deleteIndex(String index) {
        try {
            client.indices().delete(new DeleteIndexRequest(index), RequestOptions.DEFAULT);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Description: delete by query
     *
     * @param index   index
     * @param builder builder
     * @author xingguo
     * @date 2019/7/26 15:16
     */
    public void deleteByQuery(String index, QueryBuilder builder) {
        DeleteByQueryRequest request = new DeleteByQueryRequest(index);
        request.setQuery(builder);
        //设置批量操作数量,最大为10000
        request.setBatchSize(10000);
        request.setConflicts("proceed");
        try {
            client.deleteByQuery(request, RequestOptions.DEFAULT);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}