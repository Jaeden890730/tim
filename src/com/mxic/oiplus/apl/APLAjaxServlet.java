package com.mxic.oiplus.apl;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import java.util.*;
import com.mxic.oiplus.util.*;
import com.mxic.oiplus.resource.DBConnection;

public class APLAjaxServlet extends HttpServlet {

  // get available test mode for product/process for apply 'aplStatus' (hold or release)
  protected void get_test_mode_by_procprod(HttpServletRequest request,
                                           PrintWriter out) {
    String product_body = request.getParameter("product_body");
    String process_type = request.getParameter("process_type");
    String apl_status = request.getParameter("aplStatus");

    ArrayList test_mode = getTestModeForApply(product_body,process_type,apl_status);

    out.println("<test_mode>");
    if (test_mode != null)
    for (int i = 0; i < test_mode.size(); i++)
      out.println("<mode>" + (String)test_mode.get(i) + "</mode>");
    out.println("</test_mode>");
    out.close();
    /*        out.println("<response>");
            out.println("<passed>" + Boolean.toString(passed) + "</passed>");
            out.println("<message>" + message + "</message>");
            out.println("</response>");
            out.close();
    */
  }

  public static ArrayList getTestModeForApply(String product_body,
                                              String process_type,
                                              String apl_status) {
    String sql;
    ArrayList vl = null;
    Connection conn = null;

    try {
      conn = DBConnection.getConnection();
      sql = "SELECT DISTINCT TEST_MODE FROM AP_APL " +
            "WHERE PRODUCT_BODY = ? " +
            " AND PROCESS_TYPE = ? " +
            " AND APL_STATUS = ? " +
            " AND APL_IS_AVAILABLE(SID) = 1";

      PreparedStatement ps = conn.prepareStatement(sql);
      ps.setString(1,product_body);
      ps.setString(2,process_type);
      ps.setString(3,apl_status);
      ResultSet rs = ps.executeQuery();

      while (rs.next()) {
        if (vl == null)
          vl = new ArrayList();
          vl.add(rs.getString("TEST_MODE"));
      }
      rs.close();
      rs = null;
    }
    catch (Exception ex) {
      ex.printStackTrace();
      ex.fillInStackTrace();
      TDSLogger.println(ex.getMessage());
      return null;
    }
    finally {
      DBConnection.close(conn);
      conn = null;
    }
    return vl;
  }


  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    String ajax_type = request.getParameter("type");

    response.setContentType("text/xml");
    response.setHeader("charset","Big5");
//    response.setLocale(new Locale(new String("zh"), new String("TW")));
    response.setHeader("Cache-Control", "no-cache");

    if (ajax_type.equals("get_test_mode_by_procprod"))
      get_test_mode_by_procprod(request, out);
  }
}
