package com.ggj.java.firstdemo.com.ggj.java.hst.Listener;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.fastjson.JSONObject;
import com.ggj.java.firstdemo.com.ggj.java.hst.bean.Dictionary;
import com.ggj.java.firstdemo.com.ggj.java.hst.bean.GetHTS;
import com.ggj.java.firstdemo.com.ggj.java.hst.bean.Result;
import com.ggj.java.firstdemo.com.ggj.java.hst.es.ElasticService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author gaoguangjin
 */
@Slf4j
public class GetHTSDataListener extends AnalysisEventListener<GetHTS> {
    private static final int BATCH_COUNT = 100;
    private List<GetHTS> list = new ArrayList<GetHTS>();
    private List<Result> resultList = new ArrayList<Result>();

    private TransportClient client;

    public GetHTSDataListener(TransportClient client) {
        this.client = client;
    }

    @Override
    public void invoke(GetHTS orderData, AnalysisContext analysisContext) {
        list.add(orderData);
        if (list.size() >= BATCH_COUNT) {
            postData();
            list.clear();
        }
    }


    private void postData() {
        for (GetHTS hts : list) {
            SearchResponse response = getSearchResponse(hts);
            Result result = new Result();
            resultList.add(result);
            result.setCargoChineseDescription(hts.getChineseName());
            result.setCargoDescription(hts.getEngName());
            if (response.getHits().getHits().length == 0) {
                log.info("无法匹配");
                result.setHtsCode("无法匹配");
            } else {
                log.info("{}size：{},{}", response.getHits().getHits().length, hts.toString(), response.getHits().getHits()[0].getSourceAsString());
                Dictionary dictionary = JSONObject.parseObject(response.getHits().getHits()[0].getSourceAsString(), Dictionary.class);
                result.setChineseName(dictionary.getChineseName());
                result.setEngName(dictionary.getEngName());
                result.setHtsCode(dictionary.getHtsCode());
                result.setScore(response.getHits().getHits()[0].getScore()+"");
            }
            /*
            for (SearchHit searchHitFields : response.getHits()) {
                //log.info("{}size：{},{}", response.getHits().getHits().length, hts.toString(), searchHitFields.getSourceAsString());
                break;
            }*/
        }
    }

    private SearchResponse getSearchResponse(GetHTS hts) {

        String name = hts.getEngName().replaceAll("[^A-Za-z]", " ").replace(" xl"," ")
                .replace(" The"," ")
                ;
        String[] array = name.split(" ");
        name=array[(array.length - 1)];
        //1、优先英文
        SearchResponse response = client.prepareSearch(ElasticService.indexName)
                .setTypes(ElasticService.type)
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(QueryBuilders.boolQuery()
                        .must(QueryBuilders.matchQuery("engName", name.replaceAll("[^A-Za-z]", " ")))
                ).execute().actionGet();

        //2、中文匹配
        if (response.getHits().getHits().length == 0) {
            response = client.prepareSearch(ElasticService.indexName)
                    .setTypes(ElasticService.type)
                    .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                    .setQuery(QueryBuilders.boolQuery()
                            .must(QueryBuilders.matchQuery("chineseName", hts.getChineseName()))
                            //.must(QueryBuilders.wildcardQuery("chineseName", "*" + hts.getChineseName() + "*"))
                    ).execute().actionGet();
        }
        if (response.getHits().getHits().length == 0) {
            //log.info("找不到");
            return response;
        } else if (response.getHits().getHits().length == 1) {
            return response;
        } else {
            SearchResponse muxResponse = client.prepareSearch(ElasticService.indexName)
                    .setTypes(ElasticService.type)
                    .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                    .setQuery(QueryBuilders.boolQuery()
                                    .must(QueryBuilders.matchQuery("engName", name.replaceAll("[^A-Za-z]", " ")))
                              .must(QueryBuilders.wildcardQuery("chineseName", "*"+hts.getChineseName()+"*"))
                    ).execute().actionGet();
            if (muxResponse.getHits().getHits().length == 0) {
                log.info("组合没有:{}", response.getHits().getHits().length);
                return response;
            } else {
                log.info("组合前后数量:{}，{}", response.getHits().getHits().length, muxResponse.getHits().getHits().length);
                return muxResponse;
            }
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        postData();
        // 写法1
        String fileName = "/data/" + DateFormatUtils.format(new Date(), "yyyy-MM-dd") + "-V2.xlsx";
        // 这里 需要指定写用哪个class去写，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
        // 如果这里想使用03 则 传入excelType参数即可
        EasyExcel.write(fileName, Result.class).sheet("模板").doWrite(resultList);
    }
}

