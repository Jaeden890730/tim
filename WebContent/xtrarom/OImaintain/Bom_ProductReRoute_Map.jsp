<!-- /xtrarom/OImaintain/Bom_ProductReRoute_map.jsp -->

<%@page contentType="text/html; charset=big5"%>
<%@taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<%@ page import="com.mxic.oiplus.xtrarom.oimaintain.*" %>

<%
String t = (String) request.getAttribute("flag");
String pro_b=(String) request.getAttribute("pro_b");
String brand=(String) request.getAttribute("brand");
String entry=(String) request.getParameter("entry");
if (entry == null) entry = "false";
//String sid=(String) request.getAttribute("sid");

/*將Product_Body和Brand帶入下列2個程式後分別會傳回FW 與 FP ROUTE*/
ProTestReRouteBean[] pt=OiMaintainService.RWReRoute(pro_b,brand);
ProTestReRouteBean[] FTpt=OiMaintainService.RPReRoute(pro_b,brand);
/*將傳回的FW 與 FP ROUTE分別放入2個StringBuffer*/
StringBuffer option = new StringBuffer();
StringBuffer ftoption=new StringBuffer();
StringBuffer routeaddoption = new StringBuffer();
StringBuffer ftrouteaddoption=new StringBuffer();

if (pt != null){
    option.append("<option value='NA'>NA</option>\n");
    for(int i=0;i<pt.length;i++){
        option.append("<option value='" + pt[i].getRoutename() + "' >");
        option.append(pt[i].getRoutename());
        option.append("</option>\n");
        routeaddoption.append("<option value='" + pt[i].getRoutename() + "' >");
        routeaddoption.append(pt[i].getRoutename());
        routeaddoption.append("</option>\n");
    }
}

if (FTpt != null){
    ftoption.append("<option value='NA'>NA</option>\n");
    for(int i=0;i<FTpt.length;i++){
        ftoption.append("<option value='" + FTpt[i].getRoutename() + "' >");
        ftoption.append(FTpt[i].getRoutename());
        ftoption.append("</option>\n");
        ftrouteaddoption.append("<option value='" + FTpt[i].getRoutename() + "' >");
        ftrouteaddoption.append(FTpt[i].getRoutename());
        ftrouteaddoption.append("</option>\n");
    }
}
%>

<html:html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=big5">
<title>TIM BOM VS Product Route - Recycle Test Maintenance</title>
<link rel="stylesheet" type="text/css" href='<html:rewrite page="/css/css.css"/>'>
<link rel="stylesheet" type="text/css" href='<html:rewrite page="/css/filtergrid.css"/>'>
<script src='<html:rewrite page="/js/util.js"/>' language="javascript"></script>
<script src='<html:rewrite page="/js/areaSelect.js"/>' language="javascript"></script>
<script language="javascript" type="text/javascript" src='<html:rewrite page="/js/topmenu.js"/>'></script>
<script language="javascript" type="text/javascript" src='<html:rewrite page="/js/tablefilter.js"/>'></script>
<script language="javascript" type="text/javascript" src='<html:rewrite page="/js/sortabletable.js"/>'></script>

<script type="text/javascript">
function getLineByItem(item){
	return item.parentNode.parentNode.cells[1].innerText;
}
function checkcheck(tmp){
    if(checkdata(tmp)) {
        if(checkConsist(tmp)){
            if(window.confirm("確定要送出嗎？")){
                tmp.submit();
            }
        }
    }
}

function isEmpty(data){
    return ((data == null) || (data.length == 0 || data==' '));
}

function checkConsist(fm){
    var chkObj = fm.id;
    for (var i=0;i<chkObj.length-1;i++){
        for (var k=1;k<chkObj.length;k++){
	    if (fm.bodyversion[i].value==fm.bodyversion[k].value&&fm.maskopt[i].value==fm.maskopt[k].value&&fm.maskoptrev[i].value==fm.maskoptrev[k].value&&fm.codeno[i].value==fm.codeno[k].value&&fm.pincount[i].value==fm.pincount[k].value&&fm.pkgtype[i].value==fm.pkgtype[k].value&&fm.routetype[i].value==fm.routetype[k].value&&fm.recycle_code[i].value==fm.recycle_code[k].value) {
	        if (fm.ftroute[i].value!=fm.ftroute[k].value || fm.ftAddroute[i].value!=fm.ftAddroute[k].value){
		    var beginRow=i+1;
            	    var endRowNum=k+1;
            	    window.alert("第"+getLineByItem(fm.ftroute[i])+"行與第"+getLineByItem(fm.ftroute[k])+"行的 FT Route/Additional Route 必須一致");
             	    return false;
              	}
	        if (fm.txtFtComment[i].value!=fm.txtFtComment[k].value){
		    var beginRow=i+1;
            	    var endRowNum=k+1;
            	    window.alert("第"+getLineByItem(fm.txtFtComment[i])+"行與第"+getLineByItem(fm.txtFtComment[k])+"行的 FT Route Comment 必須一致");
             	    return false;
              	}
            }

        }
    }
    return true;
}

