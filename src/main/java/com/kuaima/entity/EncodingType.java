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
 * 编码新增记录
 */
@Entity
@Table(name = "BIZ_ENCODING_TYPE")
@GenericGenerator(name = "uuidGenerator", strategy = "uuid")
@Repository
public class EncodingType {
	private String id;
	// 编码类型
	private String encodingTypeCode;
	// 编码类型名称
	private String encodingTypeName;
	// 最新的编码
	private String lastCode;
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
		default:
			return "";
		}
	};

	// 根据enum值返回对应的属性类型;
	public static String getPropertyTypeByEnum(int enumValue) {
		switch (enumValue) {
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

	@Column(name = "ENCODING_TYPE_CODE")
	public String getEncodingTypeCode() {
		return encodingTypeCode;
	}

	public void setEncodingTypeCode(String encodingTypeCode) {
		this.encodingTypeCode = encodingTypeCode;
	}

	@Column(name = "ENCODING_TYPE_NAME")
	public String getEncodingTypeName() {
		return encodingTypeName;
	}

	public void setEncodingTypeName(String encodingTypeName) {
		this.encodingTypeName = encodingTypeName;
	}

	@Column(name = "LAST_CODE")
	public String getLastCode() {
		return lastCode;
	}

	public void setLastCode(String lastCode) {
		this.lastCode = lastCode;
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