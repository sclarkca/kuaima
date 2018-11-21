package com.kuaima.controller.merchant;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.bstek.bdf3.dorado.jpa.CriteriaUtils;
import com.bstek.bdf3.dorado.jpa.JpaUtil;
import com.bstek.bdf3.log.annotation.Log;
import com.bstek.bdf3.security.ContextUtils;
import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.Expose;
import com.bstek.dorado.data.provider.Criteria;
import com.bstek.dorado.data.provider.Page;
import com.bstek.dorado.data.provider.filter.FilterOperator;
import com.bstek.dorado.uploader.UploadFile;
import com.bstek.dorado.uploader.annotation.FileResolver;
import com.bstek.dorado.view.resolver.ClientRunnableException;
import com.kuaima.entity.City;
import com.kuaima.entity.CommodityQuotedPrice;
import com.kuaima.entity.InstallTemp;
import com.kuaima.entity.MerchantFeeRelationship;
import com.kuaima.entity.MerchantOrderInfo;
import com.kuaima.entity.PostStation;
import com.kuaima.entity.ServiceOrder;
import com.kuaima.entity.TransFeeTemp;
import com.kuaima.entity.WorkerInfo;
import com.kuaima.entity.merchant.MerchantGrantedAuthority;
import com.kuaima.entity.merchant.MerchantInfo;
import com.kuaima.entity.repo.ServiceOrderImp;
import com.kuaima.entity.repo.WorkerOrderCount;
import com.kuaima.service.CityService;
import com.kuaima.service.EncodingTypeService;
import com.kuaima.service.ServiceOrderService;
import com.kuaima.utils.common.MapUtil;
import com.kuaima.utils.common.MyBeanUtil;
import com.kuaima.utils.common.MyCriteriaUtils;
import com.kuaima.utils.common.MyDateUtil;
import com.kuaima.utils.common.MyStringUtil;
import com.kuaima.utils.excel.ExcelData;
import com.kuaima.utils.excel.ExcelHeader;
import com.kuaima.utils.excel.ExcelImportError;
import com.kuaima.utils.excel.ExcelReader;
import com.kuaima.utils.excel.ExcelUtil;

@Controller
@Transactional
public class ServiceOrderController {

	@Autowired
	CityService cityService;

	@Autowired
	ServiceOrderService serviceOrderService;
	@Autowired
	EncodingTypeService encodingTypeService;

	/**
	 * 创建订单
	 */
	@Expose
	public void createOrder(Map<String, Object> parameter) {
		String serviceOrderStr = JSON.toJSONString(parameter.get("serviceOrder"));
		ServiceOrder serviceOrder = JSON.parseObject(serviceOrderStr, ServiceOrder.class);
		serviceOrder.setCreateDate(new Date());
		serviceOrder.setCreater(ContextUtils.getLoginUsername());
		buildOrderParam(serviceOrder);
		// 创建订单
		ServiceOrder order = JpaUtil.persist(serviceOrder);
		String sku = serviceOrder.getSku();
		// 获取 市区详细地址
		String city = serviceOrder.getCity();
		String cityName = cityService.queryCityNameByAcCode(city);
		String region = serviceOrder.getRegion();
		String regionName = cityService.queryCityNameByAcCode(region);
		String address = serviceOrder.getAddress();
		String addr = cityName + regionName + address;
		// 创建toB订单信息
		createToBOrder(sku, order, region, addr);
	}

	/**
	 * 组装订单信息
	 * 
	 * @param serviceOrder
	 */
	private void buildOrderParam(ServiceOrder serviceOrder) {
		// 订单绑定商户编码
		String merchantCode = this.queryNowUserMerchantCode();
		serviceOrder.setMerchantCode(merchantCode);
		/**
		 * String orderNo = serviceOrder.getOrderNo();
		 * String sixNo = orderNo.substring(14, orderNo.length());
		 * this.crudCode(sixNo);
		 */
		
		// 初始化订单状态 10:未发货
		serviceOrder.setOrderStatus("10");
		// 获取 市区详细地址
		String region = serviceOrder.getRegion();
		// 根据城市编码查询驿站编码
		PostStation postStation = JpaUtil.linq(PostStation.class).equal("region", region).findOne();
		String postStationCode = postStation.getPostStationCode();
		serviceOrder.setPostStationCode(postStationCode);
		Integer hasDistribution = serviceOrder.getHasDistribution();
		if (null == hasDistribution) {
			serviceOrder.setHasDistribution(1);
		}
		Integer hasInstallation = serviceOrder.getHasInstallation();
		if (null == hasInstallation) {
			serviceOrder.setHasInstallation(1);
		}
	}

