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
	function submitform(action)
	{
	 document.updateARForm.action = action;
	 document.updateARForm.submit();
	}
</SCRIPT>
	


<div id="messages">
<jsp:include page="../common/action_messages.jsp"/>
</div>

<div class="form-container"> 
<s:form action="updateAR" name="updateARForm">
<s:token/>
<div id="srch_panel">
<table width="800px">
<tr>
<td><div id="srch_crtra"><mmr:message messageId="menu.admin.updateAR"/><div id="srch_crtra_darkgray">
Select the invoices to update and fill in the appropriate details, then click on "Update"
</div></div></td>
<td>&nbsp;</td>
</tr>
</table>
</div>

<div id="srchinv_actions_imgs_uar">
<table>
<tr>
<td><img src="<s:url value="/mmr/images/DataGrid_headerSeparatorSkin.png" includeContext="true" />" border="0">&nbsp;</td>
<td><img src="<s:url value="/mmr/images/process_ar.png" includeContext="true" />" border="0">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
<td><img src="<s:url value="/mmr/images/DataGrid_headerSeparatorSkin.png" includeContext="true" />" border="0">&nbsp;</td>
<td><img src="<s:url value="/mmr/images/search_icon.png" includeContext="true" />" border="0">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
</tr>
</table>
</div>
<div id="srchinv_actions_uar">
<table>
<tr>
<td><a href="javascript:submitform('processAR.action')"><mmr:message messageId="menu.admin.processAR"/></a> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
<td><a href="javascript:submitform('updateAR.action')"><mmr:message messageId="label.search.btn.search"/></a> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
</tr>
</table>
</div>

<div id="update_ar_srch_table">
<table width="700px">
	            	<tr>
		            	<td class="srchinv_text_03" width="175px"><mmr:message messageId="label.customer.name" />: </td>
		            	<s:url id="customerList" action="listCustomers" />
		            	<td colspan="2"><sx:autocompleter keyName="invoice.customerId" name="searchString" href="%{customerList}" dataFieldName="customerSearchResult" loadOnTextChange="true" loadMinimumCount="3" cssStyle="width:200px;"/></td>
		            	<td width="150px">&nbsp;</td>
		            	<td class="srchinv_text_03" width="175px"><mmr:message messageId="label.invoice.number" />: </td>		            	
		            	<s:url id="invoiceList" action="listInvoices" />
		            	<td colspan="2"><sx:autocompleter keyName="invoice.invoiceId" name="searchStringInvoices" href="%{invoiceList}" dataFieldName="invoiceSearchResult" loadOnTextChange="true" loadMinimumCount="1" cssStyle="width:100px;"/></td>
	            	</tr>
		            	
		            	
		            <!--  	<td class="text_01_updtAR">
			            	<s:submit 
			            	id="formSubmit2"
			            	onclick="javascript:submitform('processAR.action')"
			            	value="Process A/R" 
			            	/>
		            	</td>
		            	<td class="text_01_updtAR">
			            	<s:submit 
			            	id="formSubmit2"
			            	onclick="javascript:submitform('updateAR.action')"
			            	value="Search" 
			            	/>
		            	</td>-->	            	
	            </table>
</div>


        
	<div id="formResult">
	   <s:include value="updateARInvoiceList.jsp"></s:include>
	</div>
</s:form>
</div>
</div>
