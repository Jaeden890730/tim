package com.mxic.oiplus.eif;

import java.io.*;
import java.util.*;
import java.sql.Timestamp;
import com.mxic.oiplus.util.*;
import com.mxic.oiplus.resource.*;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class LogWriter {
  private PrintWriter writer = null;
  private FileOutputStream os = null;


   public LogWriter(String fileName) {
          String logPath = (String) TDSResource.getProperties("EIF").get(".logPath");
          try{
                String filePath = logPath+fileName+".log";
                os = new FileOutputStream(filePath,true);
                writer = new PrintWriter(os);
                writer.println(" --  "+fileName+" Interface Server B E G I N !  at "+timer());
                writer.flush();
          }catch(Exception ex){
                TDSLogger.println(ex);
                close();
          }finally{

          }
  }

  private String timer(){
          Timestamp time = new Timestamp(System.currentTimeMillis());
          return time.toString();
  }

  public PrintWriter getWriter(){
          return writer;
  }

  public void WriterToLog(String value){
          value = "|"+timer()+"|      " + value;
          Println(value);
  }

  public void WriterToLog(String eifAction, String actionReason, HashMap value){
          WriterToLog(eifAction+"|"+receiveHashMapValues(value)+actionReason);
  }

  public void WriterToLog(String eifAction, String actionReason, StringBuffer value){
          WriterToLog(eifAction+"|"+value.toString()+actionReason);
  }

  public void WriterToLog(String eifAction, String actionReason, String value){
        WriterToLog(eifAction+"|"+value+actionReason);
  }

  private String receiveHashMapValues(HashMap value){
          if(value!=null){
                StringBuffer str = new StringBuffer();
                Iterator keys = value.keySet().iterator();
                while(keys.hasNext()){
                      String key = (String)keys.next();
                      str.append(value.get(key));
                      str.append(";");
                }
                if(str.length()>0){
                      str.delete(str.length()-1,str.length());
                }
                return str.toString();
          }else{
                return "";
          }
  }

  public void Println(String value){
          if(writer != null){
              writer.println(value);
              writer.flush();
          }
  }
  public void Print(String value){
          if(writer != null){
              writer.print(value);
              writer.flush();
          }
  }
  public void close(){
          try{
                if(writer != null){
                      writer.println(" -- Server S T O P P E D!  at "+timer()+" -------------");
                      writer.flush();
                      writer.close();
                }
                if(os != null){
                      os.close();
                }
                writer = null;
                os = null;
          }catch(Exception ex){
                TDSLogger.println(ex);
          }
  }

  public static void main(String arg[]){
          try{
                String list[] = new File("c:/analysisFile/").list();
                if(list!=null && list.length>0){
                      for(int i=0;i<list.length;i++){
                            TDSLogger.println("file:"+list[i]);
                      }
                }
          }catch(Exception ex){
                TDSLogger.println(ex);
          }
  }
}