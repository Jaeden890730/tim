/******************************************************************************************************/
//	Author	: 	Robin Mao
//	Date	:	May 19, 2006.
/******************************************************************************************************/
package com.mxic.oiplus.eif;

public class IF_TF_BOM_ROUTE implements java.io.Serializable
{
  public Number SEQ;
  public String RECTYPE; //C1
  public String ACTION; //C1
  //	    public java.sql.Date LOG_DATE;
  public String LOG_DATE;
  public String PRODUCT_BODY; //C4
  public String BRAND; //C2
  public String BACKEND_OPTION; //C1
  public String FG_WITH_CODE; //C1
  public String PIN_COUNT;
  public String PACKAGE_TYPE; //VC2
  public String FT_ROUTE_CODE; //C2
  public String FT_ROUTE; //VC5
  public String FT_ROUTE_ADD; //VC5
  public String MASK_OPTION; //C1
  public String SORT_ROUTE_CODE; //C2
  public String DB_WITH_CODE; //C1
  public String WS_ROUTE; //VC5
  public String WS_ROUTE_ADD; //VC5
  public String TF_COMMENT; //VC64
  public String TF_WS_COMMENT; //VC64
  public String SALES_FORM; //VC2
  public String BODY_REV; //VC1
  public String MASK_OPTION_REV; //VC1
  public String CODE_NO; //VC4
  public String ROUTE_TYPE; //VC8
  public String PRODUCT_TYPE; //VC4
  public String ENDURANCE; //VC12
  public String AVI; //VC1
  public String INK; //VC1
  public String FT_GROUP_KEY; //VC40
  public String WS_GROUP_KEY; //VC40
  public java.sql.Date END_TIME;
  public String WSSPECIALCONTROL; //VC12

  // for PRM2 Interface
  public String WS_STEP; //C80
  public String WS_ADD_STEP; //C80
  public String FT_STEP; //C80
  public String FT_ADD_STEP; //C80
  public String WS_ROUTE_ADD1; //C80
  public String WS_ROUTE_ADD1_STEP; //C80
  public String WS_ROUTE_ADD2; //C80
  public String WS_ROUTE_ADD2_STEP; //C80
  public String WS_ROUTE_ADD3; //C80
  public String WS_ROUTE_ADD3_STEP; //C80
  public String WS_ROUTE_ADD4; //C80
  public String WS_ROUTE_ADD4_STEP; //C80
  public String FT_ROUTE_ADD1; //C80
  public String FT_ROUTE_ADD1_STEP; //C80
  public String FT_ROUTE_ADD2; //C80
  public String FT_ROUTE_ADD2_STEP; //C80
  public String FT_ROUTE_ADD3; //C80
  public String FT_ROUTE_ADD3_STEP; //C80
  public String FT_ROUTE_ADD4; //C80
  public String FT_ROUTE_ADD4_STEP; //C80
  public String FT_ROUTE_ADD5; //C80
  public String FT_ROUTE_ADD5_STEP; //C80
  public String QUALITY_LEVEL; //VC10
  public String QUALITY_LEVEL_COMMENT; //VC128

  public Number COMPONENT_NO;
  public String COM_PROD_BODY;
  public String COM_MASK_OPTION;
  public String COM_BACKEND_OPTION;
  
  public String FORM_FACTOR_NAME;
  public String MODULE_OPTION;
  
  public IF_TF_BOM_ROUTE[] IF_TF_BOM_ROUTE_ARRAY;
}
