package com.ggj.java.firstdemo;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.admin.cluster.health.ClusterHealthResponse;
import org.elasticsearch.action.admin.indices.alias.IndicesAliasesRequest;
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
import org.elasticsearch.client.ClusterAdminClient;
import org.elasticsearch.client.IndicesAdminClient;
import org.elasticsearch.client.Requests;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;

import lombok.extern.slf4j.Slf4j;



/**
 * @author:gaoguangjin
 * @date 2016/10/24 15:29
 */
@Slf4j
public class CrudDemo {

    private static TransportClient client = null;
    //索引名称要小写
    private static String INDEX_NAME = "elsdb";
    private static String INDEX_ALIAS_NAME_VERSION_ONE = "elsdb_alis_v1";
    private static String INDEX_ALIAS_NAME_ALIS = "elsdb_alis";
    private static String INDEX_ALIAS_NAME_VERSION_TWO = "elsdb_alis_v2";
    private static String TYPE_NAME = "tb_system";
    private static String FIELD_NAME = "user_name";

    static {
        try {
            Map<String, String> map = new HashMap();
            // 基础名称
            map.put("cluster.name", "my-application-A");
            Settings.Builder settings = Settings.builder().put(map);
            client = TransportClient.builder().settings(settings).build()
                    .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300));
        } catch (UnknownHostException e) {
            log.error("init error:{}", e.getMessage());
        }
    }

    public static void main(String[] args) {
        try {
            index();
            //等待集群shard,防止No shard available for 异常
            ClusterAdminClient clusterAdminClient = client.admin().cluster();
            clusterAdminClient.prepareHealth().setWaitForYellowStatus().execute().actionGet(5000);
           // analyze();
        } catch (Exception e) {
            log.error("main error:{}", e.getMessage());
        }finally {
            client.close();
        }
    }

    /**
     * ttp://localhost:9200/index/_analyze?text=做人如果没有梦想那和咸鱼有什么区别勒&analyzer=pinyin_analyzer
     * ttp://localhost:9200/index/_analyze?text=今天是个好天气啊&analyzer=ik_max_word
     */
    private static void analyze() {
        IndicesAdminClient indicesAdminClient = client.admin().indices();
        String analyzerName="pinyin";
        String text="默认的拼音例子";
        showAnaylzerText(indicesAdminClient,analyzerName,text);
        //自定义分析器必须要指定indexName,插件的就不用了
         analyzerName="pinyin_analyzer";
        text="做人如果没有梦想那和咸鱼有什么区别勒";
        showAnaylzerText(indicesAdminClient,analyzerName,text);
        //官网的demo例子
        analyzerName="my_analyzer";
        text="The quick & brown fox is a dog";
        showAnaylzerText(indicesAdminClient,analyzerName,text);
        //验证ik分词
        analyzerName="ik_max_word";
        text="好好学习天天向上！";
        showAnaylzerText(indicesAdminClient,analyzerName,text);
    }

    private static void showAnaylzerText(IndicesAdminClient indicesAdminClient,String analyzerName, String text) {
        AnalyzeResponse analyzeResponse = indicesAdminClient.analyze(new AnalyzeRequest(INDEX_NAME).analyzer(analyzerName).text(text)).actionGet();
        List<AnalyzeResponse.AnalyzeToken> token=analyzeResponse.getTokens();
        for (AnalyzeResponse.AnalyzeToken analyzeToken : token) {
            log.info(analyzerName+": {}",analyzeToken.getTerm());
        }

    }

    /**
     * http://es.xiaoleilu.com/010_Intro/25_Tutorial_Indexing.html
     * http://es.xiaoleilu.com/070_Index_Mgmt/05_Create_Delete.html
     * 索引相关的
     */
    private static void index() throws IOException, InterruptedException {
        // cluster()，产生一个允许从集群中执行action或操作的client；
        IndicesAdminClient indicesAdminClient = client.admin().indices();
        //创建索引
        if (checkExistsIndex(indicesAdminClient, INDEX_NAME)) {
            deleteIndex(indicesAdminClient, INDEX_NAME);
        }
//        String settings = getIndexSetting();
//        CreateIndexResponse createIndexResponse = indicesAdminClient.prepareCreate(INDEX_NAME).setSettings(settings).execute().actionGet();

        CreateIndexResponse createIndexResponse = indicesAdminClient.prepareCreate(INDEX_NAME).setSettings().execute().actionGet();
//        log.info("创建索引{}：{}", INDEX_NAME, createIndexResponse.getContext());

        //索引的相关配置操作
        indexConfig(indicesAdminClient, INDEX_NAME);

//        indexMapping(indicesAdminClient, INDEX_NAME, TYPE_NAME);
    }

    /**
     *
     * @param indicesAdminClient
     * @param indexName
     * @param typeName
     * @throws IOException
     */
    private static void indexMapping(IndicesAdminClient indicesAdminClient, String indexName, String typeName) throws IOException {
        //type就相当于表的
        String typeSource=getIndexTypeSource(typeName,FIELD_NAME);
        //typetwo
        PutMappingRequest mapping = Requests.putMappingRequest(indexName).type(typeName).source("typeSource");

        PutMappingResponse putMappingResponseTwo = indicesAdminClient.putMapping(mapping).actionGet();
    }

    /**
     * DELETE /index_one,index_two
     * DELETE /index_*
     * 删除所有
     * DELETE /_all
     * 删除索引
     *
     * @param indicesAdminClient
     * @param indexName
     */
    private static void deleteIndex(IndicesAdminClient indicesAdminClient, String indexName) {
        //删除索引 new DeleteIndexRequest("a","b") 支持多个
        DeleteIndexResponse deleteIndexResponse = indicesAdminClient.delete(new DeleteIndexRequest(indexName)).actionGet();
        log.info("删除索引{}：{}", indexName, deleteIndexResponse.getContext());
    }


    /**
     * 索引的相关操作
     *
     * @param indicesAdminClient
     * @param indexName
     * @throws IOException
     */
    private static void indexConfig(IndicesAdminClient indicesAdminClient, String indexName) throws IOException {
        //settings 设置
        String settings = getIndexSetting();
        // PUT /my_temp_index/_settings updatesettings
        showIndexSettings(indicesAdminClient,indexName);
        UpdateSettingsResponse updateSettingsResponse = indicesAdminClient.prepareUpdateSettings(indexName).setSettings(settings).execute().actionGet();
        log.info("更新 index setting:{}", updateSettingsResponse);


        //更新索引settings之前要关闭索引
        indicesAdminClient.close(new CloseIndexRequest().indices(indexName)).actionGet();
        //配置拼音自定义分析器
        indicesAdminClient.prepareUpdateSettings(indexName).setSettings(getIndexPinYinSetting()).execute().actionGet();
        //自定义分析器
        indicesAdminClient.prepareUpdateSettings(indexName).setSettings(getIndexDemoSetting()).execute().actionGet();
        //打开索引
        indicesAdminClient.open(new OpenIndexRequest().indices(indexName)).actionGet();

        //索引别名映射
        createAliasIndex(indicesAdminClient);

        showIndexSettings(indicesAdminClient,indexName);
    }

    /**
     * 索引 别名 就像一个快捷方式或软连接，可以指向一个或多个索引，也可以给任何需要索引名的 API 使用。别名带给我们极大的灵活性，
     * 比如 我们线上使用一个索引名为index_a的结构，里面有一个类型现在不满足要求，现在需要修改，因为elasticsearch不支持直接修改类型，所以我们必须要重新建立一个新的索引
     * 然后将久索引的数据拷贝过去。 但是如果让新的索引起作用，我们需要要修改引用代码，因为索引名称更换了，但是如果我们一开始创建索引的时候就给索引增加一个别名
     * 使用的时候都是使用index_alis 无论软连接的指向是什么索引，对外暴露的始终都是index_alis
     * @param indicesAdminClient
     */
    private static void createAliasIndex(IndicesAdminClient indicesAdminClient) {
//        1 创建索引 elsdb_alis_v1。
//        2 将别名 elsdb_alis 指向 elsdb_alis_v1
//        3 然后，我们决定修改索引elsdb_alis_v1中一个字段的映射。当然我们不能修改现存的映射，索引我们需要重新索引数据。首先，我们创建有新的映射的索引 elsdb_alis_v2。
//        4 然后我们从将数据从 elsdb_alis_v1 迁移到 elsdb_alis_v2，下面的过程在【重新索引】中描述过了。一旦我们认为数据已经被正确的索引了，我们就将别名指向新的索引。


        //创建索引
        if (checkExistsIndex(indicesAdminClient, INDEX_ALIAS_NAME_VERSION_ONE)) {
            deleteIndex(indicesAdminClient, INDEX_ALIAS_NAME_VERSION_ONE);
        }

        indicesAdminClient.prepareCreate(INDEX_ALIAS_NAME_VERSION_ONE).setSettings().execute().actionGet();
        //添加alias 所有别名
        indicesAdminClient.prepareAliases().addAlias(INDEX_ALIAS_NAME_VERSION_ONE,INDEX_ALIAS_NAME_ALIS).execute().actionGet();

        GetAliasesResponse getAliasesResponse = indicesAdminClient.getAliases(new GetAliasesRequest().indices(INDEX_ALIAS_NAME_ALIS)).actionGet();
        //log.info("getAliasesResponse index setting:{}", getAliasesResponse.getAliases());

        indicesAdminClient.prepareAliases().removeAlias(INDEX_ALIAS_NAME_VERSION_ONE,INDEX_ALIAS_NAME_ALIS).addAlias(INDEX_ALIAS_NAME_VERSION_TWO,INDEX_ALIAS_NAME_ALIS);
    }

    private static void showIndexSettings(IndicesAdminClient indicesAdminClient, String indexName) {
        GetSettingsResponse settingInfo = indicesAdminClient.getSettings(new GetSettingsRequest().indices(indexName)).actionGet();
//        log.info("显示 index setting:{}", settingInfo.getIndexToSettings().get(indexName).getAsMap());
    }


    private static boolean checkExistsIndex(IndicesAdminClient indicesAdminClient, String indexName) {
        return indicesAdminClient.prepareExists(indexName).execute().actionGet().isExists();
    }

    /**
     number_of_shards
     定义一个索引的主分片个数，默认值是 `5`。这个配置在索引创建后不能修改。
     number_of_replicas
     每个主分片的复制分片个数，默认是 `1`。这个配置可以随时在活跃的索引上修改。
     * When changing the number of replicas the index needs to be open. Changing the number of replicas on a closed index might prevent the index to be opened correctly again.
     * @return
     * @throws IOException
     */
    public static String getIndexSetting() throws IOException {
        //默认索引创建的分片数都是5
        XContentBuilder mapping = XContentFactory.jsonBuilder()
                .startObject()
                //.field("number_of_shards", "5")
                .field("number_of_replicas", "1")
                .endObject();
        return mapping.string();
    }

    /**
     * https://github.com/medcl/elasticsearch-analysis-pinyin/tree/v1.7.5
     * pinyin配置分析器
     * @return
     */
    public static String getIndexPinYinSetting() throws IOException {
        XContentBuilder mapping = XContentFactory.jsonBuilder()
                .startObject()
                    .startObject("analysis")
                        .startObject("analyzer")
                            .startObject("pinyin_analyzer")
                                .field("tokenizer", "my_pinyin")
                                .array("filter","nGram","word_delimiter")
                            .endObject()
                         .endObject()
                        .startObject("tokenizer")
                            .startObject("my_pinyin")
                                .field("type", "pinyin")
                                .field("first_letter","prefix")
                                .field("padding_char","")
                            .endObject()
                         .endObject()
                    .endObject()
                .endObject();
        return mapping.string();
    }

    /**
     * 分析器 是三个顺序执行的组件的结合（字符过滤器，分词器，标记过滤器）
     *     http://es.xiaoleilu.com/070_Index_Mgmt/20_Custom_Analyzers.html
     * @return
     */
    public static String getIndexDemoSetting() throws IOException {
            XContentBuilder mapping = null;
            try {
                mapping = XContentFactory.jsonBuilder()
                        .startObject()
                            .startObject("analysis")
                                //字符过滤器
                                .startObject("char_filter")
                                    .startObject("&_to_and")
                                        .field("type", "mapping")
                                        .field("mappings","&=> and")
                                    .endObject()
                                .endObject()
                                .startObject("filter")
                                    .startObject("my_stopwords")
                                        .field("type", "stop")
                                        .field("stopwords","the","a")
                                    .endObject()
                                .endObject()
                                //分析器
                                .startObject("analyzer")
                                    .startObject("my_analyzer")
                                        //自定义
                                        .field("type", "custom")
                                          //用 html_strip 字符过滤器去除所有的 HTML 标签
                                        .array("char_filter","html_strip","&_to_and")
                                        //使用 standard 分词器分割单词
                                        .field("tokenizer", "standard")
                                        // 使用 lowercase 标记过滤器将词转为小写,stop 标记过滤器去除一些自定义停用词
                                        .array("filter","lowercase","my_stopwords")
                                    .endObject()
                                .endObject()
                            .endObject()
                        .endObject();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return mapping.string();
    }


    /**
     * {
     * "fulltext": {
     * "_all": {
     * "analyzer": "ik_max_word",
     * "search_analyzer": "ik_max_word",
     * "term_vector": "no",
     * "store": "false"
     * },
     * "properties": {
     * "content": {
     * "type": "text",
     * "analyzer": "ik_max_word",
     * "search_analyzer": "ik_max_word",
     * "include_in_all": "true",
     * "boost": 8
     * }
     * }
     * }
     * }'
     *
     * @param typeName
     * @return
     */
    private static String getIndexTypeSource(String typeName, String fieldName) throws IOException {
        XContentBuilder mapping = XContentFactory.jsonBuilder()
                .startObject()
                // 索引库名（类似数据库中的表）
                .startObject(typeName)
                //匹配全部
                //.startObject("_all").field("analyzer","ik_max_word").field("search_analyzer","ik_max_word").field("term_vector","no").field("store","false").endObject()
                .startObject("properties")
                //根据只对content这个Fields分词
                .startObject(fieldName).field("type", "string").field("store", "no")
                .field("term_vector", "with_positions_offsets").field("analyzer", "ik_max_word")
                .field("search_analyzer", "ik_max_word").field("include_in_all", "true").field("boost", 8)
                .endObject()
                .endObject()
                .endObject()
                .endObject();
        return mapping.string();
    }

}
