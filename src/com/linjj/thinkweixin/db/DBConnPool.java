package com.linjj.thinkweixin.db;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.util.Date;
import java.util.Vector;

import javax.naming.InitialContext;
/**
 * Title:这个是数据库连接池类
 * Description:
 * Copyright:    Copyright (c) 2004
 * Company:      ZRJ    2004-09-17 created
 * @author salary
 * @version 1.0.0.0
 */

public class DBConnPool
{
  private String sDriverName="";//驱动程序名称
  private String sUrl="";       //连接串
  private int iTimeOut=3;       //默认sql执行超时时间设置,单位秒
  private Driver driver=null;   //驱动程序
  private javax.sql.DataSource ds=null;
  private Vector aConn=null;    //数据库连接列表
  private String sPwd="";       //口令
  private String sUsrName="";   //用户
  private int iConnectNum=0;    //连接个数
  private int iMaxNum=0;        //最大连接个数
  private String sDataBaseType="ORACLE";//数据库类型
  private String sConnectPoolName="";
  private String sError="Connect have not been Init";
  private boolean bSuccess=false;
  private boolean bClose=false;
  private boolean bOracle=false;
  public boolean bUseExternPool=false;//是否使用外部的连接池

  public java.sql.Connection getConnectByExtern()
  {
    try
    {
      if (DBReadConfig.isLock())
      {
        System.err.println("DB Pool Connect is Locked!");
        return null;
      }
      InitialContext jndiContext = new InitialContext();
      ds = (javax.sql.DataSource) jndiContext.lookup(sUrl);
      java.sql.Connection conn=null;
      conn=ds.getConnection();
      if(conn==null) System.err.println("get extern DB Pool Connect Fail!");
      return  conn;
    }
    catch (Exception ex)
    {
      ds=null;
      System.err.println("get extern DB Pool Connect Fail:"+ex.toString());
      return null;
    }
  }


  private  java.sql.Connection getConnectByLocal()
  {
    try
    {
       java.sql.Connection conn=DriverManager.getConnection(sUrl,sUsrName,sPwd);
       if(conn==null)System.err.println("get local DB Pool Connect Fail!");
       return conn;
    }
    catch (Exception ex)
   {
      System.err.println("get local DB Pool Connect Fail:"+ex.toString());
      return null;
    }
  }


  private DBConnObj newOneConnect()
  {
    try
    {
      bSuccess=true;
      int iLen=aConn.size();
      if((iMaxNum<=iLen)||(DBReadConfig.isLock()))return null;

      Connection conn=null;
      if(!bUseExternPool)conn=getConnectByLocal();
      else conn=getConnectByExtern();

      DBConnObj conobj=new DBConnObj();
      conobj.bUseExternPool=bUseExternPool;
      conobj.conn=conn;
      aConn.add(conobj);  //放到连接池里
      if(DBPoolManage.bDebug) System.err.println("Create one Connect to "+sUrl+" Success!");
      return conobj;
    }
    catch (Exception ex)
    {
      bSuccess=false;
      sError="DBConnPool.newOneConnect()==>"+ex.toString();
      System.err.println(sError);
      return null;
    }
  }

  private boolean openConnects()
  {
    try
    {
       bSuccess=true;
       if((sDriverName==null)||(sDriverName.length()==0))
       {
          bUseExternPool = true;
          driver=null;
       }
       else
       {
          driver = (Driver) Class.forName(sDriverName).newInstance();
          DriverManager.registerDriver(driver);
          bUseExternPool=false;
       }
       if(iConnectNum==0)iConnectNum=1;

       for(int i=0;i<iConnectNum;i++)
       {
          DBConnObj conobj=new DBConnObj();
          conobj.bUseExternPool=bUseExternPool;
          conobj.dGet=new Date();
          if(!bUseExternPool)
          {
            conobj.conn = getConnectByLocal();
            if(conobj.conn==null)bSuccess=false;
          }
          else conobj.conn=null;
          aConn.add(conobj); //放到连接池里
       }

       return bSuccess;
    }
    catch(Exception e)
    {
      bSuccess=false;
      sError="DBConnPool.openConnects()==>"+e.toString();
      System.err.println(sError);
      return false;
    }
  }

  private boolean closeConnects()
  {
    try
    {
      int iLen=aConn.size();

      boolean bError=false;
      for(int i=0;i<iLen;i++)
      {
        DBConnObj conobj=(DBConnObj)(aConn.elementAt(i));
        if(conobj.setRef(0)<=0)
        {
          try
          {
            if(!bUseExternPool) conobj.conn.close();
          }
          catch (Exception ex2)
          {
          }
        }
        else if(!bUseExternPool)conobj.setRef(-1);
      }
      aConn.clear();
      bSuccess=true;
      return true;
    }
    catch (Exception ex)
    {
      bSuccess=false;
      sError="DBConnPool.closeConnects()==>"+ex.toString();
      System.err.println(sError);
      return false;
    }

  }


  public synchronized boolean initPool()
  {
    return openConnects();
  }

  public synchronized boolean closePool()
  {
    bClose=true;
    return closeConnects();
  }


  private void adjustConnPool()
  {
    try
    {
      if(!bUseExternPool)
      {
        int iLen = aConn.size();
        for (int i = iLen-1;i >=0;i--)
        {
          DBConnObj connobj2 = (DBConnObj) (aConn.elementAt(i));
          if((connobj2.conn==null)||((connobj2.conn.isClosed()) &&(connobj2.setRef(0)<=0)))
          {
            connobj2.setRef(-3);
            connobj2.dGet = new Date();
            connobj2.conn=getConnectByLocal();
            if (DBPoolManage.bDebug)
              System.err.println("one connect in pool:[" + getPoolName() +
                                "] have been closed,now restart to connect!");
          }
        }
      }
    }
    catch(Exception e)
    {
    }
  }

