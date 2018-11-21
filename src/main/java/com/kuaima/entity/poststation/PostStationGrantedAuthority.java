package com.kuaima.entity.poststation;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.core.GrantedAuthority;

/**
 * 驿站用户关系表
 * 
 * @date 2018年10月8日
 */
@Entity
@Table(name = "biz_granted_authority_post_station")
@GenericGenerator(name = "uuidGenerator", strategy = "uuid")
public class PostStationGrantedAuthority implements GrantedAuthority{

	
		private static final long serialVersionUID = 1L;

		@Id
		@Column(name = "ID", length = 64)
		private String id;
		
		@Column(name = "ACTOR_ID", length = 64)
		private String actorId;
		
		@Column(name = "POST_ID", length = 64)
		private String postId;

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getPostId() {
			return postId;
		}

		public void setPostId(String postId) {
			this.postId = postId;
		}

		public String getActorId() {
			return actorId;
		}

		public void setActorId(String actorId) {
			this.actorId = actorId;
		}

		@Override
		public String getAuthority() {
			if (StringUtils.isEmpty(postId)) {
				return null;
			}
			return "POST_" + postId;
		}


}
