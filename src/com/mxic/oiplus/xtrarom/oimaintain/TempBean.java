package com.mxic.oiplus.xtrarom.oimaintain;

import java.awt.*;
import javax.swing.*;

public class TempBean {
  private String temp;
  private String timeunit;
  
  public void setTemp(String temp) {
    this.temp = temp;
  }

  public void setTimeunit(String timeunit) {
    this.timeunit = timeunit;
  }

  public String getTemp() {
    return temp;
  }

  public String getTimeunit() {
    return timeunit;
  }
}
