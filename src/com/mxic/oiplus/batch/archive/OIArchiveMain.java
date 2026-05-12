package com.mxic.oiplus.batch.archive;

import java.io.File;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

import com.mxic.oiplus.resource.DBConnection;
import com.mxic.oiplus.rs.TDSConfig;
import com.mxic.oiplus.util.TDSLogger;
import com.mxic.tdsplus.common.SafeExec;


/**
 * 
 * 1. args[0] = "0", archive backup for product
 * 	a. args[1]: product type
 *  b. args[2]: product body
 *  c. args[3]: brand
 *  d. args[4]: version
 * 2. args[0] = "1", archive backup for time
 *  a. args[1] = start time, ex. 2011/6/29 or 20110629
 *  b. args[2] = end time, ex. 2012/6/29 or 20120629
 *  
 * @author Kevin Huang
 */
public class OIArchiveMain {
	
	public static void main(String[] args) {
		if(args.length > 0){
			OIArchiveMain oiArchive = new OIArchiveMain();
			ArrayList tfInfoList = null;
			if(args[0].equals("0")){
				String productType = null;
				String productBody = null;
				String brand = null;
				String version = null;
				switch(args.length){
					case 2:
						productType = args[1];
						break;
					case 3:
						productType = args[1];
						productBody = args[2];
						break;
					case 4:
						productType = args[1];
						productBody = args[2];
						brand = args[3];
						break;
					case 5:
						productType = args[1];
						productBody = args[2];
						brand = args[3];
						version = args[4];
						break;
				}				
				tfInfoList = TfInformationDao.getBackupListByPorduct(productType, productBody, brand, version);
			}else if(args[0].equals("1")){
				String startTime = args[1]; 
				String endTime = args[2];
				tfInfoList = TfInformationDao.getBackupListByTime(startTime, endTime);
			}else{
				throw new RuntimeException("args[0]: " + args[0] + " not have term!");
			}
			oiArchive.backup(tfInfoList);
		}
	}
	

