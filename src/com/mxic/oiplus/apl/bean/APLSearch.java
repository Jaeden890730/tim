package com.mxic.oiplus.apl.bean;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;

import com.mxic.oiplus.apl.bean.APL.*;
import com.mxic.oiplus.apl.APLUtil;
import com.mxic.oiplus.util.StringUtil;

public class APLSearch {
  String target = "data";
  String option = "query";

  final static String EQ = " = ";
  final static String LIKE = " LIKE ";

  public APLSearch(String searchTarget, String searchOption) {
    target = searchTarget;
    option = searchOption;
  }

  private HashSet options = null;
  public ArrayList getOptions() {
    return dump(options);
  }

  private HashSet packages = null;
  public ArrayList getPackCodes() {
    return dump(packages);
  }

  private HashSet pinCounts = null;
  public ArrayList getPinCounts() {
    return dump(pinCounts);
  }

  private HashSet testModes = null;
  public ArrayList getTestModes() {
    return dump(testModes);
  }

  private HashSet testerTypes = null;
  public ArrayList getTesterTypes() {
    return dump(testerTypes);
  }
  
  private HashSet bodySizes = null;
  public ArrayList getBodySizes() {
    return dump(bodySizes);
  }
  
  private HashSet carrierTypes = null;
  public ArrayList getCarrierTypes() {
    return dump(carrierTypes);
  }
  
  private HashSet inks = null;
  public ArrayList getInks() {
    return dump(inks);
  }
  
  private HashSet markingSpecVersions = null;
  public ArrayList getMarkingSpecVersions() {
    return dump(markingSpecVersions);
  }
  
  private HashSet markingSpecNos = null;
  public ArrayList getMarkingSpecNos() {
    return dump(markingSpecNos);
  }
  
  private HashSet sites = null;
  public ArrayList getSites() {
    return dump(sites);
  }

  private HashMap vendors = null;
  private ArrayList vendorNo = null;
  private ArrayList vendorName = null;
  public ArrayList getVendorNames() {
    return vendorName;
  }
  public ArrayList getVendorNos() {
    return vendorNo;
  }

  protected void getSummary(AP_APL[] samples) {
    options = new HashSet();
    packages = new HashSet();
    pinCounts = new HashSet();
    testModes = new HashSet();
    testerTypes = new HashSet();
    vendors = new HashMap();
    sites = new HashSet();
    bodySizes = new HashSet();
    carrierTypes = new HashSet();
    markingSpecNos = new HashSet();
    markingSpecVersions = new HashSet();
    inks = new HashSet();
    for ( int i = 0; samples != null && i < samples.length; i++ ) {
      options.add(samples[i].OPTIONS);
      packages.add(samples[i].PACKAGE_TYPE);
      pinCounts.add(samples[i].PIN_COUNT);
      testModes.add(samples[i].TEST_MODE);
      testerTypes.add(samples[i].TESTER_TYPE);
      vendors.put(samples[i].VENDOR_NO, samples[i].VENDOR_NAME);
      sites.add(samples[i].SITE);
      bodySizes.add(samples[i].BODY_SIZE);
      carrierTypes.add(samples[i].CARRIER_TYPE);
      markingSpecNos.add(samples[i].MARKING_SPEC_NO);
      markingSpecVersions.add(samples[i].MARKING_SPEC_VERSION);
      inks.add(samples[i].INK);
    }
    vendorNo = new ArrayList(vendors.size() + 1);
    vendorName = dump(vendors, vendorNo);
  }

  public ArrayList execute(Connection conn) throws Exception {
    return execute(conn, new HashMap(), null);
  }

