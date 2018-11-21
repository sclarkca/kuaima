package com.kuaima.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import com.bstek.bdf3.dorado.jpa.JpaUtil;
import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.Expose;
import com.kuaima.entity.City;
import com.kuaima.service.CityService;

@Controller
@Transactional(readOnly = true)
public class CityController {

	@Autowired
	CityService cityService;
	/**
	 * 查询省列表
	 * 
	 * @return
	 */
	@DataProvider
	public List<City> queryProvinceList() {
		List<City> list = JpaUtil.linq(City.class).like("adCode", "%0000").list();
		list.remove(0);
		return list;
	}

	/**
	 * 根据省编码(adCode)查询市列表 注:adCode截取前三位进行查询
	 * 
	 * @return
	 */
	@DataProvider
	public List<City> queryCityListByProCode(String proCode) {
		if (StringUtils.isNotBlank(proCode)){
			String provinceCode = proCode.substring(0, 2);
			List<City> list = JpaUtil.linq(City.class).like("adCode", provinceCode + "%00").list();
			list.remove(0);
			return list;
		}
		return new ArrayList<>();
	}

	/**
	 * 根据城市编码查询区/县信息
	 * 
	 * @param cityCode
	 * @return
	 */
	@DataProvider
	public List<City> queryRegionListByCityCode(String cityCode) {
		if (StringUtils.isNotBlank(cityCode)){
			String city = cityCode.substring(0, 4);
			List<City> list = JpaUtil.linq(City.class).like("adCode", city + "%").list();
			list.remove(0);
			return list;
		}
		return new ArrayList<>();
	}
	
	/**
	 * 根据城市编码查询城市名称
	 * @param adCode
	 * @return
	 */
	@Expose
	public String getCityNameByCode(String adCode){
		return cityService.queryCityNameByAcCode(adCode);
	}
}
