package com.mxic.oiplus.eif;

import java.sql.*;
import java.util.*;

import com.mxic.oiplus.oimaintain.*;
import com.mxic.oiplus.pdf.pdfService;
import com.mxic.oiplus.resource.DBConnection;
import com.mxic.oiplus.resource.TDSResource;
import com.mxic.oiplus.util.*;

public class EifCATReleaseApply {

  // This will be scheduled for prepare CAT data
  // select approving (A) / release (R) oi
  //
  // Steps :
  // 1. Get log_time > sysdate -0.01 OI list from tf_information (status = 'A' or 'R')
  // 2. if status = 'A' : delete same body+brand+version+status='A' data in CAT_ROUTE_PGM / CAT_ROUTE / CAT_PGM
  //	if status = 'R' : delete same body+brand+version+status='R' data in CAT_ROUTE_PGM / CAT_ROUTE / CAT_PGM
  // 3. insert status = 'A' or 'R' data to CAT_ROUTE_PGM / CAT_ROUTE / CAT_PGM table for cat_route_view & cat_pgm_view used
  // 4. if execute DML period happen error , insert dat to cat_error table
  
  public static void main(String[] args) {
	ArrayList CATList = null;
	ArrayList CATList_ForMes = new ArrayList();
	Connection conn = null;
	
	try {
	 conn = DBConnection.getConnection();	
	 if (EIFService.updateInterfaceTime("EifCATReleaseApply", "CURRENT_TIME", conn) == 1) {
	  if (args.length == 0) {
		  CATList = EifCATReleaseApplyService.getRecentlyOI(conn);
	  } else if (args.length == 4) {
		  CATList = EifCATReleaseApplyService.getRecentlyOIByPara(args[0], args[1], args[2], args[3], conn);
	  } else if (args.length == 1 ){ //Data Migration by product_type
		  CATList = EifCATReleaseApplyService.getProductTypeOI(args[0], conn);
	  } else if (args.length == 2 ){ //Data Migration by product_type , Do Release New (First time migration need do ) ex: NVM New
		  CATList = EifCATReleaseApplyService.getProductTypeOICurrent(args[0], conn);
	  } else {
		  System.out.println("USAGE: EifCATReleaseApply [PRODUCT_BODY BRAND VERSION PRODUCT_TYPE]");
		  System.out.println("   EX: EifCATReleaseApply 6621 MX 1 NVM");
		  System.exit(0);
	  }
      String oi = null;
      boolean success = false;

      if (CATList == null) {
          TDSLogger.println("CAT ReleaseApply : Nothing to do !!");
          EIFService.updateInterfaceTime("EifCATReleaseApply", "LAST_TIME", conn);
          return;
      }
      Iterator i = CATList.iterator();
      while (i.hasNext())
      {
          oi = (String) i.next();
          String attr[] = oi.split(",");
          success = false;
          
          if(!CATList_ForMes.contains(attr[1])) {
        	  CATList_ForMes.add(attr[1]);
		  }
          

          TDSLogger.println("Processing CAT: "+attr[0] + " " + attr[1] + " " + attr[2] + " " + attr[3] + " " + attr[4]);
          //oisid = EifCATReleaseApplyService.getOIsid(attr[0],attr[1],attr[2],attr[3],attr[4]);
          
          // insert CAT Table
          success = do_CATData(attr[0],attr[1],attr[2],attr[3],attr[4],attr[5], conn);
           
          String mailto = (String) TDSResource.getProperties("EIF").get("e8049OIRelease.mailto");
          String mailfrom = (String) TDSResource.getProperties("EIF").get("e8049OIRelease.mailfrom");
          String mailsubject = null;
          String mailbody = null;
          if (success) {// mark processed
              mailsubject = "CAT ReleaseApply Notice [Success]: " + attr[1]  + attr[2] + "V"+ attr[3] + attr[5];
              mailbody = mailsubject;
              TDSLogger.println(mailsubject);
          }
          else {
              mailsubject = "CAT ReleaseApply Notice [FAIL]: " + attr[1]  + attr[2] + "V"+ attr[3] + attr[5];
              mailbody = mailsubject;
              TDSLogger.println(mailsubject);
          }
          SendMail.send(mailto, mailfrom, mailsubject, mailbody, mailbody, null);

      }
      Iterator ii = CATList_ForMes.iterator();
      while (ii.hasNext())
      {
    	  String productBody = (String) ii.next();
    	  EifCATReleaseApplyService.insertMesTable(productBody);
      }
      EIFService.updateInterfaceTime("EifCATReleaseApply", "LAST_TIME", conn);
	 }
	} catch (Exception e) {
		TDSLogger.println(e);
	}finally {
        DBConnection.close(conn);
    }
// for test
//      do_release(args[0],args[1],args[2],args[3]);
  }

