package com.ggj.webmagic.util;

import java.io.IOException;

import org.apache.http.client.fluent.Request;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.FileInfo;
import com.qiniu.util.Auth;

import lombok.extern.slf4j.Slf4j;

/**
 * @author:gaoguangjin
 * @date 2016/8/25 11:35
 */
@Component
@Slf4j
public class QiNiuUtil implements InitializingBean {
	
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
	 * @return
	 * @param key
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
	
	public  String upload(String key, byte[] bytes) throws IOException {
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
		auth = Auth.create(accessKey, secretKey);
		uploadManager = new UploadManager();
		// 实例化一个BucketManager对象
		bucketManager = new BucketManager(auth);
	}
}
