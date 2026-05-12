<!-- /xtrarom/OImaintain/MakePDFTX.jsp -->

<%@page contentType="text/html; charset=Big5"%>
<%@taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<%@ page import="java.util.*"%>
<%@ page import="com.mxic.oiplus.xtrarom.oimaintain.*" %>
<%@ page import="com.mxic.oiplus.oimaintain.ProTestRouteBeanAF" %>
<%@ page import="com.mxic.oiplus.oimaintain.WsTestBean" %>
<%@page import="com.mxic.oiplus.resource.*"%>
<%
//TDSProperties pdfProp = TDSResource.getProperties("TIMPdf");
//String path=pdfProp.getProperty("pdf_dl.dir");
ProTestRouteBeanAF fm = (ProTestRouteBeanAF)request.getAttribute("proTestRouteBeanAF");
String vendor = fm.getVendor();
String vdr = vendor;
String brand = null;
WsTestBean[] vendors = (WsTestBean[]) request.getAttribute("AvailVendor");
if ((vendor == null) || (vendor.equals(""))) {
	vendor = vendors[0].getSite_short_name();
} else {
	for (int i=0; i<vendors.length; i++) {
		if (vendors[i].getSite().equals(vendor)) {
			vendor = vendors[i].getSite_short_name();
			break;
		}
	}
}
if (fm.getBrand().equals("KH"))
	brand = "k";
else
	brand = "";
%>


<html:html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=big5">
    <title>Producing PDF Files</title>
    <link rel="stylesheet" type="text/css" href='<html:rewrite page="/css/css.css"/>'>
    <script src='<html:rewrite page="/js/util.js"/>' language="javascript"></script>
    <script src='<html:rewrite page="/js/areaSelect.js"/>' language="javascript"></script>

    <script type="text/javascript">
    function redirectPDF(tmp){
	tmp.vendor = "";
	tmp.flag.value='mxic';
	tmp.submit();


    }
    function redirectBackToMain(tmp1){

      window.location="<html:rewrite page="/OImaintain/searchActionX.do"/>?sid="+tmp1;
    }

    function redirectVendorPDF(tmp){
	tmp.flag.value='vendor';
	tmp.submit();

    }

    function redirectVendorDiffPDF(tmp){
	tmp.flag.value='vendorcover';
	tmp.submit();

    }

    function redirectVendorDiff(tmp){
	tmp.flag.value='vendorDiff';
	tmp.submit();
    }

    function changeVendor(s){
//         alert("TEST: " + s.selectedIndex);
        var ref = document.getElementById("vendorOI");
        var vdr = s.options[s.selectedIndex].label;

        ref.onclick=function() {
      	    var fileName = '8049<%=brand%>-<bean:write name="proTestRouteBeanAF" property="productbody"/>' + vdr + 'v<bean:write name="proTestRouteBeanAF" property="version"/>_tx.pdf';
      		commonDownloadFile('${pageContext.request.contextPath}', 'TIMPdf', 'pdf_dl.dir', fileName);

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
            <td noWrap height="25" width="85%" class="title4"><font size="4">Producing TX PDF Files</font>
            </td>
          </tr>
          <tr>
            <td height="20" colspan="2"><hr width="100%" color=#B4761B size="1"></td>
          </tr>
        </table>

 <html:form action="/OImaintain/pdfMadeActionX.do">
   <input type="hidden" name="flag"/><!--flag將傳到SaveSubmitBomAction.do用來判斷是要SAVE還是SUBMIT-->
   <input type="hidden" name="productbody" value="<%=fm.getProductbody()%>"/>
   <input type="hidden" name="brand" value="<%=fm.getBrand()%>"/>
   <input type="hidden" name="version" value="<%=fm.getVersion()%>"/>
   <input type="hidden" name="sid" value="<%=fm.getSid()%>"/>
   <input type="hidden" name="package_component" value="<%=fm.getPackage_component()%>"/>
        <div id="myDIV1" align="center" style="border:0;" >
          <table width="95%" border="0" id="table28">
            <tr>
              <td>
                [<a href="#" onclick="commonDownloadFile('${pageContext.request.contextPath}', 'TIMPdf', 'pdf_dl.dir', '8049<%=brand%>-<bean:write name="proTestRouteBeanAF" property="productbody"/>v<bean:write name="proTestRouteBeanAF" property="version"/>_tx.pdf');" >廠內 OI</a>]  
                [<a href="#" onclick="commonDownloadFile('${pageContext.request.contextPath}', 'TIMPdf', 'pdf_dl.dir', '8049<%=brand%>-<bean:write name="proTestRouteBeanAF" property="productbody"/>cv<bean:write name="proTestRouteBeanAF" property="version"/>_tx.pdf');" >廠內 Cover Page</a>]  
                
                               <br><br>
		        
                <select name="vendorname" onchange="changeVendor(this);">
                  <logic:present name="AvailVendor" >
                    <logic:iterate id="result" name="AvailVendor" >
                      <logic:notPresent name="proTestRouteBeanAF" property="vendor">
                        <option value="<bean:write name="result" property="site"/>" label="<bean:write name="result" property="site_short_name"/>" ><bean:write name="result" property="site"/></option>
                      </logic:notPresent>
                      <logic:present name="proTestRouteBeanAF" property="vendor">
                        <logic:equal name="result" property="site" value="<%=vdr%>">
                          <option value="<bean:write name="result" property="site"/>" label="<bean:write name="result" property="site_short_name"/>" SELECTED><bean:write name="result" property="site"/></option>
                        </logic:equal>
                        <logic:notEqual name="result" property="site" value="<%=vdr%>">
                          <option value="<bean:write name="result" property="site"/>" label="<bean:write name="result" property="site_short_name"/>"><bean:write name="result" property="site"/></option>
                        </logic:notEqual>
                      </logic:present>
                    </logic:iterate>
                  </logic:present>
                </select>
                  
                  [<a id="vendorOI" href="#" onclick="commonDownloadFile('${pageContext.request.contextPath}', 'TIMPdf', 'pdf_dl.dir', '8049<%=brand%>-<bean:write name="proTestRouteBeanAF" property="productbody"/><%=vendor%>v<bean:write name="proTestRouteBeanAF" property="version"/>_tx.pdf');">各廠 OI</a>]
                <br><br>
                <input type="button" name="bak1" value="回前一畫面" onclick="history.go(-1);return true;" class="button1" />
              </td>
            </tr>
          </table>
          <!--Data Table-->

    <!--下面的按鈕-->

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