function checkdata(fm){
    var chkObj = fm.id;
    for (var i=0;i<chkObj.length;i++){
        if (isEmpty(fm.ftroute[i].value)){
            window.alert("第 "+getLineByItem(fm.ftroute[i])+"行 FT_ROUTE 未設定");
            return false;
        }
    }
    return true;
}

function redirectDup(tmpform){
    //var tmp=getRadioValue('raSel');
    var tmp=getCheckBox('raSel');
    var tmp1=tmpform.sid.value;
    //if (checkRadio5("raSel","要複製的資料")){
    if (checkCheckBoxNoLimit("raSel","要複製的資料")){
        window.location="<html:rewrite page="/OImaintain/duplicateRowReActionX.do"/>?id="+tmp+"&sid="+tmp1;
    }
}

function redirectExpired(tmpform){
    //var tmp=getRadioValue('raSel');
    var tmp=getCheckBox('raSel');
    var tmp1=tmpform.sid.value;
    //if (checkRadio5("raSel","要 Expire 的資料")){
    if (checkCheckBoxNoLimit("raSel","要 Expire 的資料")){
        window.location="<html:rewrite page="/OImaintain/expireBomReRouteActionX.do"/>?id="+tmp+"&flag=2&sid="+tmp1;
    }
}

function redirectUnExpired(tmpform){
    //var tmp=getRadioValue('raSel');
    var tmp=getCheckBox('raSel');
    var tmp1=tmpform.sid.value;
    //if (checkRadio5("raSel","要 Reset Expire 的資料")){
    if (checkCheckBoxNoLimit("raSel","要 Reset Expire 的資料")){
        window.location="<html:rewrite page="/OImaintain/expireBomReRouteActionX.do"/>?id="+tmp+"&flag=0&sid="+tmp1;
    }
}

function redirectDelete(tmpform){
    //var tmp=getRadioValue('raSel');
    var tmp=getCheckBox('raSel');
    var tmp1=tmpform.sid.value;
    //var tag = getValueByRadio('raSel', 'tag');
    var tag = getValueByCheckbox('raSel', 'tag');
    if (tag != '1') {
        alert("無法刪除由前一版帶入資料");
    	return false;
    }
    //if (checkRadio5("raSel","要刪除的資料")){
    if (checkCheckBoxNoLimit("raSel","要刪除的資料")){
        if(window.confirm("確定要刪除勾選資料嗎？")){
            window.location="<html:rewrite page="/OImaintain/deleteBomReRouteTxActionX.do"/>?id="+tmp+"&sid="+tmp1;
        }
    }
}

function redirectBackToMain(tmp1){
    window.location="<html:rewrite page="/OImaintain/searchActionX.do"/>?sid="+tmp1;
}

function redirectReset(tmp){
    if(window.confirm("確定 Reset 資料嗎？")){
        var tmp1=tmp.sid.value;
        window.location="<html:rewrite page="/OImaintain/resetBomReRouteActionX.do"/>?sid="+tmp1;
    }
}

function redirectToSubmit(tmp){
    if (checkdata(tmp))
	if (checkConsist(tmp)){
        	if(window.confirm("確定要送出嗎？")){
            		tmp.flag.value = 'submit';
            		tmp.submit();
       	 	}
    	}
}

