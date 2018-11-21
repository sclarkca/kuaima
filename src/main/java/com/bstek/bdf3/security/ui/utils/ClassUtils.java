package com.bstek.bdf3.security.ui.utils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
/**
 * @author Kevin.yang
 */
public abstract class ClassUtils {
	private ClassUtils (){}
	public static Class<?> getGenericType(Class<?> clazz ,int index){
		Type genType = clazz.getGenericSuperclass();
		if (!(genType instanceof ParameterizedType)) {
			return Object.class;
		}
		Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
		if (index >= params.length || index < 0) {
		   throw new IndexOutOfBoundsException("Index outof bounds");
		}
		if (!(params[index] instanceof Class)) {
			return Object.class;
		}
		return (Class<?>) params[index];
	}
}
