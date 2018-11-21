package com.kuaima.entity.repo;

import java.io.Serializable;

/**
 * 订单信息
 */
public class ServiceOrderImp implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5613348117372051683L;
	// 定单编码
	private String orderNo;
	// 安装师傅编码
	private String workerId;
	// 师傅姓名
	private String cardName;

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getWorkerId() {
		return workerId;
	}

	public void setWorkerId(String workerId) {
		this.workerId = workerId;
	}

	public String getCardName() {
		return cardName;
	}

	public void setCardName(String cardName) {
		this.cardName = cardName;
	}

}