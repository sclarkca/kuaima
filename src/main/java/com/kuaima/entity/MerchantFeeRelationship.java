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
 * 商户费用关系
 */
@Entity
@Table(name = "BIZ_MERCHANT_FEE_RELATIONSHIP")
@GenericGenerator(name = "uuidGenerator", strategy = "uuid")
@Repository
public class MerchantFeeRelationship {
	private String id;
	// 名称
	private String name;
	// 关系模板ID
	private String relationshipTempNo;
	// 商户编码
	private String merchantCode;
	// 创建日期
	private Date createDate;
	// 创建人
	private String creater;
	// 更新日期
	private Date updateDate;
	// 更新人
	private String updater;

	@Id
	@GeneratedValue(generator = "uuidGenerator")
	@Column(name = "id", unique = true)
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "RELATIONSHIP_TEMP_NO")
	public String getRelationshipTempNo() {
		return relationshipTempNo;
	}

	public void setRelationshipTempNo(String relationshipTempNo) {
		this.relationshipTempNo = relationshipTempNo;
	}

	@Column(name = "MERCHANT_CODE")
	public String getMerchantCode() {
		return merchantCode;
	}

	public void setMerchantCode(String merchantCode) {
		this.merchantCode = merchantCode;
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

	public static String getPropertyTypeByEnum(Integer enumValue) {
		switch (enumValue) {
		case 1:
			return "String";
		case 2:
			return "String";
		case 3:
			return "String";
		default:
			return "";
		}
	}

	public static String getPropertyNameByEnum(Integer enumValue) {
		switch (enumValue) {
		case 1:
			return "name";
		case 2:
			return "relationshipTempNo";
		case 3:
			return "merchantCode";
		default:
			return "";
		}
	}
}