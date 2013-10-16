<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="mmr" uri="/mmr-tags" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title><s:property value="%{#session.business.systemName}"/></title>
<style type="text/css">
<!-- @import url("styles/shiplinx_login01_styles.css"); -->
</style>
<link href="<s:url value='/mmr/styles/shiplinx_login01_styles.css' includeContext="true"/>" rel="stylesheet" type="text/css" />
</head>

<body>

<script language="JavaScript" src="<s:url value='/mmr/scripts/submitEnter.js' includeContext="true"/>"></script>
<script language="JavaScript" src="<s:url value='/mmr/scripts/utils.js' includeContext="true"/>"></script>
<script language="JavaScript" src="<s:url value='/mmr/scripts/cookies.js' includeContext="true"/>"></script>
<script language="JavaScript">

function submitForgotPassword() {
	var theForm = document.getElementById("loginForm");
	theForm.submit();
	//	document.forms.loginForm.submit();
}

// use this function to enable ACEGI rememberme service

function keyPressSubmitRememberPassword(myfield,e) {
	var keycode;
	if (window.event) keycode = window.event.keyCode;
	else if (e) keycode = e.which;
	else return true;

	if (keycode == 13) {
	   myfield.form.submit();
	   return false;
    } else {
       return true;
    }
}


//use this functions if just user name will be remembered until checkbox is marked off

</script>

<form action="doForgotPassword.action" method="post" id="loginForm" name="loginForm">
<div id="wrapper">
	<div id="top">&nbsp;</div>
	<div id="middle">
	<div id="logo">
		<s:if test="%{#session.business!=null}">
			<img src="<%=request.getContextPath()%>/mmr/images/<s:property value="%{#session.business.logoFileName}"/>" />
		</s:if>
		<s:else>
			<img src="<%=request.getContextPath()%>/mmr/images/logo_shiplinx.gif"/>
		</s:else>
	</div>
	<div id="text">
	<div id="messages">
		<jsp:include page="../common/action_messages.jsp"/>
	</div>
	<table width="380" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td colspan="10"><strong><mmr:message messageId="label.forgotPassword.enterDetails"/></strong></td>
    </tr>
  <tr>
  <tr>
    <td colspan="10"><mmr:message messageId="label.prompt.username"/>:</td>
    </tr>
  <tr>
    <td colspan="10"><s:textfield name="j_username" id="username" class="text_01" maxlength="50" size="35" onkeypress="return keyPressSubmitRememberPassword(this,event)"/></td>
    </tr>
  <tr>
    <td colspan="10"><mmr:message messageId="label.email"/>:</td>
    </tr>
  <tr>
    <td colspan="10"><s:textfield id="email" name="j_email" class="text_01" maxlength="50" size="35" onkeypress="return keyPressSubmitRememberPassword(this,event)"/></td>
   </tr>

  <tr>
    <td colspan="10"><mmr:message messageId="label.enter.captcha"/>:</td>
    </tr>
  <tr>
  	<tr>
  		<td colspan="10"><input type="text" size="8" name="turing" value=""/></td>
  	</tr>
	<tr>
  	<td><img src="<%=request.getContextPath()%>/jcaptcha" /></td>
  	</tr>
  <tr>
    <td width="68"><input onclick="submitForgotPassword()"  type="submit" name="retrievePassword" value="Reset Password"/>
  </tr>
</table>

	</div>
	</div>
	
	<div id="bottom">&nbsp;</div>
	<div id="copy">Copyright © 2012    |   <s:property value="%{#session.business.systemName}"/>    |    All rights reserved</div>
	</div>

</body>
</form>
</html>
