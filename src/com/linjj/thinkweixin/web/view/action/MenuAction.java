package com.linjj.thinkweixin.web.view.action;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import com.linjj.thinkweixin.service.view.IMenuService;
import com.linjj.thinkweixin.vo.ZMenu;
import com.linjj.thinkweixin.web.base.BaseAction;

public class MenuAction extends BaseAction {
	
	@Resource
	private IMenuService menuDaoImpl; 
	private ZMenu zMenu;
	private List<ZMenu> menuList= new ArrayList<ZMenu>();
	
	@Override
	public Object getModel(){
		return zMenu;
		
	}
	
	
	public String queryMenu(){
		    String con = " 1=1 ";
		    menuList = menuDaoImpl.findMenuByCon(con);
			return SUCCESS;
	}
	
	

}
