<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags"%> 
<%@ taglib prefix="mmr" uri="/mmr-tags" %>
<%@ taglib prefix="sx" uri="/struts-dojo-tags" %>
<!DOCTYPE html>
<html> 
	<head> 
	    <title><s:text name="search.shipment.title"/></title> 
	    <sj:head jqueryui="true" />
	    <sx:head />
	</head> 
<body> 
	<SCRIPT language="JavaScript">
	
		function disableChk()
		{
			if(document.getElementById("searchform_shippingOrder_statusId").value > 1)
				document.getElementById("cancelled").disabled = true;
			else
				document.getElementById("cancelled").disabled = false;
		}
		function searchShipment()
		{
				 document.searchform.action = "list.shipment.action";
			 	document.searchform.submit();
		}
		function saveShipmentList() {
			document.searchform.action = "save.shipment.action";
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
		
		window.onload = disableChk;
		
		function repeatOrder(oid)
		{
			//alert(oid);
			document.searchform.action = "repeat.order.action?order_id="+oid;
			document.searchform.submit();
		}
	</SCRIPT>

	<div id="messages">
		<jsp:include page="../common/action_messages.jsp"/>
	</div>

	<div class="form-container"> 
		<s:form id="searchform" action="list.shipment.action" name="searchform">
		
		<div id="srch_shipmnt_panel">
		<table>
		<tr>
		<td><div id="srch_crtra"><mmr:message messageId="label.search.shipments"/></div></td>
		<td>&nbsp;</td>
		</tr>
		</table>
		</div>
		
		<div id="srchshpmnts_imgs">
		<table>
		<tr>
		<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
		<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
		<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
		<td>
		<img src="<s:url value="/mmr/images/DataGrid_headerSeparatorSkin.png" includeContext="true" />" border="0">&nbsp;
		<a href="search.shipment.action"><img border="0" src="<%=request.getContextPath()%>/mmr/images/reset_icon.png" border="0"/>&nbsp;<mmr:message messageId="label.btn.reset"/>&nbsp;&nbsp;</a></td>
		<td>
		<img src="<s:url value="/mmr/images/DataGrid_headerSeparatorSkin.png" includeContext="true" />" border="0">&nbsp;
		<a href="javascript: saveShipmentList()"><img border="0" src="<%=request.getContextPath()%>/mmr/images/save_icon.png" border="0"/>&nbsp;Save List</a>&nbsp;&nbsp;</td>
		<td>
		<img src="<s:url value="/mmr/images/DataGrid_headerSeparatorSkin.png" includeContext="true" />" border="0">&nbsp;
		<a href="javascript: searchShipment()"><img border="0" src="<%=request.getContextPath()%>/mmr/images/search_icon.png" border="0"/>&nbsp;Search</a>&nbsp;&nbsp;</td>
		</tr>
		</table>
		</div>
		
		
		
		<div id="srch_shipmnt_table">
			<table width="1150px" border="0" cellpadding="0" cellspacing="2"  style="margin-top:7px;margin-left: 15px;">
						<tr>
							<td class="srchshipmnt_text_03">From Date:</td>	
							<td>
					<!--  		<sx:datetimepicker name="shippingOrder.fromDate" />-->
							<s:textfield name="shippingOrder.fromDate" id="f_date_c" cssStyle="width: 75px;"
											cssClass="text_02_tf" readonly="readonly"/><img src="<%=request.getContextPath()%>/mmr/images/icon_Appt.gif"
											id="f_trigger_c" style="cursor: pointer;"
											title="Date selector" border="0" onClick="selectDate('f_date_c','f_trigger_c');">
							</td>						
							<td class="srchshipmnt_text_03">To Date:</td>	
							<td>
					<!--  		<sx:datetimepicker name="shippingOrder.toDate" />-->
							<s:textfield name="shippingOrder.toDate" id="t_date_c" cssStyle="width: 75px;"
											cssClass="text_02_tf" readonly="readonly"/><img src="<%=request.getContextPath()%>/mmr/images/icon_Appt.gif"
											id="t_trigger_c" style="cursor: pointer;"
											title="Date selector" border="0" onClick="selectDate('t_date_c','t_trigger_c');">
							</td>						
			                <td class="srchshipmnt_text_03">Shipment Order #</td>  
			                <td  class="text_01"  align="left">
								<s:textfield size="5" key="shippingOrder.id" name="shippingOrder.id" cssClass="text_02_tf_small"/>
							</td>
							<td class="srchshipmnt_text_03"><mmr:message messageId="label.track.carrier"/>:</td>
							<td class="text_01">
								<s:select cssClass="text_01_combo_big" cssStyle="width:120px;" listKey="id" listValue="name" 
									name="shippingOrder.carrierId" headerKey="" headerValue=""  list="#session.CARRIERS" 
									onchange="javascript:showState();"  id="firstBox" theme="simple"/>
							</td>
							<td class="srchshipmnt_text_03"><mmr:message messageId="label.markup.service"/>:</td>
							<td class="text_01" id="stateid">
								<s:select cssClass="text_01_combo_big" cssStyle="width:120px;" listKey="id" listValue="name"
									name="shippingOrder.serviceId" list="#session.SERVICES" 
										headerKey="-1" id="secondBox" theme="simple"/>													
							</td>							
						</tr>	
						<tr>
							 <td class="srchshipmnt_text_03" colspan="1">Tracking #</td>	
							 <td class="text_01" colspan="1">
								<s:textfield key="shippingOrder.masterTrackingNum" name="shippingOrder.masterTrackingNum" 
									cssStyle="width:160px;" cssClass="text_02_tf" />
							</td>
							 <s:if test="%{#session.ROLE.contains('busadmin')}">		
								<td class="srchshipmnt_text_03" colspan="1"><mmr:message messageId="label.edi.invoiceNumber"/></td>
								<td class="srchshipmnt_text_03"><s:textfield key="shippingOrder.ediInvoiceNumber" name="shippingOrder.ediInvoiceNumber" 
									cssStyle="width:160px;" cssClass="text_02_tf" /></td>
							</s:if>
							<s:else>
								<td colspan="2" class="text_01"  align="center"></td>
							</s:else>
			                <td class="srchshipmnt_text_03">Batch ID:</td> 
			                <td class="text_01"  align="left">
								<s:textfield size="15" key="shippingOrder.batchId" name="shippingOrder.batchId" cssStyle="width:120px;"  cssClass="text_02_tf"/>
							</td>
							<td class="srchshipmnt_text_03"><mmr:message messageId="label.track.status"/>:</td>
							<td class="text_01" align="left">
								<s:select listKey="id"  listValue="name" cssStyle="width:120px;"   name="shippingOrder.statusId" headerKey="-1"  
									cssClass="text_01_combo_big" list="#session.orderStatusList" theme="simple" onchange="disableChk()"/>
							</td>
							<s:if test="%{#session.ROLE.contains('busadmin')}">
							<td class="srchshipmnt_text_03"><mmr:message messageId="label.billing.status"/>:</td>
							<td class="text_01" align="left">
								<s:select cssStyle="width:120px;" listKey="billingStatusId"  listValue="billingStatusText"  name="shippingOrder.billingStatus" headerKey="-1"  
									cssClass="text_01_combo_big" list="#session.billingStatusList" theme="simple"/>
							</td>
							</s:if>
						</tr>	
						<tr>
							<td class="srchshipmnt_text_03"><mmr:message messageId="label.reference"/>:</td>
							<td>
							<s:textfield key="shippingOrder.referenceCode" name="shippingOrder.referenceCode" 
									cssStyle="width:160px;" cssClass="text_02_tf" />
									</td>
							<s:if test="%{#session.ROLE.contains('busadmin')}">
								<td class="srchshipmnt_text_03"><mmr:message messageId="label.customer.name" />:</td>
								<s:url id="customerList" action="listCustomers" />
				            	<td  class="text_01">
				            		<sx:autocompleter keyName="shippingOrder.customerId" name="searchString" 
				            			href="%{customerList}" dataFieldName="customerSearchResult"  
				            			cssStyle="width:165px;" loadOnTextChange="true" loadMinimumCount="3"/>
				            	</td>
							</s:if>
							<td class="srchshipmnt_text_03"><mmr:message messageId="label.search.OrderBy" />:</td>
							<td colspan="2" class="text_01" align="left">	<s:select cssStyle="width:100px;" 
									name="shippingOrder.orderBy" headerKey="1"
									list="{'Order #','Shipment Date'}" />
									<s:select cssStyle="width:60px;" 
									name="shippingOrder.order" headerKey="1"
									list="{'Asc','Desc'}" />
							</td>
							<td class="srchshipmnt_text_03" colspan="3" align="right">
							<s:checkbox id="cancelled" key="shippingOrder.showCancelledShipments"
							name="shippingOrder.showCancelledShipments" cssClass="text_02"/>&nbsp;<mmr:message messageId="label.include.cancelled.shipments"/></td>
						</tr>
								
					</table>
		</div>
		
		<div id="formResult">
		   <s:include value="list_shipment.jsp"></s:include>
		</div>
				
			
	</s:form>		
</body>
</html>
					