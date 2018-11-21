package com.kuaima.utils.common;

import java.io.File;

/**
 * 
 * 
 * java类获取web应用的根目录
 * 
 */
public class PathUtil {
	public PathUtil() {

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		PathUtil p = new PathUtil();
		System.out.println(p.getWebClassesPath());
		System.out.println(p.getWebInfPath());
		System.out.println(p.getWebRoot());
	}

	public String getWebClassesPath() {
		String path = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
		return path;

	}

	public String getWebInfPath() throws IllegalAccessException {
		String path = getWebClassesPath();
		if (path.indexOf("WEB-INF") > -1) {
			path = path.substring(0, path.indexOf("WEB-INF") + 8);
		} else {
			throw new IllegalAccessException("路径获取错误");
		}
		return path;
	}

	public String getWebRoot() throws IllegalAccessException {
		String path = getWebClassesPath();
		if (path.indexOf("WEB-INF") > -1) {
			path = path.substring(0, path.indexOf("WEB-INF/classes"));
		} else {
			throw new IllegalAccessException("路径获取错误");
		}
		return path;
	}

	// 上传使用
	public static File getDestFile(String fileName, File folder) {
		File destFile = new File(folder, fileName);
		// 若文件存在则删除
		if (!new File(folder, fileName).exists()) {
			destFile.delete();
		}
		destFile.getParentFile().mkdirs();
		return destFile;
	}
}