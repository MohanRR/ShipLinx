<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags"%> 
<%@ taglib prefix="mmr" uri="/mmr-tags" %>
<%@ taglib prefix="sx" uri="/struts-dojo-tags" %>

<html> 
<head> 
    <title><s:text name="markup.title"/></title> 
    <sj:head jqueryui="true" />
    <sx:head />
</head> 
<body> 
<SCRIPT language="JavaScript">
	function searchMarkup()
	{
	 document.searchform.action = "searchMarkup.action";
	 document.searchform.submit();
	}
	function addMarkup()
	{
	 document.searchform.action = "addMarkup.action";
	 document.searchform.submit();
	}	
	function saveMarkupList()
	{
	 document.searchform.action = "saveMarkupList.action";
	 document.searchform.submit();
	}
	function defaultmarkup(customerId)
	{
 	 document.searchform.action = "defaultmarkup.action?customerId="+customerId;
	 document.searchform.submit();
	}
	function copyCustomerMarkup()
	{
	 document.searchform.action = "copyCustomerMarkup.action";
	 document.searchform.submit();
	}	

	function copyCustomerMarkup()
	{
	 document.searchform.action = "copyCustomerMarkup.action";
	 document.searchform.submit();
	}	
	
	function getAccountInformation(url){
		window.open(url,'','width=760,height=540,left=100,top=100,scrollbars=1');
	}
    function showServices() {
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
		  url="<%=request.getContextPath()%>/markup.listService.action?value="+firstBox.value;
			//param="objName=ref_state&country_id="+country_id;
		  	ajax_Service.open("GET",url,true);
		  	ajax_Service.send(this);
	} // End function showState()	
	
	
</SCRIPT>

<div id="messages">
<jsp:include page="../common/action_messages.jsp"/>
</div>

