package com.linjj.thinkweixin.service.impl.view;

import java.util.List;

import javax.annotation.Resource;

import com.linjj.thinkweixin.dao.BaseIbatisDao;
import com.linjj.thinkweixin.service.view.IMenuService;
import com.linjj.thinkweixin.vo.ZMenu;

public class MenuDaoImpl implements IMenuService {

	@Resource
	private BaseIbatisDao baseIbatisDao;
	public boolean deleteMenu(ZMenu menu) {
		// TODO Auto-generated method stub
		return false;
	}

	public ZMenu findMenu(String con) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<ZMenu> findMenuByCon(String con) {
		return baseIbatisDao.findByCondition("z_menu.findAllbyConds", con);
	}

	public boolean insertMenu(ZMenu menu) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean updateMenu(ZMenu menu) {
		// TODO Auto-generated method stub
		return false;
	}

}