 // if modify == true, �Ω�h�󭫰e
  public ArrayList execute(Connection conn, HashMap filter, String reApplyNo) throws Exception {
    ArrayList temp = new ArrayList(filter.size() + 10);
    if ( target.equals("data") && !option.equals("sign") ) {
      String query = buildDataSearchQuery(filter, temp, reApplyNo);
      Object[] wheres = temp.toArray();
      AP_APL[] data =
          (AP_APL[]) APLUtil.getData(conn,query,AP_APL.class,wheres).toArray(new AP_APL[0]);
      if ( data != null ) {
        temp.clear();
        temp.ensureCapacity(data.length);
        for ( int i = 0; i < data.length; i++ ) {
          temp.add(new APL(data[i]));
        }
        getSummary(data);
      }
      // 20061218 add for clean objects
      data = null;
    }
    if ( target.equals("data") && option.equals("sign")) {
      String query = buildSignSearchQuery(filter, temp);
      Object[] wheres = temp.toArray();
      REPORT[] data = (REPORT[]) APLUtil.getData(conn,query,REPORT.class,wheres).toArray(new REPORT[0]);
      if ( data != null ) {
        temp.clear();
        temp.ensureCapacity(data.length);
        for ( int i = 0; i < data.length; i++ ) {
          temp.add(data[i]);
        }
      }
      // 20061218 add for clean objects
      data = null;
    }
    if ( target.equals("process")) {
      String query = buildProcessSearchQuery(filter, temp);
      Object[] wheres = temp.toArray();
      REPORT[] data = (REPORT[]) APLUtil.getData(conn,query,REPORT.class,wheres).toArray(new REPORT[0]);
      if ( data != null ) {
        temp.clear();
        temp.ensureCapacity(data.length);
        for ( int i = 0; i < data.length; i++ ) {
          temp.add(data[i]);
        }
        getSummary((AP_APL[])data);
      }
      // 20061218 add for clean objects
      data = null;
    }
    return temp;
  }

  // modify == true if �h�󭫰e
  private String buildDataSearchQuery(HashMap filter, ArrayList wheres, String reApplyNo) {
    StringBuffer sql = new StringBuffer(filter.size() > 0 ? 8 * filter.size() : 32);
    String time_const = "";
    String check_time = "";

    if (reApplyNo != null)
      sql.append("SELECT DECODE(AP_APP_DETAIL.APP_NO,null,'','CHECKED') checked, APL.SID,APL.PROCESS_TYPE,APL.PRODUCT_BODY,APL.OPTIONS,APL.PACKAGE_TYPE,APL.PIN_COUNT,APL.TEST_MODE,APL.TESTER_TYPE,APL.VENDOR_NO,APL.APL_STATUS,DECODE(APL.ISEXISTS8049,null,'',APL.ISEXISTS8049) ISEXISTS8049,APL.APL_NO,APL.LOG_TIME,APL.PIM_RELEASEDATE,NVL(APL.APL_NO,' ') APP_NO, PLANT.PLANT_NAME VENDOR_NAME, APL.SITE, APL.BODY_SIZE, APL.CARRIER_TYPE, APL.MARKING_SPEC_NO, APL.MARKING_SPEC_VERSION, APL.INK ");
    else
      sql.append("SELECT APL.SID,APL.PROCESS_TYPE,APL.PRODUCT_BODY,APL.OPTIONS,APL.PACKAGE_TYPE,APL.PIN_COUNT,APL.TEST_MODE,APL.TESTER_TYPE,APL.VENDOR_NO,APL.APL_STATUS,DECODE(APL.ISEXISTS8049,null,'',APL.ISEXISTS8049) ISEXISTS8049,APL.APL_NO,APL.LOG_TIME,APL.PIM_RELEASEDATE, NVL(APL.APL_NO,' ') APP_NO, PLANT.PLANT_NAME VENDOR_NAME, APL.SITE, APL.BODY_SIZE, APL.CARRIER_TYPE, APL.MARKING_SPEC_NO, APL.MARKING_SPEC_VERSION, APL.INK ");
    sql.append("FROM AP_APL APL, BA_PLANT PLANT ");
    if (reApplyNo != null)
      sql.append(",AP_APP_DETAIL ");
    sql.append("WHERE APL.VENDOR_NO = PLANT.SAP_PLANT_NO AND (PLANT.PGM_FLAG = 1 or PLANT.LS_FLAG = 1)");
    if (reApplyNo != null)
      sql.append("AND APL.SID = AP_APP_DETAIL.APL_SID (+)")
         .append("AND AP_APP_DETAIL.APP_NO(+) = '"+reApplyNo+"'  ");

    if ( filter.size() > 0 ) {
      Iterator iter = filter.keySet().iterator();
      while ( iter.hasNext() ) {
        String column = (String) iter.next();
        if (column.endsWith("TIME")) {
          if (column.equalsIgnoreCase("END_TIME"))
            time_const = time_const + "AND TO_DATE('" + filter.get(column) + "','yyyy-mm-dd hh24:mi:ss') ";
          else if (column.equalsIgnoreCase("START_TIME"))
            time_const = "AND PIM_ReleaseDate BETWEEN TO_DATE('" +
                filter.get(column) + "','yyyy-mm-dd hh24:mi:ss') " +
                time_const;
          else check_time = (String) filter.get(column);
          continue;
        }
        else if ( filter.get(column).getClass().isArray() ) {
          sql.append( append(column, (Object[])filter.get(column), " OR ", wheres) );
          continue;
        }
        sql.append( append(column, filter.get(column), wheres) );
      }
    }

    if ( option.startsWith("apply") && !filter.containsKey("SID") ) {
      sql.append("AND APL_IS_AVAILABLE(SID) = 1 ");
    }

    if (!check_time.equals("") && !check_time.equals(""))
      sql.append(time_const);

    return sql.toString();
  }

