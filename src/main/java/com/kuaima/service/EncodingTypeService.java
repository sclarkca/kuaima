package com.kuaima.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bstek.bdf3.dorado.jpa.JpaUtil;
import com.kuaima.entity.EncodingType;

@Service
@Transactional
public class EncodingTypeService {

	@Autowired
	EncodingTypeService encodingTypeService;

	/**
	 * 根据菜单类型查询最新的 编码
	 * 
	 * @param encodingTypeCode
	 * @return
	 */
	public String queryLastCodeByType(String encodingTypeCode) {
		EncodingType encodingType;
		try {
			encodingType = JpaUtil.linq(EncodingType.class).equal("encodingTypeCode", encodingTypeCode).findOne();
		} catch (Exception e) {
			return null;
		}
		if (null != encodingType) {
			return encodingType.getLastCode();
		}
		return null;
	}

	/**
	 * 根据 菜单类型和最新编码更新
	 * 
	 * @param encodingTypeCode
	 * @param lastCode
	 * @return
	 */
	public void updateLastCodeByType(String encodingTypeCode, String lastCode) {
		EncodingType et = JpaUtil.linq(EncodingType.class).equal("encodingTypeCode", encodingTypeCode).findOne();
		et.setLastCode(lastCode);
		JpaUtil.merge(et);
	}

	/**
	 * 创建 编码(保存数据库)
	 * 
	 * @param encodingTypeCode
	 * @param lastCode
	 * @return
	 * @return
	 */
	public EncodingType createLastCodeByType(String encodingTypeCode, String lastCode) {
		EncodingType et = new EncodingType();
		et.setEncodingTypeCode(encodingTypeCode);
		et.setLastCode(lastCode);
		return JpaUtil.persist(et);
	}

	/**
	 * 创建编码 公共方法(提供页面展示)
	 * 
	 * @param encodingTypeCode
	 * @param prefix
	 * @return
	 */
	public String createCode(String encodingTypeCode, String prefix) {
		// 查询 encodingType表中对应数据有无最新code
		String brandCode = encodingTypeService.queryLastCodeByType(encodingTypeCode);
		if (StringUtils.isNotBlank(brandCode)) {
			// 分割字符串
			String suffixBrandCode = brandCode.substring(prefix.length(), brandCode.length());
			// 转换为integer类型
			Integer intBrandCode = Integer.valueOf(suffixBrandCode);
			// 自增+1
			intBrandCode = intBrandCode + 1;
			// 转换成String类型补0 (总6位)
			String suffixBrandCodeNew = String.format("%06d", intBrandCode);
			// 拼接
			String newBrandCode = prefix + suffixBrandCodeNew;
			return newBrandCode;
		}
		String format = String.format("%06d", 1);
		return prefix + format;
	}
}