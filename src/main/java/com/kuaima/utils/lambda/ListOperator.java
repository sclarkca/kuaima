package com.kuaima.utils.lambda;

@FunctionalInterface
public interface ListOperator<T> {
	/**
	 * 适用于集合等有下标的操作
	 * 
	 * @param t
	 * @param index
	 */
	public boolean operator(T t, int index);
}
