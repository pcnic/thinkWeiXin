package com.linjj.thinkweixin.dao.impl;

import java.util.List;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.linjj.thinkweixin.common.Params;
import com.linjj.thinkweixin.dao.BaseIbatisDao;
import com.linjj.thinkweixin.vo.ZSequence;

public class BaseIbatisDaoImpl extends SqlMapClientDaoSupport implements BaseIbatisDao {

	Params params=null;
	BaseIbatisDaoImpl(){
		params=new Params();
	}
	public int countByCondition(String sqlMapName, String conditions) {
		params.clear();
		params.setConditions(conditions);
		Integer count = (Integer)  getSqlMapClientTemplate().queryForObject(sqlMapName, params);
        return count.intValue();
	}
	public int countByCondition2(String sqlMapName, String conditions, String conditions2) {
		params.clear();
		params.setConditions(conditions);
		params.setConditions2(conditions2);
		Integer count = (Integer)  getSqlMapClientTemplate().queryForObject(sqlMapName, params);
        return count.intValue();
	}
	public int countByConditionValues(String sqlMapName, String value, String value1, String conditions) {
		params.clear();
		params.setValue(value);
		params.setValue1(value1);
		params.setConditions(conditions);
		Integer count = (Integer)  getSqlMapClientTemplate().queryForObject(sqlMapName, params);
        return count.intValue();
	}
	
