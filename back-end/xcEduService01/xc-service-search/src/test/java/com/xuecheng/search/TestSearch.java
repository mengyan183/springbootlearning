package com.xuecheng.search;

import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.search.TotalHits;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class TestSearch {

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Test
    public void searchAll() throws IOException {
        String indexName = "xc_course";
        SearchRequest searchRequest = new SearchRequest(indexName);
        // 全部查询
//        searchRequest.source(matchAll());
        // 分页查询
//        searchRequest.source(pageSize());
        //term query
//        searchRequest.source(termQuery());
        // id 查询
//        searchRequest.source(idQuery());
//         match query
//        searchRequest.source(matchQuery());
        //多字段查询,以及权重提升
//        searchRequest.source(multiQuery());
        //bool查询
//        searchRequest.source(boolQuery());
        // filter
//        searchRequest.source(filter());
        // sort
        searchRequest.source(sort());
        SearchResponse search = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        SearchHits hits = search.getHits();
        //查询到总条数
        TotalHits totalHits = hits.getTotalHits();
        log.info(totalHits.value + "");
        for (SearchHit searchHit : hits) {
            if (searchHit == null) continue;
            Map<String, Object> sourceAsMap = searchHit.getSourceAsMap();

            log.info("Id:{},source:{}", searchHit.getId(), searchHit.getSourceAsString());
        }
    }

    public SearchSourceBuilder matchAll() {
        //构建搜索源对象
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        // 搜索全部
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        // 设置返回字段
        searchSourceBuilder.fetchSource(new String[]{"name", ""}, new String[]{});
        return searchSourceBuilder;
    }

    public SearchSourceBuilder pageSize() {
        //构建搜索源对象
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        // 搜索全部
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        //分页查询
        searchSourceBuilder.from(0);
        searchSourceBuilder.size(1);
        // 设置返回字段
        searchSourceBuilder.fetchSource(new String[]{"name"}, new String[]{});
        return searchSourceBuilder;
    }

    public SearchSourceBuilder termQuery() {
        //构建搜索源对象
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        // 关键字查询, term 的查询逻辑为 将搜索字段在指定索引树(分词数据)中进行精确匹配查询,不会对查询条件字段进行分词匹配
        searchSourceBuilder.query(QueryBuilders.termQuery("name", "sprin"));
        // 设置返回字段
        searchSourceBuilder.fetchSource(new String[]{"name"}, new String[]{});
        return searchSourceBuilder;
    }

    public SearchSourceBuilder idQuery() {
        //构建搜索源对象
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        // _id精确查询
        searchSourceBuilder.query(QueryBuilders.idsQuery().addIds("1", "2"));
        // 设置返回字段
        searchSourceBuilder.fetchSource(new String[]{"name"}, new String[]{});
        return searchSourceBuilder;
    }


    public SearchSourceBuilder matchQuery() {
        /**
         * match Query即全文检索，它的搜索方式是先将搜索字符串分词，再使用各各词条从索引中搜索。
         * match query与Term query区别是match query在搜索前先将搜索关键字分词，再拿各各词语去索引中搜索
         */

        //构建搜索源对象
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        // matchquery  全文检索,对搜索条件分词后再匹配 , operator 表示 每个文档要满足 搜索条件所有分词数据 的条件
//        searchSourceBuilder.query(QueryBuilders.matchQuery("description", "spring开发").operator(Operator.AND));
        // minimumShouldMatch 表示 每个文档要满足 搜索条件所有分词数据所占比例向上取整后的个数 的条件
        searchSourceBuilder.query(QueryBuilders.matchQuery("description", "spring开发").minimumShouldMatch("80%"));
        // 设置返回字段
        searchSourceBuilder.fetchSource(new String[]{"name", "description"}, new String[]{});
        return searchSourceBuilder;
    }

    public SearchSourceBuilder multiQuery() {
        /**
         * 多字段匹配 , 并可以通过设置 字段 的权重 提升 匹配文档的 boost
         */
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        // text字段如果中间没有空格代表搜索条件不会分词; field表示 指定字段 并改变其权重
        MultiMatchQueryBuilder field = QueryBuilders.multiMatchQuery("spring 开发", "name", "description").minimumShouldMatch("50%").field("name", 10f);
        searchSourceBuilder.query(field);
        // 设置返回字段
        searchSourceBuilder.fetchSource(new String[]{"name", "description"}, new String[]{});
        return searchSourceBuilder;
    }

    public SearchSourceBuilder boolQuery() {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        // bool查询 ,将多种查询组合在一起 , 组合方式包括 must : 条件必须全部满足 ,相当于 and ; should: 至少满足 条件(满足个数可以设置),相当于or; must_not: 必须不匹配 ,相当于not;
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(termQuery().query());
        boolQueryBuilder.should(idQuery().query());
        boolQueryBuilder.mustNot(matchQuery().query());
        boolQueryBuilder.should(multiQuery().query());
        searchSourceBuilder.query(boolQueryBuilder);
        return searchSourceBuilder;
    }

    public SearchSourceBuilder filter() {
        /**
         * 过滤是针对搜索的结果进行过滤，过滤器主要判断的是文档是否匹配，不去计算和判断文档的匹配度得分，所以过
         *过滤器性能比查询要高，且方便缓存，推荐尽量使用过滤器去实现查询或者过滤器和查询共同使用。
         */
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.should(multiQuery().query());
        // 对搜索到的结果进行过滤查询, 一般可以搭配 range 范围查询
        boolQueryBuilder.filter(QueryBuilders.termQuery("name", "spring"));
        searchSourceBuilder.query(boolQueryBuilder);
        return searchSourceBuilder;
    }

    public SearchSourceBuilder sort() {
        // 可以在字段上添加一个或多个排序，支持在keyword、date、float等类型上添加，text类型的字段上不允许添加排序。
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.sort(new FieldSortBuilder("studymodel").order(SortOrder.DESC));
        return searchSourceBuilder;
    }

    public void testHighlight() throws IOException {
        SearchRequest searchRequest = new SearchRequest("xc_course");

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder(); //source源字段过虑
        searchSourceBuilder.fetchSource(new String[]{"name", "studymodel", "price", "description"}, new String[]{});
        searchRequest.source(searchSourceBuilder);
        //匹配关键字
        MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders.multiMatchQuery("开发", "description");
        searchSourceBuilder.query(multiMatchQueryBuilder);
        //布尔查询
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(searchSourceBuilder.query());
//过虑
        boolQueryBuilder.filter(QueryBuilders.rangeQuery("price").gte(0).lte(100));
//排序
        searchSourceBuilder.sort(new FieldSortBuilder("studymodel").order(SortOrder.DESC));
        searchSourceBuilder.sort(new FieldSortBuilder("price").order(SortOrder.ASC)); //高亮设置
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.preTags("<tag>");//设置前缀 highlightBuilder.postTags("</tag>");//设置后缀
// 设置高亮字段
        highlightBuilder.fields().add(new HighlightBuilder.Field("name"));
        highlightBuilder.fields().add(new HighlightBuilder.Field("description"));
        searchSourceBuilder.highlighter(highlightBuilder);
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        SearchHits hits = searchResponse.getHits();
        SearchHit[] searchHits = hits.getHits();
        for (SearchHit hit : searchHits) {
            Map<String, Object> sourceAsMap = hit.getSourceAsMap(); //名称
            String name = (String) sourceAsMap.get("name"); //取出高亮字段内容
            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
            if (highlightFields != null) {
                HighlightField nameField = highlightFields.get("name");
                if (nameField != null) {
                    Text[] fragments = nameField.getFragments();
                    StringBuffer stringBuffer = new StringBuffer();
                    for (Text str : fragments) {
                        stringBuffer.append(str.string());
                    }
                    name = stringBuffer.toString();
                }
            }
            String studymodel = (String) sourceAsMap.get("studymodel");
            String description = (String) sourceAsMap.get("description");
            System.out.println(name);
            System.out.println(studymodel);
            System.out.println(description);
        }
    }
}
