<%
	response.setHeader("Cache-Control","no-cache");
	response.setHeader("Pragma","no-cache");
	response.setDateHeader("Expires",0);
%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="mmr" uri="/mmr-tags" %>

<!DOCTYPE html>
<html> 
<head>	
	<title><s:text name="user.form.title"/></title> 
</head> 
<body> 
<script type="text/javascript" src="<%=request.getContextPath()%>/mmr/scripts/countryProvince.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/mmr/scripts/jquery-1.4.2.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/mmr/scripts/jquery.autocomplete.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/mmr/styles/style.css" />

<script type="text/javascript">  
  var contextPath = "<%=request.getContextPath()%>";  
</script>  
<SCRIPT language="JavaScript">
	  var userrole = "<%=request.getAttribute("USERROLE") %>";
	function preloader()
	{
		var pallet = "<%=request.getAttribute("pallet")%>";
		var pallet_opt1 = document.getElementById("hide_this_one_quote");
		var pallet_opt2 = document.getElementById("hide_this_two_quote");
		if(pallet!='null')
		{
			if(navigator.appName.indexOf("Microsoft") > -1)
			{
				disableEnableComponents(pallet_opt1, 'block');
				disableEnableComponents(pallet_opt2, 'block');
			}
			else 
			{
				disableEnableComponents(pallet_opt1, 'table-row');
				disableEnableComponents(pallet_opt2, 'table-row');
			}
			resetwidths();
		}
		else
		{
			setwidths();
		}
		document.getElementById("loading-img-from").style.display = 'none'; 
    	document.getElementById("loading-img-to").style.display = 'none';         
    	
    	if(userrole!='null')//disabling the address fields for role 'customer_shipper'
	 	{
	 		//alert("disabling all the address fields of the table");
	 		var quote_add_table = document.getElementById("quote_address_tbl");
	 		//disabling all input fields of the table
	 		var ft_inputs = quote_add_table.getElementsByTagName('input');
	 		 for(var i=0; i<ft_inputs.length; ++i)
        		ft_inputs[i].disabled=true;
        	//disabling all select fields of the table
        	var ft_selects = quote_add_table.getElementsByTagName('select');
	 		 for(var i=0; i<ft_selects.length; ++i)
        		ft_selects[i].disabled=true;
	 	}  
    }

	function searchFromAddress()
	{
		var value= document.getElementById("addressFromId").value;
		var url="search.customer.from.address.action?searchId="+value+"&type=fromAddress";
		displaySearchAddressFrom(url);
	}
	
	function searchToAddress()
	{
		var value= document.getElementById("addressToId").value;
		var url="search.customer.to.address.action?searchId="+value+"&type=toAddress";
		displaySearchAddressFrom(url);
	}
		  
	function submitform(method)
	{
	 document.userform.action = "packageInformation.action?method="+method;
	 document.userform.submit();
	}     
	
	function getRates()
	{
		document.userform.action = "shipment.stageThree.quote.action";
	 	document.userform.submit();
	}
	
	

//	function update_packagetype(caller){
//	
//		ajax_Country=ajaxFunction();
//		ajax_Country.onreadystatechange=function()
//		  {
//			   if(ajax_Country.readyState==4)
//				{
	//			reponse=ajax_Country.responseText;
	//			js_stateid=document.getElementById("additionalServices");
	//			js_stateid.innerHTML= reponse;
	//			}
//		  }
		 
//			url="shipment.additionalservices.action?value="+caller.id;
//			ajax_Country.open("GET",url,true);
//			ajax_Country.send(this);
		
