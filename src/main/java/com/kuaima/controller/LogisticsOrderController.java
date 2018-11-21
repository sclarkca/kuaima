package com.kuaima.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.bstek.bdf3.dorado.jpa.CriteriaUtils;
import com.bstek.bdf3.dorado.jpa.JpaUtil;
import com.bstek.bdf3.dorado.jpa.policy.SaveContext;
import com.bstek.bdf3.dorado.jpa.policy.impl.SmartSavePolicyAdapter;
import com.bstek.bdf3.log.annotation.Log;
import com.bstek.bdf3.security.ContextUtils;
import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.annotation.Expose;
import com.bstek.dorado.data.entity.EntityState;
import com.bstek.dorado.data.entity.EntityUtils;
import com.bstek.dorado.data.provider.Criteria;
import com.bstek.dorado.data.provider.Page;
import com.bstek.dorado.data.provider.filter.FilterOperator;
import com.kuaima.entity.LogisticsOrder;
import com.kuaima.entity.ServiceOrder;
import com.kuaima.utils.common.MyCriteriaUtils;

@Controller
@Transactional
public class LogisticsOrderController {
	/**
	 * 增删改
	 */
	@DataResolver
	@Transactional
	@Log(module = "物流订单", category = "CRUD")
	public void save(List<LogisticsOrder> logisticsOrders) {
		JpaUtil.save(logisticsOrders, new SmartSavePolicyAdapter() {
			@Override
			public boolean beforeInsert(SaveContext context) {
				LogisticsOrder logisticsOrder = context.getEntity();
				if (EntityState.NEW.equals(EntityUtils.getState(logisticsOrder))) {
					logisticsOrder.setCreateDate(new Date());
					logisticsOrder.setCreater(ContextUtils.getLoginUsername());
				}
				return true;
			}

			@Override
			public boolean beforeUpdate(SaveContext context) {
				LogisticsOrder logisticsOrder = context.getEntity();
				if (EntityState.MODIFIED.equals(EntityUtils.getState(logisticsOrder))) {
					logisticsOrder.setUpdateDate(new Date());
					logisticsOrder.setUpdater(ContextUtils.getLoginUsername());
				}
				return true;
			}
		});
	}

	/**
	 * 查询
	 */
	@DataProvider
	public void query(Page<LogisticsOrder> page, Map<String, Object> parameter) {
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
	private void buildCondition(Page<LogisticsOrder> page, Map<String, Object> parameter, Criteria criteria) {
		com.bstek.dorado.data.provider.Order order = new com.bstek.dorado.data.provider.Order();
		order.setProperty("createDate");
		order.setDesc(true);
		criteria.addOrder(order);
		// 订单号
		if (parameter.containsKey("orderId")) {
			CriteriaUtils.add(criteria, "orderId", FilterOperator.like, parameter.get("orderId"));
			parameter.remove("orderId", parameter.get("orderId"));
		}
		// 物流单号
		if (parameter.containsKey("logisticsNo")) {
			CriteriaUtils.add(criteria, "logisticsNo", FilterOperator.like, parameter.get("logisticsNo"));
			parameter.remove("logisticsNo", parameter.get("logisticsNo"));
		}
		// 物流公司编码
		if (parameter.containsKey("logisticsCompanyCode")) {
			CriteriaUtils.add(criteria, "logisticsCompanyCode", FilterOperator.like,
					parameter.get("logisticsCompanyCode"));
			parameter.remove("logisticsCompanyCode", parameter.get("logisticsCompanyCode"));
		}
		// 联系人
		if (parameter.containsKey("receiver")) {
			CriteriaUtils.add(criteria, "receiver", FilterOperator.like, parameter.get("receiver"));
			parameter.remove("receiver", parameter.get("receiver"));
		}
		// 省
		if (parameter.containsKey("province")) {
			CriteriaUtils.add(criteria, "province", FilterOperator.like, parameter.get("province"));
			parameter.remove("province", parameter.get("province"));
		}
		// 市
		if (parameter.containsKey("city")) {
			CriteriaUtils.add(criteria, "city", FilterOperator.like, parameter.get("city"));
			parameter.remove("city", parameter.get("city"));
		}
		// 区/县
		if (parameter.containsKey("region")) {
			CriteriaUtils.add(criteria, "region", FilterOperator.like, parameter.get("region"));
			parameter.remove("region", parameter.get("region"));
		}
		// 镇
		if (parameter.containsKey("town")) {
			CriteriaUtils.add(criteria, "town", FilterOperator.like, parameter.get("town"));
			parameter.remove("town", parameter.get("town"));
		}
		// 详细地址
		if (parameter.containsKey("address")) {
			CriteriaUtils.add(criteria, "address", FilterOperator.like, parameter.get("address"));
			parameter.remove("address", parameter.get("address"));
		}
		MyCriteriaUtils.buildCriteriaMap(parameter);
		criteria = MyCriteriaUtils.add(criteria, parameter);
		JpaUtil.linq(LogisticsOrder.class).where(criteria).paging(page);
	}

	/**
	 * 创建物流订单信息
	 * @param parameter
	 */
	@Expose
	public void createLogisticsOrder(Map<String,Object> parameter){
		String str = JSON.toJSONString(parameter.get("parameter"));
		LogisticsOrder logisticsOrder = JSON.parseObject(str,LogisticsOrder.class);
		JpaUtil.persist(logisticsOrder);
		this.updateOrderInfo(logisticsOrder);
	}

	/**
	 * 更新订单物流信息
	 * @param logisticsOrder
	 */
	public void updateOrderInfo(LogisticsOrder logisticsOrder){
		// 创建物流订单的时候,更新订单表中的物流信息
		String orderId = logisticsOrder.getOrderId();
		String logisticsNo = logisticsOrder.getLogisticsNo();
		ServiceOrder serviceOrder = JpaUtil.linq(ServiceOrder.class).equal("id", orderId).findOne();
		serviceOrder.setLogisticsNo(logisticsNo);
		serviceOrder.setOrderStatus("20");
		JpaUtil.merge(serviceOrder);
	}
}