<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="mmr" uri="/mmr-tags" %>
<!DOCTYPE html>
<!-- <!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"> 
<html xmlns="http://www.w3.org/1999/xhtml"> -->
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title><s:property value="%{#session.business.systemName}"/></title>
<style type="text/css">
<!--
@import url("styles/shiplinx_login01_styles.css");
-->
</style>
<!--   <link href="<s:url value='/mmr/styles/shiplinx_login01_styles.css' includeContext="true"/>" rel="stylesheet" type="text/css" />-->
<link
	href="<s:url value='/mmr/styles/Shiplinx.css' includeContext="true"/>"
	rel="stylesheet" type="text/css" />
</head>

<body>

<script language="JavaScript"
	src="<s:url value='/mmr/scripts/submitEnter.js' includeContext="true"/>"></script>
<script language="JavaScript"
	src="<s:url value='/mmr/scripts/utils.js' includeContext="true"/>"></script>
<script language="JavaScript"
	src="<s:url value='/mmr/scripts/cookies.js' includeContext="true"/>"></script>
<script language="JavaScript">

function setRememberUserName() {
	if(document.getElementById("username").value.length  > 0) {
		document.getElementById("rememberMe").checked = true;
	}
} 

// use this functions to enable ACEGI rememberme service

function loginSubmitACEGI() {
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

function loginSubmit() {
	if(document.getElementById("rememberMe").checked) {
		save_field(document.getElementById("username"));	
	} else {
		delete_cookie(document.getElementById("username"));
	}
	var theForm = document.getElementById("loginForm");
	theForm.submit();
}

function keyPressSubmit(myfield,e) {
	var keycode;
	if (window.event) keycode = window.event.keyCode;
	else if (e) keycode = e.which;
	else return true;
	
	if (keycode == 13) {
		if(document.getElementById("rememberMe").checked) {
			save_field(document.getElementById("username"));	
		} else {
			delete_cookie(document.getElementById("username"));
		}
	   myfield.form.submit();
	   return false;
    } else {
       return true;
    }
}
</script>

<form action="j_acegi_security_check" method="post" id="loginForm"
	name="loginForm"><s:hidden name="nextAction" />
<div id="wrapper"><!--  	<div id="top">&nbsp;</div>-->
<div id="middle">
<div id="logo">

	<s:if test="%{#session.logoURL!=null}">
		<s:set name="url" value="%{#session.logoURL}" />
		<img src="<s:property value="%{url}"/>" border="0" width="310px" height="82px" includeContext="true"/>
	</s:if> 
	<s:elseif test="%{#session.business!=null}">
		<img
			src="<%=request.getContextPath()%>/mmr/images/<s:property value="%{#session.business.logoFileName}"/>" style="height: 90px; width: 300px;"/>
	</s:elseif> 
	<s:else>
		<img src="<%=request.getContextPath()%>/mmr/images/logo_shiplinx.gif" style="height: 90px; width: 300px;"/>
	</s:else></div>
<hr style="color: #00A53C; height: 1px; width: 500px;" />
<div id="text">
<div id="messages"><jsp:include
	page="../common/action_messages.jsp" /></div>
<br />
<div id="login_bg_hdr">
<div id="icon_lock">
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;LOGIN TO START SHIPPING
</div>
</div>
<br />
<table width="380" border="0" cellspacing="1" cellpadding="8"
	style="background: none repeat scroll 0 0 #F7F7F7;">
	<tr>
		<td colspan="20"
			style="border-bottom: 1px solid #EEEEEE; border-top: 1px solid #FFFFFF;"
			align="center"><font size="1"><mmr:message messageId="note.login.1" /></font></td>
	</tr>
	<tr>
		<td colspan="10"
			style="border-bottom: 1px solid #EEEEEE; border-top: 1px solid #FFFFFF;"
			align="right"><mmr:message messageId="label.prompt.username" />:</td>
		<td colspan="10"
			style="border-bottom: 1px solid #EEEEEE; border-top: 1px solid #FFFFFF;"><s:textfield
			name="j_username" id="username" class="text_01"
			onkeypress="return keyPressSubmitRememberPassword(this,event)" /></td>
	</tr>
	<tr>
		<td colspan="10"
			style="border-bottom: 1px solid #EEEEEE; border-top: 1px solid #FFFFFF;"
			align="right"><mmr:message messageId="label.prompt.password" />:</td>

		<td colspan="10"
			style="border-bottom: 1px solid #EEEEEE; border-top: 1px solid #FFFFFF;"><s:password
			id="password" name="j_password" class="text_01" maxlength="20"
			onkeypress="return keyPressSubmitRememberPassword(this,event)" /></td>
	</tr>
	<tr>
		<td colspan="10"
			style="border-bottom: 1px solid #EEEEEE; border-top: 1px solid #FFFFFF;"
			align="right">New User ?</td>
		<td colspan="10"
			style="border-bottom: 1px solid #EEEEEE; border-top: 1px solid #FFFFFF;"><a
			href="<%=request.getContextPath()%>/signup.action"
			style="cursor: pointer; border: #f7f7f7 solid 1px;" ><img
			src="<%=request.getContextPath()%>/mmr/images/signup_button_grey.png" style="text-decoration: none;" border="0"/></a></td>
	</tr>
	<tr>
		<td colspan="20"
			style="border-bottom: 1px solid #EEEEEE; border-top: 1px solid #FFFFFF;"
			align="center"><s:checkbox name="_acegi_security_remember_me"
			cssClass="text_01" value="checkbox" />&nbsp;&nbsp;<mmr:message
			messageId="label.remember.me" /></td>
	</tr>
	<!--    <tr>
    <td colspan="10" style="border-bottom: 1px solid #EEEEEE;border-top: 1px solid #FFFFFF;">&nbsp;</td>
    </tr>-->
	<tr valign="bottom">
		<td colspan="10"
			style="border-bottom: 1px solid #EEEEEE; border-top: 1px solid #FFFFFF;"
			align="right" valign="middle"><img
			src="<%=request.getContextPath()%>/mmr/images/submit-button2.png"
			alt="Submit" onclick="loginSubmitACEGI()" style="cursor: pointer;" /></td>

		<!--   <td width="24" style="border-bottom: 1px solid #EEEEEE;border-top: 1px solid #FFFFFF;"><img src="<%=request.getContextPath()%>/mmr/images/icon_forgot_pwd.gif" alt="Forgot Password" width="22" height="24" /></td>-->
		<td colspan="10"
			style="border-bottom: 1px solid #EEEEEE; border-top: 1px solid #FFFFFF;"
			align="center" valign="middle">&nbsp;&nbsp;<a
			href="forgotPassword.action">Forgot Password?</a></td>
	</tr>
</table>
<div class="forgot_pwd"><img
	src="<%=request.getContextPath()%>/mmr/images/forgot-password.png" /></div>
</div>
</div>

<!--  <div id="bottom">&nbsp;</div>-->
<div id="copy">Copyright © 2012 | <s:property value="%{#session.business.systemName}"/> | All rights reserved</div>
</div>
</body>
</form>
</html>