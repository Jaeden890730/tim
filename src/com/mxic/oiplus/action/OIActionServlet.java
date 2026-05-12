package com.mxic.oiplus.action;

import java.io.IOException;
import java.sql.Connection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionServlet;
import org.apache.struts.action.RequestProcessor;
import org.apache.struts.config.ModuleConfig;
import org.apache.struts.util.ModuleUtils;
import com.mxic.oiplus.au.AUACTForm;
import com.mxic.oiplus.au.Authority;
import com.mxic.oiplus.resource.TDSResource;
import com.mxic.oiplus.util.TDSLogger;
import com.mxic.peis.au.AUService;
import com.mxic.tdsplus.resource.DBConnection;



/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class OIActionServlet extends ActionServlet {

  public static Map TotalSession = null;
  /**
   * <p>The request attribute under which the path information is stored for
   * processing during a <code>RequestDispatcher.include</code> call.</p>
   */
  public static final String INCLUDE_PATH_INFO =
      "javax.servlet.include.path_info";


  /**
   * <p>The request attribute under which the servlet path information is stored
   * for processing during a <code>RequestDispatcher.include</code> call.</p>
   */
  public static final String INCLUDE_SERVLET_PATH =
      "javax.servlet.include.servlet_path";


  static{
    TotalSession = getSingleMap();
  }

  static Map getSingleMap(){
    if(TotalSession == null){
      TotalSession = Collections.synchronizedMap(new HashMap());
    }
    return TotalSession;
  }

  /**
   * <p>Returns the RequestProcessor for the given module or null if one does not
   * exist.  This method will not create a RequestProcessor.</p>
   *
   * @param config The ModuleConfig.
   */
  private RequestProcessor getProcessorForModule(ModuleConfig config) {
      String key = Globals.REQUEST_PROCESSOR_KEY + config.getPrefix();
      return (RequestProcessor) getServletContext().getAttribute(key);
  }

  /**
   * <p>Perform the standard request processing for this request, and create
   * the corresponding response.</p>
   *
   * @param request The servlet request we are processing
   * @param response The servlet response we are creating
   *
   * @exception IOException if an input/output error occurs
   * @exception ServletException if a servlet exception is thrown
   */
  protected void process(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {

      ModuleUtils.getInstance().selectModule(request, getServletContext());
      ModuleConfig config = getModuleConfig(request);

      RequestProcessor processor = getProcessorForModule(config);
      if (processor == null) {
         processor = getRequestProcessor(config);
      }
	  listSessionInfo(request);
	  updateSessionMap(request);

	  String path = processPath(request, response, config);

      String authority_service = TDSResource.getProperties("TDS").getValue("authority.service");
      //TDSResource.getValue("authority.service");
      if(authority_service == null || !authority_service.equals("off")){
        if(path != null && !path.equals("/au/AULoginAction")&& 
				!path.equals("/apl/APLShow")){
          HttpSession session = request.getSession();
          Authority auth = (Authority)session.getAttribute("user_authority");
          if(auth == null){
            response.sendRedirect(response.encodeRedirectURL(request.getContextPath()+"/Login/login.jsp"));
            return;
          }
          if(!checkAuthority(auth,path)){
        	if(path.indexOf("gprs")>0 || path.indexOf("GPRS")>0){
        		response.sendRedirect(response.encodeRedirectURL(request.getContextPath()+"/NoAuthority.jsp?gprs=Y"));
        	}else{
        		response.sendRedirect(response.encodeRedirectURL(request.getContextPath()+"/NoAuthority.jsp?gprs=N"));
        	}
            
            return;
          }
          //AUService.insertTDSLog(auth,path);
          Connection conn = null;
          try {
              conn = DBConnection.getConnection();
              AUService service = new AUService(request, conn, "TIM", null, TDSResource.getProperties("TDS"), true, TDSLogger.class);
              service.insertTDSLog(conn, auth.getUser_name(), path);
          } catch (Exception e) {
              TDSLogger.println(e);
          } finally {
              DBConnection.close(conn);
          }
        }
      }
      try{
        request.setCharacterEncoding("Big5");
      }catch (Exception ex){}
      processor.process(request, response);

  }

  /**
   * <p>Identify and return the path component (from the request URI) that
   * we will use to select an <code>ActionMapping</code> with which to dispatch.
   * If no such path can be identified, create an error response and return
   * <code>null</code>.</p>
   *
   * @param request The servlet request we are processing
   * @param response The servlet response we are creating
   *
   * @exception IOException if an input/output error occurs
   */
  protected String processPath(HttpServletRequest request,
                               HttpServletResponse response,
                               ModuleConfig moduleConfig)
      throws IOException {

      String path = null;

      // For prefix matching, match on the path info (if any)
      path = (String) request.getAttribute(INCLUDE_PATH_INFO);
      if (path == null) {
          path = request.getPathInfo();
      }
      if ((path != null) && (path.length() > 0)) {
          return (path);
      }

      // For extension matching, strip the module prefix and extension
      path = (String) request.getAttribute(INCLUDE_SERVLET_PATH);
      if (path == null) {
          path = request.getServletPath();
      }
      String prefix = moduleConfig.getPrefix();
      if (!path.startsWith(prefix)) {
          String msg = getInternal().getMessage("processPath");

          log.error(msg + " " + request.getRequestURI());
          response.sendError(HttpServletResponse.SC_BAD_REQUEST, msg);

          return null;
      }

      path = path.substring(prefix.length());
      int slash = path.lastIndexOf("/");
      int period = path.lastIndexOf(".");
      if ((period >= 0) && (period > slash)) {
          path = path.substring(0, period);
      }
      return (path);

  }


  /**
   * check path in Authority
   *
   * @param request The servlet request we are processing
   */
  protected boolean checkAuthority(Authority auth, String path) {
    if(auth != null){
      AUACTForm[] aufm = auth.getAct_action();
      if(aufm != null && aufm.length>0){
        for(int i=0;i<aufm.length;i++){
          if(aufm[i].getAct_class().equals(path)){
            return true;
          }
        }
      }
    }
    return false;
  }

  public void listSessionInfo(HttpServletRequest request){
    TDSLogger.println("SESSION INFO");
    int i=0;
    try{
      StringBuffer bf = new StringBuffer();
      bf.append("======================================="+"\n");
      HttpSession session = request.getSession();
      if(session!=null){
        bf.append("Session Information of "+session.getId()+"\n");
        Enumeration enu =  session.getAttributeNames();
        if(enu!=null){
          while(enu.hasMoreElements()){
            String key  = (String)enu.nextElement();
            bf.append("Attribute["+key+"]="+session.getAttribute(key)+"\n");
            key = null;
            i++;
          }
        }
      }
      bf.append("======================================= "+i+" items.\n");
      bf.append(Runtime.getRuntime().freeMemory()+"/"+Runtime.getRuntime().totalMemory()+"\n");
      TDSLogger.println(bf.toString());
    }catch(Exception e){
      TDSLogger.println(e);
    }
  }

  void updateSessionMap(HttpServletRequest request){
    try{
      HttpSession session = request.getSession();
      String sessionkey = null;
      if(session != null){
        sessionkey = session.getId();
      }else{
        sessionkey = "dummy";
      }
      long now = System.currentTimeMillis();
      synchronized(TotalSession){
        TotalSession.put(sessionkey, new Long(now));

        String timeoutstr = TDSResource.getProperties("TDS").getValue("session.timeout");
        long oldtime = now;
        if(timeoutstr != null){
          oldtime = now - (Integer.parseInt(timeoutstr)*1000);
        }


        Iterator itr = TotalSession.keySet().iterator();
        String oldkey  = null;
        Long oldvalue = null;
        while(itr.hasNext()){
          oldkey = (String)itr.next();
          if((oldvalue = (Long)TotalSession.get(oldkey))!=null){
            if(oldvalue.longValue() < oldtime){
              TotalSession.remove(oldkey);
              break;
            }
          }
        }
      }
    }catch(Exception e){
     // TDSLogger.println(e);
    }
  }
}