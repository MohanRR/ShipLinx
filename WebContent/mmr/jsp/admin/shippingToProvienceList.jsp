<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<body>
	<s:set name="cName" value="%{#session.shippingOrder.toAddress.countryCode}"/>
	<s:if test ='%{#cName ==  "CA"}'>
	    <s:select key="shippingOrder.state" name="shippingOrder.toAddress.provinceCode"  cssClass="text_02" 
	    cssStyle="width:158px;" listKey="provinceCode" listValue="provinceName" list="#session.Toprovinces"/>
   </s:if>
   	<s:elseif test ='%{#cName ==  "US"}'>
	     <s:select key="shippingOrder.state" name="shippingOrder.toAddress.provinceCode"  cssClass="text_02" 
	   	 cssStyle="width:158px;" listKey="provinceCode" listValue="provinceName" list="#session.Toprovinces"/>
   </s:elseif>
   <s:else>
   		<s:textfield size="20" key="shippingOrder.state" name="shippingOrder.toAddress.provinceCode"  cssClass="text_02_tf" value="%{shippingOrder.toAddress.provinceCode}"/>
   </s:else>
   
</body>
</html>