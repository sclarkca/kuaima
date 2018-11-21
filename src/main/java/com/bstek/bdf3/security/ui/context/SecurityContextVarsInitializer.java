package com.bstek.bdf3.security.ui.context;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.WebAttributes;

import com.bstek.bdf3.security.ContextUtils;
import com.bstek.dorado.core.el.ContextVarsInitializer;
import com.bstek.dorado.web.DoradoContext;

/**
 * @author Kevin Yang (mailto:kevin.yang@bstek.com)
 * @since 2016年12月28日
 */
public class SecurityContextVarsInitializer implements ContextVarsInitializer {

	@Override
	public void initializeContext(Map<String, Object> vars) throws Exception {
		String loginUsername = ContextUtils.getLoginUsername();
		if (StringUtils.isNotBlank(loginUsername)){
			vars.put("OAUsername",loginUsername.substring("RCAMS".length(), loginUsername.length()));
		}
		vars.put("loginUsername", ContextUtils.getLoginUsername());
		vars.put("loginUser", ContextUtils.getLoginUser());
		AuthenticationException ex = (AuthenticationException) DoradoContext.getAttachedRequest().getSession()
				.getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
		String errorMsg = ex != null ? ex.getMessage() : "none";
		vars.put("authenticationErrorMsg", errorMsg);

	}

}
