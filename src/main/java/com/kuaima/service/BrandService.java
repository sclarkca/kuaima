package com.kuaima.service;

import org.malagu.linq.JpaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kuaima.entity.Brand;

@Service
public class BrandService {
	
	@Autowired
	EncodingTypeService encodingTypeService;
	/**
	 * 创建 品牌编码
	 * @param encodingTypeCode
	 * @param prefix
	 * @return
	 */
	public String createBrandCode(String encodingTypeCode,String prefix){
		return encodingTypeService.createCode(encodingTypeCode, prefix);
	}
	
	/**
	 * 根据品牌编码查询品牌信息
	 * @param brandCode
	 * @return
	 */
	public Brand queryBrandName(String brandCode){
		return JpaUtil.linq(Brand.class).equal("brandCode", brandCode).findOne();
	}
}