<!-- /xtrarom/OImaintain/RouteSearchResult.jsp -->

<%@page contentType="text/html; charset=Big5"%>
<%@taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<%@ page import="java.util.*"%>
<%@ page import="com.mxic.oiplus.xtrarom.oimaintain.*" %>

<%
TempBean[] arr = OiMaintainService.QueryTemperature();
TempBean[] arr2 = OiMaintainService.QueryTimeUnit();
StringBuffer unitoption= new StringBuffer();
StringBuffer option = new StringBuffer();


  if(arr != null){
    for(int i=0;i<arr.length;i++){
      option.append("<option value='" + arr[i].getTemp() + "' >");
      option.append(arr[i].getTemp());
      option.append("</option>\n");
    }
  }

  if(arr2 != null){
    for(int i=0;i<arr2.length;i++){
      if(arr2[i].getTimeunit()==null){
      }else{
        unitoption.append("<option value='" + arr2[i].getTimeunit() + "' >");
        unitoption.append(arr2[i].getTimeunit());
        unitoption.append("</option>\n");
      }
    }
  }



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
  window.location="<html:rewrite page="/OImaintain/searchAction.do"/>?sid="+tmp1;
}

function redirectSearchRoute(tmp){
  var RN = tmp.txtRoutename.value;
  window.location="<html:rewrite page="/OImaintain/searchRouteAction.do"/>?txtRoutename="+RN;
}

function redirectAddNew(){
  window.location="<html:rewrite page="/OImaintain/Add_New_Route.jsp"/>";
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

                    <html:form action="/OImaintain/insertRouteNameAction.do">
                      <div id="myDIV1" align="center" style="border:0;" >
                        <table width="95%" border="0" id="table28">
                          <tr>
                            <td>
                              <input type="submit" name="sav" value="Save" />
                              <input type="button" name="reset" value="Reset" onclick="redirectAddNew();" />
                              <input type="button" name="bak2" value="回維護主畫面" onclick="redirectBackToMain('<bean:write name="proTestRouteBeanAF" property="sid"/>');"/>
                            </td>
                          </tr>
                        </table>

                        <table id="table27" cellspacing=1 cellpadding=0 class=table2 height="100" >
                          <thead>
                            <input type="hidden" name="sid" value="<bean:write name="proTestRouteBeanAF" property="sid"/>"/>
                            <input type="hidden" name="pro_b" value="<bean:write name="proTestRouteBeanAF" property="productbody"/>"/>
                            <input type="hidden" name="routename" value="<bean:write name="proTestRouteBeanAF" property="routename"/>"/>
                            <input type="hidden" name="brand" value="<bean:write name="proTestRouteBeanAF" property="brand"/>"/>
                            <input type="hidden" name="version" value="<bean:write name="proTestRouteBeanAF" property="version"/>"/>
                            <tr class="list1">
                              <td align="left"><b>Product</b></td>
                              <td colspan="7" align="left"><b><bean:write name="proTestRouteBeanAF" property="productbody"/> / <bean:write name="proTestRouteBeanAF" property="brand"/></b></td>
                            </tr>
                            <tr class="list1">
                              <td align="left"><b>Route Name</b></td>
                              <td colspan="7" align="left">
                                <b><input type="text" name="txtRoutename" <bean:write name="proTestRouteBeanAF" property="readonly"/>"" value="<bean:write name="proTestRouteBeanAF" property="routename"/>"/></b>
                                <input type="button" name="submitsearch" value="Get Steps" class="button1" onclick="redirectSearchRoute(this.form);"/>
                                <font color="red"><b><bean:write name="proTestRouteBeanAF" property="message"/></b></font>
                              </td>
                            </tr>
                            <tr class="title1">
                              <td align="left">Step Seq</td>
                              <td align="left">Step Name　</td>
                              <td align="left">Conditions　</td>
                              <td align="left" >Temperature　</td>
                              <td align="left" >抽測Test Mode</td>
                              <td align="left">Remark　</td>
                            </tr>
                          </thead>

                          <tbody>
                            <logic:present name="RouteResult">
                              <logic:iterate id="result" name="RouteResult">
                                <logic:notEqual  name="result" property="stepname" value="NULL">
                                  <tr class="list1">
                                    <td align="left"><input type="hidden" name="seq" value="<bean:write name="result" property="count"/>"/><bean:write name="result" property="count"/></td>
                                    <td align="left"><input type="hidden" name="stepname" value="<bean:write name="result" property="stepname"/>"/><bean:write name="result" property="stepname"/></td>
                                    <td align="left"><input type="text" name="txtTime"/><select name="txtUnit" size="1"><%=unitoption.toString()%></select></td>
                                    <td align="left"><select name="txtTemp" size="1"><%=option.toString()%></select></td>
                                    <td align="left"><select name="txtSamplingtest" size="1">
                                                        <option value=""> </option>
                                                        <option value="Y">Y</option>
                                                     </select></td>
                                    <td align="left"><input type="text" name="txtRemark"/></td>
                                  </tr>
                                </logic:notEqual>
                              </logic:iterate>
                            </logic:present>
                          </tbody>
                        </table>
                        <table width="95%" border="0" id="table28">
                          <tr>
                            <td>
                              <input type="submit" name="sav" value="Save" />
                              <input type="button" name="reset" value="Reset" onclick="redirectAddNew();" />
                              <input type="button" name="bak2" value="回維護主畫面" onclick="redirectBackToMain('<bean:write name="proTestRouteBeanAF" property="sid"/>');"/>
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


