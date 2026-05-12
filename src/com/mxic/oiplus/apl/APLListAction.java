package com.mxic.oiplus.apl;

import java.sql.*;
import java.util.*;
import javax.servlet.http.*;

import org.apache.struts.action.*;
import org.apache.struts.upload.*;
import com.mxic.oiplus.apl.bean.*;
import com.mxic.oiplus.resource.*;
import com.mxic.oiplus.util.*;
//import mic.af.aplrms.*;

public class APLListAction extends Action {
  private APLListForm form = null;
  private Connection conn = null;

  public ActionForward execute(ActionMapping mapping,
                               ActionForm form,
                               HttpServletRequest request,
                               HttpServletResponse response) {
    this.form = (APLListForm) form;
    ActionForward forward = null;
    try {
      conn = DBConnection.getConnection();
      Class[] paramClass = new Class[] {ActionMapping.class, HttpServletRequest.class, HttpServletResponse.class};
      java.lang.reflect.Method actionMethod = this.getClass().getMethod(this.form.getActionType(), paramClass);
      forward = (ActionForward) actionMethod.invoke(this, new Object[]{mapping, request, response});
    } catch (Exception ex) {
      ex.printStackTrace();
    } finally {
      DBConnection.close(conn);
      conn = null;
    }
    return forward;
  }

  public ActionForward applyNew(ActionMapping mapping,
                                HttpServletRequest request,
                                HttpServletResponse response) throws Exception {
    ApplyForm applyForm = apply(request, APLDef.ApplyType.NEW);
    if ( applyForm != null ) {
//                  return mapping.findForward("success");
      return mapping.findForward("complete_af");
    }
    return back(mapping, request, response);
  }

  public ActionForward applyHold(ActionMapping mapping,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
    ApplyForm applyForm = apply(request, APLDef.ApplyType.HOLD);
    if ( applyForm != null ) {
      return mapping.findForward("success");
    }
    return back(mapping, request, response);
  }

  public ActionForward applyRelease(ActionMapping mapping,
                                    HttpServletRequest request,
                                    HttpServletResponse response) throws Exception {
    ApplyForm applyForm = apply(request, APLDef.ApplyType.RELEASE);
    if ( applyForm != null ) {
      return mapping.findForward("success");
    }
    return back(mapping, request, response);
  }

  public ActionForward uploadNew(ActionMapping mapping,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
    upload(request, "A");
    return back(mapping, request, response);
  }

  public ActionForward uploadHold(ActionMapping mapping,
                                  HttpServletRequest request,
                                  HttpServletResponse response) throws Exception {
    upload(request, "H");
    return back(mapping, request, response);
  }

  public ActionForward uploadRelease(ActionMapping mapping,
                                     HttpServletRequest request,
                                     HttpServletResponse response) throws Exception {
    upload(request, "R");
    return back(mapping, request, response);
  }

  public ActionForward history(ActionMapping mapping,
                               HttpServletRequest request,
                               HttpServletResponse response) throws Exception {
    if ( form.getSID().length > 0 ) {
      String sid = form.getSID()[0];
      APLSearch search = new APLSearch("process", "query");
      HashMap wheres = new HashMap();
      wheres.put("APP_STATUS", "R,C");
      wheres.put("SID", form.getSID()[0]);
      ArrayList all = search.execute(conn, wheres, null);
      request.setAttribute("procType",form.getProcType());
      request.setAttribute("actionType", "listData");
      request.setAttribute("readonly", Boolean.TRUE);
      request.setAttribute("selectMode", "none");
      request.setAttribute("list", all);
      request.setAttribute("searchTarget", "history");
      request.setAttribute("searchOption", "query");
      return mapping.findForward("fail");
    }
    return back(mapping, request, response);
  }

  public ActionForward exportdata(ActionMapping mapping,
                                  HttpServletRequest request,
                                  HttpServletResponse response) throws Exception {
    export(response, request.getParameterValues("export"), "data");
    return null;
  }

  public ActionForward exporthistory(ActionMapping mapping,
                                     HttpServletRequest request,
                                     HttpServletResponse response) throws Exception {
    export(response, request.getParameterValues("export"), "history");
    return null;
  }

  public ActionForward exportprocess(ActionMapping mapping,
                                     HttpServletRequest request,
                                     HttpServletResponse response) throws Exception {
    export(response, request.getParameterValues("export"), "process");
    return null;
  }

