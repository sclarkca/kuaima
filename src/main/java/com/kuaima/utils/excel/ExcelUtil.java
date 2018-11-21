package com.kuaima.utils.excel;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kuaima.utils.common.MyStringUtil;


public class ExcelUtil {
	private ExcelUtil (){}
	private static final Logger logger = LoggerFactory.getLogger(ExcelUtil.class);
	/**
	 * 从excel中读取sheet
	 * 
	 * @param formFile
	 *            Excel文件
	 * @param sheetIdx
	 *            第一个为0
	 * @return
	 * @throws IOException 
	 */
	public static Sheet getSheetByForm(File file, int sheetIdx) throws IOException{
		Sheet sheet = null;
		InputStream is =null;
		try {
			 is = FileUtils.openInputStream(file);
			Workbook wb = WorkbookFactory.create(is);
			sheet = wb.getSheetAt(sheetIdx);
		} catch (Exception e) {
			logger.info("Exception",e);
		}finally {
			if(is !=null){
				is.close();
			}
		}
		return sheet;
	}
	public static Sheet getSheetByFormIO(InputStream inputStream, int sheetIdx) throws InvalidFormatException, IOException  {
		Workbook wb = WorkbookFactory.create(inputStream);
		return wb.getSheetAt(sheetIdx);
	}

	
	/**
	 * 把EXCEL某列AAA_BBB_CCC批量换成aaaBbbCcc
	 */
	public static void excelToJava() {
		File file = new File("input.txt");
		try {
			List<String> nameList = FileUtils.readLines(file);
			for (int i = 0; i < nameList.size(); i++) {
				String str = nameList.get(i);
				if (StringUtils.isBlank(str)) {
					logger.info("{}","");
					continue;
				}
				char c = str.charAt(0);
				if ((str.getBytes("utf-8").length == str.length() && Character.isUpperCase(c)) || str.contains("_")) {
					String columnTofield = MyStringUtil.columnTofield(str);
					
					logger.info(columnTofield);
				} else {
					logger.info(str);
				}
			}

		} catch (IOException e) {

			logger.info("Exception",e);
		}

	}
}
