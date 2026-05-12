<!-- /xtrarom/OImaintain/EditionCompare.jsp -->

<%@page contentType="text/html; charset=Big5"%>
<%@taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>

<%@page import="com.mxic.oiplus.xtrarom.oimaintain.*"%>
<%@page import="com.mxic.oiplus.resource.TDSResource"%>
<%

  String pd_body = request.getParameter("pd_body");
  String sid = request.getParameter("sid");
  String brand = request.getParameter("brand");
  String version = request.getParameter("version");
  String status = request.getParameter("status");
  int version_be = Integer.parseInt(version) - 1;
  String productType = OiMaintainService.getProductType(sid);

  String sid2 = OiMaintainService.getPreviousVersionSid(sid);

  //String pd_body = "6615";
  //String sid = "1";
  //String brand = "MX";
  //String version = "99";
  //String status = "P";
  //int version_be = 98;
  WSProductRouteDefinitionForm[] rs3 = EditiionCompareService.CompareWSPDR(sid, sid2, status, null, 1);
  request.setAttribute("list3", rs3);
  FTProductRouteDefinitionForm[] rs5 = EditiionCompareService.CompareFTPDR(sid, sid2, status, null, 1);
  request.setAttribute("list5", rs5);
  FTProductReRouteDefinitionForm[] rs6 = EditiionCompareService.CompareFTPDR_RE(sid, sid2, status, null, 1);
  request.setAttribute("list6", rs6);
//  FTProductRouteDefinitionForm[] rs6 = EditiionCompareService.ComparePBCPDR(sid, sid2, status, null, 1);
//  request.setAttribute("list6", rs6);
  /*TFIMBasicActionForm[] rs7 = EditiionCompareService.CompareBA(sid, sid2, status, 1, 1);
  request.setAttribute("list7", rs7);
  TFIMBasicActionForm[] rs8 = EditiionCompareService.CompareBA(sid2, sid, status, 0, 1);
  request.setAttribute("list8", rs8);*/
  EditiionCompareActionForm[] rs9 = EditiionCompareService.ComparePDR(sid, sid2, status, 1, 1);
  request.setAttribute("list9", rs9);
  EditiionCompareActionForm[] rs10 = EditiionCompareService.ComparePDR(sid2, sid, status, 0, 1);
  request.setAttribute("list10", rs10);
  EditiionCompareActionForm[] rs11 = EditiionCompareService.SearchDOC(sid, status, pd_body, brand, version, "Y");
  request.setAttribute("list11", rs11);
EditiionCompareActionForm[] rs12 = EditiionCompareService.SearchDOC(sid, status, pd_body, brand, version, "T");
  request.setAttribute("list12", rs12);

  boolean flag2 = false;
  boolean flag3 = false;
  boolean flag4 = false;
  boolean flag5 = false;
  boolean flag6 = false;
  boolean flag7 = false;
  if ((rs3 != null) && (rs3.length > 0))
	flag2 = true;
  if ((rs5 != null) && (rs5.length > 0))
	flag3 = true;
  if ((rs6 != null) && (rs6.length > 0))
	flag7 = true;
  /*if (((rs7 != null) && (rs7.length > 0)) || ((rs8 != null) && (rs8.length > 0)))
	flag4 = true;*/
  if (((rs9 != null) && (rs9.length > 0)) || ((rs10 != null) && (rs10.length > 0)))
	flag5 = true;
  if (((rs11 != null) && (rs11.length > 0)) || ((rs12 != null) && (rs12.length > 0)))
	flag6 = true;

%>
<html:html>

   <script language="JavaScript" type="text/javascript">
 <!--



 function back(sid){

window.location="<html:rewrite page="/OImaintain/searchActionX.do"/>?sid=" +sid;

}

//-->

</script>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=big5">
<script type="text/javascript">

        function transfer(){
      location.href = "./FTAdd.jsp";
               //  document.write(location.href);

    }


    function open_new(file_name){
    alert("file_name="+file_name);
window.open("../../" + file_name);
 document.forms[0].submit();

}






              </script><title>Edition Comparison</title>
  <link rel="stylesheet" type="text/css" href='<html:rewrite page="/css/css.css"/>'>
