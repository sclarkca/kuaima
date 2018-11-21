package com.kuaima.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import com.bstek.bdf3.dorado.jpa.CriteriaUtils;
import com.bstek.bdf3.dorado.jpa.JpaUtil;
import com.bstek.bdf3.dorado.jpa.policy.SaveContext;
import com.bstek.bdf3.dorado.jpa.policy.impl.SmartSavePolicyAdapter;
import com.bstek.bdf3.log.annotation.Log;
import com.bstek.bdf3.security.ContextUtils;
import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.data.entity.EntityState;
import com.bstek.dorado.data.entity.EntityUtils;
import com.bstek.dorado.data.provider.Criteria;
import com.bstek.dorado.data.provider.Page;
import com.bstek.dorado.data.provider.filter.FilterOperator;
import com.kuaima.entity.NoticeNews;
import com.kuaima.utils.common.MyCriteriaUtils;
import com.kuaima.utils.common.MyDateUtil;

@Controller
@Transactional(readOnly = true)
public class NoticeNewsController {
	/**
	 * 增删改
	 */
	@DataResolver
	@Transactional
	@Log(module = "公告/私信", category = "CRUD")
	public void save(List<NoticeNews> notices) {
		JpaUtil.save(notices, new SmartSavePolicyAdapter() {
			@Override
			public boolean beforeInsert(SaveContext context) {
				NoticeNews notice = context.getEntity();
				if (EntityState.NEW.equals(EntityUtils.getState(notice))) {
					Date date = new Date();
					long time = date.getTime();
					notice.setCreateDate(date);
					notice.setCreater(ContextUtils.getLoginUsername());
					Long workerId = notice.getWorkerId();
					if (null == workerId){
						notice.setWorkerId(0L);
					}
					notice.setTimestamp(time);
				}
				return true;
			}

			@Override
			public boolean beforeUpdate(SaveContext context) {
				NoticeNews notice = context.getEntity();
				if (EntityState.MODIFIED.equals(EntityUtils.getState(notice))) {
					Date date = new Date();
					long time = date.getTime();
					notice.setUpdateDate(date);
					notice.setUpdater(ContextUtils.getLoginUsername());
					Long workerId = notice.getWorkerId();
					if (null == workerId){
						notice.setWorkerId(0L);
					}
					notice.setTimestamp(time);
				}
				return true;
			}
		});
	}

	/**
	 * 查询
	 */
	@DataProvider
	public void query(Page<NoticeNews> page, Map<String, Object> parameter) {
		if (parameter == null) {
			parameter = new HashMap<>();
		}
		MyCriteriaUtils.clearEmptyValue(parameter);
		Criteria criteria = new Criteria();
		buildCondition(page, parameter, criteria);
	}

	/**
	 * 构建查询条件
	 *
	 * @param page
	 * @param parameter
	 * @param criteria
	 */
	private void buildCondition(Page<NoticeNews> page, Map<String, Object> parameter, Criteria criteria) {
		com.bstek.dorado.data.provider.Order order = new com.bstek.dorado.data.provider.Order();
		order.setProperty("timestamp");
		order.setDesc(true);
		criteria.addOrder(order);
		// 标题
		if (parameter.containsKey("title")) {
			CriteriaUtils.add(criteria, "title", FilterOperator.like, parameter.get("title"));
			parameter.remove("title", parameter.get("title"));
		}
		// 内容
		if (parameter.containsKey("content")) {
			CriteriaUtils.add(criteria, "content", FilterOperator.like, parameter.get("content"));
			parameter.remove("content", parameter.get("content"));
		}
		// 时间
		Date timeStartDate = (Date) parameter.get("timeStart");
		if (parameter.containsKey("timeStart") && null != timeStartDate) {
			CriteriaUtils.add(criteria, "time", FilterOperator.ge, MyDateUtil.getCurrStart(timeStartDate));
			parameter.remove("timeStart", parameter.get("timeStart"));
		}
		Date timeEndDate = (Date) parameter.get("timeEnd");
		if (parameter.containsKey("timeEnd") && null != timeEndDate) {
			CriteriaUtils.add(criteria, "time", FilterOperator.le, MyDateUtil.getCurrEnd(timeEndDate));
			parameter.remove("timeEnd", parameter.get("timeEnd"));
		}
		// 类型
		if (parameter.containsKey("type")) {
			CriteriaUtils.add(criteria, "type", FilterOperator.eq, parameter.get("type"));
			parameter.remove("type", parameter.get("type"));
		}
		// 用户编码
		if (parameter.containsKey("workerId")) {
			CriteriaUtils.add(criteria, "workerId", FilterOperator.like, parameter.get("workerId"));
			parameter.remove("workerId", parameter.get("workerId"));
		}
		// 状态
		if (parameter.containsKey("status")) {
			CriteriaUtils.add(criteria, "status", FilterOperator.eq, parameter.get("status"));
			parameter.remove("status", parameter.get("status"));
		}
		// 时间戳
		if (parameter.containsKey("timestamp")) {
			CriteriaUtils.add(criteria, "timestamp", FilterOperator.eq, parameter.get("timestamp"));
			parameter.remove("timestamp", parameter.get("timestamp"));
		}
		// 范围
		if (parameter.containsKey("scope")) {
			CriteriaUtils.add(criteria, "scope", FilterOperator.like, parameter.get("scope"));
			parameter.remove("scope", parameter.get("scope"));
		}
		MyCriteriaUtils.buildCriteriaMap(parameter);
		criteria = MyCriteriaUtils.add(criteria, parameter);
		JpaUtil.linq(NoticeNews.class).where(criteria).paging(page);
	}
	
}