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
	<s:if test="%{pickups.size()>1}">
	<div id="rslt_stmnt"><br/><s:property value="pickups.size()" /><mmr:message messageId="label.search.results.items"/></div>
	</s:if>
	<s:elseif test="%{pickups.size()==1}">
	<div id="rslt_stmnt"><br/><s:property value="pickups.size()" /><mmr:message messageId="label.search.results.item"/></div>
	</s:elseif>
	<s:else>
	<div id="rslt_stmnt"><br/><mmr:message messageId="label.search.results.noitems"/></div>
	</s:else>
	</div>	

<div id="srchshipmnt_result_tbl">

	<s:set var="sortable">
		<s:if test="%{#request.fromCart != null && #request.fromCart == 'false'}">
			true
		</s:if>
		<s:else>
			false
		</s:else>
	</s:set>
	
	<display:table id="pickupsTable"  name="pickups" export="false" uid="row"  sort="list" requestURI="${requrl}" cellspacing="0">

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
	<s:set var="pickuptime">
		( <mmr:message messageId="label.pickup.readytime"/> - <mmr:message messageId="label.pickup.closetime"/>)
	</s:set>

 	<display:column headerClass="srchpk_tableTitle2" property="customer.name"  sortable="${sortable}" maxLength="10" title="Customer" />
 	
	<display:column headerClass="srchpk_tableTitle2" property="pickupDate"  format="{0,date,MMM dd yyyy}" sortable="${sortable}" title="Pickup Date" />
	<display:column headerClass="srchpk_tableTitle2" sortable="${sortable}" title="${pickuptime}">
		<s:property value="%{pickups[#attr.row_rowNum-1].readyHour}"/>:<s:property value="%{pickups[#attr.row_rowNum-1].readyMin}"/>&nbsp;-&nbsp;<s:property value="%{pickups[#attr.row_rowNum-1].closeHour}"/>:<s:property value="%{pickups[#attr.row_rowNum-1].closeMin}"/>
	</display:column>
	<display:column headerClass="srchpk_tableTitle2" property="carrier.name"  sortable="${sortable}" title="Carrier" />
	<display:column headerClass="srchpk_tableTitle2" property="confirmationNum"  maxLength="12" sortable="${sortable}" title="Confirmation No" />

	<display:column headerClass="srchpk_tableTitle2" property="address.longAddress"  maxLength="30" sortable="${sortable}" title="Pickup Address" />
	<display:column headerClass="srchpk_tableTitle2" title="Status" >
	<s:if test="%{pickups[#attr.row_rowNum-1].status=='10'}">
			<mmr:message messageId="pickup.status.active"/>
	</s:if>	
	<s:elseif test="%{pickups[#attr.row_rowNum-1].status=='40'}">
			<mmr:message messageId="status.cancelled"/>
	</s:elseif>
	</display:column>
	<display:column headerClass="srchpk_tableTitle2">
	<s:if test="%{pickups[#attr.row_rowNum-1].status=='10'}">
			<a href="javascript: cancelPickup(<s:property value="%{#attr.row.pickupId}"/>);"><img border="0" src="<%=request.getContextPath()%>/mmr/images/cancel_btn.png" border="0" style="width: 75px; height:23px;"/></a>
	</s:if>
	<s:elseif test="%{pickups[#attr.row_rowNum-1].status=='40'}">
		&nbsp;
	</s:elseif>
	</display:column>
	</display:table>
	
</div>

<div id="srchinv_res_tbl_end"></div>

