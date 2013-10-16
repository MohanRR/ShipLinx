<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib prefix="mmr" uri="/mmr-tags"%>
<div id="comment_table">
<div id="add_info_shipping_tab"><br/></div>
	<div id="add_info_shipping_res"><mmr:message messageId="label.search.results"/></div>
	<div id="add_info_shipping_results">	
	<s:if test="%{loggedList.size()>1}">
	<div id="add_info_shipping_rslt_stmnt"><br/><s:property value="loggedList.size()" /><mmr:message messageId="label.search.results.items"/>
	</div>
	</s:if>
	<s:elseif test="%{loggedList.size()==1}">
	<div id="add_info_shipping_rslt_stmnt"><br/><s:property value="loggedList.size()" /><mmr:message messageId="label.search.results.item"/>
	</div>
	</s:elseif>
	<s:else>
	<div id="add_info_shipping_rslt_stmnt"><br/><mmr:message messageId="label.search.results.noitems"/>
	</div>
	</s:else>
	</div>
	
	
<div id="add_info_shipping_result_tbl">		
		<display:table id="comment" uid="row" name="loggedList" pagesize="100" export="true" cellspacing="0" cellpadding="0" class="srch_tbl_ais">
			<display:column headerClass="tableTitle2_product_dt" property="eventDateTime" format="{0,date,MMM dd HH:mm}" title="Date Time" style="width:10%; text-align:center;"/>
			<display:column headerClass="tableTitle2_product_msg" property="message" title="Message" maxLength="50" style="width:30%; text-align:center;"/>
			<display:column headerClass="tableTitle2_product_msg" property="systemLog" title="Log" maxLength="50" style="width:30%;text-align:center;"/>
			<s:if test="%{#session.ROLE.contains('busadmin')}">
			<display:column headerClass="tableTitle2_product_upby" property="eventUsername" title="Updated By" maxLength="10" style="width:10%;text-align:center;"/>
			<display:column headerClass="tableTitle2_product_bit" title="Private" style="width:5%;text-align:center;">
			<s:if test="%{#attr.row.privateMessage != true}">
				<img src="<s:url value="/mmr/images/cross.png" includeContext="true" />" alt="Cross" border="0">
			</s:if>
			<s:else>
				<img src="<s:url value="/mmr/images/tick.png" includeContext="true" />" alt="Tick" border="0">
			</s:else>
			</display:column>
			<display:column headerClass="tableTitle2_product_bit" title="Delete" style="width:5%;text-align:center;">
			<s:if test="%{#attr.row.deletedMessage != true}">
				<s:a onclick="return confirm('Do you really want to delete the selected Product?')" href="Javascript: deleteComment(%{#attr.row.Id});">
					<img src="<s:url value="/mmr/images/delete.gif" includeContext="true" />" alt="Customer Account Info" border="0"> </s:a>
			</s:if>
			<s:else>
					<img src="<s:url value="/mmr/images/tick.png" includeContext="true" />" alt="Tick" border="0">
			</s:else>
			</display:column>
			</s:if>
		</display:table>
</div>
<div id="add_info_shipping_res_tbl_end"></div>
</div>