	/**
	 * 檔案備份
	 * @param con
	 * @param tfInfoList
	 * @throws Exception 
	 */
	public void backup(ArrayList tfInfoList){
 		Connection con = null;
 		try {
 	        Properties prop = TDSConfig.getProperty("TIMPdf");        
 			String backupPath = prop.getProperty("oi_archive_backup_path");
 			String backupDir = this.getBackupDir();
 			String pdfPath = prop.getProperty("pdf.dir");
 			String jpg_txPath = prop.getProperty("jpg_tx.path");
 			String jpgPath = prop.getProperty("jpg.path");
 			String deletFlag = prop.getProperty("oi_archive_ori_file_delete_flag");
 			
			con= DBConnection.getConnection();
			for(int i=0; i<tfInfoList.size(); i++){
				TfInformationBean bean = (TfInformationBean)tfInfoList.get(i);
				TDSLogger.println("e8049OI Backup Notice [Processing OI ] : " + bean.getProduct_type() + bean.getProduct_body() + bean.getBrand()+ bean.getVersion());
				String bkupFileName = null;
				String bkupFileCond = null;
				String bkupFileCond1 = null;
				String bkupFileCond2 = null;
				String bkupFileCond3 = null;
				String bkupFileCond4 = null;
				String bkupFileCond5 = null;
				String bkupFileCond6 = null;
				String bkupPngCond = bean.getProduct_body() + "_" + bean.getBrand() 
							+ "_" + bean.getVersion() + "_*";
				if(bean.getBrand().equals("KH")){ //for KH
					bkupFileName = "8049k_" + bean.getProduct_body() + "_" + bean.getVersion() + ".tar";					
					bkupFileCond = "8049k-" + bean.getProduct_body() + "*v" + bean.getVersion() + ".pdf";					
					bkupFileCond1 = "8049k-" + bean.getProduct_body() + "*v" + bean.getVersion() + "_s.pdf";
					bkupFileCond2 = "8049k-" + bean.getProduct_body() + "*v" + bean.getVersion() + "_tx.pdf";
					bkupFileCond3 = "8049k-" + bean.getProduct_body() + "*v" + bean.getVersion() + "_tx_s.pdf";
					bkupFileCond4 = "8049k-" + bean.getProduct_body() + "*v" + bean.getVersion() + "-Bin.txt";
					bkupFileCond5 = "8049k-" + bean.getProduct_body() + "*v" + bean.getVersion() + "-PGM.txt";
					bkupFileCond6 = "8049k-" + bean.getProduct_body() + "*v" + bean.getVersion() + "-Route.txt";
				}else{	//for MX
					bkupFileName = "8049_" + bean.getProduct_body() + "_" + bean.getVersion() + ".tar";
					bkupFileCond = "8049-" + bean.getProduct_body() + "*v" + bean.getVersion() + ".pdf";
					bkupFileCond1 = "8049-" + bean.getProduct_body() + "*v" + bean.getVersion() + "_s.pdf";
					bkupFileCond2 = "8049-" + bean.getProduct_body() + "*v" + bean.getVersion() + "_tx.pdf";
					bkupFileCond3 = "8049-" + bean.getProduct_body() + "*v" + bean.getVersion() + "_tx_s.pdf";
					bkupFileCond4 = "8049-" + bean.getProduct_body() + "*v" + bean.getVersion() + "-Bin.txt";
					bkupFileCond5 = "8049-" + bean.getProduct_body() + "*v" + bean.getVersion() + "-PGM.txt";
					bkupFileCond6 = "8049-" + bean.getProduct_body() + "*v" + bean.getVersion() + "-Route.txt";
				}
				
				
		        StringBuffer cmdBuffer = new StringBuffer();
		        File tar = new File(backupPath + File.separator + backupDir);
		        if (!tar.exists()) {
		            tar.mkdirs();
		        }
		        cmdBuffer.append("cd ").append(tar.getAbsolutePath()).append(";");
		        cmdBuffer.append("tar -cvf ").append(bkupFileName);
		        cmdBuffer.append(" ").append(pdfPath).append(File.separator).append(bkupFileCond);
		        cmdBuffer.append(" ").append(pdfPath).append(File.separator).append(bkupFileCond1);
		        cmdBuffer.append(" ").append(pdfPath).append(File.separator).append(bkupFileCond2);
		        cmdBuffer.append(" ").append(pdfPath).append(File.separator).append(bkupFileCond3);
		        cmdBuffer.append(" ").append(pdfPath).append(File.separator).append(bkupFileCond4);
		        cmdBuffer.append(" ").append(pdfPath).append(File.separator).append(bkupFileCond5);
		        cmdBuffer.append(" ").append(pdfPath).append(File.separator).append(bkupFileCond6);
		        cmdBuffer.append(" ").append(jpgPath).append(File.separator).append(bkupPngCond);
		        cmdBuffer.append(" ").append(jpg_txPath).append(File.separator).append(bkupPngCond);
		        cmdBuffer.append(";");

		        cmdBuffer.append("chmod 666 " + tar.getAbsolutePath()).append(File.separator).append(bkupFileName);
		        cmdBuffer.append("gzip -f " + tar.getAbsolutePath()).append(File.separator).append(bkupFileName);
		        String tarCommand = cmdBuffer.toString();
		        TDSLogger.println("execute " + tarCommand);

		        SafeExec makeTar = new SafeExec(tarCommand);
		        StringBuffer sout = new StringBuffer();
		        StringBuffer serr = new StringBuffer();
		        int result = makeTar.perform(sout, serr);
		        if (result != 0) {		        	
		        	TDSLogger.println("Error on executing " + tarCommand + "\n, Return Code = " + result);
					bean.setAchv_flag("F");
					bean.setAchv_result("Fail");
					bean.setAchv_dir(backupDir);
					TfInformationDao.updateArchiveFlag(con, bean);
		        	TfArchiveBackupLogDao.insertComments(con, bean.getProduct_type(), bean.getProduct_body(), bean.getBrand(), bean.getVersion(), backupDir, tarCommand);
		        }else{
		        	StringBuffer cmdDelBuffer = new StringBuffer();
			        sout = new StringBuffer();
			        serr = new StringBuffer();
			        if(deletFlag !=null && deletFlag.equals("Y")){
			        	String MainPDFFile = "";
			        	if(bean.getBrand().equals("KH")){ //for KH
			        		MainPDFFile = "8049k-" + bean.getProduct_body() + "v" + bean.getVersion() + ".pdf";						
			        	}else{
			        		MainPDFFile = "8049-" + bean.getProduct_body() + "v" + bean.getVersion() + ".pdf";						
			        	}
		        	    cmdDelBuffer.append("mv ").append(pdfPath).append(File.separator).append(MainPDFFile).append(" ").append(pdfPath).append(File.separator).append("tmp_file.pdf;");
			        	cmdDelBuffer.append("rm ").append(pdfPath).append(File.separator).append(bkupFileCond).append(";");
			        	cmdDelBuffer.append("rm ").append(pdfPath).append(File.separator).append(bkupFileCond1).append(";");
			        	cmdDelBuffer.append("rm ").append(pdfPath).append(File.separator).append(bkupFileCond2).append(";");
			        	cmdDelBuffer.append("rm ").append(pdfPath).append(File.separator).append(bkupFileCond3).append(";");
			        	cmdDelBuffer.append("rm ").append(pdfPath).append(File.separator).append(bkupFileCond4).append(";");
			        	cmdDelBuffer.append("rm ").append(pdfPath).append(File.separator).append(bkupFileCond5).append(";");
			        	cmdDelBuffer.append("rm ").append(pdfPath).append(File.separator).append(bkupFileCond6).append(";");
			        	cmdDelBuffer.append("rm ").append(jpgPath).append(File.separator).append(bkupPngCond).append(";");
			        	cmdDelBuffer.append("rm ").append(jpg_txPath).append(File.separator).append(bkupPngCond).append(";");
			        	cmdDelBuffer.append("mv ").append(pdfPath).append(File.separator).append("tmp_file.pdf ").append(" ").append(pdfPath).append(File.separator).append(MainPDFFile).append(";");
				        String delCommand = cmdDelBuffer.toString();
				        TDSLogger.println("execute " + delCommand);
				        makeTar = new SafeExec(delCommand);
				        result = makeTar.perform(sout, serr);
				        if (result != 0) {	
				        	TDSLogger.println("Error on executing " + delCommand + "\n, Return Code = " + result);
				        	TfArchiveBackupLogDao.insertComments(con, bean.getProduct_type(), bean.getProduct_body(), bean.getBrand(), bean.getVersion(), backupDir, delCommand);			        	
				        }
			        }    
					bean.setAchv_flag("Y");
					bean.setAchv_result("Pass");
					bean.setAchv_dir(backupDir);
					TfInformationDao.updateArchiveFlag(con, bean);
					TfArchiveBackupLogDao.insert(con, bean.getProduct_type(), bean.getProduct_body(), bean.getBrand(), bean.getVersion(), backupDir, bkupFileName);					
		        }
		        TDSLogger.println("e8049OI Backup Notice [Success OI ]  " + bean.getProduct_type() + bean.getProduct_body() + bean.getBrand()+ bean.getVersion());
			}
		}catch (Exception e){
			TDSLogger.println(e);
		}finally{
			DBConnection.close(con);
		}
	}
	
	/**
	 * 取得備份目錄
	 * @return
	 */
	private String getBackupDir(){		
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
		return df.format(new Date());
	}
}
