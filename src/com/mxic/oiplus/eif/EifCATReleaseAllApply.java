package com.mxic.oiplus.eif;

import java.sql.*;
import java.util.*;

import com.mxic.oiplus.oimaintain.*;
import com.mxic.oiplus.pdf.pdfService;
import com.mxic.oiplus.resource.DBConnection;
import com.mxic.oiplus.resource.TDSResource;
import com.mxic.oiplus.util.*;

public class EifCATReleaseAllApply {

  // This will be scheduled for prepare CAT data
  // select approving (A) / release (R) oi
  //
  // Steps :
  // 1. Get log_time > sysdate -0.01 OI list from tf_information (status = 'A' or 'R')會簽中 or 已生效
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
	 if (EIFService.updateInterfaceTime("EifCATReleaseAllApply", "CURRENT_TIME", conn) == 1) {
	  if (args.length == 0) {
		  CATList = EifCATReleaseAllApplyService.getRecentlyOI(conn);
	  } else if (args.length == 4) {
		  CATList = EifCATReleaseAllApplyService.getRecentlyOIByPara(args[0], args[1], args[2], args[3], conn);
	  } else if (args.length == 1 ){ //Data Migration by product_type
		  //CATList = EifCATReleaseAllApplyService.getProductTypeOI(args[0], conn); 會簽中無須使用，這次都是生效OI過去
	  } else if (args.length == 2 ){ //Data Migration by product_type , Do Release New (First time migration need do ) ex: NVM New
		  CATList = EifCATReleaseAllApplyService.getProductTypeOICurrent(args[0], conn);
	  }else if (args.length == 3 ){ //Data Migration by product_type , Do Release New (First time migration need do ) ex: NVM New
		  CATList = EifCATReleaseAllApplyService.getProductTypeOICustom(conn);
	  }
	  else {
		  System.out.println("USAGE: EifCATReleaseAllApply [PRODUCT_BODY BRAND VERSION PRODUCT_TYPE]");
		  System.out.println("   EX: EifCATReleaseAllApply 6621 MX 1 NVM");
		  System.exit(0);
	  }
      String oi = null;
      boolean success = false;

      if (CATList == null) {
          TDSLogger.println("CAT ReleaseApply : Nothing to do !!");
          EIFService.updateInterfaceTime("EifCATReleaseAllApply", "LAST_TIME", conn);
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

          
          // insert CAT Table
          success = do_CATAllData(attr[0],attr[1],attr[2],attr[3],attr[4],attr[5], conn);
           
          String mailto = (String) TDSResource.getProperties("EIF").get("e8049OIReleaseAll.mailto");
          String mailfrom = (String) TDSResource.getProperties("EIF").get("e8049OIReleaseAll.mailfrom");
          String mailsubject = null;
          String mailbody = null;
          if (success) {// mark processed
              mailsubject = "CAT ReleaseAllApply Notice [Success]: " + attr[1]  + attr[2] + "V"+ attr[3] + attr[5];
              mailbody = mailsubject;
              TDSLogger.println(mailsubject);
          }
          else {
              mailsubject = "CAT ReleaseAllApply Notice [FAIL]: " + attr[1]  + attr[2] + "V"+ attr[3] + attr[5];
              mailbody = mailsubject;
              TDSLogger.println(mailsubject);
          }
          SendMail.send(mailto, mailfrom, mailsubject, mailbody, mailbody, null);

      }
      Iterator ii = CATList_ForMes.iterator();
      while (ii.hasNext())
      {
    	  String productBody = (String) ii.next();
    	  if("Y".equals(TDSResource.getProperties("EIF").get("e8049OIReleaseAll.insertMesTable"))){
    	  EifCATReleaseAllApplyService.insertMesTable(productBody);
    	  }
      }
      EIFService.updateInterfaceTime("EifCATReleaseAllApply", "LAST_TIME", conn);
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
  public static boolean do_CATAllData(String sid,
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
                  TDSLogger.println("make NVM CAT_ROUTE_PGM_ALL!!");
                  /*if("A".equals(status)){
                	  if ((EifCATReleaseAllApplyService.delete_data(conn, "CAT_ROUTE_PGM", product_body, brand, "A")) &&
                	   (EifCATReleaseAllApplyService.delete_data(conn, "CAT_ROUTE", product_body, brand, "A")) &&
                	   (EifCATReleaseAllApplyService.delete_data(conn, "CAT_ROUTE_TMP", product_body, brand, "A")) &&
                	   (EifCATReleaseAllApplyService.delete_data(conn, "CAT_PGM", product_body, brand, "A")) &&
                	   (EifCATReleaseAllApplyService.insert_cat_route_pgm_ws_NVM( fm, "_tx", product_type, status, conn)) &&
                	   (EifCATReleaseAllApplyService.insert_cat_route_pgm_ft_NVM( fm, "_tx", product_type, status, conn)) &&
                	   (EifCATReleaseAllApplyService.insert_cat_route_pgm_ws_MCP_NVM( fm, "_tx", product_type, status, conn)) &&
                	   (EifCATReleaseAllApplyService.insert_cat_route_pgm_ft_MCP_NVM( fm, "_tx", product_type, status, conn)) &&
                	   (EifCATReleaseAllApplyService.insert_cat_route_tmp1( fm, "_tx", product_type, status, conn)) &&
                	   (EifCATReleaseAllApplyService.insert_cat_route_tmp2( fm, "_tx", product_type, status, conn)) &&
                	   (EifCATReleaseAllApplyService.insert_cat_route( fm, status, conn)) &&
                	   (EifCATReleaseAllApplyService.insert_cat_pgm( fm, "_tx", status, product_type, conn)) )
                	  {
                		  TDSLogger.println("table records copy done, Success!!");
                		  //TDSLogger.println("table records copy done, going to commit!!");
                          //conn.commit();
                          //TDSLogger.println("table records committed!!");  
                      } else {
                          //DBConnection.rollback(conn);
                          TDSLogger.println(product_body + brand + " V" + version + " Fail !!");
                          EifCATReleaseAllApplyService.CATError_insert(conn, product_body, brand, version, status);
                          return false;
                      }
                  }else*/ 
                  if("R".equals(status)){
                	  if((EifCATReleaseAllApplyService.delete_data(conn, "CAT_ROUTE_PGM_ALL", product_body, brand, "")) &&
                	   (EifCATReleaseAllApplyService.delete_data(conn, "CAT_ROUTE_ALL", product_body, brand, "")) &&
                	   (EifCATReleaseAllApplyService.delete_data(conn, "CAT_ROUTE_ALL_TMP", product_body, brand, "")) && 
                	   (EifCATReleaseAllApplyService.delete_data(conn, "CAT_PGM_ALL", product_body, brand, "")) &&
                	   (EifCATReleaseAllApplyService.insert_cat_route_pgm_all_ws_NVM( fm, "", product_type, status, conn)) &&
                	   (EifCATReleaseAllApplyService.insert_cat_route_pgm_all_ft_NVM( fm, "", product_type, status, conn)) &&
                	   (EifCATReleaseAllApplyService.insert_cat_route_pgm_all_ws_MCP_NVM( fm, "", product_type, status, conn)) &&
                	   (EifCATReleaseAllApplyService.insert_cat_route_pgm_all_ft_MCP_NVM( fm, "", product_type, status, conn)) &&
                	   (EifCATReleaseAllApplyService.insert_cat_route_all_tmp1( fm, "", product_type, status, conn)) &&
                       (EifCATReleaseAllApplyService.insert_cat_route_all_tmp2( fm, "", product_type, status, conn)) &&
                       (EifCATReleaseAllApplyService.insert_cat_route_all( fm, status, conn))&&
                       (EifCATReleaseAllApplyService.insert_cat_pgm_all( fm, "", status, product_type, conn)) )  
                	  {
                		  TDSLogger.println("table records copy done, Success!!");
                		  //TDSLogger.println("table records copy done, going to commit!!");
                          //conn.commit();
                          //TDSLogger.println("table records committed!!"); 
                      } else {
                          //DBConnection.rollback(conn);
                          TDSLogger.println(product_body + brand + " V" + version + " Fail !!");
                          EifCATReleaseAllApplyService.CATAllError_insert(conn, product_body, brand, version, status);
                          return false;
                      }
                  }
                  /*else if("P".equals(status)){
                	  if((EifCATReleaseAllApplyService.delete_data(conn, "CAT_ROUTE_PGM", product_body, brand, "A")) &&
                	   (EifCATReleaseAllApplyService.delete_data(conn, "CAT_ROUTE", product_body, brand, "A")) &&
                	   (EifCATReleaseAllApplyService.delete_data(conn, "CAT_ROUTE_TMP", product_body, brand, "A")) && 
                	   (EifCATReleaseAllApplyService.delete_data(conn, "CAT_PGM", product_body, brand, "A")))  
                	  {
                		  TDSLogger.println("Delete Status A Data done (Status change to 'P'), Success!!");
                		  //TDSLogger.println("table records copy done, going to commit!!");
                          //conn.commit();
                          //TDSLogger.println("table records committed!!"); 
                      } else {
                          //DBConnection.rollback(conn);
                          TDSLogger.println(product_body + brand + " V" + version + " Delete Fail !!");
                          EifCATReleaseAllApplyService.CATError_insert(conn, product_body, brand, version, status);
                          return false;
                      }   
                  }
                  */

              } else if (product_type.equals("MROM")) {
            	  TDSLogger.println("make MROM CAT_ROUTE_PGM_ALL!!");
                  /*if("A".equals(status)){
                	  if ((EifCATReleaseAllApplyService.delete_data(conn, "CAT_ROUTE_PGM", product_body, brand, "A")) &&
                	   (EifCATReleaseAllApplyService.delete_data(conn, "CAT_ROUTE", product_body, brand, "A")) &&
                	   (EifCATReleaseAllApplyService.delete_data(conn, "CAT_ROUTE_TMP", product_body, brand, "A")) &&
                	   (EifCATReleaseAllApplyService.delete_data(conn, "CAT_PGM", product_body, brand, "A")) &&
                	   (EifCATReleaseAllApplyService.insert_cat_route_pgm_ws_MROM( fm, "_tx", product_type, status, conn)) &&
                	   (EifCATReleaseAllApplyService.insert_cat_route_pgm_ft_MROM( fm, "_tx", product_type, status, conn)) &&
                	   (EifCATReleaseAllApplyService.insert_cat_route_tmp1( fm, "_tx", product_type, status, conn)) &&
                	   (EifCATReleaseAllApplyService.insert_cat_route_tmp2( fm, "_tx", product_type, status, conn)) &&
                	   (EifCATReleaseAllApplyService.insert_cat_route( fm, status, conn)) &&
                	   (EifCATReleaseAllApplyService.insert_cat_pgm( fm, "_tx", status, product_type, conn)) )
                	  {
                		  TDSLogger.println("table records copy done, Success!!");
                		  //TDSLogger.println("table records copy done, going to commit!!");
                          //conn.commit();
                          //TDSLogger.println("table records committed!!");  
                      } else {
                          //DBConnection.rollback(conn);
                          TDSLogger.println(product_body + brand + " V" + version + " Fail !!");
                          EifCATReleaseAllApplyService.CATError_insert(conn, product_body, brand, version, status);
                          return false;
                      }
                  }else 
                  */if("R".equals(status)){
                	  if((EifCATReleaseAllApplyService.delete_data(conn, "CAT_ROUTE_PGM_ALL", product_body, brand, "")) &&
                	   (EifCATReleaseAllApplyService.delete_data(conn, "CAT_ROUTE_ALL", product_body, brand, "")) &&
                	   (EifCATReleaseAllApplyService.delete_data(conn, "CAT_ROUTE_ALL_TMP", product_body, brand, "")) && 
                	   (EifCATReleaseAllApplyService.delete_data(conn, "CAT_PGM_ALL", product_body, brand, "")) &&
                	   (EifCATReleaseAllApplyService.insert_cat_route_pgm_all_ws_MROM( fm, "", product_type, status, conn)) &&
                	   (EifCATReleaseAllApplyService.insert_cat_route_pgm_all_ft_MROM( fm, "", product_type, status, conn)) &&
                	   (EifCATReleaseAllApplyService.insert_cat_route_all_tmp1( fm, "", product_type, status, conn)) &&
                       (EifCATReleaseAllApplyService.insert_cat_route_all_tmp2( fm, "", product_type, status, conn)) &&
                       (EifCATReleaseAllApplyService.insert_cat_route_all( fm, status, conn))&&
                       (EifCATReleaseAllApplyService.insert_cat_pgm_all( fm, "", status, product_type, conn)) )  
                	  {
                		  TDSLogger.println("table records copy done, Success!!");
                		  //TDSLogger.println("table records copy done, going to commit!!");
                          //conn.commit();
                          //TDSLogger.println("table records committed!!"); 
                      } else {
                          //DBConnection.rollback(conn);
                          TDSLogger.println(product_body + brand + " V" + version + " Fail !!");
                          EifCATReleaseAllApplyService.CATAllError_insert(conn, product_body, brand, version, status);
                          return false;
                      } 
                  }
                  /*else if("P".equals(status)){
                	  if((EifCATReleaseAllApplyService.delete_data(conn, "CAT_ROUTE_PGM", product_body, brand, "A")) &&
                	   (EifCATReleaseAllApplyService.delete_data(conn, "CAT_ROUTE", product_body, brand, "A")) &&
                	   (EifCATReleaseAllApplyService.delete_data(conn, "CAT_ROUTE_TMP", product_body, brand, "A")) && 
                	   (EifCATReleaseAllApplyService.delete_data(conn, "CAT_PGM", product_body, brand, "A")))  
                	  {
                		  TDSLogger.println("Delete Status A Data done (Status change to 'P'), Success!!");
                		  //TDSLogger.println("table records copy done, going to commit!!");
                          //conn.commit();
                          //TDSLogger.println("table records committed!!"); 
                      } else {
                          //DBConnection.rollback(conn);
                          TDSLogger.println(product_body + brand + " V" + version + " Delete Fail !!");
                          EifCATReleaseAllApplyService.CATError_insert(conn, product_body, brand, version, status);
                          return false;
                      }   	  
                  }
                  */
              } else if (product_type.equals("XROM")) {
            	  TDSLogger.println("make XROM CAT_ROUTE_PGM_ALL!!");
                  /*if("A".equals(status)){
                	  if ((EifCATReleaseAllApplyService.delete_data(conn, "CAT_ROUTE_PGM", product_body, brand, "A")) &&
                	   (EifCATReleaseAllApplyService.delete_data(conn, "CAT_ROUTE", product_body, brand, "A")) &&
                	   (EifCATReleaseAllApplyService.delete_data(conn, "CAT_ROUTE_TMP", product_body, brand, "A")) &&
                	   (EifCATReleaseAllApplyService.delete_data(conn, "CAT_PGM", product_body, brand, "A")) &&
                	   (EifCATReleaseAllApplyService.insert_cat_route_pgm_ws_XROM( fm, "_tx", product_type, status, conn)) &&
                	   (EifCATReleaseAllApplyService.insert_cat_route_pgm_ft_XROM( fm, "_tx", product_type, status, conn)) &&
                	   (EifCATReleaseAllApplyService.insert_cat_route_tmp1( fm, "_tx", product_type, status, conn)) &&
                	   (EifCATReleaseAllApplyService.insert_cat_route_tmp2( fm, "_tx", product_type, status, conn)) &&
                	   (EifCATReleaseAllApplyService.insert_cat_route( fm, status, conn)) &&
                	   (EifCATReleaseAllApplyService.insert_cat_pgm( fm, "_tx", status, product_type, conn)) )
                	  {
                		  TDSLogger.println("table records copy done, Success!!");
                		  //TDSLogger.println("table records copy done, going to commit!!");
                          //conn.commit();
                          //TDSLogger.println("table records committed!!");  
                      } else {
                          //DBConnection.rollback(conn);
                          TDSLogger.println(product_body + brand + " V" + version + " Fail !!");
                          EifCATReleaseAllApplyService.CATError_insert(conn, product_body, brand, version, status);
                          return false;
                      }
                  }else */
                  if("R".equals(status)){
                	  if((EifCATReleaseAllApplyService.delete_data(conn, "CAT_ROUTE_PGM_ALL", product_body, brand, "")) &&
                	   (EifCATReleaseAllApplyService.delete_data(conn, "CAT_ROUTE_ALL", product_body, brand, "")) &&
                	   (EifCATReleaseAllApplyService.delete_data(conn, "CAT_ROUTE_ALL_TMP", product_body, brand, "")) && 
                	   (EifCATReleaseAllApplyService.delete_data(conn, "CAT_PGM_ALL", product_body, brand, "")) &&
                	   (EifCATReleaseAllApplyService.insert_cat_route_pgm_all_ws_XROM( fm, "", product_type, status, conn)) &&
                	   (EifCATReleaseAllApplyService.insert_cat_route_pgm_all_ft_XROM( fm, "", product_type, status, conn)) &&
                	   (EifCATReleaseAllApplyService.insert_cat_route_all_tmp1( fm, "", product_type, status, conn)) &&
                       (EifCATReleaseAllApplyService.insert_cat_route_all_tmp2( fm, "", product_type, status, conn)) &&
                       (EifCATReleaseAllApplyService.insert_cat_route_all( fm, status, conn))&&
                       (EifCATReleaseAllApplyService.insert_cat_pgm_all( fm, "", status, product_type, conn)) )  
                	  {
                		  TDSLogger.println("table records copy done, Success!!");
                		  //TDSLogger.println("table records copy done, going to commit!!");
                          //conn.commit();
                          //TDSLogger.println("table records committed!!"); 
                      } else {
                          //DBConnection.rollback(conn);
                          TDSLogger.println(product_body + brand + " V" + version + " Fail !!");
                          EifCATReleaseAllApplyService.CATAllError_insert(conn, product_body, brand, version, status);
                          return false;
                      }
                  }
                  /*else if("P".equals(status)){
                	  if((EifCATReleaseAllApplyService.delete_data(conn, "CAT_ROUTE_PGM", product_body, brand, "A")) &&
                	   (EifCATReleaseAllApplyService.delete_data(conn, "CAT_ROUTE", product_body, brand, "A")) &&
                	   (EifCATReleaseAllApplyService.delete_data(conn, "CAT_ROUTE_TMP", product_body, brand, "A")) && 
                	   (EifCATReleaseAllApplyService.delete_data(conn, "CAT_PGM", product_body, brand, "A")))  
                	  {
                		  TDSLogger.println("Delete Status A Data done (Status change to 'P'), Success!!");
                		  //TDSLogger.println("table records copy done, going to commit!!");
                          //conn.commit();
                          //TDSLogger.println("table records committed!!"); 
                      } else {
                          //DBConnection.rollback(conn);
                          TDSLogger.println(product_body + brand + " V" + version + " Delete Fail !!");
                          EifCATReleaseAllApplyService.CATError_insert(conn, product_body, brand, version, status);
                          return false;
                      }   	  
                  }*/
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
              else if (product_type.equals("MMS")) {
            	  if("R".equals(status)){
                	  if((EifCATReleaseAllApplyService.delete_data(conn, "CAT_ROUTE_PGM_ALL", product_body, brand, "")) &&
                	   (EifCATReleaseAllApplyService.delete_data(conn, "CAT_ROUTE_ALL", product_body, brand, "")) &&
                	   (EifCATReleaseAllApplyService.delete_data(conn, "CAT_ROUTE_ALL_TMP", product_body, brand, "")) && 
                	   (EifCATReleaseAllApplyService.delete_data(conn, "CAT_PGM_ALL", product_body, brand, "")) &&
                	   (EifCATReleaseAllApplyService.insert_cat_route_pgm_all_ft_MMS( fm, "", product_type, status, conn)) &&
                	   (EifCATReleaseAllApplyService.insert_cat_route_all_tmp1( fm, "", product_type, status, conn)) &&
                       (EifCATReleaseAllApplyService.insert_cat_route_all_tmp2( fm, "", product_type, status, conn)) &&
                       (EifCATReleaseAllApplyService.insert_cat_route_all( fm, status, conn))&&
                       (EifCATReleaseAllApplyService.insert_cat_pgm_all( fm, "", status, product_type, conn)) )  
                	  {
                		  TDSLogger.println("table records copy done, Success!!");
                		  //TDSLogger.println("table records copy done, going to commit!!");
                          //conn.commit();
                          //TDSLogger.println("table records committed!!"); 
                      } else {
                          //DBConnection.rollback(conn);
                          TDSLogger.println(product_body + brand + " V" + version + " Fail !!");
                          EifCATReleaseAllApplyService.CATAllError_insert(conn, product_body, brand, version, status);
                          return false;
                      }
                  }
              }
      }
      catch (Exception ex) {
          //DBConnection.rollback(conn);
          TDSLogger.println(ex);
          TDSLogger.println(product_body+brand+" V"+version+" Fail !!");
          EifCATReleaseAllApplyService.CATAllError_insert(conn, product_body, brand, version, status);
          return false;
      }
      finally {
          //DBConnection.close(conn);
      }
      return true;
  }

  

}
