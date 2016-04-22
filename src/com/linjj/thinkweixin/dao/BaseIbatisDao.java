package com.linjj.thinkweixin.dao;

import java.util.List;
/**
 * 
 * @author linjj
 * DAO基类
 * @param <T>
 */
public interface BaseIbatisDao<T> {
	/**
	 * 插入
	 * @param sqlMapName  ibatis命名空间、id
	 * @param record      插入数据库的对象
	 */
	void insert(String sqlMapName,T record);
    /**
     * 更新
     * 有两个语句1.updateByObj 插入对象如果空对象不插入。2updateByExample 插入对象所以值
     * @param sqlMapName     ibatis命名空间、id
     * @param record         更新的对象
     */
	void updateByPrimaryKey(String sqlMapName,T record);
	/**
	 * 外部字段组合条件更新单个字段
	 * @param sqlMapName     ibatis命名空间、id  
	 * @param field_name1    更新字段名称  
	 * @param field_value1   更新字段值
	 * @param field_name2    更新条件的字段名称
	 * @param field_value2   更新条件的字段值
	 */
	void updateAttrByPK(String sqlMapName,String field_name, Object field_value,String field_name1, Object field_value1);
	/**
	 * 外部组合条件更新单个字段
	 * @param sqlMapName     ibatis命名空间、id  
	 * @param field_name     更新字段名称
	 * @param field_value    更新字段值
	 * @param condition      外部组合的更新条件
	 */
	void updateAttrByCondition(String sqlMapName,String field_name, Object field_value,String condition);
    /**
     * 查询所有
     * @param sqlMapName  ibatis命名空间、id
     * @return            返回list对象集
     */
	List findAll(String sqlMapName);
    /**
     * 根据传入的对象进行组合查询
     * @param sqlMapName   ibatis命名空间、id
     * @param example      把对象的属性用and连接起来组合成SQL语句，如果属性为null则不组合
     * @return             返回list对象集
     */
	List findByObj(String sqlMapName,Object example);
    /**
     * 外部组合查询条件成参数传入
     * @param sqlMapName   ibatis命名空间、id
     * @param condition    外部组合的查询条件
     * @return             返回list对象集
     */
	List findByCondition(String sqlMapName,String condition);
	/**
     * 外部组合查询条件成参数传入
     * @param sqlMapName   ibatis命名空间、id
     * @param condition    外部组合的查询条件
     * @return             返回list对象集
     */
	List findByCondition(String sqlMapName,String condition,String condition1);
    /**
     * 查询所有并排序
     * @param sqlMapName   ibatis命名空间、id
     * @param orderBy      排序值(order by已在xml写好不需要再写)
     * @return             返回list对象集
     */
	List findAll(String sqlMapName,String orderBy);
    /**
     * 外部组合查询条件并排序以参数传入
     * @param sqlMapName   ibatis命名空间、id
     * @param condition    外部组合的查询条件
     * @param orderBy      排序值(order by已在xml写好不需要再写)
     * @return             返回list对象集
     */
	List findByConditionOrder(String sqlMapName,String condition, String orderBy);
    /**
     * 外部组合查询条件并排序、分页都以参数传入
     * @param sqlMapName   ibatis命名空间、id
     * @param condition    外部组合的查询条件
     * @param orderBy      排序值(order by已在xml写好不需要再写)
     * @param startNum     开始行数
     * @param endNum       结束行数
     * @return             返回list对象集
     */
	List findByConditionPage(String sqlMapName,String condition, String orderBy, Integer startNum, Integer endNum);
	
	List findByCondition2Page(String sqlMapName,String condition,String condition2, String orderBy, Integer startNum, Integer endNum);
	List findByCondition3Page(String sqlMapName,String condition,String condition2,String condition3, String orderBy, Integer startNum, Integer endNum);
	/**
	 * 外部组合查询条件并排序、分页都以参数传入
	 * @param sqlMapName  ibatis命名空间、id
	 * @param value1      附加条件1
	 * @param value2      附加条件2
	 * @param condition   外部组合的查询条件
	 * @param orderBy     排序值(order by已在xml写好不需要再写)
	 * @param startNum    开始行数
	 * @param endNum      结束行数 
	 * @return            返回list对象集
	 */
	List findByConditionPageValues(String sqlMapName,String value,String value1, String condition, String orderBy, Integer startNum, Integer endNum);
    /**
     * 传入列名及列值查询对象
     * @param sqlMapName   ibatis命名空间、id
     * @param field_name   列名
     * @param field_value  列值
     * @return             返回对象(Object)
     */
	T findObjectByField(String sqlMapName,String field_name, Object field_value);
    /**
     * 外部组合条件查询对象
     * @param sqlMapName   ibatis命名空间、id
     * @param condition    外部组合的查询条件
     * @return             返回对象(Object)
     */
	T findObjectByCondition(String sqlMapName, String condition);
    /**
     * 传入列名及列值删除对象
     * @param sqlMapName   ibatis命名空间、id
     * @param field_name   列名
     * @param field_value  列值
     */
	void deleteByPrimaryKeyField(String sqlMapName,String field_name, Object field_value);
    /**
     * 外部组合条件删除对象
     * @param sqlMapName   ibatis命名空间、id
     * @param conditions   外部组合的查询条件
     */
	void deleteByCondition(String sqlMapName,String conditions);
    /**
     * 外部组合条件查询总数
     * @param sqlMapName  ibatis命名空间、id
     * @param conditions  外部组合的查询条件
     * @return
     */
	int countByCondition(String sqlMapName,String conditions);
	int countByCondition2(String sqlMapName,String conditions,String conditions2);
	/**
     * 外部组合条件查询总数
     * @param sqlMapName  ibatis命名空间、id
     * @param value1      附加条件1
	 * @param value2      附加条件2 
     * @param conditions  外部组合的查询条件
     * @return
     */
	int countByConditionValues(String sqlMapName,String value,String value1,String conditions);
	/**
	 * 获得自增ID号并且自动增加相应的数更新seqvalue字段
	 * @param tableName   获得自增ID的表名
	 * @param count       自增ID数为null或者0或者大于1
	 * @return            String 表当前最大ID值
	 */
	String getSequenceId(String tableName,Integer count);
	/**
	 * 外部传入自定义参数 Params
	 * @param tableName   ibatis命名空间、id
	 * @param params      Params自定义参数
	 * @return            List 
	 */
	List findListByParams(String sqlMapName,T params);
	/**
	 * 外部传入自定义参数 Params
	 * @param tableName   ibatis命名空间、id
	 * @param params      Params自定义参数
	 * @return            Object
	 */
	T findObjectByParams(String sqlMapName,T params);
	
}
