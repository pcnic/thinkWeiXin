package com.linjj.thinkweixin.db;
import java.io.Serializable;
import java.sql.ResultSet;
//
/**
 * 调用Sql语句返回的结果集<br>
 * Title:提供调用Sql语句返回的结果集
 * Description:
 * Copyright:    Copyright (c) 2004
 * Company:      salary
 * @author ZRJ    2004-09-17 created
 * @version 1.0.0.0
 */

public class Result implements Serializable
{
  public int iErrorCode;             //结果描述码，非0表示错误
  public String sErrorDescriptor;    //错误描述
  public int iFieldCount;            //返回的字段数
  public int iActualNum;             //实际返回的记录数
  public int iSum;                   //总的记录数
  public String[] aLineOut;          //单行输出结果
  public String[][] aaMultiLinesOut; //多行输出结果
  public String sErrorDetail;        //详细错误信息
  public ResultSet rset;

  public Result() {
  }

}
