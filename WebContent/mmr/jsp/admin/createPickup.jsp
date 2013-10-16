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
	var dojoAdd_key = 0;
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
		
		function assignCompany()
		{
			var dojos = getElementsByClassName("dojoComboBox");
			//alert("Company:::"+dojos[4].value);
			
			var ccvalt = dojos[0].value;
			//alert(ccvalt);
			document.getElementById("pickup.address.abbreviationName").value=ccvalt;
		}
		
		dojo.event.topic.subscribe("/valueChanged", function(value, key, text, widget)
		{
			//fetch only the company name and set it to the autocompleter after ajax
			dojoAdd_key = key;
			setAddress('pickup');
			var companyto = value;
			companyto = companyto.substring(0,companyto.indexOf(","));
			var dojost = getElementsByClassName("dojoComboBox");
			dojost[0].value = companyto;
			document.getElementById("pickup.address.abbreviationName").value=companyto;
		});
		
	function setAddress(type)
	{	
		var autoCompleter="";
		var value =0;
			//autoCompleter = dojo.widget.byId("autoaddressp");
			//alert("1");
			value = dojoAdd_key;
			//alert("To::"+value);
			//ajax call for setting the address
			ajax_ChangeTo=ajaxFunction();
			ajax_ChangeTo.onreadystatechange=function()
		  	{
				   if(ajax_ChangeTo.readyState==4)
					{
					reponse=ajax_ChangeTo.responseText;
					js_stateid=document.getElementById("pickup_inner");
					js_stateid.innerHTML= reponse;
					}
		 	}
			var url="<%=request.getContextPath()%>/admin/selectShippingAddress.action?addressid="+value+"&type="+type;
			ajax_ChangeTo.open("GET",url,true);
			ajax_ChangeTo.send(this);
			//end of ajax call --
	}
		
	function submitPickup(){
		//alert("Pickup Submitted");
		document.searchpickupform.action = "createPickup.action";
		document.searchpickupform.submit();
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
	
	function getkey(e){
		if (window.event)
		  return window.event.keyCode;
		else if (e)
		  return e.which;
		else
		  return null;
	}
</SCRIPT>

	<div id="messages">
		<jsp:include page="../common/action_messages.jsp"/>
	</div>

	<div class="form-container"> 
		<s:form id="searchpickupform" action="search.pickups.action" name="searchpickupform">
		
		<div id="create_pickups_panel">
		<table>
		<tr>
		<td><div id="srch_crtra"><mmr:message messageId="label.create.pickup"/></div></td>
		<td>&nbsp;</td>
		</tr>
		</table>
		</div>
		
		<div id="create_pickups_imgs">
		<table>
		<tr>
		<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
		<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src="<s:url value="/mmr/images/DataGrid_headerSeparatorSkin.png" includeContext="true" />" border="0">&nbsp;<img border="0" src="<%=request.getContextPath()%>/mmr/images/save_icon.png" border="0"/>&nbsp;&nbsp;</td>
		<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
		<td><img src="<s:url value="/mmr/images/DataGrid_headerSeparatorSkin.png" includeContext="true" />" border="0">&nbsp;<img border="0" src="<%=request.getContextPath()%>/mmr/images/cancel.png" border="0"/>&nbsp;</td>
		<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
		<td>
		&nbsp;
		</tr>
		</table>
		</div>
		
		<div id="create_pickups_actions">
		<table>
		<tr>
		<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
		<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
		<td>&nbsp;&nbsp;<a href="javascript: submitPickup()"><mmr:message messageId="label.submit"/></td>
		<td>
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		</td>
		<td>
		<a href="search.pickups.action"><mmr:message messageId="label.btn.cancel"/></td>
		</tr>
		</table>
		</div>
		
		
		
		<div id="create_pickups_table">
			<table width="1150px" class="text_01" cellpadding="0" cellspacing="0">
						<tr>
							<td width="10%" class="srchshipmnt_text_03"><mmr:message messageId="label.pickup.date"/>:</td>
							<td width="14%">
								<s:textfield name="pickup.pickupDate_web" id="f_date_c" cssStyle="width: 90px;"
											cssClass="text_02_tf" readonly="readonly"/><img src="<%=request.getContextPath()%>/mmr/images/icon_Appt.gif"
											id="f_trigger_c" style="cursor: pointer;"
											title="Date selector" border="0" onClick="selectDate('f_date_c','f_trigger_c');">
							</td>
							<td width="9%" class="srchshipmnt_text_03"><mmr:message messageId="label.pickup.readytime"/>:</td>
							<td width="15%">
								<s:select value="%{pickup.readyHour}" name="pickup.readyHour" list="{'00','01','02','03','04','05','06','07','08','09','10','11','12','13','14','15','16','17','18','19','20','21','22','23'}" cssStyle="width: 46px;" class="text_01" ></s:select>
								:&nbsp;<s:select value="%{pickup.readyMin}" name="pickup.readyMin" list="{'00','01','02','03','04','05','06','07','08','09','10','11','12','13','14','15','16','17','18','19','20','21','22','23','24','25','26','27','28','29','30','31','32','33','34','35','36','37','38','39','40','41','42','43','44','45','46','47','48','49','50','51','52','53','54','55','56','57','58','59'}" cssStyle="width: 46px;" class="text_01" ></s:select>
							</td>
							<td width="8%" class="srchshipmnt_text_03"><mmr:message messageId="label.pickup.closetime"/>:</td>
							<td width="15%">
								<s:select value="%{pickup.closeHour}" name="pickup.closeHour" list="{'00','01','02','03','04','05','06','07','08','09','10','11','12','13','14','15','16','17','18','19','20','21','22','23'}" cssStyle="width: 46px;" class="text_01" ></s:select>
								:&nbsp;<s:select value="%{pickup.closeMin}" name="pickup.closeMin" list="{'00','01','02','03','04','05','06','07','08','09','10','11','12','13','14','15','16','17','18','19','20','21','22','23','24','25','26','27','28','29','30','31','32','33','34','35','36','37','38','39','40','41','42','43','44','45','46','47','48','49','50','51','52','53','54','55','56','57','58','59'}" cssStyle="width: 46px;" class="text_01" ></s:select>
							</td>
							<td width="7%" class="srchshipmnt_text_03"><mmr:message messageId="label.pickup.preferred.location"/>:</td>
							<td width="13%">
								<s:select value="%{pickup.pickupLocation}" name="pickup.pickupLocation" list="{'','Front Door','Back Door','Side Door','Receiving','Reception','Office','Mail Room','Garage','Upstairs','Downstairs','Guard House','Third Party','Warehouse'}" cssStyle="width: 106px;" class="text_01" ></s:select>
							</td>	
							<td width="20%" class="srchshipmnt_text_03" align="center"><mmr:message messageId="label.pickup.reference"/>:</td>
							<td width="10%"><s:textfield size="20" key="pickup.pickupReference" name="pickup.pickupReference"  cssClass="text_02_tf" value="%{pickup.pickupReference}"/></td>		
						</tr>
					</table>
			<table width="1150px" border="0" cellpadding="2" cellspacing="0" class="text_01">
						<tr>
							<td width="10%" class="srchshipmnt_text_03"><mmr:message messageId="label.shippingOrder.company"/>:</td>
							<td width="46%" colspan="1">
							<s:hidden name="pickup.address.abbreviationName" id="pickup.address.abbreviationName"/>
							<!-- Check if the From Address exists, then set the Abbreviation Name to the company Autocompleter.  -->
								<s:if test="%{pickup.address.address1 !=null}">
										<s:url id="addressList" action="listAddresses" >
										 <s:param name="pickup.customerId" value="%{pickup.customerId}"/>
										</s:url>
									    <sx:autocompleter preload="false" id="autoaddressp" keyName="address.addressId" name="searchString" href="%{addressList}" dataFieldName="addressSearchResult" loadOnTextChange="true" loadMinimumCount="3" autoComplete="false" valueNotifyTopics="/valueChanged" showDownArrow="false"  value="%{pickup.address.abbreviationName}" onkeyup="javascript: assignCompany();" cssStyle="width: 432px;" />
								</s:if>
								<s:else>
									<s:url id="addressList" action="listAddresses" >
										 <s:param name="shippingOrder.customerId" value="%{shippingOrder.customerId}"/>
										</s:url>
									    <sx:autocompleter preload="false" id="autoaddressf" keyName="address.addressId" name="searchString" href="%{addressList}" dataFieldName="addressSearchResult" loadOnTextChange="true" loadMinimumCount="3" autoComplete="false" valueNotifyTopics="/valueChanged" showDownArrow="false" onkeyup="javascript: assignCompany();" cssStyle="width: 432px;"/>
								</s:else>
							</td>					
							<td width="14%" class="srchshipmnt_text_03"><mmr:message messageId="label.track.carrier"/>:</td>
							<td width="17%" class="text_01">
								<s:select cssClass="text_01_combo_big" cssStyle="width:158px;" listKey="id" listValue="name" 
									name="pickup.carrierId" list="#session.CARRIERS" 
									onchange="javascript:showState();"  id="firstBox" theme="simple"/>
							</td>
							<td width="8%" class="srchshipmnt_text_03" align="center"><mmr:message messageId="label.markup.service"/>:</td>
							<td width="15%" class="text_01" id="stateid">
								<s:select cssClass="text_01_combo_big" cssStyle="width:158px;" listKey="id" listValue="name"
									name="pickup.serviceId" list="#session.SERVICES" 
										headerKey="-1" id="secondBox" theme="simple"/>													
							</td>							
						</tr>	
					</table>
					<div id="pickup_inner">
						<s:include value="pickupAddress_inner.jsp"/>
					</div>
		</div>
		
		<div id="formResult">
		   <s:include value="list_pickups.jsp"></s:include>
		</div>
				
			
	</s:form>	
	</div>
</body>
</html>