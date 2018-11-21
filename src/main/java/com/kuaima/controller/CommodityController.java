package com.kuaima.controller;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Sheet;
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
import com.bstek.dorado.uploader.UploadFile;
import com.bstek.dorado.uploader.annotation.FileResolver;
import com.kuaima.entity.Brand;
import com.kuaima.entity.Bu;
import com.kuaima.entity.Commodity;
import com.kuaima.service.BrandService;
import com.kuaima.service.BuService;
import com.kuaima.service.CommodityService;
import com.kuaima.service.EncodingTypeService;
import com.kuaima.utils.common.MyBeanUtil;
import com.kuaima.utils.common.MyCriteriaUtils;
import com.kuaima.utils.common.MyDateUtil;
import com.kuaima.utils.common.MyStringUtil;
import com.kuaima.utils.excel.ExcelData;
import com.kuaima.utils.excel.ExcelHeader;
import com.kuaima.utils.excel.ExcelImportError;
import com.kuaima.utils.excel.ExcelReader;
import com.kuaima.utils.excel.ExcelUtil;

/**
 * 商品信息Controller
 * 
 * @author 88388018
 * @date 2018年9月18日
 */
@Controller
@Transactional(readOnly = true)
public class CommodityController {

	// 商品
	private static final String ENCODING_TYPE_CODE = "COMMODITY";
	// 商品编码前缀
	private static final String PREFIX = "CM";

