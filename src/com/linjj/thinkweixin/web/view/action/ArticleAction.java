package com.linjj.thinkweixin.web.view.action;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import com.linjj.thinkweixin.service.view.IArticleService;
import com.linjj.thinkweixin.service.view.IMenuService;
import com.linjj.thinkweixin.vo.ZArticle;
import com.linjj.thinkweixin.vo.ZMenu;
import com.linjj.thinkweixin.web.base.BaseAction;

public class ArticleAction extends BaseAction {
	
	/**
	 * 微信公众账号文章信息
	 */
	private static final long serialVersionUID = 1L;
	@Resource
	private IArticleService articleDaoImpl;
	@Resource
	private IMenuService menuDaoImpl; 
	private ZMenu zMenu;
	private ZArticle zArticle;
	
	private List<ZArticle> articleList = new ArrayList<ZArticle> ();
	private List<ZMenu> menuList = new ArrayList<ZMenu> ();
	@Override
	public Object getModel(){
		return zArticle;
	}

	public String query() throws Exception{
		String sqlStr = " 1=1 ";
		//setMenulist(menuDaoImpl.findMenuByCon(sqlStr));
		menuList = menuDaoImpl.findMenuByCon(sqlStr);
		articleList = articleDaoImpl.queryByCon(sqlStr);
		return SUCCESS;
	}

	public List<ZArticle> getArticleList(){
		return articleList;
	}
	public void setArticleList(List<ZArticle> articleList){
		this.articleList = articleList;
	}

	public void setMenulist(List<ZMenu> menuList) {
		this.menuList = menuList;
	}

	public List<ZMenu> getMenuList() {
		return menuList;
	}

}
