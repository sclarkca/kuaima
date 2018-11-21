package com.kuaima.utils.common;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Description:配置文件配置项
 */
@Component
public class ConstantProperties implements InitializingBean {

	@Value("${aliyun.oss.endpoint}")
	private String endpoint;
	// 阿里云API的密钥Access Key ID
	@Value("${aliyun.oss.accesskeytd}")
	private String accessKeyId;
	// 阿里云API的密钥Access Key Secret
	@Value("${aliyun.oss.accesskeysecret}")
	private String accessKeySecret;
	// 阿里云API的bucket名称
	@Value("${aliyun.oss.bucketname}")
	private String bucketName;
	// 阿里云API的文件夹名称
	@Value("${aliyun.oss.filedir}")
	private String folder;

	public static String SPRING_FILE_ENDPOINT;
	public static String SPRING_FILE_ACCESS_KEY_ID;
	public static String SPRING_FILE_ACCESS_KEY_SECRET;
	public static String SPRING_FILE_BUCKET_NAME1;
	public static String SPRING_FILE_FILE_HOST;

	@Override
	public void afterPropertiesSet() throws Exception {
		SPRING_FILE_ENDPOINT = endpoint;
		SPRING_FILE_ACCESS_KEY_ID = accessKeyId;
		SPRING_FILE_ACCESS_KEY_SECRET = accessKeySecret;
		SPRING_FILE_BUCKET_NAME1 = bucketName;
		SPRING_FILE_FILE_HOST = folder;
	}

}
