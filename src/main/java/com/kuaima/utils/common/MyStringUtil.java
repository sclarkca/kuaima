package com.kuaima.utils.common;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;

public class MyStringUtil extends StringUtils {

	public static String cleanInput(String input) {
		String myInput = StringUtils.trimToEmpty(input);
		return BCConvert.qj2bj(myInput);
	}
	/**
	 * "aaa,bbb,ccc"转{aaa,bbb,ccc}
	 */
	public static List<String> stringToStringList(String string) {
		List<String> strList = new ArrayList<String>();
		StringTokenizer st = new StringTokenizer(string.trim(), ",");
		while (st.hasMoreTokens()) {
			String s = st.nextToken().trim();
			strList.add(s);
		}
		return strList;
	}

	/**
	 * private static final Logger logger =
	 * LoggerFactory.getLogger(MyStringUtil.class);
	 */
	/**
	 * 判断是否是数字
	 * 
	 * @param number
	 * @return
	 */
	public static boolean isNumber(String number) {
		if (number == null || "".equals(number))
			return false;
		int index = number.indexOf(".");
		if (index < 0) {
			return StringUtils.isNumeric(number);
		} else {
			String num1 = number.substring(0, index);
			String num2 = number.substring(index + 1);

			return StringUtils.isNumeric(num1) && StringUtils.isNumeric(num2);
		}
	}

	/**
	 * "AAA_BBB_CCC"转"aaaBbbCcc"
	 */
	public static String columnTofield(String columnStrPara) {
		String columnStr = StringUtils.upperCase(columnStrPara);
		StringBuilder sb = new StringBuilder();

		for (int i = 1; i < columnStr.length(); i++) {
			char c = columnStr.charAt(i);
			char b = columnStr.charAt(i - 1);
			if (b != 95) {
				sb.append(Character.toLowerCase(c));
			}
			if (b == 95) {
				sb.append(Character.toUpperCase(c));
			}
		}
		String s = columnStr.substring(0, 1).toLowerCase() + sb.toString();
		return StringUtils.remove(s, "_");
	}

	/**
	 * 判断一个字符串是否是UUID
	 * 
	 */
	public static boolean isUuid(String string) {
		// 8a998be95161e494015161f9ac7b0008转为57f27802-b2c3-4cf5-b169-8ea1e8fafc79
		if (string.length() != 32 || string.contains("|")) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 获得一个UUID
	 * 
	 * @return String UUID
	 */
	public static String getUUID() {
		String s = UUID.randomUUID().toString();
		// 去掉“-”符号
		return StringUtils.remove(s, "-");
	}

}
