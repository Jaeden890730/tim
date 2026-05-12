<!-- 8049FW/fw8049ProductBodyAdd.jsp -->

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

 	document.forms[0].act.value = 'add_new_version';
 	document.forms[0].product_body.value = product;
  	document.forms[0].brand.value = brand;
  	document.forms[0].version.value = version;
  	document.forms[0].creator.value = creator;
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
						<b><font size="4">TIM</font>　</b>Add A New Version</td>
					</tr>
					<tr>
						<td height="20">
							<hr width="100%" size="1" class="hr">
						</td>
					</tr>
				</table>
					<html:form styleId="form" action ="/8049fw/Fw8049MainAction.do" method="post">
						<input name="act" id="act" type="hidden">
						<input name="sid" type="hidden">
						<input name="product_body" type="hidden">
						<input name="brand" type="hidden" value="MX">
						<input name="version" type="hidden">
						<input name="creator" type="hidden">
			<div id="myDIV1" align="center" style="boder:0;height:310" >
						<table width="95%" cellspacing="1" cellpadding="0" align="center" class="table2">
								<tr class="title1">
									<td height="20" align="left" width=33%;>Product Body
									</td>
									<td height="20" align="left" width=33%;>Version
	              					</td>
	              					<td height="20" align="left" width=33%;>Creator
	              					</td>
	              				</tr>
				<logic:present name="list" >
		  		<logic:iterate id="tmp" name="list" >
		  						<tr class="list1">
									<td align="left" height="20" >
									<font color="#FF0000"><bean:write name="tmp" property="product_body"/></font>
									</td>
									<td align="left" height="20" ><bean:write name="tmp" property="version"/>
									</td>
									<td align="left" height="20" ><bean:write name="user_authority" property="user_name" scope="session"/>
									</td>
								</tr>
						</table>
			</div>
					
						<table width="95%" cellspacing="1" cellpadding="0" align="center">
								<tr>
									<td height="20" align="left" width="160" >
										<input type="button" value="確認新增一筆" class="button1" onclick="add_new('<bean:write name="tmp" property="product_body"/>','MX','<bean:write name="tmp" property="version"/>','<bean:write name="user_authority" property="user_name" scope="session"/>')">
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
		</html:form>
<%@  include file="../index-down.jsp"%>

</body>
</html:html>