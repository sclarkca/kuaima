package com.kuaima.controller;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Sheet;
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
import com.bstek.dorado.uploader.UploadFile;
import com.bstek.dorado.uploader.annotation.FileResolver;
import com.kuaima.entity.ServiceCommodity;
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
@Transactional(readOnly = true)
public class ServiceCommodityController {
	/**
	 * 增删改
	 */
	@DataResolver
	@Transactional
	@Log(module = "服务商品", category = "CRUD")
	public void save(List<ServiceCommodity> serviceCommoditys) {
		JpaUtil.save(serviceCommoditys, new SmartSavePolicyAdapter() {
			@Override
			public boolean beforeInsert(SaveContext context) {
				ServiceCommodity serviceCommodity = context.getEntity();
				if (EntityState.NEW.equals(EntityUtils.getState(serviceCommodity))) {
					serviceCommodity.setCreateDate(new Date());
					serviceCommodity.setCreater(ContextUtils.getLoginUsername());
				}
				return true;
			}

			@Override
			public boolean beforeUpdate(SaveContext context) {
				ServiceCommodity serviceCommodity = context.getEntity();
				if (EntityState.MODIFIED.equals(EntityUtils.getState(serviceCommodity))) {
					serviceCommodity.setUpdateDate(new Date());
					serviceCommodity.setUpdater(ContextUtils.getLoginUsername());
				}
				return true;
			}
		});
	}

	/**
	 * 查询
	 */
	@DataProvider
	public void query(Page<ServiceCommodity> page, Map<String, Object> parameter) {
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
	private void buildCondition(Page<ServiceCommodity> page, Map<String, Object> parameter, Criteria criteria) {
		com.bstek.dorado.data.provider.Order order = new com.bstek.dorado.data.provider.Order();
		order.setProperty("updateDate");
		order.setDesc(true);
		criteria.addOrder(order);
		// 商品编码
		if (parameter.containsKey("sku")) {
			CriteriaUtils.add(criteria, "sku", FilterOperator.like, parameter.get("sku"));
			parameter.remove("sku", parameter.get("sku"));
		}
		// 商品名称
		if (parameter.containsKey("commodityName")) {
			CriteriaUtils.add(criteria, "commodityName", FilterOperator.like, parameter.get("commodityName"));
			parameter.remove("commodityName", parameter.get("commodityName"));
		}
		// 商品单价
		if (parameter.containsKey("commodityPrice")) {
			CriteriaUtils.add(criteria, "commodityPrice", FilterOperator.like, parameter.get("commodityPrice"));
			parameter.remove("commodityPrice", parameter.get("commodityPrice"));
		}
		// 商品分类 品类
		if (parameter.containsKey("commodityClassification")) {
			CriteriaUtils.add(criteria, "commodityClassification", FilterOperator.like,
					parameter.get("commodityClassification"));
			parameter.remove("commodityClassification", parameter.get("commodityClassification"));
		}
		MyCriteriaUtils.buildCriteriaMap(parameter);
		criteria = MyCriteriaUtils.add(criteria, parameter);
		JpaUtil.linq(ServiceCommodity.class).where(criteria).paging(page);
	}

	/**
	 * EXCEL文件上传导入
	 */
	@FileResolver
	@Transactional
	@Log(module = "服务商品", category = "批量导入")
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
		// String batchCode = getBatchCode();
		List<ServiceCommodity> newObjectList = new ArrayList<ServiceCommodity>();
		// 记录错误行
		List<ExcelImportError> errorRowList = new ArrayList<ExcelImportError>();
		for (int excelRow = 0; excelRow < rowNum; excelRow++) {
			// System.out.println(excelRow);
			// 入库实体类
			ServiceCommodity serviceCommodity = new ServiceCommodity();
			// 记录错误类
			ExcelImportError excelImportError = new ExcelImportError();
			// 错误原因
			StringBuffer reasonSb = new StringBuffer();
			// EXCEL的每一行数据
			ExcelData excelData = (ExcelData) initExcelDataList.get(excelRow);
			// 正式导入
			for (int mappingIdx = 0; mappingIdx < headerList.size(); mappingIdx++) {
				String propertyType = ServiceCommodity.getPropertyTypeByEnum(initMappingList.get(mappingIdx));
				String propertyName = ServiceCommodity.getPropertyNameByEnum(initMappingList.get(mappingIdx));
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
						BeanUtils.setProperty(serviceCommodity, propertyName, arg);
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
								BeanUtils.setProperty(serviceCommodity, propertyName, 0);
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
								BeanUtils.setProperty(serviceCommodity, propertyName, arg);
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
								BeanUtils.setProperty(serviceCommodity, propertyName, 0);
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
								BeanUtils.setProperty(serviceCommodity, propertyName, valueFromString);
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
								BeanUtils.setProperty(serviceCommodity, propertyName, 0);
							}
						}
						if (StringUtils.isNotBlank(valueFromForm)) {
							// 数字格式不对
							try {
								BigDecimal arg = new BigDecimal(valueFromForm);
								BeanUtils.setProperty(serviceCommodity, propertyName, arg);
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
								 * BeanUtils.setProperty(serviceCommodity,
								 * propertyName, null);
								 */
							}
						}
						if (StringUtils.isNotBlank(valueFromForm)) {
							// 日期格式不对
							try {
								Date arg = MyDateUtil.parserToDay(valueFromForm);
								BeanUtils.setProperty(serviceCommodity, propertyName, arg);
							} catch (Exception e) {
								System.out.println("Date error----------------");
								// e.printStackTrace();
								// 记录错误
								reasonSb.append(" 第" + (mappingIdx + 1) + "列日期格式不对");
							}
						}
					}
				}
				// serviceCommodity.setBatchNum(batchCode);
			}
			excelImportError.setRowIdx(excelRow);
			excelImportError.setReason(reasonSb.toString());
			errorRowList.add(excelImportError);
			newObjectList.add(serviceCommodity);
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
		List<ServiceCommodity> goodObjectList = new ArrayList<ServiceCommodity>();
		for (int i = 0; i < goodIdxList.size(); i++) {
			ServiceCommodity serviceCommodity = newObjectList.get(goodIdxList.get(i));
			if (!MyBeanUtil.checkAllFieldValueNull(serviceCommodity)) {
				goodObjectList.add(serviceCommodity);
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
				ServiceCommodity serviceCommodity = goodObjectList.get(i);
				serviceCommodity.setCreateDate(new Date());
				serviceCommodity.setCreater(ContextUtils.getLoginUsername());
				JpaUtil.persist(serviceCommodity);
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
}