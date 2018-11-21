package com.kuaima.utils.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectResult;

/**
 * 阿里云 OSS工具类
 * 
 * @author Monkey
 * @date 2017年9月30日下午3:38:09
 * @version 1.0
 */
@Component
public class OSSClientUtil {

	public static final Logger logger = LoggerFactory.getLogger(OSSClientUtil.class);
	private String endpoint=ConstantProperties.SPRING_FILE_ENDPOINT;
	// 阿里云API的密钥Access Key ID
	private String accessKeyId = ConstantProperties.SPRING_FILE_ACCESS_KEY_ID;
	// 阿里云API的密钥Access Key Secret
	private String accessKeySecret = ConstantProperties.SPRING_FILE_ACCESS_KEY_SECRET;
	// 阿里云API的bucket名称
	private String bucketName = ConstantProperties.SPRING_FILE_BUCKET_NAME1;
	// 阿里云API的文件夹名称
	//private String folder = ConstantProperties.SPRING_FILE_FILE_HOST;
	/**
	 * 上传图片
	 *
	 * @param url
	 * @throws ImgException
	 */
	public void uploadImg2Oss(String url,String folder) throws ImgException {
		File fileOnServer = new File(url);
		FileInputStream fin;
		try {
			fin = new FileInputStream(fileOnServer);
			String[] split = url.split("/");
			this.uploadFile2OSS(fin, split[split.length - 1],folder);
		} catch (FileNotFoundException e) {
			throw new ImgException("图片上传失败");
		}
	}

	public String uploadImg2Oss(MultipartFile file,String folder) throws ImgException {
		if (file.getSize() > 10 * 1024 * 1024) {
			throw new ImgException("上传图片大小不能超过10M！");
		}
		String originalFilename = file.getOriginalFilename();
		String substring = originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase();
		Random random = new Random();
		String name = random.nextInt(10000) + System.currentTimeMillis() + substring;
		try {
			InputStream inputStream = file.getInputStream();
			this.uploadFile2OSS(inputStream, name,folder);
			return name;
		} catch (Exception e) {
			throw new ImgException("图片上传失败");
		}
	}

	/**
	 * 获得图片路径
	 *
	 * @param fileUrl
	 * @return
	 */
	public String getImgUrl(String fileUrl,String folder) {
		System.out.println(fileUrl);
		if (!StringUtils.isEmpty(fileUrl)) {
			String[] split = fileUrl.split("/");
			return this.getUrl(folder + split[split.length - 1]);
		}
		return null;
	}

	/**
	 * 上传到OSS服务器 如果同名文件会覆盖服务器上的
	 *
	 * @param instream
	 *            文件流
	 * @param fileName
	 *            文件名称 包括后缀名
	 * @return 出错返回"" ,唯一MD5数字签名
	 */
	public String uploadFile2OSS(InputStream instream, String fileName,String folder) {
		OSSClient ossClient = new OSSClient(endpoint,accessKeyId,accessKeySecret);
		String ret = "";
		try {
			// 创建上传Object的Metadata
			ObjectMetadata objectMetadata = new ObjectMetadata();
			objectMetadata.setContentLength(instream.available());
			objectMetadata.setCacheControl("no-cache");
			objectMetadata.setHeader("Pragma", "no-cache");
			objectMetadata.setContentType(getcontentType(fileName.substring(fileName.lastIndexOf("."))));
			objectMetadata.setContentDisposition("inline;filename=" + fileName);
			// 上传文件
			PutObjectResult putResult = ossClient.putObject(bucketName, folder + fileName, instream, objectMetadata);
			ret = putResult.getETag();
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		} finally {
			try {
				if (instream != null) {
					instream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return ret;
	}

	/**
	 * Description: 判断OSS服务文件上传时文件的contentType
	 *
	 * @param FilenameExtension
	 *            文件后缀
	 * @return String
	 */
	public static String getcontentType(String filenameExtension) {
		if (filenameExtension.equalsIgnoreCase("bmp")) {
			return "image/bmp";
		}
		if (filenameExtension.equalsIgnoreCase("gif")) {
			return "image/gif";
		}
		if (filenameExtension.equalsIgnoreCase("jpeg") || filenameExtension.equalsIgnoreCase("jpg")
				|| filenameExtension.equalsIgnoreCase("png")) {
			return "image/jpeg";
		}
		if (filenameExtension.equalsIgnoreCase("html")) {
			return "text/html";
		}
		if (filenameExtension.equalsIgnoreCase("txt")) {
			return "text/plain";
		}
		if (filenameExtension.equalsIgnoreCase("vsd")) {
			return "application/vnd.visio";
		}
		if (filenameExtension.equalsIgnoreCase("pptx") || filenameExtension.equalsIgnoreCase("ppt")) {
			return "application/vnd.ms-powerpoint";
		}
		if (filenameExtension.equalsIgnoreCase("docx") || filenameExtension.equalsIgnoreCase("doc")) {
			return "application/msword";
		}
		if (filenameExtension.equalsIgnoreCase("xml")) {
			return "text/xml";
		}
		return "image/jpeg";
	}

	/**
	 * 获得url链接
	 *
	 * @param key
	 * @return
	 */
	public String getUrl(String key) {
		OSSClient ossClient = new OSSClient(endpoint,accessKeyId,accessKeySecret);
		Date expiration = new Date(System.currentTimeMillis() + 3600L * 1000 * 24 * 365 * 10);
		// 生成URL
		URL url = ossClient.generatePresignedUrl(bucketName, key, expiration);
		if (url != null) {
			return url.toString();
		}
		return null;
	}
	// public static void main(String[] args) {
	// //上传文件
	// String files="D:\\ceshi.jpg";
	// File file = new File(files);
	// String name = file.getName();
	// System.err.println(name);
	// try {
	// OSSClientUtil oss =new OSSClientUtil();
	// InputStream in = new FileInputStream(file);
	// String md5 = oss.uploadFile2OSS(in, name);
	// String imgUrl = oss.getImgUrl(name);
	// System.err.println(md5);
	// System.err.println(imgUrl);
	// } catch (FileNotFoundException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }
	// //34D176BA06B4294B7E7723BA88EE908B

}
