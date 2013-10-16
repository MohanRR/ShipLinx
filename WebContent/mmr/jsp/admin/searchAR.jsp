<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags"%> 
<%@ taglib prefix="mmr" uri="/mmr-tags" %>
<%@ taglib prefix="sx" uri="/struts-dojo-tags" %>


<html> 
<head> 
    <sj:head jqueryui="true" />
    <sx:head />
    <title><s:text name="edi.title"/></title> 
</head> 
<body> 
<SCRIPT language="JavaScript">
	function submitform()
	{	alert('here');
		var val = 	document.searchARForm.elements['arTransaction.customerId'].value;
		alert(val);
	 document.searchARForm.action = "searchAR.action";
	 document.searchARForm.submit();
	 //return false;
	}

</SCRIPT>

<div id="messages">
<jsp:include page="../common/action_messages.jsp"/>
</div>

<div class="form-container"> 
<s:form action="searchAR" name="searchARForm">

<div id="srch_panel">
<table>
<tr>
<td><div id="srch_crtra_geninv"><mmr:message messageId="menu.admin.searchAR"/></div></td>
<td>&nbsp;</td>
</tr>
</table>
</div>

<div id="srchinv_actions_imgs">
<table>
<tr>
<td><img src="<s:url value="/mmr/images/DataGrid_headerSeparatorSkin.png" includeContext="true" />" border="0">&nbsp;</td>
<td><img src="<s:url value="/mmr/images/search_icon.png" includeContext="true" />" border="0">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
</tr>
</table>
</div>
<div id="srchinv_actions">
<table>
<tr>
<td><a href="javascript: submitform()"><mmr:message messageId="label.search.btn.search"/></a> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
</tr>
</table>
</div>

<div id="srch_ar_table">
<table width="600px">
	            	<tr>
		            	<td class="srchinv_text_03"><mmr:message messageId="label.customer.name" />: </td>
		            	<td class="srchinv_text_03"><mmr:message messageId="label.invoice.number" />: </td>		            	
		            	<td>&nbsp;</td>
	            	<tr>
		            	<s:url id="customerList" action="listCustomers" />
		            	<td>
		            	<sx:autocompleter keyName="arTransaction.customerId" name="searchString" href="%{customerList}" dataFieldName="customerSearchResult" loadOnTextChange="true" loadMinimumCount="3" cssStyle="width:200px;" cssClass="dojoComboBox"/>
		            	</td>
		            	<s:url id="invoiceList" action="listInvoices" />
		            	<td><sx:autocompleter keyName="arTransaction.invoiceId" name="searchStringInvoices" href="%{invoiceList}" dataFieldName="invoiceSearchResult" loadOnTextChange="true" loadMinimumCount="1" cssStyle="width:100px;"/></td>
	            	</tr>
	            </table>
</div>

 	<div id="formResult">
	   <s:include value="arList.jsp"></s:include>
	</div>
</s:form>
</div>
</div>
