/* Copyright 2004, 2005, 2006 Acegi Technology Pty Limited
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.bstek.bdf3.security.access.intercept;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.springframework.security.access.SecurityMetadataSource;
import org.springframework.security.access.intercept.AbstractSecurityInterceptor;
import org.springframework.security.access.intercept.InterceptorStatusToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;


/**
 * 菜单安全拦截器
 * @author Kevin Yang (mailto:kevin.yang@bstek.com)
 * @since 2016年1月23日
 */
public class FilterSecurityInterceptor extends AbstractSecurityInterceptor implements Filter {

	// ~ Static fields/initializers
	// =====================================================================================

	private static final String FILTER_APPLIED = "__spring_security_filterSecurityInterceptor_filterApplied_2";

	// ~ Instance fields
	// ================================================================================================

	private FilterInvocationSecurityMetadataSource securityMetadataSource;
	private boolean observeOncePerRequest = true;

	// ~ Methods
	// ========================================================================================================

	/**
	 * Not used (we rely on IoC container lifecycle services instead)
	 *
	 * @param arg0 ignored
	 *
	 * @throws ServletException never thrown
	 */
	@Override
	public void init(FilterConfig arg0) throws ServletException {
	 // Do nothing
	}

	/**
	 * Not used (we rely on IoC container lifecycle services instead)
	 */
	@Override
	public void destroy() {
	 // Do nothing because of X and Y.
	}

	/**
	 * Method that is actually called by the filter chain. Simply delegates to the
	 * {@link #invoke(FilterInvocation)} method.
	 *
	 * @param request the servlet request
	 * @param response the servlet response
	 * @param chain the filter chain
	 *
	 * @throws IOException if the filter chain fails
	 * @throws ServletException if the filter chain fails
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) {
		FilterInvocation fi = new FilterInvocation(request, response, chain);
		try {
			invoke(fi);
		} catch (Exception e) {
			logger.error("分发异常"+e);
		}
	}
	
	public FilterInvocationSecurityMetadataSource getSecurityMetadataSource() {
		return this.securityMetadataSource;
	}
	@Override
	public SecurityMetadataSource obtainSecurityMetadataSource() {
		return this.securityMetadataSource;
	}

	public void setSecurityMetadataSource(FilterInvocationSecurityMetadataSource newSource) {
		this.securityMetadataSource = newSource;
	}

	@Override
	public Class<?> getSecureObjectClass() {
		return FilterInvocation.class;
	}

	public void invoke(FilterInvocation fi) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if ((fi.getRequest() != null)
				&& (fi.getRequest().getAttribute(FILTER_APPLIED) != null)
				&& observeOncePerRequest
				|| (auth.isAuthenticated() && auth.getPrincipal() instanceof String && "anonymousUser".equals(auth.getPrincipal()))) {
			// filter already applied to this request and user wants us to observe
			// once-per-request handling, so don't re-do security checking
			try {
				fi.getChain().doFilter(fi.getRequest(), fi.getResponse());
			} catch (IOException | ServletException e) {
				// Auto-generated catch block
				logger.error("doFilter异常1"+e);
			}
		}
		else {
			// first time this request being called, so perform security checking
			if (fi.getRequest() != null) {
				fi.getRequest().setAttribute(FILTER_APPLIED, Boolean.TRUE);
			}

			InterceptorStatusToken token = super.beforeInvocation(fi);

			try {
				try {
					fi.getChain().doFilter(fi.getRequest(), fi.getResponse());
				} catch (IOException | ServletException e) {
					logger.error("doFilter异常2"+e);
				}
			}
			finally {
				super.finallyInvocation(token);
			}

			super.afterInvocation(token, null);
		}
	}

	/**
	 * Indicates whether once-per-request handling will be observed. By default this is
	 * <code>true</code>, meaning the <code>FilterSecurityInterceptor</code> will only
	 * execute once-per-request. Sometimes users may wish it to execute more than once per
	 * request, such as when JSP forwards are being used and filter security is desired on
	 * each included fragment of the HTTP request.
	 *
	 * @return <code>true</code> (the default) if once-per-request is honoured, otherwise
	 * <code>false</code> if <code>FilterSecurityInterceptor</code> will enforce
	 * authorizations for each and every fragment of the HTTP request.
	 */
	public boolean isObserveOncePerRequest() {
		return observeOncePerRequest;
	}

	public void setObserveOncePerRequest(boolean observeOncePerRequest) {
		this.observeOncePerRequest = observeOncePerRequest;
	}
}
