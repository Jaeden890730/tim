package com.mxic.oiplus.apl.bean;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.GregorianCalendar;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.upload.FormFile;

import com.mxic.gprs.applyform.ApplyFormService;
import com.mxic.oiplus.apl.APLDef;
import com.mxic.oiplus.apl.APLListForm;
import com.mxic.oiplus.apl.APLUtil;
import com.mxic.oiplus.au.User;
import com.mxic.oiplus.resource.TDSResource;
import com.mxic.oiplus.util.StringUtil;

public class ApplyFlow {
  private Connection conn = null;

  private Exception error = null;
  public Exception getError() {
    return error;
  }

  public ApplyFlow(Connection conn) {
    this.conn = conn;
  }
  /**
   * invoked when start a new apply
   * @param form
   * @param prodList
   * @param applyType
   * @param message
   * @return
   */
  public ApplyForm initial(APLListForm form, String[] prodList, String applyType, ArrayList message) {
    // current date
    GregorianCalendar now = new GregorianCalendar();
    // generate an ApplyForm
    ApplyForm apply = new ApplyForm();
    apply.setApplyType(applyType);
    apply.setProcType(form.getProcType());// must WS or FT
    apply.setBatchType(form.getBatchType());
    apply.setQueryString((String) message.remove(0)); // query_string
    apply.setReason(form.getRemarks());
    apply.setAttachment(form.getFileName());
    for (KeyGenerator keygen = new KeyGenerator();;) { // create app_no by current date
      apply.setApplyNo( keygen.getKey(conn, now.getTime(), applyType) );
      if ( keygen.isValid(apply.getApplyNo()) ) break;
      message.add(apply.getApplyNo());
      return null;
    }
    for (APLUser applicant = new APLUser();;) { // set applicant (user who applies)
      try {
        applicant = APLUser.getUserById(conn, Integer.valueOf(form.getOperatorId()));
      } catch (Exception ex) {
        applicant.setUserId(form.getOperatorId());
      }
      apply.setApplicant(applicant);
      break;
    }
    // insert
    int i = 0;
    try {
      conn.setAutoCommit(false);
      // INSERT INTO AP_APP_MASTER
      StringBuffer sql = new StringBuffer("INSERT INTO AP_APP_MASTER ");
      sql.append("(APP_NO, PROCESS_TYPE, APP_TYPE, APP_STATUS, BATCH_TYPE, ");
      sql.append(" INITIAL_USER, INITIAL_TIME, PROCESS_USER, PROCESS_TIME, QUERY_STRING, REASON, ATTACHMENT) ");
      sql.append("VALUES (?,?,?,?,?,?,?,?,?,?,?,?)");
      Timestamp d = new Timestamp(now.getTimeInMillis());
      PreparedStatement stmt = conn.prepareStatement(sql.toString());
      stmt.setString(++i, apply.getApplyNo());
      stmt.setString(++i, apply.getProcType());
      stmt.setString(++i, apply.getApplyType());
      stmt.setString(++i, apply.getApplyStatus());
      stmt.setString(++i, apply.getBatchType());
      stmt.setString(++i, apply.getApplicant().getEmpNo().trim());
      stmt.setTimestamp(++i, d);
      stmt.setString(++i, "?");
      stmt.setTimestamp(++i, d);
      stmt.setString(++i, apply.getQueryString());
      stmt.setString(++i, StringUtil.Utf8ToBig5(apply.getReason()));
      stmt.setString(++i, StringUtil.Utf8ToBig5(apply.getApplyNo()+APLUtil.getExt(apply.getAttachment())));
      i = stmt.executeUpdate();
      // INSERT INTO AP_APP_SIGN by Kevin Huang on 2012.11.2
      String applicant = apply.getApplicant().getEmpNo().trim();	//申請者
      String csDeptId1 = (String)TDSResource.getProperties("APL").get("dept_qec");
      String csDeptId2 = "";
      if (apply.getProcType().startsWith("W"))
    	  csDeptId2 = (String)TDSResource.getProperties("APL").get("dept_ws");
      else if (apply.getProcType().startsWith("F"))
    	  csDeptId2 = (String)TDSResource.getProperties("APL").get("dept_ft");

      sql.delete(0,sql.length());
      sql.append("INSERT INTO AP_APP_SIGN ");
      sql.append("(APP_NO, STATUS, APPLICANT, APPLICANT_BOSS, APPLICANT_DEPT_BOSS, COUNTERSIGN_BOSS_1");
      if(csDeptId2 != null && !csDeptId2.equals(""))
    	  sql.append(", COUNTERSIGN_BOSS_2) ");
      else
    	  sql.append(") ");
      sql.append("VALUES (?,?,?,?,?,?");
      if(csDeptId2 != null && !csDeptId2.equals(""))
    	  sql.append(",?)");
      else
    	  sql.append(")");
      stmt = conn.prepareStatement(sql.toString());
      stmt.setString(1, apply.getApplyNo());
      stmt.setString(2, "申請者填單");
      stmt.setString(3, applicant);
      stmt.setString(4, ApplyFormService.getManagerByEmpId(applicant));
      stmt.setString(5, ApplyFormService.getDEPT_MANAGER(applicant));
      stmt.setString(6, ApplyFormService.getDEPT_MANAGER_byDept(csDeptId1));
      if(csDeptId2 != null && !csDeptId2.equals("")){
    	  if(csDeptId2.equals("M0380")){
    		  stmt.setString(7, (String)TDSResource.getProperties("APL").get("M0380_section_manager_id"));  
    	  }else{
    		  stmt.setString(7, ApplyFormService.getDEPT_MANAGER_byDept(csDeptId2));
    	  }    	      	  
      }
      stmt.executeUpdate();
      // INSERT INTO AP_APP_DETAIL
      // cannot using APL_IS_AVAILABLE() here, it will cause ORA-04091: table TIM.AP_APP_DETAIL is mutating...
      sql.delete(0,sql.length());
      sql.append("INSERT INTO AP_APP_DETAIL ");
      sql.append("SELECT APP_NO, P.SID APL_SID FROM AP_APP_MASTER M, AP_APL P ");
      sql.append("WHERE M.APP_NO = ? AND P.SID = ? ");
      sql.append("AND NOT EXISTS (SELECT 1 FROM AP_APP_DETAIL D, AP_APP_MASTER E ");
      sql.append("WHERE D.APP_NO = E.APP_NO AND P.SID = D.APL_SID ");
      sql.append("AND E.APP_STATUS NOT IN ('R','C','O')) ");
      stmt = conn.prepareStatement(sql.toString());
      for ( int x = 0; x < prodList.length; x++ ) {
        stmt.setString(1,apply.getApplyNo());
        stmt.setInt(2,Integer.parseInt(prodList[x]));
        //stmt.setInt(3,Integer.parseInt(prodList[x]));
        stmt.addBatch();
      }
      int[] result = stmt.executeBatch();
      Arrays.fill(result, 0); // since executeBatch will return -2 (unknown)
      // 2006/10/27 將 file move 片斷移出
      if ( form.getFileName() != null ) {
        FileProcessor fp = new FileProcessor();
        String srcName = fp.tempPath + form.getFileName();
        String destName = fp.filePath + apply.getApplyNo() + APLUtil.getExt(apply.getAttachment());
        if (!fp.move(srcName, destName))
          throw new Exception(" file "+fp.tempPath + form.getFileName()+" copy error !!");
      }

      // UPDATE AP_APL.APL_NO
      /* 2006/10/26 申請時，不需要 update AP_APL.APL_NO，這是在生效後再做
          sql.delete(0,sql.length());
          sql.append("UPDATE AP_APL SET APL_NO = ? WHERE SID = ? AND APL_NO IS NULL AND EXISTS ");
          sql.append("(SELECT * FROM AP_APP_DETAIL WHERE APP_NO = ? AND APL_SID = ?) ");
          stmt = conn.prepareStatement(sql.toString());
          stmt.setString(1,apply.getApplyNo());
          stmt.setString(3,apply.getApplyNo());
          for ( int x = 0; x < prodList.length; x++ ) {
           stmt.setInt(2, Integer.parseInt(prodList[x]));
           stmt.setInt(4, Integer.parseInt(prodList[x]));
           result[x] = stmt.executeUpdate();
           if ( result[x] == 0 ) message.add(prodList[x]);
           i += result[x];
           if ( i == prodList.length + 1 ) {
            conn.commit();
            if ( form.getFileName() != null ) {
             FileProcessor fp = new FileProcessor();
             String srcName = fp.tempPath + form.getFileName();
             String destName = fp.filePath + apply.getApplyNo();
             fp.move(srcName, destName);
            }
           }
          }
       */
    } catch (Exception ex) {
      message.add(ex.getMessage());
      ex.printStackTrace();
      try {
        conn.rollback();
      } catch (Exception ex2) {
        ex2.printStackTrace();
      }
    } finally {
      try {
// 20061026 remark, 因為不用 update 每一筆 APL，不會有此狀況
//				if ( conn != null && i < prodList.length + 1 )
//                                  conn.rollback();
        if ( conn != null ) {
// 20061026 add commit();
          conn.commit();
          conn.setAutoCommit(true);
        }
      } catch (Exception sqle) {
        sqle.printStackTrace();
      }
    }
    return apply;
  }
  /**
   * invoked when re-issue an apply
   * @param form
   * @param prodList
   * @param applyType
   * @param message
   * @return
   */
  public ApplyForm initial(ApplyForm apply, String[] prodList, ArrayList message) {
    // current date
    GregorianCalendar now = new GregorianCalendar();
    int i = 0;
    try {
      // INSERT INTO AP_APP_MASTER
      StringBuffer sql = new StringBuffer("UPDATE AP_APP_MASTER ");
      sql.append("SET APP_STATUS = '" + APLDef.ApplyState.RE_INITIAL + "' ");
      sql.append(",REASON = '" + apply.getReason() + "' ");
      sql.append("WHERE APP_NO = ? ");
      Timestamp d = new Timestamp(now.getTimeInMillis());
      PreparedStatement stmt = conn.prepareStatement(sql.toString());
      stmt.setString(++i, apply.getApplyNo());
      conn.setAutoCommit(false);
      i = stmt.executeUpdate();
      // clear AP_APP_DETAIL
      sql.delete(0,sql.length());
      sql.append("DELETE FROM AP_APP_DETAIL WHERE APP_NO = ? ");
      stmt = conn.prepareStatement(sql.toString());
      stmt.setString(1, apply.getApplyNo());
      stmt.executeUpdate();
      // reset AP_APP_DETAIL
      sql.delete(0,sql.length());
      sql.append("INSERT INTO AP_APP_DETAIL (APP_NO, APL_SID) values (?, ?)");
      /*
      sql.append("INSERT INTO AP_APP_DETAIL ");
      sql.append("SELECT APP_NO, P.SID APL_SID FROM ");
      sql.append("AP_APP_MASTER M, AP_APL P WHERE M.APP_NO = ? AND P.SID = ? ");
      sql.append("AND NOT EXISTS (SELECT * FROM AP_APP_DETAIL WHERE APL_SID = ?) ");
      */
      stmt = conn.prepareStatement(sql.toString());
      for ( int x = 0; x < prodList.length; x++ ) {
    	  stmt.setString(1,apply.getApplyNo());
    	  stmt.setInt(2,Integer.parseInt(prodList[x]));
    	  //stmt.setInt(3,Integer.parseInt(prodList[x]));
    	  stmt.addBatch();
      }
      int[] result = stmt.executeBatch();
      Arrays.fill(result, 0); // since executeBatch will return -2 (unknown)

      // have attachments and not equal (updated)
      if ( apply.getAttachment() != null &&
           !apply.getAttachment().equals(apply.getApplyNo() + APLUtil.getExt(apply.getAttachment()))) {
        FileProcessor fp = new FileProcessor();
        String srcName = fp.tempPath + apply.getAttachment();
        String destName = fp.filePath + apply.getApplyNo() + APLUtil.getExt(apply.getAttachment());
        if (!fp.move(srcName, destName))
          throw new Exception(" file "+fp.tempPath + apply.getAttachment()+" copy error !!");
      }

    } catch (Exception ex) {
      try {
        conn.rollback();
        message.add(ex.getMessage());
        ex.printStackTrace();
      } catch (Exception sqle) {
        sqle.printStackTrace();
      }
    } finally {
      try {
        if ( conn != null )
          conn.commit();
        conn.setAutoCommit(true);
      } catch (Exception sqle) {
        sqle.printStackTrace();
      }
    }
    return apply;
  }

