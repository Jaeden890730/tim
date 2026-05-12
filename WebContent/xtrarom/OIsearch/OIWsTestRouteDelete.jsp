<!-- /xtrarom/OIsearch/OIWsTestRouteDelete.jsp -->

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
						<font size="4"><b>TIM<font size="2">　Delete The Route</font></b>
						</font>
						</td>
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
									<td height="20" align="left" width="125" >
										<input name="delete" type="submit" value="Delete The Route" class="button1">
										<input type="hidden" name="listControl" checked value="bt_delete">
									</td>
						</logic:present>
									<td height="20" align="left" >
										<input type="button" value="Cancel" onclick="history.go(-1);return true;" class="button1">
									</td>
								</tr>
						</table>


						<table width="95%" cellspacing="1" cellpadding="0" align="center" class="table2">
								<tr align="left" class="list1">
									<td height="20" align="left" class="title1">
										Route Name
									</td>
									<td height="20" align="left" class="title1" width="75">
										Step 1
              						</td>
              						<td height="20" align="left" class="title1" width="75">
										Step 2
              						</td>
              						<td height="20" align="left" class="title1" width="75">
										Step 3
              						</td>
              						<td height="20" align="left" class="title1" width="75">
										Step 4
              						</td>
              						<td height="20" align="left" class="title1" width="75">
										Step 5
              						</td>
              						<td height="20" align="left" class="title1" width="75">
										Step 6
              						</td>
              						<td height="20" align="left" class="title1" width="75">
										Step 7
              						</td>
              						<td height="20" align="left" class="title1" width="75">
										Step 8
              						</td>
              						<td height="20" align="left" class="title1" width="75">
										Step 9
              						</td>
              						<td height="20" align="left" class="title1" width="75">
										Step 10
              						</td>
								</tr>
				<logic:present name="list" >
		  		<logic:iterate id="tmp" name="list" >
		  						<tr class="list1">
									<td align="left" height="20"><bean:write name="tmp" property="route_name"/>
										<input name="ra_select" type="hidden" value="<bean:write name="tmp" property="route_name"/>">
									</td>
									<td align="left" height="20" width="75"><bean:write name="tmp" property="step1"/>
									</td>
									<td align="left" height="20" width="75"><bean:write name="tmp" property="step2"/>
									</td>
									<td align="left" height="20" width="75"><bean:write name="tmp" property="step3"/>
									</td>
									<td align="left" height="20" width="75"><bean:write name="tmp" property="step4"/>
									</td>
									<td align="left" height="20" width="75"><bean:write name="tmp" property="step5"/>
									</td>
									<td align="left" height="20" width="75"><bean:write name="tmp" property="step6"/>
									</td>
									<td align="left" height="20" width="75"><bean:write name="tmp" property="step7"/>
									</td>
									<td align="left" height="20" width="75"><bean:write name="tmp" property="step8"/>
									</td>
									<td align="left" height="20" width="75"><bean:write name="tmp" property="step9"/>
									</td>
									<td align="left" height="20" width="75"><bean:write name="tmp" property="step10"/>
									</td>
								</tr>
							</td>
				  </logic:iterate>
				  </logic:present>
						</table>
				<logic:notPresent name="list" >
						<table width="95%" cellspacing="1" cellpadding="0" align="center">
								<tr align="left" class="list1">
									<td height="20" align="left" class="title1">
										<font size="2"><b>您並未點選任何一筆資料</b></font>
									</td>
								</tr>
		  				</table>
		  		</logic:notPresent>　


						<table width="95%" cellspacing="1" cellpadding="0" align="center">
								<tr>
						<logic:present name="list" >
									<td height="20" align="left" width="124" >
										<input name="delete" type="submit" value="Delete The Route" class="button1">
										<input type="hidden" name="listControl" checked value="bt_delete">
									</td>
						</logic:present>
									<td height="20" align="left" >
										<input type="button" value="Cancel" onclick="history.go(-1);return true;" class="button1">
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