  private ActionForward back(ActionMapping mapping,
                             HttpServletRequest request,
                             HttpServletResponse response) throws Exception {
    String userId = form.getOperatorId();
    String action = form.getActionType();
    if ( action.startsWith("upload") ) { // uploadNew -> applyNew
      action = "apply" + action.substring("upload".length());
    }
    if ( action.startsWith("export") ) {
      action = action.substring("export".length());
      request.setAttribute("searchTarget", action);
      request.setAttribute("searchOption", "query");
      request.setAttribute("selectMode", action.equals("data") ? "single" : "none");
      request.setAttribute("readonly", Boolean.TRUE);
      // remember query results
      HashMap wheres = new HashMap();
      wheres.put("SID", request.getParameterValues("export"));
      if (action.equals("history")) {
        wheres.put("APP_STATUS", "R,C");
        action = "process";
      }
      APLSearch search = new APLSearch(action, "query");
      request.setAttribute("list", search.execute(conn, wheres, null));
      action = "listData";
    }
    if (action.startsWith("apply")) {
      request.setAttribute("searchTarget", "data");
      request.setAttribute("searchOption", action);
      if ( !action.equals("applyNew") ) {
        request.setAttribute("searchOption", "applyBy" + form.getBatchType());
      }
      request.setAttribute("selectMode", "multiple");
      request.setAttribute("readonly", Boolean.FALSE);
      // remember query conditions
      APLSearch search = new APLSearch("data", "apply");
      if (request.getParameterValues("query") != null) {
        String[] queryItems = request.getParameterValues("query");
        HashMap wheres = APLSearchForm.getSearch(queryItems);
        // check first initial or re-initial case
        if ( form.getApplyNo() != null && form.getApplyNo().length() > 0 ) {
          request.setAttribute("applyNo", form.getApplyNo());
          // for search to skip condition (APL_NO IS NULL)
          wheres.put("SID", "%");
          request.setAttribute("list", search.execute(conn, wheres, null));
          wheres.remove("SID");
        } else
          request.setAttribute("list", search.execute(conn, wheres, null));
        request.setAttribute("query", wheres);
      } else
        request.setAttribute("list", new ArrayList(0));
    }
    if ( userId != null )
      request.setAttribute("applicant", APLUser.getUserById(conn, Integer.valueOf(userId)));
    request.setAttribute("actionType", action);
    request.setAttribute("procType", form.getProcType());
    // remember select products
    String[] prodList = form.getSID();
    if ( prodList != null && prodList.length > 0 ) {
      request.setAttribute("prodList", prodList);
    }
    // remember fileName and remarks
    if ( form.getFileName() != null )
      request.setAttribute("fileName", form.getFileName());
    request.setAttribute("remarks", StringUtil.Utf8ToBig5(form.getRemarks()));
    request.setAttribute("reapply", form.getReapply());
    return mapping.findForward("fail");
  }

