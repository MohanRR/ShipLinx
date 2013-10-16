<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="sj" uri="/struts-jquery-tags"%>
<%@ taglib prefix="mmr" uri="/mmr-tags"%>
<%@ taglib prefix="sx" uri="/struts-dojo-tags"%>

<html>
<head>
<title><s:text name="search.shipment.title" /></title>
<sj:head jqueryui="true" />
<sx:head />
</head>
<body>
<SCRIPT language="JavaScript">
var def_wh_id=0;
var product_id=0;

		function addActualCharge() {
			document.viewform.action = "add.actual.charge.shipment.action";
			document.viewform.submit();
		}
		function addQuotedCharge() {
			document.viewform.action = "add.quoted.charge.shipment.action";
			document.viewform.submit();
		}		
		function updateActualCharge() {
			document.viewform.action = "update.actual.charge.shipment.action";
			document.viewform.submit();
		}	
		function updateQuotedCharge() {
			document.viewform.action = "update.quoted.charge.shipment.action";
			document.viewform.submit();
		}	
		function clearExceptionStatus() {
			document.viewform.action = "clear.exception.status.action";
			document.viewform.submit();
		}		
		function processPayment() 
		{
			var submit = true;
			//if the checkbox is not null, then the storecc of the business is true.
			if(document.getElementById("storecc_id") != null)
			{
				if(!document.getElementById("storecc_id").checked)
				{
					alert("Please click the Note Check box to agree to the condition stated");
					submit = false;
				}
			}
			if(submit)
			{
				document.viewform.action = "processPayment.action";
				document.viewform.submit();
			}
		}		
		function generateLabel(id){
		//alert("label::"+id);
		var arrOrders = new Array();
		arrOrders[0] = id;
			var slcopies = document.getElementById("label_copies").value;
			if(document.getElementById("customsinv_copies")!=null) //for international shipments
	    	var ccopies = document.getElementById("customsinv_copies").value;
	    	else // for domestic shipments
	    	var ccopies = 0;
	    	var url="getShippingLabel.action?slcopies="+slcopies+"&cicopies="+ccopies+"&arrayOrders="+arrOrders;
	    	//alert(url);
			window.open(url,'','width=760,height=540,left=100,top=100,scrollbars=1');

		}
		function cancelShipment() {
			document.viewform.action = "cancelShipment.action";
			document.viewform.submit();
		}		
		function showPackage(id)
		{
			var tabContainer = dojo.widget.byId("order_detail_page");
     		tabContainer.selectTab(id);		
		}
		
		function showOrHideProductSummary(index,pid)
		{
			var divclss = "show_summ_"+index;
			var anchor = getElementsByClassName(divclss);
			var inner = anchor[0].innerHTML;
			var divid = "inner_div_"+pid;
			var cid= document.viewform.cid.value;
			if(inner != '[&nbsp;+&nbsp;]')
			{
				document.getElementById(divid).style.display = 'none';
				anchor[0].innerHTML = '[&nbsp;+&nbsp;]';
			}
			else
			{
				product_id=pid;
				//ajax implemantation
				 ajax_prods=ajaxFunction();
				ajax_prods.onreadystatechange=function()
				  {
					   if(ajax_prods.readyState==4)
						{
						reponse=ajax_prods.responseText;
						js_innerdiv=document.getElementById(divid);
						js_innerdiv.innerHTML= reponse;
						}
				  }
				  	url="goToProductInventory.action?productId="+pid+"&cid="+cid+"&productsummary=true";
					ajax_prods.open("GET",url,true);
					ajax_prods.send(this);
					
				document.getElementById(divid).style.display = 'block';
				anchor[0].innerHTML = '[&nbsp;-&nbsp;]';
			}
		}
		
		function populateBatches(pid, index)
		{
			var sid="wiplocs_"+pid+"_"+index;
			var locid=document.getElementById(sid).value;
			var vid = "b_"+pid+"_"+index;
			callBatchesAjaxFunc(locid, vid, pid, index);
		}
		
		function callBatchesAjaxFunc(key, tdid, prodid, index)
		{
			ajax_batches=ajaxFunction();
			ajax_batches.onreadystatechange=function()
		  	{
			   if(ajax_batches.readyState==4)
				{
				reponse=ajax_batches.responseText;
				js_batchByLocId=document.getElementById(tdid);
				js_batchByLocId.innerHTML= reponse;
				}
		  	}
		 	url="<%=request.getContextPath()%>/admin/listBatchesByLocId.action?locId="+key+"&prodid="+prodid+"&index="+index;
		  	ajax_batches.open("GET",url,true);
		  	ajax_batches.send(this);
			
		}
		
		function moveProductToWIP(wid,ix,fromlocId,frombatch)
		{
			//alert("Product Id::"+product_id);
			var errorcount=0;
			var divid = "inner_div_"+product_id;
			var cid= document.viewform.cid.value;
			//alert("Product Id::"+product_id);
			//alert("Index::"+ix);
			var qtyid = "qty_"+product_id+"_"+ix;
			var qty = document.getElementById(qtyid).value;
			//alert("after qty");
			if(qty=='')
			{
				alert("Please enter the Quantity of Units to Move");
				return false;
				errorcount++;
			}
				
			var wipslocid = "wiplocs_"+product_id+"_"+ix;
			var wipsloc = document.getElementById(wipslocid).value;
			if(wipsloc=='-1')
			{
				alert("Please select the WIP Location to Move");
				return false;
				errorcount++;
			}
			var batchid = "batches_"+product_id+"_"+ix;
			var batchloc = document.getElementById(batchid).value;
			
			//alert("Quantity::"+qty);
			//alert("Warehouse ID::"+wid);
			//alert("From Location ID::"+fromlocId);
			//alert("To Location Id::"+wipsloc);
			//alert("To Batch Id::"+batchloc);	
			//alert("From Batch Id::"+frombatch);	
			if(errorcount==0)
			//Call Ajax implementation to move product in Inventory
			{
				//ajax implemantation
				 ajax_prods=ajaxFunction();
				ajax_prods.onreadystatechange=function()
				  {
					   if(ajax_prods.readyState==4)
						{
						reponse=ajax_prods.responseText;
					//	alert(reponse);
						//alert(divid);
						js_innerdiv=document.getElementById(divid);
						//js_innerdiv.innerHTML= "123";
						js_innerdiv.innerHTML= reponse;
						}
				  }
				  	url="moveProductInInventoryAjax.action?flocid="+fromlocId+"&twhid="+wid+"&tlocid="+wipsloc+"&qty="+qty+"&batch="+frombatch+"&batchto="+batchloc+"&productId="+product_id+"&cid="+cid+"&productsummary=true";
					ajax_prods.open("GET",url,true);
					ajax_prods.send(this);
			}
		}
			
	function showBillingState() {
	ajax_Country=ajaxFunction();
	ajax_Country.onreadystatechange=function()
	  {
		   if(ajax_Country.readyState==4)
			{
			reponse=ajax_Country.responseText;
			js_stateid=document.getElementById("billingstateid");
			js_stateid.innerHTML= reponse;
			}
	  }
		firstBox = document.getElementById('billingCountry');
	  	url="<%=request.getContextPath()%>/customer.listProvince.action?value="+firstBox.value;
		//param="objName=ref_state&country_id="+country_id;
		ajax_Country.open("GET",url,true);
		ajax_Country.send(this);
	} // End function showState()

	window.onload = showAutoPrintLabel;

	function showAutoPrintLabel()
	{
		//alert(document.viewform.orderId.value);
		var autoprint = "<%=request.getAttribute("autoprint")%>";
		var popup = "<%=request.getAttribute("nopopup")%>";
		//alert(popup);
		if(autoprint == 'true' && popup != 'null')
		{
			//alert("Auto Print");
			generateLabel(document.viewform.orderId.value);	
		}
	}


	function copyToActual()
		{
			if(document.getElementById("copy_to_actual").value=='')
				alert("Please choose status option for actual charges")
			else{
				if(confirm("Would you like to copy these charges into the actual charges?"))
				if(document.getElementById("copy_to_actual").value=='')			
					alert("Please choose status option for actual charges");
				else
				{
					document.viewform.action = "copy.actual.charge.action";
					document.viewform.submit();
				}
			}
		}
			
	</SCRIPT>

