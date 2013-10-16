<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib prefix="mmr" uri="/mmr-tags" %>

<!--  
<div id="messages">
<jsp:include page="../common/action_messages.jsp"/>
</div>-->

<div id="arlist_tab">&nbsp;</div>
	<div id="arlist_res"><mmr:message messageId="label.search.results"/></div>
	<div id="arlist_results">	
	<s:if test="%{arTransactions.size()>1}">
	<div id="rslt_stmnt"><br/><s:property value="arTransactions.size()" /><mmr:message messageId="label.search.results.items"/></div>
	</s:if>
	<s:elseif test="%{arTransactions.size()==1}">
	<div id="rslt_stmnt"><br/><s:property value="arTransactions.size()" /><mmr:message messageId="label.search.results.item"/></div>
	</s:elseif>
	<s:else>
	<div id="rslt_stmnt"><br/><mmr:message messageId="label.search.results.noitems"/></div>
	</s:else>
	</div>
	
<div id="srchinv_result_tbl">
	<display:table id="artable"  name="arTransactions" export="false" uid="row"  pagesize="100"   sort="list" requestURI="/search.address2.action" >

		<display:column class="text_01" property="customer.name"  sortable="true" title="Company" />
		<display:column class="text_01" property="paymentDate"  sortable="true" title="Remittance Date" />
		<display:column class="text_01" property="amount"  sortable="true" title="Amount" />
		<display:column class="text_01" property="modeOfPayment"  sortable="true" title="Paid By" />
		<display:column class="text_01" property="paymentRefNum"  sortable="true" title="Payment Ref#" />
	</display:table>
</div>
<div id="srchinv_res_tbl_end"></div>