//	}
	    
	function submitform(method)
	{
	 document.packageform.action = "stageTwo.action?method="+method;
	 document.packageform.submit();
	} 
	
	function modifyQuantity(){
		var quantity= document.getElementById("quantity").value;
		var type= document.getElementById("packType").value;
		var pallet_opt1 = document.getElementById("hide_this_one_quote");
		var pallet_opt2 = document.getElementById("hide_this_two_quote");
		if(type=='type_env' || type=='type_pak')
			document.getElementById("quantity").value = 1;
		if(type=='type_pallet')
		{
			resetwidths();
			if(navigator.appName.indexOf("Microsoft") > -1)
			{
				disableEnableComponents(pallet_opt1, 'block');
				disableEnableComponents(pallet_opt2, 'block');
			}
			else 
			{
				disableEnableComponents(pallet_opt1, 'table-row');
				disableEnableComponents(pallet_opt2, 'table-row');
			}
		}
		else
		{
			setwidths();
			disableEnableComponents(pallet_opt1, 'none');
			disableEnableComponents(pallet_opt2, 'none');
		}
		ajax_Country=ajaxFunction();
		ajax_Country.onreadystatechange=function()
		  {
			   if(ajax_Country.readyState==4)
				{
				reponse=ajax_Country.responseText;
				js_stateid=document.getElementById("dimensions");
				js_stateid.innerHTML= reponse;
				}
		  }
			url="dimensionInformation.action?quantity="+quantity+"&type="+type;
			ajax_Country.open("GET",url,true);
			ajax_Country.send(this);
		
		}
	
	window.onload = preloader;
	
	function setwidths()
	{
		//dynamic width implementation
		var choose = document.getElementById("choose_pckg");
		var choosetext = document.getElementById("choose_pckg_text");
		var quantity = document.getElementById("qty_pckg");
		var refcode = document.getElementById("refcode_pckg");
		var docsonly = document.getElementById("docs_pckg");
		var pckgtable = document.getElementById("pckg_tbl");
			//alert(choose);
			choose.width = '70px';
			choosetext.width = '170px';
			//alert(quantity);
			quantity.width = '70px';
			//alert(refcode);
			refcode.width = '70px';
			//alert(docsonly);
			docsonly.width = '120px';
			pckgtable.width = '900px';
	}
	
	function resetwidths()
	{
		//dynamic width implementation
		var choose = document.getElementById("choose_pckg");
		var quantity = document.getElementById("qty_pckg");
		var refcode = document.getElementById("refcode_pckg");
		var docsonly = document.getElementById("docs_pckg");
		var pckgtable = document.getElementById("pckg_tbl");
		//alert(choose);
			choose.width = '150px';
			//alert(quantity);
			quantity.width = '140px';
			//alert(refcode);
			refcode.width = '140px';
			//alert(docsonly);
			docsonly.width = '130px';
			pckgtable.width = '1090px';
		
	}
	
	function disableEnableComponents(palletopts, val)
	{
		palletopts.style.display = val;
	}
</SCRIPT> 