<div class="form-container"> 
<s:form action="searchMarkup" name="searchform">
<div id="mrkup_srch_panel">
<table>
<tr>
<td ><div id="srch_crtra">Markups</div></td>
<td align="center"><div id="filter"><s:property value="%{markup.customerBusName}"/></div></td>
</tr>
</table>
</div>
<div id="markup_table">
<table border="0" cellpadding="5" cellspacing="0">
				<tr>
					<td class="markup_tbl_font"><mmr:message messageId="label.track.carrier"/></td>
					<td class="markup_tbl_font"><mmr:message messageId="label.markup.service"/></td>
					<td class="markup_tbl_font"><mmr:message messageId="label.markup.fromCountry"/></td>
					<td class="markup_tbl_font"><mmr:message messageId="label.markup.toCountry"/></td>
					<td class="markup_tbl_font"><mmr:message messageId="label.markup.type"/></td>
					<td class="markup_tbl_font"><mmr:message messageId="label.markup.fromWeight"/></td>
					<td class="markup_tbl_font"><mmr:message messageId="label.markup.toWeight"/></td>					
					<td class="markup_tbl_font"><mmr:message messageId="label.markup.percentage"/></td>
					<td class="markup_tbl_font"><mmr:message messageId="label.markup.flat"/></td>
				</tr>	
				<tr>
					<td>
						<s:select cssClass="text_01_combo_big"  listKey="id" listValue="name" cssStyle="width:125px;" 
							name="markup.carrierId" headerValue="" headerKey="-1"  list="#session.CARRIERS" 
								onchange="javascript:showServices();"  id="firstBox" theme="simple" size="1" />
					</td>				
					<td id="stateid">
						<s:select cssClass="text_01_combo_big" cssStyle="width:125px;" listKey="id"
							listValue="name" name="markup.serviceId" list="#session.SERVICES" 
							 headerKey="-1" id="service" theme="simple"/>						
					</td>
					<td>
						<s:select cssClass="text_01_combo_big" cssStyle="width:125px;" listKey="countryCode" 
							listValue="countryName" name="markup.fromCountryCode" list="#session.COUNTRIES" 
							 headerKey="" headerValue="ANY" id="fromCountry" theme="simple"/>
						
					<td>
						<s:select cssClass="text_01_combo_big" cssStyle="width:125px;" listKey="countryCode" 
							listValue="countryName" name="markup.toCountryCode" list="#session.COUNTRIES" 
							 headerKey="" headerValue="ANY" id="toCountry" theme="simple"/>						
					</td>
					<td>
						<s:select value="%{markup.typeText}" id="type"  cssClass="text_01_combo_medium" cssStyle="width:90px;"
							name="markup.typeText" list="{'', 'Markup','Markdown'}" theme="simple" />
					</td>
					<td>
						<s:textfield size="5" key="markup.fromWeight" name="markup.fromWeight" cssClass="text_02_tf_small"/>
					</td>
					<td>
						<s:textfield size="5" key="markup.toWeight" name="markup.toWeight" cssClass="text_02_tf_small"/>
					</td>					
					<td>
						<s:textfield size="5" key="markup.markupPercentage" name="markup.markupPercentage" cssClass="text_02_tf_small"/>
					</td>
					<td>
						<s:textfield size="5" key="markup.markupFlat" name="markup.markupFlat" cssClass="text_02_tf_small"/>
					</td>	
					
				</tr>		
				<s:if test='%{markup.customerBusName != "Default"}' >
				<tr>
					<td class="markup_tbl_font" colspan="1">  
							<s:a onclick="return confirm('Do you really want to copy markup(s) from selected customer ?')"  href="javascript: copyCustomerMarkup()"> 
								Copy Markup 
							</s:a> &nbsp;&nbsp;&nbsp;&nbsp;From
						</td>
						<td  class="text_01">
							<s:if test="%{#session.ROLE.contains('busadmin')}">								
		            			<s:url id="customerList" action="listCustomers" />
		            			<sx:autocompleter keyName="markup.sourceCustomerId" name="searchString" 
		            				href="%{customerList}" dataFieldName="customerSearchResult"  
		            					cssStyle="width:120px;" loadOnTextChange="true" loadMinimumCount="3"/>
		            		</s:if>
		            	</td>
						
						<td class="markup_tbl_font" colspan="4">To &nbsp;&nbsp;&nbsp;<s:label key="markup.customerBusName" /> </td>
						<td class="markup_tbl_font">&nbsp;</td>
				</tr>
				</s:if>	
				<s:else>
					<tr>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
					</tr>
				</s:else>				
			</table>			
