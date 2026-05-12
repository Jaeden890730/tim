package com.mxic.oiplus.resource;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */
import java.util.Enumeration;
import java.util.Properties;
import java.util.ResourceBundle;

import com.mxic.oiplus.util.TDSLogger;

public class TDSConfig {
  // The single instance
  static private TDSConfig instance;

  private TDSConfig() {}

  static Properties getTDSProperties(){
    Properties retprop = null;
    ResourceBundle resourcebundle = null;
    Enumeration  iteration = null;
    String pkey = null;
    retprop = new Properties();
    resourcebundle = ResourceBundle.getBundle("default");
    iteration = resourcebundle.getKeys();
    while (iteration.hasMoreElements()){
      pkey = (String)iteration.nextElement();
      TDSLogger.println(pkey+" , "+resourcebundle.getString(pkey));
      retprop.setProperty(pkey, resourcebundle.getString(pkey));
    }
    return retprop;
  }

  public static void main(String[] args) {
  }
}
