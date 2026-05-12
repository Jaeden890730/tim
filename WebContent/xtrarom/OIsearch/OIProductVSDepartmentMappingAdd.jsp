<!-- /xtrarom/OIsearch/OIProductVSDepartmentMappingAdd.jsp -->

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
function bt_add(){

 	document.forms[0].btControl.value = 'bt_add';
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
						<font size="4"><b>TIM<font size="2">　Product vs Department Mapping
						- Add New Product</font></b></font></td>
					</tr>
					<tr>
						<td height="20">
							<hr width="100%" size="1" class="hr">
						</td>
					</tr>
				</table>
				<form name="form1" action="<html:rewrite page="/OIsearch/oiDeleteTheRouteAtionX.do"/>" method="post">
						<table width="95%" cellspacing="1" cellpadding="0" align="center">
								<tr>
						<logic:present name="list" >
									<td height="20" align="left" width="95" >
										<input name="delete" type="button" value="Add Product" onclick="bt_add();" class="button1">
									</td>
						</logic:present>
									<td height="20" align="left" >
										<input type="button" value="Cancel" onclick="history.go(-1);return true;" class="button1">
						<logic:present name="message" >
										<bean:write name="message"/>
						</logic:present>
									</td>
								</tr>
						</table>


						<table width="95%" cellspacing="1" cellpadding="0" align="center" class="table2">
								<tr align="left" class="list1">
									<td height="20" align="left" class="title1">
										Product Name：
									</td>
								</tr>
		  						<tr class="list1">
									<td align="left" height="20">
										<input name="txt_productname" type="text" class="text1">
									</td>
								</tr>
						</table>
						<table width="95%" cellspacing="1" cellpadding="0" align="center">
								<tr>
						<logic:present name="list" >
									<td height="20" align="left" width="95" >
										<input name="add" type="button" value="Add Product" onclick="bt_add();" class="button1">
									</td>
						</logic:present>
									<td height="20" align="left" >
										<input type="button" value="Cancel" onclick="history.go(-1);return true;" class="button1">
						<logic:present name="message" >
										<bean:write name="message"/>
						</logic:present>
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
<%@  include file="../../index-down.jsp"%>

</body>
</html:html>