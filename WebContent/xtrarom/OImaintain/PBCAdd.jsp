<!-- /xtrarom/OImaintain/PBCAdd.jsp -->

<%@page contentType="text/html; charset=Big5"%>
<%@taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<%@page import="java.util.*"%>
<%@page import="com.mxic.oiplus.xtrarom.oimaintain.*"%>
<html:html>
<%
	String pd_body = request.getParameter("pd_body");
	String sid=request.getParameter("sid");
	String brand = request.getParameter("brand");
	String version = request.getParameter("version");

	String[] testerTypes = PBCService.getTesterType();
	StringBuffer testerTypeHTML = new StringBuffer();
	if (testerTypeHTML != null) {
		for (int i=0; i<testerTypes.length; i++) {
			if (testerTypes[i].indexOf("'") != -1)
				continue;
			testerTypeHTML.append("<option value=\"" + testerTypes[i] + "\">" + testerTypes[i] + "</option>");
		}
		testerTypeHTML.append("<option value=\"NA\">NA</option>");
	}
	testerTypes = null;

	String[] testMode = com.mxic.oiplus.oimaintain.PBCService.getTestMode();
	StringBuffer testModeHTML = new StringBuffer();
	if (testModeHTML != null) {
		for (int i=0; i<testMode.length; i++) {
			testModeHTML.append("<option value=\"" + testMode[i] + "\">" + testMode[i] + "</option>");
		}
	}
	testMode = null;

	String[] backendOptions = PBCService.getProductData(pd_body, brand, 0);
	StringBuffer backendOptionHTML = new StringBuffer();
	if (backendOptionHTML != null) {
		for (int i=0; i<backendOptions.length; i++) {
			backendOptionHTML.append("<option value=\"" + backendOptions[i] + "\">" + backendOptions[i] + "</option>");
		}
		backendOptionHTML.append("<option value=\"*\">*</option>");
	}
	backendOptions = null;

	String[] pinCounts = PBCService.getProductData(pd_body, brand, 1);
	StringBuffer pinCountHTML = new StringBuffer();
	if (pinCountHTML != null) {
		for (int i=0; i<pinCounts.length; i++) {
			pinCountHTML.append("<option value=\"" + pinCounts[i] + "\">" + pinCounts[i] + "</option>");
		}
		pinCountHTML.append("<option value=\"0\">0</option>");
	}
	pinCounts = null;

	String[] pkgs = PBCService.getProductData(pd_body, brand, 2);
	StringBuffer pkgHTML = new StringBuffer();
	if (pkgHTML != null) {
		for (int i=0; i<pkgs.length; i++) {
			pkgHTML.append("<option value=\"" + pkgs[i] + "\">" + pkgs[i] + "</option>");
		}
		pkgHTML.append("<option value=\"NA\">NA</option>");
	}
	pkgs = null;

	String[] bodySizes = PBCService.getProductData(pd_body, brand, 3);
	StringBuffer bodySizeHTML = new StringBuffer();
	if (bodySizeHTML != null) {
		for (int i=0; i<bodySizes.length; i++) {
			bodySizeHTML.append("<option value=\"" + bodySizes[i] + "\">" + bodySizes[i] + "</option>");
		}
		bodySizeHTML.append("<option value=\"NA\">NA</option>");
	}
	bodySizes = null;

	String[] sites = PBCService.getVendor();
	StringBuffer siteHTML = new StringBuffer();
	if (siteHTML != null) {
		for (int i=0; i<sites.length; i++) {
			siteHTML.append("<option value=\"" + sites[i] + "\">" + sites[i] + "</option>");
		}
	}
	sites = null;