  private String buildSignSearchQuery(HashMap filter, ArrayList wheres) {
    StringBuffer sql = new StringBuffer(filter.size() > 0 ? 8 * filter.size() : 32);
    sql.append("SELECT APPLY.APP_NO, APPLY.PROCESS_TYPE, APPLY.BATCH_TYPE, ");
    sql.append("APPLY.APP_TYPE, APPLY.APP_STATUS, APPLY.INITIAL_USER INITIAL_UID, ");
    sql.append("APPLY.INITIAL_TIME, USR.REAL_NAME INITIAL_USER, APPLY.REASON ");
    sql.append("FROM AP_APP_MASTER APPLY, AU_USER_ACCOUNT USR ");
    sql.append("WHERE TRIM(APPLY.INITIAL_USER) = TRIM(USR.EMPLOYEE_NO) ");
    if ( filter.size() > 0 ) {
      Iterator iter = filter.keySet().iterator();
      while ( iter.hasNext() ) {
        String column = (String) iter.next();
        if ( filter.get(column).getClass().isArray() ) {
          sql.append( append(column, (Object[])filter.get(column), " OR ", wheres) );
          continue;
        }
        if ( column.equals("APP_STATUS") ) {
          StringTokenizer tokens = new StringTokenizer( (String) filter.get(column), "," );
          sql.append( append(column, tokens, " OR ", wheres) );
          continue;
        }
        sql.append( append(column, filter.get(column), wheres) );
      }
    }
    return sql.toString();
  }

