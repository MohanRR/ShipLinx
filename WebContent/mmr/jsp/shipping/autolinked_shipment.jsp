
<!-- -->
<!DOCTYPE html>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags"%> 
<%@ taglib prefix="mmr" uri="/mmr-tags" %>
<%@ taglib prefix="sx" uri="/struts-dojo-tags" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib prefix="sjt" uri="/struts-jquery-tree-tags"%>


<html> 
	<head> 
	    <title>Auto Linked Shipments</title> 
	    <sj:head jqueryui="true" />
	    <sx:head />
	</head> 
<body> 
	<SCRIPT language="JavaScript">
		function reassignCustomer()
		{
		 document.searchform.action = "reassigncustomer.shipment.action";
		 document.searchform.submit();
		}
		function acceptShipments() {
			document.searchform.action = "accept.shipment.action";
			document.searchform.submit();
		}	
		
		function getAccountInformation(url){
			window.open(url,'','width=760,height=540,left=100,top=100,scrollbars=1');
		}
	    function showState() {
			ajax_Service=ajaxFunction();
			ajax_Service.onreadystatechange=function()
			  {
				   if(ajax_Service.readyState==4)
					{
					reponse=ajax_Service.responseText;
					js_stateid=document.getElementById("stateid");
					js_stateid.innerHTML= reponse;
					}
			  }
			  firstBox = document.getElementById('firstBox');
			  url="<%=request.getContextPath()%>/markup.listService.action?value="+firstBox.value;
				//param="objName=ref_state&country_id="+country_id;
			  	ajax_Service.open("GET",url,true);
			  	ajax_Service.send(this);
		} // End function showState()	
	</SCRIPT>

	<div id="messages">
		<jsp:include page="../common/action_messages.jsp"/>
	</div>

	<div class="form-container"> 
		<s:form id="searchform" action="list.shipment.action" name="searchform">
<s:set var="checkAll">
    <input type="checkbox" name="check_uncheck" onClick="checkUncheck('check_uncheck_row')" style="margin: 0 0 0 4px" />
</s:set>		
<div id="unlkd_shpmnts_srch_panel">
<table>
<tr>
<td><div id="srch_crtra"  style="margin-left:5px;"><mmr:message messageId="label.autolinked.shipments"/></div></td>
<td>&nbsp;</td>
</tr>
</table>
</div>	
<div id="unlkd_shpmnts_srch_glass">&nbsp;</div>	

<div id="autolkd_shpmnts_srchactions_imgs">
<table>
<tr>
<td><img src="<s:url value="/mmr/images/DataGrid_headerSeparatorSkin.png" includeContext="true" />" border="0">&nbsp;</td>
<td><img src="<s:url value="/mmr/images/save_icon.png" includeContext="true" />" border="0">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src="<s:url value="/mmr/images/DataGrid_headerSeparatorSkin.png" includeContext="true" />" border="0">&nbsp;</td>
<td><img src="<s:url value="/mmr/images/icon_print.png" includeContext="true" />" border="0">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
</tr>
</table>
</div>
<div id="autolkd_shpmnts_srch_actions">
<table>
<tr>
<td><a href="javascript: reassignCustomer()"><mmr:message messageId="label.reassign.customer"/></a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
<td><a href="javascript: acceptShipments()"><mmr:message messageId="label.accept.customer"/></a> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
</tr>
</table>
</div>
	
<div id="unlkd_shpmnts_srch_table">
	<table width="400px" border="0" cellpadding="5" cellspacing="0"  style="margin-top:2px;">
						<tr>					
							<td class="edi_text_03"><strong><mmr:message messageId="label.customer.name" /></strong></td>
				            <s:url id="customerList" action="listCustomers" />
				            <td>
				            	<sx:autocompleter keyName="shippingOrder.customerId" name="searchString" 
				            		href="%{customerList}" dataFieldName="customerSearchResult"  
				            		cssStyle="width:158px;" cssClass="dojoComboBox" loadOnTextChange="true" loadMinimumCount="3"/>
				            </td>							
						</tr>											
					</table>
</div>	

