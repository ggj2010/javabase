package com.ggj.java.firstdemo.com.ggj.java.hst.es;

import com.alibaba.fastjson.JSONObject;
import com.ggj.java.firstdemo.com.ggj.java.bean.Goods;
import com.ggj.java.firstdemo.com.ggj.java.bean.User;
import com.ggj.java.firstdemo.com.ggj.java.hst.bean.Dictionary;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.admin.indices.alias.get.GetAliasesRequest;
import org.elasticsearch.action.admin.indices.alias.get.GetAliasesResponse;
import org.elasticsearch.action.admin.indices.analyze.AnalyzeRequest;
import org.elasticsearch.action.admin.indices.analyze.AnalyzeResponse;
import org.elasticsearch.action.admin.indices.close.CloseIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingResponse;
import org.elasticsearch.action.admin.indices.open.OpenIndexRequest;
import org.elasticsearch.action.admin.indices.settings.get.GetSettingsRequest;
import org.elasticsearch.action.admin.indices.settings.get.GetSettingsResponse;
import org.elasticsearch.action.admin.indices.settings.put.UpdateSettingsResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.ClusterAdminClient;
import org.elasticsearch.client.IndicesAdminClient;
import org.elasticsearch.client.Requests;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.FuzzyQueryBuilder;
import org.elasticsearch.index.query.IdsQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermsQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author:gaoguangjin
 * @date 2016/10/24 15:29
 */
@Slf4j
public class ElasticService {
    public final static String indexName = "htsdb";
    public final static String type = "hts";

    public static TransportClient initAndGetTransportClient() {
        TransportClient client = null;

        //创建zk分词 mapping
        try {
            Map<String, String> map = new HashMap();
            //基础名称
            map.put("cluster.name", "my-application");
            Settings.Builder settings = Settings.builder().put(map);
            client = TransportClient.builder().settings(settings).build()
                    .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("129.211.29.93"), 9300));
            //分词
            zkfc(indexName, type, client);
    Thread.sleep(3000);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            //  client.close();
        }
        return client;
    }


    private static void zkfc(String indexName, String type, TransportClient client) throws IOException {
        IndicesAdminClient indicesAdminClient = client.admin().indices();

        //查询索引是否存在
        if (indicesAdminClient.prepareExists(indexName).execute().actionGet().isExists()) {
            indicesAdminClient.delete(new DeleteIndexRequest(indexName)).actionGet();
        }
        //1、创建索引
        if (!indicesAdminClient.prepareExists(indexName).execute().actionGet().isExists()) {
            CreateIndexResponse createIndexResponse = indicesAdminClient.prepareCreate(indexName).setSettings().execute().actionGet();
            log.info("创建索引{}：{}", indexName, createIndexResponse.getContext());
            //创建zk分词
            PutMappingRequest mapping = Requests.putMappingRequest(indexName).type(type).source(createIKMapping(type).string());
            client.admin().indices().putMapping(mapping).actionGet();
        }
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
                    .startObject("properties")

                    //根据只对content这个Fields分词
                    .startObject("chineseName").field("type", "string").field("store", "no")
                    .field("term_vector", "with_positions_offsets").field("analyzer", "ik_max_word")
                    .field("search_analyzer", "ik_max_word").field("include_in_all", "true")
                    .endObject()

                    .startObject("engName").field("type", "string").field("analyzer", "standard")
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




}
