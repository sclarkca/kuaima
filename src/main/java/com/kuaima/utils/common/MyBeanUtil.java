package com.kuaima.utils.common;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.collections.ComparatorUtils;
import org.apache.commons.collections.comparators.ComparableComparator;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyBeanUtil {
	private static final Logger logger = LoggerFactory.getLogger(MyBeanUtil.class);

	/**
	 * 将一个 Map 对象转化为一个 JavaBean
	 * 
	 * @param type
	 *            要转化的类型
	 * @param map
	 *            包含属性值的 map
	 * @return 转化出来的 JavaBean 对象
	 * @throws IntrospectionException
	 *             如果分析类属性失败
	 * @throws IllegalAccessException
	 *             如果实例化 JavaBean 失败
	 * @throws InstantiationException
	 *             如果实例化 JavaBean 失败
	 * @throws InvocationTargetException
	 *             如果调用属性的 setter 方法失败
	 */
	public static Object convertMap(Class type, Map map)
			throws IntrospectionException, IllegalAccessException, InstantiationException, InvocationTargetException {
		BeanInfo beanInfo = Introspector.getBeanInfo(type); // 获取类属性
		Object obj = type.newInstance(); // 创建 JavaBean 对象

		// 给 JavaBean 对象的属性赋值
		PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
		for (int i = 0; i < propertyDescriptors.length; i++) {
			PropertyDescriptor descriptor = propertyDescriptors[i];
			String propertyName = descriptor.getName();

			if (map.containsKey(propertyName)) {
				// 下面一句可以 try 起来，这样当一个属性赋值失败的时候就不会影响其他属性赋值。
				Object value = map.get(propertyName);

				Object[] args = new Object[1];
				args[0] = value;

				descriptor.getWriteMethod().invoke(obj, args);
			}
		}
		return obj;
	}

	/**
	 * 将一个 JavaBean 对象转化为一个 Map
	 * 
	 * @param bean
	 *            要转化的JavaBean 对象
	 * @return 转化出来的 Map 对象
	 * @throws IntrospectionException
	 *             如果分析类属性失败
	 * @throws IllegalAccessException
	 *             如果实例化 JavaBean 失败
	 * @throws InvocationTargetException
	 *             如果调用属性的 setter 方法失败
	 */
	public static Map convertBean(Object bean)
			throws IntrospectionException, IllegalAccessException, InvocationTargetException {
		Class type = bean.getClass();
		Map returnMap = new HashMap();
		BeanInfo beanInfo = Introspector.getBeanInfo(type);

		PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
		for (int i = 0; i < propertyDescriptors.length; i++) {
			PropertyDescriptor descriptor = propertyDescriptors[i];
			String propertyName = descriptor.getName();
			if (!propertyName.equals("class")) {
				Method readMethod = descriptor.getReadMethod();
				Object result = readMethod.invoke(bean, new Object[0]);
				if (result != null) {
					returnMap.put(propertyName, result);
				} else {
					returnMap.put(propertyName, "");
				}
			}
		}
		return returnMap;
	}

	public static boolean checkFieldValueNull(Object bean) {
		boolean result = true;
		if (bean == null) {
			return true;
		}
		Class<?> cls = bean.getClass();
		Method[] methods = cls.getDeclaredMethods();
		Field[] fields = cls.getDeclaredFields();
		for (Field field : fields) {
			try {
				String fieldGetName = parGetName(field.getName());
				if (!checkGetMet(methods, fieldGetName)) {
					continue;
				}
				Method fieldGetMet = cls.getMethod(fieldGetName, new Class[] {});
				Object fieldVal = fieldGetMet.invoke(bean, new Object[] {});
				if (fieldVal != null) {
					if ("".equals(fieldVal)) {
						result = true;
					} else {
						result = false;
					}
				}
			} catch (Exception e) {
				logger.info("Exception", e);
				continue;
			}
		}
		return result;
	}

	/**
	 * 拼接某属性的 get方法
	 * 
	 * @param fieldName
	 * @return String
	 */
	public static String parGetName(String fieldName) {
		if (null == fieldName || "".equals(fieldName)) {
			return null;
		}
		int startIndex = 0;
		if (fieldName.charAt(0) == '_')
			startIndex = 1;
		return "get" + fieldName.substring(startIndex, startIndex + 1).toUpperCase()
				+ fieldName.substring(startIndex + 1);
	}

	/**
	 * 判断是否存在某属性的 get方法
	 * 
	 * @param methods
	 * @param fieldGetMet
	 * @return boolean
	 */
	public static boolean checkGetMet(Method[] methods, String fieldGetMet) {
		for (Method met : methods) {
			if (fieldGetMet.equals(met.getName())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断对象属性全是空值
	 * 
	 * @param bean
	 * @return
	 */
	public static boolean checkAllFieldValueNull(Object bean) {
		boolean allNull = true;
		try {
			Map<String, Object> map = (HashMap<String, Object>) convertBean(bean);
			for (String key : map.keySet()) {
				if (StringUtils.isNotBlank(map.get(key).toString())) {
					allNull = false;
				}
			}
			return allNull;

		} catch (IllegalAccessException | InvocationTargetException | IntrospectionException e) {
			logger.info("Exception", e);
			return allNull;
		}

	}

	/**
	 * 判断对象属性全是空值,除了某属性
	 * 
	 * @param bean
	 * @return
	 */
	public static boolean checkAllFieldValueNullExclude(Object bean, String exclude) {
		boolean allNull = true;
		try {
			Map<String, String> map = (HashMap<String, String>) convertBean(bean);
			map.remove(exclude);
			for (String key : map.keySet()) {
				if (StringUtils.isNotBlank(map.get(key))) {
					allNull = false;
				}
			}
			return allNull;

		} catch (IllegalAccessException | InvocationTargetException | IntrospectionException e) {
			logger.info("Exception", e);
			return allNull;
		}

	}

	/**
	 * 比较两对象，记录不同字段
	 * 
	 * @param source
	 * @param dest
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 */
	public static String getModifiedFieldResult(Object source, Object dest)
			throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {

		StringBuffer sb = new StringBuffer();
		return StringUtils.substringBeforeLast(sb.toString(), ",");
	}

	/**
	 * 如果目标对象的属性为空，覆盖；如果不为空，不覆盖
	 * 
	 * @param dest
	 * @param orig
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public static void copyBlankProperties(Object dest, Object orig)
			throws IllegalAccessException, InvocationTargetException {

		// Validate existence of the specified beans
		if (dest == null) {
			throw new IllegalArgumentException("No destination bean specified");
		}
		if (orig == null) {
			throw new IllegalArgumentException("No origin bean specified");
		}

		PropertyDescriptor[] origDescriptors = BeanUtilsBean.getInstance().getPropertyUtils()
				.getPropertyDescriptors(orig);
		for (int i = 0; i < origDescriptors.length; i++) {
			String name = origDescriptors[i].getName();
			if ("class".equals(name)) {
				continue; // No point in trying to set an object's class
			}
			if (BeanUtilsBean.getInstance().getPropertyUtils().isReadable(orig, name)
					&& BeanUtilsBean.getInstance().getPropertyUtils().isWriteable(dest, name)) {
				try {
					Object value = BeanUtilsBean.getInstance().getPropertyUtils().getSimpleProperty(orig, name);

					Object destValue = BeanUtils.getProperty(dest, name);
					Object sourceValue = BeanUtils.getProperty(orig, name);
					if (null == destValue && null != sourceValue) {
						BeanUtilsBean.getInstance().copyProperty(dest, name, value);
					} else {
						continue;
					}

				} catch (NoSuchMethodException e) {
					logger.info("Exception", e);
				}
			}
		}

	}

	/**
	 * 如果目标对象的属性为空，覆盖；不管为不为空
	 * 
	 * @param dest
	 * @param orig
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public static void copyProperties(Object dest, Object orig)
			throws IllegalAccessException, InvocationTargetException {

		// Validate existence of the specified beans
		if (dest == null) {
			throw new IllegalArgumentException("No destination bean specified");
		}
		if (orig == null) {
			throw new IllegalArgumentException("No origin bean specified");
		}

		PropertyDescriptor[] origDescriptors = BeanUtilsBean.getInstance().getPropertyUtils()
				.getPropertyDescriptors(orig);
		for (int i = 0; i < origDescriptors.length; i++) {
			String name = origDescriptors[i].getName();
			if ("class".equals(name)) {
				continue; // No point in trying to set an object's class
			}
			if (BeanUtilsBean.getInstance().getPropertyUtils().isReadable(orig, name)
					&& BeanUtilsBean.getInstance().getPropertyUtils().isWriteable(dest, name)) {
				try {
					Object value = BeanUtilsBean.getInstance().getPropertyUtils().getSimpleProperty(orig, name);

					Object destValue = BeanUtils.getProperty(dest, name);
					Object sourceValue = BeanUtils.getProperty(orig, name);
					if (null != sourceValue) {
						BeanUtilsBean.getInstance().copyProperty(dest, name, value);
					} else {
						continue;
					}

				} catch (NoSuchMethodException e) {
					logger.info("Exception", e);
				}
			}
		}

	}

	/**
	 * 如果目标对象的属性为空，覆盖；不管为不为空 业务用
	 * 
	 * @param dest
	 * @param orig
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public static void copyBizProperties(Object dest, Object orig)
			throws IllegalAccessException, InvocationTargetException {

		// Validate existence of the specified beans
		if (dest == null) {
			throw new IllegalArgumentException("找不到内部平台历史记录");
		}
		if (orig == null) {
			throw new IllegalArgumentException("找不到内部平台历史记录");
		}

		PropertyDescriptor[] origDescriptors = BeanUtilsBean.getInstance().getPropertyUtils()
				.getPropertyDescriptors(orig);
		for (int i = 0; i < origDescriptors.length; i++) {
			String name = origDescriptors[i].getName();
			if ("class".equals(name)) {
				continue; // No point in trying to set an object's class
			}
			if (BeanUtilsBean.getInstance().getPropertyUtils().isReadable(orig, name)
					&& BeanUtilsBean.getInstance().getPropertyUtils().isWriteable(dest, name)) {
				try {
					Object value = BeanUtilsBean.getInstance().getPropertyUtils().getSimpleProperty(orig, name);

					Object destValue = BeanUtils.getProperty(dest, name);
					Object sourceValue = BeanUtils.getProperty(orig, name);
					if (null != sourceValue) {
						BeanUtilsBean.getInstance().copyProperty(dest, name, value);
					} else {
						continue;
					}

				} catch (NoSuchMethodException e) {
					logger.info("Exception", e);
					// Should not happen
				}
			}
		}

	}

	/**
	 * 实现Java对象的按属性排序
	 * 
	 * @param list
	 *            对象列表
	 * @param property
	 *            对象属性
	 * @param asc
	 *            是否升序
	 */
	public static <T> void sort(List<T> list, String property, boolean asc) {
		Comparator<?> comparator = ComparableComparator.getInstance();
		comparator = ComparatorUtils.nullLowComparator(comparator);
		if (!asc) {
			comparator = ComparatorUtils.reversedComparator(comparator);
		}
		Collections.sort(list, new BeanComparator(property, comparator));
	}
}
