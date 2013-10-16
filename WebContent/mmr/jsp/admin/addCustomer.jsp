<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="mmr" uri="/mmr-tags" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<html> 
<head> 
    <title><s:text name="customer.form.title"/></title> 
</head> 
<body onload="doOnLoad()"> 
<script type="text/javascript" src="<%=request.getContextPath()%>/mmr/scripts/countryProvince.js"></script>

<SCRIPT language="JavaScript">
var CCSectionActive = false;
var active = "<%=request.getAttribute("active_cc")%>";
var add = "<%=request.getAttribute("add")%>";
var newcc = "<%= request.getAttribute("new_cc")%>";

window.onload = doOnload;

	function doOnload()
	{
		//alert(newcc);
		if(newcc!='null')
			showCC('block');
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
	  url="<%=request.getContextPath()%>/customer.listProvince.action?value="+billingCountry.value;
		//param="objName=ref_state&country_id="+country_id;
		ajax_Country.open("GET",url,true);
		ajax_Country.send(this);
} // End function showState()

	function submitform()
	{
		//local boolean check if everything is ok, then submit the form
		var check = true;
		if(CCSectionActive) //if the credit card section is active or the user has selected to enter or update the credit card info, then check.
		{
			if(document.getElementById("ccid").value == '')
			{
				alert("Please enter the credit card Number");
				check = false;
			}
			else if(document.getElementById("cvdcid").value == '')
			{
				alert("Please enter the CVD Code");
				check = false;
			}
		}
		if(document.customerform.method.value=='edit')
		{
			 document.customerform.action = "editcustomerinfo.action";
		}
		else
		{
			 document.customerform.action = "createCustomer.action";
		}
		if(check)//if everything is fine, then submit the form.
		document.customerform.submit();
	}
	
	function submitcarrier()
	{
	 document.customerform.action = "createAccount.action";
	 document.customerform.submit();
	}
	
    function showState() {
		ajax_Country=ajaxFunction();
		ajax_Country.onreadystatechange=function()
		  {
			   if(ajax_Country.readyState==4)
				{
				reponse=ajax_Country.responseText;
				js_stateid=document.getElementById("stateid");
				js_stateid.innerHTML= reponse;
				}
		  }
		  firstBox = document.getElementById('firstBox');
		  url="<%=request.getContextPath()%>/customer.listProvince.action?value="+firstBox.value;
			//param="objName=ref_state&country_id="+country_id;
			ajax_Country.open("GET",url,true);
			ajax_Country.send(this);
	} // End function showState()	
	
	function checkBothSalesAgent()
	{	
		var e = document.getElementById("salesAgent");
		var strSalesUser = e.options[e.selectedIndex].value;
		
		var e2 = document.getElementById("salesAgent2");
		var strSalesUser2 = e2.options[e2.selectedIndex].value;
		
		if(strSalesUser==strSalesUser2)
		{
			alert("Both the selected Sales Agent should not be same.");
			e.selectedIndex = 0;
			e2.selectedIndex = 0;
		}		
	}
	
	function ShowCCForCCType(val)
	{
		if(val==2)
		{
			if(add!='null')
			showCC('block');
		}
		else
		{
			if(add!='null')
			showCC('none');
		}		
	}
	
	function showCC(val){
		if(val=='block')
			CCSectionActive = true;
		else
			CCSectionActive = false;
		document.getElementById("payment_info_pnl").style.display = val;
	}
	
	function setPrevAdd()
	{
		setValue('customer.newCreditCard.billingAddress.contactName',document.customerform.ccactive_contactname.value);
		setValue('customer.newCreditCard.billingAddress.address1',document.customerform.ccactive_address1.value);
		setValue('customer.newCreditCard.billingAddress.address2',document.customerform.ccactive_address2.value);
		setValue('customer.newCreditCard.billingAddress.city',document.customerform.ccactive_city.value);
		setValue('customer.newCreditCard.billingAddress.postalCode',document.customerform.ccactive_postalCode.value);
		setValue('customer.newCreditCard.billingAddress.countryCode',document.customerform.ccactive_countryCode.value);
		setValue('customer.newCreditCard.billingAddress.provinceCode',document.customerform.ccactive_provinceCode.value);
	}
	
	function setValue(name, val)
	{
		document.getElementsByName(name)[0].value = val;
	}
