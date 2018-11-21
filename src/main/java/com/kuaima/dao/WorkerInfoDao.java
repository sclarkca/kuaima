package com.kuaima.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.kuaima.entity.WorkerInfo;

@Repository
public class WorkerInfoDao {

	@Autowired
	JdbcTemplate jdbcTemplate;

	/**
	 * 查询可接单的师傅列表
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<WorkerInfo> queryWorkerInfoList() {
		String sql = "select i.worker_id,i.worker_phone,i.card_name,i.is_working from biz_worker_info i where i.is_working = '1';";
		List<WorkerInfo> list;
		try {
			list = jdbcTemplate.query(sql, new BeanPropertyRowMapper<WorkerInfo>(WorkerInfo.class));
		} catch (DataAccessException e) {
			return new ArrayList<>();
		}
		return list;
	}
}
