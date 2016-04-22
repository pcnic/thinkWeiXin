package com.linjj.thinkweixin.upload;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import com.linjj.thinkweixin.base.BaseInfoBean;
import com.linjj.thinkweixin.base.MyDateBean;
import com.linjj.thinkweixin.db.DBSql;
import com.linjj.thinkweixin.db.Result;
import com.linjj.thinkweixin.util.strFormat;

/**
 * MProgramFileAction.java
 * 
 * Copyright (c)
 * 
 * @author
 * @version 1.0
 */
@SuppressWarnings("serial")
public class MProgramFileAction extends BaseAction {
	private Map<String, Object> infos = new HashMap<String, Object>();

	public static final String ROOT = "upload/";

	public static final String MSG = "msg";

	private File myUpload;

	private String path = "";
	
	private String myUploadContentType;

	private String myUploadFileName;

	private boolean success;

	private String msg = "";

	private String proName = "";

	private String proVer = "";
	
	private String screenType = "";

	private String programType = "";
	
	public String getMsg() {
		return msg;
	}

	
	public String returnFalseMSG(String msg) {
		myUpload.delete();
		this.success = false;
		this.msg = msg;
		return MSG;
	}

	public File getMyUpload() {
		return myUpload;
	}

	public void setMyUpload(File myUpload) {
		this.myUpload = myUpload;
	}

	public String getMyUploadContentType() {
		return myUploadContentType;
	}

	public void setMyUploadContentType(String myUploadContentType) {
		this.myUploadContentType = myUploadContentType;
	}

	public String getMyUploadFileName() {
		return myUploadFileName;
	}

	public void setMyUploadFileName(String myUploadFileName) {
		this.myUploadFileName = myUploadFileName;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) throws UnsupportedEncodingException {
		this.path = URLDecoder.decode(path, "UTF-8");
	}

	public Map<String, Object> getInfos() {
		return infos;
	}

	public void setInfos(Map<String, Object> infos) {
		this.infos = infos;
	}

	public String getScreenType() {
		return screenType;
	}

	public void setScreenType(String screenType) {
		this.screenType = screenType;
	}

	public static void main(String[] cyx) throws Exception {

	}

	public String getProName() {
		return proName;
	}

	public void setProName(String proName) throws UnsupportedEncodingException {
		this.proName = URLDecoder.decode(proName, "UTF-8");
	}

	public String getProVer() {
		return proVer;
	}

	public void setProVer(String proVer) throws UnsupportedEncodingException {
		this.proVer = URLDecoder.decode(proVer, "UTF-8");
	}

	public String getProgramType() {
		return programType;
	}

	public void setProgramType(String programType)throws UnsupportedEncodingException {
		this.programType = URLDecoder.decode(programType, "UTF-8");
	}
}
