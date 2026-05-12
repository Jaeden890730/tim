<!-- 8049FW/fw8049_search_result.jsp -->

<%@page contentType="text/html; charset=Big5"%>
<%@taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<%@ page import="java.util.*"%>

<html:html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=big5">
<title>8040OI - OI 查詢</title>
<link rel="stylesheet" type="text/css" href='<html:rewrite page="/css/css.css"/>'>
<script src='<html:rewrite page="/js/util.js"/>' language="javascript"></script>
<script src='<html:rewrite page="/js/areaSelect.js"/>' language="javascript"></script>
<script language="JavaScript" type="text/JavaScript">
function productBodyMaintain(sid,productBody,customer_no,version,status){
	document.forms[0].act.value = 'oiMaintainProductMain';
	document.forms[0].sid.value = sid;
	document.forms[0].product_body.value = productBody;
	document.forms[0].customer_no.value = customer_no;
	document.forms[0].version.value = version;
	document.forms[0].status.value = status;
	document.forms[0].submit();
}
</script>
</head>
<body topmargin="0" leftmargin="0">
<%@  include file="../index-menu.jsp"%>
<table width="100%" border="0" class="bg1">
	<tr>
		<td valign="top">
		<table width="100%" border="0" cellspacing="0" cellpadding="0" class="bg1" align="center">
			<tr>
				<td width="100%" height="490" valign="top"><br>
				<table width="95%" border="0" cellspacing="0" cellpadding="0" align="center">
					<tr>
						<td width="100%" height="25" class="title2">
						<img src="../image/arrow.gif" width="5" height="14" hspace="3" alt="">
						<b><font size="4">8040 OI</font>　<font size="2"> OI 查詢</font></b>
						</td>
					</tr>
					<tr>
						<td height="20">
							<hr width="100%" size="1" class="hr">
						</td>
					</tr>
				</table>
			<html:form action="/oi8040/TgInformationAction.do" method="post">
			<input name="act" id="act" type="hidden" value="oiMaintain" />
			<input name="sid" id="sid" type="hidden" />
			<input name="product_body" id="product_body" type="hidden" />
			<input name="customer_no" id="customer_no" type="hidden" />
			<input name="version" id="version" type="hidden" />
			<input name="status" id="status" type="hidden" />

			<div id="myDIV1" align="center" style="border:0;height:310px" >
				<table width="95%" cellspacing="1" cellpadding="0" align="center" class="table2">
					<tr class="title1">
						<td height="20" align="left" width="80">Product Body</td>
						<td height="20" align="left" width="100">Brand</td>
						<td height="20" align="left" width="80">Version</td>
   						<td height="20" align="left" width="80">Status</td>
   						<td height="20" align="left" width="100">Creator</td>
   						<td height="20" align="left" width="130">Last Modified Date</td>
					</tr>
					<logic:present name="list" >
			  		<logic:iterate id="tmp" name="list" >
		  						<tr class="list1">
									<td align="left" height="20" class="box9a" onclick="productBodyMaintain(
										'<bean:write name="tmp" property="sid"/>','<bean:write name="tmp" property="product_body"/>','<bean:write name="tmp" property="customer_no"/>',
										'<bean:write name="tmp" property="version"/>','<bean:write name="tmp" property="status"/>');">
									<bean:write name="tmp" property="product_body"/>
									</td>
									<td align="left" height="20"><bean:write name="tmp" property="brand"/></td>
									<td align="left" height="20"><bean:write name="tmp" property="version"/></td>
									<td align="left" height="20"><bean:write name="tmp" property="status_display"/></td>
									<td align="left" height="20"><bean:write name="tmp" property="creator"/></td>
									<td align="left" height="20"><bean:write name="tmp" property="customerName"/></td>
								</tr>
							</td>
					</logic:iterate>
					</logic:present>
					<logic:notPresent name="list" >
						<table width="95%" cellspacing="1" cellpadding="0" align="center">
	                		<tr>
	                			<td class="list1">
	                		此次搜尋共　'0'　筆資料
	                			</td>
			  				</tr>
		  				</table>
		  		  	</logic:notPresent>　
			</div>
			</html:form>			
					<table width="95%" cellspacing="1" cellpadding="0" align="center">
							<tr>
								<td height="20" align="left" >
									<input type="button" value="<Back" onclick="history.go(-1);return true;" class="button1">
								</td>
							</tr>
					</table>
				</td>
			</tr>
		</table>
		</td>
	</tr>
</table>
<%@  include file="../index-down.jsp"%>

</body>
</html:html>