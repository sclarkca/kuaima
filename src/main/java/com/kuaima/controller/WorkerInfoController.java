package com.kuaima.controller;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.kuaima.entity.WorkerInfo;
import com.kuaima.service.CityService;
import com.kuaima.service.WorkerInfoService;
import com.kuaima.utils.common.MyCriteriaUtils;

@Controller
@Transactional(readOnly = true)
public class WorkerInfoController {
	
	@Autowired
	WorkerInfoService workerInfoService;
	@Autowired
	CityService cityService;
	/**
	 * 增删改
	 */
	@DataResolver
	@Transactional
	@Log(module = "师傅信息", category = "CRUD")
	public void save(List<WorkerInfo> workerInfos) {
		JpaUtil.save(workerInfos, new SmartSavePolicyAdapter() {
			@Override
			public boolean beforeInsert(SaveContext context) {
				WorkerInfo workerInfo = context.getEntity();
				if (EntityState.NEW.equals(EntityUtils.getState(workerInfo))) {
					workerInfo.setCreateTime(new Date());
				}
				return true;
			}

			@Override
			public boolean beforeUpdate(SaveContext context) {
				WorkerInfo workerInfo = context.getEntity();
				if (EntityState.MODIFIED.equals(EntityUtils.getState(workerInfo))) {
					workerInfo.setUpdateTime(new Date());
				}
				return true;
			}
		});
	}

	/**
	 * 查询
	 */
	@DataProvider
	public void query(Page<WorkerInfo> page, Map<String, Object> parameter) {
		if (parameter == null) {
			parameter = new HashMap<>();
		}
		MyCriteriaUtils.clearEmptyValue(parameter);
		Criteria criteria = new Criteria();
		buildCondition(page, parameter, criteria);
		Collection<WorkerInfo> entities = page.getEntities();
		for (WorkerInfo workerInfo : entities) {
			String cityId = workerInfo.getCityId();
			String cityName = cityService.queryCityNameByAcCode(cityId);
			workerInfo.setCityName(cityName);
		}
	}

