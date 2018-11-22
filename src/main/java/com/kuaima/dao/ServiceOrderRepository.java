package com.kuaima.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.kuaima.entity.ServiceOrder;
import com.kuaima.entity.repo.ServiceOrderImp;
import com.kuaima.entity.repo.WorkerOrderCount;

@Repository
public class ServiceOrderRepository {

	@Autowired
	JdbcTemplate jdbcTemplate;

	/**
	 * 统计查询当天师傅订单数
	 * 
	 * @return
	 * @return
	 * 
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<WorkerOrderCount> queryWorkerDayOrder() {
		String sql = "select count(order_no) as num,o.worker_id from biz_service_order o where 1=1 and !isnull(o.worker_id) and to_days(o.create_date) = to_days(now()) group by o.worker_id";
		List<WorkerOrderCount> list;
		try {
			list = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(WorkerOrderCount.class));
		} catch (DataAccessException e) {
			return new ArrayList<>();
		}
		return list;
	}

	/**
	 * 查询未分配的订单
	 * 
	 * @return
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<ServiceOrder> queryServiceOrderWithoutWorker(String postStationCode) {
		String sqlIf = "";
		if (StringUtils.isNotBlank(postStationCode)){
			sqlIf = "and o.post_station_code = "+"'"+postStationCode+"'";
		}
		String sql = "select o.order_no,o.receiver,o.receiver_phone,o.region,c.`name` as regionName,o.address from biz_service_order o left join biz_city c on o.region = c.ad_code where  (isnull(o.worker_id) or o.worker_id = '') and o.order_status = '30' "+sqlIf;
		List<ServiceOrder> list;
		try {
			list = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(ServiceOrder.class));
		} catch (DataAccessException e) {
			return new ArrayList<>();
		}
		return list;
	}
	
	/**
	 * 查询已当天已分配订单信息列表(或者未完成)
	 * @return
	 */
	public List<ServiceOrderImp> queryNowDistibutionOrder(){
		String sql ="select o.order_no,o.worker_id,i.card_name from biz_service_order o left join biz_worker_info i on o.worker_id = i.worker_id where !isnull(o.worker_id) and ((to_days(o.create_date) = to_days(now()) or to_days(o.update_date)= to_days(now())) or o.order_status != '80')";
		List<ServiceOrderImp> list;
		try {
			list = jdbcTemplate.query(sql, new BeanPropertyRowMapper<ServiceOrderImp>(ServiceOrderImp.class));
		} catch (DataAccessException e) {
			return new ArrayList<>();
		}
		return list;
	}
}
