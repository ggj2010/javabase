package com.ggj.webmagic.elasticsearch;

import com.alibaba.fastjson.JSONObject;
import com.ggj.webmagic.tieba.bean.ContentBean;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.admin.indices.mapping.get.GetMappingsRequest;
import org.elasticsearch.action.admin.indices.mapping.get.GetMappingsResponse;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.client.Requests;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.cluster.metadata.MappingMetaData;
import org.elasticsearch.common.collect.ImmutableOpenMap;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import static jdk.nashorn.internal.objects.NativeRegExp.source;

/**
 * @author:gaoguangjin
 * @date 2016/9/29 14:31
 */
@Service
@Slf4j
public class ElasticSearch implements InitializingBean{
    @Getter
    public  BlockingQueue<ContentBean> beanBlockingDeque=new ArrayBlockingQueue<ContentBean>(10000);
    @Getter
    private TransportClient transportClient=null;
    public  static  final String INDEX_NAME="masterdb";
    public static final String TIEABA_CONTENT_TYPE="tieba_content";
    //对title字段进行zk分词
    public static final String TIEABA_CONTENT_FIELD="title";

    public void addTieBaContentIndex() {
        List<ContentBean> list=new ArrayList<>();
        beanBlockingDeque.drainTo(list);
        for (ContentBean contentBean : list) {
            transportClient.prepareIndex(INDEX_NAME,TIEABA_CONTENT_TYPE).setId(contentBean.getId()).setSource(JSONObject.toJSONString(contentBean)).execute().actionGet();
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Map<String, String> map = new HashMap();
        //基础名称
        map.put("cluster.name", "my-application-A");
        Settings.Builder settings = Settings.builder().put(map);
        try {
            transportClient = TransportClient.builder().settings(settings).build()
                    .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300));
            //查询mapping是否存在，已存在就不创建了
            GetMappingsResponse getMappingsResponse = transportClient.admin().indices().getMappings(new GetMappingsRequest().indices(INDEX_NAME)).actionGet();
            ImmutableOpenMap<String, ImmutableOpenMap<String, MappingMetaData>> indexToMappings = getMappingsResponse.getMappings();
           if(indexToMappings.get(INDEX_NAME).get(TIEABA_CONTENT_TYPE)==null) {
               //创建zk分词mapping
               PutMappingRequest mapping = Requests.putMappingRequest(INDEX_NAME).type(TIEABA_CONTENT_TYPE).source(createIKMapping(TIEABA_CONTENT_TYPE, TIEABA_CONTENT_FIELD).string());
               mapping.updateAllTypes(true);
               transportClient.admin().indices().putMapping(mapping).actionGet();
           }
        } catch (Exception e) {
           log.error("初始化 elasticsearch cliet error"+e.getLocalizedMessage());
        }
    }

    /**
     * 创建mapping分词IK索引
     * Elasticsearch的mapping一旦创建，只能增加字段，而不能修改已经mapping的字段
     * @param indexType
     * @return
     */
    private static XContentBuilder createIKMapping(String indexType,String field) {
        XContentBuilder mapping = null;
        try {
            mapping = XContentFactory.jsonBuilder().startObject()
                    // 索引库名（类似数据库中的表）
                    .startObject(indexType)
                    //匹配全部
                    .startObject("properties")
                    //根据只对content这个Fields分词
                    .startObject(field).field("type","string").field("store","no")
                    .field("term_vector","with_positions_offsets").field("analyzer","ik_max_word")
                    .field("search_analyzer","ik_max_word").field("include_in_all","true").field("boost",8)
                    .endObject()
                    .endObject()
                    .endObject().endObject();
        } catch (IOException e) {
            log.error("创建mapping分词IK索引 error"+e.getLocalizedMessage());
        }
        return mapping;
    }
}
