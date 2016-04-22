package com.linjj.thinkweixin.db;
import java.io.InputStream;

import com.linjj.thinkweixin.util.ExtString;
import com.linjj.thinkweixin.util.XmlParse;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: salary</p>
 * @author ZRJ    2004-09-17 created
 * @version 1.0
 */

public class DBReadConfig
{

  private static final String SYS_CONFIG_FILE="dbcfn.xml";//数据源配置文件
  private static String sConfigFileName=null;
  private static String sContent=null;
  public static boolean bRead=false;
  private static boolean bLock=false;

  public DBReadConfig()
  {
  }

  public synchronized static boolean readConfigFile()
  {
    try
    {
      if(sContent==null)
      {
        System.err.println("Config XML FileName is Null,Try to Find File...");
        DBReadConfig dbcfg=new DBReadConfig();
        ClassLoader cl=dbcfg.getClass().getClassLoader();

        InputStream input=cl.getResourceAsStream(SYS_CONFIG_FILE);
        sConfigFileName=cl.getResource(SYS_CONFIG_FILE).toString();
        cl=null;

        int iNum=input.available();
        byte[] aContent=new byte[iNum];
        input.read(aContent);
        input.close();
        sContent=new String(aContent);
      }

      System.err.println("Read Config XML File:["+sConfigFileName+"]");

      restartPool("",true); //启动数据库连接

      bRead=true;
      return true;
    }
    catch (Exception ex)
    {
      System.out.println("readConfigFile()==>"+ex.toString());
      sContent=null;
      bRead=false;
      return false;
    }
  }

  //bAll 启动所有连接池(有多个),否则只启动指定名称的连接池
  public synchronized static boolean restartPool(String PoolName,boolean bAll)
  {
    String pwd="";
    try
    {
      XmlParse xml=new XmlParse();
      if(!xml.bGetXML(sContent))
      {
        System.out.println(xml.ErrMsg);
        return false;
      }

      if((PoolName==null)||(PoolName.trim().length()==0))
      PoolName="Default Connect Pool Created By System";

      int iNum=xml.GetNameNum("pool");  //得到数据库连接池的个数
      int iWaitTimes=0;

      boolean bClose=false;
      if(bAll)bClose=DBPoolManage.closeAllPool();
      else bClose=DBPoolManage.closePool(PoolName);

      lock();
      while(!bClose)
      { //尝试关闭连接池
        Thread.sleep(1000);
        iWaitTimes++;
        if(iWaitTimes>=10)break;
        if(bAll)bClose=DBPoolManage.closeAllPool();
        else bClose=DBPoolManage.closePool(PoolName);
      }
      unlock();
      if(bAll)System.err.println("Try to Start All Pool...");
      else System.err.println("Try to Start Pool Named ["+PoolName+"]...");

      for(int i=0;i<iNum;i++)
      {
        //调试信息
        DBPoolManage.bDebug=xml.sGetValueByName("debug",i).equalsIgnoreCase("true");

        String sPoolname=xml.sGetValueByName("name",i);
        if((!sPoolname.equals(PoolName))&&(!bAll))continue;

        DBConnPool pool=DBPoolManage.createPool(sPoolname);
        pool.setDriverName(xml.sGetValueByName("drivername",i));
        pool.setUsrName(xml.sGetValueByName("user",i));
        pwd=xml.sGetValueByName("pwd",i);

        if(DBPoolManage.bDebug) System.err.println("PWD in Config="+pwd);
        pwd=ExtString.Encrypt(pwd,false);
        if(DBPoolManage.bDebug) System.err.println("PWD in Config1="+pwd);

        pool.setPwd(pwd);
        pool.setUrl(xml.sGetValueByName("url",i));
        pool.setPoolName(xml.sGetValueByName("name",i));
        try
        {
          pool.setConnMaxNum(Integer.parseInt(xml.sGetValueByName("max", i)));
          pool.setConnectNum(Integer.parseInt(xml.sGetValueByName("min", i)));
          pool.setTimeOut(Integer.parseInt(xml.sGetValueByName("time_out", i)));
        }
        catch(Exception ex2)
        {
        }

        pool.setDataBaseType(xml.sGetValueByName("databasetype"));
        if(pool.initPool()) //创建指定名称的连接池
        {
          StringBuffer sbDebug = new StringBuffer();
          sbDebug.append("Create Connect Pool Named [" + pool.getPoolName() +
                        "]\r\n");
          sbDebug.append("Pool Named [" + pool.getPoolName() + "] URL is [" +
                        pool.getUrl() + "]\r\n");
          sbDebug.append("Pool Named [" + pool.getPoolName() +
                        "] Init Connect Num is [" + pool.getConnectedNum() +
                        "]\r\n");
          sbDebug.append("Pool Named [" + pool.getPoolName() +
                        "] Max Connect Num is [" + pool.getConnMaxNum() +
                        "]\r\n");
          System.out.println(new String(sbDebug));
        }
        else
        {
          System.out.println("Connect Pool ["+xml.sGetValueByName("name",i)+"]Created Fail!" );
        }
      }
      return true;
    }
    catch (Exception ex)
    {
      System.out.println("restartPool()==>"+ex.toString());
      unlock();
      return false;
    }
  }


  public static void freeClassEvent()
  {
    try
    {
      DBPoolManage.closeAllPool();
      System.out.println("JDBC Pool is Closed");
    }
    catch (Exception ex)
    {
      System.out.println("JDBC Pool is Closed:"+ex.toString());
    }
  }

  public static boolean isLock()
  {
    return bLock;
  }

  public static void lock()
  {
    bLock=true;
  }

  public static void unlock()
  {
    bLock=false;
  }
}
