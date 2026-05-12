package com.mxic.oiplus.eif;

import java.sql.*;
import java.util.*;

import com.mxic.oiplus.oimaintain.*;
import com.mxic.oiplus.resource.DBConnection;
import com.mxic.oiplus.resource.TDSResource;
import com.mxic.oiplus.util.*;

public class EifOIReleasePDF {

  // This will be scheduled for releasing the OI
  // Notes will insert data into TF_IF_COVERPAGE while an OI was approved, rejected or do_obsolesced
  //
  // Steps :
  // 1. Get non-processed OI list from TF_IF_COVERPAGE (status = 'N')
  // 2. for each item from step 1. mark the status as 'I' to indicate the item was in-processed
  // 3. check if TIM has such OI (in approve status)
  // 4. according the Notes status, process each OI
  // 5. set the OI is processed (TF_IF_COVERPAGE status = 'Y')
  public static void main(String[] args) {
	  ArrayList OIList = null;
	  Connection conn = null;
	try {
	  conn = DBConnection.getConnection();
	  if (args.length == 0) {
		  OIList = EifOIReleaseService.getNonProcessedOI(conn);
	  } else if (args.length == 3) {
		  OIList = EifOIReleaseService.getOICoverPage(conn, args[0], args[1], args[2]);
	  } else if (args.length == 4) {
		  OIList = EifOIReleaseService.getOIByUser(conn, args[0], args[1], args[2], args[3]);		    
	  } else {
		  System.out.println("USAGE: EifOIRelease [CASENO DOCNO REV]");
		  System.out.println("   EX: EifOIRelease CG1001 8049-6609 1");
		  System.exit(0);
	  }
      String oi = null;
      boolean success = false;
      String oisid;

      if (OIList == null) {
          TDSLogger.println("OI Release: Nothing to do !!");
          return;
      }
      Iterator i = OIList.iterator();
      while (i.hasNext())
      {
          oi = (String) i.next();
          String attr[] = oi.split(",");
          success = false;

          TDSLogger.println("Processing OI: "+attr[0]  + attr[2] + "V"+ attr[1]);
          // mark this oi is in process
          
          oisid = EifOIReleaseService.getOIsid(conn, attr[0],attr[1],attr[2],"R");
          // no such OI
          if (oisid == null) {
              TDSLogger.println("No such OI: "+attr[0]  + attr[2] + "V"+ attr[1]);
              continue;
          }

          if (attr[3].equals("¥Í®Ä"))
                  // make coverpage/pdf
               success = do_release(oisid,attr[0],attr[1],attr[2],attr[4]);


          String mailto = (String) TDSResource.getProperties("EIF").get("e8049OIRelease.mailto");
          String mailfrom = (String) TDSResource.getProperties("EIF").get("e8049OIRelease.mailfrom");
          String mailsubject = null;
          String mailbody = null;
          if (success) {// mark processed
              mailsubject = "e8049OI Release Notice [Success]: " + attr[0]  + attr[2] + "V"+ attr[1];
              mailbody = mailsubject;
              TDSLogger.println(mailsubject);
          }
          else {
              mailsubject = "e8049OI Release Notice [FAIL]: " + attr[0]  + attr[2] + "V"+ attr[1];
              mailbody = mailsubject;
              TDSLogger.println(mailsubject);
          }
          SendMail.send(mailto, mailfrom, mailsubject, mailbody, mailbody, null);

      }
	}catch (Exception ex) {
          ex.printStackTrace();
          ex.fillInStackTrace();
          TDSLogger.println(ex.getMessage());
    }
    finally {
          DBConnection.close(conn);
    }  
// for test
//      do_release(args[0],args[1],args[2],args[3]);
  }

  // Release the OI and make PDF files
  public static boolean do_release(String sid,
                                   String product_body,
                                   String version,
                                   String brand,
                                   String caseno) {
      Connection conn = null;
      ProTestRouteBeanAF fm = 
    	  new ProTestRouteBeanAF();
      fm.setSid(sid);
      fm.setProductbody(product_body);
      fm.setBrand(brand);
      fm.setVersion(version);

      try {
          conn = DBConnection.getConnection();
          String productType = OiMaintainService.getProductType(conn, sid);
          fm.setPackage_component(OiMaintainService.getPackageComponent(conn,product_body, brand));
          
              if (productType.equals("NVM")) {
                  TDSLogger.println("make NVM pdf!!");
                  com.mxic.oiplus.pdf.MakePDF1.makePDF(conn,fm);
                  com.mxic.oiplus.pdf.MakeCoverPage1.makePDFCoverPage(conn,fm, null, "R");
              } else if (productType.equals("MROM")) {
                TDSLogger.println("make MROM pdf!!");
            	  com.mxic.oiplus.mrom.pdf.MakePDF1.makePDF(conn,fm);
            	  com.mxic.oiplus.mrom.pdf.MakeCoverPage1.makePDFCoverPage(conn,fm, null, "R");
              } else if (productType.equals("XROM")) {
                TDSLogger.println("make XROM pdf!!");
            	  com.mxic.oiplus.xtrarom.pdf.MakePDF1.makePDF(conn,fm);
            	  com.mxic.oiplus.xtrarom.pdf.MakeCoverPage1.makePDFCoverPage(conn,fm, null, "R");
              }

              ArrayList vl = EifOIReleaseService.getVendorList(conn,sid);

              Iterator i = vl.iterator();
              String vendor;
              while (i.hasNext())
              {
                   vendor = (String) i.next();
                   if (!vendor.equals("TEST2")) {
                	   fm.setVendor(vendor);
                       TDSLogger.println("Make subcon "+vendor+ "pdf..");
                       if (productType.equals("NVM"))
                    	   com.mxic.oiplus.pdf.MakeVendorPDF.makePDF(conn,fm, "R");
                       else if (productType.equals("MROM"))
                    	   com.mxic.oiplus.mrom.pdf.MakeVendorPDF.makePDF(conn,fm,"R");
                       else if (productType.equals("XROM"))
                    	   com.mxic.oiplus.xtrarom.pdf.MakeVendorPDF.makePDF(conn,fm,"R");
                   }
              }

              TDSLogger.println("Remove all *tx pdf files...");
              EifOIReleaseService.deletePDFFile(conn, product_body, brand, version);


      }
      catch (Exception ex) {
          //DBConnection.rollback(conn);
          TDSLogger.println(ex);
          TDSLogger.println(product_body+brand+" V"+version+" release fail !!");
          return false;
      }
      finally {
          DBConnection.close(conn);
      }
      return true;
  }

  public static boolean do_reject(Connection conn, String sid) {
	  //Connection conn = null;
	  try {
		  //conn = DBConnection.getConnection();
		  EifOIReleaseService.set_oi_status(conn, sid, "P");
	  } catch (Exception ex) {
		  DBConnection.rollback(conn);
		  TDSLogger.println(ex);
		  TDSLogger.println("OI sid = " + sid + "Reject Fail !!");
		  return false;
	  }
	  finally {
		  //DBConnection.close(conn);
		  //conn = null;
	  }

	  return true;
  }

  public static boolean do_obsolesce(String sid) {
      return true;
  }

}
