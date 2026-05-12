<!-- /xtrarom/OImaintain/FTTest.jsp -->

<%@page contentType="text/html; charset=Big5"%>
<%@taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<%@page import="java.util.*"%>
<%@page import="com.mxic.oiplus.xtrarom.oimaintain.*"%>
<%@page import="com.mxic.oiplus.au.*"%>
<style>
#bj {
    height: 400px; 
    width:  800px;
    border: solid #d0cab7 3px;
    cursor: move;
    background-color: white;
    position: absolute;
    z-index: 10;
}
#bjlabel{ 
  background-color: #d0cab7;
}


</style>
<%
int sid = Integer.parseInt(request.getParameter("sid"));
boolean status_apply_bo = FTService.status_apply(sid);
boolean doublicatePIM =  com.mxic.oiplus.oimaintain.FTService.doublicatePIM(sid);
String doublicatePIMS =  com.mxic.oiplus.oimaintain.FTService.doublicatePIMS(sid);
boolean chkUser;

OiMaintainStep ois = OiMaintainService.SearchFunction(String.valueOf(sid));
String creator=ois.getCreator();
String sp1=ois.getSponsor_1();
String sp2=ois.getSponsor_2();
User Auth=(User)session.getAttribute("user");
String user=Auth.getUserName();

if(user.equals(creator)){
	chkUser=true;
}else if(user.equals(sp1)){
	chkUser=true;
}else if(user.equals(sp2)){
	chkUser=true;
}else{
	chkUser=false;
}

FTTestActionForm rs = FTService.getInfo(sid);
request.setAttribute("list1", rs);
FTTestActionForm[] rs1 = FTService.getInitialInfo(sid);
request.setAttribute("list2", rs1);
String productType = OiMaintainService.getProductType(Integer.toString(sid));
Vector temperatureList = OiMaintainService.getTemperatureList();
Vector temperatureList_roomtemp = new Vector();
temperatureList_roomtemp.add("ROOM TEMP");
ArrayList al = new ArrayList();
String All_Tester = "";
for(int i=0; i<rs1.length; i++){
   if(!al.contains(rs1[i].getTester())){
       al.add(rs1[i].getTester());
       All_Tester += rs1[i].getTester() + ",";
   }    
}
String[] ROOMTEMP_TesterList = OiMaintainService.getDescription("62");
int remark_length = OiMaintainService.getColumnLength("TF_TEST_PARAMETER_FT_TX", "TF_COMMENT");
%>

<html:html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=big5">
<title>TIM</title>
    <link rel="stylesheet" type="text/css" href='<html:rewrite page="/css/css.css"/>'>
    <link rel="stylesheet" type="text/css" href='<html:rewrite page="/css/filtergrid.css"/>'>
<script src='<html:rewrite page="/js/util.js"/>' language="javascript"></script>
<script src='<html:rewrite page="/js/areaSelect.js"/>' language="javascript"></script>
<script language="javascript" type="text/javascript" src='<html:rewrite page="/js/topmenu.js"/>'></script>
<script language="javascript" type="text/javascript" src='<html:rewrite page="/js/tablefilter-2.js"/>'></script>
<script language="javascript" type="text/javascript" src='<html:rewrite page="/js/sortabletable2.js"/>'></script>
<script language="javascript" type="text/javascript" src='<html:rewrite page="/js/prototype.js"/>'></script>
<script type="text/javascript"></script>

<script language="JavaScript" type="text/javascript">
<!--
function isEmpty(data){
	return ((trim(data) == null) || (trim(data).length == 0));
}

function add_vendor(){
   	if (checkCheckBox('record_id','PGM ', 1)) {
	  if(checkRadio("record_id","")) {
       	document.forms[0].listControl.value = 'add_vendor';
    	document.forms[0].submit();
  	  }
    }
}

function delete_row(){
      var tmp=getCheckBox('record_id');
  	  document.forms[0].record_list.value=tmp;

	if(checkRadio("record_id","")) {
		if(window.confirm("確定要刪除嗎？")){
			document.forms[0].listControl.value = 'delete_row';
			document.forms[0].submit();
        	}

  	}
}


function reset_tx(){
	if(window.confirm("確定要送出嗎？")){
		document.forms[0].listControl.value = 'reset_tx';
		document.forms[0].submit();
  	}
}