</SCRIPT> 

<div id="messages">
	<jsp:include page="../common/action_messages.jsp"/>
</div>
<div class="form-container" > 

<s:form action="createCustomer" name="customerform">
<s:hidden name="ccactive_contactname" value="%{customer.creditCardActive.billingAddress.contactName}"/>
<s:hidden name="ccactive_address1" value="%{customer.creditCardActive.billingAddress.address1}"/>
<s:hidden name="ccactive_address2" value="%{customer.creditCardActive.billingAddress.address2}"/>
<s:hidden name="ccactive_city" value="%{customer.creditCardActive.billingAddress.city}"/>
<s:hidden name="ccactive_postalCode" value="%{customer.creditCardActive.billingAddress.postalCode}"/>
<s:hidden name="ccactive_countryCode" value="%{customer.creditCardActive.billingAddress.countryCode}"/>
<s:hidden name="ccactive_provinceCode" value="%{customer.creditCardActive.billingAddress.provinceCode}"/>

<div id="add_cust_hdr">
<table cellpadding="4" cellspacing="0" width="500px">
<tr>
<td>
<s:if test="#session.edit == 'true'">
		Edit Customer: 
    	<s:hidden name="method" value="edit"/>
    	<s:hidden name="customer.businessId" />
    </s:if> 
    <s:else>
 		Add Customer
    	<s:hidden name="method" value="add"/>
    </s:else>

</td>
<td align="left">
<div id="add_cust_name"><s:property value="customer.name"/></div>
</td>
</tr>
</table>
</div>


<div id="add_cust_hdr_imgs">
<table width="275px">
<tr>
<s:if test="#session.edit != 'true'">
<td>&nbsp;</td>
<td>&nbsp;</td>
<td>&nbsp;</td>
<td>&nbsp;</td>
<td>&nbsp;</td>
<td>&nbsp;</td>
<td>&nbsp;</td>
<td>&nbsp;</td>
<td>&nbsp;</td>
<td>&nbsp;</td>
<td>&nbsp;</td>
<td>&nbsp;</td>
<td>&nbsp;</td>
<td>&nbsp;</td>
<td>&nbsp;</td>
<td>&nbsp;</td>
<td><img src="<s:url value="/mmr/images/DataGrid_headerSeparatorSkin.png" includeContext="true" />" border="0">&nbsp;<img border="0" src="<%=request.getContextPath()%>/mmr/images/reset_icon.png" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
</s:if> 
<s:else>
 <s:if test="%{#session.ROLE.contains('busadmin')}">
<td><img src="<s:url value="/mmr/images/DataGrid_headerSeparatorSkin.png" includeContext="true" />" border="0">&nbsp;<img src="<s:url value="/mmr/images/sales_users.png" includeContext="true" />" border="0">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
</s:if>
<s:else>
<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
</s:else>
<td><img src="<s:url value="/mmr/images/DataGrid_headerSeparatorSkin.png" includeContext="true" />" border="0">&nbsp;<img border="0" src="<%=request.getContextPath()%>/mmr/images/cancel.png"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
</s:else>
<td><img src="<s:url value="/mmr/images/DataGrid_headerSeparatorSkin.png" includeContext="true" />" border="0">&nbsp;<img src="<s:url value="/mmr/images/save_icon.png" includeContext="true" />" border="0">&nbsp;</td>
</tr>
</table>
</div>
<div id="add_cust_hdr_actions">
<table width="316px">
<tr>
<s:if test="#session.edit != 'true'">
<td>&nbsp;</td>
<td>&nbsp;</td>
<td>&nbsp;</td>
<td>&nbsp;</td>
<td>&nbsp;</td>
<td>&nbsp;</td>
<td>&nbsp;</td>
<td>&nbsp;</td>
<td>&nbsp;</td>
<td>&nbsp;</td>
<td>&nbsp;</td>
<td>&nbsp;</td>
<td>&nbsp;</td>
<td>&nbsp;</td>
<td>&nbsp;</td>
<td>&nbsp;</td>
<td>&nbsp;</td>
<td><a href="addcustomer.action">&nbsp;&nbsp;Reset</a>&nbsp;&nbsp;&nbsp;&nbsp;</td>
</s:if> 
<s:else>
<td>&nbsp;</td>
<td>&nbsp;</td>
<td>&nbsp;</td>
 <s:if test="%{#session.ROLE.contains('busadmin')}">