  private ApplyForm apply(HttpServletRequest request,
                          char applyType) throws Exception {
    boolean reapply = false;
    ApplyForm applyForm = null;
    String[] wheres = request.getParameterValues("query");
    String[] prodList = form.getSID();

    if ( prodList != null && prodList.length > 0 ) {
      ArrayList message = new ArrayList(prodList.length);
      ApplyFlow agent = new ApplyFlow(conn);
      if ( form.getApplyNo() != null && form.getApplyNo().length() > 0 ) {
        applyForm = ApplyForm.getApplyForm(conn, form.getApplyNo());
      }
      if ( applyForm == null ) { // start a new apply
        message.add(APLUtil.join(wheres, "|")); // as a query_string
        applyForm = agent.initial(form, prodList, Character.toString(applyType), message);
        reapply = false;
        if ( applyForm == null ) { // error apply no.
          request.setAttribute("errors", (String) message.get(0));
          return null;
        }
      }
      else { // re-initial an apply
        applyForm.setAttachment(form.getFileName());
        applyForm.setReason(form.getRemarks());
        applyForm = agent.initial(applyForm, prodList, message);
        reapply = true;
      }
      if ( message.size() > 0 ) { // some APL under process
        request.setAttribute("errors", (String[]) message.toArray(new String[0]));
        return null;
      }
      applyForm = ApplyForm.getApplyForm(conn, applyForm.getApplyNo());
    }
    request.setAttribute("actionType", "apply");
    
    String signApply_URL = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    signApply_URL += "/apl/APLAppSignAction.do?act=query&app_no=" + applyForm.getApplyNo();
    applyForm.setAf_url(signApply_URL);
    
    /* remove by Kevin Huang on 2012.11.8
    // 2006/10/27 add AF Interface Here
    // mark for testing RE-DIRECT
    String AFServerIP = (String)TDSResource.getProperties("APL").get("af_server_ip");
    APLManager aplManager = APLManager.getInstance(AFServerIP);
    FormParameter form = new FormParameter();
    form.setCSDeptID1((String)TDSResource.getProperties("APL").get("dept_qec"));
    if (applyType == 'A')
      form.setTxaApplyContent("生效申請：" + applyForm.getReason());
    else if (applyType == 'R')
      form.setTxaApplyContent("復投申請：" + applyForm.getReason());
    else if (applyType == 'H')
      form.setTxaApplyContent("停投申請："+applyForm.getReason());

    if (applyType == 'A' || applyType == 'R' || applyType == 'H') {
      if (applyForm.getProcType().startsWith("W"))
        form.setCSDeptID2((String)TDSResource.getProperties("APL").get("dept_ws"));
      else if (applyForm.getProcType().startsWith("F"))
        form.setCSDeptID2((String)TDSResource.getProperties("APL").get("dept_ft"));
    }
    String URL = "http://" + request.getServerName() + ":" +
        request.getServerPort() +
        request.getContextPath() +
        "/apl/APLShow.do?appNo=" +
        applyForm.getApplyNo();

    form.setTxtApplyContentLink(URL);
    form.setTxtFormSN(applyForm.getApplyNo());

    CreationResult result = null;
    TaskProgress tp = null;
    String firstTaskID = null;
    String rootTaskID = null;

    if (reapply == false) {
      result = aplManager.createProcess(applyForm.getApplicant().getEmpNo(),
                                        (String) TDSResource.getProperties("APL").get("af_apl_id"), form);
      firstTaskID = result.getFirstTaskID();
      rootTaskID = result.getRootTaskID();
    }
    else {
        aplManager.reapply(applyForm.getAf_taskid(),
                           (String) TDSResource.getProperties("APL").get("af_apl_id"), form);
        tp = aplManager.getApplyStatus(applyForm.getAf_taskid());
        firstTaskID = tp.getTaskID();
        rootTaskID = tp.getTaskID();
      }

    if (result != null || reapply) {
      ArrayList wheres2 = new ArrayList();
      String sql = "UPDATE AP_APP_MASTER SET AF_TASKID = ? WHERE APP_NO = ?";
      // Sample URL
      // http://172.17.25.31:8888//WebAgenda/SingleSignon.do?loginName=04421&checkpass=false&action=/eform.do?taskID=
      //20070713 modified, according AgentFlow migration
      //applyForm.setAf_url("http://" + AFServerIP + ":" +
      //                    (String)TDSResource.getProperties("APL").get("af_web_port").toString().trim() +
      //                    "/WebAgenda/SingleSignon.do?loginName=" +
      //                    applyForm.getApplicant().getEmpNo() +
      //                    "&checkpass=false&action=/eform.do?taskID=" +
      //                    firstTaskID);

      String key = com.flowring.util.common.SecurityURL.encode("loginName=" +
              applyForm.getApplicant().getEmpNo() +
              "&checkpass=false&action=/eform.do?taskID=" +
              firstTaskID);

      applyForm.setAf_url("http://" + AFServerIP + ":" +
                    (String)TDSResource.getProperties("APL").get("af_web_port").toString().trim() +
                    "/WebAgenda/SingleSignon.do?key="+key);
      TDSLogger.println("AF Link = "+applyForm.getApplyNo()+ " http://" + AFServerIP + ":" +
              (String)TDSResource.getProperties("APL").get("af_web_port").toString().trim() +
              "/WebAgenda/SingleSignon.do?key="+key);
      if (reapply == false) { // new apply
        wheres2.add(rootTaskID);
        wheres2.add(applyForm.getApplyNo());
        int cnt = APLUtil.update(conn, sql.toString(), wheres2.toArray());
        cnt = cnt;
      }
    }
    */ 
    
    /**/
    // update APP 為 會簽中 狀態的動作，將放在 cron job 去 update，以免 APL 中的狀態比 AF 早更新
    // 如果 AF 失敗怎麼辨？ set applyForm = null
    //                applyForm.setAf_url("http://psesap:7080/tdsplus/");

    if (applyForm != null) {
      request.setAttribute("applyForm", applyForm);
    }
    return applyForm;
  }

  private void upload(HttpServletRequest request, String applyType) {
    FormFile upload = form.getFile();
    if ( upload == null )
      return;
    if ( upload.getFileName().trim().length() == 0 || upload.getFileSize() == 0 ) {
      return; //未上傳檔案
    }
    ApplyFlow agent = new ApplyFlow(conn);
    StringBuffer filename = new StringBuffer();
    if ( agent.upload(upload, applyType, request.getSession().getId(),filename) ) {
      //form.setFileName(upload.getFileName());
    	form.setFileName(filename.toString());
      //request.setAttribute("fileName", upload.getFileName());
    	request.setAttribute("fileName", filename.toString());
    }
  }

