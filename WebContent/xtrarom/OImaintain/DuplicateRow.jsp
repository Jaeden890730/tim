<!-- /xtrarom/OImaintain/DuplicateRow.jsp -->

<%@page contentType="text/html; charset=Big5"%>
<%@taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<%@ page import="java.util.*"%>
<%@ page import="com.mxic.oiplus.xtrarom.oimaintain.*" %>

<%
BomProductRouteBean[] bm = (BomProductRouteBean[]) request.getAttribute("DupRow");
String pro_b=null;
String brand=null;
/*Get Bean裡面的Product Body和Brand*/
pro_b=bm[0].getProductbody();
brand=bm[0].getBrand();
/*將Product_Body和Brand帶入下列2個程式後分別會傳回FW 與 FP ROUTE*/
ProTestRouteBean[] pt=OiMaintainService.RWRoute(pro_b,brand);
ProTestRouteBean[] FTpt=OiMaintainService.RPFTRoute(pro_b,brand);
ProTestRouteBean[] Bodyver=OiMaintainService.Bodyversion(pro_b);//lai-add-20070522
ProTestRouteBean[] Maskoptrev=OiMaintainService.Maskoptionrev(pro_b);//lai-add-20070522
/*將傳回的FW 與 FP ROUTE分別放入2個StringBuffer*/
StringBuffer option = new StringBuffer();
StringBuffer routeaddoption = new StringBuffer();
StringBuffer ftoption=new StringBuffer();
StringBuffer ftrouteaddoption=new StringBuffer();
StringBuffer bodyversion=new StringBuffer();
StringBuffer bodyversionaddoption=new StringBuffer();
StringBuffer maskoptrev=new StringBuffer();
StringBuffer maskoptrevaddoption=new StringBuffer();

  if(pt != null){
    for(int i=0;i<pt.length;i++){
      option.append("<option value='" + pt[i].getRoutename() + "' >");
      option.append(pt[i].getRoutename());
      option.append("</option>\n");
      routeaddoption.append("<option value='" + pt[i].getRoutename() + "' >");
      routeaddoption.append(pt[i].getRoutename());
      routeaddoption.append("</option>\n");
    }
      option.append("<option value='NA'>NA</option>\n");
  }

  if(FTpt != null){
    for(int i=0;i<FTpt.length;i++){
      ftoption.append("<option value='" + FTpt[i].getRoutename() + "' >");
      ftoption.append(FTpt[i].getRoutename());
      ftoption.append("</option>\n");
      ftrouteaddoption.append("<option value='" + FTpt[i].getRoutename() + "' >");
      ftrouteaddoption.append(FTpt[i].getRoutename());
      ftrouteaddoption.append("</option>\n");
    }
      ftoption.append("<option value='NA'>NA</option>\n");
  }

  if(Bodyver != null){
    for(int i=0;i<Bodyver.length;i++){
      bodyversion.append("<option value='" + Bodyver[i].getBodyversion() + "' >");
      bodyversion.append(Bodyver[i].getBodyversion());
      bodyversion.append("</option>\n");
      bodyversionaddoption.append("<option value='" + Bodyver[i].getBodyversion() + "' >");
      bodyversionaddoption.append(Bodyver[i].getBodyversion());
      bodyversionaddoption.append("</option>\n");
    }
      bodyversion.append("<option value='NA'>NA</option>\n");
  }

  if(Maskoptrev != null){
    for(int i=0;i<Maskoptrev.length;i++){
      maskoptrev.append("<option value='" + Maskoptrev[i].getMaskoptrev() + "' >");
      maskoptrev.append(Maskoptrev[i].getMaskoptrev());
      maskoptrev.append("</option>\n");
      maskoptrevaddoption.append("<option value='" + Maskoptrev[i].getMaskoptrev() + "' >");
      maskoptrevaddoption.append(Maskoptrev[i].getMaskoptrev());
      maskoptrevaddoption.append("</option>\n");
    }
      maskoptrev.append("<option value='NA'>NA</option>\n");
  }
%>


