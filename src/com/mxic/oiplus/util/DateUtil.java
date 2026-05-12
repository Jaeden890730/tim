package com.mxic.oiplus.util;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

import java.sql.*;
import java.text.*;
import java.util.*;
import java.util.Date;

public class DateUtil {
  /**
   * parse datetime string into a timestamp object
   * <p>
   * Timeformat could be "yyyy.MM.dd G 'at' hh:mm:ss a zzz"
   * </p>
   *
   * <pre>
   *  Symbol   Meaning                 Presentation        Example<br>
   *   ------   -------                 ------------        -------
   *   G        era designator          (Text)              AD
   *   y        year                    (Number)            1996
   *   M        month in year           (Text & Number)     July & 07
   *   d        day in month            (Number)            10
   *   h        hour in am/pm (1~12)    (Number)            12
   *   H        hour in day (0~23)      (Number)            0
   *   m        minute in hour          (Number)            30
   *   s        second in minute        (Number)            55
   *   S        millisecond             (Number)            978
   *   E        day in week             (Text)              Tuesday
   *   D        day in year             (Number)            189
   *   F        day of week in month    (Number)            2 (2nd Wed in July)
   *   w        week in year            (Number)            27
   *   W        week in month           (Number)            2
   *   a        am/pm marker            (Text)              PM
   *   k        hour in day (1~24)      (Number)            24
   *   K        hour in am/pm (0~11)    (Number)            0
   *   z        time zone               (Text)              Pacific Standard Time
   *   '        escape for text         (Delimiter)
   *   ''       single quote            (Literal)           '
   *
   *  Format Pattern                         Result
   *   --------------                         -------
   *   "yyyy.MM.dd G 'at' hh:mm:ss z"    ->>  1996.07.10 AD at 15:08:56 PDT
   *   "EEE, MMM d, ''yy"                ->>  Wed, July 10, '96
   *   "h:mm a"                          ->>  12:08 PM
   *   "hh 'o''clock' a, zzzz"           ->>  12 o'clock PM, Pacific Daylight Time
   *   "K:mm a, z"                       ->>  0:00 PM, PST
   *   "yyyyy.MMMMM.dd GGG hh:mm aaa"    ->>  1996.July.10 AD 12:08 PM

   *
   *
   * </pre>

   * @param timestr an String represent a time
   * @param timeformat
   * @return an Timestamp
   * @throws Exception
   */
  public static Timestamp parseTimestamp(String timestr,
                                         String timeformat) throws Exception {
    // Format the current time.
    SimpleDateFormat formatter
        = new SimpleDateFormat (timeformat, Locale.ENGLISH);
    // Parse the previous string back into a Date.
    ParsePosition pos = new ParsePosition(0);
    java.util.Date date = formatter.parse(timestr, pos);
    if (date == null){
      throw new Exception("Date format error :"+timestr );
    }

    return new Timestamp(date.getTime());
  }

  public static String Timestamp2String(Date timestamp,
                                        String timeformat) throws Exception {
    SimpleDateFormat formatter
        = new SimpleDateFormat (timeformat, Locale.ENGLISH);
    return formatter.format(timestamp);
  }

  public static long getDiffMinutes(Date date1, Date date2){
    // date1 = End Time ; date2 = Start Time
    long msec1 = date1.getTime();
    long msec2 = date2.getTime();
    msec1 -= msec2;
    return (msec1 / 1000 / 60);
  }
  public static long getDiffSecond(Date date1, Date date2){
	    // date1 = End Time ; date2 = Start Time
	    long msec1 = date1.getTime();
	    long msec2 = date2.getTime();
	    msec1 -= msec2;
	    return (msec1 / 1000);
	  }

  public static long getDiffDays(Date date1, Date date2){
    // date1 = End Time ; date2 = Start Time
    long msec1 = date1.getTime();
    long msec2 = date2.getTime();
    msec1 -= msec2;
    return (msec1 / 1000 / 60 / 60 / 24);
  }

  public static long getDiffDays(String date1, String date2){
    try{
      String format1 = "yyyy/MM/dd";
      String format2 = "yyyy/MM/dd";
      if (date1 != null && date1.indexOf(":") > 7){
        format1 = "yyyy/MM/dd HH:mm:ss";
      }
      if (date2 != null && date2.indexOf(":") > 7){
        format2 = "yyyy/MM/dd HH:mm:ss";
      }
      Timestamp t1 = parseTimestamp(date1, format1);
      Timestamp t2 = parseTimestamp(date2, format2);
      Date a2 = new Date(t2.getTime());
      Date a1 = new Date(t1.getTime());
      return getDiffDays(a2,a1);
    } catch (Exception e){
      e.printStackTrace();
      return 0;
    }
  }

