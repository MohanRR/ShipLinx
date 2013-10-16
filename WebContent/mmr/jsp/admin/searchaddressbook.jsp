<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="mmr" uri="/mmr-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %> 

<html> 
<head> 
    <sj:head jqueryui="true" />
    <title><s:text name="edi.title"/></title> 
</head> 
<body> 
<script type="text/javascript">

function submitForm()
{
	document.addressform.action="search.address.action";
	document.addressform.submit();
}
</script>
<div id="messages">
	<jsp:include page="../common/action_messages.jsp"/>
</div>
<link rel="stylesheet" type="text/css"
	href="<s:url value='/mmr/styles/common.css' includeContext="true"/>" />
<div class="form-container"> 
<s:form action="search.address" name="addressform">

<div id="srchaddrss_panel">
<table>
<tr>
<td><div id="srchaddrss_crtra"><mmr:message messageId="label.addresses.search"/></div></td>
<td>&nbsp;</td>
</tr>
</table>
</div>

<div id="srchaddrsstactions_imgs">
<table>
<tr>
<td><img src="<s:url value="/mmr/images/DataGrid_headerSeparatorSkin.png" includeContext="true" />" border="0">&nbsp;</td>
<td><img src="<s:url value="/mmr/images/addNew_icon.png" includeContext="true" />" border="0">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
<img src="<s:url value="/mmr/images/DataGrid_headerSeparatorSkin.png" includeContext="true" />" border="0">&nbsp;<img src="<s:url value="/mmr/images/search_icon.png" includeContext="true" />" border="0"></td>
</tr>
</table>
</div>
<div id="srchaddrss_actions">
<table>
<tr>
<td>&nbsp;</td>
<td><a href="new.address.action"><mmr:message messageId="label.search.addnew"/></a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript: submitForm();">Search</a>&nbsp;</td>
</tr>
</table>
</div>

<div id="srchaddrss_table">
<table cellpadding="4" cellspacing="0">
<tr>
<td class="hdr"><mmr:message messageId="label.search.criteria" />:</td>
<td><s:textfield  name="address.abbreviationName" cssClass="text_02_tf"/><!--<mmr:message messageId="label.addressbook.contactName" />: --></td>
<td class="hdr"><!--<mmr:message messageId="label.addressbook.city" />:--></td>
<td class="hdr"><!--<mmr:message messageId="label.addressbook.postalCode"/>:--></td>
</tr>
<tr>
<td> </td>
<td><!--<s:textfield  name="address.contactName" cssClass="text_02_tf"/>--></td>
<td><!--<s:textfield  name="address.city" cssClass="text_02_tf"/>--></td>
<td><!--<s:textfield  name="address.postalCode" cssClass="text_02_tf"/>--></td>

</tr>
</table>
</div>
<!-- 
<div id="srch_addrss" class="text_01">
				<s:url id="searchAddress" value="search.address.action"/>
	            <sj:submit 
	            	id="formSubmit2"	            	
	            	href="%{request.getContextPath()}/%{searchAddress}" 
	            	targets="formResult" 
	            	value="Search" 
	            	indicator="indicator"
	            	button="true"
	            	/>
	        </div>-->

</s:form>
	        
</div>
	<div id="formResult">
	   <s:include value="addressList.jsp"></s:include>
	</div>
</div>

