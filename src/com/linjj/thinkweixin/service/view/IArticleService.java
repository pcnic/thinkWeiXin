package com.linjj.thinkweixin.service.view;

import java.util.List;

import com.linjj.thinkweixin.vo.ZArticle;

public interface IArticleService {
	
	public List<ZArticle> queryByCon(String condition);
	public void update(String condition);
	public void delete(ZArticle zArticle);
	public void save(ZArticle zArticle);
	public ZArticle queryAccountByID(String condition);

	
	
	
	
	
}
