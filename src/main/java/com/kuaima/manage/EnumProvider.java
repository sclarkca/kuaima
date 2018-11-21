package com.kuaima.manage;

import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.bstek.bdf3.dictionary.domain.DictionaryItem;
import com.bstek.bdf3.dictionary.service.DictionaryServiceImpl;
import com.bstek.dorado.annotation.DataProvider;

/**
 * 数据字典
 * 
 * @author 
 *
 */
@Service
public class EnumProvider {

	@Autowired
	private DictionaryServiceImpl dictionaryServiceImpl;
	private static final Logger LOGGER = Logger.getLogger(EnumProvider.class);

	/**
	 * 查询数据字典
	 * 
	 * @param code
	 * @return
	 */
	@DataProvider
	public Map<String, String> getValues(String code) {
		List<DictionaryItem> dictionaryItemList = dictionaryServiceImpl.getDictionaryItemsBy(code);
		Map<String, String> map = new LinkedHashMap<>();
		for (DictionaryItem dictionaryItem : dictionaryItemList) {
			map.put(dictionaryItem.getKey(), dictionaryItem.getValue());
		}
		return map;
	}

	/**
	 * 查询数据字典单个value
	 * 
	 * @param code
	 * @return
	 */
	@DataProvider
	public String getValue(String code, String key) {
		if (StringUtils.isNotBlank(key)) {

			List<DictionaryItem> dictionaryItemList = dictionaryServiceImpl.getDictionaryItemsBy(code);
			Map<String, String> map = new LinkedHashMap<>();
			for (DictionaryItem dictionaryItem : dictionaryItemList) {
				if (key.equals(dictionaryItem.getKey())) {

					map.put(dictionaryItem.getKey(), dictionaryItem.getValue());
				}
			}
			return map.get(key);
		}
		return key;
	}

	/**
	 * 查询数据字典-全部
	 *
	 * @param code
	 * @return
	 */
	@DataProvider
	public Map<String, String> getValuesWithAll(String code) {
		List<DictionaryItem> dictionaryItemList = dictionaryServiceImpl.getDictionaryItemsBy(code);
		Map<String, String> map = new LinkedHashMap<>();
		map.put("-1", "全部");
		for (DictionaryItem dictionaryItem : dictionaryItemList) {
			map.put(dictionaryItem.getKey(), dictionaryItem.getValue());
		}
		return map;
	}

	/**
	 * 查询数据字典-请选择
	 *
	 * @param code
	 * @return
	 */
	@DataProvider
	public Map<String, String> getValuesWithSelect(String code) {
		List<DictionaryItem> dictionaryItemList = dictionaryServiceImpl.getDictionaryItemsBy(code);
		Map<String, String> map = new LinkedHashMap<>();
		map.put("-1", "请选择");
		for (DictionaryItem dictionaryItem : dictionaryItemList) {
			map.put(dictionaryItem.getKey(), dictionaryItem.getValue());
		}
		return map;
	}

	/**
	 * 查询数据字典子目录
	 * 
	 * @param code
	 * @param dictionaryItemKey
	 * @return
	 */
	public String getDictionaryItemValue(String code, String dictionaryItemKey) {
		Map<String, String> dictionaryItemMap = getValues(code);
		String value = dictionaryItemMap.get(dictionaryItemKey);
		if (StringUtils.isNotBlank(value)) {
			return value;
		} else {
			return "";
		}
	}

	/**
	 * 查询多选框用的数据字典目录
	 * 
	 * @param code
	 * @return 返回 List<DictionaryItem>
	 */
	@DataProvider
	public List<DictionaryItem> getTargetDisplayValues(String code) {
		LOGGER.info("EnumProvider#getTargetDisplayValues code" + code);
		List<DictionaryItem> result = dictionaryServiceImpl.getDictionaryItemsBy(code);
		LOGGER.info("EnumProvider#getTargetDisplayValues result" + JSON.toJSONString(result));
		return result;
	}

	/**
	 * 获得枚举的共用方法
	 * 
	 * @param value
	 *            数据字典对应String值
	 * @param cls
	 *            枚举的class对象
	 * @return t 枚举对象
	 * 
	 */
	public static <T> T valueOf(String value, Class<T> cls) {
		try {

			Method method = cls.getMethod("values", null);
			T[] tenumArr = (T[]) method.invoke(null, null);
			for (T t1 : tenumArr) {
				Method getCode = t1.getClass().getMethod("getCode", null);
				Object code = getCode.invoke(t1, null);
				if (StringUtils.isNoneBlank(value) && value.equals(String.valueOf(code))) {
					return t1;
				}
			}
		} catch (Exception e) {
			LOGGER.info("EnumCoverter#valueOf code转换成enum e" + e);
		}
		return null;
	}
}