function update_data(tmp){
	if(checkdata1(tmp)) {
		if(window.confirm("確定要送出嗎？")){
			document.forms[0].listControl.value = 'update_data';
			document.forms[0].submit();
        	}
  	}
}

function submit_data(tmp){
	var result = showStatTemp(tmp, 1);
	if(result){
		if(checkdata1(tmp)) {
			if(window.confirm("確定要送出嗎？\n因變更會影響criteria檢查\n會將Step 9: Basic Information Definition 打勾(Submit)取消")){
				document.forms[0].listControl.value = 'submit_data';
				document.forms[0].submit();
	        	}
	  	}
	}	
}

function checkdata(){
	var b1=document.form1.record_id;
	if(b1 == null || b1.length ==1){
		return true;
	}
	for(var i=0;i<b1.length;i++){
    	if(b1[i].checked){
    		<%
    		for(int j=0; j<ROOMTEMP_TesterList.length; j++){%>
    			if(document.getElementsByName("tester")[i].value=="<%=ROOMTEMP_TesterList[j]%>"){
    				tester_62++;
    			}
    	  <%}%>
    	  	tester_check++;
    	  	document.getElementById("tester_62").value=document.getElementsByName("tester")[i].value;
    	}
  	}
	if((tester_check - tester_62 >0) && tester_62>0){
		window.alert("特定機台溫度僅能有 ROOM TEMP 選項不可與非特定機台一起維護");
		return false;
	}	
	if(!tester_check>0){
  		window.alert("請點選一項目");
  		return false;
	}
  	return false;
}

function checkdata1(fm){
	var chkObj =fm.record_id;
	var prodType = fm.productType;

	if (chkObj == null) {
		return true;
	}

	if(chkObj.length == 1) {
		if(isEmpty(fm.c_grade.value)){
			window.alert("第 1 行請填寫 C_Grade");
			return false;
		}
		if(document.getElementsByName("hw_configure_radio_0")[1].checked==true){
		     if(isEmpty(document.getElementById("hw_configure_content_0").value)){
			      window.alert("第 1 行請填寫 HW Configure");
			      return false;
			 }
			 //alert("hw_configure_content="+document.getElementById(hw_configure_content).value);
		}
       if (fm.comment.value.length > <%=remark_length%>) {
       	window.alert("第 1 行Notes 字串超過 "+<%=remark_length%>+" 字元！");
       	return false;
       }
       return true;
	}	
	for(var i=0;i<chkObj.length;i++){
		if(isEmpty(fm.c_grade[i].value)){
			window.alert("第 "+(i+1)+" 行請填寫 C_Grade");
			return false;
		}


//		if(fm.tester[i].value == 'KALOS' && isEmpty(fm.comment[i].value)){
//			window.alert("第 "+(i+1)+" 行，KALOS 機台必需填寫實際 program name 於 Notes 欄位");
//			return false;
//		}
		//HW Configure check
			var hw_configure_content = "hw_configure_content_"+i;
			var hw_configure_radio = "hw_configure_radio_"+i;
			document.getElementById(hw_configure_content).value;
			//alert("hw_configure_content="+hw_configure_content);
			//alert("hw_configure_radio.length="+document.getElementsByName(hw_configure_radio).length);
			if(document.getElementsByName(hw_configure_radio)[1].checked==true){
			     if(isEmpty(document.getElementById(hw_configure_content).value)){
				      window.alert("第 "+(i+1)+" 行請填寫 HW Configure");
				      return false;
				 }
				 //alert("hw_configure_content="+document.getElementById(hw_configure_content).value);
			}
	        if (fm.comment[i].value.length > <%=remark_length%>) {
	        	window.alert("第 "+(i+1)+" 行Notes 字串超過 "+<%=remark_length%>+" 字元！");
	        	return false;
	        }
	}
  	<%if (doublicatePIM == true) {%>
	alert("新舊版程式並存，無法送出 請檢查程式 <%=doublicatePIMS%>");
		return false;
	<%}%>
	return true;
}

function add_PIM(sid,pd_body,brand,version,tmp){
	window.location="<html:rewrite page="/xtrarom/OImaintain/FTAdd.jsp"/>?sid=" +sid +"&pd_body=" + pd_body +"&brand="+ brand +"&version=" + version +"&pgmflag=" + tmp;
}