<script>
// Start Autocomplete Script
		jQuery(function() {
		$("#tocity").autocomplete("<s:url action="getCitySuggest.action"/>", {extraParams:{addressType:'to'}});
		$("#tocity").result(function(event, data, formatted) {
                                var strSelectedCityZip = data[0];
								var strSelectedCity = "";
								var strSelectedZip = "";
								// If city, zip in proper format								
								if(strSelectedCityZip.split(",").length == 2)
								{
		                            strSelectedCity = strSelectedCityZip.split(",")[0];
		                            strSelectedCity = strSelectedCity.replace(/^\s+|\s+$/g, '');

	                                strSelectedZip = strSelectedCityZip.split(",")[1];
	                                strSelectedZip = strSelectedZip.replace(/^\s+|\s+$/g, '');
	                                
	                            }
								else if(strSelectedCityZip.split(",").length == 1)
								{
		                            strSelectedCity = strSelectedCityZip.split(",")[0];
		                            strSelectedCity = strSelectedCity.replace(/^\s+|\s+$/g, '');
									strSelectedZip = "";
								}
								else
								{
									// If mutliple commas found in city name
		                            strSelectedCity = strSelectedCityZip.substr(0, strSelectedCityZip.lastIndexOf(","));
		                            strSelectedCity = strSelectedCity.replace(/^\s+|\s+$/g, '');
									strSelectedZip = strSelectedCityZip.substr(strSelectedCityZip.lastIndexOf(",") + 1, strSelectedCityZip.length);
									strSelectedZip = strSelectedZip.replace(/^\s+|\s+$/g, '');
								}
                                $("#tocity").val(strSelectedCity);   
                                $("#toPostalCode").val(strSelectedZip);

				
                            });		
                            
		$("#toPostalCode").autocomplete("<s:url action="getZipSuggest.action"/>", {extraParams:{addressType:'to'}});
		$("#toPostalCode").result(function(event, data, formatted) {
                                var strSelectedZipCity = data[0];

								var strSelectedCity = "";
								var strSelectedZip = "";

								if(strSelectedZipCity.split(",").length == 2)
								{
	                                strSelectedZip = strSelectedZipCity.split(",")[0];
	                                strSelectedZip = strSelectedZip.replace(/^\s+|\s+$/g, '');
	                                strSelectedCity = strSelectedZipCity.split(",")[1];
	                                strSelectedCity = strSelectedCity.replace(/^\s+|\s+$/g, '');
	                            }
								else if(strSelectedZipCity.split(",").length == 1)
								{
									// If Zip, City combination doesn't found
	                                strSelectedZip = strSelectedZipCity.split(",")[0];
	                                strSelectedZip = strSelectedZip.replace(/^\s+|\s+$/g, '');
									strSelectedCity = "";
								}
								else
								{
									// If multiple commas found
	                                strSelectedZip = strSelectedZipCity.substr(0, strSelectedZipCity.indexOf(","));
	                                strSelectedZip = strSelectedZip.replace(/^\s+|\s+$/g, '');
	                                strSelectedCity = strSelectedZipCity.substr(strSelectedZipCity.indexOf(",") + 1, strSelectedZipCity.length);
	                                strSelectedCity = strSelectedCity.replace(/^\s+|\s+$/g, '');
								}
                                $("#tocity").val(strSelectedCity);   
                                $("#toPostalCode").val(strSelectedZip);   
				
                            });		
        

		$("#fromcity").autocomplete("<s:url action="getCitySuggest.action"/>", {extraParams:{addressType:'from'}});
		$("#fromcity").result(function(event, data, formatted) {
                                var strSelectedCityZip = data[0];
								var strSelectedCity = "";
								var strSelectedZip = "";

                                if(strSelectedCityZip.split(",").length == 2)
                                {
	                                strSelectedCity = strSelectedCityZip.split(",")[0];
	                                strSelectedCity = strSelectedCity.replace(/^\s+|\s+$/g, '');
	                                strSelectedZip = strSelectedCityZip.split(",")[1];
	                                strSelectedZip = strSelectedZip.replace(/^\s+|\s+$/g, '');
	                            }
								else if(strSelectedCityZip.split(",").length == 1)
								{
									// If City, Zip combination doesn't found
	                                strSelectedCity = strSelectedCityZip.split(",")[0];
	                                strSelectedCity = strSelectedCity.replace(/^\s+|\s+$/g, '');
									strSelectedZip = "";
								}
								else
								{
									// If mutliple commas found in city name
		                            strSelectedCity = strSelectedCityZip.substr(0, strSelectedCityZip.lastIndexOf(","));
		                            strSelectedCity = strSelectedCity.replace(/^\s+|\s+$/g, '');
									strSelectedZip = strSelectedCityZip.substr(strSelectedCityZip.lastIndexOf(",") + 1, strSelectedCityZip.length);
									strSelectedZip = strSelectedZip.replace(/^\s+|\s+$/g, '');
								}	

                                $("#fromcity").val(strSelectedCity);   
                                $("#fromPostalCode").val(strSelectedZip);
                                
                                   
                            });		
                            
		$("#fromPostalCode").autocomplete("<s:url action="getZipSuggest.action"/>", {extraParams:{addressType:'from'}});
		$("#fromPostalCode").result(function(event, data, formatted) {
                                var strSelectedZipCity = data[0];
								var strSelectedCity = "";
								var strSelectedZip = "";

								if(strSelectedZipCity.split(",").length == 2)
								{
	                                strSelectedZip = strSelectedZipCity.split(",")[0];
	                                strSelectedZip = strSelectedZip.replace(/^\s+|\s+$/g, '');
	                                strSelectedCity = strSelectedZipCity.split(",")[1];
	                                strSelectedCity = strSelectedCity.replace(/^\s+|\s+$/g, '');
	                            }
								else if(strSelectedZipCity.split(",").length == 1)
								{
									// If Zip, City combination doesn't found
	                                strSelectedZip = strSelectedZipCity.split(",")[0];
	                                strSelectedZip = strSelectedZip.replace(/^\s+|\s+$/g, '');
									strSelectedCity = "";
								}
								else
								{
									// If multiple commas found
	                                strSelectedZip = strSelectedZipCity.substr(0, strSelectedZipCity.indexOf(","));
	                                strSelectedZip = strSelectedZip.replace(/^\s+|\s+$/g, '');
	                                strSelectedCity = strSelectedZipCity.substr(strSelectedZipCity.indexOf(",") + 1, strSelectedZipCity.length);
	                                strSelectedCity = strSelectedCity.replace(/^\s+|\s+$/g, '');
								}


                                $("#fromcity").val(strSelectedCity);   
                                $("#fromPostalCode").val(strSelectedZip);   

                            });		

	});
