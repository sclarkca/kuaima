package com.bstek.bdf3.security.ui.interceptor;

import org.aopalliance.intercept.MethodInvocation;

import com.bstek.dorado.common.proxy.PatternMethodInterceptor;

public class AjaxServiceInterceptor extends PatternMethodInterceptor {
	@Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
       
    	return methodInvocation.proceed();
    }
}