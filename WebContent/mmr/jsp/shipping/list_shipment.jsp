<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags"%> 
<%@ taglib prefix="sjt" uri="/struts-jquery-tree-tags"%>
<%@ taglib prefix="mmr" uri="/mmr-tags" %>
<%@ taglib prefix="sx" uri="/struts-dojo-tags" %>

<SCRIPT language="JavaScript">
// Didn't work
	function selectAll(box, classStyle) {
	    var checked = box.checked;
	    document.getElementsByClassName(classStyle).each( alert('checkbox') );
	    document.getElementsByClassName(classStyle).each( function(checkbox){
	            checkbox.checked = checked;
	        });
	}
	
	function atleastOneShipmentChecked(cname)
	{
		//alert(document.searchform.autoprint.value);
		var count =0;
		var arrOrders = new Array();
		var aa = getElementsByClassName(cname);
		for(var i=0; i < aa.length; i++)
	    {
	 		if(aa[i].checked == true)
	 		{
	 			var hidden_id = "searchform_selectedShipments_"+i+"__id";
	 			var oid = document.getElementById(hidden_id).value;
	 			arrOrders[i] = oid;
	      		count ++;
	      	}
	    }
	    if(count==0)
	    {
	    	alert("Please select atleast one Shipment item.");
	    }
	    else
	    {
			//Call the action for Printing Label for the selected Shipments.    
	    	if(confirm("Would you like to print label for selected Shipments?"))
	    	{
	    		var slcopies = document.getElementById("label_copies").value;
	    		var ccopies = document.getElementById("customsinv_copies").value;
	    		var url="getShippingLabel.action?slcopies="+slcopies+"&cicopies="+ccopies+"&arrayOrders="+arrOrders;
	    		window.open(url,'','width=760,height=540,left=100,top=100,scrollbars=1');
		 	}
	    }
	}
	
	function updateShipment(orderid)
	{
		if(confirm('Acknowledge the Order?'))
		{
			var classnm = "ajax_status_"+orderid;
		
			ajax_Service=ajaxFunction();
			ajax_Service.onreadystatechange=function()
			  {
				   if(ajax_Service.readyState==4)
					{
					reponse=ajax_Service.responseText;
					js_stateid=getElementsByClassName(classnm);
					js_stateid[0].innerHTML= reponse;
					}
			  }
	 		url="updateShipmentStatus.action?order_id="+orderid;
		  	ajax_Service.open("GET",url,true);
		  	ajax_Service.send(this);
		  }
	}
</SCRIPT>

<div id="srchinv_tab_shipments">&nbsp;</div>
	<div id="srchshipmnt_res"><mmr:message messageId="label.search.results"/></div>
	<div id="srchshipmnt_results">	
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

<div id="srchshipmnt_result_tbl">


<s:if test="%{#request.fromCart != null && #request.fromCart == 'false'}">
<div id="srchshipmnt_result_tbl_print_label">
	<font class="srchshipmnt_text_03"><mmr:message messageId="label.copies.shipping.label"/></font>&nbsp;
	<s:if test="%{#request.no_of_lbls != null}">
		<s:select id="label_copies" cssStyle="width:39px;" cssClass="text_01_combo_small" list="#{'0':'0','1':'1','2':'2','3':'3','4':'4','5':'5','6':'6','7':'7','8':'8','9':'9','10':'10'}" value="%{#request.no_of_lbls}"/>
	</s:if>
	<s:else>
		<s:select id="label_copies" cssStyle="width:39px;" cssClass="text_01_combo_small" list="#{'0':'0','1':'1','2':'2','3':'3','4':'4','5':'5','6':'6','7':'7','8':'8','9':'9','10':'10'}" value="1"/>
	</s:else>
		&nbsp;<font class="srchshipmnt_text_03"><mmr:message messageId="label.copies.customsinvoice"/></font>&nbsp;
	<s:if test="%{#request.no_of_ci != null}">
		<s:select id="customsinv_copies" cssStyle="width:39px;" cssClass="text_01_combo_small" list="#{'0':'0','1':'1','2':'2','3':'3','4':'4','5':'5','6':'6','7':'7','8':'8','9':'9','10':'10'}" value="%{#request.no_of_ci}"/>
	</s:if>
	<s:else>
		<s:select id="customsinv_copies" cssStyle="width:39px;" cssClass="text_01_combo_small" list="#{'0':'0','1':'1','2':'2','3':'3','4':'4','5':'5','6':'6','7':'7','8':'8','9':'9','10':'10'}" value="3"/>
	</s:else>
	&nbsp;<a href="javascript: atleastOneShipmentChecked('check_uncheck_row');">&nbsp;<img border="0" src="<s:url value="/mmr/images/DataGrid_headerSeparatorSkin.png" includeContext="true" />">&nbsp;
	<img border="0" style="margin-bottom: -3px;" src="<s:url value="/mmr/images/generate_label.png" includeContext="true" />">&nbsp;
	<mmr:message messageId="label.print.label"/></a>
