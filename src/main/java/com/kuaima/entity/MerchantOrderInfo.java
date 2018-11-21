package com.kuaima.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.stereotype.Repository;

/**
 * 商家订单信息
 */
@Entity
@Table(name = "BIZ_MERCHANT_ORDER_INFO")
@GenericGenerator(name = "uuidGenerator", strategy = "uuid")
@Repository
public class MerchantOrderInfo {
	private String id;
	// 服务商品金额
	private String serviceCommdityAmount;
	// 安装金额
	private String installAmount;
	// 配送金额
	private String distributionAmount;
	// 总金额
	private String totalAmount;
	// ToB订单编码
	private String orderId;
	// 中心位置经度
	private String centerLongitude;
	// 中心位置维度
	private String centerLatitude;
	// 配送位置经度
	private String distributionLongitude;
	// 配送位置维度
	private String distributionLatitude;
	// 初始公里数
	private String initKm;
	// 初始公里费用
	private String initKmPrice;
	// 每公里单价
	private String perKmPrice;
	// 每公里费用
	private String perKmAmount;
	// 创建日期
	private Date createDate;
	// 创建人
	private String creater;
	// 更新日期
	private Date updateDate;
	// 更新人
	private String updater;
	// 师傅安装收入
	private String workerInstallAmount;
	// 师傅运费收入
	private String workerDistributionAmount;
	// 师傅初始费用
	private String workerInitPrice;
	// 实付师傅每公里费用
	private String workerPerKmPrice;
	// 商户编码
	private String merchantCode;
	// 路程
	private String distance;

	@Transient
	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
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

	@Column(name = "SERVICE_COMMDITY_AMOUNT")
	public String getServiceCommdityAmount() {
		return serviceCommdityAmount;
	}

	public void setServiceCommdityAmount(String serviceCommdityAmount) {
		this.serviceCommdityAmount = serviceCommdityAmount;
	}

	@Column(name = "INSTALL_AMOUNT")
	public String getInstallAmount() {
		return installAmount;
	}

	public void setInstallAmount(String installAmount) {
		this.installAmount = installAmount;
	}

	@Column(name = "DISTRIBUTION_AMOUNT")
	public String getDistributionAmount() {
		return distributionAmount;
	}

	public void setDistributionAmount(String distributionAmount) {
		this.distributionAmount = distributionAmount;
	}

	@Column(name = "TOTAL_AMOUNT")
	public String getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
	}

	@Column(name = "ORDER_ID")
	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	@Column(name = "CENTER_LONGITUDE")
	public String getCenterLongitude() {
		return centerLongitude;
	}

	public void setCenterLongitude(String centerLongitude) {
		this.centerLongitude = centerLongitude;
	}

	@Column(name = "CENTER_LATITUDE")
	public String getCenterLatitude() {
		return centerLatitude;
	}

	public void setCenterLatitude(String centerLatitude) {
		this.centerLatitude = centerLatitude;
	}

	@Column(name = "DISTRIBUTION_LONGITUDE")
	public String getDistributionLongitude() {
		return distributionLongitude;
	}

	public void setDistributionLongitude(String distributionLongitude) {
		this.distributionLongitude = distributionLongitude;
	}

	@Column(name = "DISTRIBUTION_LATITUDE")
	public String getDistributionLatitude() {
		return distributionLatitude;
	}

	public void setDistributionLatitude(String distributionLatitude) {
		this.distributionLatitude = distributionLatitude;
	}

	@Column(name = "INIT_KM")
	public String getInitKm() {
		return initKm;
	}

	public void setInitKm(String initKm) {
		this.initKm = initKm;
	}

	@Column(name = "INIT_KM_PRICE")
	public String getInitKmPrice() {
		return initKmPrice;
	}

	public void setInitKmPrice(String initKmPrice) {
		this.initKmPrice = initKmPrice;
	}

	@Column(name = "PER_KM_PRICE")
	public String getPerKmPrice() {
		return perKmPrice;
	}

	public void setPerKmPrice(String perKmPrice) {
		this.perKmPrice = perKmPrice;
	}

	@Column(name = "PER_KM_AMOUNT")
	public String getPerKmAmount() {
		return perKmAmount;
	}

	public void setPerKmAmount(String perKmAmount) {
		this.perKmAmount = perKmAmount;
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

	@Column(name = "WORKER_INSTALL_AMOUNT")
	public String getWorkerInstallAmount() {
		return workerInstallAmount;
	}

	public void setWorkerInstallAmount(String workerInstallAmount) {
		this.workerInstallAmount = workerInstallAmount;
	}

	@Column(name = "WORKER_DISTRIBUTION_AMOUNT")
	public String getWorkerDistributionAmount() {
		return workerDistributionAmount;
	}

	public void setWorkerDistributionAmount(String workerDistributionAmount) {
		this.workerDistributionAmount = workerDistributionAmount;
	}

	@Column(name = "WORKER_INIT_PRICE")
	public String getWorkerInitPrice() {
		return workerInitPrice;
	}

	public void setWorkerInitPrice(String workerInitPrice) {
		this.workerInitPrice = workerInitPrice;
	}

	@Column(name = "WORKER_PER_KM_PRICE")
	public String getWorkerPerKmPrice() {
		return workerPerKmPrice;
	}

	public void setWorkerPerKmPrice(String workerPerKmPrice) {
		this.workerPerKmPrice = workerPerKmPrice;
	}
	
	@Column(name = "MERCHANT_CODE")
	public String getMerchantCode() {
		return merchantCode;
	}

	public void setMerchantCode(String merchantCode) {
		this.merchantCode = merchantCode;
	}
	
}