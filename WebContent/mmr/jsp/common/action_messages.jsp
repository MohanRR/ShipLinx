<%@ taglib prefix="s" uri="/struts-tags"%>
<s:if test="hasErrors()">
	<div id="errorMsgContainer">
		<div>
			<s:actionerror cssClass="actionErrorStyle" cssStyle="color:#FF0000;background-color: #F8ECE0;"/>
			<strong><s:fielderror cssClass="fieldErrorStyle" /></strong>
		</div>
	</div>
</s:if>
<s:else>
	<div id="errorMsgContainer">
		<div>
			<strong><s:actionmessage cssClass="actionErrorStyle" cssStyle="color:#009900;background-color: #F8ECE0;"/></strong>
		</div>
	</div>
</s:else>
