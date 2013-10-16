<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags"%> 
<%@ taglib prefix="mmr" uri="/mmr-tags" %>
<%@ taglib prefix="sx" uri="/struts-dojo-tags" %>

<html>
<head> 
	    <title><s:text name="search.shipment.title"/></title> 
	    <sj:head jqueryui="true" />
	    <sx:head />
	</head>
<body>
<SCRIPT language="JavaScript">
	
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
			  url="<%=request.getContextPath()%>/markup.listService.action?value="+firstBox.value+"&pickup='true'";
				//param="objName=ref_state&country_id="+country_id;
			  	ajax_Service.open("GET",url,true);
			  	ajax_Service.send(this);
		} // End function showState()
		
		function searchShipment()
		{
			document.searchpickupform.action = "list.pickups.action";
			document.searchpickupform.submit();
		}
		
		function cancelPickup(pid)
		{
			//alert(pid);
			if(confirm("Would you like to cancel this pickup request?"))
			{
			document.searchpickupform.action = "cancelPickup.action?pickupid="+pid;
			document.searchpickupform.submit();
			}
		}
		
		function goToCreatePickup()
		{
			//alert("Create Pickup");
			document.searchpickupform.action = "gotoCreate.pickup.action";
			document.searchpickupform.submit();
		}
		
		
</SCRIPT>

	<div id="messages">
		<jsp:include page="../common/action_messages.jsp"/>
	</div>

	<div class="form-container"> 
		<s:form id="searchpickupform" action="search.pickups.action" name="searchpickupform">
		
		<div id="srch_pickups_panel">
		<table>
		<tr>
		<td><div id="srch_crtra"><mmr:message messageId="label.search.pickup"/></div></td>
		<td>&nbsp;</td>
		</tr>
		</table>
		</div>
		
		<div id="srchpickups_imgs">
		<table>
		<tr>
		<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
		<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
		<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
		<td><img src="<s:url value="/mmr/images/DataGrid_headerSeparatorSkin.png" includeContext="true" />" border="0">&nbsp;<img border="0" src="<%=request.getContextPath()%>/mmr/images/reset_icon.png" border="0"/>&nbsp;</td>
		<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
		<td>
		<img src="<s:url value="/mmr/images/DataGrid_headerSeparatorSkin.png" includeContext="true" />" border="0">&nbsp;<img border="0" src="<%=request.getContextPath()%>/mmr/images/search_icon.png" border="0"/>&nbsp;</td>
		</tr>
		</table>
		</div>
		
		<div id="srchpickups_actions">
		<table>
		<tr>
		<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
		<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
		<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
		<td>
		<a href="javascript: goToCreatePickup()"><mmr:message messageId="label.create.pickup"/></a>
		</td>
		<td>
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript: searchShipment()">Search</a>&nbsp;&nbsp;</td>
		</tr>
		</table>
		</div>
		
		
		
		<div id="srch_pickups_table">
			<table width="1000px" border="0" cellpadding="2" cellspacing="0"  style="margin-top:7px;margin-left: 15px;">
						<tr>
							<td class="srchshipmnt_text_03"><mmr:message messageId="label.from.date"/>:</td>	
							<td>
					<!--  		<sx:datetimepicker name="shippingOrder.fromDate" />-->
							<s:textfield name="pickup.fromDate" id="f_date_c" cssStyle="width: 75px;"
											cssClass="text_02_tf" readonly="readonly"/><img src="<%=request.getContextPath()%>/mmr/images/icon_Appt.gif"
											id="f_trigger_c" style="cursor: pointer;"
											title="Date selector" border="0" onClick="selectDate('f_date_c','f_trigger_c');">
							</td>						
							<td class="srchshipmnt_text_03"><mmr:message messageId="label.to.date"/>:</td>	
							<td>
					<!--  		<sx:datetimepicker name="shippingOrder.toDate" />-->
							<s:textfield name="pickup.toDate" id="t_date_c" cssStyle="width: 75px;"
											cssClass="text_02_tf" readonly="readonly"/><img src="<%=request.getContextPath()%>/mmr/images/icon_Appt.gif"
											id="t_trigger_c" style="cursor: pointer;"
											title="Date selector" border="0" onClick="selectDate('t_date_c','t_trigger_c');">
							</td>						
							<td class="srchshipmnt_text_03"><mmr:message messageId="label.track.carrier"/>:</td>
							<td class="text_01">
								<s:select cssClass="text_01_combo_big" cssStyle="width:120px;" listKey="id" listValue="name" 
									name="pickup.carrierId" list="#session.CARRIERS" 
									id="firstBox" theme="simple"/>
							</td>
							<!--<td class="srchshipmnt_text_03"><mmr:message messageId="label.markup.service"/>:</td>
							<td class="text_01" id="stateid">
								<s:select cssClass="text_01_combo_big" cssStyle="width:120px;" listKey="id" listValue="name"
									name="pickup.serviceId" list="#session.SERVICES" 
										headerKey="-1" id="secondBox" theme="simple"/>													
							</td>							
						-->
							 <td class="srchshipmnt_text_03"><mmr:message messageId="label.carrier.conf.num"/>: </td>  
			                <td  class="text_01"  align="left">
								<s:textfield size="25" name="pickup.confirmationNum" cssClass="text_02_tf" cssStyle="width: 150px;"/>
							</td>
							<td class="srchshipmnt_text_03"><mmr:message messageId="label.status"/>:</td>
							<td class="text_01" align="left">
								<s:select cssStyle="width:120px;"   name="pickup.status" 
									cssClass="text_01_combo_big" list="#{'-1':'---Select---','10':'ACTIVE','40':'CANCELLED'}" theme="simple"/>
							</td>
								</tr>
								<s:if test="%{#session.ROLE.contains('busadmin')}">
								<tr>
								<td class="srchshipmnt_text_03"><mmr:message messageId="label.customer.name" />:</td>
								<s:url id="customerList" action="listCustomers" />
				            	<td class="text_01" colspan="2">
				            		<sx:autocompleter keyName="pickup.customerId" name="searchString" 
				            			href="%{customerList}" dataFieldName="customerSearchResult"  
				            			cssStyle="width:165px;" loadOnTextChange="true" loadMinimumCount="3"/>
				            	</td>
				            	</tr>
							</s:if>
						
					</table>
		</div>
		
		<div id="formResult">
		   <s:include value="list_pickups.jsp"></s:include>
		</div>
				
			
	</s:form>	
	</div>
</body>
</html>