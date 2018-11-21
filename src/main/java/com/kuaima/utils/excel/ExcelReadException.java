package com.kuaima.utils.excel;

public class ExcelReadException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7214730257070029052L;

	final String err;

	/* 1 必填项为空 */
	private static final String REQUIRED_ERR = "fail.batch.excel.required_err";

	/* 2 存在重复项 */
	private static final String DUPLICATE = "fail.batch.excel.duplicate";

	/* 报错行号 */
	final int rowErr;

	public ExcelReadException(int type, int rowErr) {
		switch (type) {
		case 1:
			this.err = REQUIRED_ERR;
			this.rowErr = rowErr + 1;
			break;
		case 2:
			this.err = DUPLICATE;
			this.rowErr = rowErr + 1;
			break;
		default :
			this.err = DUPLICATE;
			this.rowErr = rowErr + 1;
		}
	}

	public String getErr() {
		return err;
	}

	public int getRowErr() {
		return rowErr;
	}
}
