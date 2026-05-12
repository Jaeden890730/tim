package com.mxic.oiplus.resource;

import java.io.*;
import java.util.*;

import com.mxic.oiplus.util.*;
import com.mxic.tdsplus.util.TDSLogger;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: CIT Corp.</p>
 * @author Han, Tian-shiang
 * @version 1.0
 */

public class TDSResource {

  // static ResourceBundle resourceboundle = null;
  static Properties defprop = TDSConfig.getTDSProperties();

  public static TDSProperties getProperties(String resname){
    File file = null;
    InputStream ins = null;
    TDSProperties myprop = new TDSProperties();

    if (defprop != null && defprop.getProperty("properties.path") != null){
      try {
        file = new File(defprop.getProperty("properties.path")+File.separator+resname+".properties");
        ins = new FileInputStream(file);
        myprop.load(ins);
        ins.close();
      } catch (Exception e){
    	if (!"TDSLog".equals(resname))
        TDSLogger.println(e);
    	else
    		System.out.println(e.getMessage());
      }
    }
    return myprop;
  }

  public static void main(String[] args){
    TDSResource res = new TDSResource();
  }
}
