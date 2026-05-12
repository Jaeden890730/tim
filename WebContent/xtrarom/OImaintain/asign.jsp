<!-- /xtrarom/OIMaintain/asign.jsp -->

<%@page contentType="text/html; charset=Big5"%>
<%@taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<%@page import="java.util.*"%>
<%@page import="javax.servlet.http.HttpSession"%>
<%@page import="com.mxic.oiplus.xtrarom.oimaintain.*"%>
<%@page import="com.mxic.oiplus.au.*"%>
<%@page import="com.mxic.oiplus.resource.TDSResource"%>
<%
//int sid=1;
//String path = TDSResource.getProperties("TIMPdf").getValue("pdf_dl.dir");

int sid = Integer.parseInt(request.getParameter("sid"));
FTTestActionForm rs = FTService.getInfo(sid);
request.setAttribute("list1", rs);
FTTestActionForm[] rs2 = FTService.getPGMInfo(sid);
request.setAttribute("list2", rs2);
FTTestActionForm[] rs3 = FTService.getVendorOIList(sid);
request.setAttribute("list3", rs3);
//User sb=(User)session.getAttribute("user");
boolean flag1=false;
%>
<html:html>
  <script language="JavaScript" type="text/javascript">
  <!--
  function compare(sid,pd_body,brand,version,status){
	window.location="<html:rewrite page="/xtrarom/OImaintain/EditiionCompare.jsp"/>?sid=" +sid +"&pd_body=" + pd_body +"&brand="+ brand +"&version=" + version +"&status=" + status;
  }
  //searchAction.do?sid=<%=sid%>
  function back(sid){
	window.location="<html:rewrite page="/OImaintain/searchActionX.do"/>?sid=" +sid;
  }
/*
  function opennew(pro_b,br,ver){
    window.open("../../pdf_files/" + pro_b+br+"V"+ver+"_TX.pdf");
    document.forms[0].listControl.value = 'reload';
    document.forms[0].submit();
  }

  function opennewDiff(pro_b,br,ver){
	window.open("../../pdf_files/" + pro_b+br+"V"+ver+"Difference"+"_TX.pdf");
	document.forms[0].listControl.value = 'reload';
	document.forms[0].submit();
  }
	*/
  function downloadFile(fileName){
      commonDownloadFile('${pageContext.request.contextPath}', 'TIMPdf', 'pdf_dl.dir', fileName);
  }

  function clr(){
	if(window.confirm("確定要送出嗎？")){
		document.forms[0].submit();
  }
}

//-->

</script>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=big5">

<title>專案管理</title>
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
              <table width="95%" border="0" cellspacing="0" cellpadding="0" align="center">
                <tr>
                  <td width="15%" height="25" class="title2">
                    <img src="../../image/arrow.gif" width="5" height="14" hspace="3" alt="">
                    <font size="4">TIM</font>
                  </td>
                  <td noWrap height="25" width="85%" class="title4">
                    <font size="2">會簽申請</font>
                  </td>
                </tr>
                <tr>
                  <td height="20" colspan="2">
                     <hr width="100%" size="1" class="hr">
                  </td>
                </tr>
              </table>
              <form name="form1" action="<html:rewrite page="/OImaintain/cancellationAsignActionX.do"/>?sid=<%=sid%>&pd_body=<bean:write name="list1" property="pd_body"/>&brand=<bean:write name="list1" property="brand"/>&version=<bean:write name="list1" property="version"/>">
                <input type="hidden" name="sid" value="<%=sid%>">
                <input name="listControl" type="hidden">
           <div id="myDIV1" align="center" style="border:0;">
                <table id="table27" cellspacing=1 cellpadding=0 class=table2>
                  <thead>

                   <tr class="list1">
                      <td height="20" align="left">
                        <font size="2">
                        <b>                            Product :
                                <bean:write name="list1" property="pd_body"/>
                                / Version
                                <bean:write name="list1" property="version"/>
                          </b>
                        </font>
                      </td>
                    </tr>
                  </thead>
                </table>
              </div>
              <br />

               <div id="myDIV1" align="center" style="boder:0;height:310 ">
                <br/>
                <table id="table27" cellspacing=1 cellpadding=0 class=table2>
                  <thead>
                    <tr class="title1">
                      <td align="left" height="20" width="25%">
                            <bean:write name="list1" property="pd_body"/>
                            / 8049 OI Version
                            <bean:write name="list1" property="version"/>
                            PDF
                      </td>
                      <td align="left" height="20">
                        <logic:equal name="list1" property="brand" value="KH">
                          <a href="#" onclick="downloadFile('8049k-<bean:write name="list1" property="pd_body"/>v<bean:write name="list1" property="version"/>_tx.pdf')"><img border="0"  style="" name="image_new" src='<html:rewrite page="/image/pdf.gif"/>' alt=""></a>
                        </logic:equal>
                        <logic:notEqual name="list1" property="brand" value="KH">
                          <a href="#" onclick="downloadFile('8049-<bean:write name="list1" property="pd_body"/>v<bean:write name="list1" property="version"/>_tx.pdf')"><img border="0"  style="" name="image_new" src='<html:rewrite page="/image/pdf.gif"/>' alt=""></a>
                        </logic:notEqual>
                      </td>
                    </tr>
                       <tr class="title1">
                      <td align="left" height="20">
                     與前一版本差異比較PDF
                      </td>
                      <td align="left" height="20">
                          <a href="#" onclick="downloadFile('8049-<bean:write name="list1" property="pd_body"/>cv<bean:write name="list1" property="version"/>_tx.pdf')"><img border="0"  style="" name="image_new" src='<html:rewrite page="/image/pdf.gif"/>' alt=""></a>
                      </td>
                    </tr>
			 <logic:present name="list3">
                      <tr width="90%" align="left" class="title1">
                      <td align="left">Vendor PDF List</td>
                      <td align="left">
                      <logic:iterate id="result3" name="list3">
						  <a href="#" onclick="downloadFile('<bean:write name="result3" property="actual_file"/>.pdf')"><img border="0"  style="" name="image_new" src='<html:rewrite page="/image/pdf.gif"/>' alt="">
							<bean:write name="result3" property="plant_name"/>
						  </a>
                      </logic:iterate>
					  </td>
                      </tr>
			 </logic:present>
                      <tr width="90%" align="left" class="title1">
                      <td align="left">PGM ID Listing</td>
                      <td align="left" >
			 <logic:present name="list2">
                  <logic:iterate id="result2" name="list2">
                     <bean:write name="result2" property="pg_id"/>,
                          </logic:iterate>
             </logic:present>   </td>
                    </tr>
                  </thead>
                </table>
                  <table width="95%" border="0" cellspacing="0" cellpadding="0" align="center">
                <tr>
                  <td height="20" colspan="2">
<% if (flag1==true) { %>
<input type="button" name="clear" value="解除會簽申請狀態" class="button1" onclick="clr();">
 <% } %>

                    <input type="button" name="goback" value="回維護主畫面" class="button1" onclick="back('<%=sid%>');">
                   </td>
                </tr>
              </table>

                <br/>

              </div>

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
