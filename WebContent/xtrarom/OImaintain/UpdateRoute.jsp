<!-- /xtrarom/OImaintain/UpdateRoute.jsp -->

<%@page contentType="text/html; charset=Big5"%>
<%@taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<%@ page import="java.util.*"%>

<html:html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=big5">
<title>OI Update Steps</title>
<link rel="stylesheet" type="text/css" href='<html:rewrite page="/css/css.css"/>'>
<script src='<html:rewrite page="/js/util.js"/>' language="javascript"></script>
<script src='<html:rewrite page="/js/areaSelect.js"/>' language="javascript"></script>

<script type="text/javascript">
function redirectBackToMain(){
  window.location="/OIplus/OImaintain/maintain_index.jsp";
}

function redirectPro_R_Map(){
  window.location="/OIplus/OImaintain/Product_Route_Map.jsp";
}
</script>


</head>
<body topmargin="0" leftmargin="0">
<%@  include file="../../index-menu.jsp"%>
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
                <td noWrap height="25" width="85%" class="title4"><font size="4">Product VS Test Route Mapping</font></td>

              </tr>
        <tr>

          <td height="20" colspan="2">

            <hr width="100%" color=#B4761B size="1">

          </td>
        </tr>
      </table>
 <form name ="frm2" action="">

        <div id="myDIV1" align="center" style="border:0;" >

          <!--<table width="95%" border="0" id="table28">
          <tr>
            <td>
          <input type="button" name="suabc" value="Add New"/>
          <input type="button" name="Submit1" value="Update" onclick= "redirect();" >
          <input type="submit"  name="Submit2"  value="Delete"  >
          <input type="button" name="ddd" value="Submit" onclick="assign();"  />
          <input type="button" name="suabcv" value="回維護主畫面"/>
          </td>
          </tr>
        </table>-->





          <table id="table27" cellspacing=1 cellpadding=0 class=table2 height="187" >
            <tr class="list1">
              <td align="left">Product</td>
              <td align="left" colspan="4">6615 /MX</td>
            </tr>
            <tr class="list1">
              <td align="left">Route Name</td>
              <td align="left" colspan="4">FW21</td>
            </tr>
			<tr class="title1">

				<td align="left">Step Seq　</td>
				<td align="left" >Step Name　</td>
				<td align="left">Conditions　</td>
				<td align="left">Temperature　</td>
				<td align="left">Remark　</td>
			</tr>
			<tr class="list1">

				<td align="left">01</td>
				<td align="left">UV</td>
				<td align="left"><input type="text" value="40mins"/></td>
                                <td align="left"><input type="text" value="null"/></td>
                                <td align="left"><input type="text" value=""/></td>
			</tr >
			<tr class="list1">
				<td align="left">02</td>
				<td align="left">SORT1</td>
				<td align="left"><input type="text" value=""/></td>
				<td align="left"><input type="text" value="75C"/></td>
				<td align="left"><input type="text" value=""/></td>
			</tr >
			<tr class="list1">
				<td align="left">03</td>
				<td align="left">BAKE</td>
				<td align="left"><input type="text" value="24hrs"/></td>
				<td align="left"><input type="text" value="250C"/></td>
				<td align="left"><input type="text" value=""/></td>
			</tr >
			<tr class="list1">
				<td align="left">04</td>
				<td align="left">SORT</td>
				<td align="left"><input type="text" value=""/></td>
				<td align="left"><input type="text" value="常溫"/></td>
				<td align="left"><input type="text" value=""/></td>
			</tr>
			<tr class="list1" >
				<td align="left">05</td>
				<td align="left">offline ink or inkless</td>
				<td align="left"><input type="text" value=""/></td>
				<td align="left"><input type="text" value="null"/></td>
				<td align="left"><input type="text" value=""/></td>
			</tr>
                        <tr class="list1">
				<td align="left">06</td>
				<td align="left">QC</td>
				<td align="left"><input type="text" value=""/></td>
				<td align="left"><input type="text" value="null"/></td>
				<td align="left"><input type="text" value=""/></td>
			</tr>



			</table>

<p></p>
<p></p>
          <table width="95%" border="0" id="table28">
          <tr>
            <td>
          <input type="button" name="suabc" value="Save" onclick="redirectPro_R_Map();"/>
          <input type="button" name="Submit1" value="回維護主畫面" onclick= "redirectBackToMain();" >

              </td>
          </tr>
        </table>
</div>


<br>

</form>
            　
    </td>
  </tr>
</table>
</td></tr>
</table>
<%@  include file="../../index-down.jsp"%>
</body>
</html:html>


