<!-- /xtrarom/OImaintain/search.jsp -->

<%@ page contentType="text/html; charset=Big5" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-template.tld" prefix="template" %>
<%@ page import="java.util.*"%>
<%@ page import="com.mxic.oiplus.xtrarom.oimaintain.*"%>
<html>
<head>
<title>
search
</title>
</head>
<body bgcolor="#ffffff">
<h1>
Search for Product Body
</h1>

<form name="frm1" method="post" action="/OIplus/OImaintain/searchAction.do">
<input type="text" name="pd"/>
<input type="radio" name="br" value="MX"/>MX
<input type="radio" name="br" value="KH"/>KH
<input type="submit" name="abc" value="Submit">
</form>
</body>
</html>
