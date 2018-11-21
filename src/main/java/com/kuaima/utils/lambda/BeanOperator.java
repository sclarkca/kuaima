package com.kuaima.utils.lambda;

@FunctionalInterface
public interface BeanOperator<T> {
	/**
	 * 适用于普通对象
	 * 
	 * @param t
	 */
	public boolean operator(T t);
}