//searchAction.do?sid=<%=sid%>
function back(sid){
	window.location="<html:rewrite page="/OImaintain/searchActionX.do"/>?sid=" +sid;
}

function cleardata(spanId,option3,field){
    //alert("spanId="+spanId);
    //alert("option3="+option3);
   	if(option3==''){
        document.getElementById(field+spanId).innerHTML = '';
   	}else{
    	document.getElementById(field+spanId).innerHTML = option3;
    } 	
    return true;
}

function getHWConfigure(spanId){
    var hw_configure_span = "hw_configure_span_"+spanId;
    var hw_configure_content = "hw_configure_content_"+spanId;
    //alert("hw_configure_span="+hw_configure_span);
    //onclick="window.open('../common/HWConfigure.jsp','miniwin','scrollbars=1,toolbar=0,location=0,width=800,height=200')" 
    //window.open('../common/HWConfigure.jsp?field='+hw_configure_span,'miniwin','scrollbars=1,toolbar=0,location=0,width=800,height=200')";
    //window.open('../common/HWConfigure.jsp?field='+hw_configure_span+'&field1='+hw_configure_content,'miniwin','scrollbars=1,toolbar=0,location=0,width=600,height=300');
    window.open('<html:rewrite page="/common/HWConfigure.jsp?field='+hw_configure_span+'&field1='+hw_configure_content+'"/>','miniwin','scrollbars=1,toolbar=0,location=0,width=600,height=300');
}

function getHWConfigure_By_Tester(){
        //alert("hw_configure_span="+hw_configure_span);
        //onclick="window.open('../common/HWConfigure.jsp','miniwin','scrollbars=1,toolbar=0,location=0,width=800,height=200')" 
        //window.open('../common/HWConfigure.jsp?field='+hw_configure_span,'miniwin','scrollbars=1,toolbar=0,location=0,width=800,height=200')";
        //window.open('../common/HWConfigure.jsp?field='+hw_configure_span+'&field1='+hw_configure_content,'miniwin','scrollbars=1,toolbar=0,location=0,width=600,height=300');
        //alert("aaa="+document.getElementById("all_tester").value);
        //alert("aaa="+<%=al.size() %>);(o)
        //alert("bbb="+<%=All_Tester%>);(x)
        var all_tester=document.getElementById("all_tester").value;
        window.open('<html:rewrite page="/common/HWConfigure_By_Tester.jsp?facility=FT&tester='+all_tester+'&field=hw_configure_span_&field1=hw_configure_content_&field2=hw_configure_radio_"/>','miniwin','scrollbars=1,toolbar=0,location=0,width=600,height=300');
    }
function getSite(spanId){
    var site_span = "site_span_"+spanId;
    var site_content = "site_content_"+spanId;
    var site_content2 = document.getElementById(site_content).value;
    //alert("site_content="+site_content);
    //alert("site_content2="+site_content2);
    window.open('<html:rewrite page="/common/Site.jsp?field='+site_span+'&field1='+site_content+'&field2='+site_content2+'"/>');
}        
function showStatTemp(tmp, sendflag) {
    var chkObj = tmp.record_id;
    var LineItem = new Array();
    if (chkObj == null) {
        return true;
    }
    if(chkObj.length == null|| chkObj.length == 0) {
      	 window.alert("筆數過少，無須統計");
      	 return true;
      }
    for (var i = 0; i < chkObj.length; i++) {
        if (LineItem.length == 0) { //第一筆
            LineItem[0] = new Array();
            LineItem[0][0] = tmp.test_type[i].value;
            LineItem[0][1] = tmp.c_grade[i].value;
            continue;
        }
        for (var j = 0; j < LineItem.length; j++) {
            if (LineItem[j][0] == tmp.test_type[i].value) {
                if (LineItem[j][1].indexOf(tmp.c_grade[i].value) < 0) {
                    LineItem[j][1] = LineItem[j][1] + ',' + tmp.c_grade[i].value;
                }
                break;
            } else if ((LineItem.length - 1) == j) {
                LineItem[j + 1] = new Array();
                LineItem[j + 1][0] = tmp.test_type[i].value;
                LineItem[j + 1][1] = tmp.c_grade[i].value;
            }
        }
    }

    var msg = '<br><br><br><table align=center cellspacing=1 cellpadding=0 class=table2><thead><tr class=title1><td colspan=11 align=center>溫度統計</td></tr><tr class=title1><td>TEST_MODE</td>'
    msg = msg + '<td>C</td>'

    msg = msg + '</tr></thead><tbody><tr class=list1 onMouseOver="overcolor2(this);" onMouseOut="outcolor2(this);">';

    for (var i = 0; i < LineItem.length; i++) {
        msg = msg + '<td>' + LineItem[i][0] + '</td>';
        msg = msg + '<td>' + LineItem[i][1] + '</td>';
        msg = msg + '</tr>';
        if (i != LineItem.length - 1) {
            msg = msg + '<tr class=list1 onMouseOver="overcolor2(this);" onMouseOut="outcolor2(this);">';
        }

    }
    msg = msg + '<input type="button" name="close1" value="close" onClick="showAction('+sendflag+')" class="button1"></thead></table>'
    $("bj").style.left = "150px";
    $("bj").style.top = "80px";
    $("bj").innerHTML = msg;
    showAction(sendflag);
    return false;
}

