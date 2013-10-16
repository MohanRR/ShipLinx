<%
	response.setHeader("Cache-Control","no-cache");
	response.setHeader("Pragma","no-cache");
	response.setDateHeader("Expires",0);
%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="mmr" uri="/mmr-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags"%> 
<%@ taglib prefix="sx" uri="/struts-dojo-tags" %>

<html> 
<head>
    <sx:head/>
    <title><s:text name="user.form.title"/></title> 
</head> 
<body> 
<script type="text/javascript" src="<%=request.getContextPath()%>/mmr/scripts/countryProvince.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/mmr/scripts/jquery-1.4.2.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/mmr/scripts/jquery.autocomplete.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/mmr/styles/style.css" />

<script type="text/javascript">  
  var contextPath = "<%=request.getContextPath()%>";
  var userrole = "<%=request.getAttribute("USERROLE") %>";
  window.onload = function()
  {
	  	var packaging_type = document.getElementById("packType").value;
  		var pallet_opt1 = document.getElementById("hide_this_one");
		var pallet_opt2 = document.getElementById("hide_this_two");
		if(packaging_type=='type_pallet')
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
		}
  	  var add1 = document.getElementsByName("shippingOrder.fromAddress.address1");
	  var anchorfrom = getElementsByClassName("edit_shipfrom");
	  var lblto = document.getElementById("labelto");
	   if(add1[0].value.length <1)
	   {
	   		document.getElementById("div_ship_from").style.display = 'block';
	   		document.getElementById("labelfrom").style.display = 'none';
	   		anchorfrom[0].innerHTML = '[Hide]';
	   }
	   else
	   {
	   		document.getElementById("div_ship_from").style.display = 'none';
	   		document.getElementById("labelfrom").style.display = 'block';
	   		anchorfrom[0].innerHTML = '[Show]';
	   }	
	   lblto.innerHTML = '&nbsp;';
	   
//	   if(document.getElementById("qkship_checkbox").checked)
	 //  {
		   	document.getElementById("qkship_div_panel").style.display = 'block';
		   document.getElementById("qkship_div_panel2").style.display = 'none';
//		   	document.getElementById("get_rates_td").innerHTML = "<a href='javascript:submitQuickShip()' onclick='return (validateOrder(3,1))'><img src='<s:url value='/mmr/images/quick_ship_btn.png' includeContext='true' />' border='0'></a>";
	//   }
	   if(document.getElementById('firstBoxCarrier').value > 0)
		{
	 		if(navigator.appName.indexOf("Microsoft") > -1)
				document.getElementById("qkship_div_panel2").style.display="block";
			else
	 			document.getElementById("qkship_div_panel2").style.display="table-row";
	 	}
	 	changeOtherFields(document.getElementById('billToType').value);
	 	//alert("userrole---->"+userrole);
	 	if(userrole!='null')//disabling the address fields for role 'customer_shipper'
	 	{
	 		//alert("disabling all the address fields of the table");
	 		var from_add_table = document.getElementById("from_add_inner_table");
	 		//disabling all input fields of the table
	 		var f_inputs = from_add_table.getElementsByTagName('input');
	 		 for(var i=0; i<f_inputs.length; ++i)
        		f_inputs[i].disabled=true;
        	//disabling all select fields of the table
        	var f_selects = from_add_table.getElementsByTagName('select');
	 		 for(var i=0; i<f_selects.length; ++i)
        		f_selects[i].disabled=true;
	 		//alert(from_add_table);
	 		var to_add_table = document.getElementById("to_add_inner_table");
	 		//disabling all input fields of the table
	 		var t_inputs = to_add_table.getElementsByTagName('input');
	 		 for(var i=0; i<t_inputs.length; ++i)
        		t_inputs[i].disabled=true;
        	//disabling all select fields of the table
        	var t_selects = to_add_table.getElementsByTagName('select');
	 		 for(var i=0; i<t_selects.length; ++i)
        		t_selects[i].disabled=true;
	 		//alert(to_add_table);
	 		//alert(document.getElementById("autoaddresst"));
	 		document.getElementById("autoaddresst").disabled = true;
	 		//alert(document.getElementById("autoaddressf"));
	 		document.getElementById("autoaddressf").disabled = true;
	 	}
  }  	
</script>  