  private String buildProcessSearchQuery(HashMap filter, ArrayList wheres) {
    StringBuffer sql = new StringBuffer(512 + (8 * filter.size()));
    sql.append("SELECT APPLY.*, PLIST.SID, PLIST.APL_STATUS, PLIST.PRODUCT_BODY, ");
    sql.append("PLIST.OPTIONS, PLIST.PACKAGE_TYPE, PLIST.PIN_COUNT, PLIST.TEST_MODE, ");
    sql.append("PLIST.TESTER_TYPE, PLIST.BODY_SIZE, PLIST.VENDOR_NO, PLANT.PLANT_NAME VENDOR_NAME, NVL(PLIST.APL_NO,' ') APL_NO ");
    if ( filter.containsKey("APP_STATUS") &&
         ((String) filter.get("APP_STATUS")).indexOf('C') >= 0 ) {
      sql.append("FROM AP_HIST_MASTER APPLY, AP_HIST_DETAIL DETAIL");
    } else
      sql.append("FROM AP_APP_MASTER APPLY, AP_APP_DETAIL DETAIL");
    sql.append(", AP_APL PLIST, BA_PLANT PLANT ");
    sql.append("WHERE APPLY.APP_NO = DETAIL.APP_NO AND DETAIL.APL_SID = PLIST.SID ");
    sql.append("AND PLIST.VENDOR_NO = PLANT.SAP_PLANT_NO ");
    if ( filter.size() > 0 ) {
      Iterator iter = filter.keySet().iterator();
      while ( iter.hasNext() ) {
        String column = ((String) iter.next()).toUpperCase();
        if ( filter.get(column).getClass().isArray() ) {
          sql.append( append(column, (String[])filter.get(column), " OR ", wheres) );
          continue;
        }
        if ( column.equals("APP_STATUS") ) {
          StringTokenizer tokens = new StringTokenizer( (String) filter.get(column), "," );
          sql.append( append(column, tokens, " OR ", wheres) );
          continue;
        }
        if ( column.equals("PROCESS_TYPE") || column.equals("APP_NO") ) {
          sql.append( append("APPLY." + column, filter.get(column), wheres) );
        } else {
          sql.append( append(column, filter.get(column), wheres) );
        }
      }
    }
    return sql.toString();
  }
  // define 'EQUAL' or 'LIKE'
  private String valueOf( Object value ) {
    String op = EQ;
    if ( value instanceof String ) {
      if ( ((String) value).indexOf("%") >= 0 ) op = LIKE;
    }
    return op + "?";
  }
  // append a column
  private String append( String key, Object value, ArrayList wheres ) {
    wheres.add(value);
    return "AND " + key + valueOf(value);
  }
  // append an array
  private String append( String key, Object[] values, String op, ArrayList wheres ) {
    if (values == null || values.length == 0)
      return "";
    StringBuffer sb = new StringBuffer((key.length() + 10) * values.length);
    for ( int i = 0; i < values.length; i++ ) {
      sb.append(key + valueOf(values[i]) + (i < values.length - 1 ? op : ""));
      wheres.add(values[i]);
    }
    return "AND (" + sb + ") ";
  }
  // append a comma-connected string (a,b,c,...)
  private String append( String key, StringTokenizer tokens, String op, ArrayList wheres ) {
    if (tokens == null || tokens.countTokens() == 0)
      return "";
    StringBuffer sb = new StringBuffer((key.length() + 10) * tokens.countTokens());
    for ( int i = 0, cnt = tokens.countTokens(); i < cnt; i++ ) {
      String next = tokens.nextToken();
      sb.append(key + valueOf(next) + (i < cnt - 1 ? op : ""));
      wheres.add(next);
    }
    return "AND (" + sb + ") ";
  }

  private ArrayList dump( HashSet values ) {
    ArrayList result = new ArrayList(values != null ? values.size() + 1 : 10);
    if ( values != null ) {
      result.addAll(values);
      Collections.sort(result);
    }
    result.add(0, "");
    return result;
  }

  private ArrayList dump( HashMap values, ArrayList keyArray ) {
    if ( values != null ) {
      keyArray.ensureCapacity(values.size() + 1);
      keyArray.addAll(values.keySet());
      Collections.sort(keyArray);
      keyArray.add(0, "");
    }
    ArrayList result = new ArrayList(keyArray.size());
    for (int i = 1; i < keyArray.size(); i++) {
      result.add(values.get(keyArray.get(i)));
    }
    result.add(0, "");
    return result;
  }

  public static class REPORT extends APL.AP_APL {
    public String APP_TYPE;
    public String APP_STATUS;
    public String BATCH_TYPE;
    public String INITIAL_USER;
    public java.util.Date INITIAL_TIME;
    public String PROCESS_USER;
    public java.util.Date PROCESS_TIME;
    public String COMPLETE_USER;
    public java.util.Date COMPLETE_TIME;
    public String REASON;

