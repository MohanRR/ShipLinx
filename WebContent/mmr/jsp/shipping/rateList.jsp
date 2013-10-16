<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sx" uri="/struts-dojo-tags" %>
<%@ taglib prefix="mmr" uri="/mmr-tags" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<script type="text/javascript" src="<%=request.getContextPath()%>/mmr/scripts/orderManager.js">
</script>
<link rel="stylesheet" type="text/css"
	href="<s:url value='/mmr/styles/common.css' includeContext="true"/>" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/mmr/jsp/shipping/style.css">

<head>
<sx:head />
</head>

<SCRIPT language="JavaScript">
var radioselected = 0;
	function submitShipment(val,url)
	{
		var error=true;
		var checkelement = document.getElementById("customs_invoice_checkbox");
		if(checkelement!=null)
		{
			var check = checkelement.checked;
			if(check)
			{
				if(radioselected == '3' && document.getElementById("billToAccountNum").value=='')
				{
					alert("Please enter the Account Number for the Third Party BillTo Address");
					error=false;
				}
				else if(dojo.widget.byId("ci.currency").getValue()=='')
				{
					error=false;
					alert("Please enter the Currency in Customs Invoice Information");					
				}
				else if(document.getElementById("ci.totalvalue").value=='' )
				{
					error= false;
					alert("Please enter the Total Value in Customs Invoice Information");
					document.getElementById("ci.totalvalue").focus();
				}
				else if(document.getElementById("ci.totalweight").value=='')
				{
					error= false;
					alert("Please enter the Total Weight in Customs Invoice Information");
					document.getElementById("ci.totalweight").focus();			
				}
			
				var email=document.getElementById("ci.emailid").value;
				if(!echeck(email))
				{
					error=false;
					alert("Please enter a valid Email Address");
					document.getElementById("ci.emailid").value='';
					document.getElementById("ci.emailid").focus();
				}
			}
		}
		
		if(error & url=="null")
		{
			document.userform.elements['shippingOrder.rateIndex'].value = val;
			document.userform.submit();				
		}
		else if(error && url!="null")
		{
			window.open(url);  //This is for LOOMIS Carrier
		}
	}
	
	function sendCustomerEmail(sel){
		document.userform.action= "shipment.stageThree.rates.mail.action?selected="+sel;
		document.userform.submit();
	}
	
	function backToShipment(){
		document.userform.action= "backToShipment.action";
		document.userform.submit();
	}
	
	function showOrHideSummary()
	{
		var anchor = getElementsByClassName("show_summ");
		var inner = anchor[0].innerHTML;
		
		if(inner != '[&nbsp;+&nbsp;]')
		{
			document.getElementById("summary_div_middle").style.display = 'none';
			document.getElementById("summary_div_bot").style.display = 'none';
			document.getElementById("rate_img").style.display = 'none';
			anchor[0].innerHTML = '[&nbsp;+&nbsp;]';
		}
		else
		{
			document.getElementById("summary_div_middle").style.display = 'block';
			document.getElementById("summary_div_bot").style.display = 'block';
			document.getElementById("rate_img").style.display = 'block';
			anchor[0].innerHTML = '[&nbsp;-&nbsp;]';
		}
	}
	
	function echeck(str) 
	{
		var at="@";
		var dot=".";
		var lat=str.indexOf(at);
		var lstr=str.length;
		var ldot=str.indexOf(dot);
		
		if(str.length > 0)
		{
			if(str.indexOf(at)==-1)
			{
			     return false;
			}
			if(str.indexOf(at)==-1 || str.indexOf(at)==0 || str.indexOf(at)==lstr)
			{
			     return false;
			}
			if(str.indexOf(dot)==-1 || str.indexOf(dot)==0 || str.indexOf(dot)==lstr)
			{
			   return false;
			}
			if(str.indexOf(at,(lat+1))!=-1)
			{
			   return false;
			}
			if(str.substring(lat-1,lat)==dot || str.substring(lat+1,lat+2)==dot)
			{
			   return false;
			}
			if(str.indexOf(dot,(lat+2))==-1)
			{
			   return false;
			}
		}
					
 		return true;					
	}
	
</script>

