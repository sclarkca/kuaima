package com.kuaima.service;

import org.malagu.linq.JpaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kuaima.entity.PostStation;

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
	
	/**
	 * 根据驿站编码查询异常名称
	 * @param postCode
	 * @return
	 */
	public String queryPostNameByCode(String postCode){
		PostStation postStation = JpaUtil.linq(PostStation.class).equal("postStationCode", postCode).findOne();
		String postStationName = null;
		if (null != postStation){
			postStationName = postStation.getPostStationName();
		}
		return postStationName;
	}
}