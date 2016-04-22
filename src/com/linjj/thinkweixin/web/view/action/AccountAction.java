package com.linjj.thinkweixin.web.view.action;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import com.linjj.thinkweixin.service.view.IAccountService;
import com.linjj.thinkweixin.service.view.IMenuService;
import com.linjj.thinkweixin.vo.ZAccount;
import com.linjj.thinkweixin.vo.ZMenu;
import com.linjj.thinkweixin.web.base.BaseAction;

public class AccountAction extends BaseAction {
	private static final long serialVersionUID = 1L;
	@Resource
	private IAccountService accountDaoImpl;
	@Resource
	private IMenuService menuDaoImpl; 
	private ZMenu zMenu;
	private ZAccount zAccount;
	private List<ZAccount> accountlist = new ArrayList<ZAccount>();
	private List<ZMenu> menuList = new ArrayList<ZMenu> ();

	@Override
	public Object getModel(){
		return zAccount;
		
	}	
	/**
	 * 微信公众账号信息
	 */

	public String query() throws Exception{
		String sqlStr = " 1=1 ";
		setMenuList(menuDaoImpl.findMenuByCon(sqlStr));
		accountlist = accountDaoImpl.findAccountByCon(sqlStr);
		return SUCCESS;
	}

	public List<ZAccount> getAccountlist() {
		return accountlist;
	}

	public void setAccountlist(List<ZAccount> accountlist) {
		this.accountlist = accountlist;
	}

	public void setMenuList(List<ZMenu> menuList) {
		this.menuList = menuList;
	}

	public List<ZMenu> getMenuList() {
		return menuList;
	}
}
