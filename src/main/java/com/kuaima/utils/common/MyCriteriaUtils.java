package com.kuaima.utils.common;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bstek.bdf3.dorado.jpa.CriteriaUtils;

public class MyCriteriaUtils extends CriteriaUtils {
	private static final Logger logger = LoggerFactory.getLogger(MyCriteriaUtils.class);

	public static void buildCriteriaMap(Map<String, Object> map) {
		Map<String, Object> valueMap = new HashMap<>();
		Iterator<Entry<String, Object>> iterator = map.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<String, Object> entry = iterator.next();
			String key = entry.getKey();
			Object value = entry.getValue();
			if (value.getClass().getName().startsWith("com.kuaima")) {
				iterator.remove();
				valueMap.putAll(getNotNullValue(key, value));
			}
		}
		map.putAll(valueMap);
	}

	public static Map<String, Object> getNotNullValue(String prefix, Object obj) {
		Map<String, Object> map = new HashMap<>();
		Field[] fs = obj.getClass().getSuperclass().getDeclaredFields();
		for (int i = 0; i < fs.length; i++) {
			Field f = fs[i];
			f.setAccessible(true);
			Object value = null;
			try {
				value = f.get(obj);
				if (value != null && MyStringUtil.isNotEmpty(value.toString()) && f.getModifiers() == 2) {
					map.put(prefix + "." + f.getName(), value);
				}
			} catch (Exception e) {
				logger.info("Exception", e);
			}
		}
		return map;
	}

	public static void clearEmptyValue(Map<String, Object> map) {
		Iterator<String> iterator = map.keySet().iterator();
		while (iterator.hasNext()) {
			Object value = map.get(iterator.next());
			if (value == null || MyStringUtil.isEmpty(value.toString())) {
				iterator.remove();
			}
		}
	}
}