    public String getApplyNo() {
      return APP_NO;
    }
    public String getAplNo() {
      return APL_NO;
    }
    public String getProcType() {
      return PROCESS_TYPE;
    }
    public String getApplyType() {
      return APP_TYPE;
    }
    public String getApplyStatus() {
      return APP_STATUS;
    }
    public String getBatchType() {
      return BATCH_TYPE;
    }
    public String getInitialUser() {
      return INITIAL_USER;
    }
    public java.util.Date getInitialTime() {
      return INITIAL_TIME;
    }
    public String getProcessUser() {
      return PROCESS_USER;
    }
    public java.util.Date getProcessTime() {
      return PROCESS_TIME;
    }
    public String getCompleteUser() {
      return COMPLETE_USER;
    }
    public java.util.Date getCompleteTime() {
      return COMPLETE_TIME;
    }
    //
    public Number getSID() {
      return SID;
    }
    public String getProdBody() {
      return PRODUCT_BODY;
    }
    public String getOption() {
      return OPTIONS;
    }
    public String getPackCode() {
      return PACKAGE_TYPE;
    }
    public String getPinCount() {
      return PIN_COUNT;
    }
    public String getTestMode() {
      return TEST_MODE;
    }
    public String getTesterType() {
      return TESTER_TYPE;
    }
    public String getVendorNo() {
      return VENDOR_NO;
    }
    public String getVendorName() {
      return VENDOR_NAME;
    }
    public String getAplStatus() {
      return APL_STATUS;
    }
    public String getBodySize() {
      return BODY_SIZE;
    }
    public String getReason() {
      return REASON;
    }
    public String getSite() {
        return SITE;
    }
    public String getCarrierType() {
        return CARRIER_TYPE;
    }
    public String getMarkingSpecNo() {
        return MARKING_SPEC_NO;
    }
    public String getMarkingSpecVersion() {
        return MARKING_SPEC_VERSION;
    }
    public String getInk() {
        return INK;
    }

    public String toString() {
      char c = ',';
      String app_status = null;
      String app_type = null;
      String apl_status = null;

      if (APP_STATUS.equals("R"))
        app_status = "�w�h��";
      else if (APP_STATUS.equals("C"))
        app_status = "�w����";
      else if (APP_STATUS.equals("I"))
        app_status = "���g��";
      else if (APP_STATUS.equals("P"))
        app_status = "�|ñ��";
      else if (APP_STATUS.equals("N"))
        app_status = "���ӽ�";
      else if (APP_STATUS.equals("J"))
        app_status = "�h��";
      else if (APP_STATUS.equals("O"))
        app_status = "�v�@�o";

      if (APP_TYPE.equals("P"))
        app_type = "By ���~";
      else if (APP_TYPE.equals("T"))
        app_type = "By ���x";
      else if (APP_TYPE.equals("A"))
        app_type = "�ͮĥӽ�";

      if (APL_STATUS.equals("W"))
        apl_status = "Wait";
      else if (APL_STATUS.equals("R"))
        apl_status = "Release";
      else if (APL_STATUS.equals("H"))
        apl_status = "Hold";

      SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
      StringBuffer sb = new StringBuffer();
      if (APP_STATUS.equals("R") || APP_STATUS.equals("C")) {
        sb.append(app_type + c).append(APP_NO.trim() + c);
      }

      if (getProcType().equals("WS"))
        sb.append(PRODUCT_BODY + c)
            .append(OPTIONS + c)
            .append(TEST_MODE + c)
            .append(TESTER_TYPE + c)
            .append(VENDOR_NAME + c);
      else 
        sb.append(PRODUCT_BODY + c)
            .append(OPTIONS + c)
            .append(PACKAGE_TYPE + c)
            .append(PIN_COUNT + c)
          .append(BODY_SIZE + c)
            .append(TEST_MODE + c)
            .append(TESTER_TYPE + c)
            .append(VENDOR_NAME + c);
      

      if (!APP_STATUS.equals("R") && !APP_STATUS.equals("C")) {
        sb.append(apl_status + c).append(app_status + c).append(app_type + c).append(APP_NO.trim() + c);
      }
      if (APP_STATUS.equals("R") || APP_STATUS.equals("C")) {
        sb.append(apl_status + c).append(app_status + c);
      }

      if (INITIAL_TIME == null)
        sb.append(c);
      else
        sb.append(sdf.format(INITIAL_TIME) + c);
      sb.append(APLUser.getUserByEmpNo(INITIAL_USER) + c);

      if (PROCESS_TIME == null)
        sb.append(c);
      else
        sb.append(sdf.format(PROCESS_TIME) + c);
      sb.append(APLUser.getUserByEmpNo(PROCESS_USER) + c);

      if (COMPLETE_TIME == null)
        sb.append(c);
      else
        sb.append(sdf.format(COMPLETE_TIME) + c);
      sb.append(APLUser.getUserByEmpNo(COMPLETE_USER));

      return sb.toString();
    }
  }
}
