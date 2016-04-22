package com.linjj.thinkweixin.service.impl.view;

import java.util.List;

import javax.annotation.Resource;

import com.linjj.thinkweixin.dao.BaseIbatisDao;
import com.linjj.thinkweixin.service.view.IAccountService;
import com.linjj.thinkweixin.vo.ZAccount;

public class AccountDaoImpl implements IAccountService {

	@Resource
	private BaseIbatisDao<?> baseIbatisDao;
	
	public void delete(ZAccount account) {
		// TODO Auto-generated method stub
		
	}

	public ZAccount queryAccountByID(String condition) {
		// TODO Auto-generated method stub
		return null;
	}

	public void save(ZAccount account) {
		// TODO Auto-generated method stub
		
	}

	public void update(String condition) {
		// TODO Auto-generated method stub
		
	}

	public void deleteAccount(ZAccount account) {
		// TODO Auto-generated method stub
		
	}

	public List<ZAccount> findAccountByCon(String condition) {
		return baseIbatisDao.findByCondition("z_account.findAllbyConds", condition);
	}

	public ZAccount findAccountByID(String condition) {
		// TODO Auto-generated method stub
		return null;
	}

	public void insertAccount(ZAccount account) {
		// TODO Auto-generated method stub
		
	}

	public void updateAccount(String condition) {
		// TODO Auto-generated method stub
		
	}
	}