// End Autocomplete Script		
</script>

<div id="messages">
	<jsp:include page="../common/action_messages.jsp"/>
</div>
<s:form action="shipment.stageThree.quote" name="userform" theme="simple" >
	<div class="form-container" valign="top" >
	
	<div id="ship_from_txt_quote">Ship From:</div>	
	<div id="ship_to_txt_quote">Ship To: &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	<img src="<s:url value="/mmr/images/normal_track.png" includeContext="true" />" border="0" style="margin-bottom:-3px;">&nbsp;<a href="backToShipment.action?switch=true">Switch to Ship Mode</a></div>
	<div id="div_ship_from_quote">
	<table style="width:1014px; margin-left: 40px;" border="0" cellspacing="3" cellpadding="2" class="text_01" id="quote_address_tbl">
				  	<tr><td>&nbsp;</td>
				  	<!--
	                  <td width="12%">&nbsp;</td>
	                  <td width="29%">&nbsp;</td>
	                  
	                  <td width="4%">&nbsp;</td>
	                  <td width="1%">&nbsp;</td>
		              <td colspan="4"  width="58%" class="pckg_icons" style="margin-bottom:-3px;"  height="43px;">
		              	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		              	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		              	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		                  <img src="<s:url value="/mmr/images/DataGrid_headerSeparatorSkin.png" includeContext="true" />" border="0" style="margin-bottom:-2px;">&nbsp;
		                  <s:submit type="image" src="%{#session.ContextPath}/mmr/images/rate_list.png" cssStyle="margin-top:5px;"/>&nbsp;<a style="margin-bottom:-3px;"><mmr:message messageId="menu.getRates"/></a>
		                  &nbsp;&nbsp;<img src="<s:url value="/mmr/images/DataGrid_headerSeparatorSkin.png" includeContext="true" />" border="0" style="margin-bottom:-2px;">&nbsp;		                  
		                  <img src="<%=request.getContextPath()%>/mmr/images/cancel.png" />
		                  &nbsp;<a style="margin-bottom:-3px;"><mmr:message messageId="label.navigation.cancel"/></a></td>-->
	                </tr>
					<tr>
						<td class="text_03" width="12%"><mmr:message messageId="shippingOrder.shipFromId.residential"/>:</td>
						<td width="29%"><s:checkbox cssClass="text_01" value="%{shippingOrder.fromAddress.residential}"  name="shippingOrder.fromAddress.residential"/></td>
						<td width="41%" colspan="2">&nbsp;</td>
						<td width="2%">&nbsp;</td>
					 	<td  width="1%"><img src="<s:url value="/mmr/images/ver_div.jpg" includeContext="true" />" border="0"></td>					  	
					  	<td class="text_03" width="12%"><mmr:message messageId="shippingOrder.shipToId.residential"/>:</td>
						<td width="33%"><s:checkbox cssClass="text_01" value="%{shippingOrder.toAddress.residential}"  name="shippingOrder.toAddress.residential"/></td>	
						<td width="2%">&nbsp;</td>
					</tr>

	                <tr>
			          <td  width="12%" class="text_03"><mmr:message messageId="label.shippingOrder.country"/>:</td>
	                  <td width="29%"><s:select cssClass="text_01" cssStyle="width:158px;" listKey="countryCode" listValue="countryName" name="shippingOrder.fromAddress.countryCode" headerKey="-1"  list="#session.CountryList" 
	                  onchange="javascript:showShipFromState();"  id="firstBox" theme="simple"/>	                  
					  <td  width="12%" class="text_03"><mmr:message messageId="label.shippingOrder.zip"/>:</td>
	                  <td width="29%"><s:textfield size="20" key="shippingOrder.postalCode" onblur="javascript:getAddressSuggestFrom();" id="fromPostalCode" name="shippingOrder.fromAddress.postalCode"  cssClass="text_02_tf" value="%{shippingOrder.fromAddress.postalCode}"/>
	                  <img id="loading-img-from" style="display:none;" src="<s:url value="/mmr/images/loading.gif" includeContext="true" />" border="0"></td>
	                  <td width="2%">&nbsp;</td>
					  <td  width="1%"><img src="<s:url value="/mmr/images/ver_div.jpg" includeContext="true" />" border="0"></td>					 
	                  <td width="12%" class="text_03"><mmr:message messageId="label.shippingOrder.country"/>:</td>
					  <td width="33%"><s:select cssClass="text_01" cssStyle="width:158px;" listKey="countryCode" listValue="countryName" name="shippingOrder.toAddress.countryCode" list="#session.CountryList" onchange="javascript:showShipToState();" headerKey="-1"  id="firstBox2" theme="simple"/></td>	
					  <td width="2%">&nbsp;</td>				  				  
					  <td width="12%" class="text_03"><mmr:message messageId="label.shippingOrder.zip"/>:</td>
					  <td width="33%"><s:textfield size="20" key="shippingOrder.postalCode" name="shippingOrder.toAddress.postalCode" id="toPostalCode" onblur="javascript:getAddressSuggestTo();"  cssClass="text_02_tf" value="%{shippingOrder.toAddress.postalCode}"/>
					  <img id="loading-img-to" style="display:none;"  src="<s:url value="/mmr/images/loading.gif" includeContext="true" />" border="0">
					  </td>	                
	                </tr>

					<tr>
					  <td  width="12%" class="text_03"><mmr:message messageId="label.shippingOrder.city"/>:</td>
	                  <td width="29%" id="fromCity" ><s:textfield size="20" id="fromcity" key="shippingOrder.city" name="shippingOrder.fromAddress.city"  cssClass="text_02_tf" value="%{shippingOrder.fromAddress.city}"/></td>
	                  <td  width="12%" class="text_03"><mmr:message messageId="label.shippingOrder.state"/>:</td>
	    			  <td width="29%" id="stateid"><s:include value="../admin/shippingFromProvienceList.jsp"/></td>
					  <td width="2%">&nbsp;</td>
					  <td  width="1%"><img src="<s:url value="/mmr/images/ver_div.jpg" includeContext="true" />" border="0"></td>
	                  <td width="12%" class="text_03"><mmr:message messageId="label.shippingOrder.city"/>:</td>
       				  <td width="33%"><s:textfield size="20" key="shippingOrder.city" name="shippingOrder.toAddress.city" id="tocity" cssClass="text_02_tf" value="%{shippingOrder.toAddress.city}"/></td>
       				  <td width="2%">&nbsp;</td>
       				  <td width="12%" class="text_03"><mmr:message messageId="label.shippingOrder.state"/>:</td>
					  <td width="33%" id="stateid2"><s:include value="../admin/shippingToProvienceList.jsp"/></td>
					  <td width="2%">&nbsp;</td>
	                </tr>	               
	              </table>
