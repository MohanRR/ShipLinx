<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib prefix="mmr" uri="/mmr-tags" %>

<div id="srchaddrss_tab">&nbsp;</div>
	<div id="srchaddrss_res"><mmr:message messageId="label.search.results"/></div>
	<div id="srchaddrss_results">	
	<s:if test="%{addressList.size()>1}">
	<div id="rslt_stmnt"><br/><s:property value="addressList.size()" /><mmr:message messageId="label.search.results.items"/></div>
	</s:if>
	<s:elseif test="%{addressList.size()==1}">
	<div id="rslt_stmnt"><br/><s:property value="addressList.size()" /><mmr:message messageId="label.search.results.item"/></div>
	</s:elseif>
	<s:else>
	<div id="rslt_stmnt"><br/><mmr:message messageId="label.search.results.noitems"/></div>
	</s:else>
	</div>	
	
<div id="srchaddrss_result_tbl">
	<display:table id="addresstable"  name="addressList" export="false" uid="row"  sort="list" requestURI="/search.address2.action" cellspacing="0">

	<display:setProperty name="paging.banner.items_name" value=""></display:setProperty>
	<display:setProperty name="paging.banner.some_items_found" value=""/>
	<display:setProperty name="paging.banner.all_items_found" value=""></display:setProperty>
	<display:setProperty name="paging.banner.placement" value="bottom"></display:setProperty>
	<display:setProperty name="paging.banner.group_size" value="3"></display:setProperty>

		<s:set name="addressId" value="addressList[#attr.row_rowNum-1].getAddressId()" />
		<display:column headerClass="tableTitle2_addrss_img">
			 <s:a href="edit.address.action?addressid=%{#addressId}"> 
			 <img src="<s:url value="/mmr/images/edit_pencil.png" includeContext="true" />" alt="Edit Address" border="0"> </s:a>
		</display:column>
		<display:column headerClass="tableTitle2_addrss_img">
            <s:a onclick="return confirm('Do you really want to delete the selected address?')" href="delete.address.action?addressid=%{#addressId}"> 
			<img src="<s:url value="/mmr/images/delete.gif" includeContext="true" />" alt="Delete Address" border="0"> 
			</s:a>
		</display:column>
		<display:column headerClass="tableTitle2" property="abbreviationName"  sortable="true" title="Company" />
		<display:column headerClass="tableTitle2" property="city"  sortable="true" title="City" />
		<display:column headerClass="tableTitle2" property="provinceCode"  sortable="true" title="State / Province" />
		<display:column headerClass="tableTitle2" property="contactName"  sortable="true" title="Contact Name" />
		<display:column headerClass="tableTitle2" property="emailAddress"  sortable="true" title="Email" />
		<display:column headerClass="tableTitle2" property="postalCode"  sortable="true" title="Zip Code" />
	</display:table>
</div>

<div id="srchaddrss_res_tbl_end"></div>
