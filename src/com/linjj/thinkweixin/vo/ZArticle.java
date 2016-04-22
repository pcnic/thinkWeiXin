package com.linjj.thinkweixin.vo;

/**
 * @author pcnic
 *微信公众账号文章信息
 */
public class ZArticle extends BaseVo{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String getArticleId() {
		return articleId;
	}
	public void setArticleId(String articleId) {
		this.articleId = articleId;
	}
	public String getArticleName() {
		return articleName;
	}
	public void setArticleName(String articleName) {
		this.articleName = articleName;
	}
	public String getArticleInfo() {
		return articleInfo;
	}
	public void setArticleInfo(String articleInfo) {
		this.articleInfo = articleInfo;
	}
	public String getArticleType() {
		return articleType;
	}
	public void setArticleType(String articleType) {
		this.articleType = articleType;
	}
	public String getArticleCreateDate() {
		return articleCreateDate;
	}
	public void setArticleCreateDate(String articleCreateDate) {
		this.articleCreateDate = articleCreateDate;
	}
	private String articleId;
	private String articleName;
	private String articleInfo;
	private String articleType;
	private String articleCreateDate;
}
