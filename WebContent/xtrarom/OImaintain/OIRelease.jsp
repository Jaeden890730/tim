<!-- /xtrarom/OImaintain/OIRelease.jsp -->

<%@page contentType="text/html; charset=Big5"%>
<%@taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<%@page import="java.util.*"%>
<%@page import="com.mxic.oiplus.xtrarom.oimaintain.*"%>
<%
 // String pd_body = request.getParameter("pd_body");
  //String sid = request.getParameter("sid");
 // String brand = request.getParameter("brand");
 // String version = request.getParameter("version");

  String pd_body = "6615";
  String sid = "1";
  String brand ="MX";
  String version = "99";
  // OIReleaseService.PD_insert(sid, pd_body, brand, version);
  // OIReleaseService.BOM_insert(sid, pd_body, brand, version);
 //  OIReleaseService.WS_insert(sid, pd_body, brand, version);
 //  OIReleaseService.FT_insert(sid, pd_body, brand, version);
 //  OIReleaseService.BA_insert(sid, pd_body, brand, version);
 //  String path=request.getRealPath("/");
 //    OIReleaseActionForm[] rs = OIReleaseService.pdf_show(sid,"pdf_internal",path);
 //               request.setAttribute("list1", rs);
 // OIReleaseService.DL_insert(sid, pd_body, brand, version,path);
 //   OIReleaseService.reset_tx(sid, pd_body, brand, version);
%>
<html:html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=big5">
<script type="text/javascript">

        function transfer(){
      location.href = "./FTAdd.jsp";
               //  document.write(location.href);

    }

       function open_file(file_name){
window.open(file_name);


}


              </script><title>專案管理</title>
  <link rel="stylesheet" type="text/css" href='<html:rewrite page="/css/css.css"/>'>
<script src='<html:rewrite page="/js/util.js"/>' language="javascript"></script><script src='<html:rewrite page="/js/areaSelect.js"/>' language="javascript"></script><script type="text/javascript">
</script></head>
<body topmargin="0" leftmargin="0">
<%@include file="../../index-menu.jsp"%>
  <table width="100%" border=0 class="bg1">
    <tr>
      <!--<td valign="top" width="159" class="bg">-->
</td>      <td valign="top">
        <table width="100%" border="0" cellspacing="0" cellpadding="0" class="bg1" align="center">
          <tr>
            <td width="100%" height="490" valign="top">
              <br>
              <table width="95%" border="0" cellspacing="0" cellpadding="0" align="center">
                <tr>
                  <td width="15%" height="25" class="title2">
                    <img src="../../image/arrow.gif" width="5" height="14" hspace="3" alt="">
                    <font size="4">OI</font>
                  </td>
                  <td noWrap height="25" width="85%" class="title4">
                    <font size="2">PDF</font>
                  </td>
                </tr>
                <tr>
                  <td height="20" colspan="2">
                    <hr width="100%" size="1" class="hr">
                  </td>
                </tr>
              <form name="form1" action="<html:rewrite page="/OImaintain/oIRelease.do"/>">
              </table>
              <div id="myDIV1" align="center" style="border:0;">
                <br/>
                <table id="table27" cellspacing=1 cellpadding=0 class=table2>
                  <thead>
                    <logic:present name="list1" >
                      <logic:iterate id="result" name="list1" >
                    <tr>
                          <td align="left" height="20" width="100%" onclick="open_file('<bean:write name="result" property="file_name" ignore="true"/>');">
                            <bean:write name="result" property="title"  ignore="true"/>
                          </td>
                        </tr> </logic:iterate>
                    </logic:present>

                    <!--<logic:present name="list2">
                      <logic:iterate id="result2" name="list2">
                        <tr>
                          <td align="left" height="20" width="100%" onclick="open_file('<bean:write name="result2" property="file_name"  ignore="true"/>');">
                            <bean:write name="result2" property="title"  ignore="true"/>
                          </td>
                        </tr>
                      </logic:iterate>
                    </logic:present>
                    <logic:present name="list3">
                      <logic:iterate id="result3" name="list3">
                        <tr>
                          <td align="left" height="20" width="100%" onclick="open_file('<bean:write name="result3" property="file_name"  ignore="true"/>');">
                            <bean:write name="result3" property="title"  ignore="true"/>
                          </td>
                        </tr>
                      </logic:iterate>
                    </logic:present>
                    <logic:present name="list4">
                      <logic:iterate id="result4" name="list4">
                        <tr>
                          <td align="left" height="20" width="100%" onclick="open_file('<bean:write name="result4" property="file_name"  ignore="true"/>');">
                            <bean:write name="result4" property="title"   ignore="true"/>
                          </td>
                        </tr>
                      </logic:iterate>
                    </logic:present>
                    <logic:present name="list5">
                      <logic:iterate id="result5" name="list5">
                        <tr>
                          <td align="left" height="20" width="100%" onclick="open_file('<bean:write name="result5" property="file_name"  ignore="true"/>');">
                            <bean:write name="result5" property="title"  ignore="true"/>
                          </td>
                        </tr>
                      </logic:iterate>
                    </logic:present>-->
                  </thead>
                </table>
              </div>
</form>              　
            </td>
          </tr>
        </table>
      </td>
    </tr>
  </table>
<%@include file="../../index-down.jsp"%>
</body>
</html:html>
