<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<body>
<s:select key="customer.address.provinceCode" name="customer.address.provinceCode"  
		cssClass="text_01_combo_big" cssStyle="width:135px;" value="%{customer.address.provinceCode}" 
		listKey="provinceCode" listValue="provinceName" list="#session.provinces"/>
</body>
</html>