function setButton() {
    //var tag = getValueByRadio('raSel', 'tag');
    var tag = getValueByCheckbox('raSel', 'tag');
    var expbtn1 = document.getElementById('exp1');
    var unexpbtn1 = document.getElementById('rse1');
    var expbtn2 = document.getElementById('exp2');
    var unexpbtn2 = document.getElementById('rse2');
    var delbtn1 = document.getElementById('del1');
    var delbtn2 = document.getElementById('del2');
    var dupbtn1 = document.getElementById('dup1');
    var dupbtn2 = document.getElementById('dup2');

    var checknum = getCheckedNum('raSel');
    if (checknum > 1){
    	dupbtn1.disabled = true;
	    dupbtn2.disabled = true;
    }else{
    	dupbtn1.disabled = false;
	    dupbtn2.disabled = false;
    }
    
    if (tag == '0') {
    	expbtn1.disabled = false;
    	unexpbtn1.disabled = true;
    	expbtn2.disabled = false;
    	unexpbtn2.disabled = true;
    	delbtn1.disabled = true;
    	delbtn2.disabled = true;
    } else if (tag == '1') {
    	expbtn1.disabled = true;
    	unexpbtn1.disabled = true;
    	expbtn2.disabled = true;
    	unexpbtn2.disabled = true;
    	delbtn1.disabled = false;
    	delbtn2.disabled = false;
    } else if (tag == '2') {
    	expbtn1.disabled = true;
    	unexpbtn1.disabled = false;
    	expbtn2.disabled = true;
    	unexpbtn2.disabled = false;
    	delbtn1.disabled = true;
    	delbtn2.disabled = true;
    } else if (tag == '3') {
    	//alert("tag=3");
    	expbtn1.disabled = true;
    	unexpbtn1.disabled = true;
    	expbtn2.disabled = true;
    	unexpbtn2.disabled = true;
    	delbtn1.disabled = true;
    	delbtn2.disabled = true;
    }
}
</script>
</head>

<body topmargin="0" leftmargin="0">
<%@  include file="../../index-menu2.jsp"%>
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
          <td noWrap height="25" width="85%" class="title4">
            <font size="4">TIM BOM VS Product Route - Recycle Test Maintenance</font>
          </td>
        </tr>
        <tr>
          <td height="20" colspan="2"><hr width="100%" size="1" class="hr">
          </td>
        </tr>
      </table>

      <html:form action="/OImaintain/saveSubmitBomReActionX.do">
        <input type="hidden" name="flag"/><!--flag將傳到SaveSubmitBomReActionX.do用來判斷是要SAVE還是SUBMIT-->
        <input type="hidden" name="brand" value="<%=brand%>"/>
        <div id="myDIV1" align="center" style="border:0;">
          <table width="95%" border="0" id="table28">
	   		<% if (t.equals("Show")) { %>
            <tr>
              <td height="20" colspan="2"><font color="#C00000">注意：若需要刪除所有資料，請先按 "Submit" 後，再做刪除動作，否則系統將再自動帶入前一版資料。</font>
              </td>
            </tr>
	   		<% } %>
            <tr>
              <td>
                <% if (t.equals("Show") ) { %>
                <input type="button" id="save" name="save" value="Save" class = "button1" onclick="checkcheck(this.form);"/>
                <input type="button" id="del1" name="del1" value="Delete Row" class = "button1" onclick= "redirectDelete(this.form);"/>
                <input type="button" id="dup1" name="dup1"  value="Duplicate Row" class = "button1" onclick="redirectDup(this.form);"/>
                <input type="button" id="sub1" name="sub1" value="Submit" class = "button1" onclick="redirectToSubmit(this.form);"/>
                <input type="button" id="reset1" name="reset1" value="Reset" class = "button1" onclick="redirectReset(this.form);"/>
                <input type="button" id="exp1" name="exp1" value="set Expired" class = "button1" onclick="redirectExpired(this.form);"   />
                <input type="button" id="rse1" name="rse1" value="reset Expired" class = "button1" onclick="redirectUnExpired(this.form);"   />
                <input type="button" id="reset1" name="reset1" value="Download" class = "button1" onClick="document.forms[document.forms.length - 1].submit();"/>
                <input type="button" id="selall1" name="selall1" value="Select All" class = "button1" onclick="SelectAllCheckBox(this.form);setButton();"/>
                <input type="button" id="cleall1" name="cleall1" value="Clear All" class = "button1" onclick="ClearAllCheckBox(this.form);setButton();"/>
		<%}%>
                <input type="button" name="bak1" value="回維護主畫面" class = "button1" onclick="redirectBackToMain('<bean:write name="proTestReRouteBeanAFX" property="sid"/>');"/>
                <input type="button" name="bak1" value="維護注意事項" class = "button1" onclick="showModalDialog('bom_maintain_notice.jsp','Status:NO;dialogWidth:430px;dialogHeight:300px');"/>
              </td>
            </tr>
          </table>
          <!--Data Table-->
          <table id="table27" cellspacing=1 cellpadding=0 class="table2">
          <thead>
            <tr class="list1">
              <td colspan="18" align="left"colspan="1"><font size = 2><B>Product :<input type="hidden" name="sid" value="<bean:write name="proTestReRouteBeanAFX" property="sid"/>" />
              <bean:write name="proTestReRouteBeanAFX" property="productbody"/> / Version <bean:write name="proTestReRouteBeanAFX" property="version"/></B></font></td>
            </tr>
