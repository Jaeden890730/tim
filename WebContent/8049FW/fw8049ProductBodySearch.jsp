<!-- /8049FW/fw8049ProductBodySearch.jsp -->

<%@page contentType="text/html; charset=Big5"%>
<%@taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<%@ page import="java.util.*"%>

<html:html>



<head>
<meta http-equiv="Content-Type" content="text/html; charset=big5">
<title>TIM</title>
<link rel="stylesheet" type="text/css"
	href='<html:rewrite page="/css/css.css"/>'>
<script src='<html:rewrite page="/js/util.js"/>' language="javascript"></script>
<script src='<html:rewrite page="/js/areaSelect.js"/>'
	language="javascript"></script>
<script language="JavaScript" type="text/JavaScript">
	function table_search(sid, product, version, status) {

		document.forms[0].act.value = 'table_search';
		document.forms[0].sid.value = sid;
		document.forms[0].txt_productbody.value = product;
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
				<table width="100%" border="0" cellspacing="0" cellpadding="0"
					class="bg1" align="center">
					<tr>
						<td width="100%" height="490" valign="top"><br>
							<table width="95%" border="0" cellspacing="0" cellpadding="0"
								align="center">
								<tr>
									<td width="100%" height="25" class="title2"><img
										src="../image/arrow.gif" width="5" height="14" hspace="3"
										alt=""> <b><font size="4">TIM</font> <font size="2">OI
												查詢</font></b></td>
								</tr>
								<tr>
									<td height="20">
										<hr width="100%" size="1" class="hr">
									</td>
								</tr>
							</table>
							<form name="form1"
								action="<html:rewrite page="/8049fw/Fw8049MainAction.do"/>"
								method="post">
								<input name="sid" type="hidden"> 
								<input name="act" id="act" type="hidden">
								<input name="txt_productbody" type="hidden"> 
								<input name="ra_select" type="hidden">
								<input name="version" type="hidden"> 
								<input name="status" type="hidden">
								<div id="myDIV1" align="center" valign="top"
									style="boder: 0; height: 500; overflow: scroll">
									<table width="95%" cellspacing="1" cellpadding="0"
										align="center" class="table2">
										<tr class="title1">
											<td height="20" align="left" width="171">Product Body</td>
											<td height="20" align="left" width="135">Version</td>
											<td height="20" align="left" width="111">Status</td>
											<td height="20" align="left" width="107">Creator</td>
											<td height="20" align="left">Last Modified Date</td>
											<td height="20" align="left">Route Chart Change</td>
										</tr>
										<logic:present name="list">
											<logic:iterate id="tmp" name="list">
												<tr class="list1">
													<td align="left" height="20" width="171" class="box9a"
														onclick="table_search('<bean:write name="tmp" property="sid"/>','<bean:write name="tmp" property="product_body"/>','<bean:write name="tmp" property="version"/>','<bean:write name="tmp" property="status"/>')">
														<bean:write name="tmp" property="product_body" />
													</td>
													<td align="left" height="20" width="135"><bean:write
															name="tmp" property="version" /></td>
													<td align="left" height="20" width="111"><bean:write
															name="tmp" property="status_display" /></td>
													<td align="left" height="20" width="107"><bean:write
															name="tmp" property="creator" /></td>
													<td align="left" height="20" width="223"><bean:write
															name="tmp" property="showTime" /></td>
													<td align="left" height="20" width="223">
														<%-- 													<bean:write --%> <%-- 															name="tmp" property="route_change" /></td> --%>
												</tr>
												</td>
											</logic:iterate>
										</logic:present>
										<logic:notPresent name="list">
											<table width="95%" cellspacing="1" cellpadding="0"
												align="center">
												<tr>
													<td class="list1">此次搜尋共 '0' 筆資料</td>
												</tr>
											</table>
										</logic:notPresent>
										</div>
										</form>
										<table width="95%" cellspacing="1" cellpadding="0"
											align="center">
											<tr>
												<td height="20" align="left"><input type="button"
													value="<Back" onclick="history.go(-1);return true;"
													class="button1"></td>
											</tr>
										</table>

										</td>
										</tr>
									</table></td>
					</tr>
				</table> <%@  include file="../index-down.jsp"%>
</body>
</html:html>