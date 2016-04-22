package com.linjj.thinkweixin.upload;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.linjj.thinkweixin.base.MyDateBean;

public class MyUtils {

	/**
	 * 上传文件
	 * 
	 * @param savePath
	 *            文件的保存路径
	 * @param uploadFile
	 *            被上传的文件
	 * @return newFileName
	 */
	public static String upload(String uploadFileName, String savePath, File uploadFile,String resourceType) {
		String newFileName = getUUIDName(uploadFileName, savePath,resourceType);
		try {
			FileOutputStream fos = new FileOutputStream(savePath + newFileName);
			FileInputStream fis = new FileInputStream(uploadFile);
			byte[] buffer = new byte[1024];
			int len = 0;
			while ((len = fis.read(buffer)) > 0) {
				fos.write(buffer, 0, len);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return newFileName;
	}

	public static String getUUIDName(String fileName, String dir,String resourceType) {
		String[] split = fileName.split("\\.");
		String extendFile = "." + split[split.length - 1].toLowerCase();
		MyDateBean my = new MyDateBean();
		//return java.util.UUID.randomUUID().toString() + extendFile;
		return "res-"+resourceType+"-"+my.Format("yyyyMMddHHmmssSSS")+extendFile;
	}

	/**
	 * 根据路径创建一系列的目录
	 * 
	 * @param path
	 */
	public static boolean mkDirectory(String path) {
		File file = null;
		try {
			file = new File(path);
			if (!file.exists()) {
				return file.mkdirs();
			}
		} catch (RuntimeException e) {
			e.printStackTrace();
		} finally {
			file = null;
		}
		return false;
	}
	/**
	 *  测试此抽象路径名表示的文件或目录是否存在。
	 */
	public static boolean exists(String filePath){
		if(filePath == null || filePath.equals("")){
			return false;
		}
		try{
			java.io.File file = new java.io.File(filePath);
			return file.exists();
		}catch(Exception e){
			e.printStackTrace();
		}
		return false;
	}

}