<div id="messages"><jsp:include
	page="../common/action_messages.jsp" /></div>

<div class="form-container"><s:form id="viewform"
	action="update.charge.shipment.action" name="viewform">
	<s:hidden name="cid" value="%{selectedOrder.customer.id}"/>
	<s:token/>
	<s:if test="%{#session.ROLE.contains('busadmin')}"> <!-- Condition to display the Back button -->
		<div id="div_back_link">
			&nbsp;&nbsp;
			<img src="<s:url value="/mmr/images/back.png" includeContext="true" />"
					border="0">&nbsp;					
			<a href="list.shipment.action" style="cursor: pointer;"><mmr:message messageId="label.navigation.back.results"/></a>&nbsp;
			&nbsp;
		</div>
	</s:if>
	<s:set name="status">
	<s:if test="%{#request.notrackurl == 'true'}">
		add_info
	</s:if>
	<s:else>
		orderDetail
	</s:else>
	</s:set>
	<!-- Start change: Sumanth Kulkarni -  -->
	<sx:tabbedpanel id="order_detail_page" cssStyle="margin-left: 90px;" selectedTab="%{status}">
		<sx:div id="orderDetail" label="Order Details">
		  	<s:if test="%{selectedOrder.paymentRequired==true}"> 
				<s:token/>
				<!-- Condition to Show or Hide the Payment Panel , Hides: If the Customer need not Pay - Shows: If the Customer is required to make the Payment.-->
				<div id="payment_rqd_top">&nbsp;&nbsp;Payment Required:
				<div id="payment_actions_imgs">
				<table>
					<tr>
						<td><img
							src="<s:url value="/mmr/images/DataGrid_headerSeparatorSkin.png" includeContext="true" />"
							border="0">&nbsp;</td>
						<td><img
							src="<s:url value="/mmr/images/money-coin.png" includeContext="true" />"
							border="0">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
						<td><img
							src="<s:url value="/mmr/images/DataGrid_headerSeparatorSkin.png" includeContext="true" />"
							border="0">&nbsp;</td>
						<td><img
							src="<s:url value="/mmr/images/money-coin.png" includeContext="true" />"
							border="0">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
					</tr>
				</table>
				</div>
				<div id="payment_actions">
				<table width="260px">
					<tr>
						<td>&nbsp;&nbsp;<a href="javascript: processPayment()"><mmr:message
							messageId="label.pay.now" /></a>
						</td>
						<td><a href="backToShipment.action"><mmr:message
							messageId="label.shipment.edit" /></a>
						</td>
					</tr>
				</table>
				</div>
				</div>
				<div id="payment_rqd_table">
				<div id="payment_info"><img
					src="<s:url value="/mmr/images/information.png" includeContext="true" />"
					border="0"> &nbsp;&nbsp;<mmr:message
					messageId="label.payment.needed" /></div>

				<table width="950px" cellpadding="2" cellspacing="0">
					<tr>
						<td width="11%" class="add_cust_tbl_cols"><mmr:message	messageId="label.creditcard.number" />:</td>
						<td width="10%"><s:textfield size="24" key="creditCard.ccNumber" name="selectedOrder.creditCard.ccNumber" cssClass="text_02_tf" /></td>
						<td width="3%">&nbsp;</td>
						<td width="12%" class="add_cust_tbl_cols"><mmr:message
							messageId="label.creditcard.expiryMonth" />:</td>
						<td width="10%"><s:select required="true"
							list="#{'01':'Jan', '02':'Feb', '03':'Mar', '04':'Apr', '05':'May', '06':'Jun', '07':'Jul', '08':'Aug', '09':'Sep', '10':'Oct', '11':'Nov', '12':'Dec'}"
							key="creditCard.ccExpiryMonth"
							name="selectedOrder.creditCard.ccExpiryMonth"
							cssClass="text_01_combo_medium" /></td>
						<td width="3%">&nbsp;</td>
						<td width="10%" class="add_cust_tbl_cols"><mmr:message messageId="label.creditcard.expiryYear" />:</td>
						<td width="10%"><s:select list="#{'2012':'2012', '2013':'2013', '2014':'2014', '2015':'2015', '2016':'2016', '2017':'2017', '2018':'2018', '2019':'2019', '2020':'2020'}"
							key="creditCard.ccExpiryYear" name="selectedOrder.creditCard.ccExpiryYear" cssClass="text_01_combo_medium" /></td>
					</tr>
					<tr>
						<td width="10%" class="add_cust_tbl_cols"><mmr:message
							messageId="label.creditcard.cvdCode" />:</td>
						<td width="10%"><s:textfield size="5" key="creditCard.cvd"
							name="selectedOrder.creditCard.cvd" cssClass="text_02_tf_small" />
						</td>
						<td width="3%">&nbsp;</td>
						<td width="10%" class="add_cust_tbl_cols" align="left"><mmr:message
							messageId="label.pay.amount" />:</td>
						<td width="10%" class="ordrdtl_title_val" style="text-align: left; border-bottom-style: none;"><s:property
							value="selectedOrder.totalChargeQuoted" /></td>
						<td width="3%">&nbsp;</td>
					</tr>
					<tr>
			    <td width="10%" class="add_cust_tbl_cols"><mmr:message messageId="label.creditcard.nameOnCard"/>:</td>
			    <td width="10%"><s:textfield size="24" key="selectedOrder.creditCard.billingAddress.contactName" name="selectedOrder.creditCard.billingAddress.contactName" cssClass="text_02_tf"/></td>
			    <td width="3%">&nbsp;</td>
			    <td width="10%" class="add_cust_tbl_cols"><mmr:message messageId="label.creditcard.billingAddress1"/>:</td>
			    <td width="10%"><s:textfield size="24" key="selectedOrder.creditCard.billingAddress.address1" name="selectedOrder.creditCard.billingAddress.address1" cssClass="text_02_tf"/></td>
			    <td width="3%">&nbsp;</td>
			    <td width="10%" class="add_cust_tbl_cols"><mmr:message messageId="label.creditcard.billingAddress2"/>:</td>
			    <td width="10%"><s:textfield size="24" key="selectedOrder.creditCard.billingAddress.address2" name="selectedOrder.creditCard.billingAddress.address2" cssClass="text_02_tf" cssStyle="width: 130px;"/></td>
			</tr>
	  		<tr>
			    <td width="10%" class="add_cust_tbl_cols"><mmr:message messageId="label.creditcard.billingCity"/>:</td>
			    <td width="10%"><s:textfield size="24" key="selectedOrder.creditCard.billingAddress.city" name="selectedOrder.creditCard.billingAddress.city" cssClass="text_02_tf"/></td>
			    <td width="3%">&nbsp;</td>
			    <td width="10%" class="add_cust_tbl_cols"><mmr:message messageId="label.creditcard.billingPostalCode"/>:</td>
			    <td width="10%"><s:textfield size="24" key="selectedOrder.creditCard.billingAddress.postalCode" name="selectedOrder.creditCard.billingAddress.postalCode" cssClass="text_02_tf"/></td>
			    <td width="3%">&nbsp;</td>
			    <td width="10%" class="add_cust_tbl_cols"><mmr:message messageId="label.creditcard.billingCountry"/>:</td>
			    <td width="10%">						
	    			<s:select cssClass="text_01_combo_big" cssStyle="width:135px;" listKey="countryCode" listValue="countryName" 
								name="selectedOrder.creditCard.billingAddress.countryCode" headerKey="-1"  list="#session.CountryList" 
									onchange="javascript:showBillingState();"  id="billingCountry" theme="simple"/>
	  			</td>
	  		</tr>
	  		<tr>
			    <td width="10%" class="add_cust_tbl_cols"><mmr:message messageId="label.creditcard.billingProvince"/>:</td>
			    <td id="billingstateid" width="10%">						
			    	<s:select key="selectedOrder.creditCard.billingAddress.provinceCode" name="selectedOrder.creditCard.billingAddress.provinceCode"  cssClass="text_01_combo_big" cssStyle="width:135px;" 
										listKey="provinceCode" listValue="provinceName" list="#session.provinces"/>
			  	</td>
			    <td width="3%">&nbsp;</td> 
			    <td width="10%"></td>
			    <td width="10%"></td>
			    <td width="3%">&nbsp;</td> 
			    <td colspan="2">&nbsp;</td>
	  		</tr>
			</table>
			<s:if test="%{selectedOrder.business.storeCC == true}">
				<div id="note_div"><s:checkbox name="storecc" id="storecc_id"/>&nbsp;<mmr:message messageId="label.storecc.note"/> </div>
			</s:if>	
				</div>
				<div id="payment_rqd_end">&nbsp;</div>
	 		</s:if> 
			<s:hidden name="orderId" value="%{selectedOrder.id}" /> 
			<s:set name="oid" value="%{selectedOrder.id}" /> 
			<div id="order_detail_heading">&nbsp;&nbsp;Pick Up
			From:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<font color="#000066" size="3"
				style="font: Arial; font-variant: small-caps">Order Details
			for #&nbsp;<s:property value="%{selectedOrder.id}" /></font>
			<s:if test="%{selectedOrder.statusId!=40}"> <!-- Condition to check if the Cancel Shipment should be displayed or no  -->
				<div id="cancel_shipment">
				<img
					src="<s:url value="/mmr/images/DataGrid_headerSeparatorSkin.png" includeContext="true" />"
					border="0" style="margin-bottom: -3px;">&nbsp;
				<img
					src="<s:url value="/mmr/images/cancel.png" includeContext="true" />"
					border="0" style="margin-bottom: -3px;">	
				<s:a href="javascript:cancelShipment()" cssStyle="text-decoration: none;">Cancel Shipment</s:a>
				</div>
			</s:if>
			<div id="vw_shpmnt_nxt">
			<img
				src="<s:url value="/mmr/images/DataGrid_headerSeparatorSkin.png" includeContext="true" />"
				border="0" style="margin-bottom: -3px;">&nbsp; <a href="#"
				class="nextTab"><img
				src="<s:url value="/mmr/images/next.png" includeContext="true" />"
				border="0" style="margin-bottom: -3px;"></a>&nbsp;&nbsp;<a
				href="javascript: showPackage('Package');"
				style="text-decoration: none;"><mmr:message
				messageId="label.navigation.next" /></a> 
			</div>
			</div>
			<div id="order_detail_from_to_table">
			<table cellpadding="3" cellspacing="0">
				<tr>
					<td class="ordrdtl_title" width="8%">Company:</td>
					<td class="ordrdtl_title_val" width="30%"><s:property
						value="%{selectedOrder.fromAddress.abbreviationName}" /></td>
					<td width="1%">&nbsp;</td>
					<td class="ordrdtl_title" width="8%">Address:</td>
					<td class="ordrdtl_title_val" width="35%">
						<s:property value="%{selectedOrder.fromAddress.address1}" />:&nbsp; 
						<s:property	value="%{selectedOrder.fromAddress.city}" /> , 
						<s:property	value="%{selectedOrder.fromAddress.provinceCode}" /> , 
						<s:property	value="%{selectedOrder.fromAddress.postalCode}" />, 
						<s:property	value="%{selectedOrder.fromAddress.countryName}" />
					</td>
					<td width="1%">&nbsp;</td>
					<td width="5%">&nbsp;</td>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td class="ordrdtl_title">Phone:</td>
					<td class="ordrdtl_title_val"><s:property
						value="%{selectedOrder.fromAddress.phoneNo}" /></td>
					<td width="1%">&nbsp;</td>
					<td class="ordrdtl_title">Email:</td>
					<td class="ordrdtl_title_val"><s:property
						value="%{selectedOrder.fromAddress.emailAddress}" /></td>
					<td width="1%">&nbsp;</td>
					<td class="ordrdtl_title">Attn:</td>
					<td class="ordrdtl_title_val"><s:property
						value="%{selectedOrder.fromAddress.contactName}" /></td>
				</tr>
			</table>
			</div>

			<div id="payment_rqd_end">&nbsp;</div>

			<div id="order_detail_to_table_hdng">
				<table width="970px">
					<tr>
						<td>Ship To:</td>
						<td align="right">
				<s:if test="%{selectedOrder.paymentRequired!=true}">
				<!-- Condition to Show or Hide the Generate Label Link, Hides: If Customer has not paid - Shows: If the Customer has made the Payment-->
				<font class="srchshipmnt_text_03"><mmr:message messageId="label.copies.shipping.label"/></font>&nbsp;
				<s:if test="%{#request.no_of_lbls != null}">
					<s:select id="label_copies" cssStyle="width:39px;" cssClass="text_01_combo_small" list="#{'0':'0','1':'1','2':'2','3':'3','4':'4','5':'5','6':'6','7':'7','8':'8','9':'9','10':'10'}" value="%{#request.no_of_lbls}"/>
				</s:if>
				<s:else>
					<s:select id="label_copies" cssStyle="width:39px;" cssClass="text_01_combo_small" list="#{'0':'0','1':'1','2':'2','3':'3','4':'4','5':'5','6':'6','7':'7','8':'8','9':'9','10':'10'}" value="1"/>
				</s:else>
				&nbsp;
				<s:if test="%{selectedOrder.isInternationalShipment==true}">
				<font class="srchshipmnt_text_03">
				<mmr:message messageId="label.copies.customsinvoice"/></font>&nbsp;
				<s:if test="%{#request.no_of_ci != null}">
					<s:select id="customsinv_copies" cssStyle="width:39px;" cssClass="text_01_combo_small" list="#{'0':'0','1':'1','2':'2','3':'3','4':'4','5':'5','6':'6','7':'7','8':'8','9':'9','10':'10'}" value="%{#request.no_of_ci}"/>
				</s:if>
				<s:else>
					<s:select id="customsinv_copies" cssStyle="width:39px;" cssClass="text_01_combo_small" list="#{'0':'0','1':'1','2':'2','3':'3','4':'4','5':'5','6':'6','7':'7','8':'8','9':'9','10':'10'}" value="3"/>
				</s:else>
				</s:if>
				&nbsp;
				<img
					src="<s:url value="/mmr/images/DataGrid_headerSeparatorSkin.png" includeContext="true" />"
					border="0" style="margin-bottom: -3px;">&nbsp; <s:a
					href="javascript:generateLabel('%{#oid}')"
					cssStyle="text-decoration: none;">
				<img
						src="<s:url value="/mmr/images/generate_label.png" includeContext="true" />"
						border="0" style="margin-bottom: -3px;">&nbsp;&nbsp;Print Label</s:a>
				</s:if>				
				</td>
					</tr>
				</table>
			</div>
			<div id="order_detail_from_to_table">
			<table cellpadding="3" cellspacing="0">
				<tr>
					<td class="ordrdtl_title" width="8%">Company:</td>
					<td class="ordrdtl_title_val" width="30%"><s:property
						value="%{selectedOrder.toAddress.abbreviationName}" /></td>
					<td width="1%">&nbsp;</td>
					<td class="ordrdtl_title" width="8%">Address:</td>
					<td class="ordrdtl_title_val" width="35%">
						<s:property value="%{selectedOrder.toAddress.address1}" />:&nbsp; 
						<s:property	value="%{selectedOrder.toAddress.city}" /> , 
						<s:property	value="%{selectedOrder.toAddress.provinceCode}" /> , 
						<s:property	value="%{selectedOrder.toAddress.postalCode}" />, 
						<s:property	value="%{selectedOrder.toAddress.countryName}" />
						</td>
					<td width="1%">&nbsp;</td>
					<td width="5%">&nbsp;</td>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td class="ordrdtl_title">Phone:</td>
					<td class="ordrdtl_title_val"><s:property
						value="%{selectedOrder.toAddress.phoneNo}" /></td>
					<td width="1%">&nbsp;</td>
					<td class="ordrdtl_title">Email:</td>
					<td class="ordrdtl_title_val"><s:property
						value="%{selectedOrder.toAddress.emailAddress}" /></td>
					<td width="1%">&nbsp;</td>
					<td class="ordrdtl_title">Attn:</td>
					<td class="ordrdtl_title_val"><s:property
						value="%{selectedOrder.toAddress.contactName}" /></td>
				</tr>
			</table>
			</div>

			<div id="payment_rqd_end">&nbsp;</div>

			<div id="order_detail_div_start">&nbsp;&nbsp;Order Details:</div>
			<div id="order_detail_table">
			<table width="940px" cellpadding="5" cellspacing="0">
				<tr>
					<td width="12%" class="ordrdtl_title">Customer</td>
					<td class="ordrdtl_title_val" width="16%"><s:property
						value="%{selectedOrder.customer.name}" /></td>
					<td width="1%">&nbsp;</td>
					<td class="ordrdtl_title" width="10%">Zone From / To</td>
					<td class="ordrdtl_title_val" width="16%"><s:property
						value="%{selectedOrder.fromZone}" /> / <s:property
						value="%{selectedOrder.toZone}" /></td>
					<td width="1%">&nbsp;</td>
					<td class="ordrdtl_title" width="13%">Currency</td>
					<td class="ordrdtl_title_val" width="15%"><s:property
						value="%{selectedOrder.currency}" /></td>
				</tr>
				<tr>
					<td class="ordrdtl_title">Carrier</td>
					<td class="ordrdtl_title_val"><s:property
						value="%{selectedOrder.service.masterCarrier.name}" /></td>
					<td width="1%">&nbsp;</td>
					<td class="ordrdtl_title">Service</td>
					<td class="ordrdtl_title_val"><s:property
						value="%{selectedOrder.service.name}" /></td>
					<td width="1%">&nbsp;</td>
					<td class="ordrdtl_title">Pick up Confirmation#</td>
					<td class="ordrdtl_title_val"><s:property
						value="%{selectedOrder.pickup.confirmationNum}" /></td>
				</tr>
				<tr>
					<td class="ordrdtl_title" align="left" valign="middle">Carrier
					Tracking #</td>
					<td class="ordrdtl_title_val" align="left" valign="middle"><strong><s:property
						value="%{selectedOrder.masterTrackingNum}" /></strong></td>
					<td width="1%">&nbsp;</td>
					<td class="ordrdtl_title">Reference 1</td>
					<td class="ordrdtl_title_val"><s:property
						value="%{selectedOrder.referenceCode}" /></td>
					<td width="1%">&nbsp;</td>
					<td class="ordrdtl_title">Reference 2</td>
					<td class="ordrdtl_title_val"><s:property
						value="%{selectedOrder.referenceOne}" /> <s:property
						value="%{selectedOrder.referenceTwo}" /></td>
				</tr>
				<tr>
					<td class="ordrdtl_title">Weight (Entered / Billed)</td>
					<td class="ordrdtl_title_val">
						<s:property	value="%{selectedOrder.quotedWeight}" /> <s:property value="%{selectedOrder.quotedWeightUOM}"/> 
						<s:if test="%{selectedOrder.billedWeight > 0}">
							/ <s:property	value="%{selectedOrder.billedWeight}" /> <s:property value="%{selectedOrder.billedWeightUOM}"/>
						</s:if>
						<s:if test="%{selectedOrder.ratedAsWeight > 0}">
							<br>Rated As: <s:property value="%{selectedOrder.ratedAsWeight}"/> <s:property value="%{selectedOrder.billedWeightUOM}"/>
						</s:if>
					</td>
					<td width="1%">&nbsp;</td>
					<td nowrap="nowrap" class="ordrdtl_title">Shipment Date</td>
					<td nowrap="nowrap" align="left" class="ordrdtl_title_val"><strong><s:property
						value="%{selectedOrder.scheduledShipDate}" /></strong></td>
					<td width="1%">&nbsp;</td>
					<td class="ordrdtl_title">Billing Status</td>
					<td class="ordrdtl_title_val"><s:property
						value="%{selectedOrder.billingStatusText}" />
						<s:if test="%{selectedOrder.billToType =='Third Party' || selectedOrder.billToType=='Collect'}">
							: <s:property value="%{selectedOrder.billToAccountNum}" />
						</s:if>
					</td>
				</tr>
				<tr>
					<td class="ordrdtl_title">Status</td>
					<td class="ordrdtl_title_val"><s:property
						value="%{selectedOrder.statusName}" /></td>
					<td width="1%">&nbsp;</td>
					
					<s:if test="%{#session.ROLE.contains('busadmin') && selectedOrder.statusId==50}"> 
						<td class="ordrdtl_title">
							<s:a 
									href="javascript: clearExceptionStatus()" 
									cssStyle="text-decoration: none;">Clear Exception
							</s:a>									
						</td>
						<td width="20%"> <s:select cssClass="text_01" cssStyle="width:158px;" listKey="id" listValue="name" name="status_id" list="#session.orderStatusList" headerKey="-1"  id="status_id" theme="simple"/></td>

						<!--
						<td>
							<s:select cssClass="text_01_combo_biggest" cssStyle="width:215px;" 
									listKey="id" listValue="name" name="selectedOrder.statusId" 
									list="#session.orderStatusList" headerKey="-1"  id="new_status_id" 
									theme="simple"/></td>								
						</td>
						-->
						<td width="1%">&nbsp;</td>						
				
					</s:if>					
					
					
					<s:if test="%{#session.ROLE.contains('busadmin')}"> 
						<s:if test="%{selectedOrder.markType == 1}">
							<td class="ordrdtl_title">Mark-up Applied</td>
						</s:if>
						<s:if test="%{selectedOrder.markType == 2}">
							<td class="ordrdtl_title">Mark-down Applied</td>
						</s:if>
						
						<td class="ordrdtl_title_val"><s:property
							value="%{selectedOrder.markPercent}" /> %</td>
						<td width="1%">&nbsp;</td>		
						
									
					</s:if>
				</tr>
			</table>
			</div>

			<div id="payment_rqd_end">&nbsp;</div>


			<div id="charges_div_table"><s:if
				test="%{selectedOrder.quotedCharges.size > 0}">
				<!-- Condition to check if results exist for Quoted Charges, If yes, display the Quoted Charges, else Dont Display -->
				<div id="charges_div">&nbsp;Quote&nbsp;Charges:&nbsp;&nbsp;&nbsp;<font
					color="#000066" size="2" style="font: Arial;">$<s:property
					value="%{selectedOrder.totalChargeQuoted}" /></font>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					<s:if test="%{selectedOrder.actualCharges.size ==0 && #session.ROLE.contains('busadmin')}">
					<div id="copy2actual">
						<table>
							<tr>
								<td><a href="javascript: copyToActual();"  style="text-decoration: none;">Copy to Actual</a></td>
								<td class="ordrdtl_title_val"><s:select id="copy_to_actual"  
													cssClass="text_01_combo_big" cssStyle="width:140px;"
														name="quotedChargeStatusText"
															list="{'','Pending Release','Ready to Invoice','Quick Invoice'}" theme="simple" /></td>
							</tr>
						</table>
					</div>
					</s:if>
				</div>
						<table width="940px" cellpadding="2" cellspacing="0"
							style="margin-left: 10px;">
							<tr>
								<td class="ordrdtl_title_hdng"><strong>Name</strong></td>
								<s:if test="%{#session.ROLE.contains('busadmin')}">
									<td class="ordrdtl_title_hdng"><strong>Cost</strong></td>
									<td class="ordrdtl_title_hdng"><strong>Tariff</strong></td>
								</s:if>
								<td class="ordrdtl_title_hdng"><strong>Charge</strong></td>
								<td class="ordrdtl_title_hdng"><strong>Status</strong></td>
								<td class="ordrdtl_title_hdng" align="center">&nbsp;</td>
							</tr>
							<tr>
								<td valign="top"><s:iterator id="quotedCharges"
									value="selectedOrder.quotedCharges" status="row">
									<tr>
										<!-- status=30=Billed -- >
										<!-- Unbilled charges will only be displayed to Business Admin -->

										<s:if test="%{status != 30 && #session.ROLE.contains('busadmin')}">
											<td class="ordrdtl_title_val">
												<s:textfield size="30"
													key="quotedChargeName" name="quotedChargeName"
													value="%{name}" cssClass="text_02" />
											</td>
											<td class="ordrdtl_title_val"><s:textfield size="5"
												key="quotedChargeCost" name="quotedChargeCost"
												value="%{cost}" cssClass="text_02" /></td>
											<td class="ordrdtl_title_val">
												<s:property value="tariffRate" />
											</td>
											<td class="ordrdtl_title_val"><s:hidden
												name="quotedChargeIds" value="%{id}" /> <s:textfield
												size="5" key="quotedCharge" name="quotedCharge"
												value="%{charge}" cssClass="text_02" /></td>
												
											<td class="ordrdtl_title_val">
												<s:select value="%{statusText}"
													cssClass="text_01_combo_big" cssStyle="width:140px;"
														name="quotedChargeStatusText"
															list="{'Pending Release','Ready to Invoice'}" theme="simple" />
											</td>	
											<td class="ordrdtl_title_val">
												<s:a onclick="return confirm('Do you really want to delete the selected charge?')" href="delete.quoted.charge.shipment.action?method=deletetCharge&id=%{id}">
													<img src="<s:url value="/mmr/images/delete.gif" includeContext="true" />" alt="Delete Charge" border="0"> 
												</s:a>
											</td>												
												
										</s:if>
										<s:else>
											<td class="ordrdtl_title_val"><s:property value="name" /></td>
											<s:if test="%{#session.ROLE.contains('busadmin')}">
												<td class="ordrdtl_title_val"><s:property value="cost" /></td>
											<td class="ordrdtl_title_val"><s:property
												value="tariffRate" /></td>											
											</s:if>
											<td class="ordrdtl_title_val"><s:property value="charge" /></td>
											<td class="ordrdtl_title_val">
												<s:property	value="statusText" />
											</td>
											<td></td>
										</s:else>

									</tr>
								</s:iterator></td>
							</tr>
							<s:if test="%{selectedOrder.quotedCharges.size()>0 && status != 30 && #session.ROLE.contains('busadmin')}">
							<tr>							
									<td align="left" colspan="6" class="ordrdtl_title_val"><a
										href="javascript: updateQuotedCharge()">
										<img border="0" src="<s:url value="/mmr/images/update_charge_btn.png" includeContext="true" />"	>
										</a></td>
									
							</tr>
							</s:if>
							<s:if test="%{#session.ROLE.contains('busadmin')}">
							<tr>
									<td class="ordrdtl_title" colspan="4"><strong>New Charge:</strong></td>
							</tr>
							<tr>
									<s:url id="carrierChargesList" action="listCarrierCharges" />
									<td class="ordrdtl_title_val"><sx:autocompleter keyName="newQuotedCharge.chargeId"
										name="searchString" href="%{carrierChargesList}"
										dataFieldName="carrierChargesSearchResult"
										cssStyle="width:190px;" loadOnTextChange="true"
										loadMinimumCount="3" />
									</td>
									<td class="ordrdtl_title_val">
										<s:textfield size="7"
										key="newQuotedCharge.cost" name="newQuotedCharge.cost"
										cssClass="text_02" />
									</td>
									<td>&nbsp;</td>
									<td class="ordrdtl_title_val">
										<s:textfield size="7"
										key="newQuotedCharge.charge" name="newQuotedCharge.charge"
										cssClass="text_02" />
									</td>
									<td class="ordrdtl_title_val"><s:select
										value="%{newQuotedCharge.statusText}" id="status"
										cssClass="text_01_combo_big" cssStyle="width:140px;"
										name="newQuotedCharge.statusText"
										list="{'Pending Release','Ready to Invoice'}" theme="simple" />
									</td>
									<td class="ordrdtl_title_val"><a
										href="javascript: addQuotedCharge()"><img border="0"
										src="<s:url value="/mmr/images/add_product.png" includeContext="true" />"
										></a></td>
								</tr>
								
							</s:if>
							
							</table>
							<s:if test="%{#session.ROLE.contains('busadmin')}">
								<table width="940px" cellpadding="3" cellspacing="0" style="margin-left: 10px;">
								
								
							</table>
						</s:if>						
			</s:if> 
			<!-- Condition to check if results exist for Actual Charges, If yes, display the Actual Charges, else Dont Display -->
				<div id="charges_div">&nbsp;Actual&nbsp;Charges:&nbsp;&nbsp; </div>
						<table width="940px" cellpadding="3" cellspacing="0"
							style="margin-left: 20px;">
							
							<tr>
								<td class="ordrdtl_title_hdng" width="20%"><strong>Name</strong></td>
								<s:if test="%{#session.ROLE.contains('busadmin')}">
									<td class="ordrdtl_title_hdng" width="10%"><strong>Cost</strong></td>
									<td class="ordrdtl_title_hdng" width="10%"><strong>Tariff</strong></td>
								</s:if>
								<td class="ordrdtl_title_hdng" width="12%"><strong>Charge</strong></td>
								<td class="ordrdtl_title_hdng" width="14%"><strong>Status</strong></td>
								<td class="ordrdtl_title_hdng" width="12%"><strong>Invoice#</strong></td>
								<s:if test="%{#session.ROLE.contains('busadmin')}">
									<td class="ordrdtl_title_hdng" width="12%"><strong>EDI#</strong></td>
								</s:if>
							</tr>
							<tr>
								<td valign="top"><s:iterator id="actualCharges"
									value="selectedOrder.actualCharges" status="row">
									<tr>
										<!-- status=30=Billed -- >
										<!-- Unbilled charges will only be displayed to Business Admin -->

										<s:if test="%{status != 30 && #session.ROLE.contains('busadmin')}">
											<td class="ordrdtl_title_val">
												<s:textfield size="30"
													key="actualChargeName" name="actualChargeName"
													value="%{name}" cssClass="text_02" />
											</td>
											<td class="ordrdtl_title_val"><s:textfield size="5"
												key="actualChargeCost" name="actualChargeCost"
												value="%{cost}" cssClass="text_02" /></td>
											<td class="ordrdtl_title_val">
												<s:property value="tariffRate" />
											</td>
											<td class="ordrdtl_title_val"><s:hidden
												name="actualChargeIds" value="%{id}" /> <s:textfield
												size="5" key="actualCharge" name="actualCharge"
												value="%{charge}" cssClass="text_02" /></td>
											<td class="ordrdtl_title_val">
												<s:select value="%{statusText}"
													cssClass="text_01_combo_big" cssStyle="width:140px;"
														name="actualChargeStatusText"
															list="{'Pending Release','Ready to Invoice'}" theme="simple" />
											</td>	
											<td class="ordrdtl_title_val">
												<s:property value="invoiceNum" />
											</td>
											<s:if test="%{#session.ROLE.contains('busadmin')}">
												<td class="ordrdtl_title_val"><s:property value="ediInvoiceNumber" /></td>
											</s:if>
											<td>
												<s:a onclick="return confirm('Do you really want to delete the selected charge?')" href="delete.actual.charge.shipment.action?method=deletetCharge&id=%{id}">
													<img src="<s:url value="/mmr/images/delete.gif" includeContext="true" />" alt="Delete Charge" border="0"> 
												</s:a>
											</td>
										</s:if>
										<s:else>
											<td class="ordrdtl_title_val"><s:property value="name" /></td>
											<s:if test="%{#session.ROLE.contains('busadmin')}">
												<td class="ordrdtl_title_val"><s:property value="cost" /></td>
											<td class="ordrdtl_title_val">
												<s:property value="tariffRate" />
											</td>
											</s:if>
											<td class="ordrdtl_title_val"><s:property value="charge" /></td>
											<td class="ordrdtl_title_val">
												<s:property	value="statusText" />
											</td>
											<td class="ordrdtl_title_val">
												<s:property value="invoiceNum" />
											</td>	
											<s:if test="%{#session.ROLE.contains('busadmin')}">
												<td class="ordrdtl_title_val"><s:property value="ediInvoiceNumber" /></td>
											</s:if>
											<td></td>
											
										</s:else>


									</tr>
								</s:iterator></td>
							</tr>
							<s:if test="%{selectedOrder.actualCharges.size()>0 && status != 30 && #session.ROLE.contains('busadmin')}">
							<tr>							
									<td align="left" colspan="7" class="ordrdtl_title_val"><a
										href="javascript: updateActualCharge()"><img border="0"
										src="<s:url value="/mmr/images/update_charge_btn.png" includeContext="true" />"
										style="padding-bottom: 3px;"></a></td>
							</tr>
							</s:if>
							<s:if test="%{#session.ROLE.contains('busadmin')}">
								<tr>
									<td colspan="4" class="ordrdtl_title" align="center"><strong>New Charge:</strong></td>
								</tr>
								<tr>
									<s:url id="carrierChargesList" action="listCarrierCharges" />
									<td align="left" width="35%"><sx:autocompleter keyName="newActualCharge.chargeId"
										name="searchString" href="%{carrierChargesList}"
										dataFieldName="carrierChargesSearchResult"
										cssStyle="width:190px;" loadOnTextChange="true"
										loadMinimumCount="3" />
									</td>
									<td align="left">
										<s:textfield size="7"
										key="newActualCharge.cost" name="newActualCharge.cost"
										cssClass="text_02" />
									</td>
									<td>&nbsp;</td>
									<td align="left">
										<s:textfield size="7"
										key="newActualCharge.charge" name="newActualCharge.charge"
										cssClass="text_02" />
									</td>
									<td align="left" colspan="2"><s:select
										value="%{newActualCharge.statusText}" id="status"
										cssClass="text_01_combo_big" cssStyle="width:140px;"
										name="newActualCharge.statusText"
										list="{'Pending Release','Ready to Invoice'}" theme="simple" />
									</td>
									<td align="left" colspan="2"><a
										href="javascript: addActualCharge()"><img border="0"
										src="<s:url value="/mmr/images/add_product.png" includeContext="true" />"
										>
										</a></td>
								</tr>
						</s:if>
					</table>
			</div>


			<div id="payment_rqd_end">&nbsp;</div>
			
			<!-- Start: Payment Info Module -->
			<s:if test="%{selectedOrder.ccTransactions.size > 0}"> <!--  Condition to display the Payment Info Panel, Shows if there are CCtransactions, else doesnt show. -->
			<div id="payment_info_table">
			<div id="payment_inform">&nbsp;&nbsp;Payment Info:</div>
					<display:table id="payment_info" name="selectedOrder.ccTransactions" export="false" uid="row" cellspacing="0" cellpadding="3">
					<display:column headerClass="payment_info_tableTitle" sortable="true" title="" />
					<display:column headerClass="payment_info_tableTitle" property="authNum"  sortable="true" title="Auth #" ></display:column>
					<display:column headerClass="payment_info_tableTitle" property="statusString"  sortable="true" title="Status" ></display:column>
					<display:column headerClass="payment_info_tableTitle" format="{0,number,currency}" property="amount"  sortable="true" title="Amount" ></display:column>
					<display:column headerClass="payment_info_tableTitle" property="referenceNumber"  sortable="true" title="Ref #" ></display:column>
					<display:column headerClass="payment_info_tableTitle" property="processorTransactionId"  sortable="true" title="Processor Ref #" ></display:column>
					<display:column headerClass="payment_info_tableTitle" property="cardNumCharged"  sortable="true" title="CC #" ></display:column>
					<!-- Implementation of Refund Charge based on Role and Status: Status should be 30 for 'Processed' -->
					<s:if test="%{#session.ROLE.contains('busadmin') && #status==30}">
						<display:column headerClass="payment_info_tableTitle" sortable="true" title="">
						<a href="">Refund Charge</a> <!-- Implementation of Refund Charge Logic. -->
						</display:column>
					</s:if>					
				</display:table>
			</div>
			<div id="payment_rqd_end">&nbsp;</div>
			</s:if>
			<!-- End: Payment Info Module -->
			
		</sx:div>
		<sx:div id="Package" label="Package Details">
			<div id="order_detail_heading">&nbsp;&nbsp;Package:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<font color="#000066" size="3"
				style="font: Arial; font-variant: small-caps">Package Details
			for #&nbsp;<s:property value="%{selectedOrder.id}" /></font>
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<img
				src="<s:url value="/mmr/images/DataGrid_headerSeparatorSkin.png" includeContext="true" />"
				border="0" style="margin-bottom: -3px;">&nbsp; <a href="#"
				class="prevTab"><img
				src="<s:url value="/mmr/images/back.png" includeContext="true" />"
				border="0" style="margin-bottom: -3px;"></a>&nbsp;&nbsp;<a
				href="javascript: showPackage('orderDetail');"
				style="text-decoration: none;"><mmr:message
				messageId="label.navigation.back" /></a></div>
			<div id="pckg_table">
			<table width="600px" style="margin-left: 20px;">
				<tr>
					<td class="ordrdtl_title">Package</td>
					<td class="ordrdtl_title_val"><s:property
						value="%{selectedOrder.packageTypeId.name}" /></td>
					<td class="ordrdtl_title">Quantity</td>
					<td class="ordrdtl_title_val"><s:property
						value="%{selectedOrder.quantity}" /></td>
				</tr>
				<s:iterator value="selectedOrder.packages" status="counterIndex">
					<tr>
						<td colspan="1" class="ordrdtl_title">Dimensions of Package <s:property
							value="%{#counterIndex.index+1}" />:</td>
						<td colspan="1" class="ordrdtl_title_val"><s:property
							value="getText('{0,number,#,##0.0}',{selectedOrder.packages[#counterIndex.index].height})" />
						x <s:property
							value="getText('{0,number,#,##0.0}',{selectedOrder.packages[#counterIndex.index].width})" />
						x <s:property
							value="getText('{0,number,#,##0.0}',{selectedOrder.packages[#counterIndex.index].length})" />
						</td>
						<td colspan="1" class="ordrdtl_title">Weight of Package <s:property
							value="%{#counterIndex.index+1}" />:</td>
						<td class="ordrdtl_title_val"><s:property
							value="getText('{0,number,#,##0.0}',{selectedOrder.packages[#counterIndex.index].weight})" />
						lbs</td>
					</tr>
					<tr>
						<td colspan="1" class="ordrdtl_title">Carrier Dim Package <s:property
							value="%{#counterIndex.index+1}" />:</td>
						<td colspan="1" class="ordrdtl_title_val"><s:property
							value="%{selectedOrder.packages[#counterIndex.index].dimmedString}" />
						</td>
					</tr>
				</s:iterator>
			</table>
			</div>


		</sx:div>
		<sx:div label="Status Update" id="add_info">
			<div id="additional_info">
				<s:include value="add_info_shipping.jsp"></s:include>
			</div>
		</sx:div>
		<s:if test="%{selectedOrder.customer.warehouseCustomer == true}">
			<sx:div label="Warehousing" id="product_tab_div">
				<s:include value="view_shipment_added_products.jsp"></s:include>
			</sx:div>
		</s:if>
	</sx:tabbedpanel>
	<!-- End change: Sumanth Kulkarni - -->



</s:form></div>
</body>
</html>


