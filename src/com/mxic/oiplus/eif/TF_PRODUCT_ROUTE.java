/******************************************************************************************************/
//	Author	: 	Robin Mao
//	Date	:	May 19, 2006.
/******************************************************************************************************/
package com.mxic.oiplus.eif;

public class TF_PRODUCT_ROUTE implements java.io.Serializable
{
  public Number STEP_SEQ;
  public String PRODUCT_BODY; //C4
  public String BRAND; //C2
  public String ROUTE_NAME; //VC12
  public String STEP_NAME; //VC32
  public Number TEST_TIME;
  public String TIME_UNIT; //VC6
  public String TEMPERATURE; //VC12
  public String REMARK; //VC64
  public String REWORK_STEP; //VC32
  public Number TEST_TIME2;
  public String TIME_UNIT2; //VC6
  public String PROCESS_TYPE; //C2
  public String SAMPLING_TEST; //C1
  public String STEP_DEF;
  public Number SID;


  public TF_PRODUCT_ROUTE[] TF_PRODUCT_ROUTE_ARRAY;
}
