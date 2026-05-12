<!-- /xtrarom/OImaintain/PBCAddVendor.jsp -->

<%@page contentType="text/html; charset=Big5"%>
<%@taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<%@page import="com.mxic.oiplus.xtrarom.oimaintain.*"%>
<%@page import="java.util.*"%>
<html:html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=big5">
<script language="JavaScript" type="">
function add_vendor_data(){
   if(checkdata()) {

       document.forms[0].listControl.value = 'add_vendor_data';
  	document.forms[0].submit();

     }


}
function checkdata(){
  if(checkCheckBox('site_data','','30')){
    var id = getRadioValue('site_data');
     document.forms[0].listControl.value = 'add_vendor_data';
     document.forms[0].submit();

  }
}


function can(sid){

window.location="<html:rewrite page="/OImaintain/pbcTestParameterActionX.do"/>?sid=" +sid;

}


</script>
<%
  String pd_body = request.getParameter("product_body");
  String sid = request.getParameter("sid");
  String brand = request.getParameter("brand");
  String version = request.getParameter("version");
  String recno = (String)request.getAttribute("recno");
%>
<title>BCA Test Parameter - Add Vendor</title>
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
                    <font size="4">TIM</font>
                  </td>
                  <td noWrap height="25" width="85%" class="title4">
                    <font size="4">BCA Test Parameter Information</font>
                  </td>
                </tr>
                <tr>
                  <td height="20" colspan="2">
                    <hr width="100%" size="1" class="hr">
                  </td>
                </tr>
              <form name="form1" action="<html:rewrite page="/OImaintain/PBCAddVendorActionX.do"/>?sid=<%=sid%>&pd_body=<%=pd_body %>&brand=<%=brand %>&version=<%=version %>" method="post">
                <input name="listControl" type="hidden">
                <tr>
                  <td height="20" colspan="2">
                    <logic:present name="list1">
                      <input type="button" name="add" value="Add This" class="button1" onclick="add_vendor_data()">
                    </logic:present>
                    <input type="button" name="Cancel" value="Cancel" class="button1"  onclick="can('<%=sid%>');">
                  </td>
                </tr>

              </table>
              <div id="myDIV1" align="center" style="border:0;">
                <table id="table27" cellspacing=1 cellpadding=0 class=table2>
                  <tr class="list1">
                    <td height="20" width="15%" align="left" colspan="<%=recno %>">
                      <font size="2">Product : </font>
                      <font size="2"><%=pd_body %>                        /
version
<%=version %>                      </font>
                    </td>
                  </tr>
                  <tr class="list1">
                    <td height="20" width="15%" align="left" colspan="<%=recno %>">Available Vendor</td>
					<% int idx = 0; %>
                    <logic:present name="list1">
                    <tr class="list1">
                    <tr class="list1">
                      <logic:iterate id="result" name="list1" indexId="i">
                        <td height="20" align="left">
                          <input type="checkbox" name="site_data" value="<bean:write name="result" property="site_str"/>"/>
                          <bean:write name="result" property="site_str"/>
                          <input type="hidden" name="backend_option" value="<bean:write name="result" property="backend_option"/>">
                          <input type="hidden" name="test_type" value="<bean:write name="result" property="test_type"/>">
                          <input type="hidden" name="pin_count" value="<bean:write name="result" property="pin_count"/>">
                          <input type="hidden" name="tester" value="<bean:write name="result" property="tester"/>">
                          <input type="hidden" name="program_name" value="<bean:write name="result" property="program_name"/>">
                          <input type="hidden" name="actual_file" value="<bean:write name="result" property="actual_file"/>">
                          <input type="hidden" name="package_type" value="<bean:write name="result" property="package_type"/>">
                          <input type="hidden" name="i_grade" value="<bean:write name="result" property="i_grade"/>">
                          <input type="hidden" name="c_grade" value="<bean:write name="result" property="c_grade"/>">
                          <input type="hidden" name="body_size" value="<bean:write name="result" property="body_size"/>">
                          <input type="hidden" name="sid" value="<%=sid%>">
                          <input type="hidden" name="product_body" value="<%=pd_body%>">
                          <input type="hidden" name="brand" value="<%=brand%>">
                          <input type="hidden" name="version" value="<%=version%>">
                          <input type="hidden" name="pgm_id" value="<bean:write name="result" property="pgm_id"/>">
                        </td>
                           <% idx++;
                             if (idx == 6) {
                            	 idx = 0;
                          %>
                             </tr>
                             <tr class="list1">
                          <%
                             }
                          %>
                      </logic:iterate>
                    </tr>
                    </tr>
                    </logic:present>
                    <logic:notPresent name="list1">
                      <td  height="20" align="left">此次搜尋共　'0'　筆資料</td>
                    </logic:notPresent>
                  </tr>
                </table>
              </div>
              <table width="95%" border="0" cellspacing="0" cellpadding="0" align="center">
                <tr>
                  <td height="20">
                    <logic:present name="list1">
                      <input type="button" name="add" value="Add This" class="button1" onclick="add_vendor_data()">
                    </logic:present>
                    <input type="button" name="Cancel" value="Cancel" class="button1"  onclick="can('<%=sid%>');">
                  </td>
                </tr>
              </table>
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
