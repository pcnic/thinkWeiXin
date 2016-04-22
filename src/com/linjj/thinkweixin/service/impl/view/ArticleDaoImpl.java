package com.linjj.thinkweixin.service.impl.view;

import java.util.List;

import javax.annotation.Resource;

import com.linjj.thinkweixin.dao.BaseIbatisDao;
import com.linjj.thinkweixin.service.view.IArticleService;
import com.linjj.thinkweixin.vo.ZArticle;

public class ArticleDaoImpl implements IArticleService {
	@Resource
	private BaseIbatisDao baseIbatisDao;
	
	public void delete(ZArticle article) {
		// TODO Auto-generated method stub
		
	}

	public ZArticle queryAccountByID(String condition) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<ZArticle> queryByCon(String condition) {
		return baseIbatisDao.findByCondition("z_article.findAllbyConds", condition);
	}

	public void save(ZArticle article) {
		// TODO Auto-generated method stub
		
	}

	public void update(String condition) {
		// TODO Auto-generated method stub
		
	}}
