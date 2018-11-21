package com.kuaima.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.bstek.bdf3.dorado.jpa.JpaUtil;
import com.kuaima.entity.City;

@Service
public class CityService {

	/**
	 * 根据城市编码查询名称
	 * @param adCode
	 * @return
	 */
	public String queryCityNameByAcCode(String adCode){
		if (StringUtils.isNotBlank(adCode)){
			City city = JpaUtil.linq(City.class).equal("adCode", adCode).findOne();
			return city.getName();
		}
		return null;
	}
}