</div>
</s:if>

	<s:set var="sortable">
		<s:if test="%{#request.fromCart != null && #request.fromCart == 'false'}">
			true
		</s:if>
		<s:else>
			false
		</s:else>
	</s:set>

	<display:table id="shipmentsTable"  name="shipments" export="true" uid="row"  sort="list" 
		requestURI="${requrl}" cellspacing="0"
		decorator="com.meritconinc.shiplinx.utils.ShippingOrderDecorator">

	<display:setProperty name="paging.banner.items_name" value=""></display:setProperty>
	<display:setProperty name="paging.banner.some_items_found" value=""/>
	<display:setProperty name="paging.banner.all_items_found" value=""></display:setProperty>
	<display:setProperty name="paging.banner.placement" value="bottom"></display:setProperty>
	<display:setProperty name="paging.banner.group_size" value="3"></display:setProperty>
	<s:set var="checkAll">
    	<input type="checkbox" name="check_uncheck" onclick="checkUncheck('check_uncheck_row')" style="margin: 0 0 0 4px" />
	</s:set> 
	<s:set var="status_d">
		ajax_status_<s:property value="%{#attr.row.id}"/>
	</s:set>
	<display:column headerClass="srchinv_tableTitle2" title="${checkAll}" sortable="false" media="html">
  	  <s:hidden name="selectedShipments[%{#attr.row_rowNum - 1}].id" value="%{#attr.row.id}"/>
  	  <s:checkbox name="select[%{#attr.row_rowNum - 1}]" value="select[%{#attr.row_rowNum - 1}]" cssClass="check_uncheck_row" />
 	</display:column> 	 	

 	<display:column headerClass="srchinv_tableTitle2" sortable="${sortable}" title="" media="html">
 		<!-- If Shipment status is STATUS_PRE_DISPATCHED and STATUS_READYTOPROCESS, then customer_admin role should be able to edit shipment and do manual shipping  -->
 		<s:if test="%{#session.ROLE.contains('busadmin') || shipments[#attr.row_rowNum-1].statusId==60 || shipments[#attr.row_rowNum-1].statusId==80}">
 			 <s:a href="process.shipment.action?order_id=%{#attr.row.id}"> 
					<img src="<s:url value="/mmr/images/edit_pencil.png" includeContext="true" />" alt="Edit Shipment" border="0">
			</s:a>	
		</s:if>
	</display:column>
	
	<s:if test="%{#session.ROLE.contains('busadmin') || #session.ROLE.contains('sales')}"> 
	 	<display:column headerClass="srchinv_tableTitle2" property="customer.name"  sortable="${sortable}" maxLength="10" title="Company" />
	 </s:if>
 	<display:column headerClass="srchinv_tableTitle2" sortable="${sortable}" title="Order #" media="html">
 		<s:a href="view.shipment.action?viewShipmentId=%{#attr.row.id}"> 
 			<s:property value="%{#attr.row.id}"/>
 		</s:a>
 	</display:column>
 	<display:column headerClass="srchinv_tableTitle2" sortable="${sortable}" property="id" title="Order #" media="csv xml excel"/>
  	
 	<s:if test="%{#request.fromCart != null && #request.fromCart != 'false'}">
 	<display:column headerClass="srchinv_tableTitle2" sortable="${sortable}" title="Reference # / Shopify Order #"  media="html">
 		<s:property value="%{shipments[#attr.row_rowNum-1].referenceCode}"/>&nbsp;<strong>/</strong>&nbsp;<s:property value="%{shipments[#attr.row_rowNum-1].referenceTwo}"/>
 	</display:column>
 	</s:if>	
 		
	<display:column headerClass="srchinv_tableTitle2" sortable="${sortable}" title="Tracking #"  media="html">
	<s:if test="%{shipments[#attr.row_rowNum-1].trackingURL!=null}">
  		<a href="javascript:track('<s:property value="%{#attr.row.id}"/>')"><s:property value="%{#attr.row.masterTrackingNum}"/> </a>
  	</s:if>
  	<s:else>
  		<s:a href="view.shipment.action?notrackurl='true'&viewShipmentId=%{#attr.row.id}"> 
 			<s:property value="%{#attr.row.masterTrackingNum}"/>
 		</s:a>
  	</s:else>
  	</display:column>
  	<display:column headerClass="srchinv_tableTitle2" sortable="${sortable}" property="masterTrackingNum" title="Tracking #" media="csv xml excel"/>
  	
	<display:column headerClass="srchinv_tableTitle2" property="scheduledShipDate"  format="{0,date,MMM dd yyyy}" sortable="${sortable}" title="Ship Date" />
	<s:if test="%{#request.fromCart != null && #request.fromCart != 'false'}">
		<s:if test="%{carrierName != null}">
			<display:column headerClass="srchinv_tableTitle2" property="carrierName"  sortable="${sortable}" title="Carrier" />
		</s:if>
		<s:else>
			<display:column headerClass="srchinv_tableTitle2" sortable="${sortable}" title="Carrier" > </display:column>
		</s:else>
	</s:if>
	<s:else>
		<display:column headerClass="srchinv_tableTitle2" sortable="${sortable}" title="Carrier" >
			<s:property value="%{shipments[#attr.row_rowNum-1].service.masterCarrier.name}"/>
		</display:column>
	</s:else>
	<display:column headerClass="srchinv_tableTitle2" property="service.name"  sortable="${sortable}" title="Service" />
	<display:column headerClass="srchinv_tableTitle2" property="quotedWeight"  sortable="${sortable}" title="Quoted Weight" />
	<display:column headerClass="srchinv_tableTitle2" property="billedWeight"  sortable="${sortable}" title="Billed Weight" />

	<display:column headerClass="srchinv_tableTitle2" title="Quoted Charge" sortable="${sortable}">
			$<s:property value="%{shipments[#attr.row_rowNum-1].totalChargeQuoted}"/>
			<s:if test="%{#session.ROLE.contains('busadmin')}">
			/ $<s:property value="%{shipments[#attr.row_rowNum-1].totalCostQuoted}"/>
			</s:if>
	</display:column>
	<display:column headerClass="srchinv_tableTitle2" title="Billed Charge" sortable="${sortable}">
			$<s:property value="%{shipments[#attr.row_rowNum-1].totalChargeActual}"/>
			<s:if test="%{#session.ROLE.contains('busadmin')}">
			/ $<s:property value="%{shipments[#attr.row_rowNum-1].totalCostActual}"/>
			</s:if>			
	</display:column>

	<display:column headerClass="srchinv_tableTitle2" property="fromAddress.longAddress"  maxLength="10" sortable="${sortable}" title="From Address" />
	<display:column headerClass="srchinv_tableTitle2" property="toAddress.longAddress"  maxLength="10" sortable="${sortable}" title="To Address" />
	
	<display:column headerClass="srchinv_tableTitle2" sortable="${sortable}" title="Status"  class="${status_d}">
	<s:if test="%{shipments[#attr.row_rowNum-1].statusName == 'Sent to Warehouse' && #session.ROLE.contains('busadmin')}">
		<s:property value="%{shipments[#attr.row_rowNum-1].statusName}"/>&nbsp;
			<img src="<s:url value="/mmr/images/stamp_check.png" includeContext="true" />" alt="Update" border="0" onclick="javascript:updateShipment('<s:property value="%{#attr.row.id}"/>');" style="cursor: pointer;">
	</s:if>
	<s:else>
		<s:property value="%{shipments[#attr.row_rowNum-1].statusName}"/>
	</s:else>
	</display:column>
	<display:column headerClass="srchinv_tableTitle2" property="batchId"  sortable="${sortable}" title="Batch ID" />
	<display:column headerClass="srchinv_tableTitle2" property="billingStatusText"  sortable="${sortable}" title="Billing Status" />
	<display:column headerClass="srchinv_tableTitle2" title="Repeat Shipment" style="text-align: center;" media="html">
			<s:a href="Javascript: repeatOrder(%{#attr.row.id});"><img src="<s:url value="/mmr/images/repeat.png" includeContext="true" />" alt="Repeat" border="0" /></s:a> 
	</display:column>
	<s:if test="%{customer!=null}">
		<display:caption class="unlkd_caption">Shipments List: <s:text name="%{customer.name}"/></display:caption>	
	</s:if>
	<s:else>
		<display:caption class="unlkd_caption">Shipments List</display:caption>		
	</s:else>
	
	</display:table>
</div>

<div id="srchinv_res_tbl_end"></div>

