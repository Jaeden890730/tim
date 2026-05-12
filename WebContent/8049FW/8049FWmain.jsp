<!-- 8049FW/8049FWmain.jsp -->

<%@page language="java" contentType="text/html; charset=Big5"%>
<%@taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="com.mxic.fw8049.action.*"%>
<%@page import="com.mxic.oiplus.au.*"%>
<%@ page import="java.util.List"%>

<html:html>
<%
String delete_data = (String) request.getAttribute("delete_data");
String return_data = (String) request.getAttribute("return_data");
String add_data = (String) request.getAttribute("add_data");
List<Fw8049MainActionForm> prmattr = (List<Fw8049MainActionForm>) request.getAttribute("list");

User Auth=(User)session.getAttribute("user");
String user=Auth.getUserName();
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=big5">
<title>TIM - Home</title>
<link rel="stylesheet" type="text/css"
	href='<html:rewrite page="/css/css.css"/>'>
<script src='<html:rewrite page="/js/util.js"/>' language="javascript"></script>
<script src='<html:rewrite page="/js/areaSelect.js"/>'
	language="javascript"></script>

<script type="text/javascript">
if ("<%=delete_data%>" == "success")
	alert ("OI �R�����\�I");
if ("<%=return_data%>" == "success")
	alert ("OI �w�h�^�B�z���I");
if ("<%=return_data%>" == "failure")
	alert ("�ץ�w�� EPC �B�z�AOI �L�k�h�^�I");
if ("<%=return_data%>" == "abnormal")
	alert ("OI �h�^�o�Ͳ��`�A�лP MIT �s���I");
if ("<%=add_data%>" == "success")
	alert ("OI �s�W���\�I");

function deleteInfo(){
    if (getRadioValue('checksid') == "") {
		alert("�п�ܭn�R������� !");
		return false;
    }
    
	var txt = getRadioValue('checksid');
	var parts = txt.split(",");
	var sid = parts[0];
	var status = parts[3];
	var creator = parts[4];
	var user = "<%= user %>"

	if (user != creator) {
		alert('����R����L�H�إߪ���ƪ���� !');
		return false;
	}

	if (status != '�B�z��') {
		alert('����R�� ' + status + ' ����� !');
		return false;
	}

    if(!window.confirm("�T�w�n�R���ܡH")){
		return false;
    }

	document.forms[0].sid.value = sid;
 	document.forms[0].act.value = 'delete';
  	document.forms[0].submit();
}

// function returnProcess(){
// 	if (getRadioValue('checksid') == "") {
// 		alert("�п�ܭn�h�^�B�z������� !");
// 		return false;
//     }
// 	var txt = getRadioValue('checksid');
// 	var splits = ",";
// 	var sid = txt.substring(0,txt.indexOf(','));
// 	var rest = txt.substring(txt.indexOf(',')+1,txt.length);
// 	var status = rest.substring(0,rest.indexOf(','));
// 	rest = rest.substring(rest.indexOf(',')+1,rest.length);
// 	var user = rest.substring(0,rest.indexOf(','));
// 	var creator = rest.substring(rest.indexOf(',')+1,rest.length);

// 	if (user != creator) {
// 		alert('����h�^��L�H�إߪ���ƪ���� !');
// 		return false;
// 	}

// 	if (status != '�|ñ��') {
// 		alert('����h�^ ' + status + ' ����� !');
// 		return false;
// 	}

//     if(!window.confirm("�T�w�n�h�^�ܡH")){
// 		return false;
//     }

// 	document.forms[0].sid.value = sid;
//  	document.forms[0].act.value = 'returnProcess';
//   	document.forms[0].submit();
// }

function queryInfo(status){
	document.forms[0].queryType.value = status;
 	document.forms[0].act.value = 'getList';
  	document.forms[0].submit();	
}

function oiSearch(){
	var txt = document.getElementById('txt_productbody').value;	
	if (txt == "" || txt == null || txt.length != 4){
		alert("�п�J�|�X Product Body !");
		return false;
	} else {
	 	document.forms[0].act.value = 'oiSearch';
	  	document.forms[0].submit();
	}
}