<html>
	<head>
	
		<title></title>

	</head>
	
	<body>
	<script type="text/javascript">
	 window.onload = function()
  		{
  			var chckbx = document.getElementById("customs_invoice_checkbox");
  			if(chckbx.checked)
  			{  				
  				document.getElementById("customs_invoice_panel").style.display = 'block';	
  			}
  			else
  			{	  				
  				document.getElementById("customs_invoice_panel").style.display = 'none';	
  			}
  		}
	</script>
	<div id="messages">
	<jsp:include page="../common/action_messages.jsp"/>
</div>
<div class="form-container">
<s:form action="shipment.save" name="userform">
	<s:token/>

	<div id="summary_div_top">
	<table>
	<tr><td><div id="srch_crtra"><mmr:message messageId="label.shippingOrder.summary"/> &nbsp;<a href="#" class="show_summ" onclick="showOrHideSummary()">[&nbsp;+&nbsp;]</a></div><td></tr>
	</table>
	</div>
	<div id="summary_div_middle" style="width: 974px;">
		<table cellpadding="0" cellspacing="0" width="970px">
			<tr>
				<td class="hlp_sprt_hdng" width="12%"><mmr:message messageId="label.shippingOrder.shipTo"/>:</td>
				<td width="1%">&nbsp;</td>
				<td class="hlp_sprt" width="70%" colspan="1">
					<s:if test='%{shippingOrder.toAddress.abbreviationName != ""}'>
						<s:property value="%{shippingOrder.toAddress.abbreviationName}"/>&nbsp;,&nbsp;
					</s:if>
					<s:else>
						
					</s:else>
			<!--  	</td>
				<td class="hlp_sprt" width="15%"> -->
					<s:if test='%{shippingOrder.toAddress.city != "" && shippingOrder.toAddress.postalCode == ""}'>
						<s:property value="%{shippingOrder.toAddress.city}"/>&nbsp;,&nbsp;
					</s:if>
					<s:elseif test='%{shippingOrder.toAddress.city == "" && shippingOrder.toAddress.postalCode != ""}'>
						<s:property value="%{shippingOrder.toAddress.postalCode}"/>&nbsp;,&nbsp;
					</s:elseif>
					<s:elseif test='%{shippingOrder.toAddress.city != "" && shippingOrder.toAddress.postalCode != ""}'>
						<s:property value="%{shippingOrder.toAddress.city}"/>&nbsp;,&nbsp;<s:property value="%{shippingOrder.toAddress.postalCode}"/>&nbsp;,&nbsp;
					</s:elseif>  
					<s:else>
						
					</s:else>
			<!--  	</td>
				<td class="hlp_sprt" width="15%"> -->
					<s:if test='%{shippingOrder.toAddress.province !="" && shippingOrder.toAddress.countryName == ""}'>
						<s:property value="%{shippingOrder.toAddress.province}"/>&nbsp;,&nbsp;
					</s:if>
					<s:elseif test='%{shippingOrder.toAddress.province == "" && shippingOrder.toAddress.countryName != ""}'>
						<s:property value="%{shippingOrder.toAddress.countryName}"/>&nbsp;,&nbsp;
					</s:elseif>
					<s:elseif test='%{shippingOrder.toAddress.province != "" && shippingOrder.toAddress.countryName != ""}'>
						<s:property value="%{shippingOrder.toAddress.province}"/>&nbsp;,&nbsp;<s:property value="%{shippingOrder.toAddress.countryName}"/>&nbsp;,&nbsp;
					</s:elseif>
					<s:else>
						
					</s:else>					
			<!--  	</td>
				<td class="hlp_sprt" width="15%"> -->
				<s:if test='%{shippingOrder.toAddress.contactEmail != ""}'>
					<s:property value="%{shippingOrder.toAddress.contactEmail}"/>
				</s:if>
				<s:else>
					
				</s:else>
				</td>				
			</tr>
			<tr>
				<td class="hlp_sprt_hdng"><mmr:message messageId="label.shippingOrder.shipFrom"/>:</td>				
				<td>&nbsp;</td>
				<td class="hlp_sprt" colspan="1">
					<s:if test='%{shippingOrder.fromAddress.abbreviationName != ""}'>	
						<s:property value="%{shippingOrder.fromAddress.abbreviationName}"/>&nbsp;,&nbsp;
					</s:if>
					<s:else>
						
					</s:else>
			<!--  	</td>			
				<td class="hlp_sprt">-->				
					<s:if test='%{shippingOrder.fromAddress.city != "" && shippingOrder.fromAddress.postalCode == ""}'>
						<s:property value="%{shippingOrder.fromAddress.city}"/>&nbsp;,&nbsp;  
					</s:if>
					<s:elseif test='%{shippingOrder.fromAddress.city == "" && shippingOrder.fromAddress.postalCode != ""}'>
						<s:property value="%{shippingOrder.fromAddress.postalCode}"/>&nbsp;,&nbsp;
					</s:elseif>
					<s:elseif test='%{shippingOrder.fromAddress.city != "" && shippingOrder.fromAddress.postalCode != ""}'>
						<s:property value="%{shippingOrder.fromAddress.city}"/>&nbsp;,&nbsp;<s:property value="%{shippingOrder.fromAddress.postalCode}"/>&nbsp;,&nbsp;
					</s:elseif>
					<s:else>
						
					</s:else>
			<!--  	</td>			
				<td class="hlp_sprt">-->
					<s:if test='%{shippingOrder.fromAddress.province != "" && shippingOrder.fromAddress.countryName == ""}'>
						<s:property value="%{shippingOrder.fromAddress.province}"/>&nbsp;,&nbsp;
					</s:if>	
					<s:elseif test='%{shippingOrder.fromAddress.province == "" && shippingOrder.fromAddress.countryName != ""}'>
						<s:property value="%{shippingOrder.fromAddress.countryName}"/>&nbsp;,&nbsp;
					</s:elseif>
					<s:elseif test='%{shippingOrder.fromAddress.province != "" && shippingOrder.fromAddress.countryName != ""}'>
						<s:property value="%{shippingOrder.fromAddress.province}"/>&nbsp;,&nbsp;<s:property value="%{shippingOrder.fromAddress.countryName}"/>&nbsp;,&nbsp;
					</s:elseif>
					<s:else>
						
					</s:else>					
			<!--  </td>			
				<td class="hlp_sprt"> -->
				<s:if test='%{shippingOrder.fromAddress.contactEmail != ""}'>
					<s:property value="%{shippingOrder.fromAddress.contactEmail}"/>
				</s:if>
				<s:else>
					
				</s:else>	
				</td>
			</tr>
			<tr>
				<td class="hlp_sprt_hdng"><mmr:message messageId="label.shippingOrder.packageDetail"/>:</td>
				<td>&nbsp;</td>
				<td class="hlp_sprt">
				<s:if test='%{shippingOrder.packageTypeId.name != ""}'>	
					<s:property value="%{shippingOrder.packageTypeId.name}"/>
				</s:if>				
				<s:else>
					---
				</s:else>
			<!--  	</td>
				<td class="hlp_sprt"> -->
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<s:if test='%{shippingOrder.quantity != ""}'>
					<s:property value="%{shippingOrder.quantity}"/>
				</s:if>
				<s:else>
					---				
				</s:else>
				</td>
			</tr>
			<s:iterator value="shippingOrder.packages" status="counterIndex">
			<tr>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
				<td class="hlp_sprt"><s:label id="shippingOrder.packageArray[%{#counterIndex.index}].length" name="shippingOrder.packages[%{#counterIndex.index}].length" value="%{#session.shippingOrder.packages[#counterIndex.index].length}"/>&nbsp;x&nbsp;<s:label id="shippingOrder.packageArray[%{#counterIndex.index}].width" name="shippingOrder.packages[%{#counterIndex.index}].width" value="%{#session.shippingOrder.packages[#counterIndex.index].width}"/>&nbsp;x&nbsp;<s:label id="shippingOrder.packageArray[%{#counterIndex.index}].height" name="shippingOrder.packages[%{#counterIndex.index}].height" value="%{#session.shippingOrder.packages[#counterIndex.index].height}"/>&nbsp;&nbsp;(L x W x H)
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<s:label id="shippingOrder.packageArray[%{#counterIndex.index}].weight" name="shippingOrder.packages[%{#counterIndex.index}].weight" value="%{#session.shippingOrder.packages[#counterIndex.index].weight}"/>&nbsp;lbs</td>
			</tr>			
			</s:iterator>			
		</table>
	</div>
	<div id="summary_div_bot">&nbsp;</div>	
	<div id="rate_img"><img src="<s:url value="/mmr/images/img_ship_rates.png" includeContext="true" />" border="0"></div>
	<div id="summary_div_tab">&nbsp;</div>
	
	
	<!-- Check if the Shipment is International and if the packagetype is 'Package' OR 'Pallet' -->
	<s:if test="%{shippingOrder.isInternationalShipment==true && (#session.shippingOrder.packageTypeId.packageTypeId==3 || #session.shippingOrder.packageTypeId.packageTypeId==4)}">
	<div id="customInvoiceDetails">
		<s:include value="CustomsInvoiceDetails.jsp" />
	</div>
	</s:if>
	
	<div id="rates_list_div_top">
	<table width="970px">
	<tr>
	<td width="50%"><mmr:message messageId="label.rates.list"/></td>
	<td width="50%">
	<div id="rate_lst_btns">
	<table width="450px">
		<tr>
			<td width="40%" align="right">
		<td width="10%" align="right">	
            <s:a href="javascript: backToShipment();">
	            <img src="<%=request.getContextPath()%>/mmr/images/back.png" border="0"/>
	            &nbsp;<mmr:message messageId="label.navigation.back"/> </s:a>
		</td>
		</tr>
	</table>
