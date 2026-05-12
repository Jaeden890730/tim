<!-- /xtrarom/OIsearch/OIProductBodySearchResult.jsp -->

<%@page contentType="text/html; charset=Big5"%>
<%@taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<%@ page import="java.util.*"%>
<%@ page import="com.mxic.oiplus.resource.TDSResource"%>
<%
//String path = TDSResource.getProperties("TIMPdf").getValue("pdf_dl.dir");
%>
<html:html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=big5">
<title>TIM</title>
<link rel="stylesheet" type="text/css" href='<html:rewrite page="/css/css.css"/>'>
<script src='<html:rewrite page="/js/util.js"/>' language="javascript"></script>
<script src='<html:rewrite page="/js/areaSelect.js"/>' language="javascript"></script>
<script language="JavaScript" type="text/JavaScript">
function bt_back(){
	location.replace("<html:rewrite page="/Login/oiMainActionX.do"/>")
}

function on_step1(){
        //alert("document.forms[0].action="+document.forms[0].action.value);
 	document.forms[0].btControl.value = '1';
 	document.forms[0].submit();
}
function on_step2(){
 	document.forms[0].btControl.value = '2';
 	document.forms[0].submit();
}
function on_step3(){
 	document.forms[0].btControl.value = '3';
 	document.forms[0].submit();
}
function on_step4(){
 	document.forms[0].btControl.value = '4';
 	document.forms[0].submit();
}
function on_step5(){
 	document.forms[0].btControl.value = '5';
 	document.forms[0].submit();
}
function on_step6(){
 	document.forms[0].btControl.value = '6';
 	document.forms[0].submit();
}
function on_step7(){
 	document.forms[0].btControl.value = '7';
 	document.forms[0].submit();
}
function on_step8(){
 	document.forms[0].btControl.value = '8';
 	document.forms[0].submit();
}
function on_step9(){
    document.forms[0].btControl.value = '9';
	document.forms[0].submit();
}
function on_step10(){
	document.forms[0].btControl.value = '10';
	document.forms[0].submit();
}
function on_step11(){
	document.forms[0].btControl.value = '11';
	document.forms[0].submit();
}
function on_step12(){ // WS Yield Defintion
	document.forms[0].btControl.value = '12';
	document.forms[0].submit();
}

function on_step13(){ // FT Yield Defintion
	document.forms[0].btControl.value = '13';
	document.forms[0].submit();
}

function on_pdf(tmp1,tmp2,tmp3){
 	window.location="<html:rewrite page="/OImaintain/goPDFPageActionX.do"/>?pro_b="+tmp1+"&brand="+tmp2+"&version="+tmp3;
}
function bt_copy(){
 	document.forms[0].btControl.value = 'bt_copy';
  	document.forms[0].submit();
}

function on_step14(){
 	document.forms[0].btControl.value = '14';
 	document.forms[0].submit();
}

function bt_cancle(){
	location.replace("<html:rewrite page="/xtrarom/OIsearch/OITestRouteDefinition.jsp"/>")
}

