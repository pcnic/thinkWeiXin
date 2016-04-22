package com.linjj.thinkweixin.service.view;

import java.util.List;

import com.linjj.thinkweixin.vo.ZAccount;

public interface IAccountService {
	
	public List<ZAccount> findAccountByCon(String condition);
	public void updateAccount(String condition);
	public void deleteAccount(ZAccount zAccount);
	public void insertAccount(ZAccount zAccount);
	public ZAccount findAccountByID(String condition);

	
	
	
	
	
}