<script src='<html:rewrite page="/js/util.js"/>' language="javascript"></script><script src='<html:rewrite page="/js/areaSelect.js"/>' language="javascript"></script><script type="text/javascript">
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
              <form name="form1" action="<html:rewrite page="/OImaintain/editiionCompareActionX.do"/>?sid=<%=sid%>&pd_body=<%=pd_body%>&brand=<%=brand%>&version=<%=version%>&status=<%=status%>&">
                 <input type="hidden" name="sid" value="<%=sid%>">
                          <input type="hidden" name="pd_body" value="<%=pd_body%>">
                          <input type="hidden" name="brand" value="<%=brand%>">
                          <input type="hidden" name="version" value="<%=version%>">
                             <input type="hidden" name="status" value="<%=status%>">
              <table width="95%" border="0" cellspacing="0" cellpadding="0" align="center">
                <tr>
                  <td width="15%" height="25" class="title2">
                    <img src="../../image/arrow.gif" width="5" height="14" hspace="3" alt="">
                    <font size="4">TIM</font>
                  </td>
                  <td noWrap height="25" width="85%" class="title4">
                    <font size="4">OI 版本差異</font>
                  </td>
                </tr>
                <tr>
                  <td height="20" colspan="2">
                    <hr width="100%" size="1" class="hr">
                  </td>
                </tr>
                <tr>
                  <td height="20" colspan="2">
                    <input type="button" name="回前一畫面" value="回前一畫面" class="button1" onclick="back('<%=sid%>');">

                  </td>
                </tr>

              </table>
              <div id="myDIV1" align="center" style="border:0;">
                <table id="table27" cellspacing=1 cellpadding=0 class=table2>
                  <thead>
                    <tr class="list1">
                      <td height="20" align="left">
                        <font size="2">
                          <b><%=pd_body %>                            /
