// Decompiled by Jad v1.5.7. Copyright 1997-99 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/SiliconValley/Bridge/8617/jad.html
// Decompiler options: packimports(3)
// Source File Name:   MyDateBean.java
//ZRJ    2004-09-17 created

package com.linjj.thinkweixin.base;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MyDateBean
{
    private Date P_Date;

    public MyDateBean()
    {
        P_Date = new Date();
    }

    public MyDateBean(Date I_Date)
    {
        P_Date = I_Date;
    }

    public MyDateBean(java.sql.Date I_Date)
    {
        if(I_Date == null)
            P_Date = null;
        else
            P_Date = new Date(I_Date.getTime());
    }

    public MyDateBean(Time I_Time)
    {
        if(I_Time == null)
            P_Date = null;
        else
            P_Date = new Date(I_Time.getTime());
    }

    public MyDateBean(Timestamp I_Time)
    {
        if(I_Time == null)
            P_Date = null;
        else
            P_Date = new Date(I_Time.getTime());
    }

    public MyDateBean(int I_Year, int I_Month, int I_Day)
    {
        setDate(I_Year, I_Month, I_Day, 0, 0, 0);
    }

    public MyDateBean(int I_Year, int I_Month, int I_Day, int I_Hour, int I_Minute, int I_Second)
    {
        setDate(I_Year, I_Month, I_Day, I_Hour, I_Minute, I_Second);
    }

    public MyDateBean(String I_Date, String I_FormatString)
    {
        setFormatedTime(I_Date, I_FormatString);
    }

    public MyDateBean(String I_DateTime)
    {
        if(I_DateTime.length() == 8)
            setFormatedTime(I_DateTime, "yyyyMMdd");
        else
            setFormatedTime(I_DateTime, "yyyyMMddHHmmss");
    }

    public void setDate(int I_Year, int I_Month, int I_Day)
    {
        setDate(I_Year, I_Month, I_Day, 0, 0, 0);
    }

    public void setDate(int I_Year, int I_Month, int I_Day, int I_Hour, int I_Minute, int I_Second)
    {
        Calendar V_Calendar = Calendar.getInstance();
        V_Calendar.set(I_Year, I_Month - 1, I_Day, I_Hour, I_Minute, I_Second);
        P_Date = V_Calendar.getTime();
    }

    public void setDate(String I_Date, String I_FormatString)
    {
        setFormatedTime(I_Date, I_FormatString);
    }

    public void setSystemTime()
    {
        if(P_Date == null)
            P_Date = new Date(System.currentTimeMillis());
        else
            P_Date.setTime(System.currentTimeMillis());
    }

    public Date getUtilDate()
    {
        return P_Date;
    }

    public java.sql.Date getSqlDate()
    {
        if(P_Date == null)
            return null;
        else
            return new java.sql.Date(P_Date.getTime());
    }

    public Time getSqlTime()
    {
        if(P_Date == null)
            return null;
        else
            return new Time(P_Date.getTime());
    }

    public Timestamp getSqlTimestamp()
    {
        if(P_Date == null)
            return null;
        else
            return new Timestamp(P_Date.getTime());
    }

    public String getYear()
    {
        if(P_Date == null)
            return "";
        else
            return (new SimpleDateFormat("yyyy")).format(P_Date);
    }

    public int getIntYear()
    {
        if(P_Date == null)
            return 0;
        else{
          String sYear=this.getYear();
          return (Integer.parseInt(sYear));
        }
    }

    public String getMonth()
    {
        if(P_Date == null)
            return "";
        else
            return (new SimpleDateFormat("MM")).format(P_Date);
    }

    public int getIntMonth()
    {
        if(P_Date == null)
            return 0;
        else{
          String sMonth=this.getMonth();
          return (Integer.parseInt(sMonth));
        }
    }

    //取当前日期的季度
    public int getIntSeason(){
      if (P_Date==null){
        return 0;
      }else{
        int iSeason=0;
        if (this.getIntMonth()%3==0){
          iSeason=this.getIntMonth()/3;
        }else{
          iSeason=this.getIntMonth()/3+1;
        }
        return iSeason;
      }
    }

    //取当前日期所在季度的第一个月
    public int getSeasonFirstMonth(){
      if (P_Date==null){
        return 0;
      }else{
        int sMonth=(this.getIntSeason()-1)*3+1;
        return sMonth;
      }
    }

    //取当前日期所在季度的最后一天的日期
    public int getSeasonLastDay(){
      if (P_Date==null){
        return 0;
      }else{
        int sMonth=this.getSeasonFirstMonth();
        int iday=0;
        switch(sMonth){
          case 3:
            iday = 31;
            break;
          case 6:
            iday = 30;
            break;
          case 9:
            iday = 30;
            break;
          case 12:
            iday = 31;
            break;
        }
        return iday;
      }
    }

    public String getDay()
    {
        if(P_Date == null)
            return "";
        else
            return (new SimpleDateFormat("dd")).format(P_Date);
    }

    public int getIntDay()
    {
        if(P_Date == null)
            return 0;
        else{
          String sDay=this.getDay();
          return (Integer.parseInt(sDay));
        }
    }

    public String getDayOfWeek(){
      //根据日期，获取星期几
      if(P_Date == null)
        return "";
      else{
        Calendar cal = Calendar.getInstance();
        cal.setTime(P_Date);
        return String.valueOf(cal.get(Calendar.DAY_OF_WEEK)-1);
      }
    }

    public int getIntDayOfWeek(){
      //根据日期，获取星期几
      if(P_Date == null)
        return 0;
      else{
        Calendar cal = Calendar.getInstance();
        cal.setTime(P_Date);
        return cal.get(Calendar.DAY_OF_WEEK)-1;
      }
    }

    public int getDayOfYear()
    {
        if(P_Date == null)
            return 0;
        else{
          Calendar cal = Calendar.getInstance();
          cal.setTime(P_Date);
          return cal.get(Calendar.DAY_OF_YEAR);
        }
    }

    public int getDayOfMonth()
    {
        if(P_Date == null)
            return 0;
        else{
          Calendar cal = Calendar.getInstance();
          cal.setTime(P_Date);
          return cal.get(Calendar.DAY_OF_MONTH);
        }
    }

    public int getLastDayOfMonth()
    {
        if(P_Date == null)
            return 0;
        else{
          int[] monthDays = new int[]{31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
          int iyear=this.getIntYear();
          //瑞年
          if (((iyear%4)==0&&(iyear%100!=0))||(iyear%400==0)){
            monthDays[1]=29;
          }
          int imonth = this.getIntMonth();
          return monthDays[imonth];
        }
    }

    public int getDayOfWeekInMonth()
    {
        if(P_Date == null)
            return 0;
        else{
          Calendar cal = Calendar.getInstance();
          cal.setTime(P_Date);
          return cal.get(Calendar.DAY_OF_WEEK_IN_MONTH);
        }
    }

    public int getWeekOfMonth()
    {
        if(P_Date == null)
            return 0;
        else{
          Calendar cal = Calendar.getInstance();
          cal.setTime(P_Date);
          return cal.get(Calendar.WEEK_OF_MONTH);
        }
    }

    public int getWeekOfYear()
    {
        if(P_Date == null)
            return 0;
        else{
          Calendar cal = Calendar.getInstance();
          cal.setTime(P_Date);
          return cal.get(Calendar.WEEK_OF_YEAR);
        }
    }
    //一年中的最后一周
    public int getLastWeekNumInYear()
    {
        if(P_Date == null)
            return 0;
        else{
          MyDateBean myDate=new MyDateBean(P_Date);
          myDate.setDate(this.getIntYear(),12,31);
          int iWeekOfYear=myDate.getWeekOfYear();
          return iWeekOfYear;
        }
    }
    //一个季度中的最后一周在一年中的周数
    public int getLastWeekNumInSeason()
    {
        if(P_Date == null)
            return 0;
        else{
          int iSeasonFirstMonth=this.getSeasonFirstMonth();
          int iSeasonLastDay=this.getSeasonLastDay();
          MyDateBean lastMonthDay=new MyDateBean(this.P_Date);
          lastMonthDay.setDate(lastMonthDay.getIntYear(),iSeasonFirstMonth+2,iSeasonLastDay);
          int ilastWeekNum=lastMonthDay.getWeekOfYear();
          return ilastWeekNum;
        }
    }
    //一个季度中的第一周在一年中的周数
    public int getFirstWeekNumInSeason()
    {
        if(P_Date == null)
            return 0;
        else{
          int iSeasonFirstMonth=this.getSeasonFirstMonth();
          MyDateBean firstMonthDay=new MyDateBean(this.P_Date);
          firstMonthDay.setDate(firstMonthDay.getIntYear(),iSeasonFirstMonth,1);
          int ifirstWeekNum=firstMonthDay.getWeekOfYear();
          return ifirstWeekNum;
        }
    }

    //一个季度中的最后一周在一年中的周数
    public int getLastWeekNumInMonth()
    {
        if(P_Date == null)
            return 0;
        else{
          int iDay=this.getLastDayOfMonth();
          MyDateBean lastMonthDay=new MyDateBean(this.P_Date);
          lastMonthDay.setDate(lastMonthDay.getIntYear(),lastMonthDay.getIntMonth(),iDay);
          int ilastWeekNum=lastMonthDay.getWeekOfYear();
          return ilastWeekNum;
        }
    }

    public String getHour()
    {
        if(P_Date == null)
            return "";
        else
            return (new SimpleDateFormat("HH")).format(P_Date);
    }

    public String getMinute()
    {
        if(P_Date == null)
            return "";
        else
            return (new SimpleDateFormat("mm")).format(P_Date);
    }

    public String getSecond()
    {
        if(P_Date == null)
            return "";
        else
            return (new SimpleDateFormat("ss")).format(P_Date);
    }

    public void addYear(int I_Years)
    {
        if(P_Date == null)
        {
            return;
        }
        else
        {
            Calendar V_Calendar = Calendar.getInstance();
            V_Calendar.setTime(P_Date);
            V_Calendar.add(1, I_Years);
            P_Date = V_Calendar.getTime();
            return;
        }
    }

    public void addMonth(int I_Months)
    {
        if(P_Date == null)
        {
            return;
        }
        else
        {
            Calendar V_Calendar = Calendar.getInstance();
            V_Calendar.setTime(P_Date);
            V_Calendar.add(2, I_Months);
            P_Date = V_Calendar.getTime();
            return;
        }
    }

    public void addDay(int I_Days)
    {
        if(P_Date == null)
        {
            return;
        }
        else
        {
            Calendar V_Calendar = Calendar.getInstance();
            V_Calendar.setTime(P_Date);
            V_Calendar.add(5, I_Days);
            P_Date = V_Calendar.getTime();
            return;
        }
    }

    public void addHour(int I_Hours)
    {
        if(P_Date == null)
        {
            return;
        }
        else
        {
            Calendar V_Calendar = Calendar.getInstance();
            V_Calendar.setTime(P_Date);
            V_Calendar.add(10, I_Hours);
            P_Date = V_Calendar.getTime();
            return;
        }
    }

    public void addMinute(int I_Minutes)
    {
        if(P_Date == null)
        {
            return;
        }
        else
        {
            Calendar V_Calendar = Calendar.getInstance();
            V_Calendar.setTime(P_Date);
            V_Calendar.add(12, I_Minutes);
            P_Date = V_Calendar.getTime();
            return;
        }
    }

    public void addSecond(int I_Seconds)
    {
        if(P_Date == null)
        {
            return;
        }
        else
        {
            Calendar V_Calendar = Calendar.getInstance();
            V_Calendar.setTime(P_Date);
            V_Calendar.add(13, I_Seconds);
            P_Date = V_Calendar.getTime();
            return;
        }
    }

    public int betweenYear(Date I_Date)
    {
        if(P_Date == null || I_Date == null)
        {
            return 0;
        }
        else
        {
            SimpleDateFormat V_YearUtl = new SimpleDateFormat("yyyy");
            int V_Years = Integer.parseInt(V_YearUtl.format(P_Date)) - Integer.parseInt(V_YearUtl.format(I_Date));
            return V_Years;
        }
    }

    public int betweenMonth(Date I_Date)
    {
        if(P_Date == null || I_Date == null)
        {
            return 0;
        }
        else
        {
            SimpleDateFormat V_YearUtl = new SimpleDateFormat("yyyy");
            SimpleDateFormat V_MonthUtl = new SimpleDateFormat("yyyy");
            int V_Years = Integer.parseInt(V_YearUtl.format(P_Date)) - Integer.parseInt(V_YearUtl.format(I_Date));
            int V_Months = Integer.parseInt(V_MonthUtl.format(P_Date)) - Integer.parseInt(V_MonthUtl.format(I_Date));
            return V_Years * 12 + V_Months;
        }
    }

    public int betweenDay(Date I_Date)
    {
        if(P_Date == null || I_Date == null)
            return 0;
        else
            return (int)((P_Date.getTime() - I_Date.getTime()) / (long)0x5265c00);
    }

    public long betweenHour(Date I_Date)
    {
        if(P_Date == null || I_Date == null)
            return 0L;
        else
            return (P_Date.getTime() - I_Date.getTime()) / (long)0x36ee80;
    }

    public long betweenMinute(Date I_Date)
    {
        if(P_Date == null || I_Date == null)
            return 0L;
        else
            return (P_Date.getTime() - I_Date.getTime()) / (long)60000;
    }

    public long betweenSecond(Date I_Date)
    {
        if(P_Date == null || I_Date == null)
            return 0L;
        else
            return (P_Date.getTime() - I_Date.getTime()) / (long)1000;
    }

    public String Format(String I_FormatString)
    {
        if(P_Date == null)
            return null;
        else
            return (new SimpleDateFormat(I_FormatString)).format(P_Date);
    }

    private void setFormatedTime(String I_Date, String I_Format)
    {
        if(I_Date == null || I_Format == null || I_Date.trim().equals("") || I_Format.trim().equals(""))
        {
            P_Date = null;
            return;
        }
        int V_Year = 0;
        int V_Month = 0;
        int V_Day = 0;
        int V_Hour = 0;
        int V_Minute = 0;
        int V_Second = 0;
        int V_DateLen = I_Date.length();
        int V_Pos = I_Format.indexOf("yyyy");
        if(V_Pos >= 0)
            V_Year = Integer.parseInt(I_Date.substring(V_Pos, V_Pos + 4));
        V_Pos = I_Format.indexOf("MM");
        if(V_Pos >= 0)
            V_Month = Integer.parseInt(I_Date.substring(V_Pos, V_Pos + 2));
        V_Pos = I_Format.indexOf("dd");
        if(V_Pos >= 0)
            V_Day = Integer.parseInt(I_Date.substring(V_Pos, V_Pos + 2));
        V_Pos = I_Format.indexOf("HH");
        if(V_Pos >= 0)
            V_Hour = Integer.parseInt(I_Date.substring(V_Pos, V_Pos + 2));
        V_Pos = I_Format.indexOf("mm");
        String V_FieldType = "\u5206";
        if(V_Pos >= 0)
            V_Minute = Integer.parseInt(I_Date.substring(V_Pos, V_Pos + 2));
        V_Pos = I_Format.indexOf("ss");
        V_FieldType = "\u79D2";
        if(V_Pos >= 0)
            V_Second = Integer.parseInt(I_Date.substring(V_Pos, V_Pos + 2));
        setDate(V_Year, V_Month, V_Day, V_Hour, V_Minute, V_Second);
    }
    //得到时间
    public static long getLongTime()
    {
        Date aDate = new Date();
        long atime = aDate.getTime();
        return atime;
    }
    //得到毫秒数
    public static long getaLongTime()
    {
        return System.currentTimeMillis();
    }
}
