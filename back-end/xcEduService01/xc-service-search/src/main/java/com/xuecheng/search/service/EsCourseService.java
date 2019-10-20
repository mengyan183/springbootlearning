package com.xuecheng.search.service;

import com.xuecheng.framework.domain.course.CoursePub;
import com.xuecheng.framework.domain.search.CourseSearchParam;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.search.config.ElasticsearchConfig;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

/**
 * 课程搜索服务
 */
@Service
public class EsCourseService {
    @Autowired
    private RestHighLevelClient restHighLevelClient;
    @Autowired
    private ElasticsearchConfig elasticsearchConfig;

    /**
     * 分页查询课程信息
     *
     * @param page
     * @param size
     * @param courseSearchParam
     * @return
     */
    public QueryResponseResult<CoursePub> list(int page, int size, CourseSearchParam courseSearchParam) {

        if (page < 1) {
            page = 1;
        }
        if (size < 1) {
            size = 20;
        }
        if (courseSearchParam == null) {
            courseSearchParam = new CourseSearchParam();
        }
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        // 查询源字段
        searchSourceBuilder.fetchSource(elasticsearchConfig.getSourceField().split(","), new String[]{});
        // 设置分页查询
        searchSourceBuilder.from((page - 1) * size);
        searchSourceBuilder.size(size);
        // 组装查询条件
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();

        // 关键字查询
        String keyword = courseSearchParam.getKeyword();
        if (StringUtils.isNotBlank(keyword)) {
            MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders.multiMatchQuery(keyword, "name", "teachplan", "description");
            // 设置匹配度
            multiMatchQueryBuilder.minimumShouldMatch("80");
            // 提高name的权重值
            multiMatchQueryBuilder.field("name", 10f);
            // 采用  multiMatchQuery
            boolQueryBuilder.must(multiMatchQueryBuilder);
        }
        //过滤
        if (StringUtils.isNotBlank(courseSearchParam.getMt())) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("mt", courseSearchParam.getMt()));
        }
        if (StringUtils.isNotBlank(courseSearchParam.getSt())) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("st", courseSearchParam.getSt()));
        }
        if (StringUtils.isNotBlank(courseSearchParam.getGrade())) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("grade", courseSearchParam.getGrade()));
        }

        searchSourceBuilder.query(boolQueryBuilder);

        //高亮设置
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.preTags("<font class='eslight'>");
        highlightBuilder.postTags("</font>");
        //设置高亮字段
        highlightBuilder.fields().add(new HighlightBuilder.Field("name"));
        searchSourceBuilder.highlighter(highlightBuilder);


        SearchRequest searchRequest = new SearchRequest(elasticsearchConfig.getCourseIndex());
        searchRequest.source(searchSourceBuilder);
        QueryResult queryResult = new QueryResult();
        ArrayList<CoursePub> coursePubs = new ArrayList<>();
        try {
            SearchResponse search = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            // 获取响应结果
            SearchHits hits = search.getHits();
            // 获取查询总条数
            long value = hits.getTotalHits().value;
            for (SearchHit searchHit : hits) {
                if (searchHit != null) {
                    Map<String, Object> sourceAsMap = searchHit.getSourceAsMap();
                    CoursePub coursePub = new CoursePub();
                    String name = (String) sourceAsMap.get("name");
                    coursePub.setName(name);
                    //图片
                    String pic = (String) sourceAsMap.get("pic");
                    coursePub.setPic(pic);
                    //价格
                    Float price = null;
                    try {
                        if (sourceAsMap.get("price") != null) {
                            price = Float.parseFloat(sourceAsMap.get("price").toString());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    coursePub.setPrice(price);
                    Float price_old = null;
                    try {
                        if (sourceAsMap.get("price_old") != null) {
                            price_old = Float.parseFloat(sourceAsMap.get("price_old").toString());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    coursePub.setPrice_old(price_old);
                    coursePubs.add(coursePub);
                }
            }
            queryResult.setList(coursePubs);
            queryResult.setTotal(value);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new QueryResponseResult<>(CommonCode.SUCCESS, queryResult);
    }
}
