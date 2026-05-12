<!-- /xtrarom/OImaintain/PBC_Test_Parameter.jsp -->

<%@page contentType="text/html; charset=Big5"%>
<%@taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<%@page import="java.util.*"%>
<%@page import="com.mxic.oiplus.xtrarom.oimaintain.*"%>
<%@page import="com.mxic.oiplus.au.*"%>
<%
String t = (String) request.getAttribute("flag");
String sid = Integer.toString(((PBCTestParameterForm) request.getAttribute("proTestRouteBeanAFX")).getSid());
String productType = OiMaintainService.getProductType(sid);
PBCTestParameterBean[] result3 = (PBCTestParameterBean[])request.getAttribute("pbcParam");
ArrayList al = new ArrayList();
String All_Tester = "";
for(int i=0; i<result3.length; i++){
   if(!al.contains(result3[i].getTester())){
       al.add(result3[i].getTester());
       All_Tester += result3[i].getTester() + ",";
   }    
}
System.out.println("All_Tester="+All_Tester);
%>

<html:html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=big5">
<title>Burn-in/Cycling/AVI Test Parameter Information</title>
    <link rel="stylesheet" type="text/css" href='<html:rewrite page="/css/css.css"/>'>
    <link rel="stylesheet" type="text/css" href='<html:rewrite page="/css/filtergrid.css"/>'>
<script src='<html:rewrite page="/js/util.js"/>' language="javascript"></script>
<script src='<html:rewrite page="/js/areaSelect.js"/>' language="javascript"></script>
<script language="javascript" type="text/javascript" src='<html:rewrite page="/js/topmenu.js"/>'></script>
<script language="javascript" type="text/javascript" src='<html:rewrite page="/js/tablefilter-2.js"/>'></script>
<script language="javascript" type="text/javascript" src='<html:rewrite page="/js/sortabletable2.js"/>'></script>
<script type="text/javascript"></script>

<script language="JavaScript" type="text/javascript">
<!--
function isEmpty(data){
	return ((trim(data) == null) || (trim(data).length == 0));
}

function add_vendor(){
	if(checkRadio("record_id","")) {
//      		if(window.confirm("確定要送出嗎？")){
        	document.forms[0].listControl.value = 'add_vendor';
    		document.forms[0].submit();
//        	}
  	}
}

function delete_row(){
/*
    var tag = getValueByRadio('record_id', 'tag');
    if (tag == '0') {
        alert("無法刪除由前一版帶入資料");
    	return false;
    }
*/
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
	if(checkdata1(tmp)) {
		if(window.confirm("確定要送出嗎？")){
			document.forms[0].listControl.value = 'submit_data';
			document.forms[0].submit();
        	}
  	}
}

function checkdata(){
	var b1=document.form1.record_id;
	if(b1 == null){
		return true;
	}
	for(var i=0;i<b1.length;i++){
    		if(b1[i].checked){
      			//window.alert("請選擇起始日");
      			return true;
    		}
  	}
  	window.alert("請點選一項目");
  	return false;
}

function checkdata1(fm){
	var chkObj =fm.record_id;
	var prodType = fm.productType;

	if (chkObj == null) {
	    return true;
	}
	//alert("chkObj="+chkObj.value);
    //alert("chkObj"+chkObj.length); Only 1 record --> error
	for(var i=0;i<chkObj.length;i++){

		if(isEmpty(fm.c_grade[i].value)){
			window.alert("第 "+(i+1)+" 行請填寫 Temperature");
			return false;
		}

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

	}
    return true;
}

function add(sid,pd_body,brand,version){
	window.location="<html:rewrite page="/xtrarom/OImaintain/PBCAdd.jsp"/>?sid=" +sid +"&pd_body=" + pd_body +"&brand="+ brand +"&version=" + version;
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
        var all_tester=document.getElementById("all_tester").value;
        window.open('<html:rewrite page="/common/HWConfigure_By_Tester.jsp?facility=FT&tester='+all_tester+'&field=hw_configure_span_&field1=hw_configure_content_&field2=hw_configure_radio_"/>','miniwin','scrollbars=1,toolbar=0,location=0,width=600,height=300');
    }
