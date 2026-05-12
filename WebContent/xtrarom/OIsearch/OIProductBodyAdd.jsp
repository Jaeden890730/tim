<!-- /xtrarom/OIsearch/OIProductBodyAdd.jsp -->

<%@page contentType="text/html; charset=Big5"%>
<%@taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<%@ page import="java.util.*"%>

<html:html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=big5">
<title>TIM</title>
<link rel="stylesheet" type="text/css" href='<html:rewrite page="/css/css.css"/>'>
<script src='<html:rewrite page="/js/util.js"/>' language="javascript"></script>
<script src='<html:rewrite page="/js/areaSelect.js"/>' language="javascript"></script>
<script language="JavaScript" type="text/JavaScript">
function add_new(product,brand,version,creator){

 	document.forms[0].listControl.value = 'add_new_version';
 	document.forms[0].txt_productbody.value = product;
  	document.forms[0].brand.value = brand;
  	document.forms[0].version.value = version;
  	document.forms[0].creator.value = creator;
  	document.forms[0].submit();

}


</script>
</head>

<body topmargin="0" leftmargin="0">

<%@  include file="../../index-menu.jsp"%>
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
						<b><font size="4">TIM</font>　</b>Add A New Version</td>
					</tr>
					<tr>
						<td height="20">
							<hr width="100%" size="1" class="hr">
						</td>
					</tr>
				</table>
						<form name="form1" action="<html:rewrite page="/Login/oiNewVersionActionX.do"/>" method="post">
						<input name="sid" type="hidden">
						<input name="listControl" type="hidden">
						<input name="txt_productbody" type="hidden">
						<input name="brand" value="MX" type="hidden">
						<input name="version" type="hidden">
						<input name="creator" type="hidden">
			<div id="myDIV1" align="center" style="boder:0;height:310" >
						<table width="95%" cellspacing="1" cellpadding="0" align="center" class="table2">
								<tr class="title1">
									<td height="20" align="left" width="347">Product Body
									</td>
									<td height="20" align="left" width="109">Version
	              					</td>
	              					<td height="20" align="left" width="218">Creator
	              					</td>
	              				</tr>
				<logic:present name="list" >
		  		<logic:iterate id="tmp" name="list" >
		  						<tr class="list1">
									<td align="left" height="20" width="347">
									<font color="#FF0000"><bean:write name="tmp" property="product_body"/></font>
									</td>
									<td align="left" height="20" width="109"><bean:write name="tmp" property="version"/>
									</td>
									<td align="left" height="20" width="218"><bean:write name="user_authority" property="user_name" scope="session"/>
									</td>
								</tr>
						</table>
			</div>
						</form>
						<table width="95%" cellspacing="1" cellpadding="0" align="center">
								<tr>
									<td height="20" align="left" width="160" >
										<input type="button" value="確認新增一筆" class="button1" onclick="add_new('<bean:write name="tmp" property="product_body"/>','<bean:write name="tmp" property="brand"/>','<bean:write name="tmp" property="version"/>','<bean:write name="user_authority" property="user_name" scope="session"/>')">
									</td>
									<td height="20" align="left" >
										<input type="button" value="Cancel" onclick="history.go(-1);return true;" class="button1">
									</td>
								</tr>
						</table>
				</logic:iterate>
				</logic:present>
				<logic:notPresent name="list" >
						<table width="95%" cellspacing="1" cellpadding="0" align="center">
	                		<tr>
	                			<td class="list1">
	                		無法新增一筆
	                			</td>
			  				</tr>
		  				</table>
		  				<table width="95%" cellspacing="1" cellpadding="0" align="center">
		  					<tr>
		  						<td height="20" align="left" >
									<input type="button" value="Cancel" onclick="history.go(-1);return true;" class="button1">
								</td>
		  					</tr>
		  		</logic:notPresent>　
				</td>
			</tr>
		</table>
		</td>
	</tr>
</table>
<%@  include file="../../index-down.jsp"%>

</body>
</html:html>