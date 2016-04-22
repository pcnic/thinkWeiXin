package com.linjj.thinkweixin.filter;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;
/**
 * ����9����
 */
public class ProjectInterceptor implements Interceptor {
	static final long serialVersionUID = 1L;  
    private Logger log = Logger.getLogger( ProjectInterceptor.class);
	public void destroy() {}  
	public void init() {}  
	
	public String intercept(ActionInvocation invocation) throws Exception {
		HttpServletResponse response=ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		return invocation.invoke();
	}

}
