package com.mxic.oiplus.util;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */
import java.security.*;

public class MD51 {
  public MD51() {
  }

  public static void main(String[] args) {
    MD5 MD51 = new MD5();
  }

  static public String getMD5String(byte[] bts){
    StringBuffer stf = new StringBuffer();
    String res = null;
    MessageDigest md = null;
    int tmpvalue = 0;
    int hivalue = 0;
    int lowvalue = 0;
    try {
      md = MessageDigest.getInstance("MD5");
      md.update(bts);
      byte[] toResDigest = md.digest();
      //System.out.println("toResDigest : " + toResDigest.length);
      for(int i = 0 ; i < toResDigest.length; i++){
        tmpvalue = (toResDigest[i]>=0)?(int)toResDigest[i]:toResDigest[i] + 256;
        hivalue = tmpvalue / 16;
        lowvalue = tmpvalue % 16;
        stf.append(Integer.toHexString(hivalue));
        stf.append(Integer.toHexString(lowvalue));
      }
      res = stf.toString();
    } catch (Exception cnse) {
      res = null;
    }
    return res;
  }
}
