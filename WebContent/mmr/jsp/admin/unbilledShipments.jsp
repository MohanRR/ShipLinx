<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags"%> 
<%@ taglib prefix="sx" uri="/struts-dojo-tags" %>
<%@ page import="com.meritconinc.shiplinx.utils.ShiplinxConstants"%>
<%@ taglib prefix="mmr" uri="/mmr-tags" %>

<s:set var="checkAll">
    <input type="checkbox" name="check_uncheck" onclick="checkUncheck('check_uncheck_row')" style="margin: 0 0 0 4px" />
</s:set>
<div id="geninv_tab">&nbsp;</div>
	<div id="geninv_res"><mmr:message messageId="label.search.results"/></div>
	<div id="unbilled_shpmnts_results">	
	<s:if test="%{orders.size()>1}">
	<div id="rslt_stmnt"><br/><s:property value="orders.size()" /> Unbilled shipments for all Customers</div>
	</s:if>
	<s:elseif test="%{orders.size()==1}">
	<div id="rslt_stmnt"><br/><s:property value="orders.size()" /> Unbilled shipment for all Customers</div>
	</s:elseif>
	<s:else>
	<div id="rslt_stmnt"><br/>No Unbilled shipments for any Customer</div>
	</s:else>
	</div>
	
<div id="unbilled_shpmnts_result_tbl">
<display:table id="ordersTable"  name="orders" export="false" uid="row" sort="list" requestURI="invoiceable.list.action?type=refresh" >

	<display:setProperty name="paging.banner.items_name" value=""></display:setProperty>
	<display:setProperty name="paging.banner.some_items_found" value=""/>
	<display:setProperty name="paging.banner.all_items_found" value=""></display:setProperty>
	<display:setProperty name="paging.banner.placement" value="bottom"></display:setProperty>
	<display:setProperty name="paging.banner.group_size" value="3"></display:setProperty>
	

	<display:column headerClass="srchinv_tableTitle2_checkbox" title="${checkAll}" >
  	  <s:hidden name="selectedOrders[%{#attr.row_rowNum - 1}].id" value="%{#attr.row.id}"/>
  	  <s:checkbox name="select[%{#attr.row_rowNum - 1}]" value="select[%{#attr.row_rowNum - 1}]" cssClass="check_uncheck_row" />
 	</display:column> 	
 	<display:column headerClass="srchinv_tableTitle2" property="customerName"  sortable="true" title="Company" >
  	</display:column>
	<display:column headerClass="srchinv_tableTitle2" property="id"  sortable="true" title="Order #" />
	<display:column headerClass="srchinv_tableTitle2" format="{0,date,dd-MMM-yyyy}" property="dateCreated"  sortable="true" title="Date Created" />
	<display:column headerClass="srchinv_tableTitle2" sortable="true" title="Cost / charge" >
		<s:property value="%{#attr.row.totalToCost}"/> / <s:property value="%{#attr.row.totalToCharge}"/>
	</display:column>
	
	<display:column headerClass="srchinv_tableTitle2" sortable="true" title="Billed" >
		<s:property value="%{#attr.row.previouslyBilled}"/>
	</display:column>
			
	</display:table>
</div>
<div id="unbilled_shpmnts_res_tbl_end"></div>


