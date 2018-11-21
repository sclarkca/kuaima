package com.kuaima.controller.temp;

import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ResourceLoader;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
 
 
/** 
 * Description: [平台券模板下载controller]
 * @author 
 */
@RestController
@RequestMapping("/order")
public class OrderTemplateDownloadController {

	private static final Logger LOGGER = LoggerFactory.getLogger(OrderTemplateDownloadController.class);
	@Resource
	private ResourceLoader resourceLoader;
	
	/**
	 * <p>Description:[下载模板功能]</p>
	 * @param response response对象
	 * @param request response对象
	 */
	@GetMapping("/downloadTemplate")
	@Transactional
	public void downloadTemplate(HttpServletResponse response, HttpServletRequest request) {
		InputStream inputStream = null;
        	ServletOutputStream servletOutputStream = null;
		try {
			String filename = "批量订单模板.xlsx";
	        String path = "static/order/orderTemplate.xlsx";
	        org.springframework.core.io.Resource resource = resourceLoader.getResource("classpath:"+path);
	        
	        response.setContentType("application/vnd.ms-excel");
	        response.addHeader("Cache-Control", "no-cache, no-store, must-revalidate");
	        response.addHeader("charset", "utf-8");
	        response.addHeader("Pragma", "no-cache");
	        String encodeName = URLEncoder.encode(filename, StandardCharsets.UTF_8.toString());
	        response.setHeader("Content-Disposition", "attachment; filename=\"" + encodeName + "\"; filename*=utf-8''" + encodeName);
	        
	        inputStream = resource.getInputStream();
	        servletOutputStream = response.getOutputStream();
	        IOUtils.copy(inputStream, servletOutputStream);
	        response.flushBuffer();
		} catch (Exception e) {
			LOGGER.error("系统异常",e);
		} finally {
			try {
				if (servletOutputStream != null) {
					servletOutputStream.close();
					servletOutputStream = null;
				}
				if (inputStream != null) {
					inputStream.close();
					inputStream = null;
				}
				// 召唤jvm的垃圾回收器
				System.gc();
			} catch (Exception e) {
				LOGGER.error("系统异常",e);
			}
		}
	}
	
}
