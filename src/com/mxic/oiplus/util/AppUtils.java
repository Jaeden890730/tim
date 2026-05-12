package com.mxic.oiplus.util;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */
import java.text.DateFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AppUtils {

  /**
   * For use in URLs referenced by JSP pages or servlets, where
   * you want to avoid hardcoding the Web app name. Replace
   * <pre>
   * <XML>
   * <IMG SRC="/images/foo.gif" ...>
   * with the following two lines:
   * <% String imageURL = webAppURL("/iamges/foo.gif",
   *                                 request); %>
   * <IMG SRC="<%= imageURL %>" ...>
   *
   * </XML>
   * </pre>
   * @param origURL
   * @param request
   * @return
   */
  public static String webAppURL(String origURL,
                                 HttpServletRequest request){
    return (request.getContextPath() + origURL);
  }

  /**
   * For use when you want to support session tracking with
   * URL encoding and you are putting a URL
   * beginning with a slash into a page from a Web app.
   * @param origURL
   * @param request
   * @param response
   * @return
   */
  public static String encodeURL(String origURL,
                                 HttpServletRequest request,
                                 HttpServletResponse response){
    return (response.encodeURL(webAppURL(origURL, request)));
  }

  /**
   * For use when you want to support session tracking with
   * URL encoding and you are using sendRedirect to send a URL
   * beginning with a slash to the client.
   * @param origURL
   * @param request
   * @param response
   * @return
   */
  public static String encodeRedirectURL(String origURL,
                                         HttpServletRequest request,
                                         HttpServletResponse response){
    return (response.encodeRedirectURL(webAppURL(origURL, request)));
  }

  /**
   * produce HTML code for WS fail map
   * @param ftable a char[][] array with value 'F', '-', or else
   * @param width width of table
   * @param height height of table
   * @return HTML code string
   */
  public static String produceFailMapHTML(char [][] ftable,
                                          int width,
                                          int height){
    if (ftable == null || width==0 || height == 0 ){
      return "";
    }
    StringBuffer res = new StringBuffer();
    int max_x = ftable.length;
    int max_y = ftable[0].length;
    int each_x = width / (max_x+1) ;
    int each_y = height / (max_y+1);

    String content;
    String bgcolor;
    String classstyle;

    res.append("<style>\n");
    res.append("		.ftableborder\n");
    res.append("		{font-family: \"Arial\", \"Helvetica\", \"sans-serif\";\n");
    res.append("		font-size: 12px;\n");
    res.append("		font-weight: 500;\n");
    res.append("		border: 0px #FFFFFF;\n");
    res.append("		color: #2B4351;\n");
    res.append("		text-decoration: none;\n");
    res.append("		letter-spacing: 0px;\n");
    res.append("</style>\n");

    res.append("<table width=\"" + width + "\" height=\"" + height +"\" border=\"0\">\n");
    for (int i = 0; i <= max_x; i++){
      classstyle="";
      bgcolor = "#FFFFFF";
      res.append("<tr>");
      for (int j = 0; j <= max_y; j++){
        if (i ==0 && j == 0){
          content = "X/Y";
          bgcolor = "#FFFFFF";
          classstyle = "ftableborder";
        } else if (j == 0){
          content = Integer.toString(i);
          bgcolor = "#CCCCFF";
          classstyle = "ftableborder";

        } else if (i == 0){
          content = Integer.toString(j);
          bgcolor = "#CCCCFF";
          classstyle = "ftableborder";
        } else {
          if (ftable[i-1][j-1] == 'F'){
            content = String.valueOf(ftable[i-1][j-1]);
            bgcolor = "#CCCCCC";
          } else if(ftable[i-1][j-1] == '-'){
            content = String.valueOf(ftable[i-1][j-1]);
            bgcolor = "#CCCCCC";
          } else {
            content = "&nbsp;";
            bgcolor = "#FFFFFF";
          }
        }
        res.append("<td width=\"" + each_x + "\"  height=\"" + each_y +"\" bgcolor=\"" + bgcolor+ "\" class=\"" + classstyle + "\"><div align=\"center\">");
        res.append(content);
        res.append("</div></td> ");
      }
      res.append("</tr>\n");
    }
    res.append("</table>\n");
    return res.toString();
  }

  public static HashMap sumByGroup(HashMap hashmap,
                                   Object item,
                                   int itemvalue){
    if (hashmap == null || item == null ){
      return null;
    }
    int oldvalue;
    if (hashmap.containsKey(item)){
      oldvalue = ((Integer)hashmap.get(item)).intValue();
      hashmap.remove(item);
      hashmap.put(item, new Integer(oldvalue + itemvalue));
    } else {
      hashmap.put(item, new Integer(itemvalue));
    }
    return hashmap;
  }

  public static String getRandomColor(){
    String rr = Integer.toHexString((int)(Math.random()*256));
    String gg = Integer.toHexString((int)(Math.random()*256));
    String bb = Integer.toHexString((int)(Math.random()*256));
    if (rr.length() < 2){
      rr = "0" + rr;
    }
    if (gg.length() < 2){
      gg = "0" + gg;
    }
    if (bb.length() < 2){
      bb = "0" + bb;
    }
    return ("#" + rr + gg + bb).toUpperCase();
  }

  public static String getSubDir(String lotno){
    String ret = null;
    if (lotno == null){
      return "X";
    }
    if (lotno.length() >= 5 && Character.isDigit(lotno.charAt(4))){
      ret = lotno.substring(4,5);
    } else {
      ret = "X";
    }
    return ret;
  }

  public int getGreatestNumber(ArrayList array_numbers){
    int ibig = 0;
    try
    {
      for(int i=0;i<array_numbers.size();i++)
      {
        if(((Integer)(array_numbers.get(i))).intValue() > ibig){
        ibig =((Integer)(array_numbers.get(i))).intValue();
      }
    }
  } catch (Exception ex) {
    ex.printStackTrace();
  }
  return ibig;
}

  public static long getSeconds(String date1,
                                String date2,
                                String format)
  {
    long timedifferenceSeconds=0;
    try
    {
      SimpleDateFormat sdf=new SimpleDateFormat(format);
      DateFormat df = sdf;
      java.util.Date dates1=df.parse(date1);
      long timeInSeconds1 =(dates1.getTime()/1000);

      java.util.Date dates2=df.parse(date2);
      long timeInSeconds2 =(dates2.getTime()/1000);
      timedifferenceSeconds=timeInSeconds2-timeInSeconds1;
    } catch(Exception ex) {
      ex.printStackTrace();
    }
    return timedifferenceSeconds;
  }

  public static java.sql.Date getFormatter(String date_string)
  {
    java.sql.Date formatted_date=null;
    try
    {
      SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd");
      ParsePosition pos = new ParsePosition(0);
      formatted_date = new java.sql.Date(formatter.parse(date_string, pos).getTime());
    } catch(Exception ex) {
      ex.printStackTrace();
    }
    return formatted_date;
  }

  public static int getCurrentUserCount()
  {
    int ibig=0;
    try
    {
      //ibig = TDSActionServlet.TotalSession.size();
    } catch(Exception ex) {
      ex.printStackTrace();
    }
    return ibig;
  }
}