  public boolean apply(ApplyForm applyForm) {
    // current date
    GregorianCalendar now = new GregorianCalendar();
    //
    StringBuffer sql = new StringBuffer("UPDATE AP_APP_MASTER SET ");
    ArrayList wheres = new ArrayList();
    //
    User user = applyForm.getManager();
    if ( !user.equals(APLUser.EMPTY) ) {
      sql.append((wheres.size() > 0 ? "," : "") + " PROCESS_USER = ? ");
      wheres.add( user.getUserId() );
    }
    user = applyForm.getDirector();
    if ( !user.equals(APLUser.EMPTY) ) {
      sql.append((wheres.size() > 0 ? "," : "") + " COMPLETE_USER = ? ");
      wheres.add( user.getUserId() );
    }
    sql.append( "WHERE APP_NO = ? " );
    wheres.add( applyForm.getApplyNo() );
    int cnt = 0;
    try {
      //conn.setAutoCommit(false); 歷史紀錄不在此處建立
      for ( Date d = new Date(now.getTimeInMillis()); ; ) {
        cnt = APLUtil.update(conn, sql.toString(), wheres.toArray());
        if (cnt < Integer.MAX_VALUE) break; // if ( cnt == 0 ) break;
        // INSERT INTO AP_HIST_MASTER
        sql.delete(0,sql.length());
        sql.append("INSERT INTO AP_HIST_MASTER ");
        sql.append("SELECT APP_NO, PROCESS_TYPE, APP_TYPE, APP_STATUS, BATCH_TYPE, ");
        sql.append("INITIAL_USER, INITIAL_TIME, PROCESS_USER, PROCESS_TIME, ");
        sql.append("COMPLETE_USER, COMPLETE_TIME, ? AS LOG_TIME " );
        sql.append("FROM AP_APP_MASTER WHERE APP_NO = ? ");
        PreparedStatement stmt = conn.prepareStatement(sql.toString());
        stmt.setDate(1, d);
        stmt.setString(2,applyForm.getApplyNo());
        cnt += stmt.executeUpdate();
        if ( cnt == 1 ) break;
        // INSERT INTO AP_HIST_DETAIL
        sql.delete(0,sql.length());
        sql.append("INSERT INTO AP_HIST_DETAIL ");
        sql.append("SELECT APP_NO, APL_SID " );
        sql.append("FROM AP_APP_DETAIL WHERE APP_NO = ? ");
        stmt = conn.prepareStatement(sql.toString());
        stmt.setString(1,applyForm.getApplyNo());
        cnt += stmt.executeUpdate();
        if ( cnt == 2 )
          break;
        conn.commit();
        cnt = 0;
        break;
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    } finally {
      try {
        //if ( cnt != 0 ) conn.rollback();
        if ( conn != null )
          conn.setAutoCommit(true);
      } catch (Exception sqle) {
        sqle.printStackTrace();
      }
    }
    return true;
  }
  /**
   * invoked when drop/cancel an apply
   * @param form
   * @param prodList
   * @param applyType
   * @param message
   * @return
   */
  public ApplyForm delete(ApplyForm applyForm) {
    StringBuffer sql = new StringBuffer(128);
    try {
      // UPDATE AP_APL.APL_NO
//      sql.append("UPDATE AP_APL SET APL_NO = NULL WHERE TRIM(APL_NO) = ? ");
//      PreparedStatement stmt = conn.prepareStatement(sql.toString());
//      stmt.setString(1,applyForm.getApplyNo());
//      conn.setAutoCommit(false);
//      int cnt = stmt.executeUpdate();
//      if (cnt > 0) {
        sql.delete(0, sql.length());
//        sql.append("DELETE FROM AP_APP_DETAIL WHERE APP_NO = ? ");
//        PreparedStatement stmt = conn.prepareStatement(sql.toString());
//        stmt.setString(1, applyForm.getApplyNo());
//        int cnt = stmt.executeUpdate();

        PreparedStatement stmt = null;
        int cnt = 0;

        if (cnt == 0) {
          sql.delete(0,sql.length());
  //          sql.append("DELETE FROM AP_APP_MASTER WHERE APP_NO = ? ");
          sql.append("UPDATE AP_APP_MASTER SET APP_STATUS = 'O' WHERE APP_NO = ? ");
          stmt = conn.prepareStatement(sql.toString());
          stmt.setString(1,applyForm.getApplyNo());
          cnt = 1 - stmt.executeUpdate();
          if ( cnt == 0 )
            conn.commit();
//        }
      }
      if ( cnt != 0 )
        conn.rollback();
    }
    catch (Exception ex) {
      ex.printStackTrace();
    } finally {
      try {
        if ( conn != null )
          conn.setAutoCommit(true);
      } catch (Exception sqle) {
        sqle.printStackTrace();
      }
    }
    return applyForm;
  } // end delete

  	
  public ApplyForm reject(ApplyForm applyForm, HttpServletRequest request) {

    request.setAttribute("fail_reject","success");
    /*
    if (!APLUtil.setReject(applyForm.getAf_taskid())) {
      request.setAttribute("fail_reject","af_fail");
      return null;
    }
	*/
    StringBuffer sql = new StringBuffer(64);
    StringBuffer sql2 = new StringBuffer();
    try {
        conn.setAutoCommit(false);
        // UPDATE AP_APL.APL_NO
        sql.append("UPDATE AP_APP_MASTER SET APP_STATUS = 'R' WHERE APP_NO = ? ");
        PreparedStatement stmt = conn.prepareStatement(sql.toString());
        stmt.setString(1,applyForm.getApplyNo());
        int cnt = stmt.executeUpdate();
        
        sql2.append("UPDATE ap_app_sign SET STATUS = '已作廢' WHERE APP_NO = ? ");
        stmt = conn.prepareStatement(sql2.toString());
        stmt.setString(1,applyForm.getApplyNo());
        int cnt2 = stmt.executeUpdate();

        if (cnt > 0 && cnt2 >0) {
        	conn.commit();
        	applyForm.setApplyStatus("R");
        }
        if ( cnt == 0 )
        	conn.rollback();
    } catch (Exception ex) {
    	ex.printStackTrace();
    	request.setAttribute("fail_reject","db_fail");
    } finally {
    	try {
    		if ( conn != null )
    			conn.setAutoCommit(true);
    	} catch (Exception sqle) {
    		sqle.printStackTrace();
    		request.setAttribute("fail_reject","db_fail");
    	}
    }
    return applyForm;
  } // end reject

  public boolean upload(FormFile file, String applyType, String sessionId, StringBuffer filename) {
    FileProcessor fp = new FileProcessor();
    return fp.upload(file, applyType, sessionId, filename);
  }
}

class KeyGenerator {
  java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyyMMdd");
  public boolean isValid(String key) {
    if (key == null || key.length() != ApplyForm.KEY_LENGTH )
      return false;
    if (key.charAt(0) != 'A' && key.charAt(0) != 'H' && key.charAt(0) != 'R')
      return false;
    boolean parse = false;
    try {
      sdf.parse(key.substring(1, ApplyForm.KEY_LENGTH - 2));
      Integer.parseInt(key.substring(ApplyForm.KEY_LENGTH - 2));
      parse = true;
    } catch (Exception ex) {
    }
    return parse;
  }
  public String getKey(Connection conn, java.util.Date applyDate, String applyType) {
    String sql = "SELECT APP_NO FROM AP_APP_MASTER WHERE APP_NO LIKE ? ORDER BY APP_NO DESC";
    String key = applyType + sdf.format(applyDate);
    try {
      PreparedStatement stmt = conn.prepareStatement(sql);
      stmt.setString(1, key + "__");
      ResultSet rs = stmt.executeQuery();
      if ( rs.next() ) key = rs.getString(1);
      else key = key + "00";
      rs.close();
      if ( isValid(key) ) {
        int seq = Integer.parseInt(key.substring(ApplyForm.KEY_LENGTH - 2));
        key = key.substring(0, ApplyForm.KEY_LENGTH - 2) + Integer.toString(seq + 101).substring(1);
      } else key = "INVALID APPLY NO CREATED " + key;
    } catch (Exception ex) {
      key = "CREATE KEY ERROR : " + ex.getMessage();
    }
    return key;
  }
}

class FileProcessor {
  String tempPath = null;
  String filePath = null;