<td><a href="manage.sales.users.action"><mmr:message messageId="label.manage.sales"/></a>&nbsp;&nbsp;</td>
</s:if>
<s:else>
<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
</s:else>
<td><a href="searchcustomer.action"><mmr:message messageId="label.btn.cancel"/></a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
</s:else>
<td><a href="javascript: submitform()"> <mmr:message messageId="label.btn.save"/></a> &nbsp;</td>
</tr>
</table>
</div>
	

<div id="right_left_new" align="left" >
	
	<div id="add_cust_bttm_tbl" class="text_01">
       
    <table width="98%" cellspacing="0" cellpadding="2">
               <tr>
                  <td>&nbsp;</td>
                  <td>&nbsp;</td>
				  <td>&nbsp;</td>
				  <td>&nbsp;</td>
				  <td>&nbsp;</td>
				  <td>&nbsp;</td>
                </tr>
                <tr>
                  <td>&nbsp;</td>
                  <td>&nbsp;</td>
				  <td>&nbsp;</td>
				  <td>&nbsp;</td>
				  <td>&nbsp;</td>
				  <td>&nbsp;</td>
                </tr>
                <tr><td colspan="6">&nbsp;</td></tr>
            	<s:if test="#session.edit != 'true'">
	                <tr>
		                <td class="add_cust_tbl_cols"><mmr:message messageId="label.customer.username"/>:</td>
		                <td><s:textfield size="24" key="customer.username" name="customer.username" cssClass="text_02_tf"/></td>
						<td >&nbsp;</td>
		                <td class="add_cust_tbl_cols"><mmr:message messageId="label.customer.userpassword"/>:</td>
		                <td><s:password size="24" key="customer.password" name="customer.password" cssClass="text_02_tf"/></td>
	                </tr>  
	            </s:if> 
                <tr>
                  <td class="add_cust_tbl_cols"><mmr:message messageId="label.customer.name"/>:</td>
                  <td><s:textfield size="24" key="customer.name" name="customer.name" cssClass="text_02_tf"/></td>
				  <td >&nbsp;</td>
				  <s:if test="%{#session.ROLE.contains('busadmin')}">				  
                  <td class="add_cust_tbl_cols"><mmr:message messageId="label.customer.accountNumber"/>:</td>
	              <td><s:textfield size="24" key="customer.accountNumber" name="customer.accountNumber" cssClass="text_02_tf"/></td>
				  <td >&nbsp;</td>
				  </s:if>
 	            </tr>
	            <tr>
                  <td class="add_cust_tbl_cols"><mmr:message messageId="label.customer.contact"/>:</td>
                  <td><s:textfield size="24" key="customer.address.contactName" name="customer.address.contactName" cssClass="text_02_tf" /></td>
                  <td>&nbsp;</td>
                  <td class="add_cust_tbl_cols"><mmr:message messageId="label.customer.address1"/>:</td>
                  <td><s:textfield size="24" key="customer.address.address1" name="customer.address.address1" cssClass="text_02_tf"/></td>
                  <td>&nbsp;</td>
                 </tr>
                <tr>					
                  <td class="add_cust_tbl_cols"><mmr:message messageId="label.customer.address2"/>:</td>
                  <td><s:textfield size="24" key="customer.address.address2" name="customer.address.address2" cssClass="text_02_tf"/></td>
                  <td>&nbsp;</td>
                  <td class="add_cust_tbl_cols"><mmr:message messageId="label.customer.postalCode"/>: </td>
                  <td>
                  <s:textfield size="24" id="custPostalCode" key="customer.address.postalCode" name="customer.address.postalCode" onblur="javascript:getAddressSuggestCustomer();" cssClass="text_02_tf"/>
                  <img id="loading-img-cust" style="display:none;" src="<s:url value="/mmr/images/loading.gif" includeContext="true" />" border="0">
                  </td>
				 <td>&nbsp;</td>
                   <td class="add_cust_tbl_cols"><mmr:message messageId="label.customer.city"/>:</td>
                  <td><s:textfield size="24" key="customer.address.city" name="customer.address.city" cssClass="text_02_tf"/></td>
                </tr>
                
				<tr>
					<td  class="add_cust_tbl_cols" width="11%"><mmr:message messageId="label.customer.country"/>:</td>
					<td width="21%">
						<s:select cssClass="text_01_combo_big" cssStyle="width:135px;" listKey="countryCode" listValue="countryName" 
							name="customer.address.countryCode" headerKey="-1"  list="#session.CountryList" 
								onchange="javascript:showState();"  id="firstBox" theme="simple"/>
					</td>
					<td width="3%">&nbsp;</td>
					<td  class="add_cust_tbl_cols" width="11%"><mmr:message messageId="label.customer.province"/>:</td>
					<td width="21%" id="stateid">
						<s:select key="customer.address.provinceCode" name="customer.address.provinceCode"  cssClass="text_01_combo_big" cssStyle="width:135px;" 
							listKey="provinceCode" listValue="provinceName" list="#session.provinces"/>
					</td>
					<td width="3%">&nbsp;</td>
				 	<td class="add_cust_tbl_cols"><mmr:message messageId="label.customer.phone"/>: </td>
                  <td> <s:textfield size="24" key="customer.address.phoneNo" name="customer.address.phoneNo" cssClass="text_02_tf" /></td>
               </tr>                
                <tr>            
				  
                  <td class="add_cust_tbl_cols"><mmr:message messageId="label.customer.email"/>: </td>
                  <td><s:textfield size="24" key="customer.address.emailAddress" name="customer.address.emailAddress" cssClass="text_02_tf" /></td>
                  <td>&nbsp;</td>
                  <td class="add_cust_tbl_cols"><mmr:message messageId="label.customer.invoice.email"/>:</td>
                  <td><s:textfield size="24" key="customer.invoicingemail" name="customer.invoicingEmail" cssClass="text_02_tf" /></td>
                  <td>&nbsp;</td>
                 </tr>
               <s:if test="%{#session.ROLE.contains('busadmin')}">
                <tr>
                  <td class="add_cust_tbl_cols"><mmr:message messageId="label.customer.apiusername"/>:</td>
                  <td><s:textfield size="24" key="customer.apiusername" name="customer.apiUsername" cssClass="text_02_tf" /></td>
                   <td>&nbsp;</td>
               	  <td class="add_cust_tbl_cols"><mmr:message messageId="label.customer.apipassword"/>:</td>
                  <td><s:password size="24" key="customer.apipassword" name="customer.apiPassword" cssClass="text_02_tf" showPassword="true" /></td>
                 <td>&nbsp;</td>
				 <td class="add_cust_tbl_cols"><mmr:message messageId="label.customer.active"/>:</td>
                  <td><s:select key="customer.active" cssStyle="width:70px;"  name="customer.active" headerKey="1" list="#{'true':'Yes','false':'No'}"  value="customer.active" cssClass="text_01_combo_big"/>
				  </td>
				   </tr>
                <tr>
				<td class="add_cust_tbl_cols"><mmr:message messageId="label.customer.timezone"/>:</td>
				<td>
					<s:select key="customer.timezone" cssStyle="width:140px;" cssClass="text_01_combo_big" name="customer.timeZone" headerKey="1"   
						listKey="id" listValue="name" list="#session.timeZones"/>
		  		</td>
		  		 <td>&nbsp;</td>
                  <td class="add_cust_tbl_cols"><mmr:message messageId="label.customer.payableDays"/>:</td>
                  <td><s:textfield size="24" key="customer.payableDays" name="customer.payableDays" cssClass="text_02_tf" /></td>
               	 <td>&nbsp;</td>
				 <td class="add_cust_tbl_cols"><mmr:message messageId="label.customer.paymentType"/>:</td>
                  <td><s:select id="ptid" key="customer.paymentType" name="customer.paymentType" headerKey="1" list="#{'1':'On Credit','2':'Credit Card'}"   cssClass="text_01_combo_big" onchange="ShowCCForCCType(this.value);"/>
				  </td>
				  </tr>
                <tr>
				 <td class="add_cust_tbl_cols"><mmr:message messageId="label.customer.creditLimit"/>:</td>
                  <td><s:textfield size="24" key="customer.creditLimit" name="customer.creditLimit" cssClass="text_02_tf" />
				  </td>
				   <td>&nbsp;</td>
                  <s:if test="%{#session.ROLE.contains('busadmin')}">
                  <td class="add_cust_tbl_cols"><mmr:message messageId="label.customer.default.currency"/>:</td>
                  <td>
                  	<s:select
							list="#{'CAD':'CAD', 'USD':'USD'}"
							name="customer.defaultCurrency" headerKey="" headerValue=""
							cssClass="text_01_combo_medium" /> </td>
                  </s:if>
                  <s:else>
                   <td class="add_cust_tbl_cols">&nbsp;</td>
                  <td>
                  	&nbsp;</td>
                  </s:else>
                  <td colspan="2">&nbsp;</td>                
                </tr>
                </s:if>
 				<tr><td colspan="6">&nbsp;</td></tr>
 	</table>
		</div>
		
		<s:if test="#session.edit == null && #session.edit != 'true'">
		<div id="carrier_hdr">
		<table cellpadding="4" cellspacing="0" width="500px">
		<tr>
		<td><mmr:message messageId="label.carriers.setup"/></td>
		</tr>
		</table>
		</div>
		<div id="carrier_mid" class="text_01">
		<table cellpadding="4" cellspacing="5" width="800px">
			<tr>
			<td class="add_cust_tbl_cols"><mmr:message messageId="label.monthly.spend"/>:&nbsp;
            <s:textfield size="24" key="customer.monthlySpend" name="customer.monthlySpend" cssClass="text_02_tf" /></td>
			</tr>
			<s:iterator value="#session.CARRIERS" status="carriercount">
				<s:set name="carrier" value="%{#session.CARRIERS.get(#carriercount.index)}"/>
					<s:if test="%{#carriercount.index % 3 == 0}">
						<tr>
					</s:if>
					<td class="add_cust_tbl_cols" width="30%"><s:checkbox name="select[%{#carriercount.index}]" value="select[%{#carriercount.index}]" cssClass="text_02" />&nbsp;&nbsp;&nbsp;<s:property value="#carrier.name"/></td>
			</s:iterator>
		</table>
		</div>
		<div id="carrier_end">&nbsp;</div>
		</s:if>

