package com.linjj.thinkweixin.vo;

import java.io.Serializable;

public class BaseVo implements Serializable {

	private static final long serialVersionUID = 1L;

	private String DEPTCODE;
	private String LINECODE;
	private String EMPCODE1;
	private boolean ISDEL;
	private boolean ISUPDATE;
	public String getDEPTCODE() {
		return DEPTCODE;
	}
	public void setDEPTCODE(String deptcode) {
		DEPTCODE = deptcode;
	}
	public String getLINECODE() {
		return LINECODE;
	}
	public void setLINECODE(String linecode) {
		LINECODE = linecode;
	}
	public String getEMPCODE1() {
		return EMPCODE1;
	}
	public void setEMPCODE1(String empcode1) {
		EMPCODE1 = empcode1;
	}
	public boolean isISDEL() {
		return ISDEL;
	}
	public void setISDEL(boolean isdel) {
		ISDEL = isdel;
	}
	public boolean isISUPDATE() {
		return ISUPDATE;
	}
	public void setISUPDATE(boolean isupdate) {
		ISUPDATE = isupdate;
	}

}
