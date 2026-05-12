package com.mxic.oiplus.rs;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.*;
public class TDSConfig {
  // The single instance
  static private TDSConfig instance;
  public static final String mainPropName = "TDS";
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
      retprop.setProperty(pkey, resourcebundle.getString(pkey));
    }
    return retprop;
  }

  public static void main(String[] args) {
      Properties defprop = TDSConfig.getTDSProperties();
  }
  public static Properties getDefaultProperties(){
	    Properties retprop = null;
	    ResourceBundle resourcebundle = null;
	    Enumeration  iteration = null;
	    String pkey = null;
	    retprop = new Properties();
	    resourcebundle = ResourceBundle.getBundle("default");
	    iteration = resourcebundle.getKeys();
	    while(iteration.hasMoreElements()){
	      pkey = (String)iteration.nextElement();
	      retprop.setProperty(pkey, resourcebundle.getString(pkey));
	    }
	    return retprop;
	  }
  /**
   * 指定 property name 傳回 Property
   * @param propertyName .
   * @return Properties
   */
  public static Properties getProperty(String propertyName) throws Exception {
    Properties prop = null;
    try {
      String propertyPath = getDefaultProperties().getProperty("properties.path");
      File file = new File(propertyPath + File.separator + propertyName + ".properties");
      InputStream ins = new FileInputStream(file);
      prop = new Properties();
      prop.load(ins);
      ins.close();
    }
    catch (Exception e){
      e.printStackTrace();
      throw e;
    }
    finally {
      return prop;
    }
  }
  /**
   * 指定 property name 與 key name，傳回值
   * @param propertyName .
   * @param keyName .
   * @return string value
   */
  public static String getPropertyKeyValue(String propertyName, String keyName){
    String val = null;
    try {
      Properties prop = getProperty(propertyName);
      val = prop.getProperty(keyName);
    }
    catch (Exception e){
      e.printStackTrace();
    }
    finally {
      return val;
    }
  }
  /**
   * 指定 key name，傳回mainPropName 內值
   * @param keyName .
   * @return .
   */
  public static String getMainPropKeyValue(String keyName){
    return getPropertyKeyValue(mainPropName, keyName);
  }
}
