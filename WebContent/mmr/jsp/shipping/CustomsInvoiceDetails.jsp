

<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="mmr" uri="/mmr-tags" %>
<%@ taglib prefix="sx" uri="/struts-dojo-tags" %>

<link rel="stylesheet" type="text/css"
	href="<s:url value='/mmr/styles/common.css' includeContext="true"/>" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/mmr/jsp/shipping/style.css">

<html>
<head>

<title><s:text name="user.form.title"/></title>
<script type="text/javascript">
window.onload = function() {
     var e = document.getElementById("customs_invoice_checkbox");
     if(e.checked)
     {
      document.getElementById("radio_billto").style.display= "block";
      document.getElementById("customs_invoice_panel").style.display= "block";
     }
  
};
</script>
</head>
<body>
<script type="text/javascript" src="<%=request.getContextPath()%>/mmr/scripts/countryProvince.js"></script>
<script type="text/javascript">



var desc_event=dojo.event.topic.subscribe("/value_desc", function(value, key, text, widget){
		//alert("Event:"+event); 
		//alert("value_desc");
	 ajax_Country_desc=ajaxFunction();	 
	ajax_Country_desc.onreadystatechange=function()
	  {
		   if(ajax_Country_desc.readyState==4 && ajax_Country_desc.status==200)
			{
						
			response_desc=ajax_Country_desc.responseText;
			js_stateid=document.getElementById("hide_this");
			js_stateid.innerHTML= response_desc;
			
			var sess_desc= document.getElementById("desc_id").value;
			dojo.widget.byId("autoproductdesc").setValue(sess_desc);
			
			var sess_hcode= document.getElementById("hcode_id").value;
			document.getElementById("new_prod_hcode").value=sess_hcode;
			//dojo.widget.byId("autoproducthcode").setValue(sess_hcode);
						
			var sess_uprice= document.getElementById("uprice_id").value;
			document.getElementById("new_prod_uprice").value=sess_uprice;
				
			var sess_country= document.getElementById("country_id").value;
			var dd_country= document.getElementById("new_productOrigin");
			for(var i=0;i < dd_country.options.length;i++)
			{
				if (dd_country.options[i].value == sess_country )
					 dd_country.options[i].selected = true;
			}
				//desc_event=null;
				//alert("done");
				//e.stopPropagation();
				
				//alert("done-desc");
			}			
			
	  }	
		url="populateProducts.action?productId="+key+"&product_desc="+value;
		ajax_Country_desc.open("GET",url,true);
		ajax_Country_desc.send(this);	   
});

</script>