</div>


<div id="pckg_choose_quote">Package:</div>
<div id="pckg_panel_quote">
	<table border="0" width ="1090px" cellspacing="2" cellpadding="2" id="pckg_tbl">	
	<tr><td>&nbsp;</td></tr>			                
			   	<tr>
				  <td class="text_03" width="150px" id="choose_pckg">
					 <legend class="text_03"><strong><mmr:message messageId="label.shippingOrder.choosePackage"/>:</strong></legend>
				  </td>
				  <td class="text_03" id="choose_pckg_text">
			<s:select cssClass="text_01_combo_big" cssStyle="width:158px;" listKey="type" listValue="name" id="packType" name="shippingOrder.packageTypeId.type" 
			list="#session.listPackages" onchange="javascript:modifyQuantity();" headerKey="-1" theme="simple"/>
 				  </td>
 				  <td class="text_03" width="140px" id="qty_pckg"><mmr:message messageId="label.shippingOrder.additionalServices.quantity"/>:</td>
				  <td class="text_01" width="65px"><s:select cssClass="text_01_combo_small" name="shippingOrder.quantity" id="quantity" onchange="modifyQuantity()" cssStyle="width: 43px;" list="{1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35}"  cssClass="text_01_combo_small"></s:select></td>
 				  <td class="text_03" width="120px" id="refcode_pckg">
					<legend class="text_03"><strong><s:label key="shippingOrder.refCode"/>:</strong></legend>
				  </td>
				  <td class="text_03" width="175px">					 
						<s:textfield size="20" key="shippingOrder.refCode" name="shippingOrder.referenceCode" cssClass="text_02_tf" value="%{shippingOrder.referenceCode}"/>						
	              </td>				 
				  <td class="text_03" id="docs_pckg"><mmr:message messageId="label.shippingOrder.docsOnly"/>:</td>
				  <td valign="middle" align="left" class="text_01">
				  <s:checkbox name="shippingOrder.docsOnly"  value="%{#session.shippingOrder.docsOnly}"/>
	              </td>				 
				</tr>
				<tr id="hide_this_one_quote">
					<td class="text_03" width="100px"><mmr:message messageId="label.shippingOrder.additionalServices.tradeShowPickup"/>:</td>
					<td align="left" width="170px"> <s:checkbox name="shippingOrder.tradeShowPickup" value="%{#session.shippingOrder.tradeShowPickup}" /></td>
					<td class="text_03" width="70px"><mmr:message messageId="label.shippingOrder.additionalServices.tradeShowDelivery"/>:</td>
					<td width="65px" align="left"><s:checkbox name="shippingOrder.tradeShowDelivery"  value="shippingOrder.tradeShowDelivery"/></td>
					<td class="text_03" width="95px"><mmr:message messageId="label.shippingOrder.additionalServices.insidePickup"/>:</td>
					<td><s:checkbox name="shippingOrder.insidePickup"  value="shippingOrder.insidePickup"/></td>
					<td class="text_03" align="left" width="140px"><mmr:message messageId="label.shippingOrder.additionalServices.appointmentPickup"/>:</td>
				<td align="left"> <s:checkbox name="shippingOrder.appointmentPickup" value="%{#session.shippingOrder.appointmentPickup}" /></td>
			</tr>
			<tr id="hide_this_two_quote">
				<td class="text_03" width="100px" align="left"><mmr:message messageId="label.shippingOrder.additionalServices.appointmentDelivery"/>:</td>
				<td align="left"><s:checkbox name="shippingOrder.appointmentDelivery"  value="shippingOrder.appointmentDelivery"/></td>
				<td class="text_03" width="70px"><mmr:message messageId="label.shippingOrder.additionalServices.fromTailgate"/>:</td>
				<td width="65px"><s:checkbox name="shippingOrder.fromTailgate"  value="shippingOrder.fromTailgate"/></td>
				<td class="text_03" align="left"><mmr:message messageId="label.shippingOrder.additionalServices.toTailgate"/>:</td>
				<td align="left"> <s:checkbox name="shippingOrder.toTailgate" value="%{#session.shippingOrder.toTailgate}" /></td>
			</tr>
			</table>
