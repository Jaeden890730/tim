<!-- /xtrarom/OImaintain/EditRoute.jsp -->

<%@page contentType="text/html; charset=Big5"%>
<%@taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<%@ page import="java.util.*"%>
<%@ page import="com.mxic.oiplus.xtrarom.oimaintain.*" %>

<%
/******將Temperature與TimeUnit從TABLE撈出放到下拉選單******/
TempBean[] arr = OiMaintainService.QueryTemperature();
//TempBean[] arr2 = OiMaintainService.QueryTimeUnit();
StringBuffer unitoption= new StringBuffer();
StringBuffer option = new StringBuffer();

  if(arr != null){
    for(int i=0;i<arr.length;i++){
      option.append("<option value='" + arr[i].getTemp() + "' >");
      option.append(arr[i].getTemp());
      option.append("</option>\n");
    }
  }


  //if(arr2 != null){
    //for(int i=0;i<arr2.length;i++){
      //if(arr2[i].getTimeunit()==null){

      //}else{
        //unitoption.append("<option value='" + arr2[i].getTimeunit() + "'  >");
        //unitoption.append(arr2[i].getTimeunit());
        //unitoption.append("</option>\n");
        //}
      //}
    //}
%>

<html:html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=big5">
<title>OI維護</title>
<link rel="stylesheet" type="text/css" href='<html:rewrite page="/css/css.css"/>'>
<script src='<html:rewrite page="/js/util.js"/>' language="javascript"></script>
<script src='<html:rewrite page="/js/areaSelect.js"/>' language="javascript"></script>

<script type="text/javascript">
function redirectBackToMain(tmp1){
  window.location="<html:rewrite page="/OImaintain/searchActionX.do"/>?sid="+tmp1;
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
                    <img src="../../image/arrow.gif" width="5" height="14" hspace="3" alt=""><font size="4">TIM</font></td>
                    <td noWrap height="25" width="85%" class="title4"><font size="4">Product VS Test Route List</font></td>
                  </tr>
                  <tr>
                    <td height="20" colspan="2">
                      <hr width="100%" color=#B4761B size="1">
                      </td>
                    </tr>
                  </table>
                  <!--data table and buttons starts from here-->
                  <html:form  action="/OImaintain/updateRouteActionX.do">
                    <div id="myDIV1" align="center" style="border:0;">
                      <table id="table27" cellspacing=1 cellpadding=0 class=table2 height="187" >
                        <thead>
                          <input type="hidden" name="sid" value="<bean:write name="proTestRouteBeanAFX" property="sid"/>"/>
                          <input type="hidden" name="routename" value="<bean:write name="proTestRouteBeanAFX" property="routename"/>"/>
                          <input type="hidden" name="pro_b" value="<bean:write name="proTestRouteBeanAFX" property="productbody"/>"/>
                          <input type="hidden" name="br" value="<bean:write name="proTestRouteBeanAFX" property="brand"/>"/>
                          <tr class="list1">
                            <td colspan="7" align="left"><font size="2"><b>Product: <bean:write name="proTestRouteBeanAFX" property="productbody"/> / Version <bean:write name="proTestRouteBeanAFX" property="version"/> </b></font></td>
                          </tr>
                          <tr class="list1">
                            <td align="left"><b>Route Name</b></td>
                            <td colspan="6" align="left"><b><bean:write name="proTestRouteBeanAFX" property="routename"/></b></td>
                          </tr>
                          <tr class="title1">
                            <td align="left">Step Seq</td>
                            <td align="left">Step Name</td>
                            <td align="left">Conditions</td>
                            <td align="left">Temperature</td>
                            <td align="left">抽測Test Mode　</td>
                            <td align="left">Remark</td>
                          </tr>
                        </thead>

                        <tbody>
                          <logic:present name="EditRoute" >
                            <logic:iterate id="result" name="EditRoute" indexId="i">
                              <tr class="list1">
                                <td align="left"><input type="hidden" name="seq" value="<bean:write name="result" property="stepseq"/>"/><bean:write name="result" property="stepseq"/></td>
                                <td align="left"><bean:write name="result" property="stepname"/></td>
                                <td align="left">
                                  <input type="text" name="txtTesttime" value="<bean:write name="result" property="testtime"/>" <bean:write name="result" property="insertvalue"/>/>
                                  <select name="txtTimeunit" size="1" <bean:write name="result" property="insertvalue"/>>
                                    <option value="<bean:write name="result" property="timeunit"/>"><bean:write name="result" property="timeunit"/> </option>
                                    <option value=""> </option>
                                    <option value="HRS">HRS</option>
                                    <option value="MINS">MINS</option>
                                  </select>
                                  <input type="hidden" name="timeSeq" <bean:write name="result" property="insertvalue"/> value="<bean:write name="result" property="stepseq"/>"/>
                                </td>
                                <td align="left">
                                  <logic:equal name="productType" value="NVM">
                                    <select name="txtTemp" size="1" <bean:write name="result" property="insertvalue"/>>
                                      <option><bean:write name="result" property="temperature"/></option>
                                      <option> </option>
                                        <%=option.toString()%>
                                    </select>
                                    <input type="hidden" name="tempSeq" <bean:write name="result" property="insertvalue"/> value="<bean:write name="result" property="stepseq"/>"/>
                                  </logic:equal>
                                  <logic:notEqual name="productType" value="NVM">
                                    <select name="txtTemp" size="1" >
                                      <option><bean:write name="result" property="temperature"/></option>
                                      <option> </option>
                                        <%=option.toString()%>
                                    </select>
                                    <input type="hidden" name="tempSeq" value="<bean:write name="result" property="stepseq"/>"/>
                                  </logic:notEqual>
                                </td>
                                <td align="left">
                                  <select name="txtSamplingtest" size="1" <bean:write name="result" property="insertvalue_s"/> >
                                    <option value="<bean:write name="result" property="samplingtest"/>"><bean:write name="result" property="samplingtest"/></option>
                                    <option value=""> </option>
                                    <option value="Y">Y</option>
                                  </select>
                                    <input type="hidden" name="samplingtestSeq" <bean:write name="result" property="insertvalue_s"/> value="<bean:write name="result" property="stepseq"/>" />
                                </td>
                                <td align="left"><input type="text" name="txtRemark" value="<bean:write name="result" property="remark"/>"/></td>
                              </tr>
                            </logic:iterate>
                          </logic:present>
                        </tbody>
                      </table>
                      <table width="95%" border="0" id="table28">
                        <tr>
                          <td>
                            <input type="submit" name="sav" class="button1" value="Save" />
                            <input type="button" name="bak2" class="button1" value="回維護主畫面" onclick="redirectBackToMain('<bean:write name="proTestRouteBeanAFX" property="sid"/>');"/>
                          </td>
                        </tr>
                      </table>
                    </div>
                    <br>
                    </html:form>
                      <!--data table and buttons ends here-->
                    </td>
                  </tr>
                </table>
              </td>
            </tr>
          </table>
          <%@  include file="../../index-down.jsp"%>
        </body>
      </html:html>


