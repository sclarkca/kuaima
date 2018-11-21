package com.kuaima.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.kuaima.entity.Bu;
import com.kuaima.entity.City;
import com.kuaima.entity.Commodity;

@Repository
public class OpenCityDao {

	@Autowired
	JdbcTemplate jdbcTemplate;

	/**
	 * 查询已开通服务品类列表 is_open = 1
	 * 
	 * @return
	 */
	@Transactional
	public List<Bu> queryBuList() {
		String sql = "select distinct o.bu_code,o.bu_name from biz_open_city o where o.is_open = 1";
		List<Bu> list = jdbcTemplate.query(sql, new BeanPropertyRowMapper<Bu>(Bu.class));
		return list;
	}

	/**
	 * 根据品类编码查询已开通的城市列表信息
	 * 
	 * @param buCode
	 * @return
	 */
	public List<City> queryOpenCityByBuCode(String buCode) {
		String sql = "select c.ad_code,c.name from biz_open_city o left join biz_city c on o.city = c.ad_code where o.is_open = 1 AND o.BU_CODE = '"
				+ buCode + "'";
		List<City> list = jdbcTemplate.query(sql, new BeanPropertyRowMapper<City>(City.class));
		return list;
	}

	/**
	 * 根据 商户编码和品类编码查询商品信息列表
	 * @param merchantCode
	 * @param buCode
	 * @return
	 */
	public List<Commodity> queryCmmdityList(String merchantCode, String buCode) {
		String sql = "select c.sku,c.commodity_name from biz_merchant_fee_relationship r left join biz_commodity_quoted_price p on r.relationship_temp_no = p.relationship_temp_no left join biz_commodity c on c.sku = p.sku and c.bu_code = '"
				+ buCode + "'where r.merchant_code = '" + merchantCode + "'";
		return jdbcTemplate.query(sql, new BeanPropertyRowMapper<Commodity>(Commodity.class));
	}
}
