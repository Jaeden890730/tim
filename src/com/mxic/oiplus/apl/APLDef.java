package com.mxic.oiplus.apl;

public class APLDef {
  public static class Role {
    public static final short APPLICANT = 0x100;
    public static final short MANAGER   = 0x200;
    public static final short DIRECTOR  = 0x300;
  }

  public static class AplState {
    public static final char WAIT    = 'W';
    public static final char RELEASE = 'R';
    public static final char HOLD    = 'H';
  }

  public static class ApplyState {
    public static final char INITIAL    = 'I';
    public static final char RE_INITIAL = 'N';
    public static final char PROCESS    = 'P';
    public static final char REJECTING  = 'J';
    public static final char REJECTED   = 'R';
    public static final char COMPLETE   = 'C';
    public static final char CANCELED   = 'O';
  }

  public static class ApplyType {
    public static final char NEW     = 'A';
    public static final char HOLD    = 'H';
    public static final char RELEASE = 'R';
  }
}