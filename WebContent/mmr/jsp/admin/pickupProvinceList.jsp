<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<body>
<s:select key="pickup.address.provinceCode" name="pickup.address.provinceCode"  
		cssClass="text_01_combo_big" cssStyle="width:135px;" value="%{pickup.address.provinceCode}" 
		listKey="provinceCode" listValue="provinceName" list="#session.provinces"/>
</body>
</html>