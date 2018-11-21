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
 * 驿站信息
 */
@Entity
@Table(name = "BIZ_POST_STATION")
@GenericGenerator(name = "uuidGenerator", strategy = "uuid")
@Repository
public class PostStation {
	private String id;
	// 驿站编码
	private String postStationCode;
	// 驿站名称
	private String postStationName;
	// 负责人姓名
	private String head;
	// 负责人手机号
	private String headPhone;
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

	// 根据enum值返回对应的属性名称;
	public static String getPropertyNameByEnum(int enumValue) {
		switch (enumValue) {
		case 1:
			return "postStationCode";
		case 2:
			return "postStationName";
		case 3:
			return "head";
		case 4:
			return "headPhone";
		case 5:
			return "province";
		case 6:
			return "city";
		case 7:
			return "region";
		case 8:
			return "town";
		case 9:
			return "address";
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
		case 6:
			return "String";
		case 7:
			return "String";
		case 8:
			return "String";
		case 9:
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

	@Column(name = "POST_STATION_CODE")
	public String getPostStationCode() {
		return postStationCode;
	}

	public void setPostStationCode(String postStationCode) {
		this.postStationCode = postStationCode;
	}

	@Column(name = "POST_STATION_NAME")
	public String getPostStationName() {
		return postStationName;
	}

	public void setPostStationName(String postStationName) {
		this.postStationName = postStationName;
	}

	@Column(name = "HEAD")
	public String getHead() {
		return head;
	}

	public void setHead(String head) {
		this.head = head;
	}

	@Column(name = "HEAD_PHONE")
	public String getHeadPhone() {
		return headPhone;
	}

	public void setHeadPhone(String headPhone) {
		this.headPhone = headPhone;
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

}