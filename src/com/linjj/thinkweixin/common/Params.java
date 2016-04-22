package com.linjj.thinkweixin.common;
/**
 * ibatis公共参数类
 * @author 钟晓勇
 *
 */
public class Params {
	//条件列名1
	String name;
	//条件列名1的值
	Object value;
    //条件列名2
	String name1;
    //条件列名2的值
	Object value1;
	//排序
	String order;
	//条件1
	String conditions;
	//条件2
	String conditions2;
	//条件2
	String conditions3;
	//分页参数 开始行
	Integer startNum;
	//分页参数结束行
	Integer endNum;
	public Integer getEndNum() {
		return endNum;
	}
	public void setEndNum(Integer endNum) {
		this.endNum = endNum;
	}
	public Integer getStartNum() {
		return startNum;
	}
	public void setStartNum(Integer startNum) {
		this.startNum = startNum;
	}
	public void clear() {
		this.name = null;
		this.value = null;
		this.name1 = null;
		this.value1 = null;
		this.order = null;
		this.conditions = null;
		this.conditions2 = null;
		this.startNum=null;
		this.endNum=null;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
	public String getOrder() {
		return order;
	}
	public void setOrder(String order) {
		this.order = order;
	}
	public String getConditions() {
		return conditions;
	}
	public void setConditions(String conditions) {
		this.conditions = conditions;
	}
	public String getName1() {
		return name1;
	}
	public void setName1(String name1) {
		this.name1 = name1;
	}
	public Object getValue1() {
		return value1;
	}
	public void setValue1(Object value1) {
		this.value1 = value1;
	}
	public String getConditions2() {
		return conditions2;
	}
	public void setConditions2(String conditions2) {
		this.conditions2 = conditions2;
	}
	public String getConditions3() {
		return conditions3;
	}
	public void setConditions3(String conditions3) {
		this.conditions3 = conditions3;
	}
}
