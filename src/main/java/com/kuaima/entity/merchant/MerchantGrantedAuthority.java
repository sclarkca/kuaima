package com.kuaima.entity.merchant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.core.GrantedAuthority;

/**
 * 商户角色关系表
 * 
 * @date 2018年10月8日
 */
@Entity
@Table(name = "BIZ_GRANTED_AUTHORITY_MERCHANT")
@GenericGenerator(name = "uuidGenerator", strategy = "uuid")
public class MerchantGrantedAuthority implements GrantedAuthority {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID", length = 64)
	private String id;
	
	@Column(name = "ACTOR_ID", length = 64)
	private String actorId;
	
	@Column(name = "MERCHANT_ID", length = 64)
	private String merchantId;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public String getActorId() {
		return actorId;
	}

	public void setActorId(String actorId) {
		this.actorId = actorId;
	}

	@Override
	public String getAuthority() {
		if (StringUtils.isEmpty(merchantId)) {
			return null;
		}
		return "MERCHANT_" + merchantId;
	}

}
