<!-- /xtrarom/OImaintain/FTAdd.jsp -->

<%@page contentType="text/html; charset=Big5"%>
<%@taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>

<%@page import="java.util.*"%>
<%@page import="com.mxic.oiplus.xtrarom.oimaintain.*"%>
<html:html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=big5">
<script>
function copy(){

 //if(checkCheckBox('record_id','','50')){
 if(checkCheckBoxNoLimit('record_id','')){
	if (checkOneMainPGM()){	
    	var id = getRadioValue('record_id');
    	document.forms[0].listControl.value = 'copy';
    	document.forms[0].submit();
	}
 }
}

function checkOneMainPGM(){
    		var OB;
    		OB = document.getElementsByName("record_id");
    		OB1 = document.getElementsByName("one_main_pgm_group_version");
    		OB2 = document.getElementsByName("one_main_pgm_num");
    		if(!OB || OB.length === 0){
    		  alert("未勾選要Copy的資料");
    		  return false;
    		}
    		/*for(var i=0;i<OB.length;i++){
    			if(OB[i].checked && OB1[i].value.substring(0,OB1[i].value.indexOf("#")) != ""){
    				for(var j=0;j<OB.length;j++){
    					if(OB1[i].value.substring(0,OB1[i].value.indexOf("#")) == OB1[j].value.substring(0,OB1[j].value.indexOf("#"))){
    						if(!OB[j].checked){
    						  alert(OB1[i].value+" 需全部選取! 第"+(j+1)+"行未選取!");	
    						  return false;
    						}  
    					}	
    				}	
    			}	
    		}*/
    		if(OB.length>1){
    			for(var i=0;i<OB.length;i++){
    				var onemainpgmnum = 0;
    				if(OB[i].checked && OB1[i].value!= ""){
    					for(var j=0;j<OB.length;j++){
    						if(OB1[i].value == OB1[j].value){
    							if(!OB[j].checked){
    							  alert(OB1[i].value+" 需全部選取! 第"+(j+1)+"行未選取!");	
    							  return false;
    							}else{
    								onemainpgmnum++;
    							}  
    						}	
    					}
    					/*20180717if(onemainpgmnum!=OB2[i].value){
    						alert(OB1[i].value+" 需全部選取! 共"+OB2[i].value+"筆!");	
    						return false;
    					}*/ 
    				}	
    			}
    		}else{
    			/*20180717var onemainpgmnum = 1;
    			if(onemainpgmnum!=OB2.value){
    				alert(OB1.value+" 需全部選取! 共"+OB2.value+"筆!");	
    				return false;
    			}*/ 
    		}		
   		    return true;
    	}
function check(){
  	document.forms[0].submit();
}
function check1(){
  if(checkCheckBox('test_mode','','30')){
    var id = getRadioValue('test_mode');
    document.forms[0].listControl.value = 'check1';
document.forms[0].submit();

  }
}
function selectAll(){
	var form = document.forms[0];
	for (i = 0; i<form.elements.length; i++) {
		if (form.elements[i].type == 'checkbox' && !form.elements[i].disabled) {
			form.elements[i].checked = "true";
		}
	}
}
function unselectAll(){
	var form = document.forms[0];
	for (i = 0; i<form.elements.length; i++) {
		if (form.elements[i].type == 'checkbox' && !form.elements[i].disabled) {
			form.elements[i].checked = "";
		}
	}
}

function can(sid){

window.location="<html:rewrite page="/xtrarom/OImaintain/FTTest.jsp"/>?sid=" +sid;

}


</script><%
  String pd_body = request.getParameter("pd_body");
  String sid=request.getParameter("sid");
  String brand = request.getParameter("brand");
  String version = request.getParameter("version");
  String pgmflag = request.getParameter("pgmflag");
  System.out.println("pgmflag="+pgmflag);

  FTTestAddActionForm[] rs = FTService.gettest_mode(brand,pd_body,pgmflag);
  request.setAttribute("list1", rs);
%>
<title>專案管理</title>
  <link rel="stylesheet" type="text/css" href='<html:rewrite page="/css/css.css"/>'>
<script src='<html:rewrite page="/js/util.js"/>' language="javascript"></script><script src='<html:rewrite page="/js/areaSelect.js"/>' language="javascript"></script><script type="text/javascript">
</script></head>
<body topmargin="0" leftmargin="0">
<%@include file="../../index-menu.jsp"%>
  <table width="100%" border=0 class="bg1">
    <tr>
      <!--<td valign="top" width="159" class="bg">-->