</div>

<div id="pckg_div_quote">
<s:include value="packageDimention.jsp"/>
</div>
<div id="pckg_add_services_quote">Additional Services:</div>
<div id="pckg_pnl_header_quote">&nbsp;</div>
<div id="pckg_panel_tbl_quote">
	<table border="0" cellspacing="0" width="950px" cellpadding="2" class="text_01">
               			
          				<tr>
                 			<td valign="middle" nowrap="nowrap" class="text_03" align="right"><mmr:message messageId="label.shippingOrder.satDelivery"/></td>
							<td valign="middle" align="left" class="text_01">
								<s:checkbox name="shippingOrder.satDelivery"  value="%{#session.shippingOrder.satDelivery}"/>
							<td valign="middle" class="text_01">&nbsp;</td>
						
							<td valign="middle" nowrap="nowrap" class="text_03"><mmr:message messageId="label.shippingOrder.additionalServices.scheduledShipDate"/></td>
							<td valign="middle" nowrap="nowrap">
								<table border="0" cellpadding="0" cellspacing="0" class="text_02">
									<tbody>
										<tr>
											<td valign="middle"><s:textfield name="shippingOrder.scheduledShipDate_web" id="f_date_c" size="10" cssStyle="width: 120px;"
													cssClass="text_02_tf" readonly="readonly" value="%{#session.shippingOrder.scheduledShipDate_web}"/>
											<td valign="middle"><img src="<%=request.getContextPath()%>/mmr/images/icon_Appt.gif"
													id="f_trigger_c" style="cursor: pointer;"
													title="Date selector" border="0" onClick="selectDate('f_date_c','f_trigger_c');"> 
											</td>
										</tr>
									</tbody>
								</table>
							</td>
							<td valign="middle">&nbsp;</td>
							<s:set name="cName" value="%{#session.shippingOrder.fromAddress.countryName}"/>
						<s:if test ='%{#cName ==  "CA"}'>
							<td colspan="1" valign="middle" nowrap="nowrap"  class="text_03" ><mmr:message messageId="label.shippingOrder.additionalServices.signatureRequiredCanada"/></td>
							<td valign="middle">
								<s:select value="%{#session.shippingOrder.signatureRequired}" name="shippingOrder.signatureRequired" list="#{'1':'No','2':'Yes'}" cssStyle="width: 120px;" cssClass="text_01" ></s:select>
							</td>
						</s:if>
						<s:else>
							<td colspan="1" valign="middle" class="text_03"><mmr:message messageId="label.shippingOrder.additionalServices.signatureRequired"/></td>
							<td valign="middle">
								<s:select value="%{#session.shippingOrder.signatureRequired}" name="shippingOrder.signatureRequired" cssStyle="width: 120px;" list="#{'1':'No','2':'Del. Confirmation','3':'Signature','4':'Adult Signature'}" cssStyle="width: 120px;" cssClass="text_01" ></s:select>
							</td>   
						</s:else>	
							<td valign="middle" class="text_03" align="right"><mmr:message messageId="label.shippingOrder.additionalServices.holdForPickupRequired"/></td>
							<td valign="middle" align="left" class="text_01">
								<s:checkbox name="shippingOrder.holdForPickupRequired" value="%{#session.shippingOrder.holdForPickupRequired}" />
					  		</td>
		 				</tr>
					</table>
</div>
<!--  <div id="res_tbl_end_quote"></div>-->
<div id="img_get_rates_quote">
	<a href="javascript:getRates()" onclick="return (validateOrder(3,1))"><img src="<s:url value="/mmr/images/get_rates_btn.png" includeContext="true" />" border="0"></a>
	</div>
		
	</s:form> 
</body>
</html>