<SCRIPT type="text/javascript">
var contextPath = "<%=request.getContextPath()%>";

 
function toShoworHide(checked)
	{		
		if(checked)
		{
			//alert("Checked");
			document.getElementById("customs_invoice_panel").style.display = 'block';
			document.getElementById("radio_billto").style.display = 'block';
						
		}
		else
		{
			//alert("UnChecked");
			document.getElementById("customs_invoice_panel").style.display = 'none';
			document.getElementById("radio_billto").style.display = 'none';			
		}		
	}
	
	function changeBillTo(sel)
	{
		if(sel == '2')
		{			
			document.getElementById("accnt_number_lbl").style.display= 'none';
			document.getElementById("accnt_number_txt").style.display= 'none';	
			radioselected=2;	
		}
		else if(sel == '1')
		{			
			document.getElementById("accnt_number_lbl").style.display= 'none';
			document.getElementById("accnt_number_txt").style.display= 'none';
			radioselected=1;
		}
		else if(sel == '3')
		{			
			document.getElementById("accnt_number_lbl").style.display= 'block';
			document.getElementById("accnt_number_txt").style.display= 'block';		
			radioselected=3;	
		}
		if(sel!= '3')
		{
			document.getElementById("loading-img-billto").style.display = 'block';
			ajax_Country=ajaxFunction();
			ajax_Country.onreadystatechange=function()
	 		{
			   if(ajax_Country.readyState==4)
				{
				reponse=ajax_Country.responseText;
				js_stateid=document.getElementById("customs_invoice_billto_address");
				js_stateid.innerHTML= reponse;
				document.getElementById("loading-img-billto").style.display = 'none';
				}
	  		}
			url="setBillToAddress.action?selected="+sel;
			ajax_Country.open("GET",url,true);
			ajax_Country.send(this);
		}
		
	}
	
    
     function assignTotal()
     {
	    var p_qty = document.getElementById("new_prod_quantity").value;
		var p_uprice = document.getElementById("new_prod_uprice").value;
		
		if(p_qty!='' && (!isAllDigits(p_qty) || p_qty <= 0)) 
		{
			alert('You need to provide a valid quantity');
			document.getElementById("new_prod_quantity").value='';
			document.getElementById("new_prod_quantity").focus();
		}
		if(p_uprice!='' && (!isAllDigits(p_uprice) || p_uprice <= 0)) 
		{
			alert('You need to provide a valid price');
			document.getElementById("new_prod_uprice").value='';
			document.getElementById("new_prod_uprice").focus();
		}		
		var pprice = parseFloat(p_uprice);
		if(isNaN(pprice) || isNaN(p_qty))
			document.getElementById("new_prod_tprice").value= '0.0';
		else
		{
			document.getElementById("new_prod_tprice").value= (p_qty*p_uprice).toFixed(2);
			checkToAdd();
		}		
    }  
    
    function isAllDigits(argvalue) {
        argvalue = argvalue.toString();
        var validChars = "0123456789.";
        var startFrom = 0;
        if (argvalue.substring(0, 2) == "0x") {
           validChars = "0123456789abcdefABCDEF";
           startFrom = 2;
        } else if (argvalue.charAt(0) == "0") {
           startFrom = 1;
        } else if (argvalue.charAt(0) == "-") {
            startFrom = 1;
        }

        for (var n = startFrom; n < argvalue.length; n++) {
            if (validChars.indexOf(argvalue.substring(n, n+1)) == -1) return false;
        }
        return true;
    }
    
     function checkToAdd(){
   // alert("Inside checkToAdd");
    var dojos = getElementsByClassName("dojoComboBox");
	var p_qty = document.getElementById("new_prod_quantity").value;
	var p_uprice = document.getElementById("new_prod_uprice").value;
	//var p_desc = dojo.widget.byId("autoproductdesc").getSelectedValue();
	var p_desc = dojos[2].value;
	//alert("P_DESC:::"+p_desc);
	var p_hcode = document.getElementById("new_prod_hcode").value;
	//alert(p_hcode);
	var check = true;
	
	if(!p_desc.length > 0)
	{
		alert('Please enter Product Description');
		//dojo.widget.byId("autoproductdesc").setValue='';
		document.getElementById("new_prod_quantity").value='';		
		document.getElementById('new_prod_tprice').value='';
		check = false;
	}		
	else if(!p_qty.length > 0)
	{
		alert('Please enter quantity');
		document.getElementById("new_prod_quantity").value='';
		document.getElementById('new_prod_tprice').value='';
		document.getElementById("new_prod_quantity").focus();
		check = false;
	}
	else if(!p_uprice.length > 0)
	{
		alert('Please enter Unit Price');
		document.getElementById("new_prod_uprice").value='';
		document.getElementById("new_prod_quantity").value='';
		document.getElementById('new_prod_tprice').value='';
		document.getElementById("new_prod_uprice").focus();
		check = false;
	}
	else if(!isAllDigits(p_qty) || p_qty <= 0) 
	{
		alert('You need to provide a valid quantity');
		document.getElementById("new_prod_quantity").value='';
		document.getElementById('new_prod_tprice').value='';
		document.getElementById("new_prod_quantity").focus();
		check = false;
	}
	else if(!isAllDigits(p_uprice) || p_qty < 0) 
	{
			alert('You need to provide a valid unit price');
			document.getElementById("new_prod_uprice").value='';
			document.getElementById("new_prod_quantity").value='';
			document.getElementById('new_prod_tprice').value='';
			document.getElementById("new_prod_uprice").focus();
			check = false;
	}
		if(check)
		{
			//alert("inside check");
			addProduct();
		}
	}
	
	function addProduct(){
	//alert("Inside addProduct");
	//alert(contextPath);
	var p_desc= dojo.widget.byId("autoproductdesc").getSelectedValue();
	var p_hcode= document.getElementById("new_prod_hcode").value;
	var e= document.getElementById("new_productOrigin");
	var p_country = e.options[e.selectedIndex].value;
	var p_quantity= document.getElementById("new_prod_quantity").value;
	var p_uprice= document.getElementById("new_prod_uprice").value;
	var p_weight="";
	
	ajax_Country=ajaxFunction();
	ajax_Country.onreadystatechange=function()
	  {	
	  		//alert("ajax_Country.readyState::"+ajax_Country.readyState);
		   if(ajax_Country.readyState==4)
			{
			response_add=ajax_Country.responseText;
			//alert(response);
			js_stateid=document.getElementById("productDetails");
			js_stateid.innerHTML= response_add;
			resetProductFields();
			}
	  }
		url="addProductInformation.action?desc="+p_desc+"&hcode="+p_hcode+"&origin_country="+p_country+"&quantity="+p_quantity+"&unit_price="+p_uprice+"&weight="+p_weight;
		ajax_Country.open("GET",url,true);
		ajax_Country.send(this);
	
	
	//var blank="";
	//dojo.widget.byId("autoproductdesc").setValue(blank);	
	//resetField('new_prod_hcode');
//	resetField('new_prod_quantity');
	//resetField('new_prod_uprice');
	//resetField('new_prod_tprice');	
	}
	
	function resetProductFields()
	{
		//alert("Inside resetProductFields");
		var dojos = getElementsByClassName("dojoComboBox");
		document.getElementById('new_prod_hcode').value='';
		document.getElementById('new_prod_uprice').value='';
		document.getElementById('new_prod_quantity').value='';
		document.getElementById('new_prod_tprice').value='';
		document.getElementById('desc_id').value='';
		//alert(dojos[0].value);
		//alert(dojos[1]);
		//alert(dojos[2].value);
		//dojo.widget.byId("autoproductdesc").setValue("");
		dojos[2].value="";
		//dojo.widget.byId("autoproductdesc").setValue("test");
	}
	
	function deleteProduct(index)
	{
		//alert(index);
		ajax_Country=ajaxFunction();
		ajax_Country.onreadystatechange=function()
	  	{
		   if(ajax_Country.readyState==4)
			{
			response_del=ajax_Country.responseText;
			js_stateid=document.getElementById("productDetails");
			js_stateid.innerHTML= response_del;
			}
	  	}
		url="delete.customsinvoice.product.action?index="+index;
		ajax_Country.open("GET",url,true);
		ajax_Country.send(this);
	
	}
	
	function resetField(eid)
	{
		document.getElementById(eid).value='';
	}
	
	function resetACField(eid)
	{
		dojo.widget.byId(eid).setValue("");
	}
	
	function typenumbers(e,filterString)

	{
		var key, keychar;
		key = getkey(e);
		if (key == null)
		return true;
		
		// get character
		keychar = String.fromCharCode(key);
		keychar = keychar.toLowerCase();
		
		// control keys
		if ((key==null) || (key==0) || (key==8) || (key==9) || (key==27) )
		return true;
		
		// alphas and numbers
		else if ((filterString.indexOf(keychar) > -1))
		return true;
		else
		return false;
	}

	function getkey(e)
	{
		if (window.event)
		return window.event.keyCode;
		else if (e)
		return e.which;
		else
		return null;
	}
	
