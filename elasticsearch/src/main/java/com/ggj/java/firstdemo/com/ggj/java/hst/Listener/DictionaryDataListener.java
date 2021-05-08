package com.ggj.java.firstdemo.com.ggj.java.hst.Listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.fastjson.JSONObject;
import com.ggj.java.firstdemo.com.ggj.java.hst.bean.Dictionary;
import com.ggj.java.firstdemo.com.ggj.java.hst.es.ElasticService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.transport.TransportClient;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author gaoguangjin
 */
@Slf4j
public class DictionaryDataListener extends AnalysisEventListener<Dictionary> {
    private static final int BATCH_COUNT = 20;
    private List<Dictionary> list = new ArrayList<Dictionary>();
    private AtomicInteger count = new AtomicInteger();

    private TransportClient client;

    public DictionaryDataListener(TransportClient client) {
        this.client = client;
    }

    @Override
    public void invoke(Dictionary orderData, AnalysisContext analysisContext) {
        list.add(orderData);
        if (list.size() >= BATCH_COUNT) {
            postData();
            list.clear();
        }
    }

    private void postData() {
        for (Dictionary dictionary : list) {
            client.prepareIndex(ElasticService.indexName, ElasticService.type).setId(UUID.randomUUID().toString()).setSource(JSONObject.toJSONString(dictionary)).execute().actionGet();
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        postData();
    }
}