  private void export(HttpServletResponse response, String[] sid, String exportType) throws Exception {
    String searchType = exportType;
    StringBuffer sb = new StringBuffer();

    if ( sid == null || sid .length == 0 ) return;
    // make query conditions (search by sid)
    HashMap wheres = new HashMap();
    wheres.put("SID", sid);

    if (exportType.equals("history")) {
      wheres.put("APP_STATUS", "R,C");
      searchType = "process";
    }
    APLSearch search = new APLSearch(searchType, "query");
    ArrayList result = search.execute(conn, wheres, null);

    if (result.size() > 0) {
      if (exportType.equals("data")) {
        APL data = (APL)result.get(0);
        if (data.getProcType().equals("WS"))
          sb.append( "Product Body,Option,Test Mode,Tester Type,Site,Vendor Name,APL Status,APL Number, Log Time" +
              new String(new byte[] {Character.LINE_SEPARATOR}) +
              new String(new byte[] {Character.LETTER_NUMBER}));
        else if (data.getProcType().equals("FT"))
          sb.append( "Product Body,Option,Pin Count,Package Code,Body Size,Test Mode,Tester Type,Vendor Name,APL Status,IsExists8049,APL Number, Log Time" +
                     new String(new byte[] {Character.LINE_SEPARATOR}) +
                     new String(new byte[] {Character.LETTER_NUMBER}));
        else if (data.getProcType().equals("AVI"))
            sb.append( "Vendor Name,Product Body,Option,Ink,Tester Type,APL Status,APL Number, Log Time" +
                       new String(new byte[] {Character.LINE_SEPARATOR}) +
                       new String(new byte[] {Character.LETTER_NUMBER}));
        else if (data.getProcType().equals("FVI"))
            sb.append( "Vendor Name,Pin Count,Package Code,Body Size,Tester Type,Carrier Type,APL Status,APL Number, Log Time" +
                       new String(new byte[] {Character.LINE_SEPARATOR}) +
                       new String(new byte[] {Character.LETTER_NUMBER}));
        else //MARK
            sb.append( "Vendor Name,Marking Spec No, Marking Spec Version,Pin Count,Package Code,Body Size,Tester Type,APL Status,APL Number, Log Time" +
                       new String(new byte[] {Character.LINE_SEPARATOR}) +
                       new String(new byte[] {Character.LETTER_NUMBER}));
      }
      else if (exportType.equals("process")) {
        com.mxic.oiplus.apl.bean.APLSearch.REPORT data =
            (com.mxic.oiplus.apl.bean.APLSearch.REPORT)result.get(0);
        if (data.getProcType().equals("WS"))
          sb.append( "Product Body,Option,Test Mode,Tester Type,Vendor Name,APL Status,Issue Status,Issue Type,Issue No,Initial Time,Initial User,Process Time,Process User,Complete Time,Complete User" +
              new String(new byte[] {Character.LINE_SEPARATOR}) +
              new String(new byte[] {Character.LETTER_NUMBER}));
        else
          sb.append( "Product Body,Option,Package Code,Pin Count,Test Mode,Tester Type,Vendor Name,APL Status,Issue Status,Issue Type,Issue No,Initial Time,Initial User,Process Time,Process User,Complete Time,Complete User" +
                     new String(new byte[] {Character.LINE_SEPARATOR}) +
                     new String(new byte[] {Character.LETTER_NUMBER}));
      }
      else if (exportType.equals("history")) {
        com.mxic.oiplus.apl.bean.APLSearch.REPORT data
            = (com.mxic.oiplus.apl.bean.APLSearch.REPORT)result.get(0);
        if (data.getProcType().equals("WS"))
          sb.append( "Issue Type,Issue No,Product Body,Option,Test Mode,Tester Type,Vendor Name,APL Status,Issue Status,Initial Time,Initial User,Process Time,Process User,Complete Time,Complete User" +
              new String(new byte[] {Character.LINE_SEPARATOR}) +
              new String(new byte[] {Character.LETTER_NUMBER}));
        else
          sb.append( "Issue Type,Issue No,Product Body,Option,Package Code,Pin Count,Test Mode,Tester Type,Vendor Name,APL Status,Issue Status,Initial Time,Initial User,Process Time,Process User,Complete Time,Complete User" +
                     new String(new byte[] {Character.LINE_SEPARATOR}) +
                     new String(new byte[] {Character.LETTER_NUMBER}));
      }
    }

    for ( int i = 0; i < result.size(); i++ ) {
      sb.append(result.get(i).toString() +
                new String(new byte[] {Character.LINE_SEPARATOR}) +
                new String(new byte[] {Character.LETTER_NUMBER}));
    }
    String content = sb.toString();
    response.setContentType("application/x-msdownload");
    //不需要，設定反而會有問題 (mxtst07 WL有問題，Local Tomcat不會)
    //response.setContentLength(content.getBytes().length);
    response.setLocale(new Locale(new String("zh"), new String("TW")));
    response.setHeader("Content-Disposition", String.valueOf("attachment; filename="+exportType+".csv"));
    //
    java.io.PrintWriter out = response.getWriter();
    out.println(content);
    out.flush();
    sb = null;
    //sb.delete(0, sb.length());
  }
}
