<!-- 8049FW/fw8049_maintain.jsp -->

<%@page contentType="text/html; charset=Big5"%>
<%@taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@taglib uri="/WEB-INF/c.tld" prefix="c" %>

<html:html>
<head>
<c:set var="actFrm" value="${Fw8049MainActionForm}"/>
<%-- <c:if test="${empty actFrm}"> --%>
<%-- 	<c:set var="actFrm" value="${TgCPFlowActionForm}"/> --%>
<%-- </c:if> --%>
<%-- <c:if test="${empty actFrm}"> --%>
<%-- 	<c:set var="actFrm" value="${TgFTFlowActionForm}"/> --%>
<%-- </c:if> --%>
<%-- <c:if test="${empty actFrm}"> --%>
<%-- 	<c:set var="actFrm" value="${DocumentManageActionForm}"/> --%>
<%-- </c:if> --%>

<meta http-equiv="Content-Type" content="text/html; charset=big5">
<title>8049FW 維護</title>
<link rel="stylesheet" type="text/css" href='<html:rewrite page="/css/css.css"/>'>
<script src='<html:rewrite page="/js/util.js"/>' language="javascript"></script>
<script src='<html:rewrite page="/js/areaSelect.js"/>' language="javascript"></script>
<script type="text/javascript">
function bt_back(){
// 	location.replace("<html:rewrite page="/8049fw/Fw8049MainAction.do"/>?act=getList");
	window.history.back();
}
console.log(${actFrm.sid});
console.log(${actFrm.product_body});
console.log(${actFrm.customer_no});
console.log(${actFrm.version});
console.log(${actFrm.status});
console.log(${actFrm.brand});
console.log(${actFrm.status_display});
</script>
</head>
<body topmargin="0" leftmargin="0">
<%@  include file="../index-menu.jsp"%>
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
            <img src="../image/arrow.gif" width="5" height="14" hspace="3" alt=""><font size="4">8049 FW</font></td>
            <td noWrap height="25" width="85%" class="title4"><font size="4"> OI Maintainence</font></td>
          </tr>
          <tr>
            <td height="20" colspan="2"><hr width="100%" color=#B4761B size="1"></td>
            </tr>
          </table>

		<html:form action ="/8049fw/Fw8049MainAction.do" method="post">
            <input type="hidden" name="sid" id="sid" value="<bean:write name="actFrm" property="sid"/>"/>
            <div id="myDIV1" align="center" style="border:0;height:310px" >
              <!--Data Table Starts From Here-->
              <table id="table27" cellspacing=1 cellpadding=0 class="table2" >
                <thead>
                      <tr class="list1">
                        <td height="20" align="left"><font size="2"><b>Product: <c:out value="${actFrm.product_body}"/> / <c:if test="${actFrm.customer_no ne 'NA'}"><c:out value="${actFrm.customer_no}"/></c:if><c:out value="${actFrm.brand}"/></b></font></td>
                      </tr>
                      <tr class="list1">
                        <td height="20" align="left">
	                      <a href='<html:rewrite page="/8049fw/Fw8049MaintainenceAction.do"/>?act=getList&sid=<bean:write name="actFrm" property="sid"/>&product_body=<bean:write name="actFrm" property="product_body"/>&customer_no=<bean:write name="actFrm" property="customer_no"/>&version=<bean:write name="actFrm" property="version"/>&status=<bean:write name="actFrm" property="status"/>'>
								Step 1: FW Data Maintenance</a><br>	
                          <logic:notEqual name="actFrm" property="isAuthority" value="true">
                            <a href='<html:rewrite page="/8049FW/Document.jsp"/>?sid=<bean:write name="actFrm" property="sid"/>&pd_body=<bean:write name="actFrm" property="product_body"/>&brand=<bean:write name="actFrm" property="brand"/>&version=<bean:write name="actFrm" property="version"/>&status=<bean:write name="actFrm" property="status"/>'>Step 2: 文件管理</a><br>
                          </logic:notEqual>
                          <img src="../../image/arrow.gif" width="5" height="14" hspace="3" alt=""><a href='<html:rewrite page="/8049FW/AurthMaintain.jsp"/>?sid=<bean:write name="actFrm" property="sid"/>&pd_body=<bean:write name="actFrm" property="product_body"/>&brand=<bean:write name="actFrm" property="brand"/>&version=<bean:write name="actFrm" property="version"/>&status=<bean:write name="actFrm" property="status"/>'>權限維護</a>
                          <br>
			              <p><input type="button" name="Cancel" value="回主畫面" class="button1" onclick="bt_back();return true;">
				          <br>
                        </td>
                      </tr>
                </thead>
              </table>
              <!--Data Table Ends Here-->
            </div>
            <br>
		</html:form>
          </td>
        </tr>
      </table>
    </td>
  </tr>
</table>
<%@  include file="../index-down.jsp"%>
</body>
</html:html>
