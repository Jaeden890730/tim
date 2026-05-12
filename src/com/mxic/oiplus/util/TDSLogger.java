package com.mxic.oiplus.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.security.CodeSource;
import java.security.ProtectionDomain;

import com.mxic.oiplus.resource.TDSProperties;
import com.mxic.oiplus.resource.TDSResource;
/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class TDSLogger{

  public static void println(String str){
    TDSProperties prop = TDSResource.getProperties("TDSLog");
    FileWriter fw = null;
    if(prop!=null && prop.getProperty("log")!=null && prop.getProperty("log").equals("on") && prop.getProperty("log.dir")!=null &&  prop.getProperty("log.filename")!=null){
      try{
        fw = new FileWriter(prop.getProperty("log.dir")+File.separator+prop.getProperty("log.filename"), true);
        fw.write(str);
        fw.write("\n");
        String threadName = Thread.currentThread().getName();
        if ("main".equals(threadName))
        	threadName = threadName + getProcessInfo();
        fw.write("------["+DateUtil.getNow()+"] " + threadName);
        fw.write("\n");
        fw.flush();
      }catch(Exception e){
        e.printStackTrace();
      }finally{
        try{
          fw.close();
        }catch(Exception ee){
          ee.printStackTrace();
        }
      }
    }else{
    	System.out.println(str);
    }
  }

  public static void print(String str){
    TDSProperties prop = TDSResource.getProperties("TDSLog");
    FileWriter fw = null;
    if(prop!=null && prop.getProperty("log")!=null && prop.getProperty("log").equals("on") && prop.getProperty("log.dir")!=null &&  prop.getProperty("log.filename")!=null){
      try{
        fw = new FileWriter(prop.getProperty("log.dir")+File.separator+prop.getProperty("log.filename"), true);
        fw.write(str);
        fw.flush();
      }catch(Exception e){
        e.printStackTrace();
      }finally{
        try{
          fw.close();
        }catch(Exception ee){
          ee.printStackTrace();
        }
      }
    }else{
    	System.out.print(str);
    }
  }

  public static void println(Exception e){
    TDSLogger.println(StringUtil.makeStackTrace(e));
    //e.printStackTrace();
  }

  public static void println(Object e){
	  try{
		    TDSLogger.println(e.toString());
	  }catch(Exception ee){}
  }

  public static void println(int e){
    TDSLogger.println(Integer.toString(e));
  }

  public static void println(long e){
    TDSLogger.println(Long.toString(e));
  }

  public static void println(byte e){
    TDSLogger.println(Byte.toString(e));
  }

  public static void println(char e){
    TDSLogger.println(e+"");
  }

  public static void println(boolean e){
    TDSLogger.println(e+"");
  }

  public static void println(){
    TDSLogger.println("");
  }

  public static void main(String[] strs){
    //Logger logger = TDSLogger.getLogger(TDSLogger.class);
    //logger.info("helllo");
  }
  
  public static String getProcessInfo() {
		String pid = "";
		StringBuffer result = new StringBuffer();
    ProtectionDomain protectionDomain = TDSLogger.class.getProtectionDomain();
    CodeSource codeSource = protectionDomain.getCodeSource();
    URL location = codeSource.getLocation();

    String jarFile = "";
    result.append(" (");
    try {
			jarFile = URLDecoder.decode(location.getPath(), "UTF-8");
			jarFile = new File(jarFile).getName();
			result.append(jarFile);
		} catch (UnsupportedEncodingException e1) {
		}
		File f = new File("/proc/self");
		if (f.exists()) {
			try {
				pid = "PID " + f.getCanonicalFile().getName();
				if (result.length() > 2)
					result.append(" ");
				result.append(pid);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (result.length() > 2)
			result.append(")");
		else
			result.charAt(0);
		return result.toString();
  }
}