	/**
	 * 修改订单
	 */
	@Expose
	public void updateOrder(Map<String, Object> parameter) {
		String serviceOrderStr = JSON.toJSONString(parameter.get("serviceOrder"));
		ServiceOrder serviceOrder = JSON.parseObject(serviceOrderStr, ServiceOrder.class);
		serviceOrder.setUpdateDate(new Date());
		serviceOrder.setUpdater(ContextUtils.getLoginUsername());
		buildOrderParam(serviceOrder);
		// 根据id查询订单信息
		String id = serviceOrder.getId();
		ServiceOrder findOne = JpaUtil.linq(ServiceOrder.class).equal("id", id).findOne();
		/**
		 * ServiceOrder order = JpaUtil.persist(serviceOrder);
		 */
		try {
			BeanUtils.copyProperties(findOne, serviceOrder);
		} catch (IllegalAccessException | InvocationTargetException e) {

		}
		String sku = serviceOrder.getSku();
		// 获取 市区详细地址
		String city = serviceOrder.getCity();
		String cityName = cityService.queryCityNameByAcCode(city);
		String region = serviceOrder.getRegion();
		String regionName = cityService.queryCityNameByAcCode(region);
		String address = serviceOrder.getAddress();
		String addr = cityName + regionName + address;
		// 创建toB订单信息
		createToBOrder(sku, findOne, region, addr);
	}

