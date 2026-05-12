package com.mxic.oiplus.resource;

import java.util.*;

import com.mxic.oiplus.util.*;
/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class TDSProperties extends Properties {

  public TDSProperties() {
  }

  public String[] getValues(String key){
    Object[] obj = StringUtil.parse2Strings(getValue(key),",");
    ArrayList tm = new ArrayList();
    if(obj != null && obj.length > 0){
      for(int i = 0; i < obj.length; i++){
        tm.add((String)obj[i]);
      }
    }
    return (String[]) tm.toArray(new String[0]);
  }


  public  String getValue(String key){
    if (key != null){
      try {
        return getProperty(key);
      } catch (Exception e){
        TDSLogger.println(e);
        return null;
      }
    } else {
      return null;
    }
  }
}
