package com.linjj.thinkweixin.vo;

/**
 * @author pcnic
 * 微信公众账号信息
 *
 */
public class ZAccount extends BaseVo{ 
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String accountId;
	public String getAccountId() {
		return accountId;
	}
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	public String getAccountInfo() {
		return accountInfo;
	}
	public void setAccountInfo(String accountInfo) {
		this.accountInfo = accountInfo;
	}
	public String getAccountType() {
		return accountType;
	}
	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}
	public String getAccountCreateDate() {
		return accountCreateDate;
	}
	public void setAccountCreateDate(String accountCreateDate) {
		this.accountCreateDate = accountCreateDate;
	}
	private String accountName;
	private String accountInfo;
	private String accountType;
	private String accountCreateDate;
}