function showAction(sendflag) {
    if ($("bj").style.display == "none")
        $("bj").style.display = "";
    else
        $("bj").style.display = "none";
    
    if (sendflag == 1 && $("bj").style.display == "none"){
    	if(checkdata1(document.forms[0])) {
			if(window.confirm("確定要送出嗎？\n因變更會影響criteria檢查\n會將Step 9: Basic Information Definition 打勾(Submit)取消")){
				document.forms[0].listControl.value = 'submit_data';
				document.forms[0].submit();
	        	}
	  	}
    }    
}
//-->

</script></head>
<body topmargin="0" leftmargin="0">
<div class="bj" id="bj" style="display: none">	
	
	</div>
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
                    <font size="4">FT Test Parameter Information</font>
                  </td>
                </tr>
                <tr>
                  <td height="20" colspan="2">
                    <hr width="100%" size="1" class="hr">
                  </td>
                </tr>
              </table>
              <form name="form1" method="post" action="<html:rewrite page="/OImaintain/fTTestActionX.do"/>?sid=<%=sid%>">
                <input name="listControl" type="hidden">
                <input name="record_list" type="hidden">
                <input name="productType" type="hidden" value="<%=productType %>">
                <input type="hidden" name="all_tester" value="<%=All_Tester%>"/>
                <table width="95%" border="0" cellspacing="0" cellpadding="0" align="center">
                  <tr>
                    <td>
