<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>

<%@ taglib prefix="mmr" uri="/mmr-tags" %>


<s:set var="checkAll">
    <input type="checkbox" name="check_uncheck" onclick="checkUncheck('check_uncheck_row')" style="margin: 0 0 0 4px" />
</s:set> 
<div id="srchinv_tab">&nbsp;</div>
	<div id="srchinv_res"><mmr:message messageId="label.search.results"/></div>
	<div id="srchinv_results">	
	<s:if test="%{invoices.size()>1}">
	<div id="rslt_stmnt"><br/><s:property value="invoices.size()" /><mmr:message messageId="label.search.results.items"/></div>
	<div id="notify_div">
	<table>
	<tr>
	<td><img src="<s:url value="/mmr/images/DataGrid_headerSeparatorSkin.png" includeContext="true" />" border="0">&nbsp;</td>
	<td><img src="<s:url value="/mmr/images/mail-notification.png" includeContext="true" />" border="0">&nbsp;&nbsp;</td>
	<td><a href="javascript: atleastOneChecked('check_uncheck_row');"><mmr:message messageId="label.send.notification"/></a></td>
	</tr>
	</table>
	</div>
	</s:if>
	<s:elseif test="%{invoices.size()==1}">
	<div id="rslt_stmnt"><br/><s:property value="invoices.size()" /><mmr:message messageId="label.search.results.item"/></div>
	<div id="notify_div">
	<table>
	<tr>
	<td><img src="<s:url value="/mmr/images/DataGrid_headerSeparatorSkin.png" includeContext="true" />" border="0">&nbsp;</td>
	<td><img src="<s:url value="/mmr/images/mail-notification.png" includeContext="true" />" border="0">&nbsp;&nbsp;</td>
	<td><a href="javascript: atleastOneChecked('check_uncheck_row');"><mmr:message messageId="label.send.notification"/></a></td>
	</tr>
	</table>
	</div>
	</s:elseif>
	<s:else>
	<div id="rslt_stmnt"><br/><mmr:message messageId="label.search.results.noitems"/></div>
	</s:else>
	</div>	


<div id="srchinv_result_tbl">
	<display:table id="invoicetable"  name="invoices" export="false" uid="row"  sort="list" requestURI="/admin/invoice.list.action" cellspacing="0" cellpadding="4">

	<display:setProperty name="paging.banner.items_name" value=""></display:setProperty>
	<display:setProperty name="paging.banner.some_items_found" value=""/>
	<display:setProperty name="paging.banner.all_items_found" value=""></display:setProperty>
	<display:setProperty name="paging.banner.placement" value="bottom"></display:setProperty>
	<display:setProperty name="paging.banner.group_size" value="3"></display:setProperty>

	 <s:set name="invoiceId" value="invoices[#attr.row_rowNum-1].invoiceId" />
		
		<display:column headerClass="srchinv_tableTitle2_checkbox" title="${checkAll}" >
  	  	<s:hidden name="selectedInvoices[%{#attr.row_rowNum - 1}].invoiceId" value="%{#attr.row.invoiceId}"/>
  	  	<s:checkbox name="select[%{#attr.row_rowNum - 1}]" value="select[%{#attr.row_rowNum - 1}]" cssClass="check_uncheck_row" />
 		</display:column>
		<display:column style="width: 10%" headerClass="srchinv_tableTitle2" property="invoiceNum"  sortable="true" title="Inv#" />
		<display:column headerClass="srchinv_tableTitle2" property="customer.name"  sortable="true" title="Company" />
		<display:column headerClass="srchinv_tableTitle2" property="dateCreated"  format="{0,date,dd-MMM-yyyy}" sortable="true" title="Date Created" />
		<display:column headerClass="srchinv_tableTitle2_amount" property="invoiceAmount" format="{0,number,currency}"  sortable="true" title="Amount" />
		<s:if test="%{#session.ROLE.contains('busadmin')}">
			<display:column headerClass="srchinv_tableTitle2_cost" property="invoiceCost"  format="{0,number,currency}"  sortable="true" title="Cost" />
		</s:if>
		<display:column headerClass="srchinv_tableTitle2_tax" property="invoiceTax"  format="{0,number,currency}"  sortable="true" title="Tax" />
		<display:column headerClass="srchinv_tableTitle2_tax" property="paymentStatusString" sortable="true" title="Status" />
		<display:column headerClass="srchinv_tableTitle2_print" sortable="true">
			<a href="<s:url value="print.invoice.action?invoiceId=%{#invoiceId}"  />" target="_blank">
				<img src="<s:url value="/mmr/images/pdf.gif" includeContext="true" />" alt="Print Invoice" border="0"> 
			</a>
        </display:column>
        <!-- Column for csv Download feature - Hidden  -->
          
        <display:column headerClass="srchinv_tableTitle2_print" sortable="true" style="display: block;">
			<a href="<s:url value="download.csv.invoice.action?invoiceId=%{#invoiceId}"  />">
				<img src="<s:url value="/mmr/images/csv_icon.png" includeContext="true" />" alt="Download CSV" border="0"> 
			</a>
        </display:column> 
        
        <s:set name="paymentStatus" value="invoices[#attr.row_rowNum-1].getPaymentStatus()" />
		<s:if test="%{(#paymentStatus==10 || #paymentStatus==20) && #session.ROLE.contains('busadmin')}">
			<display:column headerClass="srchinv_tableTitle2_edit" sortable="true">
				<!-- Edit Invoice can be available for Admin and if it is paid or partially paid  -->
					<s:a href="edit.invoice.action?invoiceId=%{#invoiceId}"> 
					<img src="<s:url value="/mmr/images/edit_pencil.png" includeContext="true" />" alt="Edit Invoice" border="0"> </s:a>
			</display:column>
			<display:column headerClass="srchinv_tableTitle2_delete" sortable="true">
	            <s:a onclick="return confirm('Do you want to cancel this invoice?')" href="invoice.cancel.action?invoiceId=%{#invoiceId}"> 
				<img src="<s:url value="/mmr/images/delete.gif" includeContext="true" />" alt="Cancel Invoice" border="0"> 
				</s:a>
			</display:column>
		</s:if>
	</display:table>
</div>

<div id="srchinvoice_res_tbl_end"></div>


