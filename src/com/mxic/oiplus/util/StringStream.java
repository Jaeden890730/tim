package com.mxic.oiplus.util;

import java.io.ByteArrayInputStream;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class StringStream extends ByteArrayInputStream {

  public StringStream(String somestring) {
		super(somestring.getBytes());
  }
  public static void main(String[] args) {
    StringStream stringStream1 = new StringStream("bbb");
  }
}