</SCRIPT>

<div id="customs_invoice_div_hdr">	
	<table>
	<tr align="left">
	<td valign="middle" class="fromAdd_header_table">&nbsp;<mmr:message messageId="label.like.customs.invoice"/>&nbsp;
	<s:checkbox name="shippingOrder.customsInvoiceRequired"  value="%{shippingOrder.customsInvoiceRequired}" id="customs_invoice_checkbox" onclick="toShoworHide(this.checked);"/>
	</td></tr>
	</table>
	</div>
	<div id="radio_billto">
	<div id="loading-billto"><img id="loading-img-billto" style="display:none;" src="<s:url value="/mmr/images/loading.gif" includeContext="true" />" border="0"></div>
		<table  style="width:970px;" border="0" cellspacing="4" cellpadding="2" class="text_01">
			<tr>
				<td colspan="8" class="text_07" align="right" style="background: transparent;" valign="middle">&nbsp;&nbsp;<s:radio id="radio_address" list="#{'1':'Shipper','2':'Consignee','3':'Third Party'}" value="2" onclick="changeBillTo(this.value);" name="shippingOrder.customsInvoice.billTo" cssStyle="width:70px;" /></td>
			</tr>			
		</table>
	</div>
	
	<div id="customs_invoice_panel">
		<div id="radio_table_div">
				<table  style="width:970px;" border="0" cellspacing="4" cellpadding="2" class="text_01">
					<tr>
						<td class="fromAdd_header_table" align="left" valign="top"><mmr:message messageId="label.bill.to"/></td>
						<td>&nbsp;</td>						
					</tr>					
				</table>			
		</div>
		<s:include value="CustomsInvoice_BillToAddress.jsp"/> 
		
		<div id="div_bill_section_two">
			<table style="width:965px;" border="0" cellspacing="3" cellpadding="1" class="text_01">
				<tr>
				<td class="fromAdd_header_table" colspan="2" align="left" valign="top"><mmr:message messageId="label.customs.invoice.info"/></td>
				<td class="text_03" colspan="8" align="right" valign="middle" style="background: transparent;"><font color="red">*</font>&nbsp;<mmr:message messageId="label.mandatory.fields"/></td>
				</tr>
				<tr>	
					<td width="10%" class="text_03"><mmr:message messageId="label.reference.no"/>:</td>
			        <td width="10%"><s:textfield size="20" id="ci.abbreviationName" name="shippingOrder.customsInvoice.reference" value="%{shippingOrder.customsInvoice.reference}" cssClass="text_02_tf" /></td>
					<td width="10%" class="text_03"><mmr:message messageId="label.currency"/>&nbsp;<font color="red">*</font>&nbsp;:</td>
			        <td width="10%">
			   <!-- <s:textfield size="20" id="ci.currency" name="shippingOrder.customsInvoice.currency" value="%{shippingOrder.customsInvoice.currency}" cssClass="text_02_tf_small" onkeypress="return typenumbers(event,\'abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ\')"/>-->
			   		<sx:autocompleter name="shippingOrder.customsInvoice.currency" id="ci.currency" list="{'CAD','USD'}" cssStyle="width:70px;" autoComplete="false" preload="true"/>
			        
			        </td>
			        <td width="10%" class="text_03"><mmr:message messageId="label.total.value"/>&nbsp;<font color="red">*</font>&nbsp;:</td>
			        <td width="10%"><s:textfield size="20" id="ci.totalvalue" name="shippingOrder.customsInvoice.totalValue" value="%{shippingOrder.customsInvoice.totalValue}" cssClass="text_02_tf_small" onkeypress="return typenumbers(event,\'0123456789.\')"/></td>
			        <td width="12%" class="text_03"><mmr:message messageId="label.total.weight"/>&nbsp;<font color="red">*</font>&nbsp;:</td>
			        <td width="10%"><s:textfield size="20" id="ci.totalweight" name="shippingOrder.customsInvoice.totalWeight" value="%{shippingOrder.customsInvoice.totalWeight}" cssClass="text_02_tf_small" onkeypress="return typenumbers(event,\'0123456789.\')"/></td>
			    </tr>					
				<tr>        
					<td width="10%" class="text_03"><mmr:message messageId="label.tax.id"/>:</td>
			        <td width="10%"><s:textfield size="20" id="ci.city" name="shippingOrder.customsInvoice.taxId" value="%{shippingOrder.customsInvoice.taxId}" cssClass="text_02_tf"/></td>		       
					<td width="10%" class="text_03" colspan="3"><mmr:message messageId="label.shippingOrder.exportData"/>:</td>
			        <td width="10%" colspan="2"><s:textfield size="20" id="ci.exportData" name="shippingOrder.customsInvoice.exportData" value="%{shippingOrder.customsInvoice.exportData}" cssClass="text_02_tf" /></td>
				</tr>
								
			</table>
		</div>	
		
		<div id="div_bill_section_three">
			<table  style="width:970px;" border="0" cellspacing="4" cellpadding="2" class="text_01">
			<tr height="25px">
			<td class="fromAdd_header_table" colspan="8" align="left" valign="top"><mmr:message messageId="label.broker.info"/></td>
			</tr>
			<tr>	
				<td width="10%" class="text_03"><mmr:message messageId="label.shippingOrder.company"/>:</td>
		        <td width="20%"><s:textfield size="20" id="ci.abbreviationName" name="shippingOrder.customsInvoice.brokerAddress.abbreviationName" value="%{shippingOrder.customsInvoice.brokerAddress.abbreviationName}" cssClass="text_02_tf" /></td>
				<td width="10%" class="text_03"><mmr:message messageId="label.shippingOrder.address1"/>:</td>
		        <td width="20%"><s:textfield size="20" id="ci.address1" name="shippingOrder.customsInvoice.brokerAddress.address1" value="%{shippingOrder.customsInvoice.brokerAddress.address1}" cssClass="text_02_tf"/></td>
		        <td width="10%" class="text_03"><mmr:message messageId="label.shippingOrder.address2"/>:</td>
		        <td width="20%"><s:textfield size="20" id="ci.address2"  name="shippingOrder.customsInvoice.brokerAddress.address2" value="%{shippingOrder.customsInvoice.brokerAddress.address2}" cssClass="text_02_tf"/></td>
		        <td width="20%" class="text_03"><mmr:message messageId="label.shippingOrder.zip"/>:</td>
		        <td width="20%">
		        	<s:textfield size="20"  id="ci.postcalCode" onblur="javascript:getAddressSuggestTo();"  id="toPostalCode" name="shippingOrder.customsInvoice.brokerAddress.postalCode" value="%{shippingOrder.customsInvoice.brokerAddress.postalCode}" cssClass="text_02_tf" />
		        	<img id="loading-img-to" style="display:none;" src="<s:url value="/mmr/images/loading.gif" includeContext="true" />" border="0">
		        </td>        
			</tr>
			<tr>		
				<td width="10%" class="text_03"><mmr:message messageId="label.shippingOrder.city"/>:</td>
		        <td width="20%"><s:textfield size="20" id="ci.city" name="shippingOrder.customsInvoice.brokerAddress.city" value="%{shippingOrder.customsInvoice.brokerAddress.city}" cssClass="text_02_tf"/></td>
		        <td width="10%" class="text_03"><mmr:message messageId="label.shippingOrder.country"/>:</td>
		        <td width="20%"><s:select cssClass="text_01" cssStyle="width:158px;" listKey="countryCode" listValue="countryName" name="shippingOrder.customsInvoice.brokerAddress.countryCode" list="#session.CountryList" onchange="javascript:showShipToStateb();" headerKey="-1"  id="firstBoxb" theme="simple"/></td>
		        <td width="10%" class="text_03"><mmr:message messageId="label.shippingOrder.state"/>:</td>
		        <td width="20%" id="stateidb"><s:include value="customsInvoiceBrokerProvince.jsp"/></td>					
				<td width="20%" class="text_03"><mmr:message messageId="label.shippingOrder.phone"/>:</td>
		        <td width="20%"><s:textfield size="20" id="ci.phoneNo"  name="shippingOrder.customsInvoice.brokerAddress.phoneNo" value="%{shippingOrder.customsInvoice.brokerAddress.phoneNo}" cssClass="text_02_tf"/></td>
			</tr>	
			<tr>		
				<td width="10%" class="text_03"><mmr:message messageId="label.user.fax"/>:</td>
		        <td width="20%"><s:textfield size="20" id="ci.faxNo" name="shippingOrder.customsInvoice.brokerAddress.faxNo" value="%{shippingOrder.customsInvoice.brokerAddress.faxNo}" cssClass="text_02_tf"/></td>
		        <td width="10%" class="text_03"><mmr:message messageId="label.shippingOrder.email"/>:</td>
		        <td width="20%"><s:textfield size="20" id="ci.emailid" name="shippingOrder.customsInvoice.brokerAddress.emailAddress" value="%{shippingOrder.customsInvoice.brokerAddress.emailAddress}" cssClass="text_02_tf"/></td>
		        <td colspan="4">&nbsp;</td>
		    </tr>	
			</table>
		</div>
	
	<div id="div_bill_section_four_hdr">
			<table>
			<tr>
			<td class="fromAdd_header_table"><mmr:message messageId="label.product.info"/></td>
			</tr>
			</table>
		</div>
	
	<s:include value="populateProducts.jsp"/> 
  	<s:include value="ProductDetails.jsp"/> 
	
	</div>
	<div id="summary_div_tab2">&nbsp;</div>
	<s:include value="hidden.jsp"/> 

</body>
</html>