  public static String getNowFormat(){
	    String ret = null;
	    try{
	      ret = DateUtil.Timestamp2String(new java.util.Date(),"yyyyMMddHHmmss");
	    } catch (Exception e){
	      ret = "";
	    }
	    return ret;
	  }

  public static String getNow(){
    String ret = null;
    try{
      ret = DateUtil.Timestamp2String(new java.util.Date(),"yyyy/MM/dd HH:mm:ss");
    } catch (Exception e){
      ret = "";
    }
    return ret;
  }

  public static String getDay(){
    String ret = null;
    try{
      ret = DateUtil.Timestamp2String(new java.util.Date(),"yyyy/MM/dd");
    } catch (Exception e){
      ret = "";
    }
    return ret;
  }

  public static String getMonth(){
    String ret = null;
    try{
      ret = DateUtil.Timestamp2String(new java.util.Date(),"MM");
    } catch (Exception e){
      ret = "";
    }
    return ret;
  }

  public static String getYear(){
    String ret = null;
    try {
      ret = DateUtil.Timestamp2String(new java.util.Date(),"yyyy");
    } catch (Exception e){
      ret = "";
    }
    return ret;
  }

  public static String getOnBoardDay(String tmp){
    String ret = null;
    SimpleDateFormat formatter= new SimpleDateFormat ("yyyy/MM/dd");

    try {
      Date i = formatter.parse(tmp);
      ret = DateUtil.Timestamp2String( i,"dd");
    } catch (Exception e){
      ret = "";
    }
    return ret;
  }

  public static String getOnBoardMonth(String tmp){
    String ret = null;
    SimpleDateFormat formatter= new SimpleDateFormat ("yyyy/MM/dd");

    try{
      Date i = formatter.parse(tmp);
      ret = DateUtil.Timestamp2String( i,"MM");
    } catch (Exception e){
      ret = "";
    }
    return ret;
  }

  public static String getOnBoardYear(String tmp){
    String ret = null;
    SimpleDateFormat formatter= new SimpleDateFormat ("yyyy/MM/dd");

    try{
      Date i = formatter.parse(tmp);
      ret = DateUtil.Timestamp2String( i,"yyyy");
    } catch (Exception e){
      ret = "";
    }
    return ret;
  }

  public static String getOnBoardDate(String tmp){
    String ret = null;
    SimpleDateFormat formatter= new SimpleDateFormat ("yyyy/MM/dd");

    try{
      Date i = formatter.parse(tmp);
      ret = DateUtil.Timestamp2String( i,"yyyy/MM/dd");
    } catch (Exception e){
      ret = "";
    }
    return ret;
  }

  public static Date ConvertStringToDate(String tmp){
    Date i = null;
    SimpleDateFormat formatter= new SimpleDateFormat ("yyyy/MM/dd");

    try{
      i = formatter.parse(tmp);
    } catch (Exception e){
      i = null;
    }
    return i;
  }

  public static void main(String[] args){
    try{
      Timestamp t2 = parseTimestamp("2003-04-21", "yyyy/MM/dd");
      Timestamp t1 = parseTimestamp("2003-04-10 04:51:59", "yyyy/MM/dd HH:mm:ss");
      Date a2 = new Date(t2.getTime());
      Date a1 = new Date(t1.getTime());
      TDSLogger.println(getDiffDays(a2,a1));
    } catch (Exception e){
      TDSLogger.println(e);
    }
  }
  /**
   * <p>This method  returns value of the Date/Time or TimeStamp by receiving
   *     the following Dateformat</p>
   *
   *	       Format Pattern                         Result
   *	       --------------                         -------
   *	  "yyyy.MM.dd G 'at' hh:mm:ss z"    ->>  1996.07.10 AD at 15:08:56 PDT
   *	  "EEE, MMM d, ''yy"                ->>  Wed, July 10, '96
   *    "h:mm a"                          ->>  12:08 PM
   *    "hh 'o''clock' a, zzzz"           ->>  12 o'clock PM, Pacific Daylight Time
   *    "K:mm a, z"                       ->>  0:00 PM, PST
   *	  "yyyyy.MMMMM.dd GGG hh:mm aaa"    ->>  1996.July.10 AD 12:08 PM
   *
   *
   * @param format Format of the date/time as a String.
   *
   * @return date_time The value of the Date/Time returned as an argument.
   */
  public  static String getDateTime(String format)
  {
    String date_time=null;
    try
    {
      SimpleDateFormat formatter = new SimpleDateFormat(format);
      java.util.Date currentDate = new java.util.Date();
      date_time = formatter.format(currentDate);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return date_time;
  }

  public static DateString valueOf(String date){
    return new DateString(date);
  }

  public static DateString valueOf(String date,int days){
    CalendarUtil calendar = new CalendarUtil(date);
    calendar.addDay(days);
    return new DateString(calendar.getDate());
  }
}
