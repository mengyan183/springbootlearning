package com.xuecheng.search.service;

import com.alibaba.fastjson.JSON;
import com.xuecheng.framework.domain.course.CoursePub;
import com.xuecheng.framework.domain.course.TeachplanMediaPub;
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
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
                    //取出高亮字段
                    Map<String, HighlightField> highlightFields = searchHit.getHighlightFields();
                    if (highlightFields.get("name") != null) {
                        HighlightField highlightField = highlightFields.get("name");
                        Text[] fragments = highlightField.fragments();
                        StringBuilder stringBuffer = new StringBuilder();
                        for (Text text : fragments) {
                            stringBuffer.append(text);
                        }
                        name = stringBuffer.toString();
                    }

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
                    coursePub.setId(searchHit.getId());
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

    /**
     * 根据课程id查询课程所有信息
     *
     * @param courseId
     * @return
     */
    public Map<String, CoursePub> listAll(String courseId) throws IOException {
        HashMap<String, CoursePub> stringCoursePubHashMap = new HashMap<>();
        if (StringUtils.isBlank(courseId)) {
            return stringCoursePubHashMap;
        }
        SearchRequest searchRequest = new SearchRequest(elasticsearchConfig.getCourseIndex());
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.termQuery("_id", courseId));
        SearchResponse search = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        SearchHits hits = search.getHits();
        long value = hits.getTotalHits().value;
        if (value < 1) {
            return stringCoursePubHashMap;
        }
        for (SearchHit searchHit : hits) {
            if (searchHit != null) {
                Map<String, Object> sourceAsMap = searchHit.getSourceAsMap();
                String name = (String) sourceAsMap.get("name");
                String grade = (String) sourceAsMap.get("grade");
                String charge = (String) sourceAsMap.get("charge");
                String pic = (String) sourceAsMap.get("pic");
                String description = (String) sourceAsMap.get("description");
                String teachplan = (String) sourceAsMap.get("teachplan");
                CoursePub coursePub = new CoursePub();
                coursePub.setId(courseId);
                coursePub.setName(name);
                coursePub.setPic(pic);
                coursePub.setGrade(grade);
                coursePub.setTeachplan(teachplan);
                coursePub.setDescription(description);
                stringCoursePubHashMap.put(courseId, coursePub);
            }
        }
        return stringCoursePubHashMap;
    }

    /**
     * 通过课程计划id集合批量获取 课程计划媒资信息
     *
     * @param teachplanIds
     * @return
     */
    public QueryResponseResult<TeachplanMediaPub> getmedia(List<String> teachplanIds) throws IOException {
        if (CollectionUtils.isEmpty(teachplanIds)) {
            return new QueryResponseResult<>(CommonCode.SUCCESS, new QueryResult());
        }
        SearchRequest searchRequest = new SearchRequest(elasticsearchConfig.getCourseMediaIndex());
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.termsQuery("teachplan_id", teachplanIds.toArray(new String[]{})));
        // 设置查询字段
        searchSourceBuilder.fetchSource(elasticsearchConfig.getCourseMediaSourceField().split(","), new String[]{});
        SearchResponse search = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);

        //获取搜索结果
        SearchHits hits = search.getHits();
        SearchHit[] searchHits = hits.getHits();
        //数据列表
        List<TeachplanMediaPub> teachplanMediaPubList = new ArrayList<>();
        for (SearchHit hit : searchHits) {
            TeachplanMediaPub teachplanMediaPub = new TeachplanMediaPub();
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            //取出课程计划媒资信息
            String courseid = (String) sourceAsMap.get("courseid");
            String media_id = (String) sourceAsMap.get("media_id");
            String media_url = (String) sourceAsMap.get("media_url");
            String teachplan_id = (String) sourceAsMap.get("teachplan_id");
            String media_fileoriginalname = (String) sourceAsMap.get("media_fileoriginalname");
            teachplanMediaPub.setCourseId(courseid);
            teachplanMediaPub.setMediaUrl(media_url);
            teachplanMediaPub.setMediaFileOriginalName(media_fileoriginalname);
            teachplanMediaPub.setMediaId(media_id);
            teachplanMediaPub.setTeachplanId(teachplan_id);
            //将数据加入列表
            teachplanMediaPubList.add(teachplanMediaPub);
        }
        //构建返回课程媒资信息对象
        QueryResult<TeachplanMediaPub> queryResult = new QueryResult<>();
        queryResult.setList(teachplanMediaPubList);
        return new QueryResponseResult<>(CommonCode.SUCCESS, queryResult);
    }
}
