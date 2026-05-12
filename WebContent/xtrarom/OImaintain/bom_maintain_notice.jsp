<!-- /xtrarom/OImaintain/bom_maintain_notice.jsp -->

<%@ page language="java" contentType="text/html; charset=Big5" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %> 

<html:html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=big5">
<title>TIM</title>
<link rel="stylesheet" type="text/css" href='<html:rewrite page="/css/css.css"/>'>
<script src='<html:rewrite page="/js/util.js"/>' language="javascript"></script>
<script src='<html:rewrite page="/js/areaSelect.js"/>' language="javascript"></script>
</head>

<body bgcolor="#F5F5DC" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" >
<div align="center">
        <table width="435" border="0" cellspacing="2" cellpadding="0" align="center">
	<tr><td align="center"><h2>BOM VS Product Route 資料維護<br>注意事項</h2></td></tr>
	<tr><td><ul><li>若FT route name = FP***，其WS route code不能是"NA"</li>
<li>若FT route name = FQ***，其WS route code和WS route name的default值為"NA"</li>
<li>非量產的完整流程(ex,屬於MES for重工單的route, 為正常route的部分流程)不需維護在TIM</li>
<li>KH的WS route code 僅能為B*、Y*、Z*、K*</li>
<li>同一prod body, brand:同一Sort Route Code不可對應到不同的Sort Route+Sort Add. Route。</li>
<li>在某一測試流程中，若在某些條件下需進行加測流程且與不需加測的parts的完成品料號相同時，則應設定為同一Sort Route Code VS Sort Route+Sort Add. Route。</li>
<li>在某一測試流程中，若在某些條件下需進行加測流程且與不需加測的parts的完成品料號不同時，則應設定為不同的Sort Route Code，ex：
<br>10 VS Sort Route: FW21
<br>20 VS Sort Route: FW21 , Sort Add. Route: FW43
</ul>
<tr><td><center><input type="button" value="關閉視窗" onClick="window.close()"></center></td></tr>
</table>
</body>
</html:html>
