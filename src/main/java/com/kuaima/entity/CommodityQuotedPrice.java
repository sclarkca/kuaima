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
 * 商品报价模板关系
 */
@Entity
@Table(name = "BIZ_COMMODITY_QUOTED_PRICE")
@GenericGenerator(name = "uuidGenerator", strategy = "uuid")
@Repository
public class CommodityQuotedPrice {
	// ID
	private String id;
	// 商品编码
	private String sku;
	// 关系模板编码
	private String relationshipTempNo;
	// 关系模板名称
	private String relationshipTempName;
	// 安装模板
	private String installTempNo;
	// 运费模板
	private String transTempNo;
	// 模板标题
	private String tempTitle;
	// 城市等级
	private String cityLevel;
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

	@Column(name = "SKU")
	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	@Column(name = "RELATIONSHIP_TEMP_NO")
	public String getRelationshipTempNo() {
		return relationshipTempNo;
	}

	public void setRelationshipTempNo(String relationshipTempNo) {
		this.relationshipTempNo = relationshipTempNo;
	}

	@Column(name = "RELATIONSHIP_TEMP_NAME")
	public String getRelationshipTempName() {
		return relationshipTempName;
	}

	public void setRelationshipTempName(String relationshipTempName) {
		this.relationshipTempName = relationshipTempName;
	}

	@Column(name = "INSTALL_TEMP_NO")
	public String getInstallTempNo() {
		return installTempNo;
	}

	public void setInstallTempNo(String installTempNo) {
		this.installTempNo = installTempNo;
	}

	@Column(name = "TRANS_TEMP_NO")
	public String getTransTempNo() {
		return transTempNo;
	}

	public void setTransTempNo(String transTempNo) {
		this.transTempNo = transTempNo;
	}

	@Column(name = "TEMP_TITLE")
	public String getTempTitle() {
		return tempTitle;
	}

	public void setTempTitle(String tempTitle) {
		this.tempTitle = tempTitle;
	}

	@Column(name = "CITY_LEVEL")
	public String getCityLevel() {
		return cityLevel;
	}

	public void setCityLevel(String cityLevel) {
		this.cityLevel = cityLevel;
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

	public static String getPropertyNameByEnum(Integer enumValue) {
		switch (enumValue) {
		case 1:
			return "sku";
		case 2:
			return "relationshipTempNo";
		case 3:
			return "relationshipTempName";
		case 4:
			return "installTempNo";
		case 5:
			return "transTempNo";
		case 6:
			return "tempTitle";
		case 7:
			return "cityLevel";
		default:
			return "";
		}
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
		case 7:
			return "String";
		default:
			return "";
		}
	}
}