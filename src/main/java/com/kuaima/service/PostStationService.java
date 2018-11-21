package com.kuaima.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostStationService {

	
	@Autowired
	EncodingTypeService encodingTypeService;

	/**
	 * 创建驿站编码
	 * @param encodingTypeCode
	 * @param prefix
	 * @return
	 */
	public String createPostCode(String encodingTypeCode,String prefix) {
		return encodingTypeService.createCode(encodingTypeCode, prefix);
	}
}