	@Autowired
	CommodityService commodityService;
	@Autowired
	EncodingTypeService encodingTypeService;
	@Autowired
	BrandService brandService;
	@Autowired
	BuService buService;
	/**
	 * 增删改
	 */
	@DataResolver
	@Transactional
	@Log(module = "商品信息", category = "CRUD")
	public void save(List<Commodity> commoditys) {
		JpaUtil.save(commoditys, new SmartSavePolicyAdapter() {
			@Override
			public boolean beforeInsert(SaveContext context) {
				Commodity commodity = context.getEntity();
				if (EntityState.NEW.equals(EntityUtils.getState(commodity))) {
					commodity.setCreateDate(new Date());
					commodity.setCreater(ContextUtils.getLoginUsername());
				}
				String sku = commodity.getSku();
				this.crudCode(sku);
				return true;
			}

			@Override
			public boolean beforeUpdate(SaveContext context) {
				Commodity commodity = context.getEntity();
				if (EntityState.MODIFIED.equals(EntityUtils.getState(commodity))) {
					commodity.setUpdateDate(new Date());
					commodity.setUpdater(ContextUtils.getLoginUsername());
				}
				String sku = commodity.getSku();
				this.crudCode(sku);
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
	public void query(Page<Commodity> page, Map<String, Object> parameter) {
		if (parameter == null) {
			parameter = new HashMap<>();
		}
		MyCriteriaUtils.clearEmptyValue(parameter);
		Criteria criteria = new Criteria();
		buildCondition(page, parameter, criteria);
		// 
		Collection<Commodity> entities = page.getEntities();
		for (Commodity commodity : entities) {
			String brandCode = commodity.getBrandCode();
			String buCode = commodity.getBuCode();
			Brand brand = brandService.queryBrandName(brandCode);
			Bu bu = buService.queryBuName(buCode);
			commodity.setBrandName(brand.getBrandName());
			commodity.setBuName(bu.getBuName());
		}
	}

	/**
	 * 构建查询条件
	 *
	 * @param page
	 * @param parameter
	 * @param criteria
	 */
	private void buildCondition(Page<Commodity> page, Map<String, Object> parameter, Criteria criteria) {
		com.bstek.dorado.data.provider.Order order = new com.bstek.dorado.data.provider.Order();
		order.setProperty("updateDate");
		order.setDesc(true);
		criteria.addOrder(order);
		// 商品编码
		if (parameter.containsKey("commodityCode")) {
			CriteriaUtils.add(criteria, "commodityCode", FilterOperator.like, parameter.get("commodityCode"));
			parameter.remove("commodityCode", parameter.get("commodityCode"));
		}
		// 商品名称
		if (parameter.containsKey("commodityName")) {
			CriteriaUtils.add(criteria, "commodityName", FilterOperator.like, parameter.get("commodityName"));
			parameter.remove("commodityName", parameter.get("commodityName"));
		}
		// 品牌编码
		if (parameter.containsKey("brandCode")) {
			CriteriaUtils.add(criteria, "brandCode", FilterOperator.like, parameter.get("brandCode"));
			parameter.remove("brandCode", parameter.get("brandCode"));
		}
		// 品类编码
		if (parameter.containsKey("buCode")) {
			CriteriaUtils.add(criteria, "buCode", FilterOperator.like, parameter.get("buCode"));
			parameter.remove("buCode", parameter.get("buCode"));
		}
		// 图片
		if (parameter.containsKey("imageUrl")) {
			CriteriaUtils.add(criteria, "imageUrl", FilterOperator.like, parameter.get("imageUrl"));
			parameter.remove("imageUrl", parameter.get("imageUrl"));
		}
		// 商品状态
		if (parameter.containsKey("status")) {
			CriteriaUtils.add(criteria, "status", FilterOperator.like, parameter.get("status"));
			parameter.remove("status", parameter.get("status"));
		}
		// 商品重量
		if (parameter.containsKey("weight")) {
			CriteriaUtils.add(criteria, "weight", FilterOperator.like, parameter.get("weight"));
			parameter.remove("weight", parameter.get("weight"));
		}
		// 商品描述
		if (parameter.containsKey("desc")) {
			CriteriaUtils.add(criteria, "desc", FilterOperator.like, parameter.get("desc"));
			parameter.remove("desc", parameter.get("desc"));
		}
		MyCriteriaUtils.buildCriteriaMap(parameter);
		criteria = MyCriteriaUtils.add(criteria, parameter);
		JpaUtil.linq(Commodity.class).where(criteria).paging(page);
	}

	/**
	 * EXCEL文件上传导入
	 */
	@FileResolver
	@Transactional
	@Log(module = "商品信息", category = "批量导入")
	public Map<String, String> process(UploadFile file, Map<String, Object> parameter) throws Exception {
		InputStream inputStream = null;
		try {
			// 导EXCEL
			inputStream = file.getInputStream();
			String result = batchCreateFromExcel(inputStream);
			String success = StringUtils.substringBefore(result, ",");
			String fail = StringUtils.substringAfter(result, ",");
			String tip = "导入完成|成功" + success + "条|失败" + fail + "条";
			tip = URLEncoder.encode(tip, "UTF-8");
			Map<String, String> data = new HashMap<String, String>();
			data.put("tip", tip);
			data.put("success", success);
			data.put("fail", fail);
			return data;
		} catch (IllegalStateException e) {
			e.printStackTrace();
			Map<String, String> data = new HashMap<String, String>();
			data.put("tip", "导入失败");
			return data;
		} catch (IOException e) {
			e.printStackTrace();
			Map<String, String> data = new HashMap<String, String>();
			data.put("tip", "导入失败");
			return data;
		} finally {
			try {
				inputStream.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	// 根据导入配置批量创建
	public String batchCreateFromExcel(InputStream inputStream) throws Exception {
		ExcelData toFiledExcelData = new ExcelData();
		ExcelReader excelReader = new ExcelReader();
		// EXCEL表头集合
		Sheet sheetByFormIO = ExcelUtil.getSheetByFormIO(inputStream, 0);
		List<ExcelHeader> headerList = excelReader.getExcelDataHeaderListFromSheetIO(sheetByFormIO);
		// 映射关系
		List<Integer> initMappingList = new ArrayList<Integer>();
		for (int i = 0; i < headerList.size(); i++) {
			String toFieldStr = BeanUtils.getProperty(toFiledExcelData, "toField" + (i + 1));
			int toFiledValue = Integer.valueOf(toFieldStr);
			initMappingList.add(toFiledValue);
		}
		// 获取List
		List<ExcelData> initExcelDataList = excelReader.getExcelDataListFromSheetIO(sheetByFormIO);
		// excelRow=EXCEL行数量
		int rowNum = initExcelDataList.size();
		// 组装新对象
		List<Commodity> newObjectList = new ArrayList<>();
		// 记录错误行
		List<ExcelImportError> errorRowList = new ArrayList<>();
		for (int excelRow = 0; excelRow < rowNum; excelRow++) {
			// 入库实体类
			Commodity commodity = new Commodity();
			// 记录错误类
			ExcelImportError excelImportError = new ExcelImportError();
			// 错误原因
			StringBuffer reasonSb = new StringBuffer();
			// EXCEL的每一行数据
			ExcelData excelData = (ExcelData) initExcelDataList.get(excelRow);
			// 正式导入
			for (int mappingIdx = 0; mappingIdx < headerList.size(); mappingIdx++) {
				String propertyType = Commodity.getPropertyTypeByEnum(initMappingList.get(mappingIdx));
				String propertyName = Commodity.getPropertyNameByEnum(initMappingList.get(mappingIdx));
				if (initMappingList.get(mappingIdx) != 0) {
					if (StringUtils.isBlank(propertyType)) {
						continue;
					}
					if (propertyType.equals("String")) {
						String arg = BeanUtils.getProperty(excelData, "field" + (mappingIdx + 1));
						// EXCEL空值
						if (StringUtils.isBlank(arg)) {
							// 必填项
							if (isRequiredByRelatedFieldValue(propertyName)) {
								// 记录错误
								reasonSb.append(" 第" + (mappingIdx + 1) + "列应该是必填项");
							}
						}
						BeanUtils.setProperty(commodity, propertyName, arg);
					}
					if (propertyType.equals("enum")) {
						String valueFromForm = BeanUtils.getProperty(excelData, "field" + (mappingIdx + 1));
						valueFromForm = MyStringUtil.cleanInput(valueFromForm);
						// EXCEL空值
						if (StringUtils.isBlank(valueFromForm)) {
							// 必填项
							if (isRequiredByRelatedFieldValue(propertyName)) {
								// 记录错误
								reasonSb.append(" 第" + (mappingIdx + 1) + "列应该是必填项");
							}
							// 不必填
							if (!isRequiredByRelatedFieldValue(propertyName)) {
								BeanUtils.setProperty(commodity, propertyName, 0);
							}
						}
						if (StringUtils.isNotBlank(valueFromForm)) {
							String valueFromString = this.getEnumKeyByFieldValue(propertyName, valueFromForm);
							valueFromForm = MyStringUtil.cleanInput(valueFromForm);
							// ENUM查不到值
							if (valueFromString.equals("error")) {
								// 记录错误
								reasonSb.append(" 第" + (mappingIdx + 1) + "列无法根据名称查到对应的数据字典");
							}
							// ENUM能查到值
							if (!valueFromString.equals("error")) {
								long arg = Long.valueOf(valueFromString);
								BeanUtils.setProperty(commodity, propertyName, arg);
							}
						}
					}
					if (propertyType.equals("table")) {
						String valueFromForm = BeanUtils.getProperty(excelData, "field" + (mappingIdx + 1));
						valueFromForm = MyStringUtil.cleanInput(valueFromForm);
						// EXCEL空值
						if (StringUtils.isBlank(valueFromForm)) {
							// 必填项
							if (isRequiredByRelatedFieldValue(propertyName)) {
								// 记录错误
								reasonSb.append(" 第" + (mappingIdx + 1) + "列应该是必填项");
							}
							// 不必填
							if (!isRequiredByRelatedFieldValue(propertyName)) {
								BeanUtils.setProperty(commodity, propertyName, 0);
							}
						}
						if (StringUtils.isNotBlank(valueFromForm)) {
							String valueFromString = this.getTableKeyByFieldValue(propertyName, valueFromForm);
							// TABLE查不到值
							if (valueFromString.equals("error")) {
								// 记录错误
								reasonSb.append(" 第" + (mappingIdx + 1) + "列无法根据名称查到对应的关联表");
							}
							// TABLE能查到值
							if (!valueFromString.equals("error")) {
								BeanUtils.setProperty(commodity, propertyName, valueFromString);
							}
						}
					}
					if ((propertyType.equals("BigDecimal") || propertyType.equals("Long")
							|| propertyType.equals("Integer")) && !propertyType.equals("enum")) {
						String valueFromForm = BeanUtils.getProperty(excelData, "field" + (mappingIdx + 1));
						valueFromForm = MyStringUtil.cleanInput(valueFromForm);
						// EXCEL空值
						if (StringUtils.isBlank(valueFromForm)) {
							// 必填项
							if (isRequiredByRelatedFieldValue(propertyName)) {
								// 记录错误
								reasonSb.append(" 第" + (mappingIdx + 1) + "列应该是必填项");
							}
							// 不必填
							if (!isRequiredByRelatedFieldValue(propertyName)) {
								BeanUtils.setProperty(commodity, propertyName, 0);
							}
						}
						if (StringUtils.isNotBlank(valueFromForm)) {
							// 数字格式不对
							try {
								BigDecimal arg = new BigDecimal(valueFromForm);
								BeanUtils.setProperty(commodity, propertyName, arg);
							} catch (Exception e) {
								// 记录错误
								reasonSb.append(" 第" + (mappingIdx + 1) + "列数字格式不对");
							}
						}
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
							// 不必填
							if (!isRequiredByRelatedFieldValue(propertyName)) {
								/*
								 * BeanUtils.setProperty(commodity,
								 * propertyName, null);
								 */
							}
						}
						if (StringUtils.isNotBlank(valueFromForm)) {
							// 日期格式不对
							try {
								Date arg = MyDateUtil.parserToDay(valueFromForm);
								BeanUtils.setProperty(commodity, propertyName, arg);
							} catch (Exception e) {
								System.out.println("Date error----------------");
								// e.printStackTrace();
								// 记录错误
								reasonSb.append(" 第" + (mappingIdx + 1) + "列日期格式不对");
							}
						}
					}
				}
				// commodity.setBatchNum(batchCode);
			}
			excelImportError.setRowIdx(excelRow);
			excelImportError.setReason(reasonSb.toString());
			errorRowList.add(excelImportError);
			newObjectList.add(commodity);
		}
		// 重整errorRowList
		List<ExcelImportError> newErrorRowList = new ArrayList<ExcelImportError>();
		for (int i = 0; i < errorRowList.size(); i++) {
			ExcelImportError excelImportError = errorRowList.get(i);
			if (StringUtils.isNotBlank(excelImportError.getReason())) {
				newErrorRowList.add(excelImportError);
			}
		}
		List<ExcelImportError> newGoodRowList = new ArrayList<ExcelImportError>();
		for (int i = 0; i < errorRowList.size(); i++) {
			ExcelImportError excelImportError = errorRowList.get(i);
			if (StringUtils.isBlank(excelImportError.getReason())) {
				newGoodRowList.add(excelImportError);
			}
		}
		// errorIdxList
		List<Integer> errorIdxList = new ArrayList<Integer>();
		for (int i = 0; i < newErrorRowList.size(); i++) {
			errorIdxList.add(newErrorRowList.get(i).getRowIdx());
		}
		// goodIdxList
		List<Integer> goodIdxList = new ArrayList<Integer>();
		for (int i = 0; i < newGoodRowList.size(); i++) {
			goodIdxList.add(newGoodRowList.get(i).getRowIdx());
		}
		// 根据goodIdxList获得goodObjectList
		List<Commodity> goodObjectList = new ArrayList<Commodity>();
		for (int i = 0; i < goodIdxList.size(); i++) {
			Commodity commodity = newObjectList.get(goodIdxList.get(i));
			if (!MyBeanUtil.checkAllFieldValueNull(commodity)) {
				goodObjectList.add(commodity);
			}
		}
		// 根据goodIdxList获得goodExcelDataList
		List<ExcelData> goodExcelDataList = new ArrayList<ExcelData>();
		for (int i = 0; i < goodIdxList.size(); i++) {
			ExcelData excelData = initExcelDataList.get(goodIdxList.get(i));
			goodExcelDataList.add(excelData);
		}
		// 根据errorIdxList获得badExcelDataList
		List<ExcelData> badExcelDataList = new ArrayList<ExcelData>();
		for (int i = 0; i < errorIdxList.size(); i++) {
			ExcelData excelData = initExcelDataList.get(errorIdxList.get(i));
			badExcelDataList.add(excelData);
		}
		// 成功
		if (goodExcelDataList.size() > 0) {
			for (int i = 0; i < goodObjectList.size(); i++) {
				// 创建动作
				Commodity commodity = goodObjectList.get(i);
				commodity.setCreateDate(new Date());
				commodity.setCreater(ContextUtils.getLoginUsername());

				// TODO 创建商品编码,
				JpaUtil.persist(commodity);
			}
		}
		String result = goodObjectList.size() + "," + badExcelDataList.size();
		return result;
	}

	// 根据字段值取必填项
	private boolean isRequiredByRelatedFieldValue(String propertyName) {
		return false;
	}

	// 导入用,根据VALUE查询ENUM KEY
	private String getEnumKeyByFieldValue(String propertyName, String valueFromForm) {
		switch (propertyName) {
		default:
			return "error";
		}
	}

	// 导入用,根据VALUE查询关联表KEY
	private String getTableKeyByFieldValue(String propertyName, String valueFromForm) {
		switch (propertyName) {
		default:
			return "error";
		}
	}

	/**
	 * 创建商品编码
	 * 
	 * @return
	 */
	@Expose
	public String createCmdCode() {
		return commodityService.createCmdCode(ENCODING_TYPE_CODE, PREFIX);
	}

	/**
	 * 查询订单中查询商品对应信息商品列表
	 * 
	 * @return
	 */
	@DataProvider
	public List<Commodity> queryCmdList() {
		List<Commodity> list = JpaUtil.linq(Commodity.class).list();
		for (Commodity commodity : list) {
			String brandCode = commodity.getBrandCode();
			String buCode = commodity.getBuCode();
			Brand brand = brandService.queryBrandName(brandCode);
			Bu bu = buService.queryBuName(buCode);
			commodity.setBrandName(brand.getBrandName());
			commodity.setBuName(bu.getBuName());
		}
		return list;
	}
	
	/**
	 * 查询商品列表
	 * @return
	 */
	@DataProvider
	public List<Commodity> queryCmmdityList(){
		return JpaUtil.linq(Commodity.class).list();
	}
	
	/**
	 * 根据商品编码查询商品名称
	 */
	@Expose
	public String queryCmdNameByCode(Map<String,Object> parameter){
		String sku = (String) parameter.get("sku");
		Commodity commodity = JpaUtil.linq(Commodity.class).equal("sku", sku).findOne();
		return commodity.getCommodityName();
	}
}