  // insert CAT Table (CAT_ROUTE_PGM / CAT_ROUTE / CAT_PGM)
  //Step1. 舊資料刪除
  //       若為送會簽資料，則先刪除 CAT_ROUTE_PGM / CAT_ROUTE / CAT_PGM 中，相同 product body + brand 且 status 為會簽中的資料；
  //       若為生效資料，則刪除 CAT_ROUTE_PGM / CAT_ROUTE / CAT_PGM 中，所有相同 product body + brand 的資料
  //Step2. 資料建立
  public static boolean do_CATData(String sid,
                                   String product_body,
                                   String brand,
                                   String version,
                                   String status,
                                   String product_type,
                                   Connection conn) {
      //Connection conn = null;
      ProTestRouteBeanAF fm = 
    	  new ProTestRouteBeanAF();
      fm.setSid(sid);
      fm.setProductbody(product_body);
      fm.setBrand(brand);
      fm.setVersion(version);

      try {
          //conn = DBConnection.getConnection();
          //conn.setAutoCommit(false);

              
              if (product_type.equals("NVM")) {
                  TDSLogger.println("make NVM CAT_ROUTE_PGM!!");
                  if("A".equals(status)){
                	  if ((EifCATReleaseApplyService.delete_data(conn, "CAT_ROUTE_PGM", product_body, brand, "A")) &&
                	   (EifCATReleaseApplyService.delete_data(conn, "CAT_ROUTE", product_body, brand, "A")) &&
                	   (EifCATReleaseApplyService.delete_data(conn, "CAT_ROUTE_TMP", product_body, brand, "A")) &&
                	   (EifCATReleaseApplyService.delete_data(conn, "CAT_PGM", product_body, brand, "A")) &&
                	   (EifCATReleaseApplyService.insert_cat_route_pgm_ws_NVM( fm, "_tx", product_type, status, conn)) &&
                	   (EifCATReleaseApplyService.insert_cat_route_pgm_ft_NVM( fm, "_tx", product_type, status, conn)) &&
                	   (EifCATReleaseApplyService.insert_cat_route_pgm_ws_MCP_NVM( fm, "_tx", product_type, status, conn)) &&
                	   (EifCATReleaseApplyService.insert_cat_route_pgm_ft_MCP_NVM( fm, "_tx", product_type, status, conn)) &&
                	   (EifCATReleaseApplyService.insert_cat_route_tmp1( fm, "_tx", product_type, status, conn)) &&
                	   (EifCATReleaseApplyService.insert_cat_route_tmp2( fm, "_tx", product_type, status, conn)) &&
                	   (EifCATReleaseApplyService.insert_cat_route( fm, status, conn)) &&
                	   (EifCATReleaseApplyService.insert_cat_pgm( fm, "_tx", status, product_type, conn)) )
                	  {
                		  TDSLogger.println("table records copy done, Success!!");
                		  //TDSLogger.println("table records copy done, going to commit!!");
                          //conn.commit();
                          //TDSLogger.println("table records committed!!");  
                      } else {
                          //DBConnection.rollback(conn);
                          TDSLogger.println(product_body + brand + " V" + version + " Fail !!");
                          EifCATReleaseApplyService.CATError_insert(conn, product_body, brand, version, status);
                          return false;
                      }
                  }else if("R".equals(status)){
                	  if((EifCATReleaseApplyService.delete_data(conn, "CAT_ROUTE_PGM", product_body, brand, "")) &&
                	   (EifCATReleaseApplyService.delete_data(conn, "CAT_ROUTE", product_body, brand, "")) &&
                	   (EifCATReleaseApplyService.delete_data(conn, "CAT_ROUTE_TMP", product_body, brand, "")) && 
                	   (EifCATReleaseApplyService.delete_data(conn, "CAT_PGM", product_body, brand, "")) &&
                	   (EifCATReleaseApplyService.insert_cat_route_pgm_ws_NVM( fm, "", product_type, status, conn)) &&
                	   (EifCATReleaseApplyService.insert_cat_route_pgm_ft_NVM( fm, "", product_type, status, conn)) &&
                	   (EifCATReleaseApplyService.insert_cat_route_pgm_ws_MCP_NVM( fm, "", product_type, status, conn)) &&
                	   (EifCATReleaseApplyService.insert_cat_route_pgm_ft_MCP_NVM( fm, "", product_type, status, conn)) &&
                	   (EifCATReleaseApplyService.insert_cat_route_tmp1( fm, "", product_type, status, conn)) &&
                       (EifCATReleaseApplyService.insert_cat_route_tmp2( fm, "", product_type, status, conn)) &&
                       (EifCATReleaseApplyService.insert_cat_route( fm, status, conn))&&
                       (EifCATReleaseApplyService.insert_cat_pgm( fm, "", status, product_type, conn)) )  
                	  {
                		  TDSLogger.println("table records copy done, Success!!");
                		  //TDSLogger.println("table records copy done, going to commit!!");
                          //conn.commit();
                          //TDSLogger.println("table records committed!!"); 
                      } else {
                          //DBConnection.rollback(conn);
                          TDSLogger.println(product_body + brand + " V" + version + " Fail !!");
                          EifCATReleaseApplyService.CATError_insert(conn, product_body, brand, version, status);
                          return false;
                      }
                  }else if("P".equals(status)){
                	  if((EifCATReleaseApplyService.delete_data(conn, "CAT_ROUTE_PGM", product_body, brand, "A")) &&
                	   (EifCATReleaseApplyService.delete_data(conn, "CAT_ROUTE", product_body, brand, "A")) &&
                	   (EifCATReleaseApplyService.delete_data(conn, "CAT_ROUTE_TMP", product_body, brand, "A")) && 
                	   (EifCATReleaseApplyService.delete_data(conn, "CAT_PGM", product_body, brand, "A")))  
                	  {
                		  TDSLogger.println("Delete Status A Data done (Status change to 'P'), Success!!");
                		  //TDSLogger.println("table records copy done, going to commit!!");
                          //conn.commit();
                          //TDSLogger.println("table records committed!!"); 
                      } else {
                          //DBConnection.rollback(conn);
                          TDSLogger.println(product_body + brand + " V" + version + " Delete Fail !!");
                          EifCATReleaseApplyService.CATError_insert(conn, product_body, brand, version, status);
                          return false;
                      }   
                  }

              } else if (product_type.equals("MROM")) {
            	  TDSLogger.println("make MROM CAT_ROUTE_PGM!!");
                  if("A".equals(status)){
                	  if ((EifCATReleaseApplyService.delete_data(conn, "CAT_ROUTE_PGM", product_body, brand, "A")) &&
                	   (EifCATReleaseApplyService.delete_data(conn, "CAT_ROUTE", product_body, brand, "A")) &&
                	   (EifCATReleaseApplyService.delete_data(conn, "CAT_ROUTE_TMP", product_body, brand, "A")) &&
                	   (EifCATReleaseApplyService.delete_data(conn, "CAT_PGM", product_body, brand, "A")) &&
                	   (EifCATReleaseApplyService.insert_cat_route_pgm_ws_MROM( fm, "_tx", product_type, status, conn)) &&
                	   (EifCATReleaseApplyService.insert_cat_route_pgm_ft_MROM( fm, "_tx", product_type, status, conn)) &&
                	   (EifCATReleaseApplyService.insert_cat_route_tmp1( fm, "_tx", product_type, status, conn)) &&
                	   (EifCATReleaseApplyService.insert_cat_route_tmp2( fm, "_tx", product_type, status, conn)) &&
                	   (EifCATReleaseApplyService.insert_cat_route( fm, status, conn)) &&
                	   (EifCATReleaseApplyService.insert_cat_pgm( fm, "_tx", status, product_type, conn)) )
                	  {
                		  TDSLogger.println("table records copy done, Success!!");
                		  //TDSLogger.println("table records copy done, going to commit!!");
                          //conn.commit();
                          //TDSLogger.println("table records committed!!");  
                      } else {
                          //DBConnection.rollback(conn);
                          TDSLogger.println(product_body + brand + " V" + version + " Fail !!");
                          EifCATReleaseApplyService.CATError_insert(conn, product_body, brand, version, status);
                          return false;
                      }
                  }else if("R".equals(status)){
                	  if((EifCATReleaseApplyService.delete_data(conn, "CAT_ROUTE_PGM", product_body, brand, "")) &&
                	   (EifCATReleaseApplyService.delete_data(conn, "CAT_ROUTE", product_body, brand, "")) &&
                	   (EifCATReleaseApplyService.delete_data(conn, "CAT_ROUTE_TMP", product_body, brand, "")) && 
                	   (EifCATReleaseApplyService.delete_data(conn, "CAT_PGM", product_body, brand, "")) &&
                	   (EifCATReleaseApplyService.insert_cat_route_pgm_ws_MROM( fm, "", product_type, status, conn)) &&
                	   (EifCATReleaseApplyService.insert_cat_route_pgm_ft_MROM( fm, "", product_type, status, conn)) &&
                	   (EifCATReleaseApplyService.insert_cat_route_tmp1( fm, "", product_type, status, conn)) &&
                       (EifCATReleaseApplyService.insert_cat_route_tmp2( fm, "", product_type, status, conn)) &&
                       (EifCATReleaseApplyService.insert_cat_route( fm, status, conn))&&
                       (EifCATReleaseApplyService.insert_cat_pgm( fm, "", status, product_type, conn)) )  
                	  {
                		  TDSLogger.println("table records copy done, Success!!");
                		  //TDSLogger.println("table records copy done, going to commit!!");
                          //conn.commit();
                          //TDSLogger.println("table records committed!!"); 
                      } else {
                          //DBConnection.rollback(conn);
                          TDSLogger.println(product_body + brand + " V" + version + " Fail !!");
                          EifCATReleaseApplyService.CATError_insert(conn, product_body, brand, version, status);
                          return false;
                      } 
                  }else if("P".equals(status)){
                	  if((EifCATReleaseApplyService.delete_data(conn, "CAT_ROUTE_PGM", product_body, brand, "A")) &&
                	   (EifCATReleaseApplyService.delete_data(conn, "CAT_ROUTE", product_body, brand, "A")) &&
                	   (EifCATReleaseApplyService.delete_data(conn, "CAT_ROUTE_TMP", product_body, brand, "A")) && 
                	   (EifCATReleaseApplyService.delete_data(conn, "CAT_PGM", product_body, brand, "A")))  
                	  {
                		  TDSLogger.println("Delete Status A Data done (Status change to 'P'), Success!!");
                		  //TDSLogger.println("table records copy done, going to commit!!");
                          //conn.commit();
                          //TDSLogger.println("table records committed!!"); 
                      } else {
                          //DBConnection.rollback(conn);
                          TDSLogger.println(product_body + brand + " V" + version + " Delete Fail !!");
                          EifCATReleaseApplyService.CATError_insert(conn, product_body, brand, version, status);
                          return false;
                      }   	  
                  }
              } else if (product_type.equals("XROM")) {
            	  TDSLogger.println("make XROM CAT_ROUTE_PGM!!");
                  if("A".equals(status)){
                	  if ((EifCATReleaseApplyService.delete_data(conn, "CAT_ROUTE_PGM", product_body, brand, "A")) &&
                	   (EifCATReleaseApplyService.delete_data(conn, "CAT_ROUTE", product_body, brand, "A")) &&
                	   (EifCATReleaseApplyService.delete_data(conn, "CAT_ROUTE_TMP", product_body, brand, "A")) &&
                	   (EifCATReleaseApplyService.delete_data(conn, "CAT_PGM", product_body, brand, "A")) &&
                	   (EifCATReleaseApplyService.insert_cat_route_pgm_ws_XROM( fm, "_tx", product_type, status, conn)) &&
                	   (EifCATReleaseApplyService.insert_cat_route_pgm_ft_XROM( fm, "_tx", product_type, status, conn)) &&
                	   (EifCATReleaseApplyService.insert_cat_route_tmp1( fm, "_tx", product_type, status, conn)) &&
                	   (EifCATReleaseApplyService.insert_cat_route_tmp2( fm, "_tx", product_type, status, conn)) &&
                	   (EifCATReleaseApplyService.insert_cat_route( fm, status, conn)) &&
                	   (EifCATReleaseApplyService.insert_cat_pgm( fm, "_tx", status, product_type, conn)) )
                	  {
                		  TDSLogger.println("table records copy done, Success!!");
                		  //TDSLogger.println("table records copy done, going to commit!!");
                          //conn.commit();
                          //TDSLogger.println("table records committed!!");  
                      } else {
                          //DBConnection.rollback(conn);
                          TDSLogger.println(product_body + brand + " V" + version + " Fail !!");
                          EifCATReleaseApplyService.CATError_insert(conn, product_body, brand, version, status);
                          return false;
                      }
                  }else if("R".equals(status)){
                	  if((EifCATReleaseApplyService.delete_data(conn, "CAT_ROUTE_PGM", product_body, brand, "")) &&
                	   (EifCATReleaseApplyService.delete_data(conn, "CAT_ROUTE", product_body, brand, "")) &&
                	   (EifCATReleaseApplyService.delete_data(conn, "CAT_ROUTE_TMP", product_body, brand, "")) && 
                	   (EifCATReleaseApplyService.delete_data(conn, "CAT_PGM", product_body, brand, "")) &&
                	   (EifCATReleaseApplyService.insert_cat_route_pgm_ws_XROM( fm, "", product_type, status, conn)) &&
                	   (EifCATReleaseApplyService.insert_cat_route_pgm_ft_XROM( fm, "", product_type, status, conn)) &&
                	   (EifCATReleaseApplyService.insert_cat_route_tmp1( fm, "", product_type, status, conn)) &&
                       (EifCATReleaseApplyService.insert_cat_route_tmp2( fm, "", product_type, status, conn)) &&
                       (EifCATReleaseApplyService.insert_cat_route( fm, status, conn))&&
                       (EifCATReleaseApplyService.insert_cat_pgm( fm, "", status, product_type, conn)) )  
                	  {
                		  TDSLogger.println("table records copy done, Success!!");
                		  //TDSLogger.println("table records copy done, going to commit!!");
                          //conn.commit();
                          //TDSLogger.println("table records committed!!"); 
                      } else {
                          //DBConnection.rollback(conn);
                          TDSLogger.println(product_body + brand + " V" + version + " Fail !!");
                          EifCATReleaseApplyService.CATError_insert(conn, product_body, brand, version, status);
                          return false;
                      }
                  }else if("P".equals(status)){
                	  if((EifCATReleaseApplyService.delete_data(conn, "CAT_ROUTE_PGM", product_body, brand, "A")) &&
                	   (EifCATReleaseApplyService.delete_data(conn, "CAT_ROUTE", product_body, brand, "A")) &&
                	   (EifCATReleaseApplyService.delete_data(conn, "CAT_ROUTE_TMP", product_body, brand, "A")) && 
                	   (EifCATReleaseApplyService.delete_data(conn, "CAT_PGM", product_body, brand, "A")))  
                	  {
                		  TDSLogger.println("Delete Status A Data done (Status change to 'P'), Success!!");
                		  //TDSLogger.println("table records copy done, going to commit!!");
                          //conn.commit();
                          //TDSLogger.println("table records committed!!"); 
                      } else {
                          //DBConnection.rollback(conn);
                          TDSLogger.println(product_body + brand + " V" + version + " Delete Fail !!");
                          EifCATReleaseApplyService.CATError_insert(conn, product_body, brand, version, status);
                          return false;
                      }   	  
                  }
              } else if (product_type.equals("MMS")) {
            	  TDSLogger.println("make MMS CAT_ROUTE_PGM!!");
                  if("A".equals(status)){
                	  if ((EifCATReleaseApplyService.delete_data(conn, "CAT_ROUTE_PGM", product_body, brand, "A")) &&
                	   (EifCATReleaseApplyService.delete_data(conn, "CAT_ROUTE", product_body, brand, "A")) &&
                	   (EifCATReleaseApplyService.delete_data(conn, "CAT_ROUTE_TMP", product_body, brand, "A")) &&
                	   (EifCATReleaseApplyService.delete_data(conn, "CAT_PGM", product_body, brand, "A")) &&
                	   (EifCATReleaseApplyService.insert_cat_route_pgm_ft_MMS( fm, "_tx", product_type, status, conn)) &&
                	   (EifCATReleaseApplyService.insert_cat_route_tmp1( fm, "_tx", product_type, status, conn)) &&
                	   (EifCATReleaseApplyService.insert_cat_route_tmp2( fm, "_tx", product_type, status, conn)) &&
                	   (EifCATReleaseApplyService.insert_cat_route( fm, status, conn)) &&
                	   (EifCATReleaseApplyService.insert_cat_pgm( fm, "_tx", status, product_type, conn)) )
                	  {
                		  TDSLogger.println("table records copy done, Success!!");
                		  //TDSLogger.println("table records copy done, going to commit!!");
                          //conn.commit();
                          //TDSLogger.println("table records committed!!");  
                      } else {
                          //DBConnection.rollback(conn);
                          TDSLogger.println(product_body + brand + " V" + version + " Fail !!");
                          EifCATReleaseApplyService.CATError_insert(conn, product_body, brand, version, status);
                          return false;
                      }
                  }else if("R".equals(status)){
                	  if((EifCATReleaseApplyService.delete_data(conn, "CAT_ROUTE_PGM", product_body, brand, "")) &&
                	   (EifCATReleaseApplyService.delete_data(conn, "CAT_ROUTE", product_body, brand, "")) &&
                	   (EifCATReleaseApplyService.delete_data(conn, "CAT_ROUTE_TMP", product_body, brand, "")) && 
                	   (EifCATReleaseApplyService.delete_data(conn, "CAT_PGM", product_body, brand, "")) &&
                	   (EifCATReleaseApplyService.insert_cat_route_pgm_ft_MMS( fm, "", product_type, status, conn)) &&
                	   (EifCATReleaseApplyService.insert_cat_route_tmp1( fm, "", product_type, status, conn)) &&
                       (EifCATReleaseApplyService.insert_cat_route_tmp2( fm, "", product_type, status, conn)) &&
                       (EifCATReleaseApplyService.insert_cat_route( fm, status, conn))&&
                       (EifCATReleaseApplyService.insert_cat_pgm( fm, "", status, product_type, conn)) )  
                	  {
                		  TDSLogger.println("table records copy done, Success!!");
                		  //TDSLogger.println("table records copy done, going to commit!!");
                          //conn.commit();
                          //TDSLogger.println("table records committed!!"); 
                      } else {
                          //DBConnection.rollback(conn);
                          TDSLogger.println(product_body + brand + " V" + version + " Fail !!");
                          EifCATReleaseApplyService.CATError_insert(conn, product_body, brand, version, status);
                          return false;
                      }
                  }else if("P".equals(status)){
                	  if((EifCATReleaseApplyService.delete_data(conn, "CAT_ROUTE_PGM", product_body, brand, "A")) &&
                	   (EifCATReleaseApplyService.delete_data(conn, "CAT_ROUTE", product_body, brand, "A")) &&
                	   (EifCATReleaseApplyService.delete_data(conn, "CAT_ROUTE_TMP", product_body, brand, "A")) && 
                	   (EifCATReleaseApplyService.delete_data(conn, "CAT_PGM", product_body, brand, "A")))  
                	  {
                		  TDSLogger.println("Delete Status A Data done (Status change to 'P'), Success!!");
                		  //TDSLogger.println("table records copy done, going to commit!!");
                          //conn.commit();
                          //TDSLogger.println("table records committed!!"); 
                      } else {
                          //DBConnection.rollback(conn);
                          TDSLogger.println(product_body + brand + " V" + version + " Delete Fail !!");
                          EifCATReleaseApplyService.CATError_insert(conn, product_body, brand, version, status);
                          return false;
                      }   	  
                  }
              }
              

              /*ArrayList vl = EifOIReleaseService.getVendorList(conn,sid);

              Iterator i = vl.iterator();
              String vendor;
              while (i.hasNext())
              {
                   vendor = (String) i.next();
                   if (!vendor.equals("TEST2")) {
                	   fm.setVendor(vendor);
                       TDSLogger.println("Make subcon "+vendor+ "pdf..");
                       if (product_type.equals("NVM"))
                    	   com.mxic.oiplus.pdf.MakeVendorPDF.makePDF(fm, "R");
                       else if (product_type.equals("MROM"))
                    	   com.mxic.oiplus.mrom.pdf.MakeVendorPDF.makePDF(fm,"R");
                       else if (product_type.equals("XROM"))
                    	   com.mxic.oiplus.xtrarom.pdf.MakeVendorPDF.makePDF(fm,"R");
                   }
              }

              TDSLogger.println("FTP PDFs to DCC...");
              //EifOIReleaseService.ftp_to_dcc(product_body, brand, version);
              //20091013 move from if... to here
              TDSLogger.println("Remove all *tx pdf files...");
              EifOIReleaseService.deletePDFFile(product_body, brand, version);
              */
             
      }
      catch (Exception ex) {
          //DBConnection.rollback(conn);
          TDSLogger.println(ex);
          TDSLogger.println(product_body+brand+" V"+version+" Fail !!");
          EifCATReleaseApplyService.CATError_insert(conn, product_body, brand, version, status);
          return false;
      }
      finally {
          //DBConnection.close(conn);
      }
      return true;
  }

  

}
