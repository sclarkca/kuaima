package com.kuaima.controller.merchant;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
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
import com.bstek.dorado.annotation.Expose;
import com.bstek.dorado.data.entity.EntityState;
import com.bstek.dorado.data.entity.EntityUtils;
import com.bstek.dorado.data.provider.Criteria;
import com.bstek.dorado.data.provider.Page;
import com.bstek.dorado.data.provider.filter.FilterOperator;
import com.kuaima.entity.merchant.MerchantInfo;
import com.kuaima.service.EncodingTypeService;
import com.kuaima.utils.common.MyCriteriaUtils;

@Controller
@Transactional(readOnly = true)
public class MerchantInfoController {

	@Autowired
	EncodingTypeService encodingTypeService;
	// 品类
	private static final String ENCODING_TYPE_CODE = "MERCHANT_CODE";
	// 品类编码前缀
	private static final String PREFIX = "MC";

	/**
	 * 增删改
	 */
	@DataResolver
	@Transactional
	@Log(module = "商户信息管理", category = "CRUD")
	public void save(List<MerchantInfo> merchantInfos) {
		JpaUtil.save(merchantInfos, new SmartSavePolicyAdapter() {
			@Override
			public boolean beforeInsert(SaveContext context) {
				MerchantInfo merchantInfo = context.getEntity();
				if (EntityState.NEW.equals(EntityUtils.getState(merchantInfo))) {
					merchantInfo.setCreateDate(new Date());
					merchantInfo.setCreater(ContextUtils.getLoginUsername());
				}
				String merchantCode = merchantInfo.getMerchantCode();
				this.crudCode(merchantCode);
				return true;
			}

			@Override
			public boolean beforeUpdate(SaveContext context) {
				MerchantInfo merchantInfo = context.getEntity();
				if (EntityState.MODIFIED.equals(EntityUtils.getState(merchantInfo))) {
					merchantInfo.setUpdateDate(new Date());
					merchantInfo.setUpdater(ContextUtils.getLoginUsername());
				}
				String merchantCode = merchantInfo.getMerchantCode();
				this.crudCode(merchantCode);
				return true;
			}
			// 更新或创建编码
			public void crudCode(String lastCode) {
				String lastCodeByType = encodingTypeService.queryLastCodeByType(ENCODING_TYPE_CODE);
				// 如果为空
				if (StringUtils.isBlank(lastCodeByType)) {
					// 创建
					encodingTypeService.createLastCodeByType(ENCODING_TYPE_CODE, lastCode);
				} else {
					// 修改
					encodingTypeService.updateLastCodeByType(ENCODING_TYPE_CODE, lastCode);
				}
			}
		});
	}

	/**
	 * 查询
	 */
	@DataProvider
	public void query(Page<MerchantInfo> page, Map<String, Object> parameter) {
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
	private void buildCondition(Page<MerchantInfo> page, Map<String, Object> parameter, Criteria criteria) {
		com.bstek.dorado.data.provider.Order order = new com.bstek.dorado.data.provider.Order();
		order.setProperty("updateDate");
		order.setDesc(true);
		criteria.addOrder(order);
		// 商户编码
		if (parameter.containsKey("merchantCode")) {
			CriteriaUtils.add(criteria, "merchantCode", FilterOperator.like, parameter.get("merchantCode"));
			parameter.remove("merchantCode", parameter.get("merchantCode"));
		}
		// 商户名称
		if (parameter.containsKey("merchantName")) {
			CriteriaUtils.add(criteria, "merchantName", FilterOperator.like, parameter.get("merchantName"));
			parameter.remove("merchantName", parameter.get("merchantName"));
		}
		MyCriteriaUtils.buildCriteriaMap(parameter);
		criteria = MyCriteriaUtils.add(criteria, parameter);
		JpaUtil.linq(MerchantInfo.class).where(criteria).paging(page);
	}

	/**
	 * 创建商户编码
	 * 
	 * @return
	 */
	@Expose
	public String createMerchantCode() {
		return encodingTypeService.createCode(ENCODING_TYPE_CODE, PREFIX);
	}
}