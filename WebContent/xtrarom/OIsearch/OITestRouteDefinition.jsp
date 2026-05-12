<!-- /xtrarom/OIsearch/OITestRouteDefinition.jsp -->

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
function checkRouteName(){

	var txt_routeName = document.forms[0].txt_routename.value;
	if (txt_routeName == null){
		alert("未輸入Route Name");
		return false;
	}else if (trim(txt_routeName) == ""){
		alert("Route Name不可空白");
		return false;
	}else{
		 document.forms[0].submit();
		 return true;
	}
 	//document.forms[0].listControl.value = 'bt_oi_userpasswrd()';
  	//document.forms[0].submit();

}

function redirectAddRouteWS(tmp){
tmp.btControl.value='WS';
tmp.submit();
}

function redirectAddRouteFT(tmp){
tmp.btControl.value='FT';
tmp.submit();
}

function bt_back(){

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
						<font size="4"><b>TIM<font size="2">　Test Route Search</font></b>
						</font>
						</td>
					</tr>
					<tr>
						<td height="20">
							<hr width="100%" size="1" class="hr">
						</td>
					</tr>
				</table>　
				<table width="95%" cellspacing="1" cellpadding="0" align="center">
						<tr class="list1">
							<td height="20" align="left" class="title1" colspan="2">
										<p align="left">
										<font size="2" color="#800000"><b>Search by：</b></font><b>
										Select from the options below to
										restrict your search</b></td>
							</tr>
				</table>　
				<form name="form1" action="<html:rewrite page="/OIsearch/oiTestRouteSearchActionX.do"/>" method="post">
						<table width="95%" cellspacing="1" cellpadding="0" align="center" class="table2">
								<tr align="left" class="list1">
									<td height="20" align="left"  width="170" class="title1">
										<font size="2"><b>Route Name</b></font>
									</td>
									<td height="20" align="left" width="581" >
										<input name="txt_routename" type="text" class="text1">
              						</td>
								</tr>
						</table>　

						<table width="95%" cellspacing="1" cellpadding="0" align="center">
								<tr class="list2">

									<td height="20" align="left" width="98">
										<input name="search" type="button" value="Search Route" class="button1" onclick="checkRouteName();">
										<input type="hidden" name="btControl" value="bt_search">
									</td>
				</form>
				<form name="form2" action="<html:rewrite page="/OIsearch/oiTestRouteSearchActionX.do"/>" method="post">

									<td>
										<input name="search1" type="button" value="Add New WS Route" class="button1" onclick="redirectAddRouteWS(this.form);">
                                                                                <input name="search2" type="button" value="Add New FT Route" class="button1" onclick="redirectAddRouteFT(this.form);">
                                                                                <input name="search2" type="button" value="Back to Home" class="button1" onclick="bt_back();return true;"">
										<input type="hidden" name="btControl">

										<font color="#FF0000" size = "3">
						<logic:present name="message" >
										<bean:write name="message"/>
						</logic:present></font>
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