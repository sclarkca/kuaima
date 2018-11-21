package com.kuaima.utils.excel;

public class ExcelImportError {
	// 出错行号
	private int rowIdx;

	// 出错原因
	private String reason;

	public int getRowIdx() {
		return rowIdx;
	}

	public void setRowIdx(int rowIdx) {
		this.rowIdx = rowIdx;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

}
