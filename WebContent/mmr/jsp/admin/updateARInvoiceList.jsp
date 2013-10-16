<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags"%> 
<%@ taglib prefix="mmr" uri="/mmr-tags" %>
<%@ taglib prefix="sx" uri="/struts-dojo-tags" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!--   <div id="messages">
<jsp:include page="../common/action_messages.jsp"/>
</div>-->

<s:set var="checkAll">
    <input type="checkbox" name="check_uncheck" onclick="checkUncheck('check_uncheck_row')" style="margin: 0 0 0 4px" />
</s:set>
<div id="srchinv_tab">&nbsp;</div>
	<div id="res"><mmr:message messageId="label.search.results"/></div>
	<div id="results">	
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
	
<div id="srchinv_result_tbl">
	<display:table id="arTable"  name="invoices" export="false" uid="row"  sort="list" requestURI="/admin/updateAR.action" cellspacing="0" cellpadding="3" style="margin-top:0px;">

		<display:column headerClass="srchinv_tableTitle2_checkbox" title="${checkAll}"  sortable="false">
	  	  <s:hidden name="selectedInvoices[%{#attr.row_rowNum - 1}].invoiceId" value="%{#attr.row.invoiceId}"/>
	  	  <s:checkbox cssClass="check_uncheck_row" name="select[%{#attr.row_rowNum - 1}]" value="select[%{#attr.row_rowNum - 1}]" />
	 	</display:column> 	
		<display:column headerClass="srchinv_tableTitle2_invoice" property="invoiceNum"  sortable="true" title="Invoice#" />
		<display:column headerClass="srchinv_tableTitle2" property="customer.name"  sortable="true" title="Company" />
		<display:column headerClass="srchinv_tableTitle2" property="dateCreated" format="{0,date,dd-MMM-yyyy}"  sortable="true" title="Date Created" />
		<display:column headerClass="srchinv_tableTitle2_amount" property="invoiceAmount" format="{0,number,currency}"  sortable="true" title="Amount" />
		<display:column headerClass="srchinv_tableTitle2_tax" property="invoiceTax" format="{0,number,currency}"  sortable="true" title="Tax" />
		<display:column headerClass="srchinv_tableTitle2_cost" property="totalInvoiceCharge" format="{0,number,currency}"  sortable="true" title="Total" />
		<display:column headerClass="srchinv_tableTitle2_cost" property="paidAmount" format="{0,number,currency}"  sortable="true" title="Paid Amount" />
        <display:column headerClass="srchinv_tableTitle2_cost" property="balanceDue"  format="{0,number,currency}"  sortable="true" title="Balance Due" />
		<display:column headerClass="srchinv_tableTitle2"  title="Amount Remitted" sortable="true">
			<s:textfield name="selectedInvoices[%{#attr.row_rowNum - 1}].arTransaction.amount" value="%{#attr.row.balanceDue}" size="6" cssClass="text_02_tf_small" />
		</display:column>
		<display:column headerClass="srchinv_tableTitle2_date"  title="Date Remitted" sortable="true" style="width: 295px;">
	<!--  		<sx:datetimepicker name="selectedInvoices[%{#attr.row_rowNum - 1}].arTransaction.paymentDate_web" value="" cssClass="text_01" />-->
			<s:textfield name="selectedInvoices[%{#attr.row_rowNum - 1}].arTransaction.paymentDate_web" id="remit_date_c%{#attr.row_rowNum - 1}" cssStyle="width: 75px;"
											cssClass="text_02_tf" readonly="readonly"/><img src="<%=request.getContextPath()%>/mmr/images/icon_Appt.gif"
											id="f_trigger_c%{#attr.row_rowNum - 1}" style="cursor: pointer;"
											title="Date selector" border="0" onClick="selectDate('remit_date_c<s:property value="%{#attr.row_rowNum - 1}"/>','f_trigger_c<s:property value="%{#attr.row_rowNum - 1}"/>')">
		</display:column>
		<display:column headerClass="srchinv_tableTitle2"  title="Mode of Payment" sortable="true">
		            			<s:select 
									headerKey="" headerValue="" required="true"
									list="#{'Check':'Check', 'Credit Card':'Credit Card', 'Cash':'Cash', 'Money Order':'Money Order'}" 
									name="selectedInvoices[%{#attr.row_rowNum - 1}].arTransaction.modeOfPayment" cssClass="text_01_combo_medium" cssStyle="width: 100px;"
								/>
		</display:column>
		<display:column headerClass="srchinv_tableTitle2"  title="Payment Ref#" sortable="true">
			<s:textfield name="selectedInvoices[%{#attr.row_rowNum - 1}].arTransaction.paymentRefNum" value="" size="6" cssClass="text_02_tf_small" />
		</display:column>
	</display:table>
</div>
<div id="srchinv_res_tbl_end"></div>



