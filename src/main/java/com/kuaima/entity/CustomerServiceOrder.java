package com.kuaima.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.stereotype.Repository;

/**
 * ToC服务订单
 */
@Entity
@Table(name = "BIZ_CUSTOMER_SERVICE_ORDER")
@GenericGenerator(name = "uuidGenerator", strategy = "uuid")
@Repository
public class CustomerServiceOrder {
	private String id;
	// 订单编码
	private String customerServiceOrderNo;
	// 应付金额
	private String payableAmount;
	// 优惠金额
	private String offerAmount;
	// 实付金额
	private String actualAmount;
	// 订单状态
	private String serviceOrderStatus;
	// 创建日期
	private Date createDate;
	// 创建人
	private String creater;
	// 更新日期
	private Date updateDate;
	// 更新人
	private String updater;
	// ToB订单ID
	private String serviceOrderId;

	// 根据enum值返回对应的属性名称;
	public static String getPropertyNameByEnum(int enumValue) {
		switch (enumValue) {
		case 1:
			return "serviceOrderNo";
		case 2:
			return "payableAmount";
		case 3:
			return "offerAmount";
		case 4:
			return "actualAmount";
		case 5:
			return "serviceOrderStatus";
		default:
			return "";
		}
	};

	// 根据enum值返回对应的属性类型;
	public static String getPropertyTypeByEnum(int enumValue) {
		switch (enumValue) {
		case 1:
			return "String";
		case 2:
			return "String";
		case 3:
			return "String";
		case 4:
			return "String";
		case 5:
			return "String";
		default:
			return "";
		}
	}

	@Id
	@GeneratedValue(generator = "uuidGenerator")
	@Column(name = "id", unique = true)
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "CUSTOMER_SERVICE_ORDER_NO")
	public String getCustomerServiceOrderNo() {
		return customerServiceOrderNo;
	}

	public void setCustomerServiceOrderNo(String customerServiceOrderNo) {
		this.customerServiceOrderNo = customerServiceOrderNo;
	}

	@Column(name = "PAYABLE_AMOUNT")
	public String getPayableAmount() {
		return payableAmount;
	}

	public void setPayableAmount(String payableAmount) {
		this.payableAmount = payableAmount;
	}

	@Column(name = "OFFER_AMOUNT")
	public String getOfferAmount() {
		return offerAmount;
	}

	public void setOfferAmount(String offerAmount) {
		this.offerAmount = offerAmount;
	}

	@Column(name = "ACTUAL_AMOUNT")
	public String getActualAmount() {
		return actualAmount;
	}

	public void setActualAmount(String actualAmount) {
		this.actualAmount = actualAmount;
	}

	@Column(name = "SERVICE_ORDER_STATUS")
	public String getServiceOrderStatus() {
		return serviceOrderStatus;
	}

	public void setServiceOrderStatus(String serviceOrderStatus) {
		this.serviceOrderStatus = serviceOrderStatus;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATE_DATE")
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@Column(name = "CREATER")
	public String getCreater() {
		return creater;
	}

	public void setCreater(String creater) {
		this.creater = creater;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATE_DATE")
	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	@Column(name = "UPDATER")
	public String getUpdater() {
		return updater;
	}

	public void setUpdater(String updater) {
		this.updater = updater;
	}

	@Column(name = "SERVICE_ORDER_ID")
	public String getServiceOrderId() {
		return serviceOrderId;
	}

	public void setServiceOrderId(String serviceOrderId) {
		this.serviceOrderId = serviceOrderId;
	}

}