%>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=big5">
<script language="JavaScript" type="text/javascript">
var index=0;
function insertRow(){
        var objRow = table1.insertRow(table1.rows.length);
        var objCell = objRow.insertCell(0);
           objCell.innerHTML += '<td class="list1"><input type="text" size="8" name="pgm_id" value="0" onchange="changeVal(\'PGM\',this.parentNode.parentNode.rowIndex);"/></td>';
        var objCell = objRow.insertCell(1);
           objCell.innerHTML += '<td class="list1"><select name="backend_option" onchange="changeVal(\'OPT\',this.parentNode.parentNode.rowIndex);"/><%= backendOptionHTML %></select></td>';
        var objCell = objRow.insertCell(2);
           objCell.innerHTML += '<td class="list1"><select name="test_type" onchange="changeMode(this,this.parentNode.parentNode.rowIndex);"/><%= testModeHTML %></select></td>';
        var objCell = objRow.insertCell(3);
           objCell.innerHTML += '<td class="list1"><select name="pin_count" onchange="changeVal(\'PIN\',this.parentNode.parentNode.rowIndex);"/><%= pinCountHTML %></select></td>';
        var objCell = objRow.insertCell(4);
           objCell.innerHTML += '<td class="list1"><select name="package_type" onchange="changeVal(\'PKG\',this.parentNode.parentNode.rowIndex);"/><%= pkgHTML %></select></td>';
        var objCell = objRow.insertCell(5);
           objCell.innerHTML += '<td class="list1"><select name="body_size" onchange="changeVal(\'BODY\',this.parentNode.parentNode.rowIndex);"/><%= bodySizeHTML %></select></td>';
        var objCell = objRow.insertCell(6);
           objCell.innerHTML += '<td class="list1"><select name="tester" onchange="changeVal(\'TESTER\',this.parentNode.parentNode.rowIndex);"/><%= testerTypeHTML %></select></td>';
        var objCell = objRow.insertCell(7);
           objCell.innerHTML += '<td class="list1"><select name="site"/><%= siteHTML %></select></td>';
        var objCell = objRow.insertCell(8);
           objCell.innerHTML += '<td class="list1"><input type="text" size="20" name="program_name" class="upperText" onkeyup="onlyKeyUpperCase(this);"/></td>';
        var objCell = objRow.insertCell(9);
           objCell.innerHTML += '<td class="list1"><input type="text" size="20" name="actual_file" value="NA" class="upperText" onkeyup="onlyKeyUpperCase(this);"/></td>';
	    var objCell = objRow.insertCell(10);
           objCell.innerHTML += '<td class="list1"><input type="button" name="row' + index + '" value="delete" onclick="delRow(this.parentNode.parentNode.rowIndex)"></td>';
        index++;
}

function delRow(rowIndex) {
  table1.deleteRow(rowIndex);
  return;
}

function assignAction() {
	fm = document.getElementById("form1");
        //alert("action="+document.forms[0].action);
	if (!checkData(fm))
	    return false;
	fm.submit();
        //window.location="<html:rewrite page="/OImaintain/aX.do"/>";
}

function can(sid){
	window.location="<html:rewrite page="/OImaintain/pbcTestParameterActionX.do"/>?sid=" +sid;
}

function isEmpty(data){
	return ((trim(data) == null) || (trim(data).length == 0));
}

function checkData(fm) {
	var chkObj =fm.program_name;
	if (chkObj == null)
		return true;
	if (chkObj.length == null) {// only one record
		if (isEmpty(chkObj.value)) {
			window.alert("請填寫 Test Program");
			return false;
		}
	} else {
		for (i=0; i<chkObj.length; i++) {
			if (isEmpty(chkObj[i].value)) {
				window.alert("第 "+(i+1)+" 行請填寫 Test Program");
				return false;
			}
		}
	}
	var chkObj1 =fm.actual_file;
	if (chkObj1 == null)
		return true;
	if (chkObj1.length == null) {// only one record
		if (isEmpty(chkObj1.value)) {
			window.alert("請填寫 Actual Program Name");
			return false;
		}
	} else {
		for (i=0; i<chkObj1.length; i++) {
			if (isEmpty(chkObj1[i].value)) {
				window.alert("第 "+(i+1)+" 行請填寫 Actual Program Name");
				return false;
			}
		}
	}
	return true;
}

function isAVI(rowidx){
   var mode = "";
   var fm = document.getElementById("form1");
   if (fm.program_name.length == null)
       mode = fm.test_type.value;
   else
       mode = fm.test_type[rowidx-1].value;

   if (mode == "AVI")
	   return true;
   else
       return false;
}

function changeVal(valtype,rowidx){
	if (isAVI(rowidx)) {
        var fm = document.getElementById("form1");
		if (fm.program_name.length == null) {
			if (valtype == "OPT")
				fm.backend_option.value = "*";
			else if (valtype == "PGM")
				fm.pgm_id.value = "0";
			else if (valtype == "PIN")
				fm.pin_count.value = "0";
			else if (valtype == "PKG")
				fm.package_type.value = "NA";
			else if (valtype == "BODY")
				fm.body_size.value = "NA";
			else if (valtype == "TESTER")
				fm.tester.value = "NA";
		}
		 else {
			if (valtype == "OPT")
				fm.backend_option[rowidx-1].value = "*";
			else if (valtype == "PGM")
				fm.pgm_id[rowidx-1].value = "0";
			else if (valtype == "PIN")
				fm.pin_count[rowidx-1].value = "0";
			else if (valtype == "PKG")
				fm.package_type[rowidx-1].value = "NA";
			else if (valtype == "BODY")
				fm.body_size[rowidx-1].value = "NA";
			else if (valtype == "TESTER")
				fm.tester[rowidx-1].value = "NA";
		 }
	}
    return true;
}