<html:html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=big5">
      <title>TIM BOM VS Product Route - Normal Production Maintenance</title>
      <link rel="stylesheet" type="text/css" href='<html:rewrite page="/css/css.css"/>'>
      <script src='<html:rewrite page="/js/util.js"/>' language="javascript"></script>
      <script src='<html:rewrite page="/js/areaSelect.js"/>' language="javascript"></script>

      <script type="text/javascript">
      function redirectToBomRoute(tmpform){
    	var tmp1=tmpform.sid.value;  
        window.location="<html:rewrite page='/OImaintain/bomProductRouteActionX.do?sid="+tmp1+"'/>";
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
                      <td noWrap height="25" width="85%" class="title4"><font size="4">TIM BOM VS Product Route - Normal Production Maintenance / Duplicate Row</font></td>
                    </tr>
                    <tr>
                      <td height="20" colspan="2">
                        <hr width="100%" color=#B4761B size="1">
                        </td>
                      </tr>
                    </table>
                    <html:form  action="/OImaintain/insertBomDupActionX.do">
                      <div id="myDIV1" align="center" style="border:0;" >
                        <table width="95%" border="0" id="table28">
                          <tr>
                            <td>
                              <input type="submit" name="dup1" class = "button1" value="Duplicate"/>
                              <input type="button" name="bak1" class = "button1" value="Cancel" onclick="redirectToBomRoute(this.form);"/>
                            </td>
                          </tr>
                        </table>
                        <table id="table27" cellspacing=1 cellpadding=0 class=table2  >
                          <thead>
                            <tr class="list1">
                              <td colspan="22" align="left"><font size="2"><b>Product: <bean:write name="proTestRouteBeanAFX" property="productbody"/> / Version <bean:write name="proTestRouteBeanAFX" property="version"/> </b></font></td>
                            </tr>
                            <tr class="title1" align="left">
                              <td align="left">Product Body</td>
                              <td align="left">Body ver.</td>
                              <td align="left">Mask Opt.</td>
                              <td align="left">Mask Opt. rev.</td>
                              <td align="left">Code No</td>
                              <td align="left">Pin Count</td>
                              <td align="left">Package</td>
                              <!--<td align="left">Route type</td>-->
                              <td align="left">FT Route Code</td>
                              <td align="left">FT Route</td>
                              <td align="left">FT Add Route</td>
                              <td align="left">FT Add Route1</td>
                              <td align="left">FT Add Route2</td>
                              <td align="left">FT Add Route3</td>
                              <td align="left">FT Add Route4</td>
                              <td align="left">FT Add Route5</td>
                              <td align="left">FT Comment</td>
                              <td align="left">Sort Route Code</td>
                              <td align="left">WS Route</td>
                              <td align="left">WS Add. Route</td>
                              <td align="left">WS Add. Route1</td>
                              <td align="left">WS Add. Route2</td>
                              <td align="left">WS Add. Route3</td>
                              <td align="left">WS Add. Route4</td>
                              <td align="left">WS Comment</td>
                            </tr>
                          </thead>

                        <tbody>
                          <logic:present name="DupRow" >
                            <logic:iterate id="result" name="DupRow" >
                          <tr class="list1">
                            <input type="hidden" name="id" value="<bean:write name="result" property="id"/>"/>
                            <input type="hidden" name="sid" value="<bean:write name="result" property="sid"/>"/>

                            <td align="left"><input type="hidden" name="productbody" value="<bean:write name="result" property="productbody"/>"/><bean:write name="result" property="productbody"/></td>
                            <td>
                            <select name="bodyversion" size="1">
                                      <option value="<bean:write name="result" property="bodyversion"/>"><bean:write name="result" property="bodyversion"/></option>
                                      <option> </option>
                                      <%=bodyversionaddoption.toString()%>
                            </select>
                            </td>
                            <!--<td align="left"><input type="hidden" name="bodyversion" value="<bean:write name="result" property="bodyversion"/>"/><bean:write name="result" property="bodyversion"/></td>-->
                            <td align="left"><input type="hidden" name="maskopt" value="<bean:write name="result" property="maskopt"/>"/><bean:write name="result" property="maskopt"/></td>
                            <td>
                            <select name="maskoptrev" size="1">
                                      <option value="<bean:write name="result" property="maskoptrev"/>"><bean:write name="result" property="maskoptrev"/></option>
                                      <option> </option>
                                      <%=maskoptrevaddoption.toString()%>
                            </select>
                            </td>
                            <!--<td align="left"><input type="hidden" name="maskoptrev" value="<bean:write name="result" property="maskoptrev"/>"/><bean:write name="result" property="maskoptrev"/></td>-->
                            <td align="left"><input type="text" name="codeno" value="<bean:write name="result" property="codeno"/>"/></td>
                            <td align="left"><input type="hidden" name="pincount" value="<bean:write name="result" property="pincount"/>"/><bean:write name="result" property="pincount"/></td>
                            <td align="left"><input type="hidden" name="pkgtype" value="<bean:write name="result" property="pkgtype"/>"/><bean:write name="result" property="pkgtype"/></td>
                            <!--<td align="left"><input type="hidden" name="routetype" value="<bean:write name="result" property="routetype"/>"/><bean:write name="result" property="routetype"/></td>-->
                            <td align="left"><input type="hidden" name="ft_route_code" value="<bean:write name="result" property="ft_route_code"/>"/><bean:write name="result" property="ft_route_code"/></td>

                            <td align="left">
				<select name="ftroute" size="1">
                                <option value="<bean:write name="result" property="ftroute"/>"><bean:write name="result" property="ftroute"/></option>
              			<%=ftoption.toString()%>
              			</select>
                            </td>
                            <td>
                            <select name="ftAddroute" size="1">
              			<option value="<bean:write name="result" property="ftAddroute"/>"><bean:write name="result" property="ftAddroute"/></option>
              			<option> </option>
              			<%=ftrouteaddoption.toString()%>
                            </select>
                            </td>
                            <td>
                            <select name="ftAddroute1" size="1">
                                      <option value="<bean:write name="result" property="ftAddroute1"/>"><bean:write name="result" property="ftAddroute1"/></option>
                                      <option> </option>
                                      <%=ftrouteaddoption.toString()%>
                            </select>
                            </td>
                            <td>
                            <select name="ftAddroute2" size="1">
                                      <option value="<bean:write name="result" property="ftAddroute2"/>"><bean:write name="result" property="ftAddroute2"/></option>
                                      <option> </option>
                                      <%=ftrouteaddoption.toString()%>
                            </select>
                            </td>
                            <td>
                            <select name="ftAddroute3" size="1">
                                      <option value="<bean:write name="result" property="ftAddroute3"/>"><bean:write name="result" property="ftAddroute3"/></option>
                                      <option> </option>
                                      <%=ftrouteaddoption.toString()%>
                            </select>
                            </td>
                            <td>
                            <select name="ftAddroute4" size="1">
                                      <option value="<bean:write name="result" property="ftAddroute4"/>"><bean:write name="result" property="ftAddroute4"/></option>
                                      <option> </option>
                                      <%=ftrouteaddoption.toString()%>
                            </select>
                            </td>
                            <td>
                            <select name="ftAddroute5" size="1">
                                      <option value="<bean:write name="result" property="ftAddroute5"/>"><bean:write name="result" property="ftAddroute5"/></option>
                                      <option> </option>
                                      <%=ftrouteaddoption.toString()%>
                            </select>
                            </td>
                            <td align="left">
                            <input type="hidden" name="txtComment" value="<bean:write name="result" property="ftcomment"/>" size="14"/><bean:write name="result" property="ftcomment"/>
                          </td>

                            <td align="left"><input type="text" size = "6" name="txtRouteCode" value="<bean:write name="result" property="sortroutecode"/>"/></td>
                            <td align="left">
				<select name="wsroute" size="1">
              			<option value="<bean:write name="result" property="wsroute"/>"><bean:write name="result" property="wsroute"/></option>
              			<%=option.toString()%>
              			</select>
				</td>
                            <td align="left">
				<select name="wsaddroute" size="1">
              			<option value="<bean:write name="result" property="wsaddroute"/>"><bean:write name="result" property="wsaddroute"/></option>
              			<option>  </option>
              			<%=routeaddoption.toString()%>
              			</select>
                            </td>
                            <td align="left">
                                <select name="wsaddroute1" size="1">
                                      <option value="<bean:write name="result" property="wsaddroute1"/>"><bean:write name="result" property="wsaddroute1"/></option>
                                      <option>  </option>
                                      <%=routeaddoption.toString()%>
                                      </select>
                            </td>
                            <td align="left">
                                <select name="wsaddroute2" size="1">
                                      <option value="<bean:write name="result" property="wsaddroute2"/>"><bean:write name="result" property="wsaddroute2"/></option>
                                      <option>  </option>
                                      <%=routeaddoption.toString()%>
                                      </select>
                            </td>
                            <td align="left">
                                <select name="wsaddroute3" size="1">
                                      <option value="<bean:write name="result" property="wsaddroute3"/>"><bean:write name="result" property="wsaddroute3"/></option>
                                      <option>  </option>
                                      <%=routeaddoption.toString()%>
                                      </select>
                            </td>
                            <td align="left">
                                <select name="wsaddroute4" size="1">
                                      <option value="<bean:write name="result" property="wsaddroute4"/>"><bean:write name="result" property="wsaddroute4"/></option>
                                      <option>  </option>
                                      <%=routeaddoption.toString()%>
                                      </select>
                            </td>
                            <td align="left">
                              <input type="hidden" name="txtWsComment" value="<bean:write name="result" property="wscomment"/>" size="14"/><bean:write name="result" property="wscomment"/>
                            </td>
                          </tr>
                          </logic:iterate>
                        </logic:present>
                        </tbody>
                      </table>
                      <table width="95%" border="0" id="table28">
                        <tr>
                          <td>
                            <input type="submit" name="dup2" class = "button1" value="Duplicate" />
                            <input type="button" name="bak2" class = "button1" value="Cancel" onclick="redirectToBomRoute(this.form);"/>
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

