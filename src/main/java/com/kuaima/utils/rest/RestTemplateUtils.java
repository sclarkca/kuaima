package com.kuaima.utils.rest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSONObject;

public class RestTemplateUtils {
	private RestTemplateUtils() {
	}

	private static class DefaultResponseErrorHandler implements ResponseErrorHandler {
		private static final Logger logger = LoggerFactory.getLogger(DefaultResponseErrorHandler.class);

		@Override
		public boolean hasError(ClientHttpResponse response) throws IOException {
			return response.getStatusCode().value() != HttpServletResponse.SC_OK;
		}

		@Override
		public void handleError(ClientHttpResponse response) throws IOException {
			InputStreamReader inputStreamReader = new InputStreamReader(response.getBody());
			BufferedReader br = new BufferedReader(inputStreamReader);
			StringBuilder sb = new StringBuilder();
			String str = null;
			while ((str = br.readLine()) != null) {
				sb.append(str);
			}
			try {
				throw new StringIndexOutOfBoundsException(sb.toString());
			} catch (Exception e) {
				logger.info("Exception", e);
			} finally {
				inputStreamReader.close();
			}
		}
	}

	public static String get(String url, JSONObject params) {
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.setErrorHandler(new DefaultResponseErrorHandler());
		return restTemplate.getForObject(expandURL(url, params.keySet()), String.class, params);
	}

	/**
	 * 
	 * @Description: 将参数都拼接在url之后
	 */
	public static String post(String url, JSONObject params, MediaType mediaType) {
		RestTemplate restTemplate = new RestTemplate();
		// 拿到header信息
		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.setContentType(mediaType);
		HttpEntity<JSONObject> requestEntity = (mediaType == MediaType.APPLICATION_JSON
				|| mediaType == MediaType.APPLICATION_JSON_UTF8) ? new HttpEntity<JSONObject>(params, requestHeaders)
						: new HttpEntity<JSONObject>(null, requestHeaders);
		restTemplate.setErrorHandler(new DefaultResponseErrorHandler());
		return (mediaType == MediaType.APPLICATION_JSON || mediaType == MediaType.APPLICATION_JSON_UTF8)
				? restTemplate.postForObject(url, requestEntity, String.class)
				: restTemplate.postForObject(expandURL(url, params.keySet()), requestEntity, String.class, params);
	}

	private static String expandURL(String url, Set<?> keys) {
		final Pattern QUERY_PARAM_PATTERN = Pattern.compile("([^&=]+)(=?)([^&]+)?");
		Matcher mc = QUERY_PARAM_PATTERN.matcher(url);
		StringBuilder sb = new StringBuilder(url);
		sb.append("?");
		if (mc.find()) {
			sb.append("&");
		} else {
			sb.append("?");
		}

		for (Object key : keys) {
			sb.append(key).append("=").append("{").append(key).append("}").append("&");
		}
		return sb.deleteCharAt(sb.length() - 1).toString();
	}
}