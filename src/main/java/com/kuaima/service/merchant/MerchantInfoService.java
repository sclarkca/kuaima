package com.kuaima.service.merchant;

import org.springframework.stereotype.Service;

import com.bstek.bdf3.dorado.jpa.JpaUtil;
import com.bstek.bdf3.security.ContextUtils;
import com.bstek.dorado.view.resolver.ClientRunnableException;
import com.kuaima.entity.merchant.MerchantGrantedAuthority;
import com.kuaima.entity.merchant.MerchantInfo;

@Service
public class MerchantInfoService {
	
	/**
	 * 查询当前用户角色绑定的商户编码
	 * 
	 * @return
	 */
	public String queryNowUserMerchantCode() {
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
		String merchantCode = merchantInfo.getMerchantCode();
		return merchantCode;
	}
}