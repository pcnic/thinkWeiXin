package com.linjj.thinkweixin.service.view;

import java.util.List;

import com.linjj.thinkweixin.vo.ZMenu;

public interface IMenuService {
	
	public List<ZMenu> findMenuByCon(String con);
	public boolean insertMenu(ZMenu zMenu);
	public boolean deleteMenu(ZMenu zMenu);
	public boolean updateMenu(ZMenu zMenu);
	public ZMenu findMenu(String con);

}
