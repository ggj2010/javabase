package com.ggj.java.firstdemo.com.ggj.java.hst;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.ggj.java.firstdemo.com.ggj.java.hst.Listener.DictionaryDataListener;
import com.ggj.java.firstdemo.com.ggj.java.hst.Listener.GetHTSDataListener;
import com.ggj.java.firstdemo.com.ggj.java.hst.bean.Dictionary;
import com.ggj.java.firstdemo.com.ggj.java.hst.bean.GetHTS;
import com.ggj.java.firstdemo.com.ggj.java.hst.bean.Result;
import com.ggj.java.firstdemo.com.ggj.java.hst.es.ElasticService;
import org.elasticsearch.client.transport.TransportClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author gaoguangjin
 */
public class Test {
    public static void main(String[] args) throws FileNotFoundException {

        TransportClient transportClient = ElasticService.initAndGetTransportClient();
        InputStream inputStream = new FileInputStream(new File("/Users/gaoguangjin/Downloads/dictionary.xlsx"));
        ExcelReader excelReader = EasyExcel.read(inputStream, Dictionary.class, new DictionaryDataListener(transportClient)).build();
        ReadSheet readSheet = EasyExcel.readSheet(0).build();
        try {
            excelReader.read(readSheet);
        } catch (Exception e) {
            // 这里千万别忘记关闭，读的时候会创建临时文件，到时磁盘会崩的
            excelReader.finish();
        }


        /*InputStream inputStream2 = new FileInputStream(new File("/Users/gaoguangjin/Downloads/test.xlsx"));
        ExcelReader excelReader2 = EasyExcel.read(inputStream2, GetHTS.class, new GetHTSDataListener(transportClient)).build();
        ReadSheet readSheet2 = EasyExcel.readSheet(0).build();
        try {
            excelReader2.read(readSheet2);
        } catch (Exception e) {
            // 这里千万别忘记关闭，读的时候会创建临时文件，到时磁盘会崩的
            excelReader2.finish();
        }*/
    }
}
