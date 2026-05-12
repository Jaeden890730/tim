package com.mxic.oiplus.eif;

import java.io.*;
import java.util.*;
import java.sql.Timestamp;
import com.mxic.tdsplus.util.*;
/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class WriterFile {
  private PrintWriter writer = null;
  private FileOutputStream os = null;

  public WriterFile(String filePath) {
          try{
                os = new FileOutputStream(filePath);
                writer = new PrintWriter(os);
          }catch(Exception ex){
                TDSLogger.println(ex);
                close();
          }finally{

          }

  }
  //lai-add-20080506-start
  public WriterFile(String filePath, boolean append) {
          try{
                os = new FileOutputStream(filePath, append);
                writer = new PrintWriter(os);
          }catch(Exception ex){
                TDSLogger.println(ex);
                close();
          }finally{

          }

  }
  //lai-add-20080506-end
  public PrintWriter getWriter(){
          return writer;
  }

  public void Println(String value){
          int remain = 65;
          remain -= value.length();
          writer.print(value);
          for(int i=0;i<remain;i++){
              writer.print(" ");
          }
          writer.println();
  }
  public void Print(String value){
          writer.print(value);
  }
  public void Write(String value){
          writer.print(value);
  }
  public void Write(Object value){
          writer.print(value);
  }
  public void Writeln(String value){
        writer.println(value);
  }
  public void Writeln(Object value){
        writer.println(value);
  }
  public void close(){
          try{
                if(writer != null){
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
}