package com.ggj.webmagic.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.http.client.fluent.Request;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.BatchStatus;
import com.qiniu.storage.model.FileInfo;
import com.qiniu.util.Auth;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author:gaoguangjin
 * @date 2016/8/25 11:35
 */
@Component
@Slf4j
public class QiNiuUtil implements InitializingBean {
    @Getter
    public BlockingQueue<List<String>> deleteBlockingDeque = new ArrayBlockingQueue<List<String>>(5000);

    @Value("${qiniu.accessKey}")
    private String accessKey;

    @Value("${qiniu.secretKey}")
    private String secretKey;

    @Value("${qiniu.bucketName}")
    private String bucketName;

    @Value("${qiniu.domain}")
    private String domain;

    // 密钥配置
    private Auth auth;

    // 创建上传对象
    private UploadManager uploadManager;

    private BucketManager bucketManager;

    // 覆盖上传
    public String getUpToken(String key) {
        return auth.uploadToken(bucketName, key);
    }

    /**
     * 根据url 下载文件再上传七牛
     *
     * @param key
     * @param url
     * @return
     * @throws IOException
     */
    public String upload(String key, String url) throws IOException {
        if (checkExist(key))
            return domain + key;
        byte[] bytes = Request.Get(url).execute().returnContent().asBytes();
        return upload(key, bytes);
    }

    /**
     * 检查文件是否存在。图片已经存在就不上传了
     *
     * @param key
     * @return
     */
    private boolean checkExist(String key) {
        try {
            // 调用stat()方法获取文件的信息
            FileInfo info = bucketManager.stat(bucketName, key);
            return true;
        } catch (QiniuException e) {
            //log.info("图片不存在："+key);
            return false;
        }
    }

    public String upload(String key, byte[] bytes) throws IOException {
        try {
            //log.info("上传图片至七牛：key="+key);
            String token = getUpToken(key);
            // 调用put方法上传，这里指定的key和上传策略中的key要一致
            Response res = uploadManager.put(bytes, key, token);
            if (res.isOK())
                return domain + key;
        } catch (QiniuException e) {
            log.error("七牛上传 response：" + e.getLocalizedMessage());
        }
        return null;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Configuration cfg = new Configuration(Zone.zone0());
        auth = Auth.create(accessKey, secretKey);
        uploadManager = new UploadManager(cfg);
        // 实例化一个BucketManager对象
        bucketManager = new BucketManager(auth, cfg);

        new Thread() {
            public void run() {
                deleteBlockingDequeImage();
            }
        }.start();
    }

    private void deleteBlockingDequeImage() {
        ExecutorService pool = Executors.newFixedThreadPool(20);
        while (true) {
            try {
                List<String> list = deleteBlockingDeque.take();
                pool.execute(deleteByThread(list));
                List<List<String>> listAll = new ArrayList<>();
                deleteBlockingDeque.drainTo(listAll, 999);
                if (listAll != null) {
                    for (List<String> listImage : listAll) {
                        pool.execute(deleteByThread(listImage));
                    }
                }
            } catch (InterruptedException e) {
                log.error("循环删除BlockingDequeImage失败：{}", e.getLocalizedMessage());
            }
        }
    }

    public Runnable deleteByThread(final List<String> listImage) {
        return new Thread() {
            @Override
            public void run() {
                deleteByList(listImage);
            }
        };
    }

    /**
     * 删除图片
     * https://developer.qiniu.com/kodo/sdk/java#rs-batch-delete
     * 每次只能删除小于1000条数据
     *
     * @param list
     */
    public void deleteByList(List<String> list) {
        try {
            if (list == null) return;
            log.info("待删除图片大小：{}", list.size());
            String[] keyList = new String[list.size()];
            for (int i = 0; i < list.size(); i++) {
                keyList[i] = list.get(i).replace(domain, "");
            }
            BucketManager.Batch batchOperations = new BucketManager.Batch();
            batchOperations.delete(bucketName, keyList);
            Response response = bucketManager.batch(batchOperations);
            BatchStatus[] batchStatusList = response.jsonToObject(BatchStatus[].class);
            /*for (int k = 0; k < keyList.length; k++) {
                BatchStatus status = batchStatusList[k];
                if (status.code == 200) {
                   // log.info("delete success");
                } else {
                    log.error("删除失败", status.toString());
                }
            }*/
        } catch (QiniuException e) {
            log.error("七牛上传 response：" + e.getLocalizedMessage());
        }
    }

    /**
     * 列举指定前缀的文件
     *
     * @param prefix
     */
    public void fileList(String prefix) {
        //每次迭代的长度限制，最大1000，推荐值 1000
        int limit = 1000;
        //指定目录分隔符，列出所有公共前缀（模拟列出目录效果）。缺省值为空字符串
        String delimiter = "";
        //列举空间文件列表
        BucketManager.FileListIterator fileListIterator = bucketManager.createFileListIterator(bucketName, prefix, limit, delimiter);
        while (fileListIterator.hasNext()) {
            //处理获取的file list结果
            FileInfo[] items = fileListIterator.next();
            for (FileInfo item : items) {
                log.info(item.key);
            }
        }
    }

    public void deleteFileList(String prefix) throws InterruptedException {
        BucketManager.FileListIterator fileListIterator = bucketManager.createFileListIterator(bucketName, prefix, 1000, "");
        while (fileListIterator.hasNext()) {
            //处理获取的file list结果
            FileInfo[] items = fileListIterator.next();
            List<String> list = new ArrayList<>(items.length);
            for (FileInfo item : items) {
                list.add(item.key);
            }
           getDeleteBlockingDeque().put(list);
        }
    }
}
