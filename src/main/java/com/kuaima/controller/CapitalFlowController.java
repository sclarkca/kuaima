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
import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.data.entity.EntityState;
import com.bstek.dorado.data.entity.EntityUtils;
import com.bstek.dorado.data.provider.Criteria;
import com.bstek.dorado.data.provider.Page;
import com.bstek.dorado.data.provider.filter.FilterOperator;
import com.kuaima.entity.CapitalFlow;
import com.kuaima.utils.common.MyCriteriaUtils;
import com.kuaima.utils.common.MyDateUtil;

@Controller
@Transactional(readOnly = true)
public class CapitalFlowController {
	/**
	 * 增删改
	 */
	@DataResolver
	@Transactional
	@Log(module = "资金流水", category = "CRUD")
	public void save(List<CapitalFlow> capitalFlows) {
		JpaUtil.save(capitalFlows, new SmartSavePolicyAdapter() {
			@Override
			public boolean beforeInsert(SaveContext context) {
				CapitalFlow capitalFlow = context.getEntity();
				if (EntityState.NEW.equals(EntityUtils.getState(capitalFlow))) {
				}
				return true;
			}

			@Override
			public boolean beforeUpdate(SaveContext context) {
				CapitalFlow capitalFlow = context.getEntity();
				if (EntityState.MODIFIED.equals(EntityUtils.getState(capitalFlow))) {
				}
				return true;
			}
		});
	}

	/**
	 * 查询
	 */
	@DataProvider
	public void query(Page<CapitalFlow> page, Map<String, Object> parameter) {
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
	private void buildCondition(Page<CapitalFlow> page, Map<String, Object> parameter, Criteria criteria) {
		com.bstek.dorado.data.provider.Order order = new com.bstek.dorado.data.provider.Order();
		order.setProperty("createTime");
		order.setDesc(true);
		criteria.addOrder(order);
		// 订单编码
		if (parameter.containsKey("orderNo")) {
			CriteriaUtils.add(criteria, "orderNo", FilterOperator.like, parameter.get("orderNo"));
			parameter.remove("orderNo", parameter.get("orderNo"));
		}
		// 类型
		if (parameter.containsKey("flowType")) {
			CriteriaUtils.add(criteria, "flowType", FilterOperator.eq, parameter.get("flowType"));
			parameter.remove("flowType", parameter.get("flowType"));
		}
		// 金额
		if (parameter.containsKey("amount")) {
			CriteriaUtils.add(criteria, "amount", FilterOperator.like, parameter.get("amount"));
			parameter.remove("amount", parameter.get("amount"));
		}
		// 师傅编码
		if (parameter.containsKey("workerId")) {
			CriteriaUtils.add(criteria, "workerId", FilterOperator.eq, parameter.get("workerId"));
			parameter.remove("workerId", parameter.get("workerId"));
		}
		// 状态
		if (parameter.containsKey("flowStatus")) {
			CriteriaUtils.add(criteria, "flowStatus", FilterOperator.eq, parameter.get("flowStatus"));
			parameter.remove("flowStatus", parameter.get("flowStatus"));
		}
		// 时间
		Date createTimeStart = (Date) parameter.get("createTimeStart");
		if (parameter.containsKey("createTimeStart") && null != createTimeStart) {
			CriteriaUtils.add(criteria, "createTime", FilterOperator.ge, MyDateUtil.getCurrStart(createTimeStart));
			parameter.remove("createTimeStart", parameter.get("createTimeStart"));
		}
		Date createTimeEnd = (Date) parameter.get("createTimeEnd");
		if (parameter.containsKey("createTimeEnd") && null != createTimeEnd) {
			CriteriaUtils.add(criteria, "createTime", FilterOperator.le, MyDateUtil.getCurrEnd(createTimeEnd));
			parameter.remove("createTimeEnd", parameter.get("createTimeEnd"));
		}
		MyCriteriaUtils.buildCriteriaMap(parameter);
		criteria = MyCriteriaUtils.add(criteria, parameter);
		JpaUtil.linq(CapitalFlow.class).where(criteria).paging(page);
	}

}