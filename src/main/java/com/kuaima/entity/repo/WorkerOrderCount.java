package com.kuaima.entity.repo;

import java.io.Serializable;

public class WorkerOrderCount implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3255271342543934194L;

	private String workerId;
	private String num;

	public String getWorkerId() {
		return workerId;
	}

	public void setWorkerId(String workerId) {
		this.workerId = workerId;
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

}