</td>      <td valign="top">
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
                  <td height="20" colspan="3">
                    <hr width="100%" size="1" class="hr">
                  </td>
                </tr>
              <form name="form1" action="<html:rewrite page="/OImaintain/fTTestAddActionX.do"/>?sid=<%=sid%>&pd_body=<%=pd_body %>&brand=<%=brand %>&version=<%=version %>" method="post">
                <input name="listControl" type="hidden">
                <input type="hidden" name="pgmflag" value="<%=pgmflag%>"/>
                <tr>
                  <td height="20" colspan="3">
                     <logic:present name="list1"> <input type="button" name="Copy" value="Copy This" class="button1" onclick="copy()">
					<input type="button" class="button1" value="Select All" onclick="selectAll()" />
					<input type="button" class="button1" value="Clear All" onclick="unselectAll()" />
					 </logic:present>

                       <input type="button" name="Cancel" value="Cancel" class="button1"   onclick="can('<%=sid%>');">
                  </td>
                </tr>

              </table>
              <div id="myDIV1" align="center" style="border:0;">
                <table id="table27" cellspacing=1 cellpadding=0 class=table2>
                  <thead>
                    <tr class="list1">
                      <td height="20" align="left" colspan="3"><font size="2"><b>Product: <%=pd_body %> / Version<%=version %></b></font>
                      </td>
                    </tr>
                    <tr class="list1">
                      <td height="20" width="8%" align="left">Available Mode</td>
                      <td height="20" align="left">
                        <logic:present name="list1">
                          <logic:iterate id="result" name="list1">
                            <input type="checkbox" name="test_mode"   value="<bean:write name="result" property="test_mode_str"/>"/>
                            <bean:write name="result" property="test_mode_str"/>

                   </logic:iterate>     <input type="button" name="getpgm" value="Get PGM" onclick="check1()" class="button1"/>
                        </logic:present>
                         <logic:notPresent name="list1">
                     此次搜尋共　'0'　筆資料
                    </logic:notPresent>
                      </td>
                    </tr>
                  </thead>
                </table>
              </div>
              <div id="myDIV4" align="center" style="boder:0;height:310 ">
                <table id="table1" cellspacing=1 cellpadding=0 class=table2>
                  <thead>
                    <tr class="title1">
                      <td height="20" />
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
                      <td height="20" >PGM Special Control</td>
                      <td align="left">One Main PGM Group Version</td>
                      <td align="left">One Main PGM Number</td>
                       <td align="left">PAT Info</td>
                    </tr>
                  </thead>
                  <logic:present name="list2">
                    <logic:iterate id="result2" name="list2" indexId="i">
                      <tr class="list1">
                        <td height="20">
                          <input type="checkbox" name="record_id" value="<bean:write name="result2" property="pg_id"/>,<bean:write name="result2" property="test_mode_str"/>,<bean:write name="result2" property="be_opt"/>,<bean:write name="result2" property="pin_count"/>,<bean:write name="result2" property="pg_type"/>,<bean:write name="result2" property="body_size"/>,<bean:write name="result2" property="tester"/>,<bean:write name="result2" property="site"/>,<bean:write name="result2" property="pg_name"/>,<bean:write name="result2" property="actual_file"/>,<bean:write name="result2" property="pgm_special_control"/>,<bean:write name="result2" property="one_main_pgm_group_version"/>,<bean:write name="result2" property="os_version"/>" <c:if test="${result2.pta_auth eq 'N'}">DISABLED title="PTA未符合條件 無法選取"</c:if>/> 
                          <!--<input type="checkbox" name="record_id" value="<bean:write name="result2" property="pg_id"/>,<bean:write name="result2" property="test_mode_str"/>,<bean:write name="result2" property="be_opt"/>,<bean:write name="result2" property="pin_count"/>,<bean:write name="result2" property="pg_type"/>,<bean:write name="result2" property="body_size"/>,<bean:write name="result2" property="tester"/>,<bean:write name="result2" property="site"/>,<bean:write name="result2" property="pg_name"/>,<bean:write name="result2" property="actual_file"/>,<bean:write name="result2" property="pgm_special_control"/>,<bean:write name="result2" property="one_main_pgm_group_version"/>,<bean:write name="result2" property="os_version"/>">-->
                          <input type="hidden" name="sid" value="<%=sid%>">
                          <input type="hidden" name="pd_body" value="<%=pd_body%>">
                          <input type="hidden" name="brand" value="<%=brand%>">
                          <input type="hidden" name="version" value="<%=version%>">
                        </td>
                        <td height="20">
                          <bean:write name="result2" property="pg_id"/>
                        </td>
                        <td height="20">
                          <bean:write name="result2" property="be_opt"/>
                        </td>
                        <td height="20">
                          <bean:write name="result2" property="test_mode_str"/>
                        </td>
                        <td height="20">
                          <bean:write name="result2" property="pin_count"/>
                        </td>
                        <td height="20">
                          <bean:write name="result2" property="pg_type"/>
                        </td>
                        <td height="20">
                          <bean:write name="result2" property="body_size"/>
                        </td>
                        <td height="20">
                          <bean:write name="result2" property="tester"/>
                        </td>
                        <td height="20">
                          <bean:write name="result2" property="site"/>
                        </td>
                        <td height="20">
                          <bean:write name="result2" property="pg_name"/>
                        </td>
                        <td height="20">
                          <bean:write name="result2" property="actual_file"/>
                        </td>
                        <td height="20">
                          <bean:write name="result2" property="pgm_special_control"/>
                        </td>
                        <td height="20">
                          <bean:write name="result2" property="one_main_pgm_group_version"/>
                          <input type="hidden" name="one_main_pgm_group_version" value="<bean:write name="result2" property="one_main_pgm_group_version"/>">
                        </td>
                        <td height="20">
                          <bean:write name="result2" property="one_main_pgm_num"/>
                          <input type="hidden" name="one_main_pgm_num" value="<bean:write name="result2" property="one_main_pgm_num"/>">
                        </td>
                        <td height="20">
                          <bean:write name="result2" property="pta_status"/>
                          <input type="hidden" name="pta_status" value="<bean:write name="result2" property="pta_status"/>">
                        </td>
                      </tr>
                    </logic:iterate>
                  </logic:present>
                </table>
              </div>
              <table width="95%" border="0" cellspacing="0" cellpadding="0" align="center">
                <tr>
                  <td height="20">
                     <logic:present name="list1"> 
                     <input type="button" name="Copy" value="Copy This" class="button1" onclick="copy()"> 
                     <input type="button" class="button1" value="Select All" onclick="selectAll()" />
					<input type="button" class="button1" value="Clear All" onclick="unselectAll()" />
                     </logic:present>
                    <input type="button" name="Cancel" value="Cancel" class="button1"  onclick="can('<%=sid%>');">
                  </td>
                </tr>
              </table>
              　
            </td>
          </tr>
        </table>
</form>      </td>
    </tr>
  </table>
<%@include file="../../index-down.jsp"%>
</body>
</html:html>