<s:if test="%{#session.edit != null && #session.business.defaultPaymentTypeLevel!=0}">	
	<div id="credit_card_hdr">
	<mmr:message messageId="label.billing.section"/>:
	</div>
	<!--  -->
<div id="creditcard_panel">
	<div id="credit_card_link">
		<table width="300px">
			<tr>
				<td><img src="<s:url value="/mmr/images/DataGrid_headerSeparatorSkin.png" includeContext="true" />" border="0">&nbsp;</td>
				<s:if test="%{#session.ActiveCC==null}"> <!-- If customer credit card is null-->
					<td><img src="<s:url value="/mmr/images/addNew_icon.png" includeContext="true" />" border="0"></td>
					<td><s:a href="#" onclick="showCC('block');"><mmr:message messageId="add.payment.information"/></s:a></td>
				</s:if>
				<s:else>	<!-- If customer credit card is NOT null-->
					<td><img src="<s:url value="/mmr/images/update.png" includeContext="true" />" border="0"></td>
					<td><s:a href="#" onclick="showCC('block');"><mmr:message messageId="update.payment.information"/></s:a></td>
				</s:else>
			</tr>
		</table>
	</div>
	<div id="creditcard_panel_tbl">
	<s:if test="%{#session.ActiveCC!=null}">	<!-- If customer credit card is available -->
		<table width="720px" cellspacing="0" cellpadding="2">
			<tr>
				<td rowspan="3" width="15%" align="left"><img src="<s:url value="/mmr/images/credit_cards_64.png" includeContext="true" />" border="0"> </td>
				<td class="markup_tbl_font" width="17%" align="right"><mmr:message messageId="label.last.4.digits"/></td>
				<td width="3%">&nbsp;&nbsp;</td>
				<td class="credit_info"><s:property value="%{customer.creditCardActive.ccLast4}"/> </td>
				<td class="markup_tbl_font" width="17%" align="right"><mmr:message messageId="label.creditcard.expiryMonthYear"/></td>
				<td width="3%">&nbsp;&nbsp;</td>
				<td class="credit_info"><s:property value="%{customer.creditCardActive.ccExpiryMonth}"/>&nbsp;/&nbsp;<s:property value="%{customer.creditCardActive.ccExpiryYear}"/>
				</td>
			</tr>
			<tr>
				<td class="markup_tbl_font" width="17%" align="right"><mmr:message messageId="label.creditcard.billingAddress1"/></td>
				<td width="3%">&nbsp;&nbsp;</td>
				<td class="credit_info" colspan="4">
					&nbsp;<s:property value="%{customer.creditCardActive.billingAddress.longAddress}"/> 
				</td>
			</tr>
		</table>
	</s:if>
	<s:else> <!-- If credit card is not available -->
		<table width="460px" cellspacing="0" cellpadding="5" style="margin-left: 320px;">
			<tr><td>&nbsp;</td></tr>
			<tr height="20px">
				<td class="nocredit_info"><mmr:message messageId="no.credit.card.information"/></td>
			</tr>
			<tr><td>&nbsp;</td></tr>
		</table>
	</s:else>
	</div>