	public void deleteByCondition(String sqlMapName, String conditions) {
		params.clear();
		params.setConditions(conditions);
		getSqlMapClientTemplate().delete(sqlMapName, params);
	}
	public void deleteByPrimaryKeyField(String sqlMapName, String field_name, Object field_value) {
		params.clear();
		params.setName(field_name);
		params.setValue(field_value);
		getSqlMapClientTemplate().delete(sqlMapName, params);
	}
	public List findAll(String sqlMapName) {
		return getSqlMapClientTemplate().queryForList(sqlMapName);
	}
	public List findAll(String sqlMapName, String orderBy) {
		params.clear();
		params.setOrder(orderBy);
		return getSqlMapClientTemplate().queryForList(sqlMapName,params);
	}
	public List findByConditionPage(String sqlMapName, String condition, String orderBy, Integer startNum, Integer endNum) {
		
		if (startNum == 0) {
			startNum = 1;
		}else{
			startNum=startNum/endNum+1;
		}
		Integer start=endNum * (startNum - 1) + 1;
		Integer end=endNum * startNum;
		params.clear();
		params.setConditions(condition);
		params.setOrder(orderBy);
		params.setStartNum(start);
		params.setEndNum(end);		
		return getSqlMapClientTemplate().queryForList(sqlMapName, params);
	}
	public List findByCondition2Page(String sqlMapName, String condition,String condition2, String orderBy, Integer startNum, Integer endNum) {
		
		if (startNum == 0) {
			startNum = 1;
		}else{
			startNum=startNum/endNum+1;
		}
		Integer start=endNum * (startNum - 1) + 1;
		Integer end=endNum * startNum;
		params.clear();
		params.setConditions(condition);
		params.setConditions2(condition2);
		params.setOrder(orderBy);
		params.setStartNum(start);
		params.setEndNum(end);
		return getSqlMapClientTemplate().queryForList(sqlMapName, params);
	}
	public List findByCondition3Page(String sqlMapName, String condition,String condition2,String condition3, String orderBy, Integer startNum, Integer endNum) {
		
		if (startNum == 0) {
			startNum = 1;
		}else{
			startNum=startNum/endNum+1;
		}
		Integer start=endNum * (startNum - 1) + 1;
		Integer end=endNum * startNum;
		params.clear();
		params.setConditions(condition);
		params.setConditions2(condition2);
		params.setConditions3(condition3);
		params.setOrder(orderBy);
		params.setStartNum(start);
		params.setEndNum(end);
		return getSqlMapClientTemplate().queryForList(sqlMapName, params);
	}
	public List findByConditionPageValues(String sqlMapName, String value, String value1, String condition, String orderBy, Integer startNum, Integer endNum) {
		if (startNum == 0) {
			startNum = 1;
		}else{
			startNum=startNum/endNum+1;
		}
		Integer start=endNum * (startNum - 1) + 1;
		Integer end=endNum * startNum;
		params.clear();
		params.setValue(value);
		params.setValue1(value1);
		params.setConditions(condition);
		params.setOrder(orderBy);
		params.setStartNum(start);
		params.setEndNum(end);
		return getSqlMapClientTemplate().queryForList(sqlMapName, params);
	}
	public Object findObjectByField(String sqlMapName, String field_name, Object field_value) {
		params.clear();
		params.setName(field_name);
		params.setValue(field_value);
		return getSqlMapClientTemplate().queryForObject(sqlMapName,params);
	}
	public Object findObjectByCondition(String sqlMapName, String condition) {
		params.clear();
		params.setConditions(condition);
		return getSqlMapClientTemplate().queryForObject(sqlMapName,params);
	}
	public List findByCondition(String sqlMapName, String condition) {
		params.clear();
		params.setConditions(condition);
		return getSqlMapClientTemplate().queryForList(sqlMapName, params);
	}
	public List findByCondition(String sqlMapName, String condition,String condition1) {
		params.clear();
		params.setConditions(condition);
		params.setConditions2(condition1);
		return getSqlMapClientTemplate().queryForList(sqlMapName, params);
	}
	public List findByConditionOrder(String sqlMapName, String condition, String orderBy) {
		params.clear();
		params.setConditions(condition);
		params.setOrder(orderBy);
		return getSqlMapClientTemplate().queryForList(sqlMapName, params);
	}
	public List findByObj(String sqlMapName, Object example) {
		 List list = getSqlMapClientTemplate().queryForList(sqlMapName, example);
	     return list;
	}
	public List findListByParams(String sqlMapName, Object params) {
		return getSqlMapClientTemplate().queryForList(sqlMapName, params);
	}
	public Object findObjectByParams(String sqlMapName, Object params) {
		return getSqlMapClientTemplate().queryForObject(sqlMapName,params);
	}
	public void insert(String sqlMapName, Object record) {
		getSqlMapClientTemplate().insert(sqlMapName, record);
	}
	public void updateByPrimaryKey(String sqlMapName, Object record) {
		getSqlMapClientTemplate().update(sqlMapName, record);
	}
	public void updateAttrByCondition(String sqlMapName, String field_name, Object field_value, String condition) {
		params.clear();
		params.setName(field_name);
		params.setValue(field_value);
		params.setConditions(condition);
		getSqlMapClientTemplate().update(sqlMapName, params);
	}
	public void updateAttrByPK(String sqlMapName, String field_name, Object field_value, String field_name1, Object field_value1) {
		params.clear();
		params.setName(field_name);
		params.setValue(field_value);
		params.setName1(field_name1);
		params.setValue1(field_value1);
		getSqlMapClientTemplate().update(sqlMapName, params);
	}
	public synchronized String getSequenceId(String tableName,Integer count){
		params.clear();
		params.setName("TNAME");
		params.setValue(tableName);
		ZSequence zSequence=(ZSequence) getSqlMapClientTemplate().queryForObject("z_sequence.findByPk",params);
		String strSeqval=zSequence.getSEQVALUE();
		if(strSeqval==null||"".equals(strSeqval)){
			strSeqval="0";
		}
		Integer intSeqval=Integer.valueOf(strSeqval);
		if(count==null){
			intSeqval++;
		}else if(count==0){
			intSeqval++;
		}else{
			intSeqval=intSeqval+count;
		}
		params.clear();
		params.setName("TNAME");
		params.setValue(tableName);
		params.setName1("SEQVALUE");
		params.setValue1(intSeqval);
		getSqlMapClientTemplate().update("z_sequence.updateAttrByPK", params);
		return intSeqval.toString();
	}	
}