<div id="autolkd_tab"><br/></div>
	<div id="autolkd_res"><mmr:message messageId="label.search.results"/></div>
	<div id="autolkd_results">	
	<s:if test="%{shipments.size()>1}">
	<div id="rslt_stmnt"><br/><s:property value="shipments.size()" /><mmr:message messageId="label.search.results.items"/></div>
	</s:if>
	<s:elseif test="%{shipments.size()==1}">
	<div id="rslt_stmnt"><br/><s:property value="shipments.size()" /><mmr:message messageId="label.search.results.item"/></div>
	</s:elseif>
	<s:else>
	<div id="rslt_stmnt"><br/><mmr:message messageId="label.search.results.noitems"/></div>
	</s:else>
	</div>
	
	<div id="autolkd_result_tbl">
	<display:table id="shipmentsTable"  name="shipments" export="false" uid="row"  pagesize="100"   sort="list" requestURI="list.shipment.action?type=refresh" cellspacing="0" >
		
					<display:setProperty name="paging.banner.items_name" value=""></display:setProperty>
					<display:setProperty name="paging.banner.some_items_found" value=""/>
					<display:setProperty name="paging.banner.all_items_found" value=""></display:setProperty>
					<display:setProperty name="paging.banner.placement" value="bottom"></display:setProperty>
					<display:setProperty name="paging.banner.group_size" value="3"></display:setProperty>
			
					<display:column headerClass="tableTitle2" title="${checkAll}" >
				  	  <s:hidden name="selectedShipments[%{#attr.row_rowNum - 1}].id" value="%{#attr.row.id}"/>
				  	  <s:checkbox cssClass="check_uncheck_row" name="select[%{#attr.row_rowNum - 1}]" value="select[%{#attr.row_rowNum - 1}]" />
				 	</display:column> 	
				 	<display:column headerClass="tableTitle2" property="customer.name"  sortable="true" title="Company" />
					<display:column headerClass="tableTitle2" property="id"  sortable="true" title="Order #" />
					<display:column headerClass="tableTitle2" property="masterTrackingNum"  sortable="true" title="Tracking #" />
					<display:column headerClass="tableTitle2" property="dateCreated"  sortable="true" title="Date Created" />
					<display:column headerClass="tableTitle2" property="service.name"  sortable="true" title="Service" />
					<display:column headerClass="tableTitle2" title="Charges/Costs" sortable="true">
				 		  <sx:tree  cssClass="text_01" label="<b>Total : %{shipments[#attr.row_rowNum-1].totalChargeActual} / %{shipments[#attr.row_rowNum-1].totalCostActual}</b>" >
		  
							<s:iterator  value="%{shipments[#attr.row_rowNum-1].actualCharges}">
							 <sx:treenode cssClass="text_01" label="%{name} : %{charge} / %{cost}" />
							</s:iterator>
						 </sx:tree>	 
					</display:column>
					
					<display:column headerClass="tableTitle2" title="Addresses" >
						<sx:tree cssClass="text_01" label="From : %{shipments[#attr.row_rowNum-1].fromAddress.abbreviationName}">
								<sx:treenode label="%{shipments[#attr.row_rowNum-1].fromAddress.address1} : %{shipments[#attr.row_rowNum-1].fromAddress.city}" />                       
						</sx:tree>
						<sx:tree label="To : %{shipments[#attr.row_rowNum-1].toAddress.abbreviationName}">
						 		<sx:treenode label="%{shipments[#attr.row_rowNum-1].toAddress.address1} : %{shipments[#attr.row_rowNum-1].toAddress.city}" />                       
						</sx:tree>
					</display:column>	
					<display:column headerClass="tableTitle2" property="billingStatusText"  sortable="true" title="Billing Status" />
					
					<s:if test="%{customer!=null}">
						<display:caption class="unlkd_caption">Auto Linked Shipments : <s:text name="%{customer.name}"/></display:caption>	
					</s:if>
					<s:else>
						<display:caption class="unlkd_caption">Auto Linked Shipments</display:caption>		
					</s:else>
				
				
				</display:table>
				<div id="autolnkd_res_tbl_end"></div>
	
	
	</div>
		
				
	</s:form>		
</body>
</html>













