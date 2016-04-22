package com.linjj.thinkweixin.db;
import java.util.Vector;

/**
 * Title:这是用来管理数据库连接池的类
 * Description:
 * Copyright:    Copyright (c) 2004
 * Company:      ZRJ    2004-09-17 created
 * @author salary
 * @version 1.0.0.0
 */

public class DBPoolManage
{
  private static Vector aPool=new Vector();  //连接池列表
  private static String sDriverName="";      //驱动器名称
  private static String sUrl="";             //连接串
  private static String sUsrName="";         //用户名
  private static String sPwd="";             //口令
  private static int iConnectNum=0;          //连接个数
  private static int iMaxNum=0;              //最大个数

  public static final String DEFAULT_POOL_NAME="Default Connect Pool Created By System";
  //public static boolean bDebug=false;
  public static boolean bDebug=true;

  private static boolean readConfig()
  {
    return false;
  }
  //创建连接池
  private static DBConnPool newPool(String sPoolName)
  {
    try
    {
      if((sPoolName==null)||(sPoolName.length()==0))sPoolName=DEFAULT_POOL_NAME;
      int iLen=aPool.size();
      for(int i=0;i<iLen;i++)
      {
        DBConnPool pool=(DBConnPool)(aPool.elementAt(i));
        if(pool.getPoolName().equalsIgnoreCase(sPoolName))
        { //如果已经建立直接返回
          return pool;
        }
      }
      DBConnPool pool=new DBConnPool();
      pool.setPoolName(sPoolName);
      aPool.add(pool);
      return pool;
    }
    catch(Exception e)
    {
      System.err.println("DBPoolManage.createPool()==>"+e.toString());
      return null;
    }
  }

  //根据指定名称,得到连接池
  public synchronized static DBConnPool getPool(String sPoolName)
  {
    try
    {
      if((sPoolName==null)||(sPoolName.length()==0))sPoolName=DEFAULT_POOL_NAME;
      int iLen=aPool.size();
      for(int i=0;i<iLen;i++)
      {
        DBConnPool pool= (DBConnPool)(aPool.elementAt(i));
        if(pool.getPoolName().equalsIgnoreCase(sPoolName))
        {
          if(pool.getCloseed())
          {
            aPool.remove(i);
            return null;
          }
          return pool;
        }
      }
      return null;
    }
    catch(Exception e)
    {
      System.err.println("DBPoolManage.getPool==>"+e.toString());
      return null;
    }
  }
  //创建指定名称的连接池
  public synchronized static DBConnPool createPool(String sPoolName)
  {
    return newPool(sPoolName);
  }

  //创建一个缺省的可用的连接池,它读取缺省配置文件==db.cfg
  public synchronized static DBConnPool defaultCreate()
  {
    try
    {
      if(!DBReadConfig.bRead)
      {
        if(!DBReadConfig.readConfigFile()) DBReadConfig.restartPool("",false);
      }
      DBConnPool pool=newPool(DEFAULT_POOL_NAME);
      if(pool.getConnectedNum()<=0)
      {
        readConfig();
        if(iConnectNum==0)return null;
        pool.setDriverName(sDriverName);
        pool.setUrl(sUrl);
        pool.setUsrName(sUsrName);
        pool.setPwd(sPwd);
        pool.setConnectNum(iConnectNum);
        pool.setConnMaxNum(iMaxNum);
        if(iConnectNum>0)pool.initPool();
      }
      return pool;
    }
    catch(Exception e)
    {
      System.err.println("DBPoolManage.defaultCreate()==>"+e.toString());
      return null;
    }
  }
  //关闭所有连接池
  public synchronized static boolean closeAllPool()
  {
    try
    {
      int iLen=aPool.size();
      DBReadConfig.bRead=false;
      for(int i=0;i<iLen;i++)
      {
        DBConnPool pool=(DBConnPool)(aPool.elementAt(i));
        if(!pool.closePool())
          throw new Exception("no way close pool named:"+pool.getPoolName());
      }
      aPool.clear();
      return true;
    }
    catch(Exception e)
    {
      System.err.println("DBPoolManage.closeAllPool()==>"+e.toString());
      return false;
    }
  }

  //关闭指定名称的连接池
  public synchronized static boolean closePool(String sPoolName)
  {
    try
    {
      if((sPoolName==null)||(sPoolName.length()==0))sPoolName=DEFAULT_POOL_NAME;
      int iLen=aPool.size();
      if(iLen==1)DBReadConfig.bRead=false;
      for(int i=0;i<iLen;i++)
      {
        DBConnPool pool=(DBConnPool)(aPool.elementAt(i));
        if(pool.getPoolName().equalsIgnoreCase(sPoolName))
        {
          if(pool.closePool())aPool.remove(i);
          else throw new Exception("no way close pool named:"+pool.getPoolName());
          break;
        }
      }
      return true;
    }
    catch(Exception e)
    {
       System.err.println("DBPoolManage.closePool()==>"+e.toString());
       return false;
    }
  }
}
