package com.mxic.oiplus.eif;

import java.sql.*;
import java.util.*;

import com.mxic.gprs.bean.GprsBaProductOwnerBean;
import com.mxic.gprs.dao.GprsBaProductOwnerDao;
import com.mxic.gprs.applyform.ApplyFormSendMail;
import com.mxic.oiplus.common.TF_OI_GENLIST;
import com.mxic.oiplus.oimaintain.*;
import com.mxic.oiplus.resource.DBConnection;
import com.mxic.oiplus.resource.TDSResource;
import com.mxic.oiplus.util.*;

public class EifOIRelease {

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
          if (!EifOIReleaseService.do_markProcessed(conn, attr[4], attr[5], "I"))
              continue;

          oisid = EifOIReleaseService.getOIsid(conn, attr[0],attr[1],attr[2],"A");
          // no such OI
          if (oisid == null) {
              EifOIReleaseService.do_markProcessed(conn, attr[4], attr[5], "N");
              TDSLogger.println("No such OI: "+attr[0]  + attr[2] + "V"+ attr[1]);
              continue;
          }

          if (attr.length == 6){        	  
              if (attr[3].equals("生效"))
                  // make coverpage/pdf
                  success = do_release(oisid,attr[0],attr[1],attr[2],attr[4]);
              else if (attr[3].equals("退件"))
                  success = do_reject(conn, oisid);
              else if (attr[3].equals("作廢"))
                  success = do_reject(conn, oisid);
          }
          String mailto = (String) TDSResource.getProperties("EIF").get("e8049OIRelease.mailto");
          String mailfrom = (String) TDSResource.getProperties("EIF").get("e8049OIRelease.mailfrom");
          String mailsubject = null;
          String mailbody = null;
          if (success) {// mark processed
              EifOIReleaseService.do_markProcessed(conn, attr[4], attr[5], "Y");
              mailsubject = "e8049OI Release Notice [Success]: " + attr[0]  + attr[2] + "V"+ attr[1];
              mailbody = mailsubject;
              TDSLogger.println(mailsubject);
          }
          else {
              EifOIReleaseService.do_markProcessed(conn, attr[4], attr[5], "N");
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
          conn.setAutoCommit(false);
          String productType = OiMaintainService.getProductType(conn, sid);

          // 應加一段去 set Notes info
          if (EifOIReleaseService.product_route_insert(conn, sid, product_body, brand, version) == true)
        	 if (EifOIReleaseService.delete_data(conn, "tf_product_route_tx", sid, product_body, brand, version))
        	 if (EifOIReleaseService.product_waferlevel_insert(conn, sid, product_body, brand, version))	 
        	 if (EifOIReleaseService.delete_data(conn, "tf_prod_waferlevel_tx", sid, product_body, brand, version))
             if (EifOIReleaseService.bom_route_insert(conn, sid, product_body, brand, version))
             if (EifOIReleaseService.bom_route_mcp_insert(conn, sid, product_body, brand, version))
             if (EifOIReleaseService.bom_route_xrom_insert(conn, sid, product_body, brand, version))
             if (EifOIReleaseService.bom_route_mrom_insert(conn, sid))
             if (EifOIReleaseService.bom_reroute_xrom_insert(conn, sid, product_body, brand, version))
             if (EifOIReleaseService.bom_route_mms_insert(conn, sid, product_body, brand, version))
             if (EifOIReleaseService.setProdEpnExpire(conn, sid, product_body, brand))
             if (EifOIReleaseService.main_route_insert(conn, sid, product_body, version))
             if (EifOIReleaseService.delete_data(conn, "tf_bom_route_tx", sid, product_body, brand, version))
             if (EifOIReleaseService.delete_data(conn, "tf_bom_route_mcp_tx", sid, product_body, brand, version))
             if (EifOIReleaseService.delete_data(conn, "tf_bom_route_xrom_tx", sid, product_body, version))
             if (EifOIReleaseService.delete_data(conn, "tf_bom_reroute_xrom_tx", sid, product_body, version))
             if (EifOIReleaseService.delete_data(conn, "tf_bom_route_mrom_tx", sid, product_body, version))
             if (EifOIReleaseService.delete_data(conn, "tf_bom_route_mms_tx", sid, product_body, version))
             if (EifOIReleaseService.delete_data(conn, "tf_main_route_xrom_tx", sid, product_body, version))
             if (EifOIReleaseService.Yield_insert(conn, sid, product_body, brand, version))
             if (EifOIReleaseService.delete_data(conn, "tf_yield_tx", sid, product_body, brand, version))
             if (EifOIReleaseService.Yield_Def_insert(conn, sid))
             if (EifOIReleaseService.delete_data(conn, "tf_yield_definition_tx", sid))
             if (EifOIReleaseService.Yield_GroupItems_insert(conn, sid))
             if (EifOIReleaseService.delete_data(conn, "tf_yield_groupitems_tx", sid))            	 
             if (EifOIReleaseService.WS_insert(conn, sid, product_body, brand, version))
             if (EifOIReleaseService.delete_data(conn, "tf_test_parameter_ws_tx", sid, product_body, brand, version))
             if (EifOIReleaseService.FT_insert(conn, sid, product_body, brand, version))
             if (EifOIReleaseService.delete_data(conn, "tf_test_parameter_ft_tx", sid, product_body, brand, version))
             if (EifOIReleaseService.PBC_insert(conn, sid, product_body, brand, version))
             if (EifOIReleaseService.delete_data(conn, "tf_test_parameter_pbc_tx", sid, product_body, brand, version))
             if (EifOIReleaseService.Basicdata_insert(conn, sid, product_body, brand, version))
             if (EifOIReleaseService.delete_data(conn, "tf_basic_info_tx", sid, product_body, brand, version))
             if (EifOIReleaseService.delete_data(conn, "tf_basic_info_mms_tx", sid, product_body, brand, version))
             if (EifOIReleaseService.BasicdataComment_insert(conn, sid))
             if (EifOIReleaseService.delete_data(conn, "TF_BASIC_INFO_COMMENT_TX", sid))
             if (EifOIReleaseService.Wip_insert(conn, sid))
             if (EifOIReleaseService.delete_data(conn, "tf_wip_tx", sid))
             if (EifOIReleaseService.coverpage_insert(conn, product_body, brand, version, caseno))
             if (EifOIReleaseService.do_document_likage(conn, sid, product_body, brand, version))
             if (EifOIReleaseService.set_oi_status(conn, sid, "R"))
          {
              TF_OI_GENLIST tfoigen = new TF_OI_GENLIST();
              TDSLogger.println("table records copy done, going to commit!!");
              conn.commit();
              TDSLogger.println("table records committed!!");

              if (productType.equals("NVM")) {
                  TDSLogger.println("make NVM pdf!!");
                  fm.setPackage_component(OiMaintainService.getPackageComponent(conn,product_body, brand));
                  com.mxic.oiplus.pdf.MakePDF1.makePDF(conn,fm);
                  com.mxic.oiplus.pdf.MakeCoverPage1.makePDFCoverPage(conn, fm, null, "R");
               	  do_notice_mcp(conn, fm);
              } else if (productType.equals("MROM")) {
                TDSLogger.println("make MROM pdf!!");
            	  com.mxic.oiplus.mrom.pdf.MakePDF1.makePDF(conn,fm);
            	  com.mxic.oiplus.mrom.pdf.MakeCoverPage1.makePDFCoverPage(conn, fm, null, "R");
              } else if (productType.equals("XROM")) {
                TDSLogger.println("make XROM pdf!!");
            	  com.mxic.oiplus.xtrarom.pdf.MakePDF1.makePDF(conn,fm);
            	  com.mxic.oiplus.xtrarom.pdf.MakeCoverPage1.makePDFCoverPage(conn, fm, null, "R");
              } else if (productType.equals("MMS")) {
                  TDSLogger.println("make MMS pdf!!");
              	  com.mxic.oiplus.mms.pdf.MakePDF1.makePDF(conn,fm);
              	  com.mxic.oiplus.mms.pdf.MakeCoverPage1.makePDFCoverPage(conn, fm, null, "R");
                }

              ArrayList vl = EifOIReleaseService.getVendorList(conn,sid);
              
              if(vl!=null){
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
	                       else if (productType.equals("MMS"))
	                    	   com.mxic.oiplus.mms.pdf.MakeVendorPDF.makePDF(conn,fm,"R");
	                   }
	              }
              }    
              tfoigen.insertData("11512", fm.getSid(), fm.getProductbody(), fm.getVersion());
              TDSLogger.println("FTP PDFs to DCC...");
              EifOIReleaseService.ftp_to_dcc(conn, product_body, brand, version, null);
              //20091013 move from if... to here
              TDSLogger.println("Remove all *tx pdf files...");
              EifOIReleaseService.deletePDFFile(conn, product_body, brand, version);

          } else {
              //DBConnection.rollback(conn);
              TDSLogger.println(product_body + brand + " V" + version + " release fail !!");
              return false;
          }
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

  public static void do_notice_mcp(Connection conn, ProTestRouteBeanAF fm){
	  boolean sendMail = false;
	  if(fm.getPackage_component().equals("S")){
		  ApplyFormSendMail mailObj = new ApplyFormSendMail();
          String mailto = "";
          String mailfrom = (String) TDSResource.getProperties("EIF").get("e8049OIRelease.mailfrom");
          
          String mailsubject = fm.getProductbody() + " SCP 8049 OI BOM 進版 , 請確認 MCP 8049 OI 是否需進版";
          String mailbody = "SCP 8049- " + fm.getProductbody() + " V" + fm.getVersion() + " 己生效 , 影響 MCP 產品, 相關Data 如下list :<br>";
		  
          ArrayList<String> prodBodyList = new ArrayList<String>();
          ArrayList<String> cretList = new ArrayList<String>();
          ArrayList<String> ownerList = new ArrayList<String>();
          ArrayList<String> mcpSidList = new ArrayList<String>();
          String creator = OiMaintainService.getCreator(fm.getSid()); 
          cretList.add(creator);
          mailto += creator + mailObj.MAIL_ADRESS;
		  ArrayList<HashMap<String,String>> effMcpList = EifOIReleaseService.getEffectMcpList(conn, fm.getSid(), fm.getProductbody());
		  int i=1;
		  for(HashMap<String, String> hm : effMcpList){
			  sendMail = true;
			  String mcpSid = hm.get("SID").toString();
			  String prodBody = hm.get("PRODUCT_BODY").toString(); 
			  String brand = hm.get("BRAND").toString(); 
			  String bckOpt = hm.get("BACKEND_OPTION").toString();
			  String comProdBody = hm.get("COM_PROD_BODY").toString();
			  String comMaskOpt = hm.get("COM_MASK_OPTION").toString();
			  String wsRoute = hm.get("WS_ROUTE").toString(); 
			  if(!mcpSidList.contains(mcpSid)){
				  mcpSidList.add(mcpSid);
				  String mcpCreator = OiMaintainService.getCreator(mcpSid);
				  if(!cretList.contains(mcpCreator)){
					  cretList.add(mcpCreator);
					  mailto += "," + mcpCreator + mailObj.MAIL_ADRESS;
				  }
			  }
			  
	          if(!prodBodyList.contains(prodBody)){
	        	  prodBodyList.add(prodBody);
				  GprsBaProductOwnerBean prodOwner = GprsBaProductOwnerDao.queryByProductBody(conn, prodBody);
				  if(prodOwner!=null){
					  if(!ownerList.contains(prodOwner.getPl_pe_owner()))
						  ownerList.add(prodOwner.getPl_pe_owner());
					  if(!ownerList.contains(prodOwner.getPl_pe_backup()))
						  ownerList.add(prodOwner.getPl_pe_backup());
					  if(!ownerList.contains(prodOwner.getMp_pe_owner()))
						  ownerList.add(prodOwner.getMp_pe_owner());
					  if(!ownerList.contains(prodOwner.getMp_pe_backup()))
						  ownerList.add(prodOwner.getMp_pe_backup());
				  }
	          }
	          //scp owner
	          GprsBaProductOwnerBean prodOwner = GprsBaProductOwnerDao.queryByProductBody(conn, fm.getProductbody());
			  if(prodOwner!=null){
				  if(!ownerList.contains(prodOwner.getPl_pe_owner()))
					  ownerList.add(prodOwner.getPl_pe_owner());
				  if(!ownerList.contains(prodOwner.getPl_pe_backup()))
					  ownerList.add(prodOwner.getPl_pe_backup());
				  if(!ownerList.contains(prodOwner.getMp_pe_owner()))
					  ownerList.add(prodOwner.getMp_pe_owner());
				  if(!ownerList.contains(prodOwner.getMp_pe_backup()))
					  ownerList.add(prodOwner.getMp_pe_backup());
			  }
	          mailbody += (i++) + ". SCP " + (brand + "-" + comProdBody + comMaskOpt) + "(Mask OPT)/Route: " 
	          			+ wsRoute + " Expired, 影響 MCP " + (brand + "-" + prodBody + bckOpt)
	          			+ "(BE OPT) BOM Data , 請確認 " + prodBody + " OI 是否需進版(BOM Route).<br>";
	          //"1. SCP 6709H(Mask OPT)/Route: FWxxx 有變更,影響6115H(BE OPT) BOM Data , 請確認6115 OI 是否需進版(BOM Route).\n\r";
	          //mailbody += "2. SCP 6709G          /Route: FWxxx 有變更,影響6115D         BOM Data , 請確認6115 OI 是否需進版(BOM Route).";
	          
		  }
		  if(ownerList.size()>0){
			  String ownerStr = ownerList.toString();
			  mailto += "," + mailObj.getNoteId(ownerStr.substring(1, ownerStr.length()-1).replaceAll(" ", ""));
		  }
		  System.out.println(mailto);
		  System.out.println(mailsubject);
		  System.out.println(mailbody);
		  if(sendMail){
			  System.out.println("sendMail~~~");
			  SendMail.sendHtml(mailto, mailfrom, mailsubject, mailbody);
		  }  
      }    	  
  }
  
}
