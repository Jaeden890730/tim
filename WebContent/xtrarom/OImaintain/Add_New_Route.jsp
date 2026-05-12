<!-- /xtrarom/OIMaintain/Add_New_Route.jsp -->

<%@page contentType="text/html; charset=Big5"%>
<%@taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<%@ page import="com.mxic.oiplus.xtrarom.oimaintain.*" %>

<%
TempBean[] arr = OiMaintainService.QueryTemperature();
StringBuffer option = new StringBuffer();
String sid = request.getParameter("sid");
String productType = OiMaintainService.getProductType(sid);

  if(arr != null){
    for(int i=0;i<arr.length;i++){
      option.append("<option value='" + arr[i].getTemp() + "' >");
      option.append(arr[i].getTemp());
      option.append("</option>\n");
    }
  }
%>
<html:html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=big5">
      <title>OI���@</title>
      <link rel="stylesheet" type="text/css" href='<html:rewrite page="/css/css.css"/>'>
      <script src='<html:rewrite page="/js/util.js"/>' language="javascript"></script>
      <script src='<html:rewrite page="/js/areaSelect.js"/>' language="javascript"></script>

      <script type="text/javascript">

        function redirectBackToMain(tmp1){
          window.location="<html:rewrite page="/OImaintain/searchActionX.do"/>?sid="+tmp1;
        }

        function redirectSearchRoute(tmp){
          var RN = tmp.txtRoutename.value;
          var RNall='all';
          var sid=tmp.sid.value;
          if (RN == '%'){
            window.location="<html:rewrite page="/OImaintain/searchRouteActionX.do"/>?txtRoutename="+RNall+"&sid="+sid;
          }else{
            if ((RN.indexOf('%') == -1) && (RN.indexOf('*') == -1))
              window.location="<html:rewrite page="/OImaintain/searchRouteActionX.do"/>?txtRoutename="+RN+"&sid="+sid;
            else
              alert('�п�J���� Route Name');
          }

        }

        function redirectAddNew(tmp){
          window.location="<html:rewrite page="/OImaintain/addNewRouteActionX.do"/>";
        }


      </script>
    </head>
    <body topmargin="0" leftmargin="0">
      <%@include file="../../index-menu.jsp"%>
      <table width="100%" border=0 class="bg1">
        <tr><!--<td valign="top" width="159" class="bg">--></td>
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
                        <!--Form and Table starts from here-->
                        <html:form  action="/OImaintain/insertRouteNameActionX.do">
                          <div id="myDIV1" align="center" style="border:0;" >
                            <table width="95%" border="0" id="table28">
                          <tr>
                            <td>
                              <input type="submit" name="sav" class="button1" value="Save" />
                              <input type="button" name="reset" class="button1" value="Reset" onclick="redirectAddNew();" />
                              <input type="button" name="bak2" class="button1" value="�^���@�D�e��" onclick="redirectBackToMain('<bean:write name="proTestRouteBeanAFX" property="sid"/>');"/>
                            </td>
                          </tr>
                        </table>
                            <table id="table27" cellspacing=1 cellpadding=0 class=table2 height="100" >
                              <thead>
                            <input type="hidden" name="sid" value="<bean:write name="proTestRouteBeanAFX" property="sid"/>"/>
                            <input type="hidden" name="pro_b" value="<bean:write name="proTestRouteBeanAFX" property="productbody"/>"/>
                            <input type="hidden" name="routename" value="<bean:write name="proTestRouteBeanAFX" property="routename"/>"/>
                            <input type="hidden" name="brand" value="<bean:write name="proTestRouteBeanAFX" property="brand"/>"/>
                            <input type="hidden" name="version" value="<bean:write name="proTestRouteBeanAFX" property="version"/>"/>
                            <tr class="list1">
                                <td colspan="7" align="left"><font size="2"><b>Product: <bean:write name="proTestRouteBeanAFX" property="productbody"/> / Version <bean:write name="proTestRouteBeanAFX" property="version"/> </b></font></td>
                            </tr>
                            <tr class="list1">
                              <td align="left"><b>Route Name</b></td>
                              <td colspan="6" align="left">
                                <b><input type="text" class="Text1" name="txtRoutename" <bean:write name="proTestRouteBeanAFX" property="readonly"/>"" value="<bean:write name="proTestRouteBeanAFX" property="routename"/>"/></b>
                                <input type="button" name="submitsearch" value="Get Steps" class="button1" onclick="redirectSearchRoute(this.form);" class="button1"/> &nbsp;&nbsp;�d�ߩҦ� Route �п�J %<br>
				<logic:present name="All">
                                <logic:iterate id="tmp" name="All">
				<bean:write name="tmp" property="routename"/>/
				</logic:iterate>
				</logic:present>
                              </td>
                            </tr>
                            <tr class="title1">
                              <td align="left">Step Seq</td>
                              <td align="left">Step Name�@</td>
                              <td align="left">Conditions�@</td>
                              <td align="left" >Temperature�@</td>
                              <td align="left" >���Test Mode�@</td>
                              <td align="left">Remark�@</td>
                            </tr>
                          </thead>
                            <tbody>
                            <logic:present name="RouteResult">
                              <logic:iterate id="result" name="RouteResult" indexId="i">
                                  <tr class="list1">
                                    <td align="left"><input type="hidden" name="seq" value="<bean:write name="result" property="count"/>"/><bean:write name="result" property="count"/></td>
                                    <td align="left"><input type="hidden" name="stepname" value="<bean:write name="result" property="stepname"/>"/><bean:write name="result" property="stepname"/></td>

                                    <td align="left">
					                  <input type="text" name="txtTime" <bean:write name="result" property="insertvalue"/>/><select name="txtUnit" size="1" <bean:write name="result" property="insertvalue"/>""><option value="HRS">HRS</option><option value="MINS">MINS</option></select>
					                  <input type="hidden" name="timeSeq" <bean:write name="result" property="insertvalue"/> value="<bean:write name="result" property="count"/>"/>
					                </td>
                                    <td align="left">
                                    <% if (productType.equals("NVM")) {
                                    %>
					                  <select name="txtTemp" size="1" <bean:write name="result" property="insertvalue"/>><%=option.toString()%></select>
					                  <input type="hidden" name="tempSeq" <bean:write name="result" property="insertvalue"/> value="<bean:write name="result" property="count"/>"/>
					                <% } else {
                                    %>
					                  <select name="txtTemp" size="1"><%=option.toString()%></select>
					                  <input type="hidden" name="tempSeq" value="<bean:write name="result" property="count"/>"/>
                                    <% }
					                %>
					                </td>
					                <td align="left">
					                  <select name="txtSamplingtest" size="1" <bean:write name="result" property="insertvalue"/>>
					                  <option value=""> </option>
                                      <option value="Y">Y</option>
					                  </select>
					                  <input type="hidden" name="samplingtestSeq" <bean:write name="result" property="insertvalue"/> value="<bean:write name="result" property="count"/>"/>
					                </td>
                                    <td align="left"><input type="text" name="txtRemark"/></td>
                                  </tr>
                              </logic:iterate>
                            </logic:present>
                          </tbody>
                        </table>
                            <table width="95%" border="0" id="table28">
                          <tr>
                            <td>
                              <input type="submit" name="sav" class="button1" value="Save" />
                              <input type="button" name="reset" class="button1" value="Reset" onclick="redirectAddNew();" />
                              <input type="button" name="bak2" class="button1" value="�^���@�D�e��" onclick="redirectBackToMain('<bean:write name="proTestRouteBeanAFX" property="sid"/>');"/>
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


