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
 * 订单信息
 */
@Entity
@Table(name = "BIZ_SERVICE_ORDER")
@GenericGenerator(name = "uuidGenerator", strategy = "uuid")
@Repository
public class ServiceOrder {
	private String id;
	// 定单编码
	private String orderNo;
	// 订单状态
	private String orderStatus;
	// 安装师傅编码
	private String workerId;
	// 收货人
	private String receiver;
	// 收货手机
	private String receiverPhone;
	// 省
	private String province;
	// 市
	private String city;
	// 区县
	private String region;
	// 镇
	private String town;
	// 详细地址
	private String address;
	// 预约安装时间
	private Date appointmentTime;
	// 安装完成时间
	private Date finishTime;
	// 系统分配时间
	private Date allocateTime;
	// 师傅取件时间
	private Date takeTime;
	// 商品编码
	private String sku;
	// 物流单号
	private String logisticsNo;
	// 驿站编码
	private String postStationCode;
	// 第三方订单编码
	private String retailerOrderNo;
	// 是否配送
	private Integer hasDistribution;
	// 是否安装
	private Integer hasInstallation;
	// 创建日期
	private Date createDate;
	// 创建人
	private String creater;
	// 更新日期
	private Date updateDate;
	// 更新人
	private String updater;
	// 省名称
	private String provinceName;
	// 市名称
	private String cityName;
	// 区名称
	private String regionName;

	// 商户编码
	private String merchantCode;

	// 驿站名称
	private String postStationName;

	// 商品名称
	private String commodityName;

	// 品类编码
	private String buCode;
	// 品类名称
	private String buName;

	// 安装师傅cardName
	private String worker;

	@Id
	@GeneratedValue(generator = "uuidGenerator")
	@Column(name = "id", unique = true)
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "ORDER_NO")
	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	@Column(name = "ORDER_STATUS")
	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	@Column(name = "WORKER_ID")
	public String getWorkerId() {
		return workerId;
	}

	public void setWorkerId(String workerId) {
		this.workerId = workerId;
	}

	@Column(name = "RECEIVER")
	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	@Column(name = "RECEIVER_PHONE")
	public String getReceiverPhone() {
		return receiverPhone;
	}

	public void setReceiverPhone(String receiverPhone) {
		this.receiverPhone = receiverPhone;
	}

	@Column(name = "PROVINCE")
	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	@Column(name = "CITY")
	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	@Column(name = "REGION")
	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	@Column(name = "TOWN")
	public String getTown() {
		return town;
	}

	public void setTown(String town) {
		this.town = town;
	}

	@Column(name = "ADDRESS")
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "APPOINTMENT_TIME")
	public Date getAppointmentTime() {
		return appointmentTime;
	}

	public void setAppointmentTime(Date appointmentTime) {
		this.appointmentTime = appointmentTime;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "FINISH_TIME")
	public Date getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(Date finishTime) {
		this.finishTime = finishTime;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "ALLOCATE_TIME")
	public Date getAllocateTime() {
		return allocateTime;
	}

	public void setAllocateTime(Date allocateTime) {
		this.allocateTime = allocateTime;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "TAKE_TIME")
	public Date getTakeTime() {
		return takeTime;
	}

	public void setTakeTime(Date takeTime) {
		this.takeTime = takeTime;
	}

	@Column(name = "SKU")
	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	@Column(name = "LOGISTICS_NO")
	public String getLogisticsNo() {
		return logisticsNo;
	}

	public void setLogisticsNo(String logisticsNo) {
		this.logisticsNo = logisticsNo;
	}

	@Column(name = "POST_STATION_CODE")
	public String getPostStationCode() {
		return postStationCode;
	}

	public void setPostStationCode(String postStationCode) {
		this.postStationCode = postStationCode;
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

	@Transient
	public String getProvinceName() {
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	@Transient
	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	@Transient
	public String getRegionName() {
		return regionName;
	}

	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}

	@Column(name = "HAS_DISTRIBUTION")
	public Integer getHasDistribution() {
		return hasDistribution;
	}

	public void setHasDistribution(Integer hasDistribution) {
		this.hasDistribution = hasDistribution;
	}

	@Column(name = "HAS_INSTALLATION")
	public Integer getHasInstallation() {
		return hasInstallation;
	}

	public void setHasInstallation(Integer hasInstallation) {
		this.hasInstallation = hasInstallation;
	}

	@Column(name = "RETAILER_ORDER_NO")
	public String getRetailerOrderNo() {
		return retailerOrderNo;
	}

	public void setRetailerOrderNo(String retailerOrderNo) {
		this.retailerOrderNo = retailerOrderNo;
	}

	@Column(name = "MERCHANT_CODE")
	public String getMerchantCode() {
		return merchantCode;
	}

	public void setMerchantCode(String merchantCode) {
		this.merchantCode = merchantCode;
	}

	@Transient
	public String getPostStationName() {
		return postStationName;
	}

	public void setPostStationName(String postStationName) {
		this.postStationName = postStationName;
	}

	@Transient
	public String getCommodityName() {
		return commodityName;
	}

	public void setCommodityName(String commodityName) {
		this.commodityName = commodityName;
	}

	@Transient
	public String getBuCode() {
		return buCode;
	}

	public void setBuCode(String buCode) {
		this.buCode = buCode;
	}

	@Transient
	public String getBuName() {
		return buName;
	}

	public void setBuName(String buName) {
		this.buName = buName;
	}

	@Transient
	public String getWorker() {
		return worker;
	}

	public void setWorker(String worker) {
		this.worker = worker;
	}

}