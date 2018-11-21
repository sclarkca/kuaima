package com.kuaima.utils.common;

public class ImgException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4469545678849716725L;
	/**
	 * 异常信息描述
	 */
	private String msg;
	/**
	 * 异常编码
	 */
	private String errCode = "";

	public ImgException() {
		super();
	}

	public ImgException(String msg) {
		super(msg);
		this.msg = msg;
	}

	public ImgException(String msg, String errCode) {
		super(errCode + " : " + msg);
		this.msg = msg;
		this.errCode = errCode;
	}

	public ImgException(String msg, Throwable throwable) {
		super(msg, throwable);
		this.msg = msg;
	}

	public ImgException(Throwable throwable) {
		super(throwable);
	}

	public String getMsg() {
		return msg;
	}

	public String getErrCode() {
		return errCode;
	}

}
