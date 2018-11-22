package com.kuaima.controller.poststation;

import java.util.Collection;
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
import com.bstek.dorado.annotation.Expose;
import com.bstek.dorado.data.entity.EntityState;
import com.bstek.dorado.data.entity.EntityUtils;
import com.bstek.dorado.data.provider.Criteria;
import com.bstek.dorado.data.provider.Page;
import com.bstek.dorado.data.provider.filter.FilterOperator;
import com.bstek.dorado.view.resolver.ClientRunnableException;
import com.kuaima.entity.Bu;
import com.kuaima.entity.Commodity;
import com.kuaima.entity.PostStation;
import com.kuaima.entity.ServiceOrder;
import com.kuaima.entity.WorkerInfo;
import com.kuaima.entity.poststation.PostStationGrantedAuthority;
import com.kuaima.service.BuService;
import com.kuaima.service.CityService;
import com.kuaima.service.CommodityService;
import com.kuaima.service.EncodingTypeService;
import com.kuaima.service.PostStationService;
import com.kuaima.service.ServiceOrderService;
import com.kuaima.service.WorkerInfoService;
import com.kuaima.utils.common.MyCriteriaUtils;
import com.kuaima.utils.common.MyDateUtil;

@Controller
@Transactional
public class ServiceOrderPostStationController {
	@Autowired
	CityService cityService;

	@Autowired
	ServiceOrderService serviceOrderService;
	@Autowired
	EncodingTypeService encodingTypeService;
	@Autowired
	CommodityService commodityService;
	@Autowired
	PostStationService postStationService;
	@Autowired
	BuService buService;
	@Autowired
	WorkerInfoService workerInfoService;
	/**
	 * 增删改
	 */
	@DataResolver
	@Transactional
	@Log(module = "订单信息", category = "CRUD")
	public void save(List<ServiceOrder> serviceOrders) {
		JpaUtil.save(serviceOrders, new SmartSavePolicyAdapter() {
			@Override
			public boolean beforeInsert(SaveContext context) {
				ServiceOrder serviceOrder = context.getEntity();
				if (EntityState.NEW.equals(EntityUtils.getState(serviceOrder))) {
					serviceOrder.setCreateDate(new Date());
					serviceOrder.setCreater(ContextUtils.getLoginUsername());
				}
				return true;
			}

			@Override
			public boolean beforeUpdate(SaveContext context) {
				ServiceOrder serviceOrder = context.getEntity();
				if (EntityState.MODIFIED.equals(EntityUtils.getState(serviceOrder))) {
					serviceOrder.setUpdateDate(new Date());
					serviceOrder.setUpdater(ContextUtils.getLoginUsername());
				}
				return true;
			}


		});
	}

	/**
	 * 查询
	 */
	@DataProvider
	public void query(Page<ServiceOrder> page, Map<String, Object> parameter) {
		if (parameter == null) {
			parameter = new HashMap<>();
		}
		MyCriteriaUtils.clearEmptyValue(parameter);
		Criteria criteria = new Criteria();
		buildCondition(page, parameter, criteria);
		Collection<ServiceOrder> entities = page.getEntities();
		for (ServiceOrder serviceOrder : entities) {
			String province = serviceOrder.getProvince();
			// 省
			String provinceName = cityService.queryCityNameByAcCode(province);
			String city = serviceOrder.getCity();
			// 市
			String cityName = cityService.queryCityNameByAcCode(city);
			String region = serviceOrder.getRegion();
			// 区镇
			String regionName = cityService.queryCityNameByAcCode(region);
			String sku = serviceOrder.getSku();
			// 商品信息
			Commodity commodity = commodityService.queryCmmdityByCode(sku);
			if (null != commodity){
				// 商品名称
				serviceOrder.setCommodityName(commodity.getCommodityName());
				String buCode = commodity.getBuCode();
				// 品类信息
				Bu bu = buService.queryBuName(buCode);
				if (null != bu){
					// 品类名称
					serviceOrder.setBuName(bu.getBuName());
				}
			}
			String workerId = serviceOrder.getWorkerId();
			// 师傅姓名
			if (StringUtils.isNotBlank(workerId)){
				WorkerInfo workerInfo = workerInfoService.queryWorkerByCode(workerId);
				if (null != workerInfo){
					serviceOrder.setWorker(workerInfo.getCardName());
				}
			}
			String postStationCode = serviceOrder.getPostStationCode();
			String postStationName = postStationService.queryPostNameByCode(postStationCode);
			serviceOrder.setPostStationName(postStationName);
			serviceOrder.setProvinceName(provinceName);
			serviceOrder.setCityName(cityName);
			serviceOrder.setRegionName(regionName);
		}
	}

