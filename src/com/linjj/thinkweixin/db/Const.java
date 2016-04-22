package com.linjj.thinkweixin.db;

/**
 * Title:定义常量
 * Description:
 * Copyright:    Copyright (c) 2004
 * Company:      salary
 * @author ZRJ    2004-09-17 created
 * @version 1.0.0.0
 */


import java.util.Hashtable;
public class Const
{
  public static Hashtable htErrorInfo = new Hashtable();

  /**
  * 功能描述: 取错误信息函数
  * @param   int nErrorID   :错误编码
  * @return  String 错误信息字符串
  * NOTE :所有在该单元有进行错误常量定义
  */
  public static String getErrInfo(int nErrorID)
  {
    Integer nID = new Integer(nErrorID);
    return (String)htErrorInfo.get(nID);
  }

  /**
  * 功能描述: 错误信息注册函数
  * @param   int nErrorID  :错误编码
  * @param   String sErrorInfo ：错误信息字符串
  */
  private static void addErrorInfo(int nErrorID,String sErrorInfo)
  {
    Integer nID = new Integer(nErrorID);
    if (null == htErrorInfo.get(nID))
       htErrorInfo.put(nID,sErrorInfo);
  }
  /*应用类型错误，常量前缀I_ERR_APP
    该部分错误信息用来描述应用代码可能出现的错误
  */
  public static final int I_ERR_APP_NOT_ALLOW=1;	    //不允许
  public static final int I_ERR_APP_SUCCESS=0;	            //成功或允许
  public static final int I_ERR_APP_SQL=-1;                 //Sql语句运行失败
  public static final int I_ERR_APP_NODATA_FOUND=-2;        //没找到记录
  public static final int I_ERR_APP_PARAM_FORMAT=-3;        //输入参数格式错误
  public static final int I_ERR_APP_SQL_PARSE=-4;           //分析Sql语句失败(用于nExecuteSql)
  public static final int I_ERR_APP_UPDATE=-5;              //更新记录失败
  public static final int I_ERR_APP_INSERT=-6;              //插入记录失败
  public static final int I_ERR_APP_DELETE=-7;              //删除记录失败

  public static final String LOG_LEVEL_0= "0";//日志级别0(严重错误错误级别)
  public static final String LOG_LEVEL_1= "1";//日志级别1(一般错误级别)
  public static final String LOG_LEVEL_2= "2";//日志级别2(警告级别)
  public static final String LOG_LEVEL_3= "3";//日志级别3(信息调试级别)

  /*注册错误信息*/
  static
  {
    htErrorInfo.clear();
    /*应用类型错误，常量前缀I_ERR_APP
      该部分错误信息用来描述应用代码可能出现的错误
    */
    addErrorInfo(I_ERR_APP_NOT_ALLOW,"不允许");
    addErrorInfo(I_ERR_APP_SUCCESS,"成功或允许");
    addErrorInfo(I_ERR_APP_SQL,"Sql语句运行失败");
    addErrorInfo(I_ERR_APP_NODATA_FOUND,"没找到记录");
    addErrorInfo(I_ERR_APP_PARAM_FORMAT,"输入参数格式错误");
    addErrorInfo(I_ERR_APP_SQL_PARSE,"分析Sql语句失败");
    addErrorInfo(I_ERR_APP_UPDATE,"更新记录失败");
    addErrorInfo(I_ERR_APP_INSERT,"插入记录失败");
    addErrorInfo(I_ERR_APP_DELETE,"删除记录失败");
  }
}