function fwInsert(){
    var comflag = false;
    var txt = document.forms[0].product_body.value;
	if (txt == "" || txt == null || txt.length != 4){
		alert("�п�J�|�X Product Body !");
		return false;
	}else{
		<%if (prmattr != null) {
	for (Fw8049MainActionForm form : prmattr) {
		String body = form.getProduct_body();
		// �T�O��X�ɹ�޸����S���r������q
		body = body == null ? "" : body.replace("\"", "\\\"").replace("\n", "");%>
		    if (form.txt_productbody.value === "<%=body%>") {
				comflag = true;
			}
<%}
}%>
	document.forms[0].action = "<html:rewrite page="/8049fw/Fw8049MainAction.do"/>";
			document.forms[0].act.value = 'fwInsert';
			document.forms[0].txt_productbody.value = document.getElementById("form").txt_productbody.value;
			document.forms[0].brand.value = document.getElementById("form").ra_select.value;
			document.forms[0].submit();
			// 		var url='<html:rewrite page="/oi8040/common/customerList.jsp?field=group_no&product_body="/>' + txt;
			//alert(url);
			//WITS-20250721 Mark var rtn=window.showModalDialog(url, "", "dialogHeight:420px;dialogWidth:400px;help:no;status:no");
			//WITS-20250721 Add Start
			// 		window.useModalDialog(url, "", "dialogHeight:420px;dialogWidth:400px;help:no;status:no").then(function(rtn){
			// 			//MXIC Original Start
			// 		    if(rtn){
			// 		        var result=rtn.split("===");
			// 		        //alert(result)
			// 		        document.forms[0].group_no.value = result[0];
			// 		        document.forms[0].aeb_flag.value = result[1];
			// 			 	document.forms[0].act.value = 'oiMaintain';
			// 			  	document.forms[0].submit();
			// 		    } else {
			// 		      	return false;
			// 		    }
			//MXIC Original End
			// 		})['catch'](promiseErrHandler);
			//WITS-20250721 Add End
			//var rtn=window.open(url, 'miniwin','scrollbars=1,toolbar=0,location=0,width=800;,height=620');
		}
	}

	function productBodyMaintain(sid, productBody, brand, version, status) {
		document.forms[0].act.value = 'fw8049MaintainProductMain';
		document.forms[0].sid.value = sid;
		document.forms[0].txt_productbody.value = productBody;
		document.forms[0].brand.value = brand;
		document.forms[0].version.value = version;
		document.forms[0].status.value = status;
		document.forms[0].submit();
	}
</script>
</head>