  FileProcessor() {
    tempPath = (String) TDSResource.getProperties("APL").get("upload.temp") + File.separator;
    filePath = (String) TDSResource.getProperties("APL").get("upload.path") + File.separator;
  }

  public boolean upload(FormFile file, String applyType, String sessionId, StringBuffer filename) {
    boolean result = false;
    FileOutputStream out = null;
    try {
      String extname = APLUtil.getExt(file.getFileName());
      // 將上傳的檔案存在TempPath裡
      for ( String temp = getFileTemplate(applyType, sessionId); result != true; ) {
        if (  temp == null )
          break;
        out = new FileOutputStream(tempPath + temp + "." + extname);
        out.write(file.getFileData());
        out.flush();
        file.destroy();
        filename.append(temp + "." + extname);
        //System.out.println("filename="+filename);
        //file.setFileName(temp + "." + extname);
        result = true;
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    } finally {
      try {
        if ( out != null )
          out.close();
      } catch (IOException ie) {
        ie.printStackTrace();
      }
    }
    return result;

  }

  public String getFileTemplate(String applyType, String sessionId) {
    java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyyMMddHHmmssSSSSSS");
    String name = sdf.format(new GregorianCalendar().getTime()) + sessionId;
    for ( int i = 1000; i < 2000; i++ ) {
      File f = new File(tempPath + name + i + "." + applyType );
      if ( f.exists() )
        continue;
      try {
        f.createNewFile();
      } catch (Exception ex) {
        continue;
      }
      if ( f.exists() )
        return f.getName();
    }
    return null;  // can not find any fileName
  }

  public boolean move(String src, String dest) {
    boolean result = false;
    FileChannel srcChannel = null;
    FileChannel destChannel = null;
    try {
      srcChannel = new FileInputStream(new File(src)).getChannel();
      destChannel = new FileOutputStream(new File(dest)).getChannel();
// dest file will be rewrite if exists
      destChannel.transferFrom(srcChannel, 0, srcChannel.size());
      result = true;
    } catch (Exception ex) {
      ex.printStackTrace();
    } finally {
      try {
        if (srcChannel != null)
          srcChannel.close();
        if (destChannel != null)
          destChannel.close();
        if (result == true)
          new File(src).delete();
      } catch (Exception e) {
      }
      return result;
    }
  }
}