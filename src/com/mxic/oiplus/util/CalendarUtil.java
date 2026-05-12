package com.mxic.oiplus.util;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */
import java.util.*;

public class CalendarUtil {
  private Calendar cal;
  private String year;
  private String month;
  private String day;
  private String hours;
  private String minute;
  private String second;
  private String week;
  private String short_week;
  private String week_of_month;
  private String week_of_year;
  private boolean aboutTime = false;

  public CalendarUtil(String date, String format) {
    try{
      cal = Calendar.getInstance();
      Date cdate = new Date((DateUtil.parseTimestamp(date, format)).getTime());
      cal.setTime(cdate);
    } catch (Exception ex){
      ex.printStackTrace();
    }
  }

  public CalendarUtil(String date) {
    try{
      String format = "yyyy-MM-dd";
      if (date != null && date.indexOf(":") > 7){
        format = "yyyy-MM-dd HH:mm:ss";
        aboutTime = true;
      }
      cal = Calendar.getInstance();
      Date cdate = new Date((DateUtil.parseTimestamp(date, format)).getTime());
      cal.setTime(cdate);
    } catch (Exception ex){
      ex.printStackTrace();
    }
  }

  public CalendarUtil() {
    cal = Calendar.getInstance();
    cal.setTime(new Date(System.currentTimeMillis()));
  }

  public void setTime(String date){
    try {
      Date cdate = new Date((DateUtil.parseTimestamp(date, "dd-MM-yyyy HH:mm:ss")).getTime());
      cal.setTime(cdate);
    } catch (Exception ex){
      ex.printStackTrace();
    }
  }

  private void setWeek(){
    int i = cal.get(cal.DAY_OF_WEEK);
    switch(i){
      case 2 :  short_week = "Mon";
        week = "Monday";
        break;
      case 3 :  short_week = "Tue";
        week = "Tuesday";
        break;
      case 4 :  short_week = "Wed";
        week = "Wednesday";
        break;
      case 5 :  short_week = "Thu";
        week = "Thursday";
        break;
      case 6 :  short_week = "Fri";
        week = "Friday";
        break;
      case 7 :  short_week = "Sat";
        week = "Saturday";
        break;
      case 1 :  short_week = "Sun";
        week = "Sunday";
        break;
      default : break;
    }
  }

  public static void main(String[] args) {
    CalendarUtil calendarUtil1 = new CalendarUtil("2003-02-23");
    calendarUtil1.addDay(3);
    TDSLogger.println(calendarUtil1.getDate());
  }

  public String getDate() {
    if(aboutTime){
      return getYear()+"-"+getMM_Month()+"-"+getDD_Day()+" "+getHH_Hours()+":"+getMM_Minute()+":"+getSS_Second();
    }
    return getYear()+"-"+getMM_Month()+"-"+getDD_Day();
  }

  public String getYear() {
    year = Integer.toString(cal.get(cal.YEAR));
    return year;
  }

  public String getMonth() {
    month = Integer.toString(cal.get(cal.MONTH)+1);
    return month;
  }

  public String getMM_Month() {
    month = getMonth();
    if(Integer.parseInt(month)<10){
      month = "0"+month;
    }
    return month;
  }

  public String getDay() {
    day = Integer.toString(cal.get(cal.DAY_OF_MONTH));
    return day;
  }

  public String getDD_Day() {
    day = getDay();
    if (Integer.parseInt(day) < 10){
      day = "0"+day;
    }
    return day;
  }

  public String getHours() {
    hours = Integer.toString(cal.get(cal.HOUR_OF_DAY));
    return hours;
  }

  public String getHH_Hours() {
    hours = Integer.toString(cal.get(cal.HOUR_OF_DAY));
    if (Integer.parseInt(hours)<10){
      hours = "0"+hours;
    }
    return hours;
  }

  public String getMinute() {
    minute = Integer.toString(cal.get(cal.MINUTE));
    return minute;
  }

  public String getMM_Minute() {
    minute = Integer.toString(cal.get(cal.MINUTE));
    if (Integer.parseInt(minute) < 10){
        minute = "0"+minute;
    }
    return minute;
  }

  public String getSecond() {
    second = Integer.toString(cal.get(cal.SECOND));
    return second;
  }

  public String getSS_Second() {
    second = Integer.toString(cal.get(cal.SECOND));
    if (Integer.parseInt(second) < 10){
      second = "0"+second;
    }
    return second;
  }

  public String getWeek(){
    setWeek();
    return week;
  }

  public String getShortWeek(){
    setWeek();
    return short_week;
  }

  public String getWeekOfMonth(){
    week_of_month = Integer.toString(cal.get(cal.WEEK_OF_MONTH));
    return week_of_month;
  }

  public String getWeekOfYear(){
    week_of_year = Integer.toString(cal.get(cal.WEEK_OF_YEAR));
    return week_of_year;
  }

  public String addMonth(int month){
    cal.add(Calendar.MONTH,month);
    return new java.sql.Date(cal.getTime().getTime()).toString();
  }

  public String addDay(int day){
    cal.add(Calendar.DATE,day);
    return new java.sql.Date(cal.getTime().getTime()).toString();
  }

  public String addYear(int year){
    cal.add(Calendar.YEAR,year);
    return new java.sql.Date(cal.getTime().getTime()).toString();
  }
}
