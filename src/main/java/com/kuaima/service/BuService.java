package com.kuaima.service;

import org.malagu.linq.JpaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kuaima.entity.Bu;

@Service
public class BuService {

	@Autowired
	EncodingTypeService encodingTypeService;

	/**
	 * 创建 品类编码
	 * @param encodingTypeCode
	 * @param prefix
	 * @return
	 */
	public String createBuCode(String encodingTypeCode,String prefix) {
		return encodingTypeService.createCode(encodingTypeCode, prefix);
	}
	/**
	 * 根据品类编码查询品类信息
	 * @param brandCode
	 * @return
	 */
	public Bu queryBuName(String buCode){
		return JpaUtil.linq(Bu.class).equal("buCode", buCode).findOne();
	}
}