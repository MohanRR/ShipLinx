<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>

<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="mmr" uri="/mmr-tags" %>

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
	{	
	 document.searchInvoiceForm.action = "salesReport.action";
	 document.searchInvoiceForm.submit();
	 //return false;
	}
	

</SCRIPT>

<div id="messages">
<jsp:include page="../common/action_messages.jsp"/>
</div>

<div class="form-container"> 
<s:form action="invoice.list" name="searchInvoiceForm">

<div id="srch_panel_commission">
<table>
<tr>
<td><div id="srch_crtra"><mmr:message messageId="menu.admin.salesReport"/></div></td>
<td>&nbsp;</td>
</tr>
</table>
</div>

<div id="searchinv_actions_imgs">
<table>
<tr>
<td><img src="<s:url value="/mmr/images/DataGrid_headerSeparatorSkin.png" includeContext="true" />" border="0">&nbsp;</td>
<td><img src="<s:url value="/mmr/images/search_icon.png" includeContext="true" />" border="0">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
</tr>
</table>
</div>
<div id="searchinv_actions">
<table>
<tr>
<td><a href="javascript: submitform()"><mmr:message messageId="label.search.btn.search"/></a> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
</tr>
</table>
</div>

<div id="srch_table_sales">
<table width="750px" style="margin-top:2px;">
	            	<tr>
		            	<td class="srchinv_text_03"><mmr:message messageId="label.calendar.year" />: </td>
		            	<td class="srchinv_text_03"><mmr:message messageId="label.calendar.month" />: </td> 
		            	<td class="srchinv_text_03"><mmr:message messageId="label.currency" />: </td> 
		            	<td class="srchinv_text_03"><mmr:message messageId="label.customer.name" />: </td>  	
	            	</tr>
	            	<tr>
		            	<td><s:select
							list="#{'2012':'2012', '2013':'2013', '2014':'2014', '2015':'2015', '2016':'2016', '2017':'2017', '2018':'2018', '2019':'2019', '2020':'2020'}"
							name="salesRecord.year"
							cssClass="text_01_combo_medium" /></td>
		            	<td> 
			            	<s:select cssClass="text_01_combo_medium" cssStyle="width:123px;" list="months" 
									id="salesAgent" theme="simple" name="salesRecord.monthName" headerKey="0" headerValue="All" />
		            	</td>	
		            	<td><s:select
							list="#{'CAD':'CAD', 'USD':'USD'}"
							name="salesRecord.currency" headerKey="" headerValue=""
							cssClass="text_01_combo_medium" /></td>
	           	     	<td>
		            		<s:url id="customerList" action="listCustomers" />
		            		<sx:autocompleter value="%{invoice.customer.name}" keyName="salesRecord.customerId" name="searchString" href="%{customerList}" dataFieldName="customerSearchResult" loadOnTextChange="true" loadMinimumCount="3"/>	            	
          			    </td>
	            	</tr>
	            </table>
</div>
	
</s:form>
	
</div>
		
<div id="commrprts_tab">&nbsp;</div>
	<div id="srchinv_res"><mmr:message messageId="label.search.results"/></div>
	<div id="srchinv_results">	
	<s:if test="%{salesRecords.size()>1}">
	<div id="rslt_stmnt"><br/><s:property value="salesRecords.size()" /><mmr:message messageId="label.search.results.items"/></div>
	</s:if>
	<s:elseif test="%{salesRecords.size()==1}">
	<div id="rslt_stmnt"><br/><s:property value="salesRecords.size()" /><mmr:message messageId="label.search.results.item"/></div>
	</s:elseif>
	<s:else>
	<div id="rslt_stmnt"><br/><mmr:message messageId="label.search.results.noitems"/></div>
	</s:else>
	</div>	
	
<div id="srchinv_result_tbl">
	<display:table id="salesRecordtable"  name="salesRecords" export="true" uid="row"  varTotals="totals" sort="list" requestURI="/admin/salesReport.action" cellspacing="0" cellpadding="4">
		<display:column style="width: 10%" headerClass="srchinv_tableTitle2" property="year"  sortable="true" title="Year" />
		<display:column style="width: 10%" headerClass="srchinv_tableTitle2" property="monthName"  sortable="true" title="Month" />
		<display:column style="width: 10%" headerClass="srchinv_tableTitle2" property="currency"  sortable="true" title="Currency" />
		<display:column style="width: 10%" headerClass="srchinv_tableTitle2" property="totalCost" total="true" format="{0,number,currency}" sortable="true" title="Total Cost" />
		<display:column style="width: 10%" headerClass="srchinv_tableTitle2" property="totalAmount" total="true" format="{0,number,currency}" sortable="true" title="Total Revenue" />
        <display:footer>
   		 <tr>
   		 	<td colspan="3">Totals:</td>
   	   		<td>
   	   			<fmt:formatNumber type="currency"><s:property value="%{#attr.totals.column4}" /></fmt:formatNumber>
   	   		</td>
   	   		<td>
   	   			<fmt:formatNumber type="currency"><s:property value="%{#attr.totals.column5}" /></fmt:formatNumber>
   	   		</td>
   		 </tr>
  		</display:footer>	
	</display:table>
</div>

<div id="srchinv_res_tbl_end_sales"></div>


