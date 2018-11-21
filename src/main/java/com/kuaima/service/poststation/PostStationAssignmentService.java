package com.kuaima.service.poststation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.bstek.bdf3.dorado.jpa.JpaUtil;
import com.bstek.bdf3.security.orm.User;
import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.Expose;
import com.bstek.dorado.data.provider.Page;
import com.bstek.dorado.view.resolver.ClientRunnableException;
import com.kuaima.entity.PostStation;
import com.kuaima.entity.poststation.PostStationGrantedAuthority;

/**
 */
@Service
@Transactional
public class PostStationAssignmentService{

	/**
	 * 查询未绑定驿站的用户信息
	 */
	@DataProvider
    public void loadUsersWithout(Page<User> page, Map<String,Object> parameter) {
        JpaUtil.linq(User.class).notExists(PostStationGrantedAuthority.class).equalProperty("actorId", "username").end().paging(page);
    }

	@DataProvider
    public void loadUsersWithin(Page<User> page, Map<String,Object> parameter) {
		if (null != parameter ){
			String postId = (String) parameter.get("parameter");
			JpaUtil.linq(User.class).exists(PostStationGrantedAuthority.class).equalProperty("actorId", "username")
			.equal("postId", postId).end().paging(page);
		}
    }
	
	@Expose
	@Transactional
	public String updatePostStationInfo(Map<String, Object> parameter) {
		if (null != parameter && !parameter.isEmpty()) {
			PostStation postStation = (PostStation) parameter.get("dgPostStationCurrent");
			if (null == postStation) {
				throw new ClientRunnableException("dorado.widget.NotifyTipManager.notify('请选择绑定的驿站');");
			}
			String userStr = JSON.toJSONString(parameter.get("userSourceSelection"));
			if (StringUtils.isBlank(userStr)) {
				throw new ClientRunnableException("dorado.widget.NotifyTipManager.notify('请选择绑定的用户');");
			}
			List<User> list = JSON.parseArray(userStr, User.class);
			String id = postStation.getId();
			List<PostStationGrantedAuthority> mgas = new ArrayList<>();
			for (User user : list) {
				String username = user.getUsername();
				// 保存商户和用户的绑定关系
				PostStationGrantedAuthority psga = new PostStationGrantedAuthority();
				psga.setActorId(username);
				psga.setPostId(id);
				mgas.add(psga);
			}
			List<PostStationGrantedAuthority> persist = JpaUtil.persist(mgas);
			if (null != persist && !persist.isEmpty()) {
				return "SUCCESS";
			}
		}
		return null;
	}
	
	/**
	 * 解除绑定
	 * @param parameter
	 * @return
	 */
	@Expose	
	public String deletePostStationUser(Map<String, Object> parameter) {
		if (null != parameter && !parameter.isEmpty()) {
			String userTargets = JSON.toJSONString(parameter.get("userTargets"));
			if (StringUtils.isBlank(userTargets)) {
				throw new ClientRunnableException("dorado.widget.NotifyTipManager.notify('请选择移除的用户');");
			}
			boolean flag = true;
			List<User> list = JSON.parseArray(userTargets, User.class);
			for (User user : list) {
				String username = user.getUsername();
				int delete = JpaUtil.lind(PostStationGrantedAuthority.class).equal("actorId", username).delete();
				if (delete == 0){
					flag = false;
				}
			}
			if (flag){
				return "SUCCESS";
			}
		}
		return null;
	}
}
