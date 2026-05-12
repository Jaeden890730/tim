package com.mxic.oiplus.apl;

import java.sql.*;
import java.util.*;
import javax.servlet.http.*;

import org.apache.struts.action.*;
import com.mxic.oiplus.apl.bean.*;
import com.mxic.oiplus.resource.*;

public class APLApplyAction extends Action {
  private APLApplyForm form = null;
  private ApplyForm apply = null;
  private Connection conn = null;

  public ActionForward execute(ActionMapping mapping,
                               ActionForm form,
                               HttpServletRequest request,
                               HttpServletResponse response) {
    this.form = (APLApplyForm) form;
    ActionForward forward = null;
    try {
      conn = DBConnection.getConnection();
      if ( request.getParameter("appNo") != null ) {
        forward = sign(request.getParameter("appNo").trim(), mapping, request, response);
      }
      else {
        apply = ApplyForm.getApplyForm(conn, this.form.getApplyNo());
        if (apply != null) {
          Class[] paramClass = new Class[] {ActionMapping.class, HttpServletRequest.class,HttpServletResponse.class};
          java.lang.reflect.Method actionMethod = this.getClass().getMethod(this.form.getActionType(), paramClass);
          forward = (ActionForward) actionMethod.invoke(this, new Object[] {mapping, request, response});
        }
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    } finally {
      DBConnection.close(conn);
      conn = null;
    }
    return forward;
  }

  public ActionForward apply(ActionMapping mapping,
                             HttpServletRequest request,
                             HttpServletResponse response) throws Exception {
    APLUser user = APLUser.getUserById(conn, Integer.valueOf(form.getManager()));
    user.setUserId("1");
    apply.setManager(user);
    user.setUserId(form.getDirector());
    user.setUserId("1");
    apply.setDirector(user);
    ApplyFlow agent = new ApplyFlow(conn);
    agent.apply(apply);
    // forward to search page
    return home(mapping, request, response);
  }

  public ActionForward delete(ActionMapping mapping,
                              HttpServletRequest request,
                              HttpServletResponse response) throws Exception {
    ApplyFlow agent = new ApplyFlow(conn);
    agent.delete(apply);
    // forward to search page
    return home(mapping, request, response);
  }

  public ActionForward exit(ActionMapping mapping,
                            HttpServletRequest request,
                            HttpServletResponse response) throws Exception {
    // forward to search page
    return home(mapping, request, response);
  }

  public ActionForward sign(String applyNo,
                            ActionMapping mapping,
                            HttpServletRequest request,
                            HttpServletResponse response) throws Exception {
    ApplyForm applyForm = ApplyForm.getApplyForm(conn, applyNo);
    if ( applyForm != null ) {
      request.setAttribute("applyForm", applyForm);
    }
    request.setAttribute("actionType", "sign");
    return mapping.findForward("sign");
  }

  public ActionForward accept(ActionMapping mapping,
                              HttpServletRequest request,
                              HttpServletResponse response) throws Exception {
    String status = apply.getApplyStatus();
    ApplyFlow agent = new ApplyFlow(conn);
    //agent.reject(apply);
    // forward to search page
    return home(mapping, request, response);
  }

  // 實際退件
  public ActionForward reject(ActionMapping mapping,
                              HttpServletRequest request,
                              HttpServletResponse response) throws Exception {
    String status = apply.getApplyStatus();
    ApplyFlow agent = new ApplyFlow(conn);
    agent.reject(apply, request);
    // forward to search page
    return home(mapping, request, response);
  }

  public ActionForward resend(ActionMapping mapping,
                              HttpServletRequest request,
                              HttpServletResponse response) throws Exception {
	  String contextPath = request.getServerName() + ":" + String.valueOf(request.getServerPort()) + 
	  						request.getContextPath();
	  APLUtil.resend(contextPath, apply.getApplyNo());
	  /*
    String AFServerIP = (String)TDSResource.getProperties("APL").get("af_server_ip");
    APLManager aplManager = APLManager.getInstance(AFServerIP);
    TaskProgress tp = aplManager.getApplyStatus(apply.getAf_taskid());

    ArrayList list = tp.getCpTask();

    if (list!=null){
      Iterator it = list.iterator();
      while (it.hasNext()) {
        System.out.println("平行簽核子流程簽核情形");
        TaskProgress t = (TaskProgress) it.next();
        String taskState = t.getTaskStatus();

        // 只取執行中的 task
        if (!"complete".equals(taskState) && !"dead".equals(taskState)) {
          aplManager.resendSignNotice(t.getTaskID());
        }
      }
    } else aplManager.resendSignNotice(tp.getTaskID());
    */

    request.setAttribute("af_resent","success");
    return home(mapping, request, response);
  }

  public ActionForward revise(ActionMapping mapping,
                              HttpServletRequest request,
                              HttpServletResponse response) throws Exception {
    return home(mapping, request, response);
  }

  public ActionForward modify(ActionMapping mapping,
                              HttpServletRequest request,
                              HttpServletResponse response) throws Exception {
    String action = "apply";
    if ( apply.getApplyType().charAt(0) == APLDef.ApplyType.NEW )
      action = "applyNew";
    else
      action = action + "By" + apply.getBatchType();
    request.setAttribute("searchTarget", "data");
    request.setAttribute("searchOption", action);
    if ( !action.equals("applyNew") ) {
      action = apply.getApplyType().charAt(0) == APLDef.ApplyType.HOLD ? "applyHold" : "applyRelease";
    }
    request.setAttribute("actionType", action);
    request.setAttribute("applicant", apply.getApplicant());
    request.setAttribute("procType", apply.getProcType());
    request.setAttribute("applyNo", apply.getApplyNo());
    request.setAttribute("remarks", apply.getReason());
    request.setAttribute("fileName", apply.getAttachment());
    request.setAttribute("reapply", "yes");
    // do search
    APLSearch search = new APLSearch("data", "apply");
    String query = apply.getQueryString();
    HashMap queryItems = APLSearchForm.getSearch(APLUtil.split(query, "|"));
    queryItems.put("SID", "%"); // for search to skip condition (APL_NO IS NULL)
    request.setAttribute("list", search.execute(conn, queryItems, apply.getApplyNo()));
    queryItems.remove("SID");
    request.setAttribute("query", queryItems);
    request.setAttribute("selectMode", "multiple");
    request.setAttribute("readonly", Boolean.FALSE);
    return mapping.findForward("modify");
  }

  public ActionForward home(ActionMapping mapping,
                            HttpServletRequest request,
                            HttpServletResponse response) throws Exception {
    // forward to search page
    APLSearch search = new APLSearch("data","sign");
    HashMap queryItems = new HashMap();
    queryItems.put("APP_STATUS","I,N,J,P");
    ArrayList all = search.execute(conn, queryItems, null);
    request.setAttribute("list", all);
    request.setAttribute("selectMode", "none");
    request.setAttribute("searchTarget", "data");
    request.setAttribute("searchOption", "sign");
    return mapping.findForward("success");
  }
}