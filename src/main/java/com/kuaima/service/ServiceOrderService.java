package com.kuaima.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kuaima.dao.ServiceOrderRepository;
import com.kuaima.entity.ServiceOrder;
import com.kuaima.entity.repo.ServiceOrderImp;
import com.kuaima.entity.repo.WorkerOrderCount;

@Service
public class ServiceOrderService {
	
	@Autowired
	ServiceOrderRepository serviceOrderRepository;
	
	/**
	 * 查询当天分配订单的师傅列表和订单数信息
	 * @return
	 */
	public List<WorkerOrderCount> queryWorkerNowOrder(){
		List<WorkerOrderCount> list = serviceOrderRepository.queryWorkerDayOrder();
		return list;
	}
	
	/**
	 * 查询未分配的订单列表
	 * @return
	 */
	public List<ServiceOrder> queryServiceOrderListWithoutWorker(String postStationCode){
		List<ServiceOrder> list = serviceOrderRepository.queryServiceOrderWithoutWorker(postStationCode);
		return list;
	}
	
	/**
	 * 查询当天已分配定单列表
	 * @return
	 */
	public List<ServiceOrderImp> queryNowDistibutionOrder(){
		List<ServiceOrderImp> list = serviceOrderRepository.queryNowDistibutionOrder();
		return list;
	}
	
	public void updateOrderInfo(String orderNo,String workerId){
		
	}
}