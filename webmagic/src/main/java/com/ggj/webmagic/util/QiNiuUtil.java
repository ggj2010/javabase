package com.ggj.webmagic.util;

import java.io.IOException;

import com.qiniu.storage.BucketManager;
import com.qiniu.storage.model.FileInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.fluent.Request;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;


/**
 * @author:gaoguangjin
 * @date 2016/8/25 11:35
 */
@Component
@Slf4j
public class QiNiuUtil implements InitializingBean{
    @Value("${qiniu.accessKey}")
    private String accessKey ;
    @Value("${qiniu.secretKey}")
    private String secretKey ;
    @Value("${qiniu.bucketName}")
    private String bucketName ;
    @Value("${qiniu.domain}")
    private String domain ;
    //密钥配置
    private Auth auth;
    //创建上传对象
    private UploadManager uploadManager;
    private BucketManager bucketManager;

    // 覆盖上传
    public String getUpToken(String key){
        return auth.uploadToken(bucketName, key);
    }

    /**
     * 根据url 下载文件再上传七牛
     * @param key
     * @param url
     * @return
     * @throws IOException
     */
    public String upload(String key,String url) throws IOException {
       if(!checkExist(key))return  null;
        byte[] bytes = Request.Get(url).execute().returnContent().asBytes();
        return upload(key,bytes);
    }

    /**
     * 检查文件是否存在。图片已经存在就不上传了
     * @return
     * @param key
     */
    private boolean checkExist(String key) {
        try {
            //调用stat()方法获取文件的信息
            FileInfo info = bucketManager.stat(bucketName, key);
            return true;
        } catch (QiniuException e) {
            return  false;
        }
    }


    public String upload(String key,byte[] bytes) throws IOException {
        try {
            String dd = getUpToken(key);
            //调用put方法上传，这里指定的key和上传策略中的key要一致
            Response res = uploadManager.put(bytes, key, dd);
            if(res.isOK())
                return domain+key;
        } catch (QiniuException e) {
            Response r = e.response;
            // 请求失败时打印的异常信息
            log.error(r.toString());
            try {
                //响应的文本信息
               log.info(r.bodyString());
            } catch (QiniuException e1) {
                log.error("七牛上传 response："+e1.getLocalizedMessage());
            }
        }
        return null;
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        auth=Auth.create(accessKey, secretKey);
         uploadManager = new UploadManager();
        //实例化一个BucketManager对象
         bucketManager = new BucketManager(auth);
    }
}
