<!-- /xtrarom/OIsearch/OIProductVSDepartmentMapping.jsp -->

<%@page contentType="text/html; charset=Big5"%>
<%@taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<%@ page import="java.util.*"%>
<%@ page import="com.mxic.oiplus.au.Authority"%>
<% Authority temp = (Authority) session.getAttribute("user_authority"); %>


<html:html>



<head>
<meta http-equiv="Content-Type" content="text/html; charset=big5">
<title>TIM</title>
<link rel="stylesheet" type="text/css" href='<html:rewrite page="/css/css.css"/>'>
<script src='<html:rewrite page="/js/util.js"/>' language="javascript"></script>
<script src='<html:rewrite page="/js/areaSelect.js"/>' language="javascript"></script>
<script language="JavaScript" type="text/JavaScript">
function bt_delete(){

 	document.forms[0].btControl.value = 'bt_delete';
  	document.forms[0].submit();

}
function bt_add(){

 	document.forms[0].btControl.value = 'bt_add';
  	document.forms[0].submit();

}
function bt_cancel(){

	location.replace("<html:rewrite page="/Login/oiMainActionX.do"/>")
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
						<font size="4"><b>TIM</font><font size="2">　Product vs Department Mapping</font></b>
						</font>
						</td>
					</tr>
					<tr>
						<td height="20">
							<hr width="100%" size="1" class="hr">
						</td>
					</tr>
				</table>
				　

				<form name="form1" action="<html:rewrite page="/OIsearch/oiProductVSDepartmentMappingActionX.do"/>" method="post">
				<input type="hidden" name="btControl">
						<table width="95%" cellspacing="0" cellpadding="0" align="center">
								<tr>
									<td height="20" align="left" width="107">
										<input name="save" type="button" value="Delete Product" onclick="bt_delete();" class="button1">
									</td>
									<td height="20" align="left" width="131">
										<input name="copy" type="button" value="Add New Product"  onclick="bt_add();" class="button1">
									</td>
									<td height="20" align="left" width="674">
										<input type="button" value="Cancel" onclick="bt_cancel();" class="button1">
						<logic:present name="message" >
										<b><font color="#FF0000"><bean:write name = "message"></bean:write></font></b>
						</logic:present>
									</td>

								</tr>
						</table>
						<table width="95%" cellspacing="1" cellpadding="0" align="center" class="table2">
								<tr align="left" class="list1">
									<td height="20" align="left"  width="102" class="title1">
										<font size="2"><b>Department</b></font>
									</td>
									<td height="20" align="left" width="845" >

              							<%
              							if (temp.getDept_id() != null){
              								out.print(temp.getDept_id());
              							}
              							%>


              						</td>
								</tr>
								<tr align="left" class="list1">
									<td height="20" align="left"  width="102" class="title1">
										<font size="2"><b>Product List</b></font>
									</td>
									<td height="20" align="left" width="845" >

              						</td>
								</tr>
				<logic:present name="list" >
		  		<logic:iterate id="tmp" name="list" >
		  						<tr align="left" class="list1">
									<td height="20" align="center"  width="102">
										<input name="ra_select" type="radio" value="<bean:write name="tmp" property="product_body"/>" checked>
									</td>
									<td height="20" align="left" width="845" >
              							<bean:write name="tmp" property="product_body"/>
              						</td>
								</tr>
				</logic:iterate>
				</logic:present>
				<logic:notPresent name="list" >
								<tr align="left" class="list1">
									<td height="20" align="left" class="title1">
										<font size="2"><b>目部門尚未有資料</b></font>
									</td>
								</tr>
			  	</logic:notPresent>　
						</table>　
						<table width="95%" cellspacing="0" cellpadding="0" align="center">
								<tr>
									<td height="20" align="left" width="107">
										<input name="save" type="button" value="Delete Product" onclick="bt_delete();" class="button1">
									</td>
									<td height="20" align="left" width="131">
										<input name="copy" type="button" value="Add New Product"  onclick="bt_add();" class="button1">
									</td>
									<td height="20" align="left" width="674">
										<input type="button" value="< Back" onclick="history.go(-1);return true;" class="button1">
						<logic:present name="message" >
										<b><font color="#FF0000"><bean:write name = "message"></bean:write></font></b>
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