package com.mxic.oiplus.oimaintain;

import java.awt.*;
import javax.swing.*;

public class wtbean {
  private String mask_option;
  private String site;
  private String undefine_step;
  private String undefine_msg;

  public void setMask_option(String mask_option) {
    this.mask_option = mask_option;
  }

  public String getMask_option() {
    return mask_option;
  }

  public String getSite() {
    return site;
  }

  public void setSite(String site) {
    this.site = site;
  }

  public String getUndefine_step() {
    return undefine_step;
  }

  public void setUndefine_step(String undefine_step) {
    this.undefine_step = undefine_step;
  }

  public String getUndefine_msg() {
    return undefine_msg;
  }

  public void setUndefine_msg(String undefine_msg) {
    this.undefine_msg = undefine_msg;
  }
}
