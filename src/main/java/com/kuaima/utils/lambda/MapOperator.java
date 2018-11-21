package com.kuaima.utils.lambda;

@FunctionalInterface
public interface MapOperator<K, V> {
	/**
	 * 适用于Map等键值对的操作
	 * 
	 * @param t
	 * @param key
	 */
	public boolean operator(K key, V value);
}
