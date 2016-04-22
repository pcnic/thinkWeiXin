package com.linjj.thinkweixin.service.manager;

import java.util.List;

import com.linjj.thinkweixin.vo.ZUser;

public interface IUserService {


	/**
	 * 用户登录方法
	 * 
	 * @param user
	 * @return
	 */
	public List login(String condition);
	
	public boolean updateUser(ZUser zUser);
	
	public boolean inserUser(ZUser zUser);
	
	public boolean deleteUser(ZUser zUser);
	
	public boolean findUser(ZUser zUser);
	
	public List<ZUser> findUserAll();
	
}
