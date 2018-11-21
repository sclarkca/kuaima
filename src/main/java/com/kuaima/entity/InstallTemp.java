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
 * 安装价格模板
 */
@Entity
@Table(name = "BIZ_INSTALL_TEMP")
@GenericGenerator(name = "uuidGenerator", strategy = "uuid")
@Repository
public class InstallTemp {
	private String id;
	// 模板编码
	private String installTempNo;
	// 模板名称
	private String installTempName;
	// 价格
	private String price;
	// 师傅完成金额
	private String finishPrice;
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

	@Column(name = "INSTALL_TEMP_NO")
	public String getInstallTempNo() {
		return installTempNo;
	}

	public void setInstallTempNo(String installTempNo) {
		this.installTempNo = installTempNo;
	}

	@Column(name = "INSTALL_TEMP_NAME")
	public String getInstallTempName() {
		return installTempName;
	}

	public void setInstallTempName(String installTempName) {
		this.installTempName = installTempName;
	}

	@Column(name = "PRICE")
	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	@Column(name = "FINISH_PRICE")
	public String getFinishPrice() {
		return finishPrice;
	}

	public void setFinishPrice(String finishPrice) {
		this.finishPrice = finishPrice;
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
		case 4:
			return "String";
		default:
			return "";
		}
	}

	public static String getPropertyNameByEnum(Integer enumValue) {
		switch (enumValue) {
		case 1:
			return "installTempNo";
		case 2:
			return "installTempName";
		case 3:
			return "price";
		case 4:
			return "finishPrice";
		default:
			return "";
		}
	}
}