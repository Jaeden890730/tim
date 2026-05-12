<!-- /xtrarom/OIMaintain/Add_Vendor.jsp -->

<%@page contentType="text/html; charset=Big5"%>
<%@taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<%@ page import="java.util.*"%>

<html:html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=big5">
<title>Add Vendor</title>
<link rel="stylesheet" type="text/css" href='<html:rewrite page="/css/css.css"/>'>
<script src='<html:rewrite page="/js/util.js"/>' language="javascript"></script>
<script src='<html:rewrite page="/js/areaSelect.js"/>' language="javascript"></script>

<script type="text/javascript">
function redirectWS(tmp){
	    var sid = tmp.sidd.value;
        window.location="<html:rewrite page="/OImaintain/wsTestParameterActionX.do"/>?sid="+sid;
      }

function redirectCopy(tmp){

	tmp.submit();

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
                      <td noWrap height="25" width="85%" class="title4"><font size="4">WS Test Parameter Information / Add Vendor</font></td>
                    </tr>
                    <tr>
                      <td height="20" colspan="2">
                        <hr width="100%" color=#B4761B size="1">
                        </td>
                      </tr>
                    </table>
                    <html:form  action="/OImaintain/copyVendorActionX.do">
                    <input type="hidden" name="sidd" value="<bean:write name="proTestRouteBeanAFX" property="sid"/>"/>
			<logic:present name="list" >
                        <logic:iterate id="tmp" name="list" >
			<input type="hidden" name="sid" value="<bean:write name="tmp" property="sid"/>"/>
			<input type="hidden" name="pgm_id" value="<bean:write name="tmp" property="pgm_id"/>"/>
			<input type="hidden" name="product_body" value="<bean:write name="tmp" property="product_body"/>"/>
			<input type="hidden" name="brand" value="<bean:write name="tmp" property="brand"/>"/>
			<input type="hidden" name="version" value="<bean:write name="tmp" property="version"/>"/>
			<input type="hidden" name="mask_option" value="<bean:write name="tmp" property="mask_option"/>"/>
			<input type="hidden" name="test_type" value="<bean:write name="tmp" property="test_type"/>"/>
			<input type="hidden" name="tester" value="<bean:write name="tmp" property="tester"/>"/>
			<input type="hidden" name="site" value="<bean:write name="tmp" property="site"/>"/>
			<input type="hidden" name="program_name" value="<bean:write name="tmp" property="program_name"/>"/>
			<input type="hidden" name="pgm_special_control" value="<bean:write name="tmp" property="pgm_special_control"/>"/>
			<input type="hidden" name="tf_comment" value="<bean:write name="tmp" property="tf_comment"/>"/>
			<input type="hidden" name="hw_configure" value="<bean:write name="tmp" property="hw_configure"/>"/>
			<input type="hidden" name="temperature" value="<bean:write name="tmp" property="temperature"/>"/>
			<input type="hidden" name="one_main_pgm_group_version" value="<bean:write name="tmp" property="one_main_pgm_group_version"/>"/>
			</logic:iterate>
                        </logic:present>
                      <div id="myDIV1" align="center" style="border:0;" >
                        <table width="95%" border="0" id="table28">
                          <tr>
                            <td>
                              <input type="button" name="suabc" class="button1" value="Add This" onclick="redirectCopy(this.form)"/>
                              <input type="button" name="sureset" class="button1" value="Cancel" onclick="redirectWS(this.form);"/>
                            </td>
                          </tr>
                        </table>
                        <table id="table27" cellspacing=1 cellpadding=0 class=table2  >
                          <thead>
                            <tr class="list1" >
                              <td align="left"  colspan="2"><font size="2"><b><bean:write name="proTestRouteBeanAFX" property="productbody"/> / Version <bean:write name="proTestRouteBeanAFX" property="version"/></b></font></td>
                            </tr>
                            <tr class="list1">
                              <td align="left" ><b>Available Vendor</b></td>
                              <td align="left" >
                                <logic:present name="AddVendor" >
                                  <logic:iterate id="result" name="AddVendor" >
                                    <b><input type="checkbox" name="chk1" value="<bean:write name="result" property="site"/>"/><bean:write name="result" property="site"/></b>
                                  </logic:iterate>
                                </logic:present>
                              	<b><font color="#FF0000"><bean:write name="proTestRouteBeanAFX" property="message"/></font></b>
                              </td>
                            </tr>
                          </thead>
                        </table>
                        <table width="95%" border="0" id="table28">
                          <tr>
                            <td>
                              <input type="button" class="button1" name="suabc" value="Add This" onclick="redirectCopy(this.form);"/>
                              <input type="button" class="button1" name="sureset" value="Cancel" onclick="redirectWS(this.form);"/>
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




