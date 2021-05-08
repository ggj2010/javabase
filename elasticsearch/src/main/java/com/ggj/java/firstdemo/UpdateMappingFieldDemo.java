package com.ggj.java.firstdemo;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthResponse;
import org.elasticsearch.action.admin.indices.alias.IndicesAliasesRequestBuilder;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.ClusterAdminClient;
import org.elasticsearch.client.IndicesAdminClient;
import org.elasticsearch.client.Requests;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;

import com.alibaba.fastjson.JSONObject;
import com.ggj.java.firstdemo.com.ggj.java.bean.ItemInfo;

import lombok.extern.slf4j.Slf4j;


/**
 * 修改已经mapping的field
 * 支持类型：string
 *
 * @author:gaoguangjin
 * @date 2016/11/14 9:24
 */
@Slf4j
public class UpdateMappingFieldDemo {
    public static final String INDEX_NAME_v1 = "updatemappingfield_v1";
    public static final String ALIX_NAME = "updatemappingfield";
    public static final String INDEX_NAME_v2 = "updatemappingfield_v2";
    public static final String INDEX_TYPE = "itemInfo";
    private static TransportClient client = null;

    static {
        try {
            Map<String, String> map = new HashMap();
            // 基础名称
            map.put("cluster.name", "my-application");
            Settings.Builder settings = Settings.builder().put(map);
            client = TransportClient.builder().settings(settings).build()
                    .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300));
        } catch (UnknownHostException e) {
            log.error("init error:{}", e.getMessage());
        }
    }

    /**
     * 索引 别名 就像一个快捷方式或软连接，可以指向一个或多个索引，也可以给任何需要索引名的 API 使用。别名带给我们极大的灵活性，
     * 比如 我们线上使用一个索引名为index_a的结构，里面有一个类型现在不满足要求，现在需要修改，因为elasticsearch不支持直接修改类型，所以我们必须要重新建立一个新的索引
     * 然后将久索引的数据拷贝过去。 但是如果让新的索引起作用，我们需要要修改引用代码，因为索引名称更换了，但是如果我们一开始创建索引的时候就给索引增加一个别名
     * 使用的时候都是使用index_alis 无论软连接的指向是什么索引，对外暴露的始终都是index_alis
     */
    public static void main(String[] args) {
        beforeUpdate();
        update();
    }

    private static void update() {
        try {
            IndicesAdminClient indicesAdminClient = client.admin().indices();
            if (indicesAdminClient.prepareExists(INDEX_NAME_v2).execute().actionGet().isExists()) {
                indicesAdminClient.delete(new DeleteIndexRequest(INDEX_NAME_v2)).actionGet();
            }
            indicesAdminClient.prepareCreate(INDEX_NAME_v2).addMapping(INDEX_TYPE, getItemInfoMapping()).execute().actionGet();
            //0、更新mapping
            updateMapping();
            //1、更新数据
            reindexData(indicesAdminClient);
            //2、realias 重新建立连接
            indicesAdminClient.prepareAliases().removeAlias(INDEX_NAME_v1, ALIX_NAME).addAlias(INDEX_NAME_v2, ALIX_NAME).execute().actionGet();
        } catch (Exception e) {
            log.error("beforeUpdate error:{}" + e.getLocalizedMessage());
        }

    }

    private static void updateMapping() throws IOException {
        PutMappingRequest mapping = Requests.putMappingRequest(INDEX_NAME_v2).type(INDEX_TYPE).source(getUpdateItemInfoMapping().string());
      client.admin().indices().putMapping(mapping).actionGet();
    }


    private static void reindexData(IndicesAdminClient indicesAdminClient) {
        //查询原来的所有数据,TimeValue是需要保存的时长
        SearchResponse searchResponse = client.prepareSearch(ALIX_NAME).setTypes(INDEX_TYPE).setQuery(QueryBuilders.matchAllQuery()).
                setSearchType(SearchType.SCAN).setScroll(new TimeValue(20000))
                .setSize(100).execute().actionGet();
        //当前id
        String scrollId = searchResponse.getScrollId();
        while (StringUtils.isNotEmpty(scrollId)) {
            BulkRequestBuilder bulkRequestBuilder = client.prepareBulk();
            SearchResponse scrollSearchResponse = client.prepareSearchScroll(scrollId).setScroll(new TimeValue(20000)).execute().actionGet();
            SearchHits hits = scrollSearchResponse.getHits();
            if (hits.getHits().length > 0) {
                for (SearchHit searchHitFields : hits.getHits()) {
                    Map<String, Object> map = searchHitFields.getSource();
                    map.put("createDate",new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
                    IndexRequestBuilder indexRequestBuilder = client.prepareIndex(INDEX_NAME_v2, INDEX_TYPE).setId(searchHitFields.getId()).setSource(searchHitFields.getSource()).setOpType(IndexRequest.OpType.INDEX);
                    bulkRequestBuilder.add(indexRequestBuilder);
                }
                BulkResponse bulkResponse = bulkRequestBuilder.execute().actionGet();
                if (bulkResponse.hasFailures()) {
                    log.error("reindex失败 : {}", bulkResponse.buildFailureMessage());
                } else {
                    log.info("reindex {}条成功:", hits.getHits().length);
                }
            } else {
                break;
            }
        }
    }

    private static void prepareData(IndicesAdminClient indicesAdminClient) throws InterruptedException {
        BulkRequestBuilder bulkRequestBuilder = client.prepareBulk();
        //批量添加
        for (int i = 0; i < 1000; i++) {
            ItemInfo iteminfo = new ItemInfo(i, "商品" + i, new Random().nextFloat(), new Date());
            // 当opType是Index时,如果文档id存在,更新文档,否则创建文档 当opType是Create,如果文档id存在,抛出文档存在的错误 *
            IndexRequestBuilder indexRequestBuilder = client.prepareIndex(INDEX_NAME_v1, INDEX_TYPE).setId(i + "").setSource(JSONObject.toJSONString(iteminfo)).setOpType(IndexRequest.OpType.INDEX);
            bulkRequestBuilder.add(indexRequestBuilder);
            //数据日期不一样
        }
        //立即刷新
        bulkRequestBuilder.setRefresh(true);
        BulkResponse bulkResponse = bulkRequestBuilder.execute().actionGet();
        if (bulkResponse.hasFailures()) {
            log.error("bulk index error:{}", bulkResponse.buildFailureMessage());
        } else {
            log.info("index docs : {}", bulkResponse);
        }
    }


    private static void beforeUpdate() {
        try {
            IndicesAdminClient indicesAdminClient = client.admin().indices();
            if (indicesAdminClient.prepareExists(INDEX_NAME_v1).execute().actionGet().isExists()) {
                indicesAdminClient.delete(new DeleteIndexRequest(INDEX_NAME_v1)).actionGet();
            }
            indicesAdminClient.prepareCreate(INDEX_NAME_v1).addMapping(INDEX_TYPE, getItemInfoMapping()).execute().actionGet();
            //等待集群shard,防止No shard available for 异常
            ClusterAdminClient clusterAdminClient = client.admin().cluster();
            clusterAdminClient.prepareHealth().setWaitForYellowStatus().execute().actionGet(5000);
            //创建别名alias
            indicesAdminClient.prepareAliases().addAlias(INDEX_NAME_v1, ALIX_NAME).execute().actionGet();
            prepareData(indicesAdminClient);
        } catch (Exception e) {
            log.error("beforeUpdate error:{}" + e.getLocalizedMessage());
        }
    }

    /**
     * 初始化时候的mappgin
     *
     * @return
     * @throws IOException
     */
    public static String getItemInfoMapping() throws IOException {
        XContentBuilder mapping = null;
        try {
            mapping = XContentFactory.jsonBuilder().startObject()
                    // 索引库名（类似数据库中的表）
                    .startObject(INDEX_TYPE)
                    //匹配全部
                    .startObject("properties")
                    //根据只对content这个Fields分词
                    .startObject("createDate").field("type", "Date").field("format", "yyyy-MM-dd HH:mm:ss")
                    .endObject()
                    .endObject()
                    .endObject().endObject();
        } catch (IOException e) {
            log.error("getItemInfoMapping error:{}", e.getLocalizedMessage());
        }
        return mapping.string();
    }

    /**
     * 新mapping
     *
     * @return
     */
    private static XContentBuilder getUpdateItemInfoMapping() {
        XContentBuilder mapping = null;
        try {
            mapping = XContentFactory.jsonBuilder().startObject()
                    // 索引库名（类似数据库中的表）
                    .startObject(INDEX_TYPE)
                    //匹配全部
                    .startObject("properties")
                    //根据只对content这个Fields分词
                    .startObject("createDate").field("type", "Date").field("format", "yyyy-MM-dd")
                    .endObject()
                    .endObject()
                    .endObject().endObject();
        } catch (IOException e) {
            log.error("getUpdateItemInfoMapping error:{}", e.getLocalizedMessage());
        }
        return mapping;
    }

}
