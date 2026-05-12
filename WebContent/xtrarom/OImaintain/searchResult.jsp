<!-- /xtrarom/OImaintain/searchResult.jsp -->

<%@page contentType="text/html; charset=Big5"%>
<%@taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<%@ page import="java.util.*"%>
<%@ page import="com.mxic.oiplus.xtrarom.oimaintain.*"%>
<%@page import="com.mxic.oiplus.au.*"%>
<html:html>
<%
String[] admin = com.mxic.oiplus.oimaintain.OiMaintainService.QueryAdmin();
User Auth=(User)session.getAttribute("user");
String user=Auth.getUserName();
boolean chkUser;
chkUser=false;
for(int i=0;i<admin.length;i++){
	if(user.equals(admin[i])){
		chkUser=true;
	}
}		
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=big5">
<title>OI維護</title>
<link rel="stylesheet" type="text/css" href='<html:rewrite page="/css/css.css"/>'>
<script src='<html:rewrite page="/js/util.js"/>' language="javascript"></script>
<script src='<html:rewrite page="/js/areaSelect.js"/>' language="javascript"></script>
<script type="text/javascript">
function bt_back(){

	location.replace("<html:rewrite page="/Login/oiMainActionX.do"/>");
}
</script>
</head>
<body topmargin="0" leftmargin="0">
<%@  include file="../../index-menu.jsp"%>
<table width="100%" border=0 class="bg1">
<tr>
<td valign="top">