	/**
	 * 构建查询条件
	 *
	 * @param page
	 * @param parameter
	 * @param criteria
	 */
	private void buildCondition(Page<ServiceOrder> page, Map<String, Object> parameter, Criteria criteria) {
		com.bstek.dorado.data.provider.Order order = new com.bstek.dorado.data.provider.Order();
		order.setProperty("createDate");
		order.setDesc(true);
		criteria.addOrder(order);
		parameter.remove("provinceName", parameter.get("provinceName"));
		parameter.remove("cityName", parameter.get("cityName"));
		parameter.remove("regionName", parameter.get("regionName"));

		// 默认传入当前用户绑定的驿站编码进行查询
		String postStationCode = queryNowUserLogisticsCode();
		if (StringUtils.isNotBlank(postStationCode)) {
			CriteriaUtils.add(criteria, "postStationCode", FilterOperator.eq, postStationCode);
			parameter.remove("postStationCode", parameter.get("postStationCode"));
		}
		// 定单编码
		if (parameter.containsKey("orderNo")) {
			CriteriaUtils.add(criteria, "orderNo", FilterOperator.like, parameter.get("orderNo"));
			parameter.remove("orderNo", parameter.get("orderNo"));
		}
		// 订单状态
		if (parameter.containsKey("orderStatus")) {
			CriteriaUtils.add(criteria, "orderStatus", FilterOperator.like, parameter.get("orderStatus"));
			parameter.remove("orderStatus", parameter.get("orderStatus"));
		}
		// 安装师傅编码
		if (parameter.containsKey("workerId")) {
			CriteriaUtils.add(criteria, "workerId", FilterOperator.like, parameter.get("workerId"));
			parameter.remove("workerId", parameter.get("workerId"));
		}
		// 收货人
		if (parameter.containsKey("receiver")) {
			CriteriaUtils.add(criteria, "receiver", FilterOperator.like, parameter.get("receiver"));
			parameter.remove("receiver", parameter.get("receiver"));
		}
		// 收货手机
		if (parameter.containsKey("receiverPhone")) {
			CriteriaUtils.add(criteria, "receiverPhone", FilterOperator.like, parameter.get("receiverPhone"));
			parameter.remove("receiverPhone", parameter.get("receiverPhone"));
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
		// 区县
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
		// 预约安装时间
		Date appointmentTimeStartDate = (Date) parameter.get("appointmentTimeStart");
		if (parameter.containsKey("appointmentTimeStart") && null != appointmentTimeStartDate) {
			CriteriaUtils.add(criteria, "appointmentTime", FilterOperator.ge,
					MyDateUtil.getCurrStart(appointmentTimeStartDate));
			parameter.remove("appointmentTimeStart", parameter.get("appointmentTimeStart"));
		}
		Date appointmentTimeEndDate = (Date) parameter.get("appointmentTimeEnd");
		if (parameter.containsKey("appointmentTimeEnd") && null != appointmentTimeEndDate) {
			CriteriaUtils.add(criteria, "appointmentTime", FilterOperator.le,
					MyDateUtil.getCurrEnd(appointmentTimeEndDate));
			parameter.remove("appointmentTimeEnd", parameter.get("appointmentTimeEnd"));
		}
		// 安装完成时间
		Date finishTimeStartDate = (Date) parameter.get("finishTimeStart");
		if (parameter.containsKey("finishTimeStart") && null != finishTimeStartDate) {
			CriteriaUtils.add(criteria, "finishTime", FilterOperator.ge, MyDateUtil.getCurrStart(finishTimeStartDate));
			parameter.remove("finishTimeStart", parameter.get("finishTimeStart"));
		}
		Date finishTimeEndDate = (Date) parameter.get("finishTimeEnd");
		if (parameter.containsKey("finishTimeEnd") && null != finishTimeEndDate) {
			CriteriaUtils.add(criteria, "finishTime", FilterOperator.le, MyDateUtil.getCurrEnd(finishTimeEndDate));
			parameter.remove("finishTimeEnd", parameter.get("finishTimeEnd"));
		}
		// 系统分配时间
		Date allocateTimeStartDate = (Date) parameter.get("allocateTimeStart");
		if (parameter.containsKey("allocateTimeStart") && null != allocateTimeStartDate) {
			CriteriaUtils.add(criteria, "allocateTime", FilterOperator.ge,
					MyDateUtil.getCurrStart(allocateTimeStartDate));
			parameter.remove("allocateTimeStart", parameter.get("allocateTimeStart"));
		}
		Date allocateTimeEndDate = (Date) parameter.get("allocateTimeEnd");
		if (parameter.containsKey("allocateTimeEnd") && null != allocateTimeEndDate) {
			CriteriaUtils.add(criteria, "allocateTime", FilterOperator.le, MyDateUtil.getCurrEnd(allocateTimeEndDate));
			parameter.remove("allocateTimeEnd", parameter.get("allocateTimeEnd"));
		}
		// 师傅取件时间
		Date takeTimeStartDate = (Date) parameter.get("takeTimeStart");
		if (parameter.containsKey("takeTimeStart") && null != takeTimeStartDate) {
			CriteriaUtils.add(criteria, "takeTime", FilterOperator.ge, MyDateUtil.getCurrStart(takeTimeStartDate));
			parameter.remove("takeTimeStart", parameter.get("takeTimeStart"));
		}
		Date takeTimeEndDate = (Date) parameter.get("takeTimeEnd");
		if (parameter.containsKey("takeTimeEnd") && null != takeTimeEndDate) {
			CriteriaUtils.add(criteria, "takeTime", FilterOperator.le, MyDateUtil.getCurrEnd(takeTimeEndDate));
			parameter.remove("takeTimeEnd", parameter.get("takeTimeEnd"));
		}
		// 商品编码
		if (parameter.containsKey("sku")) {
			CriteriaUtils.add(criteria, "sku", FilterOperator.like, parameter.get("sku"));
			parameter.remove("sku", parameter.get("sku"));
		}
		// 物流单号
		if (parameter.containsKey("logisticsNo")) {
			CriteriaUtils.add(criteria, "logisticsNo", FilterOperator.like, parameter.get("logisticsNo"));
			parameter.remove("logisticsNo", parameter.get("logisticsNo"));
		}
		// 驿站编码
		if (parameter.containsKey("postStationCode")) {
			CriteriaUtils.add(criteria, "postStationCode", FilterOperator.like, parameter.get("postStationCode"));
			parameter.remove("postStationCode", parameter.get("postStationCode"));
		}
		MyCriteriaUtils.buildCriteriaMap(parameter);
		criteria = MyCriteriaUtils.add(criteria, parameter);
		JpaUtil.linq(ServiceOrder.class).where(criteria).paging(page);
	}

	/**
	 * 查询当前用户绑定的驿站编码
	 * @return
	 */
	@Expose
	public String queryNowUserLogisticsCode() {
		StringBuilder script = new StringBuilder();
		String loginUsername = ContextUtils.getLoginUsername();
		// 查询用户驿站绑定关系表
		PostStationGrantedAuthority psga;
		try {
			psga = JpaUtil.linq(PostStationGrantedAuthority.class).equal("actorId", loginUsername).findOne();
			String postId = psga.getPostId();
			// 查询驿站表
			PostStation ps = JpaUtil.linq(PostStation.class).equal("id", postId).findOne();
			String postStationCode = ps.getPostStationCode();
			return postStationCode;
		} catch (Exception e) {
			script.append("dorado.widget.NotifyTipManager.notify('请确认用户是否是驿站管理员');");
			throw new ClientRunnableException(script.toString());
		}
	}
	/**
	 * 驿站订单状态修改
	 * 根据 传入orderStatus状态和订单表id 更新订单状态
	 * @param parameter
	 * @return
	 */
	@Expose
	public String updateOrderStatusByPostStation(Map<String, Object> parameter) {
		if (null != parameter && !parameter.isEmpty()) {
			String id = (String) parameter.get("id");
			if (StringUtils.isBlank(id)) {
				throw new ClientRunnableException("dorado.widget.NotifyTipManager.notify('请选择需要操作的订单');");
			}
			String orderStatus = (String) parameter.get("orderStatus");
			if (StringUtils.isBlank(orderStatus)) {
				throw new ClientRunnableException("dorado.widget.NotifyTipManager.notify('无法修改更新订单状态,请重试!');");
			}
			// "30":未分配/已入库  "50":已取件
			if (!"30".equals(orderStatus) && !"50".equals(orderStatus)){
				throw new ClientRunnableException("dorado.widget.NotifyTipManager.notify('您无权更新订单至其他状态!');");
			}
			// 根据id查询订单
			ServiceOrder serviceOrder = JpaUtil.linq(ServiceOrder.class).equal("id", id).findOne();
			// 更新订单状态
			serviceOrder.setOrderStatus(orderStatus);
			// 跟新操作
			JpaUtil.merge(serviceOrder);
		}
		return null;
	}
}