	/**
	 * 构建查询条件
	 *
	 * @param page
	 * @param parameter
	 * @param criteria
	 */
	private void buildCondition(Page<WorkerInfo> page, Map<String, Object> parameter, Criteria criteria) {
		com.bstek.dorado.data.provider.Order order = new com.bstek.dorado.data.provider.Order();
		order.setProperty("createTime");
		order.setDesc(true);
		criteria.addOrder(order);
		// 师傅编码
		if (parameter.containsKey("workerId")) {
			CriteriaUtils.add(criteria, "workerId", FilterOperator.like, parameter.get("workerId"));
			parameter.remove("workerId", parameter.get("workerId"));
		}
		// 所在城市
		if (parameter.containsKey("cityId")) {
			CriteriaUtils.add(criteria, "cityId", FilterOperator.like, parameter.get("cityId"));
			parameter.remove("cityId", parameter.get("cityId"));
		}
		// 真实姓名
		if (parameter.containsKey("cardName")) {
			CriteriaUtils.add(criteria, "cardName", FilterOperator.like, parameter.get("cardName"));
			parameter.remove("cardName", parameter.get("cardName"));
		}
		// 身份证号
		if (parameter.containsKey("cardId")) {
			CriteriaUtils.add(criteria, "cardId", FilterOperator.like, parameter.get("cardId"));
			parameter.remove("cardId", parameter.get("cardId"));
		}
		// 身份证正面
		if (parameter.containsKey("cardFrontUrl")) {
			CriteriaUtils.add(criteria, "cardFrontUrl", FilterOperator.like, parameter.get("cardFrontUrl"));
			parameter.remove("cardFrontUrl", parameter.get("cardFrontUrl"));
		}
		// 身份证反面
		if (parameter.containsKey("cardBackUrl")) {
			CriteriaUtils.add(criteria, "cardBackUrl", FilterOperator.like, parameter.get("cardBackUrl"));
			parameter.remove("cardBackUrl", parameter.get("cardBackUrl"));
		}
		// 紧急联系人
		if (parameter.containsKey("emergencyContactPerson")) {
			CriteriaUtils.add(criteria, "emergencyContactPerson", FilterOperator.like,
					parameter.get("emergencyContactPerson"));
			parameter.remove("emergencyContactPerson", parameter.get("emergencyContactPerson"));
		}
		// 紧急联系方式
		if (parameter.containsKey("emergencyContact")) {
			CriteriaUtils.add(criteria, "emergencyContact", FilterOperator.like, parameter.get("emergencyContact"));
			parameter.remove("emergencyContact", parameter.get("emergencyContact"));
		}
		// 汽车类型
		if (parameter.containsKey("carType")) {
			CriteriaUtils.add(criteria, "carType", FilterOperator.like, parameter.get("carType"));
			parameter.remove("carType", parameter.get("carType"));
		}
		// 照片
		if (parameter.containsKey("carImageUrl")) {
			CriteriaUtils.add(criteria, "carImageUrl", FilterOperator.like, parameter.get("carImageUrl"));
			parameter.remove("carImageUrl", parameter.get("carImageUrl"));
		}
		// 车牌号
		if (parameter.containsKey("licensePlate")) {
			CriteriaUtils.add(criteria, "licensePlate", FilterOperator.like, parameter.get("licensePlate"));
			parameter.remove("licensePlate", parameter.get("licensePlate"));
		}
		// 驾驶证照片
		if (parameter.containsKey("driversLicenseUrl")) {
			CriteriaUtils.add(criteria, "driversLicenseUrl", FilterOperator.like, parameter.get("driversLicenseUrl"));
			parameter.remove("driversLicenseUrl", parameter.get("driversLicenseUrl"));
		}
		// 行驶证照片
		if (parameter.containsKey("drivingLicenseUrl")) {
			CriteriaUtils.add(criteria, "drivingLicenseUrl", FilterOperator.like, parameter.get("drivingLicenseUrl"));
			parameter.remove("drivingLicenseUrl", parameter.get("drivingLicenseUrl"));
		}
		// 审核状态
		if (parameter.containsKey("status")) {
			CriteriaUtils.add(criteria, "status", FilterOperator.like, parameter.get("status"));
			parameter.remove("status", parameter.get("status"));
		}
		// 是否缴纳保证金
		if (parameter.containsKey("payDeposit")) {
			CriteriaUtils.add(criteria, "payDeposit", FilterOperator.like, parameter.get("payDeposit"));
			parameter.remove("payDeposit", parameter.get("payDeposit"));
		}
		// 保证金金额
		if (parameter.containsKey("deposit")) {
			CriteriaUtils.add(criteria, "deposit", FilterOperator.like, parameter.get("deposit"));
			parameter.remove("deposit", parameter.get("deposit"));
		}
		// 保证金编码
		if (parameter.containsKey("depositCode")) {
			CriteriaUtils.add(criteria, "depositCode", FilterOperator.like, parameter.get("depositCode"));
			parameter.remove("depositCode", parameter.get("depositCode"));
		}
		// 保证金名称
		if (parameter.containsKey("depositName")) {
			CriteriaUtils.add(criteria, "depositName", FilterOperator.like, parameter.get("depositName"));
			parameter.remove("depositName", parameter.get("depositName"));
		}
		// 手机号
		if (parameter.containsKey("workerPhone")) {
			CriteriaUtils.add(criteria, "workerPhone", FilterOperator.like, parameter.get("workerPhone"));
			parameter.remove("workerPhone", parameter.get("workerPhone"));
		}
		// 会员等级
		if (parameter.containsKey("memberRank")) {
			CriteriaUtils.add(criteria, "memberRank", FilterOperator.like, parameter.get("memberRank"));
			parameter.remove("memberRank", parameter.get("memberRank"));
		}
		// 评分
		if (parameter.containsKey("score")) {
			CriteriaUtils.add(criteria, "score", FilterOperator.like, parameter.get("score"));
			parameter.remove("score", parameter.get("score"));
		}
		// 是否接单
		if (parameter.containsKey("isWorking")) {
			CriteriaUtils.add(criteria, "isWorking", FilterOperator.eq, parameter.get("isWorking"));
			parameter.remove("isWorking", parameter.get("isWorking"));
		}
		MyCriteriaUtils.buildCriteriaMap(parameter);
		criteria = MyCriteriaUtils.add(criteria, parameter);
		JpaUtil.linq(WorkerInfo.class).where(criteria).paging(page);
	}

	/**
	 * 查询师傅信息列表
	 * @return
	 */
	@DataProvider
	public void queryWorkerInfoList(Page<WorkerInfo> page, Map<String, Object> parameter){
		List<WorkerInfo> list = workerInfoService.queryWorkerInfoList();
		page.setEntities(list);
	}
}