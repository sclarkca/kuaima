/**
 * 
 */
package com.kuaima.entity;

import java.io.Serializable;

public class CmmdtyManageImport implements Serializable {

	private static final long serialVersionUID = -5714355174604508742L;

	/**
	 * 商品编码
	 */
	private String cmmdtyCode;

	/**
	 * 记录格式校验错误信息
	 */
	private String errorMsg;
	/**
	 * 判读格式是否错误
	 */
	private Boolean yesOrNo;

	public String getCmmdtyCode() {
		return cmmdtyCode;
	}

	public void setCmmdtyCode(String cmmdtyCode) {
		this.cmmdtyCode = cmmdtyCode;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public Boolean getYesOrNo() {
		return yesOrNo;
	}

	public void setYesOrNo(Boolean yesOrNo) {
		this.yesOrNo = yesOrNo;
	}

}