<%=brand %>                            OI 資料第
<%=version%>                            版本 VS 第
<%=version_be%>                            版本差異
</b>
                        </font>
                      </td>
                    </tr>
                    <tr class="list1">
                      <td height="20" align="left">
                        <font size="1">
                          <b>
                          <%if (flag5 == true) {                          %>
                            Product VS Test Route Mapping ||
                          <%}                          %>
                          <%if (flag2 == true) {                          %>
                            WS Product Route Definition ||
                          <%}                          %>
                          <%if (flag3 == true) {                          %>
                            FT Product Route Definition ||
                          <%}                          %>
                          <%if (flag7 == true) {                          %>
                            FT Product Rework Route Definition ||
                          <%}                          %>
                          <%if (flag4 == true) {                          %>
                            Basic Information Definition ||
                          <%}                          %>
                          <%if (flag6 == true) {                          %>
                            Document Linkage
                          <%}                          %>
                          </b>
                        </font>
                      </td>
                    </tr>
                  </thead>
                </table>
              </div>
              <br />
              <div id="myDIV1" align="center" style="border:0;">
                <table id="table27" cellspacing=1 cellpadding=0 class=table2>
                  <thead> <tr >Product VS Test Route Mapping </tr>
                    <tr class="title1">
                      <td height="20" width="16%">Route</td>
                      <td height="20" width="16%">Step Seq</td>
                      <td height="20" width="16%">Step Name</td>
                      <td height="20" width="20%">Conditions</td>
                      <td height="20" width="16%">Temperature</td>
                      <td height="20" width="16%">Remark</td>
                    </tr>
                  </thead>

                  <logic:present name="list10">
                    <logic:iterate id="result10" name="list10">
                      <tr bgcolor="#3366FF">
                        <td height="20">
                          <bean:write name="result10" property="route_name"/>
                        </td>
                        <td height="20">
                          <bean:write name="result10" property="step_seq"/>
                        </td>
                        <td height="20">
                          <bean:write name="result10" property="step_name"/>
                        </td>
                        <td height="20">
                          <bean:write name="result10" property="test_time"/>
                          <bean:write name="result10" property="time_unit"/>
                        </td>
                        <td height="20">
                          <bean:write name="result10" property="temperature"/>
                        </td>
                        <td height="20">
                          <bean:write name="result10" property="remark"/>
                        </td>
                      </tr>
                    </logic:iterate>
                  </logic:present>
 				  <logic:present name="list9">
                    <logic:iterate id="result9" name="list9">
                      <tr bgcolor="#FF99FF">
                        <td height="20">
                          <bean:write name="result9" property="route_name"/>
                        </td>
                        <td height="20">
                          <bean:write name="result9" property="step_seq"/>
                        </td>
                        <td height="20">
                          <bean:write name="result9" property="step_name"/>
                        </td>
                        <td height="20">
                          <bean:write name="result9" property="test_time"/>
                          <bean:write name="result9" property="time_unit"/>
                        </td>
                        <td height="20">
                          <bean:write name="result9" property="temperature"/>
                        </td>
                        <td height="20">
                          <bean:write name="result9" property="remark"/>
                        </td>
                      </tr>
                    </logic:iterate>
                  </logic:present>
                </table>
                <br/>
                  <table id="table27" cellspacing=1 cellpadding=0 class=table2>
                    <thead><tr >WS Product Route Definition </tr>
                      <tr class="title1">
                        <td height="20" width="10%">Product Body</td>
                        <td height="20" width="10%">Body Version</td>
                        <td height="20" width="10%">Mask Opt.</td>
                        <td height="20" width="10%">Mask Opt. Rev.</td>
                        <td height="20" width="10%">Code No.</td>
                        <td height="20" width="10%">Sort Route Code</td>
                        <td height="20" width="5%">WS Route</td>
                        <td height="20" width="5%">Test Mode</td>
                        <td height="20" width="5%">Tester</td>
                        <td height="20" width="5%">Site</td>
                        <td height="20" width="10%">PGM Name</td>
                        <td height="20" width="10%">Temperature</td>
                        <td height="20" width="10%">HW Configure</td>
                        <td height="20" width="10%">Notes</td>
                        <td height="20" width="10%">WS Notes</td>
                        
                      </tr>
                    </thead>
                    <logic:present name="list3">
                      <logic:iterate id="result3" name="list3">
                        <logic:equal name="result3" property="tag" value="NEW">
                          <tr bgcolor="#FF99FF">
                        </logic:equal>
                        <logic:notEqual name="result3" property="tag" value="NEW">
	                      <tr bgcolor="#3366FF">
                        </logic:notEqual>
                          <td height="20">
                            <bean:write name="result3" property="productBody"/>
                          </td>
                          <td height="20">
                            <bean:write name="result3" property="bodyVersion"/>
                          </td>
                          <td height="20">
                            <bean:write name="result3" property="maskOption"/>
                          </td>
                          <td height="20">
                            <bean:write name="result3" property="maskOptionRev"/>
                          </td>
                          <td height="20">
                            <bean:write name="result3" property="codeNo"/>
                          </td>
                          <td height="20">
                            <bean:write name="result3" property="sortRouteCode"/>
                          </td>
                          <td height="20">
                            <bean:write name="result3" property="wsRoute"/>
                          </td>
                          <td height="20">
                            <bean:write name="result3" property="testMode"/>
                          </td>
                          <td height="20">
                            <bean:write name="result3" property="tester"/>
                          </td>
                          <td height="20">
                            <bean:write name="result3" property="site"/>
                          </td>
                          <td height="20">
                            <bean:write name="result3" property="programName"/>
                          </td>
                          <td height="20">
                            <bean:write name="result3" property="temperature"/>
                          </td>
                          
                          <td height="20">
                            <c:forTokens items="${result3.hw_configure}" delims=";" var ="item">
		                       <c:out value="${item}"/><BR>
		                    </c:forTokens>
                            <!--<bean:write name="result5" property="s_Grade"/>-->
                          </td>
                          <td height="20">
                            <bean:write name="result3" property="tfComment"/>
                          </td>
                          <td height="20">
                            <bean:write name="result3" property="wsComment"/>
                          </td>
                        </tr>
                      </logic:iterate>
                    </logic:present>
                  </table>
                  <br/>
                  <table id="table27" cellspacing=1 cellpadding=0 class=table2>
                    <thead><tr >FT Product Route Definition </tr>
                      <tr class="title1">
                        <td height="20" width="5%">Product Body</td>
                        <td height="20" width="5%">Pkg. Code</td>
                        <td height="20" width="5%">Pkg. Name</td>
                        <td height="20" width="5%">Pin Count</td>
                        <td height="20" width="5%">BE Opt.</td>
                        <td height="20" width="5%">Body Version</td>
                        <td height="20" width="5%">Code No</td>
                        <td height="20" width="10%">Route Type</td>
                        <td height="20" width="5%">FT Route Code</td>
                        <td height="20" width="5%">FT Route</td>
                        <td height="20" width="5%">Test Type</td>
                        <td height="20" width="10%">Tester</td>
                        <td height="20" width="5%">Site</td>
                        <td height="20" width="5%">PGM Name</td>
                        <td height="20" width="5%">I_Grade</td>
                        <td height="20" width="5%">C_Grade</td>
                        <td height="20" width="5%">Body Size</td>
                        <td height="20" width="10%">HW Configure</td>
                        <td height="20" width="10%">Notes</td>
                        <td height="20" width="10%">FT Notes</td>
                        
                      </tr>
                    </thead>
                    <logic:present name="list5">
                      <logic:iterate id="result5" name="list5">
                        <logic:equal name="result5" property="tag" value="NEW">
                          <tr bgcolor="#FF99FF">
                        </logic:equal>
                        <logic:notEqual name="result5" property="tag" value="NEW">
	                      <tr bgcolor="#3366FF">
                        </logic:notEqual>
                          <td height="20">
                            <bean:write name="result5" property="productBody"/>
                          </td>
                          <td height="20">
                            <bean:write name="result5" property="packageCode"/>
                          </td>
                          <td height="20">
                            <bean:write name="result5" property="packageName"/>
                          </td>
                          <td height="20">
                            <bean:write name="result5" property="pinCount"/>
                          </td>
                          <td height="20">
                            <bean:write name="result5" property="backendOption"/>
                          </td>
                          <td height="20">
                            <bean:write name="result5" property="bodyVersion"/>
                          </td>
                          <td height="20">
                            <bean:write name="result5" property="codeNo"/>
                          </td>
                          <td height="20">
                            <bean:write name="result5" property="route_type_x"/>
                          </td>
                          <td height="20">
                            <bean:write name="result5" property="ftRouteCode"/>
                          </td>
                          <td height="20">
                            <bean:write name="result5" property="ftRoute"/>
                          </td>
                          <td height="20">
                            <bean:write name="result5" property="testMode"/>
                          </td>
                          <td height="20">
                            <bean:write name="result5" property="tester"/>
                          </td>
                          <td height="20">
                            <bean:write name="result5" property="site"/>
                          </td>
                          <td height="20">
                            <bean:write name="result5" property="programName"/>
                          </td>
                          <td height="20">
                            <bean:write name="result5" property="i_Grade"/>
                          </td>
                          <td height="20">
                            <bean:write name="result5" property="c_Grade"/>
                          </td>
                          <td height="20">
                            <bean:write name="result5" property="bodySize"/>
                          </td>
                          
                          <td height="20">
                            <c:forTokens items="${result5.hw_configure}" delims=";" var ="item">
		                       <c:out value="${item}"/><BR>
		                    </c:forTokens>
                            <!--<bean:write name="result5" property="s_Grade"/>-->
                          </td>
                          <td height="20">
                            <bean:write name="result5" property="tfComment"/>
                          </td>
                          <td height="20">
                            <bean:write name="result5" property="ftComment"/>
                          </td>
                        </tr>
                      </logic:iterate>
                    </logic:present>
                  </table>
                  <br/>
                  <table id="table27" cellspacing=1 cellpadding=0 class=table2>
                    <thead><tr >FT Product Rework Route Definition </tr>
                      <tr class="title1">
                        <td height="20" width="5%">Product Body</td>
                        <td height="20" width="5%">Pkg. Code</td>
                        <td height="20" width="5%">Pkg. Name</td>
                        <td height="20" width="5%">Pin Count</td>
                        <td height="20" width="5%">BE Opt.</td>
                        <td height="20" width="10%">Body Version</td>
                        <td height="20" width="10%">Mask Opt.</td>
                        <td height="20" width="10%">Mask Opt. Rev.</td>
                        <td height="20" width="10%">Code No.</td>
                        <td height="20" width="10%">Route Type</td>
                        <td height="20" width="10%">Recycle Code</td>
                        <td height="20" width="5%">FT Route Code</td>
                        <td height="20" width="5%">FT Route</td>
                        <td height="20" width="5%">Test Type</td>
                        <td height="20" width="10%">Tester</td>
                        <td height="20" width="5%">Site</td>
                        <td height="20" width="5%">PGM Name</td>
                        <td height="20" width="5%">I_Grade</td>
                        <td height="20" width="5%">C_Grade</td>
                        <td height="20" width="5%">Body Size</td>
                        <td height="20" width="10%">HW Configure</td>
                        <td height="20" width="10%">Notes</td>
                        <td height="20" width="10%">FT Notes</td>
                        
                      </tr>
                    </thead>
                    <logic:present name="list6">
                      <logic:iterate id="result6" name="list6">
                        <logic:equal name="result6" property="tag" value="NEW">
                          <tr bgcolor="#FF99FF">
                        </logic:equal>
                        <logic:notEqual name="result6" property="tag" value="NEW">
                              <tr bgcolor="#3366FF">
                        </logic:notEqual>
                          <td height="20">
                            <bean:write name="result6" property="productBody"/>
                          </td>
                          <td height="20">
                            <bean:write name="result6" property="packageCode"/>
                          </td>
                          <td height="20">
                            <bean:write name="result6" property="packageName"/>
                          </td>
                          <td height="20">
                            <bean:write name="result6" property="pinCount"/>
                          </td>
                          <td height="20">
                            <bean:write name="result6" property="backendOption"/>
                          </td>
                          <td height="20">
                            <bean:write name="result6" property="bodyVersion"/>
                          </td>
                          <td height="20">
                            <bean:write name="result6" property="maskOption"/>
                          </td>
                          <td height="20">
                            <bean:write name="result6" property="maskOptionRev"/>
                          </td>
                          <td height="20">
                            <bean:write name="result6" property="codeNo"/>
                          </td>
                          <td height="20">
                            <bean:write name="result6" property="route_type_x"/>
                          </td>
                          <td height="20">
                            <bean:write name="result6" property="recycleCode"/>
                          </td>
                          <td height="20">
                            <bean:write name="result6" property="ftRouteCode"/>
                          </td>
                          <td height="20">
                            <bean:write name="result6" property="ftRoute"/>
                          </td>
                          <td height="20">
                            <bean:write name="result6" property="testMode"/>
                          </td>
                          <td height="20">
                            <bean:write name="result6" property="tester"/>
                          </td>
                          <td height="20">
                            <bean:write name="result6" property="site"/>
                          </td>
                          <td height="20">
                            <bean:write name="result6" property="programName"/>
                          </td>
                          <td height="20">
                            <bean:write name="result6" property="i_Grade"/>
                          </td>
                          <td height="20">
                            <bean:write name="result6" property="c_Grade"/>
                          </td>
                          <td height="20">
                            <bean:write name="result6" property="bodySize"/>
                          </td>
                          
                          <td height="20">
                            <c:forTokens items="${result6.hw_configure}" delims=";" var ="item">
		                       <c:out value="${item}"/><BR>
		                    </c:forTokens>
                            <!--<bean:write name="result5" property="s_Grade"/>-->
                          </td>
                          <td height="20">
                            <bean:write name="result6" property="tfComment"/>
                          </td>
                          <td height="20">
                            <bean:write name="result6" property="ftComment"/>
                          </td>
                        </tr>
                      </logic:iterate>
                    </logic:present>
                  </table>
                  <br/>
                </div>
                <br/>
                <div id="myDIV1" align="center" style="border:0;">
                  <table id="table27" cellspacing=1 cellpadding=0 class=table2>
                    <tr >Document Linkage </tr>
                    <thead>
                      <logic:present name="list11">
                        <logic:iterate id="result11" name="list11">
                          <tr class="list1">
                            <td width="10%" height="20" align="left">
                              <font size="2">                                Yield Definition  <bean:write name="result11" property="test_flow"/><bean:write name="result11" property="old_show_title"/>
                                <a target="_blank" href='<bean:write name="result11" property="file_old"/>'><img border="0"  style="<bean:write name="result11" property="old_show"/>" name="image" src='<html:rewrite page="/image/gif.gif"/>' alt=""></a>

                                 <bean:write name="result11" property="new_show_title"/>
                                 <a target="_blank" href='<bean:write name="result11" property="file_new"/>'><img border="0"  style="<bean:write name="result11" property="new_show"/>" name="image" src='<html:rewrite page="/image/gif.gif"/>' alt=""></a>

                              </font>
                            </td>
                          </tr>
                        </logic:iterate>
                      </logic:present>
                      <logic:present name="list12">
                        <logic:iterate id="result12" name="list12">
                          <tr class="list1">
                            <td width="10%" height="20" align="left">
                              <font size="2">                                Test flow  <bean:write name="result12" property="test_flow"/><bean:write name="result12" property="old_show_title"/>
                                <a target="_blank" href='<bean:write name="result12" property="file_old"/>'><img border="0"  style="<bean:write name="result12" property="old_show"/>" name="image" src='<html:rewrite page="/image/gif.gif"/>' alt=""></a>

                                 <bean:write name="result12" property="new_show_title"/>
                                 <a target="_blank" href='<bean:write name="result12" property="file_new"/>'><img border="0"  style="<bean:write name="result12" property="new_show"/>" name="image" src='<html:rewrite page="/image/gif.gif"/>' alt=""></a>

                              </font>
                            </td>
                          </tr>
                        </logic:iterate>
                      </logic:present>
                    </thead>
                  </table>
                </div>
                <table width="95%" border="0" cellspacing="0" cellpadding="0" align="center">
                  <tr>
                    <td height="20">
                      <input type="button" name="回前一畫面" value="回前一畫面" class="button1" onclick="back('<%=sid%>');">

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
<%@include file="../../index-down.jsp"%>
</body>
</html:html>