	/**
	 * 计算安装或运费
	 * 
	 * @param parameter
	 * @return
	 */
	@Expose
	public Map<String, Object> getFeeAction(Map<String, Object> parameter) {
		StringBuilder script = new StringBuilder();
		Map<String, Object> resultMap = new HashMap<>();
		String jsonString = JSON.toJSONString(parameter.get("serviceOrder"));
		ServiceOrder serviceOrder = JSON.parseObject(jsonString, ServiceOrder.class);
		String orderNo = serviceOrder.getOrderNo();
		String sku = serviceOrder.getSku();
		serviceOrder.setOrderStatus("10");
		// 获取 市区详细地址
		String city = serviceOrder.getCity();
		String cityName = cityService.queryCityNameByAcCode(city);
		String region = serviceOrder.getRegion();
		String regionName = cityService.queryCityNameByAcCode(region);
		String address = serviceOrder.getAddress();
		String addr = cityName + regionName + address;
		// 根据城市编码查询驿站编码
		PostStation postStation;
		try {
			postStation = JpaUtil.linq(PostStation.class).equal("region", region).findOne();
		} catch (Exception e) {
			script.append("dorado.widget.NotifyTipManager.notify('该地区暂不支持配送');");
			throw new ClientRunnableException(script.toString());
		}
		String postProvince = postStation.getProvince();
		String postProvinceName = cityService.queryCityNameByAcCode(postProvince);
		String postCity = postStation.getCity();
		String postCityName = cityService.queryCityNameByAcCode(postCity);
		String postRegion = postStation.getRegion();
		String postRegionName = cityService.queryCityNameByAcCode(postRegion);
		postStation.setProvinceName(postProvinceName);
		postStation.setCityName(postCityName);
		postStation.setRegionName(postRegionName);
		resultMap.put("postStation", postStation);
		String postStationCode = postStation.getPostStationCode();
		serviceOrder.setPostStationCode(postStationCode);
		String merchantCode = queryNowUserMerchantCode();
		List<MerchantFeeRelationship> list = JpaUtil.linq(MerchantFeeRelationship.class)
				.equal("merchantCode", merchantCode).list();
		if (null == list || list.isEmpty()) {
			script.append("dorado.widget.NotifyTipManager.notify('请确认商户是否绑定关系模板');");
			throw new ClientRunnableException(script.toString());
		}
		boolean skuFlag = false;
		for (MerchantFeeRelationship merchantFeeRelationship : list) {
			String relationshipTempNo = merchantFeeRelationship.getRelationshipTempNo();
			List<CommodityQuotedPrice> list2 = JpaUtil.linq(CommodityQuotedPrice.class)
					.equal("relationshipTempNo", relationshipTempNo).list();
			for (CommodityQuotedPrice commodityQuotedPrice : list2) {
				String cmmdityCode = commodityQuotedPrice.getSku();
				if (sku.equals(cmmdityCode)) {
					skuFlag = true;
					// 判断是否安装
					Integer hasInstallation = serviceOrder.getHasInstallation();
					// toB 安装费
					String price = "0";
					// to师傅安装费
					String finishPrice = "0";
					if (hasInstallation == null || 1 == hasInstallation) {
						// 获取安装运费模板
						String installTempNo = commodityQuotedPrice.getInstallTempNo();
						InstallTemp installTemp = JpaUtil.linq(InstallTemp.class).equal("installTempNo", installTempNo)
								.findOne();
						// 获取安装价格(toB)
						price = installTemp.getPrice();
						// 获取实付安装费用
						finishPrice = installTemp.getFinishPrice();
					}
					Integer hasDistribution = serviceOrder.getHasDistribution();
					// toB 总运费
					Double totalTransFee = 0.0;
					// 师傅总运费收入
					Double totalTransFeeForWorker = 0.0;
					// 获取运费初始价格
					String initPrice = null;
					// 获取初始公里
					String initKm = null;
					// 获取运费每公里价格
					String perKmPrice = null;
					// 获取运费初始价格(To worker)
					String serverInitPrice = null;
					// 获取实付师傅每公里费用(To worker)
					String actualPerKmPrice = null;
					// 根据地址获取坐标
					String coordinate;
					try {
						coordinate = MapUtil.coordinate(addr);
					} catch (Exception e) {
						script.append("dorado.widget.NotifyTipManager.notify('未根据地址信息获取坐标信息(地址有误或网络异常)');");
						throw new ClientRunnableException(script.toString());
					}
					// 获取订单所在区的中心经纬度
					City cityObj = JpaUtil.linq(City.class).equal("adCode", region).findOne();
					// 经度
					Double lng = cityObj.getLng();
					// 纬度
					Double lat = cityObj.getLat();
					double dist = 0;
					if (hasDistribution == null || 1 == hasDistribution) {
						String transTempNo = commodityQuotedPrice.getTransTempNo();
						TransFeeTemp transFeeTemp = JpaUtil.linq(TransFeeTemp.class).equal("transTempNo", transTempNo)
								.findOne();
						// 获取运费初始价格(To B)
						initPrice = transFeeTemp.getInitPrice();
						// 获取初始公里(To B)
						initKm = transFeeTemp.getInitKm();
						// 获取运费每公里价格(To B)
						perKmPrice = transFeeTemp.getPerKmPrice();
						/********************************************************/
						// 获取运费初始价格(To worker)
						serverInitPrice = transFeeTemp.getServerInitPrice();
						// 获取实付师傅每公里费用(To worker)
						actualPerKmPrice = transFeeTemp.getActualPerKmPrice();
						/********************************************************/
						String center = lng.toString() + "," + lat.toString();
						// 计算距离
						String distance = MapUtil.distance(center, coordinate);
						dist = Math.ceil(Double.valueOf(distance) / 1000);
						// toB 超出运费
						Double morePrice = 0.0;
						// 师傅超出运费
						Double workerMoreAmount = 0.0;
						Double km = Double.valueOf(initKm);
						if (dist > km) {
							Double moreThanDistance = dist - km;
							// (ToB)
							morePrice = Double.valueOf(perKmPrice) * moreThanDistance;
							// 师傅运费
							workerMoreAmount = Double.valueOf(actualPerKmPrice) * moreThanDistance;
						}
						// 总运费(To B)
						totalTransFee = Double.valueOf(initPrice) + morePrice;
						totalTransFeeForWorker = Double.valueOf(serverInitPrice) + workerMoreAmount;
					}
					// 总金额
					Double totalAmount = Double.valueOf(price) + totalTransFee;
					// 创建TO B 对象
					MerchantOrderInfo moi = new MerchantOrderInfo();
					moi.setInstallAmount(price);
					moi.setDistributionAmount(String.valueOf(totalTransFee));
					moi.setTotalAmount(String.valueOf(totalAmount));
					moi.setOrderId(orderNo);
					moi.setCenterLongitude(String.valueOf(lng));
					moi.setCenterLatitude(String.valueOf(lat));
					moi.setDistributionLongitude(coordinate.substring(0, coordinate.indexOf(",")));
					moi.setDistributionLatitude(coordinate.substring(coordinate.indexOf(",") + 1, coordinate.length()));
					moi.setInitKm(initKm);
					moi.setInitKmPrice(initPrice);
					moi.setPerKmPrice(perKmPrice);
					moi.setPerKmAmount(perKmPrice);
					moi.setDistance(String.valueOf(dist));
					// 师傅费用
					moi.setWorkerInstallAmount(finishPrice);
					moi.setWorkerDistributionAmount(String.valueOf(totalTransFeeForWorker));
					moi.setWorkerInitPrice(serverInitPrice);
					moi.setWorkerPerKmPrice(actualPerKmPrice);
					resultMap.put("merchantOrderInfo", moi);
				}
			}
		}
		if (!skuFlag) {
			script.append("dorado.widget.NotifyTipManager.notify('请确认此商品是否设置模板信息');");
			throw new ClientRunnableException(script.toString());
		}
		return resultMap;
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
			String provinceName = cityService.queryCityNameByAcCode(province);
			String city = serviceOrder.getCity();
			String cityName = cityService.queryCityNameByAcCode(city);
			String region = serviceOrder.getRegion();
			String regionName = cityService.queryCityNameByAcCode(region);
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

		// 默认传入商户编码查询
		String merchantCode = queryNowUserMerchantCode();
		if (StringUtils.isNotBlank(merchantCode)) {
			CriteriaUtils.add(criteria, "merchantCode", FilterOperator.eq, merchantCode);
			parameter.remove("merchantCode", parameter.get("merchantCode"));
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
	 * 查询当前用户角色绑定的商户编码
	 * 
	 * @return
	 */
	private String queryNowUserMerchantCode() {
		StringBuilder script = new StringBuilder();
		String loginUsername = ContextUtils.getLoginUsername();
		// 根据用户查询商户和用户绑定关系表
		MerchantGrantedAuthority mga;
		try {
			mga = JpaUtil.linq(MerchantGrantedAuthority.class).equal("actorId", loginUsername).findOne();
		} catch (Exception e) {
			script.append("dorado.widget.NotifyTipManager.notify('请确认用户是否绑定商户');");
			throw new ClientRunnableException(script.toString());
		}
		String merchantId = mga.getMerchantId();
		// 根据id查询MerchantInfo表获取商户信息
		MerchantInfo merchantInfo = JpaUtil.linq(MerchantInfo.class).equal("id", merchantId).findOne();
		return merchantInfo.getMerchantCode();
	}

	/**
	 * EXCEL文件上传导入
	 */
	@FileResolver
	@Transactional
	@Log(module = "订单信息", category = "批量导入")
	public Map<String, String> process(UploadFile file, Map<String, Object> parameter) throws Exception {
		try (InputStream inputStream = file.getInputStream();) {
			String sku = (String) parameter.get("sku");
			// 导EXCEL
			String result = batchCreateFromExcel(inputStream, sku);
			String success = StringUtils.substringBefore(result, ",");
			String fail = StringUtils.substringAfter(result, ",");
			String tip = "导入完成|成功" + success + "条|失败" + fail + "条";
			tip = URLEncoder.encode(tip, "UTF-8");
			Map<String, String> data = new HashMap<>();
			data.put("tip", tip);
			data.put("success", success);
			data.put("fail", fail);
			return data;
		} catch (ClientRunnableException e) {
			Map<String, String> data = new HashMap<>();
			String script = e.getScript();
			data.put("tip", script.substring(script.indexOf("('"), script.indexOf("')")));
			return data;
		} catch (IllegalStateException e) {
			Map<String, String> data = new HashMap<>();
			data.put("tip", "导入失败");
			return data;
		} catch (IOException e) {
			Map<String, String> data = new HashMap<>();
			data.put("tip", "导入失败");
			return data;
		}
	}

	// 根据导入配置批量创建
	public String batchCreateFromExcel(InputStream inputStream, String sku) throws Exception {
		ExcelData toFiledExcelData = new ExcelData();
		ExcelReader excelReader = new ExcelReader();
		// EXCEL表头集合
		Sheet sheetByFormIO = ExcelUtil.getSheetByFormIO(inputStream, 0);
		List<ExcelHeader> headerList = excelReader.getExcelDataHeaderListFromSheetIO(sheetByFormIO);
		// 映射关系
		List<Integer> initMappingList = new ArrayList<>();
		for (int i = 0; i < headerList.size(); i++) {
			String toFieldStr = BeanUtils.getProperty(toFiledExcelData, "toField" + (i + 1));
			int toFiledValue = Integer.parseInt(toFieldStr);
			initMappingList.add(toFiledValue);
		}
		// 获取List
		List<ExcelData> initExcelDataList = excelReader.getExcelDataListFromSheetIO(sheetByFormIO);
		// excelRow=EXCEL行数量
		int rowNum = initExcelDataList.size();
		// 组装新对象
		List<ServiceOrder> newObjectList = new ArrayList<>();
		// 记录错误行
		List<ExcelImportError> errorRowList = new ArrayList<>();
		for (int excelRow = 0; excelRow < rowNum; excelRow++) {
			// 入库实体类
			ServiceOrder serviceOrder = new ServiceOrder();
			// 记录错误类
			ExcelImportError excelImportError = new ExcelImportError();
			// 错误原因
			StringBuilder reasonSb = new StringBuilder();
			// EXCEL的每一行数据
			ExcelData excelData = (ExcelData) initExcelDataList.get(excelRow);
			// 正式导入
			for (int mappingIdx = 0; mappingIdx < headerList.size(); mappingIdx++) {
				String propertyType = ServiceOrder.getPropertyTypeByEnum(initMappingList.get(mappingIdx));
				String propertyName = ServiceOrder.getPropertyNameByEnum(initMappingList.get(mappingIdx));
				if (initMappingList.get(mappingIdx) != 0) {
					if (StringUtils.isBlank(propertyType)) {
						continue;
					}
					if (propertyType.equals("String")) {
						String arg = BeanUtils.getProperty(excelData, "field" + (mappingIdx + 1));
						// EXCEL空值
						if (StringUtils.isBlank(arg)) {
							// 必填项
							if (!"retailerOrderNo".equals(propertyName)) {
								// 记录错误
								reasonSb.append(" 第" + (mappingIdx + 1) + "列应该是必填项");
							}
						}
						BeanUtils.setProperty(serviceOrder, propertyName, arg);
					}
					if (propertyType.equals("date")) {
						String valueFromForm = BeanUtils.getProperty(excelData, "field" + (mappingIdx + 1));
						valueFromForm = MyStringUtil.cleanInput(valueFromForm);
						// EXCEL空值
						if (StringUtils.isBlank(valueFromForm)) {
							// 必填项
							if (isRequiredByRelatedFieldValue(propertyName)) {
								// 记录错误
								reasonSb.append(" 第" + (mappingIdx + 1) + "列应该是必填项");
							}
						}
						if (StringUtils.isNotBlank(valueFromForm)) {
							// 日期格式不对
							try {
								Date arg = MyDateUtil.parserToSecond(valueFromForm);
								BeanUtils.setProperty(serviceOrder, propertyName, arg);
							} catch (Exception e) {
								reasonSb.append(" 第" + (mappingIdx + 1) + "列日期格式不对");
							}
						}
					}
					if (propertyType.equals("Integer")) {
						String valueFromForm = BeanUtils.getProperty(excelData, "field" + (mappingIdx + 1));
						if (StringUtils.isNotBlank(valueFromForm)) {
							// 必填项
							if (!"hasDistribution".equals(propertyName)) {
								if ("Y".equals(valueFromForm)) {
									BeanUtils.setProperty(serviceOrder, propertyName, 1);
								}
								if ("N".equals(valueFromForm)) {
									BeanUtils.setProperty(serviceOrder, propertyName, 2);
								}
							}
							if (!"hasInstallation".equals(propertyName)) {
								if ("Y".equals(valueFromForm)) {
									BeanUtils.setProperty(serviceOrder, propertyName, 1);
								}
								if ("N".equals(valueFromForm)) {
									BeanUtils.setProperty(serviceOrder, propertyName, 2);
								}
							}
						}

					}
				}
			}
			excelImportError.setRowIdx(excelRow);
			excelImportError.setReason(reasonSb.toString());
			errorRowList.add(excelImportError);
			newObjectList.add(serviceOrder);
		}
		// 重整errorRowList
		List<ExcelImportError> newErrorRowList = new ArrayList<>();
		for (int i = 0; i < errorRowList.size(); i++) {
			ExcelImportError excelImportError = errorRowList.get(i);
			if (StringUtils.isNotBlank(excelImportError.getReason())) {
				newErrorRowList.add(excelImportError);
			}
		}
		List<ExcelImportError> newGoodRowList = new ArrayList<>();
		for (int i = 0; i < errorRowList.size(); i++) {
			ExcelImportError excelImportError = errorRowList.get(i);
			if (StringUtils.isBlank(excelImportError.getReason())) {
				newGoodRowList.add(excelImportError);
			}
		}
		// errorIdxList
		List<Integer> errorIdxList = new ArrayList<>();
		for (int i = 0; i < newErrorRowList.size(); i++) {
			errorIdxList.add(newErrorRowList.get(i).getRowIdx());
		}
		// goodIdxList
		List<Integer> goodIdxList = new ArrayList<>();
		for (int i = 0; i < newGoodRowList.size(); i++) {
			goodIdxList.add(newGoodRowList.get(i).getRowIdx());
		}
		// 根据goodIdxList获得goodObjectList
		List<ServiceOrder> goodObjectList = new ArrayList<>();
		for (int i = 0; i < goodIdxList.size(); i++) {
			ServiceOrder serviceOrder = newObjectList.get(goodIdxList.get(i));
			if (!MyBeanUtil.checkAllFieldValueNull(serviceOrder)) {
				goodObjectList.add(serviceOrder);
			}
		}
		// 根据goodIdxList获得goodExcelDataList
		List<ExcelData> goodExcelDataList = new ArrayList<>();
		for (int i = 0; i < goodIdxList.size(); i++) {
			ExcelData excelData = initExcelDataList.get(goodIdxList.get(i));
			goodExcelDataList.add(excelData);
		}
		// 根据errorIdxList获得badExcelDataList
		List<ExcelData> badExcelDataList = new ArrayList<>();
		for (int i = 0; i < errorIdxList.size(); i++) {
			ExcelData excelData = initExcelDataList.get(errorIdxList.get(i));
			badExcelDataList.add(excelData);
		}
		// 成功
		if (goodExcelDataList.size() > 0) {
			for (int i = 0; i < goodObjectList.size(); i++) {
				// 创建动作
				ServiceOrder serviceOrder = goodObjectList.get(i);
				// 创建订单编码 日期+6位流水号
				SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmsss");
				String temp = sf.format(new Date());
				String sixNo = this.create6RandomNum();
				String orderNo = temp + sixNo;
				serviceOrder.setOrderNo(orderNo);
				extracted(sku, serviceOrder, orderNo);
			}
		}
		String result = goodObjectList.size() + "," + badExcelDataList.size();
		return result;
	}

	private void extracted(String sku, ServiceOrder serviceOrder, String orderNo) {
		serviceOrder.setOrderStatus("10");
		serviceOrder.setCreateDate(new Date());
		serviceOrder.setCreater(ContextUtils.getLoginUsername());
		// 获取 市区详细地址
		String city = serviceOrder.getCity();
		String cityName = cityService.queryCityNameByAcCode(city);
		String region = serviceOrder.getRegion();
		String regionName = cityService.queryCityNameByAcCode(region);
		String address = serviceOrder.getAddress();
		String addr = cityName + regionName + address;
		// 根据城市编码查询驿站编码
		PostStation postStation = JpaUtil.linq(PostStation.class).equal("region", region).findOne();
		String postStationCode = postStation.getPostStationCode();
		serviceOrder.setPostStationCode(postStationCode);
		JpaUtil.persist(serviceOrder);
		// 更新编码
		/**
		 * this.crudCode(sixNo);
		 */
		createToBOrder(sku, serviceOrder, region, addr);
	}

	// 1.创建toB的费用订单
	private void createToBOrder(String sku, ServiceOrder serviceOrder, String region, String addr) {
		StringBuilder script = new StringBuilder();
		String orderId = serviceOrder.getId();
		// 获取订单创建人
		Date createDate = serviceOrder.getCreateDate();
		String creater = serviceOrder.getCreater();
		Date updateDate = serviceOrder.getUpdateDate();
		String updater = serviceOrder.getUpdater();
		// 查询当前用户绑定的商户编码
		String merchantCode = serviceOrder.getMerchantCode();
		List<MerchantFeeRelationship> list = JpaUtil.linq(MerchantFeeRelationship.class)
				.equal("merchantCode", merchantCode).list();
		if (null == list || list.isEmpty()) {
			script.append("dorado.widget.NotifyTipManager.notify('请确认商户是否绑定关系模板');");
			throw new ClientRunnableException(script.toString());
		}
		boolean skuFlag = false;
		for (MerchantFeeRelationship merchantFeeRelationship : list) {
			String relationshipTempNo = merchantFeeRelationship.getRelationshipTempNo();
			List<CommodityQuotedPrice> list2 = JpaUtil.linq(CommodityQuotedPrice.class)
					.equal("relationshipTempNo", relationshipTempNo).list();
			for (CommodityQuotedPrice commodityQuotedPrice : list2) {
				String cmmdityCode = commodityQuotedPrice.getSku();
				if (sku.equals(cmmdityCode)) {
					skuFlag = true;
					// 判断是否安装
					Integer hasInstallation = serviceOrder.getHasInstallation();
					// toB 安装费
					String price = "0";
					// to师傅安装费
					String finishPrice = "0";
					if (hasInstallation == null || 1 == hasInstallation) {
						// 获取安装运费模板
						String installTempNo = commodityQuotedPrice.getInstallTempNo();
						InstallTemp installTemp = JpaUtil.linq(InstallTemp.class).equal("installTempNo", installTempNo)
								.findOne();
						// 获取安装价格(toB)
						price = installTemp.getPrice();
						// 获取实付安装费用
						finishPrice = installTemp.getFinishPrice();
					}
					Integer hasDistribution = serviceOrder.getHasDistribution();
					// toB 总运费
					Double totalTransFee = 0.0;
					// 师傅总运费收入
					Double totalTransFeeForWorker = 0.0;
					// 获取运费初始价格
					String initPrice = null;
					// 获取初始公里
					String initKm = null;
					// 获取运费每公里价格
					String perKmPrice = null;
					// 获取运费初始价格(To worker)
					String serverInitPrice = null;
					// 获取实付师傅每公里费用(To worker)
					String actualPerKmPrice = null;
					// 根据地址获取坐标
					String coordinate = MapUtil.coordinate(addr);
					// 获取订单所在城市的中心经纬度
					City cityObj = JpaUtil.linq(City.class).equal("adCode", region).findOne();
					// 经度
					Double lng = cityObj.getLng();
					// 纬度
					Double lat = cityObj.getLat();
					double dist = 0;
					if (hasDistribution == null || 1 == hasDistribution) {
						String transTempNo = commodityQuotedPrice.getTransTempNo();
						TransFeeTemp transFeeTemp = JpaUtil.linq(TransFeeTemp.class).equal("transTempNo", transTempNo)
								.findOne();
						// 获取运费初始价格(To B)
						initPrice = transFeeTemp.getInitPrice();
						// 获取初始公里(To B)
						initKm = transFeeTemp.getInitKm();
						// 获取运费每公里价格(To B)
						perKmPrice = transFeeTemp.getPerKmPrice();
						/********************************************************/
						// 获取运费初始价格(To worker)
						serverInitPrice = transFeeTemp.getServerInitPrice();
						// 获取实付师傅每公里费用(To worker)
						actualPerKmPrice = transFeeTemp.getActualPerKmPrice();
						/********************************************************/
						String center = lng.toString() + "," + lat.toString();
						// 计算距离
						String distance = MapUtil.distance(center, coordinate);
						dist = Math.ceil(Double.valueOf(distance) / 1000);
						// toB 超出运费
						Double morePrice = 0.0;
						// 师傅超出运费
						Double workerMoreAmount = 0.0;
						Double km = Double.valueOf(initKm);
						if (dist > km) {
							Double moreThanDistance = dist - km;
							// (ToB)
							morePrice = Double.valueOf(perKmPrice) * moreThanDistance;
							// 师傅运费
							workerMoreAmount = Double.valueOf(actualPerKmPrice) * moreThanDistance;
						}
						// 总运费(To B)
						totalTransFee = Double.valueOf(initPrice) + morePrice;
						totalTransFeeForWorker = Double.valueOf(serverInitPrice) + workerMoreAmount;
					}
					// 总金额
					Double totalAmount = Double.valueOf(price) + totalTransFee;
					// 创建TO B 对象
					MerchantOrderInfo moi = new MerchantOrderInfo();
					moi.setInstallAmount(price);
					moi.setDistributionAmount(String.valueOf(totalTransFee));
					moi.setTotalAmount(String.valueOf(totalAmount));
					moi.setOrderId(orderId);
					moi.setCenterLongitude(String.valueOf(lng));
					moi.setCenterLatitude(String.valueOf(lat));
					moi.setDistributionLongitude(coordinate.substring(0, coordinate.indexOf(",")));
					moi.setDistributionLatitude(coordinate.substring(coordinate.indexOf(",") + 1, coordinate.length()));
					moi.setInitKm(initKm);
					moi.setInitKmPrice(initPrice);
					moi.setPerKmPrice(perKmPrice);
					moi.setPerKmAmount(perKmPrice);
					moi.setDistance(String.valueOf(dist));
					// 师傅费用
					moi.setWorkerInstallAmount(finishPrice);
					moi.setWorkerDistributionAmount(String.valueOf(totalTransFeeForWorker));
					moi.setWorkerInitPrice(serverInitPrice);
					moi.setWorkerPerKmPrice(actualPerKmPrice);
					moi.setMerchantCode(merchantCode);
					moi.setCreateDate(createDate);
					moi.setCreater(creater);
					try {
						MerchantOrderInfo findOne = JpaUtil.linq(MerchantOrderInfo.class).equal("orderId", orderId)
								.findOne();
						findOne.setInstallAmount(price);
						findOne.setDistributionAmount(String.valueOf(totalTransFee));
						findOne.setTotalAmount(String.valueOf(totalAmount));
						findOne.setOrderId(orderId);
						findOne.setCenterLongitude(String.valueOf(lng));
						findOne.setCenterLatitude(String.valueOf(lat));
						findOne.setDistributionLongitude(coordinate.substring(0, coordinate.indexOf(",")));
						findOne.setDistributionLatitude(
								coordinate.substring(coordinate.indexOf(",") + 1, coordinate.length()));
						findOne.setInitKm(initKm);
						findOne.setInitKmPrice(initPrice);
						findOne.setPerKmPrice(perKmPrice);
						findOne.setPerKmAmount(perKmPrice);
						// 师傅费用
						findOne.setWorkerInstallAmount(finishPrice);
						findOne.setWorkerDistributionAmount(String.valueOf(totalTransFeeForWorker));
						findOne.setWorkerInitPrice(serverInitPrice);
						findOne.setWorkerPerKmPrice(actualPerKmPrice);
						findOne.setMerchantCode(merchantCode);
						findOne.setUpdateDate(updateDate);
						findOne.setUpdater(updater);
						JpaUtil.merge(findOne);
					} catch (Exception e) {
						JpaUtil.persist(moi);
					}
				}

			}
		}
		if (!skuFlag) {
			script.append("dorado.widget.NotifyTipManager.notify('请确认此商品是否设置模板信息');");
			throw new ClientRunnableException(script.toString());
		}
	}


	// 根据字段值取必填项
	private boolean isRequiredByRelatedFieldValue(String propertyName) {
		return false;
	}

	/**
	 * 查询师傅当天订单数列表
	 * 
	 * @return
	 */
	@DataProvider
	public void queryWorkerNowOrder(Page<WorkerOrderCount> page, Map<String, Object> parameter) {
		List<WorkerOrderCount> list = serviceOrderService.queryWorkerNowOrder();
		page.setEntities(list);
	}

	/**
	 * 查询当天已分配订单列表
	 * 
	 * @param page
	 * @param parameter
	 */
	@DataProvider
	public void queryNowDistibutionOrder(Page<ServiceOrderImp> page, Map<String, Object> parameter) {
		List<ServiceOrderImp> list = serviceOrderService.queryNowDistibutionOrder();
		page.setEntities(list);
	}

	/**
	 * 查询未分配订单列表
	 * 
	 * @return
	 */
	@DataProvider
	public void queryNotAssignedOrderList(Page<ServiceOrder> page, Map<String, Object> parameter) {
		String postStationCode = null;
		if (parameter != null && !parameter.isEmpty()) {
			postStationCode = (String) parameter.get("postStationCode");
		}
		List<ServiceOrder> list = serviceOrderService.queryServiceOrderListWithoutWorker(postStationCode);
		page.setEntities(list);
	}

	/**
	 * 订单和师傅绑定
	 * 
	 * @param parameter
	 * @return
	 */
	@Expose
	@Transactional
	public String updateOrderInfo(Map<String, Object> parameter) {
		if (null != parameter && !parameter.isEmpty()) {
			WorkerInfo workerInfo = (WorkerInfo) parameter.get("workerInfoSelections");
			if (null == workerInfo) {
				throw new ClientRunnableException("dorado.widget.NotifyTipManager.notify('请选择绑定的师傅');");
			}
			String orderStr = JSON.toJSONString(parameter.get("serviceOrderSelections"));
			if (StringUtils.isBlank(orderStr)) {
				throw new ClientRunnableException("dorado.widget.NotifyTipManager.notify('请选择绑定的订单');");
			}
			List<ServiceOrder> list = JSON.parseArray(orderStr, ServiceOrder.class);
			List<ServiceOrder> modifyOrders = new ArrayList<>();
			for (ServiceOrder serviceOrder : list) {
				String orderNo = serviceOrder.getOrderNo();
				// 根据订单查询订单信息
				ServiceOrder order = JpaUtil.linq(ServiceOrder.class).equal("orderNo", orderNo).findOne();
				order.setOrderStatus("40");
				order.setWorkerId(workerInfo.getWorkerId().toString());
				order.setUpdateDate(new Date());
				order.setUpdater(ContextUtils.getLoginUsername());
				modifyOrders.add(order);
			}
			List<ServiceOrder> merge = JpaUtil.merge(modifyOrders);
			if (null != merge && !merge.isEmpty()) {
				return "SUCCESS";
			}
		}
		return null;
	}

	/**
	 * 订单解绑
	 * 
	 * @param parameter
	 * @return
	 */
	@Expose
	public String deleteOrderWorkerInfo(Map<String, Object> parameter) {
		if (null != parameter && !parameter.isEmpty()) {
			String orderStr = JSON.toJSONString(parameter.get("serviceOrderImpInfo"));
			if (StringUtils.isBlank(orderStr)) {
				throw new ClientRunnableException("dorado.widget.NotifyTipManager.notify('请选择解绑的订单');");
			}
			List<ServiceOrderImp> list = JSON.parseArray(orderStr, ServiceOrderImp.class);
			List<ServiceOrder> modifyOrders = new ArrayList<>();
			for (ServiceOrderImp serviceOrderImp : list) {
				String orderNo = serviceOrderImp.getOrderNo();
				// 根据订单查询订单信息
				ServiceOrder serviceOrder = JpaUtil.linq(ServiceOrder.class).equal("orderNo", orderNo).findOne();
				serviceOrder.setWorkerId(null);
				serviceOrder.setUpdateDate(new Date());
				serviceOrder.setUpdater(ContextUtils.getLoginUsername());
				modifyOrders.add(serviceOrder);
			}
			List<ServiceOrder> merge = JpaUtil.merge(modifyOrders);
			if (null != merge && !merge.isEmpty()) {
				return "SUCCESS";
			}
		}
		return null;
	}

	/**
	 * 创建订单编码
	 * 
	 * @return
	 */
	@Expose
	public String createOrderCode() {
		// 创建订单编码 日期+6位流水号
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmsss");
		String temp = sf.format(new Date());
		/**
		 * String sixNo = encodingTypeService.createCode(ENCODING_TYPE_CODE, PREFIX);
		 */
		String sixNo = this.create6RandomNum();
		return temp + sixNo;
	}

	public String create6RandomNum(){
		Random random = new Random();
		String result="";
		for (int i=0;i<6;i++)
		{
			result+=random.nextInt(10);
		}
		return result;
	}
	/**
	 * 取消订单
	 * 
	 * @param parameter
	 */
	@Expose
	public void deleteOrder(Map<String, Object> parameter) {
		String id = (String) parameter.get("id");
		// 根据订单id 删除订单和toB订单数据
		if (StringUtils.isNotBlank(id)) {
			JpaUtil.lind(ServiceOrder.class).equal("id", id).delete();
			JpaUtil.lind(MerchantOrderInfo.class).equal("orderId", id).delete();
		}
	}
}