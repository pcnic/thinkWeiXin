package com.linjj.thinkweixin.service.impl.manager;

import java.util.List;

import javax.annotation.Resource;

import com.linjj.thinkweixin.dao.BaseIbatisDao;
import com.linjj.thinkweixin.service.manager.IUserService;
import com.linjj.thinkweixin.vo.ZUser;



public class UserDaoImpl implements IUserService{
	@Resource
	private BaseIbatisDao<?> baseIbatisDao;
	public List<?> login(String condition) {
		return baseIbatisDao.findByCondition("z_user.login", condition);
	}
    public boolean updateUser(ZUser zUser){
    	return false;
    }
	public boolean deleteUser(ZUser user) {
		// TODO Auto-generated method stub
		return false;
	}
	public boolean findUser(ZUser user) {
		// TODO Auto-generated method stub
		return false;
	}
	public List<ZUser> findUserAll() {
		// TODO Auto-generated method stub
		return null;
	}
	public boolean inserUser(ZUser user) {
		// TODO Auto-generated method stub
		return false;
	}

}
