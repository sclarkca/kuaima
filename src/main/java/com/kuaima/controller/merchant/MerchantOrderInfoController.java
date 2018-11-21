package com.kuaima.controller.merchant;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.kuaima.entity.MerchantOrderInfo;
import com.kuaima.service.merchant.MerchantInfoService;
import com.kuaima.utils.common.MyCriteriaUtils;

/**
 * 商户和师傅 资金流水记录
 * @date 2018年10月10日
 */
@Controller
@Transactional(readOnly = true)
public class MerchantOrderInfoController {
	
	@Autowired
	MerchantInfoService merchantInfoService;
	/**
	 * 增删改
	 */
	@DataResolver
	@Transactional
	@Log(module = "商家订单信息", category = "CRUD")
	public void save(List<MerchantOrderInfo> merchantOrderInfos) {
		JpaUtil.save(merchantOrderInfos, new SmartSavePolicyAdapter() {
			@Override
			public boolean beforeInsert(SaveContext context) {
				MerchantOrderInfo merchantOrderInfo = context.getEntity();
				if (EntityState.NEW.equals(EntityUtils.getState(merchantOrderInfo))) {
					merchantOrderInfo.setCreateDate(new Date());
					merchantOrderInfo.setCreater(ContextUtils.getLoginUsername());
				}
				return true;
			}

			@Override
			public boolean beforeUpdate(SaveContext context) {
				MerchantOrderInfo merchantOrderInfo = context.getEntity();
				if (EntityState.MODIFIED.equals(EntityUtils.getState(merchantOrderInfo))) {
					merchantOrderInfo.setUpdateDate(new Date());
					merchantOrderInfo.setUpdater(ContextUtils.getLoginUsername());
				}
				return true;
			}
		});
	}

	/**
	 * 查询
	 */
	@DataProvider
	public void query(Page<MerchantOrderInfo> page, Map<String, Object> parameter) {
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
	private void buildCondition(Page<MerchantOrderInfo> page, Map<String, Object> parameter, Criteria criteria) {
		com.bstek.dorado.data.provider.Order order = new com.bstek.dorado.data.provider.Order();
		order.setProperty("createDate");
		order.setDesc(true);
		criteria.addOrder(order);
		// 查询当前用户绑定的商户编码
		String merchantCode = merchantInfoService.queryNowUserMerchantCode();
		if (StringUtils.isNotBlank(merchantCode)) {
			CriteriaUtils.add(criteria, "merchantCode", FilterOperator.eq, merchantCode);
			parameter.remove("merchantCode", parameter.get("merchantCode"));
		}
		// 服务商品金额
		if (parameter.containsKey("serviceCommdityAmount")) {
			CriteriaUtils.add(criteria, "serviceCommdityAmount", FilterOperator.like,
					parameter.get("serviceCommdityAmount"));
			parameter.remove("serviceCommdityAmount", parameter.get("serviceCommdityAmount"));
		}
		// 安装金额
		if (parameter.containsKey("installAmount")) {
			CriteriaUtils.add(criteria, "installAmount", FilterOperator.like, parameter.get("installAmount"));
			parameter.remove("installAmount", parameter.get("installAmount"));
		}
		// 配送金额
		if (parameter.containsKey("distributionAmount")) {
			CriteriaUtils.add(criteria, "distributionAmount", FilterOperator.like, parameter.get("distributionAmount"));
			parameter.remove("distributionAmount", parameter.get("distributionAmount"));
		}
		// 总金额
		if (parameter.containsKey("totalAmount")) {
			CriteriaUtils.add(criteria, "totalAmount", FilterOperator.like, parameter.get("totalAmount"));
			parameter.remove("totalAmount", parameter.get("totalAmount"));
		}
		// ToB订单编码
		if (parameter.containsKey("orderNo")) {
			CriteriaUtils.add(criteria, "orderNo", FilterOperator.like, parameter.get("orderNo"));
			parameter.remove("orderNo", parameter.get("orderNo"));
		}
		// 中心位置经度
		if (parameter.containsKey("centerLongitude")) {
			CriteriaUtils.add(criteria, "centerLongitude", FilterOperator.like, parameter.get("centerLongitude"));
			parameter.remove("centerLongitude", parameter.get("centerLongitude"));
		}
		// 中心位置维度
		if (parameter.containsKey("centerLatitude")) {
			CriteriaUtils.add(criteria, "centerLatitude", FilterOperator.like, parameter.get("centerLatitude"));
			parameter.remove("centerLatitude", parameter.get("centerLatitude"));
		}
		// 配送位置经度
		if (parameter.containsKey("distributionLongitude")) {
			CriteriaUtils.add(criteria, "distributionLongitude", FilterOperator.like,
					parameter.get("distributionLongitude"));
			parameter.remove("distributionLongitude", parameter.get("distributionLongitude"));
		}
		// 配送位置维度
		if (parameter.containsKey("distributionLatitude")) {
			CriteriaUtils.add(criteria, "distributionLatitude", FilterOperator.like,
					parameter.get("distributionLatitude"));
			parameter.remove("distributionLatitude", parameter.get("distributionLatitude"));
		}
		// 初始公里数
		if (parameter.containsKey("initKm")) {
			CriteriaUtils.add(criteria, "initKm", FilterOperator.like, parameter.get("initKm"));
			parameter.remove("initKm", parameter.get("initKm"));
		}
		// 初始公里费用
		if (parameter.containsKey("initKmPrice")) {
			CriteriaUtils.add(criteria, "initKmPrice", FilterOperator.like, parameter.get("initKmPrice"));
			parameter.remove("initKmPrice", parameter.get("initKmPrice"));
		}
		// 每公里单价
		if (parameter.containsKey("perKmPrice")) {
			CriteriaUtils.add(criteria, "perKmPrice", FilterOperator.like, parameter.get("perKmPrice"));
			parameter.remove("perKmPrice", parameter.get("perKmPrice"));
		}
		// 每公里费用
		if (parameter.containsKey("perKmAmount")) {
			CriteriaUtils.add(criteria, "perKmAmount", FilterOperator.like, parameter.get("perKmAmount"));
			parameter.remove("perKmAmount", parameter.get("perKmAmount"));
		}
		MyCriteriaUtils.buildCriteriaMap(parameter);
		criteria = MyCriteriaUtils.add(criteria, parameter);
		JpaUtil.linq(MerchantOrderInfo.class).where(criteria).paging(page);
	}
}