<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags"%> 
<%@ taglib prefix="mmr" uri="/mmr-tags" %>
<%@ taglib prefix="sx" uri="/struts-dojo-tags" %>

<html> 
 
<head> 
	<title><s:text name="edi.title"/></title> 
	<sj:head jqueryui="true" />
	<sx:head />
	
</head> 
<body> 
<SCRIPT language="JavaScript">
	function searchEdi()
	{
	 document.searchform.action = "searchedi.action";
	 document.searchform.submit();
	}
	
	function releaseAll() {
		var autoCompleter = dojo.widget.byId("ediFileNameId");
		var value = autoCompleter.getSelectedValue();
		if (value == null || value == "") {
			alert("Please select EDI File to Release...");
		} else {
			if (confirm("Do you really want to release charges for " + value +" EDI File?")) {
				 document.searchform.action = "release.edi.file.action?method=releaseEdiFile&ediFileName=" + value;
				 document.searchform.submit();	
			}
		}
//		alert("Value is::"+value);
//		var value = autoCompleter.getText();
//		alert("Text is::"+value);		
	}
	
</SCRIPT>

<div id="messages">
<jsp:include page="../common/action_messages.jsp"/>
</div>

<div class="form-container"> 
<s:form id="searchform" action="searchedi.action" name="searchform">

<div id="edi_srch_panel">
<table>
<tr>
<td><div id="srch_crtra"><mmr:message messageId="menu.admin.searchedi"/></div></td>
<td>&nbsp;</td>
</tr>
</table>
</div>

<div id="edi_srchactions_imgs">
<table>
	<tr>
		<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
		<td><img src="<s:url value="/mmr/images/DataGrid_headerSeparatorSkin.png" includeContext="true" />" border="0">&nbsp;</td>
		<td><img src="<s:url value="/mmr/images/search_icon.png" includeContext="true" />" border="0">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src="<s:url value="/mmr/images/DataGrid_headerSeparatorSkin.png" includeContext="true" />" border="0">&nbsp;</td>
		<td><img src="<s:url value="/mmr/images/release_all.png" includeContext="true" />" border="0">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src="<s:url value="/mmr/images/DataGrid_headerSeparatorSkin.png" includeContext="true" />" border="0">&nbsp;</td>
		<td><img src="<s:url value="/mmr/images/reset_icon.png" includeContext="true" />" border="0">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
	</tr>
</table>
</div>

<div id="edi_srch_actions">
<table>
	<tr>
		<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
		<td><a href="javascript: searchEdi()"><mmr:message messageId="label.search.btn.search"/></a> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
		<td><a href="javascript: releaseAll()"><mmr:message messageId="label.edi.release.all"/></a> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
		<td><a href="searchedi.action?method=reset"><mmr:message messageId="label.btn.reset"/></a>&nbsp;&nbsp;&nbsp;</td>
	</tr>
</table>
</div>

