


package com.mxic.oiplus.apl;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.mxic.oiplus.apl.bean.APLSearch;
import com.mxic.oiplus.apl.bean.APLUser;
import com.mxic.oiplus.apl.bean.ApplyForm;
import com.mxic.oiplus.au.User;
import com.mxic.oiplus.resource.DBConnection;
import com.mxic.oiplus.resource.TDSResource;
//import mic.af.aplrms.*;
import com.mxic.oiplus.util.TDSLogger;

public class APLSearchAction extends Action {
  private APLSearchForm form = null;
  private Connection conn = null;
  //private APLManager aplManager = null;

  public ActionForward execute(ActionMapping mapping,
                               ActionForm form,
                               HttpServletRequest request,
                               HttpServletResponse response) {

    ActionForward forward = null;

    //if (aplManager == null)
    //  aplManager = APLManager.getInstance((String)TDSResource.getProperties("APL").get("af_server_ip"));
    // 申請的動作需要 AgentFlow on-line 才有意義
    /*
    if (request.getParameter("P") != null && request.getParameter("P").startsWith("apply")){
      if (aplManager == null || !aplManager.isAlive()) {
        request.setAttribute("af_unavailable", "false");
        forward = mapping.findForward("af_unavailable");
        request.setAttribute("searchTarget", "data");
        request.setAttribute("searchOption", "sign");
        return forward;
      }
    }
    */
//???    aplManager.destroy();
    this.form = (APLSearchForm) form;
    if (this.form.getAplStatus() != null) // status == 'A' 表示不限 status
      if (this.form.getAplStatus().equals("A")) // status == 'A' 表示不限 status
        this.form.setAplStatus("");
    if ( request.getParameter("T") != null )
      this.form.setSearchTarget(request.getParameter("T"));
    if ( request.getParameter("P") != null )
      this.form.setSearchOption(request.getParameter("P"));

    // don't restrict PIM Release Date while initializing APL Search page
    if (request.getParameter("T") != null && request.getParameter("P") != null) {
      ((APLSearchForm) form).setEndTime(null);
      ((APLSearchForm) form).setStartTime(null);
    }
    // ignore PIM Release Date in history/process search
    if (((APLSearchForm) form).getSearchTarget().equals("process")) {
      ((APLSearchForm) form).setEndTime(null);
      ((APLSearchForm) form).setStartTime(null);
    }
    // ignore PIM Release Date in searching apply case
    if (((APLSearchForm) form).getSearchOption().equals("sign")) {
      ((APLSearchForm) form).setEndTime(null);
      ((APLSearchForm) form).setStartTime(null);
    }

    try {
      conn = DBConnection.getConnection();
      if ( request.getParameter("appNo") != null ) {
        forward = view(request.getParameter("appNo").trim(), mapping, request, response);
      }else if (request.getParameter("npiNo") != null ){
    	forward = view_npi(request.getParameter("npiNo").trim(), "NPI", mapping, request, response); 
      }else if (request.getParameter("timftNo") != null ){
        forward = view_npi(request.getParameter("timftNo").trim(), "TIMFT", mapping, request, response);
      }else if (request.getParameter("semwsNo") != null ){
      	forward = view_npi(request.getParameter("semwsNo").trim(), "SEMWS", mapping, request, response);
      }else if (request.getParameter("semaviNo") != null ){
        forward = view_npi(request.getParameter("semaviNo").trim(), "SEMAVI", mapping, request, response);
      }else if (request.getParameter("semfviNo") != null ){
        forward = view_npi(request.getParameter("semfviNo").trim(), "SEMFVI", mapping, request, response);
      }else if (request.getParameter("semmarkNo") != null ){
        forward = view_npi(request.getParameter("semmarkNo").trim(), "SEMMARK", mapping, request, response);  	
      }else {
        if ( !this.form.isValid() ) {
          // 基本資料查詢：SearchTarget=data, SearchOption=query
          // 歷史及處理中資料查詢：SearchTarget=process, SearchOption=query
          // 生效申請：SearchTarget=data, SearchOption=applyNew (Check AF)
          // 停復投作業 by product：SearchTarget=data, SearchOption=applyByP (Check AF)
          // 停復投作業 by tester：SearchTarget=data, SearchOption=applyByT (Check AF)
          // 申請中案件列表：SearchTarget=data, SearchOption=sign
          forward = initial(mapping, request, response);
        }
        else {
          if ( this.form.getSearchOption().equals("sign") ) {
            // 查詢申請中案件：SearchTarget=null, SearchOption=null
            String appNo = this.form.getIssueNumber();
            if ( appNo != null && appNo.trim().length() > 0 )
              forward = view(appNo.trim(), mapping, request, response);
            else
              forward = doSearch(mapping, request, response);
          }
          else
            forward = doSearch(mapping, request, response);
        }
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    } finally {
      DBConnection.close(conn);
      conn = null;
    }
    request.setAttribute("searchTarget", this.form.getSearchTarget());
    request.setAttribute("searchOption", this.form.getSearchOption());
    if(this.form.getSearchOption().equals("applyByP") || this.form.getSearchOption().equals("applyByT"))
      request.setAttribute("app_form", "APL");
    return forward;
  }

  public ActionForward initial(ActionMapping mapping,
                               HttpServletRequest request,
                               HttpServletResponse response) throws Exception {
    if ( form.getSearchOption().startsWith("sign") ) {
      return mapping.findForward("applyquery");
    }
    if ( form.getSearchOption().startsWith("apply") ) { // apply xxx
        APLSearch search = new APLSearch("data", "query");
        ArrayList all = search.execute(conn);
        request.setAttribute("criterion", search);
      return mapping.findForward("applyquery");
    }
    TDSLogger.println("APLSearchAction：dataquery start!!");
    return mapping.findForward("dataquery"); // startsWith("query")
  }

  public ActionForward doSearch(ActionMapping mapping,
                                HttpServletRequest request,
                                HttpServletResponse response) throws Exception {
    APLSearch search = new APLSearch(form.getSearchTarget(), form.getSearchOption());
    ArrayList all = search.execute(conn, form.getSearch(), null);
    request.setAttribute("list", all);
    request.setAttribute("selectMode", "none");
    if ( form.getSearchOption().equals("sign") ) {
      return mapping.findForward("signlist");
    }
    if ( form.getSearchOption().equals("query") ) {
      request.setAttribute("actionType", "listData");
      request.setAttribute("readonly", Boolean.TRUE);
      if ( form.getSearchTarget().equals("process") &&
           form.getIssueStatus().indexOf('C') >= 0 )
        form.setSearchTarget("history");
    }
    if ( form.getSearchOption().startsWith("apply") ) {
      User user = (User) request.getSession().getAttribute("user");
      request.setAttribute("applicant", new APLUser(user));
      request.setAttribute("query", form.getSearch());
      request.setAttribute("actionType", getApplyActionByStatus(form.getAplStatus().charAt(0)));
      request.setAttribute("readonly", Boolean.FALSE);
    }
    if ( form.getSearchTarget().equals("data") ) {
      if ( form.getSearchOption().equals("query") ) {
        request.setAttribute("selectMode", "single");
      }
      if ( form.getSearchOption().startsWith("apply") ) {
        request.setAttribute("selectMode", "multiple");
      }
    }
    request.setAttribute("procType", form.getProcType());
    return mapping.findForward("searchlist");
  }
  
  /**
   * 
   * @param applyNo
   * @param mapping
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public ActionForward view(String applyNo,
                            ActionMapping mapping,
                            HttpServletRequest request,
                            HttpServletResponse response) throws Exception {
    form.setSearchTarget("data");
    request.setAttribute("selectMode", "none");
    request.setAttribute("readonly", Boolean.TRUE);
    // load apply form
    ApplyForm applyForm = ApplyForm.getApplyForm(conn, applyNo);
    if ( applyForm != null ) {
      String action = getApplyActionByType(applyForm.getApplyType().charAt(0));
      APLSearch search = new APLSearch(form.getSearchTarget(), action);
      HashMap queryItems = new HashMap();
      queryItems.put("SID", applyForm.getData());
      request.setAttribute("list", search.execute(conn, queryItems, null));
      request.setAttribute("applicant", applyForm.getApplicant());
      if ( !form.getSearchOption().startsWith("sign") && !form.getSearchOption().startsWith("apply") ) {
        form.setSearchOption("applyBy" + applyForm.getBatchType());
        if ( applyForm.getApplyType().charAt(0) == APLDef.ApplyType.NEW )
          form.setSearchOption("applyNew");
      }

      String AF_URL = "";
      String AF_HIST_URL = "";
      String NPI_URL = "";
      String SEMWS_URL = "";
      String SEMAVI_URL = "";
      String SEMFVI_URL = "";
      String SEMMARK_URL = "";
      
      if (applyForm.getAf_taskid() != null && !applyForm.getAf_taskid().equals("")) {
          /*
        TaskProgress tp = aplManager.getApplyStatus(applyForm.getAf_taskid());
        AF_URL = "http://" +
            (String)TDSResource.getProperties("APL").get("af_server_ip").toString().trim() +
            ":" +
            (String)TDSResource.getProperties("APL").get("af_web_port").toString().trim() +
            "/WebAgenda/SingleSignon.do?loginName=" +
            applyForm.getApplicant().getEmpNo() +
            "&checkpass=false&action=/preAction.do?artInsID=" +
            tp.getFormID();
        AF_HIST_URL = "http://" +
            (String)TDSResource.getProperties("APL").get("af_server_ip").toString().trim() +
            ":" +
            (String)TDSResource.getProperties("APL").get("af_web_port").toString().trim() +
            "/WebAgenda/SingleSignon.do?loginName=" +
            applyForm.getApplicant().getEmpNo() +
            "&checkpass=false&action=/TaskReview.do?taskID=" +
            applyForm.getAf_taskid();
        String key = com.flowring.util.common.SecurityURL.encode("loginName=" + applyForm.getApplicant().getEmpNo() +
            "&checkpass=false&action=/preAction.do?artInsID=" + tp.getFormID());

        AF_URL = "http://" +
            (String)TDSResource.getProperties("APL").get("af_server_ip").toString().trim() +
            ":" +
            (String)TDSResource.getProperties("APL").get("af_web_port").toString().trim() +
            "/WebAgenda/SingleSignon.do?key=" + key;

        key = com.flowring.util.common.SecurityURL.encode("loginName=" + applyForm.getApplicant().getEmpNo() +
            "&checkpass=false&action=/TaskReview.do?taskID=" + applyForm.getAf_taskid());

        AF_HIST_URL = "http://" +
            (String)TDSResource.getProperties("APL").get("af_server_ip").toString().trim() +
            ":" +
            (String)TDSResource.getProperties("APL").get("af_web_port").toString().trim() +
            "/WebAgenda/SingleSignon.do?key=" + key;
         */
      }else{	//Add 查詢案件簽核單 and 記錄列表 link by Kevin Huang on 2012.11.06
    	  AF_URL = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    	  AF_URL += "/apl/APLAppSignAction.do?act=query&app_no=" + applyForm.getApplyNo();  
    	  
    	  AF_HIST_URL = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    	  AF_HIST_URL += "/apl/APLAppSignAction.do?act=getSignRecordList&app_no=" + applyForm.getApplyNo();    	  
      }
      request.setAttribute("procType", applyForm.getProcType());
      request.setAttribute("actionType", action);
      request.setAttribute("searchOption", form.getSearchOption());
      request.setAttribute("remarks", applyForm.getReason());
      request.setAttribute("fileName", applyForm.getAttachment());
      request.setAttribute("afurl", AF_URL);
      request.setAttribute("afhisturl", AF_HIST_URL);
      request.setAttribute("app_form", "APL");
      request.setAttribute("npiurl", NPI_URL);
      request.setAttribute("semwsurl", SEMWS_URL);
      request.setAttribute("semaviurl", SEMAVI_URL);
      request.setAttribute("semfviurl", SEMFVI_URL);
      request.setAttribute("semmarkurl", SEMMARK_URL);
    }
    return mapping.findForward("searchlist");
  }
  
  public ActionForward view_npi(String applyNo,
		                    String appType,
                            ActionMapping mapping,
                            HttpServletRequest request,
                            HttpServletResponse response) throws Exception {
    form.setSearchTarget("data");
    request.setAttribute("selectMode", "none");
    request.setAttribute("readonly", Boolean.TRUE);
    // load apply form (npi, semws, semavi, semfvi, semmark)
    ApplyForm applyForm = ApplyForm.getApplyFormNPI(conn, applyNo, appType);
    if ( applyForm != null ) {
      String action = getApplyActionByType(applyForm.getApplyType().charAt(0));
      APLSearch search = new APLSearch(form.getSearchTarget(), action);
      HashMap queryItems = new HashMap();
      queryItems.put("SID", applyForm.getData());
      request.setAttribute("list", search.execute(conn, queryItems, null));
      request.setAttribute("applicant", applyForm.getApplicant());
      if ( !form.getSearchOption().startsWith("sign") && !form.getSearchOption().startsWith("apply") ) {
        form.setSearchOption("applyBy" + applyForm.getBatchType());
        if ( applyForm.getApplyType().charAt(0) == APLDef.ApplyType.NEW )
          form.setSearchOption("applyNew");
      }

      String AF_URL = "";
      String AF_HIST_URL = "";
      String NPI_URL = "";
      String TIMFT_URL = "";
      String SEMWS_URL = "";
      String SEMAVI_URL = "";
      String SEMFVI_URL = "";
      String SEMMARK_URL = "";
      String app_form = "";
      if (applyForm.getAf_taskid() != null && !applyForm.getAf_taskid().equals("")) {
    	  /*
        TaskProgress tp = aplManager.getApplyStatus(applyForm.getAf_taskid());
        String key = com.flowring.util.common.SecurityURL.encode("loginName=" + applyForm.getApplicant().getEmpNo() +
            "&checkpass=false&action=/preAction.do?artInsID=" + tp.getFormID());

        AF_URL = "http://" +
            (String)TDSResource.getProperties("APL").get("af_server_ip").toString().trim() +
            ":" +
            (String)TDSResource.getProperties("APL").get("af_web_port").toString().trim() +
            "/WebAgenda/SingleSignon.do?key=" + key;

        key = com.flowring.util.common.SecurityURL.encode("loginName=" + applyForm.getApplicant().getEmpNo() +
            "&checkpass=false&action=/TaskReview.do?taskID=" + applyForm.getAf_taskid());

        AF_HIST_URL = "http://" +
            (String)TDSResource.getProperties("APL").get("af_server_ip").toString().trim() +
            ":" +
            (String)TDSResource.getProperties("APL").get("af_web_port").toString().trim() +
            "/WebAgenda/SingleSignon.do?key=" + key;
            */
      }
      //read NPI applyform--ttp://mxpeis/npi/ShowPageAction.do?app_id=WS-0012B-201001-01441
      if (appType.equals("NPI")){
	      NPI_URL = "http://" + (String)TDSResource.getProperties("APL").get("server_ip_port_npi").toString().trim() +
	      "ShowPageAction.do?npi_app_id="+applyNo;
	      app_form = "NPI";
      }else if (appType.equals("TIMFT")){
    	  TIMFT_URL = "";
    	  app_form = "TIMFT";    
      }else if (appType.equals("SEMWS")){
    	  //SEMWS_URL = "http://" + (String)TDSResource.getProperties("APL").get("server_ip_port_sem").toString().trim() +
	      //"showpage.do?sem_app_id="+applyNo;
    	//http://192.168.211.123:7001/sem/login.jsp?redirectUrl=%2Fts%2FWSAPL_ApplyAction.do%3Fact%3DtoQuery%26appId%3DW111200003
    	  SEMWS_URL = "http://" + (String)TDSResource.getProperties("APL").get("server_ip_port_sem").toString().trim() +
	      "login.jsp?redirectUrl=%2Fts%2FWSAPL_ApplyAction.do%3Fact%3DtoQuery%26appId%3D"+applyNo;
    	  app_form = "SEMWS";
      }else if (appType.equals("SEMAVI")){
    	  //SEMAVI_URL = "http://" + (String)TDSResource.getProperties("APL").get("server_ip_port_sem").toString().trim() +
	      //"showpage.do?sem_app_id="+applyNo;
    	  SEMAVI_URL = "http://" + (String)TDSResource.getProperties("APL").get("server_ip_port_sem").toString().trim() +
	      "login.jsp?redirectUrl=%2Fts%2FAVIAPL_ApplyAction.do%3Fact%3DtoQuery%26appId%3D"+applyNo;
    	  app_form = "SEMAVI";
      }else if (appType.equals("SEMFVI")){
    	  //SEMFVI_URL = "http://" + (String)TDSResource.getProperties("APL").get("server_ip_port_sem").toString().trim() +
	      //"showpage.do?sem_app_id="+applyNo;
    	  SEMFVI_URL = "http://" + (String)TDSResource.getProperties("APL").get("server_ip_port_sem").toString().trim() +
	      "login.jsp?redirectUrl=%2Fts%2FFVIAPL_ApplyAction.do%3Fact%3DtoQuery%26appId%3D"+applyNo;
    	  app_form = "SEMFVI";
      }else if (appType.equals("SEMMARK")){
    	  //SEMMARK_URL = "http://" + (String)TDSResource.getProperties("APL").get("server_ip_port_sem").toString().trim() +
	      //"showpage.do?sem_app_id="+applyNo;
    	  SEMMARK_URL = "http://" + (String)TDSResource.getProperties("APL").get("server_ip_port_sem").toString().trim() +
	      "login.jsp?redirectUrl=%2Fts%2FMARKAPL_ApplyAction.do%3Fact%3DtoQuery%26appId%3D"+applyNo;
    	  app_form = "SEMMARK";
      }
    	  
      request.setAttribute("procType", applyForm.getProcType());
      request.setAttribute("actionType", action);
      request.setAttribute("searchOption", form.getSearchOption());
      request.setAttribute("remarks", applyForm.getReason());
      request.setAttribute("fileName", applyForm.getAttachment());
      request.setAttribute("afurl", AF_URL);
      request.setAttribute("afhisturl", AF_HIST_URL);
      request.setAttribute("app_form", app_form);//"NPI"
      request.setAttribute("npiurl", NPI_URL);
      request.setAttribute("timfturl", TIMFT_URL);
      request.setAttribute("semwsurl", SEMWS_URL);
      request.setAttribute("semaviurl", SEMAVI_URL);
      request.setAttribute("semfviurl", SEMFVI_URL);
      request.setAttribute("semmarkurl", SEMMARK_URL);
      request.setAttribute("aplmail", "Y");
    }
    return mapping.findForward("searchlist");
  }
  

  private String getApplyActionByStatus(char aplState) {
    if (aplState == APLDef.AplState.RELEASE)
      return "applyHold";
    if (aplState == APLDef.ApplyType.HOLD)
      return "applyRelease";
    return "applyNew";
  }

  private String getApplyActionByType(char applyType) {
    if (applyType == APLDef.ApplyType.HOLD)
      return "applyHold";
    if (applyType == APLDef.ApplyType.RELEASE)
      return "applyRelease";
    return "applyNew";
  }
}