<%if (status_apply_bo == true && chkUser) {                  %>
                <input type="button" name="save" value="Save" class="button1" onclick="update_data(this.form)">
                <input type="button" name="AddPIM" value="Add from PIM(Buyoff)" class="button1" onclick="add_PIM('<%=sid%>','<bean:write name="list1" property="pd_body"/>','<bean:write name="list1" property="brand"/>','<bean:write name="list1" property="version"/>','buyoff');">
                <input type="button" name="AddPIM" value="Add from PIM(Release)" class="button1" onclick="add_PIM('<%=sid%>','<bean:write name="list1" property="pd_body"/>','<bean:write name="list1" property="brand"/>','<bean:write name="list1" property="version"/>','release');">
                <input type="button" name="AddVendor" value="Add Vendor" class="button1" onclick="add_vendor()">
                <input type="button" name="deleteRow" value="Delete Row" class="button1" onclick="delete_row()">
                <input type="button" name="Submit" value="Submit" class="button1" onclick="submit_data(this.form)">
                <input type="button" name="Reset" value="Reset" class="button1" onclick="reset_tx()">
    <% } %>     <input type="button" name="goback" value="回維護主畫面" class="button1" onclick="back('<%=sid%>');">
                    </td>
                  </tr>
                </table>

           <div id="myDIV1" align="center" style="border:0;">
                <table cellspacing=1 cellpadding=0 class=table2>
                  <thead>
                    <tr class="list1">
                      <td height="20" align="left">
                        <font size="2">
                          <b>                            Product :
                                <bean:write name="list1" property="pd_body"/>
                                / Version
                                <bean:write name="list1" property="version"/>
                          </b>
                        </font>
                      </td>
                    </tr>
                  </thead>
                </table>
              </div>
               <br>
               <div id="myDIV1" align="center">
                <table id="table27" cellspacing=1 cellpadding=0 class=table2>
                  <thead>
                    
                    <tr class="title1">
                    <td height="20" width="3%" ></td>
                      <td height="20" width="5%" >PGM ID</td>
                      <td height="20" width="5%" >BE Opt.</td>
                      <td height="20" width="5%" >Test Mode</td>
                      <td height="20" width="5%" >Pin Count</td>
                      <td height="20" width="5%" >Pkg. Type</td>
                   
                    <td height="20" width="10%" >Temperature(℃)<br><input type="button" value="Temperature Stat." class = "button1" onclick="showStatTemp(this.form, 0);" /></td>

                    <td height="20" width="13%" >Body Size</td>
                      <td height="20" width="18%" >Tester</td>
                      <td height="20" width="5%" >Site</td>
                      <td height="20" width="8%" >Test Program Name</td>
                      <td height="20" width="8%" >Actual Program Name</td>
                      <td height="20" width="8%" >PGM Special Control</td>
                      <td height="20" width="8%" >One Main PGM Group Version</td>
                      <td height="20" width="18%" >H/W Configure限制(單位:M Bit)
                      <input type="button" name="hw_configure_by_tester" value="Upload By Tester" class = "button1" onclick="getHWConfigure_By_Tester();" />
                    </td>
                      <td height="20" width="18%" >Notes</td>
                  </thead>
                  <tbody>
                  <logic:present name="list2">
                  <%
                    int index = 0;
                  %>
                    <logic:iterate id="result1" name="list2" indexId="i">
                      <tr class="list1">
                        <td height="20" bgcolor="<bean:write name="result1" property="changecolor"/>">
                          <input type="checkbox" name="record_id" value="<bean:write name="result1" property="be_opt"/>,<bean:write name="result1" property="test_mode"/>,<bean:write name="result1" property="pin_count"/>,<bean:write name="result1" property="pg_type"/>,<bean:write name="result1" property="tester"/>,<bean:write name="result1" property="pg_name"/>,<bean:write name="result1" property="site"/>,<bean:write name="result1" property="body_size"/>,<bean:write name="result1" property="actual_file"/>,<bean:write name="result1" property="pgm_special_control"/>,<bean:write name="result1" property="one_main_pgm_group_version"/>,<bean:write name="result1" property="pg_id"/>,<bean:write name="result1" property="i_grade"/>,<bean:write name="result1" property="c_grade"/>,<bean:write name="result1" property="hw_configure"/>,<bean:write name="result1" property="notes"/>">
                          <input type="hidden" name="sid" value="<bean:write name="result1" property="sid"/>">
                          <input type="hidden" name="pd_body" value="<bean:write name="list1" property="pd_body"/>">
                          <input type="hidden" name="brand" value="<bean:write name="list1" property="brand"/>">
                          <input type="hidden" name="version" value="<bean:write name="list1" property="version"/>">
                          <input type="hidden" name="test_type" value="<bean:write name="result1" property="test_mode"/>">
                          <input type="hidden" name="be_opt" value="<bean:write name="result1" property="be_opt"/>">
                          <input type="hidden" name="pin_count" value="<bean:write name="result1" property="pin_count"/>">
                          <input type="hidden" name="pg_type" value="<bean:write name="result1" property="pg_type"/>">
                          <input type="hidden" name="body_size" value="<bean:write name="result1" property="body_size"/>">
                          <input type="hidden" name="tester" value="<bean:write name="result1" property="tester"/>">
                          <input type="hidden" name="site" value="<bean:write name="result1" property="site"/>">
                          <input type="hidden" name="pg_name" value="<bean:write name="result1" property="pg_name"/>">
                          <input type="hidden" name="actual_file" value="<bean:write name="result1" property="actual_file"/>">
                          <input type="hidden" name="pgm_special_control" value="<bean:write name="result1" property="pgm_special_control"/>">
                          <input type="hidden" name="one_main_pgm_group_version" value="<bean:write name="result1" property="one_main_pgm_group_version"/>">
                          <input type="hidden" name="pg_id" value="<bean:write name="result1" property="pg_id"/>">
                          <input type="hidden" name="tag" value="<bean:write name="result1" property="tag"/>">
                        </td>
                        <td height="20" bgcolor="<bean:write name="result1" property="changecolor"/>">
                          <bean:write name="result1" property="pg_id"/>
                        </td>
                        <td height="20" bgcolor="<bean:write name="result1" property="changecolor"/>">
                          <bean:write name="result1" property="be_opt"/>
                        </td>
                        <td height="20" bgcolor="<bean:write name="result1" property="changecolor"/>">
                          <bean:write name="result1" property="test_mode"/>
                        </td>
                        <td height="20" bgcolor="<bean:write name="result1" property="changecolor"/>">
                          <bean:write name="result1" property="pin_count"/>
                        </td>
                        <td height="20" bgcolor="<bean:write name="result1" property="changecolor"/>">
                          <bean:write name="result1" property="pg_type"/>
                        </td>
                        <%
                        String option = "";
                        String option2 = "";
                        boolean roomtempFlag = false;
                        for(int ii=0; ii<ROOMTEMP_TesterList.length; ii++){
                        	if(ROOMTEMP_TesterList[ii].equals(rs1[index].getTester())){
                        		roomtempFlag = true;
                        	}    
                        }
                        if(roomtempFlag){
                        	option = OiMaintainService.getTemperatureOption(temperatureList_roomtemp, rs1[index].getI_grade(), 1);
                        	option2 = OiMaintainService.getTemperatureOption(temperatureList_roomtemp, rs1[index].getC_grade(), 0);
                        }else{
                        	option = OiMaintainService.getTemperatureOption(temperatureList, rs1[index].getI_grade(), 1);
                        	option2 = OiMaintainService.getTemperatureOption(temperatureList, rs1[index].getC_grade(), 0);
                        }
                        if (productType.equals("NVM")) {

                        %>
                        <td align="left" height="20" bgcolor="<bean:write name="result1" property="changecolor"/>">
                          <select name="i_grade"><%=option %></select>
                        </td>
                        <td height="20" bgcolor="<bean:write name="result1" property="changecolor"/>">
                          <select name="c_grade"><%=option2 %></select>
                        </td>
                        <%
                        } else {
                        %>
                        <td align="left" height="20" bgcolor="<bean:write name="result1" property="changecolor"/>">
                          <select name="c_grade"><%=option2 %></select>
                        </td>

                        <%
                        }

                        %>
                        <td height="20" bgcolor="<bean:write name="result1" property="changecolor"/>">
                          <bean:write name="result1" property="body_size"/>
                        </td>
                        <td height="20" bgcolor="<bean:write name="result1" property="changecolor"/>">
                          <bean:write name="result1" property="tester"/>
                        </td>
                        <td height="20" bgcolor="<bean:write name="result1" property="changecolor"/>">
                          	<input type="button" name="site_radio_<bean:write name="i"/>" value="Remove Site" checked="true" onclick="getSite(<bean:write name="i"/>)"  /><BR>
                        	<span align="left" id="site_span_<bean:write name="i"/>" name="site_span_<bean:write name="i"/>" ><bean:write name="result1" property="site"/></span>
                        	<input type="hidden" id="site_content_<bean:write name="i"/>" name="site_content_<bean:write name="i"/>" value="<bean:write name="result1" property="site"/>" />
                        </td>
                        <td height="20" bgcolor="<bean:write name="result1" property="changecolor"/>">
                          <bean:write name="result1" property="pg_name"/>
                        </td>
                        <td height="20" bgcolor="<bean:write name="result1" property="changecolor"/>">
                          <bean:write name="result1" property="actual_file"/>
                        </td>
                        <td height="20" bgcolor="<bean:write name="result1" property="changecolor"/>">
                          <bean:write name="result1" property="pgm_special_control"/>
                        </td>
                        <td height="20" bgcolor="<bean:write name="result1" property="changecolor"/>">
                          <bean:write name="result1" property="one_main_pgm_group_version"/>
                        </td>
                        
                       
                        <% String optionHW = OiMaintainService.getSplitString(rs1[index].getHw_configure()); %>
                        <td height="20" bgcolor="<bean:write name="result1" property="changecolor"/>">
                        <span id="hw_configure_radio_span_<bean:write name="i"/>" name="hw_configure_radio_span_<bean:write name="i"/>" >
                          <%  if((rs1[index].getHw_configure()==null) || (rs1[index].getHw_configure().equals("NA"))){ %>
	                            <input type="radio" name="hw_configure_radio_<bean:write name="i"/>" value="NA" checked="true" onclick="cleardata(<bean:write name="i"/>,'','hw_configure_span_')" />NA<input type="radio" name="hw_configure_radio_<bean:write name="i"/>" value="HWConfigure" onclick="getHWConfigure(<bean:write name="i"/>)"  />HWConfigure
	                            <span id="hw_configure_span_<bean:write name="i"/>" name="hw_configure_span_<bean:write name="i"/>" ></span>
                                <input type="hidden" id="hw_configure_content_<bean:write name="i"/>" name="hw_configure_content_<bean:write name="i"/>" value="" />
	                      <%   }else{ %>
                                <input type="radio" name="hw_configure_radio_<bean:write name="i"/>" value="NA" onclick="cleardata(<bean:write name="i"/>,'','hw_configure_span_')" />NA<input type="radio" name="hw_configure_radio_<bean:write name="i"/>" value="HWConfigure" checked="true" onclick="getHWConfigure(<bean:write name="i"/>)"  />HWConfigure
                                <span id="hw_configure_span_<bean:write name="i"/>" name="hw_configure_span_<bean:write name="i"/>" ><%=optionHW %></span>
                                <input type="hidden" id="hw_configure_content_<bean:write name="i"/>" name="hw_configure_content_<bean:write name="i"/>" value="<%=optionHW %>" />
	                      <%   }    %>   
	                    </span>    
                        </td>
                       
                        <td width="100%" align="left" height="20" bgcolor="<bean:write name="result1" property="changecolor"/>">
                          <input type="text" size=20 name="comment" value="<bean:write name="result1" property="notes"/>"/>
                        </td>
                        <%index++; %>
                      </tr>
                    </logic:iterate>
                  </logic:present>
                  </tbody>
                </table>
              </div>

              <table width="95%" border="0" cellspacing="0" cellpadding="0" align="center">
                <tr>
                  <td height="20" colspan="2">