function changeMode(s,rowidx){
	// selectedIndex 指的是這個 Select 選單選擇的值
	var fm = document.getElementById("form1");

    if (fm.program_name.length == null) { // only one record
	    if (fm.test_type.value == "AVI") {
			fm.pgm_id.value = "0";
			fm.backend_option.value = "*";
			fm.pin_count.value = "0";
			fm.package_type.value = "NA";
			fm.body_size.value = "NA";
			fm.tester.value = "NA";
			fm.program_name.value = "NA";
			fm.actual_file.value = "NA";
	    } else {
			fm.backend_option.selectedIndex = 0;
			fm.pin_count.selectedIndex = 0;
			fm.package_type.selectedIndex = 0;
			fm.body_size.selectedIndex = 0;
			fm.tester.selectedIndex = 0;
			fm.program_name.value = "";
	    }
    } else { // more than one record
	    if (s.options[s.selectedIndex].value == "AVI") {
			fm.pgm_id[rowidx-1].value = "0";
			fm.backend_option[rowidx-1].value = "*";
			fm.pin_count[rowidx-1].value = "0";
			fm.package_type[rowidx-1].value = "NA";
			fm.body_size[rowidx-1].value = "NA";
			fm.tester[rowidx-1].value = "NA";
			fm.program_name[rowidx-1].value = "NA";
			fm.actual_file[rowidx-1].value = "NA";
		} else {
			fm.backend_option[rowidx-1].selectedIndex = 0;
			fm.pin_count[rowidx-1].selectedIndex = 0;
			fm.package_type[rowidx-1].selectedIndex = 0;
			fm.body_size[rowidx-1].selectedIndex = 0;
			fm.tester[rowidx-1].selectedIndex = 0;
			fm.program_name[rowidx-1].value = "";
	    }
    }
	return true;
}

</script>
<%

%>
<title>專案管理</title>
  <link rel="stylesheet" type="text/css" href='<html:rewrite page="/css/css.css"/>'>
<script src='<html:rewrite page="/js/util.js"/>' language="javascript"></script><script src='<html:rewrite page="/js/areaSelect.js"/>' language="javascript"></script><script type="text/javascript">
</script></head>
<body topmargin="0" leftmargin="0">
<%@include file="../../index-menu.jsp"%>
  <table width="100%" border=0 class="bg1">
    <tr>
      <td valign="top">
        <table width="100%" border="0" cellspacing="0" cellpadding="0" class="bg1" align="center">
          <tr>
            <td width="100%" height="490" valign="top">
              <br>
              <table width="95%" border="0" cellspacing="0" cellpadding="0" align="center">
                <tr>
                  <td width="15%" height="25" class="title2">
                    <img src="../../image/arrow.gif" width="5" height="14" hspace="3" alt="">
                    <font size="4">TIM</font>
                  </td>
                  <td noWrap height="25" width="85%" class="title4">
                    <font size="4">Add BCA Test Parameter Information</font>
                  </td>
                </tr>
                <tr>
                  <td height="20" colspan="2">
                    <hr width="100%" size="1" class="hr">
                  </td>
                </tr>
                <tr>
                  <td height="20" colspan="2">
                    <input type="button" name="Insert Row" value="Insert Row" class="button1"  onclick="insertRow();">
                    <input type="button" name="Save" value="save" class="button1"  onclick="assignAction();">
                    <input type="button" name="Cancel" value="Cancel" class="button1"   onclick="can('<%=sid%>');">
                  </td>
                </tr>

              </table>
              <form id="form1" name="form1" action="<html:rewrite page="/OImaintain/addPBCTestParameterActionX.do"/>" method="post">
                <div id="myDIV1" align="center" style="border:0;">
                  <table id="table1" cellspacing=1 cellpadding=0 class=table2>
                    <thead>
                      <tr class="title1">
                        <td height="20" >PGM ID</td>
                        <td height="20" >BE Opt.</td>
                        <td height="20" >Test Mode</td>
                        <td height="20" >Pin Count</td>
                        <td height="20" >Pkg. Type</td>
                        <td height="20" >Device Size</td>
                        <td height="20" >Tester</td>
                        <td height="20" >Site</td>
                        <td height="20" >Test Program Name</td>
                        <td height="20" >Actual Program Name</td>
                        <td></td>
                      </tr>
                    </thead>
                  </table>
                </div>
                <table width="95%" border="0" cellspacing="0" cellpadding="0" align="center">
                  <tr>
                    <td height="20">
                      <input type="button" name="Insert Row" value="Insert Row" class="button1"  onclick="insertRow();">
                      <input type="button" name="Save" value="save" class="button1"  onclick="assignAction();">
                      <input type="button" name="Cancel" value="Cancel" class="button1"  onclick="can('<%=sid%>');">
                      <input type="hidden" name="product_body" value="<%=pd_body %>">
                      <input type="hidden" name="brand" value="<%=brand %>">
                      <input type="hidden" name="version" value="<%=version %>">
                      <input type="hidden" name="sid" value="<%=sid %>">
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
<%@include file="../../index-down.jsp"%>
</body>
</html:html>
