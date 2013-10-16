<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="mmr" uri="/mmr-tags" %>
<html> 
<head> 
    <title><s:text name="customer.form.title"/></title> 
</head> 
<body> 
<script type="text/javascript" src="<%=request.getContextPath()%>/mmr/scripts/countryProvince.js"></script>

<SCRIPT language="JavaScript">

var pplistr = "<%= request.getAttribute("ppList")%>";
var pplists= pplistr.substring(1,pplistr.length-1);
var pplist = pplists.split(",");

var pclistr = "<%= request.getAttribute("pcList")%>";
var pclists= pclistr.substring(1,pclistr.length-1);
var pclist = pclists.split(",");

var pslistr = "<%= request.getAttribute("psList")%>";
var pslists= pslistr.substring(1,pslistr.length-1);
var pslist = pslists.split(",");

	function submitform()
	{
		if(document.customerform.method.value=='edit'){
			 document.customerform.action = "editcustomerinfo.action";
		}else{

			 document.customerform.action = "createCustomer.action";
		}

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
	
	function checkAllSalesAgent(valSelected)
	{	
		//alert(pplist);
		//alert(pplists.length);
		//alert(pplistr.length);
		var elem = document.getElementById(valSelected);
		var actVal = elem.options[elem.selectedIndex].value;
		//alert(elem.selectedIndex);
		var e = document.getElementsByName("csListSalesCommission");
		var count=0;
		for(var i=0; i < e.length; i++)
		{
			var strSalesUser = e[i].value;			
			if(actVal==strSalesUser)
				count++;
		}
		//alert(pplist[elem.selectedIndex-1]);
		//alert(pplist.length);
		//alert(pplist);
		if(count>0)
		{
			alert("Commissions have already been set for this sales person. Please update the existing values.");
			elem.selectedIndex = 0;
		}	
		else
		{
			//alert("check");
			//calling ajax function to populate the commission percentages
			if(elem.selectedIndex > 0){
				document.getElementById('new_comm_percentage').value = pclist[elem.selectedIndex-1];
				document.getElementById('new_comm_percentage_pp').value = pplist[elem.selectedIndex-1];
				document.getElementById('new_comm_percentage_ps').value = pslist[elem.selectedIndex-1];
			}
			else
			{
				document.getElementById('new_comm_percentage').value = '0.0';
				document.getElementById('new_comm_percentage_pp').value = '0.0';
				document.getElementById('new_comm_percentage_ps').value = '0.0';
			}
		}		
	}
	
	function checkToAdd()
	{
		var e= document.getElementById("salesAgent");
		var cp = document.getElementById("new_comm_percentage");
		var salesagent = e.options[e.selectedIndex].value;
		
		var commission = document.getElementById("new_comm_percentage").value;
		var commission_pp = document.getElementById("new_comm_percentage_pp").value;
		var commission_ps = document.getElementById("new_comm_percentage_ps").value;
		
		if(e.selectedIndex==0)
			alert("Please select a Sales User to add.");
		else if(cp.value > 100 || commission_pp>100 || commission_ps>100)
			alert("Commission percentage cannot be more than 100");
		else
		{	// call the action.
		 	document.customerform.action="add.new.customersales.action?SalesAgent="+salesagent+"&Commission_Percentage="+commission+"&Commission_Percentage_PP="+commission_pp+"&Commission_Percentage_PS="+commission_ps;
		 	document.customerform.submit();
		}
	}
	
	function updateSalesUser(id,index)
	{	

		var cp=document.getElementById("csListSalesCommission[" + index + "]_comm");
		var valcp =cp.value;
		var cp_pp=document.getElementById("csListSalesCommission[" + index + "]_comm_pp");
		var valcp_pp = cp_pp.value;
		var cp_ps=document.getElementById("csListSalesCommission[" + index + "]_comm_ps");
		var valcp_ps = cp_ps.value;

		if(valcp>100 || valcp_pp>100 || valcp_ps>100)
			alert("Commission percentage cannot be more than 100");
		else
		{
			document.customerform.action="update.customer.sales.agent.action?cs_id="+id+"&cp="+valcp+"&cp_pp="+valcp_pp+"&cp_ps="+valcp_ps;
			document.customerform.submit();
		}
	}
	

</SCRIPT> 

<div id="messages">
	<jsp:include page="../common/action_messages.jsp"/>
</div>
<div class="form-container" > 

<s:form action="createCustomer" name="customerform">
</br>

<div id="sales_usr_hdr">
<table cellpadding="4" cellspacing="0" width="1060px">
<tr>
<td width="25%">
<mmr:message messageId="label.manage.sales.users"/>
</td>
<td width="52%" align="left">
<div id="add_cust_name"><s:property value="customer.name"/></div>
</td>
<td align="right" valign="middle">
<div id="sales_user_bck"><img src="<s:url value="/mmr/images/DataGrid_headerSeparatorSkin.png" includeContext="true" />" border="0">&nbsp;<img border="0" src="<%=request.getContextPath()%>/mmr/images/back.png" />&nbsp;</div>
<div id="sales_user_bck_link"><a href="getcustomerinfo.action?method=update&id=${customer.id}" style="text-decoration:none;"><mmr:message messageId="label.back.customer.edit" /></a></div>
</td>
</tr>
</table>
</div>
<div id="sales_user_tab">&nbsp;</div>

<div id="sales_user_srch_hdr">
<table>
<tr>
<td><mmr:message messageId="label.search.results"/></td>
</tr>
</table>
</div>
<div id="sales_user_srch_results">	
	<s:if test="%{customer.CustomerSalesUser.size()>1}">
	<div id="rslt_stmnt"><br/><s:property value="customer.CustomerSalesUser.size()" /><mmr:message messageId="label.search.results.items"/></div>
	</s:if>
	<s:elseif test="%{customer.CustomerSalesUser.size()==1}">
	<div id="rslt_stmnt"><br/><s:property value="customer.CustomerSalesUser.size()" /><mmr:message messageId="label.search.results.item"/></div>
	</s:elseif>
	<s:else>
	<div id="rslt_stmnt"><br/><mmr:message messageId="label.search.results.noitems"/></div>
	</s:else>
	</div>
	
	<div id="sales_user_srch_tbl">
	<table width="1000px" cellpadding="3" cellspacing="0">
		 <tr bgcolor="#cccccc">
		 	<td class="tableTitle3" align="center">#</td>
	        <td class="tableTitle3" align="center"><mmr:message messageId="label.salesAgent"/></td>
	        <td class="tableTitle3" align="center"><mmr:message messageId="label.commission.percentage"/></td>
	        <td class="tableTitle3" align="center"><mmr:message messageId="label.commission.percentage.pp"/></td>
	        <td class="tableTitle3" align="center"><mmr:message messageId="label.commission.percentage.ps"/></td>
	        <td class="tableTitle3" align="center"><mmr:message messageId="label.update"/></td>	
	        <td class="tableTitle3" align="center"><mmr:message messageId="label.delete"/></td>	          	       
		</tr>
		<s:iterator value="customer.CustomerSalesUser" status="counterIndex">	
			<s:set name="custsalesid" value="%{customer.CustomerSalesUser[#counterIndex.index].id}"/>
			<tr>
				<td class="tablerow3" align="center"><s:property value="%{#counterIndex.index+1}"/></td>
		        <td class="tablerow3" align="center"><s:textfield id="csListSalesCommission[#counterIndex.index]" name="csListSalesCommission"  cssClass="text_02_tf" theme="simple" size="3" value="%{customer.CustomerSalesUser[#counterIndex.index].salesAgent}" readonly="true"/></td>
		        <td class="tablerow3" align="center"><s:textfield id="csListSalesCommission[%{#counterIndex.index}]_comm" name="customerSalesCommission"  cssClass="text_02_tf_small" theme="simple" size="3" value="%{customer.CustomerSalesUser[#counterIndex.index].commissionPercentage}"/></td>
		        <td class="tablerow3" align="center"><s:textfield id="csListSalesCommission[%{#counterIndex.index}]_comm_pp" name="customerSalesCommissionPP"  cssClass="text_02_tf_small" theme="simple" size="3" value="%{customer.CustomerSalesUser[#counterIndex.index].commissionPercentagePerPalletService}"/></td>
		        <td class="tablerow3" align="center"><s:textfield id="csListSalesCommission[%{#counterIndex.index}]_comm_ps" name="customerSalesCommissionPS"  cssClass="text_02_tf_small" theme="simple" size="3" value="%{customer.CustomerSalesUser[#counterIndex.index].commissionPercentagePerSkidService}"/></td>
				<td class="tablerow3" align="center">
				<s:a href="javascript: if(confirm('Would you like to update the commissions for this sales person?')){updateSalesUser(%{#custsalesid},%{#counterIndex.index})}">
				<img src="<s:url value="/mmr/images/update.png" includeContext="true" />" border="0" style="text-decoration: none;">
				</s:a></td>
				<td class="tablerow3" align="center">
				<s:a onclick="return confirm('Would you like to delete this sales person from the customer account?')" href="delete.customer.sales.agent.action?cs_id=%{#custsalesid}">
				<img src="<s:url value="/mmr/images/delete.gif" includeContext="true" />" border="0">
				</s:a></td>
			</tr>	
		</s:iterator>
		<tr height="1px">
		<td colspan="6" style="color: #EAECEE;">_________________________________________________________________________________________________________________________</td>
		</tr>
		<tr>
			<td class="tablerow3" align="center"><mmr:message messageId="label.search.addnew"/></td>
	        <td class="tablerow3" align="center"><s:select cssClass="text_01_combo_big" cssStyle="width:135px;" listKey="username" listValue="fullName" 
							name="customer.salesAgent1" headerKey=""  headerValue="---Select---" list="salesUsers" 
								id="salesAgent" theme="simple" onchange="checkAllSalesAgent(this.id)"/></td>
	        <td class="tablerow3" align="center"><s:textfield id="new_comm_percentage" name="new_comm_percentage"  cssClass="text_02_tf_small" theme="simple" size="3" value="0.0" maxlength="5"/></td>
	        <td class="tablerow3" align="center"><s:textfield id="new_comm_percentage_pp" name="new_comm_percentage_pp"  cssClass="text_02_tf_small" theme="simple" size="3" value="0.0" maxlength="5"/></td>
	        <td class="tablerow3" align="center"><s:textfield id="new_comm_percentage_ps" name="new_comm_percentage_ps"  cssClass="text_02_tf_small" theme="simple" size="3" value="0.0" maxlength="5"/></td>
			<td class="tablerow3" align="center"><img src="<s:url value="/mmr/images/sales_user_add_btn.png" includeContext="true" />" border="0" style="cursor: pointer;" onclick="javascript: checkToAdd()"></td>
			<td>&nbsp;</td>
		</tr>
	</table>
	</div>
	<div id="sales_user_tbl_end">&nbsp;</div>


		</s:form>
		</div>
		</body>	  
</html>