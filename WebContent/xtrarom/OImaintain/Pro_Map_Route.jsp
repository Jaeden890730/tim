<!-- /xtrarom/OImaintain/Pro_Map_Route.jsp -->

<%@ page contentType="text/html; charset=Big5" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="com.mxic.oiplus.xtrarom.oimaintain.*" %>
<%@ page import="com.mxic.oiplus.au.*" %>
<%
String t = (String) request.getAttribute("flag");
%>
<html:html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=big5">
<link rel="stylesheet" type="text/css" href='<html:rewrite page="/css/css.css"/>'>
<title>Product VS Test Route Mapping</title>
<script src='<html:rewrite page="/js/util.js"/>' language="javascript"></script>
<script src='<html:rewrite page="/js/areaSelect.js"/>' language="javascript"></script>
<script type="text/javascript">

function redirectBackToMain(tmp1){
  window.location="<html:rewrite page="/OImaintain/searchActionX.do"/>?sid="+tmp1;
}

function redirectUpdate(tmp1){
  var tmp2 = getRadioValue('raSel');
  if (checkRadio("raSel","Route")){
    window.location="<html:rewrite page="/OImaintain/editRouteAction.do"/>?sid="+tmp1+"&RN="+tmp2;
  }
}

function redirectDelete(tmp1){
  var tmp2 = getRadioValue('raSel');
  if (checkRadio("raSel","Route")){
    if(window.confirm("確定要刪除此筆資料？")){
      window.location="<html:rewrite page="/OImaintain/delRouteAction.do"/>?sid="+tmp1+"&RN="+tmp2;
    }
  }
}

function redirectAddNew(tmp){
  var pb = "";
  if (tmp.txtPbname != null)
	tmp.txtPbname.value = '';
  window.location="<html:rewrite page="/OImaintain/addNewRouteAction.do"/>?txtPbname="+pb;
}

function redirectCopyFrom(tmp){
  var pb = tmp.txtPbname.value;
  if (pb.length != 4)
    alert('請輸入四碼 Product Body');
  else if (window.confirm("現有 Route 資料將被清除，並以產品 "+pb+" 的資料取代，是否確定執行？"))
    window.location="<html:rewrite page="/OImaintain/addNewRouteActionX.do"/>?txtPbname="+pb;
}

function redirectSubmit(tmp1){
  if(window.confirm("確定送出？")){
    window.location="<html:rewrite page="/OImaintain/submitProductTestAction.do"/>?sid="+tmp1;
  }
}

</script>

</head>
<body topmargin="0" leftmargin="0">
<%@  include file="../../index-menu.jsp"%>
<table width="100%" border=0 class="bg1">
<tr><!--<td valign="top" width="159" class="bg"></td>-->
<td valign="top">
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="bg1" align="center">
  <tr>
    <td width="100%" height="490" valign="top">
      <br>
        <table width="95%" border="0" cellspacing="0" cellpadding="0" align="center">
          <tr>
            <td width="15%" height="25" class="title2">
              <img src="../../image/arrow.gif" width="5" height="14" hspace="3" alt=""><font size="4">TIM</font></td>
              <td noWrap height="25" width="85%" class="title4"><font size="4">Product VS Test Route Mapping</font></td>
            </tr>
            <tr>
              <td height="20" colspan="2"><hr width="100%" color=#B4761B size="1"></td>
              </tr>
            </table>
	<html:form action="/OImaintain/editRouteActionX.do">
  	 <input type="hidden" name="flag"/><!--flag將傳到SaveSubmitBomAction.do用來判斷是要SAVE還是SUBMIT-->
        <div id="myDIV1" align="center">
          <table width="95%" border="0" id="table28">
            <tr>
              <td>
	<% if (t.equals("Show")) { %>
                <input type="button" name="add" value="Add New" class = "button1" onclick="redirectAddNew('<bean:write name="proTestRouteBeanAF" property="sid"/>');"/>
                <input type="button" name="upt" value="Update" class = "button1" onclick="redirectUpdate('<bean:write name="proTestRouteBeanAF" property="sid"/>');" />
                <input type="button" name="del"  value="Delete" class = "button1" onclick="redirectDelete('<bean:write name="proTestRouteBeanAF" property="sid"/>');" />
                <input type="button" name="sub" value="Submit" class = "button1" onclick="redirectSubmit('<bean:write name="proTestRouteBeanAF" property="sid"/>');"  />
	<% } %>
