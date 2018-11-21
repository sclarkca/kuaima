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
 * 公告/私信
 */
@Entity
@Table(name = "BIZ_NOTICE")
@GenericGenerator(name = "uuidGenerator", strategy = "uuid")
@Repository
public class NoticeNews {
//	ID` varchar(32) NOT NULL,
//	  `TITLE` varchar(64) DEFAULT NULL COMMENT '标题',
//	  `CONTENT` varchar(255) DEFAULT NULL COMMENT '内容',
//	  `TIME` datetime DEFAULT NULL COMMENT '时间',
//	  `TIMESTAMP` bigint(20) DEFAULT NULL COMMENT '同上时间戳，作为分页排序使用',
//	  `SCOPE` int(11) DEFAULT NULL COMMENT '私信类 scoup(1) 公告类 scoup(2)',
//	  `TYPE` int(32) DEFAULT NULL COMMENT '处罚:type(1)通知:type(2)公告:type(3)',
//	  `WORKER_ID` bigint(20) DEFAULT NULL COMMENT '用户编码',
//	  `STATUS` tinyint(32) DEFAULT NULL COMMENT '状态',
//	  `CREATE_DATE` datetime DEFAULT NULL COMMENT '创建日期',
//	  `CREATER` varchar(32) DEFAULT NULL COMMENT '创建人',
//	  `UPDATE_DATE` datetime DEFAULT NULL COMMENT '更新日期',
//	  `UPDATER` varchar(32) DEFAULT NULL COMMENT '更新人',
	private String id;
	// 标题
	private String title;
	// 内容
	private String content;
	// 时间
	private Date time;
	// 类型
	private Integer type;
	// 用户编码
	private Long workerId;
	// 状态
	private Integer status;
	// 创建日期
	private Date createDate;
	// 创建人
	private String creater;
	// 更新日期
	private Date updateDate;
	// 更新人
	private String updater;
	// 时间戳(毫秒值,排序使用)
	private Long  timestamp;
	// 范围
	private Integer scope;
	

	@Id
	@GeneratedValue(generator = "uuidGenerator")
	@Column(name = "id", unique = true)
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "TITLE")
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Column(name = "CONTENT")
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "TIME")
	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	@Column(name = "TYPE")
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	@Column(name = "WORKER_ID")
	public Long getWorkerId() {
		return workerId;
	}

	public void setWorkerId(Long workerId) {
		this.workerId = workerId;
	}

	@Column(name = "STATUS")
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
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

	@Column(name = "TIMESTAMP")
	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	@Column(name = "SCOPE")
	public Integer getScope() {
		return scope;
	}

	public void setScope(Integer scope) {
		this.scope = scope;
	}
	
}