function downloadFile(fileName){
    commonDownloadFile('${pageContext.request.contextPath}', 'TIMPdf', 'pdf_dl.dir', fileName);
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
						<b>
						<font size="4">TIM</font><font size="2">　OI </font></b>
						<font size="2"><b>Query</b></font>
						</font>
						</td>
					</tr>
					<tr>
						<td height="20">
							<hr width="100%" size="1" class="hr">
						</td>
					</tr>
				</table>
				<form name="form1" action="../OIsearch/oiQueryStepActionX.do" method="post">
				<table width="95%" cellspacing="1" cellpadding="0" align="center" class="table2">
				        <input type="hidden" name="btControl">
				        <input type="hidden" name="product_body" value="<bean:write name="result" property="product_body"/>">
				        <input type="hidden" name="brand" value="<bean:write name="result" property="brand"/>">
				        <input type="hidden" name="version" value="<bean:write name="result" property="version"/>">
			         	<tr class="list1">
			          		<td height="20" align="left"><font size="2" ><b>Product: <bean:write name="result" property="product_body"/> / Version <bean:write name="result" property="version"/></b></font></td>
			         	</tr>
			    		<tr class="list1">
			    			<td height="20" class="box9a" align="left">
			    				<br>
			    				<li onclick="on_step1();">Product VS Test Route Mapping<br></li>
			    				<li onclick="on_step2();">TIM BOM VS Product Route - Normal Production Maintenance <br></li>
                                                        <li onclick="on_step8();">Main Route vs Substitution Route Mapping <br></li>
                                                        <li onclick="on_step3();">TIM BOM VS Product Route - Recycle Test Maintenance<br></li>
                                                        <li onclick="on_step9();">Main Route vs Rework Route Mapping  <br></li>
			    				<li onclick="on_step4();">WS Test Parameter Information<br></li>
			    				<li onclick="on_step5();">FT Test Parameter Information<br></li>
			    				<li onclick="on_step6();">Burn-in/Cycling/AVI Test Parameter Information<br></li>
			    				<li onclick="on_step11();">Basic Information<br></li>
    				            <li onclick="on_step12();">WS Yield Definition<br></li>
     				            <li onclick="on_step13();">FT Yield Definition<br></li>
			    				<li onclick="on_step7();">Yield Definition (Old)<br></li>
			    				<li onclick="on_step10();">CP WIP Handling Control<br></li>

                                                        <logic:equal name="result" property="brand" value="KH">
			    				    <li><a href="#" onclick="downloadFile('8049k-<bean:write name="result" property="product_body"/>v<bean:write name="result" property="version"/>.pdf')">EPC 生效文件</a><br></li>
			    				</logic:equal>
			    				<logic:notEqual name="result" property="brand" value="KH">
			    				    <li><a href="#" onclick="downloadFile('8049-<bean:write name="result" property="product_body"/>v<bean:write name="result" property="version"/>.pdf')">EPC 生效文件</a><br></li>
			    				</logic:notEqual>
			    				<logic:equal name="result" property="brand" value="KH">
			    				    <li><a href="#" onclick="downloadFile('8049k-<bean:write name="result" property="product_body"/>cv<bean:write name="result" property="version"/>.pdf')">EPC 生效差異文件</a><br></li>
			    				</logic:equal>
			    				<logic:notEqual name="result" property="brand" value="KH">
			    				    <li><a href="#" onclick="downloadFile('8049-<bean:write name="result" property="product_body"/>cv<bean:write name="result" property="version"/>.pdf')">EPC 生效差異文件</a><br></li>
			    				</logic:notEqual>
			    				<logic:equal name="result" property="authorityUser" value="true">
									<li onclick="on_pdf('<bean:write name="result" property="product_body"/>',　'<bean:write name="result" property="brand"/>','<bean:write name="result" property="version"/>');">MAKE PDF<br></li>
								</logic:equal>
								<logic:equal name="user_authority" property="user_name" value="prjmgr">
								    <li onclick="on_pdf('<bean:write name="result" property="product_body"/>',　'<bean:write name="result" property="brand"/>','<bean:write name="result" property="version"/>');">MAKE PDF(prjmgr)<br></li>
								</logic:equal>
								<logic:equal name="user_authority" property="user_name" value="livantsai">
								    <li onclick="on_pdf('<bean:write name="result" property="product_body"/>',　'<bean:write name="result" property="brand"/>','<bean:write name="result" property="version"/>');">MAKE PDF(prjmgr)<br></li>
								</logic:equal>
								<li onclick="on_step14();">Product group key basic data<br></li>
			    				<br>
			    			</td>
			    		</tr>
					<tr class="list1"><td height="20" class="box9a" align="left">
			    				<input type="button" name="back" value="回主畫面" class="button1" onclick="bt_back();return true;">
					</td></tr>
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