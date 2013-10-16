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
	 document.searchInvoiceForm.action = "commReport.action";
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
<td><div id="srch_crtra"><mmr:message messageId="label.report.commission"/></div></td>
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

<div id="srch_table_commission">
<table width="750px" style="margin-top:2px;">
	            	<tr>
		            	<td class="srchinv_text_03"><mmr:message messageId="label.from.date" />: </td>
		            	<td class="srchinv_text_03"><mmr:message messageId="label.to.date" />: </td> 
		            	<td class="srchinv_text_03"><mmr:message messageId="label.salesAgent" />: </td>  	
	            	</tr>
	            	<tr>
		            	<td> <!--   <sj:datepicker name="invoice.fromInvoiceDate_web" />-->
		            	<!--  	<sx:datetimepicker name="invoice.fromInvoiceDate_web"></sx:datetimepicker>-->
		            	<s:textfield name="invoice.fromInvoiceDate_web" id="f_date_c" cssStyle="width: 75px;"
											cssClass="text_02_tf" readonly="readonly"/><img src="<%=request.getContextPath()%>/mmr/images/icon_Appt.gif"
											id="f_trigger_c" style="cursor: pointer;"
											title="Date selector" border="0" onClick="selectDate('f_date_c','f_trigger_c');">
		            	</td>		            	
		            	<td>
		              <!--  	<sj:datepicker name="invoice.toInvoiceDate_web"/>-->
		            <!--  	<sx:datetimepicker name="invoice.toInvoiceDate_web"></sx:datetimepicker>-->
		            <s:textfield name="invoice.toInvoiceDate_web" id="t_date_c" cssStyle="width: 75px;"
											cssClass="text_02_tf" readonly="readonly"/><img src="<%=request.getContextPath()%>/mmr/images/icon_Appt.gif"
											id="t_trigger_c" style="cursor: pointer;"
											title="Date selector" border="0" onClick="selectDate('t_date_c','t_trigger_c');">
		            	</td>
		            	<s:if test="%{#session.ROLE.contains('busadmin')}">
	              			<td>
								<s:select cssClass="text_01_combo_big" cssStyle="width:185px;" listKey="username" listValue="fullName" 
							name="invoice.salesUsername" list="salesUsers" 
								id="salesAgent" theme="simple" />
	          			    </td>
	     			    </s:if>
	     			    <s:else>
	     			    	<td>
	     			    		<s:property value="%{#session.username}"/>
	     			    	</td>
	     			    </s:else>
	            	</tr>
	            </table>
</div>
	
</s:form>
	
</div>
		
<div id="commrprts_tab">&nbsp;</div>
	<div id="srchinv_res"><mmr:message messageId="label.search.results"/></div>
	<div id="srchinv_results">	
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
	<display:table id="invoicetable"  name="invoices" export="true" uid="row"  pagesize="100"  varTotals="totals" sort="list" requestURI="/admin/commReport.action" cellspacing="0" cellpadding="4">


		<s:set name="invoiceId" value="invoices[#attr.row_rowNum-1].getInvoiceId()" />
	
		<display:column style="width: 10%" headerClass="srchinv_tableTitle2" property="invoiceNum"  sortable="true" title="Inv#" />
		<display:column headerClass="srchinv_tableTitle2" property="customer.name"  sortable="true" title="Company" />
		<display:column headerClass="srchinv_tableTitle2" property="dateCreated"  format="{0,date,dd-MMM-yyyy}" sortable="true" title="Date Created" />
		<display:column headerClass="srchinv_tableTitle2_tax" property="commissionAmount"  total="true" format="{0,number,currency}"  sortable="true" title="Commission" />
		<display:column headerClass="srchinv_tableTitle2_amount" property="invoiceAmount" format="{0,number,currency}"  sortable="true" title="Amount" />
		<s:if test="%{#session.ROLE.contains('busadmin')}">
			<display:column headerClass="srchinv_tableTitle2_cost" property="invoiceCost"  format="{0,number,currency}"  sortable="true" title="Cost" />
		</s:if>
		<display:column headerClass="srchinv_tableTitle2_tax" property="paymentStatusString" sortable="true" title="Status" />
		<display:column headerClass="srchinv_tableTitle2_print" sortable="true" media="html">
			<a href="<s:url value="print.invoice.action?invoiceId=%{#invoiceId}"  />" target="_blank">
				<img src="<s:url value="/mmr/images/pdf.gif" includeContext="true" />" alt="Print Invoice" border="0"> 
			</a>
        </display:column>	
        <display:footer>
   		 <tr>
   		 	<td colspan="1">&nbsp;</td>
   	   		<td colspan="2">Total Commission:</td>
     	 <td><fmt:formatNumber type="currency"><s:property value="%{#attr.totals.column4}" />
     	 </fmt:formatNumber>
     	 </td>
   		 </tr>
  		</display:footer>	
	</display:table>
</div>

<div id="srchinv_res_tbl_end_commission"></div>


