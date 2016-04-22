package com.linjj.thinkweixin.web.manager.action;

import java.util.List;

import javax.annotation.Resource;

import org.apache.struts2.ServletActionContext;

import com.linjj.thinkweixin.service.manager.IUserService;
import com.linjj.thinkweixin.vo.ZUser;
import com.linjj.thinkweixin.web.base.BaseAction;

public class LoginAction extends BaseAction {
	private static final long serialVersionUID = 1L;
	@Resource
	private IUserService userDaoImpl;

	private ZUser zUser = new ZUser();
	@Override 
	public Object getModel() {
		return zUser;
	}

	/**
	 * login
	 * @return
	 * @throws Exception
	 */
	public String login() throws Exception {
		
		String sqlStr = " userId='" + zUser.getUserId() + "' and passWord='" + zUser.getPassWord() + "'";
		List list = userDaoImpl.login(sqlStr);
		if (list == null || list.size() == 0) {
			return ERROR;
		} else if (list.size() > 1) {
			return ERROR;
		} else {
			ServletActionContext.getContext().getSession().put("zUser",zUser);
			return SUCCESS;
		}
	}
}