<div id="edi_srch_tbl">
<table width="800px" border="0" cellpadding="4" cellspacing="0">
				<tr>
					<td class="edi_text_03">From Date</td>
					<td class="edi_text_03">To Date</td>
					<td class="edi_text_03">Invoice #</td> 
					<td class="edi_text_03">File Name</td>
					<td class="edi_text_03">Carrier</td>
					<td class="edi_text_03">Status</td>
				</tr>	
				<tr>					
					<td>
					<s:textfield name="ediItem.fromDate" id="f_date_c" cssStyle="width: 75px;"
											cssClass="text_02_tf" readonly="readonly"/><img src="<%=request.getContextPath()%>/mmr/images/icon_Appt.gif"
											id="f_trigger_c" style="cursor: pointer;"
											title="Date selector" border="0" onClick="selectDate('f_date_c','f_trigger_c');">
				<!--  	<sj:datepicker name="ediItem.fromDate" displayFormat="yy-mm-dd"/></td>-->
					<td>
					<s:textfield name="ediItem.toDate" id="t_date_c" cssStyle="width: 75px;"
											cssClass="text_02_tf" readonly="readonly"/><img src="<%=request.getContextPath()%>/mmr/images/icon_Appt.gif"
											id="t_trigger_c" style="cursor: pointer;"
											title="Date selector" border="0" onClick="selectDate('t_date_c','t_trigger_c');">
			<!--  		<sx:datetimepicker name="ediItem.toDate" displayFormat="yy-mm-dd"/>-->
					</td>
					<td class="edi_text_03">
		            	<s:url id="invoiceList" action="listEdiInvoice" />
		            	<sx:autocompleter keyName="invoiceNumberKeyName" name="ediItem.invoiceNumber" 
		            		href="%{invoiceList}" dataFieldName="ediInvoiceSearchResult"  
		            		cssStyle="width:120px;" cssClass="dojoComboBox" loadOnTextChange="true" loadMinimumCount="3"/>
					</td>
					<td class="edi_text_03">
	            		<s:url id="fileNameList" action="listEdiFileName" />
		            	<sx:autocompleter id="ediFileNameId" keyName="ediFileKeyName" name="ediItem.ediFileName" 
		            		href="%{fileNameList}" dataFieldName="ediFileNameSearchResult"  
		            		cssStyle="width:120px;" cssClass="dojoComboBox" loadOnTextChange="true" loadMinimumCount="3"/>
					</td>
					<td class="edi_text_03">
						<s:select cssClass="text_01_combo_big" cssStyle="width:154px;" listKey="id" listValue="name" 
							name="ediItem.carrierId" headerKey="-1"  list="#session.CARRIERS" 
							id="firstBox" />
					</td>	
					<td class="edi_text_03">
						<s:select value="%{ediItem.statusText}" id="statusText"  cssClass="text_01_combo_medium" cssStyle="width:100px;"
							name="ediItem.statusText" list="#session.EDI_STATUS_LIST" theme="simple" />
					</td>					
				</tr>
								
			</table>
			</div>
