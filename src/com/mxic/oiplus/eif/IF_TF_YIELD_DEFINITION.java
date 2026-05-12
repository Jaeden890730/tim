/******************************************************************************************************/
//	Author	: 	Robin Mao
//	Date	:	May 19, 2006.
/******************************************************************************************************/
package com.mxic.oiplus.eif;

import java.sql.Date;



public class IF_TF_YIELD_DEFINITION implements java.io.Serializable {
    
    public String STAGE;
    public String BRAND;
    public String VERSION;
    public String PRODUCT_CODE;
    public String TEST_MODE;
    public String ACTION;
    public String DG_ACTION;
    public String ALL_LOT_DGRADE;
    public String RELEASE_DATE;
    public Number SID;
    public Number COUNT;
    public String ROUTE_NAME;
    public String DGRADEPRODCODE;
    
    
    public String getTEST_MODE() {
        if(this.TEST_MODE != null  && this.TEST_MODE.length() < 4 && this.TEST_MODE.startsWith("S")) {
            return this.TEST_MODE.replaceAll("S", "SORT");
        }else {
            return this.TEST_MODE;
        }
    }
    
    public IF_TF_YIELD_DEFINITION[] IF_TF_YIELD_DEFINITION_ARRAY;
}