<%if (status_apply_bo == true && chkUser) {                  %>
                        <input type="button" name="save" value="Save" class="button1" onclick="update_data(this.form)">
                        <input type="button" name="AddPIM" value="Add from PIM(Buyoff)" class="button1" onclick="add_PIM('<%=sid%>','<bean:write name="list1" property="pd_body"/>','<bean:write name="list1" property="brand"/>','<bean:write name="list1" property="version"/>','buyoff');">
                        <input type="button" name="AddPIM" value="Add from PIM(Release)" class="button1" onclick="add_PIM('<%=sid%>','<bean:write name="list1" property="pd_body"/>','<bean:write name="list1" property="brand"/>','<bean:write name="list1" property="version"/>','release');">
                        <input type="button" name="AddVendor" value="Add Vendor" class="button1" onclick="add_vendor()">
                        <input type="button" name="deleteRow" value="Delete Row" class="button1" onclick="delete_row()">
                        <input type="button" name="Submit" value="Submit" class="button1" onclick="submit_data(this.form)">
                        <input type="button" name="Reset" value="Reset" class="button1" onclick="reset_tx()">
                   <% } %>     <input type="button" name="goback" value="回維護主畫面" class="button1" onclick="back('<%=sid%>');">

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
  <script type="text/javascript">
	  //20110531var st1 = new SortableTable(document.getElementById("table27"));
</script>
<%@include file="../../index-down.jsp"%>
</body>
<script language="JavaScript">
    $("bj").style.left = "150px";
    $("bj").style.top = "80px";
    $("bj").onmousedown = function(evt) {
        evt = event || evt;
        px = parseInt($("bj").style.left);
        py = parseInt($("bj").style.top);
        x = evt.clientX;
        y = evt.clientY;
        document.onmousemove = move;
    }


    function move(evt) {
        evt = event || evt;
        $("bj").style.left = evt.clientX - x + px + "px";
        $("bj").style.top = evt.clientY - y + py + "px";
        document.onmouseup = function() {
            document.onmousemove = null;
        }
    }
</script>
<script type="text/javascript">
  var props = {
    filters_row_index: 1,
    loader: true,
    loader_html: '<img src="<html:rewrite page="/image/loader.gif"/>" alt="" style="margin: 0pt 5px; vertical-align: middle;"><span>Loading...</span>',
    status_bar: false,
//        col_0: "none",
    enter_key: true
  };

      setFilterGrid("table27",props);
</script>
</html:html>
