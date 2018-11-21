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
 * ToC服务订单行
 */
@Entity
@Table(name = "BIZ_CUSTOMER_SERVICE_ORDER_ITEM")
@GenericGenerator(name = "uuidGenerator", strategy = "uuid")
@Repository
public class ServiceOrderItem {
	private String id;
	// 服务商品编码
	private String sku;
	// 名称
	private String serviceCommdityName;
	// 单位
	private String unit;
	// 单价
	private String price;
	// ToC服务订单号
//	private String serviceOrderNo;
	// 创建日期
	private Date createDate;
	// 创建人
	private String creater;
	// 更新日期
	private Date updateDate;
	// 更新人
	private String updater;
	// ToC服务订单ID
	private String customerServiceOrderId;

	// 根据enum值返回对应的属性名称;
	public static String getPropertyNameByEnum(int enumValue) {
		switch (enumValue) {
		case 1:
			return "sku";
		case 2:
			return "serviceCommdityName";
		case 3:
			return "unit";
		case 4:
			return "price";
		case 5:
			return "serviceOrderNo";
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

	@Column(name = "CUSTOMER_SERVICE_ORDER_ID")
	public String getCustomerServiceOrderId() {
		return customerServiceOrderId;
	}

	public void setCustomerServiceOrderId(String customerServiceOrderId) {
		this.customerServiceOrderId = customerServiceOrderId;
	}

	@Column(name = "SKU")
	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	@Column(name = "SERVICE_COMMDITY_NAME")
	public String getServiceCommdityName() {
		return serviceCommdityName;
	}

	public void setServiceCommdityName(String serviceCommdityName) {
		this.serviceCommdityName = serviceCommdityName;
	}

	@Column(name = "UNIT")
	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	@Column(name = "PRICE")
	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

//	@Column(name = "SERVICE_ORDER_NO")
//	public String getServiceOrderNo() {
//		return serviceOrderNo;
//	}
//
//	public void setServiceOrderNo(String serviceOrderNo) {
//		this.serviceOrderNo = serviceOrderNo;
//	}

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
}