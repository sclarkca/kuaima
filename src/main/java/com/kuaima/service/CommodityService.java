package com.kuaima.service;

import java.util.List;

import org.malagu.linq.JpaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kuaima.entity.Commodity;

@Service
public class CommodityService {

	@Autowired
	EncodingTypeService encodingTypeService;

	/**
	 * 创建 商品编码
	 * 
	 * @param encodingTypeCode
	 * @param prefix
	 * @return
	 */
	public String createCmdCode(String encodingTypeCode, String prefix) {
		return encodingTypeService.createCode(encodingTypeCode, prefix);
	}
	
	/**
	 * 根据品类编码查询商品信息列表
	 * @param buCode
	 * @return
	 */
	public List<Commodity> queryCommodityListByBuCode(String buCode){
		List<Commodity> list = JpaUtil.linq(Commodity.class).equal("buCode", buCode).list();
		return list;
	}
}