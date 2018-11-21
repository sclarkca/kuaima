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
 * 运费模板
 */
@Entity
@Table(name = "BIZ_TRANS_FEE_TEMP")
@GenericGenerator(name = "uuidGenerator", strategy = "uuid")
@Repository
public class TransFeeTemp {
	private String id;
	// 模板编码
	private String transTempNo;
	// 模板名称
	private String transTempName;
	// 初始价格
	private String initPrice;
	// 每公里价格
	private String perKmPrice;
	// 师傅初始金额
	private String serverInitPrice;
	// 实付每公里价格
	private String actualPerKmPrice;
	// 创建日期
	private Date createDate;
	// 创建人
	private String creater;
	// 更新日期
	private Date updateDate;
	// 更新人
	private String updater;
	// 初始公里
	private String initKm;
	@Id
	@GeneratedValue(generator = "uuidGenerator")
	@Column(name = "id", unique = true)
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "TRANS_TEMP_NO")
	public String getTransTempNo() {
		return transTempNo;
	}

	public void setTransTempNo(String transTempNo) {
		this.transTempNo = transTempNo;
	}

	@Column(name = "TRANS_TEMP_NAME")
	public String getTransTempName() {
		return transTempName;
	}

	public void setTransTempName(String transTempName) {
		this.transTempName = transTempName;
	}

	@Column(name = "INIT_PRICE")
	public String getInitPrice() {
		return initPrice;
	}

	public void setInitPrice(String initPrice) {
		this.initPrice = initPrice;
	}

	@Column(name = "PER_KM_PRICE")
	public String getPerKmPrice() {
		return perKmPrice;
	}

	public void setPerKmPrice(String perKmPrice) {
		this.perKmPrice = perKmPrice;
	}

	@Column(name = "SERVER_INIT_PRICE")
	public String getServerInitPrice() {
		return serverInitPrice;
	}

	public void setServerInitPrice(String serverInitPrice) {
		this.serverInitPrice = serverInitPrice;
	}

	@Column(name = "ACTUAL_PER_KM_PRICE")
	public String getActualPerKmPrice() {
		return actualPerKmPrice;
	}

	public void setActualPerKmPrice(String actualPerKmPrice) {
		this.actualPerKmPrice = actualPerKmPrice;
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
	
	@Column(name = "INIT_KM")
	public String getInitKm() {
		return initKm;
	}

	public void setInitKm(String initKm) {
		this.initKm = initKm;
	}

	public static String getPropertyTypeByEnum(Integer enumValue) {
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
		case 6:
			return "String";
		default:
			return "";
		}
	}

	public static String getPropertyNameByEnum(Integer enumValue) {
		switch (enumValue) {
		case 1:
			return "transTempNo";
		case 2:
			return "transTempName";
		case 3:
			return "initPrice";
		case 4:
			return "perKmPrice";
		case 5:
			return "serverInitPrice";
		case 6:
			return "actualPerKmPrice";
		default:
			return "";
		}
	}
}