<SCRIPT language="JavaScript">

var dojos = getElementsByClassName("dojoComboBox");


//	function update_packagetype(){
//	var type= document.getElementById("packType").value;
//	alert(type);
//	ajax_Country=ajaxFunction();
//	ajax_Country.onreadystatechange=function()
//	  {
//		   if(ajax_Country.readyState==4)
//			{
//			reponse=ajax_Country.responseText;
//			js_stateid=document.getElementById("additionalServices");
//			js_stateid.innerHTML= reponse;
//			}
//	  }
//		url="<%=request.getContextPath()%>/shipment.additionalservices.action?type="+type;
//		ajax_Country.open("GET",url,true);
//		ajax_Country.send(this);
	
//	}

	function modifyQuantity(){
	var quantity= document.getElementById("quantity").value;
	var type= document.getElementById("packType").value;
	var pallet_opt1 = document.getElementById("hide_this_one");
	var pallet_opt2 = document.getElementById("hide_this_two");
	var pallet_opt3 = document.getElementById("hide_this_three");
	if(type=='type_env' || type=='type_pak')
	{
		document.getElementById("quantity").value = 1;
		//document.getElementById('dg_field').selectedIndex=0;
		//document.getElementById("dg_field").disabled = true;
	}
	else if(type=='type_pallet')
	{
		if(navigator.appName.indexOf("Microsoft") > -1)
		{
			disableEnableComponents(pallet_opt1, 'block');
			disableEnableComponents(pallet_opt2, 'block');
			disableEnableComponents(pallet_opt3, 'block');
		}
		else 
		{
			disableEnableComponents(pallet_opt1, 'table-row');
			disableEnableComponents(pallet_opt2, 'table-row');
			disableEnableComponents(pallet_opt3, 'table-row');
		}
		//document.getElementById("dg_field").disabled = false;
	}
	else
	{
		disableEnableComponents(pallet_opt1, 'none');
		disableEnableComponents(pallet_opt2, 'none');
		disableEnableComponents(pallet_opt3, 'none');
		//document.getElementById("dg_field").disabled = false;
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
	
	function disableEnableComponents(palletopts, val)
	{
		palletopts.style.display = val;
	}
	
	
	function searchFAddress()
	{
		var autoCompleter = dojo.widget.byId("autoaddress");
    // alert("autoCompleter is::"+autoCompleter); //get this value
     //key (in the states example above, "AL")
     var key = autoCompleter.getSelectedKey();
     //alert("Key is::"+key);
     
     //value (in the states example above, "Alabama")
     var value = autoCompleter.getSelectedValue();
     //alert("Value is::"+value);
     
      var value = autoCompleter.getText();
     //alert("Text is::"+value);
	}
	
	function searchToAddress()
	{		
		var value= document.getElementById("addressToId2").value;
		ajax_Country=ajaxFunction();
		ajax_Country.onreadystatechange=function()
		  {
			   if(ajax_Country.readyState==4)
				{
				reponse=ajax_Country.responseText;
				js_stateid=document.getElementById("addressTo");
				js_stateid.innerHTML= reponse;
				}
		  }
			var url="<%=request.getContextPath()%>/select.customer.from.address.action?addressid="+value+"&type=toAddress";
			ajax_Country.open("GET",url,true);
			ajax_Country.send(this);

	}
	function submitform(method)
	{
		document.userform.action = "shipment.stageThree.action?method=" + method;
	 	document.userform.submit();
	} 	 
	function getRates()
	{
		document.userform.action = "shipment.stageThree.action";
	 	document.userform.submit();
	} 	 
	function updateShipment()
	{
		var custid = document.getElementsByName("shippingOrder.customerId")[0].value;
		var webcustid = document.getElementsByName("shippingOrder.webCustomerId")[0].value;
//		alert('WebCustID:' + webcustid);
		
//		if (webcustid == 'shippingOrder.webCustomerId' || webcustid == 'undefined') {
//			document.getElementsByName("shippingOrder.webCustomerId")[0].value = custid;
//			alert('shippingOrder.webCustomerId:' + document.getElementsByName("shippingOrder.webCustomerId")[0].value);
//		}
		
		if (custid != webcustid) {
			if(confirm("Are you sure, you want to change customer from " + custid + " to " + webcustid + " ?")) {
				document.userform.action = "shipment.update.action";
			 	document.userform.submit();
			}
		} else {
			document.userform.action = "shipment.update.action";
		 	document.userform.submit();
		}
	}	
	function saveCurrentShipment()
	{
		document.userform.action = "save.current.shipment.action";
	 	document.userform.submit();
	}	
	function setCustomer()
	{
		var autoCompleter = dojo.widget.byId("customerSelected");
		var value = autoCompleter.getSelectedKey();
//		autoCompleter.setSelectedKey("4134");
//		alert(value);	
		document.userform.action = "shipment.setcustomer.action?customerId=" + value;
	 	document.userform.submit();

	} 
	
	function showPackage(id)
	{
		var tabContainer = dojo.widget.byId("accordion");
     	tabContainer.selectTab(id);		
	}
	
	function showAddToEdit()
	{
		//alert("Inside showAddToEdit()");	
		var anchorto = getElementsByClassName("edit_shipto");
		var shipto = document.getElementById("div_ship_to");
		var lblto = document.getElementById("labelto");
		//alert(anchorto[0].innerHTML);
		if(anchorto[0].innerHTML != '[Hide]')
		{
			shipto.style.display = 'block';
			lblto.innerHTML = '&nbsp;';
			anchorto[0].innerHTML = '[Hide]';
		}
		else
		{
			shipto.style.display = 'none';
			setLabelvalues('to');
			lblto.style.display = 'block';
			anchorto[0].innerHTML = '[Show]';	
		}		
	}
	
	function showAddFromEdit()
	{	
		//alert("Inside showAddFromEdit()");	
		var anchorfrom = getElementsByClassName("edit_shipfrom");
		var shipfrom = document.getElementById("div_ship_from");
		var lblfrom = document.getElementById("labelfrom");
		//alert(anchorfrom[0].innerHTML);
		if(anchorfrom[0].innerHTML != '[Hide]')
		{
			shipfrom.style.display = 'block';
			//lblfrom.style.display = 'none';
			lblfrom.innerHTML = '&nbsp;';
			anchorfrom[0].innerHTML = '[Hide]';
		}
		else
		{
			shipfrom.style.display = 'none';
			setLabelvalues('from');
			lblfrom.style.display = 'block';
			anchorfrom[0].innerHTML = '[Show]';	
		}			
	}
	
	function setLabelvalues(ft) //from or to?
	{
		var dojos = getElementsByClassName("dojoComboBox");
		//alert(dojos);
		if(ft=='from')
		{
			var labelfrom= "";
			var ctry = document.getElementById("firstBox");
			//alert("ctry:::"+ctry);
			var lblfrom = document.getElementById("labelfrom");
			//alert("lblfrom:::"+lblfrom);
			var province = document.getElementsByName("shippingOrder.fromAddress.provinceCode")[0];
			//alert("province:::"+province);
			labelfrom = dojos[0].value+", "+document.getElementsByName("shippingOrder.fromAddress.address1")[0].value+" "+document.getElementsByName("shippingOrder.fromAddress.address2")[0].value+", "+document.getElementsByName("shippingOrder.fromAddress.city")[0].value+", "+province.options[province.selectedIndex].value+", "+document.getElementById("fromPostalCode").value+", "+ctry.options[ctry.selectedIndex].value;
			lblfrom.innerHTML= labelfrom;			
		}
		else
		{
			var labelto= "";
			var ctry = document.getElementById("firstBox2");
			var lblto = document.getElementById("labelto");
			var province = document.getElementsByName("shippingOrder.toAddress.provinceCode")[0];
			labelto = dojos[2].value+", "+document.getElementsByName("shippingOrder.toAddress.address1")[0].value+" "+document.getElementsByName("shippingOrder.toAddress.address2")[0].value+", "+document.getElementsByName("shippingOrder.toAddress.city")[0].value+", "+province.options[province.selectedIndex].value+", "+document.getElementById("toPostalCode").value+", "+ctry.options[ctry.selectedIndex].value;
			lblto.innerHTML= labelto;		
		}	
	}
	
	function toShoworHide(checked)
	{
		if(checked)
		{
			//alert("Show");
			document.getElementById("pickup_div_panel").style.display = 'block';			
		}
		else
		{
			//alert("Hide");
			document.getElementById("pickup_div_panel").style.display = 'none';			
		}		
	}
	
	function toShoworHideQuickShip(checked)
	{
		if(checked)
		{
			document.getElementById("qkship_div_panel").style.display = 'block';	
			document.getElementById("get_rates_td").innerHTML = "<a href='javascript:submitQuickShip()' onclick='return (validateOrder(3,1))'><img src='<s:url value='/mmr/images/quick_ship_btn.png' includeContext='true' />' border='0'></a>";	
		}
		else
		{
			//alert("Hide");
			document.getElementById("qkship_div_panel").style.display = 'none';	
			document.getElementById("get_rates_td").innerHTML ="<a href='javascript:getRates()' onclick='return (validateOrder(3,1))'><img src='<s:url value='/mmr/images/get_rates_btn.png' includeContext='true' />' border='0'></a>";			
		}		
	}
	
	function submitQuickShip()
	{
		//alert(document.getElementById("firstBoxCarrier").value);
		//alert(document.getElementById("secondBox").value);
		//alert(document.getElementById("qkship_fastest").checked);
		//alert(document.getElementById("qkship_cheapest").checked);
		if(document.getElementById("firstBoxCarrier").value <= 0 && document.getElementById("secondBox").value <= 0 && !document.getElementById("qkship_fastest").checked && !document.getElementById("qkship_cheapest").checked)
			alert("Please select atleast Fastest or Cheapest method if you dont wish to select the Carrier or Service");
		else
		{
			//alert("done");
			document.userform.action = "shipment.stageThree.action";
	 		document.userform.submit();
		}
	}
	
	dojo.event.topic.subscribe("/valueChangedTo", function(value, key, text, widget){
	//alert("changedto::"+value);
	setAddress('toAddress');
	//fetch only the company name and set it to the autocompleter after ajax
	var companyto = value;
	companyto = companyto.substring(0,companyto.indexOf(","));
	//alert(companyto);
	var dojost = getElementsByClassName("dojoComboBox");
	dojost[2].value = companyto;
	document.getElementById("shippingOrder.toAddress.abbreviationName").value=companyto;
		});
		
	dojo.event.topic.subscribe("/valueChangedFrom", function(value, key, text, widget){
	//alert("changedfrom::"+value);
	setAddress('fromAddress');
	//fetch only the company name and set it to the autocompleter after ajax
	var companyfrom = value;
	companyfrom = companyfrom.substring(0,companyfrom.indexOf(","));
	//alert(companyfrom);
	var dojosf = getElementsByClassName("dojoComboBox");
	dojosf[0].value = companyfrom;
	document.getElementById("shippingOrder.fromAddress.abbreviationName").value=companyfrom;
		});	
		
	function setAddress(type)
	{	
		var autoCompleter="";
		var value =0;
		//alert(type);
		if(type=='toAddress')
		{
			autoCompleter = dojo.widget.byId("autoaddresst");
			value = autoCompleter.getSelectedKey();
			//alert("To::"+value);
			//ajax call for setting the address
			ajax_ChangeTo=ajaxFunction();
			ajax_ChangeTo.onreadystatechange=function()
		  	{
				   if(ajax_ChangeTo.readyState==4)
					{
					reponse=ajax_ChangeTo.responseText;
					js_stateid=document.getElementById("toAdd_inner");
					js_stateid.innerHTML= reponse;
					}
		 	}
			var url="<%=request.getContextPath()%>/admin/selectShippingAddress.action?addressid="+value+"&type="+type;
			ajax_ChangeTo.open("GET",url,true);
			ajax_ChangeTo.send(this);
			//end of ajax call --
		}
		else
		{
			autoCompleter = dojo.widget.byId("autoaddressf");
			value = autoCompleter.getSelectedKey();
			//alert("From::"+value);
			//ajax call for setting the address
			ajax_ChangeFrom=ajaxFunction();
			ajax_ChangeFrom.onreadystatechange=function()
		  	{
				   if(ajax_ChangeFrom.readyState==4)
					{
					reponse=ajax_ChangeFrom.responseText;
					js_stateid=document.getElementById("fromAdd_inner");
					js_stateid.innerHTML= reponse;
					}
		  	}
			var url="<%=request.getContextPath()%>/admin/selectShippingAddress.action?addressid="+value+"&type="+type;
			ajax_ChangeFrom.open("GET",url,true);
			ajax_ChangeFrom.send(this);
	//end of ajax call --
		}
		//document.userform.action = "selectShippingAddress.action?addressid="+value+"&type="+type;
		//document.userform.submit();

	}
	
	
	function assignCompany(val)
	{
	//alert(val);
		var dojos = getElementsByClassName("dojoComboBox");
		//alert(dojos);
		//alert("Company:::"+dojos[0].value);
		if(val=='to')
		{
			var ccvalt = dojos[2].value;
			//alert("TO:"+ccvalt);
			document.getElementById("shippingOrder.toAddress.abbreviationName").value=ccvalt;
		}
		else
		{
			var ccvalf = dojos[0].value;
			//alert("FROM:"+ccvalf);
			document.getElementById("shippingOrder.fromAddress.abbreviationName").value=ccvalf;
		}
	}
	
	function changeCustomer(customerval)
	{
		//alert(customerval);
		document.userform.action = "shipment.setcustomer.action?customerId=" + customerval;
	 	document.userform.submit();
	}
	
	function lookup()
	{
		//alert("Looking up...");
		if(document.getElementById("lookupid").value=="" || document.getElementById("lookupid").value.length == 0)
			alert("Please enter Reference value");
		else
		{
			document.userform.action = "list.reference.order.action";
	 		document.userform.submit();
	 	}
	}
	 function showServices() {
	 //alert(document.getElementById('firstBoxCarrier').value);
	 	if(document.getElementById('firstBoxCarrier').value==-1)
	 		document.getElementById("qkship_div_panel2").style.display="none";
	 	else
	 	{
	 		if(navigator.appName.indexOf("Microsoft") > -1)
				document.getElementById("qkship_div_panel2").style.display="block";
			else
	 			document.getElementById("qkship_div_panel2").style.display="table-row";
	 	}
			ajax_Service=ajaxFunction();
			ajax_Service.onreadystatechange=function()
			  {
				   if(ajax_Service.readyState==4)
					{
					reponse=ajax_Service.responseText;
					js_stateid=document.getElementById("stateid_ship");
					js_stateid.innerHTML= reponse;
					}
			  }
			  firstBox = document.getElementById('firstBoxCarrier');
			  url="<%=request.getContextPath()%>/carrier.services.list.action?value="+firstBox.value+"&quickship=true";
				//param="objName=ref_state&country_id="+country_id;
			  	ajax_Service.open("GET",url,true);
			  	ajax_Service.send(this);
		} // End function showState()	
		
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

	function changeOtherFields(val)
	{
		//alert('|'+val+'|');
		if(val == "Third Party" || val == "Collect")
		{
			//alert('enabling...');
			document.getElementById("billToANumber").disabled = false;
			document.getElementById("billToACountry").disabled = false;
			document.getElementById("billToPostalCode").disabled = false;
		}
		else
		{
			//alert('disabling...');
			document.getElementById("billToANumber").value = '';
			document.getElementById("billToACountry").value = 'CA';
			document.getElementById("billToPostalCode").value = '';
			document.getElementById("billToANumber").disabled = true;
			document.getElementById("billToACountry").disabled = true;
			document.getElementById("billToPostalCode").disabled = true;	
		}
	}
</script>

<div id="messages">
	<jsp:include page="../common/action_messages.jsp"/>
</div>
<s:form action="shipment.stageThree" name="userform" id="userform" theme="simple" >
<div class="form-container" >
	<div class="newshipment">
	<div id="pckg_choose">
	<img src="<s:url value="/mmr/images/DataGrid_headerSeparatorSkin.png" includeContext="true" />" border="0" style="margin-bottom:-3px;">
	&nbsp;&nbsp;&nbsp;<img src="<s:url value="/mmr/images/fast_forward.png" includeContext="true" />" border="0" style="margin-bottom:-3px;">&nbsp;&nbsp;  
	<a href="backToShipment.action?switch=true">Switch to Quote Mode</a>
	</div>
	<s:include value="order_SearchQuickAddress.jsp"/>
	<div id="search_reference">
	<table>
		<tr>
			<td class="ref_hdr">
				<mmr:message messageId="label.search.reference"/>&nbsp;&nbsp;(Reference available only from past shipments within this system)
			</td>
		</tr>
	</table>
	<table cellpadding="4" cellspacing="0" width="30%">
		<tr>
			<td class="text_03"><mmr:message messageId="label.reference"/>:</td>
			<td><s:textfield id="lookupid" size="20" key="shippingOrder.referenceValue" name="shippingOrder.referenceValue"  cssClass="text_02_tf" onblur="Javascript: lookup();"/></td>
			<td><!--<s:a href="Javascript: lookup();"><img src="<s:url value="/mmr/images/lookup_btn.png" includeContext="true" />" alt="Lookup" border="0"></s:a></td>-->
		</tr>
	</table>
	</div>
	<div id="fromAdd_header">		
		<table cellpadding="0" cellspacing="0" width="1050px" id="fromaddtab">
			<tr>
				<td align="left" class="fromAdd_header_table" valign="middle" width="124px"><mmr:message messageId="label.shippingOrder.shipFrom"/>:&nbsp;&nbsp;&nbsp;<a href="#" class="edit_shipfrom" onclick="showAddFromEdit()">[<mmr:message messageId="label.show"/>]</a></td>
				<td>&nbsp;&nbsp;&nbsp;</td>
				<td>
				<p id="labelfrom">
				<s:property value="%{shippingOrder.fromAddress.abbreviationName}"/>,&nbsp;<s:property value="%{shippingOrder.fromAddress.address1}"/>&nbsp;<s:property value="%{shippingOrder.fromAddress.address2}"/>,&nbsp;
				<s:property value="%{shippingOrder.fromAddress.city}"/>,&nbsp;<s:property value="%{shippingOrder.fromAddress.provinceCode}"/>,&nbsp;<s:property value="%{shippingOrder.fromAddress.postalCode}"/>,&nbsp;<s:property value="%{#session.shippingOrder.fromAddress.countryCode}"/>.</p>
				</td>
				<td align="right" class="fromAdd_header_table" valign="middle"></td>
			</tr>
		</table>	
	</div>	
	<s:include value="order_SelectedFromAddress.jsp"/>	
	<div id="toAdd_header">		
		<table cellpadding="0" cellspacing="0" width="1050px" id="toaddtab">
			<tr>
				<td align="left" class="toAdd_header_table" valign="middle" width="124px"><mmr:message messageId="label.shippingOrder.shipTo"/>:&nbsp;&nbsp;&nbsp;<a href="#" class="edit_shipto" onclick="showAddToEdit()">[<mmr:message messageId="label.hide"/>]</a></td>
				<td>&nbsp;&nbsp;&nbsp;</td>
				<td>
				<p id="labelto">
					<s:property value="%{shippingOrder.toAddress.abbreviationName}"/>,&nbsp;<s:property value="%{shippingOrder.toAddress.address1}"/>&nbsp;<s:property value="%{shippingOrder.toAddress.address2}"/>,&nbsp;
					<s:property value="%{shippingOrder.toAddress.city}"/>,&nbsp;<s:property value="%{shippingOrder.toAddress.provinceCode}"/>,&nbsp;<s:property value="%{shippingOrder.toAddress.postalCode}"/>,&nbsp;<s:property value="%{#session.shippingOrder.toAddress.countryCode}"/>.</p>			
				</td>
				<td align="right" class="toAdd_header_table" valign="middle"></td>
			</tr>
		</table>		
	</div>	
	
		
	<s:include value="order_SelectedToAddress.jsp"/>
	
	<s:include value="shipping_packages.jsp"/>
	<div id="pckg_div_quote">
	<s:include value="packageDimention.jsp"/>
	</div>
	<div id="pickup_div_hdr">
	<table>
	<tr align="left">
	<td valign="middle" class="fromAdd_header_table">&nbsp;<mmr:message messageId="label.like.pickup.schedule"/>&nbsp;
	<s:checkbox name="shippingOrder.pickup.pickupRequired"  value="%{shippingOrder.pickup.pickupRequired}" id="pickup_checkbox" onclick="toShoworHide(this.checked);"/>
	</td></tr>
	</table>
	</div>
	<div id="pickup_div_panel">
	<table cellpadding="2" cellspacing="4" width="1050px" class="text_01">
		<tr>
		</tr>
		<tr>
			<td class="text_03"><mmr:message messageId="label.pickup.readytime"/>:</td>
			<td>
			<s:select value="%{shippingOrder.pickup.readyHour}" name="shippingOrder.pickup.readyHour" list="{'00','01','02','03','04','05','06','07','08','09','10','11','12','13','14','15','16','17','18','19','20','21','22','23'}" cssStyle="width: 46px;" class="text_01" ></s:select>
			:&nbsp;<s:select value="%{shippingOrder.pickup.readyMin}" name="shippingOrder.pickup.readyMin" list="{'00','01','02','03','04','05','06','07','08','09','10','11','12','13','14','15','16','17','18','19','20','21','22','23','24','25','26','27','28','29','30','31','32','33','34','35','36','37','38','39','40','41','42','43','44','45','46','47','48','49','50','51','52','53','54','55','56','57','58','59'}" cssStyle="width: 46px;" class="text_01" ></s:select>
			</td>
			<td class="text_03"><mmr:message messageId="label.pickup.closetime"/>:</td>
			<td>
			<s:select value="%{shippingOrder.pickup.closeHour}" name="shippingOrder.pickup.closeHour" list="{'00','01','02','03','04','05','06','07','08','09','10','11','12','13','14','15','16','17','18','19','20','21','22','23'}" cssStyle="width: 46px;" class="text_01" ></s:select>
			:&nbsp;<s:select value="%{shippingOrder.pickup.closeMin}" name="shippingOrder.pickup.closeMin" list="{'00','01','02','03','04','05','06','07','08','09','10','11','12','13','14','15','16','17','18','19','20','21','22','23','24','25','26','27','28','29','30','31','32','33','34','35','36','37','38','39','40','41','42','43','44','45','46','47','48','49','50','51','52','53','54','55','56','57','58','59'}" cssStyle="width: 46px;" class="text_01" ></s:select>
			</td>
			<td class="text_03"><mmr:message messageId="label.pickup.preferred.location"/>:</td>
			<td>
			<s:select value="%{shippingOrder.pickup.pickupLocation}" name="shippingOrder.pickup.pickupLocation" list="{'Shipping','Back Door','Downstairs','Front Door','Garage','Guard House','Mail Room','Office','Receiving','Reception','Side Door','Upstairs'}" cssStyle="width: 106px;" class="text_01" ></s:select>
			</td>	
			<td class="text_03"><mmr:message messageId="label.pickup.reference"/>:</td>
			<td><s:textfield size="20" key="shippingOrder.pickup.pickupReference" name="shippingOrder.pickup.pickupReference"  cssClass="text_02_tf" value="%{shippingOrder.pickup.pickupReference}"/></td>		
		</tr>
		<tr>
			<td class="text_03"><mmr:message messageId="label.pickup.instructions"/>:</td>
			<td colspan="2">
			<s:textfield cssStyle="background-color: #F2F2F2; border-radius:4px; border: 1px solid; width:235px; height:22px;" rows="1" key="shippingOrder.pickup.instructions" name="shippingOrder.pickup.instructions"  cssClass="text_02"/>
			</td>			
		</tr>
	</table>
	</div>
	<!--Start: Implementation of Quick Ship UI -->
	<div id="qkship_div_hdr">
	<table>
	<tr align="left">
	<td valign="middle" class="fromAdd_header_table">&nbsp;<mmr:message messageId="label.quick.ship"/>?&nbsp;
	<!--<s:checkbox name="shippingOrder.quickShipRequired"  value="%{shippingOrder.quickShipRequired}" id="qkship_checkbox" onclick="toShoworHideQuickShip(this.checked);"/>-->
	</td></tr>
	</table>
	</div>
	<div id="qkship_div_panel">
	<table cellpadding="4" cellspacing="2" width="1050px" class="text_01">
		<tr>
		</tr>
		<tr>
			<td class="srchshipmnt_text_03" width="12%" align="right"><mmr:message messageId="label.fastest.method"/>&nbsp;:&nbsp;</td>
			<td class="srchshipmnt_text_03" width="10%"><s:checkbox name="shippingOrder.fastestMethod"  value="%{shippingOrder.fastestMethod}" id="qkship_fastest"/></td>
			<td class="srchshipmnt_text_03" width="13%" align="right"><mmr:message messageId="label.cheapest.method"/>&nbsp;:&nbsp;</td>
			<td class="srchshipmnt_text_03" width="10%"><s:checkbox name="shippingOrder.cheapestMethod"  value="%{shippingOrder.cheapestMethod}" id="qkship_cheapest"/></td>
			<td class="srchshipmnt_text_03" align="right"><mmr:message messageId="label.track.carrier"/>:</td>
			<td class="text_01">
				<s:select cssClass="text_01_combo_big" cssStyle="width:120px;" listKey="id" listValue="name" headerKey="-1" headerValue="ANY" name="shippingOrder.carrierId_web" list="#session.CARRIERS" onchange="javascript:showServices();" 
									id="firstBoxCarrier" theme="simple"/>
			</td>
			<td class="srchshipmnt_text_03" align="right"><mmr:message messageId="label.markup.service"/>:</td>
			<td class="text_01" id="stateid_ship">
				<s:select cssClass="text_01_combo_big" cssStyle="width:120px;" listKey="id" listValue="name" name="shippingOrder.serviceId_web" list="#session.SERVICES" 
										headerKey="-1" headerValue="ANY" id="secondBox" theme="simple" value="%{shippingOrder.serviceId_web}"/>													
			</td>							
		</tr>
		<tr id="qkship_div_panel2">
			<td class="srchshipmnt_text_03" width="12%" align="right"><mmr:message messageId="label.bill.to"/>&nbsp;:&nbsp;</td>
			<td class="srchshipmnt_text_03" width="10%">
			<s:select id="billToType" list="{'Soluship Acct','Third Party','Collect'}" name="shippingOrder.billToType" cssStyle="width: 100px;" onchange="changeOtherFields(this.value);"/>
			</td>
			<td class="srchshipmnt_text_03" width="13%" align="right"><mmr:message messageId="label.customer.accountNumber"/>&nbsp;:&nbsp;</td>
			<td class="srchshipmnt_text_03" width="10%"><s:textfield id="billToANumber" name="shippingOrder.billToAccountNum" cssClass="text_02_tf_medium" maxlength="10" disabled="true"/></td>
			<td class="srchshipmnt_text_03" width="10%" align="right"><mmr:message messageId="label.country.account"/>:</td>
			<td class="text_01" width="10%">
				<s:select cssClass="text_01" cssStyle="width:120px;" listKey="countryCode" listValue="countryName" name="shippingOrder.billToAccountCountry" headerKey="-1"  list="#session.CountryList" 
	                  id="billToACountry" theme="simple" disabled="true"/>
			</td>
			<td class="srchshipmnt_text_03" width="15%" align="right"><mmr:message messageId="label.account.zipPostalCode"/>:</td>
			<td class="text_01">
				<s:textfield id="billToPostalCode" name="shippingOrder.billToAccountPostalCode" cssClass="text_02_tf" maxlength="10" cssStyle="width: 120px;" disabled="true"/>													
			</td>							
		</tr>
	</table>
	</div>
	<!-- End: Implementation of Quick Ship UI-->
	
	<s:if test="%{#session.ROLE.contains('busadmin') && shippingOrder.isAdditionalFieldsEditable() != false}">
		<s:include value="shipping_additional_fields.jsp"/>
		<div id="img_save_shipment">
			<a href="javascript:updateShipment()">
				<img src="<s:url value="/mmr/images/save_shipment_btn.png" includeContext="true" />" alt="Save Shipment" border="0">
			</a>
		</div>
	</s:if>
	<s:else>
		<div id="img_get_rates">
		<table cellpadding="3" cellspacing="0">
		<tr>
			<td align="right"><a href="javascript:saveCurrentShipment()"><img src="<s:url value="/mmr/images/save_shipment_btn.png" includeContext="true" />" alt="Save Shipment" border="0" onclick=""></a></td>
			<td align="left" id="get_rates_td"><a href="javascript:getRates()" onclick="return (validateOrder(3,1))"><img src="<s:url value="/mmr/images/get_rates_btn.png" includeContext="true" />" border="0"></a></td>
		</tr>
		</table>
		</div>	
	</s:else>
	
  	</div>
  	</s:form> 

</body>
</html>
    
    
 