package com.linjj.thinkweixin.db;
import java.sql.Connection;
import java.util.Date;
/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: salary</p>
 * @author ZRJ    2004-09-17 created
 * @version 1.0.0.0
 */

public class DBConnObj
{
  private int iRefTimes=0;
  public Connection conn=null;
  public Date dGet=new Date();
  public  boolean bUseExternPool=false;
  //1:得到连接,-1:释放连接,0:判断该连接是否正在使用,-3:强制把连接设置为空闲
  public synchronized int setRef(int iFlag)
  {
    if(iFlag==1)iRefTimes++;
    if(iFlag==-1)
    {
      try
      {
        if((bUseExternPool)&&(conn!=null))conn.close();
      }
      catch(Exception ex){
      }
      iRefTimes--;
    }
    if(iFlag==-3)iRefTimes=0;
    return iRefTimes;
  }
}