</div>
	</td>
	</tr>
	</table>
	</div>
	


	<div id="rate_res"><mmr:message messageId="label.search.results"/></div>
	<div id="rate_results">	
	<s:if test="%{shippingOrder.rateList.size()>1}">
	<div id="rslt_stmnt"><br/><s:property value="shippingOrder.rateList.size()" /><mmr:message messageId="label.search.results.items"/></div>
	</s:if>
	<s:elseif test="%{shippingOrder.rateList.size()==1}">
	<div id="rslt_stmnt"><br/><s:property value="shippingOrder.rateList.size()" /><mmr:message messageId="label.search.results.item"/></div>
	</s:elseif>
	<s:else>
	<div id="rslt_stmnt"><br/><mmr:message messageId="label.search.results.noitems"/></div>
	</s:else>
	</div>	
	<s:set var="sortTotal">
	<s:if test="%{#session.SHIP_MODE=='SHIP'}">
    	<s:a href="/shiplinx/admin/shipment.stageThree.action"><mmr:message messageId="label.total.price"/>&nbsp;&nbsp;<img src="<%=request.getContextPath()%>/mmr/images/sort_icon.png" border="0"/></s:a>
    </s:if>
    <s:else>
    	<s:a href="/shiplinx/admin/shipment.stageThree.quote.action"><mmr:message messageId="label.total.price"/>&nbsp;&nbsp;<img src="<%=request.getContextPath()%>/mmr/images/sort_icon.png" border="0"/></s:a>
    </s:else>
	</s:set> 
	<s:set var="sortTotalCostCharge">
	<s:if test="%{#session.SHIP_MODE=='SHIP'}">
    	<s:a href="/shiplinx/admin/shipment.stageThree.action"><mmr:message messageId="label.total.costCharge"/>&nbsp;&nbsp;<img src="<%=request.getContextPath()%>/mmr/images/sort_icon.png" border="0"/></s:a>
    </s:if>
    <s:else>
    	<s:a href="/shiplinx/admin/shipment.stageThree.quote.action"><mmr:message messageId="label.total.costCharge"/>&nbsp;&nbsp;<img src="<%=request.getContextPath()%>/mmr/images/sort_icon.png" border="0"/></s:a>
    </s:else>	
	</s:set>
	<s:set var="sortCarrier">
    	<mmr:message messageId="label.track.carrier"/>&nbsp;&nbsp;<img src="<%=request.getContextPath()%>/mmr/images/sort_icon.png" border="0"/>
	</s:set>
	<s:set var="sortTransit">
    	<mmr:message messageId="label.transit.days"/>&nbsp;&nbsp;<img src="<%=request.getContextPath()%>/mmr/images/sort_icon.png" border="0"/>
	</s:set>
	<s:set var="requri">
		<s:if test="%{#session.SHIP_MODE=='SHIP'}">
			/admin/shipment.stageThree.action
		</s:if>
		<s:else>
			/admin/shipment.stageThree.quote.action
		</s:else>
	</s:set>
	<div id="rates_result_tbl">
		
		<display:table id="order_table"  name="shippingOrder.rateList" export="false" uid="row" cellspacing="0" sort="page" requestURI="${requri}">
		<display:column headerClass="srchinv_tableTitle2" property="id" title="#"/>
		<display:column headerClass="srchinv_tableTitle2" property="carrierName"  sortable="true" title="${sortCarrier}" />
		<display:column headerClass="srchinv_tableTitle2" property="serviceName" title="Service" />
		<display:column headerClass="srchinv_tableTitle2"  sortable="true" title="${sortTransit}">
			<s:if test="%{shippingOrder.rateList[#attr.row_rowNum-1].transitDaysMin > 0 && shippingOrder.rateList[#attr.row_rowNum-1].transitDaysMin != shippingOrder.rateList[#attr.row_rowNum-1].transitDays}">
				<s:property value="%{shippingOrder.rateList[#attr.row_rowNum-1].transitDaysMin}" /> to 
			</s:if>
			<s:property value="%{shippingOrder.rateList[#attr.row_rowNum-1].transitDays}" />
		</display:column>
		<display:column headerClass="srchinv_tableTitle2" property="billWeight" title="Bill Wt(LBS)" />
		<s:if test="%{#request.BillToType!=null}">
			<display:column headerClass="srchinv_tableTitle2" title="Bill To"> <s:property value="%{#request.BillToType}"/> </display:column>
		</s:if>
		<s:else>
			<s:if test="%{#session.ADMIN_USER != null}"> 
				<display:column headerClass="srchinv_tableTitle2" title="${sortTotalCostCharge}" >
				  <sx:tree  cssClass="text_01" label="<b>Total : $ %{shippingOrder.rateList[#attr.row_rowNum-1].totalCost} : $ %{shippingOrder.rateList[#attr.row_rowNum-1].total}</b>" >		  
					<s:iterator  value="%{shippingOrder.rateList[#attr.row_rowNum-1].charges}">
					 <sx:treenode cssClass="text_01" label="%{name} : %{tariffRate} : %{cost} : %{charge}" />
					</s:iterator>
					<sx:treenode cssClass="text_01" label="%{shippingOrder.rateList[#attr.row_rowNum-1].markupTypeText} : %{shippingOrder.rateList[#attr.row_rowNum-1].markupPercentage}%" />
				 </sx:tree>	 		  
				</display:column>
			</s:if>
			<s:else>
				<s:set name="total" value="%{shippingOrder.rateList[#attr.row_rowNum-1].total}" />
				<display:column headerClass="srchinv_tableTitle2" title="${sortTotal}"  sortable="true">
				  <sx:tree  cssClass="text_01" label="<b> $%{getText('format.money',{#total})}</b>" >		  
					<s:iterator  value="%{shippingOrder.rateList[#attr.row_rowNum-1].charges}">
					 <s:set name="chargeVal" value="%{charge}" />
					 <sx:treenode cssClass="text_01" label="%{name} : $%{getText('format.money',{#chargeVal})}" />
					</s:iterator>
				 </sx:tree>	 		  
				</display:column>
			</s:else>
		</s:else>

		<display:column headerClass="srchinv_tableTitle2" title="" sortable="true">
			<s:if test="%{shippingOrder.rateList[#attr.row_rowNum-1].loginURL != null}">
				<img onclick="javascript:submitShipment('<s:property value="#attr.row_rowNum-1" />','<s:url value="%{shippingOrder.rateList[#attr.row_rowNum-1].loginURL}"  />');" src="<%=request.getContextPath()%>/mmr/images/shipnow_btn.png" border="0" style="cursor: pointer;"/>				
			</s:if>
			<s:if test="%{shippingOrder.rateList[#attr.row_rowNum-1].loginURL == null}">
				<img onclick="javascript:submitShipment('<s:property value="#attr.row_rowNum-1" />','null');"  src="<%=request.getContextPath()%>/mmr/images/shipnow_btn.png" border="0" style="cursor: pointer;"/>				
			</s:if>
		</display:column>
		
		<display:column headerClass="srchinv_tableTitle2" title="" sortable="true" style="cursor: pointer;">
			<img onclick="javascript: sendCustomerEmail('<s:property value="#attr.row_rowNum-1" />');"  src="<%=request.getContextPath()%>/mmr/images/mail-notification.png" border="0" title="Email this Quote"/>	
		</display:column>
	</display:table>
	<s:hidden name="shippingOrder.rateIndex" />
	</div>
	<div id="rates_res_tbl_end"></div>


			</s:form> 
		</div>

	</body>
</html>