<TR class=list1>
<TD align=left colSpan=18>
<TABLE id="myTable1" width="95%" border=0>
<THEAD>            
            <tr class="title1" align="left">
              <td align="left" >  </td>
              <td align="left" >Line#</td>
              <td align="left" title="Status">St</td>
              <td align="left" >Body ver.</td>
              <td align="left" >Mask Opt.</td>
              <td align="left" >Mask Opt. rev.</td>
              <td align="left" >Code No</td>
              <td align="left" >Pin Count</td>
              <td align="left" >Pkg Code</td>
              <td align="left" >Route Type</td>
              <td align="left" >Recycle BOM Code</td>
              <td align="left" >FT Route</td>
              <td align="left" >FT Add Route</td>
              <td align="left" >FT Comment</td>
            </tr>
          </thead>
          <tbody>
            <% int i = 1; %>
            <logic:present name="BomProductRoute" >
            <logic:iterate id="result" name="BomProductRoute" >
            <input type="hidden" name="id" value="<bean:write name="result" property="id"/>"/>
	        <input type="hidden" name="tag" value="<bean:write name="result" property="tag"/>"/>
            <tr class="list1">
              <td align="left" bgcolor="<bean:write name="result" property="changecolor"/>"><input type="checkbox" name="raSel" value="<bean:write name="result" property="id"/>" onclick="setButton();"/></td>
              <td align="right" bgcolor="<bean:write name="result" property="changecolor"/>"><%=i++%></td>
 	          <logic:equal name="result" property="tag" value="2">
	            <td align="center" bgcolor="<bean:write name="result" property="changecolor"/>" title="Expired">E</td>
	          </logic:equal>
 	          <logic:notEqual name="result" property="tag" value="2">
	            <td align="center" bgcolor="<bean:write name="result" property="changecolor"/>" title="Processing">P</td>
	          </logic:notEqual>

                <td align="left"  bgcolor="<bean:write name="result" property="changecolor"/>" ><input type="hidden" name="bodyversion" value="<bean:write name="result" property="bodyversion"/>"/><bean:write name="result" property="bodyversion"/></td>
                <td align="left"   bgcolor="<bean:write name="result" property="changecolor"/>"><input type="hidden" name="maskopt" value="<bean:write name="result" property="maskopt"/>"/><bean:write name="result" property="maskopt"/></td>
                <td align="left"   bgcolor="<bean:write name="result" property="changecolor"/>"><input type="hidden" name="maskoptrev" value="<bean:write name="result" property="maskoptrev"/>"/><bean:write name="result" property="maskoptrev"/></td>
                <td align="left" bgcolor="<bean:write name="result" property="changecolor"/>" ><input type="hidden" name="codeno" value="<bean:write name="result" property="codeno"/>"/><bean:write name="result" property="codeno"/></td>
                <td align="right" bgcolor="<bean:write name="result" property="changecolor"/>" ><input type="hidden" name="pincount" value="<bean:write name="result" property="pincount"/>"/><bean:write name="result" property="pincount"/></td>
                <td align="left" bgcolor="<bean:write name="result" property="changecolor"/>" ><input type="hidden" name="pkgtype" value="<bean:write name="result" property="pkgtype"/>"/><bean:write name="result" property="pkgtype"/></td>
                <td align="left"  bgcolor="<bean:write name="result" property="changecolor"/>" >
                      <select name="routetype" size="1">
                         <option value="<bean:write name="result" property="routetype"/>">
							<logic:equal name="result" property="routetype" value="0">erase code</logic:equal>
							<logic:equal name="result" property="routetype" value="1">boot code</logic:equal>
							<logic:equal name="result" property="routetype" value="2">erase code+boot code</logic:equal>
							<logic:equal name="result" property="routetype" value="3">repair</logic:equal>
						 </option>
                              <logic:equal name="result" property="tag" value="1">
                                 <option value='0' >erase code</option>
                                 <option value='1' >boot code</option>
                                 <option value='2' >erase code+boot code</option>
                                 <option value='3' >repair</option>
                              </logic:equal>
                        </select>
                </td>
                <td align="left"  bgcolor="<bean:write name="result" property="changecolor"/>" ><input type="text" name="recycle_code" size = 6 value="<bean:write name="result" property="recycle_code"/>"/></td>

              <td align="left"  bgcolor="<bean:write name="result" property="changecolor"/>">
                <select name="ftroute" size="1">
                  <option value="<bean:write name="result" property="ftroute"/>"><bean:write name="result" property="ftroute"/></option>
 	          <logic:equal name="result" property="tag" value="1">
                  <%=ftoption.toString()%>
 	          </logic:equal>
                </select>
              </td>
              <td align="left"   bgcolor="<bean:write name="result" property="changecolor"/>">
                <select name="ftAddroute" size="1">
                  <option value="<bean:write name="result" property="ftAddroute"/>"><bean:write name="result" property="ftAddroute"/></option>
                  <option> </option>
 	          <logic:equal name="result" property="tag" value="1">
                  <%=ftrouteaddoption.toString()%>
 	          </logic:equal>
                </select>
              </td>
              <td align="left"   bgcolor="<bean:write name="result" property="changecolor"/>">
                <input type="text" name="txtFtComment" value="<bean:write name="result" property="ftcomment"/>"/>
              </td>
            </tr>
            </logic:iterate>
            </logic:present>
