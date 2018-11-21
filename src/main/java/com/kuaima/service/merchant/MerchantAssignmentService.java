package com.kuaima.service.merchant;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.bstek.bdf3.dorado.jpa.JpaUtil;
import com.bstek.bdf3.security.ContextUtils;
import com.bstek.bdf3.security.orm.User;
import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.Expose;
import com.bstek.dorado.data.provider.Page;
import com.bstek.dorado.view.resolver.ClientRunnableException;
import com.kuaima.entity.merchant.MerchantGrantedAuthority;
import com.kuaima.entity.merchant.MerchantInfo;

/**
 */
@Service
@Transactional
public class MerchantAssignmentService{

	/**
	 * 查询未绑定商户的用户信息
	 */
	@DataProvider
    public void loadUsersWithout(Page<User> page, Map<String,Object> parameter) {
        JpaUtil.linq(User.class).notExists(MerchantGrantedAuthority.class).equalProperty("actorId", "username").end().paging(page);
    }

	@DataProvider
    public void loadUsersWithin(Page<User> page, Map<String,Object> parameter) {
		if (null != parameter ){
			String merchantId = (String) parameter.get("parameter");
			JpaUtil.linq(User.class).exists(MerchantGrantedAuthority.class).equalProperty("actorId", "username")
			.equal("merchantId", merchantId).end().paging(page);
		}
    }
	
	@Expose
	@Transactional
	public String updateMerchantInfo(Map<String, Object> parameter) {
		if (null != parameter && !parameter.isEmpty()) {
			MerchantInfo merchantInfo = (MerchantInfo) parameter.get("dgMerchantCurrent");
			if (null == merchantInfo) {
				throw new ClientRunnableException("dorado.widget.NotifyTipManager.notify('请选择绑定的商户');");
			}
			String userStr = JSON.toJSONString(parameter.get("userSourceSelection"));
			if (StringUtils.isBlank(userStr)) {
				throw new ClientRunnableException("dorado.widget.NotifyTipManager.notify('请选择绑定的用户');");
			}
			List<User> list = JSON.parseArray(userStr, User.class);
			String id = merchantInfo.getId();
			List<MerchantGrantedAuthority> mgas = new ArrayList<>();
			for (User user : list) {
				String username = user.getUsername();
				// 保存商户和用户的绑定关系
				MerchantGrantedAuthority mga = new MerchantGrantedAuthority();
				mga.setActorId(username);
				mga.setMerchantId(id);
				mgas.add(mga);
			}
			List<MerchantGrantedAuthority> persist = JpaUtil.persist(mgas);
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
	public String deleteMerchantUser(Map<String, Object> parameter) {
		if (null != parameter && !parameter.isEmpty()) {
			String userTargets = JSON.toJSONString(parameter.get("userTargets"));
			if (StringUtils.isBlank(userTargets)) {
				throw new ClientRunnableException("dorado.widget.NotifyTipManager.notify('请选择移除的用户');");
			}
			boolean flag = true;
			List<User> list = JSON.parseArray(userTargets, User.class);
			for (User user : list) {
				String username = user.getUsername();
				int delete = JpaUtil.lind(MerchantGrantedAuthority.class).equal("actorId", username).delete();
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
	
	/**
	 * 获取当前用户绑定的商户编码
	 * @return
	 */
	public String getCurrentMerchantCode(){
		StringBuilder script = new StringBuilder();
		String loginUsername = ContextUtils.getLoginUsername();
		// 根据用户查询商户和用户绑定关系表
		MerchantGrantedAuthority mga;
		try {
			mga = JpaUtil.linq(MerchantGrantedAuthority.class).equal("actorId", loginUsername).findOne();
		} catch (Exception e) {
			script.append("dorado.widget.NotifyTipManager.notify('请确认用户是否绑定商户');");
			throw new ClientRunnableException(script.toString());
		}
		String merchantId = mga.getMerchantId();
		// 根据id查询MerchantInfo表获取商户信息
		MerchantInfo merchantInfo = JpaUtil.linq(MerchantInfo.class).equal("id", merchantId).findOne();
		return merchantInfo.getMerchantCode();
	}
}