<input type="button" name="bak" value="回維護主畫面" class = "button1" onclick="redirectBackToMain('<bean:write name="proTestRouteBeanAF" property="sid"/>');"/>
     </td>
            <td>
	<% if (t.equals("Show")) { %>Product Body :
                <input type="text" class="text3" name="txtPbname" value="<bean:write name="proTestRouteBeanAF" property="message"/>"/>
                <input type="button" name="submitsearch" value="<- Copy From This" class="button1" onclick="redirectCopyFrom(this.form);" class="button1"/>
	<% } %>
            </td>
            </tr>
          </table>
          <!--Data Table-->
        <table id="table27" cellspacing=1 cellpadding=0 class=table2>
          <thead>
            <tr class="list1">
              <td colspan="10" align="left"><font size="2"><b>Product: <bean:write name="proTestRouteBeanAF" property="productbody"/> / Version <bean:write name="proTestRouteBeanAF" property="version"/></b></font></td>
            </tr>
              <tr class="title1" align="left">
              <td align="center" colspan="7">Step Definition</td>
              <td align="center" colspan="2" class="title6">Rework Definition</td>
              <td align="left" rowspan="2">Remark (此欄位不會顯示於OI PDF 文件中)</td>
            </tr>
            <tr class="title1" align="left">
              <td align="left">#　</td>
              <td align="left">Route　</td>
              <td align="left">Step Seq　</td>
              <td align="left" >Step Name　</td>
              <td align="left">Conditions　</td>
              <td align="left">Temperature　</td>
              <td align="left">抽測Test Mode</td>
              <td align="left" class="title6">Rework Step　</td>
              <td align="left" class="title6">Condition　</td>
            </tr>
          </thead>
      <tbody>
        <logic:present name="TestRoute" >
        <logic:iterate id="result" name="TestRoute">
        <tr class="list1" onMouseOver="overcolor2(this);" onMouseOut="outcolor2(this);">
	<logic:greaterThan name="result" property="count" value="0">
        <td align="left" rowspan="<bean:write name="result" property="count"/>">
        	<input type="radio" name="raSel" value="<bean:write name="result" property="routename"/>"/>
        	<input type="hidden" name="tag" value="<bean:write name="result" property="tag"/>"/>
        </td>
        <td align="left" rowspan="<bean:write name="result" property="count"/>"><bean:write name="result" property="routename"/></td>
	</logic:greaterThan>
        <td align="left"><bean:write name="result" property="stepseq"/></td>
        <td align="left"><bean:write name="result" property="stepname"/></td>
        <td align="left"><bean:write name="result" property="testtime"/> <bean:write name="result" property="timeunit"/></td>
        <td align="left"><bean:write name="result" property="temperature"/></td>
        <td align="left"><bean:write name="result" property="samplingtest"/></td>
        <td align="left"><bean:write name="result" property="reworkstep"/></td>
        <td align="left"><bean:write name="result" property="testtime2"/> <bean:write name="result" property="timeunit2"/></td>
        <td align="left"><bean:write name="result" property="remark"/></td>
        </tr>

        </logic:iterate>
        </logic:present>
      </tbody>
    </table>
    <!--下面的按鈕-->
    <table width="95%" border="0" id="table28">
      <tr>
        <td>
	<% if (t.equals("Show")) { %>
          <input type="button" name="add" value="Add New" class = "button1" onclick="redirectAddNew('<bean:write name="proTestRouteBeanAF" property="sid"/>');"/>
          <input type="button" name="upt" value="Update" class = "button1" onclick="redirectUpdate('<bean:write name="proTestRouteBeanAF" property="sid"/>');" />
          <input type="button" name="del"  value="Delete" class = "button1" onclick="redirectDelete('<bean:write name="proTestRouteBeanAF" property="sid"/>');" />
          <input type="button" name="sub" value="Submit" class = "button1" onclick="redirectSubmit('<bean:write name="proTestRouteBeanAF" property="sid"/>');"  />
	<% } %>
          <input type="button" name="bak" value="回維護主畫面" class = "button1" onclick="redirectBackToMain('<bean:write name="proTestRouteBeanAF" property="sid"/>');"/>
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