</div>
<div id="edi_tab"><br/></div>
	<div id="edi_res"><mmr:message messageId="label.search.results"/></div>
	<div id="edi_results">	
	<s:if test="%{ediItemList.size()>1}">
	<div id="edi_rslt_stmnt"><br/><s:property value="ediItemList.size()" /><mmr:message messageId="label.search.results.items"/></div>
	</s:if>
	<s:elseif test="%{ediItemList.size()==1}">
	<div id="edi_rslt_stmnt"><br/><s:property value="ediItemList.size()" /><mmr:message messageId="label.search.results.item"/></div>
	</s:elseif>
	<s:else>
	<div id="edi_rslt_stmnt"><br/><mmr:message messageId="label.search.results.noitems"/></div>
	</s:else>
	</div>
	<div id="edi_result_tbl">
			<table width="96%" border="0" cellpadding="5" cellspacing="0"  style="margin-top:2px;">							
				<tr>
				<tr>
						<!--    <td class="tableTitle2"><s:checkbox name="check_uncheck" value="yesno" id="selectall" onclick="checkUncheck('check_uncheck_row');"/></td> -->
								<td class="tableTitle2"><strong><mmr:message messageId="label.shippingOrder.id"/></strong></td>
								<td class="tableTitle2"><strong><mmr:message messageId="label.track.carrier"/></strong></td>
								<td class="tableTitle2"><strong><mmr:message messageId="label.rightmenu.account"/></strong></td>
								<td class="tableTitle2"><strong><mmr:message messageId="menu.admin.invoice"/></strong></td>
								<td class="tableTitle2"><strong><mmr:message messageId="label.invoice.date"/></strong></td>
								<td class="tableTitle2"><strong><mmr:message messageId="label.processed.date"/></strong></td>
								<td class="tableTitle2"><strong><mmr:message messageId="label.invoice.amount"/></strong></td>
								<td class="tableTitle2"><strong><mmr:message messageId="label.detail.seq.number"/></strong></td>
								<td class="tableTitle2"><strong><mmr:message messageId="label.edi.filename"/></strong></td>
								<td class="tableTitle2"><strong><mmr:message messageId="label.status"/></strong></td>
								<td class="tableTitle2"><strong><mmr:message messageId="label.message"/></strong></td>
								<td class="tableTitle2"></td>
								<td class="tableTitle2"></td>
								<td class="tableTitle2"></td>
							</tr>					
						<s:iterator id="ediTable" value="ediItemList" status="row">
							
							<tr>
							<s:if test="#row.even == true">	
						<!--	<td class="even"><s:checkbox name="check_uncheck_row" value="yesno" id="selecteach" /></td> -->							
								<td class="even"><s:property value="id"/></td>
								<td class="even"><s:property value="%{getCarrierName(carrierId)}"/></td>
								<td class="even"><s:property value="accountNumber"/></td>
								<td class="even"><s:property value="invoiceNumber"/></td>
								<td class="even"><s:property value="invoiceDate"/></td>
								<td class="even"><s:property value="processedDate"/></td>
								<td class="even"><s:property value="totInvoiceAmount"/></td>
								<td class="even"><s:property value="detailSeqNumber"/></td>
								<td class="even"><s:property value="ediFileName"/></td>
								<td class="even"><s:property value="%{getEdiItemStatus(status)}"/></td>
								<td class="even"><s:property value="%{getMessage(message)}"/></td>
					            <!--
								<td class="even">
					            	<s:if test="%{status == 3}" >
					            		<s:a onclick="return confirm('Do you really want to release charges for %{invoiceNumber} invoice?')"  href="release.edi.invoice.action?method=releaseEdiInvoice&ediItemId=%{id}&invoiceNumber=%{invoiceNumber}"> 
					            		<img src="<s:url value="/mmr/images/edit_pencil.png" includeContext="true" />" alt="Release" border="0"></s:a>
					            	</s:if>
				            	</td>	
				            	-->
					            <td class="even">
				            		<s:a href="autolinked.shipment.action?method=autolinkedShipment&ediItemId=%{id}&ediInvoiceNumber=%{invoiceNumber}"> 
				            		<img src="<s:url value="/mmr/images/icon_link.png" includeContext="true" />" alt="Autolinked" border="0"></s:a>
				            	</td>	
					            <td class="even">
			            			<s:a href="unlinked.shipment.action?method=unlinkedShipment&ediItemId=%{id}&ediInvoiceNumber=%{invoiceNumber}"> 
			            			<img src="<s:url value="/mmr/images/icon_unlink.png" includeContext="true" />" alt="Unlinked" border="0"></s:a>
			            		</td>
			            		</s:if>
			            		<s:else>
			            	<!--	<td class="odd"><s:checkbox name="check_uncheck_row" value="yesno" /></td> -->
			            			<td class="odd"><s:property value="id"/></td>
									<td class="odd"><s:property value="%{getCarrierName(carrierId)}"/></td>
									<td class="odd"><s:property value="accountNumber"/></td>
									<td class="odd"><s:property value="invoiceNumber"/></td>
									<td class="odd"><s:property value="invoiceDate"/></td>
									<td class="odd"><s:property value="processedDate"/></td>
									<td class="odd"><s:property value="totInvoiceAmount"/></td>
									<td class="odd"><s:property value="detailSeqNumber"/></td>
									<td class="odd"><s:property value="ediFileName"/></td>
									<td class="odd"><s:property value="%{getEdiItemStatus(status)}"/></td>
									<td class="odd"><s:property value="%{getMessage(message)}"/></td>
					            <!--
								<td class="odd">
					            	<s:if test="%{status == 3}" >
					            		<s:a onclick="return confirm('Do you really want to release charges for %{invoiceNumber} invoice?')"  href="release.edi.invoice.action?method=releaseEdiInvoice&ediItemId=%{id}&invoiceNumber=%{invoiceNumber}"> 
					            		<img src="<s:url value="/mmr/images/edit_pencil.png" includeContext="true" />" alt="Release" border="0"></s:a>
					            	</s:if>
				            	</td>
				            	-->
					            <td class="odd">
				            		<s:a href="autolinked.shipment.action?method=autolinkedShipment&ediItemId=%{id}&ediInvoiceNumber=%{invoiceNumber}"> 
				            		<img src="<s:url value="/mmr/images/icon_link.png" includeContext="true" />" alt="Autolinked" border="0"></s:a>
				            	</td>	
					            <td class="odd">
			            			<s:a href="unlinked.shipment.action?method=unlinkedShipment&ediItemId=%{id}&ediInvoiceNumber=%{invoiceNumber}"> 
			            			<img src="<s:url value="/mmr/images/icon_unlink.png" includeContext="true" />" alt="Unlinked" border="0"></s:a>
			            		</td>
			            		</s:else>				            	
							</tr>
						</s:iterator>
				</tr>
			</table>
		</div>
	<div id="edi_res_tbl_end"></div>
	</s:form>
</div>
</div>
			