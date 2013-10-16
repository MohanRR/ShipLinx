<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="mmr" uri="/mmr-tags" %>
<%@ taglib prefix="sx" uri="/struts-dojo-tags" %>
<html> 
<head>
<sx:head/>
    <title><s:text name="user.form.title"/></title> 
</head> 

<body> 
<script type="text/javascript" src="<%=request.getContextPath()%>/mmr/scripts/jquery-1.4.2.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/mmr/scripts/jquery.autocomplete.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/mmr/styles/style.css" />
<script type="text/javascript">


var default_address_id_from =0;
var default_address_id_to =0;

window.onload = function() {

    	var e = document.getElementById("idRoles");
		var strRole = e.options[e.selectedIndex].value;
		if(strRole=='sales')
		document.getElementById("sales_div").style.display= "block";
		else
		document.getElementById("sales_div").style.display= "none";
		
		showHideSalesDiv('user.userRole');
		
};

	function submitform()
	{
		var msg = "";
		if(document.getElementById("user_timeout").value < 30)
			alert("Session timeout for the User should range from 30 and greater.");
		else
		{
			if(default_address_id_to > 0 && default_address_id_to==default_address_id_from)
				alert("Default To and From addresses cannot be the same")
			else
			{
	 			document.userform.action = "createUser.action";
	 			document.userform.submit();
	 		}
	 	}
	}
	
	function showHideSalesDiv(valname)
	{
		//alert(valname);
		var e = document.getElementsByName(valname);
		var strRole = e[0].options[e[0].selectedIndex].value;
		//alert(strRole);
		if(strRole == 'sales')
		document.getElementById("sales_div").style.display= "block";
		else
		document.getElementById("sales_div").style.display= "none";
		if(strRole == 'customer_admin')
		{
			document.getElementById("rules").innerHTML="- Unrestricted access to all functions within the customer account.<br/>";	
		}
		else if(strRole == 'customer_base')
		{
			document.getElementById("rules").innerHTML="- Access to Shipping information, including create new shipments and view all shipments created under the customer account.<br/> - Access to Address Book functionality. &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; - No access to invoicing module and other admin functions (such as user management).";	
		}		
		else if(strRole == 'customer_shipper')
		{
			document.getElementById("rules").innerHTML="- Restricted access to Shipping information.&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;- Can create new shipments.<br/>- Can view only shipments created by this user.&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;- No access to invoicing module and other admin functions (such as user management).";	
		}
		else
		{
			document.getElementById("rules").innerHTML="";	
		}
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
		
	jQuery(function() {
		$("#defaultFromAddText").autocomplete("<s:url action="listAddresses.action"/>", {extraParams:{customerId: document.userform.cid.value}});
		$("#defaultFromAddText").result(function(event, data, formatted) {
			//alert(data[0]);
			//alert(data[1]);
			document.getElementById("user.defaultFromAddressId").value=data[1];
			default_address_id_from = data[1];
			//document.getElementById("user.defaultFromAddressText").value=data[0];
			});
			
		$("#defaultToAddText").autocomplete("<s:url action="listAddresses.action"/>", {extraParams:{customerId: document.userform.cid.value}});
		$("#defaultToAddText").result(function(event, data, formatted) {
			//alert(data[0]);
			//alert(data[1]);
			document.getElementById("user.defaultToAddressId").value=data[1];
			default_address_id_to = data[1];
			//document.getElementById("user.defaultToAddressText").value=data[0];
			});
		
		// $("#defaultFromAddText").click(function(){$("#defaultFromAddText").val('')});
         $("#defaultFromAddText").blur(function(){if($("#defaultFromAddText").val()==''){
         $("#defaultFromAddText").val('None');
         document.getElementById("user.defaultFromAddressId").value=0;
         }});
          
        // $("#defaultToAddText").click(function(){$("#defaultToAddText").val('')});
         $("#defaultToAddText").blur(function(){if($("#defaultToAddText").val()==''){
         $("#defaultToAddText").val('None');
         document.getElementById("user.defaultToAddressId").value = 0;
         }});		
			
		});
</script> 

<div id="messages">
	<jsp:include page="../common/action_messages.jsp"/>
</div>
<div class="form-container">
<s:form action="createUser" name="userform">
	<s:if test="#session.edit == 'true'">
    	<s:hidden name="method" value="update"/>
    </s:if> 
    <s:else>
    	<s:hidden name="method" value="add"/>
     </s:else>
    <s:hidden name="cid" value="%{user.customerId}" />
<div id="addusr_crtra">
<table width="935px;">
<tr>
	<td class="srch_crt">
	<s:if test="#session.edit == 'true'">
		<mmr:message messageId="label.shippingOrder.editUser"/>
	</s:if>
	<s:else>
		<mmr:message messageId="label.shippingOrder.addNewUser"/>
	</s:else>		
	</td>
	<td class="icon_btns" align="right">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	<img src="<s:url value="/mmr/images/DataGrid_headerSeparatorSkin.png" includeContext="true" />" border="0" style="margin-top:3px;">&nbsp;&nbsp;<s:submit  type="image" src="%{#session.ContextPath}/mmr/images/save_icon.png" />&nbsp;&nbsp;<a href="javascript: submitform()"><mmr:message messageId="label.btn.save"/></a>
&nbsp;&nbsp;<img src="<s:url value="/mmr/images/DataGrid_headerSeparatorSkin.png" includeContext="true" />" border="0">&nbsp;&nbsp;<a href="adduser.action"><img border="0" src="<%=request.getContextPath()%>/mmr/images/reset_icon.png" />&nbsp;&nbsp;<mmr:message messageId="label.btn.reset"/></a></td>
</tr>
</table>
</div>
    
<div id="addusr_srch_panel">
<table width="95%" border="0" cellspacing="0" cellpadding="2">
<tr>
	<td class="markup_tbl_font"><mmr:message messageId="label.user.username"/>:</td>
    <td >
      <s:if test="#session.edit == 'true'">
         <s:textfield size="20"  readonly="true" id="userName" key="user.username" name="user.username" cssClass="text_02_tf" onkeypress="return typenumbers(event,\'abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789-.@\')"/>
      </s:if>
   	  <s:else>
         <s:textfield size="20"  id="userName" key="user.username" name="user.username" cssClass="text_02_tf" onkeypress="return typenumbers(event,\'abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789-.@\')"/>
      </s:else>
    </td>
    <td class="markup_tbl_font"><mmr:message messageId="label.user.password"/>:</td>
    <td><s:password size="20" key="user.password" name="user.password"  cssClass="text_02_tf"/></td>
    <td class="markup_tbl_font"><mmr:message messageId="label.first.name"/>:</td>
    <td><s:textfield size="20" key="user.firstName" name="user.firstName"  cssClass="text_02_tf"/></td>                  
</tr>
<tr>
	<td class="markup_tbl_font"><mmr:message messageId="label.last.name"/>:</td>
    <td><s:textfield size="20" key="user.lastName" name="user.lastName"  cssClass="text_02_tf"/></td>
    <td class="markup_tbl_font"><mmr:message messageId="label.user.phone"/>:</td>
    <td><s:textfield size="20" key="user.phoneNumber" id="phoneNumber" name="user.phoneNumber"  cssClass="text_02_tf" onkeypress="return typenumbers(event,\'0123456789+-()\')"/></td>
    <td class="markup_tbl_font"><mmr:message messageId="label.user.fax"/>:</td>
    <td><s:textfield size="20" key="user.fax" name="user.fax"  cssClass="text_02_tf"/></td>
</tr>
<tr>
	<td class="markup_tbl_font"><mmr:message messageId="label.user.email"/>:</td>
    <td><s:textfield size="20" key="user.email" name="user.email" id="email" cssClass="text_02_tf"/></td>
    <td class="markup_tbl_font"><mmr:message messageId="label.user.enabled"/>:</td>
    <td><s:select key="user.enabled" name="user.enabled" headerKey="1" list="{'true','false'}"   cssClass="text_01_combo_medium" cssStyle="width:120px;"/></td>
    <td class="markup_tbl_font"><mmr:message messageId="label.user.roles"/> : </td>
    <td>						
      <s:select cssClass="text_01_combo_big" cssStyle="width:135px;" name="user.userRole"  listKey="role" listValue="description" 
	  headerKey="-1"  headerValue="--Select Role--" list="#session.availableRoles" theme="simple" onchange="showHideSalesDiv(this.name);" id="idRoles"/><br/>
	</td>                  
</tr>
<tr>
	<td class="markup_tbl_font"><mmr:message messageId="label.user.GL"/>:</td>
    <td><s:textfield size="20" key="user.userGLOrRefNumber" name="user.userGLOrRefNumber" cssClass="text_02_tf"/></td>
	<td class="markup_tbl_font" colspan="2">&nbsp;</td>
	<td class="markup_tbl_font"><mmr:message messageId="label.session.timeout"/>: </td>
	<td class="markup_tbl_font"><s:textfield size="20" key="user.sessionTimeout" name="user.sessionTimeout" id="user_timeout" cssClass="text_02_tf" onkeypress="return typenumbers(event,\'0123456789\')"/></td>
</tr>
<tr>
	<td colspan="6" align="left" valign="top"><p id="rules" style="font-family: Arial; color: #000066; font-size: 8pt; font-weight: bold;">&nbsp;</p></td>
</tr>
</table>
</div>
<div id="print_setup_hdr">
<table><tr><td><mmr:message messageId="label.print.setup"/></td></tr></table>
</div>
<div id="print_setup">
	<s:if test="#session.edit == 'true'">
		<table width="95%" border="0" cellspacing="0" cellpadding="2">
			<tr>
				<td class="markup_tbl_font">
					<mmr:message messageId="label.copies.shipping.label"/>:&nbsp;
					<s:select id="label_copies" cssStyle="width:39px;" cssClass="text_01_combo_small" name="user.printNoOfLabels" list="#{'0':'0','1':'1','2':'2','3':'3','4':'4','5':'5','6':'6','7':'7','8':'8','9':'9','10':'10'}" value="%{user.printNoOfLabels}"/>
				</td>
				<td class="markup_tbl_font">
					<mmr:message messageId="label.copies.customsinvoice"/>:&nbsp;
					<s:select id="customsinv_copies" cssStyle="width:39px;" cssClass="text_01_combo_small" name="user.printNoOfCI" list="#{'0':'0','1':'1','2':'2','3':'3','4':'4','5':'5','6':'6','7':'7','8':'8','9':'9','10':'10'}" value="%{user.printNoOfCI}"/>
				</td>
				<td class="markup_tbl_font">
					<mmr:message messageId="label.preferred.label.size"/>&nbsp;
					<s:select id="label_size" cssClass="text_01_combo_medium" name="user.preferredLabelSize" list="{'--Select--','4 x 6','8 x 11'}"/>
				</td>
				<td class="markup_tbl_font"><mmr:message messageId="label.auto.print"/>:&nbsp;<s:checkbox key="user.autoPrint" name="user.autoPrint" cssClass="text_02" /></td>
			</tr>
		</table>
	</s:if>
	<s:else>
		<table width="95%" border="0" cellspacing="0" cellpadding="2">
		<tr>
			<td class="markup_tbl_font">
				<mmr:message messageId="label.copies.shipping.label"/>:&nbsp;
				<s:select id="label_copies" cssStyle="width:39px;" cssClass="text_01_combo_small" name="user.printNoOfLabels" list="#{'0':'0','1':'1','2':'2','3':'3','4':'4','5':'5','6':'6','7':'7','8':'8','9':'9','10':'10'}" value="1"/>
			</td>
			<td class="markup_tbl_font">
				<mmr:message messageId="label.copies.customsinvoice"/>:&nbsp;
				<s:select id="customsinv_copies" cssStyle="width:39px;" cssClass="text_01_combo_small" name="user.printNoOfCI" list="#{'0':'0','1':'1','2':'2','3':'3','4':'4','5':'5','6':'6','7':'7','8':'8','9':'9','10':'10'}" value="3"/>
			</td>
			<td class="markup_tbl_font">
				<mmr:message messageId="label.preferred.label.size"/>&nbsp;
				<s:select id="label_size" cssClass="text_01_combo_medium" name="user.preferredLabelSize" list="{'--Select--','4 x 6','8 x 11'}" value="%{user.preferredLabelSize}"/>
			</td>
			<td class="markup_tbl_font"><mmr:message messageId="label.auto.print"/>:&nbsp;<s:checkbox key="user.autoPrint" name="user.autoPrint" cssClass="text_02" /></td>
		</tr>
		</table>
	</s:else>
</div>
<div id="sales_div" style="display: none;">
	<table cellpadding="1" width="800px">
		<tr>
			<td class="srch_crt"><mmr:message messageId="label.shippingOrder.salescheckin"/></td>
		</tr>
		<tr><td>&nbsp;</td></tr>
		<tr>
			<td class="markup_tbl_font"><mmr:message messageId="label.commission.percentage"/> :&nbsp;<s:textfield size="10" key="user.commissionPercentage" name="user.commissionPercentage"  cssClass="text_02_tf_medium"/></td>
			<td class="markup_tbl_font"></td>
			<td class="markup_tbl_font"><mmr:message messageId="label.commission.percentage.pp"/> :&nbsp;<s:textfield size="10" key="user.commissionPercentagePP" name="user.commissionPercentagePP"  cssClass="text_02_tf_medium"/></td>
			<td class="markup_tbl_font"></td>
			<td class="markup_tbl_font"><mmr:message messageId="label.commission.percentage.ps"/> :&nbsp;<s:textfield size="10" key="user.commissionPercentagePS" name="user.commissionPercentagePS"  cssClass="text_02_tf_medium"/></td>
			<td class="markup_tbl_font"></td>
		</tr>
		<tr>
			<td class="markup_tbl_font"><mmr:message messageId="label.user.userCode"/> :&nbsp;<s:textfield size="10" key="user.userCode" name="user.userCode"  cssClass="text_02_tf_medium"/></td>
			<td class="markup_tbl_font"></td>
			<td colspan="2" class="markup_tbl_font"><mmr:message messageId="label.user.logoURL"/> :&nbsp;<s:textfield size="10" key="user.logoURL" name="user.logoURL"  cssClass="text_02_tf_bigger"/></td>
			<td class="markup_tbl_font"></td>
		</tr>
	</table>
</div>

<div id="print_setup_hdr">
<table><tr><td><mmr:message messageId="label.addressbook.defaultAddress"/></td></tr></table>
</div>
<div id="print_setup">
	<table cellpadding="3" cellspacing="1" width="800px">
		<tr>
			<td class="markup_tbl_font"><mmr:message messageId="label.addressbook.defaultFromAddress"/>:</td>
			<td class="markup_tbl_font">
			<s:hidden name="user.defaultFromAddressId" id="user.defaultFromAddressId"/>
			<!--<s:if test="%{#request.long_f_add!=null}">
			<s:url id="addressList" action="listAddresses" >
					 <s:param name="shippingOrder.customerId" value="%{user.customerId}"/>
					 <s:param name="adduser" value="yes"/>
					</s:url>
				    <sx:autocompleter id="autoaddressf" keyName="address.addressId" name="searchString" href="%{addressList}" dataFieldName="addressSearchResult" loadOnTextChange="true" loadMinimumCount="0" autoComplete="false" valueNotifyTopics="/valueChangedFrom" cssStyle="width: 600px; background: none;" preload="true" value="%{#request.long_f_add}"/>
			</s:if>
			<s:else>
				<s:url id="addressList" action="listAddresses" >
					 <s:param name="shippingOrder.customerId" value="%{user.customerId}"/>
					 <s:param name="adduser" value="yes"/>
					</s:url>
				    <sx:autocompleter id="autoaddressf" keyName="address.addressId" name="searchString" href="%{addressList}" dataFieldName="addressSearchResult" loadOnTextChange="true" loadMinimumCount="0" autoComplete="false" valueNotifyTopics="/valueChangedFrom" cssStyle="width: 600px; background: none;" preload="true"/>
			</s:else>
			-->
			<s:if test="%{user.defaultFromAddressId > 0}">
				<s:textfield name="user.defaultFromAddressText" id="defaultFromAddText" cssClass="text_02_tf_ac"/>
			</s:if>
			<s:else>
				<s:textfield name="user.defaultFromAddressText" id="defaultFromAddText" cssClass="text_02_tf_ac" value="None"/>
			</s:else>
			</td>
		</tr>
		<tr>
			<td class="markup_tbl_font"><mmr:message messageId="label.addressbook.defaultToAddress"/>:</td>
			<td class="markup_tbl_font">
			<s:hidden name="user.defaultToAddressId" id="user.defaultToAddressId"/>
			<!--<s:if test="%{#request.long_t_add!=null}">
				<s:url id="addressList" action="listAddresses" >
			    <s:param name="shippingOrder.customerId" value="%{user.customerId}"/>
			    <s:param name="adduser" value="yes"/>
			    </s:url>
			    <sx:autocompleter id="autoaddresst" name="searchString" href="%{addressList}" dataFieldName="addressSearchResult" loadOnTextChange="true" loadMinimumCount="0" autoComplete="false" valueNotifyTopics="/valueChangedTo" cssStyle="width: 600px; background: none;" preload="true" value="%{#request.long_t_add}"/>
			</s:if>
			<s:else>
				<s:url id="addressList" action="listAddresses" >
			    <s:param name="shippingOrder.customerId" value="%{user.customerId}"/>
			    <s:param name="adduser" value="yes"/>
			    </s:url>
			    <sx:autocompleter id="autoaddresst" name="searchString" href="%{addressList}" dataFieldName="addressSearchResult" loadOnTextChange="true" loadMinimumCount="0" autoComplete="false" valueNotifyTopics="/valueChangedTo" cssStyle="width: 600px; background: none;" preload="true"/>
			</s:else>
			-->
			<s:if test="%{user.defaultToAddressId > 0}">
				<s:textfield name="user.defaultToAddressText" id="defaultToAddText" cssClass="text_02_tf_ac"/>
			</s:if>
			<s:else>
				<s:textfield name="user.defaultToAddressText" id="defaultToAddText" cssClass="text_02_tf_ac" value="None"/>
			</s:else>
			</td>
		</tr>
	</table>
</div>

		</s:form> 
	</div>
</body>
</html>
    
    
 