  //获取一个还没被使用的连接对象
  private DBConnObj getConnObj()
  {
    try
    {
      DBConnObj connobj=null;
      int iLen=aConn.size();
      for(int i=iLen-1;i>=0;i--)
      {
        connobj=(DBConnObj)(aConn.elementAt(i));
        if(connobj.setRef(0)==0)
        {
          //如果是使用外部,则取一个连接
          if(bUseExternPool)connobj.conn=getConnectByExtern();
          //显示当前使用的连接索引
          if(DBPoolManage.bDebug) System.err.println("当前连接索引 = " +i);
          break;
        }
        else connobj=null;
      }

      if(connobj!=null)
      {
        connobj.dGet=new Date();
        connobj.setRef(1);
      }
      return connobj;
    }
    catch (Exception ex)
    {
     return null;
    }

  }

  private DBConnObj newConnObj()
  {
    try
    {
       DBConnObj connobj=newOneConnect();
       if(connobj==null)return null;
       connobj.setRef(1);
       connobj.dGet=new Date();
       return connobj;
    }
    catch (Exception ex)
    {
      return null;
    }
  }

  public synchronized DBConnObj getConnect()
  {
    try
    {
      if(bClose)
        throw new Exception("this pool startting close");

      if(DBReadConfig.isLock())
        throw new Exception("DB Pool Connect is Locked!");

      bSuccess = true;
      if (aConn.size() <= 0)
        return newConnObj();
      else
      {
        adjustConnPool();
        DBConnObj connobj = getConnObj();

        if (connobj != null)
        {
          if (DBPoolManage.bDebug) System.err.println("get one connect to use");
          return connobj;
        }
        //系统当前没有可用的连接,生成一个新的连接
        return newConnObj();
      }
    }
    catch (Exception ex)
    {
      bSuccess=false;
      sError="DBConnPool.getConnect()==>"+ex.toString();
      System.err.println(sError);
      return null;
    }
  }

  public DBConnPool()
  {
    aConn=new Vector();
    bSuccess=true;
  }

  public void setPoolName(String sPoolName)
  {
    sConnectPoolName=sPoolName;
  }

  public String getPoolName()
  {
    return sConnectPoolName;
  }

  public void setConnectNum(int iConnNum)
  {
    if(iConnNum<=0) iConnNum=1;
    iConnectNum=iConnNum;
  }

  public int getConnectNum(int iConnNum)
  {
   return iConnectNum;
  }

  public void setConnMaxNum(int iNum)
  {
    if(iNum<iConnectNum)iNum=iConnectNum;
    iMaxNum=iNum;
  }

  public int getConnMaxNum()
  {
     return iMaxNum;
  }

  public void setDriverName(String sDrvName)
  {
    sDriverName=sDrvName;
    if(sDataBaseType.indexOf("ORACLE")!=-1)bOracle=true;
  }

  public String getDriverName()
  {
    return sDriverName;
  }

  public void setUrl(String sConnstr)
  {
    sUrl=sConnstr;
  }

  public String getUrl()
  {
    return sUrl;
  }

  public void setUsrName(String sUserName)
  {
    sUsrName=sUserName;
  }

  public String getUsrName()
  {
    return sUsrName;
  }

  public void setPwd(String sPassword)
  {
    sPwd=sPassword;
  }

  public String getPwd()
  {
    return sPwd;
  }

  public String getDataBaseType()
  {
    return sDataBaseType;
  }

  public void setDataBaseType(String sDataBaseType)
  {
    this.sDataBaseType=sDataBaseType;
  }

  public boolean getSuccess()
  {
    return bSuccess;
  }

  public String getErrorMsg()
  {
    return sError;
  }


  public int getConnectedNum()
  {
    return aConn.size();
  }

  public String[][] getPoolStatus()
  {
    String[][] aaResult=null;
    try
    {
      Vector aMonConn=(Vector)(aConn.clone());
      int iLen=aMonConn.size();
      if(iLen>0)
      {
        aaResult=new String[iLen][2];
        for(int i=0;i<iLen;i++)
        {
          DBConnObj connobj=(DBConnObj)(aMonConn.elementAt(i));
          switch (connobj.setRef(0))
          {
           case 0:
             aaResult[i][0]="空闲";
             aaResult[i][1]="上次使用时间:"+connobj.dGet.toString();
             break;
           case -1:
             aaResult[i][0]="关闭";
             aaResult[i][1]="关闭时间:"+connobj.dGet.toString();
             break;
           default:
             aaResult[i][0]="活动";
             aaResult[i][1]="开始使用时间:"+connobj.dGet.toString();
             break;
          }
        }
      }
      else
      {
        aaResult=new String[1][2];
        aaResult[0][0]="无任何连接";
        aaResult[0][1]=(new Date()).toString();
      }
      return aaResult;
    }
    catch (Exception ex)
    {
      aaResult=new String[1][2];
      aaResult[0][0]="错误";
      aaResult[0][1]=ex.toString();
      return aaResult;
    }
  }

  public boolean getCloseed()
  {
    return bClose;
  }

  public boolean isOracle()
  {
    try
    {
      if(sDataBaseType.toUpperCase().indexOf("ORACLE")>=0)return true;
      else return false;
    }
    catch (Exception ex)
    {
     return false;
    }
  }

  public boolean isSybase()
  {
    try
    {
      if(sDataBaseType.toUpperCase().indexOf("SYBASE")>=0)return true;
      else return false;
    }
    catch (Exception ex)
    {
      return false;
    }
  }

  public int getTimeOut()
  {
    return iTimeOut;
  }

  public void setTimeOut(int iTimeOut)
  {
    this.iTimeOut=iTimeOut;
  }
}
