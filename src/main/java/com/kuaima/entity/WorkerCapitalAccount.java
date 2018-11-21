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
 * 师傅资金账户
 */
@Entity
@Table(name = "BIZ_WORKER_CAPITAL_ACCOUNT")
@GenericGenerator(name = "uuidGenerator", strategy = "uuid")
@Repository
public class WorkerCapitalAccount {
	private String id;
	// 总金额
	private String totalAmount;
	// 可提现金额
	private String canCashAmount;
	// 冻结金额
	private String frozenAmount;
	// 已提现总额
	private String alreadyCashAmount;
	// 师傅编码
	private String workerId;
	// 创建日期
	private Date createDate;
	// 创建人
	private String creater;
	// 更新日期
	private Date updateDate;
	// 更新人
	private String updater;

	// 根据enum值返回对应的属性名称;
	public static String getPropertyNameByEnum(int enumValue) {
		switch (enumValue) {
		case 1:
			return "totalAmount";
		case 2:
			return "canCashAmount";
		case 3:
			return "frozenAmount";
		case 4:
			return "alreadyCashAmount";
		case 5:
			return "workerId";
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

	@Column(name = "TOTAL_AMOUNT")
	public String getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
	}

	@Column(name = "CAN_CASH_AMOUNT")
	public String getCanCashAmount() {
		return canCashAmount;
	}

	public void setCanCashAmount(String canCashAmount) {
		this.canCashAmount = canCashAmount;
	}

	@Column(name = "FROZEN_AMOUNT")
	public String getFrozenAmount() {
		return frozenAmount;
	}

	public void setFrozenAmount(String frozenAmount) {
		this.frozenAmount = frozenAmount;
	}

	@Column(name = "ALREADY_CASH_AMOUNT")
	public String getAlreadyCashAmount() {
		return alreadyCashAmount;
	}

	public void setAlreadyCashAmount(String alreadyCashAmount) {
		this.alreadyCashAmount = alreadyCashAmount;
	}

	@Column(name = "WORKER_ID")
	public String getWorkerId() {
		return workerId;
	}

	public void setWorkerId(String workerId) {
		this.workerId = workerId;
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
}