</div>
<div id="markup_tab">&nbsp;</div>
<div id="markup_srchactions_imgs">
<table>
<tr>
<td><img src="<s:url value="/mmr/images/DataGrid_headerSeparatorSkin.png" includeContext="true" />" border="0">&nbsp;</td>
<td><img src="<s:url value="/mmr/images/search_icon.png" includeContext="true" />" border="0">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src="<s:url value="/mmr/images/DataGrid_headerSeparatorSkin.png" includeContext="true" />" border="0">&nbsp;</td>
<td><img src="<s:url value="/mmr/images/addNew_icon.png" includeContext="true" />" border="0">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src="<s:url value="/mmr/images/DataGrid_headerSeparatorSkin.png" includeContext="true" />" border="0">&nbsp;</td>
<td><img src="<s:url value="/mmr/images/reset_icon.png" includeContext="true" />" border="0">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src="<s:url value="/mmr/images/DataGrid_headerSeparatorSkin.png" includeContext="true" />" border="0">&nbsp;</td>
<td><img src="<s:url value="/mmr/images/save_icon.png" includeContext="true" />" border="0">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
</tr>
</table>
</div>
<div id="markup_srch_actions">
<table>
<tr>
<td><a href="javascript: searchMarkup()"><mmr:message messageId="label.search.btn.search"/></a> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
<td><a href="javascript: addMarkup()"><mmr:message messageId="label.search.addnew"/></a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
<td><a href="javascript: defaultmarkup('<s:property value="%{markup.customerId}"/>')"><mmr:message messageId="label.btn.reset"/></a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
<td><a href="javascript: saveMarkupList()"><mmr:message messageId="label.btn.save"/></a>&nbsp;&nbsp;&nbsp;</td>
</tr>
</table>
</div>
<div id="markup_res"><mmr:message messageId="label.search.results"/></div>
<div id="markup_results">	
	<s:if test="%{markupList.size()>1}">
	<div id="rslt_stmnt"><br/><s:property value="markupList.size()" /><mmr:message messageId="label.search.results.items"/></div>
	</s:if>
	<s:elseif test="%{markupList.size()==1}">
	<div id="rslt_stmnt"><br/><s:property value="markupList.size()" /><mmr:message messageId="label.search.results.item"/> </div>
	</s:elseif>
	<s:else>
	<div id="rslt_stmnt"><br/><mmr:message messageId="label.search.results.noitems"/></div>
	</s:else>
	</div>		
		
		<div id="markup_result_tbl">
			<table width="96%" border="0" cellpadding="4" cellspacing="0">
				<tr>
					<td class="markup_text_03"><strong><mmr:message messageId="label.shippingOrder.id"/></strong></td>
					<td class="markup_text_03"><strong><mmr:message messageId="label.track.carrier"/></strong></td>
					<td class="markup_text_03"><strong><mmr:message messageId="label.markup.service"/></strong></td>
					<td class="markup_text_03"><strong><mmr:message messageId="label.markup.fromCountry"/></strong></td>
					<td class="markup_text_03"><strong><mmr:message messageId="label.markup.toCountry"/></strong></td>
					<td class="markup_text_03"><strong><mmr:message messageId="label.markup.type"/></strong></td>
					<td class="markup_text_03"><strong><mmr:message messageId="label.markup.fromWeight"/></strong></td>
					<td class="markup_text_03"><strong><mmr:message messageId="label.markup.toWeight"/></strong></td>					
					<td class="markup_text_03"><strong><mmr:message messageId="label.markup.percentage"/></strong></td>
					<td class="markup_text_03"><strong><mmr:message messageId="label.markup.flat"/></strong></td>
					<td class="markup_text_03"><strong><mmr:message messageId="label.markup.disabled"/></strong></td>
					<td class="markup_text_03"></td>
				</tr>			
				<tr>
					<s:iterator id="markupTable" value="markupList" status="rowstatus">
								<tr>
								<s:if test="#rowstatus.even == true">
									<td class="even"><s:property value="customerId"/></td>
									<td class="even"><s:property value="carrierName"/></td>
									<td class="even"><s:property value="serviceName"/></td>
									<td class="even"><s:property value="fromCountryName"/></td>
									<td class="even"><s:property value="toCountryName"/></td>
									<td class="even">
										<s:if test='%{type == 1}' >
											Markup
										</s:if>														
										<s:elseif test='%{type == 2}' >
											Markdown
										</s:elseif>
									</td>
									<td class="even"><s:property value="fromWeight"/></td>
									<td class="even"><s:property value="toWeight"/></td>
									<td class="even"><s:textfield size="5" key="markupPercentage" name="markupPercentage" cssClass="text_02_tf_small"/></td>
									<td class="even"><s:textfield size="5" key="markupFlat" name="markupFlat" cssClass="text_02_tf_small"/></td>
									<td class="even"><s:select key="disabledFlag" name="disabledFlag" headerKey="1" list="{'true','false'}"   cssClass="text_01_combo_small" cssStyle="width: 60px;"/></td>
						            <td class="even">
						            	<s:if test='%{customerId == 0}' >
						            		<s:a onclick="return confirm('Do you really want to apply the selected markup to all customers?')"  href="applyToAllCustomersMarkup.action?method=applyToAll&serviceId=%{serviceId}&fromCountryCode=%{fromCountryCode}&toCountryCode=%{toCountryCode}"> 
						            		<img src="<s:url value="/mmr/images/edit_pencil.png" includeContext="true" />" alt="Apply to all Customers" border="0"> </s:a>
						            	</s:if>
					           
								    	<s:if test='%{!(fromCountryCode == "ANY" && toCountryCode == "ANY")}' >
								    		<s:a onclick="return confirm('Do you really want to delete the selected markup?')" href="deleteMarkup.action?method=deletetMarkup&serviceId=%{serviceId}&fromCountryCode=%{fromCountryCode}&toCountryCode=%{toCountryCode}">
								    		<img src="<s:url value="/mmr/images/delete.gif" includeContext="true" />" alt="Delete Markup" border="0"> </s:a>
								    	</s:if>
								    	<s:if test='%{serviceType==1 || serviceType==2}' >
								    		<s:a href="viewUploadRateTemplate.action?method=viewUploadRateTemplate&serviceId=%{serviceId}&customerId=%{customerId}">
						    				<img src="<s:url value="/mmr/images/upload_file.png" includeContext="true" />" alt="Upload Rate Template" border="0"> </s:a>
						    			</s:if>									    	
					           		</td>								
								</s:if>
								<s:else>
									<td class="odd"><s:property value="customerId"/></td>
								<td class="odd"><s:property value="carrierName"/></td>
								<td class="odd"><s:property value="serviceName"/></td>
								<td class="odd"><s:property value="fromCountryName"/></td>
								<td class="odd"><s:property value="toCountryName"/></td>
								<td class="odd">
									<s:if test='%{type == 1}' >
										Markup
									</s:if>														
									<s:elseif test='%{type == 2}' >
										Markdown
									</s:elseif>
								</td>
								<td class="odd"><s:property value="fromWeight"/></td>
								<td class="odd"><s:property value="toWeight"/></td>
								<td class="odd"><s:textfield size="5" key="markupPercentage" name="markupPercentage" cssClass="text_02_tf_small"/></td>
								<td class="odd"><s:textfield size="5" key="markupFlat" name="markupFlat" cssClass="text_02_tf_small"/></td>
								<td class="odd"><s:select key="disabledFlag" name="disabledFlag" headerKey="1" list="{'true','false'}"   cssClass="text_01_combo_small" cssStyle="width: 60px;"/></td>
					            <td class="odd">
					            	<s:if test='%{customerId == 0}' >
					            		<s:a onclick="return confirm('Do you really want to apply the selected markup to all customers?')"  href="applyToAllCustomersMarkup.action?method=applyToAll&serviceId=%{serviceId}&fromCountryCode=%{fromCountryCode}&toCountryCode=%{toCountryCode}"> 
					            		<img src="<s:url value="/mmr/images/edit_pencil.png" includeContext="true" />" alt="Apply to all Customers" border="0"> </s:a>
					            	</s:if>
					           
							    	<s:if test='%{!(fromCountryCode == "ANY" && toCountryCode == "ANY")}' >
							    		<s:a onclick="return confirm('Do you really want to delete the selected markup?')" href="deleteMarkup.action?method=deletetMarkup&serviceId=%{serviceId}&fromCountryCode=%{fromCountryCode}&toCountryCode=%{toCountryCode}">
							    		<img src="<s:url value="/mmr/images/delete.gif" includeContext="true" />" alt="Delete Markup" border="0"> </s:a>
							    	</s:if>
							    	<s:if test='%{serviceType==1 || serviceType==2}' >
						    			<s:a href="viewUploadRateTemplate.action?method=viewUploadRateTemplate&serviceId=%{serviceId}&customerId=%{customerId}">
						    			<img src="<s:url value="/mmr/images/upload_file.png" includeContext="true" />" alt="Upload Rate Template" border="0"> </s:a>
						    		</s:if>							    	
					           </td>
								</s:else>								
							</tr>
						</s:iterator>
				</tr>
			</table>
		</div>
		<div id="markup_res_tbl_end"></div>
	</div>
	
</s:form>
</div>
</div>