//-->

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
              <form name="form1" method="post" action="<html:rewrite page="/OImaintain/savePBCTestParameterActionX.do"/>">
                <input name="listControl" id="listControl" type="hidden">
                <input type="hidden" name="sid" id="sid" value="<%=sid %>">
                <input name="productType" id="productType" type="hidden" value="<%=productType %>">
                <input type="hidden" id="all_tester" name="all_tester" value="<%=All_Tester%>"/>
              <table width="95%" border="0" cellspacing="0" cellpadding="0" align="center">
                <tr>
                  <td width="15%" height="25" class="title2">
                    <img src="../../image/arrow.gif" width="5" height="14" hspace="3" alt="">
                    <font size="4">TIM</font>
                  </td>
                  <td noWrap height="25" width="85%" class="title4">
                    <font size="4">Burn-in/Cycling/AVI Test Parameter Information</font>
                  </td>
                </tr>
                <tr>
                  <td height="20" colspan="2">
                    <hr width="100%" size="1" class="hr">
                  </td>
                </tr>
				<% if (t.equals("Show")) { %>
                <tr>
                  <td height="20" colspan="2"><font color="#C00000">注意：若需要刪除所有資料，請先按 "Submit" 後，再做刪除動作，否則系統將再自動帶入前一版資料。</font>
                  </td>
                </tr>
				<% } %>
                <tr>
                  <td height="20" colspan="2">
				   <% if (t.equals("Show")) { %>
                        <input type="button" name="save" value="Save" class="button1" onclick="update_data(this.form)">
                        <input type="button" name="Add" value="Add" class="button1" onclick="add('<%=sid%>','<bean:write name="proTestRouteBeanAFX" property="product_body"/>','<bean:write name="proTestRouteBeanAFX" property="brand"/>','<bean:write name="proTestRouteBeanAFX" property="version"/>');">
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
                             <bean:write name="proTestRouteBeanAFX" property="product_body"/>
                             / Version
                             <bean:write name="proTestRouteBeanAFX" property="version"/>
                          </b>
                        </font>
                      </td>
                    </tr>
                  </thead>
                </table>
              </div>
<br>
               <div id="myDIV1" align="center" >
                <table id="table27" cellspacing=1 cellpadding=0 class=table2>
                  <thead>
                    <tr class="title1">
                      <td height="20" width="3%"></td>
                      <td height="20" width="5%">PGM ID</td>
                      <td height="20" width="5%">BE Opt.</td>
                      <td height="20" width="5%">Test Mode</td>
                      <td height="20" width="5%">Pin Count</td>
                      <td height="20" width="5%">Pkg. Type</td>
                      <td height="20" width="10%">Temperature(℃)</td>
                      <td height="20" width="13%">Body Size</td>
                      <td height="20" width="18%">Tester</td>
                      <td height="20" width="5%">Site</td>
                      <td height="20" width="8%">Test Program Name</td>
                      <td height="20" width="8%">Actual Program Name</td>
                      <td height="20" width="18%" >H/W Configure限制(單位:M Bit)
                      <input type="button" name="hw_configure_by_tester" value="Upload By Tester" class = "button1" onclick="getHWConfigure_By_Tester();" />
                    </td>
                      <td height="20" width="18%">Notes</td>
                      
                    </tr>
                  </thead>
                  <tbody>
                  <logic:present name="pbcParam">
                  <%
                    PBCTestParameterBean[] result2 = (PBCTestParameterBean[])request.getAttribute("pbcParam");
                    int index = 0;
                  %>
                    <logic:iterate id="result1" name="pbcParam" indexId="i">
                      <tr class="list1">
                        <td height="20" bgcolor="<bean:write name="result1" property="changecolor"/>">
                          <input type="radio" name="record_id" value="<bean:write name="result1" property="backend_option"/>,<bean:write name="result1" property="test_type"/>,<bean:write name="result1" property="pin_count"/>,<bean:write name="result1" property="package_type"/>,<bean:write name="result1" property="tester"/>,<bean:write name="result1" property="program_name"/>,<bean:write name="result1" property="site"/>,<bean:write name="result1" property="body_size"/>,<bean:write name="result1" property="actual_file"/>,<bean:write name="result1" property="pgm_id"/>,<bean:write name="result1" property="i_grade"/>,<bean:write name="result1" property="c_grade"/>,<bean:write name="result1" property="pgm_id"/>">
                          <input type="hidden" name="sid" value="<bean:write name="result1" property="sid"/>">
                          <input type="hidden" name="product_body" value="<bean:write name="result1" property="product_body"/>">
                          <input type="hidden" name="brand" value="<bean:write name="result1" property="brand"/>">
                          <input type="hidden" name="version" value="<bean:write name="result1" property="version"/>">
                          <input type="hidden" name="test_type" value="<bean:write name="result1" property="test_type"/>">
                          <input type="hidden" name="backend_option" value="<bean:write name="result1" property="backend_option"/>">
                          <input type="hidden" name="pin_count" value="<bean:write name="result1" property="pin_count"/>">
                          <input type="hidden" name="package_type" value="<bean:write name="result1" property="package_type"/>">
                          <input type="hidden" name="body_size" value="<bean:write name="result1" property="body_size"/>">
                          <input type="hidden" name="tester" value="<bean:write name="result1" property="tester"/>">
                          <input type="hidden" name="site" value="<bean:write name="result1" property="site"/>">
                          <input type="hidden" name="program_name" value="<bean:write name="result1" property="program_name"/>">
                          <input type="hidden" name="actual_file" value="<bean:write name="result1" property="actual_file"/>">
                          <input type="hidden" name="pgm_id" value="<bean:write name="result1" property="pgm_id"/>">
                          <input type="hidden" name="tag" value="<bean:write name="result1" property="tag"/>">
                        </td>
                        <td height="20" bgcolor="<bean:write name="result1" property="changecolor"/>">
                          <bean:write name="result1" property="pgm_id"/>
                        </td>
                        <td height="20" bgcolor="<bean:write name="result1" property="changecolor"/>">
                          <bean:write name="result1" property="backend_option"/>
                        </td>
                        <td height="20" bgcolor="<bean:write name="result1" property="changecolor"/>">
                          <bean:write name="result1" property="test_type"/>
                        </td>
                        <td height="20" bgcolor="<bean:write name="result1" property="changecolor"/>">
                          <bean:write name="result1" property="pin_count"/>
                        </td>
                        <td height="20" bgcolor="<bean:write name="result1" property="changecolor"/>">
                          <bean:write name="result1" property="package_type"/>
                        </td>
						<logic:notEqual name="result1" property="test_type" value="AVI">
                        <td height="20" bgcolor="<bean:write name="result1" property="changecolor"/>">
                          <input type="text" size=8 name="c_grade" value="<bean:write name="result1" property="c_grade"/>"/>
                        </td>
						</logic:notEqual>
						<logic:equal name="result1" property="test_type" value="AVI">
                        <td height="20" bgcolor="<bean:write name="result1" property="changecolor"/>">
                          <input type="text" size=8 readonly name="c_grade" value="<bean:write name="result1" property="c_grade"/>"/>
                        </td>
						</logic:equal>
                        <td height="20" bgcolor="<bean:write name="result1" property="changecolor"/>">
                          <bean:write name="result1" property="body_size"/>
                        </td>
                        <td height="20" bgcolor="<bean:write name="result1" property="changecolor"/>">
                          <bean:write name="result1" property="tester"/>
                        </td>
                        <td height="20" bgcolor="<bean:write name="result1" property="changecolor"/>">
                          <bean:write name="result1" property="site"/>
                        </td>
                        <td height="20" bgcolor="<bean:write name="result1" property="changecolor"/>">
                          <bean:write name="result1" property="program_name"/>
                        </td>
                        <td height="20" bgcolor="<bean:write name="result1" property="changecolor"/>">
                          <bean:write name="result1" property="actual_file"/>
                        </td>
                        
                        
                        <% String optionHW = OiMaintainService.getSplitString(result2[index].getHw_configure()); %>
                        <td height="20" bgcolor="<bean:write name="result1" property="changecolor"/>">
                        <span id="hw_configure_radio_span_<bean:write name="i"/>" name="hw_configure_radio_span_<bean:write name="i"/>" >
                          <%  if((result2[index].getHw_configure()==null) || (result2[index].getHw_configure().equals("NA"))){ %>
	                            <input type="radio" id="hw_configure_radio_<bean:write name="i"/>" name="hw_configure_radio_<bean:write name="i"/>" value="NA" checked="true" onclick="cleardata(<bean:write name="i"/>,'','hw_configure_span_')" />NA
	                            <input type="radio" id="hw_configure_radio_<bean:write name="i"/>" name="hw_configure_radio_<bean:write name="i"/>" value="HWConfigure" onclick="getHWConfigure(<bean:write name="i"/>)"  />HWConfigure
	                            <span id="hw_configure_span_<bean:write name="i"/>" name="hw_configure_span_<bean:write name="i"/>" ></span>
                                <input type="hidden" id="hw_configure_content_<bean:write name="i"/>" name="hw_configure_content_<bean:write name="i"/>" value="" />
	                      <%   }else{ %>
                                <input type="radio" id="hw_configure_radio_<bean:write name="i"/>" name="hw_configure_radio_<bean:write name="i"/>" value="NA" onclick="cleardata(<bean:write name="i"/>,'','hw_configure_span_')" />NA
                                <input type="radio" id="hw_configure_radio_<bean:write name="i"/>" name="hw_configure_radio_<bean:write name="i"/>" value="HWConfigure" checked="true" onclick="getHWConfigure(<bean:write name="i"/>)"  />HWConfigure
                                <span id="hw_configure_span_<bean:write name="i"/>" name="hw_configure_span_<bean:write name="i"/>" ><%=optionHW %></span>
                                <input type="hidden" id="hw_configure_content_<bean:write name="i"/>" name="hw_configure_content_<bean:write name="i"/>" value="<%=optionHW %>" />
	                      <%   }    %>    
	                    </span>   
                        </td>
                        
                        <td width="100%" align="left" height="20" bgcolor="<bean:write name="result1" property="changecolor"/>">
                          <input type="text" size="20" id="tf_comment" name="tf_comment" value="<bean:write name="result1" property="tf_comment"/>"/>
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
				   <% if (t.equals("Show")) { %>
                        <input type="button" name="save" value="Save" class="button1" onclick="update_data(this.form)">
                        <input type="button" name="Add" value="Add" class="button1" onclick="add('<%=sid%>','<bean:write name="proTestRouteBeanAFX" property="product_body"/>','<bean:write name="proTestRouteBeanAFX" property="brand"/>','<bean:write name="proTestRouteBeanAFX" property="version"/>');">
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
