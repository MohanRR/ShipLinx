<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib prefix="mmr" uri="/mmr-tags" %>
<div id="messages">
<jsp:include page="../common/action_messages.jsp"/>
</div>


<div id="srchinvpaylist_tab">&nbsp;</div>
	<div id="srchinvpaylist_res"><mmr:message messageId="label.search.results"/></div>
	<div id="srchinvpaylist_results">	
	<s:if test="%{invoices.size()>1}">
	<div id="rslt_stmnt"><br/><s:property value="invoices.size()" /><mmr:message messageId="label.search.results.items"/></div>
	</s:if>
	<s:elseif test="%{invoices.size()==1}">
	<div id="rslt_stmnt"><br/><s:property value="invoices.size()" /><mmr:message messageId="label.search.results.item"/></div>
	</s:elseif>
	<s:else>
	<div id="rslt_stmnt"><br/><mmr:message messageId="label.search.results.noitems"/></div>
	</s:else>
	</div>	
	
<div id="srchinvpaylist_result_tbl">
	<display:table id="invoicesTable"  name="invoices" export="false" uid="row"  pagesize="100"   sort="list" cellspacing="0">

		<s:if test="%{#request.postPayment == false}">
			<display:column title="" sortable="true">
		  	  <s:hidden name="selectedInvoices[%{#attr.row_rowNum - 1}].invoiceId" value="%{#attr.row.invoiceId}"/>
		  	  <s:checkbox name="select[%{#attr.row_rowNum - 1}]" value="select[%{#attr.row_rowNum - 1}]" />
		 	</display:column> 	
	 	</s:if>
		<display:column headerClass="tableTitle2" property="invoiceNum"  sortable="true" title="Invoice#" />
		<display:column headerClass="tableTitle2" property="customer.name"  sortable="true" title="Company" />
		<display:column headerClass="tableTitle2" property="dateCreated"  format="{0,date,dd-MMM-yyyy}" sortable="true" title="Date Created" />
		<display:column headerClass="tableTitle2" property="invoiceAmount"  format="{0,number,currency}"  sortable="true" title="Amount" />
		<display:column headerClass="tableTitle2" property="invoiceTax"  format="{0,number,currency}"  sortable="true" title="Tax" />
		<display:column headerClass="tableTitle2" property="totalInvoiceCharge"  format="{0,number,currency}"  sortable="true" title="Total" />
		<display:column headerClass="tableTitle2" property="paidAmount"  format="{0,number,currency}"  sortable="true" title="Paid Amount" />
        <display:column headerClass="tableTitle2" property="balanceDue"  format="{0,number,currency}"  sortable="true" title="Balance Due" />

		<s:if test="%{#request.postPayment == true}">
			<s:if test="%{#attr.row.transaction.status == 30}">
				<display:column headerClass="tableTitle2" title="Payment Result" >
					PROCESSED
				</display:column>
			</s:if>
		</s:if>

		<s:if test="%{#request.postPayment == false}">
			<display:caption>Select invoices to pay</display:caption>
		</s:if>	
		<s:else>
			<display:caption>Payment Results</display:caption>
		</s:else>		
	</display:table>
</div>

<div id="srchinvpaylist_res_tbl_end"></div>