<table width="100%" border="0" cellspacing="0" cellpadding="0" class="bg1" align="center">
  <tr>
    <td width="100%" height="490" valign="top">
      <br>
      <table width="95%" border="0" cellspacing="0" cellpadding="0" align="center">
        <tr>
          <td width="15%" height="25" class="title2">
            <img src="../../image/arrow.gif" width="5" height="14" hspace="3" alt=""><font size="4">TIM</font></td>
            <td noWrap height="25" width="85%" class="title4"><font size="4">OI Maintainence</font></td>
          </tr>
          <tr>
            <td height="20" colspan="2"><hr width="100%" color=#B4761B size="1"></td>
            </tr>
          </table>

          <form name ="frm2" action="">
            <div id="myDIV1" align="center" style="border:0;" >
              <!--Data Table Starts From Here-->
              <table id="table27" cellspacing=1 cellpadding=0 class=table2 >
                <thead>
                      <tr class="list1">
                        <td height="20" align="left"><font size="2" ><b>Product : <bean:write name="success" property="product_body"/> / Version <bean:write name="success" property="version"/></b></font></td>
                      </tr>
                      <tr class="list1">
                        <td height="20" align="left">
                          <input type="hidden" name="sid" value="<bean:write name="success" property="sid"/>"/>
                          <input type="hidden" name="message" value=""/>
                          <a href='<html:rewrite page="/OImaintain/productRouteAction.do"/>?sid=<bean:write name="success" property="sid"/>&message='>Step 1: Product VS Route Mapping<bean:write name="success" property="v_tf_product_route"/></a><br>
                          <a href='<html:rewrite page="/OImaintain/bomProductRouteActionX.do"/>?entry=true&sid=<bean:write name="success" property="sid"/>'>Step 2: BOM VS Product Route - Normal Production Maintenance<bean:write name="success" property="v_tf_bom_route"/></a><br>
                          <a href='<html:rewrite page="/OImaintain/mainRouteSubActionX.do"/>?sid=<bean:write name="success" property="sid"/>'>Step 3: Main Route vs Substitution Route Mapping<bean:write name="success" property="v_tf_main_sub"/></a><br>
                          <a href='<html:rewrite page="/OImaintain/bomProductReRouteActionX.do"/>?entry=true&sid=<bean:write name="success" property="sid"/>'>Step 4: BOM VS Product Route - Recycle Test Maintenance<bean:write name="success" property="v_tf_bom_reroute"/></a><br>
                          <a href='<html:rewrite page="/OImaintain/mainRouteReActionX.do"/>?sid=<bean:write name="success" property="sid"/>'>Step 5: Main Route vs Rework Route Mapping<bean:write name="success" property="v_tf_main_rework"/></a><br>
                          <a href='<html:rewrite page="/OImaintain/wsTestParameterActionX.do"/>?sid=<bean:write name="success" property="sid"/>'>Step 6: WS Test Parameter Information<bean:write name="success" property="v_tf_test_parameter_ws"/></a><br>
                          <a href='<html:rewrite page="/xtrarom/OImaintain/FTTest.jsp"/>?sid=<bean:write name="success" property="sid"/>&pd_body=<bean:write name="success" property="product_body"/>&brand=<bean:write name="success" property="brand"/>&version=<bean:write name="success" property="version"/>&status=<bean:write name="success" property="status"/>'>Step 7: FT Test Parameter Information<bean:write name="success" property="v_tf_test_parameter_ft"/></a><br>
                          <a href='<html:rewrite page="/OImaintain/pbcTestParameterActionX.do"/>?sid=<bean:write name="success" property="sid"/>'>Step 8: Burn-In/Cycling/AVI Test Parameter Information<bean:write name="success" property="v_tf_test_parameter_pbc"/></a><br>
                          <a href='<html:rewrite page="/OImaintain/TFIMBasic.jsp"/>?sid=<bean:write name="success" property="sid"/>&pd_body=<bean:write name="success" property="product_body"/>&brand=<bean:write name="success" property="brand"/>&version=<bean:write name="success" property="version"/>&status=<bean:write name="success" property="status"/>&product_type=<bean:write name="success" property="product_type"/>'>Step 9: Basic Information Definition<bean:write name="success" property="v_tf_basic_information"/></a><br>
                          <% if (chkUser){ %>
                          <a href='<html:rewrite page="/OImaintain/YieldDefAction.do"/>?proc_type=WS&sid=<bean:write name="success" property="sid"/>'>Step 10: WS Yield Definition(admin)<bean:write name="success" property="v_tf_yield_ws"/></a><br>
                          <a href='<html:rewrite page="/OImaintain/YieldDefAction.do"/>?proc_type=FT&sid=<bean:write name="success" property="sid"/>'>Step 11: FT Yield Definition(admin)<bean:write name="success" property="v_tf_yield_ft"/></a><br>
                          <% } %>
                          <a href='<html:rewrite page="/OImaintain/YieldDefActionNewX.do"/>?proc_type=WS&sid=<bean:write name="success" property="sid"/>'>Step 10: WS Yield Definition<bean:write name="success" property="v_tf_yield_ws"/></a><br>
                          <a href='<html:rewrite page="/OImaintain/YieldDefActionNewX.do"/>?proc_type=FT&sid=<bean:write name="success" property="sid"/>'>Step 11: FT Yield Definition<bean:write name="success" property="v_tf_yield_ft"/></a><br>
                          <!--a href='<html:rewrite page="/OImaintain/YieldDefinition.jsp"/>?sid=<bean:write name="success" property="sid"/>&pd_body=<bean:write name="success" property="product_body"/>&brand=<bean:write name="success" property="brand"/>&version=<bean:write name="success" property="version"/>&status=<bean:write name="success" property="status"/>&product_type=<bean:write name="success" property="product_type"/>'>Step -: Yield Definition (Old)<bean:write name="success" property="v_tf_yield_definition"/></a><br-->
                          <a href='<html:rewrite page="/xtrarom/OImaintain/DocLinkage.jsp"/>?sid=<bean:write name="success" property="sid"/>&pd_body=<bean:write name="success" property="product_body"/>&brand=<bean:write name="success" property="brand"/>&version=<bean:write name="success" property="version"/>&status=<bean:write name="success" property="status"/>'>Step 12: Document Linkage<bean:write name="success" property="v_tf_document_linkage"/></a><br>
                          <a href='<html:rewrite page="/OImaintain/WipControl.jsp"/>?sid=<bean:write name="success" property="sid"/>&pd_body=<bean:write name="success" property="product_body"/>&brand=<bean:write name="success" property="brand"/>&version=<bean:write name="success" property="version"/>&status=<bean:write name="success" property="status"/>&product_type=<bean:write name="success" property="product_type"/>'>Step 13: CP WIP Handling Control<bean:write name="success" property="v_tf_wip_control"/></a><br>

                          <logic:equal name="success" property="authorityUser" value="true">
                            <a href='<html:rewrite page="/xtrarom/OImaintain/Document.jsp"/>?sid=<bean:write name="success" property="sid"/>&pd_body=<bean:write name="success" property="product_body"/>&brand=<bean:write name="success" property="brand"/>&version=<bean:write name="success" property="version"/>&status=<bean:write name="success" property="status"/>'>Step 14 : 文件管理</a><br>
                          </logic:equal>
                          <logic:notEqual name="success" property="authorityUser" value="true">
                            <a href='#' DISABLED>Step 14: 文件管理</a><br>
                          </logic:notEqual>
                          <br>
                          <img src="../../image/arrow.gif" width="5" height="14" hspace="3" alt=""><a href='<html:rewrite page="/xtrarom/OImaintain/AurthMaintain.jsp"/>?sid=<bean:write name="success" property="sid"/>&pd_body=<bean:write name="success" property="product_body"/>&brand=<bean:write name="success" property="brand"/>&version=<bean:write name="success" property="version"/>&status=<bean:write name="success" property="status"/>'>權限維護</a>
			  <p><input type="button" name="Cancel" value="回主畫面" class="button1" onclick="bt_back();return true;">
				<br>

                      </tr>
                </thead>
              </table>
              <!--Data Table Ends Here-->
            </div>
            <br>
            </form>
          </td>
        </tr>
      </table>
    </td>
  </tr>
</table>
<%@  include file="../../index-down.jsp"%>
</body>
</html:html>


