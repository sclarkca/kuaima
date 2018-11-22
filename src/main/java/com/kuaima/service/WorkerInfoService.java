package com.kuaima.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bstek.bdf3.dorado.jpa.JpaUtil;
import com.kuaima.dao.WorkerInfoDao;
import com.kuaima.entity.WorkerInfo;

@Service
public class WorkerInfoService {
	
	@Autowired
	WorkerInfoDao workerInfoDao;
	
	/**
	 * 查询师傅信息列表
	 * @return
	 */
	public List<WorkerInfo> queryWorkerInfoList(){
		List<WorkerInfo> list = workerInfoDao.queryWorkerInfoList();
		return list;
	}
	
	/**
	 * 根据师傅编码查询师傅信息
	 * @param workerId
	 * @return
	 */
	public WorkerInfo queryWorkerByCode(String workerId){
		return JpaUtil.linq(WorkerInfo.class).equal("workerId", workerId).findOne();
	}
}