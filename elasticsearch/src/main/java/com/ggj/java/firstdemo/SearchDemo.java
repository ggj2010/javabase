package com.ggj.java.firstdemo;

import com.alibaba.fastjson.JSONObject;
import com.ggj.java.firstdemo.com.ggj.java.bean.Goods;
import com.ggj.java.firstdemo.com.ggj.java.bean.User;
import jdk.nashorn.internal.objects.annotations.Getter;
import jdk.nashorn.internal.objects.annotations.Setter;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.IndicesAdminClient;
import org.elasticsearch.client.Requests;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;

import static org.apache.lucene.index.TwoPhaseCommitTool.execute;
import static org.elasticsearch.index.query.QueryBuilders.boolQuery;


/**
 * Elasticsearch是实时分布式搜索和分析引擎
 * http://es.xiaoleilu.com/030_Data/15_Get.html
 * Relational DB -> Databases -> Tables -> Rows -> Columns
 * Elasticsearch -> Indices  -> Types  -> Documents -> Fields
 *
 * @author:gaoguangjin
 * @date 2016/9/20 15:29
 */
@Slf4j
public class SearchDemo {
    public static void main(String[] args) throws IOException {
        test();
    }


    private static void test() {
        TransportClient client = null;
        String indexName = "masterdb";
        String type = "user_info";
        //创建zk分词 mapping
        String zkType = "zkfc";
        try {
            Map<String, String> map = new HashMap();
            //基础名称
            map.put("cluster.name", "my-application");
            Settings.Builder settings = Settings.builder().put(map);
            client = TransportClient.builder().settings(settings).build()
                    .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300));
            //crud
            SearchResponse response = crud(indexName, type, client);
            //分词
            //SearchResponse response=zkfc(indexName,zkType,client);
            SearchHits hits = response.getHits();
            for (SearchHit searchHitFields : hits.getHits()) {
                log.info("相关数据：" + searchHitFields.getSourceAsString());
            }
            //删除
            // delete(client,indexName,zkType,hits);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            client.close();
        }

    }

    private static void delete(TransportClient client, String indexName, String zkType, SearchHits hits) {
        for (SearchHit searchHitFields : hits.getHits()) {
            String id = searchHitFields.getId();
            log.info("删除数据 id={} {}", id, searchHitFields.getSourceAsString());
            client.prepareDelete(indexName, zkType, id).execute();
            GetResponse response = client.prepareGet(indexName, zkType, id).execute().actionGet();
            log.info("删除后数据 id={} {}", id, response.getSourceAsString());
        }
    }

    private static SearchResponse zkfc(String indexName, String zkType, TransportClient client) throws IOException {
        //返回一个可以执行管理性操作的客户端
        //1) cluster()，产生一个允许从集群中执行action或操作的client；
        //2) indices()，产生一个允许从索引中执行action或操作的client。

        // cluster()，产生一个允许从集群中执行action或操作的client；
        IndicesAdminClient indicesAdminClient = client.admin().indices();
        //1、创建索引
        if (!indicesAdminClient.prepareExists(indexName).execute().actionGet().isExists()) {
            CreateIndexResponse createIndexResponse = indicesAdminClient.prepareCreate(indexName).setSettings().execute().actionGet();
            log.info("创建索引{}：{}", indexName, createIndexResponse.getContext());
        }
        //创建zk分词
        PutMappingRequest mapping = Requests.putMappingRequest(indexName).type(zkType).source(createIKMapping(zkType).string());
        client.admin().indices().putMapping(mapping).actionGet();
        Goods goodsOne = new Goods(1, "iphone7 iphone7plus 钢化膜 玻璃膜 苹果 苹果7/7plus 贴膜 买就送清水", "http://m.ule.com/item/detail/1771161");
        Goods goodsTwo = new Goods(2, "苹果 (Apple) iPhone 7 移动联通电信4G手机 土豪金 32G 标配", "http://m.ule.com/item/detail/1799356");
        Goods goodsThree = new Goods(3, "苹果 Apple iPhone 7 (A1660) 128G 金色 移动联通电信 全网通 4G手机", "http://m.ule.com/item/detail/1781429");
        client.prepareIndex(indexName, zkType).setId(1 + "").setSource(JSONObject.toJSONString(goodsOne)).execute().actionGet();
        client.prepareIndex(indexName, zkType).setId(2 + "").setSource(JSONObject.toJSONString(goodsTwo)).execute().actionGet();
        client.prepareIndex(indexName, zkType).setId(3 + "").setSource(JSONObject.toJSONString(goodsThree)).execute().actionGet();

        SearchResponse response = client.prepareSearch(indexName)
                .setTypes(zkType)
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(QueryBuilders.matchQuery("title", "苹果")
                ).execute().actionGet();
        return response;
    }

    /**
     * 简单增删改
     *
     * @param indexName
     * @param type
     * @param client
     * @return
     */
    private static SearchResponse crud(String indexName, String type, TransportClient client) {
        //返回一个可以执行管理性操作的客户端
        //1) cluster()，产生一个允许从集群中执行action或操作的client；
        //2) indices()，产生一个允许从索引中执行action或操作的client。

        // cluster()，产生一个允许从集群中执行action或操作的client；
        IndicesAdminClient indicesAdminClient = client.admin().indices();
        //查询索引是否存在
        if (indicesAdminClient.prepareExists(indexName).execute().actionGet().isExists()) {
            indicesAdminClient.delete(new DeleteIndexRequest(indexName)).actionGet();
        }
        //1、创建索引
        CreateIndexResponse createIndexResponse = indicesAdminClient.prepareCreate(indexName).setSettings().execute().actionGet();
        log.info("创建索引{}：{}", indexName, createIndexResponse.getContext());

        //设定某些字段name不analyzed 不分词
        PutMappingRequest mapping = Requests.putMappingRequest(indexName).type(type).source(createNotAnalyzedMapping(type, Arrays.asList("first_name", "last_name")));
        client.admin().indices().putMapping(mapping).actionGet();

        //add 索引
        User user = new User();
        user.setId(1);
        user.setAge(10);
        user.setFirst_name("高");
        user.setLast_name("广金哈");
        user.setAbout("java");
        user.setInterests(new String[]{"java", "redis"});


        client.prepareIndex(indexName, type).setId(user.getId() + "").
                setSource(JSONObject.toJSONString(user)).execute().actionGet();

        // find 查询
        //根据id查询
//            SearchResponse response = client.prepareSearch(indexName)
//                    .setTypes(type)
//                    .setQuery(queryByIds()).execute().actionGet();

        //根据
        SearchResponse response = client.prepareSearch(indexName)
                .setTypes(type)
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                //相当于select * from user_info where about="java"
//                    .setQuery(QueryBuilders.termQuery("first_name", "高")
//                    .setQuery(QueryBuilders.termQuery("about", "java")
//                    .setQuery(QueryBuilders.termsQuery("first_name","高"))
                //相当于select * from user_info where age>=10 and age <=20
//                    .setQuery(QueryBuilders.rangeQuery("age").from(10).to(20))
                //select * from user_info where last_name like "广%";
//            .setQuery( QueryBuilders.fuzzyQuery("about", "java")
//            .setQuery( QueryBuilders.wildcardQuery("about", "j*")
//            .setQuery( QueryBuilders.wildcardQuery("last_name", "*金*")
//                    .setQuery( QueryBuilders.matchAllQuery()
                .setQuery(QueryBuilders.prefixQuery("last_name", "金")

                ).execute().actionGet();

        return response;
    }

    /**
     * 创建mapping分词IK索引
     *
     * @param indexType
     * @return
     */
    private static XContentBuilder createIKMapping(String indexType) {
        XContentBuilder mapping = null;
        try {
            mapping = XContentFactory.jsonBuilder().startObject()
                    // 索引库名（类似数据库中的表）
                    .startObject(indexType)
                    //匹配全部
                    //.startObject("_all").field("analyzer","ik_max_word").field("search_analyzer","ik_max_word").field("term_vector","no").field("store","false").endObject()
                    .startObject("properties")
                    //根据只对content这个Fields分词
                    .startObject("content").field("type", "string").field("store", "no")
                    .field("term_vector", "with_positions_offsets").field("analyzer", "ik_max_word")
                    .field("search_analyzer", "ik_max_word").field("include_in_all", "true").field("boost", 8)
                    .endObject()
                    .endObject()
                    .endObject().endObject();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mapping;
    }

    /**
     * PUT /<index_name>/_mapping
     * {
     * <type_name>: {
     * properties: {
     * <column_name>: {type: string, index: not_analyzed}
     * }
     * }
     * }
     * ————————————————
     *
     * @param indexType
     * @param fieldNameList
     * @return
     */
    private static XContentBuilder createNotAnalyzedMapping(String indexType, List<String> fieldNameList) {
        XContentBuilder mapping = null;
        try {
            mapping = XContentFactory.jsonBuilder().startObject()
                    // 索引库名（类似数据库中的表）
                    .startObject(indexType)
                    //匹配全部
                    .startObject("properties");
            for (String fieldName : fieldNameList) {
                //根据只对content这个Fields分词
                mapping.startObject(fieldName).field("type", "string").field("index", "not_analyzed")
                        .endObject();
            }
            mapping.endObject()
                    .endObject().endObject();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mapping;
    }


    /**
     * 根据id查询
     *
     * @return
     */
    public static TermsQueryBuilder queryByIds() {
        Collection coll = new ArrayList();
        coll.add(1);
        TermsQueryBuilder queryBuilder = QueryBuilders.termsQuery("_id", coll);
        return queryBuilder;
    }

    public void query() {
        //where about=java
        MatchQueryBuilder queryBuilder1 = QueryBuilders.matchQuery("java", "about");
        //where about=java and age=java
        MultiMatchQueryBuilder queryBuilder2 = QueryBuilders.multiMatchQuery("java", "about", "age");
        //where last_name=广金 and first_name=高  and age<>2 or about=java
        BoolQueryBuilder queryBuilder3 = QueryBuilders.boolQuery()
                .must(QueryBuilders.termQuery("last_name", "广金"))
                .must(QueryBuilders.termQuery("first_name", "高"))
                .mustNot(QueryBuilders.termQuery("age", 2))
                .should(QueryBuilders.termQuery("about", "java"));
        //where id=1
        IdsQueryBuilder queryBuilder4 = QueryBuilders.idsQuery().ids("1");

        // 包裹查询只返回一个常数分数等于提高每个文档的查询。 where about=java
        QueryBuilders.constantScoreQuery(QueryBuilders.termQuery("about", "java"))
                .boost(2.0f);
        //模糊查询
        FuzzyQueryBuilder queryBuilder5 = QueryBuilders.fuzzyQuery("about", "ja");
    }
}
