/**
 * 
 */
package com.kuaima.controller.common;

import java.io.Serializable;

/**
 * @Title:  商品异常信息
 * 
 */
public class CmmodityErrorMsg implements Serializable{

	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3720226741116012379L;

	private String cmmodityCode;
	
	private String errorMsg;

	public String getCmmodityCode() {
		return cmmodityCode;
	}
	public void setCmmodityCode(String cmmodityCode) {
		this.cmmodityCode = cmmodityCode;
	}
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	
	@Override
	public String toString() {
		return "CmmodityErrorMsg [cmmodityCode=" + cmmodityCode + ", errorMsg=" + errorMsg + "]";
	}
}
