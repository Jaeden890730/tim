<!-- /xtrarom/OIMaintain/AddPGM.jsp -->

<%@page contentType="text/html; charset=Big5"%>
<%@taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<%@ page import="java.util.*"%>

<%
String pgmflag = (String) request.getAttribute("pgmflag");
System.out.println("pgmflag="+pgmflag);
%>

<html:html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=big5">
      <title>OI Add From PGM</title>
      <link rel="stylesheet" type="text/css" href='<html:rewrite page="/css/css.css"/>'>
      <script src='<html:rewrite page="/js/util.js"/>' language="javascript" type="text/javascript"></script>
      <script src='<html:rewrite page="/js/areaSelect.js"/>' language="javascript" type="text/javascript"></script>

      <script type="text/javascript">

      function redirectWS(tmp){
    	var sid = tmp.sid.value;  
        window.location="<html:rewrite page="/OImaintain/wsTestParameterActionX.do"/>?sid="+sid;
      }
      function redirectPGM(tmp){
	if (checkRadio5("chkbx","要搜尋的資料")){
        tmp.flag.value='search';
        tmp.submit();
	}
      }
      function redirectCopy(tmp){
        if (checkRadio5("chkbx2","要複製的資料")){
        	if (checkOneMainPGM()){
	          tmp.flag.value='copy';
	          tmp.submit();
        	}  
        }
      }
      function checkOneMainPGM(){
  		var OB;
  		OB = document.getElementsByName("chkbx2");
  		OB1 = document.getElementsByName("one_main_pgm_group_version");
  		OB2 = document.getElementsByName("one_main_pgm_num");
  		if(OB.length == 0){
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
				if(OB[i].checked && OB1[i].value.substring(0,OB1[i].value.indexOf("#")) != ""){
					for(var j=0;j<OB.length;j++){
						if(OB1[i].value.substring(0,OB1[i].value.indexOf("#")) == OB1[j].value.substring(0,OB1[j].value.indexOf("#"))){
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
      </script>

    </head>
    <body topmargin="0" leftmargin="0">
      <%@  include file="../../index-menu.jsp"%>
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
                          <img src="../../image/arrow.gif" width="5" height="14" hspace="3" alt=""><font size="4">TIM</font>
                          </td>
                          <td noWrap height="25" width="85%" class="title4"><font size="4">WS Test Parameter Information / Add Program</font></td>
                        </tr>
                        <tr>
                          <td height="20" colspan="4">
                            <hr width="100%" color=#B4761B size="1">
                            </td>
                          </tr>
                        </table>
                  <!--Data Table Starts From Here-->
                        <html:form action="/OImaintain/searchTestParamByMaskActionX.do">
                          <input type="hidden" name="flag"/>
                          <input type="hidden" name="pro_b" value="<bean:write name="proTestRouteBeanAFX" property="productbody"/>"/>
                          <input type="hidden" name="pgmflag" value="<%=pgmflag%>"/>
                          <input type="hidden" name="sid" value="<bean:write name="proTestRouteBeanAFX" property="sid"/>"/>
                          <div id="myDIV1" align="center" style="border:0;">
                            <table width="95%" border="0" id="table28">
                              <tr>
                                <td>
                                  <input type="button" name="suabc" class = "button1" value="Copy This" onclick="redirectCopy(this.form);"/>
                                  <input type="button" class="button1" value="Select All" onclick="selectAll()" />
									<input type="button" class="button1" value="Clear All" onclick="unselectAll()" />
                                  <input type="button" name="sureset" class = "button1" value="Cancel" onclick="redirectWS(this.form);"/>
                                </td>
                              </tr>
                            </table>
                            <table id="table27" cellspacing=1 cellpadding=0 class=table2  >
                              <thead>
                                <tr class="list1">
                                  <td align="left" colspan="10"><font size="2"><b><bean:write name="proTestRouteBeanAFX" property="productbody"/> / Version <bean:write name="proTestRouteBeanAFX" property="version"/></b></font></td>
                                </tr>
                                <!--選項與搜尋的按鈕-->
                                <tr class="list1">
                                  <td align="left" colspan="4"><b>Available Mode</b></td>
                                  <td align="left" colspan="7">
                                    <logic:present name="AddPGM" >
                                      <logic:iterate id="result" name="AddPGM" >
                                        <input type="checkbox" class = "button1" name="chkbx" value="<bean:write name="result" property="mask_option"/>"/><bean:write name="result" property="mask_option"/>
                                      </logic:iterate>
                                    </logic:present>
                                    <input type="button" name="chk1" value="Get PGM" class = "button1" onclick="redirectPGM(this.form);"/>
                                  </td>
                                </tr>
                                <!--選項與搜尋的按鈕結尾-->
                                <tr class="title1">
                                  <td align="left"></td>
                                  <td align="left">PGM ID</td>
                                  <td align="left" >Mask Opt.</td>
                                  <td align="left">Test Mode</td>
                                  <td align="left">Tester</td>
                                  <td align="left">Site</td>
                                  <td align="left">PGM Name</td>
                                  <td align="left">PGM Special Control</td>
                                  <td align="left">One Main PGM Group Version</td>
                                  <td align="left">One Main PGM Number</td>
                                </tr>
                              </thead>
                              <!--搜尋結果-->
                              <tbody>
                                <logic:present name="PGMResult" >
                                  <logic:iterate id="res" name="PGMResult" indexId="i" >
                                    <tr class="list1">
                                      <td align="left"><input type="checkbox" name="chkbx2" value="<bean:write name="res" property="pgm_id"/>#<bean:write name="i"/>"/></td>
                                      <td align="left"><input type="hidden" name="pgm_id" value="<bean:write name="res" property="pgm_id"/>#<bean:write name="i"/>"/><bean:write name="res" property="pgm_id"/></td>
                                      <td align="left"><input type="hidden" name="mask_option" value="<bean:write name="res" property="mask_option"/>#<bean:write name="i"/>"/><bean:write name="res" property="mask_option"/></td>
                                      <td align="left"><input type="hidden" name="test_type" value="<bean:write name="res" property="test_type"/>#<bean:write name="i"/>"/><bean:write name="res" property="test_type"/></td>
                                      <td align="left"><input type="hidden" name="tester" value="<bean:write name="res" property="tester"/>#<bean:write name="i"/>"/><bean:write name="res" property="tester"/></td>
                                      <td align="left"><input type="hidden" name="site" value="<bean:write name="res" property="site"/>#<bean:write name="i"/>"/><bean:write name="res" property="site"/></td>
                                      <td align="left"><input type="hidden" name="program_name" value="<bean:write name="res" property="program_name"/>#<bean:write name="i"/>"/><bean:write name="res" property="program_name"/></td>
                                      <td align="left"><input type="hidden" name="pgm_special_control" value="<bean:write name="res" property="pgm_special_control"/>#<bean:write name="i"/>"/><bean:write name="res" property="pgm_special_control"/></td>
                                      <td align="left"><input type="hidden" name="one_main_pgm_group_version" value="<bean:write name="res" property="one_main_pgm_group_version"/>#<bean:write name="i"/>"/><bean:write name="res" property="one_main_pgm_group_version"/></td>
                                      <td align="left"><input type="hidden" name="one_main_pgm_num" value="<bean:write name="res" property="one_main_pgm_num"/>"/><bean:write name="res" property="one_main_pgm_num"/></td>
                                    </tr>
                                  </logic:iterate>
                                </logic:present>
                              </tbody>
                            </table>
                            <!--搜尋結果-->
                            <table width="95%" border="0" id="table28">
                              <tr>
                                <td>
                                  <input type="button" name="suabc" value="Copy This" class = "button1" onclick="redirectCopy(this.form);"/>
                                  <input type="button" class="button1" value="Select All" onclick="selectAll()" />
									<input type="button" class="button1" value="Clear All" onclick="unselectAll()" />
                                  <input type="button" name="sureset" value="Cancel" class = "button1" onclick="redirectWS(this.form);"/>
                                </td>
                              </tr>
                            </table>
                          </div>
                          <br>
                          </html:form>
                        </td>
                      </tr>
                    </table>
                  </td>
                </tr>
              </table>
              <%@  include file="../../index-down.jsp"%>
            </body>
          </html:html>




