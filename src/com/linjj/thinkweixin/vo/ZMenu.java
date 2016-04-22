package com.linjj.thinkweixin.vo;

/**
 * 目录分类
 * @author pcnic
 *
 */
public class ZMenu extends BaseVo {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String menuId ;
	private String menuName;
	private String pMenuId;
	private String disport;
	private String isShow;
	private String createDate;
	private String link;
	
	public String getIsShow(){
		return isShow;
	}
	public void setIsShow(String isShow){
		this.isShow=isShow;
	}
	public String getMenuId(){
		return this.menuId; 
	}
	public void setMenuId(String menuId){
		this.menuId = menuId;
	}
	
	public String getMenuName(){
		return this.menuName;
	}
	public String getPMenuId() {
		return pMenuId;
	}
	public void setPMenuId(String menuId) {
		pMenuId = menuId;
	}
	public String getDisport() {
		return disport;
	}
	public void setDisport(String disport) {
		this.disport = disport;
	}
	public void setMenuName(String menuName){
		this.menuName = menuName;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public String getLink() {
		return link;
	}

}
