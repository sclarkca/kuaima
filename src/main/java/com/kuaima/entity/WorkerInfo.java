package com.kuaima.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.springframework.stereotype.Repository;

/**
 * 师傅信息
 */
@Entity
@Table(name = "BIZ_WORKER_INFO")
@Repository
public class WorkerInfo {
	// 师傅编码
	private Long workerId;
	// 所在城市
	private String cityId;
	// 真实姓名
	private String cardName;
	// 身份证号
	private String cardId;
	// 身份证正面
	private String cardFrontUrl;
	// 身份证反面
	private String cardBackUrl;
	// 紧急联系人
	private String emergencyContactPerson;
	// 紧急联系方式
	private String emergencyContact;
	// 汽车类型
	private Integer carType;
	// 照片
	private String carImageUrl;
	// 车牌号
	private String licensePlate;
	// 驾驶证照片
	private String driversLicenseUrl;
	// 行驶证照片
	private String drivingLicenseUrl;
	// 审核状态
	private Integer status;
	// 是否缴纳保证金
	private String payDeposit;
	// 保证金金额
	private String deposit;
	// 保证金编码
	private String depositCode;
	// 保证金名称
	private String depositName;
	// 手机号
	private String workerPhone;
	// 注册时间
	private Date registrationTime;
	// 最后登录时间
	private Date lastLoginTime;
	// 会员等级
	private String memberRank;
	// 评分
	private Double score;
	// 是否接单
	private String isWorking;
	// 创建日期
	private Date createTime;
	// 更新日期
	private Date updateTime;
	// 有无车辆
	private Integer hasCar;
	// 所在城市名称
	private String cityName;


	@Id
	@Column(name = "WORKER_ID")
	public Long getWorkerId() {
		return workerId;
	}

	public void setWorkerId(Long workerId) {
		this.workerId = workerId;
	}

	@Column(name = "CITY_ID")
	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	@Column(name = "CARD_NAME")
	public String getCardName() {
		return cardName;
	}

	public void setCardName(String cardName) {
		this.cardName = cardName;
	}

	@Column(name = "CARD_ID")
	public String getCardId() {
		return cardId;
	}

	public void setCardId(String cardId) {
		this.cardId = cardId;
	}

	@Column(name = "CARD_FRONT_URL")
	public String getCardFrontUrl() {
		return cardFrontUrl;
	}

	public void setCardFrontUrl(String cardFrontUrl) {
		this.cardFrontUrl = cardFrontUrl;
	}

	@Column(name = "CARD_BACK_URL")
	public String getCardBackUrl() {
		return cardBackUrl;
	}

	public void setCardBackUrl(String cardBackUrl) {
		this.cardBackUrl = cardBackUrl;
	}

	@Column(name = "EMERGENCY_CONTACT_PERSON")
	public String getEmergencyContactPerson() {
		return emergencyContactPerson;
	}

	public void setEmergencyContactPerson(String emergencyContactPerson) {
		this.emergencyContactPerson = emergencyContactPerson;
	}

	@Column(name = "EMERGENCY_CONTACT")
	public String getEmergencyContact() {
		return emergencyContact;
	}

	public void setEmergencyContact(String emergencyContact) {
		this.emergencyContact = emergencyContact;
	}

	@Column(name = "CAR_TYPE")
	public Integer getCarType() {
		return carType;
	}

	public void setCarType(Integer carType) {
		this.carType = carType;
	}

	@Column(name = "CAR_IMAGE_URL")
	public String getCarImageUrl() {
		return carImageUrl;
	}

	public void setCarImageUrl(String carImageUrl) {
		this.carImageUrl = carImageUrl;
	}

	@Column(name = "LICENSE_PLATE")
	public String getLicensePlate() {
		return licensePlate;
	}

	public void setLicensePlate(String licensePlate) {
		this.licensePlate = licensePlate;
	}

	@Column(name = "DRIVERS_LICENSE_URL")
	public String getDriversLicenseUrl() {
		return driversLicenseUrl;
	}

	public void setDriversLicenseUrl(String driversLicenseUrl) {
		this.driversLicenseUrl = driversLicenseUrl;
	}

	@Column(name = "DRIVING_LICENSE_URL")
	public String getDrivingLicenseUrl() {
		return drivingLicenseUrl;
	}

	public void setDrivingLicenseUrl(String drivingLicenseUrl) {
		this.drivingLicenseUrl = drivingLicenseUrl;
	}

	@Column(name = "STATUS")
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Column(name = "PAY_DEPOSIT")
	public String getPayDeposit() {
		return payDeposit;
	}

	public void setPayDeposit(String payDeposit) {
		this.payDeposit = payDeposit;
	}

	@Column(name = "DEPOSIT")
	public String getDeposit() {
		return deposit;
	}

	public void setDeposit(String deposit) {
		this.deposit = deposit;
	}

	@Column(name = "DEPOSIT_CODE")
	public String getDepositCode() {
		return depositCode;
	}

	public void setDepositCode(String depositCode) {
		this.depositCode = depositCode;
	}

	@Column(name = "DEPOSIT_NAME")
	public String getDepositName() {
		return depositName;
	}

	public void setDepositName(String depositName) {
		this.depositName = depositName;
	}

	@Column(name = "WORKER_PHONE")
	public String getWorkerPhone() {
		return workerPhone;
	}

	public void setWorkerPhone(String workerPhone) {
		this.workerPhone = workerPhone;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "REGISTRATION_TIME")
	public Date getRegistrationTime() {
		return registrationTime;
	}

	public void setRegistrationTime(Date registrationTime) {
		this.registrationTime = registrationTime;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "LAST_LOGIN_TIME")
	public Date getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	@Column(name = "MEMBER_RANK")
	public String getMemberRank() {
		return memberRank;
	}

	public void setMemberRank(String memberRank) {
		this.memberRank = memberRank;
	}

	@Column(name = "SCORE")
	public Double getScore() {
		return score;
	}

	public void setScore(Double score) {
		this.score = score;
	}

	@Column(name = "IS_WORKING")
	public String getIsWorking() {
		return isWorking;
	}

	public void setIsWorking(String isWorking) {
		this.isWorking = isWorking;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATE_TIME")
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATE_TIME")
	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	@Column(name = "HAS_CAR")
	public Integer getHasCar() {
		return hasCar;
	}

	public void setHasCar(Integer hasCar) {
		this.hasCar = hasCar;
	}

	@Transient
	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

}