</table></TD></TR>             
          </tbody>
          </table>
          <!--下面的按鈕-->
          <table width="95%" border="0" id="table28">
            <tr>
              <td>
                <% if (t.equals("Show") ) { %>
                  <input type="button" id="save" name="save" value="Save" class = "button1" onclick="checkcheck(this.form);" />
                  <input type="button" id="del2" name="del2" value="Delete Row" class = "button1" onclick= "redirectDelete(this.form);" />
                  <input type="button" id="dup2" name="dup2"  value="Duplicate Row" class = "button1" onclick="redirectDup(this.form);"  />
                  <input type="button" id="sub2" name="sub2" value="Submit" class = "button1" onclick="redirectToSubmit(this.form);"/>
                  <input type="button" id="res2" name="res2" value="Reset" class = "button1" onclick="redirectReset(this.form);"   />
                  <input type="button" id="exp2" name="exp2" value="set Expired" class = "button1" onclick="redirectExpired(this.form);"   />
                  <input type="button" id="rse2" name="rse2" value="reset Expired" class = "button1" onclick="redirectUnExpired(this.form);"   />
                  <input type="button" id="selall2" name="selall2" value="Select All" class = "button1" onclick="SelectAllCheckBox(this.form);setButton();"/>
                  <input type="button" id="cleall2" name="cleall2" value="Clear All" class = "button1" onclick="ClearAllCheckBox(this.form);setButton();"/>
                <%}%>
               <input type="button" name="reset1" value="Download" class = "button1" onClick="document.forms[document.forms.length - 1].submit();"/>
               <input type="button" name="bak2" value="回維護主畫面" class = "button1" onclick="redirectBackToMain('<bean:write name="proTestReRouteBeanAFX" property="sid"/>');"/>
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
<%@ include file="../../index-down.jsp"%>

<script type="text/javascript">
if ('<%=entry%>' == 'true')
	showModalDialog('bom_maintain_notice.jsp','Status:NO;dialogWidth:430px;dialogHeight:300px');
</script>

</body>
<html:form action="/common/e8049Conv2Excel.do" target="_self">
  <html:hidden property="reportResult" value=""/>
  <input type="hidden" name="sid" value="<bean:write name="proTestReRouteBeanAFX" property="sid"/>" />
  <input type="hidden" name="type" value="BOM_REROUTE_XROM_TX" />
</html:form>
</html:html>
<script>
      var st1 = new SortableTable(document.getElementById("myTable1"),
	  ["None","Number","String","String","String",
	   "String","String","Number","String","Select",
	   "Text","Select","Select","Text"]);
// IE does not remember input values when moving DOM elements
if (/MSIE/.test(navigator.userAgent)) {

	// backup check box values
	st1.onbeforesort = function () {
		var table = st1.element;
		var inputs = table.getElementsByTagName("INPUT");
		var l = inputs.length;
		for (var i = 0; i < l; i++) {
			if(inputs[i].type=="radio"){
				inputs[i].parentNode.parentNode._checked = inputs[i].checked;
			}
		}
	};

	// restore check box values
	st1.onsort = function () {
		var table = st1.element;
		var inputs = table.getElementsByTagName("INPUT");
		var l = inputs.length;
		for (var i = 0; i < l; i++) {
			if(inputs[i].type=="radio"){
				inputs[i].checked = inputs[i].parentNode.parentNode._checked;
			}
		}
	};

	var props = {
		    filters_row_index: 1,
		    loader: true,
		    loader_html: '<img src="<html:rewrite page="/image/loader.gif"/>" alt="" style="margin: 0pt 5px; vertical-align: middle;"><span>Loading...</span>',
		    status_bar: false,
//		        col_0: "none",
		    enter_key: true
		  };

		      setFilterGrid("myTable1",props);
}
</script>