<body topmargin="0" leftmargin="0" onresize="rollDivSize('myDIV1');">

	<%@  include file="../index-menu.jsp"%>
	<table width="100%" border="0" class="bg1">
		<tr>
			<td valign="top"><html:form styleId="form"
					action="/8049fw/Fw8049MainAction.do" method="post">
					<input name="act" id="act" type="hidden">
					<input name="sid" id="sid" type="hidden">
					<input name="brand" id="brand" type="hidden">
					<input name="version" id="version" type="hidden">
					<input name="status" id="status" type="hidden">
					<input name="listControl" type="hidden">
					<input name="txt_productbody" type="hidden">
					<input name="ra_select" type="hidden" value="MX">
					<html:hidden property="queryType" styleId="queryType" />
					<%-- 			<html:hidden property="aeb_flag" styleId="aeb_flag" /> --%>
					<%-- 			<html:hidden property="group_no" styleId="group_no" /> --%>
					<table width="100%" border="0" cellspacing="0" cellpadding="0"
						class="bg1" align="center">
						<tr>
							<td width="100%" height="490" valign="top"><br>
								<table width="95%" border="0" cellspacing="0" cellpadding="0"
									align="center">
									<tr>
										<td width="100%" height="25" class="title2"><img
											src="../image/arrow.gif" width="5" height="14" hspace="3"
											alt=""> <b><font size="4">TIM</font><font size="2">
													Home</font></b></td>
									</tr>
									<tr>
										<td height="20">
											<hr width="100%" size="1" class="hr">
										</td>
									</tr>
								</table>

								<table width="95%" cellspacing="1" cellpadding="0"
									align="center" class="table2">
									<tr class="list1">
										<td height="20" align="left" width="200" class="list1bg">
											<font size="2"><b>OI Information</b></font> <font size="2"
											�@ color='blue'>(8049FW)</font>
										</td>
										<td height="20" align="left" width="40"><input
											name="listall" type="button" onclick="queryInfo(this.value);"
											value="�����C�X" class="button1"></td>
										<td height="20" align="left" width="40"><input
											name="release" type="button" onclick="queryInfo(this.value);"
											value="�w�ͮ�" class="button1"></td>
										<td height="20" align="left" width="40"><input
											name="processing" type="button"
											onclick="queryInfo(this.value);" value="�B�z��" class="button1">
										</td>
										<td height="20" align="left"><input name="apply"
											type="button" onclick="queryInfo(this.value);" value="�|ñ��"
											class="button1"></td>
										<td height="20" align="left" width="200"><input
											name="delete" type="button" onclick="deleteInfo();"
											value="�R��" class="button1"> <input
											name="return_process" type="button"
											onclick="returnProcess();" value="�h�^�B�z��" class="button1">
										</td>
									</tr>
								</table>
								<div id="myDIV1" align="center" valign="top"
									style="border: 0; height: 300px; overflow: scroll">
									<table id="myTable1" width="100%" cellspacing="1"
										cellpadding="0" class="table2">
										<thead>
											<tr class="title1">
												<th height="20" align="left" width="5"></th>
												<td height="20" align="left" width="160">Product Body</td>
												<td height="20" align="left" width="135">Version</td>
												<td height="20" align="left" width="111">Status</td>
												<td height="20" align="left" width="107">Creator</td>
												<td height="20" align="left">Last Modified Date</td>
											</tr>
										</thead>
										<tbody>
											<logic:present name="list">
												<logic:iterate id="tmp" name="list">
													<tr class="list1" onMouseOver="overcolor2(this);"
														onMouseOut="outcolor2(this);">
														<td align="left" height="20"><input type="radio"
															name="checksid"
															value="${tmp.sid},${tmp.product_body},${tmp.version},${tmp.status_display},${tmp.creator},${tmp.showTime}">
														</td>
														<td align="left" height="20" class="box9a" width="171"
															onclick="productBodyMaintain('<bean:write name="tmp" property="sid"/>','<bean:write name="tmp" property="product_body"/>','<bean:write name="tmp" property="brand"/>','<bean:write name="tmp" property="version"/>','<bean:write name="tmp" property="status"/>');">
															<font color="#FF0000"><bean:write name="tmp"
																	property="product_body" /></font>
														</td>
														<td align="left" height="20" width="135"><bean:write
																name="tmp" property="version" /></td>
														<td align="left" height="20" width="111"><bean:write
																name="tmp" property="status_display" /></td>
														<td align="left" height="20" width="107"><bean:write
																name="tmp" property="creator" /></td>
														<td align="left" height="20" width="223"><bean:write
																name="tmp" property="showTime" /></td>
													</tr>
												</logic:iterate>
											</logic:present>
											<logic:notPresent name="list">
												<tr>
													<table width="95%" cellspacing="1" cellpadding="0"
														align="center">
														<tr>
															<td class="list1">�����j�M�@ '0' �����</td>
														</tr>
													</table>
												</tr>
											</logic:notPresent>
										</tbody>
									</table>
									<script type="text/javascript">
										rollDivSize('myDIV1');
										var st1 = new SortableTable(document
												.getElementById("myTable1"));
									</script>
								</div>
								<table width="95%" cellspacing="0" cellpadding="0"
									align="center" class="table2" height="21">
									<tr class="list1" align="left">
										<td nowrap height="21" align="left" class="title1" width="120"><font
											size="2">Product Body�G</font></td>
										<td height="21" align="left" class="title1" width="60"><html:text
												property="product_body" styleId="txt_productbody" size="10" /><br>
										</td>
										<td height="21" align="left" class="title1" width="40"></td>
										<td height="21" align="left" class="title1" width="40">
										<td height="21" align="left" class="title1" width="100">
											<input name="search" type="button" onclick="oiSearch();"
											value="OI �d��" class="button1">
										</td>
										<td height="21" align="left" class="title1" width=100"><input
											name="maintain" type="button" onclick="fwInsert();"
											value="FW �s�W/���@" class="button1"></td>
										<td height="21" align="left" class="title1" width="100"></td>
										<td height="21" align="left" class="title1" width="100"></td>
									</tr>
								</table></td>
						</tr>
					</table>
				</html:form></td>
		</tr>
	</table>
	<%@  include file="../index-down.jsp"%>
</body>
</html:html>
