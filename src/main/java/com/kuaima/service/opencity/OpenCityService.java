package com.kuaima.service.opencity;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bstek.dorado.annotation.DataProvider;
import com.kuaima.dao.OpenCityDao;
import com.kuaima.entity.Bu;
import com.kuaima.entity.City;
import com.kuaima.entity.Commodity;
import com.kuaima.service.merchant.MerchantAssignmentService;

@Service
public class OpenCityService {
	
	@Autowired
	OpenCityDao openCityDao;
	@Autowired
	MerchantAssignmentService merchantAssignmentService;
	/**
	 * 查询已开通服务品类列表
	 * @return 
	 */
	@DataProvider
	public List<Bu> queryBuList(){
		return openCityDao.queryBuList();
	}
	
	/**
	 * 根据品类编码查询已开通服务的城市列表
	 * @param buCode
	 * @return 
	 */
	@DataProvider
	public List<City> queryOpenCityByBuCode(String buCode){
		return openCityDao.queryOpenCityByBuCode(buCode);
	}
	
	/**
	 * 根据 商户编码和品类编码查询商品信息列表
	 * @param merchantCode
	 * @param buCode
	 * @return
	 */
	@DataProvider
	public List<Commodity> queryCmmdityListByMerCodeAndBuCode(String buCode){
		String merchantCode = merchantAssignmentService.getCurrentMerchantCode();
		return openCityDao.queryCmmdityList(merchantCode, buCode);
	}
}