</div>
</s:if>		
	<div id="payment_info_pnl">
		<div id="payment_info_hdr">
		<table width="960px">
			<tr>
				<td align="left" width="26%"><mmr:message messageId="label.credit.card"/>:</td>
				<td class="add_cust_tbl_cols2" align="center" width="55%"><mmr:message messageId="label.enter.credit.card.details"/></td>
				<td align="center" width="8%"><img src="<s:url value="/mmr/images/DataGrid_headerSeparatorSkin.png" includeContext="true" />" border="0">&nbsp;<img border="0" src="<%=request.getContextPath()%>/mmr/images/cancel.png"/>&nbsp;<a href="myaccountinfo.action" style="margin-top: 0px; position: absolute; text-decoration: none;"><mmr:message messageId="label.btn.cancel"/></a>&nbsp;</td>
				<td align="center" width="15%"><img src="<s:url value="/mmr/images/DataGrid_headerSeparatorSkin.png" includeContext="true" />" border="0">&nbsp;<img src="<s:url value="/mmr/images/save_icon.png" includeContext="true" />" border="0">&nbsp;<a href="javascript: submitform()" style="margin-top: 0px; position: absolute; text-decoration: none;"> <mmr:message messageId="label.btn.save"/> </a></td>
			</tr>
		</table>
		</div>
		<s:if test="%{#session.ActiveCC!=null}">
		<div id="radio_prev_address">
			<table cellpadding="0" cellspacing="0" width="950px">
				<tr>
					<td class="add_cust_tbl_cols" align="left"><mmr:message messageId="label.same.billing.address" />:&nbsp;<s:checkbox name="" id="chk_add" onclick="setPrevAdd();"/></td>
				</tr>
			</table>
		</div>
		</s:if>
		<table cellspacing="0" cellpadding="2" width="950px">
			 <tr>
			    <td width="20%" class="add_cust_tbl_cols"><mmr:message messageId="label.creditcard.number"/>:</td>
			    <td width="20%"><s:textfield size="24" id="ccid" key="customer.newCreditCard.ccNumber" name="customer.newCreditCard.ccNumber" cssClass="text_02_tf"/></td>
			    <td width="3%">&nbsp;</td>
			    <td width="20%" class="add_cust_tbl_cols"><mmr:message messageId="label.creditcard.cvdCode"/>:</td>
			    <td width="20%"><s:textfield size="24" id="cvdcid" key="customer.newCreditCard.cvd" name="customer.newCreditCard.cvd" cssClass="text_02_tf"/></td>
			    <td width="3%">&nbsp;</td>
			    <td width="20%" class="add_cust_tbl_cols"><mmr:message messageId="label.creditcard.expiryMonthYear"/>:</td>
			    <td width="20%"><s:select 
									list="#{'01':'Jan', '02':'Feb', '03':'Mar', '04':'Apr', '05':'May', '06':'Jun', '07':'Jul', '08':'Aug', '09':'Sep', '10':'Oct', '11':'Nov', '12':'Dec'}"
									key="customer.newCreditCard.ccExpiryMonth"
									name="customer.newCreditCard.ccExpiryMonth"
									cssClass="text_01_combo_small" cssStyle="width: 58px;"/>
									<s:select
									list="#{'2012':'2012', '2013':'2013', '2014':'2014', '2015':'2015', '2016':'2016', '2017':'2017', '2018':'2018', '2019':'2019', '2020':'2020'}"
									key="customer.newCreditCard.ccExpiryYear"
									name="customer.newCreditCard.ccExpiryYear"
									cssClass="text_01_combo_small" cssStyle="width: 58px;"/>
				</td>
			</tr>
	  		<tr>
			    <td width="20%" class="add_cust_tbl_cols"><mmr:message messageId="label.creditcard.nameOnCard"/>:</td>
			    <td width="20%"><s:textfield size="24" key="customer.newCreditCard.billingAddress.contactName" name="customer.newCreditCard.billingAddress.contactName" cssClass="text_02_tf"/></td>
			    <td width="3%">&nbsp;</td>
			    <td width="20%" class="add_cust_tbl_cols"><mmr:message messageId="label.creditcard.billingAddress1"/>:</td>
			    <td width="20%"><s:textfield size="24" key="customer.newCreditCard.billingAddress.address1" name="customer.newCreditCard.billingAddress.address1" cssClass="text_02_tf"/></td>
			    <td width="3%">&nbsp;</td>
			    <td width="20%" class="add_cust_tbl_cols"><mmr:message messageId="label.creditcard.billingAddress2"/>:</td>
			    <td width="20%"><s:textfield size="24" key="customer.newCreditCard.billingAddress.address2" name="customer.newCreditCard.billingAddress.address2" cssClass="text_02_tf"/></td>
			</tr>
	  		<tr>
			    <td width="20%" class="add_cust_tbl_cols"><mmr:message messageId="label.creditcard.billingCity"/>:</td>
			    <td width="20%"><s:textfield size="24" key="customer.newCreditCard.billingAddress.city" name="customer.newCreditCard.billingAddress.city" cssClass="text_02_tf"/></td>
			    <td width="3%">&nbsp;</td>
			    <td width="20%" class="add_cust_tbl_cols"><mmr:message messageId="label.creditcard.billingPostalCode"/>:</td>
			    <td width="20%"><s:textfield size="24" key="customer.newCreditCard.billingAddress.postalCode" name="customer.newCreditCard.billingAddress.postalCode" cssClass="text_02_tf"/></td>
			    <td width="3%">&nbsp;</td>
			    <td width="20%" class="add_cust_tbl_cols"><mmr:message messageId="label.creditcard.billingCountry"/>:</td>
			    <td width="20%">						
	    			<s:select cssClass="text_01_combo_big" cssStyle="width:135px;" listKey="countryCode" listValue="countryName" 
								name="customer.newCreditCard.billingAddress.countryCode" headerKey="-1"  list="#session.CountryList" 
									onchange="javascript:showBillingState();"  id="billingCountry" theme="simple"/>
	  			</td>
	  		</tr>
	  		<tr>
			    <td width="20%" class="add_cust_tbl_cols"><mmr:message messageId="label.creditcard.billingProvince"/>:</td>
			    <td id="billingstateid" width="20%">						
			    	<s:select key="customer.newCreditCard.billingAddress.provinceCode" name="customer.newCreditCard.billingAddress.provinceCode"  cssClass="text_01_combo_big" cssStyle="width:135px;" 
										listKey="provinceCode" listValue="provinceName" list="#session.provinces"/>
			  	</td>
			    <td width="3%">&nbsp;</td> 
			    <td width="20%"></td>
			    <td width="20%"></td>
			    <td width="3%">&nbsp;</td> 
			    <td colspan="2">&nbsp;</td>
	  		</tr>
		</table>
	</div>
</div>
		
		
		
		</s:form>
		</div>
		</body>	  
</html>